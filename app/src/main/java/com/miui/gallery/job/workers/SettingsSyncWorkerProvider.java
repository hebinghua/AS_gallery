package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.settingssync.GallerySettingsSyncHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SettingsSyncWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.SettingsSync")
/* loaded from: classes2.dex */
public final class SettingsSyncWorkerProvider implements IPeriodicWorkerProvider {
    public static final Companion Companion = new Companion(null);

    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(SettingsSyncWorker.class, 1L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiredNetworkType(GalleryPreferences.Sync.getBackupOnlyInWifi() ? NetworkType.UNMETERED : NetworkType.CONNECTED).setRequiresBatteryNotLow(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d()\n            ).build()");
        return build;
    }

    /* compiled from: SettingsSyncWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class SettingsSyncWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SettingsSyncWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            if (GalleryPreferences.Sync.getBackupOnlyInWifi()) {
                if (!BaseNetworkUtils.isNetworkConnected() || BaseNetworkUtils.isActiveNetworkMetered()) {
                    DefaultLogger.w("SettingsSyncWorker", "Network condition not available!");
                    ListenableWorker.Result failure = ListenableWorker.Result.failure();
                    Intrinsics.checkNotNullExpressionValue(failure, "failure()");
                    return failure;
                }
            } else if (!BaseNetworkUtils.isNetworkConnected()) {
                DefaultLogger.w("SettingsSyncWorker", "Network condition not available!");
                ListenableWorker.Result failure2 = ListenableWorker.Result.failure();
                Intrinsics.checkNotNullExpressionValue(failure2, "failure()");
                return failure2;
            }
            long lastDownloadTime = GalleryPreferences.SettingsSync.getLastDownloadTime();
            if (lastDownloadTime > 0 && System.currentTimeMillis() - lastDownloadTime < TimeUnit.DAYS.toMillis(1L)) {
                DefaultLogger.d("SettingsSyncWorker", "too often");
                ListenableWorker.Result failure3 = ListenableWorker.Result.failure();
                Intrinsics.checkNotNullExpressionValue(failure3, "failure()");
                return failure3;
            }
            GallerySettingsSyncHelper.doDownload(GalleryApp.sGetAndroidContext());
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        /* compiled from: SettingsSyncWorkerProvider.kt */
        /* loaded from: classes2.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }
    }

    /* compiled from: SettingsSyncWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
