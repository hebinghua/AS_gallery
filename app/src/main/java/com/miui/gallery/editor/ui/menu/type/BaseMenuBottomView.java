package com.miui.gallery.editor.ui.menu.type;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.editor.ui.menu.BaseMenuView;
import com.miui.gallery.editor.ui.menu.bottom.BaseEditBottomView;

/* loaded from: classes2.dex */
public abstract class BaseMenuBottomView extends BaseMenuView {
    public BaseMenuBottomView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return new BaseEditBottomView(this.mContext);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyBottomGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(0);
    }
}
