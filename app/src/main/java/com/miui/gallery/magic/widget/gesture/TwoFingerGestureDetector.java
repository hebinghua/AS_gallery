package com.miui.gallery.magic.widget.gesture;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/* loaded from: classes2.dex */
public abstract class TwoFingerGestureDetector extends BaseGestureDetector {
    public float mBottomSlopEdge;
    public float mCurrFingerDiffX;
    public float mCurrFingerDiffY;
    public float mCurrLen;
    public final float mEdgeSlop;
    public float mPrevFingerDiffX;
    public float mPrevFingerDiffY;
    public float mPrevLen;
    public float mRightSlopEdge;

    public TwoFingerGestureDetector(Context context) {
        super(context);
        this.mEdgeSlop = ViewConfiguration.get(context).getScaledEdgeSlop();
    }

    @Override // com.miui.gallery.magic.widget.gesture.BaseGestureDetector
    public void updateStateByEvent(MotionEvent motionEvent) {
        super.updateStateByEvent(motionEvent);
        MotionEvent motionEvent2 = this.mPrevEvent;
        this.mCurrLen = -1.0f;
        this.mPrevLen = -1.0f;
        float x = motionEvent2.getX(0);
        float y = motionEvent2.getY(0);
        this.mPrevFingerDiffX = motionEvent2.getX(1) - x;
        this.mPrevFingerDiffY = motionEvent2.getY(1) - y;
        float x2 = motionEvent.getX(0);
        float y2 = motionEvent.getY(0);
        this.mCurrFingerDiffX = motionEvent.getX(1) - x2;
        this.mCurrFingerDiffY = motionEvent.getY(1) - y2;
    }

    public static float getRawX(MotionEvent motionEvent, int i) {
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        if (i < motionEvent.getPointerCount()) {
            return motionEvent.getX(i) + rawX;
        }
        return 0.0f;
    }

    public static float getRawY(MotionEvent motionEvent, int i) {
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        if (i < motionEvent.getPointerCount()) {
            return motionEvent.getY(i) + rawY;
        }
        return 0.0f;
    }

    public boolean isSloppyGesture(MotionEvent motionEvent) {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        float f = this.mEdgeSlop;
        float f2 = displayMetrics.widthPixels - f;
        this.mRightSlopEdge = f2;
        float f3 = displayMetrics.heightPixels - f;
        this.mBottomSlopEdge = f3;
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        float rawX2 = getRawX(motionEvent, 1);
        float rawY2 = getRawY(motionEvent, 1);
        boolean z = rawX < f || rawY < f || rawX > f2 || rawY > f3;
        boolean z2 = rawX2 < f || rawY2 < f || rawX2 > f2 || rawY2 > f3;
        return (z && z2) || z || z2;
    }
}
