package com.miui.gallery.signature.core.bean;

/* loaded from: classes2.dex */
public class PointElement {
    public long tempStamp;
    public int touchType;
    public float x;
    public float y;

    public PointElement() {
    }

    public PointElement(float f, float f2, long j) {
        this.x = f;
        this.y = f2;
        this.tempStamp = j;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PointElement pointElement = (PointElement) obj;
        return Float.compare(pointElement.x, this.x) == 0 && Float.compare(pointElement.y, this.y) == 0 && this.tempStamp == pointElement.tempStamp && this.touchType == pointElement.touchType;
    }

    public int hashCode() {
        float f = this.x;
        int i = 0;
        int floatToIntBits = (f != 0.0f ? Float.floatToIntBits(f) : 0) * 31;
        float f2 = this.y;
        if (f2 != 0.0f) {
            i = Float.floatToIntBits(f2);
        }
        long j = this.tempStamp;
        return ((((floatToIntBits + i) * 31) + ((int) (j ^ (j >>> 32)))) * 31) + this.touchType;
    }
}
