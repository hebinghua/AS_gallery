package com.miui.gallery.editor.photo.core.imports.crop;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.BaseRenderView;

/* loaded from: classes2.dex */
public class CropRenderView extends BaseRenderView {
    public CropRenderView(Context context) {
        this(context, null);
    }

    public CropRenderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initContentView() {
        return FrameLayout.inflate(getContext(), R.layout.crop_preview_container, null);
    }

    @Override // com.miui.gallery.editor.photo.widgets.BaseRenderView
    public View initTopView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_title_layout, null);
    }
}
