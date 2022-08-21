package com.miui.gallery.map.cluster;

import java.util.concurrent.ThreadFactory;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class DefaultClusterRenderer$$ExternalSyntheticLambda4 implements ThreadFactory {
    public static final /* synthetic */ DefaultClusterRenderer$$ExternalSyntheticLambda4 INSTANCE = new DefaultClusterRenderer$$ExternalSyntheticLambda4();

    @Override // java.util.concurrent.ThreadFactory
    public final Thread newThread(Runnable runnable) {
        Thread lambda$new$0;
        lambda$new$0 = DefaultClusterRenderer.lambda$new$0(runnable);
        return lambda$new$0;
    }
}
