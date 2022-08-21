package com.miui.gallery.editor.photo.core.imports.filter;

import android.content.Context;
import android.graphics.Bitmap;
import com.miui.arcsoftbeauty.ArcsoftBeautyJni;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.imports.filter.render.GPUImage;
import com.xiaomi.bokeh.MiPortraitSegmenter;

/* loaded from: classes2.dex */
public class FilterEngine extends RenderEngine {
    public Context mContext;

    public FilterEngine(Context context) {
        this.mContext = context;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        if (renderData instanceof FilterRenderData) {
            FilterRenderData filterRenderData = (FilterRenderData) renderData;
            if (filterRenderData.isEmpty()) {
                return bitmap;
            }
            if (filterRenderData.isPortraitColor()) {
                FilterItem portraitColorFilterItem = filterRenderData.getPortraitColorFilterItem();
                if (!MiPortraitSegmenter.getInstance().waitSegment(bitmap)) {
                    return bitmap;
                }
                Bitmap updateEnhanceBitmap = MiPortraitSegmenter.getInstance().updateEnhanceBitmap(bitmap, (portraitColorFilterItem.progress * 1.0f) / 10.0f);
                MiPortraitSegmenter.getInstance().destroy();
                return updateEnhanceBitmap;
            }
            GPUImage gPUImage = new GPUImage(this.mContext);
            if (filterRenderData.isPortraitBeauty()) {
                smartBeauty(bitmap);
            }
            gPUImage.setFilter(filterRenderData.instantiate());
            gPUImage.setImage(bitmap);
            return gPUImage.getBitmapWithFilterApplied(true);
        }
        throw new IllegalArgumentException("expect to be FilterRenderData,your type: " + renderData.getClass().getSimpleName());
    }

    public Bitmap render(Bitmap bitmap, Metadata metadata, Object obj) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        GPUImage gPUImage = new GPUImage(this.mContext);
        if (metadata instanceof Renderable) {
            gPUImage.setFilter(((Renderable) metadata).instantiate());
            gPUImage.setImage(bitmap);
            return gPUImage.getBitmapWithFilterApplied(false);
        }
        throw new IllegalArgumentException("expect to be Renderable,your type: " + metadata.getClass().getSimpleName());
    }

    public final void smartBeauty(Bitmap bitmap) {
        if (ArcsoftBeautyJni.idBeautyAvailable()) {
            ArcsoftBeautyJni.smartBeauty(bitmap);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public void release() {
        super.release();
        if (PortraitColorCheckHelper.isPortraitEnable()) {
            MiPortraitSegmenter.getInstance().destroy();
        }
    }
}
