package com.miui.gallery.cloud.base;

import com.miui.gallery.cloud.base.SyncRequest;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SyncRequest.kt */
/* loaded from: classes.dex */
public final class SyncRequestKt {
    public static final /* synthetic */ SyncRequest SyncRequest(Function1 initializer) {
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        SyncRequest.Builder builder = new SyncRequest.Builder();
        initializer.mo2577invoke(builder);
        return builder.build();
    }
}
