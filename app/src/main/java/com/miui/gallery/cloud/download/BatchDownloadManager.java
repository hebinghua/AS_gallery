package com.miui.gallery.cloud.download;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.download.DownloadObserver;
import com.miui.gallery.cloud.jobs.SyncJobScheduler;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadFailReason;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.sdk.download.listener.DownloadListener;
import com.miui.gallery.sdk.download.util.DownloadUtil;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public final class BatchDownloadManager implements Handler.Callback, DownloadObserver.OnConditionChangeListener {
    public static final int[] COUNT_STAGE = {20, 50, 100, 200, 500, 1000, 3000, 5000, 10000};
    public static volatile boolean sHasDownloaded = false;
    public final List<OnBatchDownloadListener> mBatchDownloadListeners;
    public final Object mCollectionLock;
    public final Lock mDispatchLock;
    public Future mDispatchTask;
    public List<BatchItem> mDownloadItems;
    public Map<String, BatchItem> mDownloadItemsMap;
    public DownloadListener mDownloadListener;
    public ErrorCode mError;
    public String mErrorDesc;
    public List<BatchItem> mFailItems;
    public DownloadOptions.Builder mOptionsBuilder;
    public AtomicInteger mStatus;
    public List<BatchItem> mSuccessItems;
    public Handler mUIHandler;
    public Handler mWorkHandler;

    /* loaded from: classes.dex */
    public interface OnBatchDownloadListener {
        void onDownloadCancelled(List<BatchItem> list, List<BatchItem> list2);

        void onDownloadComplete(List<BatchItem> list, List<BatchItem> list2, ErrorCode errorCode, String str);

        void onDownloadProgress(List<BatchItem> list, List<BatchItem> list2);
    }

    /* loaded from: classes.dex */
    public static final class SingletonHolder {
        public static final BatchDownloadManager INSTANCE = new BatchDownloadManager();
    }

    public static BatchDownloadManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public BatchDownloadManager() {
        this.mCollectionLock = new Object();
        this.mDispatchLock = new ReentrantLock(true);
        this.mError = ErrorCode.NO_ERROR;
        this.mErrorDesc = null;
        this.mStatus = new AtomicInteger(0);
        this.mBatchDownloadListeners = new ArrayList();
        this.mDownloadListener = new ItemDownloadListener();
        this.mDownloadItemsMap = new HashMap();
        this.mDownloadItems = new LinkedList();
        this.mSuccessItems = new LinkedList();
        this.mFailItems = new LinkedList();
        this.mOptionsBuilder = new DownloadOptions.Builder();
        this.mUIHandler = new Handler(Looper.getMainLooper(), this);
        HandlerThread handlerThread = new HandlerThread("batch_download_work_thread", 10);
        handlerThread.start();
        this.mWorkHandler = new Handler(handlerThread.getLooper());
        DownloadObserver.getInstance().register(GalleryApp.sGetAndroidContext(), this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            Object obj = message.obj;
            if (obj != null) {
                ((OnBatchDownloadListener) obj).onDownloadComplete(Collections.unmodifiableList(this.mSuccessItems), Collections.unmodifiableList(this.mDownloadItems), this.mError, this.mErrorDesc);
            } else {
                synchronized (this.mBatchDownloadListeners) {
                    for (OnBatchDownloadListener onBatchDownloadListener : this.mBatchDownloadListeners) {
                        onBatchDownloadListener.onDownloadComplete(Collections.unmodifiableList(this.mSuccessItems), Collections.unmodifiableList(this.mDownloadItems), this.mError, this.mErrorDesc);
                    }
                }
            }
            return true;
        } else if (i == 2) {
            Object obj2 = message.obj;
            if (obj2 != null) {
                ((OnBatchDownloadListener) obj2).onDownloadProgress(Collections.unmodifiableList(this.mSuccessItems), Collections.unmodifiableList(this.mDownloadItems));
            } else {
                synchronized (this.mBatchDownloadListeners) {
                    for (OnBatchDownloadListener onBatchDownloadListener2 : this.mBatchDownloadListeners) {
                        onBatchDownloadListener2.onDownloadProgress(Collections.unmodifiableList(this.mSuccessItems), Collections.unmodifiableList(this.mDownloadItems));
                    }
                }
            }
            return true;
        } else if (i != 3) {
            return false;
        } else {
            Object obj3 = message.obj;
            if (obj3 != null) {
                ((OnBatchDownloadListener) obj3).onDownloadCancelled(Collections.unmodifiableList(this.mSuccessItems), Collections.unmodifiableList(this.mDownloadItems));
            } else {
                synchronized (this.mBatchDownloadListeners) {
                    for (OnBatchDownloadListener onBatchDownloadListener3 : this.mBatchDownloadListeners) {
                        onBatchDownloadListener3.onDownloadCancelled(Collections.unmodifiableList(this.mSuccessItems), Collections.unmodifiableList(this.mDownloadItems));
                    }
                }
            }
            return true;
        }
    }

    public void registerBatchDownloadListener(OnBatchDownloadListener onBatchDownloadListener) {
        synchronized (this.mBatchDownloadListeners) {
            this.mBatchDownloadListeners.add(onBatchDownloadListener);
        }
        int i = this.mStatus.get();
        if (i == 1) {
            callbackBatchProgress(onBatchDownloadListener);
        } else if (i == 2) {
            callbackBatchCancelled(onBatchDownloadListener);
        } else {
            callbackBatchEnd(onBatchDownloadListener);
        }
    }

    public void unregisterBatchDownloadListener(OnBatchDownloadListener onBatchDownloadListener) {
        synchronized (this.mBatchDownloadListeners) {
            this.mBatchDownloadListeners.remove(onBatchDownloadListener);
        }
    }

    public static DownloadType getAutoDownloadType() {
        DownloadType downloadType = GalleryPreferences.Sync.getDownloadType();
        if (downloadType == DownloadType.ORIGIN) {
            downloadType = DownloadType.ORIGIN_BATCH;
        }
        return downloadType == DownloadType.THUMBNAIL ? DownloadType.THUMBNAIL_BATCH : downloadType;
    }

    public final void doStopDownload() {
        this.mDispatchLock.lock();
        try {
            cancelTask();
            ImageDownloader.getInstance().cancelAll(DownloadType.THUMBNAIL_BATCH);
            ImageDownloader.getInstance().cancelAll(DownloadType.ORIGIN_BATCH);
        } finally {
            this.mDispatchLock.unlock();
        }
    }

    public void startBatchDownload(Context context, boolean z) {
        submit(new StartBatchDownloadTask(context, z));
    }

    public void stopBatchDownload(Context context) {
        submit(new StopBatchDownloadTask(context));
    }

    public final void interruptBatchDownload(Context context) {
        submit(new InterruptBatchDownloadTask(context));
    }

    public void download(Uri uri, String str) {
        submit(new RequestDownloadTask(GalleryApp.sGetAndroidContext(), uri, str));
    }

    public final void submit(RequestTask requestTask) {
        DefaultLogger.d("BatchDownloadManager", "submit task [%s].", requestTask.toString());
        this.mWorkHandler.post(requestTask);
    }

    public static boolean canAutoDownload() {
        return AccountCache.getAccount() != null && GalleryPreferences.Sync.isAutoDownload() && Preference.sIsFirstSynced();
    }

    public final void resumeBatchDownload(Context context) {
        if (canAutoDownload()) {
            DefaultLogger.d("BatchDownloadManager", "resume batch download for %s", getAutoDownloadType());
            startBatchDownload(context, false);
        }
    }

    public final void cancelTask() {
        Future future = this.mDispatchTask;
        if (future != null) {
            future.cancel();
            this.mDispatchTask = null;
        }
    }

    public final void download(List<BatchItem> list, DownloadOptions downloadOptions) {
        synchronized (this.mCollectionLock) {
            for (BatchItem batchItem : list) {
                this.mDownloadItems.add(batchItem);
                this.mDownloadItemsMap.put(BatchItem.generateKey(batchItem.mUri, batchItem.mType), batchItem);
            }
        }
        this.mDispatchLock.lock();
        try {
            for (BatchItem batchItem2 : list) {
                ImageDownloader.getInstance().load(batchItem2.mUri, batchItem2.mType, downloadOptions, this.mDownloadListener, null);
            }
        } finally {
            this.mDispatchLock.unlock();
        }
    }

    public boolean checkCondition() {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("BatchDownloadManager", "condition cta not allowed");
            setError(ErrorCode.NO_CTA_PERMISSION, null);
            return false;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.d("BatchDownloadManager", "condition no network");
            setError(ErrorCode.NO_NETWORK, null);
            return false;
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            DefaultLogger.d("BatchDownloadManager", "condition no wifi");
            setError(ErrorCode.NO_WIFI_CONNECTED, null);
            return false;
        } else {
            if (this.mOptionsBuilder.build().isRequireCharging()) {
                if (!GalleryPreferences.Sync.getIsPlugged()) {
                    DefaultLogger.d("BatchDownloadManager", "condition not charging");
                    setError(ErrorCode.NO_CHARGING, null);
                    return false;
                }
            } else if (!GalleryPreferences.Sync.getPowerCanSync()) {
                DefaultLogger.d("BatchDownloadManager", "condition low power");
                setError(ErrorCode.POWER_LOW, null);
                return false;
            }
            if (!GalleryPreferences.Sync.isDeviceStorageLow()) {
                return true;
            }
            DefaultLogger.d("BatchDownloadManager", "condition low storage");
            setError(ErrorCode.STORAGE_LOW, null);
            return false;
        }
    }

    @Override // com.miui.gallery.cloud.download.DownloadObserver.OnConditionChangeListener
    public void onConditionChanged(Context context) {
        if (checkCondition()) {
            resumeBatchDownload(context);
        } else {
            interruptBatchDownload(context);
        }
    }

    public final void clearState() {
        synchronized (this.mCollectionLock) {
            this.mDownloadItemsMap.clear();
            this.mDownloadItems.clear();
            this.mSuccessItems.clear();
            this.mFailItems.clear();
            setError(ErrorCode.NO_ERROR, null, true);
        }
        this.mStatus.set(0);
    }

    public final void setError(ErrorCode errorCode, String str) {
        setError(errorCode, str, false);
    }

    /* JADX WARN: Code restructure failed: missing block: B:6:0x0009, code lost:
        if (r2.mError != com.miui.gallery.error.core.ErrorCode.STORAGE_NO_WRITE_PERMISSION) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void setError(com.miui.gallery.error.core.ErrorCode r3, java.lang.String r4, boolean r5) {
        /*
            r2 = this;
            java.lang.Object r0 = r2.mCollectionLock
            monitor-enter(r0)
            if (r5 != 0) goto Lb
            com.miui.gallery.error.core.ErrorCode r5 = r2.mError     // Catch: java.lang.Throwable -> L11
            com.miui.gallery.error.core.ErrorCode r1 = com.miui.gallery.error.core.ErrorCode.STORAGE_NO_WRITE_PERMISSION     // Catch: java.lang.Throwable -> L11
            if (r5 == r1) goto Lf
        Lb:
            r2.mError = r3     // Catch: java.lang.Throwable -> L11
            r2.mErrorDesc = r4     // Catch: java.lang.Throwable -> L11
        Lf:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L11
            return
        L11:
            r3 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L11
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.download.BatchDownloadManager.setError(com.miui.gallery.error.core.ErrorCode, java.lang.String, boolean):void");
    }

    public final boolean isBatchDownloading(DownloadType downloadType) {
        boolean z;
        synchronized (this.mCollectionLock) {
            z = false;
            if ((this.mDownloadItems.size() > 0 ? this.mDownloadItems.get(0).mType : null) == downloadType && this.mStatus.get() == 1) {
                z = true;
            }
        }
        return z;
    }

    public final boolean isDownloadEnd(DownloadType downloadType) {
        boolean z;
        synchronized (this.mCollectionLock) {
            z = false;
            if ((this.mDownloadItems.size() > 0 ? this.mDownloadItems.get(0).mType : null) == downloadType && this.mFailItems.size() + this.mSuccessItems.size() >= this.mDownloadItems.size()) {
                z = true;
            }
        }
        return z;
    }

    public final void callbackBatchEnd() {
        callbackBatchEnd(null);
        DefaultLogger.d("BatchDownloadManager", "download %s end success[%s], total[%s], error %s", getAutoDownloadType(), Integer.valueOf(this.mSuccessItems.size()), Integer.valueOf(this.mDownloadItems.size()), this.mError);
    }

    public final void callbackBatchProgress() {
        callbackBatchProgress(null);
    }

    public final void callbackBatchCancelled() {
        callbackBatchCancelled(null);
    }

    public final void callbackBatchProgress(OnBatchDownloadListener onBatchDownloadListener) {
        this.mUIHandler.obtainMessage(2, onBatchDownloadListener).sendToTarget();
    }

    public final void callbackBatchEnd(OnBatchDownloadListener onBatchDownloadListener) {
        this.mUIHandler.obtainMessage(1, onBatchDownloadListener).sendToTarget();
    }

    public final void callbackBatchCancelled(OnBatchDownloadListener onBatchDownloadListener) {
        this.mUIHandler.obtainMessage(3, onBatchDownloadListener).sendToTarget();
    }

    public final boolean isValidItem(Uri uri, DownloadType downloadType) {
        return getAutoDownloadType() == downloadType && this.mDownloadItemsMap.containsKey(BatchItem.generateKey(uri, downloadType));
    }

    public final void onItemDownloadSuccess(Uri uri, DownloadType downloadType) {
        synchronized (this.mCollectionLock) {
            if (!isValidItem(uri, downloadType)) {
                return;
            }
            this.mSuccessItems.add(new BatchItem(uri, downloadType));
            if (isDownloadEnd(getAutoDownloadType())) {
                this.mStatus.set(4);
                callbackBatchEnd();
            } else {
                callbackBatchProgress();
            }
        }
    }

    public final void onItemDownloadFail(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
        synchronized (this.mCollectionLock) {
            if (!isValidItem(uri, downloadType)) {
                return;
            }
            updateFailReason(downloadFailReason);
            this.mFailItems.add(new BatchItem(uri, downloadType));
            if (isDownloadEnd(getAutoDownloadType()) && this.mStatus.compareAndSet(1, 4)) {
                callbackBatchEnd();
            }
        }
    }

    public final void onItemDownloadCancel(Uri uri, DownloadType downloadType) {
        synchronized (this.mCollectionLock) {
            if (!isValidItem(uri, downloadType)) {
                return;
            }
            this.mFailItems.add(new BatchItem(uri, downloadType));
            if (isDownloadEnd(downloadType) && this.mStatus.compareAndSet(1, 4)) {
                callbackBatchEnd();
            }
        }
    }

    public final void updateFailReason(DownloadFailReason downloadFailReason) {
        if (downloadFailReason != null) {
            setError(downloadFailReason.getCode(), downloadFailReason.getDesc());
        }
    }

    /* loaded from: classes.dex */
    public class DispatchJob implements ThreadPool.Job {
        public final Context mContext;
        public final DownloadType mType;

        public DispatchJob(Context context, DownloadType downloadType) {
            this.mContext = context;
            this.mType = downloadType;
        }

        public final List<BatchItem> queryDownload(Context context) {
            LinkedList linkedList = new LinkedList();
            List<BatchItem> queryDownload = BatchDownloadUtil.queryDownload(this.mContext, this.mType, false, -1);
            if (BaseMiscUtil.isValid(queryDownload)) {
                linkedList.addAll(queryDownload);
            }
            if (CloudControlStrategyHelper.getSyncStrategy().isAutoDownloadShare()) {
                List<BatchItem> queryDownload2 = BatchDownloadUtil.queryDownload(this.mContext, this.mType, true, -1);
                if (BaseMiscUtil.isValid(queryDownload2)) {
                    linkedList.addAll(queryDownload2);
                }
            }
            return linkedList;
        }

        public final boolean isValidRequest() {
            return GalleryPreferences.Sync.isAutoDownload() && this.mType == BatchDownloadManager.getAutoDownloadType() && BatchDownloadManager.this.mStatus.get() == 0;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Object mo1807run(ThreadPool.JobContext jobContext) {
            boolean isAppProcessInForeground = MiscUtil.isAppProcessInForeground();
            BatchDownloadManager.this.mOptionsBuilder.setRequireCharging(!isAppProcessInForeground);
            if (!BatchDownloadManager.this.checkCondition()) {
                DefaultLogger.d("BatchDownloadManager", "condition not ok, cannot download");
                return null;
            }
            List<BatchItem> queryDownload = queryDownload(this.mContext);
            if (BaseMiscUtil.isValid(queryDownload)) {
                boolean unused = BatchDownloadManager.sHasDownloaded = false;
                if ((jobContext != null && jobContext.isCancelled()) || !isValidRequest()) {
                    DefaultLogger.d("BatchDownloadManager", "not auto batch download before dispatching");
                    return null;
                }
                DefaultLogger.d("BatchDownloadManager", "start download %s items for %s in foreground %s", Integer.valueOf(queryDownload.size()), BatchDownloadManager.getAutoDownloadType(), Boolean.valueOf(isAppProcessInForeground));
                BatchDownloadManager.this.download(queryDownload, BatchDownloadManager.this.mOptionsBuilder.setRequireWLAN(true).setRequireDeviceStorage(true).setRequirePower(true).setRequireCharging(!isAppProcessInForeground).build());
                if ((jobContext == null || !jobContext.isCancelled()) && isValidRequest()) {
                    if (BatchDownloadManager.this.mStatus.compareAndSet(0, 1)) {
                        BatchDownloadManager.this.callbackBatchProgress();
                    }
                    BatchDownloadManager.this.statStartDownload(queryDownload.size());
                } else {
                    DefaultLogger.d("BatchDownloadManager", "not auto batch download after dispatching");
                    ImageDownloader.getInstance().cancelAll(this.mType);
                    return null;
                }
            } else {
                boolean unused2 = BatchDownloadManager.sHasDownloaded = true;
                BatchDownloadManager.this.statEndDownload();
                DefaultLogger.d("BatchDownloadManager", "no items need download for %s", this.mType);
            }
            return null;
        }
    }

    public final void statStartDownload(int i) {
        if (!Preference.sIsFirstSynced() || GalleryPreferences.Sync.getAutoDownloadTime() >= 0) {
            return;
        }
        DefaultLogger.d("BatchDownloadManager", "stat start download");
        GalleryPreferences.Sync.setAutoDownloadTime(System.currentTimeMillis());
        HashMap hashMap = new HashMap();
        hashMap.put("stage", SamplingStatHelper.formatValueStage(i, COUNT_STAGE));
        SamplingStatHelper.recordCountEvent("Sync", "sync_auto_download_weight", hashMap);
    }

    public final void statEndDownload() {
        if (!Preference.sIsFirstSynced() || GalleryPreferences.Sync.isEverAutoDownloaded()) {
            return;
        }
        DefaultLogger.d("BatchDownloadManager", "stat end download");
        GalleryPreferences.Sync.setEverAutoDownloaded();
        HashMap hashMap = new HashMap();
        hashMap.put("download_time", String.valueOf((int) ((((System.currentTimeMillis() - GalleryPreferences.Sync.getAutoDownloadTime()) / 1000) / 60) / 60)));
        SamplingStatHelper.recordCountEvent("Sync", String.format("sync_auto_download_%s", GalleryPreferences.Sync.getDownloadType()), hashMap);
    }

    /* loaded from: classes.dex */
    public class StartBatchDownloadTask extends RequestTask {
        public boolean mImmediately;

        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public String toString() {
            return "StartBatchDownloadTask";
        }

        public StartBatchDownloadTask(Context context, boolean z) {
            super(context);
            this.mImmediately = z;
        }

        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public void onHandle(Context context) {
            startBatchDownload(context, this.mImmediately);
        }

        public final void startBatchDownload(Context context, boolean z) {
            DownloadType autoDownloadType = BatchDownloadManager.getAutoDownloadType();
            if (!BatchDownloadManager.this.isBatchDownloading(autoDownloadType)) {
                if (!BatchDownloadManager.sHasDownloaded || z) {
                    BatchDownloadManager.this.mDispatchLock.lock();
                    try {
                        BatchDownloadManager.this.clearState();
                        BatchDownloadManager.this.doStopDownload();
                        BatchDownloadManager.this.mDispatchTask = ThreadManager.getMiscPool().submit(new DispatchJob(context.getApplicationContext(), autoDownloadType));
                        return;
                    } finally {
                        BatchDownloadManager.this.mDispatchLock.unlock();
                    }
                }
                DefaultLogger.d("BatchDownloadManager", "no need download because memory marker");
                return;
            }
            DefaultLogger.d("BatchDownloadManager", "batch downloading already started: %s", autoDownloadType);
        }
    }

    /* loaded from: classes.dex */
    public class StopBatchDownloadTask extends RequestTask {
        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public String toString() {
            return "StopBatchDownloadTask";
        }

        public StopBatchDownloadTask(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public void onHandle(Context context) {
            DefaultLogger.d("BatchDownloadManager", "stop batch download for %s", BatchDownloadManager.getAutoDownloadType());
            BatchDownloadManager.this.doStopDownload();
            BatchDownloadManager.this.mStatus.set(2);
            BatchDownloadManager.this.callbackBatchCancelled();
        }
    }

    /* loaded from: classes.dex */
    public class InterruptBatchDownloadTask extends RequestTask {
        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public String toString() {
            return "InterruptBatchDownloadTask";
        }

        public InterruptBatchDownloadTask(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public void onHandle(Context context) {
            if (BatchDownloadManager.this.mStatus.compareAndSet(1, 3)) {
                DefaultLogger.d("BatchDownloadManager", "interrupt batch download for %s", BatchDownloadManager.getAutoDownloadType());
                BatchDownloadManager.this.doStopDownload();
                BatchDownloadManager.this.callbackBatchEnd();
                if (!BatchDownloadManager.canAutoDownload()) {
                    return;
                }
                SyncJobScheduler.scheduleJob(GalleryApp.sGetAndroidContext());
            }
        }
    }

    /* loaded from: classes.dex */
    public class RequestDownloadTask extends RequestTask {
        public String mMimeType;
        public Uri mUri;

        public RequestDownloadTask(Context context, Uri uri, String str) {
            super(context);
            this.mUri = uri;
            this.mMimeType = str;
        }

        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public void onHandle(Context context) {
            DownloadOptions build = BatchDownloadManager.this.mOptionsBuilder.setRequireWLAN(true).setRequireDeviceStorage(true).setRequirePower(true).setQueueFirst(true).build();
            LinkedList linkedList = new LinkedList();
            linkedList.add(new BatchItem(this.mUri, IncompatibleMediaType.isUnsupportedMediaType(this.mMimeType) ? DownloadType.THUMBNAIL_BATCH : BatchDownloadManager.getAutoDownloadType()));
            BatchDownloadManager.this.download(linkedList, build);
            DefaultLogger.d("BatchDownloadManager", "download %s at first", this.mUri);
        }

        @Override // com.miui.gallery.cloud.download.BatchDownloadManager.RequestTask
        public String toString() {
            return "RequestDownloadTask [" + this.mUri + ", " + this.mMimeType + "]";
        }
    }

    /* loaded from: classes.dex */
    public static abstract class RequestTask implements Runnable {
        public Context applicationContext;

        public abstract void onHandle(Context context);

        public abstract String toString();

        public RequestTask(Context context) {
            this.applicationContext = context.getApplicationContext();
        }

        @Override // java.lang.Runnable
        public final void run() {
            onHandle(this.applicationContext);
        }
    }

    public static void switchAutoDownload(final boolean z, Activity activity) {
        GalleryPreferences.Sync.setAutoDownload(z);
        if (activity != null) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.cloud.download.BatchDownloadManager.1
                public final boolean checkValidTask(boolean z2) {
                    return z2 == GalleryPreferences.Sync.isAutoDownload();
                }

                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public Object mo1807run(ThreadPool.JobContext jobContext) {
                    if (!checkValidTask(z)) {
                        DefaultLogger.w("BatchDownloadManager", "invalid switch download task, old %s, new %s", Boolean.valueOf(z), Boolean.valueOf(GalleryPreferences.Sync.isAutoDownload()));
                        return null;
                    }
                    Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                    if (z) {
                        BatchDownloadUtil.cleanDownloadedMark(sGetAndroidContext);
                        if (checkValidTask(z) && BatchDownloadManager.canAutoDownload()) {
                            BatchDownloadManager.getInstance().startBatchDownload(sGetAndroidContext, true);
                        }
                    } else {
                        BatchDownloadManager.getInstance().stopBatchDownload(sGetAndroidContext);
                    }
                    return null;
                }
            });
        }
        statAutoDownloadSwitchChanged(z);
    }

    public static void statAutoDownloadSwitchChanged(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("state", z ? CallMethod.RESULT_ENABLE_BOOLEAN : "disable");
        SamplingStatHelper.recordCountEvent("Sync", "sync_auto_download_switch_changed", hashMap);
    }

    /* loaded from: classes.dex */
    public static class BatchItem {
        public final DownloadType mType;
        public final Uri mUri;

        public BatchItem(Uri uri, DownloadType downloadType) {
            this.mUri = uri;
            this.mType = downloadType;
        }

        public static String generateKey(Uri uri, DownloadType downloadType) {
            return DownloadUtil.generateKey(uri, downloadType);
        }
    }

    /* loaded from: classes.dex */
    public final class ItemDownloadListener implements DownloadListener {
        public ItemDownloadListener() {
        }

        @Override // com.miui.gallery.sdk.download.listener.DownloadListener
        public void onDownloadSuccess(Uri uri, DownloadType downloadType, DownloadedItem downloadedItem) {
            BatchDownloadManager.this.onItemDownloadSuccess(uri, downloadType);
        }

        @Override // com.miui.gallery.sdk.download.listener.DownloadListener
        public void onDownloadFail(Uri uri, DownloadType downloadType, DownloadFailReason downloadFailReason) {
            BatchDownloadManager.this.onItemDownloadFail(uri, downloadType, downloadFailReason);
        }

        @Override // com.miui.gallery.sdk.download.listener.DownloadListener
        public void onDownloadCancel(Uri uri, DownloadType downloadType) {
            BatchDownloadManager.this.onItemDownloadCancel(uri, downloadType);
        }
    }
}
