package com.baidu.mapsdkplatform.comapi;

import com.baidu.mapsdkplatform.comapi.NativeLoader;

/* loaded from: classes.dex */
/* synthetic */ class e {
    public static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[NativeLoader.a.values().length];
        a = iArr;
        try {
            iArr[NativeLoader.a.ARM64.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[NativeLoader.a.ARMV7.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[NativeLoader.a.ARMEABI.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[NativeLoader.a.X86_64.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            a[NativeLoader.a.X86.ordinal()] = 5;
        } catch (NoSuchFieldError unused5) {
        }
    }
}
