package com.miui.gallery.editor.photo.core.imports.doodle.painter.path;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleItem;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelablePathUtils;
import com.miui.gallery.util.parcelable.ParcelableMatrix;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DoodlePathNode extends DoodleNode {
    public Matrix mMatrix;
    public ParcelableMatrix mParcelableMatrix;
    public Path mPath;
    public List<PointF> mPointFList;
    public static final DoodleNode.DoodleDrawableType DOODLE_TYPE = DoodleNode.DoodleDrawableType.PATH;
    public static final DoodleItem DOODLE_ITEM = DoodleItem.PATH;
    public static final Parcelable.Creator<DoodlePathNode> CREATOR = new Parcelable.Creator<DoodlePathNode>() { // from class: com.miui.gallery.editor.photo.core.imports.doodle.painter.path.DoodlePathNode.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public DoodlePathNode mo790createFromParcel(Parcel parcel) {
            return new DoodlePathNode(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public DoodlePathNode[] mo791newArray(int i) {
            return new DoodlePathNode[i];
        }
    };

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processOnDownEvent(float f, float f2) {
    }

    public DoodlePathNode(Resources resources) {
        super(resources);
        this.mPath = new Path();
        this.mPointFList = new ArrayList();
        this.mMatrix = new Matrix();
        this.mParcelableMatrix = new ParcelableMatrix();
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
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restore();
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
    public void count() {
        this.mPath.computeBounds(this.mRectF, true);
    }

    public void append(float f, float f2) {
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

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void appendScale(float f) {
        if (f >= 1.0f || this.mScale >= 0.2d) {
            this.mScale *= f;
            this.mMatrix.reset();
            this.mMatrix.postScale(f, f, this.mRectF.centerX(), this.mRectF.centerY());
            this.mParcelableMatrix.postConcat(this.mMatrix);
            this.mPath.transform(this.mMatrix);
            countDecoration();
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void onReceivePosition(float f, float f2, float f3, boolean z) {
        append(f, f2);
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

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeTypedList(this.mPointFList);
        parcel.writeParcelable(this.mParcelableMatrix, i);
    }

    public DoodlePathNode(Parcel parcel) {
        super(parcel);
        this.mPath = new Path();
        this.mPointFList = new ArrayList();
        this.mMatrix = new Matrix();
        this.mParcelableMatrix = new ParcelableMatrix();
        this.mPointFList = parcel.createTypedArrayList(PointF.CREATOR);
        this.mParcelableMatrix = (ParcelableMatrix) parcel.readParcelable(ParcelableMatrix.class.getClassLoader());
    }
}
