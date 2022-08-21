package androidx.constraintlayout.motion.widget;

import android.view.View;
import androidx.constraintlayout.core.motion.utils.Easing;
import androidx.constraintlayout.widget.ConstraintAttribute;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;

/* loaded from: classes.dex */
public class MotionPaths implements Comparable<MotionPaths> {
    public static String[] names = {"position", "x", "y", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT, "pathRotate"};
    public LinkedHashMap<String, ConstraintAttribute> attributes;
    public float height;
    public int mAnimateRelativeTo;
    public Easing mKeyFrameEasing;
    public int mMode;
    public int mPathMotionArc;
    public float mRelativeAngle;
    public MotionController mRelativeToController;
    public double[] mTempDelta;
    public double[] mTempValue;
    public float position;
    public float time;
    public float width;
    public float x;
    public float y;
    public int mDrawPath = 0;
    public float mPathRotate = Float.NaN;
    public float mProgress = Float.NaN;

    public MotionPaths() {
        int i = Key.UNSET;
        this.mPathMotionArc = i;
        this.mAnimateRelativeTo = i;
        this.mRelativeAngle = Float.NaN;
        this.mRelativeToController = null;
        this.attributes = new LinkedHashMap<>();
        this.mMode = 0;
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
    }

    public void getCenter(double p, int[] toUse, double[] data, float[] point, double[] vdata, float[] velocity) {
        float f;
        float f2 = this.x;
        float f3 = this.y;
        float f4 = this.width;
        float f5 = this.height;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        for (int i = 0; i < toUse.length; i++) {
            float f10 = (float) data[i];
            float f11 = (float) vdata[i];
            int i2 = toUse[i];
            if (i2 == 1) {
                f2 = f10;
                f6 = f11;
            } else if (i2 == 2) {
                f3 = f10;
                f8 = f11;
            } else if (i2 == 3) {
                f4 = f10;
                f7 = f11;
            } else if (i2 == 4) {
                f5 = f10;
                f9 = f11;
            }
        }
        float f12 = 2.0f;
        float f13 = (f7 / 2.0f) + f6;
        float f14 = (f9 / 2.0f) + f8;
        MotionController motionController = this.mRelativeToController;
        if (motionController != null) {
            float[] fArr = new float[2];
            float[] fArr2 = new float[2];
            motionController.getCenter(p, fArr, fArr2);
            float f15 = fArr[0];
            float f16 = fArr[1];
            float f17 = fArr2[0];
            float f18 = fArr2[1];
            double d = f2;
            double d2 = f3;
            f = f4;
            double d3 = f6;
            double d4 = f8;
            float sin = (float) (f17 + (Math.sin(d2) * d3) + (Math.cos(d2) * d4));
            f14 = (float) ((f18 - (d3 * Math.cos(d2))) + (Math.sin(d2) * d4));
            f13 = sin;
            f2 = (float) ((f15 + (Math.sin(d2) * d)) - (f4 / 2.0f));
            f3 = (float) ((f16 - (d * Math.cos(d2))) - (f5 / 2.0f));
            f12 = 2.0f;
        } else {
            f = f4;
        }
        point[0] = f2 + (f / f12) + 0.0f;
        point[1] = f3 + (f5 / f12) + 0.0f;
        velocity[0] = f13;
        velocity[1] = f14;
    }

    public void setView(float position, View view, int[] toUse, double[] data, double[] slope, double[] cycle, boolean mForceMeasure) {
        float f;
        boolean z;
        boolean z2;
        float f2;
        float f3 = this.x;
        float f4 = this.y;
        float f5 = this.width;
        float f6 = this.height;
        if (toUse.length != 0 && this.mTempValue.length <= toUse[toUse.length - 1]) {
            int i = toUse[toUse.length - 1] + 1;
            this.mTempValue = new double[i];
            this.mTempDelta = new double[i];
        }
        Arrays.fill(this.mTempValue, Double.NaN);
        for (int i2 = 0; i2 < toUse.length; i2++) {
            this.mTempValue[toUse[i2]] = data[i2];
            this.mTempDelta[toUse[i2]] = slope[i2];
        }
        float f7 = Float.NaN;
        int i3 = 0;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        while (true) {
            double[] dArr = this.mTempValue;
            if (i3 >= dArr.length) {
                break;
            }
            boolean isNaN = Double.isNaN(dArr[i3]);
            double d = SearchStatUtils.POW;
            if (!isNaN || !(cycle == null || cycle[i3] == SearchStatUtils.POW)) {
                if (cycle != null) {
                    d = cycle[i3];
                }
                if (!Double.isNaN(this.mTempValue[i3])) {
                    d = this.mTempValue[i3] + d;
                }
                f2 = f7;
                float f12 = (float) d;
                float f13 = (float) this.mTempDelta[i3];
                if (i3 == 1) {
                    f7 = f2;
                    f8 = f13;
                    f3 = f12;
                } else if (i3 == 2) {
                    f7 = f2;
                    f9 = f13;
                    f4 = f12;
                } else if (i3 == 3) {
                    f7 = f2;
                    f10 = f13;
                    f5 = f12;
                } else if (i3 == 4) {
                    f7 = f2;
                    f11 = f13;
                    f6 = f12;
                } else if (i3 == 5) {
                    f7 = f12;
                }
                i3++;
            } else {
                f2 = f7;
            }
            f7 = f2;
            i3++;
        }
        float f14 = f7;
        MotionController motionController = this.mRelativeToController;
        if (motionController != null) {
            float[] fArr = new float[2];
            float[] fArr2 = new float[2];
            motionController.getCenter(position, fArr, fArr2);
            float f15 = fArr[0];
            float f16 = fArr[1];
            float f17 = fArr2[0];
            float f18 = fArr2[1];
            double d2 = f3;
            double d3 = f4;
            float sin = (float) ((f15 + (Math.sin(d3) * d2)) - (f5 / 2.0f));
            f = f6;
            float cos = (float) ((f16 - (Math.cos(d3) * d2)) - (f6 / 2.0f));
            double d4 = f8;
            double d5 = f9;
            float sin2 = (float) (f17 + (Math.sin(d3) * d4) + (Math.cos(d3) * d2 * d5));
            float cos2 = (float) ((f18 - (d4 * Math.cos(d3))) + (d2 * Math.sin(d3) * d5));
            if (slope.length >= 2) {
                z = false;
                slope[0] = sin2;
                z2 = true;
                slope[1] = cos2;
            } else {
                z = false;
                z2 = true;
            }
            if (!Float.isNaN(f14)) {
                view.setRotation((float) (f14 + Math.toDegrees(Math.atan2(cos2, sin2))));
            }
            f3 = sin;
            f4 = cos;
        } else {
            f = f6;
            z = false;
            z2 = true;
            if (!Float.isNaN(f14)) {
                view.setRotation((float) (0.0f + f14 + Math.toDegrees(Math.atan2(f9 + (f11 / 2.0f), f8 + (f10 / 2.0f)))));
            }
        }
        if (view instanceof FloatLayout) {
            ((FloatLayout) view).layout(f3, f4, f5 + f3, f4 + f);
            return;
        }
        float f19 = f3 + 0.5f;
        int i4 = (int) f19;
        float f20 = f4 + 0.5f;
        int i5 = (int) f20;
        int i6 = (int) (f19 + f5);
        int i7 = (int) (f20 + f);
        int i8 = i6 - i4;
        int i9 = i7 - i5;
        if (i8 != view.getMeasuredWidth() || i9 != view.getMeasuredHeight()) {
            z = z2;
        }
        if (z || mForceMeasure) {
            view.measure(View.MeasureSpec.makeMeasureSpec(i8, 1073741824), View.MeasureSpec.makeMeasureSpec(i9, 1073741824));
        }
        view.layout(i4, i5, i6, i7);
    }

    @Override // java.lang.Comparable
    public int compareTo(MotionPaths o) {
        return Float.compare(this.position, o.position);
    }
}
