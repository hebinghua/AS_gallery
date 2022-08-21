package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/* loaded from: classes2.dex */
public class VerticalSeekBar extends SeekBar {
    public ProgressChangeListener mProgressChangeListener;

    /* loaded from: classes2.dex */
    public interface ProgressChangeListener {
        void onProgressChange(int i, boolean z);
    }

    public VerticalSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VerticalSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public synchronized void onDraw(Canvas canvas) {
        canvas.rotate(-90.0f);
        canvas.translate(-canvas.getHeight(), 0.0f);
        super.onDraw(canvas);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i2, i, i4, i3);
    }

    @Override // android.widget.ProgressBar
    public synchronized void setProgress(int i) {
        super.setProgress(i);
        onSizeChanged(getWidth(), getHeight(), 0, 0);
    }

    @Override // android.widget.AbsSeekBar, android.widget.ProgressBar, android.view.View
    public synchronized void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override // android.widget.AbsSeekBar, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        ProgressChangeListener progressChangeListener;
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0 || action == 1 || action == 2) {
            int max = getMax() - ((int) ((getMax() * motionEvent.getY()) / getHeight()));
            setProgress(max);
            if (motionEvent.getAction() == 1 && (progressChangeListener = this.mProgressChangeListener) != null) {
                progressChangeListener.onProgressChange(validateProgress(max), true);
            }
        }
        return true;
    }

    public void notifyProgressChange(int i, boolean z, boolean z2) {
        setProgress(i, z);
        ProgressChangeListener progressChangeListener = this.mProgressChangeListener;
        if (progressChangeListener != null) {
            progressChangeListener.onProgressChange(validateProgress(i), z2);
        }
    }

    public void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
        this.mProgressChangeListener = progressChangeListener;
    }

    public final int validateProgress(int i) {
        return i < getMin() ? getMin() : Math.min(i, getMax());
    }
}
