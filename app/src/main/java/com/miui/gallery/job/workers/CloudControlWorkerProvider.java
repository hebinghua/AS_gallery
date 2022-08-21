package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CloudControlWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.CloudControl")
/* loaded from: classes2.dex */
public final class CloudControlWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(CloudControlWorker.class, 1L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).setRequiresBatteryNotLow(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d()\n            ).build()");
        return build;
    }

    /* compiled from: CloudControlWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class CloudControlWorker extends TrackedWorker {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CloudControlWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            ListenableWorker.Result failure;
            String str;
            if (new CloudControlRequestHelper(getApplicationContext()).execRequestSync()) {
                failure = ListenableWorker.Result.success();
                str = "success()";
            } else {
                failure = ListenableWorker.Result.failure();
                str = "failure()";
            }
            Intrinsics.checkNotNullExpressionValue(failure, str);
            return failure;
        }
    }
}
