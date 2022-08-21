package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Size;
import android.widget.ImageView;
import ch.qos.logback.core.CoreConstants;
import com.bumptech.glide.request.RequestOptions;

/* loaded from: classes3.dex */
public class ThumbTransitionalItem implements ImageTransitItem {
    public Drawable mDrawable;
    public RectF mFrame;
    public long mId;
    public Size mImageSize;
    public RequestOptions mOptions;
    public String mPath;
    public RequestOptions mPreviewOptions;
    public String mPreviewPath;
    public ImageView.ScaleType mScaleType;

    public ThumbTransitionalItem(long j, RectF rectF, String str, String str2, Drawable drawable, ImageView.ScaleType scaleType, RequestOptions requestOptions, RequestOptions requestOptions2, Size size) {
        this.mId = j;
        this.mFrame = rectF;
        this.mPath = str;
        this.mPreviewPath = str2;
        this.mDrawable = drawable;
        this.mScaleType = scaleType;
        this.mOptions = requestOptions;
        this.mPreviewOptions = requestOptions2;
        this.mImageSize = size;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public long getTransitId() {
        return this.mId;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ITransitItem
    public RectF getTransitFrame() {
        return this.mFrame;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public String getTransitPath() {
        return this.mPath;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public String getTransitPreviewPath() {
        return this.mPreviewPath;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public Drawable getTransitDrawable() {
        return this.mDrawable;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public ImageView.ScaleType getTransitScaleType() {
        return this.mScaleType;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public RequestOptions getOptions() {
        return this.mOptions;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public RequestOptions getPreviewOptions() {
        return this.mPreviewOptions;
    }

    @Override // com.miui.gallery.widget.recyclerview.transition.ImageTransitItem
    public Size getImageSize() {
        return this.mImageSize;
    }

    public String toString() {
        return "ThumbTransitionalItem{mId=" + this.mId + ", mFrame=" + this.mFrame.toShortString() + ", mPath='" + this.mPath + CoreConstants.SINGLE_QUOTE_CHAR + ", mScaleType=" + this.mScaleType + '}';
    }
}
