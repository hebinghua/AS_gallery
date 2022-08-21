package com.miui.gallery.editor.photo.screen.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import com.miui.gallery.editor.photo.core.common.model.BaseDrawNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.shape.DoodleShapeNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.vector.DoodleVectorNode;
import com.miui.gallery.editor.photo.screen.mosaic.MosaicNode;
import com.miui.gallery.editor.photo.screen.text.ScreenTextDrawNode;
import com.miui.gallery.util.Bitmaps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class ScreenDrawEntry {
    public Bitmap mErasableBitmap;
    public Canvas mErasableCanvas;
    public List<BaseDrawNode> mNodeList;
    public RectF mSrcRect;
    public Bitmap mTmpBitmap;
    public Canvas mTmpBitmapCanvas;
    public Paint mPaint = new Paint();
    public Paint mClearPaint = new Paint(2);

    /* renamed from: $r8$lambda$cy0mX7geSTt-3diqY5ei4K42YOc */
    public static /* synthetic */ void m920$r8$lambda$cy0mX7geSTt3diqY5ei4K42YOc(ScreenDrawEntry screenDrawEntry, RectF rectF, BaseDrawNode baseDrawNode) {
        screenDrawEntry.lambda$draw$0(rectF, baseDrawNode);
    }

    public ScreenDrawEntry(boolean z, RectF rectF, List<BaseDrawNode> list) {
        this.mSrcRect = rectF;
        this.mNodeList = new ArrayList(list);
        this.mClearPaint.setAntiAlias(true);
        this.mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void draw(Canvas canvas, final RectF rectF) {
        ArrayList arrayList = new ArrayList();
        for (BaseDrawNode baseDrawNode : this.mNodeList) {
            baseDrawNode.reset();
            if (isShape(baseDrawNode)) {
                arrayList.add(baseDrawNode);
            } else {
                this.mPaint.setAlpha(255);
                this.mTmpBitmapCanvas.drawPaint(this.mClearPaint);
                Canvas canvas2 = this.mTmpBitmapCanvas;
                if (baseDrawNode.getDoodlePen() != null) {
                    this.mPaint.setAlpha(baseDrawNode.getAlpha());
                    canvas2 = baseDrawNode.getDoodlePen().isEraser() ? this.mErasableCanvas : this.mTmpBitmapCanvas;
                }
                baseDrawNode.draw(canvas2, rectF);
                this.mErasableCanvas.drawBitmap(this.mTmpBitmap, 0.0f, 0.0f, this.mPaint);
            }
        }
        arrayList.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.screen.core.ScreenDrawEntry$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ScreenDrawEntry.m920$r8$lambda$cy0mX7geSTt3diqY5ei4K42YOc(ScreenDrawEntry.this, rectF, (BaseDrawNode) obj);
            }
        });
        this.mPaint.setAlpha(255);
        canvas.drawBitmap(this.mErasableBitmap, 0.0f, 0.0f, this.mPaint);
        DoodleNode.relese();
    }

    public /* synthetic */ void lambda$draw$0(RectF rectF, BaseDrawNode baseDrawNode) {
        baseDrawNode.draw(this.mErasableCanvas, rectF);
    }

    public Bitmap apply(Bitmap bitmap) {
        Bitmap copyBitmapInCaseOfRecycle = Bitmaps.copyBitmapInCaseOfRecycle(bitmap);
        if (copyBitmapInCaseOfRecycle == null) {
            return null;
        }
        this.mTmpBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        this.mTmpBitmapCanvas = new Canvas(this.mTmpBitmap);
        this.mErasableBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        this.mErasableCanvas = new Canvas(this.mErasableBitmap);
        Canvas canvas = new Canvas(copyBitmapInCaseOfRecycle);
        draw(canvas, new RectF(canvas.getClipBounds()));
        return copyBitmapInCaseOfRecycle;
    }

    public void putStat(HashMap<String, String> hashMap) {
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (BaseDrawNode baseDrawNode : this.mNodeList) {
            if (baseDrawNode instanceof DoodleNode) {
                i++;
            } else if (baseDrawNode instanceof ScreenTextDrawNode) {
                i2++;
            } else if (baseDrawNode instanceof MosaicNode) {
                i3++;
            }
        }
        hashMap.put("doodleCount", String.valueOf(i));
        hashMap.put("textCount", String.valueOf(i2));
        hashMap.put("mosaicCount", String.valueOf(i3));
        hashMap.put("nodeCount", String.valueOf(this.mNodeList.size()));
    }

    public final boolean isShape(BaseDrawNode baseDrawNode) {
        return (baseDrawNode instanceof DoodleVectorNode) || (baseDrawNode instanceof DoodleShapeNode);
    }
}
