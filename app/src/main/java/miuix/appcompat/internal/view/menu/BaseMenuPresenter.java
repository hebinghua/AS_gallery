package miuix.appcompat.internal.view.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.appcompat.internal.view.menu.MenuView;

/* loaded from: classes3.dex */
public abstract class BaseMenuPresenter implements MenuPresenter {
    public MenuPresenter.Callback mCallback;
    public Context mContext;
    public int mId;
    public LayoutInflater mInflater;
    public int mItemLayoutRes;
    public MenuBuilder mMenu;
    public int mMenuLayoutRes;
    public MenuView mMenuView;
    public Context mSystemContext;
    public LayoutInflater mSystemInflater;

    public abstract void bindItemView(MenuItemImpl menuItemImpl, MenuView.ItemView itemView);

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean collapseItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean expandItemActionView(MenuBuilder menuBuilder, MenuItemImpl menuItemImpl) {
        return false;
    }

    public abstract boolean shouldIncludeItem(int i, MenuItemImpl menuItemImpl);

    public BaseMenuPresenter(Context context, int i, int i2) {
        this.mSystemContext = context;
        this.mSystemInflater = LayoutInflater.from(context);
        this.mMenuLayoutRes = i;
        this.mItemLayoutRes = i2;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void initForMenu(Context context, MenuBuilder menuBuilder) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMenu = menuBuilder;
    }

    public MenuView getMenuView(ViewGroup viewGroup) {
        if (this.mMenuView == null) {
            MenuView menuView = (MenuView) this.mSystemInflater.inflate(this.mMenuLayoutRes, viewGroup, false);
            this.mMenuView = menuView;
            menuView.initialize(this.mMenu);
            updateMenuView(true);
        }
        return this.mMenuView;
    }

    /* JADX WARN: Type inference failed for: r7v2, types: [boolean, int] */
    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void updateMenuView(boolean z) {
        MenuView menuView = this.mMenuView;
        ViewGroup viewGroup = (ViewGroup) menuView;
        if (viewGroup == null) {
            return;
        }
        ?? hasBackgroundView = menuView.hasBackgroundView();
        int i = hasBackgroundView;
        if (this.mMenuView.hasBlurBackgroundView()) {
            i = hasBackgroundView + 1;
        }
        MenuBuilder menuBuilder = this.mMenu;
        if (menuBuilder != null) {
            menuBuilder.flagActionItems();
            Iterator<MenuItemImpl> it = this.mMenu.getVisibleItems().iterator();
            while (it.hasNext()) {
                MenuItemImpl next = it.next();
                if (shouldIncludeItem(i, next)) {
                    View childAt = viewGroup.getChildAt(i);
                    MenuItemImpl itemData = childAt instanceof MenuView.ItemView ? ((MenuView.ItemView) childAt).getItemData() : null;
                    View itemView = getItemView(next, childAt, viewGroup);
                    if (next != itemData) {
                        itemView.setPressed(false);
                    }
                    if (itemView != childAt) {
                        addItemView(itemView, i);
                    }
                    i++;
                }
            }
        }
        while (i < viewGroup.getChildCount()) {
            if (!this.mMenuView.filterLeftoverView(i)) {
                i++;
            }
        }
    }

    public void addItemView(View view, int i) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
        ((ViewGroup) this.mMenuView).addView(view, i);
    }

    public void setCallback(MenuPresenter.Callback callback) {
        this.mCallback = callback;
    }

    public MenuView.ItemView createItemView(ViewGroup viewGroup) {
        return (MenuView.ItemView) this.mSystemInflater.inflate(this.mItemLayoutRes, viewGroup, false);
    }

    public View getItemView(MenuItemImpl menuItemImpl, View view, ViewGroup viewGroup) {
        MenuView.ItemView createItemView;
        if (view instanceof MenuView.ItemView) {
            createItemView = (MenuView.ItemView) view;
        } else {
            createItemView = createItemView(viewGroup);
        }
        bindItemView(menuItemImpl, createItemView);
        return (View) createItemView;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        MenuPresenter.Callback callback = this.mCallback;
        if (callback != null) {
            callback.onCloseMenu(menuBuilder, z);
        }
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter
    public boolean onSubMenuSelected(SubMenuBuilder subMenuBuilder) {
        MenuPresenter.Callback callback = this.mCallback;
        return callback != null && callback.onOpenSubMenu(subMenuBuilder);
    }

    public void setId(int i) {
        this.mId = i;
    }

    public static void notifyItemsChanged(MenuBuilder menuBuilder, boolean z) {
        menuBuilder.onItemsChanged(z);
    }

    public static void close(MenuBuilder menuBuilder, boolean z) {
        menuBuilder.close(z);
    }

    public static boolean dispatchMenuItemSelected(MenuBuilder menuBuilder, MenuBuilder menuBuilder2, MenuItem menuItem) {
        return menuBuilder.dispatchMenuItemSelected(menuBuilder2, menuItem);
    }

    public static MenuItemImpl createMenuItemImpl(MenuBuilder menuBuilder, int i, int i2, int i3, int i4, CharSequence charSequence, int i5) {
        return new MenuItemImpl(menuBuilder, i, i2, i3, i4, charSequence, i5);
    }
}
