package com.miui.gallery.vlog.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$dimen;

/* loaded from: classes2.dex */
public class VlogVideoProgressBar extends View {
    public int mCircleRadius;
    public int mColor;
    public int mCurHeight;
    public int mHeight;
    public int mMaxHeight;
    public int mMinHeight;
    public Paint mPaint;
    public float mProgress;
    public int mWidth;

    public void setIsTouching(boolean z) {
    }

    public VlogVideoProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VlogVideoProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mColor = -1;
        this.mPaint = new Paint(1);
        int color = context.getResources().getColor(R$color.vlog_video_progress_bar_color);
        this.mColor = color;
        this.mPaint.setColor(color);
        this.mMaxHeight = getResources().getDimensionPixelSize(R$dimen.vlog_video_progress_bar_max_height);
        this.mMinHeight = getResources().getDimensionPixelSize(R$dimen.vlog_video_progress_bar_min_height);
        this.mCircleRadius = getResources().getDimensionPixelSize(R$dimen.vlog_video_progress_bar_height) >> 1;
        this.mCurHeight = this.mMinHeight;
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
    }

    @Override // android.view.View
    public synchronized void onDraw(Canvas canvas) {
        canvas.translate(0.0f, this.mHeight);
        canvas.drawRect(0.0f, -this.mCurHeight, (int) (this.mWidth * this.mProgress), 0.0f, this.mPaint);
    }

    public float getProgress() {
        return this.mProgress;
    }

    public void setProgress(float f) {
        this.mProgress = f;
        invalidate();
    }
}
