package com.miui.gallery.util.portJava;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class Rectangle extends Rectangle2D implements Serializable {
    private static final long serialVersionUID = -4345857070255674764L;
    public int height;
    public int width;
    public int x;
    public int y;

    public Rectangle() {
        this(0, 0, 0, 0);
    }

    public Rectangle(int i, int i2, int i3, int i4) {
        this.x = i;
        this.y = i2;
        this.width = i3;
        this.height = i4;
    }

    @Override // com.miui.gallery.util.portJava.RectangularShape
    public double getX() {
        return this.x;
    }

    @Override // com.miui.gallery.util.portJava.RectangularShape
    public double getY() {
        return this.y;
    }

    @Override // com.miui.gallery.util.portJava.RectangularShape
    public double getWidth() {
        return this.width;
    }

    @Override // com.miui.gallery.util.portJava.RectangularShape
    public double getHeight() {
        return this.height;
    }

    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
    }

    public boolean contains(int i, int i2) {
        return inside(i, i2);
    }

    @Deprecated
    public boolean inside(int i, int i2) {
        int i3 = this.width;
        int i4 = this.height;
        if ((i3 | i4) < 0) {
            return false;
        }
        int i5 = this.x;
        int i6 = this.y;
        if (i < i5 || i2 < i6) {
            return false;
        }
        int i7 = i3 + i5;
        int i8 = i4 + i6;
        if (i7 >= i5 && i7 <= i) {
            return false;
        }
        return i8 < i6 || i8 > i2;
    }

    @Override // com.miui.gallery.util.portJava.Rectangle2D
    public boolean equals(Object obj) {
        if (obj instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) obj;
            return this.x == rectangle.x && this.y == rectangle.y && this.width == rectangle.width && this.height == rectangle.height;
        }
        return super.equals(obj);
    }

    public String toString() {
        return getClass().getName() + "[x=" + this.x + ",y=" + this.y + ",width=" + this.width + ",height=" + this.height + "]";
    }
}
