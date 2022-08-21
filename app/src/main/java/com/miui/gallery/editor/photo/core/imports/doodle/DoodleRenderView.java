package com.miui.gallery.editor.photo.core.imports.doodle;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.BaseRenderView;

/* loaded from: classes2.dex */
public class DoodleRenderView extends BaseRenderView {
    public DoodleRenderView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initContentView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_doodle_render_content_view, null);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initTopView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_doodle_render_top_view, null);
    }
}
