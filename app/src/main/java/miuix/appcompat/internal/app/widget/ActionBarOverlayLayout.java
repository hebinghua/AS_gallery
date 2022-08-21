package miuix.appcompat.internal.app.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.NestedScrollingParent3;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$drawable;
import miuix.appcompat.R$id;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.OnStatusBarChangeListener;
import miuix.appcompat.app.floatingactivity.FloatingABOLayoutSpec;
import miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper;
import miuix.appcompat.internal.util.LayoutUIUtils;
import miuix.appcompat.internal.view.SearchActionModeImpl;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuDialogHelper;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.appcompat.internal.view.menu.context.ContextMenuBuilder;
import miuix.appcompat.internal.view.menu.context.ContextMenuPopupWindowHelper;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;
import miuix.view.SearchActionMode;

/* loaded from: classes3.dex */
public class ActionBarOverlayLayout extends FrameLayout implements NestedScrollingParent3 {
    public ActionBar mActionBar;
    public ActionBarContainer mActionBarBottom;
    public ActionBarContextView mActionBarContextView;
    public ActionBarContainer mActionBarTop;
    public ActionBarView mActionBarView;
    public ActionMode mActionMode;
    public boolean mAnimating;
    public Rect mBaseContentInsets;
    public Rect mBaseInnerInsets;
    public Window.Callback mCallback;
    public boolean mContentAutoFitSystemWindow;
    public Drawable mContentHeaderBackground;
    public Rect mContentInsets;
    public View mContentMask;
    public Rect mContentMaskInsets;
    public View mContentView;
    public ContextMenuBuilder mContextMenu;
    public ContextMenuCallback mContextMenuCallback;
    public MenuDialogHelper mContextMenuHelper;
    public ContextMenuPopupWindowHelper mContextMenuPopupWindowHelper;
    public boolean mExtraPaddingEnable;
    public int mExtraPaddingHorizontal;
    public int mExtraPaddingLevel;
    public FloatingABOLayoutSpec mFloatingWindowSize;
    public Rect mInnerInsets;
    public boolean mIsFloatingTheme;
    public boolean mIsFloatingWindow;
    public Rect mLastBaseContentInsets;
    public Rect mLastInnerInsets;
    public int[] mOffsetInWindow;
    public OnStatusBarChangeListener mOnStatusBarChangeListener;
    public boolean mOverlayMode;
    public boolean mRequestFitSystemWindow;
    public boolean mRootSubDecor;
    public int mTranslucentStatus;

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        boolean z = true;
        this.mRootSubDecor = true;
        this.mBaseContentInsets = new Rect();
        this.mLastBaseContentInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mBaseInnerInsets = new Rect();
        this.mLastInnerInsets = new Rect();
        this.mInnerInsets = new Rect();
        this.mContentMaskInsets = new Rect();
        this.mContextMenuCallback = new ContextMenuCallback();
        this.mIsFloatingTheme = false;
        this.mIsFloatingWindow = false;
        this.mOffsetInWindow = new int[2];
        this.mFloatingWindowSize = new FloatingABOLayoutSpec(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Window, i, 0);
        this.mIsFloatingTheme = obtainStyledAttributes.getBoolean(R$styleable.Window_isMiuixFloatingTheme, false);
        this.mIsFloatingWindow = BaseFloatingActivityHelper.isFloatingWindow(context);
        boolean z2 = obtainStyledAttributes.getBoolean(R$styleable.Window_contentAutoFitSystemWindow, false);
        this.mContentAutoFitSystemWindow = z2;
        if (z2) {
            this.mContentHeaderBackground = obtainStyledAttributes.getDrawable(R$styleable.Window_contentHeaderBackground);
        }
        int i2 = obtainStyledAttributes.getInt(R$styleable.Window_windowExtraPaddingHorizontal, 0);
        setExtraHorizontalPaddingLevel(i2);
        if (i2 != 1 && i2 != 2) {
            z = false;
        }
        setExtraHorizontalPaddingEnable(obtainStyledAttributes.getBoolean(R$styleable.Window_windowExtraPaddingHorizontalEnable, z));
        obtainStyledAttributes.recycle();
    }

    private void setFloatingMode(boolean z) {
        if (this.mIsFloatingTheme && this.mIsFloatingWindow != z) {
            this.mIsFloatingWindow = z;
            this.mFloatingWindowSize.onFloatingModeChanged(z);
            ActionBarContainer actionBarContainer = this.mActionBarTop;
            if (actionBarContainer != null) {
                actionBarContainer.setIsMiuixFloating(z);
            }
            requestFitSystemWindows();
            requestLayout();
        }
    }

    public void onFloatingModeChanged(boolean z) {
        setFloatingMode(z);
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mExtraPaddingHorizontal = LayoutUIUtils.getExtraPaddingByLevel(getContext(), this.mExtraPaddingLevel);
        this.mFloatingWindowSize.onConfigurationChanged();
    }

    public void setExtraHorizontalPaddingLevel(int i) {
        if (!LayoutUIUtils.isLevelValid(i) || this.mExtraPaddingLevel == i) {
            return;
        }
        this.mExtraPaddingLevel = i;
        this.mExtraPaddingHorizontal = LayoutUIUtils.getExtraPaddingByLevel(getContext(), i);
        requestLayout();
    }

    public int getExtraHorizontalPaddingLevel() {
        return this.mExtraPaddingLevel;
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        if (this.mExtraPaddingEnable != z) {
            this.mExtraPaddingEnable = z;
            requestLayout();
        }
    }

    public boolean isExtraHorizontalPaddingEnable() {
        return this.mExtraPaddingEnable;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View view, float f, float f2) {
        if (internalShowContextMenu(view, f, f2)) {
            return true;
        }
        return getParent() != null && getParent().showContextMenuForChild(view, f, f2);
    }

    public final boolean internalShowContextMenu(View view, float f, float f2) {
        ContextMenuBuilder contextMenuBuilder = this.mContextMenu;
        if (contextMenuBuilder == null) {
            ContextMenuBuilder contextMenuBuilder2 = new ContextMenuBuilder(getContext());
            this.mContextMenu = contextMenuBuilder2;
            contextMenuBuilder2.setCallback(this.mContextMenuCallback);
        } else {
            contextMenuBuilder.clear();
        }
        ContextMenuPopupWindowHelper show = this.mContextMenu.show(view, view.getWindowToken(), f, f2);
        this.mContextMenuPopupWindowHelper = show;
        if (show != null) {
            show.setPresenterCallback(this.mContextMenuCallback);
            return true;
        }
        return super.showContextMenuForChild(view);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View view) {
        ContextMenuBuilder contextMenuBuilder = this.mContextMenu;
        if (contextMenuBuilder == null) {
            ContextMenuBuilder contextMenuBuilder2 = new ContextMenuBuilder(getContext());
            this.mContextMenu = contextMenuBuilder2;
            contextMenuBuilder2.setCallback(this.mContextMenuCallback);
        } else {
            contextMenuBuilder.clear();
        }
        MenuDialogHelper show = this.mContextMenu.show(view, view.getWindowToken());
        this.mContextMenuHelper = show;
        if (show != null) {
            show.setPresenterCallback(this.mContextMenuCallback);
            return true;
        }
        return super.showContextMenuForChild(view);
    }

    @Override // android.view.View
    public boolean fitSystemWindows(Rect rect) {
        boolean z;
        boolean z2;
        Window window;
        OnStatusBarChangeListener onStatusBarChangeListener = this.mOnStatusBarChangeListener;
        if (onStatusBarChangeListener != null) {
            onStatusBarChangeListener.onStatusBarHeightChange(rect.top);
        }
        this.mBaseInnerInsets.set(rect);
        if (this.mIsFloatingTheme && this.mIsFloatingWindow) {
            this.mBaseInnerInsets.top = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_window_top_offset);
            Rect rect2 = this.mBaseInnerInsets;
            rect2.left = 0;
            rect2.right = 0;
        }
        boolean z3 = true;
        if (Build.VERSION.SDK_INT >= 28) {
            Activity activityContextFromView = getActivityContextFromView(this);
            boolean z4 = (activityContextFromView == null || (window = activityContextFromView.getWindow()) == null || window.getAttributes().layoutInDisplayCutoutMode != 1) ? false : true;
            if (!z4) {
                z4 = AttributeResolver.resolveInt(getContext(), 16844166, 0) == 1;
            }
            if (z4) {
                Rect rect3 = this.mBaseInnerInsets;
                rect3.left = 0;
                rect3.right = 0;
            }
        }
        if (isRootSubDecor() || (isLayoutHideNavigation() && this.mBaseInnerInsets.bottom == MiuixUIUtils.getNavigationBarHeight(getContext()))) {
            z = false;
        } else {
            this.mBaseInnerInsets.bottom = 0;
            z = true;
        }
        if (!isRootSubDecor() && !z) {
            this.mBaseInnerInsets.bottom = 0;
        }
        computeFitSystemInsets(this.mBaseInnerInsets, this.mBaseContentInsets);
        if (this.mActionBarTop != null) {
            if (isTranslucentStatus()) {
                this.mActionBarTop.setPendingInsets(rect);
                ActionMode actionMode = this.mActionMode;
                if (actionMode instanceof SearchActionModeImpl) {
                    ((SearchActionModeImpl) actionMode).setPendingInsets(rect);
                }
            }
            z2 = applyInsets(this.mActionBarTop, this.mBaseInnerInsets, true, isRootSubDecor() && !isTranslucentStatus(), false, true);
        } else {
            z2 = false;
        }
        if (this.mActionBarBottom != null) {
            this.mContentMaskInsets.set(this.mBaseInnerInsets);
            Rect rect4 = new Rect();
            rect4.set(this.mBaseContentInsets);
            if (this.mIsFloatingWindow) {
                rect4.bottom = 0;
            }
            z2 |= applyInsets(this.mActionBarBottom, rect4, true, false, true, true);
        }
        if (!this.mLastBaseContentInsets.equals(this.mBaseContentInsets)) {
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        } else {
            z3 = z2;
        }
        if (z3) {
            requestLayout();
        }
        return isRootSubDecor();
    }

    public final Activity getActivityContextFromView(View view) {
        Context context = ((ViewGroup) view.getRootView()).getChildAt(0).getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        WindowInsets onApplyWindowInsets = super.onApplyWindowInsets(windowInsets);
        return (Build.VERSION.SDK_INT < 28 || onApplyWindowInsets.isConsumed() || !isRootSubDecor()) ? onApplyWindowInsets : windowInsets.consumeDisplayCutout();
    }

    public final void computeFitSystemInsets(Rect rect, Rect rect2) {
        boolean isRootSubDecor = isRootSubDecor();
        boolean isTranslucentStatus = isTranslucentStatus();
        rect2.set(rect);
        if ((!isRootSubDecor || isTranslucentStatus) && !this.mContentAutoFitSystemWindow) {
            rect2.top = 0;
        }
        if (this.mIsFloatingWindow) {
            rect2.bottom = 0;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean applyInsets(android.view.View r3, android.graphics.Rect r4, boolean r5, boolean r6, boolean r7, boolean r8) {
        /*
            r2 = this;
            android.view.ViewGroup$LayoutParams r3 = r3.getLayoutParams()
            android.widget.FrameLayout$LayoutParams r3 = (android.widget.FrameLayout.LayoutParams) r3
            r0 = 1
            if (r5 == 0) goto L13
            int r5 = r3.leftMargin
            int r1 = r4.left
            if (r5 == r1) goto L13
            r3.leftMargin = r1
            r5 = r0
            goto L14
        L13:
            r5 = 0
        L14:
            if (r6 == 0) goto L1f
            int r6 = r3.topMargin
            int r1 = r4.top
            if (r6 == r1) goto L1f
            r3.topMargin = r1
            r5 = r0
        L1f:
            if (r8 == 0) goto L2a
            int r6 = r3.rightMargin
            int r8 = r4.right
            if (r6 == r8) goto L2a
            r3.rightMargin = r8
            r5 = r0
        L2a:
            if (r7 == 0) goto L35
            int r6 = r3.bottomMargin
            int r4 = r4.bottom
            if (r6 == r4) goto L35
            r3.bottomMargin = r4
            goto L36
        L35:
            r0 = r5
        L36:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.appcompat.internal.app.widget.ActionBarOverlayLayout.applyInsets(android.view.View, android.graphics.Rect, boolean, boolean, boolean, boolean):boolean");
    }

    public void animateContentMarginBottom(int i) {
        Rect rect = new Rect();
        Rect rect2 = this.mContentInsets;
        rect.top = rect2.top;
        rect.bottom = i;
        rect.right = rect2.right;
        rect.left = rect2.left;
        applyInsets(this.mContentView, rect, true, true, true, true);
        this.mContentView.requestLayout();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        requestFitSystemWindows();
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer == null || !actionBarContainer.isBlurEnable()) {
            return;
        }
        this.mActionBarTop.updateAllClipView();
    }

    @Override // android.view.View, android.view.ViewParent
    public void requestFitSystemWindows() {
        super.requestFitSystemWindows();
        this.mRequestFitSystemWindow = true;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        pullChildren();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int widthMeasureSpec = this.mFloatingWindowSize.getWidthMeasureSpec(i);
        int heightMeasureSpec = this.mFloatingWindowSize.getHeightMeasureSpec(i2);
        View view = this.mContentView;
        View view2 = this.mContentMask;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        for (int i9 = 0; i9 < getChildCount(); i9++) {
            View childAt = getChildAt(i9);
            if (childAt != view && childAt != view2 && childAt.getVisibility() != 8) {
                measureChildWithMargins(childAt, widthMeasureSpec, 0, heightMeasureSpec, 0);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) childAt.getLayoutParams();
                i6 = Math.max(i6, childAt.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin);
                i7 = Math.max(i7, childAt.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin);
                i8 = FrameLayout.combineMeasuredStates(i8, childAt.getMeasuredState());
            }
        }
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer == null || actionBarContainer.getVisibility() == 8) {
            i3 = 0;
            i4 = 0;
        } else {
            i3 = this.mActionBarTop.getMeasuredHeight();
            ActionBarContainer actionBarContainer2 = this.mActionBarTop;
            i4 = (actionBarContainer2 == null || !actionBarContainer2.isBlurEnable()) ? 0 : i3 <= 0 ? 0 : i3;
        }
        ActionBarView actionBarView = this.mActionBarView;
        int bottomInset = (actionBarView == null || !actionBarView.isSplitActionBar()) ? 0 : getBottomInset();
        this.mInnerInsets.set(this.mBaseInnerInsets);
        this.mContentInsets.set(this.mBaseContentInsets);
        if (isTranslucentStatus() && i3 > 0) {
            this.mContentInsets.top = 0;
        }
        ActionBarContainer actionBarContainer3 = this.mActionBarTop;
        if (actionBarContainer3 != null && actionBarContainer3.isBlurEnable()) {
            if (!this.mOverlayMode) {
                Rect rect = this.mContentInsets;
                rect.top = 0;
                rect.bottom = 0;
            } else {
                Rect rect2 = this.mInnerInsets;
                rect2.top = 0;
                this.mContentInsets.top = 0;
                rect2.bottom = 0;
            }
        } else if (!this.mOverlayMode) {
            Rect rect3 = this.mContentInsets;
            rect3.top += i3;
            rect3.bottom += bottomInset;
        } else {
            if (!isTranslucentStatus()) {
                this.mInnerInsets.top += i3;
            } else if (i3 > 0) {
                this.mInnerInsets.top = i3;
            }
            this.mInnerInsets.bottom += bottomInset;
        }
        if (!this.mIsFloatingTheme && isLayoutHideNavigation()) {
            if (getResources().getConfiguration().orientation == 1) {
                this.mContentInsets.bottom = 0;
            } else {
                Rect rect4 = this.mContentInsets;
                rect4.right = 0;
                rect4.left = 0;
                if (DeviceHelper.isTablet(getContext()) || !DeviceHelper.isHideGestureLine(getContext())) {
                    this.mContentInsets.bottom = 0;
                }
            }
        }
        if (!isBottomAnimating()) {
            i5 = i4;
            applyInsets(view, this.mContentInsets, true, true, true, true);
        } else {
            i5 = i4;
        }
        ActionBarContainer actionBarContainer4 = this.mActionBarTop;
        if (actionBarContainer4 != null && actionBarContainer4.isBlurEnable() && !this.mOverlayMode) {
            int paddingLeft = view.getPaddingLeft();
            int paddingRight = view.getPaddingRight();
            if (bottomInset == 0) {
                bottomInset = view.getPaddingBottom();
            }
            view.setPadding(paddingLeft, i5, paddingRight, bottomInset);
        } else if (!this.mOverlayMode) {
            view.setPadding(view.getPaddingLeft(), 0, view.getPaddingRight(), 0);
        }
        if (!this.mLastInnerInsets.equals(this.mInnerInsets) || this.mRequestFitSystemWindow) {
            this.mLastInnerInsets.set(this.mInnerInsets);
            super.fitSystemWindows(this.mInnerInsets);
            this.mRequestFitSystemWindow = false;
        }
        if (isTranslucentStatus() && this.mContentAutoFitSystemWindow) {
            Drawable drawable = this.mContentHeaderBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getRight() - getLeft(), this.mBaseContentInsets.top);
            } else {
                ViewGroup viewGroup = (ViewGroup) findViewById(16908290);
                if (viewGroup != null && viewGroup.getChildCount() == 1) {
                    View childAt2 = viewGroup.getChildAt(0);
                    childAt2.setPadding(childAt2.getPaddingLeft(), 0, childAt2.getPaddingRight(), childAt2.getPaddingBottom());
                }
            }
        }
        measureChildWithMargins(view, (!this.mExtraPaddingEnable || this.mExtraPaddingHorizontal <= 0) ? widthMeasureSpec : View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(widthMeasureSpec) - (this.mExtraPaddingHorizontal * 2), View.MeasureSpec.getMode(widthMeasureSpec)), 0, heightMeasureSpec, 0);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) view.getLayoutParams();
        int max = Math.max(i6, view.getMeasuredWidth() + layoutParams2.leftMargin + layoutParams2.rightMargin);
        int max2 = Math.max(i7, view.getMeasuredHeight() + layoutParams2.topMargin + layoutParams2.bottomMargin);
        int combineMeasuredStates = FrameLayout.combineMeasuredStates(i8, view.getMeasuredState());
        if (view2 != null && view2.getVisibility() == 0) {
            applyInsets(view2, this.mContentMaskInsets, true, false, true, true);
            measureChildWithMargins(view2, widthMeasureSpec, 0, heightMeasureSpec, 0);
        }
        setMeasuredDimension(FrameLayout.resolveSizeAndState(Math.max(max + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), widthMeasureSpec, combineMeasuredStates), FrameLayout.resolveSizeAndState(Math.max(max2 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), heightMeasureSpec, combineMeasuredStates << 16));
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (!this.mExtraPaddingEnable || this.mExtraPaddingHorizontal <= 0) {
            return;
        }
        View view = this.mContentView;
        int left = view.getLeft() + this.mExtraPaddingHorizontal;
        int top = view.getTop();
        int right = view.getRight() + this.mExtraPaddingHorizontal;
        int bottom = view.getBottom();
        if (ViewUtils.isLayoutRtl(this)) {
            left = view.getLeft() - this.mExtraPaddingHorizontal;
            right = view.getRight() - this.mExtraPaddingHorizontal;
        }
        view.layout(left, top, right, bottom);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        Drawable drawable;
        if (this.mContentAutoFitSystemWindow && (drawable = this.mContentHeaderBackground) != null) {
            drawable.setBounds(0, 0, getRight() - getLeft(), this.mBaseContentInsets.top);
            this.mContentHeaderBackground.draw(canvas);
        }
        super.dispatchDraw(canvas);
    }

    public int getBottomInset() {
        ActionBarContainer actionBarContainer = this.mActionBarBottom;
        if (actionBarContainer != null) {
            return actionBarContainer.getInsetHeight();
        }
        return 0;
    }

    public final boolean isBottomAnimating() {
        return this.mAnimating;
    }

    public ActionBar getActionBar() {
        return this.mActionBar;
    }

    public void setActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
    }

    public void setOverlayMode(boolean z) {
        this.mOverlayMode = z;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public ContentMaskAnimator getContentMaskAnimator(View.OnClickListener onClickListener) {
        return new ContentMaskAnimator(onClickListener);
    }

    public ActionBarView getActionBarView() {
        return this.mActionBarView;
    }

    public Window.Callback getCallback() {
        return this.mCallback;
    }

    public void setCallback(Window.Callback callback) {
        this.mCallback = callback;
    }

    public void setTranslucentStatus(int i) {
        if (this.mTranslucentStatus != i) {
            this.mTranslucentStatus = i;
            requestFitSystemWindows();
        }
    }

    public void setContentView(View view) {
        this.mContentView = view;
    }

    public final void pullChildren() {
        if (this.mContentView == null) {
            this.mContentView = findViewById(16908290);
            ActionBarContainer actionBarContainer = (ActionBarContainer) findViewById(R$id.action_bar_container);
            this.mActionBarTop = actionBarContainer;
            boolean z = false;
            if (this.mIsFloatingTheme && this.mIsFloatingWindow && actionBarContainer != null && !AttributeResolver.resolveBoolean(getContext(), R$attr.windowActionBar, false)) {
                this.mActionBarTop.setVisibility(8);
                this.mActionBarTop = null;
            }
            ActionBarContainer actionBarContainer2 = this.mActionBarTop;
            if (actionBarContainer2 == null) {
                return;
            }
            this.mActionBarView = (ActionBarView) actionBarContainer2.findViewById(R$id.action_bar);
            ActionBarContainer actionBarContainer3 = this.mActionBarTop;
            if (this.mIsFloatingTheme && this.mIsFloatingWindow) {
                z = true;
            }
            actionBarContainer3.setIsMiuixFloating(z);
        }
    }

    public void setSplitActionBarView(ActionBarContainer actionBarContainer) {
        this.mActionBarBottom = actionBarContainer;
    }

    public void setContentMask(View view) {
        View view2;
        this.mContentMask = view;
        if (!DeviceHelper.isOled() || (view2 = this.mContentMask) == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 21) {
            view2.setBackground(getContext().getResources().getDrawable(R$drawable.miuix_appcompat_window_content_mask_oled, getContext().getTheme()));
        } else {
            view2.setBackground(getContext().getResources().getDrawable(R$drawable.miuix_appcompat_window_content_mask_oled));
        }
    }

    public View getContentMask() {
        return this.mContentMask;
    }

    public void setActionBarContextView(ActionBarContextView actionBarContextView) {
        this.mActionBarContextView = actionBarContextView;
    }

    public boolean isRootSubDecor() {
        return this.mRootSubDecor;
    }

    public boolean isTranslucentStatus() {
        int windowSystemUiVisibility = getWindowSystemUiVisibility();
        return (((windowSystemUiVisibility & 256) != 0) && ((windowSystemUiVisibility & 1024) != 0)) || (this.mTranslucentStatus != 0);
    }

    public final boolean isLayoutHideNavigation() {
        return (getWindowSystemUiVisibility() & 512) != 0;
    }

    public void setRootSubDecor(boolean z) {
        this.mRootSubDecor = z;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        return this.mIsFloatingTheme;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (super.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        if (isBackPressed(keyEvent)) {
            if (this.mActionMode != null) {
                ActionBarContextView actionBarContextView = this.mActionBarContextView;
                if (actionBarContextView != null && actionBarContextView.hideOverflowMenu()) {
                    return true;
                }
                this.mActionMode.finish();
                this.mActionMode = null;
                return true;
            }
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null && actionBarView.hideOverflowMenu()) {
                return true;
            }
        }
        return false;
    }

    public final boolean isBackPressed(KeyEvent keyEvent) {
        return keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return startActionMode(view, callback);
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        ActionMode actionMode2 = null;
        this.mActionMode = null;
        if (getCallback() != null) {
            actionMode2 = getCallback().onWindowStartingActionMode(createActionModeCallbackWrapper(callback));
        }
        if (actionMode2 != null) {
            this.mActionMode = actionMode2;
        }
        if (this.mActionMode != null && getCallback() != null) {
            getCallback().onActionModeStarted(this.mActionMode);
        }
        return this.mActionMode;
    }

    public ActionMode startActionMode(View view, ActionMode.Callback callback) {
        if (view instanceof ActionBarOverlayLayout) {
            ActionMode actionMode = this.mActionMode;
            if (actionMode != null) {
                actionMode.finish();
            }
            ActionMode startActionMode = view.startActionMode(createActionModeCallbackWrapper(callback));
            this.mActionMode = startActionMode;
            return startActionMode;
        }
        return startActionMode(callback);
    }

    public final ActionModeCallbackWrapper createActionModeCallbackWrapper(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            return new SearchActionModeCallbackWrapper(callback);
        }
        return new ActionModeCallbackWrapper(callback);
    }

    /* loaded from: classes3.dex */
    public class ActionModeCallbackWrapper implements ActionMode.Callback {
        public ActionMode.Callback mWrapped;

        public ActionModeCallbackWrapper(ActionMode.Callback callback) {
            this.mWrapped = callback;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onCreateActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mWrapped.onActionItemClicked(actionMode, menuItem);
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            this.mWrapped.onDestroyActionMode(actionMode);
            if (ActionBarOverlayLayout.this.getCallback() != null) {
                ActionBarOverlayLayout.this.getCallback().onActionModeFinished(actionMode);
            }
            ActionBarOverlayLayout.this.mActionMode = null;
        }
    }

    /* loaded from: classes3.dex */
    public class SearchActionModeCallbackWrapper extends ActionModeCallbackWrapper implements SearchActionMode.Callback {
        public SearchActionModeCallbackWrapper(ActionMode.Callback callback) {
            super(callback);
        }
    }

    /* loaded from: classes3.dex */
    public class ContentMaskAnimator implements Animator.AnimatorListener {
        public ObjectAnimator mHideAnimator;
        public View.OnClickListener mOnClickListener;
        public ObjectAnimator mShowAnimator;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        public ContentMaskAnimator(View.OnClickListener onClickListener) {
            this.mOnClickListener = onClickListener;
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(ActionBarOverlayLayout.this.mContentMask, "alpha", 0.0f, 1.0f);
            this.mShowAnimator = ofFloat;
            ofFloat.addListener(this);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(ActionBarOverlayLayout.this.mContentMask, "alpha", 1.0f, 0.0f);
            this.mHideAnimator = ofFloat2;
            ofFloat2.addListener(this);
            if (!DeviceHelper.isFeatureWholeAnim()) {
                this.mShowAnimator.setDuration(0L);
                this.mHideAnimator.setDuration(0L);
            }
        }

        public Animator show() {
            return this.mShowAnimator;
        }

        public Animator hide() {
            return this.mHideAnimator;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            if (ActionBarOverlayLayout.this.mContentMask == null || ActionBarOverlayLayout.this.mActionBarBottom == null || animator != this.mShowAnimator) {
                return;
            }
            ActionBarOverlayLayout.this.mContentMask.setVisibility(0);
            ActionBarOverlayLayout.this.mContentMask.bringToFront();
            ActionBarOverlayLayout.this.mActionBarBottom.bringToFront();
            ActionBarOverlayLayout.this.mContentMask.setOnClickListener(this.mOnClickListener);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (ActionBarOverlayLayout.this.mContentMask == null || ActionBarOverlayLayout.this.mActionBarBottom == null || ActionBarOverlayLayout.this.mContentMask.getAlpha() != 0.0f) {
                return;
            }
            ActionBarOverlayLayout.this.mActionBarBottom.bringToFront();
            ActionBarOverlayLayout.this.mContentMask.setOnClickListener(null);
            ActionBarOverlayLayout.this.mContentMask.setVisibility(8);
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (ActionBarOverlayLayout.this.mContentMask == null || ActionBarOverlayLayout.this.mActionBarBottom == null || animator != this.mHideAnimator) {
                return;
            }
            ActionBarOverlayLayout.this.mActionBarBottom.bringToFront();
            ActionBarOverlayLayout.this.mContentMask.setOnClickListener(null);
        }
    }

    /* loaded from: classes3.dex */
    public class ContextMenuCallback implements MenuBuilder.Callback, MenuPresenter.Callback {
        public MenuDialogHelper mSubMenuHelper;

        @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
        public void onMenuModeChange(MenuBuilder menuBuilder) {
        }

        public ContextMenuCallback() {
        }

        public void onCloseSubMenu(MenuBuilder menuBuilder) {
            if (ActionBarOverlayLayout.this.mCallback != null) {
                ActionBarOverlayLayout.this.mCallback.onPanelClosed(6, menuBuilder.getRootMenu());
            }
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            if (menuBuilder.getRootMenu() != menuBuilder) {
                onCloseSubMenu(menuBuilder);
            }
            if (z) {
                if (ActionBarOverlayLayout.this.mCallback != null) {
                    ActionBarOverlayLayout.this.mCallback.onPanelClosed(6, menuBuilder);
                }
                ActionBarOverlayLayout.this.dismissContextMenu();
                MenuDialogHelper menuDialogHelper = this.mSubMenuHelper;
                if (menuDialogHelper == null) {
                    return;
                }
                menuDialogHelper.dismiss();
                this.mSubMenuHelper = null;
            }
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            menuBuilder.setCallback(this);
            MenuDialogHelper menuDialogHelper = new MenuDialogHelper(menuBuilder);
            this.mSubMenuHelper = menuDialogHelper;
            menuDialogHelper.show(null);
            return true;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            if (ActionBarOverlayLayout.this.mCallback != null) {
                return ActionBarOverlayLayout.this.mCallback.onMenuItemSelected(6, menuItem);
            }
            return false;
        }
    }

    public final void dismissContextMenu() {
        MenuDialogHelper menuDialogHelper = this.mContextMenuHelper;
        if (menuDialogHelper != null) {
            menuDialogHelper.dismiss();
            this.mContextMenu = null;
        }
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        this.mOnStatusBarChangeListener = onStatusBarChangeListener;
    }

    public void setAnimating(boolean z) {
        this.mAnimating = z;
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        int[] iArr2 = this.mOffsetInWindow;
        iArr2[1] = 0;
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            actionBarContainer.onNestedScroll(view, i, i2, i3, i4, i5, iArr, iArr2);
        }
        this.mContentView.offsetTopAndBottom(-this.mOffsetInWindow[1]);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        ActionBarContainer actionBarContainer;
        ActionBar actionBar = this.mActionBar;
        return actionBar != null && actionBar.isShowing() && (actionBarContainer = this.mActionBarTop) != null && actionBarContainer.onStartNestedScroll(view, view2, i, i2);
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            actionBarContainer.onNestedScrollAccepted(view, view2, i, i2);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onStopNestedScroll(View view, int i) {
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            actionBarContainer.onStopNestedScroll(view, i);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        int[] iArr2 = this.mOffsetInWindow;
        iArr2[1] = 0;
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        if (actionBarContainer != null) {
            actionBarContainer.onNestedPreScroll(view, i, i2, iArr, i3, iArr2);
        }
        this.mContentView.offsetTopAndBottom(-this.mOffsetInWindow[1]);
    }
}
