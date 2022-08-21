package com.miui.gallery.job.workers;

import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

/* compiled from: RequestSyncWorkerProvider.kt */
/* loaded from: classes2.dex */
public final class RequestSyncWorkerProvider$RequestSyncWorker$doWork$1 extends Lambda implements Function1<SyncRequest.Builder, Unit> {
    public static final RequestSyncWorkerProvider$RequestSyncWorker$doWork$1 INSTANCE = new RequestSyncWorkerProvider$RequestSyncWorker$doWork$1();

    public RequestSyncWorkerProvider$RequestSyncWorker$doWork$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo2577invoke(SyncRequest.Builder builder) {
        invoke2(builder);
        return Unit.INSTANCE;
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2(SyncRequest.Builder SyncRequest) {
        Intrinsics.checkNotNullParameter(SyncRequest, "$this$SyncRequest");
        SyncRequest.m697setSyncType(SyncType.NORMAL);
        SyncRequest.m696setSyncReason(Long.MAX_VALUE);
    }
}
