package com.miui.gallery.vlog.clip.single.seekbar;

/* loaded from: classes2.dex */
public class ExtractFrameStrategy {
    public boolean mAccurate;
    public double mSpeed = 1.0d;
    public int mTotalTimeMillis;

    public long adjustTime(int i) {
        int i2 = this.mTotalTimeMillis;
        if (i2 == 0) {
            return 0L;
        }
        int i3 = i2 / 50;
        return (long) ((i / i3) * i3 * this.mSpeed);
    }

    public void setTotalTimeMillis(int i) {
        this.mTotalTimeMillis = i;
        this.mAccurate = i <= 3000;
    }

    public void setSpeed(double d) {
        this.mSpeed = d;
    }

    public boolean isAccurate() {
        return this.mAccurate;
    }
}
