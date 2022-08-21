package com.xiaomi.milab.videosdk;

/* loaded from: classes3.dex */
public class XmsNativeObject {
    public long mNativePtr = 0;

    public long getInternalObject() {
        return this.mNativePtr;
    }

    public boolean isNULL() {
        return this.mNativePtr == 0;
    }

    public int hashCode() {
        long j = this.mNativePtr;
        return 527 + ((int) (j ^ (j >>> 32)));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof XmsNativeObject)) {
            return false;
        }
        long j = ((XmsNativeObject) obj).mNativePtr;
        long j2 = this.mNativePtr;
        return j == j2 && j == j2;
    }
}
