package com.miui.gallery.signature.core.pen;

import com.miui.gallery.signature.core.bean.ControlPoint;

/* loaded from: classes2.dex */
public class BezierControl {
    public ControlPoint mSource = new ControlPoint();
    public ControlPoint mControl = new ControlPoint();
    public ControlPoint mDestination = new ControlPoint();
    public ControlPoint mNextControl = new ControlPoint();

    public final float getMid(float f, float f2) {
        return (f + f2) / 2.0f;
    }

    public final double getWidth(double d, double d2, double d3) {
        return d + ((d2 - d) * d3);
    }

    public void init(ControlPoint controlPoint, ControlPoint controlPoint2) {
        init(controlPoint.x, controlPoint.y, controlPoint.width, controlPoint2.x, controlPoint2.y, controlPoint2.width);
    }

    public void init(float f, float f2, float f3, float f4, float f5, float f6) {
        this.mSource.set(f, f2, f3);
        float mid = getMid(f, f4);
        float mid2 = getMid(f2, f5);
        float mid3 = getMid(f3, f6);
        this.mDestination.set(mid, mid2, mid3);
        this.mControl.set(getMid(f, mid), getMid(f2, mid2), getMid(f3, mid3));
        this.mNextControl.set(f4, f5, f6);
    }

    public void addNode(ControlPoint controlPoint) {
        addNode(controlPoint.x, controlPoint.y, controlPoint.width);
    }

    public void addNode(float f, float f2, float f3) {
        this.mSource.set(this.mDestination);
        this.mControl.set(this.mNextControl);
        this.mDestination.set(getMid(this.mNextControl.x, f), getMid(this.mNextControl.y, f2), getMid(this.mNextControl.width, f3));
        this.mNextControl.set(f, f2, f3);
    }

    public void addNodeUp(ControlPoint controlPoint) {
        addNodeUp(controlPoint.x, controlPoint.y, controlPoint.width);
    }

    public void addNodeUp(float f, float f2, float f3) {
        this.mSource.set(this.mDestination);
        this.mControl.set(this.mNextControl);
        this.mDestination.set(getMid(this.mNextControl.x, f), getMid(this.mNextControl.y, f2), f3);
        this.mNextControl.set(f, f2, f3);
    }

    public ControlPoint getPoint(double d) {
        return new ControlPoint((float) getX(d), (float) getY(d), (float) getW(d));
    }

    public final double getX(double d) {
        return getValue(this.mSource.x, this.mControl.x, this.mDestination.x, d);
    }

    public final double getY(double d) {
        return getValue(this.mSource.y, this.mControl.y, this.mDestination.y, d);
    }

    public final double getW(double d) {
        return getWidth(this.mSource.width, this.mDestination.width, d);
    }

    public final double getValue(double d, double d2, double d3, double d4) {
        double d5 = 1.0d - d4;
        return (Math.pow(d5, 2.0d) * d) + (d4 * 2.0d * d5 * d2) + (Math.pow(d4, 2.0d) * d3);
    }
}
