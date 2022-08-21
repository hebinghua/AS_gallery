package com.miui.gallery.editor.photo.core.imports.doodle;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;

/* loaded from: classes2.dex */
public class DoodleEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        if (renderData instanceof DoodleRenderData) {
            return ((DoodleRenderData) renderData).mDoodleEntry.apply(bitmap);
        }
        return null;
    }
}
