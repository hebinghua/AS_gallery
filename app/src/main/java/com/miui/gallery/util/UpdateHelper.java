package com.miui.gallery.util;

import java.util.Arrays;

/* loaded from: classes2.dex */
public class UpdateHelper {
    public boolean mUpdated = false;

    public int update(int i, int i2) {
        if (i != i2) {
            this.mUpdated = true;
            return i2;
        }
        return i;
    }

    public long update(long j, long j2) {
        if (j != j2) {
            this.mUpdated = true;
            return j2;
        }
        return j;
    }

    public double update(double d, double d2) {
        if (d != d2) {
            this.mUpdated = true;
            return d2;
        }
        return d;
    }

    public byte[] update(byte[] bArr, byte[] bArr2) {
        if (!Arrays.equals(bArr, bArr2)) {
            this.mUpdated = true;
            return bArr2;
        }
        return bArr;
    }

    public <T> T update(T t, T t2) {
        if (!Utils.equals(t, t2)) {
            this.mUpdated = true;
            return t2;
        }
        return t;
    }

    public boolean isUpdated() {
        return this.mUpdated;
    }
}
