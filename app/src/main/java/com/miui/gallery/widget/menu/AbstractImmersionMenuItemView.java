package com.miui.gallery.widget.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuBuilder;
import com.miui.gallery.view.menu.MenuView$ItemView;

/* loaded from: classes2.dex */
public abstract class AbstractImmersionMenuItemView extends ImageView implements MenuView$ItemView {
    public abstract /* synthetic */ IMenuItem getItemData();

    @Override // com.miui.gallery.view.menu.MenuView$ItemView
    public boolean prefersCondensedTitle() {
        return false;
    }

    public abstract /* synthetic */ void setCheckable(boolean z);

    public abstract /* synthetic */ void setChecked(boolean z);

    public abstract /* synthetic */ void setIcon(Drawable drawable);

    public abstract /* synthetic */ void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker);

    public void setTitle(CharSequence charSequence) {
    }

    public AbstractImmersionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AbstractImmersionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
