package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.provider.peoplecover.LocalPeopleCoverManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LocalPeopleCoverWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.LocalPeopleCover")
/* loaded from: classes2.dex */
public final class LocalPeopleCoverWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(LocalPeopleCoverWorker.class, 3L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiresCharging(true).setRequiresDeviceIdle(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…   )\n            .build()");
        return build;
    }

    /* compiled from: LocalPeopleCoverWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class LocalPeopleCoverWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);
        public static final AtomicBoolean isRunning = new AtomicBoolean(false);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LocalPeopleCoverWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            AtomicBoolean atomicBoolean = isRunning;
            if (atomicBoolean.compareAndSet(false, true)) {
                LocalPeopleCoverManager.getInstance().startChooseCoverForAllPeople();
                atomicBoolean.set(false);
            } else {
                DefaultLogger.d("LocalPeopleCoverWorker", "A same job is still running.");
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        /* compiled from: LocalPeopleCoverWorkerProvider.kt */
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
