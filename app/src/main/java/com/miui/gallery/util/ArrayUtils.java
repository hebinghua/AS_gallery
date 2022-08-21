package com.miui.gallery.util;

import java.lang.reflect.Array;

/* loaded from: classes2.dex */
public class ArrayUtils {
    public static <T> T[] concat(T[]... tArr) {
        T[] tArr2;
        if (tArr == null) {
            return null;
        }
        int i = 0;
        for (T[] tArr3 : tArr) {
            if (tArr3 != null) {
                i += tArr3.length;
            }
        }
        Class<?> cls = tArr[0].getClass();
        if (cls == Object[].class) {
            tArr2 = (T[]) new Object[i];
        } else {
            tArr2 = (T[]) ((Object[]) Array.newInstance(cls.getComponentType(), i));
        }
        int i2 = 0;
        for (T[] tArr4 : tArr) {
            if (tArr4 != null) {
                System.arraycopy(tArr4, 0, tArr2, i2, tArr4.length);
                i2 += tArr4.length;
            }
        }
        return tArr2;
    }
}
