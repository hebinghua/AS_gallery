package com.miui.gallery.editor.photo.app.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView;

/* loaded from: classes2.dex */
public class BeautifyView extends PhotoEditorMenuView {
    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return null;
    }

    public BeautifyView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.beautify_menu_content_view, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_menu_height));
    }
}
