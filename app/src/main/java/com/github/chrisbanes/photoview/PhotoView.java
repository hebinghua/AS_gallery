package com.github.chrisbanes.photoview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.BitmapRecycleCallback;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.TileBitProvider;
import com.miui.gallery.util.photoview.TrimMemoryCallback;
import com.miui.os.Rom;

/* loaded from: classes.dex */
public class PhotoView extends AppCompatImageView {
    public PhotoViewAttacher attacher;
    public ImageView.ScaleType pendingScaleType;

    public PhotoView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public final void init() {
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher == null || photoViewAttacher.getImageView() == null) {
            if (this.attacher != null) {
                this.attacher = new PhotoViewAttacher.Builder().cloneFrom(this.attacher).build(this);
            } else {
                this.attacher = new PhotoViewAttacher(this);
            }
            super.setScaleType(ImageView.ScaleType.MATRIX);
        }
        ImageView.ScaleType scaleType = this.pendingScaleType;
        if (scaleType != null) {
            setScaleType(scaleType);
            this.pendingScaleType = null;
        }
    }

    public PhotoViewAttacher getAttacher() {
        return this.attacher;
    }

    public void setRotationTo(float f) {
        this.attacher.setRotationTo(f);
    }

    public void setRotationBy(float f) {
        this.attacher.setRotationBy(f);
    }

    public RectF getDisplayRect() {
        return this.attacher.getDisplayRect();
    }

    public RectF getDisplayRect(Matrix matrix) {
        return this.attacher.getDisplayRect(matrix);
    }

    public RectF getBaseDisplayRect() {
        return this.attacher.getBaseDisplayRect();
    }

    public boolean getAbsoluteRect(RectF rectF) {
        return this.attacher.getAbsoluteRect(rectF);
    }

    public RectF getDrawableSize() {
        return this.attacher.getDrawableSize();
    }

    public Matrix getDisplayMatrix() {
        return this.attacher.getDisplayMatrix();
    }

    public boolean setSuppMatrix(Matrix matrix) {
        return this.attacher.setDisplayMatrix(matrix);
    }

    public Matrix getSuppMatrix() {
        return this.attacher.getSuppMatrix();
    }

    public Matrix getBaseMatrix() {
        return this.attacher.getBaseMatrix();
    }

    public void resetMatrix() {
        this.attacher.resetMatrix();
    }

    public boolean setDisplayMatrix(Matrix matrix) {
        return setSuppMatrix(matrix);
    }

    public float getMinimumScale() {
        return this.attacher.getMinimumScale();
    }

    public void setMinimumScale(float f) {
        this.attacher.setMinimumScale(f);
    }

    public float getMediumScale() {
        return this.attacher.getMediumScale();
    }

    public void setMediumScale(float f) {
        this.attacher.setMediumScale(f);
    }

    public float getMaximumScale() {
        return this.attacher.getMaximumScale();
    }

    public void setMaximumScale(float f) {
        this.attacher.setMaximumScale(f);
    }

    public float getScale() {
        return this.attacher.getScale();
    }

    public float getRotate() {
        return this.attacher.getRotate();
    }

    @Override // android.widget.ImageView
    public ImageView.ScaleType getScaleType() {
        return this.attacher.getScaleType();
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher == null) {
            this.pendingScaleType = scaleType;
        } else {
            photoViewAttacher.setScaleType(scaleType);
        }
    }

    public void setDisableDragDownOut(boolean z) {
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher != null) {
            photoViewAttacher.setDisableDragDownOut(z);
        }
    }

    public void setUseFillWidthMode(boolean z) {
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher != null) {
            photoViewAttacher.setUseFillWidthMode(z);
        }
    }

    public void setAllowParentInterceptOnEdge(boolean z) {
        this.attacher.setAllowParentInterceptOnEdge(z);
    }

    public void setImageBitmap(Bitmap bitmap, boolean z) {
        super.setImageDrawable(new BitmapDrawable(getResources(), bitmap));
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher != null) {
            photoViewAttacher.update(z);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher != null) {
            photoViewAttacher.update(false);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i) {
        super.setImageResource(i);
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher != null) {
            photoViewAttacher.update(false);
        }
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        PhotoViewAttacher photoViewAttacher = this.attacher;
        if (photoViewAttacher != null) {
            photoViewAttacher.update(false);
        }
    }

    public void setScaleMinSpan(int i) {
        this.attacher.setScaleMinSpan(i);
    }

    public void setBaseScale(float f) {
        this.attacher.setBaseScale(f);
    }

    public float getBaseScale() {
        return this.attacher.getBaseScale();
    }

    public void setSlipProgress(float f) {
        this.attacher.setSlipProgress(f);
    }

    public void setSlippedRect(int i, int i2) {
        this.attacher.setSlippedRect(i, i2);
    }

    public void addOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.attacher.addOnMatrixChangeListener(onMatrixChangedListener);
    }

    public void removeOnMatrixChangeListener(OnMatrixChangedListener onMatrixChangedListener) {
        this.attacher.removeOnMatrixChangeListener(onMatrixChangedListener);
    }

    public void setOnHandleTouchEventListener(OnHandleTouchEventListener onHandleTouchEventListener) {
        this.attacher.setOnHandleTouchEventListener(onHandleTouchEventListener);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.attacher.setOnClickListener(onClickListener);
    }

    @Override // android.view.View
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.attacher.setOnLongClickListener(onLongClickListener);
    }

    public OnPhotoTapListener getOnPhotoTapListener() {
        return this.attacher.getOnPhotoTapListener();
    }

    public void setOnPhotoTapListener(OnPhotoTapListener onPhotoTapListener) {
        this.attacher.setOnPhotoTapListener(onPhotoTapListener);
    }

    public OnViewTapListener getOnViewTapListener() {
        return this.attacher.getOnViewTapListener();
    }

    public void setOnViewTapListener(OnViewTapListener onViewTapListener) {
        this.attacher.setOnViewTapListener(onViewTapListener);
    }

    public void setOnViewSingleTapListener(OnViewSingleTapListener onViewSingleTapListener) {
        this.attacher.setOnViewSingleTapListener(onViewSingleTapListener);
    }

    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
        this.attacher.setOnDoubleTapListener(onDoubleTapListener);
    }

    public void setOnScaleChangeListener(OnScaleChangeListener onScaleChangeListener) {
        this.attacher.setOnScaleChangeListener(onScaleChangeListener);
    }

    public void setOnScaleStateChangeListener(OnScaleStateChangeListener onScaleStateChangeListener) {
        this.attacher.setOnScaleStateChangeListener(onScaleStateChangeListener);
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.attacher.setOnExitListener(onExitListener);
    }

    public void setOnPhotoViewDragDownOutListener(OnPhotoViewDragDownOutListener onPhotoViewDragDownOutListener) {
        this.attacher.setOnPhotoViewDragDownOutListener(onPhotoViewDragDownOutListener);
    }

    public void setOnBackgroundAlphaChangedListener(OnBackgroundAlphaChangedListener onBackgroundAlphaChangedListener) {
        this.attacher.setOnBackgroundAlphaChangedListener(onBackgroundAlphaChangedListener);
    }

    public void setOnPhotoViewTransitionListener(OnPhotoViewTransitionListener onPhotoViewTransitionListener) {
        this.attacher.setOnPhotoViewTransitionListener(onPhotoViewTransitionListener);
    }

    public void setOnScaleStageChangedListener(OnScaleLevelChangedListener onScaleLevelChangedListener) {
        this.attacher.setOnScaleStageChangedListener(onScaleLevelChangedListener);
    }

    public void addOnRotateListener(OnRotateListener onRotateListener) {
        this.attacher.addOnRotateListener(onRotateListener);
    }

    public void removeOnRotateListener(OnRotateListener onRotateListener) {
        this.attacher.removeOnRotateListener(onRotateListener);
    }

    public void setOnOutsidePhotoTapListener(OnOutsidePhotoTapListener onOutsidePhotoTapListener) {
        this.attacher.setOnOutsidePhotoTapListener(onOutsidePhotoTapListener);
    }

    public void setOnViewDragListener(OnViewDragListener onViewDragListener) {
        this.attacher.setOnViewDragListener(onViewDragListener);
    }

    public void setOnSingleFlingListener(OnSingleFlingListener onSingleFlingListener) {
        this.attacher.setOnSingleFlingListener(onSingleFlingListener);
    }

    public void setScale(float f) {
        this.attacher.setScale(f);
    }

    public void setScale(float f, boolean z) {
        this.attacher.setScale(f, z);
    }

    public void setZoomable(boolean z) {
        this.attacher.setZoomable(z);
    }

    public void setScaleDragEnable(boolean z) {
        this.attacher.setScaleDragEnable(z);
    }

    public Bitmap getVisibleRectangleBitmap() {
        return this.attacher.getVisibleRectangleBitmap();
    }

    public void setBaseRotation(float f) {
        this.attacher.setBaseRotation(f);
    }

    public void setRotatable(boolean z) {
        this.attacher.setRotatable(z);
    }

    public boolean isGestureOperating() {
        return this.attacher.isGestureOperating();
    }

    public void setInterceptTouch(boolean z) {
        this.attacher.setInterceptTouch(z);
    }

    @Override // android.widget.ImageView, android.view.View
    public void onAttachedToWindow() {
        init();
        super.onAttachedToWindow();
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDetachedFromWindow() {
        this.attacher.detach();
        super.onDetachedFromWindow();
    }

    public void animEnter(ItemViewInfo itemViewInfo, TransitionListener transitionListener) {
        this.attacher.animEnter(itemViewInfo, transitionListener);
    }

    public void animExit(ItemViewInfo itemViewInfo, TransitionListener transitionListener) {
        this.attacher.animExit(itemViewInfo, transitionListener);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        if (this.attacher.beforeDraw(canvas)) {
            super.draw(canvas);
            this.attacher.afterDraw(canvas);
        }
    }

    public final boolean isDrawableValid() {
        Drawable drawable = getDrawable();
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            return bitmap != null && !bitmap.isRecycled();
        }
        return true;
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        if (!isDrawableValid()) {
            DefaultLogger.e("PhotoView", "illegal bitmap[%s]", ((BitmapDrawable) getDrawable()).getBitmap());
            if (Rom.IS_STABLE) {
                return;
            }
        }
        super.onDraw(canvas);
    }

    public void setupTile(TileBitProvider tileBitProvider, BitmapRecycleCallback bitmapRecycleCallback, TrimMemoryCallback trimMemoryCallback) {
        this.attacher.setupTile(tileBitProvider, bitmapRecycleCallback, trimMemoryCallback);
    }

    public void releaseTile() {
        this.attacher.releaseTile();
    }

    public void setRegionDecodeEnable(boolean z) {
        this.attacher.setRegionDecodeEnable(z);
    }

    public void setBackgroundAlpha(float f) {
        this.attacher.setBackgroundAlpha(f);
    }

    public void setAlphaBackground(Drawable drawable) {
        this.attacher.setAlphaBackground(drawable);
    }

    public void setHDState(int i, int i2, boolean z) {
        this.attacher.setHDState(i, i2, z);
    }

    public void setStroke(int i, int i2) {
        this.attacher.setStroke(i, i2);
    }

    public boolean translateBy(float f, float f2) {
        return this.attacher.translateBy(f, f2);
    }

    public ScaleLevel getScaleLevel() {
        return this.attacher.getScaleLevel();
    }

    public Rect getViewPort() {
        return this.attacher.getViewPort();
    }

    public void setDragInsideBoundsEnabled(boolean z) {
        this.attacher.setDragInsideBoundsEnabled(z);
    }

    public void setDraggableViewportInsets(Rect rect) {
        this.attacher.setDraggableViewportInsets(rect);
    }

    public void requestAlignBounds() {
        this.attacher.requestAlignBounds();
    }

    public void setPointsScaleEnlargeFactor(float f) {
        this.attacher.setPointsScaleEnlargeFactor(f);
    }

    public void setZoomInterpolator(Interpolator interpolator) {
        this.attacher.setZoomInterpolator(interpolator);
    }

    public void setZoomDuration(int i) {
        this.attacher.setZoomDuration(i);
    }

    public void setZoomDurationLengthenFactor(float f) {
        this.attacher.setZoomDurationLengthenFactor(f);
    }

    public void startSharedElementTransition() {
        this.attacher.startSharedElementTransition();
    }

    public void endSharedElementTransition() {
        this.attacher.endSharedElementTransition();
    }

    public void resetDefaultPhotoStatus() {
        this.attacher.resetDefaultPhotoStatus();
    }

    public void release() {
        this.attacher.release();
    }
}
