package com.miui.gallery.util.assistant;

/* loaded from: classes2.dex */
public class FlagUtil {
    public static boolean hasFlag(int i, int i2) {
        return (i & i2) > 0;
    }

    public static int setFlag(int i, int i2) {
        return i | i2;
    }
}
