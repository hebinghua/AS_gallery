package com.miui.gallery.ui.pictures.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.Size;
import android.view.View;
import androidx.core.content.ContextCompat;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.glide.NonViewAware;
import com.miui.gallery.glide.request.target.ImageAwareClearTarget;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class ImageCell {
    public static Rect sTempRect = new Rect();
    public WeakReference<View> mBindView;
    public WeakReference<RequestDrawingCallback> mCallbackRef;
    public ImageCellData mData;
    public Drawable mDrawable;
    public RectF mFrame;
    public ImageCellAware mImageAware;
    public final Object mLock;
    public Matrix mMatrix;
    public Paint mPaint;
    public int mPrivateFlags;
    public RectF mRectF;

    /* loaded from: classes2.dex */
    public interface RequestDrawingCallback {
        void requestDraw();
    }

    public ImageCell() {
        this(null);
    }

    public ImageCell(View view) {
        this(view, null, null);
    }

    public ImageCell(View view, RectF rectF, ImageCellData imageCellData) {
        this.mLock = new Object();
        this.mPrivateFlags = 2;
        this.mBindView = new WeakReference<>(view);
        this.mFrame = rectF;
        this.mData = imageCellData;
        this.mMatrix = new Matrix();
        this.mRectF = new RectF();
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (sGetAndroidContext != null) {
            int color = ContextCompat.getColor(sGetAndroidContext, R.color.image_border);
            Paint paint = new Paint(1);
            this.mPaint = paint;
            paint.setColor(color);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(sGetAndroidContext.getResources().getDimensionPixelSize(R.dimen.micro_thumb_stroke_width));
        }
    }

    public void setRecyclable(boolean z) {
        if (z) {
            this.mPrivateFlags |= 2;
        } else {
            this.mPrivateFlags &= -3;
        }
    }

    public void setActive(boolean z) {
        if (z) {
            this.mPrivateFlags |= 1;
        } else {
            this.mPrivateFlags &= -2;
        }
    }

    public ImageCell bindView(View view) {
        this.mBindView = new WeakReference<>(view);
        return this;
    }

    public ImageCell setFrame(RectF rectF) {
        this.mFrame = rectF;
        configureBounds();
        invalidate();
        return this;
    }

    public void bindData(ImageCellData imageCellData, RequestOptions requestOptions, RequestOptions requestOptions2) {
        if (!imageCellData.equals(this.mData)) {
            this.mData = imageCellData;
            ImageCellAware imageCellAware = this.mImageAware;
            if (imageCellAware != null) {
                imageCellAware.collect();
            }
            ImageCellAware imageCellAware2 = new ImageCellAware(this);
            this.mImageAware = imageCellAware2;
            BindImageHelper.bindImage(imageCellData.mLocalPath, imageCellData.mDownloadUri, DownloadType.MICRO, imageCellAware2, requestOptions, requestOptions2);
        }
    }

    public void setResourceCallback(RequestDrawingCallback requestDrawingCallback) {
        this.mCallbackRef = new WeakReference<>(requestDrawingCallback);
    }

    public boolean isActive() {
        return (this.mPrivateFlags & 1) == 1;
    }

    public boolean isRecyclable() {
        return (this.mPrivateFlags & 2) == 2;
    }

    public final int getWidth() {
        RectF rectF = this.mFrame;
        if (rectF != null) {
            return (int) rectF.width();
        }
        return 0;
    }

    public final int getHeight() {
        RectF rectF = this.mFrame;
        if (rectF != null) {
            return (int) rectF.height();
        }
        return 0;
    }

    public final boolean setImageDrawable(Drawable drawable) {
        if (isActive()) {
            synchronized (this.mLock) {
                this.mDrawable = drawable;
                invalidate();
            }
            return true;
        }
        return true;
    }

    public final boolean setImageBitmap(Bitmap bitmap) {
        BitmapDrawable bitmapDrawable;
        WeakReference<View> weakReference = this.mBindView;
        if (weakReference != null && weakReference.get() != null) {
            bitmapDrawable = new BitmapDrawable(this.mBindView.get().getResources(), bitmap);
        } else {
            bitmapDrawable = new BitmapDrawable(bitmap);
        }
        return setImageDrawable(bitmapDrawable);
    }

    public void recycle() {
        this.mData = null;
        ImageCellAware imageCellAware = this.mImageAware;
        if (imageCellAware != null) {
            imageCellAware.collect();
            this.mImageAware = null;
        }
        WeakReference<View> weakReference = this.mBindView;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.mBindView = null;
        synchronized (this.mLock) {
            this.mDrawable = null;
        }
        WeakReference<RequestDrawingCallback> weakReference2 = this.mCallbackRef;
        if (weakReference2 != null) {
            weakReference2.clear();
        }
        this.mPrivateFlags &= -2;
    }

    public void invalidate() {
        if (this.mDrawable == null || this.mFrame == null) {
            return;
        }
        WeakReference<RequestDrawingCallback> weakReference = this.mCallbackRef;
        RequestDrawingCallback requestDrawingCallback = weakReference != null ? weakReference.get() : null;
        if (requestDrawingCallback == null) {
            return;
        }
        requestDrawingCallback.requestDraw();
    }

    public final void configureBounds() {
        float f;
        float f2;
        Drawable drawable = this.mDrawable;
        if (drawable == null || this.mFrame == null) {
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = this.mDrawable.getIntrinsicHeight();
        float width = this.mFrame.width();
        float height = this.mFrame.height();
        this.mMatrix.reset();
        boolean z = false;
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            this.mDrawable.setBounds(0, 0, (int) width, (int) height);
        } else {
            this.mDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        }
        if ((intrinsicWidth < 0 || BaseMiscUtil.floatEquals(width, intrinsicWidth)) && (intrinsicHeight < 0 || BaseMiscUtil.floatEquals(height, intrinsicHeight))) {
            z = true;
        }
        if (z) {
            return;
        }
        float f3 = intrinsicWidth;
        float f4 = intrinsicHeight;
        float f5 = 0.0f;
        if (f3 * height > width * f4) {
            f = height / f4;
            f2 = 0.0f;
            f5 = (width - (f3 * f)) * 0.5f;
        } else {
            float f6 = width / f3;
            float f7 = (height - (f4 * f6)) * 0.5f;
            f = f6;
            f2 = f7;
        }
        this.mMatrix.setScale(f, f);
        this.mMatrix.postTranslate(f5, f2);
    }

    public void onDraw(Canvas canvas) {
        synchronized (this.mLock) {
            if (this.mFrame != null && this.mDrawable != null) {
                configureBounds();
                SystemClock.elapsedRealtimeNanos();
                int save = canvas.save();
                RectF rectF = this.mFrame;
                canvas.translate(rectF.left, rectF.top);
                if (this.mRectF == null) {
                    this.mRectF = new RectF();
                }
                this.mRectF.set(0.0f, 0.0f, this.mFrame.width(), this.mFrame.height());
                canvas.clipRect(this.mRectF);
                canvas.concat(this.mMatrix);
                Rect clipBounds = canvas.getClipBounds();
                this.mDrawable.draw(canvas);
                Paint paint = this.mPaint;
                if (paint != null) {
                    canvas.drawRect(clipBounds, paint);
                }
                canvas.restoreToCount(save);
            }
        }
    }

    public final RequestManager getRequestManager() {
        View view;
        WeakReference<View> weakReference = this.mBindView;
        if (weakReference == null || (view = weakReference.get()) == null) {
            return null;
        }
        return GlideRequestManagerHelper.safeGet(view);
    }

    /* loaded from: classes2.dex */
    public static class ImageCellAware extends NonViewAware {
        public WeakReference<ImageCell> mImageCellRef;

        public ImageCellAware(ImageCell imageCell) {
            super(new Size(0, 0));
            this.mImageCellRef = new WeakReference<>(imageCell);
        }

        @Override // com.miui.gallery.glide.NonViewAware, com.miui.gallery.glide.ImageAware
        public RequestManager getRequestManager() {
            ImageCell imageCell = this.mImageCellRef.get();
            if (imageCell != null) {
                return imageCell.getRequestManager();
            }
            return super.getRequestManager();
        }

        public final void collect() {
            RequestManager requestManager = getRequestManager();
            if (requestManager != null) {
                requestManager.clear(new ImageAwareClearTarget(this));
            }
            this.mImageCellRef.clear();
        }

        @Override // com.miui.gallery.glide.NonViewAware, com.miui.gallery.glide.ImageAware
        public boolean isCollected() {
            return this.mImageCellRef.get() == null;
        }

        @Override // com.miui.gallery.glide.NonViewAware, com.miui.gallery.glide.ImageAware
        public int getWidth() {
            ImageCell imageCell = this.mImageCellRef.get();
            if (imageCell != null) {
                return imageCell.getWidth();
            }
            return 0;
        }

        @Override // com.miui.gallery.glide.NonViewAware, com.miui.gallery.glide.ImageAware
        public int getHeight() {
            ImageCell imageCell = this.mImageCellRef.get();
            if (imageCell != null) {
                return imageCell.getHeight();
            }
            return 0;
        }

        @Override // com.miui.gallery.glide.ImageAware
        public boolean setImageDrawable(Drawable drawable) {
            ImageCell imageCell = this.mImageCellRef.get();
            if (imageCell != null) {
                return imageCell.setImageDrawable(drawable);
            }
            return false;
        }

        @Override // com.miui.gallery.glide.ImageAware
        public boolean setImageBitmap(Bitmap bitmap) {
            ImageCell imageCell = this.mImageCellRef.get();
            if (imageCell != null) {
                return imageCell.setImageBitmap(bitmap);
            }
            return false;
        }
    }
}
