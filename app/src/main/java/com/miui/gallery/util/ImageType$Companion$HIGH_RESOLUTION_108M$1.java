package com.miui.gallery.util;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: ImageType.kt */
/* loaded from: classes2.dex */
public final class ImageType$Companion$HIGH_RESOLUTION_108M$1 extends Lambda implements Function2<Integer, Integer, Boolean> {
    public static final ImageType$Companion$HIGH_RESOLUTION_108M$1 INSTANCE = new ImageType$Companion$HIGH_RESOLUTION_108M$1();

    public ImageType$Companion$HIGH_RESOLUTION_108M$1() {
        super(2);
    }

    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Boolean invoke(Integer num, Integer num2) {
        return invoke(num.intValue(), num2.intValue());
    }

    public final Boolean invoke(int i, int i2) {
        return Boolean.valueOf(GalleryPrimitivesKt.rangeIn(RangesKt___RangesKt.coerceAtLeast(i, i2), 12000, 100) && GalleryPrimitivesKt.rangeIn(RangesKt___RangesKt.coerceAtMost(i, i2), 9000, 100));
    }
}
