package com.miui.gallery.util;

import com.miui.gallery.concurrent.ThreadPool;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class GalleryDateUtils$$ExternalSyntheticLambda0 implements ThreadPool.Job {
    public static final /* synthetic */ GalleryDateUtils$$ExternalSyntheticLambda0 INSTANCE = new GalleryDateUtils$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run */
    public final Object mo1807run(ThreadPool.JobContext jobContext) {
        Void clearCache;
        clearCache = GalleryDateUtils.clearCache();
        return clearCache;
    }
}
