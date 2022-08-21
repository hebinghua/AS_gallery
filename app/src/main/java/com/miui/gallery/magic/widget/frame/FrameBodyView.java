package com.miui.gallery.magic.widget.frame;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import com.miui.gallery.magic.widget.frame.FrameSelectorView;

/* loaded from: classes2.dex */
public class FrameBodyView extends RelativeLayout {
    public FrameSelectorView.HandlerBodyTouchListener mHandlerBodyTouchListener;

    public FrameBodyView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mHandlerBodyTouchListener.onTouch(this, motionEvent)) {
            return true;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setHandlerBodyTouchListener(FrameSelectorView.HandlerBodyTouchListener handlerBodyTouchListener) {
        this.mHandlerBodyTouchListener = handlerBodyTouchListener;
    }
}
