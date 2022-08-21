package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.menu.BaseMenuView;

/* loaded from: classes2.dex */
public abstract class PhotoEditorMenuView extends BaseMenuView {
    public Guideline mBottomGuideline;
    public Guideline mContentGuideline;

    public PhotoEditorMenuView(Context context) {
        super(context);
    }

    public PhotoEditorMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void inflateView() {
        FrameLayout.inflate(this.mContext, R.layout.common_editor_menu_layout_rotatable, this);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return LayoutInflater.from(this.mContext).inflate(R.layout.common_editor_menu_apply_layout_rotatable, (ViewGroup) frameLayout, false);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public final void modifyBottomGuideline(Guideline guideline) {
        this.mBottomGuideline = guideline;
        guideline.setGuidelineEnd(0);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        this.mContentGuideline = guideline;
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_menu_bottom_region_height));
    }
}
