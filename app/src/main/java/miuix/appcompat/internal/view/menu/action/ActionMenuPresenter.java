package miuix.appcompat.internal.view.menu.action;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$id;
import miuix.appcompat.R$integer;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$string;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.view.ActionBarPolicy;
import miuix.appcompat.internal.view.menu.BaseMenuPresenter;
import miuix.appcompat.internal.view.menu.ListMenuPresenter;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuDialogHelper;
import miuix.appcompat.internal.view.menu.MenuItemImpl;
import miuix.appcompat.internal.view.menu.MenuPopupHelper;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.appcompat.internal.view.menu.MenuView;
import miuix.appcompat.internal.view.menu.SubMenuBuilder;
import miuix.appcompat.internal.view.menu.action.OverflowMenuButton;
import miuix.core.util.MiuixUIUtils;
import miuix.internal.util.DeviceHelper;

/* loaded from: classes3.dex */
public class ActionMenuPresenter extends BaseMenuPresenter {
    public final SparseBooleanArray mActionButtonGroups;
    public ActionButtonSubMenu mActionButtonPopup;
    public int mActionItemWidthLimit;
    public ActionBarOverlayLayout mDecorView;
    public boolean mExpandedActionViewsExclusive;
    public int mListItemLayoutRes;
    public int mListLayoutRes;
    public OverflowMenu mListOverflowMenu;
    public int mMaxItems;
    public boolean mMaxItemsSet;
    public int mOpenSubMenuId;
    public View mOverflowButton;
    public OverflowMenu mOverflowMenu;
    public int mOverflowMenuAttrs;
    public MenuItemImpl mOverflowMenuItem;
    public final PopupPresenterCallback mPopupPresenterCallback;
    public OpenOverflowRunnable mPostedOpenRunnable;
    public boolean mReserveOverflow;
    public boolean mReserveOverflowSet;
    public View mScrapActionButtonView;
    public boolean mStrictWidthLimit;
    public int mWidthLimit;
    public boolean mWidthLimitSet;

    /* loaded from: classes3.dex */
    public interface OverflowMenu {
        void dismiss(boolean z);

        boolean isShowing();

        boolean tryShow();

        void update(MenuBuilder menuBuilder);
    }

    /* renamed from: $r8$lambda$jdh71Y-3QEzgNclz5ywMVeRCFzU */
    public static /* synthetic */ void m2606$r8$lambda$jdh71Y3QEzgNclz5ywMVeRCFzU(ActionMenuPresenter actionMenuPresenter) {
        actionMenuPresenter.lambda$createOverflowMenuButton$0();
    }

    public ActionMenuPresenter(Context context, ActionBarOverlayLayout actionBarOverlayLayout, int i, int i2, int i3, int i4) {
        super(context, i, i2);
        this.mOverflowMenuAttrs = 16843510;
        this.mActionButtonGroups = new SparseBooleanArray();
        this.mPopupPresenterCallback = new PopupPresenterCallback();
        this.mListLayoutRes = i3;
        this.mListItemLayoutRes = i4;
        this.mDecorView = actionBarOverlayLayout;
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter, miuix.appcompat.internal.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        super.initForMenu(context, menuBuilder);
        context.getResources();
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = actionBarPolicy.getMaxActionButtons();
        }
        int i = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                this.mOverflowButton = createOverflowMenuButton(this.mSystemContext);
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(makeMeasureSpec, makeMeasureSpec);
            }
            i -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = i;
        this.mScrapActionButtonView = null;
    }

    public void onConfigurationChanged(Configuration configuration) {
        if (!this.mMaxItemsSet) {
            this.mMaxItems = this.mContext.getResources().getInteger(R$integer.abc_max_action_buttons);
        }
        MenuBuilder menuBuilder = this.mMenu;
        if (menuBuilder != null) {
            BaseMenuPresenter.notifyItemsChanged(menuBuilder, true);
        }
        View view = this.mOverflowButton;
        if (view instanceof OverflowMenuButton) {
            ((OverflowMenuButton) view).onConfigurationChanged(configuration);
        }
    }

    public void setWidthLimit(int i, boolean z) {
        this.mWidthLimit = i;
        this.mStrictWidthLimit = z;
        this.mWidthLimitSet = true;
    }

    public void setReserveOverflow(boolean z) {
        this.mReserveOverflow = z;
        this.mReserveOverflowSet = true;
    }

    public void setExpandedActionViewsExclusive(boolean z) {
        this.mExpandedActionViewsExclusive = z;
    }

    public void setActionEditMode(boolean z) {
        if (z) {
            this.mOverflowMenuAttrs = R$attr.actionModeOverflowButtonStyle;
        }
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter
    public MenuView getMenuView(ViewGroup viewGroup) {
        MenuView menuView = super.getMenuView(viewGroup);
        ((ActionMenuView) menuView).setPresenter(this);
        return menuView;
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter
    public View getItemView(MenuItemImpl menuItemImpl, View view, ViewGroup viewGroup) {
        View actionView = menuItemImpl.getActionView();
        if (actionView == null || menuItemImpl.hasCollapsibleActionView()) {
            if (!(view instanceof ActionMenuItemView)) {
                view = null;
            }
            actionView = super.getItemView(menuItemImpl, view, viewGroup);
        }
        actionView.setVisibility(menuItemImpl.isActionViewExpanded() ? 8 : 0);
        ActionMenuView actionMenuView = (ActionMenuView) viewGroup;
        ViewGroup.LayoutParams layoutParams = actionView.getLayoutParams();
        if (!actionMenuView.checkLayoutParams(layoutParams)) {
            actionView.setLayoutParams(actionMenuView.mo2610generateLayoutParams(layoutParams));
        }
        return actionView;
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter
    public void bindItemView(MenuItemImpl menuItemImpl, MenuView.ItemView itemView) {
        itemView.initialize(menuItemImpl, 0);
        itemView.setItemInvoker((MenuBuilder.ItemInvoker) this.mMenuView);
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter
    public boolean shouldIncludeItem(int i, MenuItemImpl menuItemImpl) {
        return menuItemImpl.isActionButton();
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter, miuix.appcompat.internal.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        super.updateMenuView(z);
        if (this.mMenuView == null) {
            return;
        }
        MenuBuilder menuBuilder = this.mMenu;
        ArrayList<MenuItemImpl> nonActionItems = menuBuilder != null ? menuBuilder.getNonActionItems() : null;
        boolean z2 = false;
        if (this.mReserveOverflow && nonActionItems != null) {
            int size = nonActionItems.size();
            if (size == 1) {
                z2 = !nonActionItems.get(0).isActionViewExpanded();
            } else if (size > 0) {
                z2 = true;
            }
        }
        if (z2) {
            View view = this.mOverflowButton;
            if (view == null) {
                this.mOverflowButton = createOverflowMenuButton(this.mSystemContext);
            } else {
                view.setTranslationY(0.0f);
            }
            ViewGroup viewGroup = (ViewGroup) this.mOverflowButton.getParent();
            if (viewGroup != this.mMenuView) {
                if (viewGroup != null) {
                    viewGroup.removeView(this.mOverflowButton);
                }
                ActionMenuView actionMenuView = (ActionMenuView) this.mMenuView;
                actionMenuView.addView(this.mOverflowButton, actionMenuView.generateOverflowButtonLayoutParams());
            }
        } else {
            View view2 = this.mOverflowButton;
            if (view2 != null) {
                ViewParent parent = view2.getParent();
                MenuView menuView = this.mMenuView;
                if (parent == menuView) {
                    ((ViewGroup) menuView).removeView(this.mOverflowButton);
                }
            }
        }
        ((ActionMenuView) this.mMenuView).setOverflowReserved(this.mReserveOverflow);
        if (shouldShowPopupOverflow()) {
            return;
        }
        getOverflowMenu().update(this.mMenu);
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter, miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        if (!subMenuBuilder.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder subMenuBuilder2 = subMenuBuilder;
        while (subMenuBuilder2.getParentMenu() != this.mMenu) {
            subMenuBuilder2 = (SubMenuBuilder) subMenuBuilder2.getParentMenu();
        }
        if (findViewForItem(subMenuBuilder2.getItem()) == null && this.mOverflowButton == null) {
            return false;
        }
        this.mOpenSubMenuId = subMenuBuilder.getItem().getItemId();
        ActionButtonSubMenu actionButtonSubMenu = new ActionButtonSubMenu(subMenuBuilder);
        this.mActionButtonPopup = actionButtonSubMenu;
        actionButtonSubMenu.show(null);
        super.onSubMenuSelected(subMenuBuilder);
        return true;
    }

    public final View findViewForItem(MenuItem menuItem) {
        ViewGroup viewGroup = (ViewGroup) this.mMenuView;
        if (viewGroup == null) {
            return null;
        }
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if ((childAt instanceof MenuView.ItemView) && ((MenuView.ItemView) childAt).getItemData() == menuItem) {
                return childAt;
            }
        }
        return null;
    }

    public boolean showOverflowMenu() {
        if (!this.mReserveOverflow || isOverflowMenuShowing() || this.mMenu == null || this.mMenuView == null || this.mPostedOpenRunnable != null) {
            return false;
        }
        OpenOverflowRunnable openOverflowRunnable = new OpenOverflowRunnable(getOverflowMenu());
        this.mPostedOpenRunnable = openOverflowRunnable;
        ((View) this.mMenuView).post(openOverflowRunnable);
        super.onSubMenuSelected(null);
        this.mOverflowButton.setSelected(true);
        return true;
    }

    public final boolean shouldShowPopupOverflow() {
        return DeviceHelper.isTablet(this.mContext) && (!DeviceHelper.isFoldDevice() || !MiuixUIUtils.isInMultiWindowMode(this.mContext));
    }

    public final OverflowMenu getOverflowMenu() {
        if (shouldShowPopupOverflow()) {
            return new PopupOverflowMenu(this.mContext, this.mMenu, this.mOverflowButton, true);
        }
        if (this.mListOverflowMenu == null) {
            this.mListOverflowMenu = new ListOverflowMenu();
        }
        return this.mListOverflowMenu;
    }

    public final MenuItemImpl getOverflowMenuItem() {
        if (this.mOverflowMenuItem == null) {
            this.mOverflowMenuItem = BaseMenuPresenter.createMenuItemImpl(this.mMenu, 0, R$id.more, 0, 0, this.mContext.getString(R$string.more), 0);
        }
        return this.mOverflowMenuItem;
    }

    public boolean hideOverflowMenu(boolean z) {
        if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
            this.mOverflowButton.setSelected(false);
            ((View) this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
            this.mPostedOpenRunnable = null;
            return true;
        }
        OverflowMenu overflowMenu = this.mOverflowMenu;
        if (overflowMenu == null) {
            return false;
        }
        boolean isShowing = overflowMenu.isShowing();
        if (isShowing) {
            this.mOverflowButton.setSelected(false);
        }
        this.mOverflowMenu.dismiss(z);
        return isShowing;
    }

    public boolean dismissPopupMenus(boolean z) {
        return hideOverflowMenu(z);
    }

    public boolean hideSubMenus() {
        ActionButtonSubMenu actionButtonSubMenu = this.mActionButtonPopup;
        if (actionButtonSubMenu != null) {
            actionButtonSubMenu.dismiss();
            return true;
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        OverflowMenu overflowMenu = this.mOverflowMenu;
        return overflowMenu != null && overflowMenu.isShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean flagActionItems() {
        ArrayList<MenuItemImpl> visibleItems = this.mMenu.getVisibleItems();
        int size = visibleItems.size();
        int i = this.mMaxItems;
        if (i < size) {
            i--;
        }
        int i2 = 0;
        while (true) {
            boolean z = true;
            if (i2 >= size || i <= 0) {
                break;
            }
            MenuItemImpl menuItemImpl = visibleItems.get(i2);
            if (!menuItemImpl.requestsActionButton() && !menuItemImpl.requiresActionButton()) {
                z = false;
            }
            menuItemImpl.setIsActionButton(z);
            if (z) {
                i--;
            }
            i2++;
        }
        while (i2 < size) {
            visibleItems.get(i2).setIsActionButton(false);
            i2++;
        }
        return true;
    }

    @Override // miuix.appcompat.internal.view.menu.BaseMenuPresenter, miuix.appcompat.internal.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        dismissPopupMenus(true);
        super.onCloseMenu(menuBuilder, z);
    }

    /* loaded from: classes3.dex */
    public class ListOverflowMenu implements OverflowMenu {
        public ListMenuPresenter mListMenuPresenter;

        public ListOverflowMenu() {
            ActionMenuPresenter.this = r1;
        }

        public final ListMenuPresenter getListMenuPresenter(MenuBuilder menuBuilder) {
            if (this.mListMenuPresenter == null) {
                this.mListMenuPresenter = new ListMenuPresenter(ActionMenuPresenter.this.mContext, ActionMenuPresenter.this.mListLayoutRes, ActionMenuPresenter.this.mListItemLayoutRes);
            }
            menuBuilder.addMenuPresenter(this.mListMenuPresenter);
            return this.mListMenuPresenter;
        }

        public View getOverflowMenuView(MenuBuilder menuBuilder) {
            if (menuBuilder == null || menuBuilder.getNonActionItems().size() <= 0) {
                return null;
            }
            return (View) getListMenuPresenter(menuBuilder).getMenuView((ViewGroup) ActionMenuPresenter.this.mMenuView);
        }

        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public boolean tryShow() {
            return ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).showOverflowMenu(ActionMenuPresenter.this.mDecorView);
        }

        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public boolean isShowing() {
            return ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).isOverflowMenuShowing();
        }

        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void dismiss(boolean z) {
            ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).hideOverflowMenu(ActionMenuPresenter.this.mDecorView);
        }

        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void update(MenuBuilder menuBuilder) {
            ((PhoneActionMenuView) ActionMenuPresenter.this.mMenuView).setOverflowMenuView(getOverflowMenuView(menuBuilder));
        }
    }

    /* loaded from: classes3.dex */
    public class PopupOverflowMenu extends MenuPopupHelper implements OverflowMenu {
        @Override // miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void update(MenuBuilder menuBuilder) {
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PopupOverflowMenu(Context context, MenuBuilder menuBuilder, View view, boolean z) {
            super(context, menuBuilder, view, z);
            ActionMenuPresenter.this = r1;
            setCallback(r1.mPopupPresenterCallback);
            setMenuItemLayout(R$layout.miuix_appcompat_overflow_popup_menu_item_layout);
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPopupHelper, android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            super.onDismiss();
            ActionMenuPresenter.this.mMenu.close();
            ActionMenuPresenter.this.mOverflowMenu = null;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPopupHelper, miuix.appcompat.internal.view.menu.action.ActionMenuPresenter.OverflowMenu
        public void dismiss(boolean z) {
            super.dismiss(z);
            if (ActionMenuPresenter.this.mOverflowButton != null) {
                ActionMenuPresenter.this.mOverflowButton.setSelected(false);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ActionButtonSubMenu extends MenuDialogHelper {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ActionButtonSubMenu(SubMenuBuilder subMenuBuilder) {
            super(subMenuBuilder);
            ActionMenuPresenter.this = r1;
            r1.setCallback(r1.mPopupPresenterCallback);
        }

        @Override // miuix.appcompat.internal.view.menu.MenuDialogHelper, android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            super.onDismiss(dialogInterface);
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
        }
    }

    /* loaded from: classes3.dex */
    public class PopupPresenterCallback implements MenuPresenter.Callback {
        public PopupPresenterCallback() {
            ActionMenuPresenter.this = r1;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) menuBuilder).getItem().getItemId();
            return false;
        }

        @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
        public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
            if (menuBuilder instanceof SubMenuBuilder) {
                BaseMenuPresenter.close(menuBuilder.getRootMenu(), false);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class OpenOverflowRunnable implements Runnable {
        public OverflowMenu mPopup;

        public OpenOverflowRunnable(OverflowMenu overflowMenu) {
            ActionMenuPresenter.this = r1;
            this.mPopup = overflowMenu;
        }

        @Override // java.lang.Runnable
        public void run() {
            ActionMenuPresenter.this.mMenu.changeMenuMode();
            View view = (View) ActionMenuPresenter.this.mMenuView;
            if (view != null && view.getWindowToken() != null && this.mPopup.tryShow()) {
                ActionMenuPresenter.this.mOverflowMenu = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    public View createOverflowMenuButton(Context context) {
        OverflowMenuButton overflowMenuButton = new OverflowMenuButton(context, null, this.mOverflowMenuAttrs);
        overflowMenuButton.setOnOverflowMenuButtonClickListener(new OverflowMenuButton.OnOverflowMenuButtonClickListener() { // from class: miuix.appcompat.internal.view.menu.action.ActionMenuPresenter$$ExternalSyntheticLambda0
            @Override // miuix.appcompat.internal.view.menu.action.OverflowMenuButton.OnOverflowMenuButtonClickListener
            public final void onOverflowMenuButtonClick() {
                ActionMenuPresenter.m2606$r8$lambda$jdh71Y3QEzgNclz5ywMVeRCFzU(ActionMenuPresenter.this);
            }
        });
        return overflowMenuButton;
    }

    public /* synthetic */ void lambda$createOverflowMenuButton$0() {
        MenuBuilder menuBuilder = this.mMenu;
        if (menuBuilder != null) {
            BaseMenuPresenter.dispatchMenuItemSelected(menuBuilder, menuBuilder.getRootMenu(), getOverflowMenuItem());
        }
        if (this.mOverflowButton.isSelected()) {
            hideOverflowMenu(true);
        } else {
            showOverflowMenu();
        }
    }
}
