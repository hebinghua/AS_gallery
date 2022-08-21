package com.miui.gallery.net.library;

import android.net.Uri;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import com.miui.gallery.assistant.library.LibraryItem;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.download.GalleryDownloadManager;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.net.download.Verifier;
import com.miui.gallery.net.resource.DownloadInfo;
import com.miui.gallery.net.resource.DownloadRequest;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miuix.net.ConnectivityHelper;

/* loaded from: classes2.dex */
public class LibraryDownloadManager {
    public Map<Long, List<Request>> mRequestMap = Collections.synchronizedMap(new HashMap());
    public Map<Long, List<LibraryManager.DownloadListener>> mDownloadListenerMap = Collections.synchronizedMap(new HashMap());
    public Set<Long> mDownloadingItems = Collections.synchronizedSet(new HashSet());

    public boolean isLibraryDownloading(long j) {
        return this.mRequestMap.containsKey(Long.valueOf(j));
    }

    public synchronized void download(final long j, boolean z, Set<LibraryItem> set, LibraryManager.DownloadListener downloadListener) {
        if (!checkCondition(z)) {
            if (downloadListener != null) {
                downloadListener.onDownloadResult(j, 1);
            }
            return;
        }
        addListener(j, downloadListener);
        if (this.mRequestMap.containsKey(Long.valueOf(j))) {
            return;
        }
        ArrayList arrayList = new ArrayList(set.size());
        this.mRequestMap.put(Long.valueOf(j), arrayList);
        final HashSet hashSet = new HashSet(set);
        Iterator<LibraryItem> it = set.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            final LibraryItem next = it.next();
            if (!this.mDownloadingItems.contains(Long.valueOf(next.getId()))) {
                Request download = download(j, next, z, new Request.Listener() { // from class: com.miui.gallery.net.library.LibraryDownloadManager.1
                    @Override // com.miui.gallery.net.download.Request.Listener
                    public void onStart() {
                    }

                    @Override // com.miui.gallery.net.download.Request.Listener
                    public void onProgressUpdate(final int i) {
                        ThreadManager.getRequestPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.net.library.LibraryDownloadManager.1.1
                            @Override // com.miui.gallery.concurrent.ThreadPool.Job
                            /* renamed from: run */
                            public Object mo1807run(ThreadPool.JobContext jobContext) {
                                AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                LibraryDownloadManager.this.downloadProgress(j, i);
                                return null;
                            }
                        });
                    }

                    @Override // com.miui.gallery.net.download.Request.Listener
                    public void onComplete(final int i) {
                        ThreadManager.getRequestPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.net.library.LibraryDownloadManager.1.2
                            @Override // com.miui.gallery.concurrent.ThreadPool.Job
                            /* renamed from: run */
                            public Object mo1807run(ThreadPool.JobContext jobContext) {
                                AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                LibraryDownloadManager.this.downloadComplete(i, next, hashSet, j);
                                return null;
                            }
                        });
                    }
                });
                if (download != null) {
                    arrayList.add(download);
                    this.mDownloadingItems.add(Long.valueOf(next.getId()));
                } else {
                    downloadResult(j, 1);
                    break;
                }
            }
        }
    }

    public final synchronized void addListener(long j, LibraryManager.DownloadListener downloadListener) {
        List<LibraryManager.DownloadListener> list = this.mDownloadListenerMap.get(Long.valueOf(j));
        if (list == null) {
            list = new ArrayList<>();
            this.mDownloadListenerMap.put(Long.valueOf(j), list);
        }
        list.add(downloadListener);
    }

    public final boolean isDownloadItemsExists(Set<LibraryItem> set, long j) {
        if (BaseMiscUtil.isValid(set)) {
            for (LibraryItem libraryItem : set) {
                if (!libraryItem.isExist(j)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public void cancel(long j) {
        downloadResult(j, 2);
    }

    public final Request download(long j, LibraryItem libraryItem, boolean z, Request.Listener listener) {
        File file = new File(libraryItem.getTargetPath(GalleryApp.sGetAndroidContext(), j));
        try {
            Object[] executeSync = new DownloadRequest(libraryItem.getId()).executeSync();
            if (executeSync == null || executeSync.length <= 0) {
                return null;
            }
            Request request = new Request(Uri.parse(((DownloadInfo) executeSync[0]).url), file);
            request.setVerifier(new Verifier.Sha1(libraryItem.getSha1()));
            request.setAllowedOverMetered(z);
            request.setListener(listener);
            GalleryDownloadManager.INSTANCE.enqueue(request);
            return request;
        } catch (RequestError e) {
            DefaultLogger.e("LibraryDownloadManager", "fetch library item info error:" + e);
            e.printStackTrace();
            return null;
        }
    }

    public final synchronized void downloadResult(long j, int i) {
        List<Request> list = this.mRequestMap.get(Long.valueOf(j));
        if (list != null) {
            if (i != 0) {
                for (Request request : list) {
                    GalleryDownloadManager.INSTANCE.cancel(request);
                }
            }
            list.clear();
        }
        List<LibraryManager.DownloadListener> list2 = this.mDownloadListenerMap.get(Long.valueOf(j));
        if (BaseMiscUtil.isValid(list2)) {
            for (LibraryManager.DownloadListener downloadListener : list2) {
                downloadListener.onDownloadResult(j, i);
            }
            list2.clear();
        }
        this.mRequestMap.remove(Long.valueOf(j));
        this.mDownloadListenerMap.remove(Long.valueOf(j));
    }

    public final synchronized void downloadProgress(long j, int i) {
        List<LibraryManager.DownloadListener> list = this.mDownloadListenerMap.get(Long.valueOf(j));
        if (BaseMiscUtil.isValid(list)) {
            for (LibraryManager.DownloadListener downloadListener : list) {
                downloadListener.onDownloadProgress(j, i);
            }
        }
    }

    public final synchronized void downloadComplete(int i, LibraryItem libraryItem, Set<LibraryItem> set, long j) {
        this.mDownloadingItems.remove(Long.valueOf(libraryItem.getId()));
        if (i == 0) {
            DefaultLogger.d("LibraryDownloadManager", "Library " + libraryItem.getName() + " download success!");
            set.remove(libraryItem);
            if (set.isEmpty() || isDownloadItemsExists(set, j)) {
                downloadResult(j, 0);
            }
        } else {
            if (i == 6) {
                GalleryPreferences.Assistant.setForceRefreshLibraryInfo(true, j);
            }
            DefaultLogger.d("LibraryDownloadManager", "Library " + libraryItem.getName() + " download failed!");
            downloadResult(j, 1);
        }
    }

    public static boolean checkCondition(boolean z) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.e("LibraryDownloadManager", "CTA not confirmed");
            return false;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.e("LibraryDownloadManager", "No network");
            return false;
        } else if (!z && !ConnectivityHelper.getInstance(StaticContext.sGetAndroidContext()).isUnmeteredNetworkConnected()) {
            DefaultLogger.e("LibraryDownloadManager", "No unmetered network connected");
            return false;
        } else if (haveEnoughSpace()) {
            return true;
        } else {
            DefaultLogger.e("LibraryDownloadManager", "No enough space");
            return false;
        }
    }

    public static boolean haveEnoughSpace() {
        try {
            if (LibraryConstantsHelper.getSpecificDirForLibrary(0L).getFreeSpace() > 104857600) {
                return true;
            }
            DefaultLogger.e("LibraryDownloadManager", "Sd card has less than 100M space left");
            return false;
        } catch (Exception e) {
            DefaultLogger.e("LibraryDownloadManager", e);
            return false;
        }
    }
}
