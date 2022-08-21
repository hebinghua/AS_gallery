package com.miui.gallery.util;

import com.miui.gallery.concurrent.ThreadPool;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class DebugUtil$$ExternalSyntheticLambda1 implements ThreadPool.Job {
    public static final /* synthetic */ DebugUtil$$ExternalSyntheticLambda1 INSTANCE = new DebugUtil$$ExternalSyntheticLambda1();

    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run */
    public final Object mo1807run(ThreadPool.JobContext jobContext) {
        Object doExportDB;
        doExportDB = DebugUtil.doExportDB();
        return doExportDB;
    }
}
