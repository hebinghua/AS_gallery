package com.miui.gallery.editor.photo.widgets.ColorSelector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class CircularView extends View {
    public String mColorTxt;
    public boolean mIsSelect;
    public Paint mPaint;
    public int mStrokeWidth;

    public CircularView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setAntiAlias(true);
        this.mStrokeWidth = getContext().getResources().getDimensionPixelSize(R.dimen.circular_view_stroke_width);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int width = getWidth() / 2;
        int i = -1;
        if (this.mColorTxt.indexOf(",") == -1) {
            Paint paint = this.mPaint;
            String str = this.mColorTxt;
            if (str != null) {
                i = Color.parseColor(str);
            }
            paint.setColor(i);
            this.mPaint.setShader(null);
        } else {
            String[] split = this.mColorTxt.split(",");
            this.mPaint.setShader(new LinearGradient(0.0f, getHeight(), getWidth(), 0.0f, Color.parseColor(split[0]), Color.parseColor(split[1]), Shader.TileMode.MIRROR));
        }
        if (this.mIsSelect) {
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.mStrokeWidth);
            float f = width;
            canvas.drawCircle(f, f, (getContext().getResources().getDimensionPixelSize(R.dimen.text_edit_circular_view_solid_circular_unchecked) / 2) - (this.mStrokeWidth / 2), this.mPaint);
        }
        this.mPaint.setStyle(Paint.Style.FILL);
        float f2 = width;
        canvas.drawCircle(f2, f2, this.mIsSelect ? getContext().getResources().getDimensionPixelSize(R.dimen.text_edit_circular_view_solid_circular_checked) / 2 : getContext().getResources().getDimensionPixelSize(R.dimen.text_edit_circular_view_solid_circular_unchecked) / 2, this.mPaint);
    }

    public String getColorTxt() {
        return this.mColorTxt;
    }

    public void setColorTxt(String str) {
        this.mColorTxt = str;
    }

    public void setIsSelect(boolean z) {
        this.mIsSelect = z;
    }
}
