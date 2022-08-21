package com.github.chrisbanes.photoview;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import java.util.Locale;

/* loaded from: classes.dex */
public class RotateGestureDetector {
    public static boolean DEBUG = Log.isLoggable("RotateGestureDetector", 3);
    public boolean isInProgress;
    public float mDegrees;
    public float mFirstPointerLastX;
    public float mFirstPointerLastY;
    public float mFocusX;
    public float mFocusY;
    public boolean mIsClockwise;
    public OnRotationGestureListener mListener;
    public int mMaxVelocity;
    public int mMinVelocity;
    public double mPointersLastDistance;
    public float mSecondPointerLastX;
    public float mSecondPointerLastY;
    public VelocityTracker mTracker;
    public float mRotateSlop = 10.0f;
    public double mDistanceDiffLimit = 50.0d;
    public boolean mIsBeingRotated = false;
    public int mFirstPointerID = -1;
    public int mSecondPointerID = -1;

    /* loaded from: classes.dex */
    public interface OnRotationGestureListener {
        boolean onRotate(RotateGestureDetector rotateGestureDetector);

        boolean onRotateBegin(RotateGestureDetector rotateGestureDetector);

        void onRotateEnd(RotateGestureDetector rotateGestureDetector, boolean z, float f);
    }

    public final float clipAngle(float f) {
        return f % 360.0f;
    }

    public float getRotateDegrees() {
        return this.mDegrees;
    }

    public float getFocusX() {
        return this.mFocusX;
    }

    public float getFocusY() {
        return this.mFocusY;
    }

    public RotateGestureDetector(Context context, OnRotationGestureListener onRotationGestureListener) {
        this.mListener = onRotationGestureListener;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    }

    public final void ensureTracker() {
        if (this.mTracker == null) {
            this.mTracker = VelocityTracker.obtain();
        }
    }

    public final void releaseTracker() {
        VelocityTracker velocityTracker = this.mTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mTracker = null;
        }
    }

    public final boolean checkPointerIndex(MotionEvent motionEvent, int i) {
        return i > -1 && i < motionEvent.getPointerCount();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        float f3;
        float f4;
        ensureTracker();
        this.mTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mFirstPointerLastX = motionEvent.getX();
            this.mFirstPointerLastY = motionEvent.getY();
            this.mFirstPointerID = motionEvent.getPointerId(0);
            this.mDegrees = 0.0f;
            callRotateEnd();
        } else if (actionMasked != 1) {
            if (actionMasked == 2) {
                int i = this.mFirstPointerID;
                if (i != -1 && this.mSecondPointerID != -1) {
                    int findPointerIndex = motionEvent.findPointerIndex(i);
                    int findPointerIndex2 = motionEvent.findPointerIndex(this.mSecondPointerID);
                    if (!checkPointerIndex(motionEvent, findPointerIndex) || !checkPointerIndex(motionEvent, findPointerIndex2)) {
                        Log.w("RotateGestureDetector", String.format("illegal pointer index, count[%s], pointer1[%s], pointer2[%s]", Integer.valueOf(motionEvent.getPointerCount()), Integer.valueOf(findPointerIndex), Integer.valueOf(findPointerIndex2)));
                    } else {
                        float x = motionEvent.getX(findPointerIndex);
                        float y = motionEvent.getY(findPointerIndex);
                        float x2 = motionEvent.getX(findPointerIndex2);
                        float y2 = motionEvent.getY(findPointerIndex2);
                        if (!this.mIsBeingRotated) {
                            f3 = x;
                            f4 = y;
                            double calculateDistance = calculateDistance(x, y, x2, y2);
                            if (DEBUG) {
                                Log.d("RotateGestureDetector", String.format(Locale.US, "distance old %s, distance new %s", Double.valueOf(this.mPointersLastDistance), Double.valueOf(calculateDistance)));
                            }
                            if (Math.abs(this.mPointersLastDistance - calculateDistance) > this.mDistanceDiffLimit) {
                                this.mFirstPointerLastX = f3;
                                this.mFirstPointerLastY = f4;
                                f = x2;
                                this.mSecondPointerLastX = f;
                                f2 = y2;
                                this.mSecondPointerLastY = f2;
                                this.mPointersLastDistance = calculateDistance(f3, f4, f, f2);
                            } else {
                                f = x2;
                                f2 = y2;
                                if (Math.abs(calculateDegrees(this.mFirstPointerLastX, this.mFirstPointerLastY, this.mSecondPointerLastX, this.mSecondPointerLastY, f3, f4, f, f2)) > this.mRotateSlop) {
                                    this.mIsBeingRotated = true;
                                    this.mFirstPointerLastX = f3;
                                    this.mFirstPointerLastY = f4;
                                    this.mSecondPointerLastX = f;
                                    this.mSecondPointerLastY = f2;
                                    OnRotationGestureListener onRotationGestureListener = this.mListener;
                                    if (onRotationGestureListener != null) {
                                        this.isInProgress = onRotationGestureListener.onRotateBegin(this);
                                    }
                                }
                            }
                        } else {
                            f = x2;
                            f2 = y2;
                            f3 = x;
                            f4 = y;
                        }
                        if (this.mIsBeingRotated) {
                            float calculateDegrees = calculateDegrees(this.mFirstPointerLastX, this.mFirstPointerLastY, this.mSecondPointerLastX, this.mSecondPointerLastY, f3, f4, f, f2);
                            this.mDegrees = calculateDegrees;
                            this.mFocusX = (f3 + f) / 2.0f;
                            this.mFocusY = (f4 + f2) / 2.0f;
                            if (DEBUG) {
                                Log.d("RotateGestureDetector", String.format("degrees %s, focusX %s, focusY %s", Float.valueOf(calculateDegrees), Float.valueOf(this.mFocusX), Float.valueOf(this.mFocusY)));
                            }
                            if (Math.abs(this.mDegrees) > 2.0f) {
                                this.mIsClockwise = this.mDegrees > 0.0f;
                            }
                            OnRotationGestureListener onRotationGestureListener2 = this.mListener;
                            if (onRotationGestureListener2 != null ? onRotationGestureListener2.onRotate(this) : true) {
                                this.mFirstPointerLastX = f3;
                                this.mFirstPointerLastY = f4;
                                this.mSecondPointerLastX = f;
                                this.mSecondPointerLastY = f2;
                            }
                        }
                    }
                }
            } else if (actionMasked == 3) {
                callRotateEnd();
                this.mFirstPointerID = -1;
                this.mSecondPointerID = -1;
            } else if (actionMasked == 5) {
                int actionIndex = motionEvent.getActionIndex();
                this.mSecondPointerLastX = motionEvent.getX(actionIndex);
                this.mSecondPointerLastY = motionEvent.getY(actionIndex);
                this.mSecondPointerID = motionEvent.getPointerId(actionIndex);
                this.mDegrees = 0.0f;
                callRotateEnd();
                this.mPointersLastDistance = calculateDistance(this.mSecondPointerLastX, this.mSecondPointerLastY, this.mFirstPointerLastX, this.mFirstPointerLastY);
            } else if (actionMasked == 6) {
                callRotateEnd();
                this.mSecondPointerID = -1;
            }
            return true;
        } else {
            callRotateEnd();
            this.mFirstPointerID = -1;
        }
        return true;
    }

    public boolean isInProgress() {
        return this.isInProgress;
    }

    public final void callRotateEnd() {
        if (this.isInProgress) {
            this.isInProgress = false;
            this.mTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
            float xVelocity = this.mTracker.getXVelocity(this.mFirstPointerID);
            float xVelocity2 = this.mTracker.getXVelocity(this.mSecondPointerID);
            float yVelocity = this.mTracker.getYVelocity(this.mFirstPointerID);
            float yVelocity2 = this.mTracker.getYVelocity(this.mSecondPointerID);
            float f = xVelocity2 - xVelocity;
            float f2 = yVelocity2 - yVelocity;
            float f3 = (Math.abs(f) > Math.abs(f2) ? f : f2) / 2.0f;
            if (DEBUG) {
                Log.i("RotateGestureDetector", String.format("x1 %s, x1 %s, y1 %s, y2 %s, deltaX %s, deltaY %s, clockwise %s", Float.valueOf(xVelocity), Float.valueOf(xVelocity), Float.valueOf(yVelocity), Float.valueOf(yVelocity2), Float.valueOf(f), Float.valueOf(f2), Boolean.valueOf(this.mIsClockwise)));
            }
            OnRotationGestureListener onRotationGestureListener = this.mListener;
            if (onRotationGestureListener != null) {
                onRotationGestureListener.onRotateEnd(this, this.mIsClockwise, f3);
            }
        }
        this.mIsBeingRotated = false;
        releaseTracker();
    }

    public final float calculateDegrees(float f, float f2) {
        float clipAngle = clipAngle(f2) - clipAngle(f);
        return clipAngle < -180.0f ? clipAngle + 360.0f : clipAngle > 180.0f ? clipAngle - 360.0f : clipAngle;
    }

    public final double calculateDistance(double d, double d2, double d3, double d4) {
        return Math.sqrt(Math.pow(d - d3, 2.0d) + Math.pow(d2 - d4, 2.0d));
    }

    public final float calculateDegrees(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        return calculateDegrees((float) Math.toDegrees((float) Math.atan2(f4 - f2, f3 - f)), (float) Math.toDegrees((float) Math.atan2(f8 - f6, f7 - f5)));
    }
}
