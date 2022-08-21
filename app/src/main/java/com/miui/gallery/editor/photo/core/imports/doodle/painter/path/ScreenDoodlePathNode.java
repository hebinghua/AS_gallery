package com.miui.gallery.editor.photo.core.imports.doodle.painter.path;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePenManager;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelablePathUtils;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenDoodlePathNode extends DoodleNode {
    public Paint mClearPaint;
    public DoodlePen mDoodlePen;
    public int mDotIndex;
    public List<Dot> mDots;
    public Paint mDstInPaint;
    public StringBuilder mKey;
    public float mLastDrawDistance;
    public float mLastScale;
    public Paint mNormalPaint;
    public ParcelableMatrix mParcelableMatrix;
    public Path mPath;
    public PathMeasure mPathMeasure;
    public List<PointF> mPointFList;
    public Resources mResources;
    public float mSpacing;
    public float[] mTempPos;
    public float[] mTempXYV;
    public float mTotalDistance;
    public static final DoodleNode.DoodleDrawableType DOODLE_TYPE = DoodleNode.DoodleDrawableType.PATH;
    public static final DoodleItem DOODLE_ITEM = DoodleItem.SCREEN_PATH;
    public static final Parcelable.Creator<DoodlePathNode> CREATOR = new Parcelable.Creator<DoodlePathNode>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.painter.path.ScreenDoodlePathNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodlePathNode mo792createFromParcel(Parcel parcel) {
            return new DoodlePathNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodlePathNode[] mo793newArray(int i) {
            return new DoodlePathNode[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void appendScale(float f) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processOnDownEvent(float f, float f2) {
    }

    public ScreenDoodlePathNode(Resources resources) {
        super(resources);
        this.mSpacing = 3.3f;
        this.mPath = new Path();
        this.mLastDrawDistance = 0.0f;
        this.mTempXYV = new float[2];
        this.mTempPos = new float[2];
        this.mDots = new ArrayList();
        this.mPathMeasure = new PathMeasure();
        this.mPointFList = new ArrayList();
        this.mParcelableMatrix = new ParcelableMatrix();
        this.mDoodlePen = DoodlePenManager.INSTANCE.getNormal();
        this.mNormalPaint = new Paint(2);
        this.mDstInPaint = new Paint(2);
        Paint paint = new Paint(2);
        this.mClearPaint = paint;
        this.mResources = resources;
        paint.setAntiAlias(true);
        this.mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mNormalPaint.setAntiAlias(true);
        this.mKey = new StringBuilder();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void init(Resources resources) {
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void initForParcelable(Context context) {
        super.initForParcelable(context);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        Path pathFromPointList = ParcelablePathUtils.getPathFromPointList(this.mPointFList);
        this.mPath = pathFromPointList;
        pathFromPointList.transform(this.mParcelableMatrix);
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void draw(Canvas canvas) {
        canvas.save();
        configCanvas(canvas, true);
        this.mKey.setLength(0);
        this.mKey.append(this.mDoodlePen.getColorInt());
        this.mKey.append(":");
        this.mKey.append(this.mDoodlePen.getSize());
        Bitmap bitmap = DoodleNode.sColoredTextureCache.get(this.mKey.toString());
        if (bitmap == null && this.mDoodlePen.getType().equals("MarkPen_01")) {
            this.mDstInPaint.setAntiAlias(true);
            this.mDstInPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
            int size = (int) this.mDoodlePen.getSize();
            bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Rect rect = new Rect(0, 0, size, size);
            Canvas canvas2 = new Canvas();
            canvas2.setBitmap(bitmap);
            canvas2.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            canvas2.drawColor(this.mDoodlePen.getColorInt(), PorterDuff.Mode.SRC);
            canvas2.drawBitmap(decodeScaledExpandResource(this.mResources, R.drawable.doodle_brush_markpen_01, size, size), (Rect) null, rect, this.mDstInPaint);
            DoodleNode.sColoredTextureCache.put(this.mKey.toString(), bitmap);
        }
        float length = this.mPathMeasure.getLength();
        this.mTotalDistance = length;
        if (this.mDots.size() > 0 && ((int) Math.ceil(length / this.mSpacing)) > 0) {
            for (int i = this.mDotIndex; i < this.mDots.size(); i++) {
                this.mDotIndex = i;
                Dot dot = this.mDots.get(i);
                drawDot(canvas, bitmap, dot.x, dot.y, dot.scale);
            }
        }
        canvas.restore();
    }

    public final void drawDot(Canvas canvas, Bitmap bitmap, float f, float f2, float f3) {
        this.mNormalPaint.setAlpha(255);
        float size = this.mDoodlePen.getSize();
        float f4 = size / 2.0f;
        if (this.mDoodlePen.isEraser()) {
            canvas.save();
            canvas.translate(f, f2);
            canvas.drawCircle(0.0f, 0.0f, size, this.mClearPaint);
            canvas.restore();
        } else if (this.mDoodlePen.getType().equals("Normal_01")) {
            canvas.save();
            canvas.translate(f, f2);
            canvas.scale(f3, f3);
            this.mNormalPaint.setColor(this.mDoodlePen.getColorInt());
            canvas.drawCircle(0.0f, 0.0f, f4, this.mNormalPaint);
            canvas.restore();
        } else if (!this.mDoodlePen.getType().equals("MarkPen_01") || bitmap == null) {
        } else {
            canvas.save();
            canvas.translate(f, f2);
            canvas.rotate(-30.0f);
            float f5 = -f4;
            canvas.drawBitmap(bitmap, f5, f5, this.mNormalPaint);
            canvas.restore();
        }
    }

    public final boolean getXYVAtDistance(Float f, float[] fArr) {
        if (this.mTotalDistance == 0.0f || f.floatValue() > this.mTotalDistance) {
            return false;
        }
        this.mPathMeasure.getPosTan(f.floatValue(), this.mTempPos, null);
        float[] fArr2 = this.mTempPos;
        fArr[0] = fArr2[0];
        fArr[1] = fArr2[1];
        return true;
    }

    public final Bitmap decodeScaledExpandResource(Resources resources, int i, int i2, int i3) {
        Bitmap decodeResource = BitmapFactory.decodeResource(resources, i);
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(decodeResource, new Rect(0, 0, decodeResource.getWidth(), decodeResource.getHeight()), new Rect(0, 0, i2, i3), (Paint) null);
        canvas.setBitmap(null);
        if (decodeResource == createBitmap) {
            return createBitmap;
        }
        decodeResource.recycle();
        return createBitmap;
    }

    public final void append(float f, float f2, float f3) {
        float f4;
        float f5;
        float f6;
        if (this.mPath.isEmpty()) {
            this.mPath.moveTo(f, f2);
        } else {
            List<PointF> list = this.mPointFList;
            PointF pointF = list.get(list.size() - 1);
            float f7 = pointF.x;
            float f8 = pointF.y;
            this.mPath.quadTo(f7, f8, (f7 + f) / 2.0f, (f8 + f2) / 2.0f);
        }
        this.mPointFList.add(new PointF(f, f2));
        this.mPathMeasure.setPath(this.mPath, false);
        float length = this.mPathMeasure.getLength();
        float f9 = length - this.mTotalDistance;
        this.mTotalDistance = length;
        float ceil = (float) Math.ceil(f9 / this.mSpacing);
        if (this.mDots.size() >= 1) {
            List<Dot> list2 = this.mDots;
            f4 = f3 - list2.get(list2.size() - 1).getT();
        } else {
            f4 = f3;
        }
        float exp = (f3 == 0.0f ? 0.0f : (f9 / f4) * 0.2f) == 0.0f ? 1.0f : (float) Math.exp((-f5) * 0.3d);
        int i = 0;
        while (getXYVAtDistance(Float.valueOf(this.mLastDrawDistance), this.mTempXYV)) {
            float[] fArr = this.mTempXYV;
            float f10 = fArr[0];
            float f11 = fArr[1];
            if (this.mDoodlePen.getNeedScale()) {
                float f12 = this.mLastScale;
                f6 = f12 + ((exp - f12) * (i / ceil));
                i++;
            } else {
                f6 = 1.0f;
            }
            this.mDots.add(new Dot(f10, f11, f6, f3));
            this.mLastDrawDistance += this.mSpacing;
        }
        this.mLastScale = exp;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public DoodleNode.DoodleDrawableType getDoodleType() {
        return DOODLE_TYPE;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public String getDoodleName() {
        return DOODLE_ITEM.name();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void onReceivePosition(float f, float f2, float f3, boolean z) {
        append(f, f2, f3);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void count() {
        this.mPath.computeBounds(this.mRectF, true);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processScaleEvent(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        getRectF(this.mRectFTemp);
        this.mRectFTemp.offset(this.mUserLocationX, this.mUserLocationY);
        float centerX = this.mRectFTemp.centerX();
        float centerY = this.mRectFTemp.centerY();
        appendScale((float) (Math.hypot(f3 - centerX, f4 - centerY) / Math.hypot(f7 - centerX, f8 - centerY)));
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public float countRotateX() {
        return this.mRectF.centerX();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public float countRotateY() {
        return this.mRectF.centerY();
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void setDoodlePen(DoodlePen doodlePen) {
        this.mDoodlePen = doodlePen.copy();
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public DoodlePen getDoodlePen() {
        return this.mDoodlePen;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void reset() {
        this.mLastDrawDistance = 0.0f;
        this.mDotIndex = 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeTypedList(this.mPointFList);
        parcel.writeParcelable(this.mParcelableMatrix, i);
    }

    /* loaded from: classes2.dex */
    public static class Dot {
        public float scale;
        public float t;
        public float x;
        public float y;

        public Dot(float f, float f2, float f3, float f4) {
            this.x = f;
            this.y = f2;
            this.scale = f3;
            this.t = f4;
        }

        public float getT() {
            return this.t;
        }
    }
}
