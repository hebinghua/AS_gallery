package com.miui.gallery.collage.core.poster;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.core.RenderData;
import com.miui.gallery.collage.core.RenderEngine;
import com.miui.gallery.collage.render.CollageRender;

/* loaded from: classes.dex */
public class PosterRenderEngine extends RenderEngine {
    public PosterRenderEngine(Context context, BitmapManager bitmapManager) {
        super(context, bitmapManager);
    }

    @Override // com.miui.gallery.collage.core.RenderEngine
    public Bitmap render(RenderData renderData) {
        if (renderData instanceof PosterRenderData) {
            CollageRender.RenderData renderData2 = ((PosterRenderData) renderData).mRenderData;
            renderData2.posterElementRender.initialize(renderData2.posterModel, renderData2.imageWidth, renderData2.imageHeight, renderData2.scaleOffset, this.mContext);
            Bitmap createBitmap = Bitmap.createBitmap(renderData2.imageWidth, renderData2.imageHeight, Bitmap.Config.ARGB_8888);
            CollageRender.doRender(new Canvas(createBitmap), renderData2, this.mBitmapManager);
            return createBitmap;
        }
        return null;
    }
}
