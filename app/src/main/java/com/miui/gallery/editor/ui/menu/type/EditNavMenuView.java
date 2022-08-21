package com.miui.gallery.editor.ui.menu.type;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.editor.R$layout;
import com.miui.gallery.editor.ui.menu.BaseMenuView;

/* loaded from: classes2.dex */
public class EditNavMenuView extends BaseMenuView {
    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return null;
    }

    public EditNavMenuView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R$layout.common_edit_menu_confirm_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R$layout.common_edit_menu_nav_content_view, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.editor_menu_height));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.photo_editor_nav_guide_line_end));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyBottomGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.editor_nav_bottom_guide_line_end));
    }
}
