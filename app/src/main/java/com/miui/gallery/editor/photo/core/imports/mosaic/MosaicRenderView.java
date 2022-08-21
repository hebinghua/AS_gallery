package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.BaseRenderView;

/* loaded from: classes2.dex */
public class MosaicRenderView extends BaseRenderView {
    public MosaicRenderView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public void modifyContentGuideLine(Guideline guideline) {
        super.modifyContentGuideLine(guideline);
        guideline.setGuidelineBegin(0);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initContentView() {
        return FrameLayout.inflate(getContext(), R.layout.mosaic_render_layout, null);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initTopView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_doodle_render_top_view, null);
    }
}
