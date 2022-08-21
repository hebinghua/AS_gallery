package com.miui.gallery.cloud.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.download.DownloadObserver;
import com.miui.gallery.cloud.download.MicroBatchDownloadManager;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.processors.PublishProcessor;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class MicroBatchDownloadManager implements DownloadObserver.OnConditionChangeListener {
    public final Object mDispatchLock;
    public Disposable mDisposable;
    public DownloadOptions mDownloadOptions;
    public final AtomicBoolean mIsTerminated;
    public final PublishProcessor<Uri> mPublishProcessor;
    public Runnable mTerminalSignal;

    public static /* synthetic */ void $r8$lambda$3EjMmknnB2quEjwCg1kIOVKTLiQ(MicroBatchDownloadManager microBatchDownloadManager, List list) {
        microBatchDownloadManager.lambda$ensureSubscribed$1(list);
    }

    public /* synthetic */ MicroBatchDownloadManager(AnonymousClass1 anonymousClass1) {
        this();
    }

    public static /* synthetic */ void access$200(MicroBatchDownloadManager microBatchDownloadManager) {
        microBatchDownloadManager.terminate();
    }

    /* loaded from: classes.dex */
    public static final class SingletonHolder {
        public static final MicroBatchDownloadManager INSTANCE = new MicroBatchDownloadManager(null);
    }

    public MicroBatchDownloadManager() {
        this.mDispatchLock = new Object();
        this.mIsTerminated = new AtomicBoolean(false);
        this.mPublishProcessor = PublishProcessor.create();
        this.mDownloadOptions = new DownloadOptions.Builder().setRequireWLAN(true).setRequireDeviceStorage(true).setRequirePower(false).setQueueFirst(false).build();
        DownloadObserver.getInstance().register(GalleryApp.sGetAndroidContext(), this);
        LocalBroadcastManager.getInstance(GalleryApp.sGetAndroidContext()).registerReceiver(new AnonymousClass1(), new IntentFilter("com.miui.gallery.action.FIRST_SYNC_FINISHED"));
    }

    /* renamed from: com.miui.gallery.cloud.download.MicroBatchDownloadManager$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends BroadcastReceiver {
        public static /* synthetic */ void $r8$lambda$5yK0sFcXPa_ecpFOdXCaVZunFgk(MicroBatchDownloadManager microBatchDownloadManager) {
            MicroBatchDownloadManager.access$200(microBatchDownloadManager);
        }

        public AnonymousClass1() {
            MicroBatchDownloadManager.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
            if (MicroBatchDownloadManager.this.mTerminalSignal == null) {
                final MicroBatchDownloadManager microBatchDownloadManager = MicroBatchDownloadManager.this;
                microBatchDownloadManager.mTerminalSignal = new Runnable() { // from class: com.miui.gallery.cloud.download.MicroBatchDownloadManager$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        MicroBatchDownloadManager.AnonymousClass1.$r8$lambda$5yK0sFcXPa_ecpFOdXCaVZunFgk(MicroBatchDownloadManager.this);
                    }
                };
            }
            ThreadManager.getMainHandler().postDelayed(MicroBatchDownloadManager.this.mTerminalSignal, 60000L);
        }
    }

    public static MicroBatchDownloadManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public final void ensureSubscribed() {
        Disposable disposable = this.mDisposable;
        if (disposable == null || disposable.isDisposed()) {
            this.mDisposable = this.mPublishProcessor.buffer(3L, TimeUnit.SECONDS, 25).filter(MicroBatchDownloadManager$$ExternalSyntheticLambda1.INSTANCE).subscribe(new Consumer() { // from class: com.miui.gallery.cloud.download.MicroBatchDownloadManager$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    MicroBatchDownloadManager.$r8$lambda$3EjMmknnB2quEjwCg1kIOVKTLiQ(MicroBatchDownloadManager.this, (List) obj);
                }
            });
        }
    }

    public static /* synthetic */ boolean lambda$ensureSubscribed$0(List list) throws Exception {
        return list.size() > 0;
    }

    public /* synthetic */ void lambda$ensureSubscribed$1(List list) throws Exception {
        if (this.mIsTerminated.get()) {
            return;
        }
        if (checkCondition()) {
            doDownload(list);
            DefaultLogger.d("MicroBatchDownloadManager", "dispatch download %d items", Integer.valueOf(list.size()));
            return;
        }
        DefaultLogger.d("MicroBatchDownloadManager", "condition is unsatisfied, skip %d items", Integer.valueOf(list.size()));
    }

    public void download(Uri uri) {
        if (uri == null || this.mIsTerminated.get()) {
            return;
        }
        synchronized (this) {
            ensureSubscribed();
            this.mPublishProcessor.onNext(uri);
        }
    }

    public final void doDownload(List<Uri> list) {
        synchronized (this.mDispatchLock) {
            for (Uri uri : list) {
                ImageDownloader.getInstance().load(uri, DownloadType.MICRO_BATCH, this.mDownloadOptions, null, null);
            }
        }
    }

    @Override // com.miui.gallery.cloud.download.DownloadObserver.OnConditionChangeListener
    public void onConditionChanged(Context context) {
        if (!this.mIsTerminated.get() && !checkCondition()) {
            DefaultLogger.i("MicroBatchDownloadManager", "condition changed, cancel all tasks");
            ImageDownloader.getInstance().cancelAll(DownloadType.MICRO_BATCH);
        }
    }

    public final void terminate() {
        if (this.mIsTerminated.compareAndSet(false, true)) {
            DefaultLogger.i("MicroBatchDownloadManager", "terminate download after first sync finish");
            if (this.mTerminalSignal != null) {
                ThreadManager.getMainHandler().removeCallbacks(this.mTerminalSignal);
                this.mTerminalSignal = null;
            }
            DownloadObserver.getInstance().unregister(GalleryApp.sGetAndroidContext(), this);
            Disposable disposable = this.mDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mDisposable.dispose();
                this.mDisposable = null;
            }
            ImageDownloader.getInstance().cancelAll(DownloadType.MICRO_BATCH);
            this.mDownloadOptions = null;
        }
    }

    public final boolean checkCondition() {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("MicroBatchDownloadManager", "condition cta not allowed");
            return false;
        } else if (!BaseNetworkUtils.isNetworkConnected()) {
            DefaultLogger.d("MicroBatchDownloadManager", "condition no network");
            return false;
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            DefaultLogger.d("MicroBatchDownloadManager", "condition no wifi");
            return false;
        } else if (!GalleryPreferences.Sync.getPowerCanSync()) {
            DefaultLogger.d("MicroBatchDownloadManager", "condition low power");
            return false;
        } else if (!GalleryPreferences.Sync.isDeviceStorageLow()) {
            return true;
        } else {
            DefaultLogger.d("MicroBatchDownloadManager", "condition low storage");
            return false;
        }
    }
}
