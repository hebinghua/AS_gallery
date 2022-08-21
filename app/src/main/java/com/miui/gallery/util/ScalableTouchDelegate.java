package com.miui.gallery.util;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewConfiguration;

/* loaded from: classes2.dex */
public class ScalableTouchDelegate extends TouchDelegate {
    public Rect mBounds;
    public boolean mDelegateTargeted;
    public View mDelegateView;
    public View mHostView;
    public float mScale;
    public int mSlop;
    public Rect mSlopBounds;

    public ScalableTouchDelegate(float f, View view, View view2) {
        super(null, view2);
        this.mScale = f < 1.0f ? 0.0f : f - 1.0f;
        this.mBounds = new Rect();
        this.mSlop = ViewConfiguration.get(view2.getContext()).getScaledTouchSlop();
        this.mSlopBounds = new Rect(this.mBounds);
        this.mDelegateView = view2;
        this.mHostView = view;
    }

    public boolean isDelegateTargeted(float f, float f2) {
        setBounds();
        return this.mBounds.contains((int) f, (int) f2);
    }

    public void setBounds() {
        if (!this.mBounds.isEmpty()) {
            return;
        }
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        this.mDelegateView.getLocationInWindow(iArr);
        this.mHostView.getLocationInWindow(iArr2);
        Rect rect = new Rect(iArr[0], iArr[1], iArr[0] + this.mDelegateView.getWidth(), iArr[1] + this.mDelegateView.getHeight());
        Rect rect2 = new Rect(iArr2[0], iArr2[1], iArr2[0] + this.mHostView.getWidth(), iArr2[1] + this.mHostView.getHeight());
        if (!rect2.contains(rect)) {
            return;
        }
        Rect rect3 = this.mBounds;
        int i = rect.left;
        int i2 = rect2.left;
        int i3 = rect.top;
        int i4 = rect2.top;
        rect3.set(i - i2, i3 - i4, rect.right - i2, rect.bottom - i4);
        this.mBounds.inset(-((int) (rect.width() * this.mScale)), -((int) (rect.height() * this.mScale)));
        this.mSlopBounds.set(this.mBounds);
        Rect rect4 = this.mSlopBounds;
        int i5 = this.mSlop;
        rect4.inset(-i5, -i5);
    }

    @Override // android.view.TouchDelegate
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean contains;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        int action = motionEvent.getAction();
        boolean z = true;
        if (action == 0) {
            setBounds();
            contains = this.mBounds.contains(x, y);
            this.mDelegateTargeted = contains;
        } else if (action == 1 || action == 2) {
            boolean z2 = this.mDelegateTargeted;
            if (z2) {
                z = this.mSlopBounds.contains(x, y);
            }
            contains = z2;
        } else if (action != 3) {
            contains = false;
        } else {
            contains = this.mDelegateTargeted;
            this.mDelegateTargeted = false;
        }
        if (contains) {
            View view = this.mDelegateView;
            if (z) {
                motionEvent.setLocation(view.getWidth() / 2, view.getHeight() / 2);
            } else {
                float f = -(this.mSlop * 2);
                motionEvent.setLocation(f, f);
            }
            return view.dispatchTouchEvent(motionEvent);
        }
        return false;
    }
}
