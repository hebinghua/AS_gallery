package com.miui.gallery.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.widget.NestedScrollView;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class GalleryPullZoomLayout extends FrameLayout implements NestedScrollingParent3, NestedScrollingChild3 {
    public boolean isFirstLayout;
    public boolean isScrolling;
    public int mActionBarHeight;
    public View mContentView;
    public int mContentViewId;
    public View mHeaderView;
    public int mHeaderViewId;
    public final NestedScrollingChildHelper mNestedScrollingChildHelper;
    public final NestedScrollingParentHelper mNestedScrollingParentHelper;
    public final int[] mNestedScrollingV2ConsumedCompat;
    public int mOffset;
    public OnScrollListener mOnScrollListener;
    public int mOriginalHeight;
    public final int[] mParentOffsetInWindow;
    public final int[] mParentScrollConsumed;
    public ValueAnimator mRecoveryAnimator;
    public int mScrollingFrom;
    public int mScrollingProgress;
    public int mScrollingTo;
    public int mTouchSlop;
    public ViewGroup.LayoutParams mZoomParams;
    public View mZoomableView;
    public int mZoomableViewId;

    /* loaded from: classes2.dex */
    public interface OnScrollListener {
        void onScrolled(ScrollBy scrollBy, float f);
    }

    /* loaded from: classes2.dex */
    public enum ScrollBy {
        Layout,
        NestedScroll,
        NestedPreScroll
    }

    public GalleryPullZoomLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GalleryPullZoomLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mNestedScrollingV2ConsumedCompat = new int[2];
        this.mParentScrollConsumed = new int[2];
        this.mParentOffsetInWindow = new int[2];
        this.isFirstLayout = true;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = miuix.core.view.NestedScrollingChildHelper.obtain(this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.GalleryPullZoomLayout);
        this.mHeaderViewId = obtainStyledAttributes.getResourceId(1, 16908310);
        this.mContentViewId = obtainStyledAttributes.getResourceId(0, 16908298);
        this.mZoomableViewId = obtainStyledAttributes.getResourceId(2, this.mHeaderViewId);
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        setNestedScrollingEnabled(true);
        View findViewById = findViewById(this.mHeaderViewId);
        removeView(findViewById);
        NestedScrollView nestedScrollView = new NestedScrollView(getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -2);
        nestedScrollView.addView(findViewById);
        nestedScrollView.setLayoutParams(layoutParams);
        addView(nestedScrollView);
        this.mHeaderView = nestedScrollView;
        View findViewById2 = findViewById(this.mContentViewId);
        this.mContentView = findViewById2;
        findViewById2.setNestedScrollingEnabled(true);
        this.mZoomableView = findViewById(this.mZoomableViewId);
    }

    public void setOriginalHeight(int i) {
        this.mOriginalHeight = i;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.isFirstLayout) {
            this.mOriginalHeight = this.mHeaderView.getMeasuredHeight();
            this.isFirstLayout = false;
        }
        this.mScrollingTo = 0;
        int i5 = (-this.mHeaderView.getMeasuredHeight()) + this.mActionBarHeight;
        this.mScrollingFrom = i5;
        int i6 = this.mScrollingProgress;
        if (i6 < i5) {
            this.mScrollingProgress = i5;
        } else {
            int i7 = this.mScrollingTo;
            if (i6 > i7) {
                this.mScrollingProgress = i7;
            }
        }
        dispatchScrollingProgressUpdated(ScrollBy.Layout);
    }

    public void setActionBarHeight(int i) {
        this.mActionBarHeight = i;
        int i2 = (-this.mHeaderView.getMeasuredHeight()) + i;
        this.mScrollingFrom = i2;
        int i3 = this.mScrollingProgress;
        if (i3 >= i2) {
            i2 = i3;
        }
        this.mScrollingProgress = i2;
        View view = this.mContentView;
        view.setPadding(view.getPaddingLeft(), this.mContentView.getPaddingTop(), this.mContentView.getPaddingRight(), this.mContentView.getPaddingBottom() + i);
    }

    @Override // android.view.View
    public boolean isNestedScrollingEnabled() {
        return this.mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override // android.view.View
    public void setNestedScrollingEnabled(boolean z) {
        this.mNestedScrollingChildHelper.setNestedScrollingEnabled(z);
    }

    @Override // android.view.View
    public boolean startNestedScroll(int i) {
        return this.mNestedScrollingChildHelper.startNestedScroll(i);
    }

    public boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2, int i3) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2, i3);
    }

    public void dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr, int i5, int[] iArr2) {
        this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr, i5, iArr2);
    }

    public void stopNestedScroll(int i) {
        this.mNestedScrollingChildHelper.stopNestedScroll(i);
        if (i == 0 && this.isScrolling) {
            recoveryZoomView();
        }
        this.isScrolling = false;
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        return this.mNestedScrollingChildHelper.startNestedScroll(i, i2) || onStartNestedScroll(view, view, i);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onStartNestedScroll(View view, View view2, int i) {
        boolean z = (i & 2) != 0;
        if (!this.mNestedScrollingChildHelper.startNestedScroll(i)) {
            return isEnabled() && z && (!this.isScrolling || view2 != this.mHeaderView);
        }
        return true;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        onNestedScrollAccepted(view, view2, i);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScrollAccepted(View view, View view2, int i) {
        this.mNestedScrollingParentHelper.onNestedScrollAccepted(view, view2, i);
        startNestedScroll(i & 2);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        int[] iArr2 = this.mParentScrollConsumed;
        int i4 = this.mOffset + i2;
        this.mOffset = i4;
        if (i4 < 0) {
            this.mOffset = 0;
        }
        if (i2 > 0) {
            int max = Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, this.mScrollingProgress - i2));
            int i5 = this.mScrollingProgress - max;
            this.mScrollingProgress = max;
            dispatchScrollingProgressUpdated(ScrollBy.NestedPreScroll);
            iArr[0] = iArr[0] + 0;
            iArr[1] = iArr[1] + i5;
        }
        if (dispatchNestedPreScroll(i - iArr[0], i2 - iArr[1], iArr2, null, i3)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        onNestedPreScroll(view, i, i2, iArr, 0);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        onNestedScroll(view, i, i2, i3, i4, 0);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        onNestedScroll(view, i, i2, i3, i4, 0, this.mNestedScrollingV2ConsumedCompat);
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        int i6;
        if (i4 < 0) {
            int max = Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, this.mScrollingProgress - i4));
            i6 = this.mScrollingProgress - max;
            this.mScrollingProgress = max;
            dispatchScrollingProgressUpdated(ScrollBy.NestedScroll);
            iArr[1] = iArr[1] + i6;
        } else {
            i6 = 0;
        }
        int i7 = i4 - i6;
        if (((i4 < 0 && this.mOffset == 0) || i7 < (-this.mTouchSlop)) && i5 == 0) {
            this.isScrolling = true;
            int measuredHeight = this.mZoomableView.getMeasuredHeight();
            setHeaderTargetHeight(measuredHeight - ((int) ((i4 + i6) * ((1500.0f - measuredHeight) / 1500.0f))));
            i6 += i7;
            iArr[1] = iArr[1] + i7;
        }
        if (i5 == 1) {
            this.isScrolling = false;
        }
        dispatchNestedScroll(i, i2 + i6, i3, i4 - i6, this.mParentOffsetInWindow, i5, iArr);
    }

    public void setHeaderTargetHeight(int i) {
        ViewGroup.LayoutParams layoutParams = this.mZoomableView.getLayoutParams();
        this.mZoomParams = layoutParams;
        layoutParams.height = i;
        this.mZoomableView.setLayoutParams(layoutParams);
    }

    public final void recoveryZoomView() {
        ValueAnimator ofInt = ValueAnimator.ofInt(this.mZoomableView.getMeasuredHeight(), this.mOriginalHeight);
        this.mRecoveryAnimator = ofInt;
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.GalleryPullZoomLayout.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                GalleryPullZoomLayout galleryPullZoomLayout = GalleryPullZoomLayout.this;
                galleryPullZoomLayout.mZoomParams = galleryPullZoomLayout.mZoomableView.getLayoutParams();
                GalleryPullZoomLayout.this.mZoomParams.height = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                GalleryPullZoomLayout.this.mZoomableView.setLayoutParams(GalleryPullZoomLayout.this.mZoomParams);
            }
        });
        this.mRecoveryAnimator.setDuration(300L);
        this.mRecoveryAnimator.start();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View view, int i) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view, i);
        stopNestedScroll(i);
    }

    public final void dispatchScrollingProgressUpdated(ScrollBy scrollBy) {
        View view = this.mHeaderView;
        view.offsetTopAndBottom(this.mScrollingProgress - view.getTop());
        this.mContentView.offsetTopAndBottom((this.mScrollingProgress + this.mHeaderView.getMeasuredHeight()) - this.mContentView.getTop());
        OnScrollListener onScrollListener = this.mOnScrollListener;
        if (onScrollListener == null || scrollBy == ScrollBy.Layout) {
            return;
        }
        onScrollListener.onScrolled(scrollBy, Math.abs(this.mScrollingProgress / (this.mScrollingTo - this.mScrollingFrom)));
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }
}
