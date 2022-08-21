package com.miui.gallery.editor.photo.core.imports.sticker;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.BaseRenderView;

/* loaded from: classes2.dex */
public class StickerRenderView extends BaseRenderView {
    public StickerRenderView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initContentView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_sticker_render_content_view, null);
    }
}
