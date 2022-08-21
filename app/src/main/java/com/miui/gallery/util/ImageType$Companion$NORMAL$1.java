package com.miui.gallery.util;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

/* compiled from: ImageType.kt */
/* loaded from: classes2.dex */
public final class ImageType$Companion$NORMAL$1 extends Lambda implements Function2<Integer, Integer, Boolean> {
    public static final ImageType$Companion$NORMAL$1 INSTANCE = new ImageType$Companion$NORMAL$1();

    public ImageType$Companion$NORMAL$1() {
        super(2);
    }

    public final Boolean invoke(int i, int i2) {
        return Boolean.TRUE;
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Boolean invoke(Integer num, Integer num2) {
        return invoke(num.intValue(), num2.intValue());
    }
}
