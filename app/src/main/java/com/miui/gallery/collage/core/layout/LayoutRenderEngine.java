package com.miui.gallery.collage.core.layout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.core.RenderData;
import com.miui.gallery.collage.core.RenderEngine;
import com.miui.gallery.collage.render.CollageRender;

/* loaded from: classes.dex */
public class LayoutRenderEngine extends RenderEngine {
    public LayoutRenderEngine(Context context, BitmapManager bitmapManager) {
        super(context, bitmapManager);
    }

    @Override // com.miui.gallery.collage.core.RenderEngine
    public Bitmap render(RenderData renderData) {
        if (renderData instanceof LayoutRenderData) {
            CollageRender.RenderData renderData2 = ((LayoutRenderData) renderData).mRenderData;
            Bitmap createBitmap = Bitmap.createBitmap(renderData2.imageWidth, renderData2.imageHeight, Bitmap.Config.ARGB_8888);
            CollageRender.doRender(new Canvas(createBitmap), renderData2, this.mBitmapManager);
            return createBitmap;
        }
        return null;
    }
}
