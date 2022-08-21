package com.baidu.platform.comapi.map.b;

import com.baidu.platform.comapi.map.b.d;

/* loaded from: classes.dex */
/* synthetic */ class e {
    public static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[d.a.values().length];
        a = iArr;
        try {
            iArr[d.a.MOVE.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[d.a.FLING.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[d.a.ZOOM_OUT.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[d.a.ZOOM_IN.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            a[d.a.ROTATE.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            a[d.a.DOUBLE_CLICK_ZOOM_IN.ordinal()] = 6;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            a[d.a.TWO_CLICK_ZOOM_OUT.ordinal()] = 7;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            a[d.a.MOVE_OVERLOOK.ordinal()] = 8;
        } catch (NoSuchFieldError unused8) {
        }
    }
}
