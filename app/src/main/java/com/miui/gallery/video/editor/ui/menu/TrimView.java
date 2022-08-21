package com.miui.gallery.video.editor.ui.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.menu.type.BaseMenuTopView;

/* loaded from: classes2.dex */
public class TrimView extends BaseMenuTopView {
    public TrimView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.video_editor_trim_content_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.video_editor_trim_top_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.type.BaseMenuTopView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_trim_top_guide_line_end));
    }

    @Override // com.miui.gallery.editor.ui.menu.type.BaseMenuTopView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_trim_content_guide_line_end));
    }
}
