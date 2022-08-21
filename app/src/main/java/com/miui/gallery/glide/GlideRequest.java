package com.miui.gallery.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.miui.gallery.glide.load.RegionConfig;

/* loaded from: classes2.dex */
public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> {
    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: apply */
    public /* bridge */ /* synthetic */ RequestBuilder mo946apply(BaseRequestOptions baseRequestOptions) {
        return mo946apply((BaseRequestOptions<?>) baseRequestOptions);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: apply  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ BaseRequestOptions mo946apply(BaseRequestOptions baseRequestOptions) {
        return mo946apply((BaseRequestOptions<?>) baseRequestOptions);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: decode */
    public /* bridge */ /* synthetic */ BaseRequestOptions mo949decode(Class cls) {
        return mo949decode((Class<?>) cls);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalTransform */
    public /* bridge */ /* synthetic */ BaseRequestOptions mo969optionalTransform(Transformation transformation) {
        return mo969optionalTransform((Transformation<Bitmap>) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: set */
    public /* bridge */ /* synthetic */ BaseRequestOptions mo975set(Option option, Object obj) {
        return mo975set((Option<Option>) option, (Option) obj);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: transform */
    public /* bridge */ /* synthetic */ BaseRequestOptions mo980transform(Transformation transformation) {
        return mo980transform((Transformation<Bitmap>) transformation);
    }

    public GlideRequest(Glide glide, RequestManager requestManager, Class<TranscodeType> cls, Context context) {
        super(glide, requestManager, cls, context);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: sizeMultiplier  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo977sizeMultiplier(float f) {
        return (GlideRequest) super.mo977sizeMultiplier(f);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: useUnlimitedSourceGeneratorsPool  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo983useUnlimitedSourceGeneratorsPool(boolean z) {
        return (GlideRequest) super.mo983useUnlimitedSourceGeneratorsPool(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: useAnimationPool  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo982useAnimationPool(boolean z) {
        return (GlideRequest) super.mo982useAnimationPool(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: onlyRetrieveFromCache  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo965onlyRetrieveFromCache(boolean z) {
        return (GlideRequest) super.mo965onlyRetrieveFromCache(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: diskCacheStrategy  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo950diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        return (GlideRequest) super.mo950diskCacheStrategy(diskCacheStrategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: priority  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo974priority(Priority priority) {
        return (GlideRequest) super.mo974priority(priority);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: placeholder  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo973placeholder(Drawable drawable) {
        return (GlideRequest) super.mo973placeholder(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: placeholder  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo972placeholder(int i) {
        return (GlideRequest) super.mo972placeholder(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: fallback  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo957fallback(Drawable drawable) {
        return (GlideRequest) super.mo957fallback(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: fallback  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo956fallback(int i) {
        return (GlideRequest) super.mo956fallback(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: error  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo955error(Drawable drawable) {
        return (GlideRequest) super.mo955error(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: error  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo954error(int i) {
        return (GlideRequest) super.mo954error(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: skipMemoryCache  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo978skipMemoryCache(boolean z) {
        return (GlideRequest) super.mo978skipMemoryCache(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: override  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo971override(int i, int i2) {
        return (GlideRequest) super.mo971override(i, i2);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: override  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo970override(int i) {
        return (GlideRequest) super.mo970override(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: signature  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo976signature(Key key) {
        return (GlideRequest) super.mo976signature(key);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: set  reason: collision with other method in class */
    public <Y> GlideRequest<TranscodeType> mo975set(Option<Y> option, Y y) {
        return (GlideRequest) super.mo975set((Option<Option<Y>>) option, (Option<Y>) y);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: decode  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo949decode(Class<?> cls) {
        return (GlideRequest) super.mo949decode(cls);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: encodeQuality  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo953encodeQuality(int i) {
        return (GlideRequest) super.mo953encodeQuality(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: format  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo958format(DecodeFormat decodeFormat) {
        return (GlideRequest) super.mo958format(decodeFormat);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: downsample  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo952downsample(DownsampleStrategy downsampleStrategy) {
        return (GlideRequest) super.mo952downsample(downsampleStrategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalCenterCrop  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo966optionalCenterCrop() {
        return (GlideRequest) super.mo966optionalCenterCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalFitCenter  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo968optionalFitCenter() {
        return (GlideRequest) super.mo968optionalFitCenter();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalCenterInside  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo967optionalCenterInside() {
        return (GlideRequest) super.mo967optionalCenterInside();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: transform  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo980transform(Transformation<Bitmap> transformation) {
        return (GlideRequest) super.mo980transform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalTransform  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo969optionalTransform(Transformation<Bitmap> transformation) {
        return (GlideRequest) super.mo969optionalTransform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: dontTransform  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo951dontTransform() {
        return (GlideRequest) super.mo951dontTransform();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: lock  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo964lock() {
        return (GlideRequest) super.mo964lock();
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: apply */
    public GlideRequest<TranscodeType> mo946apply(BaseRequestOptions<?> baseRequestOptions) {
        return (GlideRequest) super.mo946apply(baseRequestOptions);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: transition  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo981transition(TransitionOptions<?, ? super TranscodeType> transitionOptions) {
        return (GlideRequest) super.mo981transition((TransitionOptions) transitionOptions);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: listener  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo959listener(RequestListener<TranscodeType> requestListener) {
        return (GlideRequest) super.mo959listener((RequestListener) requestListener);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: addListener  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo945addListener(RequestListener<TranscodeType> requestListener) {
        return (GlideRequest) super.mo945addListener((RequestListener) requestListener);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: thumbnail  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo979thumbnail(RequestBuilder<TranscodeType> requestBuilder) {
        return (GlideRequest) super.mo979thumbnail((RequestBuilder) requestBuilder);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo962load(Object obj) {
        return (GlideRequest) super.mo962load(obj);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo963load(String str) {
        return (GlideRequest) super.mo963load(str);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo960load(Uri uri) {
        return (GlideRequest) super.mo960load(uri);
    }

    @Override // com.bumptech.glide.RequestBuilder
    /* renamed from: load  reason: collision with other method in class */
    public GlideRequest<TranscodeType> mo961load(Integer num) {
        return (GlideRequest) super.mo961load(num);
    }

    @Override // com.bumptech.glide.RequestBuilder, com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: clone */
    public GlideRequest<TranscodeType> mo948clone() {
        return (GlideRequest) super.mo948clone();
    }

    public GlideRequest<TranscodeType> fileLength(long j) {
        return (GlideRequest) GalleryLibraryGlideExtension.fileLength(this, j);
    }

    public GlideRequest<TranscodeType> secretKey(byte[] bArr) {
        return (GlideRequest) GalleryLibraryGlideExtension.secretKey(this, bArr);
    }

    public GlideRequest<TranscodeType> decodeRegion(RegionConfig regionConfig) {
        return (GlideRequest) GalleryLibraryGlideExtension.decodeRegion(this, regionConfig);
    }

    public GlideRequest<TranscodeType> skipCache() {
        return (GlideRequest) GalleryGlideExtension.skipCache(this);
    }
}
