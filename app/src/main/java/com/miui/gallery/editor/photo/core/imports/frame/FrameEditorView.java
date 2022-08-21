package com.miui.gallery.editor.photo.core.imports.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.IPositionChangeData;
import com.miui.gallery.editor.photo.utils.BigBitmapLoadUtils;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.imageview.MatrixTransition;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;
import com.miui.gallery.widget.imageview.SensitiveScaleGestureDetector;

/* loaded from: classes2.dex */
public class FrameEditorView extends View {
    public int mBackgroundColor;
    public Paint mBackgroundPaint;
    public RectF mBackgroundRect;
    public Bitmap mBitmap;
    public RectF mBitmapDisplayRect;
    public Matrix mBitmapMatrix;
    public Paint mBitmapPaint;
    public float mBitmapRatio;
    public RectF mBitmapRect;
    public Paint mBorderPaint;
    public Path mBorderPath;
    public CanvasMatrixTracker mCanvasMatrixTracker;
    public RectF mCinemaGraphicRect;
    public boolean mCinemaStyle;
    public RectF mDisplayRect;
    public int mFrameColor;
    public float mRatio;
    public float mScaleProgress;

    /* loaded from: classes2.dex */
    public interface FrameEntry extends IPositionChangeData, Parcelable {
        Bitmap apply(Bitmap bitmap, boolean z);
    }

    public FrameEditorView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FrameEditorView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBitmapRect = new RectF();
        this.mDisplayRect = new RectF();
        this.mBackgroundRect = new RectF();
        this.mBitmapDisplayRect = new RectF();
        this.mBitmapMatrix = new Matrix();
        this.mBackgroundPaint = new Paint(1);
        this.mBorderPaint = new Paint(1);
        this.mBorderPath = new Path();
        this.mCinemaGraphicRect = new RectF();
        this.mBitmapPaint = new Paint(3);
        this.mRatio = 1.0f;
        this.mScaleProgress = 1.0f;
        this.mBitmapRatio = 1.0f;
        init();
    }

    public final void init() {
        this.mCanvasMatrixTracker = new CanvasMatrixTracker(this);
        this.mBackgroundColor = getResources().getColor(R.color.photo_editor_window_bg);
        this.mBackgroundPaint.setStyle(Paint.Style.FILL);
        this.mFrameColor = -1;
        this.mBackgroundPaint.setColor(-1);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);
        this.mBorderPaint.setStrokeWidth(getResources().getDimension(R.dimen.photo_editor_frame_border_width));
        this.mBorderPaint.setColor(getResources().getColor(R.color.frame_menu_border_color));
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mCanvasMatrixTracker.onTouchEvent(motionEvent);
        return true;
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        EditorMiscHelper.configProtectiveArea(this);
    }

    public void setBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mBitmapRect.set(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        this.mBitmapRatio = bitmap.getWidth() / bitmap.getHeight();
        refreshSize();
    }

    public void setRatio(float f) {
        this.mRatio = f;
        refreshSize();
    }

    public void setFrameData(float f, boolean z) {
        this.mRatio = f;
        this.mCinemaStyle = z;
        refreshSize();
    }

    public void setScaleProgress(float f) {
        this.mScaleProgress = 1.0f - ((1.0f - f) * 0.4f);
        refreshBitmapRect();
        invalidate();
    }

    public void setFrameColor(int i) {
        this.mFrameColor = i;
        this.mBackgroundPaint.setColor(i);
        invalidate();
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        canvas.save();
        canvas.concat(this.mCanvasMatrixTracker.getCanvasMatrix());
        drawContent(canvas);
        canvas.restore();
    }

    public final void drawContent(Canvas canvas) {
        canvas.drawColor(this.mBackgroundColor);
        if (this.mCinemaStyle) {
            drawCinemaFrame(canvas);
        } else {
            drawNormalFrame(canvas);
        }
    }

    public final void drawNormalFrame(Canvas canvas) {
        canvas.drawRect(this.mBackgroundRect, this.mBackgroundPaint);
        canvas.drawBitmap(this.mBitmap, this.mBitmapMatrix, this.mBitmapPaint);
        if (this.mFrameColor == -16777216) {
            this.mBorderPath.reset();
            Path path = this.mBorderPath;
            RectF rectF = this.mBackgroundRect;
            path.moveTo(rectF.left, rectF.top);
            Path path2 = this.mBorderPath;
            RectF rectF2 = this.mBackgroundRect;
            path2.lineTo(rectF2.right, rectF2.top);
            Path path3 = this.mBorderPath;
            RectF rectF3 = this.mBackgroundRect;
            path3.lineTo(rectF3.right, rectF3.bottom);
            Path path4 = this.mBorderPath;
            RectF rectF4 = this.mBackgroundRect;
            path4.lineTo(rectF4.left, rectF4.bottom);
            Path path5 = this.mBorderPath;
            RectF rectF5 = this.mBackgroundRect;
            path5.lineTo(rectF5.left, rectF5.top);
            canvas.drawPath(this.mBorderPath, this.mBorderPaint);
        }
    }

    public final void drawCinemaFrame(Canvas canvas) {
        int save = canvas.save();
        canvas.clipRect(this.mBackgroundRect);
        canvas.drawBitmap(this.mBitmap, this.mBitmapMatrix, this.mBitmapPaint);
        canvas.save();
        float height = (this.mBackgroundRect.height() - (this.mBackgroundRect.width() / 2.39f)) / 2.0f;
        RectF rectF = this.mCinemaGraphicRect;
        RectF rectF2 = this.mBackgroundRect;
        rectF.set(rectF2.left, rectF2.top + height, rectF2.right, rectF2.bottom - height);
        canvas.clipRect(this.mCinemaGraphicRect, Region.Op.DIFFERENCE);
        canvas.drawColor(-16777216);
        this.mBorderPath.reset();
        Path path = this.mBorderPath;
        RectF rectF3 = this.mBackgroundRect;
        path.moveTo(rectF3.left, rectF3.top);
        Path path2 = this.mBorderPath;
        RectF rectF4 = this.mBackgroundRect;
        path2.lineTo(rectF4.right, rectF4.top);
        Path path3 = this.mBorderPath;
        RectF rectF5 = this.mBackgroundRect;
        path3.lineTo(rectF5.right, rectF5.bottom);
        Path path4 = this.mBorderPath;
        RectF rectF6 = this.mBackgroundRect;
        path4.lineTo(rectF6.left, rectF6.bottom);
        Path path5 = this.mBorderPath;
        RectF rectF7 = this.mBackgroundRect;
        path5.lineTo(rectF7.left, rectF7.top);
        canvas.drawPath(this.mBorderPath, this.mBorderPaint);
        canvas.restoreToCount(save);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        this.mDisplayRect.set(0.0f, 0.0f, i, i2);
        this.mCanvasMatrixTracker.updateDisplayRect(this.mDisplayRect);
        refreshSize();
    }

    public final void refreshSize() {
        if (this.mBitmapRect.isEmpty() || this.mDisplayRect.isEmpty()) {
            return;
        }
        refreshPhotoFrameRect();
        refreshBitmapRect();
        invalidate();
    }

    public final void refreshBitmapRect() {
        float width;
        this.mBitmapMatrix.reset();
        if (this.mCinemaStyle) {
            RectF rectF = this.mBackgroundRect;
            float f = rectF.left;
            float f2 = rectF.top;
            if (this.mBitmapRatio > this.mRatio) {
                width = rectF.height() / this.mBitmap.getHeight();
                f += (this.mBackgroundRect.width() - (this.mBitmap.getWidth() * width)) / 2.0f;
            } else {
                width = rectF.width() / this.mBitmap.getWidth();
                f2 += (this.mBackgroundRect.height() - (this.mBitmap.getHeight() * width)) / 2.0f;
            }
            this.mBitmapMatrix.setScale(width, width);
            this.mBitmapMatrix.postTranslate(f, f2);
            return;
        }
        this.mBitmapMatrix.setRectToRect(this.mBitmapRect, this.mBackgroundRect, Matrix.ScaleToFit.CENTER);
        this.mBitmapMatrix.mapRect(this.mBitmapDisplayRect, this.mBitmapRect);
        Matrix matrix = this.mBitmapMatrix;
        float f3 = this.mScaleProgress;
        matrix.postScale(f3, f3, this.mBitmapDisplayRect.centerX(), this.mBitmapDisplayRect.centerY());
    }

    public final void refreshPhotoFrameRect() {
        if (this.mRatio >= this.mDisplayRect.width() / this.mDisplayRect.height()) {
            RectF rectF = this.mBackgroundRect;
            RectF rectF2 = this.mDisplayRect;
            rectF.left = rectF2.left;
            rectF.right = rectF2.right;
            float width = (rectF2.width() / this.mRatio) / 2.0f;
            this.mBackgroundRect.top = this.mDisplayRect.centerY() - width;
            this.mBackgroundRect.bottom = this.mDisplayRect.centerY() + width;
        } else {
            RectF rectF3 = this.mBackgroundRect;
            RectF rectF4 = this.mDisplayRect;
            rectF3.top = rectF4.top;
            rectF3.bottom = rectF4.bottom;
            float height = (rectF4.height() * this.mRatio) / 2.0f;
            this.mBackgroundRect.left = this.mDisplayRect.centerX() - height;
            this.mBackgroundRect.right = this.mDisplayRect.centerX() + height;
        }
        this.mCanvasMatrixTracker.updateGraphicInitRect(this.mBackgroundRect);
    }

    public FrameEntry export() {
        return this.mCinemaStyle ? new CinemaFrameEntry(this.mRatio) : new NormalFrameEntry(this.mRatio, this.mScaleProgress, this.mFrameColor);
    }

    public boolean isEmpty() {
        return !this.mCinemaStyle && Float.compare(this.mRatio, this.mBitmapRatio) == 0 && Float.compare(this.mScaleProgress, 1.0f) == 0;
    }

    public static int calculateFrameWidth(float f, int i, int i2, boolean z) {
        float f2 = i;
        if (Math.min(f2 / f, i2 * f) < 450.0f) {
            return (int) (f2 * (450.0f / Math.min(i, i2)));
        }
        if (z) {
            int max = Math.max(i, i2);
            return max / BigBitmapLoadUtils.calculateInSampleSize(GalleryApp.sGetAndroidContext(), max, (int) (max / f));
        }
        int max2 = Math.max(i, i2);
        return f > 1.0f ? max2 : Math.round(max2 * f);
    }

    /* loaded from: classes2.dex */
    public static class CanvasMatrixTracker {
        public GestureDetector mGestureDetector;
        public View mHost;
        public boolean mIsShook;
        public float mMaxStableWidth;
        public ScaleGestureDetector mScaleGestureDetector;
        public float mTouchMinSize;
        public RectF mGraphicRect = new RectF();
        public RectF mGraphicInitRect = new RectF();
        public RectF mDisplayRect = new RectF();
        public RectF mRectFTemp = new RectF();
        public float mScaleFocusX = 0.0f;
        public float mScaleFocusY = 0.0f;
        public float[] mMatrixValues = new float[9];
        public Matrix mCanvasMatrix = new Matrix();
        public Matrix mAnimTargetMatrix = new Matrix();
        public MatrixTransition mMatrixTransition = new MatrixTransition();
        public MatrixTransition.MatrixUpdateListener mMatrixUpdateListener = new MatrixTransition.MatrixUpdateListener() { // from class: com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView.CanvasMatrixTracker.1
            @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
            public void onAnimationEnd() {
            }

            @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
            public void onAnimationStart() {
            }

            @Override // com.miui.gallery.widget.imageview.MatrixTransition.MatrixUpdateListener
            public void onMatrixUpdate() {
                CanvasMatrixTracker.this.mCanvasMatrix.mapRect(CanvasMatrixTracker.this.mGraphicRect, CanvasMatrixTracker.this.mGraphicInitRect);
                CanvasMatrixTracker.this.mHost.invalidate();
            }
        };

        public CanvasMatrixTracker(View view) {
            this.mHost = view;
            Context context = view.getContext();
            this.mMatrixTransition.setMatrixUpdateListener(this.mMatrixUpdateListener);
            this.mTouchMinSize = ViewConfiguration.get(context).getScaledTouchSlop();
            this.mGestureDetector = new GestureDetector(context, new CustomGesListener());
            this.mScaleGestureDetector = new SensitiveScaleGestureDetector(context, new CustomScaleListener());
        }

        public Matrix getCanvasMatrix() {
            return this.mCanvasMatrix;
        }

        public void onTouchEvent(MotionEvent motionEvent) {
            this.mScaleGestureDetector.onTouchEvent(motionEvent);
            this.mGestureDetector.onTouchEvent(motionEvent);
            int action = motionEvent.getAction();
            if (action == 1 || action == 3) {
                resetMatrixWithAnim();
                this.mHost.invalidate();
            }
        }

        public void updateDisplayRect(RectF rectF) {
            this.mDisplayRect.set(rectF);
            this.mCanvasMatrix.reset();
        }

        public void updateGraphicInitRect(RectF rectF) {
            this.mGraphicInitRect.set(rectF);
            this.mMaxStableWidth = this.mGraphicInitRect.width() * 4.0f;
        }

        public void resetMatrixWithAnim() {
            this.mCanvasMatrix.getValues(this.mMatrixValues);
            if (this.mMatrixValues[0] <= 1.0f) {
                countAnimMatrixWhenZoomOut(this.mAnimTargetMatrix);
            } else {
                countAnimMatrixWhenZoomIn(this.mAnimTargetMatrix);
            }
            this.mMatrixTransition.animMatrix(this.mCanvasMatrix, this.mAnimTargetMatrix);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x004b  */
        /* JADX WARN: Removed duplicated region for block: B:20:0x005f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void performTransition(float r9, float r10) {
            /*
                r8 = this;
                android.graphics.RectF r0 = r8.mDisplayRect
                float r0 = r0.width()
                r1 = 1073741824(0x40000000, float:2.0)
                float r0 = r0 / r1
                android.graphics.RectF r2 = r8.mDisplayRect
                float r2 = r2.height()
                float r2 = r2 / r1
                android.graphics.RectF r1 = r8.mDisplayRect
                float r1 = r1.centerX()
                android.graphics.RectF r3 = r8.mDisplayRect
                float r3 = r3.centerY()
                r4 = 0
                int r5 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
                r6 = 1065353216(0x3f800000, float:1.0)
                if (r5 <= 0) goto L32
                android.graphics.RectF r5 = r8.mGraphicRect
                float r5 = r5.left
                android.graphics.RectF r7 = r8.mDisplayRect
                float r7 = r7.left
                int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r7 <= 0) goto L32
                float r1 = r1 - r5
                float r1 = r1 / r0
                goto L47
            L32:
                int r5 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
                if (r5 >= 0) goto L46
                android.graphics.RectF r5 = r8.mGraphicRect
                float r5 = r5.right
                android.graphics.RectF r7 = r8.mDisplayRect
                float r7 = r7.right
                int r7 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1))
                if (r7 >= 0) goto L46
                float r5 = r5 - r1
                float r1 = r5 / r0
                goto L47
            L46:
                r1 = r6
            L47:
                int r0 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
                if (r0 <= 0) goto L5b
                android.graphics.RectF r0 = r8.mGraphicRect
                float r0 = r0.top
                android.graphics.RectF r5 = r8.mDisplayRect
                float r5 = r5.top
                int r5 = (r0 > r5 ? 1 : (r0 == r5 ? 0 : -1))
                if (r5 <= 0) goto L5b
                float r3 = r3 - r0
                float r6 = r3 / r2
                goto L6e
            L5b:
                int r0 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
                if (r0 >= 0) goto L6e
                android.graphics.RectF r0 = r8.mGraphicRect
                float r0 = r0.bottom
                android.graphics.RectF r4 = r8.mDisplayRect
                float r4 = r4.bottom
                int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
                if (r4 >= 0) goto L6e
                float r0 = r0 - r3
                float r6 = r0 / r2
            L6e:
                float r9 = r9 * r1
                float r10 = r10 * r6
                android.graphics.Matrix r0 = r8.mCanvasMatrix
                r0.postTranslate(r9, r10)
                android.graphics.Matrix r9 = r8.mCanvasMatrix
                android.graphics.RectF r10 = r8.mGraphicRect
                android.graphics.RectF r0 = r8.mGraphicInitRect
                r9.mapRect(r10, r0)
                android.view.View r9 = r8.mHost
                r9.invalidate()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView.CanvasMatrixTracker.performTransition(float, float):void");
        }

        public void performScale(float f, float f2, float f3) {
            this.mCanvasMatrix.postScale(f, f, f2, f3);
            this.mScaleFocusX = f2;
            this.mScaleFocusY = f3;
            this.mCanvasMatrix.mapRect(this.mGraphicRect, this.mGraphicInitRect);
            this.mHost.invalidate();
        }

        public void countAnimMatrixWhenZoomOut(Matrix matrix) {
            matrix.reset();
        }

        public void countAnimMatrixWhenZoomIn(Matrix matrix) {
            float f;
            float f2;
            matrix.set(this.mCanvasMatrix);
            this.mRectFTemp.set(this.mGraphicRect);
            RectF rectF = this.mRectFTemp;
            float width = rectF.width();
            float f3 = this.mMaxStableWidth;
            if (width > f3) {
                float width2 = f3 / rectF.width();
                matrix.postScale(width2, width2, this.mScaleFocusX, this.mScaleFocusY);
                matrix.mapRect(rectF, this.mGraphicInitRect);
            }
            float width3 = rectF.width();
            float height = rectF.height();
            float f4 = rectF.left;
            float f5 = rectF.right;
            float f6 = rectF.top;
            float f7 = rectF.bottom;
            float width4 = this.mDisplayRect.width();
            float height2 = this.mDisplayRect.height();
            RectF rectF2 = this.mDisplayRect;
            float f8 = rectF2.left;
            float f9 = rectF2.right;
            float f10 = rectF2.top;
            float f11 = rectF2.bottom;
            float f12 = 0.0f;
            if (f4 > f8) {
                f = width3 > width4 ? -(f4 - f8) : (-(f4 - f8)) + ((width4 - width3) * ((this.mGraphicInitRect.left - f8) / (rectF2.width() - this.mGraphicInitRect.width())));
            } else if (f5 < f9) {
                f = width3 > width4 ? f9 - f5 : (f9 - f5) - ((width4 - width3) * (1.0f - ((this.mGraphicInitRect.left - f8) / (rectF2.width() - this.mGraphicInitRect.width()))));
            } else {
                f = 0.0f;
            }
            if (f6 > f10) {
                if (height <= height2) {
                    float f13 = this.mGraphicInitRect.top;
                    RectF rectF3 = this.mDisplayRect;
                    f12 = (-(f6 - f10)) + ((height2 - height) * ((f13 - rectF3.top) / (rectF3.height() - this.mGraphicInitRect.height())));
                } else {
                    f2 = -(f6 - f10);
                    matrix.postTranslate(f, f2);
                }
            } else if (f7 < f11) {
                if (height > height2) {
                    f12 = f11 - f7;
                } else {
                    float f14 = this.mGraphicInitRect.top;
                    RectF rectF4 = this.mDisplayRect;
                    f12 = (f11 - f7) - ((height2 - height) * (1.0f - ((f14 - rectF4.top) / (rectF4.height() - this.mGraphicInitRect.height()))));
                }
            }
            f2 = f12;
            matrix.postTranslate(f, f2);
        }

        /* loaded from: classes2.dex */
        public class CustomGesListener implements GestureDetector.OnGestureListener {
            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onDown(MotionEvent motionEvent) {
                return true;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                return false;
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public void onLongPress(MotionEvent motionEvent) {
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public void onShowPress(MotionEvent motionEvent) {
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }

            public CustomGesListener() {
            }

            @Override // android.view.GestureDetector.OnGestureListener
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
                float hypot = (float) Math.hypot(motionEvent2.getX() - motionEvent.getX(), motionEvent2.getY() - motionEvent.getY());
                if (motionEvent2.getPointerCount() > 1 && hypot > CanvasMatrixTracker.this.mTouchMinSize) {
                    CanvasMatrixTracker.this.performTransition(-f, -f2);
                }
                return true;
            }
        }

        /* loaded from: classes2.dex */
        public class CustomScaleListener implements ScaleGestureDetector.OnScaleGestureListener {
            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            }

            public CustomScaleListener() {
            }

            @Override // com.miui.gallery.widget.imageview.ScaleGestureDetector.OnScaleGestureListener
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
                CanvasMatrixTracker.this.mCanvasMatrix.getValues(CanvasMatrixTracker.this.mMatrixValues);
                double d = CanvasMatrixTracker.this.mMatrixValues[0];
                if (d > 6.0d) {
                    if (!CanvasMatrixTracker.this.mIsShook) {
                        LinearMotorHelper.performHapticFeedback(CanvasMatrixTracker.this.mHost, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                        CanvasMatrixTracker.this.mIsShook = true;
                    }
                    return false;
                } else if (d < 0.5d) {
                    if (!CanvasMatrixTracker.this.mIsShook) {
                        LinearMotorHelper.performHapticFeedback(CanvasMatrixTracker.this.mHost, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                        CanvasMatrixTracker.this.mIsShook = true;
                    }
                    return false;
                } else {
                    CanvasMatrixTracker.this.mIsShook = false;
                    CanvasMatrixTracker.this.performScale(scaleGestureDetector.getScaleFactor(), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    return true;
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CinemaFrameEntry implements FrameEntry {
        public static final Parcelable.Creator<CinemaFrameEntry> CREATOR = new Parcelable.Creator<CinemaFrameEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView.CinemaFrameEntry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public CinemaFrameEntry mo812createFromParcel(Parcel parcel) {
                return new CinemaFrameEntry(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public CinemaFrameEntry[] mo813newArray(int i) {
                return new CinemaFrameEntry[i];
            }
        };
        public float mRatio;
        public RectF mCinemaGraphicRect = new RectF();
        public RectF mPhotoFrameRect = new RectF();
        public RectF mBitmapRect = new RectF();
        public Matrix mBitmapMatrix = new Matrix();
        public Paint mBitmapPaint = new Paint(3);

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public CinemaFrameEntry(float f) {
            this.mRatio = f;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView.FrameEntry
        public Bitmap apply(Bitmap bitmap, boolean z) {
            float f;
            float height;
            float f2;
            int width = bitmap.getWidth();
            int height2 = bitmap.getHeight();
            this.mBitmapRect.set(0.0f, 0.0f, width, height2);
            int calculateFrameWidth = FrameEditorView.calculateFrameWidth(this.mRatio, width, height2, z);
            float f3 = calculateFrameWidth;
            int i = (int) (f3 / this.mRatio);
            Bitmap createBitmap = Bitmap.createBitmap(calculateFrameWidth, i, Bitmap.Config.ARGB_8888);
            float f4 = i;
            this.mPhotoFrameRect.set(0.0f, 0.0f, f3, f4);
            if (this.mBitmapRect.width() / this.mBitmapRect.height() > this.mRatio) {
                float height3 = f4 / this.mBitmapRect.height();
                f2 = (this.mPhotoFrameRect.width() - (this.mBitmapRect.width() * height3)) / 2.0f;
                f = height3;
                height = 0.0f;
            } else {
                float width2 = f3 / this.mBitmapRect.width();
                f = width2;
                height = (this.mPhotoFrameRect.height() - (this.mBitmapRect.height() * width2)) / 2.0f;
                f2 = 0.0f;
            }
            this.mBitmapMatrix.setScale(f, f);
            this.mBitmapMatrix.postTranslate(f2, height);
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawBitmap(bitmap, this.mBitmapMatrix, this.mBitmapPaint);
            float height4 = (this.mPhotoFrameRect.height() - (this.mPhotoFrameRect.width() / 2.39f)) / 2.0f;
            RectF rectF = this.mCinemaGraphicRect;
            RectF rectF2 = this.mPhotoFrameRect;
            rectF.set(0.0f, rectF2.top + height4, rectF2.right, rectF2.bottom - height4);
            int save = canvas.save();
            canvas.clipRect(this.mCinemaGraphicRect, Region.Op.DIFFERENCE);
            canvas.drawColor(-16777216);
            canvas.restoreToCount(save);
            return createBitmap;
        }

        @Override // com.miui.gallery.editor.photo.core.common.model.IPositionChangeData
        public void refreshTargetAreaPosition(RectF rectF, RectF rectF2) {
            float height;
            float f;
            float f2;
            this.mBitmapRect.set(rectF);
            float calculateFrameWidth = FrameEditorView.calculateFrameWidth(this.mRatio, (int) rectF.width(), (int) rectF.height(), false);
            int i = (int) (calculateFrameWidth / this.mRatio);
            if (this.mBitmapRect.width() / this.mBitmapRect.height() > this.mRatio) {
                f = i / this.mBitmapRect.height();
                f2 = (calculateFrameWidth - (this.mBitmapRect.width() * f)) / 2.0f;
                height = 0.0f;
            } else {
                float width = calculateFrameWidth / this.mBitmapRect.width();
                height = (i - (this.mBitmapRect.height() * width)) / 2.0f;
                f = width;
                f2 = 0.0f;
            }
            this.mBitmapMatrix.setScale(f, f);
            this.mBitmapMatrix.postTranslate(f2, height);
            this.mBitmapMatrix.mapRect(rectF2);
            float height2 = (this.mPhotoFrameRect.height() - (this.mPhotoFrameRect.width() / 2.39f)) / 2.0f;
            RectF rectF3 = this.mCinemaGraphicRect;
            RectF rectF4 = this.mPhotoFrameRect;
            rectF3.set(0.0f, rectF4.top + height2, rectF4.right, rectF4.bottom - height2);
            rectF.set(this.mCinemaGraphicRect);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeFloat(this.mRatio);
        }

        public CinemaFrameEntry(Parcel parcel) {
            this.mRatio = parcel.readFloat();
        }
    }

    /* loaded from: classes2.dex */
    public static class NormalFrameEntry implements FrameEntry {
        public static final Parcelable.Creator<NormalFrameEntry> CREATOR = new Parcelable.Creator<NormalFrameEntry>() { // from class: com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView.NormalFrameEntry.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public NormalFrameEntry mo814createFromParcel(Parcel parcel) {
                return new NormalFrameEntry(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public NormalFrameEntry[] mo815newArray(int i) {
                return new NormalFrameEntry[i];
            }
        };
        public int mFrameColor;
        public float mRatio;
        public float mScaleProgress;
        public RectF mPhotoFrameRect = new RectF();
        public RectF mBitmapRect = new RectF();
        public Matrix mBitmapMatrix = new Matrix();
        public Paint mBitmapPaint = new Paint(3);

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public NormalFrameEntry(float f, float f2, int i) {
            this.mRatio = f;
            this.mScaleProgress = f2;
            this.mFrameColor = i;
        }

        @Override // com.miui.gallery.editor.photo.core.imports.frame.FrameEditorView.FrameEntry
        public Bitmap apply(Bitmap bitmap, boolean z) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            this.mBitmapRect.set(0.0f, 0.0f, width, height);
            int calculateFrameWidth = FrameEditorView.calculateFrameWidth(this.mRatio, width, height, z);
            float f = calculateFrameWidth;
            int i = (int) (f / this.mRatio);
            Bitmap createBitmap = Bitmap.createBitmap(calculateFrameWidth, i, Bitmap.Config.ARGB_8888);
            this.mPhotoFrameRect.set(0.0f, 0.0f, f, i);
            this.mBitmapMatrix.setRectToRect(this.mBitmapRect, this.mPhotoFrameRect, Matrix.ScaleToFit.CENTER);
            Matrix matrix = this.mBitmapMatrix;
            float f2 = this.mScaleProgress;
            matrix.postScale(f2, f2, this.mPhotoFrameRect.centerX(), this.mPhotoFrameRect.centerY());
            Canvas canvas = new Canvas(createBitmap);
            canvas.drawColor(this.mFrameColor);
            canvas.drawBitmap(bitmap, this.mBitmapMatrix, this.mBitmapPaint);
            return createBitmap;
        }

        @Override // com.miui.gallery.editor.photo.core.common.model.IPositionChangeData
        public void refreshTargetAreaPosition(RectF rectF, RectF rectF2) {
            float f;
            this.mBitmapRect.set(rectF);
            this.mPhotoFrameRect.set(0.0f, 0.0f, FrameEditorView.calculateFrameWidth(this.mRatio, (int) rectF.width(), (int) rectF.height(), false), (int) (f / this.mRatio));
            this.mBitmapMatrix.setRectToRect(this.mBitmapRect, this.mPhotoFrameRect, Matrix.ScaleToFit.CENTER);
            Matrix matrix = this.mBitmapMatrix;
            float f2 = this.mScaleProgress;
            matrix.postScale(f2, f2, this.mPhotoFrameRect.centerX(), this.mPhotoFrameRect.centerY());
            this.mBitmapMatrix.mapRect(rectF2);
            rectF.set(this.mPhotoFrameRect);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeFloat(this.mRatio);
            parcel.writeFloat(this.mScaleProgress);
            parcel.writeInt(this.mFrameColor);
        }

        public NormalFrameEntry(Parcel parcel) {
            this.mRatio = parcel.readFloat();
            this.mScaleProgress = parcel.readFloat();
            this.mFrameColor = parcel.readInt();
        }
    }
}
