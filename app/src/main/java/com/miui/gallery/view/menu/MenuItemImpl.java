package com.miui.gallery.view.menu;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/* loaded from: classes2.dex */
public class MenuItemImpl implements IMenuItem {
    public static String sDeleteShortcutLabel;
    public static String sEnterShortcutLabel;
    public static String sPrependShortcutLabel;
    public static String sSpaceShortcutLabel;
    public ActionProvider mActionProvider;
    public View mActionView;
    public final int mCategoryOrder;
    public MenuItem.OnMenuItemClickListener mClickListener;
    public final int mGroup;
    public Drawable mIconDrawable;
    public final int mId;
    public Intent mIntent;
    public Runnable mItemCallback;
    public MenuBuilder mMenu;
    public ContextMenu.ContextMenuInfo mMenuInfo;
    public MenuItem.OnActionExpandListener mOnActionExpandListener;
    public final int mOrdering;
    public char mShortcutAlphabeticChar;
    public char mShortcutNumericChar;
    public int mShowAsAction;
    public SubMenuBuilder mSubMenu;
    public CharSequence mTitle;
    public CharSequence mTitleCondensed;
    public int mIconResId = 0;
    public int mFlags = 16;
    public boolean mIsActionViewExpanded = false;

    @Override // com.miui.gallery.view.menu.IMenuItem
    public int getTitleId() {
        return 0;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isResident() {
        return false;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isSupport() {
        return false;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setResident(boolean z) {
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setSupport(boolean z) {
    }

    public MenuItemImpl(MenuBuilder menuBuilder, int i, int i2, int i3, int i4, CharSequence charSequence, int i5) {
        this.mShowAsAction = 0;
        this.mMenu = menuBuilder;
        this.mId = i2;
        this.mGroup = i;
        this.mCategoryOrder = i3;
        this.mOrdering = i4;
        this.mTitle = charSequence;
        this.mShowAsAction = i5;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean invoke() {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.mClickListener;
        if (onMenuItemClickListener == null || !onMenuItemClickListener.onMenuItemClick(this)) {
            MenuBuilder menuBuilder = this.mMenu;
            if (menuBuilder.dispatchMenuItemSelected(menuBuilder.getRootMenu(), this)) {
                return true;
            }
            Runnable runnable = this.mItemCallback;
            if (runnable != null) {
                runnable.run();
                return true;
            }
            if (this.mIntent != null) {
                try {
                    this.mMenu.getContext().startActivity(this.mIntent);
                    return true;
                } catch (ActivityNotFoundException e) {
                    Log.e("MenuItemImpl", "Can't find activity to handle intent; ignoring", e);
                }
            }
            ActionProvider actionProvider = this.mActionProvider;
            return actionProvider != null && actionProvider.onPerformDefaultAction();
        }
        return true;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return (this.mFlags & 16) != 0;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z) {
        if (z) {
            this.mFlags |= 16;
        } else {
            this.mFlags &= -17;
        }
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.mGroup;
    }

    @Override // android.view.MenuItem
    @ViewDebug.CapturedViewProperty
    public int getItemId() {
        return this.mId;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.mCategoryOrder;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public int getOrdering() {
        return this.mOrdering;
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        return this.mIntent;
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        this.mIntent = intent;
        return this;
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c) {
        if (this.mShortcutAlphabeticChar == c) {
            return this;
        }
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c) {
        if (this.mShortcutNumericChar == c) {
            return this;
        }
        this.mShortcutNumericChar = c;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c, char c2) {
        this.mShortcutNumericChar = c;
        this.mShortcutAlphabeticChar = Character.toLowerCase(c2);
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public char getShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public String getShortcutLabel() {
        char shortcut = getShortcut();
        if (shortcut == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(sPrependShortcutLabel);
        if (shortcut == '\b') {
            sb.append(sDeleteShortcutLabel);
        } else if (shortcut == '\n') {
            sb.append(sEnterShortcutLabel);
        } else if (shortcut == ' ') {
            sb.append(sSpaceShortcutLabel);
        } else {
            sb.append(shortcut);
        }
        return sb.toString();
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean shouldShowShortcut() {
        return this.mMenu.isShortcutsVisible() && getShortcut() != 0;
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        return this.mSubMenu;
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        return this.mSubMenu != null;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setSubMenu(SubMenuBuilder subMenuBuilder) {
        this.mSubMenu = subMenuBuilder;
        subMenuBuilder.setHeaderTitle(getTitle());
    }

    @Override // android.view.MenuItem
    @ViewDebug.CapturedViewProperty
    public CharSequence getTitle() {
        return this.mTitle;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public CharSequence getTitleForItemView(MenuView$ItemView menuView$ItemView) {
        if (menuView$ItemView != null && menuView$ItemView.prefersCondensedTitle()) {
            return getTitleCondensed();
        }
        return getTitle();
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
        this.mMenu.onItemsChanged(false);
        SubMenuBuilder subMenuBuilder = this.mSubMenu;
        if (subMenuBuilder != null) {
            subMenuBuilder.setHeaderTitle(charSequence);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i) {
        return setTitle(this.mMenu.getContext().getString(i));
    }

    @Override // android.view.MenuItem
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.mTitleCondensed;
        return charSequence != null ? charSequence : this.mTitle;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.mTitleCondensed = charSequence;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        Drawable drawable = this.mIconDrawable;
        if (drawable != null) {
            return drawable;
        }
        if (this.mIconResId == 0) {
            return null;
        }
        Drawable drawable2 = this.mMenu.getContext().getDrawable(this.mIconResId);
        this.mIconResId = 0;
        this.mIconDrawable = drawable2;
        return drawable2;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.mIconResId = 0;
        this.mIconDrawable = drawable;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i) {
        this.mIconDrawable = null;
        this.mIconResId = i;
        this.mMenu.onItemsChanged(false);
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public int getIconRes() {
        return this.mIconResId;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return (this.mFlags & 1) == 1;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z) {
        int i = this.mFlags;
        int i2 = (z ? 1 : 0) | (i & (-2));
        this.mFlags = i2;
        if (i != i2) {
            this.mMenu.onItemsChanged(false);
        }
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setExclusiveCheckable(boolean z) {
        this.mFlags = (z ? 4 : 0) | (this.mFlags & (-5));
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isExclusiveCheckable() {
        return (this.mFlags & 4) != 0;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return (this.mFlags & 2) == 2;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z) {
        if ((this.mFlags & 4) != 0) {
            this.mMenu.setExclusiveItemChecked(this);
        } else {
            setCheckedInt(z);
        }
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setCheckedInt(boolean z) {
        int i = this.mFlags;
        int i2 = (z ? 2 : 0) | (i & (-3));
        this.mFlags = i2;
        if (i != i2) {
            this.mMenu.onItemsChanged(false);
        }
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        ActionProvider actionProvider = this.mActionProvider;
        return (actionProvider == null || !actionProvider.overridesItemVisibility()) ? (this.mFlags & 8) == 0 : (this.mFlags & 8) == 0 && this.mActionProvider.isVisible();
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean setVisibleInt(boolean z) {
        int i = this.mFlags;
        int i2 = (z ? 0 : 8) | (i & (-9));
        this.mFlags = i2;
        return i != i2;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z) {
        if (setVisibleInt(z)) {
            this.mMenu.onItemVisibleChanged(this);
        }
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.mClickListener = onMenuItemClickListener;
        return this;
    }

    public String toString() {
        return this.mTitle.toString();
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setMenuInfo(ContextMenu.ContextMenuInfo contextMenuInfo) {
        this.mMenuInfo = contextMenuInfo;
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return this.mMenuInfo;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean shouldShowIcon() {
        return this.mMenu.getOptionalIconsVisible();
    }

    public boolean isActionButton() {
        return (this.mFlags & 32) == 32;
    }

    @Override // android.view.MenuItem
    public void setShowAsAction(int i) {
        int i2 = i & 3;
        if (i2 != 0 && i2 != 1 && i2 != 2) {
            throw new IllegalArgumentException("SHOW_AS_ACTION_ALWAYS, SHOW_AS_ACTION_IF_ROOM, and SHOW_AS_ACTION_NEVER are mutually exclusive.");
        }
        this.mShowAsAction = i;
        this.mMenu.onItemActionRequestChanged(this);
    }

    @Override // android.view.MenuItem
    public MenuItem setActionView(View view) {
        int i;
        this.mActionView = view;
        this.mActionProvider = null;
        if (view != null && view.getId() == -1 && (i = this.mId) > 0) {
            view.setId(i);
        }
        this.mMenu.onItemActionRequestChanged(this);
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionView(int i) {
        Context context = this.mMenu.getContext();
        setActionView(LayoutInflater.from(context).inflate(i, (ViewGroup) new LinearLayout(context), false));
        return this;
    }

    @Override // android.view.MenuItem
    public View getActionView() {
        View view = this.mActionView;
        if (view != null) {
            return view;
        }
        ActionProvider actionProvider = this.mActionProvider;
        if (actionProvider == null) {
            return null;
        }
        View onCreateActionView = actionProvider.onCreateActionView(this);
        this.mActionView = onCreateActionView;
        return onCreateActionView;
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException("Implementation should use setSupportActionProvider!");
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("Implementation should use getSupportActionProvider!");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public ActionProvider getSupportActionProvider() {
        return this.mActionProvider;
    }

    @Override // android.view.MenuItem
    public MenuItem setShowAsActionFlags(int i) {
        setShowAsAction(i);
        return this;
    }

    @Override // android.view.MenuItem
    public boolean expandActionView() {
        MenuItem.OnActionExpandListener onActionExpandListener;
        return ((this.mShowAsAction & 8) == 0 || this.mActionView == null || ((onActionExpandListener = this.mOnActionExpandListener) != null && !onActionExpandListener.onMenuItemActionExpand(this)) || !this.mMenu.expandItemActionView(this)) ? false : true;
    }

    @Override // android.view.MenuItem
    public boolean collapseActionView() {
        MenuItem.OnActionExpandListener onActionExpandListener;
        return (this.mShowAsAction & 8) != 0 && (this.mActionView == null || (((onActionExpandListener = this.mOnActionExpandListener) == null || onActionExpandListener.onMenuItemActionCollapse(this)) && this.mMenu.collapseItemActionView(this)));
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean hasCollapsibleActionView() {
        return ((this.mShowAsAction & 8) == 0 || this.mActionView == null) ? false : true;
    }

    @Override // android.view.MenuItem
    public boolean isActionViewExpanded() {
        return this.mIsActionViewExpanded;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException("Implementation should use setSupportOnActionExpandListener!");
    }
}
