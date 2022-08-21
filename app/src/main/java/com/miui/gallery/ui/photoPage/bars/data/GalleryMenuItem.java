package com.miui.gallery.ui.photoPage.bars.data;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuView$ItemView;
import com.miui.gallery.view.menu.SubMenuBuilder;

/* loaded from: classes2.dex */
public class GalleryMenuItem implements IMenuItem {
    public boolean isEnabled;
    public boolean isNeedFolmeAnim = true;
    public boolean isResident;
    public boolean isSelected;
    public boolean isSupport;
    public boolean isVisible;
    public boolean mCheckable;
    public boolean mChecked;
    public MenuItem.OnMenuItemClickListener mClickListener;
    public final int mGroup;
    public Drawable mIconDrawable;
    public int mIconRes;
    public final int mId;
    public final GalleryMenuBuilder mMenu;
    public final int mOrder;
    public char mShortcutAlphabeticChar;
    public char mShortcutNumericChar;
    public CharSequence mTitle;
    public CharSequence mTitleCondensed;
    public int mTitleRes;

    @Override // com.miui.gallery.view.menu.IMenuItem
    public char getShortcut() {
        return (char) 0;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public String getShortcutLabel() {
        return "";
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isExclusiveCheckable() {
        return false;
    }

    @Override // android.view.MenuItem
    public void setShowAsAction(int i) {
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean shouldShowIcon() {
        return false;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean shouldShowShortcut() {
        return false;
    }

    public GalleryMenuItem(GalleryMenuBuilder galleryMenuBuilder, int i, int i2, int i3, CharSequence charSequence) {
        this.mMenu = galleryMenuBuilder;
        this.mGroup = i;
        this.mId = i2;
        this.mOrder = i3;
        this.mTitle = charSequence;
    }

    @Override // android.view.MenuItem
    public int getItemId() {
        return this.mId;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isResident() {
        return this.isResident;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setResident(boolean z) {
        this.isResident = z;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isSupport() {
        return this.isSupport;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setSupport(boolean z) {
        this.isSupport = z;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    @Override // android.view.MenuItem
    public int getGroupId() {
        return this.mGroup;
    }

    @Override // android.view.MenuItem
    public int getOrder() {
        return this.mOrder;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(CharSequence charSequence) {
        this.mTitleRes = 0;
        this.mTitle = charSequence;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitle(int i) {
        this.mTitleRes = i;
        this.mTitle = this.mMenu.getContext().getString(i);
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public int getTitleId() {
        return this.mTitleRes;
    }

    @Override // android.view.MenuItem
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
    public CharSequence getTitleCondensed() {
        CharSequence charSequence = this.mTitleCondensed;
        return charSequence != null ? charSequence : this.mTitle;
    }

    @Override // android.view.MenuItem
    public MenuItem setTitleCondensed(CharSequence charSequence) {
        this.mTitleCondensed = charSequence;
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public int getIconRes() {
        return this.mIconRes;
    }

    @Override // android.view.MenuItem
    public Drawable getIcon() {
        Drawable drawable = this.mIconDrawable;
        if (drawable != null) {
            return drawable;
        }
        if (this.mIconRes == 0) {
            return null;
        }
        Drawable drawable2 = this.mMenu.getContext().getDrawable(this.mIconRes);
        this.mIconDrawable = drawable2;
        return drawable2;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(Drawable drawable) {
        this.mIconRes = 0;
        this.mIconDrawable = drawable;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setIcon(int i) {
        this.mIconDrawable = null;
        this.mIconRes = i;
        return this;
    }

    @Override // android.view.MenuItem
    public MenuItem setCheckable(boolean z) {
        this.mCheckable = z;
        return this;
    }

    @Override // android.view.MenuItem
    public boolean isCheckable() {
        return this.mCheckable;
    }

    @Override // android.view.MenuItem
    public MenuItem setChecked(boolean z) {
        this.mChecked = z;
        return this;
    }

    @Override // android.view.MenuItem
    public boolean isChecked() {
        return this.mChecked;
    }

    @Override // android.view.MenuItem
    public MenuItem setVisible(boolean z) {
        this.isVisible = z;
        return this;
    }

    @Override // android.view.MenuItem
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override // android.view.MenuItem
    public MenuItem setEnabled(boolean z) {
        this.isEnabled = z;
        return this;
    }

    @Override // android.view.MenuItem
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override // android.view.MenuItem
    public MenuItem setOnMenuItemClickListener(MenuItem.OnMenuItemClickListener onMenuItemClickListener) {
        this.mClickListener = onMenuItemClickListener;
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean invoke() {
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = this.mClickListener;
        return onMenuItemClickListener != null && onMenuItemClickListener.onMenuItemClick(this);
    }

    @Override // android.view.MenuItem
    public char getAlphabeticShortcut() {
        return this.mShortcutAlphabeticChar;
    }

    @Override // android.view.MenuItem
    public MenuItem setAlphabeticShortcut(char c) {
        this.mShortcutAlphabeticChar = Character.toLowerCase(c);
        return this;
    }

    @Override // android.view.MenuItem
    public char getNumericShortcut() {
        return this.mShortcutNumericChar;
    }

    @Override // android.view.MenuItem
    public MenuItem setNumericShortcut(char c) {
        this.mShortcutNumericChar = c;
        return this;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean isNeedFolmeAnim() {
        return this.isNeedFolmeAnim;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setNeedFolmeAnim(boolean z) {
        this.isNeedFolmeAnim = z;
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public int getOrdering() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setSubMenu(SubMenuBuilder subMenuBuilder) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setCheckedInt(boolean z) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean setVisibleInt(boolean z) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setMenuInfo(ContextMenu.ContextMenuInfo contextMenuInfo) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public ActionProvider getSupportActionProvider() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public boolean hasCollapsibleActionView() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setIntent(Intent intent) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public Intent getIntent() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setShortcut(char c, char c2) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public boolean hasSubMenu() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public SubMenu getSubMenu() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public ContextMenu.ContextMenuInfo getMenuInfo() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setShowAsActionFlags(int i) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setActionView(View view) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setActionView(int i) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public View getActionView() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public ActionProvider getActionProvider() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public boolean expandActionView() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public boolean collapseActionView() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public boolean isActionViewExpanded() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // com.miui.gallery.view.menu.IMenuItem
    public void setExclusiveCheckable(boolean z) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.MenuItem
    public MenuItem setOnActionExpandListener(MenuItem.OnActionExpandListener onActionExpandListener) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }
}
