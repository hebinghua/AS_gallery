package miuix.animation.utils;

import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import miuix.animation.physics.PhysicsOperator;
import miuix.animation.styles.PropertyStyle;
import miuix.view.animation.BounceEaseInInterpolator;
import miuix.view.animation.BounceEaseInOutInterpolator;
import miuix.view.animation.BounceEaseOutInterpolator;
import miuix.view.animation.CubicEaseInInterpolator;
import miuix.view.animation.CubicEaseInOutInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.ExponentialEaseInInterpolator;
import miuix.view.animation.ExponentialEaseInOutInterpolator;
import miuix.view.animation.ExponentialEaseOutInterpolator;
import miuix.view.animation.QuadraticEaseInInterpolator;
import miuix.view.animation.QuadraticEaseInOutInterpolator;
import miuix.view.animation.QuadraticEaseOutInterpolator;
import miuix.view.animation.QuarticEaseInInterpolator;
import miuix.view.animation.QuarticEaseInOutInterpolator;
import miuix.view.animation.QuinticEaseInInterpolator;
import miuix.view.animation.QuinticEaseInOutInterpolator;
import miuix.view.animation.QuinticEaseOutInterpolator;
import miuix.view.animation.SineEaseInInterpolator;
import miuix.view.animation.SineEaseInOutInterpolator;
import miuix.view.animation.SineEaseOutInterpolator;

/* loaded from: classes3.dex */
public class EaseManager {
    public static final ConcurrentHashMap<Integer, TimeInterpolator> sInterpolatorCache = new ConcurrentHashMap<>();

    public static boolean isPhysicsStyle(int i) {
        return i < -1;
    }

    public static TimeInterpolator createTimeInterpolator(int i, float... fArr) {
        switch (i) {
            case -1:
            case 1:
                return new LinearInterpolator();
            case 0:
                return new SpringInterpolator().setDamping(fArr[0]).setResponse(fArr[1]);
            case 2:
                return new QuadraticEaseInInterpolator();
            case 3:
                return new QuadraticEaseOutInterpolator();
            case 4:
                return new QuadraticEaseInOutInterpolator();
            case 5:
                return new CubicEaseInInterpolator();
            case 6:
                return new CubicEaseOutInterpolator();
            case 7:
                return new CubicEaseInOutInterpolator();
            case 8:
                return new QuarticEaseInInterpolator();
            case 9:
                return new QuadraticEaseOutInterpolator();
            case 10:
                return new QuarticEaseInOutInterpolator();
            case 11:
                return new QuinticEaseInInterpolator();
            case 12:
                return new QuinticEaseOutInterpolator();
            case 13:
                return new QuinticEaseInOutInterpolator();
            case 14:
                return new SineEaseInInterpolator();
            case 15:
                return new SineEaseOutInterpolator();
            case 16:
                return new SineEaseInOutInterpolator();
            case 17:
                return new ExponentialEaseInInterpolator();
            case 18:
                return new ExponentialEaseOutInterpolator();
            case 19:
                return new ExponentialEaseInOutInterpolator();
            case 20:
                return new DecelerateInterpolator();
            case 21:
                return new AccelerateDecelerateInterpolator();
            case 22:
                return new AccelerateInterpolator();
            case 23:
                return new BounceInterpolator();
            case 24:
                return new BounceEaseInInterpolator();
            case 25:
                return new BounceEaseOutInterpolator();
            case 26:
                return new BounceEaseInOutInterpolator();
            default:
                return null;
        }
    }

    /* loaded from: classes3.dex */
    public static class EaseStyle {
        public volatile float[] factors;
        public final double[] parameters;
        public final int style;

        public EaseStyle(int i, float... fArr) {
            double[] dArr = {SearchStatUtils.POW, SearchStatUtils.POW};
            this.parameters = dArr;
            this.style = i;
            this.factors = fArr;
            setParameters(this, dArr);
        }

        public void setFactors(float... fArr) {
            this.factors = fArr;
            setParameters(this, this.parameters);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof EaseStyle)) {
                return false;
            }
            EaseStyle easeStyle = (EaseStyle) obj;
            return this.style == easeStyle.style && Arrays.equals(this.factors, easeStyle.factors);
        }

        public int hashCode() {
            return (Objects.hash(Integer.valueOf(this.style)) * 31) + Arrays.hashCode(this.factors);
        }

        public String toString() {
            return "EaseStyle{style=" + this.style + ", factors=" + Arrays.toString(this.factors) + ", parameters = " + Arrays.toString(this.parameters) + '}';
        }

        public static void setParameters(EaseStyle easeStyle, double[] dArr) {
            PhysicsOperator phyOperator = easeStyle == null ? null : PropertyStyle.getPhyOperator(easeStyle.style);
            if (phyOperator != null) {
                phyOperator.getParameters(easeStyle.factors, dArr);
            } else {
                Arrays.fill(dArr, (double) SearchStatUtils.POW);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class InterpolateEaseStyle extends EaseStyle {
        public long duration;

        public InterpolateEaseStyle(int i, float... fArr) {
            super(i, fArr);
            this.duration = 300L;
        }

        public InterpolateEaseStyle setDuration(long j) {
            this.duration = j;
            return this;
        }

        @Override // miuix.animation.utils.EaseManager.EaseStyle
        public String toString() {
            return "InterpolateEaseStyle{style=" + this.style + ", duration=" + this.duration + ", factors=" + Arrays.toString(this.factors) + '}';
        }
    }

    public static EaseStyle getStyle(int i, float... fArr) {
        if (i >= -1) {
            InterpolateEaseStyle interpolatorStyle = getInterpolatorStyle(i, fArr.length > 1 ? Arrays.copyOfRange(fArr, 1, fArr.length) : new float[0]);
            if (fArr.length > 0) {
                interpolatorStyle.setDuration((int) fArr[0]);
            }
            return interpolatorStyle;
        }
        return new EaseStyle(i, fArr);
    }

    public static TimeInterpolator getInterpolator(int i, float... fArr) {
        return getInterpolator(getInterpolatorStyle(i, fArr));
    }

    public static InterpolateEaseStyle getInterpolatorStyle(int i, float... fArr) {
        return new InterpolateEaseStyle(i, fArr);
    }

    public static TimeInterpolator getInterpolator(InterpolateEaseStyle interpolateEaseStyle) {
        if (interpolateEaseStyle != null) {
            ConcurrentHashMap<Integer, TimeInterpolator> concurrentHashMap = sInterpolatorCache;
            TimeInterpolator timeInterpolator = concurrentHashMap.get(Integer.valueOf(interpolateEaseStyle.style));
            if (timeInterpolator == null && (timeInterpolator = createTimeInterpolator(interpolateEaseStyle.style, interpolateEaseStyle.factors)) != null) {
                concurrentHashMap.put(Integer.valueOf(interpolateEaseStyle.style), timeInterpolator);
            }
            return timeInterpolator;
        }
        return null;
    }

    /* loaded from: classes3.dex */
    public static class SpringInterpolator implements TimeInterpolator {
        public float c;
        public float c2;
        public float k;
        public float r;
        public float w;
        public float damping = 0.95f;
        public float response = 0.6f;
        public float initial = -1.0f;
        public float c1 = -1.0f;
        public float m = 1.0f;

        public SpringInterpolator() {
            updateParameters();
        }

        @Override // android.animation.TimeInterpolator
        public float getInterpolation(float f) {
            return (float) ((Math.pow(2.718281828459045d, this.r * f) * ((this.c1 * Math.cos(this.w * f)) + (this.c2 * Math.sin(this.w * f)))) + 1.0d);
        }

        public SpringInterpolator setDamping(float f) {
            this.damping = f;
            updateParameters();
            return this;
        }

        public SpringInterpolator setResponse(float f) {
            this.response = f;
            updateParameters();
            return this;
        }

        public final void updateParameters() {
            double pow = Math.pow(6.283185307179586d / this.response, 2.0d);
            float f = this.m;
            float f2 = (float) (pow * f);
            this.k = f2;
            float f3 = (float) (((this.damping * 12.566370614359172d) * f) / this.response);
            this.c = f3;
            float f4 = this.m;
            float sqrt = ((float) Math.sqrt(((f * 4.0f) * f2) - (f3 * f3))) / (f4 * 2.0f);
            this.w = sqrt;
            float f5 = -((this.c / 2.0f) * f4);
            this.r = f5;
            this.c2 = (0.0f - (f5 * this.initial)) / sqrt;
        }
    }
}
