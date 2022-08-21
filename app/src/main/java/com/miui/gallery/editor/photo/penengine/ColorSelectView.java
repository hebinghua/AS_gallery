package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ColorSelectView extends View {
    public boolean mCheck;
    public int mColor;
    public final Paint mPaint;

    public ColorSelectView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint = new Paint();
        this.mColor = -1;
        this.mCheck = false;
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColor(int i) {
        this.mColor = i;
        invalidate();
    }

    public void setCheck(boolean z) {
        this.mCheck = z;
        invalidate();
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        this.mPaint.reset();
        int width = getWidth();
        if (this.mCheck) {
            float f = width;
            float f2 = f / 2.0f;
            SweepGradient sweepGradient = new SweepGradient(f2, f2, getContext().getResources().getIntArray(R.array.gradual_color), (float[]) null);
            Matrix matrix = new Matrix();
            matrix.preRotate(90.0f, f2, f2);
            sweepGradient.setLocalMatrix(matrix);
            this.mPaint.setShader(sweepGradient);
            canvas.drawCircle(f2, f2, f2, this.mPaint);
            this.mPaint.reset();
            this.mPaint.setColor(-1);
            canvas.drawCircle(f2, f2, f2 - (f / 9.0f), this.mPaint);
            this.mPaint.setColor(this.mColor);
            canvas.drawCircle(f2, f2, f2 - (f / 5.0f), this.mPaint);
        } else {
            this.mPaint.setColor(this.mColor);
            float f3 = width;
            float f4 = f3 / 2.0f;
            canvas.drawCircle(f4, f4, f4 - (f3 / 9.0f), this.mPaint);
        }
        super.draw(canvas);
    }
}
