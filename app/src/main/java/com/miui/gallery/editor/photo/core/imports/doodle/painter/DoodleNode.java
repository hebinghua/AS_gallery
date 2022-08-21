package com.miui.gallery.editor.photo.core.imports.doodle.painter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.BaseDrawNode;
import com.miui.gallery.util.MatrixUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public abstract class DoodleNode extends BaseDrawNode implements Parcelable {
    public static Map<String, Bitmap> sColoredTextureCache = new HashMap();
    public Matrix mBitmapDisplayMatrix;
    public boolean mCanSelected;
    public int mColor;
    public float mCorrectionDegrees;
    public float mDegrees;
    public PointF mEndPoint;
    public boolean mIsInit;
    public Matrix mMatrix;
    public final float mOutLineOffsetX;
    public final float mOutLineOffsetY;
    public RectF mOutLineRectF;
    public Paint mPaint;
    public RectF mRectF;
    public RectF mRectFTemp;
    public Rect mRectTemp;
    public float mRotateX;
    public float mRotateY;
    public float mScale;
    public PointF mStartPoint;
    public RectF mStrokeRect;
    public float mStrokeWidth;
    public float mUserLocationX;
    public float mUserLocationY;

    /* loaded from: classes2.dex */
    public enum DoodleDrawableType {
        PATH,
        SHAPE,
        VECTOR
    }

    public static float resolveRotateDegree(float f, float f2) {
        float f3 = 0.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        float f4 = f % 360.0f;
        if (f4 > 0.0f && f4 < f2) {
            f4 = 0.0f;
        }
        if (f4 <= 360.0f - f2) {
            f3 = f4;
        }
        if (f3 != 90.0f && f3 > 90.0f - f2 && f3 < f2 + 90.0f) {
            f3 = 90.0f;
        }
        if (f3 != 180.0f && f3 > 180.0f - f2 && f3 < f2 + 180.0f) {
            f3 = 180.0f;
        }
        if (f3 == 270.0f || f3 <= 270.0f - f2 || f3 >= f2 + 270.0f) {
            return f3;
        }
        return 270.0f;
    }

    public abstract void appendScale(float f);

    public abstract void count();

    public abstract float countRotateX();

    public abstract float countRotateY();

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public abstract String getDoodleName();

    public abstract DoodleDrawableType getDoodleType();

    public abstract void init(Resources resources);

    public abstract void onReceivePosition(float f, float f2, float f3, boolean z);

    public abstract void processOnDownEvent(float f, float f2);

    public abstract void processScaleEvent(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    public DoodleNode(Resources resources) {
        this.mRectF = new RectF();
        this.mOutLineRectF = new RectF();
        this.mStrokeRect = new RectF();
        this.mRectTemp = new Rect();
        this.mRectFTemp = new RectF();
        this.mUserLocationX = 0.0f;
        this.mUserLocationY = 0.0f;
        this.mScale = 1.0f;
        this.mDegrees = 0.0f;
        this.mMatrix = new Matrix();
        this.mRotateX = 0.0f;
        this.mRotateY = 0.0f;
        this.mStartPoint = new PointF();
        this.mEndPoint = new PointF();
        this.mIsInit = false;
        this.mCorrectionDegrees = 5.0f;
        this.mCanSelected = true;
        this.mBitmapDisplayMatrix = new Matrix();
        initPaint();
        this.mOutLineOffsetX = resources.getDimension(R.dimen.doodle_outline_offset_x);
        this.mOutLineOffsetY = resources.getDimension(R.dimen.doodle_outline_offset_y);
        init(resources);
    }

    public final void initPaint() {
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setStyle(Paint.Style.STROKE);
    }

    public void countSize() {
        count();
        countDecoration();
        this.mRotateX = countRotateX();
        this.mRotateY = countRotateY();
    }

    public void countDecoration() {
        this.mOutLineRectF.set(this.mRectF);
        this.mOutLineRectF.inset(-(((this.mOutLineRectF.width() * this.mScale) - this.mOutLineRectF.width()) / 2.0f), -(((this.mOutLineRectF.height() * this.mScale) - this.mOutLineRectF.height()) / 2.0f));
        float scale = MatrixUtil.getScale(this.mBitmapDisplayMatrix);
        this.mOutLineRectF.inset(-(this.mOutLineOffsetX / scale), -(this.mOutLineOffsetY / scale));
        this.mStrokeRect.set(this.mOutLineRectF);
    }

    public void getStrokeRectF(RectF rectF) {
        rectF.set(this.mOutLineRectF);
    }

    public void getRectF(RectF rectF) {
        rectF.set(this.mRectF);
    }

    public boolean contains(float f, float f2) {
        getStrokeRectF(this.mRectFTemp);
        offsetRect(this.mRectFTemp);
        this.mMatrix.reset();
        this.mMatrix.postRotate(-getRotateDegrees(), this.mRotateX + this.mUserLocationX, this.mRotateY + this.mUserLocationY);
        float[] fArr = {f, f2};
        this.mMatrix.mapPoints(fArr);
        return this.mRectFTemp.contains(fArr[0], fArr[1]);
    }

    public float getRotateDegrees() {
        return this.mDegrees;
    }

    public void setPaintSize(float f) {
        this.mStrokeWidth = f;
        this.mPaint.setStrokeWidth(f);
    }

    public int getPaintColor() {
        return this.mColor;
    }

    public void setPaintColor(int i) {
        this.mColor = i;
        this.mPaint.setColor(i);
    }

    public void setImageDisplayMatrix(Matrix matrix) {
        this.mBitmapDisplayMatrix = matrix;
    }

    public void appendUserLocationX(float f) {
        this.mUserLocationX += f;
    }

    public void appendUserLocationY(float f) {
        this.mUserLocationY += f;
    }

    public void configCanvas(Canvas canvas, boolean z) {
        canvas.translate(this.mUserLocationX, this.mUserLocationY);
        if (z) {
            canvas.rotate(getRotateDegrees(), this.mRotateX, this.mRotateY);
        }
    }

    public final void offsetRect(RectF rectF) {
        rectF.offset(this.mUserLocationX, this.mUserLocationY);
    }

    public void appendDegrees(float f) {
        float f2 = this.mDegrees + f;
        this.mDegrees = f2;
        if (f2 < 0.0f) {
            this.mDegrees = f2 + 360.0f;
        }
        float f3 = this.mDegrees;
        if (f3 >= 360.0f) {
            this.mDegrees = f3 % 360.0f;
        }
    }

    public void setDegrees(float f) {
        this.mDegrees = f;
        if (f < 0.0f) {
            this.mDegrees = f + 360.0f;
        }
        float f2 = this.mDegrees;
        if (f2 >= 360.0f) {
            this.mDegrees = f2 % 360.0f;
        }
    }

    public void receivePosition(float f, float f2, float f3) {
        if (!this.mIsInit) {
            this.mStartPoint.set(f, f2);
            this.mIsInit = true;
            onReceivePosition(f, f2, f3, true);
            return;
        }
        this.mEndPoint.set(f, f2);
        onReceivePosition(f, f2, f3, false);
    }

    public void processOnUp() {
        refreshRotateCenter();
    }

    public void processRotateEvent(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        float f9 = this.mRotateX + this.mUserLocationX;
        float f10 = this.mRotateY + this.mUserLocationY;
        appendDegrees((float) Math.toDegrees(Math.atan2(f4 - f10, f3 - f9) - Math.atan2(f8 - f10, f7 - f9)));
        setDegrees(resolveRotateDegree(this.mDegrees, this.mCorrectionDegrees));
        countDecoration();
    }

    public void refreshRectByPoint(PointF pointF, PointF pointF2) {
        float f = pointF.x;
        float f2 = pointF2.x;
        boolean z = true;
        boolean z2 = f <= f2;
        float f3 = pointF.y;
        float f4 = pointF2.y;
        if (f3 > f4) {
            z = false;
        }
        RectF rectF = this.mRectF;
        rectF.left = z2 ? f : f2;
        if (z2) {
            f = f2;
        }
        rectF.right = f;
        rectF.top = z ? f3 : f4;
        if (z) {
            f3 = f4;
        }
        rectF.bottom = f3;
    }

    public void refreshPointByRect(PointF pointF, PointF pointF2) {
        RectF rectF = this.mRectF;
        pointF.set(rectF.left, rectF.top);
        RectF rectF2 = this.mRectF;
        pointF2.set(rectF2.right, rectF2.bottom);
    }

    public final void refreshRotateCenter() {
        DefaultLogger.d("DoodleNode", "--- refreshRotateCenter ---");
        float f = this.mRotateX;
        float f2 = this.mRotateY;
        this.mRotateX = countRotateX();
        float countRotateY = countRotateY();
        this.mRotateY = countRotateY;
        if (f == this.mRotateX && f2 == countRotateY) {
            return;
        }
        DefaultLogger.d("DoodleNode", "--- refreshRotateCenter real ---");
        float[] fArr = {this.mRotateX, this.mRotateY};
        this.mMatrix.reset();
        this.mMatrix.postRotate(this.mDegrees, f, f2);
        this.mMatrix.mapPoints(fArr);
        this.mUserLocationX += fArr[0] - this.mRotateX;
        this.mUserLocationY += fArr[1] - this.mRotateY;
        countDecoration();
    }

    public float getRotateX() {
        return this.mRotateX;
    }

    public float getRotateY() {
        return this.mRotateY;
    }

    public float getUserLocationX() {
        return this.mUserLocationX;
    }

    public float getUserLocationY() {
        return this.mUserLocationY;
    }

    public void initForParcelable(Context context) {
        setPaintSize(this.mStrokeWidth);
        setPaintColor(this.mColor);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(this.mStrokeWidth);
        parcel.writeInt(this.mColor);
        parcel.writeFloat(this.mUserLocationX);
        parcel.writeFloat(this.mUserLocationY);
        parcel.writeFloat(getRotateDegrees());
        parcel.writeFloat(this.mRotateX);
        parcel.writeFloat(this.mRotateY);
        parcel.writeParcelable(this.mStartPoint, i);
        parcel.writeParcelable(this.mEndPoint, i);
    }

    public DoodleNode(Parcel parcel) {
        this(GalleryApp.sGetAndroidContext().getResources());
        this.mStrokeWidth = parcel.readFloat();
        this.mColor = parcel.readInt();
        this.mUserLocationX = parcel.readFloat();
        this.mUserLocationY = parcel.readFloat();
        this.mDegrees = parcel.readFloat();
        this.mRotateX = parcel.readFloat();
        this.mRotateY = parcel.readFloat();
        this.mStartPoint = (PointF) parcel.readParcelable(PointF.class.getClassLoader());
        this.mEndPoint = (PointF) parcel.readParcelable(PointF.class.getClassLoader());
    }

    public boolean isCanSelected() {
        return this.mCanSelected;
    }

    public void setCanSelected(boolean z) {
        this.mCanSelected = z;
    }

    public static void relese() {
        for (Map.Entry<String, Bitmap> entry : sColoredTextureCache.entrySet()) {
            Bitmap value = entry.getValue();
            if (value != null && !value.isRecycled()) {
                value.recycle();
            }
        }
        sColoredTextureCache.clear();
    }
}
