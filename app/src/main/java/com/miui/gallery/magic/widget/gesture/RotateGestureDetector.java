package com.miui.gallery.magic.widget.gesture;

import android.content.Context;
import android.view.MotionEvent;

/* loaded from: classes2.dex */
public class RotateGestureDetector extends TwoFingerGestureDetector {
    public final OnRotateGestureListener mListener;
    public boolean mSloppyGesture;

    /* loaded from: classes2.dex */
    public interface OnRotateGestureListener {
        boolean onRotate(RotateGestureDetector rotateGestureDetector);

        boolean onRotateBegin(RotateGestureDetector rotateGestureDetector);

        void onRotateEnd(RotateGestureDetector rotateGestureDetector);
    }

    /* loaded from: classes2.dex */
    public static class SimpleOnRotateGestureListener implements OnRotateGestureListener {
        @Override // com.miui.gallery.magic.widget.gesture.RotateGestureDetector.OnRotateGestureListener
        public boolean onRotateBegin(RotateGestureDetector rotateGestureDetector) {
            return true;
        }

        @Override // com.miui.gallery.magic.widget.gesture.RotateGestureDetector.OnRotateGestureListener
        public void onRotateEnd(RotateGestureDetector rotateGestureDetector) {
        }
    }

    public RotateGestureDetector(Context context, OnRotateGestureListener onRotateGestureListener) {
        super(context);
        this.mListener = onRotateGestureListener;
    }

    @Override // com.miui.gallery.magic.widget.gesture.BaseGestureDetector
    public void handleStartProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 2) {
            if (!this.mSloppyGesture) {
                return;
            }
            boolean isSloppyGesture = isSloppyGesture(motionEvent);
            this.mSloppyGesture = isSloppyGesture;
            if (isSloppyGesture) {
                return;
            }
            this.mGestureInProgress = this.mListener.onRotateBegin(this);
        } else if (i != 5) {
        } else {
            resetState();
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
            this.mTimeDelta = 0L;
            updateStateByEvent(motionEvent);
            boolean isSloppyGesture2 = isSloppyGesture(motionEvent);
            this.mSloppyGesture = isSloppyGesture2;
            if (isSloppyGesture2) {
                return;
            }
            this.mGestureInProgress = this.mListener.onRotateBegin(this);
        }
    }

    @Override // com.miui.gallery.magic.widget.gesture.BaseGestureDetector
    public void handleInProgressEvent(int i, MotionEvent motionEvent) {
        if (i == 2) {
            updateStateByEvent(motionEvent);
            if (this.mCurrPressure / this.mPrevPressure <= 0.67f || !this.mListener.onRotate(this)) {
                return;
            }
            this.mPrevEvent.recycle();
            this.mPrevEvent = MotionEvent.obtain(motionEvent);
        } else if (i == 3) {
            if (!this.mSloppyGesture) {
                this.mListener.onRotateEnd(this);
            }
            resetState();
        } else if (i != 6) {
        } else {
            updateStateByEvent(motionEvent);
            if (!this.mSloppyGesture) {
                this.mListener.onRotateEnd(this);
            }
            resetState();
        }
    }

    @Override // com.miui.gallery.magic.widget.gesture.BaseGestureDetector
    public void resetState() {
        super.resetState();
        this.mSloppyGesture = false;
    }

    public float getRotationDegreesDelta() {
        return (float) (((Math.atan2(this.mPrevFingerDiffY, this.mPrevFingerDiffX) - Math.atan2(this.mCurrFingerDiffY, this.mCurrFingerDiffX)) * 180.0d) / 3.141592653589793d);
    }
}
