package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Size;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.glide.NonViewAware;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.request.target.BitmapImageAwareTarget;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import java.lang.ref.WeakReference;

/* loaded from: classes3.dex */
public final class ImageTransitionRender extends BaseTransitionRender {
    public Drawable mDrawable;
    public WeakReference<View> mHost;
    public Size mImageSize;
    public String mLocalPath;
    public Matrix mMatrix;
    public RequestOptions mPreviewOptions;
    public String mPreviewPath;
    public RequestOptions mRequestOptions;
    public ImageView.ScaleType mScaleType;
    public Target<?> mTarget;

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public int sortFactor() {
        return 0;
    }

    public /* synthetic */ ImageTransitionRender(Builder builder, AnonymousClass1 anonymousClass1) {
        this(builder);
    }

    public ImageTransitionRender(Builder builder) {
        super(builder.mFromFrame, builder.mToFrame, builder.mFromAlpha, builder.mToAlpha, builder.mAlphaInterpolator);
        this.mHost = new WeakReference<>(builder.mHost);
        this.mLocalPath = builder.mLocalPath;
        this.mPreviewPath = TextUtils.isEmpty(builder.mPreviewPath) ? builder.mLocalPath : builder.mPreviewPath;
        this.mScaleType = builder.mScaleType;
        this.mRequestOptions = builder.mRequestOptions;
        this.mPreviewOptions = builder.mPreviewOptions;
        this.mImageSize = builder.mImageSize;
        this.mMatrix = new Matrix();
        updateDrawable(builder.mDrawable);
    }

    public final void updateDrawable(Drawable drawable) {
        if (this.mDrawable != drawable) {
            this.mDrawable = drawable;
            if (getAnimFrame() == null) {
                return;
            }
            invalidate();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public boolean skipDraw() {
        return this.mDrawable == null;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public void onPreDraw(RectF rectF, int i, float f) {
        configureBounds(rectF);
        this.mDrawable.setAlpha(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.BaseTransitionRender
    public void onDraw(Canvas canvas, RectF rectF) {
        int save = canvas.save();
        canvas.translate(rectF.left, rectF.top);
        canvas.clipRect(0.0f, 0.0f, rectF.width(), rectF.height());
        canvas.concat(this.mMatrix);
        this.mDrawable.draw(canvas);
        canvas.restoreToCount(save);
    }

    public View getHostView() {
        return this.mHost.get();
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public void onPreTransition() {
        submitLoadTask();
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitionRender
    public void onPostTransition() {
        cancelLoadTask();
        Drawable drawable = this.mDrawable;
        if (drawable == null || drawable.getAlpha() == 255) {
            return;
        }
        this.mDrawable.setAlpha(255);
    }

    public final RequestManager getRequestManager() {
        View view = this.mHost.get();
        if (view != null) {
            return GlideRequestManagerHelper.safeGet(view);
        }
        return null;
    }

    public final void submitLoadTask() {
        RequestManager requestManager;
        if (this.mDrawable != null || TextUtils.isEmpty(this.mLocalPath) || (requestManager = getRequestManager()) == null) {
            return;
        }
        RequestBuilder<Bitmap> requestBuilder = null;
        if (this.mPreviewOptions != null) {
            requestBuilder = requestManager.mo985asBitmap().mo962load(GalleryModel.of(this.mPreviewPath)).mo946apply((BaseRequestOptions<?>) this.mPreviewOptions);
        }
        this.mTarget = requestManager.mo985asBitmap().mo962load(GalleryModel.of(this.mLocalPath)).mo946apply((BaseRequestOptions<?>) this.mRequestOptions).mo979thumbnail(requestBuilder).into((RequestBuilder<Bitmap>) new BitmapImageAwareTarget(new RenderViewAware(this)));
    }

    public final void cancelLoadTask() {
        Target<?> target;
        RequestManager requestManager = getRequestManager();
        if (requestManager == null || (target = this.mTarget) == null) {
            return;
        }
        requestManager.clear(target);
        this.mTarget = null;
    }

    public final void configureBounds(RectF rectF) {
        float f;
        float f2;
        Drawable drawable = this.mDrawable;
        if (drawable == null) {
            return;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = this.mDrawable.getIntrinsicHeight();
        float width = rectF.width();
        float height = rectF.height();
        boolean z = (intrinsicWidth < 0 || BaseMiscUtil.floatEquals(width, (float) intrinsicWidth)) && (intrinsicHeight < 0 || BaseMiscUtil.floatEquals(height, (float) intrinsicHeight));
        this.mMatrix.reset();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0 || ImageView.ScaleType.FIT_XY == this.mScaleType) {
            this.mDrawable.setBounds(0, 0, (int) width, (int) height);
            return;
        }
        this.mDrawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        ImageView.ScaleType scaleType = ImageView.ScaleType.MATRIX;
        ImageView.ScaleType scaleType2 = this.mScaleType;
        if (scaleType == scaleType2 || z) {
            return;
        }
        if (ImageView.ScaleType.CENTER == scaleType2) {
            this.mMatrix.setTranslate(Math.round((width - intrinsicWidth) * 0.5f), Math.round((height - intrinsicHeight) * 0.5f));
            return;
        }
        float f3 = 0.0f;
        if (ImageView.ScaleType.CENTER_CROP == scaleType2) {
            float f4 = intrinsicWidth;
            float f5 = intrinsicHeight;
            if (f4 * height > width * f5) {
                f2 = height / f5;
                f = 0.0f;
                f3 = (width - (f4 * f2)) * 0.5f;
            } else {
                float f6 = width / f4;
                f = (height - (f5 * f6)) * 0.5f;
                f2 = f6;
            }
            this.mMatrix.setScale(f2, f2);
            this.mMatrix.postTranslate(f3, f);
        } else if (ImageView.ScaleType.CENTER_INSIDE == scaleType2) {
            float f7 = intrinsicWidth;
            float min = (f7 > width || ((float) intrinsicHeight) > height) ? Math.min(width / f7, height / intrinsicHeight) : 1.0f;
            this.mMatrix.setScale(min, min);
            this.mMatrix.postTranslate(Math.round((width - (f7 * min)) * 0.5f), Math.round((height - (intrinsicHeight * min)) * 0.5f));
        } else {
            getTemp1().set(0.0f, 0.0f, intrinsicWidth, intrinsicHeight);
            getTemp2().set(0.0f, 0.0f, width, height);
            this.mMatrix.setRectToRect(getTemp1(), getTemp2(), scaleTypeToScaleToFit(this.mScaleType));
        }
    }

    /* renamed from: com.miui.gallery.widget.recyclerview.transition.ImageTransitionRender$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;

        static {
            int[] iArr = new int[ImageView.ScaleType.values().length];
            $SwitchMap$android$widget$ImageView$ScaleType = iArr;
            try {
                iArr[ImageView.ScaleType.FIT_START.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static Matrix.ScaleToFit scaleTypeToScaleToFit(ImageView.ScaleType scaleType) {
        int i = AnonymousClass1.$SwitchMap$android$widget$ImageView$ScaleType[scaleType.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return Matrix.ScaleToFit.CENTER;
            }
            if (i == 3) {
                return Matrix.ScaleToFit.END;
            }
            return Matrix.ScaleToFit.FILL;
        }
        return Matrix.ScaleToFit.START;
    }

    /* loaded from: classes3.dex */
    public static final class Builder {
        public Interpolator mAlphaInterpolator;
        public Drawable mDrawable;
        public int mFromAlpha = 255;
        public RectF mFromFrame;
        public View mHost;
        public Size mImageSize;
        public String mLocalPath;
        public RequestOptions mPreviewOptions;
        public String mPreviewPath;
        public RequestOptions mRequestOptions;
        public ImageView.ScaleType mScaleType;
        public int mToAlpha;
        public RectF mToFrame;

        public Builder(View view) {
            this.mHost = view;
        }

        public Builder setLocalPath(String str) {
            this.mLocalPath = str;
            return this;
        }

        public Builder setPreviewPath(String str) {
            this.mPreviewPath = str;
            return this;
        }

        public Builder setDrawable(Drawable drawable) {
            this.mDrawable = drawable;
            return this;
        }

        public Builder setScaleType(ImageView.ScaleType scaleType) {
            this.mScaleType = scaleType;
            return this;
        }

        public Builder setFromFrame(RectF rectF) {
            this.mFromFrame = rectF;
            return this;
        }

        public Builder setToFrame(RectF rectF) {
            this.mToFrame = rectF;
            return this;
        }

        public Builder setFromAlpha(int i) {
            this.mFromAlpha = i;
            return this;
        }

        public Builder setToAlpha(int i) {
            this.mToAlpha = i;
            return this;
        }

        public Builder setRequestOptions(RequestOptions requestOptions) {
            this.mRequestOptions = requestOptions;
            return this;
        }

        public Builder setPreviewOptions(RequestOptions requestOptions) {
            this.mPreviewOptions = requestOptions;
            return this;
        }

        public Builder setImageSize(Size size) {
            this.mImageSize = size;
            return this;
        }

        public Builder setAlphaInterpolator(Interpolator interpolator) {
            this.mAlphaInterpolator = interpolator;
            return this;
        }

        public ImageTransitionRender build() {
            return new ImageTransitionRender(this, null);
        }
    }

    /* loaded from: classes3.dex */
    public static class RenderViewAware extends NonViewAware {
        public WeakReference<ImageTransitionRender> mRenderRef;

        public RenderViewAware(ImageTransitionRender imageTransitionRender) {
            super(imageTransitionRender.mImageSize);
            this.mRenderRef = new WeakReference<>(imageTransitionRender);
        }

        @Override // com.miui.gallery.glide.NonViewAware, com.miui.gallery.glide.ImageAware
        public RequestManager getRequestManager() {
            ImageTransitionRender imageTransitionRender = this.mRenderRef.get();
            if (imageTransitionRender != null) {
                return imageTransitionRender.getRequestManager();
            }
            return super.getRequestManager();
        }

        @Override // com.miui.gallery.glide.ImageAware
        public boolean setImageBitmap(Bitmap bitmap) {
            ImageTransitionRender imageTransitionRender = this.mRenderRef.get();
            return setImageDrawable(new BitmapDrawable((imageTransitionRender == null || imageTransitionRender.getHostView() == null) ? null : imageTransitionRender.getHostView().getResources(), bitmap));
        }

        @Override // com.miui.gallery.glide.ImageAware
        public boolean setImageDrawable(Drawable drawable) {
            ImageTransitionRender imageTransitionRender = this.mRenderRef.get();
            if (imageTransitionRender != null) {
                imageTransitionRender.updateDrawable(drawable);
                return true;
            }
            return true;
        }
    }
}
