package com.miui.gallery.editor.photo.core.imports.filter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.BaseRenderView;

/* loaded from: classes2.dex */
public class FilterRenderView extends BaseRenderView {
    public FilterRenderView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public void modifyContentGuideLine(Guideline guideline) {
        super.modifyContentGuideLine(guideline);
        guideline.setGuidelineBegin(0);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initContentView() {
        return FrameLayout.inflate(getContext(), R.layout.filter_render_layout, null);
    }
}
