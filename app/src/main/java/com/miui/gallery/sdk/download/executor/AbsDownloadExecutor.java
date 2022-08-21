package com.miui.gallery.sdk.download.executor;

import android.accounts.Account;
import android.net.Uri;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.downloader.IDownloader;
import com.miui.gallery.sdk.download.listener.DownloadListener;
import com.miui.gallery.sdk.download.listener.DownloadProgressListener;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class AbsDownloadExecutor {
    public DownloadListenerController mListenerController = new DownloadListenerController();
    public DownloadCommandQueue mQueue;

    public abstract void dispatch();

    public abstract String getTag();

    public abstract void interrupt();

    public AbsDownloadExecutor(int i, int i2) {
        this.mQueue = new DownloadCommandQueue(i, i2, getTag());
    }

    public final DownloadItem repackageItem(DownloadItem downloadItem) {
        return new DownloadItem.Builder().cloneFrom(downloadItem).setDownloadListener(this.mListenerController.getDownloadListener()).setProgressListener(this.mListenerController.getDownloadProgressListener()).build();
    }

    public void download(DownloadItem downloadItem, boolean z, boolean z2) {
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e(getTag(), "invoke: no account");
            DownloadItem.callbackError(downloadItem, new DownloadFailReason(ErrorCode.NO_ACCOUNT, "no account", null));
        } else if (downloadItem == null || downloadItem.getKey() == null) {
            DefaultLogger.e(getTag(), "invoke: invalid download item");
            DownloadItem.callbackError(downloadItem, new DownloadFailReason(ErrorCode.PARAMS_ERROR, "key is null", null));
        } else {
            if (this.mListenerController.putItem(downloadItem)) {
                DownloadItem.callbackStarted(downloadItem);
            }
            DownloadCommand downloadCommand = new DownloadCommand(account, repackageItem(downloadItem));
            boolean z3 = false;
            boolean z4 = true;
            if (z2) {
                int contains = this.mQueue.contains(downloadCommand.getKey());
                if (contains == 1) {
                    return;
                }
                interrupt();
                if (contains == 0) {
                    this.mQueue.remove(downloadCommand.getKey());
                }
                if (this.mQueue.getPendingSize() > 0) {
                    z3 = true;
                }
            }
            if (this.mQueue.put(downloadCommand, z) <= 0) {
                z4 = z3;
            }
            if (!z4) {
                return;
            }
            dispatch();
        }
    }

    public boolean cancel(DownloadItem downloadItem) {
        return this.mQueue.cancel(downloadItem.getKey()) != -1;
    }

    public void cancelAll() {
        this.mQueue.cancelAll();
    }

    public boolean contains(DownloadItem downloadItem) {
        return this.mQueue.contains(downloadItem.getKey()) != -1;
    }

    public DownloadItem peek(DownloadItem downloadItem) {
        DownloadCommand downloadCommand = this.mQueue.get(downloadItem.getKey());
        if (downloadCommand != null) {
            return downloadCommand.getItem();
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class DownloadListenerController {
        public final DownloadListener mDownloadListener;
        public final HashMap<String, HashSet<DownloadListener>> mDownloadListeners;
        public final DownloadProgressListener mProgressListener;
        public final HashMap<String, HashSet<DownloadProgressListener>> mProgressListeners;

        /* loaded from: classes2.dex */
        public interface Caller<P> {
            void call(P p);
        }

        public DownloadListenerController() {
            this.mDownloadListeners = new HashMap<>();
            this.mProgressListeners = new HashMap<>();
            this.mDownloadListener = new DownloadListener() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.1
                @Override // com.miui.gallery.sdk.download.listener.DownloadListener
                public void onDownloadStarted(Uri uri, DownloadType downloadType) {
                    DownloadListenerController.this.callStarted(uri, downloadType);
                }

                @Override // com.miui.gallery.sdk.download.listener.DownloadListener
                public void onDownloadSuccess(Uri uri, DownloadType downloadType, DownloadedItem downloadedItem) {
                    DownloadListenerController.this.callSuccess(uri, downloadType, downloadedItem);
                }

                @Override // com.miui.gallery.sdk.download.listener.DownloadListener
                public void onDownloadFail(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
                    DownloadListenerController.this.callFail(uri, downloadType, downloadFailReason);
                }

                @Override // com.miui.gallery.sdk.download.listener.DownloadListener
                public void onDownloadCancel(Uri uri, DownloadType downloadType) {
                    DownloadListenerController.this.callCancel(uri, downloadType);
                }
            };
            this.mProgressListener = new DownloadProgressListener() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.2
                @Override // com.miui.gallery.sdk.download.listener.DownloadProgressListener
                public void onDownloadProgress(Uri uri, DownloadType downloadType, long j, long j2) {
                    DownloadListenerController.this.callProgress(uri, downloadType, j, j2);
                }
            };
        }

        public final <E> LinkedList<E> getListeners(HashMap<String, HashSet<E>> hashMap, String str) {
            LinkedList<E> linkedList = new LinkedList<>();
            HashSet<E> hashSet = hashMap.get(str);
            if (hashSet != null) {
                Iterator<E> it = hashSet.iterator();
                while (it.hasNext()) {
                    linkedList.add(it.next());
                }
            }
            return linkedList;
        }

        public final <E> void dispatchListener(LinkedList<E> linkedList, Caller<E> caller) {
            if (linkedList != null) {
                Iterator<E> it = linkedList.iterator();
                while (it.hasNext()) {
                    E next = it.next();
                    if (next != null) {
                        caller.call(next);
                    }
                }
            }
        }

        public final LinkedList<DownloadListener> getDownloadListeners(String str, boolean z) {
            LinkedList<DownloadListener> listeners;
            synchronized (this.mDownloadListeners) {
                listeners = getListeners(this.mDownloadListeners, str);
                if (z) {
                    this.mDownloadListeners.remove(str);
                }
            }
            if (z) {
                synchronized (this.mProgressListeners) {
                    this.mProgressListeners.remove(str);
                }
            }
            return listeners;
        }

        public final LinkedList<DownloadProgressListener> getProgressListeners(String str) {
            LinkedList<DownloadProgressListener> listeners;
            synchronized (this.mProgressListeners) {
                listeners = getListeners(this.mProgressListeners, str);
            }
            return listeners;
        }

        public final void callStarted(final Uri uri, final DownloadType downloadType) {
            dispatchListener(getDownloadListeners(DownloadUtil.generateKey(uri, downloadType), false), new Caller<DownloadListener>() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.3
                @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.Caller
                public void call(DownloadListener downloadListener) {
                    downloadListener.onDownloadStarted(uri, downloadType);
                }
            });
        }

        public final void callSuccess(final Uri uri, final DownloadType downloadType, final DownloadedItem downloadedItem) {
            dispatchListener(getDownloadListeners(DownloadUtil.generateKey(uri, downloadType), true), new Caller<DownloadListener>() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.4
                @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.Caller
                public void call(DownloadListener downloadListener) {
                    downloadListener.onDownloadSuccess(uri, downloadType, downloadedItem);
                }
            });
        }

        public final void callFail(final Uri uri, final DownloadType downloadType, final DownloadFailReason downloadFailReason) {
            dispatchListener(getDownloadListeners(DownloadUtil.generateKey(uri, downloadType), true), new Caller<DownloadListener>() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.5
                @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.Caller
                public void call(DownloadListener downloadListener) {
                    downloadListener.onDownloadFail(uri, downloadType, downloadFailReason);
                }
            });
        }

        public final void callCancel(final Uri uri, final DownloadType downloadType) {
            dispatchListener(getDownloadListeners(DownloadUtil.generateKey(uri, downloadType), true), new Caller<DownloadListener>() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.6
                @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.Caller
                public void call(DownloadListener downloadListener) {
                    downloadListener.onDownloadCancel(uri, downloadType);
                }
            });
        }

        public final void callProgress(final Uri uri, final DownloadType downloadType, final long j, final long j2) {
            dispatchListener(getProgressListeners(DownloadUtil.generateKey(uri, downloadType)), new Caller<DownloadProgressListener>() { // from class: com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.7
                @Override // com.miui.gallery.sdk.download.executor.AbsDownloadExecutor.DownloadListenerController.Caller
                public void call(DownloadProgressListener downloadProgressListener) {
                    downloadProgressListener.onDownloadProgress(uri, downloadType, j, j2);
                }
            });
        }

        public DownloadListener getDownloadListener() {
            return this.mDownloadListener;
        }

        public DownloadProgressListener getDownloadProgressListener() {
            return this.mProgressListener;
        }

        public final <E> boolean putItem(HashMap<String, HashSet<E>> hashMap, String str, E e) {
            HashSet<E> hashSet = hashMap.get(str);
            if (hashSet == null) {
                hashSet = new HashSet<>();
                hashMap.put(str, hashSet);
            }
            return hashSet.add(e);
        }

        public boolean putItem(DownloadItem downloadItem) {
            boolean z = false;
            if (downloadItem != null) {
                String generateKey = DownloadUtil.generateKey(downloadItem.getUri(), downloadItem.getType());
                if (downloadItem.getDownloadListener() != null) {
                    synchronized (this.mDownloadListeners) {
                        z = false | putItem(this.mDownloadListeners, generateKey, downloadItem.getDownloadListener());
                    }
                }
                if (downloadItem.getProgressListener() != null) {
                    synchronized (this.mProgressListeners) {
                        z |= putItem(this.mProgressListeners, generateKey, downloadItem.getProgressListener());
                    }
                }
            }
            return z;
        }
    }

    public static Map<IDownloader, List<DownloadCommand>> classifyCommand(List<DownloadCommand> list) {
        if (BaseMiscUtil.isValid(list)) {
            HashMap hashMap = new HashMap();
            for (DownloadCommand downloadCommand : list) {
                IDownloader downloader = downloadCommand.getItem().getDownloader();
                if (downloader != null) {
                    List list2 = (List) hashMap.get(downloader);
                    if (list2 == null) {
                        list2 = new LinkedList();
                        hashMap.put(downloader, list2);
                    }
                    list2.add(downloadCommand);
                }
            }
            return hashMap;
        }
        return null;
    }
}
