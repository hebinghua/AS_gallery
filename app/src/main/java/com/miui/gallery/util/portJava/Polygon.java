package com.miui.gallery.util.portJava;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class Polygon implements Serializable {
    private static final long serialVersionUID = -6460061437900069969L;
    public Rectangle bounds;
    public int npoints;
    public int[] xpoints = new int[4];
    public int[] ypoints = new int[4];

    public void calculateBounds(int[] iArr, int[] iArr2, int i) {
        int i2 = Integer.MIN_VALUE;
        int i3 = Integer.MAX_VALUE;
        int i4 = Integer.MIN_VALUE;
        int i5 = Integer.MAX_VALUE;
        for (int i6 = 0; i6 < i; i6++) {
            int i7 = iArr[i6];
            i5 = Math.min(i5, i7);
            i2 = Math.max(i2, i7);
            int i8 = iArr2[i6];
            i3 = Math.min(i3, i8);
            i4 = Math.max(i4, i8);
        }
        this.bounds = new Rectangle(i5, i3, i2 - i5, i4 - i3);
    }

    @Deprecated
    public Rectangle getBoundingBox() {
        int i = this.npoints;
        if (i == 0) {
            return new Rectangle();
        }
        if (this.bounds == null) {
            calculateBounds(this.xpoints, this.ypoints, i);
        }
        return this.bounds.getBounds();
    }

    public boolean contains(int i, int i2) {
        return contains(i, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x008c, code lost:
        if (r9 >= ((r13 / (r7 - r12)) * (r5 - r11))) goto L11;
     */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0065  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean contains(double r21, double r23) {
        /*
            Method dump skipped, instructions count: 164
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.portJava.Polygon.contains(double, double):boolean");
    }
}
