package com.miui.gallery.video.editor.ui.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.menu.type.BaseMenuBottomView;
import com.miui.gallery.video.editor.ui.menu.bottom.WaterMarkBottomView;
import com.miui.gallery.video.editor.ui.menu.content.BaseContentView;

/* loaded from: classes2.dex */
public class WaterMarkView extends BaseMenuBottomView {
    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return null;
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
    }

    public WaterMarkView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_watermark_content_line_guide));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return new BaseContentView(this.mContext);
    }

    @Override // com.miui.gallery.editor.ui.menu.type.BaseMenuBottomView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return new WaterMarkBottomView(this.mContext);
    }

    @Override // com.miui.gallery.editor.ui.menu.type.BaseMenuBottomView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyBottomGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(0);
    }
}
