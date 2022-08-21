package com.miui.gallery.app.screenChange;

/* loaded from: classes.dex */
public class ScreenSize {
    public int mScreenHeight;
    public int mScreenWidth;

    public ScreenSize(int i, int i2) {
        this.mScreenWidth = i;
        this.mScreenHeight = i2;
    }

    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    public boolean isWindowHorizontalLarge() {
        return this.mScreenWidth >= ScreenConfig.WINDOW_HORIZONTAL_LARGE;
    }

    public String toString() {
        return "ScreenSize{mScreenWidth=" + this.mScreenWidth + ", mScreenHeight=" + this.mScreenHeight + '}';
    }
}
