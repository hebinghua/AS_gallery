package com.miui.gallery.editor.photo.core.imports.doodle.painter.vector;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Parcel;
import com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode;

/* loaded from: classes2.dex */
public abstract class DoodleVectorNode extends DoodleNode {
    public static final DoodleNode.DoodleDrawableType DOODLE_TYPE = DoodleNode.DoodleDrawableType.VECTOR;
    public float[] mCurrentPoint;
    public float[] mEnd;
    public boolean mInverse;
    public Matrix mMatrix;
    public float[] mStart;

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void count() {
    }

    public abstract void drawSafely(Canvas canvas);

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processOnDownEvent(float f, float f2) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processRotateEvent(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
    }

    public DoodleVectorNode(Resources resources) {
        super(resources);
        this.mStart = new float[2];
        this.mEnd = new float[2];
        this.mMatrix = new Matrix();
        this.mCurrentPoint = new float[2];
        this.mInverse = false;
    }

    public DoodleVectorNode(Resources resources, boolean z) {
        super(resources);
        this.mStart = new float[2];
        this.mEnd = new float[2];
        this.mMatrix = new Matrix();
        this.mCurrentPoint = new float[2];
        this.mInverse = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void processScaleEvent(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float[] fArr = this.mCurrentPoint;
        fArr[0] = f3;
        fArr[1] = f4;
        float f9 = this.mRotateX + this.mUserLocationX;
        float f10 = this.mRotateY + this.mUserLocationY;
        double atan2 = Math.atan2(f8 - f10, f7 - f9);
        float[] fArr2 = this.mCurrentPoint;
        appendDegrees((float) Math.toDegrees(Math.atan2(fArr2[1] - f10, fArr2[0] - f9) - atan2));
        this.mMatrix.reset();
        this.mMatrix.postRotate(-getRotateDegrees(), f9, f10);
        this.mMatrix.mapPoints(this.mCurrentPoint);
        float[] fArr3 = this.mCurrentPoint;
        fArr3[1] = fArr3[1] - (this.mOutLineRectF.height() / 2.0f);
        if (this.mInverse) {
            fArr3[0] = fArr3[0] + this.mOutLineOffsetX;
        } else {
            fArr3[0] = fArr3[0] - this.mOutLineOffsetX;
        }
        this.mMatrix.reset();
        this.mMatrix.postRotate(getRotateDegrees(), f9, f10);
        this.mMatrix.mapPoints(this.mCurrentPoint);
        if (this.mInverse) {
            PointF pointF = this.mStartPoint;
            float[] fArr4 = this.mCurrentPoint;
            pointF.x = fArr4[0] - this.mUserLocationX;
            pointF.y = fArr4[1] - this.mUserLocationY;
        } else {
            PointF pointF2 = this.mEndPoint;
            float[] fArr5 = this.mCurrentPoint;
            pointF2.x = fArr5[0] - this.mUserLocationX;
            pointF2.y = fArr5[1] - this.mUserLocationY;
        }
        refreshRectByPoint();
        countDecoration();
    }

    public void refreshRectByPoint() {
        float strokeWidth = this.mPaint.getStrokeWidth() / 2.0f;
        PointF pointF = this.mEndPoint;
        float f = pointF.y;
        PointF pointF2 = this.mStartPoint;
        setDegrees((float) Math.toDegrees(Math.atan2(f - pointF2.y, pointF.x - pointF2.x)));
        float[] fArr = this.mStart;
        PointF pointF3 = this.mStartPoint;
        fArr[0] = pointF3.x;
        fArr[1] = pointF3.y;
        float[] fArr2 = this.mEnd;
        PointF pointF4 = this.mEndPoint;
        fArr2[0] = pointF4.x;
        fArr2[1] = pointF4.y;
        this.mRotateX = countRotateX();
        this.mRotateY = countRotateY();
        this.mMatrix.reset();
        this.mMatrix.postRotate(-getRotateDegrees(), this.mRotateX, this.mRotateY);
        this.mMatrix.mapPoints(this.mStart);
        this.mMatrix.mapPoints(this.mEnd);
        RectF rectF = this.mRectF;
        float[] fArr3 = this.mStart;
        rectF.left = fArr3[0];
        rectF.right = this.mEnd[0];
        rectF.top = fArr3[1] - strokeWidth;
        rectF.bottom = fArr3[1] + strokeWidth;
        this.mRotateX = countRotateX();
        this.mRotateY = countRotateY();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public DoodleNode.DoodleDrawableType getDoodleType() {
        return DOODLE_TYPE;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void onReceivePosition(float f, float f2, float f3, boolean z) {
        if (!z) {
            refreshRectByPoint();
        }
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.BaseDrawNode
    public void draw(Canvas canvas) {
        if (!this.mRectF.isEmpty()) {
            drawSafely(canvas);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public void appendScale(float f) {
        float width = this.mRectF.width();
        float f2 = ((f * width) - width) / 2.0f;
        this.mMatrix.reset();
        this.mMatrix.postRotate(-this.mDegrees, this.mRectF.centerX(), this.mRectF.centerY());
        float[] fArr = this.mStart;
        PointF pointF = this.mStartPoint;
        fArr[0] = pointF.x;
        fArr[1] = pointF.y;
        float[] fArr2 = this.mEnd;
        PointF pointF2 = this.mEndPoint;
        fArr2[0] = pointF2.x;
        fArr2[1] = pointF2.y;
        this.mMatrix.mapPoints(fArr);
        this.mMatrix.mapPoints(this.mEnd);
        float[] fArr3 = this.mStart;
        fArr3[0] = fArr3[0] - f2;
        float[] fArr4 = this.mEnd;
        fArr4[0] = fArr4[0] + f2;
        this.mMatrix.reset();
        this.mMatrix.postRotate(this.mDegrees, this.mRectF.centerX(), this.mRectF.centerY());
        this.mMatrix.mapPoints(this.mStart);
        this.mMatrix.mapPoints(this.mEnd);
        PointF pointF3 = this.mStartPoint;
        float[] fArr5 = this.mStart;
        pointF3.x = fArr5[0];
        pointF3.y = fArr5[1];
        PointF pointF4 = this.mEndPoint;
        float[] fArr6 = this.mEnd;
        pointF4.x = fArr6[0];
        pointF4.y = fArr6[1];
        refreshRectByPoint();
        countDecoration();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public float countRotateX() {
        return (this.mInverse ? this.mEndPoint : this.mStartPoint).x;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode
    public float countRotateY() {
        return (this.mInverse ? this.mEndPoint : this.mStartPoint).y;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.doodle.painter.DoodleNode, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeParcelable(this.mRectF, i);
    }

    public DoodleVectorNode(Parcel parcel) {
        super(parcel);
        this.mStart = new float[2];
        this.mEnd = new float[2];
        this.mMatrix = new Matrix();
        this.mCurrentPoint = new float[2];
        this.mRectF = (RectF) parcel.readParcelable(RectF.class.getClassLoader());
    }
}
