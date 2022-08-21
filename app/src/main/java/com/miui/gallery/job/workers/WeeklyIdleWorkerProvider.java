package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.cleaner.SlimTipUtil;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.util.PersistentResponseHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.recorder.RecorderManager;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: WeeklyIdleWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.WeeklyIdle")
/* loaded from: classes2.dex */
public final class WeeklyIdleWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(WeeklyIdleWorker.class, 7L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiresDeviceIdle(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d())\n            .build()");
        return build;
    }

    /* compiled from: WeeklyIdleWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class WeeklyIdleWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);
        public static final AtomicBoolean isRunning = new AtomicBoolean(false);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public WeeklyIdleWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            AtomicBoolean atomicBoolean = isRunning;
            if (atomicBoolean.compareAndSet(false, true)) {
                exec();
                atomicBoolean.set(false);
            } else {
                DefaultLogger.d("WeeklyIdleWorker", "A same job is still running.");
            }
            Boolean isSlimEntranceEnable = SlimTipUtil.isSlimEntranceEnable("notification");
            Intrinsics.checkNotNullExpressionValue(isSlimEntranceEnable, "isSlimEntranceEnable(Sli…cesStrategy.NOTIFICATION)");
            if (isSlimEntranceEnable.booleanValue()) {
                SlimTipUtil.doScan();
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        public final void exec() {
            PersistentResponseHelper.cleanupInvalidRecords();
            RecorderManager.getInstance().clearExpiredRecords();
        }

        /* compiled from: WeeklyIdleWorkerProvider.kt */
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
