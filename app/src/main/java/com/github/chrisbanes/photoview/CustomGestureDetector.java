package com.github.chrisbanes.photoview;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.miui.gallery.R;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;

/* loaded from: classes.dex */
public class CustomGestureDetector {
    public static boolean DEBUG = Log.isLoggable("CustomGestureDetector", 3);
    public final ScaleGestureDetector mDetector;
    public boolean mIsDragging;
    public float mLastTouchX;
    public float mLastTouchY;
    public OnGestureListener mListener;
    public final float mMinimumVelocity;
    public float mSecondLastTouchX;
    public float mSecondLastTouchY;
    public final float mTouchSlop;
    public VelocityTracker mVelocityTracker;
    public int mPrimaryPointerId = -1;
    public int mSecondPointerId = -1;
    public float mMultiPointerDiff = 10.0f;

    public CustomGestureDetector(Context context, OnGestureListener onGestureListener) {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mListener = onGestureListener;
        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() { // from class: com.github.chrisbanes.photoview.CustomGestureDetector.1
            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector2) {
                float scaleFactor = scaleGestureDetector2.getScaleFactor();
                if (Float.isNaN(scaleFactor) || Float.isInfinite(scaleFactor)) {
                    return false;
                }
                if (scaleFactor < 0.0f) {
                    return true;
                }
                CustomGestureDetector.this.mListener.onScale(scaleFactor, scaleGestureDetector2.getFocusX(), scaleGestureDetector2.getFocusY());
                return true;
            }

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector2) {
                CustomGestureDetector.this.mListener.onScaleBegin();
                return true;
            }

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector2) {
                CustomGestureDetector.this.mListener.oScaleEnd();
            }
        }, ThreadManager.getMainHandler());
        this.mDetector = scaleGestureDetector;
        scaleGestureDetector.setScaleMinSpan(context.getResources().getDimensionPixelSize(R.dimen.photo_view_scale_gesture_detector_min_spin));
    }

    public final float getActiveX(MotionEvent motionEvent) {
        int findPointerIndex = motionEvent.findPointerIndex(this.mPrimaryPointerId);
        if (checkPointerIndex(motionEvent, findPointerIndex)) {
            return motionEvent.getX(findPointerIndex);
        }
        return motionEvent.getX();
    }

    public final float getActiveY(MotionEvent motionEvent) {
        int findPointerIndex = motionEvent.findPointerIndex(this.mPrimaryPointerId);
        if (checkPointerIndex(motionEvent, findPointerIndex)) {
            return motionEvent.getY(findPointerIndex);
        }
        return motionEvent.getY();
    }

    public void setScaleMinSpan(int i) {
        this.mDetector.setScaleMinSpan(i);
    }

    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        try {
            this.mDetector.onTouchEvent(motionEvent);
            return processTouchEvent(motionEvent);
        } catch (IllegalArgumentException unused) {
            return true;
        }
    }

    public final boolean checkPointerIndex(MotionEvent motionEvent, int i) {
        return i > -1 && i < motionEvent.getPointerCount();
    }

    public final double calculateDistance(double d, double d2) {
        return Math.sqrt((d * d) + (d2 * d2));
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x01ac  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean processTouchEvent(android.view.MotionEvent r19) {
        /*
            Method dump skipped, instructions count: 624
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.github.chrisbanes.photoview.CustomGestureDetector.processTouchEvent(android.view.MotionEvent):boolean");
    }
}
