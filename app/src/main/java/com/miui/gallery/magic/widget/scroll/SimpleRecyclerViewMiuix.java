package com.miui.gallery.magic.widget.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class SimpleRecyclerViewMiuix extends RecyclerView {
    public boolean mAlwaysDisableSpring;
    public boolean mEnableItemClickWhileSettling;
    public boolean mTryingDispatchEvent;

    public SimpleRecyclerViewMiuix(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SimpleRecyclerViewMiuix(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutManager(new LinearLayoutManager(context, 0, false));
        setItemAnimator(null);
    }

    @Override // miuix.recyclerview.widget.RecyclerView, androidx.recyclerview.widget.RecyclerView
    public boolean fling(int i, int i2) {
        return super.fling((int) (i * 0.6f), i2);
    }

    @Override // androidx.recyclerview.widget.SpringRecyclerView, androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int scrollState = getScrollState();
        boolean onInterceptTouchEvent = super.onInterceptTouchEvent(motionEvent);
        if (this.mEnableItemClickWhileSettling) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked != 2) {
                        return onInterceptTouchEvent;
                    }
                    this.mTryingDispatchEvent = false;
                    return onInterceptTouchEvent;
                } else if (!this.mTryingDispatchEvent) {
                    return onInterceptTouchEvent;
                }
            } else if (scrollState == 2) {
                this.mTryingDispatchEvent = true;
            } else {
                this.mTryingDispatchEvent = false;
                return onInterceptTouchEvent;
            }
            return false;
        }
        return onInterceptTouchEvent;
    }

    @Override // androidx.recyclerview.widget.SpringRecyclerView, androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mAlwaysDisableSpring) {
            setSpringEnabled(true);
        }
        return super.onTouchEvent(motionEvent);
    }

    public void setAlwaysDisableSpring(boolean z) {
        this.mAlwaysDisableSpring = z;
    }

    public void setEnableItemClickWhileSettling(boolean z) {
        this.mEnableItemClickWhileSettling = z;
    }
}
