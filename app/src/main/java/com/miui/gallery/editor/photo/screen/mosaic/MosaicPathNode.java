package com.miui.gallery.editor.photo.screen.mosaic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import com.miui.gallery.util.MatrixUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MosaicPathNode extends MosaicNode {
    public static float sDefaultSize = 98.0f;
    public Paint mPaint;
    public List<PointF> mPointFList = new ArrayList();
    public Path mPath = new Path();
    public Path mTempPath = new Path();

    @Override // com.miui.gallery.editor.photo.screen.mosaic.MosaicNode
    public void init() {
        initPaint();
    }

    public final void initPaint() {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStrokeWidth(sDefaultSize);
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.MosaicNode
    public void setPaintSize(float f) {
        this.mPaint.setStrokeWidth(f / MatrixUtil.getScale(this.mBitmapDisplayMatrix));
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void draw(Canvas canvas) {
        canvas.save();
        RectF rectF = this.mDisplayBitmapRect;
        canvas.translate(-rectF.left, -rectF.top);
        this.mTempPath.set(this.mPath);
        Path path = this.mTempPath;
        RectF rectF2 = this.mDisplayBitmapRect;
        path.offset(rectF2.left, rectF2.top);
        canvas.drawPath(this.mTempPath, this.mPaint);
        canvas.restore();
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.MosaicNode
    public void setShader(Shader shader) {
        this.mPaint.setShader(shader);
    }

    @Override // com.miui.gallery.editor.photo.screen.mosaic.MosaicNode
    public void onReceivePosition(float f, float f2, boolean z) {
        if (this.mPath.isEmpty()) {
            this.mPath.moveTo(f, f2);
        } else {
            List<PointF> list = this.mPointFList;
            PointF pointF = list.get(list.size() - 1);
            float f3 = pointF.x;
            float f4 = pointF.y;
            this.mPath.quadTo(f3, f4, (f3 + f) / 2.0f, (f4 + f2) / 2.0f);
        }
        this.mPointFList.add(new PointF(f, f2));
    }
}
