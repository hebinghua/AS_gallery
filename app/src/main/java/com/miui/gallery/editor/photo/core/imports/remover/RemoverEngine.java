package com.miui.gallery.editor.photo.core.imports.remover;

import android.graphics.Bitmap;
import android.graphics.RectF;
import com.miui.gallery.editor.photo.app.remover.Inpaint;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.CounterUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class RemoverEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        CounterUtil counterUtil = new CounterUtil("RemoverEngine");
        if (renderData instanceof RemoverRenderData) {
            List<RemoverPaintData> list = ((RemoverRenderData) renderData).mPaintData;
            if (list != null && !list.isEmpty()) {
                Inpaint.initialize();
                Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ALPHA_8);
                for (RemoverPaintData removerPaintData : list) {
                    RemoverGestureView.export(createBitmap, removerPaintData, removerPaintData.mCurves);
                    RectF rectF = new RectF();
                    RemoverGestureView.getMaskBounds(rectF, createBitmap.getWidth(), createBitmap.getHeight(), removerPaintData, removerPaintData.mCurves);
                    if (!rectF.isEmpty()) {
                        Inpaint.upsampleBmpData(bitmap, createBitmap, bitmap.getWidth(), bitmap.getHeight(), (int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom, removerPaintData.mRemoverNNFData);
                    }
                }
                BaseBitmapUtils.recycleSilently(createBitmap);
                Inpaint.release();
                counterUtil.tick("remove render done");
            }
            return bitmap;
        }
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public void release() {
        Inpaint.release();
    }
}
