package com.miui.gallery.widget.recyclerview.grouped;

/* loaded from: classes3.dex */
public class GroupedAdapterHelper {
    public static int getPackedPositionChild(long j) {
        return (int) (j >>> 32);
    }

    public static long getPackedPositionForChild(int i, int i2) {
        return (i & 4294967295L) | (i2 << 32);
    }

    public static long getPackedPositionForGroup(int i) {
        return (i & 4294967295L) | (-4294967296L);
    }

    public static int getPackedPositionGroup(long j) {
        return (int) (j & 4294967295L);
    }
}
