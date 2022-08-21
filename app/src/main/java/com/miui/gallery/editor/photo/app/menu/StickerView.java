package com.miui.gallery.editor.photo.app.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView;

/* loaded from: classes2.dex */
public class StickerView extends PhotoEditorMenuView {
    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBackgroundColor(getResources().getColor(R.color.sticker_select_background));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_menu_sticker_top_guide_end));
    }

    @Override // com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_menu_sticker_content_guide_end));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_sticker_content_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_sticker_top_layout, null);
    }

    @Override // com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_menu_title_bottom_layout, null);
    }
}
