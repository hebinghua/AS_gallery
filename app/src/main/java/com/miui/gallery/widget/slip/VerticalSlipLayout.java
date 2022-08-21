package com.miui.gallery.widget.slip;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes3.dex */
public class VerticalSlipLayout extends FrameLayout {
    public int mActivePointerId;
    public View mBottomView;
    public boolean mDragEnable;
    public int mFixedSideSlipDis;
    public boolean mFlingToSlipped;
    public boolean mHasPendingUpdate;
    public float mInitialMotionX;
    public float mInitialMotionY;
    public boolean mIsBeingDragged;
    public boolean mIsSlipped;
    public boolean mIsSlippedWhenEnter;
    public float mLastMotionX;
    public float mLastMotionY;
    public int mMaxSlipY;
    public int mMaximumVelocity;
    public int mMinSlipY;
    public int mMinimumVelocity;
    public Drawable mNonSlippedDrawable;
    public OnSlipListener mSlipListener;
    public int mSlipMode;
    public SlipRunnable mSlipRunnable;
    public int mSlipState;
    public boolean mSlipViewInitialized;
    public Drawable mSlippedDrawable;
    public int mTopVInitMarginBottom;
    public int mTopVInitMarginTop;
    public View mTopView;
    public int mTouchSlop;
    public VelocityTracker mVelocityTracker;

    /* loaded from: classes3.dex */
    public interface OnSlipListener {
        void onSlipEnd(boolean z);

        void onSlipStart(boolean z);

        void onSlipStateChanged(int i);

        void onSlipping(float f);
    }

    /* renamed from: $r8$lambda$-6C0nSOc0YAH9kMCIn-4b5O6fu4 */
    public static /* synthetic */ void m1835$r8$lambda$6C0nSOc0YAH9kMCIn4b5O6fu4(VerticalSlipLayout verticalSlipLayout) {
        verticalSlipLayout.lambda$onSlipEnd$0();
    }

    public final float translateTouchDistance(float f) {
        return f * 0.6f;
    }

    public VerticalSlipLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VerticalSlipLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDragEnable = true;
        this.mInitialMotionX = 0.0f;
        this.mInitialMotionY = 0.0f;
        this.mLastMotionY = 0.0f;
        this.mLastMotionX = 0.0f;
        this.mActivePointerId = -1;
        this.mSlipMode = 0;
        this.mSlipState = 0;
        this.mIsSlippedWhenEnter = false;
        this.mMinSlipY = -1;
        this.mMaxSlipY = -1;
        this.mFixedSideSlipDis = 0;
        this.mIsSlipped = false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.VerticalSlipLayout, i, 0);
        this.mSlipMode = obtainStyledAttributes.getInt(1, 0);
        this.mIsSlippedWhenEnter = obtainStyledAttributes.getBoolean(3, false);
        this.mFixedSideSlipDis = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(2);
        if (drawable != null) {
            this.mSlippedDrawable = drawable;
        }
        obtainStyledAttributes.recycle();
        initViewPager();
    }

    public final void initViewPager() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        setBackground(this.mNonSlippedDrawable);
    }

    public void setSlippedWhenEnter(boolean z) {
        if (this.mMinSlipY != -1) {
            setSlipped(true);
        } else {
            this.mIsSlippedWhenEnter = z;
        }
    }

    public void setFixedSideSlipDistance(int i) {
        this.mFixedSideSlipDis = i;
        if (isSlipped()) {
            setSlippedInternal(false);
        }
    }

    public final void initSlipView() {
        int childCount = getChildCount();
        if (childCount < 2) {
            return;
        }
        this.mTopView = getChildAt(childCount - 1);
        this.mBottomView = getChildAt(childCount - 2);
        this.mTopVInitMarginTop = getLayoutParams(this.mTopView).topMargin;
        this.mTopVInitMarginBottom = getLayoutParams(this.mTopView).bottomMargin;
        setBottomViewVisible(this.mIsSlippedWhenEnter);
        this.mSlipViewInitialized = true;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        if (!this.mSlipViewInitialized) {
            initSlipView();
        }
    }

    public void setDraggable(boolean z) {
        this.mDragEnable = z;
    }

    public void setOnSlipListener(OnSlipListener onSlipListener) {
        this.mSlipListener = onSlipListener;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (this.mSlipState == 2) {
            return false;
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public final boolean canSlip() {
        return this.mSlipMode != 0 && this.mDragEnable && this.mSlipViewInitialized && getMinSlipY() < getMaxSlipY();
    }

    public final boolean needIntercept(float f, float f2) {
        if (Math.abs(f) > Math.abs(f2)) {
            return false;
        }
        int i = this.mSlipMode;
        if (i == 2) {
            return isSlipped() ? f2 > 0.0f : f2 < 0.0f && ((double) Math.abs(f2)) * Math.tan(0.3141592653589793d) > ((double) Math.abs(f));
        } else if (i != 1) {
            return false;
        } else {
            return isSlipped() ? f2 < 0.0f : f2 > 0.0f && ((double) Math.abs(f2)) * Math.tan(0.3141592653589793d) > ((double) Math.abs(f));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:56:0x001e, code lost:
        if (r0 != 3) goto L15;
     */
    @Override // android.view.ViewGroup
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onInterceptTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            boolean r0 = r6.canSlip()
            r1 = 0
            if (r0 != 0) goto L8
            return r1
        L8:
            int r0 = r7.getPointerCount()
            r2 = 1
            if (r0 <= r2) goto L10
            return r1
        L10:
            int r0 = r7.getAction()
            r0 = r0 & 255(0xff, float:3.57E-43)
            if (r0 == 0) goto L87
            if (r0 == r2) goto L83
            r2 = 2
            if (r0 == r2) goto L22
            r2 = 3
            if (r0 == r2) goto L83
            goto L9f
        L22:
            int r0 = r6.mActivePointerId
            r1 = -1
            if (r0 != r1) goto L29
            goto L9f
        L29:
            int r0 = r7.findPointerIndex(r0)
            if (r0 >= 0) goto L30
            goto L9f
        L30:
            float r3 = r7.getX(r0)
            float r0 = r7.getY(r0)
            boolean r4 = r6.mIsBeingDragged
            if (r4 != 0) goto L6f
            float r4 = r6.mInitialMotionY
            float r4 = r0 - r4
            float r4 = java.lang.Math.abs(r4)
            int r5 = r6.mTouchSlop
            int r5 = r5 * r2
            float r2 = (float) r5
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 <= 0) goto L6f
            float r2 = r6.mInitialMotionX
            float r2 = r3 - r2
            float r4 = r6.mInitialMotionY
            float r4 = r0 - r4
            boolean r2 = r6.needIntercept(r2, r4)
            if (r2 == 0) goto L6d
            float r1 = r6.mLastMotionX
            r6.mInitialMotionX = r1
            float r1 = r6.mLastMotionY
            r6.mInitialMotionY = r1
            r6.startDrag()
            java.lang.String r1 = "VerticalSlipLayout"
            java.lang.String r2 = "onSlipStart"
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r2)
            goto L6f
        L6d:
            r6.mActivePointerId = r1
        L6f:
            boolean r1 = r6.mIsBeingDragged
            if (r1 == 0) goto L7e
            float r1 = r6.mLastMotionY
            float r1 = r0 - r1
            float r1 = r6.translateTouchDistance(r1)
            r6.performSlipBy(r1)
        L7e:
            r6.mLastMotionX = r3
            r6.mLastMotionY = r0
            goto L9f
        L83:
            r6.endDrag()
            return r1
        L87:
            r6.mIsBeingDragged = r1
            float r0 = r7.getX()
            r6.mInitialMotionX = r0
            r6.mLastMotionX = r0
            float r0 = r7.getY()
            r6.mInitialMotionY = r0
            r6.mLastMotionY = r0
            int r0 = r7.getPointerId(r1)
            r6.mActivePointerId = r0
        L9f:
            boolean r0 = r6.mIsBeingDragged
            if (r0 == 0) goto Lab
            r6.ensureVelocityTracker()
            android.view.VelocityTracker r0 = r6.mVelocityTracker
            r0.addMovement(r7)
        Lab:
            boolean r7 = r6.mIsBeingDragged
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.slip.VerticalSlipLayout.onInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void ensureVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    public final void startDrag() {
        this.mIsBeingDragged = true;
        setSlipState(1);
        onSlipStart(false);
    }

    public final void onSlipStart(boolean z) {
        setBackground(this.mSlippedDrawable);
        setBottomViewVisible(true);
        OnSlipListener onSlipListener = this.mSlipListener;
        if (onSlipListener != null) {
            onSlipListener.onSlipStart(z);
        }
    }

    public final void onSlipEnd() {
        post(new Runnable() { // from class: com.miui.gallery.widget.slip.VerticalSlipLayout$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VerticalSlipLayout.m1835$r8$lambda$6C0nSOc0YAH9kMCIn4b5O6fu4(VerticalSlipLayout.this);
            }
        });
    }

    public /* synthetic */ void lambda$onSlipEnd$0() {
        if (this.mIsSlipped) {
            setBackground(this.mSlippedDrawable);
        } else {
            setBackground(this.mNonSlippedDrawable);
            setBottomViewVisible(false);
        }
        notifySlipAnimChildren(this, this.mIsSlipped ? 1.0f : 0.0f);
        requestLayout();
        OnSlipListener onSlipListener = this.mSlipListener;
        if (onSlipListener != null) {
            onSlipListener.onSlipEnd(this.mIsSlipped);
        }
        DefaultLogger.d("VerticalSlipLayout", "onSlipEnd %s", Boolean.valueOf(this.mIsSlipped));
    }

    public final void endDrag() {
        this.mIsBeingDragged = false;
        this.mActivePointerId = -1;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int findPointerIndex;
        int i = 0;
        if (!canSlip()) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        if (action == 0 && motionEvent.getEdgeFlags() != 0) {
            endDrag();
            return false;
        }
        if (action == 0) {
            this.mIsBeingDragged = false;
            float x = motionEvent.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            float y = motionEvent.getY();
            this.mInitialMotionY = y;
            this.mLastMotionY = y;
            this.mActivePointerId = motionEvent.getPointerId(0);
        } else if (action == 1) {
            if (this.mIsBeingDragged) {
                int i2 = this.mActivePointerId;
                if (i2 != -1) {
                    int findPointerIndex2 = motionEvent.findPointerIndex(i2);
                    if (findPointerIndex2 >= 0) {
                        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                        int yVelocity = (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                        slipTo(calculateSlipTo(translateTouchDistance(motionEvent.getY(findPointerIndex2) - this.mInitialMotionY), yVelocity), yVelocity, true);
                    }
                } else {
                    setUnSlipped(true);
                }
            }
            endDrag();
        } else if (action == 2) {
            int i3 = this.mActivePointerId;
            if (i3 != -1 && (findPointerIndex = motionEvent.findPointerIndex(i3)) >= 0) {
                float x2 = motionEvent.getX(findPointerIndex);
                float y2 = motionEvent.getY(findPointerIndex);
                if (!this.mIsBeingDragged && Math.abs(y2 - this.mInitialMotionY) > this.mTouchSlop * 2) {
                    if (needIntercept(x2 - this.mInitialMotionX, y2 - this.mInitialMotionY)) {
                        this.mInitialMotionX = this.mLastMotionX;
                        this.mInitialMotionY = this.mLastMotionY;
                        startDrag();
                        DefaultLogger.d("VerticalSlipLayout", "onSlipStart");
                    } else {
                        this.mActivePointerId = -1;
                    }
                }
                if (this.mIsBeingDragged) {
                    performSlipBy(translateTouchDistance(y2 - this.mLastMotionY));
                }
                this.mLastMotionX = x2;
                this.mLastMotionY = y2;
            }
        } else if (action == 3) {
            if (this.mIsBeingDragged) {
                int i4 = this.mActivePointerId;
                if (i4 != -1) {
                    int findPointerIndex3 = motionEvent.findPointerIndex(i4);
                    if (findPointerIndex3 >= 0) {
                        slipTo(calculateSlipTo(translateTouchDistance(motionEvent.getY(findPointerIndex3) - this.mInitialMotionY), 0), 0, true);
                    }
                } else {
                    setUnSlipped(true);
                }
            }
            endDrag();
        } else if (action == 5) {
            int actionIndex = motionEvent.getActionIndex();
            float x3 = motionEvent.getX(actionIndex);
            float y3 = motionEvent.getY(actionIndex);
            this.mLastMotionX = x3;
            this.mLastMotionY = y3;
            this.mActivePointerId = motionEvent.getPointerId(actionIndex);
            if (!this.mIsBeingDragged) {
                this.mInitialMotionX = x3;
                this.mInitialMotionY = y3;
            }
        } else if (action == 6) {
            int actionIndex2 = motionEvent.getActionIndex();
            if (this.mActivePointerId == motionEvent.getPointerId(actionIndex2)) {
                if (actionIndex2 == 0) {
                    i = 1;
                }
                float x4 = motionEvent.getX(i);
                float y4 = motionEvent.getY(i);
                this.mLastMotionX = x4;
                this.mLastMotionY = y4;
                this.mActivePointerId = motionEvent.getPointerId(i);
                if (!this.mIsBeingDragged) {
                    this.mInitialMotionX = x4;
                    this.mInitialMotionY = y4;
                }
            }
        }
        if (this.mIsBeingDragged) {
            ensureVelocityTracker();
            this.mVelocityTracker.addMovement(motionEvent);
        }
        return true;
    }

    public final int calculateSlipTo(float f, int i) {
        float abs = Math.abs(f) / (getMaxSlipY() - getMinSlipY());
        float max = Math.max(this.mMinimumVelocity * 10, this.mMaximumVelocity / 10);
        if (f > 0.0f) {
            if (abs > 0.2f) {
                return ((float) i) < (-max) ? getMinSlipY() : getMaxSlipY();
            } else if (abs <= 0.05f) {
                return getMinSlipY();
            } else {
                return i > this.mMinimumVelocity ? getMaxSlipY() : getMinSlipY();
            }
        } else if (abs > 0.2f) {
            return ((float) i) > max ? getMaxSlipY() : getMinSlipY();
        } else if (abs <= 0.05f) {
            return getMaxSlipY();
        } else {
            return i < (-this.mMinimumVelocity) ? getMinSlipY() : getMaxSlipY();
        }
    }

    private int getMinSlipY() {
        return this.mMinSlipY;
    }

    private int getMaxSlipY() {
        return this.mMaxSlipY;
    }

    public int getSlippedY() {
        int i = this.mSlipMode;
        if (i != 1) {
            if (i == 2) {
                return getMinSlipY();
            }
            return 0;
        }
        return getMaxSlipY();
    }

    public final void performSlipBy(float f) {
        performSlipTo(Math.min(Math.max(getSlipY() + f, getMinSlipY()), getMaxSlipY()));
    }

    public final void performSlipTo(float f) {
        FrameLayout.LayoutParams layoutParams = getLayoutParams(this.mTopView);
        float minSlipY = ((f - getMinSlipY()) * 1.0f) / (getMaxSlipY() - getMinSlipY());
        int i = this.mSlipMode;
        if (i == 1) {
            layoutParams.topMargin = (int) (getHeight() - f);
            int i2 = this.mFixedSideSlipDis;
            if (i2 > 18.0d) {
                layoutParams.bottomMargin = (int) (i2 * minSlipY);
            }
        } else if (i == 2) {
            minSlipY = 1.0f - minSlipY;
            layoutParams.bottomMargin = (int) (getHeight() - f);
            int i3 = this.mFixedSideSlipDis;
            if (i3 > 18.0d) {
                layoutParams.topMargin = (int) (i3 * minSlipY);
            }
        }
        notifySlipAnimChildren(this, minSlipY);
        this.mTopView.setLayoutParams(layoutParams);
    }

    public final void slipTo(int i, int i2, boolean z) {
        slipTo(i, i2, z, false);
    }

    public final void slipTo(final int i, final int i2, final boolean z, final boolean z2) {
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.widget.slip.VerticalSlipLayout.1
            {
                VerticalSlipLayout.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                if (VerticalSlipLayout.this.mSlipState == 0) {
                    VerticalSlipLayout.this.onSlipStart(z2);
                    VerticalSlipLayout.this.setSlipState(2);
                }
                if (z) {
                    if (VerticalSlipLayout.this.mSlipRunnable == null) {
                        VerticalSlipLayout verticalSlipLayout = VerticalSlipLayout.this;
                        verticalSlipLayout.mSlipRunnable = new SlipRunnable(verticalSlipLayout.getContext());
                    }
                    VerticalSlipLayout.this.mSlipRunnable.slipTo(i, i2);
                    return;
                }
                if (VerticalSlipLayout.this.mSlipRunnable != null) {
                    VerticalSlipLayout.this.mSlipRunnable.abort();
                }
                VerticalSlipLayout.this.performSlipTo(i);
                VerticalSlipLayout.this.refreshSlipStatusByTarget(i);
                VerticalSlipLayout.this.setSlipState(0);
                VerticalSlipLayout.this.onSlipEnd();
            }
        });
    }

    public int getSlipY() {
        int i = this.mSlipMode;
        if (i == 1) {
            return this.mTopView.getTop();
        }
        if (i != 2) {
            return -1;
        }
        return this.mTopView.getBottom();
    }

    public boolean isSlipped() {
        return this.mIsSlipped;
    }

    public final void refreshSlipStatusByTarget(int i) {
        int i2 = this.mSlipMode;
        boolean z = false;
        if (i2 == 1) {
            if (i > getMinSlipY()) {
                z = true;
            }
            this.mIsSlipped = z;
        } else if (i2 != 2) {
        } else {
            if (i < getMaxSlipY()) {
                z = true;
            }
            this.mIsSlipped = z;
        }
    }

    public void refreshSlipState(boolean z) {
        this.mIsSlipped = z;
    }

    public boolean isFlingToSlipped() {
        return this.mFlingToSlipped;
    }

    public boolean isSlipping() {
        return this.mSlipState != 0;
    }

    public void setUnSlipped(boolean z) {
        if (!canSlip() || !isSlipped()) {
            return;
        }
        setUnSlippedInternal(z);
    }

    private void setUnSlippedInternal(boolean z) {
        int i = this.mSlipMode;
        if (i == 1) {
            slipTo(getMinSlipY(), 0, z);
        } else if (i != 2) {
        } else {
            slipTo(getMaxSlipY(), 0, z);
        }
    }

    public void setSlipped(boolean z) {
        setSlipped(z, false);
    }

    public void setSlipped(boolean z, boolean z2) {
        if (!canSlip() || isSlipped()) {
            return;
        }
        setSlippedInternal(z, z2);
    }

    private void setSlippedInternal(boolean z) {
        setSlippedInternal(z, false);
    }

    public final void setSlippedInternal(boolean z, boolean z2) {
        int i = this.mSlipMode;
        if (i == 1) {
            slipTo(getMaxSlipY(), 0, z, z2);
        } else if (i != 2) {
        } else {
            slipTo(getMinSlipY(), 0, z, z2);
        }
    }

    public final boolean needInitSlipParams() {
        return this.mMinSlipY == -1 || this.mHasPendingUpdate;
    }

    public final FrameLayout.LayoutParams getLayoutParams(View view) {
        return (FrameLayout.LayoutParams) view.getLayoutParams();
    }

    public final void resetSlipParams() {
        if (this.mSlipState != 0) {
            this.mHasPendingUpdate = true;
            return;
        }
        this.mMinSlipY = -1;
        this.mMaxSlipY = -1;
    }

    public final void initSlipParams() {
        this.mHasPendingUpdate = false;
        int i = this.mSlipMode;
        if (i == 1) {
            this.mMinSlipY = this.mTopVInitMarginTop;
            this.mMaxSlipY = this.mBottomView.getBottom();
        } else if (i != 2) {
        } else {
            this.mMinSlipY = this.mBottomView.getTop();
            this.mMaxSlipY = ((getLayoutParams(this.mTopView).topMargin + this.mTopView.getHeight()) + getLayoutParams(this.mTopView).bottomMargin) - this.mTopVInitMarginBottom;
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        resetSlipParams();
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        resetSlipParams();
        super.onConfigurationChanged(configuration);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mSlipViewInitialized) {
            if (needInitSlipParams()) {
                initSlipParams();
            }
            if (!this.mIsSlippedWhenEnter) {
                return;
            }
            this.mIsSlippedWhenEnter = false;
            setSlippedInternal(false);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        SlipRunnable slipRunnable = this.mSlipRunnable;
        if (slipRunnable != null) {
            slipRunnable.abort();
        }
        super.onDetachedFromWindow();
    }

    public void doRelease() {
        this.mSlipListener = null;
    }

    public void setSlipState(int i) {
        if (this.mSlipState != i) {
            this.mSlipState = i;
            OnSlipListener onSlipListener = this.mSlipListener;
            if (onSlipListener == null) {
                return;
            }
            onSlipListener.onSlipStateChanged(i);
        }
    }

    private void setBottomViewVisible(boolean z) {
        this.mBottomView.setVisibility(z ? 0 : 4);
    }

    /* loaded from: classes3.dex */
    public class SlipRunnable implements Runnable {
        public boolean isAbort;
        public final Interpolator mInterpolator;
        public Scroller mScroller;
        public int mTargetY;

        public SlipRunnable(Context context) {
            VerticalSlipLayout.this = r2;
            CubicEaseOutInterpolator cubicEaseOutInterpolator = new CubicEaseOutInterpolator();
            this.mInterpolator = cubicEaseOutInterpolator;
            this.mScroller = new Scroller(context, cubicEaseOutInterpolator);
        }

        public void slipTo(int i, int i2) {
            abort();
            boolean z = false;
            this.isAbort = false;
            int slipY = VerticalSlipLayout.this.getSlipY();
            int i3 = i - slipY;
            this.mTargetY = i;
            if (i3 == 0) {
                completeSlip();
                return;
            }
            VerticalSlipLayout verticalSlipLayout = VerticalSlipLayout.this;
            if (verticalSlipLayout.getSlippedY() == i) {
                z = true;
            }
            verticalSlipLayout.mFlingToSlipped = z;
            VerticalSlipLayout.this.setSlipState(2);
            this.mScroller.startScroll(0, slipY, 0, i3, 250);
            VerticalSlipLayout.this.postOnAnimation(this);
        }

        public final void completeSlip() {
            VerticalSlipLayout.this.mFlingToSlipped = false;
            this.mScroller.abortAnimation();
            VerticalSlipLayout.this.refreshSlipStatusByTarget(this.mTargetY);
            VerticalSlipLayout.this.setSlipState(0);
            VerticalSlipLayout.this.onSlipEnd();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
                VerticalSlipLayout.this.performSlipTo(this.mScroller.getCurrY());
                VerticalSlipLayout.this.postOnAnimation(this);
            } else if (this.isAbort) {
            } else {
                completeSlip();
            }
        }

        public void abort() {
            VerticalSlipLayout.this.mFlingToSlipped = false;
            this.isAbort = true;
            this.mScroller.abortAnimation();
            VerticalSlipLayout.this.removeCallbacks(this);
        }
    }

    public final void notifySlipAnimChildren(View view, float f) {
        OnSlipListener onSlipListener = this.mSlipListener;
        if (onSlipListener != null) {
            onSlipListener.onSlipping(f);
        }
        if (view instanceof ISlipAnimView) {
            ((ISlipAnimView) view).onSlipping(f);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                notifySlipAnimChildren(viewGroup.getChildAt(i), f);
            }
        }
    }
}
