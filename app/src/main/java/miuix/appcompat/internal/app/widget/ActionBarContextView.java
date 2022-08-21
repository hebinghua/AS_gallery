package miuix.appcompat.internal.app.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.physics.DynamicAnimation;
import miuix.animation.physics.SpringAnimation;
import miuix.animation.physics.SpringAnimationSet;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$drawable;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.internal.app.widget.AbsActionBarView;
import miuix.appcompat.internal.view.EditActionModeImpl;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.action.ActionMenuItem;
import miuix.appcompat.internal.view.menu.action.ActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;
import miuix.internal.util.ViewUtils;
import miuix.view.ActionModeAnimationListener;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class ActionBarContextView extends AbsActionBarView implements ActionModeView {
    public WeakReference<ActionMode> mActionMode;
    public Drawable mActionModeBackground;
    public boolean mAnimateStart;
    public boolean mAnimateToVisible;
    public List<ActionModeAnimationListener> mAnimationListeners;
    public int mAnimationMode;
    public float mAnimationProgress;
    public Button mButton1;
    public ActionMenuItem mButton1MenuItem;
    public Button mButton2;
    public ActionMenuItem mButton2MenuItem;
    public View mCollapseContainer;
    public AbsActionBarView.CollapseView mCollapseController;
    public int mCollapseHeight;
    public int mContentInset;
    public int mExpandTitleStyleRes;
    public TextView mExpandTitleView;
    public FrameLayout mMovableContainer;
    public AbsActionBarView.CollapseView mMovableController;
    public boolean mNonTouchScrolling;
    public View.OnClickListener mOnMenuItemClickListener;
    public int mPendingHeight;
    public Runnable mPostScroll;
    public Scroller mPostScroller;
    public boolean mRequestAnimation;
    public Drawable mSplitBackground;
    public boolean mStartWithAnim;
    public CharSequence mTitle;
    public LinearLayout mTitleLayout;
    public boolean mTitleOptional;
    public int mTitleStyleRes;
    public TextView mTitleView;
    public boolean mTouchScrolling;
    public SpringAnimationSet mVisibilityAnim;

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public int getActionBarStyle() {
        return 16843668;
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ ActionBarTransitionListener getActionBarTransitionListener() {
        return super.getActionBarTransitionListener();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ ActionMenuView getActionMenuView() {
        return super.getActionMenuView();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getAnimatedVisibility() {
        return super.getAnimatedVisibility();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getContentHeight() {
        return super.getContentHeight();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ int getExpandState() {
        return super.getExpandState();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ ActionMenuView getMenuView() {
        return super.getMenuView();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ boolean isResizable() {
        return super.isResizable();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void postShowOverflowMenu() {
        super.postShowOverflowMenu();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        super.setActionBarTransitionListener(actionBarTransitionListener);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setContentHeight(int i) {
        super.setContentHeight(i);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setExpandState(int i) {
        super.setExpandState(i);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setExpandState(int i, boolean z) {
        super.setExpandState(i, z);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setResizable(boolean z) {
        super.setResizable(z);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setSplitView(ActionBarContainer actionBarContainer) {
        super.setSplitView(actionBarContainer);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public /* bridge */ /* synthetic */ void setSplitWhenNarrow(boolean z) {
        super.setSplitWhenNarrow(z);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView, android.view.View
    public /* bridge */ /* synthetic */ void setVisibility(int i) {
        super.setVisibility(i);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16843668);
    }

    public ActionBarContextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mStartWithAnim = true;
        this.mOnMenuItemClickListener = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarContextView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActionMenuItem actionMenuItem = view.getId() == 16908313 ? ActionBarContextView.this.mButton1MenuItem : ActionBarContextView.this.mButton2MenuItem;
                EditActionModeImpl editActionModeImpl = (EditActionModeImpl) ActionBarContextView.this.mActionMode.get();
                if (editActionModeImpl != null) {
                    editActionModeImpl.onMenuItemSelected((MenuBuilder) editActionModeImpl.getMenu(), actionMenuItem);
                }
                HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_TAP_NORMAL);
            }
        };
        this.mCollapseController = new AbsActionBarView.CollapseView();
        this.mMovableController = new AbsActionBarView.CollapseView();
        this.mTouchScrolling = false;
        this.mNonTouchScrolling = false;
        this.mPostScroll = new Runnable() { // from class: miuix.appcompat.internal.app.widget.ActionBarContextView.3
            @Override // java.lang.Runnable
            public void run() {
                if (ActionBarContextView.this.mPostScroller.computeScrollOffset()) {
                    ActionBarContextView actionBarContextView = ActionBarContextView.this;
                    actionBarContextView.mPendingHeight = actionBarContextView.mPostScroller.getCurrY() - ActionBarContextView.this.mCollapseHeight;
                    ActionBarContextView.this.requestLayout();
                    if (ActionBarContextView.this.mPostScroller.isFinished()) {
                        if (ActionBarContextView.this.mPostScroller.getCurrY() != ActionBarContextView.this.mCollapseHeight) {
                            if (ActionBarContextView.this.mPostScroller.getCurrY() != ActionBarContextView.this.mCollapseHeight + ActionBarContextView.this.mMovableContainer.getMeasuredHeight()) {
                                return;
                            }
                            ActionBarContextView.this.setExpandState(1);
                            return;
                        }
                        ActionBarContextView.this.setExpandState(0);
                        return;
                    }
                    ActionBarContextView.this.postOnAnimation(this);
                }
            }
        };
        this.mPostScroller = new Scroller(context);
        FrameLayout frameLayout = new FrameLayout(context);
        this.mMovableContainer = frameLayout;
        frameLayout.setId(R$id.action_bar_movable_container);
        FrameLayout frameLayout2 = this.mMovableContainer;
        Resources resources = context.getResources();
        int i2 = R$dimen.miuix_appcompat_action_bar_title_horizontal_padding;
        frameLayout2.setPaddingRelative(resources.getDimensionPixelOffset(i2), context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_top_padding), context.getResources().getDimensionPixelOffset(i2), context.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_bottom_padding));
        this.mMovableContainer.setVisibility(0);
        this.mMovableController.attachViews(this.mMovableContainer);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionMode, i, 0);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.ActionMode_android_background);
        this.mActionModeBackground = drawable;
        setBackground(drawable);
        this.mTitleStyleRes = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_android_titleTextStyle, 0);
        this.mExpandTitleStyleRes = obtainStyledAttributes.getResourceId(R$styleable.ActionMode_expandTitleTextStyle, 0);
        this.mContentHeight = obtainStyledAttributes.getLayoutDimension(R$styleable.ActionMode_android_height, 0);
        this.mSplitBackground = obtainStyledAttributes.getDrawable(R$styleable.ActionMode_android_backgroundSplit);
        this.mButton1MenuItem = new ActionMenuItem(context, 0, 16908313, 0, 0, context.getString(17039360));
        this.mButton2MenuItem = new ActionMenuItem(context, 0, 16908314, 0, 0, context.getString(R$string.miuix_appcompat_action_mode_select_all));
        this.mStartWithAnim = obtainStyledAttributes.getBoolean(R$styleable.ActionMode_actionModeAnim, true);
        obtainStyledAttributes.recycle();
    }

    public void setActionModeAnim(boolean z) {
        this.mStartWithAnim = z;
    }

    public void setContentInset(int i) {
        this.mContentInset = i;
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_horizontal_padding);
        this.mMovableContainer.setPaddingRelative(dimensionPixelOffset, getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_top_padding), dimensionPixelOffset, getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_action_bar_title_bottom_padding));
        setPaddingRelative(AttributeResolver.resolveDimensionPixelSize(getContext(), R$attr.actionBarPaddingStart), getPaddingTop(), AttributeResolver.resolveDimensionPixelSize(getContext(), R$attr.actionBarPaddingEnd), getPaddingBottom());
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.hideOverflowMenu(false);
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public void setSplitActionBar(boolean z) {
        if (this.mSplitActionBar != z) {
            if (this.mActionMenuPresenter != null) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -1);
                if (!z) {
                    ActionMenuView actionMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView = actionMenuView;
                    actionMenuView.setBackground(null);
                    ViewGroup viewGroup = (ViewGroup) this.mMenuView.getParent();
                    if (viewGroup != null) {
                        viewGroup.removeView(this.mMenuView);
                    }
                    addView(this.mMenuView, layoutParams);
                } else {
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                    layoutParams.width = -1;
                    layoutParams.height = -2;
                    layoutParams.gravity = DeviceHelper.isTablet(getContext()) ? 17 : 80;
                    ActionMenuView actionMenuView2 = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView = actionMenuView2;
                    actionMenuView2.setBackground(this.mSplitBackground);
                    ViewGroup viewGroup2 = (ViewGroup) this.mMenuView.getParent();
                    if (viewGroup2 != null) {
                        viewGroup2.removeView(this.mMenuView);
                    }
                    this.mSplitView.addView(this.mMenuView, layoutParams);
                }
            }
            super.setSplitActionBar(z);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isOverflowOpen = isOverflowMenuShowing();
        savedState.title = getTitle();
        Button button = this.mButton2;
        if (button != null) {
            savedState.defaultButtonText = button.getText();
        }
        int i = this.mInnerExpandState;
        if (i == 2) {
            savedState.expandState = 0;
        } else {
            savedState.expandState = i;
        }
        return savedState;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setTitle(savedState.title);
        setButton(16908314, savedState.defaultButtonText);
        if (savedState.isOverflowOpen) {
            postShowOverflowMenu();
        }
        setExpandState(savedState.expandState);
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        initTitle();
    }

    public void initTitle() {
        if (this.mTitleLayout == null) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R$layout.miuix_appcompat_action_mode_title_item, (ViewGroup) this, false);
            this.mTitleLayout = linearLayout;
            this.mButton1 = (Button) linearLayout.findViewById(16908313);
            this.mButton2 = (Button) this.mTitleLayout.findViewById(16908314);
            Button button = this.mButton1;
            if (button != null) {
                button.setOnClickListener(this.mOnMenuItemClickListener);
                Folme.useAt(this.mButton1).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).setAlpha(0.6f, new ITouchStyle.TouchType[0]).handleTouchOf(this.mButton1, new AnimConfig[0]);
                Folme.useAt(this.mButton1).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this.mButton1, new AnimConfig[0]);
            }
            Button button2 = this.mButton2;
            if (button2 != null) {
                button2.setOnClickListener(this.mOnMenuItemClickListener);
                Folme.useAt(this.mButton2).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).setAlpha(0.6f, new ITouchStyle.TouchType[0]).handleTouchOf(this.mButton2, new AnimConfig[0]);
                Folme.useAt(this.mButton2).hover().setEffect(IHoverStyle.HoverEffect.FLOATED_WRAPPED).handleHoverOf(this.mButton2, new AnimConfig[0]);
            }
            TextView textView = (TextView) this.mTitleLayout.findViewById(16908310);
            this.mTitleView = textView;
            if (this.mTitleStyleRes != 0) {
                textView.setTextAppearance(getContext(), this.mTitleStyleRes);
            }
            TextView textView2 = new TextView(getContext());
            this.mExpandTitleView = textView2;
            if (this.mExpandTitleStyleRes != 0) {
                textView2.setTextAppearance(getContext(), this.mExpandTitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mExpandTitleView.setText(this.mTitle);
        TextView textView3 = this.mTitleView;
        this.mCollapseContainer = textView3;
        this.mCollapseController.attachViews(textView3);
        boolean z = !TextUtils.isEmpty(this.mTitle);
        int i = 8;
        this.mTitleLayout.setVisibility(z ? 0 : 8);
        TextView textView4 = this.mExpandTitleView;
        if (z) {
            i = 0;
        }
        textView4.setVisibility(i);
        if (this.mTitleLayout.getParent() == null) {
            addView(this.mTitleLayout);
        }
        if (this.mExpandTitleView.getParent() == null) {
            this.mMovableContainer.addView(this.mExpandTitleView);
        }
        if (this.mMovableContainer.getParent() == null) {
            addView(this.mMovableContainer);
        }
        int i2 = this.mInnerExpandState;
        if (i2 == 0) {
            this.mCollapseController.setAnimFrom(1.0f, 0, 0);
            this.mMovableController.setAnimFrom(0.0f, 0, 0);
        } else if (i2 != 1) {
        } else {
            this.mCollapseController.setAnimFrom(0.0f, 0, 20);
            this.mMovableController.setAnimFrom(1.0f, 0, 0);
        }
    }

    public final boolean canTitleBeShown() {
        return (!isResizable() && getExpandState() == 0) || this.mTitleView.getPaint().measureText(this.mTitle.toString()) <= ((float) this.mTitleView.getMeasuredWidth());
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void initForMode(ActionMode actionMode) {
        if (this.mActionMode != null) {
            cancelAnimation();
            killMode();
        }
        initTitle();
        this.mActionMode = new WeakReference<>(actionMode);
        MenuBuilder menuBuilder = (MenuBuilder) actionMode.getMenu();
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus(false);
        }
        ViewParent parent = getParent();
        while (true) {
            View view = (View) parent;
            if (!(view instanceof ActionBarOverlayLayout)) {
                if (view.getParent() instanceof View) {
                    parent = view.getParent();
                } else {
                    throw new IllegalStateException("ActionBarOverlayLayout not found");
                }
            } else {
                ActionMenuPresenter actionMenuPresenter2 = new ActionMenuPresenter(getContext(), (ActionBarOverlayLayout) view, R$layout.miuix_appcompat_action_menu_layout, R$layout.miuix_appcompat_action_mode_menu_item_layout, R$layout.miuix_appcompat_action_bar_expanded_menu_layout, R$layout.miuix_appcompat_action_bar_list_menu_item_layout);
                this.mActionMenuPresenter = actionMenuPresenter2;
                actionMenuPresenter2.setReserveOverflow(true);
                this.mActionMenuPresenter.setActionEditMode(true);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -1);
                if (!this.mSplitActionBar) {
                    menuBuilder.addMenuPresenter(this.mActionMenuPresenter);
                    ActionMenuView actionMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView = actionMenuView;
                    actionMenuView.setBackground(null);
                    addView(this.mMenuView, layoutParams);
                    return;
                }
                this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                layoutParams.width = -1;
                layoutParams.height = -2;
                layoutParams.gravity = 80;
                menuBuilder.addMenuPresenter(this.mActionMenuPresenter);
                ActionMenuView actionMenuView2 = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                this.mMenuView = actionMenuView2;
                actionMenuView2.setBackground(this.mSplitBackground);
                this.mSplitView.addView(this.mMenuView, layoutParams);
                return;
            }
        }
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void closeMode() {
        endAnimation();
        this.mAnimationMode = 2;
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void killMode() {
        removeAllViews();
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            list.clear();
            this.mAnimationListeners = null;
        }
        ActionBarContainer actionBarContainer = this.mSplitView;
        if (actionBarContainer != null) {
            actionBarContainer.removeView(this.mMenuView);
        }
        this.mMenuView = null;
        this.mActionMode = null;
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu(false);
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-1, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    public void updateBackground(boolean z) {
        if (z) {
            clearBackground();
        } else {
            resetBackground();
        }
    }

    public final void resetBackground() {
        ActionMenuView actionMenuView;
        setBackground(this.mActionModeBackground);
        if (!this.mSplitActionBar || (actionMenuView = this.mMenuView) == null) {
            return;
        }
        actionMenuView.setBackground(this.mSplitBackground);
    }

    public final void clearBackground() {
        ActionMenuView actionMenuView;
        setBackground(null);
        if (this.mSplitActionBar && (actionMenuView = this.mMenuView) != null) {
            actionMenuView.setBackground(null);
        }
        FrameLayout frameLayout = this.mMovableContainer;
        if (frameLayout != null) {
            frameLayout.setBackground(null);
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int i4 = this.mContentHeight;
        if (i4 <= 0) {
            i4 = View.MeasureSpec.getSize(i2);
        }
        int paddingTop = getPaddingTop() + getPaddingBottom();
        int paddingLeft = (size - getPaddingLeft()) - getPaddingRight();
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i4 - paddingTop, Integer.MIN_VALUE);
        ActionMenuView actionMenuView = this.mMenuView;
        int i5 = 0;
        if (actionMenuView == null || actionMenuView.getParent() != this) {
            i3 = 0;
        } else {
            paddingLeft = measureChildView(this.mMenuView, paddingLeft, makeMeasureSpec, 0);
            i3 = this.mMenuView.getMeasuredHeight() + 0;
        }
        LinearLayout linearLayout = this.mTitleLayout;
        if (linearLayout != null && linearLayout.getVisibility() != 8) {
            this.mTitleLayout.measure(View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824), makeMeasureSpec);
            i3 += this.mTitleLayout.getMeasuredHeight();
            this.mTitleView.setVisibility(canTitleBeShown() ? 0 : 4);
        }
        int i6 = this.mContentHeight;
        if (i6 <= 0) {
            int childCount = getChildCount();
            int i7 = 0;
            for (int i8 = 0; i8 < childCount; i8++) {
                int measuredHeight = getChildAt(i8).getMeasuredHeight() + paddingTop;
                if (measuredHeight > i7) {
                    i7 = measuredHeight;
                }
            }
            if (i7 > 0) {
                i5 = i7 + this.mContentInset;
            }
            setMeasuredDimension(size, i5);
            return;
        }
        this.mCollapseHeight = i3 > 0 ? i6 + this.mContentInset : 0;
        this.mMovableContainer.measure(View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        int i9 = this.mInnerExpandState;
        if (i9 == 2) {
            setMeasuredDimension(size, this.mCollapseHeight + this.mPendingHeight);
        } else if (i9 == 1) {
            setMeasuredDimension(size, this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight());
        } else {
            setMeasuredDimension(size, this.mCollapseHeight);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredHeight;
        int i5 = this.mInnerExpandState;
        if (i5 == 2) {
            measuredHeight = this.mPendingHeight;
        } else {
            measuredHeight = i5 == 1 ? this.mMovableContainer.getMeasuredHeight() : 0;
        }
        int i6 = i4 - i2;
        onLayoutCollapseViews(i, i2, i3, i4 - measuredHeight);
        onLayoutExpandViews(z, i, i6 - measuredHeight, i3, i6);
        animateLayoutWithProcess((this.mMovableContainer.getMeasuredHeight() - measuredHeight) / this.mMovableContainer.getMeasuredHeight());
    }

    public final void animateLayoutWithProcess(float f) {
        float min = 1.0f - Math.min(1.0f, f * 3.0f);
        int i = this.mInnerExpandState;
        if (i == 2) {
            if (min > 0.0f) {
                this.mCollapseController.animTo(0.0f, 0, 20, this.mCollapseAnimHideConfig);
            } else {
                this.mCollapseController.animTo(1.0f, 0, 0, this.mCollapseAnimShowConfig);
            }
            this.mMovableController.animTo(min, 0, 0, this.mMovableAnimConfig);
        } else if (i == 1) {
            this.mCollapseController.animTo(0.0f, 0, 20, this.mCollapseAnimHideConfig);
            this.mMovableController.animTo(1.0f, 0, 0, this.mMovableAnimConfig);
        } else if (i != 0) {
        } else {
            this.mCollapseController.animTo(1.0f, 0, 0, this.mCollapseAnimShowConfig);
            this.mMovableController.animTo(0.0f, 0, 0, this.mMovableAnimConfig);
        }
    }

    public final void onLayoutCollapseViews(int i, int i2, int i3, int i4) {
        int paddingStart = getPaddingStart();
        int measuredHeight = this.mTitleLayout.getMeasuredHeight();
        int i5 = ((i4 - i2) - measuredHeight) / 2;
        LinearLayout linearLayout = this.mTitleLayout;
        if (linearLayout != null && linearLayout.getVisibility() != 8) {
            positionChild(this.mTitleLayout, paddingStart, i5, measuredHeight);
        }
        int paddingEnd = (i3 - i) - getPaddingEnd();
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null && actionMenuView.getParent() == this) {
            positionChildInverse(this.mMenuView, paddingEnd, i5, measuredHeight);
        }
        if (this.mRequestAnimation) {
            this.mAnimationMode = 1;
            makeInOutAnimator(true).start();
            this.mRequestAnimation = false;
        }
    }

    public void onLayoutExpandViews(boolean z, int i, int i2, int i3, int i4) {
        FrameLayout frameLayout = this.mMovableContainer;
        if (frameLayout == null || frameLayout.getVisibility() != 0 || this.mInnerExpandState == 0) {
            return;
        }
        FrameLayout frameLayout2 = this.mMovableContainer;
        frameLayout2.layout(i, i4 - frameLayout2.getMeasuredHeight(), i3, i4);
        if (ViewUtils.isLayoutRtl(this)) {
            i = i3 - this.mMovableContainer.getMeasuredWidth();
        }
        Rect rect = new Rect();
        rect.set(i, this.mMovableContainer.getMeasuredHeight() - (i4 - i2), this.mMovableContainer.getMeasuredWidth() + i, this.mMovableContainer.getMeasuredHeight());
        this.mMovableContainer.setClipBounds(rect);
    }

    public void setTitleOptional(boolean z) {
        if (z != this.mTitleOptional) {
            requestLayout();
        }
        this.mTitleOptional = z;
    }

    public void cancelAnimation() {
        SpringAnimationSet springAnimationSet = this.mVisibilityAnim;
        if (springAnimationSet != null) {
            springAnimationSet.cancel();
            this.mVisibilityAnim = null;
        }
        setSplitAnimating(false);
    }

    public void endAnimation() {
        SpringAnimationSet springAnimationSet = this.mVisibilityAnim;
        if (springAnimationSet != null) {
            springAnimationSet.endAnimation();
            this.mVisibilityAnim = null;
        }
        setSplitAnimating(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSplitAnimating(boolean z) {
        ActionBarContainer actionBarContainer = this.mSplitView;
        if (actionBarContainer != null) {
            ((ActionBarOverlayLayout) actionBarContainer.getParent()).setAnimating(z);
        }
    }

    public float getAnimationProgress() {
        return this.mAnimationProgress;
    }

    public void setAnimationProgress(float f) {
        this.mAnimationProgress = f;
        notifyAnimationUpdate(this.mAnimateToVisible, f);
    }

    public void makeInOut(boolean z) {
        float f = 1.0f;
        setAlpha(z ? 1.0f : 0.0f);
        if (!this.mSplitActionBar) {
            onFinishStartActionMode(z);
            return;
        }
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) this.mSplitView.getParent();
        int collapsedHeight = this.mMenuView.getCollapsedHeight();
        this.mMenuView.setTranslationY(z ? 0.0f : collapsedHeight);
        if (!z) {
            collapsedHeight = 0;
        }
        actionBarOverlayLayout.animateContentMarginBottom(collapsedHeight);
        ActionMenuView actionMenuView = this.mMenuView;
        if (!z) {
            f = 0.0f;
        }
        actionMenuView.setAlpha(f);
        onFinishStartActionMode(z);
    }

    public final void onFinishStartActionMode(boolean z) {
        ActionMenuView actionMenuView;
        notifyAnimationEnd(z);
        int i = 0;
        setVisibility(z ? 0 : 8);
        if (this.mSplitView == null || (actionMenuView = this.mMenuView) == null) {
            return;
        }
        if (!z) {
            i = 8;
        }
        actionMenuView.setVisibility(i);
    }

    public SpringAnimationSet makeInOutAnimator(final boolean z) {
        float f;
        float f2;
        int i;
        int i2;
        SpringAnimationSet springAnimationSet;
        if (z == this.mAnimateToVisible && this.mVisibilityAnim != null) {
            return new SpringAnimationSet();
        }
        this.mAnimateToVisible = z;
        final ActionMenuView actionMenuView = this.mMenuView;
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) getParent().getParent();
        int collapsedHeight = actionMenuView == null ? 0 : actionMenuView.getCollapsedHeight();
        float translationY = actionMenuView == null ? 0.0f : actionMenuView.getTranslationY();
        if (z) {
            f2 = 0.0f;
            f = 1.0f;
            i2 = 0;
            i = collapsedHeight;
        } else {
            f = 0.0f;
            f2 = 1.0f;
            i = 0;
            i2 = collapsedHeight;
        }
        SpringAnimationSet springAnimationSet2 = new SpringAnimationSet();
        float f3 = 986.96f;
        SpringAnimation viewSpringAnima = getViewSpringAnima(this, z ? 322.27f : 986.96f, f2, f);
        viewSpringAnima.setStartDelay(z ? 50L : 0L);
        springAnimationSet2.play(viewSpringAnima);
        setAlpha(f2);
        if (!this.mSplitActionBar) {
            viewSpringAnima.addEndListener(new DOnAnimationEndListener(z));
            this.mVisibilityAnim = springAnimationSet2;
            return springAnimationSet2;
        }
        this.mAnimateStart = false;
        int i3 = z ? 100 : 0;
        if (z) {
            f3 = 438.65f;
        }
        float f4 = f3;
        final float f5 = translationY;
        final int i4 = i2;
        final int i5 = collapsedHeight;
        final int i6 = i;
        float f6 = f;
        float f7 = f2;
        SpringAnimation springAnimation = new SpringAnimation(actionBarOverlayLayout, new FloatProperty<ActionBarOverlayLayout>("") { // from class: miuix.appcompat.internal.app.widget.ActionBarContextView.2
            @Override // miuix.animation.property.FloatProperty
            public float getValue(ActionBarOverlayLayout actionBarOverlayLayout2) {
                return 0.0f;
            }

            @Override // miuix.animation.property.FloatProperty
            public void setValue(ActionBarOverlayLayout actionBarOverlayLayout2, float f8) {
                ActionMenuView actionMenuView2 = actionMenuView;
                if (actionMenuView2 != null) {
                    actionMenuView2.setTranslationY((f5 + i5) - f8);
                }
                actionBarOverlayLayout2.animateContentMarginBottom((int) f8);
                if (!ActionBarContextView.this.mAnimateStart) {
                    ActionBarContextView.this.notifyAnimationStart(z);
                    ActionBarContextView.this.mAnimateStart = true;
                    return;
                }
                int i7 = i6;
                int i8 = i4;
                ActionBarContextView.this.notifyAnimationUpdate(z, i7 == i8 ? 1.0f : (f8 - i8) / (i7 - i8));
            }
        }, i6);
        float f8 = i4;
        springAnimation.setStartValue(f8);
        springAnimation.getSpring().setStiffness(f4);
        springAnimation.getSpring().setDampingRatio(0.9f);
        long j = i3;
        springAnimation.setStartDelay(j);
        springAnimation.addEndListener(new DOnAnimationEndListener(z));
        if (actionMenuView != null) {
            actionMenuView.setTranslationY((translationY + collapsedHeight) - f8);
        }
        actionBarOverlayLayout.animateContentMarginBottom(i4);
        if (actionMenuView != null) {
            SpringAnimation viewSpringAnima2 = getViewSpringAnima(actionMenuView, f4, f7, f6);
            viewSpringAnima2.setStartDelay(j);
            actionMenuView.setAlpha(f7);
            SpringAnimation[] springAnimationArr = {springAnimation, viewSpringAnima2};
            springAnimationSet = springAnimationSet2;
            springAnimationSet.playTogether(springAnimationArr);
        } else {
            springAnimationSet = springAnimationSet2;
            springAnimationSet.play(springAnimation);
        }
        this.mVisibilityAnim = springAnimationSet;
        return springAnimationSet;
    }

    public final SpringAnimation getViewSpringAnima(View view, float f, float f2, float f3) {
        SpringAnimation springAnimation = new SpringAnimation(view, ViewProperty.ALPHA, f3);
        springAnimation.setStartValue(f2);
        springAnimation.getSpring().setStiffness(f);
        springAnimation.getSpring().setDampingRatio(0.9f);
        springAnimation.setMinimumVisibleChange(0.00390625f);
        return springAnimation;
    }

    public void setButton(int i, CharSequence charSequence) {
        initTitle();
        int i2 = 8;
        if (i == 16908313) {
            Button button = this.mButton1;
            if (button != null) {
                if (!TextUtils.isEmpty(charSequence)) {
                    i2 = 0;
                }
                button.setVisibility(i2);
                this.mButton1.setText(charSequence);
            }
            this.mButton1MenuItem.setTitle(charSequence);
        } else if (i != 16908314) {
        } else {
            Button button2 = this.mButton2;
            if (button2 != null) {
                if (!TextUtils.isEmpty(charSequence)) {
                    i2 = 0;
                }
                button2.setVisibility(i2);
                this.mButton2.setText(charSequence);
            }
            this.mButton2MenuItem.setTitle(charSequence);
        }
    }

    public void setButton(int i, CharSequence charSequence, int i2) {
        initTitle();
        int i3 = 8;
        if (i == 16908313) {
            Button button = this.mButton1;
            if (button != null) {
                if (!TextUtils.isEmpty(charSequence) || i2 != 0) {
                    i3 = 0;
                }
                button.setVisibility(i3);
                this.mButton1.setText(charSequence);
                this.mButton1.setBackgroundResource(i2);
            }
            this.mButton1MenuItem.setTitle(charSequence);
        } else if (i == 16908314) {
            Button button2 = this.mButton2;
            if (button2 != null) {
                if (!TextUtils.isEmpty(charSequence) || i2 != 0) {
                    i3 = 0;
                }
                button2.setVisibility(i3);
                this.mButton2.setText(charSequence);
                this.mButton2.setBackgroundResource(i2);
            }
            this.mButton2MenuItem.setTitle(charSequence);
        }
        if (!TextUtils.isEmpty(charSequence) || i2 == 0) {
            return;
        }
        setButtonContentDescription(i, i2);
    }

    public final void setButtonContentDescription(int i, int i2) {
        Button button;
        if (i == 16908313) {
            button = this.mButton1;
        } else {
            button = i == 16908314 ? this.mButton2 : null;
        }
        if (button == null) {
            return;
        }
        if (R$drawable.miuix_appcompat_action_mode_title_button_cancel_light == i2 || R$drawable.miuix_appcompat_action_mode_title_button_cancel_dark == i2) {
            button.setContentDescription(getResources().getString(R$string.miuix_appcompat_cancel_description));
        } else if (R$drawable.miuix_appcompat_action_mode_title_button_confirm_light == i2 || R$drawable.miuix_appcompat_action_mode_title_button_confirm_dark == i2) {
            button.setContentDescription(getResources().getString(R$string.miuix_appcompat_confirm_description));
        } else if (R$drawable.miuix_appcompat_action_mode_title_button_select_all_light == i2 || R$drawable.miuix_appcompat_action_mode_title_button_select_all_dark == i2) {
            button.setContentDescription(getResources().getString(R$string.miuix_appcompat_select_all_description));
        } else if (R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_light == i2 || R$drawable.miuix_appcompat_action_mode_title_button_deselect_all_dark == i2) {
            button.setContentDescription(getResources().getString(R$string.miuix_appcompat_deselect_all_description));
        } else if (R$drawable.miuix_appcompat_action_mode_title_button_delete_light != i2 && R$drawable.miuix_appcompat_action_mode_title_button_delete_dark != i2) {
        } else {
            button.setContentDescription(getResources().getString(R$string.miuix_appcompat_delete_description));
        }
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void animateToVisibility(boolean z) {
        cancelAnimation();
        setSplitAnimating(this.mStartWithAnim);
        if (z) {
            if (this.mStartWithAnim) {
                setVisibility(0);
                this.mRequestAnimation = true;
                return;
            }
            makeInOut(true);
        } else if (this.mStartWithAnim) {
            makeInOutAnimator(false).start();
        } else {
            makeInOut(false);
        }
    }

    public void notifyAnimationStart(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list == null) {
            return;
        }
        for (ActionModeAnimationListener actionModeAnimationListener : list) {
            actionModeAnimationListener.onStart(z);
        }
    }

    public void notifyAnimationUpdate(boolean z, float f) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list == null) {
            return;
        }
        for (ActionModeAnimationListener actionModeAnimationListener : list) {
            actionModeAnimationListener.onUpdate(z, f);
        }
    }

    public void notifyAnimationEnd(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list == null) {
            return;
        }
        for (ActionModeAnimationListener actionModeAnimationListener : list) {
            actionModeAnimationListener.onStop(z);
        }
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void addAnimationListener(ActionModeAnimationListener actionModeAnimationListener) {
        if (actionModeAnimationListener == null) {
            return;
        }
        if (this.mAnimationListeners == null) {
            this.mAnimationListeners = new ArrayList();
        }
        this.mAnimationListeners.add(actionModeAnimationListener);
    }

    /* loaded from: classes3.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>() { // from class: miuix.appcompat.internal.app.widget.ActionBarContextView.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                if (Build.VERSION.SDK_INT >= 24) {
                    return new SavedState(parcel, classLoader);
                }
                return new SavedState(parcel);
            }
        };
        public CharSequence defaultButtonText;
        public int expandState;
        public boolean isOverflowOpen;
        public CharSequence title;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.isOverflowOpen = parcel.readInt() != 0;
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.defaultButtonText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.expandState = parcel.readInt();
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.isOverflowOpen = parcel.readInt() != 0;
            this.title = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.defaultButtonText = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.expandState = parcel.readInt();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.isOverflowOpen ? 1 : 0);
            TextUtils.writeToParcel(this.title, parcel, 0);
            TextUtils.writeToParcel(this.defaultButtonText, parcel, 0);
            parcel.writeInt(this.expandState);
        }
    }

    /* loaded from: classes3.dex */
    public class DOnAnimationEndListener implements DynamicAnimation.OnAnimationEndListener {
        public boolean mFinalVisibility;

        public DOnAnimationEndListener(boolean z) {
            this.mFinalVisibility = z;
        }

        @Override // miuix.animation.physics.DynamicAnimation.OnAnimationEndListener
        public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            ActionMenuView actionMenuView;
            int i = 0;
            ActionBarContextView.this.setSplitAnimating(false);
            ActionBarContextView.this.mAnimateStart = false;
            ActionBarContextView.this.notifyAnimationEnd(this.mFinalVisibility);
            if (ActionBarContextView.this.mAnimationMode == 2) {
                ActionBarContextView.this.killMode();
            }
            ActionBarContextView.this.mAnimationMode = 0;
            ActionBarContextView.this.mVisibilityAnim = null;
            ActionBarContextView.this.setVisibility(this.mFinalVisibility ? 0 : 8);
            ActionBarContextView actionBarContextView = ActionBarContextView.this;
            if (actionBarContextView.mSplitView == null || (actionMenuView = actionBarContextView.mMenuView) == null) {
                return;
            }
            if (!this.mFinalVisibility) {
                i = 8;
            }
            actionMenuView.setVisibility(i);
        }
    }

    @Override // miuix.appcompat.internal.app.widget.AbsActionBarView
    public void onExpandStateChanged(int i, int i2) {
        if (i == 2) {
            this.mPendingHeight = 0;
            if (!this.mPostScroller.isFinished()) {
                this.mPostScroller.forceFinished(true);
            }
        }
        if (i2 != 0) {
            this.mMovableContainer.setVisibility(0);
        }
        if (i2 == 0) {
            this.mMovableContainer.setVisibility(8);
        } else {
            this.mPendingHeight = getHeight() - this.mCollapseHeight;
        }
    }

    public void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr, int[] iArr2) {
        if (i4 >= 0 || getHeight() >= this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight()) {
            return;
        }
        int i6 = this.mPendingHeight;
        if (getHeight() - i4 <= this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight()) {
            this.mPendingHeight -= i4;
            iArr[1] = iArr[1] + i4;
        } else {
            int measuredHeight = (this.mCollapseHeight + this.mMovableContainer.getMeasuredHeight()) - getHeight();
            this.mPendingHeight = this.mMovableContainer.getMeasuredHeight();
            iArr[1] = iArr[1] + (-measuredHeight);
        }
        int i7 = this.mPendingHeight;
        if (i7 == i6) {
            return;
        }
        iArr2[1] = i6 - i7;
        requestLayout();
    }

    public boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        if (getContext().getResources().getConfiguration().orientation != 2 || DeviceHelper.isTablet(getContext())) {
            return isResizable();
        }
        return false;
    }

    public void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        if (i2 == 0) {
            this.mTouchScrolling = true;
        } else {
            this.mNonTouchScrolling = true;
        }
        if (!this.mPostScroller.isFinished()) {
            this.mPostScroller.forceFinished(true);
        }
        setExpandState(2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x000a, code lost:
        if (r3.mNonTouchScrolling == false) goto L25;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onStopNestedScroll(android.view.View r4, int r5) {
        /*
            r3 = this;
            boolean r4 = r3.mTouchScrolling
            r5 = 1
            r0 = 0
            if (r4 == 0) goto Ld
            r3.mTouchScrolling = r0
            boolean r4 = r3.mNonTouchScrolling
            if (r4 != 0) goto L15
            goto L13
        Ld:
            boolean r4 = r3.mNonTouchScrolling
            if (r4 == 0) goto L15
            r3.mNonTouchScrolling = r0
        L13:
            r4 = r5
            goto L16
        L15:
            r4 = r0
        L16:
            if (r4 == 0) goto L7f
            int r4 = r3.getHeight()
            int r1 = r3.mCollapseHeight
            if (r4 != r1) goto L24
            r3.setExpandState(r0)
            return
        L24:
            int r4 = r3.getHeight()
            int r1 = r3.mCollapseHeight
            android.widget.FrameLayout r2 = r3.mMovableContainer
            int r2 = r2.getMeasuredHeight()
            int r1 = r1 + r2
            if (r4 != r1) goto L41
            int r4 = r3.mPendingHeight
            android.widget.FrameLayout r1 = r3.mMovableContainer
            int r1 = r1.getMeasuredHeight()
            if (r4 != r1) goto L41
            r3.setExpandState(r5)
            return
        L41:
            int r4 = r3.getHeight()
            int r5 = r3.mCollapseHeight
            android.widget.FrameLayout r1 = r3.mMovableContainer
            int r1 = r1.getMeasuredHeight()
            int r1 = r1 / 2
            int r5 = r5 + r1
            if (r4 <= r5) goto L6a
            android.widget.Scroller r4 = r3.mPostScroller
            int r5 = r3.getHeight()
            int r1 = r3.mCollapseHeight
            android.widget.FrameLayout r2 = r3.mMovableContainer
            int r2 = r2.getMeasuredHeight()
            int r1 = r1 + r2
            int r2 = r3.getHeight()
            int r1 = r1 - r2
            r4.startScroll(r0, r5, r0, r1)
            goto L7a
        L6a:
            android.widget.Scroller r4 = r3.mPostScroller
            int r5 = r3.getHeight()
            int r1 = r3.mCollapseHeight
            int r2 = r3.getHeight()
            int r1 = r1 - r2
            r4.startScroll(r0, r5, r0, r1)
        L7a:
            java.lang.Runnable r4 = r3.mPostScroll
            r3.postOnAnimation(r4)
        L7f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.appcompat.internal.app.widget.ActionBarContextView.onStopNestedScroll(android.view.View, int):void");
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3, int[] iArr2) {
        if (i2 <= 0 || getHeight() <= this.mCollapseHeight) {
            return;
        }
        int height = getHeight() - i2;
        int i4 = this.mPendingHeight;
        int i5 = this.mCollapseHeight;
        if (height >= i5) {
            this.mPendingHeight = i4 - i2;
            iArr[1] = iArr[1] + i2;
        } else {
            int height2 = i5 - getHeight();
            this.mPendingHeight = 0;
            iArr[1] = iArr[1] + (-height2);
        }
        int i6 = this.mPendingHeight;
        if (i6 == i4) {
            return;
        }
        iArr2[1] = i4 - i6;
        requestLayout();
    }
}
