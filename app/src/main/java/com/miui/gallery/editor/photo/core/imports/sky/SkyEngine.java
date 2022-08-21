package com.miui.gallery.editor.photo.core.imports.sky;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyCheckHelper;
import com.miui.gallery.editor.photo.app.sky.sdk.SkyTransferTempData;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.skytransfer.SkyTranFilter;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class SkyEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        boolean z2;
        SkyRenderData skyRenderData = (SkyRenderData) renderData;
        if (!skyRenderData.isVideo()) {
            SkyTransferTempData renderTempData = skyRenderData.getRenderTempData();
            if (renderTempData == null) {
                renderTempData = SkyTranFilter.getInstance().getTransferTempData();
                skyRenderData.setRenderTempData(renderTempData);
                DefaultLogger.d("SkyEngine", "preview");
                z2 = false;
            } else {
                DefaultLogger.d("SkyEngine", MiStat.Param.ORIGIN);
                z2 = true;
            }
            SkyTranFilter.getInstance().transferSkyForSave(bitmap, skyRenderData, renderTempData, z2);
            return bitmap;
        }
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public void release() {
        super.release();
        if (SkyCheckHelper.isSkyEnable()) {
            SkyTranFilter.getInstance().releaseSeqAsync();
        }
    }
}
