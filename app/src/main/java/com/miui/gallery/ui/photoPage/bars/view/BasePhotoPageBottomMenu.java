package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import com.android.internal.WindowInsetsCompat;
import com.miui.gallery.R;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.PhotoPageActionBarManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.menuitem.More;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.video.VideoFrameSeekBar;
import com.miui.gallery.view.menu.ActionMenuItemView;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuBuilder;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public abstract class BasePhotoPageBottomMenu extends AbstractPhotoPageMenu {
    public BottomMenuAnimController mBottomActionsAnimController;
    public int mBottomBtnWidth;
    public GradientDrawable mDividerDrawable;
    public int mDividerWidth;
    public int mInsetBottom;
    public float mMoreActionsLimitHeight;
    public float mMoreActionsMaxHeight;
    public int mOriginBottomPadding;
    public int mOriginItemCount;
    public ViewGroup mRoot;
    public float mScreenWidth;
    public LinearLayout vBottomMenus;
    public FrameLayout vFrameBar;
    public ActionMenuItemView vMoreActionButton;

    public static /* synthetic */ boolean $r8$lambda$BrTr8s69lbfbFUlmRLDM8FmoQ3s(BasePhotoPageBottomMenu basePhotoPageBottomMenu, IMenuItemDelegate iMenuItemDelegate, IMenuItem iMenuItem) {
        return basePhotoPageBottomMenu.lambda$reAddResidentMenuItems$0(iMenuItemDelegate, iMenuItem);
    }

    public abstract void ensureMoreView();

    public BasePhotoPageBottomMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IViewProvider iViewProvider, IMenuItemDelegate.ClickHelper clickHelper) {
        super(iPhotoPageMenuManager, context, iViewProvider, clickHelper);
        addView(iViewProvider.getBottomMenuView(null), new ViewGroup.LayoutParams(-1, -1));
        initView();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void addRootLayout(ViewGroup viewGroup) {
        if (this.mRoot == null) {
            this.mRoot = viewGroup;
            viewGroup.addView(this);
        }
    }

    public void initView() {
        this.vFrameBar = (FrameLayout) findViewById(R.id.view_frame);
        this.vBottomMenus = (LinearLayout) findViewById(R.id.layout_menu);
        updateAlphaBackgroundDrawable();
        this.mOriginBottomPadding = this.vBottomMenus.getPaddingBottom();
        this.mBottomBtnWidth = getResources().getDimensionPixelSize(R.dimen.photo_page_menu_button_width);
        this.mDividerWidth = getResources().getDimensionPixelSize(R.dimen.photo_page_menu_button_padding);
        this.mScreenWidth = ScreenUtils.getCurScreenWidth();
        addDivider();
        this.mMoreActionsLimitHeight = ScreenUtils.dpToPixel(getContext().getResources().getConfiguration().screenHeightDp) - PhotoPageActionBarManager.getStationaryActionBarHeight();
        this.mBottomActionsAnimController = new BottomMenuAnimController();
    }

    public void updateAlphaBackgroundDrawable() {
        if (this.mMenuManager.isVideoPlayerSupportActionBarAdjust()) {
            this.vBottomMenus.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.action_bar_blur_background_on_low_ram_devices));
        } else {
            this.vBottomMenus.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.action_bar_background));
        }
    }

    public void addDivider() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        this.mDividerDrawable = gradientDrawable;
        gradientDrawable.setSize(this.mDividerWidth, -1);
        this.vBottomMenus.setDividerDrawable(this.mDividerDrawable);
        this.vBottomMenus.setShowDividers(2);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public void relayoutForItemsChanged(ArrayList<IMenuItemDelegate> arrayList) {
        super.relayoutForItemsChanged(arrayList);
        int size = arrayList.size();
        int bottomMenuWidth = getBottomMenuWidth();
        if (size <= 0 || this.mOriginItemCount == size) {
            return;
        }
        int i = ((bottomMenuWidth - (this.mBottomBtnWidth * size)) - (this.mDividerWidth * (size - 1))) / 2;
        LinearLayout linearLayout = this.vBottomMenus;
        linearLayout.setPadding(i, linearLayout.getPaddingTop(), i, this.vBottomMenus.getPaddingBottom());
        this.mOriginItemCount = size;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void relayoutForConfigChanged(float f) {
        this.mScreenWidth = f;
        this.mBottomBtnWidth = getResources().getDimensionPixelSize(R.dimen.photo_page_menu_button_width);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.photo_page_menu_button_padding);
        this.mDividerWidth = dimensionPixelSize;
        int i = this.mBottomBtnWidth;
        int i2 = this.mOriginItemCount;
        int i3 = (int) (((f - (i * i2)) - (dimensionPixelSize * (i2 - 1))) / 2.0f);
        LinearLayout linearLayout = this.vBottomMenus;
        linearLayout.setPadding(i3, linearLayout.getPaddingTop(), i3, this.vBottomMenus.getPaddingBottom());
        addDivider();
    }

    private int getBottomMenuWidth() {
        int measuredWidth = this.vBottomMenus.getMeasuredWidth();
        return measuredWidth == 0 ? (int) this.mScreenWidth : measuredWidth;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public void removeResidentMenuItems() {
        if (this.vBottomMenus.getChildCount() > 0) {
            this.vBottomMenus.removeAllViews();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public boolean residentCountChanged(int i) {
        return this.vBottomMenus.getChildCount() != i;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void reAddResidentMenuItems(ArrayList<IMenuItemDelegate> arrayList) {
        if (arrayList == null) {
            return;
        }
        removeResidentMenuItems();
        Iterator<IMenuItemDelegate> it = arrayList.iterator();
        while (it.hasNext()) {
            final IMenuItemDelegate next = it.next();
            ActionMenuItemView actionMenuItemView = (ActionMenuItemView) this.mViewProvider.getBottomItemView(this.vBottomMenus, next instanceof More);
            actionMenuItemView.initialize(next.getItemDataState(), 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            actionMenuItemView.setItemInvoker(new MenuBuilder.ItemInvoker() { // from class: com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.view.menu.MenuBuilder.ItemInvoker
                public final boolean invokeItem(IMenuItem iMenuItem) {
                    return BasePhotoPageBottomMenu.$r8$lambda$BrTr8s69lbfbFUlmRLDM8FmoQ3s(BasePhotoPageBottomMenu.this, next, iMenuItem);
                }
            });
            setViewAccessibilityDelegate(actionMenuItemView);
            actionMenuItemView.setAccessibilityTraversalAfter(R.id.lock_btn);
            this.vBottomMenus.addView(actionMenuItemView, layoutParams);
            if (next instanceof More) {
                this.vMoreActionButton = actionMenuItemView;
            }
        }
    }

    public /* synthetic */ boolean lambda$reAddResidentMenuItems$0(IMenuItemDelegate iMenuItemDelegate, IMenuItem iMenuItem) {
        IMenuItemDelegate.ClickHelper clickHelper = this.mItemClickHelper;
        if (clickHelper == null) {
            return false;
        }
        clickHelper.onMenuItemClick(iMenuItemDelegate);
        return true;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void viewRequestFocus(IMenuItemDelegate iMenuItemDelegate) {
        int order;
        ActionMenuItemView actionMenuItemView;
        if (iMenuItemDelegate != null && iMenuItemDelegate.isResident() && this.vBottomMenus.getChildCount() > (order = iMenuItemDelegate.getOrder()) && (actionMenuItemView = (ActionMenuItemView) this.vBottomMenus.getChildAt(order)) != null) {
            this.mMenuManager.setCurrentFocusView(actionMenuItemView);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void refreshMenuItem(IMenuItemDelegate iMenuItemDelegate) {
        int order;
        ActionMenuItemView actionMenuItemView;
        if (iMenuItemDelegate != null && iMenuItemDelegate.isResident() && this.vBottomMenus.getChildCount() > (order = iMenuItemDelegate.getOrder()) && (actionMenuItemView = (ActionMenuItemView) this.vBottomMenus.getChildAt(order)) != null) {
            actionMenuItemView.initialize(iMenuItemDelegate.getItemDataState(), 0);
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mBaseInnerInsets.set(ViewCompat.getSystemWindowInsets(this));
        int i = (!isLayoutHideNavigation() || (!this.mMenuManager.isInMultiWindowMode() && WindowInsetsCompat.shouldAlwaysConsumeSystemBars(this))) ? 0 : this.mBaseInnerInsets.bottom;
        if (this.mInsetBottom != i) {
            this.mInsetBottom = i;
            requestLayout();
            layoutForWindowInsets();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public void layoutForWindowInsets() {
        LinearLayout linearLayout = this.vBottomMenus;
        if (linearLayout != null) {
            linearLayout.setPadding(linearLayout.getPaddingLeft(), this.vBottomMenus.getPaddingTop(), this.vBottomMenus.getPaddingRight(), this.mOriginBottomPadding + this.mInsetBottom);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void setFrameBar(VideoFrameSeekBar videoFrameSeekBar) {
        this.vFrameBar.addView(videoFrameSeekBar);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void toggleMoreActions(boolean z) {
        if (this.isMoreActionsShowing) {
            hideMoreActions(z);
        } else {
            showMoreActions(z);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void showMoreActions(boolean z) {
        if (this.isMoreActionsShowing) {
            return;
        }
        ensureMoreView();
        this.isMoreActionsShowing = true;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void hideMoreActions(boolean z) {
        if (!this.isMoreActionsShowing) {
            return;
        }
        this.isMoreActionsShowing = false;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void refreshMoreActionsMaxHeight(float f) {
        if (f <= 0.0f) {
            return;
        }
        this.mMoreActionsMaxHeight = f;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void showMenuView(boolean z) {
        this.mBottomActionsAnimController.show(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void hideMenuView(boolean z) {
        this.mBottomActionsAnimController.hide(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public int getMenuCollapsedHeight() {
        return this.mBottomActionsAnimController.getMenuBarHeight() - this.mInsetBottom;
    }

    /* loaded from: classes2.dex */
    public class BottomMenuAnimController {
        public IStateStyle mCurrentState;
        public final BottomMenuWeakShowBottomMenuListener mWeakShowBottomMenuListener;

        public BottomMenuAnimController() {
            BasePhotoPageBottomMenu.this = r2;
            this.mWeakShowBottomMenuListener = new BottomMenuWeakShowBottomMenuListener(r2.mMenuManager);
        }

        public void show(boolean z) {
            BasePhotoPageBottomMenu.this.vBottomMenus.setVisibility(0);
            BasePhotoPageBottomMenu.this.vFrameBar.setVisibility(0);
            if (z) {
                exeAnim(true);
                BasePhotoPageBottomMenu basePhotoPageBottomMenu = BasePhotoPageBottomMenu.this;
                if (basePhotoPageBottomMenu.isMoreActionsShowing) {
                    return;
                }
                basePhotoPageBottomMenu.vBottomMenus.startLayoutAnimation();
                return;
            }
            BasePhotoPageBottomMenu.this.vBottomMenus.setTranslationY(0.0f);
            BasePhotoPageBottomMenu.this.vBottomMenus.setAlpha(1.0f);
        }

        public void hide(boolean z) {
            if (z) {
                exeAnim(false);
                return;
            }
            BasePhotoPageBottomMenu.this.vBottomMenus.setTranslationY(getMenuBarHeight() + BasePhotoPageBottomMenu.this.mInsetBottom);
            BasePhotoPageBottomMenu.this.vBottomMenus.setAlpha(0.0f);
            BasePhotoPageBottomMenu.this.vBottomMenus.setVisibility(8);
            BasePhotoPageBottomMenu.this.vFrameBar.setVisibility(8);
        }

        public final void exeAnim(boolean z) {
            IStateStyle iStateStyle;
            if (z) {
                AnimConfig animConfig = new AnimConfig();
                animConfig.addListeners(this.mWeakShowBottomMenuListener);
                animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
                AnimState animState = new AnimState("ShowBottomMenu");
                ViewProperty viewProperty = ViewProperty.TRANSLATION_Y;
                AnimState add = animState.add(viewProperty, SearchStatUtils.POW);
                ViewProperty viewProperty2 = ViewProperty.ALPHA;
                AnimState add2 = add.add(viewProperty2, 1.0d);
                BasePhotoPageBottomMenu basePhotoPageBottomMenu = BasePhotoPageBottomMenu.this;
                IStateStyle state = Folme.useAt(basePhotoPageBottomMenu.vBottomMenus, basePhotoPageBottomMenu.vFrameBar).state();
                IStateStyle iStateStyle2 = this.mCurrentState;
                if (iStateStyle2 == null) {
                    state.setTo(new AnimState("HideBottomMenu").add(viewProperty, BasePhotoPageBottomMenu.this.getResources().getDimension(R.dimen.action_bar_default_height)).add(viewProperty2, SearchStatUtils.POW));
                } else {
                    state = state.setTo(iStateStyle2);
                }
                iStateStyle = state.to(add2, animConfig);
            } else {
                int menuBarHeight = getMenuBarHeight();
                if (menuBarHeight == 0) {
                    return;
                }
                AnimConfig animConfig2 = new AnimConfig();
                animConfig2.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
                BasePhotoPageBottomMenu basePhotoPageBottomMenu2 = BasePhotoPageBottomMenu.this;
                AnimState add3 = new AnimState("HideBottomMenu").add(ViewProperty.TRANSLATION_Y, menuBarHeight + basePhotoPageBottomMenu2.mInsetBottom + basePhotoPageBottomMenu2.vFrameBar.getHeight()).add(ViewProperty.ALPHA, SearchStatUtils.POW);
                BasePhotoPageBottomMenu basePhotoPageBottomMenu3 = BasePhotoPageBottomMenu.this;
                IStateStyle state2 = Folme.useAt(basePhotoPageBottomMenu3.vBottomMenus, basePhotoPageBottomMenu3.vFrameBar).state();
                IStateStyle iStateStyle3 = this.mCurrentState;
                if (iStateStyle3 != null) {
                    state2 = state2.setTo(iStateStyle3);
                }
                iStateStyle = state2.to(add3, animConfig2);
            }
            this.mCurrentState = iStateStyle;
        }

        public final int getMenuBarHeight() {
            return BasePhotoPageBottomMenu.this.vBottomMenus.getHeight();
        }
    }

    /* loaded from: classes2.dex */
    public static class BottomMenuWeakShowBottomMenuListener extends TransitionListener {
        public final WeakReference<IPhotoPageMenuManager> mMenuManager;

        public BottomMenuWeakShowBottomMenuListener(IPhotoPageMenuManager iPhotoPageMenuManager) {
            this.mMenuManager = new WeakReference<>(iPhotoPageMenuManager);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            if (this.mMenuManager.get() == null) {
                return;
            }
            this.mMenuManager.get().onMenuActionsShown();
        }
    }
}
