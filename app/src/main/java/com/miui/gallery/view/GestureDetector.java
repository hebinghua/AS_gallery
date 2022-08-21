package com.miui.gallery.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.miui.gallery.util.ViewUtils;
import java.util.Objects;

/* loaded from: classes2.dex */
public class GestureDetector {
    public boolean mAlwaysInBiggerTapRegion;
    public boolean mAlwaysInTapRegion;
    public OnContextClickListener mContextClickListener;
    public MotionEvent mCurrentDownEvent;
    public boolean mDeferConfirmSingleTap;
    public OnDoubleTapListener mDoubleTapListener;
    public int mDoubleTapSlopSquare;
    public int mDoubleTapTouchSlopSquare;
    public float mDownFocusX;
    public float mDownFocusY;
    public final Handler mHandler;
    public boolean mIgnoreNextUpEvent;
    public boolean mInContextClick;
    public boolean mInLongPress;
    public boolean mIsDoubleTapEnabled;
    public boolean mIsDoubleTapping;
    public boolean mIsLongpressEnabled;
    public float mLastFocusX;
    public float mLastFocusY;
    public final OnGestureListener mListener;
    public int mMaximumFlingVelocity;
    public int mMinimumFlingVelocity;
    public MotionEvent mPreviousUpEvent;
    public boolean mStillDown;
    public int mTouchSlopSquare;
    public VelocityTracker mVelocityTracker;
    public static final int LONGPRESS_TIMEOUT = ViewUtils.getRealLongPressedTimeout();
    public static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    public static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();

    /* loaded from: classes2.dex */
    public interface OnContextClickListener {
    }

    /* loaded from: classes2.dex */
    public interface OnDoubleTapListener {
        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onDoubleTapEvent(MotionEvent motionEvent);

        boolean onSingleTapConfirmed(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public interface OnGestureListener {
        boolean onDown(MotionEvent motionEvent);

        boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onLongPress(MotionEvent motionEvent);

        boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);

        void onShowPress(MotionEvent motionEvent);

        boolean onSingleTapUp(MotionEvent motionEvent);
    }

    /* loaded from: classes2.dex */
    public static class SimpleOnGestureListener implements OnGestureListener, OnDoubleTapListener, OnContextClickListener {
        @Override // com.miui.gallery.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.miui.gallery.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return false;
        }

        @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
        }

        @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
        public void onShowPress(MotionEvent motionEvent) {
        }

        @Override // com.miui.gallery.view.GestureDetector.OnDoubleTapListener
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.miui.gallery.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public class GestureHandler extends Handler {
        public GestureHandler() {
        }

        public GestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                GestureDetector.this.mListener.onShowPress(GestureDetector.this.mCurrentDownEvent);
            } else if (i == 2) {
                GestureDetector.this.dispatchLongPress();
            } else if (i == 3) {
                if (GestureDetector.this.mDoubleTapListener == null) {
                    return;
                }
                if (!GestureDetector.this.mStillDown) {
                    GestureDetector.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetector.this.mCurrentDownEvent);
                } else {
                    GestureDetector.this.mDeferConfirmSingleTap = true;
                }
            } else {
                throw new RuntimeException("Unknown message " + message);
            }
        }
    }

    public GestureDetector(Context context, OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public GestureDetector(Context context, OnGestureListener onGestureListener, Handler handler) {
        if (handler != null) {
            this.mHandler = new GestureHandler(handler);
        } else {
            this.mHandler = new GestureHandler();
        }
        this.mListener = onGestureListener;
        if (onGestureListener instanceof OnDoubleTapListener) {
            setOnDoubleTapListener((OnDoubleTapListener) onGestureListener);
        }
        if (onGestureListener instanceof OnContextClickListener) {
            setContextClickListener((OnContextClickListener) onGestureListener);
        }
        init(context);
    }

    public final void init(Context context) {
        int i;
        int i2;
        Objects.requireNonNull(this.mListener, "OnGestureListener must not be null");
        this.mIsLongpressEnabled = true;
        this.mIsDoubleTapEnabled = true;
        if (context == null) {
            i = ViewConfiguration.getTouchSlop();
            i2 = 100;
            this.mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            this.mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            int scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
            int scaledDoubleTapSlop = viewConfiguration.getScaledDoubleTapSlop();
            this.mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            this.mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
            i = scaledTouchSlop;
            i2 = scaledDoubleTapSlop;
        }
        int i3 = i * i;
        this.mTouchSlopSquare = i3;
        this.mDoubleTapTouchSlopSquare = i3;
        this.mDoubleTapSlopSquare = i2 * i2;
    }

    public void setOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }

    public void setContextClickListener(OnContextClickListener onContextClickListener) {
        this.mContextClickListener = onContextClickListener;
    }

    public void setTouchSlop(int i) {
        if (i >= 1) {
            this.mTouchSlopSquare = i * i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:112:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0228  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r13) {
        /*
            Method dump skipped, instructions count: 596
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.view.GestureDetector.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void cancel() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mIsDoubleTapping = false;
        this.mStillDown = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    public final void cancelTaps() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        this.mHandler.removeMessages(3);
        this.mIsDoubleTapping = false;
        this.mAlwaysInTapRegion = false;
        this.mAlwaysInBiggerTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = false;
        this.mInContextClick = false;
        this.mIgnoreNextUpEvent = false;
    }

    public final boolean isConsideredDoubleTap(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
        if (this.mIsDoubleTapEnabled && this.mAlwaysInBiggerTapRegion) {
            long eventTime = motionEvent3.getEventTime() - motionEvent2.getEventTime();
            if (eventTime > DOUBLE_TAP_TIMEOUT || eventTime < 40) {
                return false;
            }
            int x = ((int) motionEvent.getX()) - ((int) motionEvent3.getX());
            int y = ((int) motionEvent.getY()) - ((int) motionEvent3.getY());
            return (x * x) + (y * y) < this.mDoubleTapSlopSquare;
        }
        return false;
    }

    public final void dispatchLongPress() {
        this.mHandler.removeMessages(3);
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = true;
        this.mListener.onLongPress(this.mCurrentDownEvent);
    }
}
