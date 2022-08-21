package com.miui.gallery.cloud.jobs;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import java.time.Duration;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SyncJobScheduler.kt */
/* loaded from: classes.dex */
public final class SyncJobScheduler {
    public static final SyncJobScheduler INSTANCE = new SyncJobScheduler();

    public static final int scheduleJob(Context context) {
        if (context == null) {
            return 0;
        }
        OneTimeWorkRequest build = new OneTimeWorkRequest.Builder(BackgroundDownloadWorker.class).setConstraints(new Constraints.Builder().setRequiresDeviceIdle(CloudControlStrategyHelper.getSyncStrategy().isAutoDownloadRequireDeviceIdle()).setRequiresCharging(true).setRequiredNetworkType(NetworkType.UNMETERED).build()).setInitialDelay(Duration.ofMinutes(10L)).build();
        Intrinsics.checkNotNullExpressionValue(build, "OneTimeWorkRequestBuilde…n.ofMinutes(10)) .build()");
        WorkManager.getInstance(context).enqueueUniqueWork("com.miui.gallery.job.BackgroundDownload", ExistingWorkPolicy.REPLACE, build);
        return 1;
    }
}
