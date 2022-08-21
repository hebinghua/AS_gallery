package com.miui.gallery.assistant.library;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.map.utils.MapLibraryLoaderHelper;
import com.miui.gallery.net.base.RequestError;
import com.miui.gallery.net.library.LibraryDownloadManager;
import com.miui.gallery.net.library.LibraryRequest;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ScreenSceneAlgorithmManager;
import com.xiaomi.micloudsdk.request.utils.CloudUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class LibraryManager {
    public static final String TAG = "LibraryManager";
    public final CountDownLatch mCountDownLatch;
    public final Map<Long, Library> mCurrentLibraries;
    @SuppressLint({"StaticFieldLeak"})
    public final AsyncTask<Context, Void, Void> mInitTask;
    public volatile boolean mInitialized;
    public volatile boolean mInitializing;
    public final LibraryDownloadManager mLibraryDownloadManager;
    public NetworkReceiver mNetworkReceiver;

    /* loaded from: classes.dex */
    public interface DownloadListener {
        void onDownloadProgress(long j, int i);

        void onDownloadResult(long j, int i);
    }

    public final boolean isMapLibrary(long j) {
        return 104702 == j;
    }

    public LibraryManager() {
        this.mInitTask = new AsyncTask<Context, Void, Void>() { // from class: com.miui.gallery.assistant.library.LibraryManager.1
            @Override // android.os.AsyncTask
            public Void doInBackground(Context... contextArr) {
                Context context = contextArr[0];
                LibraryManager.this.initAllLibrarys();
                LibraryManager.this.mInitializing = false;
                LibraryManager.this.mInitialized = true;
                LibraryManager.this.mCountDownLatch.countDown();
                if (!LibraryManager.this.tryDownloadAllLibrarys()) {
                    LibraryManager.this.registerNetObserver(context);
                }
                LibraryManager.this.tryLoadMapLibrary();
                return null;
            }
        };
        this.mCurrentLibraries = new ConcurrentHashMap();
        this.mLibraryDownloadManager = new LibraryDownloadManager();
        this.mCountDownLatch = new CountDownLatch(1);
    }

    public static LibraryManager getInstance() {
        return LibraryManagerHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static final class LibraryManagerHolder {
        public static final LibraryManager INSTANCE = new LibraryManager();
    }

    public synchronized void init(Context context) {
        if (!this.mInitialized && !this.mInitializing) {
            DefaultLogger.d(TAG, "init");
            this.mInitializing = true;
            this.mInitTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
        }
    }

    public synchronized boolean isInitialized() {
        return this.mInitialized;
    }

    public final void initAllLibrarys() {
        Long[] lArr = LibraryConstantsHelper.sAllLibraries;
        if (lArr == null) {
            return;
        }
        for (Long l : lArr) {
            long longValue = l.longValue();
            Library currentLibraryById = getCurrentLibraryById(longValue);
            if (currentLibraryById == null || ((GalleryPreferences.Assistant.isForceRefreshLibraryInfo(longValue) || currentLibraryById.isOverDue()) && !currentLibraryById.isLoaded())) {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    DefaultLogger.e(TAG, "CTA not confirmed");
                    return;
                } else if (!BaseNetworkUtils.isNetworkConnected()) {
                    DefaultLogger.e(TAG, "No network");
                    return;
                } else {
                    try {
                        Object[] executeSync = new LibraryRequest(longValue).executeSync();
                        if (executeSync != null && executeSync.length > 0 && (executeSync[0] instanceof Library)) {
                            refreshServerLibraryInfo((Library) executeSync[0]);
                            GalleryPreferences.Assistant.setForceRefreshLibraryInfo(false, longValue);
                        }
                    } catch (RequestError e) {
                        DefaultLogger.e(TAG, e);
                    }
                }
            }
            refreshLibraryStatusInternal(longValue);
        }
    }

    public final boolean tryDownloadAllLibrarys() {
        Long[] lArr = LibraryConstantsHelper.sAllLibraries;
        if (lArr == null) {
            return true;
        }
        if (!this.mInitialized || !LibraryDownloadManager.checkCondition(false)) {
            return false;
        }
        boolean z = true;
        for (Long l : lArr) {
            long longValue = l.longValue();
            Library library = getLibrary(longValue);
            if (library == null) {
                DefaultLogger.d(TAG, "Library %d is available or no download info,no need to download now", Long.valueOf(longValue));
            } else if (library.getLibraryStatus() == Library.LibraryStatus.STATE_NOT_DOWNLOADED) {
                DefaultLogger.d(TAG, String.format(Locale.US, "Library %d download when app start up Begin.", Long.valueOf(longValue)));
                downloadLibrary(library, false, new DownloadListener() { // from class: com.miui.gallery.assistant.library.LibraryManager.2
                    @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
                    public void onDownloadProgress(long j, int i) {
                    }

                    @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
                    public void onDownloadResult(long j, int i) {
                        DefaultLogger.d(LibraryManager.TAG, "Library %d download result:%d.", Long.valueOf(j), Integer.valueOf(i));
                        if (LibraryManager.this.isLibrarysExist(LibraryConstantsHelper.sStoryLibraries) && CloudUtils.getXiaomiAccount() == null) {
                            CardManager.getInstance().triggerGuaranteeScenarios(false);
                        }
                        if (!LibraryManager.this.isMapLibrary(j) || i != 0) {
                            return;
                        }
                        MapLibraryLoaderHelper.getInstance().startLoadLibrary();
                    }
                });
                z = false;
            } else if (isLibrarysExist(LibraryConstantsHelper.sAIModeScreenSceneLibraries)) {
                ScreenSceneAlgorithmManager.initAlgorithm();
            } else if (isLibrarysExist(LibraryConstantsHelper.sMapLibraries) && library.getLibraryStatus() == Library.LibraryStatus.STATE_AVAILABLE) {
                getInstance().loadLibrary(104702L);
            }
        }
        return z;
    }

    public final void tryLoadMapLibrary() {
        if (!MapInitializerImpl.checkMapAvailable()) {
            return;
        }
        Library library = getLibrary(104702L);
        if (!isLibrarysExist(LibraryConstantsHelper.sMapLibraries) || library.getLibraryStatus() != Library.LibraryStatus.STATE_AVAILABLE) {
            return;
        }
        getInstance().loadLibrary(104702L);
    }

    public boolean isLibrarysExist(Long[] lArr) {
        if (lArr == null || lArr.length <= 0) {
            return true;
        }
        for (Long l : lArr) {
            Library library = getLibrary(l.longValue());
            if (library == null || !(library.getLibraryStatus() == Library.LibraryStatus.STATE_AVAILABLE || library.getLibraryStatus() == Library.LibraryStatus.STATE_LOADED)) {
                return false;
            }
        }
        return true;
    }

    public final void registerNetObserver(Context context) {
        this.mNetworkReceiver = new NetworkReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE_IMMEDIATE");
        context.registerReceiver(this.mNetworkReceiver, intentFilter, null, ThreadManager.getWorkHandler());
    }

    public final void unRegisterNetObserver(Context context) {
        NetworkReceiver networkReceiver = this.mNetworkReceiver;
        if (networkReceiver != null) {
            context.unregisterReceiver(networkReceiver);
            this.mNetworkReceiver = null;
        }
    }

    public Library getLibrary(long j) {
        if (this.mInitialized) {
            return this.mCurrentLibraries.get(Long.valueOf(j));
        }
        return null;
    }

    public Library.LibraryStatus getLibraryIsDownload(long j) {
        return refreshLibraryStatusInternal(j);
    }

    public Library getLibrarySync(long j) {
        if (!this.mInitialized) {
            try {
                this.mCountDownLatch.await(1L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                DefaultLogger.e(TAG, e);
            }
            this.mCountDownLatch.countDown();
        }
        Library library = this.mCurrentLibraries.get(Long.valueOf(j));
        if (library == null) {
            try {
                Object[] executeSync = new LibraryRequest(j).executeSync();
                if (executeSync == null || executeSync.length <= 0 || !(executeSync[0] instanceof Library)) {
                    return library;
                }
                Library library2 = (Library) executeSync[0];
                try {
                    refreshServerLibraryInfo(library2);
                    refreshLibraryStatusInternal(j);
                    return library2;
                } catch (RequestError e2) {
                    e = e2;
                    library = library2;
                    DefaultLogger.e(TAG, e);
                    return library;
                }
            } catch (RequestError e3) {
                e = e3;
            }
        } else {
            return library;
        }
    }

    public final void refreshServerLibraryInfo(Library library) {
        if (library != null) {
            library.setRefreshTime(System.currentTimeMillis());
            long libraryId = library.getLibraryId();
            Library library2 = this.mCurrentLibraries.get(Long.valueOf(libraryId));
            this.mCurrentLibraries.put(Long.valueOf(libraryId), library);
            if (library2 == null) {
                GalleryEntityManager.getInstance().insert(library);
            } else {
                updateLibraryToDatabase(library);
            }
        }
    }

    public Future downloadLibrary(Library library, boolean z, final DownloadListener downloadListener) {
        if (library != null && !library.isLibraryItemInfosConsistent()) {
            final long libraryId = library.getLibraryId();
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.library.LibraryManager.3
                @Override // java.lang.Runnable
                public void run() {
                    DownloadListener downloadListener2 = downloadListener;
                    if (downloadListener2 != null) {
                        downloadListener2.onDownloadResult(libraryId, 1);
                    }
                }
            });
            return null;
        }
        return downloadLibraryInternal(library, z, downloadListener);
    }

    public final Future downloadLibraryInternal(final Library library, boolean z, final DownloadListener downloadListener) {
        DownloadListener downloadListener2 = new DownloadListener() { // from class: com.miui.gallery.assistant.library.LibraryManager.4
            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(final long j, final int i) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.library.LibraryManager.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        DownloadListener downloadListener3 = downloadListener;
                        if (downloadListener3 != null) {
                            downloadListener3.onDownloadProgress(j, i);
                        }
                    }
                });
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(final long j, final int i) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.library.LibraryManager.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        DownloadListener downloadListener3 = downloadListener;
                        if (downloadListener3 != null) {
                            downloadListener3.onDownloadResult(j, i);
                        }
                    }
                });
                String str = LibraryManager.TAG;
                DefaultLogger.d(str, "Download libs Result libraryId = %s, resultCode = %d", j + "", Integer.valueOf(i));
                if (i == 0) {
                    Library library2 = library;
                    if (library2 != null) {
                        library2.setLibraryStatus(Library.LibraryStatus.STATE_AVAILABLE);
                    }
                    if (LibraryManager.this.isLibrarysExist(LibraryConstantsHelper.sAIModeScreenSceneLibraries)) {
                        ScreenSceneAlgorithmManager.initAlgorithm();
                    }
                    if (LibraryManager.this.isMapLibrary(j)) {
                        LibraryManager.getInstance().loadLibrary(LibraryConstantsHelper.sMapLibraries);
                    }
                    DeleteLibraryWorker.schedule();
                    LibraryManager.this.recordDownloadResult(library, "success");
                } else if (i == 1) {
                    Library library3 = library;
                    if (library3 != null) {
                        library3.setLibraryStatus(Library.LibraryStatus.STATE_NOT_DOWNLOADED);
                    }
                    LibraryManager.this.recordDownloadResult(library, "cancel");
                } else if (i != 2) {
                } else {
                    Library library4 = library;
                    if (library4 != null) {
                        library4.setLibraryStatus(Library.LibraryStatus.STATE_NOT_DOWNLOADED);
                    }
                    LibraryManager.this.recordDownloadResult(library, "fail");
                }
            }
        };
        if (library != null) {
            library.setLibraryStatus(Library.LibraryStatus.STATE_DOWNLOADING);
            return ThreadManager.getRequestPool().submit(new LibraryDownloadJob(library, z, downloadListener2));
        }
        return null;
    }

    public final void recordDownloadResult(Library library, String str) {
        if (library != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("result", str);
            SamplingStatHelper.recordCountEvent("assistant", "library_download_result_" + library.getLibraryId(), hashMap);
        }
    }

    public boolean loadLibrary(long j) {
        return loadLibrary(new Long[]{Long.valueOf(j)});
    }

    public synchronized boolean loadLibrary(Long[] lArr) {
        if (lArr != null) {
            if (lArr.length > 0) {
                for (Long l : lArr) {
                    long longValue = l.longValue();
                    Library library = getLibrary(longValue);
                    if (library == null) {
                        return false;
                    }
                    Library.LibraryStatus libraryStatus = library.getLibraryStatus();
                    Library.LibraryStatus libraryStatus2 = Library.LibraryStatus.STATE_LOADED;
                    if (libraryStatus == libraryStatus2) {
                        DefaultLogger.d(TAG, "Library %d has been loaded, no need load again!", Long.valueOf(longValue));
                    } else if (library.getLibraryStatus() != Library.LibraryStatus.STATE_AVAILABLE) {
                        return false;
                    } else {
                        if (!getInstance().loadLibraryInternal(library)) {
                            return false;
                        }
                        library.setLibraryStatus(libraryStatus2);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public final boolean loadLibraryInternal(Library library) {
        return library != null && (library.isLoaded() || library.load());
    }

    public final Library getCurrentLibraryById(long j) {
        Library library = this.mCurrentLibraries.get(Long.valueOf(j));
        if (library == null && (library = (Library) GalleryEntityManager.getInstance().find(Library.class, String.valueOf(j))) != null) {
            this.mCurrentLibraries.put(Long.valueOf(j), library);
        }
        return library;
    }

    public final void updateLibraryToDatabase(Library library) {
        if (library != null) {
            ContentValues contentValues = new ContentValues();
            library.onConvertToContents(contentValues);
            GalleryEntityManager.getInstance().update(Library.class, contentValues, String.format("%s=%s", "libraryId", Long.valueOf(library.getLibraryId())), null);
        }
    }

    public final Library.LibraryStatus refreshLibraryStatusInternal(long j) {
        Library library = this.mCurrentLibraries.get(Long.valueOf(j));
        if (library == null) {
            return Library.LibraryStatus.STATE_NO_LIBRARY_INFO;
        }
        if (library.isExist()) {
            library.setLibraryStatus(library.isLoaded() ? Library.LibraryStatus.STATE_LOADED : Library.LibraryStatus.STATE_AVAILABLE);
        } else if (this.mLibraryDownloadManager.isLibraryDownloading(library.getLibraryId())) {
            library.setLibraryStatus(Library.LibraryStatus.STATE_DOWNLOADING);
        } else {
            library.setLibraryStatus(Library.LibraryStatus.STATE_NOT_DOWNLOADED);
        }
        return library.getLibraryStatus();
    }

    /* loaded from: classes.dex */
    public class LibraryDownloadJob implements ThreadPool.Job {
        public final boolean mAllowedOverMetered;
        public final Set<LibraryItem> mDownloadSet = new HashSet();
        public final Library mLibrary;
        public final DownloadListener mLibraryDownloadListener;

        public LibraryDownloadJob(Library library, boolean z, DownloadListener downloadListener) {
            this.mLibrary = library;
            this.mAllowedOverMetered = z;
            this.mLibraryDownloadListener = downloadListener;
        }

        public final void cancel() {
            LibraryManager.this.mLibraryDownloadManager.cancel(this.mLibrary.getLibraryId());
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Object mo1807run(ThreadPool.JobContext jobContext) {
            if (this.mLibrary.getLibraryItems() != null) {
                for (LibraryItem libraryItem : this.mLibrary.getLibraryItems()) {
                    if (!libraryItem.isExist(this.mLibrary.getLibraryId())) {
                        this.mDownloadSet.add(libraryItem);
                    }
                }
                if (this.mDownloadSet.isEmpty()) {
                    this.mLibraryDownloadListener.onDownloadResult(this.mLibrary.getLibraryId(), 0);
                } else if (!jobContext.isCancelled()) {
                    LibraryManager.this.mLibraryDownloadManager.download(this.mLibrary.getLibraryId(), this.mAllowedOverMetered, this.mDownloadSet, this.mLibraryDownloadListener);
                } else {
                    this.mLibraryDownloadListener.onDownloadResult(this.mLibrary.getLibraryId(), 2);
                }
            } else {
                this.mLibraryDownloadListener.onDownloadResult(this.mLibrary.getLibraryId(), 1);
            }
            jobContext.setCancelListener(new ThreadPool.CancelListener() { // from class: com.miui.gallery.assistant.library.LibraryManager.LibraryDownloadJob.1
                @Override // com.miui.gallery.concurrent.ThreadPool.CancelListener
                public void onCancel() {
                    LibraryDownloadJob.this.cancel();
                }
            });
            return null;
        }
    }

    /* loaded from: classes.dex */
    public class NetworkReceiver extends BroadcastReceiver {
        public boolean mIsNetConnected = BaseNetworkUtils.isNetworkConnected();
        public boolean mIsWifiConnected = !BaseNetworkUtils.isActiveNetworkMetered();

        public NetworkReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean z;
            boolean z2 = false;
            boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
            boolean z3 = true;
            boolean z4 = !booleanExtra;
            if (this.mIsNetConnected != z4) {
                DefaultLogger.d(LibraryManager.TAG, "NetworkReceiver lastConnect: %s, netConnect: %s", Boolean.valueOf(this.mIsNetConnected), Boolean.valueOf(z4));
                this.mIsNetConnected = z4;
                z = true;
            } else {
                z = false;
            }
            if (this.mIsNetConnected && !BaseNetworkUtils.isActiveNetworkMetered()) {
                z2 = true;
            }
            if (this.mIsWifiConnected != z2) {
                DefaultLogger.d(LibraryManager.TAG, "NetworkReceiver lastWifiConnect: %s, wifiConnect: %s", Boolean.valueOf(this.mIsWifiConnected), Boolean.valueOf(z2));
                this.mIsWifiConnected = z2;
            } else {
                z3 = z;
            }
            if (!z3 || !this.mIsWifiConnected || !LibraryManager.this.tryDownloadAllLibrarys()) {
                return;
            }
            LibraryManager.this.unRegisterNetObserver(GalleryApp.sGetAndroidContext());
        }
    }
}
