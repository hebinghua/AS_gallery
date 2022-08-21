package com.miui.gallery.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.miui.gallery.view.menu.MenuBuilder;

/* loaded from: classes2.dex */
public final class ExpandedMenuView extends ListView implements MenuBuilder.ItemInvoker, AdapterView.OnItemClickListener {
    public int mAnimations;
    public MenuBuilder mMenu;

    public ExpandedMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnItemClickListener(this);
    }

    @Override // android.widget.ListView, android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setChildrenDrawingCacheEnabled(false);
    }

    @Override // com.miui.gallery.view.menu.MenuBuilder.ItemInvoker
    public boolean invokeItem(IMenuItem iMenuItem) {
        return this.mMenu.performItemAction(iMenuItem, 0);
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        invokeItem((MenuItemImpl) getAdapter().getItem(i));
    }

    public int getWindowAnimations() {
        return this.mAnimations;
    }
}
