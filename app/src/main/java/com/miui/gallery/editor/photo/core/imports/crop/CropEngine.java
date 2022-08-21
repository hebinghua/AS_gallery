package com.miui.gallery.editor.photo.core.imports.crop;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;

/* loaded from: classes2.dex */
public class CropEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        return ((CropStateData) renderData).mEntry.apply(bitmap);
    }
}
