package com.miui.gallery.util.portJava;

/* loaded from: classes2.dex */
public abstract class Rectangle2D extends RectangularShape {
    public boolean contains(double d, double d2) {
        double x = getX();
        double y = getY();
        return d >= x && d2 >= y && d < x + getWidth() && d2 < y + getHeight();
    }

    public int hashCode() {
        long doubleToLongBits = Double.doubleToLongBits(getX()) + (Double.doubleToLongBits(getY()) * 37) + (Double.doubleToLongBits(getWidth()) * 43) + (Double.doubleToLongBits(getHeight()) * 47);
        return ((int) (doubleToLongBits >> 32)) ^ ((int) doubleToLongBits);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Rectangle2D)) {
            return false;
        }
        Rectangle2D rectangle2D = (Rectangle2D) obj;
        return getX() == rectangle2D.getX() && getY() == rectangle2D.getY() && getWidth() == rectangle2D.getWidth() && getHeight() == rectangle2D.getHeight();
    }
}
