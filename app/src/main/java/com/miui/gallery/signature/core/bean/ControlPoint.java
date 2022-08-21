package com.miui.gallery.signature.core.bean;

/* loaded from: classes2.dex */
public class ControlPoint {
    public int alpha = 255;
    public long time;
    public float width;
    public float x;
    public float y;

    public ControlPoint() {
    }

    public ControlPoint(float f, float f2) {
        this.x = f;
        this.y = f2;
    }

    public ControlPoint(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.width = f3;
    }

    public void set(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.width = f3;
    }

    public void set(ControlPoint controlPoint) {
        set(controlPoint.x, controlPoint.y, controlPoint.width);
    }
}
