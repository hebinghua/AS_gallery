package com.miui.gallery.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;

/* loaded from: classes2.dex */
public class GlideRequests extends RequestManager {
    public GlideRequests(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode, Context context) {
        super(glide, lifecycle, requestManagerTreeNode, context);
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: as  reason: collision with other method in class */
    public <ResourceType> GlideRequest<ResourceType> mo984as(Class<ResourceType> cls) {
        return new GlideRequest<>(this.glide, this, cls, this.context);
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: asBitmap  reason: collision with other method in class */
    public GlideRequest<Bitmap> mo985asBitmap() {
        return (GlideRequest) super.mo985asBitmap();
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: asDrawable  reason: collision with other method in class */
    public GlideRequest<Drawable> mo986asDrawable() {
        return (GlideRequest) super.mo986asDrawable();
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<Drawable> mo990load(String str) {
        return (GlideRequest) super.mo990load(str);
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<Drawable> mo987load(Uri uri) {
        return (GlideRequest) super.mo987load(uri);
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<Drawable> mo988load(Integer num) {
        return (GlideRequest) super.mo988load(num);
    }

    @Override // com.bumptech.glide.RequestManager
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<Drawable> mo989load(Object obj) {
        return (GlideRequest) super.mo989load(obj);
    }

    @Override // com.bumptech.glide.RequestManager
    public void setRequestOptions(RequestOptions requestOptions) {
        if (requestOptions instanceof GlideOptions) {
            super.setRequestOptions(requestOptions);
        } else {
            super.setRequestOptions(new GlideOptions().mo946apply((BaseRequestOptions<?>) requestOptions));
        }
    }
}
