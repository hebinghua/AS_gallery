package com.xiaomi.skyprocess;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/* loaded from: classes3.dex */
public class MiGLSurfaceView extends GLSurfaceView {
    private static final String TAG = "MiGLSurfaceView";
    private int mRatioHeight;
    private int mRatioWidth;

    public MiGLSurfaceView(Context context) {
        super(context);
        this.mRatioWidth = 1080;
        this.mRatioHeight = 1920;
    }

    public MiGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRatioWidth = 1080;
        this.mRatioHeight = 1920;
    }

    public void setAspectRatio(int i, int i2) {
        Log.d(TAG, "wangqm PW " + i + "h : " + i2);
        if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        this.mRatioWidth = i;
        this.mRatioHeight = i2;
        requestLayout();
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        Log.d(TAG, "onMeasure");
        super.onMeasure(i, i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int i4 = this.mRatioWidth;
        if (i4 == 0 || (i3 = this.mRatioHeight) == 0) {
            setMeasuredDimension(size, size2);
        } else if (1073741824 == mode) {
            setMeasuredDimension(size, (i3 * size) / i4);
            Log.d(TAG, "width : " + size + " height : " + ((size * this.mRatioHeight) / this.mRatioWidth));
        } else if (size < (size2 * i4) / i3) {
            setMeasuredDimension(size, (i3 * size) / i4);
        } else {
            setMeasuredDimension((i4 * size2) / i3, size2);
        }
    }
}
