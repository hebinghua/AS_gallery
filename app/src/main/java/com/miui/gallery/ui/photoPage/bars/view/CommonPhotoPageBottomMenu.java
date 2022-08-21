package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.bars.view.CommonPhotoPageBottomMenu;
import com.miui.gallery.view.menu.ActionMenuItemView;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.ListMenuItemView;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class CommonPhotoPageBottomMenu extends BasePhotoPageBottomMenu {
    public ContentMaskAnimController mMaskAnimController;
    public MoreMenuAdapter mMoreActionsAdapter;
    public MoreMenuAnimController mMoreActionsAnimController;
    public View vContentMask;
    public LimitRecyclerView vMoreActions;

    /* loaded from: classes2.dex */
    public enum OverflowMenuState {
        Collapsed,
        Expanding,
        Expanded,
        Collapsing
    }

    public CommonPhotoPageBottomMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IViewProvider iViewProvider, IMenuItemDelegate.ClickHelper clickHelper) {
        super(iPhotoPageMenuManager, context, iViewProvider, clickHelper);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu
    public void initView() {
        super.initView();
        this.vContentMask = findViewById(R.id.content_mask);
        this.mMoreActionsAnimController = new MoreMenuAnimController();
        this.mMaskAnimController = new ContentMaskAnimController();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public void refreshAllNonResidentItems() {
        MoreMenuAdapter moreMenuAdapter;
        setMoreActionsHeight();
        if (!this.isMoreActionsShowing || (moreMenuAdapter = this.mMoreActionsAdapter) == null) {
            return;
        }
        moreMenuAdapter.notifyDataSetChanged();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void refreshMenuItem(IMenuItemDelegate iMenuItemDelegate) {
        int indexOf;
        super.refreshMenuItem(iMenuItemDelegate);
        if (!this.isMoreActionsShowing || (indexOf = this.mNonResident.indexOf(iMenuItemDelegate)) < 0) {
            return;
        }
        this.mMoreActionsAdapter.notifyItemChanged(indexOf, iMenuItemDelegate);
    }

    public final void setMoreActionsHeight() {
        LimitRecyclerView limitRecyclerView;
        ArrayList<IMenuItemDelegate> arrayList = this.mNonResident;
        if (arrayList == null || arrayList.isEmpty() || (limitRecyclerView = this.vMoreActions) == null) {
            return;
        }
        float f = this.mMoreActionsMaxHeight;
        if (f > 0.0f) {
            limitRecyclerView.setMaxHeight((int) f);
        } else {
            this.vMoreActions.setMaxHeight((int) Math.min(this.mMoreActionsLimitHeight, (int) ((this.mNonResident.size() * getResources().getDimension(R.dimen.dialog_list_preferred_item_height_small)) + limitRecyclerView.getPaddingTop() + this.vMoreActions.getPaddingBottom())));
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu
    public void ensureMoreView() {
        if (this.vMoreActions == null) {
            LimitRecyclerView limitRecyclerView = (LimitRecyclerView) findViewById(R.id.layout_more_actions);
            this.vMoreActions = limitRecyclerView;
            limitRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), 1, false));
            this.vMoreActions.setItemAnimator(new DefaultItemAnimator());
            MoreMenuAdapter moreMenuAdapter = new MoreMenuAdapter(this.mNonResident);
            this.mMoreActionsAdapter = moreMenuAdapter;
            this.vMoreActions.setAdapter(moreMenuAdapter);
        } else {
            this.mMoreActionsAdapter.notifyDataSetChanged();
        }
        setMoreActionsHeight();
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void hideMoreActions(boolean z) {
        refreshBottomMenuDrawableAsMoreActionsChanged(false);
        super.hideMoreActions(z);
        OverflowMenuState overflowMenuState = this.mMoreActionsAnimController.mOverflowMenuState;
        OverflowMenuState overflowMenuState2 = OverflowMenuState.Collapsing;
        if (overflowMenuState == overflowMenuState2 || overflowMenuState == OverflowMenuState.Collapsed) {
            return;
        }
        this.mMoreActionsAnimController.mOverflowMenuState = overflowMenuState2;
        this.mMoreActionsAnimController.hide(z);
        ActionMenuItemView actionMenuItemView = this.vMoreActionButton;
        if (actionMenuItemView == null) {
            return;
        }
        IMenuItem itemData = actionMenuItemView.getItemData();
        if (itemData != null) {
            itemData.setSelected(false);
        }
        this.vMoreActionButton.setSelected(false);
        this.vMoreActionButton.setEnabled(true);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void showMoreActions(boolean z) {
        refreshBottomMenuDrawableAsMoreActionsChanged(true);
        super.showMoreActions(z);
        OverflowMenuState overflowMenuState = this.mMoreActionsAnimController.mOverflowMenuState;
        OverflowMenuState overflowMenuState2 = OverflowMenuState.Expanding;
        if (overflowMenuState == overflowMenuState2 || overflowMenuState == OverflowMenuState.Expanded || this.vMoreActions == null) {
            return;
        }
        this.mMoreActionsAnimController.mOverflowMenuState = overflowMenuState2;
        this.mMoreActionsAnimController.show(z);
        ActionMenuItemView actionMenuItemView = this.vMoreActionButton;
        if (actionMenuItemView == null) {
            return;
        }
        IMenuItem itemData = actionMenuItemView.getItemData();
        if (itemData != null) {
            itemData.setSelected(true);
        }
        this.vMoreActionButton.setSelected(true);
    }

    public void refreshBottomMenuDrawableAsMoreActionsChanged(boolean z) {
        if (!this.mMenuManager.isVideoPlayerSupportActionBarAdjust()) {
            return;
        }
        if (z) {
            this.vBottomMenus.setBackgroundResource(R.drawable.photo_page_action_bar_background);
        } else {
            updateAlphaBackgroundDrawable();
        }
    }

    /* loaded from: classes2.dex */
    public class MoreMenuAdapter extends Adapter<MoreItemViewHolder> {
        public ArrayList<IMenuItemDelegate> mItems;

        public MoreMenuAdapter(ArrayList<IMenuItemDelegate> arrayList) {
            CommonPhotoPageBottomMenu.this = r1;
            this.mItems = arrayList;
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mItems.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public MoreItemViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            CommonPhotoPageBottomMenu commonPhotoPageBottomMenu = CommonPhotoPageBottomMenu.this;
            return new MoreItemViewHolder((ListMenuItemView) commonPhotoPageBottomMenu.mViewProvider.getBottomMenuMoreItemView(viewGroup));
        }

        @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(MoreItemViewHolder moreItemViewHolder, int i) {
            super.onBindViewHolder((MoreMenuAdapter) moreItemViewHolder, i);
            moreItemViewHolder.apply(this.mItems.get(i));
        }
    }

    /* loaded from: classes2.dex */
    public static class MoreMenuWeakShowTransitionListener extends TransitionListener {
        public final WeakReference<MoreMenuAnimController> mController;

        public MoreMenuWeakShowTransitionListener(MoreMenuAnimController moreMenuAnimController) {
            this.mController = new WeakReference<>(moreMenuAnimController);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            if (this.mController.get() == null) {
                return;
            }
            this.mController.get().mOverflowMenuState = OverflowMenuState.Expanded;
        }
    }

    /* loaded from: classes2.dex */
    public static class MoreMenuWeakHideTransitionListener extends TransitionListener {
        public final WeakReference<MoreMenuAnimController> mController;
        public WeakReference<LimitRecyclerView> vMoreActionsView;

        public MoreMenuWeakHideTransitionListener(MoreMenuAnimController moreMenuAnimController) {
            this.mController = new WeakReference<>(moreMenuAnimController);
        }

        public void setMoreActionsView(LimitRecyclerView limitRecyclerView) {
            this.vMoreActionsView = new WeakReference<>(limitRecyclerView);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            if (this.mController.get() == null) {
                return;
            }
            this.mController.get().mOverflowMenuState = OverflowMenuState.Collapsed;
            WeakReference<LimitRecyclerView> weakReference = this.vMoreActionsView;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.vMoreActionsView.get().setVisibility(8);
        }
    }

    /* loaded from: classes2.dex */
    public class MoreMenuAnimController {
        public IStateStyle mCurrentState;
        public OverflowMenuState mOverflowMenuState;
        public final MoreMenuWeakHideTransitionListener mWeakHideTransitionListener;
        public final MoreMenuWeakShowTransitionListener mWeakShowTransitionListener;

        public MoreMenuAnimController() {
            CommonPhotoPageBottomMenu.this = r1;
            this.mWeakShowTransitionListener = new MoreMenuWeakShowTransitionListener(this);
            this.mWeakHideTransitionListener = new MoreMenuWeakHideTransitionListener(this);
        }

        public final void exeAnim(boolean z, boolean z2) {
            IStateStyle to;
            Folme.clean(CommonPhotoPageBottomMenu.this.vMoreActions);
            int integer = CommonPhotoPageBottomMenu.this.getResources().getInteger(17694720);
            if (z) {
                CommonPhotoPageBottomMenu.this.vMoreActions.setVisibility(0);
                AnimConfig animConfig = new AnimConfig();
                animConfig.addListeners(this.mWeakShowTransitionListener);
                animConfig.setEase(EaseManager.getStyle(6, integer));
                AnimState animState = new AnimState("ShowMoreMenu");
                ViewProperty viewProperty = ViewProperty.TRANSLATION_Y;
                AnimState add = animState.add(viewProperty, SearchStatUtils.POW);
                IStateStyle state = Folme.useAt(CommonPhotoPageBottomMenu.this.vMoreActions).state();
                IStateStyle iStateStyle = this.mCurrentState;
                if (iStateStyle == null) {
                    state.setTo(new AnimState("HideMoreMenu").add(viewProperty, CommonPhotoPageBottomMenu.this.vMoreActions.getMaxHeight() + CommonPhotoPageBottomMenu.this.vBottomMenus.getHeight() + CommonPhotoPageBottomMenu.this.mInsetBottom));
                } else {
                    state = state.setTo(iStateStyle);
                }
                if (z2) {
                    to = state.to(add, animConfig);
                } else {
                    to = state.setTo(add, animConfig);
                }
            } else {
                int maxHeight = CommonPhotoPageBottomMenu.this.vMoreActions.getMaxHeight() + CommonPhotoPageBottomMenu.this.vBottomMenus.getHeight();
                if (maxHeight == 0) {
                    return;
                }
                int i = maxHeight + CommonPhotoPageBottomMenu.this.mInsetBottom;
                AnimConfig animConfig2 = new AnimConfig();
                this.mWeakHideTransitionListener.setMoreActionsView(CommonPhotoPageBottomMenu.this.vMoreActions);
                animConfig2.addListeners(this.mWeakHideTransitionListener);
                animConfig2.setEase(EaseManager.getStyle(6, integer));
                AnimState add2 = new AnimState("HideMoreMenu").add(ViewProperty.TRANSLATION_Y, i);
                IStateStyle state2 = Folme.useAt(CommonPhotoPageBottomMenu.this.vMoreActions).state();
                IStateStyle iStateStyle2 = this.mCurrentState;
                if (iStateStyle2 != null) {
                    state2 = state2.setTo(iStateStyle2);
                }
                if (z2) {
                    to = state2.to(add2, animConfig2);
                } else {
                    to = state2.setTo(add2, animConfig2);
                }
            }
            this.mCurrentState = to;
        }

        public void show(boolean z) {
            if (CommonPhotoPageBottomMenu.this.vMoreActions == null) {
                return;
            }
            exeAnim(true, z);
            CommonPhotoPageBottomMenu.this.mMaskAnimController.show(z);
        }

        public void hide(boolean z) {
            if (CommonPhotoPageBottomMenu.this.vMoreActions == null) {
                return;
            }
            exeAnim(false, z);
            CommonPhotoPageBottomMenu.this.mMaskAnimController.hide(z);
        }
    }

    /* loaded from: classes2.dex */
    public static class ContentMaskWeakShowTransitionListener extends TransitionListener {
        public final WeakReference<CommonPhotoPageBottomMenu> mBottomMenu;

        public ContentMaskWeakShowTransitionListener(CommonPhotoPageBottomMenu commonPhotoPageBottomMenu) {
            this.mBottomMenu = new WeakReference<>(commonPhotoPageBottomMenu);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            super.onBegin(obj);
            if (this.mBottomMenu.get() == null) {
                return;
            }
            this.mBottomMenu.get().vContentMask.setVisibility(0);
            this.mBottomMenu.get().bringToFront();
            this.mBottomMenu.get().vContentMask.setOnClickListener(null);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            if (this.mBottomMenu.get() == null) {
                return;
            }
            CommonPhotoPageBottomMenu commonPhotoPageBottomMenu = this.mBottomMenu.get();
            commonPhotoPageBottomMenu.bringToFront();
            ContentMaskAnimController contentMaskAnimController = commonPhotoPageBottomMenu.mMaskAnimController;
            View view = commonPhotoPageBottomMenu.vContentMask;
            if (view == null || contentMaskAnimController == null) {
                return;
            }
            view.setOnClickListener(contentMaskAnimController.mOnClickListener);
            commonPhotoPageBottomMenu.vContentMask.setImportantForAccessibility(2);
            IPhotoPageMenuManager iPhotoPageMenuManager = commonPhotoPageBottomMenu.mMenuManager;
            LimitRecyclerView limitRecyclerView = commonPhotoPageBottomMenu.vMoreActions;
            if (limitRecyclerView == null || limitRecyclerView.getChildAt(0) == null || iPhotoPageMenuManager == null) {
                return;
            }
            iPhotoPageMenuManager.setCurrentFocusView(limitRecyclerView.getChildAt(0));
            iPhotoPageMenuManager.onMenuActionsShown();
        }
    }

    /* loaded from: classes2.dex */
    public static class ContentMaskWeakHideTransitionListener extends TransitionListener {
        public final WeakReference<CommonPhotoPageBottomMenu> mBottomMenu;

        public ContentMaskWeakHideTransitionListener(CommonPhotoPageBottomMenu commonPhotoPageBottomMenu) {
            this.mBottomMenu = new WeakReference<>(commonPhotoPageBottomMenu);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            if (this.mBottomMenu.get() == null) {
                return;
            }
            this.mBottomMenu.get().bringToFront();
            this.mBottomMenu.get().vContentMask.setOnClickListener(null);
            this.mBottomMenu.get().vContentMask.setVisibility(8);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            super.onCancel(obj);
            if (this.mBottomMenu.get() == null) {
                return;
            }
            this.mBottomMenu.get().bringToFront();
            this.mBottomMenu.get().vContentMask.setOnClickListener(null);
            this.mBottomMenu.get().vContentMask.setVisibility(8);
        }
    }

    /* loaded from: classes2.dex */
    public class ContentMaskAnimController {
        public IStateStyle mCurrentState;
        public final View.OnClickListener mOnClickListener;
        public final ContentMaskWeakHideTransitionListener mWeakHideTransitionListener;
        public final ContentMaskWeakShowTransitionListener mWeakShowTransitionListener;

        public static /* synthetic */ void $r8$lambda$fez5tXgMXXQRVTINP0OwHTvV_oo(ContentMaskAnimController contentMaskAnimController, View view) {
            contentMaskAnimController.lambda$new$0(view);
        }

        public ContentMaskAnimController() {
            CommonPhotoPageBottomMenu.this = r2;
            this.mWeakShowTransitionListener = new ContentMaskWeakShowTransitionListener(r2);
            this.mWeakHideTransitionListener = new ContentMaskWeakHideTransitionListener(r2);
            this.mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.CommonPhotoPageBottomMenu$ContentMaskAnimController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CommonPhotoPageBottomMenu.ContentMaskAnimController.$r8$lambda$fez5tXgMXXQRVTINP0OwHTvV_oo(CommonPhotoPageBottomMenu.ContentMaskAnimController.this, view);
                }
            };
        }

        public /* synthetic */ void lambda$new$0(View view) {
            CommonPhotoPageBottomMenu.this.hideMoreActions(true);
        }

        public final void exeAnim(boolean z, boolean z2) {
            IStateStyle to;
            IStateStyle to2;
            Folme.clean(CommonPhotoPageBottomMenu.this.vContentMask);
            int integer = CommonPhotoPageBottomMenu.this.getResources().getInteger(17694720);
            if (z) {
                AnimConfig animConfig = new AnimConfig();
                animConfig.addListeners(this.mWeakShowTransitionListener);
                animConfig.setEase(EaseManager.getStyle(6, integer));
                AnimState animState = new AnimState("ShowContentMask");
                ViewProperty viewProperty = ViewProperty.ALPHA;
                AnimState add = animState.add(viewProperty, 1.0d);
                IStateStyle state = Folme.useAt(CommonPhotoPageBottomMenu.this.vContentMask).state();
                IStateStyle iStateStyle = this.mCurrentState;
                if (iStateStyle == null) {
                    state.setTo(new AnimState("HideContentMask").add(viewProperty, SearchStatUtils.POW));
                } else {
                    state = state.setTo(iStateStyle);
                }
                if (z2) {
                    to2 = state.to(add, animConfig);
                } else {
                    to2 = state.setTo(add, animConfig);
                }
                this.mCurrentState = to2;
                return;
            }
            AnimConfig animConfig2 = new AnimConfig();
            animConfig2.addListeners(this.mWeakHideTransitionListener);
            animConfig2.setEase(EaseManager.getStyle(6, integer));
            AnimState add2 = new AnimState("HideContentMask").add(ViewProperty.ALPHA, SearchStatUtils.POW);
            IStateStyle state2 = Folme.useAt(CommonPhotoPageBottomMenu.this.vContentMask).state();
            IStateStyle iStateStyle2 = this.mCurrentState;
            if (iStateStyle2 != null) {
                state2 = state2.setTo(iStateStyle2);
            }
            if (z2) {
                to = state2.to(add2, animConfig2);
            } else {
                to = state2.setTo(add2, animConfig2);
            }
            this.mCurrentState = to;
        }

        public void show(boolean z) {
            exeAnim(true, z);
        }

        public void hide(boolean z) {
            exeAnim(false, z);
        }
    }

    /* loaded from: classes2.dex */
    public class MoreItemViewHolder extends RecyclerView.ViewHolder {
        public final ListMenuItemView vContentView;

        public static /* synthetic */ void $r8$lambda$6rxkpyPC3KVKKubc4zJJ2frnDwU(MoreItemViewHolder moreItemViewHolder, IMenuItemDelegate iMenuItemDelegate, View view) {
            moreItemViewHolder.lambda$apply$0(iMenuItemDelegate, view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MoreItemViewHolder(ListMenuItemView listMenuItemView) {
            super(listMenuItemView);
            CommonPhotoPageBottomMenu.this = r1;
            this.vContentView = listMenuItemView;
        }

        public void apply(final IMenuItemDelegate iMenuItemDelegate) {
            this.vContentView.initialize(iMenuItemDelegate.getItemDataState(), 0);
            this.vContentView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.CommonPhotoPageBottomMenu$MoreItemViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CommonPhotoPageBottomMenu.MoreItemViewHolder.$r8$lambda$6rxkpyPC3KVKKubc4zJJ2frnDwU(CommonPhotoPageBottomMenu.MoreItemViewHolder.this, iMenuItemDelegate, view);
                }
            });
        }

        public /* synthetic */ void lambda$apply$0(IMenuItemDelegate iMenuItemDelegate, View view) {
            CommonPhotoPageBottomMenu.this.mItemClickHelper.onMenuItemClick(iMenuItemDelegate);
        }
    }
}
