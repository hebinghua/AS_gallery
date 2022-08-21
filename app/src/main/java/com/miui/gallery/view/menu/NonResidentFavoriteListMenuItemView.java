package com.miui.gallery.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.miui.gallery.R;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.internal.util.AttributeResolver;

/* loaded from: classes2.dex */
public class NonResidentFavoriteListMenuItemView extends ListMenuItemView {
    public NonResidentFavoriteListMenuItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.view.menu.ListMenuItemView
    public void setIcon(Drawable drawable) {
        super.setIcon(drawable);
        if (this.mItemData.isExclusiveCheckable()) {
            DefaultLogger.e("NonResidentFavoriteListMenuItemView", "不支持RadioButton");
            return;
        }
        if (this.mCheckBox == null) {
            insertCheckBox();
        }
        CheckBox checkBox = this.mCheckBox;
        if (checkBox == null) {
            return;
        }
        checkBox.setButtonDrawable(drawable);
    }

    @Override // com.miui.gallery.view.menu.ListMenuItemView
    public void insertCheckBox() {
        CheckBox checkBox = (CheckBox) getInflater().inflate(R.layout.list_menu_item_checkbox, (ViewGroup) this, false);
        this.mCheckBox = checkBox;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) checkBox.getLayoutParams();
        if (marginLayoutParams == null) {
            marginLayoutParams = new ViewGroup.MarginLayoutParams(-2, -2);
        }
        marginLayoutParams.setMarginEnd(AttributeResolver.resolveDimensionPixelSize(getContext(), 16843684));
        addView(this.mCheckBox, marginLayoutParams);
    }
}
