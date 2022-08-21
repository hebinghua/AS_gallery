package com.miui.gallery.ui.photoPage.bars.data;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.miui.gallery.view.menu.IMenuItem;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class GalleryMenuBuilder implements Menu {
    public final Context mContext;
    public final ArrayList<IMenuItem> mItems = new ArrayList<>();
    public final Resources mResources;

    public GalleryMenuBuilder(Context context) {
        this.mContext = context;
        this.mResources = context.getResources();
    }

    public Context getContext() {
        return this.mContext;
    }

    public final MenuItem addInternal(int i, int i2, int i3, CharSequence charSequence) {
        GalleryMenuItem galleryMenuItem = new GalleryMenuItem(this, i, i2, i3, charSequence);
        this.mItems.add(galleryMenuItem);
        return galleryMenuItem;
    }

    @Override // android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return addInternal(0, 0, 0, charSequence);
    }

    @Override // android.view.Menu
    public MenuItem add(int i) {
        return addInternal(0, 0, 0, this.mResources.getString(i));
    }

    @Override // android.view.Menu
    public MenuItem add(int i, int i2, int i3, CharSequence charSequence) {
        return addInternal(i, i2, i3, charSequence);
    }

    @Override // android.view.Menu
    public MenuItem add(int i, int i2, int i3, int i4) {
        return addInternal(i, i2, i3, this.mResources.getString(i4));
    }

    @Override // android.view.Menu
    public int size() {
        return this.mItems.size();
    }

    @Override // android.view.Menu
    public MenuItem findItem(int i) {
        int size = size();
        for (int i2 = 0; i2 < size; i2++) {
            IMenuItem iMenuItem = this.mItems.get(i2);
            if (iMenuItem.getItemId() == i) {
                return iMenuItem;
            }
        }
        return null;
    }

    @Override // android.view.Menu
    public MenuItem getItem(int i) {
        return this.mItems.get(i);
    }

    public ArrayList<IMenuItem> getItems() {
        return this.mItems;
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(CharSequence charSequence) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i, int i2, int i3, CharSequence charSequence) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public SubMenu addSubMenu(int i, int i2, int i3, int i4) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public int addIntentOptions(int i, int i2, int i3, ComponentName componentName, Intent[] intentArr, Intent intent, int i4, MenuItem[] menuItemArr) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void removeItem(int i) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void removeGroup(int i) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void clear() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void setGroupCheckable(int i, boolean z, boolean z2) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void setGroupVisible(int i, boolean z) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void setGroupEnabled(int i, boolean z) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public boolean hasVisibleItems() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void close() {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public boolean performShortcut(int i, KeyEvent keyEvent, int i2) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public boolean isShortcutKey(int i, KeyEvent keyEvent) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public boolean performIdentifierAction(int i, int i2) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }

    @Override // android.view.Menu
    public void setQwertyMode(boolean z) {
        throw new UnsupportedOperationException("暂不支持功能 请自行实现");
    }
}
