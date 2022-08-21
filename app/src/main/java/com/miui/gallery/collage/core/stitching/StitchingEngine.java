package com.miui.gallery.collage.core.stitching;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.core.RenderData;
import com.miui.gallery.collage.core.RenderEngine;
import com.miui.gallery.collage.render.BitmapItemRender;
import com.miui.gallery.collage.render.CollageRender;
import com.miui.gallery.collage.widget.CollageStitchingLayout;
import com.miui.gallery.util.MemoryUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexChecker;

/* loaded from: classes.dex */
public class StitchingEngine extends RenderEngine {
    public StitchingEngine(Context context, BitmapManager bitmapManager) {
        super(context, bitmapManager);
    }

    @Override // com.miui.gallery.collage.core.RenderEngine
    public Bitmap render(RenderData renderData) {
        Bitmap bitmap;
        StitchingEngine stitchingEngine = this;
        if (renderData instanceof StitchingRenderData) {
            StitchingRenderData stitchingRenderData = (StitchingRenderData) renderData;
            CollageStitchingLayout.RenderData renderData2 = stitchingRenderData.mRenderData;
            CollageRender.BitmapRenderData[] bitmapRenderDataArr = renderData2.bitmapRenderDatas;
            StitchingModel stitchingModel = renderData2.stitchingModel;
            int exportBitmapWidth = getExportBitmapWidth();
            float f = exportBitmapWidth / stitchingRenderData.mRenderData.viewWidth;
            CollageStitchingLayout.BitmapPositionHolder bitmapPositionHolder = new CollageStitchingLayout.BitmapPositionHolder(bitmapRenderDataArr.length);
            stitchingModel.countHeight(exportBitmapWidth, bitmapPositionHolder, bitmapRenderDataArr);
            int i = bitmapPositionHolder.height;
            int i2 = bitmapPositionHolder.verticalOffset;
            int i3 = bitmapPositionHolder.horizontalOffset;
            BitmapItemRender bitmapItemRender = new BitmapItemRender();
            RectF rectF = new RectF();
            Bitmap createBitmap = Bitmap.createBitmap(exportBitmapWidth, i, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(-1);
            int i4 = 0;
            int i5 = i3 > 0 ? i2 + 0 : 0;
            while (i4 < bitmapRenderDataArr.length) {
                CollageRender.BitmapRenderData bitmapRenderData = bitmapRenderDataArr[i4];
                Bitmap bitmap2 = bitmapRenderData.bitmap;
                CollageRender.BitmapRenderData[] bitmapRenderDataArr2 = bitmapRenderDataArr;
                BitmapManager bitmapManager = stitchingEngine.mBitmapManager;
                if (bitmapManager != null) {
                    bitmap = createBitmap;
                    bitmapRenderData.bitmap = stitchingEngine.mBitmapManager.loadSuitableBitmapBySize(exportBitmapWidth, i, bitmapManager.getOriginUriByBitmap(bitmap2));
                } else {
                    bitmap = createBitmap;
                }
                int i6 = bitmapPositionHolder.bitmapHeights[i4] + i5;
                rectF.set(i3, i5, bitmapPositionHolder.bitmapWidth + i3, i6);
                canvas.save();
                canvas.clipRect(rectF);
                bitmapItemRender.drawBitmapItem(bitmapRenderData, rectF, canvas, f);
                canvas.restore();
                i5 = i6 + i2;
                bitmapRenderData.bitmap = bitmap2;
                i4++;
                stitchingEngine = this;
                bitmapRenderDataArr = bitmapRenderDataArr2;
                createBitmap = bitmap;
                bitmapPositionHolder = bitmapPositionHolder;
            }
            return createBitmap;
        }
        return null;
    }

    public final int getExportBitmapWidth() {
        DefaultLogger.d("StitchingEngine", "current RAM : " + MemoryUtils.getCurrentUsableRam(this.mContext));
        int ceil = (int) Math.ceil((((((double) MemoryUtils.getTotalRam(this.mContext)) * 1.0d) / 1024.0d) / 1024.0d) / 1024.0d);
        int i = ceil >= 6 ? nexChecker.UHD_HEIGHT : ceil >= 4 ? 1500 : 1080;
        DefaultLogger.d("StitchingEngine", "totalRam : %d , resultWidth is : %d", Integer.valueOf(ceil), Integer.valueOf(i));
        return i;
    }
}
