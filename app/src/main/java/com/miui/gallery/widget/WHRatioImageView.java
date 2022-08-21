package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.AppCompatImageView;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class WHRatioImageView extends AppCompatImageView {
    public int mStrokeColor;
    public float mStrokeWidth;
    public float mWHRatio;

    public WHRatioImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WHRatioImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mWHRatio = 0.0f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.WHRatioImageView, i, 0);
        if (obtainStyledAttributes != null) {
            this.mWHRatio = obtainStyledAttributes.getFloat(2, 0.0f);
            this.mStrokeColor = obtainStyledAttributes.getColor(0, 0);
            this.mStrokeWidth = obtainStyledAttributes.getDimension(1, 0.0f);
            obtainStyledAttributes.recycle();
        }
    }

    public void setWHRatio(float f) {
        if (Float.compare(f, this.mWHRatio) != 0) {
            this.mWHRatio = f;
            requestLayout();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void onMeasure(int i, int i2) {
        if (this.mWHRatio > 0.0f) {
            int mode = View.MeasureSpec.getMode(i);
            int size = View.MeasureSpec.getSize(i);
            int mode2 = View.MeasureSpec.getMode(i2);
            int size2 = View.MeasureSpec.getSize(i2);
            if (mode == 1073741824) {
                size2 = (int) (size / this.mWHRatio);
                mode2 = 1073741824;
            } else if (mode2 == 1073741824) {
                size = (int) (size2 * this.mWHRatio);
                mode = 1073741824;
            }
            i = View.MeasureSpec.makeMeasureSpec(size, mode);
            i2 = View.MeasureSpec.makeMeasureSpec(size2, mode2);
        }
        super.onMeasure(i, i2);
    }
}
