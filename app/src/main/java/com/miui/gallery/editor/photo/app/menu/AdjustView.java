package com.miui.gallery.editor.photo.app.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView;

/* loaded from: classes2.dex */
public class AdjustView extends PhotoEditorMenuView {
    public Guideline mTopGuideline;

    public AdjustView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_adjust_top_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return LayoutInflater.from(this.mContext).inflate(R.layout.photo_editor_list_content_layout, (ViewGroup) frameLayout, false);
    }

    @Override // com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_menu_title_bottom_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        this.mTopGuideline = guideline;
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_top_guide_line_end));
    }

    public void setTopGuidelineEnd(int i) {
        this.mTopGuideline.setGuidelineEnd(i);
    }
}