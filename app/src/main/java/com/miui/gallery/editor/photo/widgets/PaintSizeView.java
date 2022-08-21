package com.miui.gallery.editor.photo.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class PaintSizeView extends View {
    public int mCurrentDiameter;
    public Paint mPaint;

    public PaintSizeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(getResources().getDimension(R.dimen.paint_stroke_size));
        this.mPaint.setColor(getResources().getColor(R.color.paint_size_stroke_color));
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f, this.mCurrentDiameter / 2.0f, this.mPaint);
    }

    public void setDiameterWithAnimation(int i) {
        ValueAnimator ofArgb = ValueAnimator.ofArgb(0, i);
        ofArgb.setDuration(500L);
        ofArgb.setInterpolator(new OvershootInterpolator());
        ofArgb.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.widgets.PaintSizeView$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PaintSizeView.this.lambda$setDiameterWithAnimation$0(valueAnimator);
            }
        });
        ofArgb.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setDiameterWithAnimation$0(ValueAnimator valueAnimator) {
        this.mCurrentDiameter = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        invalidate();
    }

    public void setDiameter(int i) {
        this.mCurrentDiameter = i;
        invalidate();
    }

    public void setPaintStyle(Paint.Style style) {
        this.mPaint.setStyle(style);
    }
}
