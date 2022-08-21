package com.miui.gallery.editor.photo.core.imports.remover;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.BaseRenderView;

/* loaded from: classes2.dex */
public class RemoverRenderView extends BaseRenderView {
    public RemoverRenderView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initContentView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_remover_render_layout, null);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initTopView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_doodle_render_top_view, null);
    }
}
