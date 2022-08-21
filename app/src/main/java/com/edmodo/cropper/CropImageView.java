package com.edmodo.cropper;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import com.edmodo.cropper.cropwindow.edge.Edge;
import com.edmodo.cropper.cropwindow.handle.Handle;
import com.edmodo.cropper.util.AspectRatioUtil;
import com.edmodo.cropper.util.HandleUtil;
import com.edmodo.cropper.util.PaintUtil;

/* loaded from: classes.dex */
public class CropImageView extends ImageView {
    public static final String TAG = CropImageView.class.getName();
    public int mAspectRatioX;
    public int mAspectRatioY;
    public RectF mBitmapRect;
    public Paint mBorderPaint;
    public float mBorderThickness;
    public float mCornerLength;
    public Paint mCornerPaint;
    public float mCornerThickness;
    public boolean mFixAspectRatio;
    public Paint mGuidelinePaint;
    public int mGuidelinesMode;
    public float mHandleRadius;
    public Handle mPressedHandle;
    public float mSnapRadius;
    public Paint mSurroundingAreaOverlayPaint;
    public PointF mTouchOffset;

    public CropImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mBitmapRect = new RectF();
        this.mTouchOffset = new PointF();
        this.mAspectRatioX = 1;
        this.mAspectRatioY = 1;
        this.mGuidelinesMode = 1;
        init(context, attributeSet);
    }

    public final void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CropImageView, 0, 0);
        this.mGuidelinesMode = obtainStyledAttributes.getInteger(R$styleable.CropImageView_guidelines, 1);
        this.mFixAspectRatio = obtainStyledAttributes.getBoolean(R$styleable.CropImageView_fixAspectRatio, false);
        this.mAspectRatioX = obtainStyledAttributes.getInteger(R$styleable.CropImageView_aspectRatioX, 1);
        this.mAspectRatioY = obtainStyledAttributes.getInteger(R$styleable.CropImageView_aspectRatioY, 1);
        obtainStyledAttributes.recycle();
        Resources resources = context.getResources();
        this.mBorderPaint = PaintUtil.newBorderPaint(resources);
        this.mGuidelinePaint = PaintUtil.newGuidelinePaint(resources);
        this.mSurroundingAreaOverlayPaint = PaintUtil.newSurroundingAreaOverlayPaint(resources);
        this.mCornerPaint = PaintUtil.newCornerPaint(resources);
        this.mHandleRadius = resources.getDimension(R$dimen.target_radius);
        this.mSnapRadius = resources.getDimension(R$dimen.snap_radius);
        this.mBorderThickness = resources.getDimension(R$dimen.border_thickness);
        this.mCornerThickness = resources.getDimension(R$dimen.corner_thickness);
        this.mCornerLength = resources.getDimension(R$dimen.corner_length);
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        RectF bitmapRect = getBitmapRect();
        this.mBitmapRect = bitmapRect;
        initCropWindow(bitmapRect);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Float.isNaN(AspectRatioUtil.calculateAspectRatio(this.mBitmapRect))) {
            return;
        }
        drawDarkenedSurroundingArea(canvas);
        drawGuidelines(canvas);
        drawBorder(canvas);
        drawCorners(canvas);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            onActionDown(motionEvent.getX(), motionEvent.getY());
            return true;
        }
        if (action != 1) {
            if (action == 2) {
                onActionMove(motionEvent.getX(), motionEvent.getY());
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            } else if (action != 3) {
                return false;
            }
        }
        getParent().requestDisallowInterceptTouchEvent(false);
        onActionUp();
        return true;
    }

    public void setGuidelines(int i) {
        this.mGuidelinesMode = i;
        invalidate();
    }

    public void setFixedAspectRatio(boolean z) {
        this.mFixAspectRatio = z;
        requestLayout();
    }

    public void setAspectRatio(int i, int i2) {
        if (i <= 0 || i2 <= 0) {
            throw new IllegalArgumentException("Cannot set aspect ratio value to a number less than or equal to 0.");
        }
        this.mAspectRatioX = i;
        this.mAspectRatioY = i2;
        if (!this.mFixAspectRatio) {
            return;
        }
        requestLayout();
    }

    public Bitmap getCroppedImage() {
        Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        float[] fArr = new float[9];
        getImageMatrix().getValues(fArr);
        float f = fArr[0];
        float f2 = fArr[4];
        float f3 = fArr[2];
        float f4 = fArr[5];
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        float coordinate = (Edge.LEFT.getCoordinate() - f3) / f;
        float coordinate2 = (Edge.TOP.getCoordinate() - f4) / f2;
        float min = Math.min(Edge.getWidth() / f, bitmap.getWidth() - coordinate);
        float min2 = Math.min(Edge.getHeight() / f2, bitmap.getHeight() - coordinate2);
        int i = (int) coordinate;
        int i2 = (int) coordinate2;
        int floor = (int) Math.floor(min);
        int floor2 = (int) Math.floor(min2);
        if (floor == 0) {
            floor = 1;
        }
        if (floor2 == 0) {
            floor2 = 1;
        }
        int i3 = i + floor;
        if (i3 > bitmap.getWidth()) {
            i -= i3 - bitmap.getWidth();
        }
        int i4 = i2 + floor2;
        if (i4 > bitmap.getHeight()) {
            i2 -= i4 - bitmap.getHeight();
        }
        return Bitmap.createBitmap(bitmap, Math.max(0, i), i2, floor, floor2);
    }

    private RectF getBitmapRect() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return new RectF();
        }
        float[] fArr = new float[9];
        getImageMatrix().getValues(fArr);
        float f = fArr[0];
        float f2 = fArr[4];
        float f3 = fArr[2];
        float f4 = fArr[5];
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        int round = Math.round(intrinsicWidth * f);
        int round2 = Math.round(intrinsicHeight * f2);
        float max = Math.max(f3, 0.0f);
        float max2 = Math.max(f4, 0.0f);
        return new RectF(max, max2, Math.min(round + max, getWidth()), Math.min(round2 + max2, getHeight()));
    }

    public final void initCropWindow(RectF rectF) {
        if (this.mFixAspectRatio) {
            initCropWindowWithFixedAspectRatio(rectF);
            return;
        }
        float width = rectF.width() * 0.1f;
        float height = rectF.height() * 0.1f;
        Edge.LEFT.setCoordinate(rectF.left + width);
        Edge.TOP.setCoordinate(rectF.top + height);
        Edge.RIGHT.setCoordinate(rectF.right - width);
        Edge.BOTTOM.setCoordinate(rectF.bottom - height);
    }

    public final void initCropWindowWithFixedAspectRatio(RectF rectF) {
        if (AspectRatioUtil.calculateAspectRatio(rectF) > getTargetAspectRatio()) {
            float calculateWidth = AspectRatioUtil.calculateWidth(rectF.height(), getTargetAspectRatio()) / 2.0f;
            Edge.LEFT.setCoordinate(rectF.centerX() - calculateWidth);
            Edge.TOP.setCoordinate(rectF.top);
            Edge.RIGHT.setCoordinate(rectF.centerX() + calculateWidth);
            Edge.BOTTOM.setCoordinate(rectF.bottom);
            return;
        }
        float calculateHeight = AspectRatioUtil.calculateHeight(rectF.width(), getTargetAspectRatio());
        Edge.LEFT.setCoordinate(rectF.left);
        float f = calculateHeight / 2.0f;
        Edge.TOP.setCoordinate(rectF.centerY() - f);
        Edge.RIGHT.setCoordinate(rectF.right);
        Edge.BOTTOM.setCoordinate(rectF.centerY() + f);
    }

    public final void drawDarkenedSurroundingArea(Canvas canvas) {
        RectF rectF = this.mBitmapRect;
        float coordinate = Edge.LEFT.getCoordinate();
        float coordinate2 = Edge.TOP.getCoordinate();
        float coordinate3 = Edge.RIGHT.getCoordinate();
        float coordinate4 = Edge.BOTTOM.getCoordinate();
        canvas.drawRect(rectF.left, rectF.top, rectF.right + 0.5f, coordinate2, this.mSurroundingAreaOverlayPaint);
        canvas.drawRect(rectF.left, coordinate4, rectF.right + 0.5f, rectF.bottom, this.mSurroundingAreaOverlayPaint);
        canvas.drawRect(rectF.left, coordinate2, coordinate, coordinate4, this.mSurroundingAreaOverlayPaint);
        canvas.drawRect(coordinate3, coordinate2, rectF.right + 0.5f, coordinate4, this.mSurroundingAreaOverlayPaint);
    }

    public final void drawGuidelines(Canvas canvas) {
        if (!shouldGuidelinesBeShown()) {
            return;
        }
        float coordinate = Edge.LEFT.getCoordinate();
        float coordinate2 = Edge.TOP.getCoordinate();
        float coordinate3 = Edge.RIGHT.getCoordinate();
        float coordinate4 = Edge.BOTTOM.getCoordinate();
        float width = Edge.getWidth() / 3.0f;
        float f = coordinate + width;
        canvas.drawLine(f, coordinate2, f, coordinate4, this.mGuidelinePaint);
        float f2 = coordinate3 - width;
        canvas.drawLine(f2, coordinate2, f2, coordinate4, this.mGuidelinePaint);
        float height = Edge.getHeight() / 3.0f;
        float f3 = coordinate2 + height;
        canvas.drawLine(coordinate, f3, coordinate3, f3, this.mGuidelinePaint);
        float f4 = coordinate4 - height;
        canvas.drawLine(coordinate, f4, coordinate3, f4, this.mGuidelinePaint);
    }

    public final void drawBorder(Canvas canvas) {
        canvas.drawRect(Edge.LEFT.getCoordinate(), Edge.TOP.getCoordinate(), Edge.RIGHT.getCoordinate(), Edge.BOTTOM.getCoordinate(), this.mBorderPaint);
    }

    public final void drawCorners(Canvas canvas) {
        float coordinate = Edge.LEFT.getCoordinate();
        float coordinate2 = Edge.TOP.getCoordinate();
        float coordinate3 = Edge.RIGHT.getCoordinate();
        float coordinate4 = Edge.BOTTOM.getCoordinate();
        float f = this.mCornerThickness;
        float f2 = this.mBorderThickness;
        float f3 = (f - f2) / 2.0f;
        float f4 = f - (f2 / 2.0f);
        float f5 = coordinate - f3;
        float f6 = coordinate2 - f4;
        canvas.drawLine(f5, f6, f5, coordinate2 + this.mCornerLength, this.mCornerPaint);
        float f7 = coordinate - f4;
        float f8 = coordinate2 - f3;
        canvas.drawLine(f7, f8, coordinate + this.mCornerLength, f8, this.mCornerPaint);
        float f9 = coordinate3 + f3;
        canvas.drawLine(f9, f6, f9, coordinate2 + this.mCornerLength, this.mCornerPaint);
        float f10 = coordinate3 + f4;
        canvas.drawLine(f10, f8, coordinate3 - this.mCornerLength, f8, this.mCornerPaint);
        float f11 = coordinate4 + f4;
        canvas.drawLine(f5, f11, f5, coordinate4 - this.mCornerLength, this.mCornerPaint);
        float f12 = coordinate4 + f3;
        canvas.drawLine(f7, f12, coordinate + this.mCornerLength, f12, this.mCornerPaint);
        canvas.drawLine(f9, f11, f9, coordinate4 - this.mCornerLength, this.mCornerPaint);
        canvas.drawLine(f10, f12, coordinate3 - this.mCornerLength, f12, this.mCornerPaint);
    }

    public final boolean shouldGuidelinesBeShown() {
        int i = this.mGuidelinesMode;
        if (i != 2) {
            return i == 1 && this.mPressedHandle != null;
        }
        return true;
    }

    private float getTargetAspectRatio() {
        return this.mAspectRatioX / this.mAspectRatioY;
    }

    public final void onActionDown(float f, float f2) {
        float coordinate = Edge.LEFT.getCoordinate();
        float coordinate2 = Edge.TOP.getCoordinate();
        float coordinate3 = Edge.RIGHT.getCoordinate();
        float coordinate4 = Edge.BOTTOM.getCoordinate();
        Handle pressedHandle = HandleUtil.getPressedHandle(f, f2, coordinate, coordinate2, coordinate3, coordinate4, this.mHandleRadius);
        this.mPressedHandle = pressedHandle;
        if (pressedHandle != null) {
            HandleUtil.getOffset(pressedHandle, f, f2, coordinate, coordinate2, coordinate3, coordinate4, this.mTouchOffset);
            invalidate();
        }
    }

    public final void onActionUp() {
        if (this.mPressedHandle != null) {
            this.mPressedHandle = null;
            invalidate();
        }
    }

    public final void onActionMove(float f, float f2) {
        Handle handle = this.mPressedHandle;
        if (handle == null) {
            return;
        }
        PointF pointF = this.mTouchOffset;
        float f3 = f + pointF.x;
        float f4 = f2 + pointF.y;
        if (this.mFixAspectRatio) {
            handle.updateCropWindow(f3, f4, getTargetAspectRatio(), this.mBitmapRect, this.mSnapRadius);
        } else {
            handle.updateCropWindow(f3, f4, this.mBitmapRect, this.mSnapRadius);
        }
        invalidate();
    }
}
