package com.miui.gallery.cloud.jobs;

import android.content.Context;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.job.workers.TrackedWorker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: BackgroundDownloadWorker.kt */
/* loaded from: classes.dex */
public final class BackgroundDownloadWorker extends TrackedWorker {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public BackgroundDownloadWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(workerParams, "workerParams");
    }

    @Override // com.miui.gallery.job.workers.TrackedWorker
    public ListenableWorker.Result doWork() {
        if (BatchDownloadManager.canAutoDownload()) {
            BatchDownloadManager.getInstance().startBatchDownload(GalleryApp.sGetAndroidContext(), false);
        }
        ListenableWorker.Result success = ListenableWorker.Result.success();
        Intrinsics.checkNotNullExpressionValue(success, "success()");
        return success;
    }
}
