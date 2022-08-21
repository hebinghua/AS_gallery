package com.miui.gallery.job.workers;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.migrate.StorageMigrationManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.RecentDiscoveryMediaManager;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.VlogEntranceUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.xiaomi.stat.MiStat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DailyIdleWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.DailyIdle")
/* loaded from: classes2.dex */
public final class DailyIdleWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(DailyIdleWorker.class, 1L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiresDeviceIdle(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d())\n            .build()");
        return build;
    }

    /* compiled from: DailyIdleWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class DailyIdleWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);
        public static final AtomicBoolean isRunning = new AtomicBoolean(false);

        /* compiled from: DailyIdleWorkerProvider.kt */
        /* loaded from: classes2.dex */
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[DownloadType.values().length];
                iArr[DownloadType.ORIGIN.ordinal()] = 1;
                iArr[DownloadType.THUMBNAIL.ordinal()] = 2;
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public DailyIdleWorker(Context context, WorkerParameters workerParams) {
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
                DefaultLogger.d("DailyIdleWorker", "A same job is still running.");
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        public final void exec() {
            if (VlogEntranceUtils.isAvailable()) {
                VlogUtils.cleanUpTransCodeFile();
            }
            RecentDiscoveryMediaManager.cleanupInvalidRecords();
            recordUserStatus();
            StorageMigrationManager.migrate(GalleryApp.sGetAndroidContext());
        }

        public final void recordUserStatus() {
            Account xiaomiAccount = AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext());
            boolean z = false;
            boolean z2 = xiaomiAccount != null;
            TrackController.trackUserStatus("403.54.0.1.13952", String.valueOf(z2));
            if (z2) {
                boolean syncAutomatically = ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider");
                boolean isOnlyShowLocalPhoto = GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
                boolean isAIAlbumEnabled = AIAlbumStatusHelper.isAIAlbumEnabled();
                boolean backupOnlyInWifi = GalleryPreferences.Sync.getBackupOnlyInWifi();
                boolean isCloudControlSearchBarOpen = AIAlbumStatusHelper.isCloudControlSearchBarOpen();
                boolean isAutoDownload = GalleryPreferences.Sync.isAutoDownload();
                DownloadType downloadType = GalleryPreferences.Sync.getDownloadType();
                boolean isDeviceSupportStoryFunction = MediaFeatureManager.isDeviceSupportStoryFunction();
                FeatureProfile.Status queryFeatureStatus = CloudControlManager.getInstance().queryFeatureStatus("photo-print");
                TrackController.trackUserStatus("403.54.0.1.13953", String.valueOf(syncAutomatically));
                TrackController.trackUserStatus("403.54.0.1.13958", String.valueOf(isOnlyShowLocalPhoto));
                TrackController.trackUserStatus("403.54.0.1.13954", String.valueOf(isAIAlbumEnabled));
                TrackController.trackUserStatus("403.54.0.1.13959", String.valueOf(backupOnlyInWifi));
                TrackController.trackUserStatus("403.54.0.1.13955", String.valueOf(isCloudControlSearchBarOpen));
                TrackController.trackUserStatus("403.54.0.1.13957", String.valueOf(isDeviceSupportStoryFunction));
                if (isAutoDownload) {
                    int i = downloadType == null ? -1 : WhenMappings.$EnumSwitchMapping$0[downloadType.ordinal()];
                    if (i == 1) {
                        TrackController.trackUserStatus("403.54.0.1.13956", MiStat.Param.ORIGIN);
                    } else if (i == 2) {
                        TrackController.trackUserStatus("403.54.0.1.13956", "HD");
                    } else {
                        DefaultLogger.i("TrackController", "trackUserStatus: downloadType->%s", downloadType);
                    }
                } else {
                    TrackController.trackUserStatus("403.54.0.1.13956", String.valueOf(isAutoDownload));
                }
                if (queryFeatureStatus == FeatureProfile.Status.ENABLE) {
                    z = true;
                }
                TrackController.trackUserStatus("403.54.0.1.13960", String.valueOf(z));
            }
            TrackController.trackUserStatus("403.54.0.1.16597", String.valueOf(GalleryPreferences.Assistant.isCreativityFunctionOn()));
        }

        /* compiled from: DailyIdleWorkerProvider.kt */
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
