package com.miui.gallery.search.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class IntroIconView extends View {
    public int mBackgroundColor;
    public Drawable mIconDrawable;
    public Paint mLandPaint;
    public Path mLandPath;

    public IntroIconView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public IntroIconView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.IntroIconView);
        try {
            this.mIconDrawable = obtainStyledAttributes.getDrawable(1);
            this.mBackgroundColor = obtainStyledAttributes.getColor(0, context.getResources().getColor(R.color.intro_icon_bg));
            int color = obtainStyledAttributes.getColor(2, context.getResources().getColor(R.color.intro_icon_land_color));
            obtainStyledAttributes.recycle();
            Paint paint = new Paint();
            this.mLandPaint = paint;
            paint.setAntiAlias(true);
            this.mLandPaint.setColor(color);
            this.mLandPath = new Path();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        canvas.drawColor(this.mBackgroundColor);
        float f = height;
        float f2 = 0.75f * f;
        this.mLandPath.reset();
        float f3 = width;
        this.mLandPath.moveTo(f3, f2);
        this.mLandPath.lineTo(f3, f);
        this.mLandPath.lineTo(0.0f, f);
        this.mLandPath.lineTo(0.0f, f2);
        int i = (int) (0.03f * f);
        int i2 = 0;
        while (i2 < width) {
            this.mLandPath.lineTo(i2, (float) (f2 - (i * Math.cos(((((i2 + width) / f3) * 0.8d) + 0.6d) * 3.141592653589793d))));
            i2++;
            f2 = f2;
            i = i;
        }
        this.mLandPath.close();
        canvas.drawPath(this.mLandPath, this.mLandPaint);
        float f4 = 0.68f * f;
        int intrinsicWidth = ((int) (f3 - ((this.mIconDrawable.getIntrinsicWidth() * f4) / this.mIconDrawable.getIntrinsicHeight()))) / 2;
        int i3 = ((int) (f - f4)) / 2;
        this.mIconDrawable.setBounds(intrinsicWidth, i3, width - intrinsicWidth, height - i3);
        this.mIconDrawable.draw(canvas);
    }
}
