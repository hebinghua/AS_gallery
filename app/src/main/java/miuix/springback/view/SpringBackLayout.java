package miuix.springback.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.widget.ListView;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.widget.ListViewCompat;
import androidx.core.widget.NestedScrollView;
import java.util.ArrayList;
import java.util.List;
import miuix.core.view.NestedCurrentFling;
import miuix.os.Build;
import miuix.springback.R$styleable;

/* loaded from: classes3.dex */
public class SpringBackLayout extends ViewGroup implements NestedScrollingParent3, NestedScrollingChild3, NestedCurrentFling {
    public int consumeNestFlingCounter;
    public int mActivePointerId;
    public SpringBackLayoutHelper mHelper;
    public float mInitialDownX;
    public float mInitialDownY;
    public float mInitialMotionX;
    public float mInitialMotionY;
    public boolean mIsBeingDragged;
    public boolean mNestedFlingInProgress;
    public int mNestedScrollAxes;
    public boolean mNestedScrollInProgress;
    public final NestedScrollingChildHelper mNestedScrollingChildHelper;
    public final NestedScrollingParentHelper mNestedScrollingParentHelper;
    public final int[] mNestedScrollingV2ConsumedCompat;
    public List<OnScrollListener> mOnScrollListeners;
    public OnSpringListener mOnSpringListener;
    public int mOriginScrollOrientation;
    public final int[] mParentOffsetInWindow;
    public final int[] mParentScrollConsumed;
    public final int mScreenHeight;
    public final int mScreenWith;
    public boolean mScrollByFling;
    public int mScrollOrientation;
    public int mScrollState;
    public boolean mSpringBackEnable;
    public int mSpringBackMode;
    public SpringScroller mSpringScroller;
    public View mTarget;
    public int mTargetId;
    public float mTotalFlingUnconsumed;
    public float mTotalScrollBottomUnconsumed;
    public float mTotalScrollTopUnconsumed;
    public int mTouchSlop;
    public float mVelocityX;
    public float mVelocityY;

    /* loaded from: classes3.dex */
    public interface OnScrollListener {
        void onScrolled(SpringBackLayout springBackLayout, int i, int i2);

        void onStateChanged(int i, int i2, boolean z);
    }

    /* loaded from: classes3.dex */
    public interface OnSpringListener {
        boolean onSpringBack();
    }

    public SpringBackLayout(Context context) {
        this(context, null);
    }

    public SpringBackLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mActivePointerId = -1;
        this.consumeNestFlingCounter = 0;
        this.mParentScrollConsumed = new int[2];
        this.mParentOffsetInWindow = new int[2];
        this.mNestedScrollingV2ConsumedCompat = new int[2];
        this.mSpringBackEnable = true;
        this.mOnScrollListeners = new ArrayList();
        this.mScrollState = 0;
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = miuix.core.view.NestedScrollingChildHelper.obtain(this);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SpringBackLayout);
        this.mTargetId = obtainStyledAttributes.getResourceId(R$styleable.SpringBackLayout_scrollableView, -1);
        this.mOriginScrollOrientation = obtainStyledAttributes.getInt(R$styleable.SpringBackLayout_scrollOrientation, 2);
        this.mSpringBackMode = obtainStyledAttributes.getInt(R$styleable.SpringBackLayout_springBackMode, 3);
        obtainStyledAttributes.recycle();
        this.mSpringScroller = new SpringScroller();
        this.mHelper = new SpringBackLayoutHelper(this, this.mOriginScrollOrientation);
        setNestedScrollingEnabled(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        this.mScreenWith = displayMetrics.widthPixels;
        this.mScreenHeight = displayMetrics.heightPixels;
        if (Build.IS_INTERNATIONAL_BUILD) {
            this.mSpringBackEnable = false;
        }
    }

    public void setSpringBackEnable(boolean z) {
        this.mSpringBackEnable = z;
    }

    public boolean springBackEnable() {
        return this.mSpringBackEnable;
    }

    public void setScrollOrientation(int i) {
        this.mOriginScrollOrientation = i;
        this.mHelper.mTargetScrollOrientation = i;
    }

    public void setSpringBackMode(int i) {
        this.mSpringBackMode = i;
    }

    public int getSpringBackMode() {
        return this.mSpringBackMode;
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        View view = this.mTarget;
        if (view == null || !(view instanceof NestedScrollingChild3) || Build.VERSION.SDK_INT < 21 || z == view.isNestedScrollingEnabled()) {
            return;
        }
        this.mTarget.setNestedScrollingEnabled(z);
    }

    public final boolean supportTopSpringBackMode() {
        return (this.mSpringBackMode & 1) != 0;
    }

    public final boolean supportBottomSpringBackMode() {
        return (this.mSpringBackMode & 2) != 0;
    }

    public void setTarget(View view) {
        this.mTarget = view;
        if (Build.VERSION.SDK_INT < 21 || !(view instanceof NestedScrollingChild3) || view.isNestedScrollingEnabled()) {
            return;
        }
        this.mTarget.setNestedScrollingEnabled(true);
    }

    public final void ensureTarget() {
        if (this.mTarget == null) {
            int i = this.mTargetId;
            if (i == -1) {
                throw new IllegalArgumentException("invalid target Id");
            }
            this.mTarget = findViewById(i);
        }
        if (this.mTarget == null) {
            throw new IllegalArgumentException("fail to get target");
        }
        if (Build.VERSION.SDK_INT >= 21 && isEnabled()) {
            View view = this.mTarget;
            if ((view instanceof NestedScrollingChild3) && !view.isNestedScrollingEnabled()) {
                this.mTarget.setNestedScrollingEnabled(true);
            }
        }
        if (this.mTarget.getOverScrollMode() == 2) {
            return;
        }
        this.mTarget.setOverScrollMode(2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        this.mTarget.layout(paddingLeft, paddingTop, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, ((measuredHeight - getPaddingTop()) - getPaddingBottom()) + paddingTop);
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        ensureTarget();
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        measureChild(this.mTarget, i, i2);
        if (size > this.mTarget.getMeasuredWidth()) {
            size = this.mTarget.getMeasuredWidth();
        }
        if (size2 > this.mTarget.getMeasuredHeight()) {
            size2 = this.mTarget.getMeasuredHeight();
        }
        if (mode != 1073741824) {
            size = this.mTarget.getMeasuredWidth();
        }
        if (mode2 != 1073741824) {
            size2 = this.mTarget.getMeasuredHeight();
        }
        setMeasuredDimension(size, size2);
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.mSpringScroller.computeScrollOffset()) {
            scrollTo(this.mSpringScroller.getCurrX(), this.mSpringScroller.getCurrY());
            if (!this.mSpringScroller.isFinished()) {
                postInvalidateOnAnimation();
            } else {
                dispatchScrollState(0);
            }
        }
    }

    @Override // android.view.View
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        for (OnScrollListener onScrollListener : this.mOnScrollListeners) {
            onScrollListener.onScrolled(this, i - i3, i2 - i4);
        }
    }

    public final boolean isTargetScrollOrientation(int i) {
        return this.mScrollOrientation == i;
    }

    public final boolean isTargetScrollToTop(int i) {
        if (i == 2) {
            View view = this.mTarget;
            if (view instanceof ListView) {
                return !ListViewCompat.canScrollList((ListView) view, -1);
            }
            return !view.canScrollVertically(-1);
        }
        return !this.mTarget.canScrollHorizontally(-1);
    }

    public final boolean isTargetScrollToBottom(int i) {
        if (i == 2) {
            View view = this.mTarget;
            if (view instanceof ListView) {
                return !ListViewCompat.canScrollList((ListView) view, 1);
            }
            return !view.canScrollVertically(1);
        }
        return !this.mTarget.canScrollHorizontally(1);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 && this.mScrollState == 2 && this.mHelper.isTouchInTarget(motionEvent)) {
            dispatchScrollState(1);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (motionEvent.getActionMasked() == 1 && this.mScrollState != 2) {
            dispatchScrollState(0);
        }
        return dispatchTouchEvent;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mSpringBackEnable && isEnabled() && !this.mNestedFlingInProgress && !this.mNestedScrollInProgress && (Build.VERSION.SDK_INT < 21 || !this.mTarget.isNestedScrollingEnabled())) {
            int actionMasked = motionEvent.getActionMasked();
            if (!this.mSpringScroller.isFinished() && actionMasked == 0) {
                this.mSpringScroller.forceStop();
            }
            if (!supportTopSpringBackMode() && !supportBottomSpringBackMode()) {
                return false;
            }
            int i = this.mOriginScrollOrientation;
            if ((i & 4) != 0) {
                checkOrientation(motionEvent);
                if (isTargetScrollOrientation(2) && (this.mOriginScrollOrientation & 1) != 0 && getScrollX() == 0.0f) {
                    return false;
                }
                if (isTargetScrollOrientation(1) && (this.mOriginScrollOrientation & 2) != 0 && getScrollY() == 0.0f) {
                    return false;
                }
                if (isTargetScrollOrientation(2) || isTargetScrollOrientation(1)) {
                    disallowParentInterceptTouchEvent(true);
                }
            } else {
                this.mScrollOrientation = i;
            }
            if (isTargetScrollOrientation(2)) {
                return onVerticalInterceptTouchEvent(motionEvent);
            }
            if (isTargetScrollOrientation(1)) {
                return onHorizontalInterceptTouchEvent(motionEvent);
            }
            return false;
        }
        return false;
    }

    public final void disallowParentInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(z);
        }
    }

    public final void checkOrientation(MotionEvent motionEvent) {
        int i;
        this.mHelper.checkOrientation(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            SpringBackLayoutHelper springBackLayoutHelper = this.mHelper;
            this.mInitialDownY = springBackLayoutHelper.mInitialDownY;
            this.mInitialDownX = springBackLayoutHelper.mInitialDownX;
            this.mActivePointerId = springBackLayoutHelper.mActivePointerId;
            if (getScrollY() != 0) {
                this.mScrollOrientation = 2;
                requestDisallowParentInterceptTouchEvent(true);
            } else if (getScrollX() != 0) {
                this.mScrollOrientation = 1;
                requestDisallowParentInterceptTouchEvent(true);
            } else {
                this.mScrollOrientation = 0;
            }
            if ((this.mOriginScrollOrientation & 2) != 0) {
                checkScrollStart(2);
                return;
            } else {
                checkScrollStart(1);
                return;
            }
        }
        if (actionMasked != 1) {
            if (actionMasked == 2) {
                if (this.mScrollOrientation != 0 || (i = this.mHelper.mScrollOrientation) == 0) {
                    return;
                }
                this.mScrollOrientation = i;
                return;
            } else if (actionMasked != 3) {
                if (actionMasked != 6) {
                    return;
                }
                onSecondaryPointerUp(motionEvent);
                return;
            }
        }
        disallowParentInterceptTouchEvent(false);
        if ((this.mOriginScrollOrientation & 2) != 0) {
            springBack(2);
        } else {
            springBack(1);
        }
    }

    public final boolean onVerticalInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (isTargetScrollToTop(2) || isTargetScrollToBottom(2)) {
            if (isTargetScrollToTop(2) && !supportTopSpringBackMode()) {
                return false;
            }
            if (isTargetScrollToBottom(2) && !supportBottomSpringBackMode()) {
                return false;
            }
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                int pointerId = motionEvent.getPointerId(0);
                this.mActivePointerId = pointerId;
                int findPointerIndex = motionEvent.findPointerIndex(pointerId);
                if (findPointerIndex < 0) {
                    return false;
                }
                this.mInitialDownY = motionEvent.getY(findPointerIndex);
                if (getScrollY() != 0) {
                    this.mIsBeingDragged = true;
                    this.mInitialMotionY = this.mInitialDownY;
                } else {
                    this.mIsBeingDragged = false;
                }
            } else {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int i = this.mActivePointerId;
                        if (i == -1) {
                            Log.e("SpringBackLayout", "Got ACTION_MOVE event but don't have an active pointer id.");
                            return false;
                        }
                        int findPointerIndex2 = motionEvent.findPointerIndex(i);
                        if (findPointerIndex2 < 0) {
                            Log.e("SpringBackLayout", "Got ACTION_MOVE event but have an invalid active pointer id.");
                            return false;
                        }
                        float y = motionEvent.getY(findPointerIndex2);
                        if (isTargetScrollToBottom(2) && isTargetScrollToTop(2)) {
                            z = true;
                        }
                        if ((!z && isTargetScrollToTop(2)) || (z && y > this.mInitialDownY)) {
                            if (y - this.mInitialDownY > this.mTouchSlop && !this.mIsBeingDragged) {
                                this.mIsBeingDragged = true;
                                dispatchScrollState(1);
                                this.mInitialMotionY = y;
                            }
                        } else if (this.mInitialDownY - y > this.mTouchSlop && !this.mIsBeingDragged) {
                            this.mIsBeingDragged = true;
                            dispatchScrollState(1);
                            this.mInitialMotionY = y;
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 6) {
                            onSecondaryPointerUp(motionEvent);
                        }
                    }
                }
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
            }
            return this.mIsBeingDragged;
        }
        return false;
    }

    public final boolean onHorizontalInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = false;
        if (isTargetScrollToTop(1) || isTargetScrollToBottom(1)) {
            if (isTargetScrollToTop(1) && !supportTopSpringBackMode()) {
                return false;
            }
            if (isTargetScrollToBottom(1) && !supportBottomSpringBackMode()) {
                return false;
            }
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                int pointerId = motionEvent.getPointerId(0);
                this.mActivePointerId = pointerId;
                int findPointerIndex = motionEvent.findPointerIndex(pointerId);
                if (findPointerIndex < 0) {
                    return false;
                }
                this.mInitialDownX = motionEvent.getX(findPointerIndex);
                if (getScrollX() != 0) {
                    this.mIsBeingDragged = true;
                    this.mInitialMotionX = this.mInitialDownX;
                } else {
                    this.mIsBeingDragged = false;
                }
            } else {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        int i = this.mActivePointerId;
                        if (i == -1) {
                            Log.e("SpringBackLayout", "Got ACTION_MOVE event but don't have an active pointer id.");
                            return false;
                        }
                        int findPointerIndex2 = motionEvent.findPointerIndex(i);
                        if (findPointerIndex2 < 0) {
                            Log.e("SpringBackLayout", "Got ACTION_MOVE event but have an invalid active pointer id.");
                            return false;
                        }
                        float x = motionEvent.getX(findPointerIndex2);
                        if (isTargetScrollToBottom(1) && isTargetScrollToTop(1)) {
                            z = true;
                        }
                        if ((!z && isTargetScrollToTop(1)) || (z && x > this.mInitialDownX)) {
                            if (x - this.mInitialDownX > this.mTouchSlop && !this.mIsBeingDragged) {
                                this.mIsBeingDragged = true;
                                dispatchScrollState(1);
                                this.mInitialMotionX = x;
                            }
                        } else if (this.mInitialDownX - x > this.mTouchSlop && !this.mIsBeingDragged) {
                            this.mIsBeingDragged = true;
                            dispatchScrollState(1);
                            this.mInitialMotionX = x;
                        }
                    } else if (actionMasked != 3) {
                        if (actionMasked == 6) {
                            onSecondaryPointerUp(motionEvent);
                        }
                    }
                }
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
            }
            return this.mIsBeingDragged;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (!isEnabled() || !this.mSpringBackEnable) {
            super.requestDisallowInterceptTouchEvent(z);
        }
    }

    public void internalRequestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
    }

    public void requestDisallowParentInterceptTouchEvent(boolean z) {
        ViewParent parent = getParent();
        parent.requestDisallowInterceptTouchEvent(z);
        while (parent != null) {
            if (parent instanceof SpringBackLayout) {
                ((SpringBackLayout) parent).internalRequestDisallowInterceptTouchEvent(z);
            }
            parent = parent.getParent();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (!isEnabled() || this.mNestedFlingInProgress || this.mNestedScrollInProgress || (Build.VERSION.SDK_INT >= 21 && this.mTarget.isNestedScrollingEnabled())) {
            return false;
        }
        if (!this.mSpringScroller.isFinished() && actionMasked == 0) {
            this.mSpringScroller.forceStop();
        }
        if (isTargetScrollOrientation(2)) {
            return onVerticalTouchEvent(motionEvent);
        }
        if (isTargetScrollOrientation(1)) {
            return onHorizontalTouchEvent(motionEvent);
        }
        return false;
    }

    public final boolean onHorizontalTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (isTargetScrollToTop(1) || isTargetScrollToBottom(1)) {
            if (isTargetScrollToTop(1) && isTargetScrollToBottom(1)) {
                return onScrollEvent(motionEvent, actionMasked, 1);
            }
            if (isTargetScrollToBottom(1)) {
                return onScrollUpEvent(motionEvent, actionMasked, 1);
            }
            return onScrollDownEvent(motionEvent, actionMasked, 1);
        }
        return false;
    }

    public final boolean onVerticalTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (isTargetScrollToTop(2) || isTargetScrollToBottom(2)) {
            if (isTargetScrollToTop(2) && isTargetScrollToBottom(2)) {
                return onScrollEvent(motionEvent, actionMasked, 2);
            }
            if (isTargetScrollToBottom(2)) {
                return onScrollUpEvent(motionEvent, actionMasked, 2);
            }
            return onScrollDownEvent(motionEvent, actionMasked, 2);
        }
        return false;
    }

    public final boolean onScrollEvent(MotionEvent motionEvent, int i, int i2) {
        float signum;
        float obtainSpringBackDistance;
        int actionIndex;
        if (i == 0) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            checkScrollStart(i2);
        } else if (i == 1) {
            if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e("SpringBackLayout", "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                springBack(i2);
            }
            this.mActivePointerId = -1;
            return false;
        } else if (i == 2) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex < 0) {
                Log.e("SpringBackLayout", "Got ACTION_MOVE event but have an invalid active pointer id.");
                return false;
            } else if (this.mIsBeingDragged) {
                if (i2 == 2) {
                    float y = motionEvent.getY(findPointerIndex);
                    signum = Math.signum(y - this.mInitialMotionY);
                    obtainSpringBackDistance = obtainSpringBackDistance(y - this.mInitialMotionY, i2);
                } else {
                    float x = motionEvent.getX(findPointerIndex);
                    signum = Math.signum(x - this.mInitialMotionX);
                    obtainSpringBackDistance = obtainSpringBackDistance(x - this.mInitialMotionX, i2);
                }
                requestDisallowParentInterceptTouchEvent(true);
                moveTarget(signum * obtainSpringBackDistance, i2);
            }
        } else if (i == 3) {
            return false;
        } else {
            if (i == 5) {
                int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                if (findPointerIndex2 < 0) {
                    Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid active pointer id.");
                    return false;
                }
                if (i2 == 2) {
                    float y2 = motionEvent.getY(findPointerIndex2) - this.mInitialDownY;
                    actionIndex = motionEvent.getActionIndex();
                    if (actionIndex < 0) {
                        Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return false;
                    }
                    float y3 = motionEvent.getY(actionIndex) - y2;
                    this.mInitialDownY = y3;
                    this.mInitialMotionY = y3;
                } else {
                    float x2 = motionEvent.getX(findPointerIndex2) - this.mInitialDownX;
                    actionIndex = motionEvent.getActionIndex();
                    if (actionIndex < 0) {
                        Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                        return false;
                    }
                    float x3 = motionEvent.getX(actionIndex) - x2;
                    this.mInitialDownX = x3;
                    this.mInitialMotionX = x3;
                }
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
            } else if (i == 6) {
                onSecondaryPointerUp(motionEvent);
            }
        }
        return true;
    }

    public final void checkVerticalScrollStart(int i) {
        if (getScrollY() != 0) {
            this.mIsBeingDragged = true;
            float obtainTouchDistance = obtainTouchDistance(Math.abs(getScrollY()), Math.abs(obtainMaxSpringBackDistance(i)), 2);
            if (getScrollY() < 0) {
                this.mInitialDownY -= obtainTouchDistance;
            } else {
                this.mInitialDownY += obtainTouchDistance;
            }
            this.mInitialMotionY = this.mInitialDownY;
            return;
        }
        this.mIsBeingDragged = false;
    }

    public final void checkScrollStart(int i) {
        if (i == 2) {
            checkVerticalScrollStart(i);
        } else {
            checkHorizontalScrollStart(i);
        }
    }

    public final void checkHorizontalScrollStart(int i) {
        if (getScrollX() != 0) {
            this.mIsBeingDragged = true;
            float obtainTouchDistance = obtainTouchDistance(Math.abs(getScrollX()), Math.abs(obtainMaxSpringBackDistance(i)), 2);
            if (getScrollX() < 0) {
                this.mInitialDownX -= obtainTouchDistance;
            } else {
                this.mInitialDownX += obtainTouchDistance;
            }
            this.mInitialMotionX = this.mInitialDownX;
            return;
        }
        this.mIsBeingDragged = false;
    }

    public final boolean onScrollDownEvent(MotionEvent motionEvent, int i, int i2) {
        float signum;
        float obtainSpringBackDistance;
        int actionIndex;
        if (i == 0) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            checkScrollStart(i2);
        } else {
            if (i != 1) {
                if (i == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex < 0) {
                        Log.e("SpringBackLayout", "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    } else if (this.mIsBeingDragged) {
                        if (i2 == 2) {
                            float y = motionEvent.getY(findPointerIndex);
                            signum = Math.signum(y - this.mInitialMotionY);
                            obtainSpringBackDistance = obtainSpringBackDistance(y - this.mInitialMotionY, i2);
                        } else {
                            float x = motionEvent.getX(findPointerIndex);
                            signum = Math.signum(x - this.mInitialMotionX);
                            obtainSpringBackDistance = obtainSpringBackDistance(x - this.mInitialMotionX, i2);
                        }
                        float f = signum * obtainSpringBackDistance;
                        if (f > 0.0f) {
                            requestDisallowParentInterceptTouchEvent(true);
                            moveTarget(f, i2);
                        } else {
                            moveTarget(0.0f, i2);
                            return false;
                        }
                    }
                } else if (i != 3) {
                    if (i == 5) {
                        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex2 < 0) {
                            Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid active pointer id.");
                            return false;
                        }
                        if (i2 == 2) {
                            float y2 = motionEvent.getY(findPointerIndex2) - this.mInitialDownY;
                            actionIndex = motionEvent.getActionIndex();
                            if (actionIndex < 0) {
                                Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                                return false;
                            }
                            float y3 = motionEvent.getY(actionIndex) - y2;
                            this.mInitialDownY = y3;
                            this.mInitialMotionY = y3;
                        } else {
                            float x2 = motionEvent.getX(findPointerIndex2) - this.mInitialDownX;
                            actionIndex = motionEvent.getActionIndex();
                            if (actionIndex < 0) {
                                Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                                return false;
                            }
                            float x3 = motionEvent.getX(actionIndex) - x2;
                            this.mInitialDownX = x3;
                            this.mInitialMotionX = x3;
                        }
                        this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                    } else if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            }
            if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e("SpringBackLayout", "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                springBack(i2);
            }
            this.mActivePointerId = -1;
            return false;
        }
        return true;
    }

    public final void moveTarget(float f, int i) {
        if (i == 2) {
            scrollTo(0, (int) (-f));
        } else {
            scrollTo((int) (-f), 0);
        }
    }

    public final void springBack(int i) {
        springBack(0.0f, i, true);
    }

    public final void springBack(float f, int i, boolean z) {
        OnSpringListener onSpringListener = this.mOnSpringListener;
        if (onSpringListener == null || !onSpringListener.onSpringBack()) {
            this.mSpringScroller.forceStop();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            this.mSpringScroller.scrollByFling(scrollX, 0.0f, scrollY, 0.0f, f, i, false);
            if (scrollX == 0 && scrollY == 0 && f == 0.0f) {
                dispatchScrollState(0);
            } else {
                dispatchScrollState(2);
            }
            if (!z) {
                return;
            }
            postInvalidateOnAnimation();
        }
    }

    public final boolean onScrollUpEvent(MotionEvent motionEvent, int i, int i2) {
        float signum;
        float obtainSpringBackDistance;
        int actionIndex;
        if (i == 0) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            checkScrollStart(i2);
        } else {
            if (i != 1) {
                if (i == 2) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (findPointerIndex < 0) {
                        Log.e("SpringBackLayout", "Got ACTION_MOVE event but have an invalid active pointer id.");
                        return false;
                    } else if (this.mIsBeingDragged) {
                        if (i2 == 2) {
                            float y = motionEvent.getY(findPointerIndex);
                            signum = Math.signum(this.mInitialMotionY - y);
                            obtainSpringBackDistance = obtainSpringBackDistance(this.mInitialMotionY - y, i2);
                        } else {
                            float x = motionEvent.getX(findPointerIndex);
                            signum = Math.signum(this.mInitialMotionX - x);
                            obtainSpringBackDistance = obtainSpringBackDistance(this.mInitialMotionX - x, i2);
                        }
                        float f = signum * obtainSpringBackDistance;
                        if (f > 0.0f) {
                            requestDisallowParentInterceptTouchEvent(true);
                            moveTarget(-f, i2);
                        } else {
                            moveTarget(0.0f, i2);
                            return false;
                        }
                    }
                } else if (i != 3) {
                    if (i == 5) {
                        int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (findPointerIndex2 < 0) {
                            Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid active pointer id.");
                            return false;
                        }
                        if (i2 == 2) {
                            float y2 = motionEvent.getY(findPointerIndex2) - this.mInitialDownY;
                            actionIndex = motionEvent.getActionIndex();
                            if (actionIndex < 0) {
                                Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                                return false;
                            }
                            float y3 = motionEvent.getY(actionIndex) - y2;
                            this.mInitialDownY = y3;
                            this.mInitialMotionY = y3;
                        } else {
                            float x2 = motionEvent.getX(findPointerIndex2) - this.mInitialDownX;
                            actionIndex = motionEvent.getActionIndex();
                            if (actionIndex < 0) {
                                Log.e("SpringBackLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                                return false;
                            }
                            float x3 = motionEvent.getX(actionIndex) - x2;
                            this.mInitialDownX = x3;
                            this.mInitialMotionX = x3;
                        }
                        this.mActivePointerId = motionEvent.getPointerId(actionIndex);
                    } else if (i == 6) {
                        onSecondaryPointerUp(motionEvent);
                    }
                }
            }
            if (motionEvent.findPointerIndex(this.mActivePointerId) < 0) {
                Log.e("SpringBackLayout", "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                springBack(i2);
            }
            this.mActivePointerId = -1;
            return false;
        }
        return true;
    }

    public final void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            this.mActivePointerId = motionEvent.getPointerId(actionIndex == 0 ? 1 : 0);
        }
    }

    public final float obtainSpringBackDistance(float f, int i) {
        return obtainDampingDistance(Math.min(Math.abs(f) / (i == 2 ? this.mScreenHeight : this.mScreenWith), 1.0f), i);
    }

    public final float obtainMaxSpringBackDistance(int i) {
        return obtainDampingDistance(1.0f, i);
    }

    public final float obtainDampingDistance(float f, int i) {
        int i2 = i == 2 ? this.mScreenHeight : this.mScreenWith;
        double min = Math.min(f, 1.0f);
        return ((float) (((Math.pow(min, 3.0d) / 3.0d) - Math.pow(min, 2.0d)) + min)) * i2;
    }

    public final float obtainTouchDistance(float f, float f2, int i) {
        int i2 = i == 2 ? this.mScreenHeight : this.mScreenWith;
        if (Math.abs(f) >= Math.abs(f2)) {
            f = f2;
        }
        double d = i2;
        return (float) (d - (Math.pow(d, 0.6666666666666666d) * Math.pow(i2 - (f * 3.0f), 0.3333333333333333d)));
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        int i6 = 0;
        boolean z = this.mNestedScrollAxes == 2;
        int i7 = z ? i2 : i;
        int i8 = z ? iArr[1] : iArr[0];
        dispatchNestedScroll(i, i2, i3, i4, this.mParentOffsetInWindow, i5, iArr);
        if (!this.mSpringBackEnable) {
            return;
        }
        int i9 = (z ? iArr[1] : iArr[0]) - i8;
        int i10 = z ? i4 - i9 : i3 - i9;
        if (i10 != 0) {
            i6 = i10;
        }
        int i11 = z ? 2 : 1;
        if (i6 < 0 && isTargetScrollToTop(i11) && supportTopSpringBackMode()) {
            if (i5 != 0) {
                float obtainMaxSpringBackDistance = obtainMaxSpringBackDistance(i11);
                if (this.mVelocityY != 0.0f || this.mVelocityX != 0.0f) {
                    this.mScrollByFling = true;
                    if (i7 != 0 && (-i6) <= obtainMaxSpringBackDistance) {
                        this.mSpringScroller.setFirstStep(i6);
                    }
                    dispatchScrollState(2);
                } else if (this.mTotalScrollTopUnconsumed != 0.0f) {
                } else {
                    float f = obtainMaxSpringBackDistance - this.mTotalFlingUnconsumed;
                    if (this.consumeNestFlingCounter >= 4) {
                        return;
                    }
                    if (f <= Math.abs(i6)) {
                        this.mTotalFlingUnconsumed += f;
                        iArr[1] = (int) (iArr[1] + f);
                    } else {
                        this.mTotalFlingUnconsumed += Math.abs(i6);
                        iArr[1] = iArr[1] + i10;
                    }
                    dispatchScrollState(2);
                    moveTarget(obtainSpringBackDistance(this.mTotalFlingUnconsumed, i11), i11);
                    this.consumeNestFlingCounter++;
                }
            } else if (!this.mSpringScroller.isFinished()) {
            } else {
                this.mTotalScrollTopUnconsumed += Math.abs(i6);
                dispatchScrollState(1);
                moveTarget(obtainSpringBackDistance(this.mTotalScrollTopUnconsumed, i11), i11);
                iArr[1] = iArr[1] + i10;
            }
        } else if (i6 <= 0 || !isTargetScrollToBottom(i11) || !supportBottomSpringBackMode()) {
        } else {
            if (i5 != 0) {
                float obtainMaxSpringBackDistance2 = obtainMaxSpringBackDistance(i11);
                if (this.mVelocityY != 0.0f || this.mVelocityX != 0.0f) {
                    this.mScrollByFling = true;
                    if (i7 != 0 && i6 <= obtainMaxSpringBackDistance2) {
                        this.mSpringScroller.setFirstStep(i6);
                    }
                    dispatchScrollState(2);
                } else if (this.mTotalScrollBottomUnconsumed != 0.0f) {
                } else {
                    float f2 = obtainMaxSpringBackDistance2 - this.mTotalFlingUnconsumed;
                    if (this.consumeNestFlingCounter >= 4) {
                        return;
                    }
                    if (f2 <= Math.abs(i6)) {
                        this.mTotalFlingUnconsumed += f2;
                        iArr[1] = (int) (iArr[1] + f2);
                    } else {
                        this.mTotalFlingUnconsumed += Math.abs(i6);
                        iArr[1] = iArr[1] + i10;
                    }
                    dispatchScrollState(2);
                    moveTarget(-obtainSpringBackDistance(this.mTotalFlingUnconsumed, i11), i11);
                    this.consumeNestFlingCounter++;
                }
            } else if (!this.mSpringScroller.isFinished()) {
            } else {
                this.mTotalScrollBottomUnconsumed += Math.abs(i6);
                dispatchScrollState(1);
                moveTarget(-obtainSpringBackDistance(this.mTotalScrollBottomUnconsumed, i11), i11);
                iArr[1] = iArr[1] + i10;
            }
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        onNestedScroll(view, i, i2, i3, i4, i5, this.mNestedScrollingV2ConsumedCompat);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        onNestedScroll(view, i, i2, i3, i4, 0, this.mNestedScrollingV2ConsumedCompat);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        if (this.mSpringBackEnable) {
            this.mNestedScrollAxes = i;
            int i3 = 2;
            boolean z = i == 2;
            if (!z) {
                i3 = 1;
            }
            if ((i3 & this.mOriginScrollOrientation) == 0 || !onStartNestedScroll(view, view, i)) {
                return false;
            }
            float scrollY = z ? getScrollY() : getScrollX();
            if (i2 != 0 && scrollY != 0.0f && (this.mTarget instanceof NestedScrollView)) {
                return false;
            }
        }
        this.mNestedScrollingChildHelper.startNestedScroll(i, i2);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onStartNestedScroll(View view, View view2, int i) {
        return isEnabled();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        if (this.mSpringBackEnable) {
            int i3 = 2;
            boolean z = this.mNestedScrollAxes == 2;
            if (!z) {
                i3 = 1;
            }
            float scrollY = z ? getScrollY() : getScrollX();
            if (i2 != 0) {
                if (scrollY == 0.0f) {
                    this.mTotalFlingUnconsumed = 0.0f;
                } else {
                    this.mTotalFlingUnconsumed = obtainTouchDistance(Math.abs(scrollY), Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                }
                this.mNestedFlingInProgress = true;
                this.consumeNestFlingCounter = 0;
            } else {
                if (scrollY == 0.0f) {
                    this.mTotalScrollTopUnconsumed = 0.0f;
                    this.mTotalScrollBottomUnconsumed = 0.0f;
                } else if (scrollY < 0.0f) {
                    this.mTotalScrollTopUnconsumed = obtainTouchDistance(Math.abs(scrollY), Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                    this.mTotalScrollBottomUnconsumed = 0.0f;
                } else {
                    this.mTotalScrollTopUnconsumed = 0.0f;
                    this.mTotalScrollBottomUnconsumed = obtainTouchDistance(Math.abs(scrollY), Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                }
                this.mNestedScrollInProgress = true;
            }
            this.mVelocityY = 0.0f;
            this.mVelocityX = 0.0f;
            this.mScrollByFling = false;
            this.mSpringScroller.forceStop();
        }
        onNestedScrollAccepted(view, view2, i);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        startNestedScroll(i & 2);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        if (this.mSpringBackEnable) {
            if (this.mNestedScrollAxes == 2) {
                onNestedPreScroll(i2, iArr, i3);
            } else {
                onNestedPreScroll(i, iArr, i3);
            }
        }
        int[] iArr2 = this.mParentScrollConsumed;
        if (dispatchNestedPreScroll(i - iArr[0], i2 - iArr[1], iArr2, null, i3)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    public final void onNestedPreScroll(int i, int[] iArr, int i2) {
        boolean z = this.mNestedScrollAxes == 2;
        int i3 = z ? 2 : 1;
        int abs = Math.abs(z ? getScrollY() : getScrollX());
        float f = 0.0f;
        if (i2 == 0) {
            if (i > 0) {
                float f2 = this.mTotalScrollTopUnconsumed;
                if (f2 > 0.0f) {
                    float f3 = i;
                    if (f3 > f2) {
                        consumeDelta((int) f2, iArr, i3);
                        this.mTotalScrollTopUnconsumed = 0.0f;
                    } else {
                        this.mTotalScrollTopUnconsumed = f2 - f3;
                        consumeDelta(i, iArr, i3);
                    }
                    dispatchScrollState(1);
                    moveTarget(obtainSpringBackDistance(this.mTotalScrollTopUnconsumed, i3), i3);
                    return;
                }
            }
            if (i >= 0) {
                return;
            }
            float f4 = this.mTotalScrollBottomUnconsumed;
            if ((-f4) >= 0.0f) {
                return;
            }
            float f5 = i;
            if (f5 < (-f4)) {
                consumeDelta((int) f4, iArr, i3);
                this.mTotalScrollBottomUnconsumed = 0.0f;
            } else {
                this.mTotalScrollBottomUnconsumed = f4 + f5;
                consumeDelta(i, iArr, i3);
            }
            dispatchScrollState(1);
            moveTarget(-obtainSpringBackDistance(this.mTotalScrollBottomUnconsumed, i3), i3);
            return;
        }
        float f6 = i3 == 2 ? this.mVelocityY : this.mVelocityX;
        if (i > 0) {
            float f7 = this.mTotalScrollTopUnconsumed;
            if (f7 > 0.0f) {
                if (f6 > 2000.0f) {
                    float obtainSpringBackDistance = obtainSpringBackDistance(f7, i3);
                    float f8 = i;
                    if (f8 > obtainSpringBackDistance) {
                        consumeDelta((int) obtainSpringBackDistance, iArr, i3);
                        this.mTotalScrollTopUnconsumed = 0.0f;
                    } else {
                        consumeDelta(i, iArr, i3);
                        f = obtainSpringBackDistance - f8;
                        this.mTotalScrollTopUnconsumed = obtainTouchDistance(f, Math.signum(f) * Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                    }
                    moveTarget(f, i3);
                    dispatchScrollState(1);
                    return;
                }
                if (!this.mScrollByFling) {
                    this.mScrollByFling = true;
                    springBack(f6, i3, false);
                }
                if (this.mSpringScroller.computeScrollOffset()) {
                    scrollTo(this.mSpringScroller.getCurrX(), this.mSpringScroller.getCurrY());
                    this.mTotalScrollTopUnconsumed = obtainTouchDistance(abs, Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                } else {
                    this.mTotalScrollTopUnconsumed = 0.0f;
                }
                consumeDelta(i, iArr, i3);
                return;
            }
        }
        if (i < 0) {
            float f9 = this.mTotalScrollBottomUnconsumed;
            if ((-f9) < 0.0f) {
                if (f6 < -2000.0f) {
                    float obtainSpringBackDistance2 = obtainSpringBackDistance(f9, i3);
                    float f10 = i;
                    if (f10 < (-obtainSpringBackDistance2)) {
                        consumeDelta((int) obtainSpringBackDistance2, iArr, i3);
                        this.mTotalScrollBottomUnconsumed = 0.0f;
                    } else {
                        consumeDelta(i, iArr, i3);
                        f = obtainSpringBackDistance2 + f10;
                        this.mTotalScrollBottomUnconsumed = obtainTouchDistance(f, Math.signum(f) * Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                    }
                    dispatchScrollState(1);
                    moveTarget(-f, i3);
                    return;
                }
                if (!this.mScrollByFling) {
                    this.mScrollByFling = true;
                    springBack(f6, i3, false);
                }
                if (this.mSpringScroller.computeScrollOffset()) {
                    scrollTo(this.mSpringScroller.getCurrX(), this.mSpringScroller.getCurrY());
                    this.mTotalScrollBottomUnconsumed = obtainTouchDistance(abs, Math.abs(obtainMaxSpringBackDistance(i3)), i3);
                } else {
                    this.mTotalScrollBottomUnconsumed = 0.0f;
                }
                consumeDelta(i, iArr, i3);
                return;
            }
        }
        if (i == 0) {
            return;
        }
        if ((this.mTotalScrollBottomUnconsumed != 0.0f && this.mTotalScrollTopUnconsumed != 0.0f) || !this.mScrollByFling || getScrollY() != 0) {
            return;
        }
        consumeDelta(i, iArr, i3);
    }

    public final void consumeDelta(int i, int[] iArr, int i2) {
        if (i2 == 2) {
            iArr[1] = i;
        } else {
            iArr[0] = i;
        }
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(z);
    }

    @Override // android.view.View
    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View view, int i) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view, i);
        stopNestedScroll(i);
        if (!this.mSpringBackEnable) {
            return;
        }
        int i2 = 1;
        boolean z = this.mNestedScrollAxes == 2;
        if (z) {
            i2 = 2;
        }
        if (this.mNestedScrollInProgress) {
            this.mNestedScrollInProgress = false;
            float scrollY = z ? getScrollY() : getScrollX();
            if (!this.mNestedFlingInProgress && scrollY != 0.0f) {
                springBack(i2);
            } else if (scrollY == 0.0f) {
            } else {
                dispatchScrollState(2);
            }
        } else if (!this.mNestedFlingInProgress) {
        } else {
            this.mNestedFlingInProgress = false;
            if (this.mScrollByFling) {
                if (this.mSpringScroller.isFinished()) {
                    springBack(i2 == 2 ? this.mVelocityY : this.mVelocityX, i2, false);
                }
                postInvalidateOnAnimation();
                return;
            }
            springBack(i2);
        }
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedFling(View view, float f, float f2, boolean z) {
        return dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onNestedPreFling(View view, float f, float f2) {
        return dispatchNestedPreFling(f, f2);
    }

    public void dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
        this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i) {
        return this.mNestedScrollingChildHelper.startNestedScroll(i);
    }

    public void stopNestedScroll(int i) {
        this.mNestedScrollingChildHelper.stopNestedScroll(i);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override // android.view.View
    public boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.View
    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2);
    }

    public void smoothScrollTo(int i, int i2) {
        if (i - getScrollX() == 0 && i2 - getScrollY() == 0) {
            return;
        }
        this.mSpringScroller.forceStop();
        this.mSpringScroller.scrollByFling(getScrollX(), i, getScrollY(), i2, 0.0f, 2, true);
        dispatchScrollState(2);
        postInvalidateOnAnimation();
    }

    public final void dispatchScrollState(int i) {
        int i2 = this.mScrollState;
        if (i2 != i) {
            this.mScrollState = i;
            for (OnScrollListener onScrollListener : this.mOnScrollListeners) {
                onScrollListener.onStateChanged(i2, i, this.mSpringScroller.isFinished());
            }
        }
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListeners.add(onScrollListener);
    }

    public void setOnSpringListener(OnSpringListener onSpringListener) {
        this.mOnSpringListener = onSpringListener;
    }

    @Override // miuix.core.view.NestedCurrentFling
    public boolean onNestedCurrentFling(float f, float f2) {
        this.mVelocityX = f;
        this.mVelocityY = f2;
        return true;
    }
}
