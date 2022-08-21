package com.miui.gallery.editor.photo.core.imports.doodle.painter.shape;

import android.content.res.Resources;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;

/* loaded from: classes2.dex */
public abstract class DoodleShapeNode extends DoodleNode {
    public static final DoodleNode.DoodleDrawableType DOODLE_TYPE = DoodleNode.DoodleDrawableType.SHAPE;
    public float[] mCurrentPoint;
    public Matrix mMatrix;
    public float[] mPrePoint;

    public DoodleShapeNode(Resources resources) {
        super(resources);
        this.mMatrix = new Matrix();
        this.mPrePoint = new float[2];
        this.mCurrentPoint = new float[2];
        this.mRectF.setEmpty();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public DoodleNode.DoodleDrawableType getDoodleType() {
        return DOODLE_TYPE;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void onReceivePosition(float f, float f2, float f3, boolean z) {
        if (!z) {
            refreshRectByPoint(this.mStartPoint, this.mEndPoint);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processOnDownEvent(float f, float f2) {
        refreshPointByRect(this.mStartPoint, this.mEndPoint);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processScaleEvent(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float[] fArr = this.mCurrentPoint;
        fArr[0] = f3;
        fArr[1] = f4;
        float[] fArr2 = this.mPrePoint;
        fArr2[0] = fArr[0] - f5;
        fArr2[1] = fArr[1] - f6;
        this.mMatrix.reset();
        this.mMatrix.postRotate(-this.mDegrees, this.mRotateX + this.mUserLocationX, this.mRotateY + this.mUserLocationY);
        this.mMatrix.mapPoints(this.mPrePoint);
        this.mMatrix.mapPoints(this.mCurrentPoint);
        float[] fArr3 = this.mCurrentPoint;
        float f9 = fArr3[0];
        float[] fArr4 = this.mPrePoint;
        float f10 = f9 - fArr4[0];
        float f11 = fArr3[1] - fArr4[1];
        PointF pointF = this.mEndPoint;
        pointF.x -= f10;
        pointF.y -= f11;
        refreshRectByPoint(this.mStartPoint, pointF);
        countDecoration();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void appendScale(float f) {
        float width = this.mRectF.width();
        float height = this.mRectF.height();
        float f2 = ((width * f) - width) / 2.0f;
        float f3 = ((f * height) - height) / 2.0f;
        PointF pointF = this.mStartPoint;
        float f4 = pointF.x;
        PointF pointF2 = this.mEndPoint;
        if (f4 > pointF2.x) {
            f2 = -f2;
        }
        float f5 = pointF.y;
        if (f5 > pointF2.y) {
            f3 = -f3;
        }
        pointF.x = f4 - f2;
        pointF.y = f5 - f3;
        pointF2.x += f2;
        pointF2.y += f3;
        refreshRectByPoint(pointF, pointF2);
        countDecoration();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public float countRotateX() {
        return this.mRectF.centerX();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public float countRotateY() {
        return this.mRectF.centerY();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void count() {
        PointF pointF = this.mEndPoint;
        RectF rectF = this.mRectF;
        pointF.x = rectF.right;
        pointF.y = rectF.bottom;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processOnUp() {
        super.processOnUp();
        PointF pointF = this.mEndPoint;
        RectF rectF = this.mRectF;
        pointF.x = rectF.right;
        pointF.y = rectF.bottom;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    public DoodleShapeNode(Parcel parcel) {
        super(parcel);
        this.mMatrix = new Matrix();
        this.mPrePoint = new float[2];
        this.mCurrentPoint = new float[2];
    }
}
