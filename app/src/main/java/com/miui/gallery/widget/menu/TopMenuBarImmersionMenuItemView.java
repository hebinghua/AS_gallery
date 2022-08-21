package com.miui.gallery.widget.menu;

import android.content.Context;
import android.util.AttributeSet;
import com.miui.gallery.R;
import com.miui.gallery.view.menu.IMenuItem;

/* loaded from: classes2.dex */
public class TopMenuBarImmersionMenuItemView extends ImmersionMenuItemView {
    public TopMenuBarImmersionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TopMenuBarImmersionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuItemView
    public int getPortPadding() {
        return getResources().getDimensionPixelSize(R.dimen.photo_page_top_bar_immersion_menu_item_icon_portrait_padding);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuItemView
    public int getLandPadding() {
        return getResources().getDimensionPixelSize(R.dimen.photo_page_top_bar_immersion_menu_item_icon_horizontal_padding);
    }

    @Override // com.miui.gallery.widget.menu.ImmersionMenuItemView, com.miui.gallery.view.menu.MenuView$ItemView
    public void initialize(IMenuItem iMenuItem, int i) {
        super.initialize(iMenuItem, i);
        setMinimumWidth(getResources().getDimensionPixelSize(R.dimen.photo_page_top_menu_bar_immersion_menu_item_min_width));
    }
}
