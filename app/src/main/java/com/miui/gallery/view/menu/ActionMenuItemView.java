package com.miui.gallery.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import com.miui.gallery.R;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.view.menu.MenuBuilder;

/* loaded from: classes2.dex */
public class ActionMenuItemView extends Button implements MenuView$ItemView {
    public boolean mIsCheckable;
    public IMenuItem mItemData;
    public MenuBuilder.ItemInvoker mItemInvoker;
    public final boolean mWithText;

    public static /* synthetic */ void $r8$lambda$DtglH9TIKm3AbWRJlLNJ73ct0mY(ActionMenuItemView actionMenuItemView) {
        actionMenuItemView.lambda$initialize$0();
    }

    @Override // com.miui.gallery.view.menu.MenuView$ItemView
    public boolean prefersCondensedTitle() {
        return false;
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mWithText = getResources().getBoolean(R.bool.bottomMenu_config_withText);
    }

    @Override // com.miui.gallery.view.menu.MenuView$ItemView
    public void initialize(IMenuItem iMenuItem, int i) {
        this.mItemData = iMenuItem;
        setSelected(iMenuItem.isSelected());
        setTitle(iMenuItem.getTitle());
        setIcon(iMenuItem.getIcon());
        setCheckable(iMenuItem.isCheckable());
        setChecked(iMenuItem.isChecked());
        setEnabled(iMenuItem.isEnabled());
        setClickable(true);
        tagById(iMenuItem.getItemId());
        if (iMenuItem.isNeedFolmeAnim()) {
            post(new Runnable() { // from class: com.miui.gallery.view.menu.ActionMenuItemView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ActionMenuItemView.$r8$lambda$DtglH9TIKm3AbWRJlLNJ73ct0mY(ActionMenuItemView.this);
                }
            });
        }
    }

    public /* synthetic */ void lambda$initialize$0() {
        FolmeUtil.addAlphaPressAnim(this);
    }

    public IMenuItem getItemData() {
        return this.mItemData;
    }

    public void setTitle(CharSequence charSequence) {
        setText(this.mWithText ? charSequence : null);
        setContentDescription(charSequence);
    }

    public void setCheckable(boolean z) {
        this.mIsCheckable = z;
    }

    public void setChecked(boolean z) {
        if (this.mIsCheckable) {
            setSelected(z);
        }
    }

    public void setIcon(Drawable drawable) {
        if (getCompoundDrawables()[1] != drawable) {
            setCompoundDrawablesWithIntrinsicBounds((Drawable) null, drawable, (Drawable) null, (Drawable) null);
        }
    }

    @Override // android.view.View
    public boolean performClick() {
        if (super.performClick()) {
            return true;
        }
        MenuBuilder.ItemInvoker itemInvoker = this.mItemInvoker;
        if (itemInvoker == null || !itemInvoker.invokeItem(this.mItemData)) {
            return false;
        }
        playSoundEffect(0);
        return true;
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public final void tagById(int i) {
        setTag(Integer.valueOf(i));
    }
}
