package com.miui.gallery.gallerywidget.ui.editor;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.obsoletes.Crop;
import com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class WidgetEditorPlugin extends Crop {
    public final float DEFAULT_RATIO_HEIGHT;
    public final int MASK_COLOR_ALPHA_WIDGET;
    public final String TAG;
    public boolean mCropChanged;
    public CropChangedListener mCropChangedListener;
    public final float mCropCornerRadius;
    public final Path mCropPath;
    public RectF mRatioArea;

    /* loaded from: classes2.dex */
    public interface CropChangedListener {
        void cropChanged();
    }

    public WidgetEditorPlugin(Context context) {
        super(context);
        this.TAG = "WidgetEditorPlugin";
        this.MASK_COLOR_ALPHA_WIDGET = 51;
        this.DEFAULT_RATIO_HEIGHT = 1000.0f;
        setResizeDetectorDisable(true);
        this.mCropPath = new Path();
        this.mCropCornerRadius = context.getResources().getDimension(R.dimen.gallery_widget_bg_radius);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public boolean draw(Canvas canvas) {
        RectF rectF = this.mRatioArea;
        if (rectF == null || rectF.width() <= 0.0f) {
            this.mRatioArea = getRatioArea();
        }
        RectF rectF2 = this.mRatioArea;
        if (rectF2 == null || rectF2.width() <= 0.0f) {
            return true;
        }
        this.mCropPath.reset();
        Path path = this.mCropPath;
        RectF rectF3 = this.mRatioArea;
        float f = this.mCropCornerRadius;
        path.addRoundRect(rectF3, f, f, Path.Direction.CW);
        canvas.drawColor(getContext().getColor(R.color.gallery_widget_editor_crop_bg_ph_outside_color));
        int save = canvas.save();
        canvas.clipPath(this.mCropPath);
        canvas.drawColor(getContext().getColor(R.color.gallery_widget_editor_crop_bg_ph_inside_color));
        canvas.restoreToCount(save);
        return true;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.Crop, com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public void drawOverlay(Canvas canvas) {
        if (getImage() != null) {
            getImageMatrix().mapRect(getImageDisplayBounds(), getImageBounds());
            this.mGlobalMatrix.mapRect(getImageDisplayBounds());
            canvas.getClipBounds(this.mClipBounds);
            this.mCropPath.reset();
            Path path = this.mCropPath;
            RectF rectF = this.mCropArea;
            float f = this.mCropCornerRadius;
            path.addRoundRect(rectF, f, f, Path.Direction.CW);
            this.mPaint.setAlpha(51);
            canvas.drawBitmap(getImage(), getImageMatrix(), this.mPaint);
            this.mPaint.setAlpha(255);
            int save = canvas.save();
            canvas.clipPath(this.mCropPath);
            canvas.drawBitmap(getImage(), getImageMatrix(), this.mPaint);
            canvas.restoreToCount(save);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.obsoletes.Crop, com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView.Plugin
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mCropChanged && motionEvent.getActionMasked() == 2) {
            DefaultLogger.i("WidgetEditorPlugin", "---log---onTouchEvent ACTION_MOVE>");
            this.mCropChanged = true;
            CropChangedListener cropChangedListener = this.mCropChangedListener;
            if (cropChangedListener != null) {
                cropChangedListener.cropChanged();
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    public final RectF getRatioArea() {
        if (this.mAspectRatio != Crop.AspectRatio.RATIO_NONE) {
            Matrix matrix = new Matrix();
            RectF rectF = new RectF(0.0f, 0.0f, 1000.0f, 1000.0f);
            matrix.setRectToRect(rectF, getBounds(), Matrix.ScaleToFit.CENTER);
            RectF rectF2 = new RectF();
            matrix.mapRect(rectF2, rectF);
            return calcCropAreaWithRatio(rectF2, this.mAspectRatio.getWidth() / this.mAspectRatio.getHeight());
        }
        return null;
    }

    public final RectF calcCropAreaWithRatio(RectF rectF, float f) {
        float width = rectF.width() / rectF.height();
        if (Math.abs(width - f) <= Float.MIN_NORMAL) {
            return rectF;
        }
        RectF rectF2 = new RectF(rectF);
        if (width > f) {
            float width2 = ((rectF2.width() / f) - rectF2.height()) / 2.0f;
            rectF2.top -= width2;
            rectF2.bottom += width2;
        } else {
            float height = ((f * rectF2.height()) - rectF2.width()) / 2.0f;
            rectF2.left -= height;
            rectF2.right += height;
        }
        this.mMatrix.setRectToRect(rectF2, getBounds(), Matrix.ScaleToFit.CENTER);
        this.mMatrix.mapRect(rectF2);
        return rectF2;
    }

    public void setRatio(float f) {
        Crop.AspectRatio aspectRatio = new Crop.AspectRatio(f * 1000.0f, 1000.0f);
        this.mAspectRatio = aspectRatio;
        RectF calcCropAreaWithRatio = calcCropAreaWithRatio(this.mCropArea, aspectRatio.getWidth() / this.mAspectRatio.getHeight());
        Matrix matrix = new Matrix(getImageMatrix());
        getImageMatrix().invert(this.mInvertMatrix);
        this.mInvertMatrix.mapRect(this.mInvertArea, calcCropAreaWithRatio);
        float resolveScale = EditorView.Plugin.resolveScale(getImageBounds(), this.mInvertArea);
        matrix.preScale(resolveScale, resolveScale, this.mInvertArea.centerX(), this.mInvertArea.centerY());
        matrix.invert(this.mInvertMatrix);
        this.mInvertMatrix.mapRect(this.mInvertArea, calcCropAreaWithRatio);
        EditorView.Plugin.resolveTranslate(getImageBounds(), this.mInvertArea, this.mOffset);
        PointF pointF = this.mOffset;
        matrix.preTranslate(pointF.x, pointF.y);
        this.mCropAreaChanged = true;
        this.mCropArea.set(calcCropAreaWithRatio);
        getImageMatrix().set(matrix);
    }

    public void setImageMatrix(Matrix matrix) {
        if (matrix == null) {
            return;
        }
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        if (fArr[0] <= 0.0f || fArr[4] <= 0.0f) {
            return;
        }
        getImageMatrix().set(matrix);
        invalidate();
    }

    public void setCropChangedListener(CropChangedListener cropChangedListener) {
        this.mCropChangedListener = cropChangedListener;
    }
}
