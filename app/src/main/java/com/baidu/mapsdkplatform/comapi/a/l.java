package com.baidu.mapsdkplatform.comapi.a;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.view.animation.Interpolator;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;

/* loaded from: classes.dex */
public class l extends c {
    private Animator a = null;
    private long b = 0;
    private Interpolator c = null;
    private Animation.AnimationListener d = null;
    private int e = 1;
    private int f = 0;
    private Object[] g;

    @TargetApi(11)
    /* loaded from: classes.dex */
    public class a implements TypeEvaluator {
        public a() {
        }

        @Override // android.animation.TypeEvaluator
        public Object evaluate(float f, Object obj, Object obj2) {
            LatLng latLng = (LatLng) obj;
            LatLng latLng2 = (LatLng) obj2;
            double d = latLng.longitude;
            double d2 = f;
            double d3 = d + ((latLng2.longitude - d) * d2);
            double d4 = latLng.latitude;
            return new LatLng(d4 + (d2 * (latLng2.latitude - d4)), d3);
        }
    }

    @TargetApi(11)
    /* loaded from: classes.dex */
    public class b implements TypeEvaluator {
        public b() {
        }

        @Override // android.animation.TypeEvaluator
        public Object evaluate(float f, Object obj, Object obj2) {
            Point point = (Point) obj;
            Point point2 = (Point) obj2;
            int i = point.x;
            float f2 = i + ((point2.x - i) * f);
            int i2 = point.y;
            return new Point((int) f2, (int) (i2 + (f * (point2.y - i2))));
        }
    }

    public l(Point... pointArr) {
        this.g = pointArr;
    }

    public l(LatLng... latLngArr) {
        this.g = latLngArr;
    }

    @TargetApi(11)
    public ObjectAnimator a(Marker marker) {
        ObjectAnimator ofObject;
        if (marker.isFixed()) {
            if (!(this.g[0] instanceof Point)) {
                throw new ClassCastException("BDMapSDKException: if the marker is fixed on screen, the parameters of Transformation must be android.graphics.Point");
            }
            ofObject = ObjectAnimator.ofObject(marker, "fixedScreenPosition", new b(), this.g);
        } else if (!(this.g[0] instanceof LatLng)) {
            throw new ClassCastException("BDMapSDKException: if the marker isn't fixed on screen, the parameters of Transformation must be Latlng");
        } else {
            ofObject = ObjectAnimator.ofObject(marker, "position", new a(), this.g);
        }
        if (ofObject != null) {
            ofObject.setRepeatCount(this.f);
            ofObject.setRepeatMode(c());
            ofObject.setDuration(this.b);
            Interpolator interpolator = this.c;
            if (interpolator != null) {
                ofObject.setInterpolator(interpolator);
            }
        }
        return ofObject;
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    @TargetApi(11)
    public void a() {
        Animator animator = this.a;
        if (animator == null) {
            return;
        }
        animator.start();
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    public void a(int i) {
        this.e = i;
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    public void a(long j) {
        if (j < 0) {
            j = 0;
        }
        this.b = j;
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    @TargetApi(11)
    public void a(Animator animator) {
        if (animator == null) {
            return;
        }
        animator.addListener(new m(this));
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    public void a(Interpolator interpolator) {
        this.c = interpolator;
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    public void a(Animation.AnimationListener animationListener) {
        this.d = animationListener;
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    @TargetApi(11)
    public void a(Marker marker, Animation animation) {
        ObjectAnimator a2 = a(marker);
        this.a = a2;
        a(a2);
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    @TargetApi(11)
    public void b() {
        Animator animator = this.a;
        if (animator != null) {
            animator.cancel();
            this.a = null;
        }
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    public void b(int i) {
        if (i > 0 || i == -1) {
            this.f = i;
        }
    }

    public int c() {
        return this.e;
    }

    @Override // com.baidu.mapsdkplatform.comapi.a.c
    public void c(int i) {
    }
}
