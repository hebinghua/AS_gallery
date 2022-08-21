package com.miui.gallery.widget.menu;

import android.content.Context;
import android.view.MenuItem;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuBuilder;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ImmersionMenu extends MenuBuilder {
    public ImmersionMenu(Context context) {
        super(context);
    }

    public final MenuItem addInternal(int i, int i2, int i3, CharSequence charSequence) {
        int ordering = MenuBuilder.getOrdering(i3);
        ImmersionMenuItem immersionMenuItem = new ImmersionMenuItem(this, this.mContext, i, i2, i3, ordering, charSequence, this.mDefaultShowAsAction);
        immersionMenuItem.setListMenuItemView(true);
        this.mItems.add(MenuBuilder.findInsertIndex(this.mItems, ordering), immersionMenuItem);
        onItemsChanged(true);
        return immersionMenuItem;
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    public MenuItem add(CharSequence charSequence) {
        return addInternal(0, 0, 0, charSequence);
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    public MenuItem add(int i) {
        return addInternal(0, 0, 0, this.mResources.getString(i));
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    public MenuItem add(int i, int i2, int i3, CharSequence charSequence) {
        return addInternal(i, i2, i3, charSequence);
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    public MenuItem add(int i, int i2, int i3, int i4) {
        return addInternal(i, i2, i3, this.mResources.getString(i4));
    }

    public ImmersionMenuItem add(int i, CharSequence charSequence) {
        return add(i, charSequence, -1);
    }

    public ImmersionMenuItem add(int i, CharSequence charSequence, int i2) {
        ImmersionMenuItem immersionMenuItem = new ImmersionMenuItem(this, this.mContext, 0, i, 0, 0, charSequence, this.mDefaultShowAsAction);
        if (i2 >= 0 && i2 <= this.mItems.size()) {
            this.mItems.add(i2, immersionMenuItem);
        } else {
            this.mItems.add(immersionMenuItem);
        }
        return immersionMenuItem;
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    public int size() {
        ArrayList<IMenuItem> arrayList = this.mItems;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    /* renamed from: getItem  reason: collision with other method in class */
    public ImmersionMenuItem mo1823getItem(int i) {
        IMenuItem iMenuItem = this.mItems.get(i);
        if (iMenuItem instanceof ImmersionMenuItem) {
            return (ImmersionMenuItem) iMenuItem;
        }
        return null;
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder, android.view.Menu
    /* renamed from: findItem  reason: collision with other method in class */
    public ImmersionMenuItem mo1822findItem(int i) {
        MenuItem findItem;
        int size = size();
        for (int i2 = 0; i2 < size; i2++) {
            IMenuItem iMenuItem = this.mItems.get(i2);
            if (iMenuItem.getItemId() == i && (iMenuItem instanceof ImmersionMenuItem)) {
                return (ImmersionMenuItem) iMenuItem;
            }
            if (iMenuItem.hasSubMenu() && (findItem = iMenuItem.getSubMenu().findItem(i)) != null && (findItem instanceof ImmersionMenuItem)) {
                return (ImmersionMenuItem) findItem;
            }
        }
        return null;
    }
}
