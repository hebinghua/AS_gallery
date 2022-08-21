package com.miui.gallery.job.workers;

import android.content.Context;
import android.os.Build;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.ThumbnailUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ScanMediaWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.ScanMedia")
/* loaded from: classes2.dex */
public final class ScanMediaWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(ScanMediaWorker.class, 1L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.NOT_REQUIRED).setRequiresCharging(true).setRequiresDeviceIdle(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d()\n            ).build()");
        return build;
    }

    /* compiled from: ScanMediaWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class ScanMediaWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);
        public static final AtomicBoolean isRunning = new AtomicBoolean(false);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ScanMediaWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            DefaultLogger.i("ScanMediaWorker", "Do work");
            AtomicBoolean atomicBoolean = isRunning;
            if (atomicBoolean.compareAndSet(false, true)) {
                if (!GalleryPreferences.MediaScanner.getEverForceScanAllAlbumsForFormatExpansion()) {
                    ScannerEngine.getInstance().scanPathsAsync(StorageUtils.getMountedVolumePaths(getApplicationContext()), 18);
                } else if (Build.VERSION.SDK_INT <= 29) {
                    ScannerEngine.getInstance().scanPathsAsync(StorageUtils.getMountedVolumePaths(getApplicationContext()), 10);
                }
                ScannerEngine.getInstance().scanAsync(22);
                ThumbnailUtil.cleanup();
                atomicBoolean.set(false);
            } else {
                DefaultLogger.w("ScanMediaWorker", "A same job is still running.");
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        /* compiled from: ScanMediaWorkerProvider.kt */
        /* loaded from: classes2.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }
    }
}
