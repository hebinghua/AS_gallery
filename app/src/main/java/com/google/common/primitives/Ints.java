package com.google.common.primitives;

/* loaded from: classes.dex */
public final class Ints extends IntsMethodsForWeb {
    public static int saturatedCast(long j) {
        if (j > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        if (j >= -2147483648L) {
            return (int) j;
        }
        return Integer.MIN_VALUE;
    }
}
