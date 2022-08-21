package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.imports.filter.render.OpenGlUtils;

/* loaded from: classes2.dex */
public class MosaicEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        if (renderData instanceof MosaicRenderData) {
            return ((MosaicRenderData) renderData).mMosaicEntry.apply(OpenGlUtils.ensureBitmapSize(bitmap));
        }
        return null;
    }
}
