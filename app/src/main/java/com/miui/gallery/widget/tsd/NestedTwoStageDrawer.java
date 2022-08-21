package com.miui.gallery.widget.tsd;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import miuix.springback.view.SpringBackLayout;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes3.dex */
public class NestedTwoStageDrawer extends FrameLayout implements NestedScrollingParent3, NestedScrollingChild3, INestedTwoStageDrawer {
    public boolean mCancelAnim;
    public View mContentView;
    public int mContentViewHeight;
    public int mContentViewId;
    public Runnable mCurrentHomingRunnable;
    public DrawerState mCurrentState;
    public boolean mDragEnable;
    public Runnable mHeaderTryHomingRunnable;
    public View mHeaderView;
    public int mHeaderViewHeight;
    public int mHeaderViewId;
    public boolean mIsAnimating;
    public boolean mMarginEnable;
    public final NestedScrollingChildHelper mNestedScrollingChildHelper;
    public final NestedScrollingParentHelper mNestedScrollingParentHelper;
    public final int[] mNestedScrollingV2ConsumedCompat;
    public final int[] mParentOffsetInWindow;
    public final int[] mParentScrollConsumed;
    public List<InestedScrollerStateListener> mScrollerStateListeners;
    public int mScrollingFrom;
    public int mScrollingProgress;
    public int mScrollingTo;
    public SpringBackLayout mSpringBackLayout;
    public boolean mStickEnable;
    public View mSubHeaderView;
    public int mSubHeaderViewHeight;
    public int mSubHeaderViewId;

    public static INestedTwoStageDrawer create(Context context) {
        return new NestedTwoStageDrawer(context);
    }

    public NestedTwoStageDrawer(Context context) {
        this(context, null);
    }

    public NestedTwoStageDrawer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NestedTwoStageDrawer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mNestedScrollingV2ConsumedCompat = new int[2];
        this.mParentOffsetInWindow = new int[2];
        this.mParentScrollConsumed = new int[2];
        this.mDragEnable = true;
        this.mStickEnable = true;
        this.mMarginEnable = true;
        this.mCurrentState = DrawerState.OPEN;
        this.mHeaderTryHomingRunnable = new Runnable() { // from class: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.1
            @Override // java.lang.Runnable
            public void run() {
                NestedTwoStageDrawer nestedTwoStageDrawer = NestedTwoStageDrawer.this;
                if (nestedTwoStageDrawer.getViewTop(nestedTwoStageDrawer.mHeaderView) < NestedTwoStageDrawer.this.getTop()) {
                    NestedTwoStageDrawer nestedTwoStageDrawer2 = NestedTwoStageDrawer.this;
                    if (nestedTwoStageDrawer2.getViewBottom(nestedTwoStageDrawer2.mHeaderView) > NestedTwoStageDrawer.this.getTop()) {
                        if (NestedTwoStageDrawer.this.mCurrentHomingRunnable != null) {
                            NestedTwoStageDrawer nestedTwoStageDrawer3 = NestedTwoStageDrawer.this;
                            nestedTwoStageDrawer3.removeCallbacks(nestedTwoStageDrawer3.mCurrentHomingRunnable);
                            NestedTwoStageDrawer.this.mIsAnimating = false;
                        }
                        NestedTwoStageDrawer nestedTwoStageDrawer4 = NestedTwoStageDrawer.this;
                        nestedTwoStageDrawer4.mCurrentHomingRunnable = new HeaderAnimRunnable(false, new DrawerAnimEndListener() { // from class: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.1.1
                            @Override // com.miui.gallery.widget.tsd.DrawerAnimEndListener
                            public void onDrawerAnimEnd() {
                            }
                        });
                        NestedTwoStageDrawer nestedTwoStageDrawer5 = NestedTwoStageDrawer.this;
                        nestedTwoStageDrawer5.postOnAnimation(nestedTwoStageDrawer5.mCurrentHomingRunnable);
                        return;
                    }
                }
                NestedTwoStageDrawer nestedTwoStageDrawer6 = NestedTwoStageDrawer.this;
                int viewTop = nestedTwoStageDrawer6.getViewTop(nestedTwoStageDrawer6.mSubHeaderView);
                NestedTwoStageDrawer nestedTwoStageDrawer7 = NestedTwoStageDrawer.this;
                if (viewTop < nestedTwoStageDrawer7.getViewBottom(nestedTwoStageDrawer7.mHeaderView)) {
                    NestedTwoStageDrawer nestedTwoStageDrawer8 = NestedTwoStageDrawer.this;
                    if (nestedTwoStageDrawer8.getViewBottom(nestedTwoStageDrawer8.mSubHeaderView) <= NestedTwoStageDrawer.this.getTop()) {
                        return;
                    }
                    if (NestedTwoStageDrawer.this.mCurrentHomingRunnable != null) {
                        NestedTwoStageDrawer nestedTwoStageDrawer9 = NestedTwoStageDrawer.this;
                        nestedTwoStageDrawer9.removeCallbacks(nestedTwoStageDrawer9.mCurrentHomingRunnable);
                        NestedTwoStageDrawer.this.mIsAnimating = false;
                    }
                    NestedTwoStageDrawer nestedTwoStageDrawer10 = NestedTwoStageDrawer.this;
                    nestedTwoStageDrawer10.mCurrentHomingRunnable = new SubHeaderAnimRunnable(false, new DrawerAnimEndListener() { // from class: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.1.2
                        @Override // com.miui.gallery.widget.tsd.DrawerAnimEndListener
                        public void onDrawerAnimEnd() {
                        }
                    });
                    NestedTwoStageDrawer nestedTwoStageDrawer11 = NestedTwoStageDrawer.this;
                    nestedTwoStageDrawer11.postOnAnimation(nestedTwoStageDrawer11.mCurrentHomingRunnable);
                }
            }
        };
        this.mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        this.mNestedScrollingChildHelper = miuix.core.view.NestedScrollingChildHelper.obtain(this);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.NestedTwoStageDrawer);
        this.mHeaderViewId = obtainStyledAttributes.getResourceId(1, 16908310);
        this.mSubHeaderViewId = obtainStyledAttributes.getResourceId(2, 16908290);
        this.mContentViewId = obtainStyledAttributes.getResourceId(0, 16908298);
        obtainStyledAttributes.recycle();
        setNestedScrollingEnabled(true);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        setHeaderView(findViewById(this.mHeaderViewId));
        setSubHeaderView(findViewById(this.mSubHeaderViewId));
        setContentView(findViewById(this.mContentViewId));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int viewHeight = getViewHeight(this.mHeaderView);
        int i5 = this.mHeaderViewHeight;
        int i6 = viewHeight - i5;
        if (i6 != 0) {
            this.mHeaderViewHeight = i5 + i6;
        }
        int viewHeight2 = getViewHeight(this.mSubHeaderView);
        int i7 = this.mSubHeaderViewHeight;
        int i8 = viewHeight2 - i7;
        if (i8 != 0) {
            this.mSubHeaderViewHeight = i7 + i8;
        }
        int viewHeight3 = getViewHeight(this.mContentView);
        int i9 = this.mContentViewHeight;
        int i10 = viewHeight3 - i9;
        if (i10 != 0) {
            this.mContentViewHeight = i9 + i10;
        }
        if (i6 != 0 || i8 != 0 || (i10 != 0 && !isAnimating() && this.mCurrentState != null)) {
            setDrawerState(this.mCurrentState, false, null);
        }
        setScrollingRange((-this.mHeaderViewHeight) - this.mSubHeaderViewHeight, 0);
        View view = this.mHeaderView;
        if (view != null) {
            view.setZ(0.01f);
        }
    }

    public final void setScrollingRange(int i, int i2) {
        if (i > i2) {
            i = i2;
        }
        this.mScrollingFrom = i;
        this.mScrollingTo = i2;
        if (this.mScrollingProgress < i) {
            this.mScrollingProgress = i;
        }
        if (this.mScrollingProgress > i2) {
            this.mScrollingProgress = i2;
        }
        dispatchScrollingProgressUpdated(false);
    }

    public final void dispatchScrollingProgressUpdated(boolean z) {
        onScrollingProgressUpdated(this.mScrollingProgress, z);
    }

    public final void onScrollingProgressUpdated(int i, boolean z) {
        int i2;
        List<InestedScrollerStateListener> list;
        DrawerState drawerState = this.mCurrentState;
        if (this.mSubHeaderView != null && i == (-this.mSubHeaderViewHeight)) {
            this.mCurrentState = DrawerState.HALF_OPEN;
        } else if (i == (-this.mHeaderViewHeight) - this.mSubHeaderViewHeight) {
            this.mCurrentState = DrawerState.CLOSE;
        } else if (i == 0) {
            this.mCurrentState = DrawerState.OPEN;
        } else {
            this.mCurrentState = DrawerState.INVALID;
        }
        if (drawerState != this.mCurrentState && (list = this.mScrollerStateListeners) != null) {
            for (int size = list.size() - 1; size >= 0; size--) {
                this.mScrollerStateListeners.get(size).onScrollerStateChanged(this.mCurrentState, this.mHeaderViewHeight + i + this.mSubHeaderViewHeight);
            }
        }
        List<InestedScrollerStateListener> list2 = this.mScrollerStateListeners;
        if (list2 != null) {
            for (int size2 = list2.size() - 1; size2 >= 0; size2--) {
                this.mScrollerStateListeners.get(size2).onScrollerUpdate(this.mCurrentState, i, this.mHeaderViewHeight + this.mSubHeaderViewHeight);
            }
        }
        if (this.mSubHeaderView != null) {
            i2 = Math.min(0, this.mSubHeaderViewHeight + i);
            int max = this.mHeaderViewHeight + Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, i));
            View view = this.mSubHeaderView;
            view.offsetTopAndBottom(max - getViewTop(view));
        } else {
            i2 = i;
        }
        View view2 = this.mHeaderView;
        if (view2 != null) {
            view2.offsetTopAndBottom(i2 - getViewTop(view2));
            View view3 = this.mContentView;
            if (view3 != null) {
                view3.offsetTopAndBottom(((i + this.mHeaderViewHeight) + this.mSubHeaderViewHeight) - view3.getTop());
            }
        }
        if (z) {
            removeCallbacks(this.mHeaderTryHomingRunnable);
            removeCallbacks(this.mCurrentHomingRunnable);
            this.mIsAnimating = false;
        }
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
    }

    @Override // android.view.View, androidx.core.view.NestedScrollingChild
    public void stopNestedScroll() {
        this.mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        if (view == this.mContentView) {
            return this.mNestedScrollingChildHelper.startNestedScroll(i, i2) || onStartNestedScroll(view, view, i);
        }
        DefaultLogger.w("NestedTwoStageDrawer", "a view which is not mContentView has asked for nested scrolling, this will be ignored, or may cause unexpected result.");
        return false;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent, androidx.core.view.NestedScrollingParent
    public boolean onStartNestedScroll(View view, View view2, int i) {
        if (view == this.mContentView) {
            return this.mNestedScrollingChildHelper.startNestedScroll(i) || (isEnabled() && ((i & 2) != 0));
        }
        DefaultLogger.w("NestedTwoStageDrawer", "a view which is not mContentView has asked for nested scrolling, this will be ignored, or may cause unexpected result.");
        return false;
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
        if (this.mDragEnable && i2 > 0) {
            int max = Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, this.mScrollingProgress - i2));
            int i4 = this.mScrollingProgress - max;
            this.mScrollingProgress = max;
            dispatchScrollingProgressUpdated(true);
            iArr[0] = iArr[0] + 0;
            iArr[1] = iArr[1] + i4;
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
        if (!this.mDragEnable || i4 >= 0) {
            i6 = 0;
        } else if (this.mStickEnable && i5 == 1 && (this.mCurrentState == DrawerState.CLOSE || (getViewHeight(this.mHeaderView) == 0 && this.mCurrentState == DrawerState.HALF_OPEN))) {
            i6 = i4;
        } else {
            int max = Math.max(this.mScrollingFrom, Math.min(this.mScrollingTo, this.mScrollingProgress - i4));
            i6 = this.mScrollingProgress - max;
            this.mScrollingProgress = max;
            dispatchScrollingProgressUpdated(true);
            iArr[1] = iArr[1] + i6;
        }
        dispatchNestedScroll(i, i2, i3, i4 - i6, this.mParentOffsetInWindow, i5, iArr);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View view, int i) {
        this.mNestedScrollingParentHelper.onStopNestedScroll(view, i);
        stopNestedScroll(i);
        postDelayed(this.mHeaderTryHomingRunnable, 100L);
    }

    public final int getViewHeight(View view) {
        int i = 0;
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            i = marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
        }
        return view.getMeasuredHeight() + i;
    }

    public final int getViewTop(View view) {
        int i = 0;
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            i = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
        }
        return view.getTop() - i;
    }

    public final int getViewBottom(View view) {
        int i = 0;
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            i = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return view.getBottom() + i;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public boolean isDrawerOpen() {
        return this.mCurrentState == DrawerState.OPEN;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void cancelDrawerAnim() {
        this.mCancelAnim = true;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setDrawerState(final DrawerState drawerState, boolean z, final DrawerAnimEndListener drawerAnimEndListener) {
        boolean isAnimating = isAnimating();
        if (!z) {
            int i = this.mScrollingProgress;
            if (i > this.mScrollingTo || i < this.mScrollingFrom) {
                return;
            }
            int i2 = AnonymousClass3.$SwitchMap$com$miui$gallery$widget$tsd$DrawerState[drawerState.ordinal()];
            if (i2 == 1) {
                this.mScrollingProgress = this.mScrollingTo;
                dispatchScrollingProgressUpdated(false);
            } else if (i2 == 2) {
                this.mScrollingProgress = -this.mSubHeaderViewHeight;
                dispatchScrollingProgressUpdated(false);
            } else if (i2 == 3) {
                this.mScrollingProgress = (-this.mHeaderViewHeight) - this.mSubHeaderViewHeight;
                dispatchScrollingProgressUpdated(false);
            }
            this.mCurrentState = drawerState;
        } else if (isAnimating) {
        } else {
            DrawerAnimEndListener drawerAnimEndListener2 = new DrawerAnimEndListener() { // from class: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.2
                @Override // com.miui.gallery.widget.tsd.DrawerAnimEndListener
                public void onDrawerAnimEnd() {
                    DrawerAnimEndListener drawerAnimEndListener3 = drawerAnimEndListener;
                    if (drawerAnimEndListener3 != null) {
                        drawerAnimEndListener3.onDrawerAnimEnd();
                    }
                    NestedTwoStageDrawer.this.mCurrentState = drawerState;
                }
            };
            int i3 = AnonymousClass3.$SwitchMap$com$miui$gallery$widget$tsd$DrawerState[drawerState.ordinal()];
            if (i3 == 1) {
                DrawerState drawerState2 = this.mCurrentState;
                if (drawerState2 == DrawerState.HALF_OPEN) {
                    postOnAnimation(new SubHeaderAnimRunnable(true, drawerAnimEndListener2));
                } else if (drawerState2 != DrawerState.CLOSE) {
                } else {
                    postOnAnimation(new BothHeadersAnimRunnable(true, drawerAnimEndListener2));
                }
            } else if (i3 == 2) {
                DrawerState drawerState3 = this.mCurrentState;
                if (drawerState3 == DrawerState.CLOSE) {
                    postOnAnimation(new HeaderAnimRunnable(true, drawerAnimEndListener2));
                } else if (drawerState3 != DrawerState.OPEN) {
                } else {
                    postOnAnimation(new SubHeaderAnimRunnable(false, drawerAnimEndListener2));
                }
            } else if (i3 != 3) {
            } else {
                DrawerState drawerState4 = this.mCurrentState;
                if (drawerState4 == DrawerState.HALF_OPEN) {
                    postOnAnimation(new HeaderAnimRunnable(false, drawerAnimEndListener2));
                } else if (drawerState4 != DrawerState.OPEN) {
                } else {
                    postOnAnimation(new BothHeadersAnimRunnable(false, drawerAnimEndListener2));
                }
            }
        }
    }

    /* renamed from: com.miui.gallery.widget.tsd.NestedTwoStageDrawer$3  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$widget$tsd$DrawerState;

        static {
            int[] iArr = new int[DrawerState.values().length];
            $SwitchMap$com$miui$gallery$widget$tsd$DrawerState = iArr;
            try {
                iArr[DrawerState.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$widget$tsd$DrawerState[DrawerState.HALF_OPEN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$widget$tsd$DrawerState[DrawerState.CLOSE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setDragEnabled(boolean z) {
        this.mDragEnable = z;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setHeaderView(View view) {
        this.mHeaderView = view;
        if (view == null) {
            return;
        }
        if (view.getParent() != null) {
            ((ViewGroup) this.mHeaderView.getParent()).removeView(this.mHeaderView);
        }
        addView(this.mHeaderView);
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void removeHeaderView(View view) {
        if (view == null) {
            return;
        }
        removeView(view);
        this.mHeaderView = null;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void removeSubHeaderView(View view) {
        if (view == null) {
            return;
        }
        removeView(view);
        this.mSubHeaderView = null;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setSubHeaderView(View view) {
        removeView(view);
        this.mSubHeaderView = view;
        if (view != null) {
            if (view.getParent() == null) {
                addView(this.mSubHeaderView);
            } else if (this.mSubHeaderView.getParent() != this) {
                throw new IllegalStateException("mSubHeaderView has a parent.");
            }
        }
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setContentView(View view) {
        removeView(this.mContentView);
        View wrapContentView = wrapContentView(view);
        this.mContentView = wrapContentView;
        if (wrapContentView != null) {
            wrapContentView.setNestedScrollingEnabled(true);
            if (this.mContentView.getParent() == null) {
                if (this.mMarginEnable) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) wrapContentView.getLayoutParams();
                    if (marginLayoutParams == null) {
                        marginLayoutParams = new ViewGroup.MarginLayoutParams(-1, -1);
                    }
                    int dimensionPixelSize = BaseBuildUtil.isLargeScreenDevice() ? 0 : getResources().getDimensionPixelSize(R.dimen.home_page_margin_horizontal);
                    marginLayoutParams.setMarginStart(dimensionPixelSize);
                    marginLayoutParams.setMarginEnd(dimensionPixelSize);
                    addView(this.mContentView, marginLayoutParams);
                    return;
                }
                addView(this.mContentView);
            } else if (this.mContentView.getParent() != this) {
                throw new IllegalStateException("mContentView has a parent.");
            }
        }
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setMarginEnabled(boolean z) {
        this.mMarginEnable = z;
    }

    public final View wrapContentView(View view) {
        if (!(view instanceof GalleryRecyclerView)) {
            return view;
        }
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
        SpringBackLayout springBackLayout = new SpringBackLayout(getContext());
        this.mSpringBackLayout = springBackLayout;
        springBackLayout.addView(view);
        this.mSpringBackLayout.setTarget(view);
        return this.mSpringBackLayout;
    }

    public SpringBackLayout getSpringBackLayout() {
        return this.mSpringBackLayout;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public boolean isAnimating() {
        return this.mIsAnimating;
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void addScrollerStateListener(InestedScrollerStateListener inestedScrollerStateListener) {
        if (this.mScrollerStateListeners == null) {
            this.mScrollerStateListeners = new ArrayList();
        }
        this.mScrollerStateListeners.add(inestedScrollerStateListener);
    }

    @Override // com.miui.gallery.widget.tsd.INestedTwoStageDrawer
    public void setStickEnable(boolean z) {
        this.mStickEnable = z;
    }

    public void setHeaderViewBackground(Drawable drawable) {
        View view = this.mHeaderView;
        if (view != null) {
            view.setBackground(drawable);
        }
        View view2 = this.mSubHeaderView;
        if (view2 != null) {
            view2.setBackground(drawable);
        }
    }

    /* loaded from: classes3.dex */
    public abstract class BaseAnimRunnable implements Runnable {
        public final int mDuration;
        public final int mEndScrollingProgress;
        public DrawerAnimEndListener mListener;
        public WeakReference<View> mRef;
        public final int mStartScrollingProgress;
        public final long mStartTime = System.currentTimeMillis();
        public final Interpolator mInterpolator = new CubicEaseOutInterpolator();

        public BaseAnimRunnable(View view, int i, int i2, DrawerAnimEndListener drawerAnimEndListener) {
            this.mRef = new WeakReference<>(view);
            this.mStartScrollingProgress = NestedTwoStageDrawer.this.mScrollingProgress;
            this.mEndScrollingProgress = NestedTwoStageDrawer.this.mScrollingProgress + i;
            this.mDuration = i2;
            this.mListener = drawerAnimEndListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            View view = this.mRef.get();
            if (view == null || NestedTwoStageDrawer.this.mCancelAnim) {
                NestedTwoStageDrawer.this.mIsAnimating = false;
                NestedTwoStageDrawer.this.mCancelAnim = false;
                return;
            }
            NestedTwoStageDrawer.this.mIsAnimating = true;
            float interpolate = interpolate();
            NestedTwoStageDrawer nestedTwoStageDrawer = NestedTwoStageDrawer.this;
            int i = this.mEndScrollingProgress;
            int i2 = this.mStartScrollingProgress;
            nestedTwoStageDrawer.mScrollingProgress = (int) (((i - i2) * interpolate) + i2);
            NestedTwoStageDrawer.this.dispatchScrollingProgressUpdated(false);
            if (interpolate >= 1.0f) {
                NestedTwoStageDrawer.this.mIsAnimating = false;
                DrawerAnimEndListener drawerAnimEndListener = this.mListener;
                if (drawerAnimEndListener == null) {
                    return;
                }
                drawerAnimEndListener.onDrawerAnimEnd();
                return;
            }
            view.postOnAnimation(this);
        }

        public final float interpolate() {
            return this.mInterpolator.getInterpolation(Math.min(1.0f, (((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / this.mDuration));
        }
    }

    /* loaded from: classes3.dex */
    public class HeaderAnimRunnable extends BaseAnimRunnable {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public HeaderAnimRunnable(boolean r8, com.miui.gallery.widget.tsd.DrawerAnimEndListener r9) {
            /*
                r6 = this;
                com.miui.gallery.widget.tsd.NestedTwoStageDrawer.this = r7
                android.view.View r2 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$000(r7)
                int r0 = r7.getTop()
                android.view.View r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$000(r7)
                if (r8 == 0) goto L15
                int r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$100(r7, r1)
                goto L19
            L15:
                int r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$200(r7, r1)
            L19:
                int r3 = r0 - r1
                if (r8 == 0) goto L20
                r8 = 800(0x320, float:1.121E-42)
                goto L22
            L20:
                r8 = 500(0x1f4, float:7.0E-43)
            L22:
                r4 = r8
                r0 = r6
                r1 = r7
                r5 = r9
                r0.<init>(r2, r3, r4, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.HeaderAnimRunnable.<init>(com.miui.gallery.widget.tsd.NestedTwoStageDrawer, boolean, com.miui.gallery.widget.tsd.DrawerAnimEndListener):void");
        }
    }

    /* loaded from: classes3.dex */
    public class SubHeaderAnimRunnable extends BaseAnimRunnable {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public SubHeaderAnimRunnable(boolean r8, com.miui.gallery.widget.tsd.DrawerAnimEndListener r9) {
            /*
                r6 = this;
                com.miui.gallery.widget.tsd.NestedTwoStageDrawer.this = r7
                android.view.View r2 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$500(r7)
                android.view.View r0 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$000(r7)
                int r0 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$200(r7, r0)
                android.view.View r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$500(r7)
                if (r8 == 0) goto L19
                int r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$100(r7, r1)
                goto L1d
            L19:
                int r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$200(r7, r1)
            L1d:
                int r3 = r0 - r1
                if (r8 == 0) goto L24
                r8 = 800(0x320, float:1.121E-42)
                goto L26
            L24:
                r8 = 500(0x1f4, float:7.0E-43)
            L26:
                r4 = r8
                r0 = r6
                r1 = r7
                r5 = r9
                r0.<init>(r2, r3, r4, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.SubHeaderAnimRunnable.<init>(com.miui.gallery.widget.tsd.NestedTwoStageDrawer, boolean, com.miui.gallery.widget.tsd.DrawerAnimEndListener):void");
        }
    }

    /* loaded from: classes3.dex */
    public class BothHeadersAnimRunnable extends BaseAnimRunnable {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public BothHeadersAnimRunnable(boolean r8, com.miui.gallery.widget.tsd.DrawerAnimEndListener r9) {
            /*
                r6 = this;
                com.miui.gallery.widget.tsd.NestedTwoStageDrawer.this = r7
                android.view.View r2 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$000(r7)
                int r0 = r7.getTop()
                android.view.View r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$000(r7)
                if (r8 == 0) goto L1d
                int r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$100(r7, r1)
                android.view.View r3 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$500(r7)
                int r3 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$100(r7, r3)
                goto L29
            L1d:
                int r1 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$200(r7, r1)
                android.view.View r3 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$500(r7)
                int r3 = com.miui.gallery.widget.tsd.NestedTwoStageDrawer.access$200(r7, r3)
            L29:
                int r1 = r1 + r3
                int r3 = r0 - r1
                if (r8 == 0) goto L31
                r8 = 800(0x320, float:1.121E-42)
                goto L33
            L31:
                r8 = 500(0x1f4, float:7.0E-43)
            L33:
                r4 = r8
                r0 = r6
                r1 = r7
                r5 = r9
                r0.<init>(r2, r3, r4, r5)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.tsd.NestedTwoStageDrawer.BothHeadersAnimRunnable.<init>(com.miui.gallery.widget.tsd.NestedTwoStageDrawer, boolean, com.miui.gallery.widget.tsd.DrawerAnimEndListener):void");
        }
    }
}
