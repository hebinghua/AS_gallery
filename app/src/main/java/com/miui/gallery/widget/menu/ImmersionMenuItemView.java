package com.miui.gallery.widget.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.miui.gallery.R;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.view.menu.IMenuItem;
import com.miui.gallery.view.menu.MenuBuilder;

/* loaded from: classes2.dex */
public class ImmersionMenuItemView extends AbstractImmersionMenuItemView {
    public boolean mIsCheckable;
    public IMenuItem mItemData;
    public MenuBuilder.ItemInvoker mItemInvoker;

    public static /* synthetic */ void $r8$lambda$fn2uXhnXNcxRWQjjvPxocxJbolQ(ImmersionMenuItemView immersionMenuItemView) {
        immersionMenuItemView.lambda$new$0();
    }

    public ImmersionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ImmersionMenuItemView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        int portPadding = getPortPadding();
        int landPadding = getLandPadding();
        setPadding(landPadding, portPadding, landPadding, portPadding);
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.widget.menu.ImmersionMenuItemView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ImmersionMenuItemView.$r8$lambda$fn2uXhnXNcxRWQjjvPxocxJbolQ(ImmersionMenuItemView.this);
            }
        });
        if (!LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            setHapticFeedbackEnabled(true);
        }
    }

    public /* synthetic */ void lambda$new$0() {
        FolmeUtil.addAlphaPressAnim(this);
    }

    public int getPortPadding() {
        return getResources().getDimensionPixelSize(R.dimen.immersion_menu_item_icon_portrait_padding);
    }

    public int getLandPadding() {
        return getResources().getDimensionPixelSize(R.dimen.immersion_menu_item_icon_horizontal_padding);
    }

    @Override // com.miui.gallery.view.menu.MenuView$ItemView
    public void initialize(IMenuItem iMenuItem, int i) {
        this.mItemData = iMenuItem;
        setSelected(false);
        setIcon(iMenuItem.getIcon());
        setCheckable(iMenuItem.isCheckable());
        setChecked(iMenuItem.isChecked());
        setEnabled(iMenuItem.isEnabled());
        setClickable(true);
        setContentDescription(iMenuItem.getTitle());
        tagById(iMenuItem.getItemId());
    }

    @Override // com.miui.gallery.widget.menu.AbstractImmersionMenuItemView
    public IMenuItem getItemData() {
        return this.mItemData;
    }

    @Override // com.miui.gallery.widget.menu.AbstractImmersionMenuItemView
    public void setCheckable(boolean z) {
        this.mIsCheckable = z;
    }

    @Override // com.miui.gallery.widget.menu.AbstractImmersionMenuItemView
    public void setChecked(boolean z) {
        if (this.mIsCheckable) {
            setSelected(z);
        }
    }

    @Override // com.miui.gallery.widget.menu.AbstractImmersionMenuItemView
    public void setIcon(Drawable drawable) {
        if (getDrawable() != drawable) {
            setImageDrawable(drawable);
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
        if (!LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            playSoundEffect(0);
        }
        return true;
    }

    @Override // com.miui.gallery.widget.menu.AbstractImmersionMenuItemView
    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public final void tagById(int i) {
        setTag(Integer.valueOf(i));
    }
}
