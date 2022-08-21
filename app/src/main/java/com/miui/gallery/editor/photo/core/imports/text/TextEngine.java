package com.miui.gallery.editor.photo.core.imports.text;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;

/* loaded from: classes2.dex */
public class TextEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        if (renderData instanceof TextRenderData) {
            return ((TextRenderData) renderData).mTextEntry.apply(bitmap);
        }
        return null;
    }
}
