package miuix.appcompat.internal.app.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.util.Objects;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$id;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.appcompat.internal.view.menu.action.PhoneActionMenuView;
import miuix.blurdrawable.widget.BlurBackgroundView;
import miuix.core.util.WindowUtils;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;

/* loaded from: classes3.dex */
public class ActionBarContainer extends FrameLayout implements ActionBar.FragmentViewPagerChangeListener {
    public ActionBarContextView mActionBarContextView;
    public ActionBarView mActionBarView;
    public Drawable mBackground;
    public Drawable[] mBackgroundArray;
    public Drawable mBackgroundBackup;
    public BlurBackgroundView mBlurBackgroundView;
    public int mCurBarExpandState;
    public boolean mCurBarResizable;
    public int mCurContextBarExpandState;
    public boolean mCurContextBarResizable;
    public Animator mCurrentShowAnim;
    public boolean mCustomBackground;
    public boolean mCustomViewAutoFitSystemWindow;
    public int mHeightMaxMeasureSpec;
    public AnimatorListenerAdapter mHideListener;
    public boolean mIsMiuixFloating;
    public boolean mIsShowBlurBackgroundView;
    public boolean mIsSplit;
    public boolean mIsStacked;
    public boolean mIsTransitioning;
    public Rect mPendingInsets;
    public boolean mRequestAnimation;
    public AnimatorListenerAdapter mShowListener;
    public Drawable mSplitBackground;
    public Drawable mSplitBackgroundBackup;
    public Drawable mStackedBackground;
    public View mTabContainer;
    public int mTabContainerPaddingTop;

    @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
    public void onPageSelected(int i) {
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public ActionMode startActionModeForChild(View view, ActionMode.Callback callback) {
        return null;
    }

    public ActionBarContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        boolean z = false;
        this.mIsShowBlurBackgroundView = false;
        this.mCustomBackground = false;
        this.mHeightMaxMeasureSpec = -1;
        this.mHideListener = new AnimatorListenerAdapter() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ActionBarContainer.this.setVisibility(8);
                ActionBarContainer.this.mCurrentShowAnim = null;
            }
        };
        this.mShowListener = new AnimatorListenerAdapter() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ActionBarContainer.this.mCurrentShowAnim = null;
            }
        };
        setBackground(null);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_android_background);
        this.mBackground = drawable;
        this.mBackgroundArray = new Drawable[]{drawable, obtainStyledAttributes.getDrawable(R$styleable.ActionBar_actionBarEmbededTabsBackground), obtainStyledAttributes.getDrawable(R$styleable.ActionBar_actionBarStackedBackground)};
        this.mCustomViewAutoFitSystemWindow = obtainStyledAttributes.getBoolean(R$styleable.ActionBar_customViewAutoFitSystemWindow, false);
        if (getId() == R$id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = obtainStyledAttributes.getDrawable(R$styleable.ActionBar_android_backgroundSplit);
        }
        obtainStyledAttributes.recycle();
        if (!this.mIsSplit) {
            setPadding(0, 0, 0, 0);
        }
        resizeSplitMaxHeight();
        addActionBarBlurView(context);
        if (!this.mIsSplit ? !(this.mBackground != null || this.mStackedBackground != null) : this.mSplitBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = (ActionBarView) findViewById(R$id.action_bar);
        this.mActionBarContextView = (ActionBarContextView) findViewById(R$id.action_context_bar);
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            this.mCurBarExpandState = actionBarView.getExpandState();
            this.mCurBarResizable = this.mActionBarView.isResizable();
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            this.mCurContextBarExpandState = actionBarContextView.getExpandState();
            this.mCurContextBarResizable = this.mActionBarContextView.isResizable();
        }
    }

    public void setIsMiuixFloating(boolean z) {
        this.mIsMiuixFloating = z;
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            if (z) {
                this.mCurBarExpandState = actionBarView.getExpandState();
                this.mCurBarResizable = this.mActionBarView.isResizable();
                this.mActionBarView.setExpandState(0);
                this.mActionBarView.setResizable(false);
            } else {
                actionBarView.setResizable(this.mCurBarResizable);
                this.mActionBarView.setExpandState(this.mCurBarExpandState);
            }
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null) {
            if (z) {
                this.mCurContextBarExpandState = actionBarContextView.getExpandState();
                this.mCurContextBarResizable = this.mActionBarContextView.isResizable();
                this.mActionBarContextView.setExpandState(0);
                this.mActionBarContextView.setResizable(false);
                return;
            }
            actionBarContextView.setResizable(this.mCurContextBarResizable);
            this.mActionBarContextView.setExpandState(this.mCurContextBarExpandState);
        }
    }

    public boolean isMiuixFloating() {
        return this.mIsMiuixFloating;
    }

    public void setActionBarContextView(ActionBarContextView actionBarContextView) {
        this.mActionBarContextView = actionBarContextView;
    }

    public void setPendingInsets(Rect rect) {
        if (!this.mIsSplit) {
            if (this.mPendingInsets == null) {
                this.mPendingInsets = new Rect();
            }
            if (Objects.equals(this.mPendingInsets, rect)) {
                return;
            }
            this.mPendingInsets.set(rect);
            applyInsets(this.mActionBarView);
            applyInsets(this.mActionBarContextView);
        }
    }

    public Rect getPendingInsets() {
        return this.mPendingInsets;
    }

    public boolean setBlurBackground(boolean z) {
        boolean blurBackground;
        if (this.mIsShowBlurBackgroundView == z) {
            return true;
        }
        if (this.mIsSplit) {
            this.mIsShowBlurBackgroundView = z;
            this.mBlurBackgroundView.setBlurBackground(false);
            updateBackground(z);
            blurBackground = false;
            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) instanceof PhoneActionMenuView) {
                    PhoneActionMenuView phoneActionMenuView = (PhoneActionMenuView) getChildAt(i);
                    boolean blurBackground2 = phoneActionMenuView.setBlurBackground(z);
                    if (blurBackground2) {
                        phoneActionMenuView.updateBackground(z);
                    }
                    blurBackground = blurBackground2;
                }
            }
        } else {
            blurBackground = this.mBlurBackgroundView.setBlurBackground(z);
            if (blurBackground) {
                updateAllClipView(!z);
                this.mIsShowBlurBackgroundView = z;
                updateBackground(z);
            }
        }
        return blurBackground;
    }

    public final void updateBackground(boolean z) {
        if (z) {
            clearBackground();
        } else {
            resetBackground();
        }
    }

    public final void clearBackground() {
        this.mBackgroundBackup = this.mBackground;
        setPrimaryBackground(null);
        if (this.mIsSplit) {
            this.mSplitBackgroundBackup = this.mSplitBackground;
            setSplitBackground(null);
            return;
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setBackground(null);
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView == null) {
            return;
        }
        actionBarContextView.updateBackground(true);
    }

    public final void resetBackground() {
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable != null) {
                setSplitBackground(drawable);
                return;
            }
            Drawable drawable2 = this.mSplitBackgroundBackup;
            if (drawable2 == null) {
                return;
            }
            setSplitBackground(drawable2);
            return;
        }
        Drawable drawable3 = this.mBackground;
        if (drawable3 != null) {
            setPrimaryBackground(drawable3);
        } else {
            Drawable drawable4 = this.mBackgroundBackup;
            if (drawable4 != null) {
                setPrimaryBackground(drawable4);
            }
        }
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView == null) {
            return;
        }
        actionBarContextView.updateBackground(false);
    }

    public boolean isBlurEnable() {
        return this.mIsShowBlurBackgroundView;
    }

    public void updateAllClipView() {
        boolean z = this.mIsShowBlurBackgroundView;
        if (!z) {
            return;
        }
        updateAllClipView(!z);
    }

    public final void updateAllClipView(boolean z) {
        ViewGroup viewGroup;
        if (getParent() == null || !(getParent() instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup2 = (ViewGroup) getParent();
        if (!(viewGroup2 instanceof ActionBarOverlayLayout) || (viewGroup = (ViewGroup) viewGroup2.findViewById(16908290)) == null) {
            return;
        }
        setAllClipChildren(viewGroup, z);
        setAllClipToPadding(viewGroup, z);
    }

    public final void setAllClipChildren(ViewGroup viewGroup, boolean z) {
        viewGroup.setClipChildren(z);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                setAllClipChildren((ViewGroup) childAt, z);
            }
        }
    }

    public final void setAllClipToPadding(ViewGroup viewGroup, boolean z) {
        viewGroup.setClipToPadding(z);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof ViewGroup) {
                setAllClipToPadding((ViewGroup) childAt, z);
            }
        }
    }

    public final void addActionBarBlurView(Context context) {
        this.mBlurBackgroundView = new BlurBackgroundView(context);
        this.mBlurBackgroundView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.mBlurBackgroundView, 0);
    }

    public int getCollapsedHeight() {
        if (this.mIsSplit) {
            int i = 0;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                if (getChildAt(i2) instanceof ActionMenuView) {
                    ActionMenuView actionMenuView = (ActionMenuView) getChildAt(i2);
                    if (actionMenuView.getVisibility() == 0 && actionMenuView.getAlpha() != 0.0f && actionMenuView.getCollapsedHeight() > 0) {
                        i = Math.max(i, actionMenuView.getCollapsedHeight());
                    }
                }
            }
            return i;
        }
        return 0;
    }

    public int getInsetHeight() {
        return getCollapsedHeight();
    }

    public void setPrimaryBackground(Drawable drawable) {
        Drawable drawable2 = this.mBackground;
        Rect rect = null;
        if (drawable2 != null) {
            Rect bounds = drawable2.getBounds();
            this.mBackground.setCallback(null);
            unscheduleDrawable(this.mBackground);
            rect = bounds;
        }
        this.mBackground = drawable;
        boolean z = true;
        if (drawable != null) {
            drawable.setCallback(this);
            if (rect == null) {
                requestLayout();
            } else {
                this.mBackground.setBounds(rect);
            }
            this.mCustomBackground = true;
        } else {
            this.mCustomBackground = false;
        }
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setStackedBackground(Drawable drawable) {
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        boolean z = true;
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        View view = this.mTabContainer;
        if (view != null) {
            view.setBackground(this.mStackedBackground);
        }
    }

    public void setSplitBackground(Drawable drawable) {
        Drawable drawable2 = this.mSplitBackground;
        if (drawable2 != null) {
            drawable2.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        boolean z = true;
        if (!this.mIsSplit ? this.mBackground != null || this.mStackedBackground != null : this.mSplitBackground != null) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        boolean z = i == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(z, false);
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null) {
            drawable2.setVisible(z, false);
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 != null) {
            drawable3.setVisible(z, false);
        }
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return (drawable == this.mBackground && !this.mIsSplit) || (drawable == this.mStackedBackground && this.mIsStacked) || ((drawable == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(drawable));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        Drawable drawable2 = this.mStackedBackground;
        if (drawable2 != null && drawable2.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        Drawable drawable3 = this.mSplitBackground;
        if (drawable3 == null || !drawable3.isStateful()) {
            return;
        }
        this.mSplitBackground.setState(getDrawableState());
    }

    public void setTransitioning(boolean z) {
        this.mIsTransitioning = z;
        setDescendantFocusability(z ? 393216 : nexEngine.ExportHEVCMainTierLevel52);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return !this.mIsSplit && super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        return !this.mIsSplit;
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public void setTabContainer(ScrollingTabContainerView scrollingTabContainerView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        if (scrollingTabContainerView != null) {
            addView(scrollingTabContainerView);
            ViewGroup.LayoutParams layoutParams = scrollingTabContainerView.getLayoutParams();
            layoutParams.width = -1;
            layoutParams.height = -2;
            scrollingTabContainerView.setAllowCollapse(false);
            this.mTabContainerPaddingTop = scrollingTabContainerView.getPaddingTop();
        } else {
            View view2 = this.mTabContainer;
            if (view2 != null) {
                view2.setBackground(null);
            }
        }
        this.mTabContainer = scrollingTabContainerView;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        if (this.mIsSplit) {
            DeviceHelper.isTablet(getContext());
            return;
        }
        Drawable drawable = this.mBackground;
        if (drawable == null) {
            return;
        }
        drawable.draw(canvas);
    }

    public final void applyInsets(View view) {
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        if (view == this.mActionBarView && this.mCustomViewAutoFitSystemWindow) {
            return;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        Rect rect = this.mPendingInsets;
        marginLayoutParams.topMargin = (rect == null || this.mIsMiuixFloating) ? 0 : rect.top;
        view.setLayoutParams(marginLayoutParams);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        Rect rect;
        if (this.mIsSplit) {
            onMeasureSplit(i, i2);
            return;
        }
        View view = this.mTabContainer;
        if (view != null) {
            view.setPadding(view.getPaddingLeft(), this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
        }
        applyInsets(this.mActionBarView);
        applyInsets(this.mActionBarContextView);
        super.onMeasure(i, i2);
        ActionBarView actionBarView = this.mActionBarView;
        boolean z = (actionBarView == null || actionBarView.getVisibility() == 8 || this.mActionBarView.getMeasuredHeight() <= 0) ? false : true;
        if (z) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mActionBarView.getLayoutParams();
            i3 = this.mActionBarView.isCollapsed() ? layoutParams.topMargin : layoutParams.bottomMargin + this.mActionBarView.getMeasuredHeight() + layoutParams.topMargin;
        } else {
            i3 = 0;
        }
        View view2 = this.mTabContainer;
        if (view2 != null && view2.getVisibility() != 8 && View.MeasureSpec.getMode(i2) == Integer.MIN_VALUE) {
            setMeasuredDimension(getMeasuredWidth(), Math.min(i3 + this.mTabContainer.getMeasuredHeight(), View.MeasureSpec.getSize(i2)) + ((z || (rect = this.mPendingInsets) == null) ? 0 : rect.top));
        }
        int i4 = 0;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            if (childAt != this.mBlurBackgroundView && childAt.getVisibility() == 0 && childAt.getMeasuredHeight() > 0 && childAt.getMeasuredWidth() > 0) {
                i4++;
            }
        }
        if (i4 != 0) {
            return;
        }
        setMeasuredDimension(0, 0);
    }

    public final void onMeasureSplit(int i, int i2) {
        if (View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            i = View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(i), 1073741824);
        }
        int i3 = this.mHeightMaxMeasureSpec;
        if (i3 != -1) {
            i2 = i3;
        }
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        int i4 = 0;
        for (int i5 = 0; i5 < childCount; i5++) {
            if (!(getChildAt(i5) instanceof BlurBackgroundView)) {
                i4 = Math.max(i4, getChildAt(i5).getMeasuredHeight());
            }
        }
        if (i4 == 0) {
            setMeasuredDimension(0, 0);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        int measuredHeight = getMeasuredHeight();
        View view = this.mTabContainer;
        boolean z2 = false;
        if (view != null && view.getVisibility() != 8) {
            int measuredHeight2 = this.mTabContainer.getMeasuredHeight();
            ActionBarView actionBarView = this.mActionBarView;
            if (actionBarView != null && actionBarView.getVisibility() == 0 && this.mActionBarView.getMeasuredHeight() > 0) {
                View view2 = this.mTabContainer;
                view2.setPadding(view2.getPaddingLeft(), this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
            } else {
                Rect rect = this.mPendingInsets;
                measuredHeight2 += rect != null ? rect.top : 0;
                View view3 = this.mTabContainer;
                int paddingLeft = view3.getPaddingLeft();
                Rect rect2 = this.mPendingInsets;
                view3.setPadding(paddingLeft, rect2 != null ? rect2.top + this.mTabContainerPaddingTop : this.mTabContainerPaddingTop, this.mTabContainer.getPaddingRight(), this.mTabContainer.getPaddingBottom());
            }
            this.mTabContainer.layout(i, measuredHeight - measuredHeight2, i3, measuredHeight);
        }
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                z2 = true;
            }
        } else {
            selectDrawable();
            Drawable drawable2 = this.mBackground;
            if (drawable2 != null) {
                drawable2.setBounds(0, 0, i3 - i, measuredHeight);
                z2 = true;
            }
        }
        if (z2) {
            invalidate();
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        resizeSplitMaxHeight();
    }

    public final void resizeSplitMaxHeight() {
        TypedValue resolveTypedValue;
        if (!this.mIsSplit || (resolveTypedValue = AttributeResolver.resolveTypedValue(getContext(), R$attr.actionBarSplitMaxPercentageHeight)) == null || resolveTypedValue.type != 6) {
            return;
        }
        float windowHeight = WindowUtils.getWindowHeight(getContext());
        this.mHeightMaxMeasureSpec = View.MeasureSpec.makeMeasureSpec((int) resolveTypedValue.getFraction(windowHeight, windowHeight), Integer.MIN_VALUE);
    }

    @Override // android.view.View
    public void setAlpha(float f) {
        if (this.mIsShowBlurBackgroundView) {
            if (!this.mIsSplit) {
                return;
            }
            for (int i = 0; i < getChildCount(); i++) {
                if (!(getChildAt(i) instanceof BlurBackgroundView)) {
                    if (getChildAt(i) instanceof PhoneActionMenuView) {
                        ((PhoneActionMenuView) getChildAt(i)).setAlpha(f);
                    } else {
                        getChildAt(i).setAlpha(f);
                    }
                }
            }
            return;
        }
        super.setAlpha(f);
    }

    public void show(boolean z) {
        Animator animator = this.mCurrentShowAnim;
        if (animator != null) {
            animator.cancel();
        }
        setVisibility(0);
        if (z) {
            if (!this.mIsSplit) {
                return;
            }
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "TranslationY", getHeight(), 0.0f);
            this.mCurrentShowAnim = ofFloat;
            ofFloat.setDuration(DeviceHelper.isFeatureWholeAnim() ? getContext().getResources().getInteger(17694720) : 0L);
            this.mCurrentShowAnim.addListener(this.mShowListener);
            this.mCurrentShowAnim.start();
            ActionMenuView actionMenuView = (ActionMenuView) getChildAt(1);
            if (actionMenuView == null) {
                return;
            }
            actionMenuView.startLayoutAnimation();
            return;
        }
        setTranslationY(0.0f);
    }

    @Override // miuix.appcompat.app.ActionBar.FragmentViewPagerChangeListener
    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        ActionMenuView actionMenuView;
        if (!this.mIsSplit || (actionMenuView = (ActionMenuView) getChildAt(1)) == null) {
            return;
        }
        actionMenuView.onPageScrolled(i, f, z, z2);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mRequestAnimation) {
            post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarContainer.3
                @Override // java.lang.Runnable
                public void run() {
                    ActionBarContainer.this.show(true);
                }
            });
            this.mRequestAnimation = false;
        }
    }

    public final void selectDrawable() {
        ActionBarView actionBarView;
        Drawable[] drawableArr;
        if (this.mCustomBackground || this.mIsSplit || (actionBarView = this.mActionBarView) == null || this.mBackground == null || (drawableArr = this.mBackgroundArray) == null || drawableArr.length < 3) {
            return;
        }
        char c = 0;
        if (actionBarView.isTightTitleWithEmbeddedTabs()) {
            c = 1;
            int displayOptions = this.mActionBarView.getDisplayOptions();
            if ((displayOptions & 2) != 0 || (displayOptions & 4) != 0 || (displayOptions & 16) != 0) {
                c = 2;
            }
        }
        Drawable[] drawableArr2 = this.mBackgroundArray;
        if (drawableArr2[c] == null) {
            return;
        }
        this.mBackground = drawableArr2[c];
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedScroll(view, i, i2, i3, i4, i5, new int[]{0, 0}, new int[]{0, 0});
        }
        this.mActionBarView.onNestedScroll(view, i, i2, i3, i4, i5, iArr, iArr2);
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onStartNestedScroll(view, view2, i, i2);
        }
        return this.mActionBarView.onStartNestedScroll(view, view2, i, i2);
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedScrollAccepted(view, view2, i, i2);
        }
        this.mActionBarView.onNestedScrollAccepted(view, view2, i, i2);
    }

    public void onStopNestedScroll(View view, int i) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onStopNestedScroll(view, i);
        }
        this.mActionBarView.onStopNestedScroll(view, i);
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
        ActionBarContextView actionBarContextView = this.mActionBarContextView;
        if (actionBarContextView != null && actionBarContextView.getVisibility() == 0) {
            this.mActionBarContextView.onNestedPreScroll(view, i, i2, new int[]{0, 0}, i3, new int[]{0, 0});
        }
        this.mActionBarView.onNestedPreScroll(view, i, i2, iArr, i3, iArr2);
    }
}
