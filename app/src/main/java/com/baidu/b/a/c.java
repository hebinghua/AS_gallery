package com.baidu.b.a;

import java.util.Arrays;

/* loaded from: classes.dex */
class c {
    public static void a(byte[] bArr, byte[] bArr2, int i) {
        if (i >= 0) {
            if (bArr == null || bArr.length == 0) {
                throw new IllegalArgumentException("dst array should not be null or empty");
            }
            if (bArr2 == null || bArr2.length == 0) {
                throw new IllegalArgumentException("src array should not be null or empty");
            }
            if (bArr.length < bArr2.length) {
                throw new IllegalArgumentException("dst array length should be longer than:" + bArr2.length);
            }
            if (bArr.length >= bArr2.length + i) {
                System.arraycopy(bArr2, 0, bArr, i, bArr2.length);
                return;
            }
            throw new IllegalArgumentException("start should be less than:" + (bArr.length - bArr2.length));
        }
        throw new IllegalArgumentException("start should be more than zero!");
    }

    public static byte[] a(byte[] bArr, int i) {
        if (bArr == null || bArr.length == 0) {
            throw new IllegalArgumentException("original array should not be null or empty");
        }
        if (i < 0) {
            throw new IllegalArgumentException("length should be more than zero!");
        }
        return Arrays.copyOf(bArr, i);
    }
}
