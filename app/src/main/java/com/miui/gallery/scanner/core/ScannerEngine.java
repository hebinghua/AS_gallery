package com.miui.gallery.scanner.core;

import android.content.Context;
import android.os.Looper;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.scanner.core.ScanRequest;
import com.miui.gallery.scanner.core.messenger.GalleryScanMessenger;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.eventual.CleanFileTask;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public final class ScannerEngine {
    public BaseMediaScannerReceiver mMediaScannerReceiver;
    public LazyValue<Object, Boolean> mMediaStoreSupportGalleryScan;
    public GalleryScanMessenger mMessenger;
    public final LifecycleObserver mProcessObserver;
    public final AtomicBoolean mStarted;

    public static /* synthetic */ void $r8$lambda$JzikfFsEpJMVBo1BHu5Ons52FcA(ScannerEngine scannerEngine) {
        scannerEngine.lambda$doStart$1();
    }

    /* renamed from: $r8$lambda$T_i--BGiVGZSILO8mVZditTHRBw */
    public static /* synthetic */ void m1271$r8$lambda$T_iBGiVGZSILO8mVZditTHRBw(ScannerEngine scannerEngine) {
        scannerEngine.lambda$doStart$0();
    }

    public final void onAppSwitch2Background() {
    }

    /* loaded from: classes2.dex */
    public static final class Singleton {
        public static ScannerEngine sInstance = new ScannerEngine();
    }

    public ScannerEngine() {
        this.mMediaStoreSupportGalleryScan = new LazyValue<Object, Boolean>() { // from class: com.miui.gallery.scanner.core.ScannerEngine.1
            {
                ScannerEngine.this = this;
            }

            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit */
            public Boolean mo1272onInit(Object obj) {
                return Boolean.valueOf(Preference.sIsMediaStoreSupportGalleryScan());
            }
        };
        this.mProcessObserver = new DefaultLifecycleObserver() { // from class: com.miui.gallery.scanner.core.ScannerEngine.2
            {
                ScannerEngine.this = this;
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public void onStart(LifecycleOwner lifecycleOwner) {
                ScannerEngine.this.onAppSwitch2Foreground();
            }

            @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
            public void onStop(LifecycleOwner lifecycleOwner) {
                ScannerEngine.this.onAppSwitch2Background();
            }
        };
        this.mStarted = new AtomicBoolean(false);
        this.mMessenger = new GalleryScanMessenger();
    }

    public static ScannerEngine getInstance() {
        return Singleton.sInstance;
    }

    public void start() {
        if (this.mStarted.compareAndSet(false, true)) {
            doStart();
        }
    }

    public final void doStart() {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.scanner.core.ScannerEngine$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ScannerEngine.m1271$r8$lambda$T_iBGiVGZSILO8mVZditTHRBw(ScannerEngine.this);
            }
        });
        this.mMediaScannerReceiver = createMediaScannerReceiver(isMediaStoreSupportGalleryScan());
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.scanner.core.ScannerEngine$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ScannerEngine.$r8$lambda$JzikfFsEpJMVBo1BHu5Ons52FcA(ScannerEngine.this);
            }
        });
    }

    public /* synthetic */ void lambda$doStart$0() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this.mProcessObserver);
    }

    public /* synthetic */ void lambda$doStart$1() {
        BaseMediaScannerReceiver.register(GalleryApp.sGetAndroidContext(), this.mMediaScannerReceiver);
    }

    public final BaseMediaScannerReceiver createMediaScannerReceiver(boolean z) {
        return z ? new BaseMediaScannerReceiver() : new MediaScannerReceiver();
    }

    public final boolean isMediaStoreSupportGalleryScan() {
        if (this.mMediaStoreSupportGalleryScan.hasResolved() || Thread.currentThread() != Looper.getMainLooper().getThread()) {
            return this.mMediaStoreSupportGalleryScan.get(null).booleanValue();
        }
        return false;
    }

    public final void onAppSwitch2Foreground() {
        DefaultLogger.d("ScannerEngine", "onAppSwitch2Foreground, triggerScan.");
        triggerScan();
    }

    public void triggerScan() {
        submit(new ScanRequest.Builder().setSceneCode(0).isMediaStoreSupportGalleryScan(isMediaStoreSupportGalleryScan()).build());
    }

    public void cancelRunning() {
        submit(new ScanRequest.Builder().setSceneCode(1).build());
    }

    public ScanResult scanFile(Context context, String str, int i) {
        return ScanSingleFileTask.create(context, Paths.get(str, new String[0]), ScanTaskConfigFactory.get(i), 4L).mo1807run(null);
    }

    public ScanResult cleanFile(Context context, String str, int i) {
        return new CleanFileTask(context, ScanTaskConfigFactory.get(i), Paths.get(str, new String[0])).mo1807run(null);
    }

    public void scanAsync(int i) {
        submit(new ScanRequest.Builder().setSceneCode(i).build());
    }

    public void scanPathAsync(String str, int i) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        scanPathsAsync(arrayList, i);
    }

    public void scanPathsAsync(List<String> list, int i) {
        submit(new ScanRequest.Builder().setPaths(list).setSceneCode(i).build());
    }

    public final void submit(ScanRequest scanRequest) {
        this.mMessenger.submit(scanRequest);
    }
}
