package com.miui.gallery.job.workers;

import android.content.Context;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CardUpdateWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.CardUpdate")
/* loaded from: classes2.dex */
public final class CardUpdateWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(CardUpdateWorker.class, 1L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiresDeviceIdle(true).setRequiresBatteryNotLow(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d()\n            ).build()");
        return build;
    }

    /* compiled from: CardUpdateWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class CardUpdateWorker extends TrackedWorker {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CardUpdateWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            if (MediaFeatureManager.isDeviceSupportStoryFunction()) {
                CardManager.getInstance().triggerScenarios();
                CardManager.getInstance().updateCardCovers();
            }
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }
    }
}
