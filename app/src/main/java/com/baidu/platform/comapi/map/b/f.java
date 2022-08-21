package com.baidu.platform.comapi.map.b;

import android.os.Build;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.baidu.platform.comapi.map.b.a;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
public class f {
    public final int a;
    public final int b;
    private VelocityTracker c;

    public f() {
        int scaledMaximumFlingVelocity;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(com.baidu.platform.comapi.b.e());
        if (viewConfiguration == null) {
            this.b = ViewConfiguration.getMinimumFlingVelocity();
            scaledMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        } else {
            this.b = viewConfiguration.getScaledMinimumFlingVelocity();
            scaledMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        }
        this.a = scaledMaximumFlingVelocity;
    }

    public void a() {
        this.c = VelocityTracker.obtain();
    }

    public void a(MotionEvent motionEvent) {
        VelocityTracker velocityTracker = this.c;
        if (velocityTracker == null) {
            this.c = VelocityTracker.obtain();
        } else {
            velocityTracker.addMovement(motionEvent);
        }
    }

    public void b() {
        VelocityTracker velocityTracker = this.c;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.c = null;
        }
    }

    public Pair<a.d, a.d> c() {
        float xVelocity;
        float yVelocity;
        float xVelocity2;
        float yVelocity2;
        VelocityTracker velocityTracker = this.c;
        if (velocityTracker == null) {
            return new Pair<>(new a.d((double) SearchStatUtils.POW, (double) SearchStatUtils.POW), new a.d((double) SearchStatUtils.POW, (double) SearchStatUtils.POW));
        }
        velocityTracker.computeCurrentVelocity(1000, this.a);
        if (Build.VERSION.SDK_INT < 8) {
            xVelocity = this.c.getXVelocity();
            yVelocity = this.c.getYVelocity();
            xVelocity2 = this.c.getXVelocity();
            yVelocity2 = this.c.getYVelocity();
        } else {
            xVelocity = this.c.getXVelocity(0);
            yVelocity = this.c.getYVelocity(0);
            xVelocity2 = this.c.getXVelocity(1);
            yVelocity2 = this.c.getYVelocity(1);
        }
        return new Pair<>(new a.d(xVelocity, yVelocity), new a.d(xVelocity2, yVelocity2));
    }
}
