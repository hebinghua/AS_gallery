package com.miui.gallery.magic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/* loaded from: classes2.dex */
public class TouchFrameLayout extends FrameLayout {
    public OnTouchDownListener mOnTouchDownListener;

    /* loaded from: classes2.dex */
    public interface OnTouchDownListener {
        void onDown();
    }

    public TouchFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mOnTouchDownListener != null && motionEvent.getAction() == 0) {
            this.mOnTouchDownListener.onDown();
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setOnTouchDownListener(OnTouchDownListener onTouchDownListener) {
        this.mOnTouchDownListener = onTouchDownListener;
    }
}
