package androidx.constraintlayout.motion.utils;

import android.view.View;
import androidx.constraintlayout.core.motion.utils.KeyCache;
import androidx.constraintlayout.core.motion.utils.TimeCycleSplineSet;

/* loaded from: classes.dex */
public abstract class ViewTimeCycle extends TimeCycleSplineSet {
    public abstract boolean setProperty(View view, float t, long time, KeyCache cache);

    public float get(float pos, long time, View view, KeyCache cache) {
        this.mCurveFit.getPos(pos, this.mCache);
        float[] fArr = this.mCache;
        float f = fArr[1];
        if (f == 0.0f) {
            this.mContinue = false;
            return fArr[2];
        } else if (Float.isNaN(this.last_cycle)) {
            throw null;
        } else {
            this.last_cycle = (float) ((this.last_cycle + (((time - this.last_time) * 1.0E-9d) * f)) % 1.0d);
            throw null;
        }
    }

    /* loaded from: classes.dex */
    public static class PathRotate extends ViewTimeCycle {
        @Override // androidx.constraintlayout.motion.utils.ViewTimeCycle
        public boolean setProperty(View view, float t, long time, KeyCache cache) {
            return this.mContinue;
        }

        public boolean setPathRotate(View view, KeyCache cache, float t, long time, double dx, double dy) {
            view.setRotation(get(t, time, view, cache) + ((float) Math.toDegrees(Math.atan2(dy, dx))));
            return this.mContinue;
        }
    }
}
