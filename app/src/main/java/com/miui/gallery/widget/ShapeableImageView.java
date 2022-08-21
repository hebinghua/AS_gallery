package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class ShapeableImageView extends ImageView {
    public float bottomLeftCornerRadius;
    public float bottomRightCornerRadius;
    public final Path outlinePath;
    public float topLeftCornerRadius;
    public float topRightCornerRadius;

    public ShapeableImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.topLeftCornerRadius = 0.0f;
        this.topRightCornerRadius = 0.0f;
        this.bottomLeftCornerRadius = 0.0f;
        this.bottomRightCornerRadius = 0.0f;
        this.outlinePath = new Path();
        init(context, attributeSet);
    }

    public final void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ShapeableImageView);
        try {
            try {
                float dimension = obtainStyledAttributes.getDimension(2, 0.0f);
                this.topLeftCornerRadius = obtainStyledAttributes.getDimension(3, dimension);
                this.topRightCornerRadius = obtainStyledAttributes.getDimension(4, dimension);
                this.bottomLeftCornerRadius = obtainStyledAttributes.getDimension(0, dimension);
                this.bottomRightCornerRadius = obtainStyledAttributes.getDimension(1, dimension);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        generateOutlinePath();
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        canvas.clipPath(this.outlinePath);
        super.onDraw(canvas);
    }

    public final void generateOutlinePath() {
        this.outlinePath.rewind();
        float[] fArr = new float[8];
        float f = this.topLeftCornerRadius;
        if (f > 0.0f) {
            fArr[0] = f;
            fArr[1] = f;
        }
        float f2 = this.topRightCornerRadius;
        if (f2 > 0.0f) {
            fArr[2] = f2;
            fArr[3] = f2;
        }
        float f3 = this.bottomRightCornerRadius;
        if (f3 > 0.0f) {
            fArr[4] = f3;
            fArr[5] = f3;
        }
        float f4 = this.bottomLeftCornerRadius;
        if (f4 > 0.0f) {
            fArr[6] = f4;
            fArr[7] = f4;
        }
        this.outlinePath.addRoundRect(new RectF(0.0f, 0.0f, getWidth(), getHeight()), fArr, Path.Direction.CCW);
    }
}
