package com.miui.gallery.util;

import com.miui.gallery.concurrent.ThreadPool;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class DebugUtil$$ExternalSyntheticLambda0 implements ThreadPool.Job {
    public static final /* synthetic */ DebugUtil$$ExternalSyntheticLambda0 INSTANCE = new DebugUtil$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run */
    public final Object mo1807run(ThreadPool.JobContext jobContext) {
        Object lambda$dumpDatabaseInfo$3;
        lambda$dumpDatabaseInfo$3 = DebugUtil.lambda$dumpDatabaseInfo$3(jobContext);
        return lambda$dumpDatabaseInfo$3;
    }
}
