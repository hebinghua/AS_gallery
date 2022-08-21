package com.miui.gallery.util;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;

/* compiled from: ImageType.kt */
/* loaded from: classes2.dex */
public final class ImageType {
    public final boolean isHighResolution;
    public final boolean isNavMapSupported;
    public final String name;
    public final int ordinal;
    public final Function2<Integer, Integer, Boolean> predicate;
    public static final Companion Companion = new Companion(null);
    public static final ImageType HIGH_RESOLUTION_108M = new ImageType("HIGH_RESOLUTION_108MP", 0, true, true, ImageType$Companion$HIGH_RESOLUTION_108M$1.INSTANCE);
    public static final ImageType HIGH_RESOLUTION_64M = new ImageType("HIGH_RESOLUTION_64MP", 1, true, false, ImageType$Companion$HIGH_RESOLUTION_64M$1.INSTANCE, 8, null);
    public static final ImageType HIGH_RESOLUTION = new ImageType("HIGH_RESOLUTION_AT_LEAST_24MP", 2, true, false, ImageType$Companion$HIGH_RESOLUTION$1.INSTANCE, 8, null);
    public static final ImageType NORMAL = new ImageType("NORMAL", 3, false, false, ImageType$Companion$NORMAL$1.INSTANCE, 12, null);

    public static final ImageType of(int i, int i2) {
        return Companion.of(i, i2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ImageType(String str, int i, boolean z, boolean z2, Function2<? super Integer, ? super Integer, Boolean> function2) {
        this.name = str;
        this.ordinal = i;
        this.isHighResolution = z;
        this.isNavMapSupported = z2;
        this.predicate = function2;
    }

    public /* synthetic */ ImageType(String str, int i, boolean z, boolean z2, Function2 function2, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, i, (i2 & 4) != 0 ? false : z, (i2 & 8) != 0 ? false : z2, function2);
    }

    public final String getName() {
        return this.name;
    }

    public final boolean isHighResolution() {
        return this.isHighResolution;
    }

    public final boolean isNavMapSupported() {
        return this.isNavMapSupported;
    }

    public String toString() {
        return "ImageType{ordinal=" + this.ordinal + ", name='" + this.name + "', isHighResolution=" + this.isHighResolution + ", isNavMapSupported=" + this.isNavMapSupported + '}';
    }

    /* compiled from: ImageType.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final ImageType of(int i, int i2) {
            ImageType[] values = values();
            int length = values.length;
            int i3 = 0;
            while (i3 < length) {
                ImageType imageType = values[i3];
                i3++;
                if (((Boolean) imageType.predicate.invoke(Integer.valueOf(i), Integer.valueOf(i2))).booleanValue()) {
                    return imageType;
                }
            }
            return ImageType.NORMAL;
        }

        public final ImageType[] values() {
            return new ImageType[]{ImageType.HIGH_RESOLUTION_108M, ImageType.HIGH_RESOLUTION_64M, ImageType.HIGH_RESOLUTION, ImageType.NORMAL};
        }
    }
}
