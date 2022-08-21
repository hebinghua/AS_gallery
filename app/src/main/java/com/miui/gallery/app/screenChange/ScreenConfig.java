package com.miui.gallery.app.screenChange;

/* loaded from: classes.dex */
public class ScreenConfig {
    public static int WINDOW_HORIZONTAL_LARGE = 637;
    public int mOrientation;
    public int mScreenHeight;
    public int mScreenLayout;
    public int mScreenWidth;

    public ScreenConfig(int i, int i2, int i3, int i4) {
        this.mScreenWidth = i;
        this.mScreenHeight = i2;
        this.mOrientation = i3;
        this.mScreenLayout = i4;
    }

    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getScreenLayout() {
        return this.mScreenLayout;
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public int mOrientation;
        public int mScreenHeight;
        public int mScreenLayout;
        public int mScreenWidth;

        public Builder setScreenWidth(int i) {
            this.mScreenWidth = i;
            return this;
        }

        public Builder setScreenHeight(int i) {
            this.mScreenHeight = i;
            return this;
        }

        public Builder setOrientation(int i) {
            this.mOrientation = i;
            return this;
        }

        public Builder setScreenLayout(int i) {
            this.mScreenLayout = i;
            return this;
        }

        public ScreenConfig build() {
            return new ScreenConfig(this.mScreenWidth, this.mScreenHeight, this.mOrientation, this.mScreenLayout);
        }
    }

    public String toString() {
        return "ScreenConfig{mScreenWidth=" + this.mScreenWidth + ", mScreenHeight=" + this.mScreenHeight + ", mOrientation=" + this.mOrientation + ", mScreenLayout=" + this.mScreenLayout + '}';
    }
}
