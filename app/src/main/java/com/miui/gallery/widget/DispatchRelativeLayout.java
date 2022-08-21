package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

/* loaded from: classes2.dex */
public class DispatchRelativeLayout extends RelativeLayout {
    public boolean mCanClick;
    public int mDownX;
    public int mDownY;
    public ParentStateListener mParentStateListener;
    public int mScaleTouchSlop;

    /* loaded from: classes2.dex */
    public interface ParentStateListener {
        void onClickView(View view);
    }

    public DispatchRelativeLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public DispatchRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mScaleTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        ParentStateListener parentStateListener;
        if (this.mCanClick) {
            int action = motionEvent.getAction();
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (action == 0) {
                this.mDownX = x;
                this.mDownY = y;
            } else if (action == 1) {
                int i = this.mDownX - x;
                int i2 = this.mDownY - y;
                int i3 = this.mScaleTouchSlop;
                if (i < i3 && i2 < i3 && (parentStateListener = this.mParentStateListener) != null) {
                    parentStateListener.onClickView(this);
                }
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public void setCanClick(boolean z) {
        this.mCanClick = z;
    }

    public void setParentStateListener(ParentStateListener parentStateListener) {
        this.mParentStateListener = parentStateListener;
    }
}
