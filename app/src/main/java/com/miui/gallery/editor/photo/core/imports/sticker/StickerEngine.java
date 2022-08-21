package com.miui.gallery.editor.photo.core.imports.sticker;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class StickerEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        DefaultLogger.d("StickerEngine", "rendering sticker data");
        if (renderData instanceof StickerRenderData) {
            return ((StickerRenderData) renderData).mEntry.apply(bitmap);
        }
        return null;
    }
}
