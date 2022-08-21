package com.miui.gallery.editor.photo.core.imports.remover2;

import android.graphics.Bitmap;
import com.miui.gallery.editor.photo.app.remover2.Inpaint2;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.util.CounterUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class Remover2Engine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        CounterUtil counterUtil = new CounterUtil("Remover2Engine");
        if (renderData instanceof Remover2RenderData) {
            List<Remover2PaintData> list = ((Remover2RenderData) renderData).mPaintData;
            if (list != null && !list.isEmpty()) {
                Bitmap.Config config = bitmap.getConfig();
                Bitmap.Config config2 = Bitmap.Config.ARGB_8888;
                if (config != config2) {
                    bitmap = bitmap.copy(config2, true);
                }
                if (bitmap == null) {
                    return null;
                }
                for (Remover2PaintData remover2PaintData : list) {
                    Inpaint2.getInstance().upsample(bitmap, remover2PaintData.mRemoverNNFData);
                }
                counterUtil.tick("remove render done");
            }
            return bitmap;
        }
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public void release() {
        super.release();
        Inpaint2.getInstance().release();
    }
}
