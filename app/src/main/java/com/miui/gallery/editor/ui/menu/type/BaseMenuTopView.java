package com.miui.gallery.editor.ui.menu.type;

import android.content.Context;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.editor.R$dimen;

/* loaded from: classes2.dex */
public abstract class BaseMenuTopView extends BaseMenuBottomView {
    public BaseMenuTopView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.editor_filter_and_adjust_top_guide_line_end));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.editor_menu_filter_guide_line_end));
    }
}
