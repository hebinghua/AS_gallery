package io.reactivex.internal.util;

/* loaded from: classes3.dex */
public final class Pow2 {
    public static int roundToPowerOfTwo(int i) {
        return 1 << (32 - Integer.numberOfLeadingZeros(i - 1));
    }
}
