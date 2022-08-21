package miuix.appcompat.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import java.util.ArrayList;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$layout;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.appcompat.internal.view.menu.MenuView;
import miuix.internal.util.AnimHelper;
import miuix.internal.util.TaggingDrawableUtil;
import miuix.internal.widget.ListPopup;

/* loaded from: classes3.dex */
public class MenuPopupHelper implements AdapterView.OnItemClickListener, View.OnKeyListener, PopupWindow.OnDismissListener, MenuPresenter {
    public static final int ITEM_LAYOUT = R$layout.miuix_appcompat_popup_menu_item_layout;
    public MenuAdapter mAdapter;
    public View mAnchorView;
    public Context mContext;
    public boolean mForceShowIcon;
    public LayoutInflater mInflater;
    public MenuBuilder mMenu;
    public int mMenuItemLayout = ITEM_LAYOUT;
    public boolean mOverflowOnly;
    public ListPopup mPopup;
    public int mPopupMaxWidth;
    public MenuPresenter.Callback mPresenterCallback;

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean flagActionItems() {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
    }

    public MenuPopupHelper(Context context, MenuBuilder menuBuilder, View view, boolean z) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMenu = menuBuilder;
        this.mOverflowOnly = z;
        Resources resources = context.getResources();
        this.mPopupMaxWidth = Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(R$dimen.miuix_appcompat_config_prefDialogWidth));
        this.mAnchorView = view;
        menuBuilder.addMenuPresenter(this);
    }

    public void setForceShowIcon(boolean z) {
        this.mForceShowIcon = z;
    }

    public void setMenuItemLayout(int i) {
        this.mMenuItemLayout = i;
    }

    public boolean tryShow() {
        ListPopup listPopup = new ListPopup(this.mContext);
        this.mPopup = listPopup;
        listPopup.setMaxAllowedHeight(this.mContext.getResources().getDimensionPixelOffset(R$dimen.miuix_appcompat_menu_popup_max_height));
        this.mPopup.setHasShadow(false);
        this.mPopup.setOnDismissListener(this);
        this.mPopup.setOnItemClickListener(this);
        MenuAdapter menuAdapter = new MenuAdapter(this.mMenu);
        this.mAdapter = menuAdapter;
        this.mPopup.setAdapter(menuAdapter);
        ListPopup listPopup2 = this.mPopup;
        listPopup2.setHorizontalOffset(-listPopup2.getMinMarginScreen());
        this.mPopup.setVerticalOffset(0);
        this.mPopup.show(this.mAnchorView, null);
        this.mPopup.getListView().setOnKeyListener(this);
        return true;
    }

    public void dismiss(boolean z) {
        if (isShowing()) {
            this.mPopup.dismiss();
        }
    }

    @Override // android.widget.PopupWindow.OnDismissListener
    public void onDismiss() {
        this.mPopup = null;
        this.mMenu.close();
    }

    public boolean isShowing() {
        ListPopup listPopup = this.mPopup;
        return listPopup != null && listPopup.isShowing();
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        MenuAdapter menuAdapter = this.mAdapter;
        menuAdapter.mAdapterMenu.performItemAction(menuAdapter.getItem(i), 0);
    }

    @Override // android.view.View.OnKeyListener
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1 && i == 82) {
            dismiss(false);
            return true;
        }
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        MenuAdapter menuAdapter = this.mAdapter;
        if (menuAdapter != null) {
            menuAdapter.notifyDataSetChanged();
        }
    }

    public void setCallback(MenuPresenter.Callback callback) {
        this.mPresenterCallback = callback;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        boolean z;
        if (subMenuBuilder.hasVisibleItems()) {
            MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.mContext, subMenuBuilder, this.mAnchorView, false);
            menuPopupHelper.setCallback(this.mPresenterCallback);
            int size = subMenuBuilder.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    z = false;
                    break;
                }
                MenuItem item = subMenuBuilder.getItem(i);
                if (item.isVisible() && item.getIcon() != null) {
                    z = true;
                    break;
                }
                i++;
            }
            menuPopupHelper.setForceShowIcon(z);
            if (menuPopupHelper.tryShow()) {
                MenuPresenter.Callback callback = this.mPresenterCallback;
                if (callback != null) {
                    callback.onOpenSubMenu(subMenuBuilder);
                }
                return true;
            }
        }
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        if (menuBuilder != this.mMenu) {
            return;
        }
        dismiss(true);
        MenuPresenter.Callback callback = this.mPresenterCallback;
        if (callback == null) {
            return;
        }
        callback.onCloseMenu(menuBuilder, z);
    }

    /* loaded from: classes3.dex */
    public class MenuAdapter extends BaseAdapter {
        public MenuBuilder mAdapterMenu;
        public int mExpandedIndex = -1;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        public MenuAdapter(MenuBuilder menuBuilder) {
            this.mAdapterMenu = menuBuilder;
            findExpandedIndex();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            ArrayList<MenuItemImpl> nonActionItems = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            if (this.mExpandedIndex < 0) {
                return nonActionItems.size();
            }
            return nonActionItems.size() - 1;
        }

        @Override // android.widget.Adapter
        public MenuItemImpl getItem(int i) {
            ArrayList<MenuItemImpl> nonActionItems = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            int i2 = this.mExpandedIndex;
            if (i2 >= 0 && i >= i2) {
                i++;
            }
            return nonActionItems.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = MenuPopupHelper.this.mInflater.inflate(MenuPopupHelper.this.mMenuItemLayout, viewGroup, false);
                AnimHelper.addPressAnimWithBg(view);
            }
            TaggingDrawableUtil.updateItemPadding(view, i, getCount());
            MenuView.ItemView itemView = (MenuView.ItemView) view;
            if (MenuPopupHelper.this.mForceShowIcon) {
                ((ListMenuItemView) view).setForceShowIcon(true);
            }
            itemView.initialize(getItem(i), 0);
            return view;
        }

        public void findExpandedIndex() {
            MenuItemImpl expandedItem = MenuPopupHelper.this.mMenu.getExpandedItem();
            if (expandedItem != null) {
                ArrayList<MenuItemImpl> nonActionItems = MenuPopupHelper.this.mMenu.getNonActionItems();
                int size = nonActionItems.size();
                for (int i = 0; i < size; i++) {
                    if (nonActionItems.get(i) == expandedItem) {
                        this.mExpandedIndex = i;
                        return;
                    }
                }
            }
            this.mExpandedIndex = -1;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }
}
