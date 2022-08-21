package com.miui.gallery.util.portJava;

/* loaded from: classes2.dex */
public abstract class RectangularShape implements Cloneable {
    public abstract double getHeight();

    public abstract double getWidth();

    public abstract double getX();

    public abstract double getY();

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }
}
