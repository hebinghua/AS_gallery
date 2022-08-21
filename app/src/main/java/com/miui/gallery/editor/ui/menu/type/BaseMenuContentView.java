package com.miui.gallery.editor.ui.menu.type;

import android.content.Context;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.editor.R$dimen;

/* loaded from: classes2.dex */
public abstract class BaseMenuContentView extends BaseMenuBottomView {
    public BaseMenuContentView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R$dimen.editor_menu_smart_video_guide_line_end));
    }
}
