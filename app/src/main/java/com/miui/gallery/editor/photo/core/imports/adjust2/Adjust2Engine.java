package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.miui.gallery.editor.photo.app.filter.Filter;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class Adjust2Engine extends RenderEngine {
    public Adjust2Engine(Context context) {
        Filter.initialize();
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        Bitmap bitmap2 = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            long currentTimeMillis = System.currentTimeMillis();
            if (renderData instanceof Adjust2RenderData) {
                Adjust2RenderData adjust2RenderData = (Adjust2RenderData) renderData;
                List<Adjust2Data> list = adjust2RenderData.mEffects;
                if (list == null || list.isEmpty()) {
                    return bitmap;
                }
                if (!z) {
                    bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                }
                DefaultLogger.d("AdjustEngine", "effect=%s--result=%d--scene=%d", adjust2RenderData.getFilterEffect(), Integer.valueOf(Filter.filterBmpData(bitmap, bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth(), adjust2RenderData.getFilterEffect())), Integer.valueOf(Filter.getScene()));
                bitmap2 = bitmap;
            }
            DefaultLogger.d("AdjustEngine", "consumingTime=" + (System.currentTimeMillis() - currentTimeMillis));
        }
        return bitmap2;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public void release() {
        super.release();
        new ReleaseTask().execute(new Void[0]);
    }

    /* loaded from: classes2.dex */
    public static class ReleaseTask extends AsyncTask<Void, Void, Void> {
        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            Filter.release();
            return null;
        }
    }
}
