package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.assistant.process.ExistAnalyticFaceAndSceneTask;
import com.miui.gallery.assistant.process.ExistImageFeatureTask;
import com.miui.gallery.cloud.GalleryMiCloudUtil;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.provider.PeopleFaceSnapshotHelper;
import com.miui.gallery.provider.album.AlbumSnapshotHelper;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.util.DeleteDataUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DailyChargingWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.DailyCharging")
/* loaded from: classes2.dex */
public final class DailyChargingWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(DailyChargingWorker.class, 1L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiresCharging(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d())\n            .build()");
        return build;
    }

    /* compiled from: DailyChargingWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class DailyChargingWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);
        public static final AtomicBoolean isRunning = new AtomicBoolean(false);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DailyChargingWorker(Context context, WorkerParameters workerParams) {
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
                DefaultLogger.d("DailyChargingWorker", "A same job is still running.");
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        public final void exec() {
            PendingTaskManager.getInstance().checkTaskList();
            TrashUtils.cleanExpireItems();
            TrashUtils.cleanInvalidTrashFile();
            if (MediaFeatureManager.isImageFeatureCalculationEnable()) {
                PendingTaskManager.getInstance().postTask(6, null, ExistImageFeatureTask.class.getSimpleName());
            }
            if (MediaFeatureManager.isStoryGenerateEnable()) {
                PendingTaskManager.getInstance().postTask(11, null, ExistAnalyticFaceAndSceneTask.class.getSimpleName());
            }
            AlbumSnapshotHelper.queryAndPersist(GalleryApp.sGetAndroidContext());
            PeopleFaceSnapshotHelper.queryAndPersist(GalleryApp.sGetAndroidContext());
            GalleryMiCloudUtil.statUnSyncedCount();
            DeleteDataUtil.deleteMicroThumb();
        }

        /* compiled from: DailyChargingWorkerProvider.kt */
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
