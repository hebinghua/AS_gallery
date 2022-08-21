package com.miui.epoxy.utils;

/* loaded from: classes.dex */
public class EpoxyAdapterUtils {
    public static int hashInt(int i) {
        int i2 = i ^ (i << 13);
        int i3 = i2 ^ (i2 >>> 17);
        return i3 ^ (i3 << 5);
    }

    public static int calculateItemViewType(int i) {
        return hashInt(i);
    }
}
