package com.miui.gallery.magic.widget.gesture;

import android.content.Context;
import android.view.MotionEvent;

/* loaded from: classes2.dex */
public abstract class BaseGestureDetector {
    public final Context mContext;
    public MotionEvent mCurrEvent;
    public float mCurrPressure;
    public boolean mGestureInProgress;
    public MotionEvent mPrevEvent;
    public float mPrevPressure;
    public long mTimeDelta;

    public abstract void handleInProgressEvent(int i, MotionEvent motionEvent);

    public abstract void handleStartProgressEvent(int i, MotionEvent motionEvent);

    public BaseGestureDetector(Context context) {
        this.mContext = context;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if (!this.mGestureInProgress) {
            handleStartProgressEvent(action, motionEvent);
            return true;
        }
        handleInProgressEvent(action, motionEvent);
        return true;
    }

    public void updateStateByEvent(MotionEvent motionEvent) {
        MotionEvent motionEvent2 = this.mPrevEvent;
        MotionEvent motionEvent3 = this.mCurrEvent;
        if (motionEvent3 != null) {
            motionEvent3.recycle();
            this.mCurrEvent = null;
        }
        this.mCurrEvent = MotionEvent.obtain(motionEvent);
        if (motionEvent == null || motionEvent2 == null) {
            return;
        }
        this.mTimeDelta = motionEvent.getEventTime() - motionEvent2.getEventTime();
        this.mCurrPressure = motionEvent.getPressure(motionEvent.getActionIndex());
        this.mPrevPressure = motionEvent2.getPressure(motionEvent2.getActionIndex());
    }

    public void resetState() {
        MotionEvent motionEvent = this.mPrevEvent;
        if (motionEvent != null) {
            motionEvent.recycle();
            this.mPrevEvent = null;
        }
        MotionEvent motionEvent2 = this.mCurrEvent;
        if (motionEvent2 != null) {
            motionEvent2.recycle();
            this.mCurrEvent = null;
        }
        this.mGestureInProgress = false;
    }
}
