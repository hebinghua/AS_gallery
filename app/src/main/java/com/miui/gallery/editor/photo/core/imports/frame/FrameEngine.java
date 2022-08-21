package com.miui.gallery.editor.photo.core.imports.frame;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;

/* loaded from: classes2.dex */
public class FrameEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        if (renderData instanceof FrameRenderData) {
            return ((FrameRenderData) renderData).mFrameEntry.apply(bitmap, z);
        }
        return null;
    }
}
