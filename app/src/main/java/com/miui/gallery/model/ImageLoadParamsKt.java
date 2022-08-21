package com.miui.gallery.model;

import com.miui.gallery.model.ImageLoadParams;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ImageLoadParams.kt */
/* loaded from: classes2.dex */
public final class ImageLoadParamsKt {
    public static final /* synthetic */ ImageLoadParams ImageLoadParams(Function1 initializer) {
        Intrinsics.checkNotNullParameter(initializer, "initializer");
        ImageLoadParams.Builder builder = new ImageLoadParams.Builder();
        initializer.mo2577invoke(builder);
        return builder.build();
    }
}
