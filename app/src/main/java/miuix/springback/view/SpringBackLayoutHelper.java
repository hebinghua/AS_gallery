package miuix.springback.view;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

/* loaded from: classes3.dex */
public class SpringBackLayoutHelper {
    public int mActivePointerId = -1;
    public float mInitialDownX;
    public float mInitialDownY;
    public int mScrollOrientation;
    public ViewGroup mTarget;
    public int mTargetScrollOrientation;
    public int mTouchSlop;

    public SpringBackLayoutHelper(ViewGroup viewGroup, int i) {
        this.mTarget = viewGroup;
        this.mTargetScrollOrientation = i;
        this.mTouchSlop = ViewConfiguration.get(viewGroup.getContext()).getScaledTouchSlop();
    }

    public boolean isTouchInTarget(MotionEvent motionEvent) {
        int findPointerIndex = motionEvent.findPointerIndex(motionEvent.getPointerId(0));
        if (findPointerIndex >= 0) {
            float y = motionEvent.getY(findPointerIndex);
            float x = motionEvent.getX(findPointerIndex);
            int[] iArr = {0, 0};
            this.mTarget.getLocationInWindow(iArr);
            int i = iArr[0];
            int i2 = iArr[1];
            return new Rect(i, i2, this.mTarget.getWidth() + i, this.mTarget.getHeight() + i2).contains((int) x, (int) y);
        }
        return false;
    }

    public void checkOrientation(MotionEvent motionEvent) {
        int findPointerIndex;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            int pointerId = motionEvent.getPointerId(0);
            this.mActivePointerId = pointerId;
            int findPointerIndex2 = motionEvent.findPointerIndex(pointerId);
            if (findPointerIndex2 < 0) {
                return;
            }
            this.mInitialDownY = motionEvent.getY(findPointerIndex2);
            this.mInitialDownX = motionEvent.getX(findPointerIndex2);
            this.mScrollOrientation = 0;
            return;
        }
        int i = 1;
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                int i2 = this.mActivePointerId;
                if (i2 == -1 || (findPointerIndex = motionEvent.findPointerIndex(i2)) < 0) {
                    return;
                }
                float y = motionEvent.getY(findPointerIndex);
                float x = motionEvent.getX(findPointerIndex);
                float f = y - this.mInitialDownY;
                float f2 = x - this.mInitialDownX;
                if (Math.abs(f2) <= this.mTouchSlop && Math.abs(f) <= this.mTouchSlop) {
                    return;
                }
                if (Math.abs(f2) <= Math.abs(f)) {
                    i = 2;
                }
                this.mScrollOrientation = i;
                return;
            } else if (actionMasked != 3) {
                return;
            }
        }
        this.mScrollOrientation = 0;
        this.mTarget.requestDisallowInterceptTouchEvent(false);
    }
}
