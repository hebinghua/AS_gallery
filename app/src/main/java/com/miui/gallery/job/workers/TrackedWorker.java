package com.miui.gallery.job.workers;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Keep;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: TrackedWorker.kt */
/* loaded from: classes2.dex */
public abstract class TrackedWorker extends ListenableWorker {
    public static final Companion Companion = new Companion(null);
    public SettableFuture<ListenableWorker.Result> mFuture;

    public static /* synthetic */ void $r8$lambda$K_19k101H5UCcicudO_P1xO4z38(TrackedWorker trackedWorker) {
        m1003startWork$lambda0(trackedWorker);
    }

    public abstract ListenableWorker.Result doWork();

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    @Keep
    @SuppressLint({"BanKeepAnnotation"})
    public TrackedWorker(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(workerParams, "workerParams");
    }

    public final SettableFuture<ListenableWorker.Result> getMFuture() {
        SettableFuture<ListenableWorker.Result> settableFuture = this.mFuture;
        if (settableFuture != null) {
            return settableFuture;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mFuture");
        return null;
    }

    public final void setMFuture(SettableFuture<ListenableWorker.Result> settableFuture) {
        Intrinsics.checkNotNullParameter(settableFuture, "<set-?>");
        this.mFuture = settableFuture;
    }

    @Override // androidx.work.ListenableWorker
    @SuppressLint({"RestrictedApi"})
    public final ListenableFuture<ListenableWorker.Result> startWork() {
        SettableFuture<ListenableWorker.Result> create = SettableFuture.create();
        Intrinsics.checkNotNullExpressionValue(create, "create()");
        setMFuture(create);
        getBackgroundExecutor().execute(new Runnable() { // from class: com.miui.gallery.job.workers.TrackedWorker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TrackedWorker.$r8$lambda$K_19k101H5UCcicudO_P1xO4z38(TrackedWorker.this);
            }
        });
        return getMFuture();
    }

    /* renamed from: startWork$lambda-0 */
    public static final void m1003startWork$lambda0(TrackedWorker this$0) {
        String str;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        try {
            ListenableWorker.Result doWork = this$0.doWork();
            this$0.getMFuture().set(doWork);
            if (doWork instanceof ListenableWorker.Result.Success) {
                str = "JobServiceDone";
            } else {
                str = doWork instanceof ListenableWorker.Result.Retry ? "JobServiceRescheduled" : "JobServiceFailed";
            }
            SamplingStatHelper.recordCountEvent("job_service", str, MapsKt__MapsJVMKt.mapOf(TuplesKt.to("name", this$0.getClass().getName())));
            DefaultLogger.i("TrackedWorker", ((Object) this$0.getClass().getSimpleName()) + " finished with result: " + doWork);
        } catch (Throwable th) {
            this$0.getMFuture().setException(th);
            SamplingStatHelper.recordCountEvent("job_service", "JobServiceFailed", MapsKt__MapsJVMKt.mapOf(TuplesKt.to("name", this$0.getClass().getName())));
            DefaultLogger.e("TrackedWorker", Intrinsics.stringPlus(this$0.getClass().getSimpleName(), " got error"), th);
        }
    }

    @Override // androidx.work.ListenableWorker
    public void onStopped() {
        super.onStopped();
        SamplingStatHelper.recordCountEvent("job_service", "JobServiceStopped", MapsKt__MapsJVMKt.mapOf(TuplesKt.to("name", getClass().getName())));
        DefaultLogger.w("TrackedWorker", Intrinsics.stringPlus(getClass().getSimpleName(), " was stopped"));
    }

    /* compiled from: TrackedWorker.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
