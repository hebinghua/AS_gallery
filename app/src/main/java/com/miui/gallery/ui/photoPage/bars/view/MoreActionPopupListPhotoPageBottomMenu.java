package com.miui.gallery.ui.photoPage.bars.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import com.miui.gallery.R;
import com.miui.gallery.ui.photoPage.bars.IPhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.view.menu.ActionMenuItemView;
import com.miui.gallery.view.menu.IMenuItem;
import miuix.internal.widget.ListPopup;

/* loaded from: classes2.dex */
public class MoreActionPopupListPhotoPageBottomMenu extends BasePhotoPageBottomMenu {
    public MoreActionsAdapter mMoreActionsAdapter;
    public MoreActions vMoreActions;

    public static /* synthetic */ void $r8$lambda$a1lXAtZFR14uF22jcE5peendeqk(MoreActionPopupListPhotoPageBottomMenu moreActionPopupListPhotoPageBottomMenu, AdapterView adapterView, View view, int i, long j) {
        moreActionPopupListPhotoPageBottomMenu.lambda$ensureMoreView$1(adapterView, view, i, j);
    }

    public static /* synthetic */ void $r8$lambda$jKqXFxQ2eiSDkJGiPkG7YIkyxkY(MoreActionPopupListPhotoPageBottomMenu moreActionPopupListPhotoPageBottomMenu) {
        moreActionPopupListPhotoPageBottomMenu.lambda$ensureMoreView$0();
    }

    public MoreActionPopupListPhotoPageBottomMenu(IPhotoPageMenuManager iPhotoPageMenuManager, Context context, IViewProvider iViewProvider, IMenuItemDelegate.ClickHelper clickHelper) {
        super(iPhotoPageMenuManager, context, iViewProvider, clickHelper);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.AbstractPhotoPageMenu
    public void refreshAllNonResidentItems() {
        MoreActionsAdapter moreActionsAdapter = this.mMoreActionsAdapter;
        if (moreActionsAdapter != null) {
            moreActionsAdapter.notifyDataSetChanged();
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu
    public void ensureMoreView() {
        if (this.vMoreActions == null) {
            this.vMoreActions = new MoreActions(getContext());
            MoreActionsAdapter moreActionsAdapter = new MoreActionsAdapter(this.mNonResident, this.mViewProvider);
            this.mMoreActionsAdapter = moreActionsAdapter;
            this.vMoreActions.setAdapter(moreActionsAdapter);
            this.vMoreActions.setOnDismissListener(new PopupWindow.OnDismissListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.MoreActionPopupListPhotoPageBottomMenu$$ExternalSyntheticLambda1
                @Override // android.widget.PopupWindow.OnDismissListener
                public final void onDismiss() {
                    MoreActionPopupListPhotoPageBottomMenu.$r8$lambda$jKqXFxQ2eiSDkJGiPkG7YIkyxkY(MoreActionPopupListPhotoPageBottomMenu.this);
                }
            });
            this.vMoreActions.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.view.MoreActionPopupListPhotoPageBottomMenu$$ExternalSyntheticLambda0
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    MoreActionPopupListPhotoPageBottomMenu.$r8$lambda$a1lXAtZFR14uF22jcE5peendeqk(MoreActionPopupListPhotoPageBottomMenu.this, adapterView, view, i, j);
                }
            });
            return;
        }
        this.mMoreActionsAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$ensureMoreView$0() {
        this.isMoreActionsShowing = false;
        ActionMenuItemView actionMenuItemView = this.vMoreActionButton;
        if (actionMenuItemView != null) {
            IMenuItem itemData = actionMenuItemView.getItemData();
            if (itemData != null) {
                itemData.setSelected(false);
            }
            this.vMoreActionButton.setSelected(false);
            this.vMoreActionButton.setEnabled(true);
        }
    }

    public /* synthetic */ void lambda$ensureMoreView$1(AdapterView adapterView, View view, int i, long j) {
        this.vMoreActions.dismiss();
        this.mItemClickHelper.onMenuItemClick(this.mNonResident.get(i));
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void showMoreActions(boolean z) {
        super.showMoreActions(z);
        this.vMoreActions.show(this.vMoreActionButton, null);
        ActionMenuItemView actionMenuItemView = this.vMoreActionButton;
        if (actionMenuItemView != null) {
            IMenuItem itemData = actionMenuItemView.getItemData();
            if (itemData != null) {
                itemData.setSelected(true);
            }
            this.vMoreActionButton.setSelected(true);
        }
    }

    @Override // com.miui.gallery.ui.photoPage.bars.view.BasePhotoPageBottomMenu, com.miui.gallery.ui.photoPage.bars.view.IPhotoPageMenu
    public void hideMoreActions(boolean z) {
        super.hideMoreActions(z);
        MoreActions moreActions = this.vMoreActions;
        if (moreActions != null) {
            moreActions.dismiss();
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
