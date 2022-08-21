package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.cloud.base.SyncRequestKt;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.cloud.jobs.SyncJobScheduler;
import com.miui.gallery.cloud.syncstate.DirtyCount;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SyncUtil;
import java.util.concurrent.TimeUnit;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: RequestSyncWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.RequestSync")
/* loaded from: classes2.dex */
public final class RequestSyncWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(RequestSyncWorker.class, 12L, TimeUnit.HOURS).setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).setRequiresCharging(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d()\n            ).build()");
        return build;
    }

    /* compiled from: RequestSyncWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class RequestSyncWorker extends TrackedWorker {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RequestSyncWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            Context applicationContext = getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "applicationContext");
            if (SyncUtil.isGalleryCloudSyncable(applicationContext)) {
                DirtyCount dirtyCount = SyncStateUtil.getDirtyCount(applicationContext);
                Intrinsics.checkNotNullExpressionValue(dirtyCount, "getDirtyCount(context)");
                if (dirtyCount.getSyncableCount() > 0 && (!SyncStateUtil.isSyncActive() || !SyncStateUtil.isUploading())) {
                    SyncUtil.requestSync(applicationContext, SyncRequestKt.SyncRequest(RequestSyncWorkerProvider$RequestSyncWorker$doWork$1.INSTANCE));
                    SamplingStatHelper.recordCountEvent("Sync", "sync_request_sync_periodic", MapsKt__MapsJVMKt.mapOf(TuplesKt.to("trigger_time", MiscUtil.getCurrentDate())));
                }
            }
            if (BatchDownloadManager.canAutoDownload()) {
                SyncJobScheduler.scheduleJob(applicationContext);
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }
    }
}
