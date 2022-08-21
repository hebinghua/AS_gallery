package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.android.internal.WindowInsetsCompat;
import com.miui.gallery.R;
import com.miui.gallery.compat.view.ViewCompat;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.menuitem.More;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.video.VideoFrameSeekBar;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuBuilder;
import com.miui.gallery.widget.menu.ImmersionMenuItemView;
import com.miui.gallery.widget.menu.TopMenuBarImmersionMenuItemView;
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
import miuix.internal.widget.ListPopup;

/* loaded from: classes2.dex */
public class PhotoPageTopMenu extends AbstractPhotoPageMenu {
    public AnimState mInitState;
    public int mInsetBottom;
    public MoreActionsAdapter mMoreActionsAdapter;
    public final int mOriginBottomPadding;
    public ViewGroup mRoot;
    public final TopMenuAnimController mTopActionsAnimController;
    public MoreActions vMoreAction;
    public ImmersionMenuItemView vMoreActionButton;
    public ViewGroup vSeekBarLayout;

    public static /* synthetic */ void $r8$lambda$P8XRJP74nC5SLd2ENCCDpaI_byM(PhotoPageTopMenu photoPageTopMenu, AdapterView adapterView, View view, int i, long j) {
        photoPageTopMenu.lambda$ensureMoreView$2(adapterView, view, i, j);
    }

    public static /* synthetic */ boolean $r8$lambda$kKNBb3JGdJ4WvG3r_UUQximLYfU(PhotoPageTopMenu photoPageTopMenu, IMenuItemDelegate iMenuItemDelegate, IMenuItem iMenuItem) {
        return photoPageTopMenu.lambda$reAddResidentMenuItems$0(iMenuItemDelegate, iMenuItem);
    }

    public static /* synthetic */ void $r8$lambda$m4OzevrFhHUhwD2P8tH2Pfwljdw(PhotoPageTopMenu photoPageTopMenu) {
        photoPageTopMenu.lambda$ensureMoreView$1();
    }

    public PhotoPageTopMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IViewProvider iViewProvider, IMenuItemDelegate.ClickHelper clickHelper) {
        super(iPhotoPageMenuManager, context, iViewProvider, clickHelper);
        setOrientation(0);
        this.mTopActionsAnimController = new TopMenuAnimController();
        FrameLayout frameLayout = new FrameLayout(getContext());
        this.vSeekBarLayout = frameLayout;
        frameLayout.setVisibility(8);
        this.mOriginBottomPadding = this.vSeekBarLayout.getPaddingBottom();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void addRootLayout(ViewGroup viewGroup) {
        if (this.mRoot == null) {
            this.mRoot = viewGroup;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
            layoutParams.gravity = 81;
            this.mRoot.addView(this.vSeekBarLayout, layoutParams);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public void removeResidentMenuItems() {
        if (getChildCount() > 0) {
            removeAllViews();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public boolean residentCountChanged(int i) {
        return getChildCount() != i;
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
            TopMenuBarImmersionMenuItemView topMenuBarImmersionMenuItemView = (TopMenuBarImmersionMenuItemView) this.mViewProvider.getTopItemView(this);
            topMenuBarImmersionMenuItemView.initialize(next.getItemDataState(), 0);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
            layoutParams.weight = 1.0f;
            topMenuBarImmersionMenuItemView.setItemInvoker(new MenuBuilder.ItemInvoker() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenu$$ExternalSyntheticLambda2
                @Override // com.miui.gallery.view.menu.MenuBuilder.ItemInvoker
                public final boolean invokeItem(IMenuItem iMenuItem) {
                    return PhotoPageTopMenu.$r8$lambda$kKNBb3JGdJ4WvG3r_UUQximLYfU(PhotoPageTopMenu.this, next, iMenuItem);
                }
            });
            setViewAccessibilityDelegate(topMenuBarImmersionMenuItemView);
            addView(topMenuBarImmersionMenuItemView, layoutParams);
            if (next instanceof More) {
                this.vMoreActionButton = topMenuBarImmersionMenuItemView;
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
        ImmersionMenuItemView immersionMenuItemView;
        if (iMenuItemDelegate != null && iMenuItemDelegate.isResident() && getChildCount() > (order = iMenuItemDelegate.getOrder()) && (immersionMenuItemView = (ImmersionMenuItemView) getChildAt(order)) != null) {
            this.mMenuManager.setCurrentFocusView(immersionMenuItemView);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public void refreshAllNonResidentItems() {
        MoreActionsAdapter moreActionsAdapter;
        if (!this.isMoreActionsShowing || (moreActionsAdapter = this.mMoreActionsAdapter) == null) {
            return;
        }
        moreActionsAdapter.notifyDataSetChanged();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void refreshMenuItem(IMenuItemDelegate iMenuItemDelegate) {
        ImmersionMenuItemView immersionMenuItemView;
        if (iMenuItemDelegate == null) {
            return;
        }
        if (iMenuItemDelegate.isResident()) {
            int order = iMenuItemDelegate.getOrder();
            if (getChildCount() <= order || (immersionMenuItemView = (ImmersionMenuItemView) getChildAt(order)) == null) {
                return;
            }
            immersionMenuItemView.initialize(iMenuItemDelegate.getItemDataState(), 0);
        } else if (!this.isMoreActionsShowing) {
        } else {
            this.mMoreActionsAdapter.notifyDataSetChanged();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void setFrameBar(VideoFrameSeekBar videoFrameSeekBar) {
        this.vSeekBarLayout.addView(videoFrameSeekBar);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void showMenuView(boolean z) {
        this.mTopActionsAnimController.show(z);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void hideMenuView(boolean z) {
        this.mTopActionsAnimController.hide(z);
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
        if (this.isMoreActionsShowing || this.vMoreActionButton == null || !this.mMenuManager.isActivityActive()) {
            return;
        }
        ensureMoreView();
        this.vMoreAction.show(this.vMoreActionButton, null);
        this.isMoreActionsShowing = true;
        ImmersionMenuItemView immersionMenuItemView = this.vMoreActionButton;
        if (immersionMenuItemView == null) {
            return;
        }
        immersionMenuItemView.setSelected(true);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void hideMoreActions(boolean z) {
        if (!this.isMoreActionsShowing || this.vMoreAction == null || !this.mMenuManager.isActivityActive()) {
            return;
        }
        this.vMoreAction.dismiss();
        this.isMoreActionsShowing = false;
        ImmersionMenuItemView immersionMenuItemView = this.vMoreActionButton;
        if (immersionMenuItemView == null) {
            return;
        }
        immersionMenuItemView.setSelected(false);
    }

    public final void ensureMoreView() {
        if (this.vMoreAction == null) {
            this.vMoreAction = new MoreActions(getContext());
            MoreActionsAdapter moreActionsAdapter = new MoreActionsAdapter(this.mNonResident, this.mViewProvider);
            this.mMoreActionsAdapter = moreActionsAdapter;
            this.vMoreAction.setAdapter(moreActionsAdapter);
            this.vMoreAction.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenu$$ExternalSyntheticLambda1
                @Override // android.widget.PopupWindow.OnDismissListener
                public final void onDismiss() {
                    PhotoPageTopMenu.$r8$lambda$m4OzevrFhHUhwD2P8tH2Pfwljdw(PhotoPageTopMenu.this);
                }
            });
            this.vMoreAction.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.PhotoPageTopMenu$$ExternalSyntheticLambda0
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    PhotoPageTopMenu.$r8$lambda$P8XRJP74nC5SLd2ENCCDpaI_byM(PhotoPageTopMenu.this, adapterView, view, i, j);
                }
            });
            return;
        }
        this.mMoreActionsAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$ensureMoreView$1() {
        this.isMoreActionsShowing = false;
        ImmersionMenuItemView immersionMenuItemView = this.vMoreActionButton;
        if (immersionMenuItemView != null) {
            immersionMenuItemView.setSelected(false);
        }
    }

    public /* synthetic */ void lambda$ensureMoreView$2(AdapterView adapterView, View view, int i, long j) {
        this.vMoreAction.dismiss();
        this.mItemClickHelper.onMenuItemClick(this.mNonResident.get(i));
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mBaseInnerInsets.set(ViewCompat.getSystemWindowInsets(this));
        int i = 0;
        if ((getResources().getConfiguration().orientation == 2 && BaseBuildUtil.isLargeScreen(getContext())) || (isLayoutHideNavigation() && getResources().getConfiguration().orientation == 1 && !WindowInsetsCompat.shouldAlwaysConsumeSystemBars(this))) {
            i = this.mBaseInnerInsets.bottom;
        }
        if (this.mInsetBottom != i) {
            this.mInsetBottom = i;
            requestLayout();
            layoutForWindowInsets();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public void layoutForWindowInsets() {
        ViewGroup viewGroup = this.vSeekBarLayout;
        if (viewGroup != null) {
            viewGroup.setPadding(viewGroup.getPaddingLeft(), this.vSeekBarLayout.getPaddingTop(), this.vSeekBarLayout.getPaddingRight(), this.mOriginBottomPadding + this.mInsetBottom);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public int getMenuCollapsedHeight() {
        ViewGroup viewGroup = this.vSeekBarLayout;
        if (viewGroup != null) {
            return viewGroup.getHeight();
        }
        return 0;
    }

    /* loaded from: classes2.dex */
    public static class WeakShowTopMenuListener extends TransitionListener {
        public WeakReference<IPhotoPageMenuManager> mMenuManager;

        public WeakShowTopMenuListener(IPhotoPageMenuManager iPhotoPageMenuManager) {
            this.mMenuManager = new WeakReference<>(iPhotoPageMenuManager);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            WeakReference<IPhotoPageMenuManager> weakReference = this.mMenuManager;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.mMenuManager.get().onMenuActionsShown();
        }

        public final void release() {
            WeakReference<IPhotoPageMenuManager> weakReference = this.mMenuManager;
            if (weakReference != null) {
                weakReference.clear();
                this.mMenuManager = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public class TopMenuAnimController {
        public IStateStyle mCurrentState;
        public WeakShowTopMenuListener mWeakShowTopMenuListener;

        public TopMenuAnimController() {
            PhotoPageTopMenu.this = r1;
        }

        public void show(boolean z) {
            PhotoPageTopMenu.this.vSeekBarLayout.setVisibility(0);
            if (!z) {
                PhotoPageTopMenu.this.vSeekBarLayout.setTranslationY(0.0f);
                PhotoPageTopMenu.this.vSeekBarLayout.setAlpha(1.0f);
                return;
            }
            exeAnim(true);
            PhotoPageTopMenu photoPageTopMenu = PhotoPageTopMenu.this;
            if (photoPageTopMenu.isMoreActionsShowing) {
                return;
            }
            photoPageTopMenu.vSeekBarLayout.startLayoutAnimation();
        }

        public void hide(boolean z) {
            if (!z) {
                PhotoPageTopMenu.this.vSeekBarLayout.setTranslationY(getMenuBarHeight() + PhotoPageTopMenu.this.mInsetBottom);
                PhotoPageTopMenu.this.vSeekBarLayout.setAlpha(0.0f);
                PhotoPageTopMenu.this.vSeekBarLayout.setVisibility(8);
                return;
            }
            exeAnim(false);
        }

        public final void exeAnim(boolean z) {
            if (z) {
                AnimConfig animConfig = new AnimConfig();
                ensureListener();
                animConfig.addListeners(this.mWeakShowTopMenuListener);
                animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
                AnimState animState = new AnimState("ShowBottomMenu");
                ViewProperty viewProperty = ViewProperty.TRANSLATION_Y;
                AnimState add = animState.add(viewProperty, SearchStatUtils.POW);
                ViewProperty viewProperty2 = ViewProperty.ALPHA;
                AnimState add2 = add.add(viewProperty2, 1.0d);
                IStateStyle state = Folme.useAt(PhotoPageTopMenu.this.vSeekBarLayout).state();
                if (PhotoPageTopMenu.this.mInitState == null) {
                    PhotoPageTopMenu.this.mInitState = new AnimState("HideBottomMenu").add(viewProperty, PhotoPageTopMenu.this.getResources().getDimension(R.dimen.action_bar_default_height)).add(viewProperty2, SearchStatUtils.POW);
                }
                IStateStyle iStateStyle = this.mCurrentState;
                if (iStateStyle == null) {
                    state.setTo(PhotoPageTopMenu.this.mInitState);
                } else if (iStateStyle.getCurrentState().getFloat(viewProperty) == 0.0f) {
                    state = state.setTo(PhotoPageTopMenu.this.mInitState);
                } else {
                    state = state.setTo(this.mCurrentState);
                }
                this.mCurrentState = state.to(add2, animConfig);
                return;
            }
            int menuBarHeight = getMenuBarHeight();
            if (menuBarHeight == 0) {
                return;
            }
            AnimConfig animConfig2 = new AnimConfig();
            animConfig2.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
            AnimState add3 = new AnimState("HideBottomMenu").add(ViewProperty.TRANSLATION_Y, menuBarHeight + PhotoPageTopMenu.this.mInsetBottom).add(ViewProperty.ALPHA, SearchStatUtils.POW);
            IStateStyle state2 = Folme.useAt(PhotoPageTopMenu.this.vSeekBarLayout).state();
            IStateStyle iStateStyle2 = this.mCurrentState;
            if (iStateStyle2 != null) {
                state2 = state2.setTo(iStateStyle2);
            }
            this.mCurrentState = state2.to(add3, animConfig2);
        }

        public final int getMenuBarHeight() {
            return PhotoPageTopMenu.this.vSeekBarLayout.getHeight();
        }

        public final void release() {
            WeakShowTopMenuListener weakShowTopMenuListener = this.mWeakShowTopMenuListener;
            if (weakShowTopMenuListener != null) {
                weakShowTopMenuListener.release();
                this.mWeakShowTopMenuListener = null;
            }
        }

        public void ensureListener() {
            if (this.mWeakShowTopMenuListener == null) {
                this.mWeakShowTopMenuListener = new WeakShowTopMenuListener(PhotoPageTopMenu.this.mMenuManager);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        TopMenuAnimController topMenuAnimController = this.mTopActionsAnimController;
        if (topMenuAnimController != null) {
            topMenuAnimController.release();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu, android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        TopMenuAnimController topMenuAnimController = this.mTopActionsAnimController;
        if (topMenuAnimController != null) {
            topMenuAnimController.ensureListener();
        }
    }

    /* loaded from: classes2.dex */
    public static class MoreActions extends ListPopup {
        public MoreActions(Context context) {
            super(context);
            setMaxAllowedHeight(context.getResources().getDimensionPixelOffset(R.dimen.miuix_appcompat_menu_popup_max_height));
        }
    }
}
