package com.jakewharton.picnic;

import java.util.Arrays;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: IntCounts.kt */
/* loaded from: classes.dex */
public final class IntCounts {
    public int[] data;
    public int size;

    public IntCounts(int i) {
        this.data = new int[i];
    }

    public /* synthetic */ IntCounts(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 10 : i);
    }

    public final int getSize() {
        return this.size;
    }

    public final int get(int i) {
        if (i >= this.size) {
            return 0;
        }
        return this.data[i];
    }

    public final void set(int i, int i2) {
        int i3 = i + 1;
        int[] iArr = this.data;
        if (i3 > iArr.length) {
            int[] copyOf = Arrays.copyOf(iArr, iArr.length * 2);
            Intrinsics.checkNotNullExpressionValue(copyOf, "java.util.Arrays.copyOf(this, newSize)");
            this.data = copyOf;
        }
        this.data[i] = i2;
        this.size = RangesKt___RangesKt.coerceAtLeast(this.size, i3);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i = this.size;
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 > 0) {
                sb.append(", ");
            }
            sb.append(this.data[i2]);
        }
        sb.append(']');
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder().apply(builderAction).toString()");
        return sb2;
    }
}
