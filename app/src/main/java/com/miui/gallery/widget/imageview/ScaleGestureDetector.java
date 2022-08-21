package com.miui.gallery.widget.imageview;

import android.content.Context;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes2.dex */
public class ScaleGestureDetector {
    public int mAnchoredScaleMode;
    public float mAnchoredScaleStartX;
    public float mAnchoredScaleStartY;
    public final Context mContext;
    public float mCurrSpan;
    public float mCurrSpanX;
    public float mCurrSpanY;
    public long mCurrTime;
    public boolean mEventBeforeOrAboveStartingGestureEvent;
    public float mFocusX;
    public float mFocusY;
    public GestureDetector mGestureDetector;
    public final Handler mHandler;
    public boolean mInProgress;
    public float mInitialSpan;
    public final OnScaleGestureListener mListener;
    public int mMinSpan;
    public float mPrevSpan;
    public float mPrevSpanX;
    public float mPrevSpanY;
    public long mPrevTime;
    public boolean mQuickScaleEnabled;
    public int mSpanSlop;
    public boolean mStylusScaleEnabled;

    /* loaded from: classes2.dex */
    public interface OnScaleGestureListener {
        boolean onScale(ScaleGestureDetector scaleGestureDetector);

        boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector);

        void onScaleEnd(ScaleGestureDetector scaleGestureDetector);
    }

    public ScaleGestureDetector(Context context, OnScaleGestureListener onScaleGestureListener) {
        this(context, onScaleGestureListener, null);
    }

    public ScaleGestureDetector(Context context, OnScaleGestureListener onScaleGestureListener, Handler handler) {
        this.mAnchoredScaleMode = 0;
        this.mContext = context;
        this.mListener = onScaleGestureListener;
        this.mSpanSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 2;
        this.mMinSpan = Math.max(this.mSpanSlop, BaseMiscUtil.getDimensionPixelOffset(context.getResources(), "config_minScalingSpan", "dimen", "android"));
        this.mHandler = handler;
        int i = context.getApplicationInfo().targetSdkVersion;
        if (i > 18) {
            setQuickScaleEnabled(true);
        }
        if (i > 22) {
            setStylusScaleEnabled(true);
        }
    }

    public void setScaleMinSpan(int i) {
        this.mMinSpan = Math.max(i, this.mSpanSlop);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        this.mCurrTime = motionEvent.getEventTime();
        int actionMasked = motionEvent.getActionMasked();
        if (this.mQuickScaleEnabled) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        int pointerCount = motionEvent.getPointerCount();
        boolean z = (motionEvent.getButtonState() & 32) != 0;
        boolean z2 = this.mAnchoredScaleMode == 2 && !z;
        boolean z3 = actionMasked == 1 || actionMasked == 3 || z2;
        float f3 = 0.0f;
        if (actionMasked == 0 || z3) {
            if (this.mInProgress) {
                this.mListener.onScaleEnd(this);
                this.mInProgress = false;
                this.mInitialSpan = 0.0f;
                this.mAnchoredScaleMode = 0;
            } else if (inAnchoredScaleMode() && z3) {
                this.mInProgress = false;
                this.mInitialSpan = 0.0f;
                this.mAnchoredScaleMode = 0;
            }
            if (z3) {
                return true;
            }
        }
        if (!this.mInProgress && this.mStylusScaleEnabled && !inAnchoredScaleMode() && !z3 && z) {
            this.mAnchoredScaleStartX = motionEvent.getX();
            this.mAnchoredScaleStartY = motionEvent.getY();
            this.mAnchoredScaleMode = 2;
            this.mInitialSpan = 0.0f;
        }
        boolean z4 = actionMasked == 0 || actionMasked == 6 || actionMasked == 5 || z2;
        boolean z5 = actionMasked == 6;
        int actionIndex = z5 ? motionEvent.getActionIndex() : -1;
        int i = z5 ? pointerCount - 1 : pointerCount;
        if (inAnchoredScaleMode()) {
            f2 = this.mAnchoredScaleStartX;
            f = this.mAnchoredScaleStartY;
            if (motionEvent.getY() < f) {
                this.mEventBeforeOrAboveStartingGestureEvent = true;
            } else {
                this.mEventBeforeOrAboveStartingGestureEvent = false;
            }
        } else {
            float f4 = 0.0f;
            float f5 = 0.0f;
            for (int i2 = 0; i2 < pointerCount; i2++) {
                if (actionIndex != i2) {
                    f4 += motionEvent.getX(i2);
                    f5 += motionEvent.getY(i2);
                }
            }
            float f6 = i;
            float f7 = f4 / f6;
            f = f5 / f6;
            f2 = f7;
        }
        float f8 = 0.0f;
        for (int i3 = 0; i3 < pointerCount; i3++) {
            if (actionIndex != i3) {
                f3 += Math.abs(motionEvent.getX(i3) - f2);
                f8 += Math.abs(motionEvent.getY(i3) - f);
            }
        }
        float f9 = i;
        float f10 = (f3 / f9) * 2.0f;
        float f11 = (f8 / f9) * 2.0f;
        float hypot = inAnchoredScaleMode() ? f11 : (float) Math.hypot(f10, f11);
        boolean z6 = this.mInProgress;
        this.mFocusX = f2;
        this.mFocusY = f;
        if (!inAnchoredScaleMode() && this.mInProgress && (hypot < this.mMinSpan || z4)) {
            this.mListener.onScaleEnd(this);
            this.mInProgress = false;
            this.mInitialSpan = hypot;
        }
        if (z4) {
            this.mCurrSpanX = f10;
            this.mPrevSpanX = f10;
            this.mCurrSpanY = f11;
            this.mPrevSpanY = f11;
            this.mCurrSpan = hypot;
            this.mPrevSpan = hypot;
            this.mInitialSpan = hypot;
        }
        int i4 = inAnchoredScaleMode() ? this.mSpanSlop : this.mMinSpan;
        if (!this.mInProgress && hypot >= i4 && (z6 || Math.abs(hypot - this.mInitialSpan) > this.mSpanSlop)) {
            this.mCurrSpanX = f10;
            this.mPrevSpanX = f10;
            this.mCurrSpanY = f11;
            this.mPrevSpanY = f11;
            this.mCurrSpan = hypot;
            this.mPrevSpan = hypot;
            this.mPrevTime = this.mCurrTime;
            this.mInProgress = this.mListener.onScaleBegin(this);
        }
        if (actionMasked == 2) {
            this.mCurrSpanX = f10;
            this.mCurrSpanY = f11;
            this.mCurrSpan = hypot;
            if (this.mInProgress ? this.mListener.onScale(this) : true) {
                this.mPrevSpanX = this.mCurrSpanX;
                this.mPrevSpanY = this.mCurrSpanY;
                this.mPrevSpan = this.mCurrSpan;
                this.mPrevTime = this.mCurrTime;
            }
        }
        return true;
    }

    public final boolean inAnchoredScaleMode() {
        return this.mAnchoredScaleMode != 0;
    }

    public void setQuickScaleEnabled(boolean z) {
        this.mQuickScaleEnabled = z;
        if (!z || this.mGestureDetector != null) {
            return;
        }
        this.mGestureDetector = new GestureDetector(this.mContext, new GestureDetector.SimpleOnGestureListener() { // from class: com.miui.gallery.widget.imageview.ScaleGestureDetector.1
            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                ScaleGestureDetector.this.mAnchoredScaleStartX = motionEvent.getX();
                ScaleGestureDetector.this.mAnchoredScaleStartY = motionEvent.getY();
                ScaleGestureDetector.this.mAnchoredScaleMode = 1;
                return true;
            }
        }, this.mHandler);
    }

    public void setStylusScaleEnabled(boolean z) {
        this.mStylusScaleEnabled = z;
    }

    public boolean isInProgress() {
        return this.mInProgress;
    }

    public float getFocusX() {
        return this.mFocusX;
    }

    public float getFocusY() {
        return this.mFocusY;
    }

    public float getCurrentSpan() {
        return this.mCurrSpan;
    }

    public float getScaleFactor() {
        if (inAnchoredScaleMode()) {
            boolean z = this.mEventBeforeOrAboveStartingGestureEvent;
            boolean z2 = (z && this.mCurrSpan < this.mPrevSpan) || (!z && this.mCurrSpan > this.mPrevSpan);
            float abs = Math.abs(1.0f - (this.mCurrSpan / this.mPrevSpan)) * 0.5f;
            if (this.mPrevSpan <= this.mSpanSlop) {
                return 1.0f;
            }
            return z2 ? 1.0f + abs : 1.0f - abs;
        }
        float f = this.mPrevSpan;
        if (f <= 0.0f) {
            return 1.0f;
        }
        return this.mCurrSpan / f;
    }
}
