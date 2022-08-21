package com.miui.gallery.adapter.itemmodel.base;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.request.RequestOptions;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.common.BaseItemModel;
import com.miui.gallery.base_optimization.support.IUtilsMethodSupport;
import com.miui.gallery.base_optimization.support.IViewSupport;
import com.miui.gallery.base_optimization.support.UtilsMethodSupportDelegate;
import com.miui.gallery.base_optimization.support.ViewSupportDelegate;
import com.miui.gallery.util.imageloader.IImageLoaderSupport;
import com.miui.gallery.util.imageloader.ImageLoaderSupportDelegate;

/* loaded from: classes.dex */
public abstract class BaseGalleryItemModel<DATA, VH extends EpoxyViewHolder> extends BaseItemModel<DATA, VH> implements IViewSupport, IUtilsMethodSupport, IImageLoaderSupport {
    public IImageLoaderSupport mImageLoaderSupport;
    public IUtilsMethodSupport mUtilsMethodSupport;
    public IViewSupport mViewSupport;

    public BaseGalleryItemModel() {
        this(-1L);
    }

    public BaseGalleryItemModel(long j) {
        this(j, null);
    }

    public BaseGalleryItemModel(long j, DATA data) {
        super(j, data);
        this.mViewSupport = ViewSupportDelegate.getInstance();
        this.mImageLoaderSupport = ImageLoaderSupportDelegate.getInstance();
        this.mUtilsMethodSupport = UtilsMethodSupportDelegate.getInstance();
    }

    @Override // com.miui.gallery.base_optimization.support.IViewSupport
    public void setText(TextView textView, CharSequence charSequence) {
        this.mViewSupport.setText(textView, charSequence);
    }

    @Override // com.miui.gallery.base_optimization.support.IViewSupport
    public void gone(View view) {
        this.mViewSupport.gone(view);
    }

    @Override // com.miui.gallery.base_optimization.support.IUtilsMethodSupport
    public boolean isEmpty(Object obj) {
        return this.mUtilsMethodSupport.isEmpty(obj);
    }

    @Override // com.miui.gallery.base_optimization.support.IUtilsMethodSupport
    public boolean isEmpty(CharSequence charSequence) {
        return this.mUtilsMethodSupport.isEmpty(charSequence);
    }

    @Override // com.miui.gallery.base_optimization.support.IUtilsMethodSupport
    public boolean isEquals(Object obj, Object obj2) {
        return this.mUtilsMethodSupport.isEquals(obj, obj2);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, RequestOptions requestOptions) {
        this.mImageLoaderSupport.bindImage(imageView, requestOptions);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, String str, Uri uri, RequestOptions requestOptions) {
        this.mImageLoaderSupport.bindImage(imageView, str, uri, requestOptions);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, String str, RequestOptions requestOptions) {
        this.mImageLoaderSupport.bindImage(imageView, str, requestOptions);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void bindImage(ImageView imageView, Uri uri, RequestOptions requestOptions) {
        this.mImageLoaderSupport.bindImage(imageView, uri, requestOptions);
    }

    @Override // com.miui.gallery.util.imageloader.IImageLoaderSupport
    public void unbindImage(ImageView imageView) {
        this.mImageLoaderSupport.unbindImage(imageView);
    }
}
