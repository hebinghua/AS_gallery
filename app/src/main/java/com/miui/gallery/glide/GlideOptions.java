package com.miui.gallery.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.util.face.FaceRegionRectF;

/* loaded from: classes2.dex */
public final class GlideOptions extends RequestOptions {
    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: apply */
    public /* bridge */ /* synthetic */ RequestOptions mo946apply(BaseRequestOptions baseRequestOptions) {
        return mo946apply((BaseRequestOptions<?>) baseRequestOptions);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: decode */
    public /* bridge */ /* synthetic */ RequestOptions mo949decode(Class cls) {
        return mo949decode((Class<?>) cls);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalTransform */
    public /* bridge */ /* synthetic */ RequestOptions mo969optionalTransform(Transformation transformation) {
        return mo969optionalTransform((Transformation<Bitmap>) transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: set */
    public /* bridge */ /* synthetic */ RequestOptions mo975set(Option option, Object obj) {
        return mo975set((Option<Option>) option, (Option) obj);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: transform */
    public /* bridge */ /* synthetic */ RequestOptions mo980transform(Transformation transformation) {
        return mo980transform((Transformation<Bitmap>) transformation);
    }

    public static GlideOptions formatOf(DecodeFormat decodeFormat) {
        return new GlideOptions().mo958format(decodeFormat);
    }

    public static GlideOptions downsampleOf(DownsampleStrategy downsampleStrategy) {
        return new GlideOptions().mo952downsample(downsampleStrategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: sizeMultiplier  reason: avoid collision after fix types in other method */
    public RequestOptions mo977sizeMultiplier(float f) {
        return (GlideOptions) super.mo977sizeMultiplier(f);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: useUnlimitedSourceGeneratorsPool  reason: avoid collision after fix types in other method */
    public RequestOptions mo983useUnlimitedSourceGeneratorsPool(boolean z) {
        return (GlideOptions) super.mo983useUnlimitedSourceGeneratorsPool(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: useAnimationPool  reason: avoid collision after fix types in other method */
    public RequestOptions mo982useAnimationPool(boolean z) {
        return (GlideOptions) super.mo982useAnimationPool(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: onlyRetrieveFromCache  reason: avoid collision after fix types in other method */
    public RequestOptions mo965onlyRetrieveFromCache(boolean z) {
        return (GlideOptions) super.mo965onlyRetrieveFromCache(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: diskCacheStrategy  reason: avoid collision after fix types in other method */
    public RequestOptions mo950diskCacheStrategy(DiskCacheStrategy diskCacheStrategy) {
        return (GlideOptions) super.mo950diskCacheStrategy(diskCacheStrategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: priority  reason: avoid collision after fix types in other method */
    public RequestOptions mo974priority(Priority priority) {
        return (GlideOptions) super.mo974priority(priority);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: placeholder  reason: avoid collision after fix types in other method */
    public RequestOptions mo973placeholder(Drawable drawable) {
        return (GlideOptions) super.mo973placeholder(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: placeholder  reason: avoid collision after fix types in other method */
    public RequestOptions mo972placeholder(int i) {
        return (GlideOptions) super.mo972placeholder(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: fallback  reason: avoid collision after fix types in other method */
    public RequestOptions mo957fallback(Drawable drawable) {
        return (GlideOptions) super.mo957fallback(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: fallback  reason: avoid collision after fix types in other method */
    public RequestOptions mo956fallback(int i) {
        return (GlideOptions) super.mo956fallback(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: error  reason: avoid collision after fix types in other method */
    public RequestOptions mo955error(Drawable drawable) {
        return (GlideOptions) super.mo955error(drawable);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: error  reason: avoid collision after fix types in other method */
    public RequestOptions mo954error(int i) {
        return (GlideOptions) super.mo954error(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: skipMemoryCache  reason: avoid collision after fix types in other method */
    public RequestOptions mo978skipMemoryCache(boolean z) {
        return (GlideOptions) super.mo978skipMemoryCache(z);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: override  reason: avoid collision after fix types in other method */
    public RequestOptions mo971override(int i, int i2) {
        return (GlideOptions) super.mo971override(i, i2);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: override */
    public RequestOptions mo970override(int i) {
        return (GlideOptions) super.mo970override(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: signature  reason: avoid collision after fix types in other method */
    public RequestOptions mo976signature(Key key) {
        return (GlideOptions) super.mo976signature(key);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: clone  reason: collision with other method in class */
    public RequestOptions mo948clone() {
        return (GlideOptions) super.clone();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: set  reason: avoid collision after fix types in other method */
    public <Y> RequestOptions mo975set(Option<Y> option, Y y) {
        return (GlideOptions) super.mo975set((Option<Option<Y>>) option, (Option<Y>) y);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: decode  reason: avoid collision after fix types in other method */
    public RequestOptions mo949decode(Class<?> cls) {
        return (GlideOptions) super.mo949decode(cls);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions encodeFormat(Bitmap.CompressFormat compressFormat) {
        return (GlideOptions) super.encodeFormat(compressFormat);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: encodeQuality  reason: avoid collision after fix types in other method */
    public RequestOptions mo953encodeQuality(int i) {
        return (GlideOptions) super.mo953encodeQuality(i);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: format  reason: avoid collision after fix types in other method */
    public RequestOptions mo958format(DecodeFormat decodeFormat) {
        return (GlideOptions) super.mo958format(decodeFormat);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: downsample  reason: avoid collision after fix types in other method */
    public RequestOptions mo952downsample(DownsampleStrategy downsampleStrategy) {
        return (GlideOptions) super.mo952downsample(downsampleStrategy);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalCenterCrop  reason: avoid collision after fix types in other method */
    public RequestOptions mo966optionalCenterCrop() {
        return (GlideOptions) super.mo966optionalCenterCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions centerCrop() {
        return (GlideOptions) super.centerCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalFitCenter  reason: avoid collision after fix types in other method */
    public RequestOptions mo968optionalFitCenter() {
        return (GlideOptions) super.mo968optionalFitCenter();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalCenterInside  reason: avoid collision after fix types in other method */
    public RequestOptions mo967optionalCenterInside() {
        return (GlideOptions) super.mo967optionalCenterInside();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions circleCrop() {
        return (GlideOptions) super.circleCrop();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: transform  reason: avoid collision after fix types in other method */
    public RequestOptions mo980transform(Transformation<Bitmap> transformation) {
        return (GlideOptions) super.mo980transform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: optionalTransform  reason: avoid collision after fix types in other method */
    public RequestOptions mo969optionalTransform(Transformation<Bitmap> transformation) {
        return (GlideOptions) super.mo969optionalTransform(transformation);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: dontTransform  reason: avoid collision after fix types in other method */
    public RequestOptions mo951dontTransform() {
        return (GlideOptions) super.mo951dontTransform();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: apply  reason: collision with other method in class */
    public RequestOptions mo946apply(BaseRequestOptions<?> baseRequestOptions) {
        return (GlideOptions) super.mo946apply(baseRequestOptions);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    /* renamed from: lock  reason: avoid collision after fix types in other method */
    public RequestOptions mo964lock() {
        return (GlideOptions) super.mo964lock();
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestOptions autoClone() {
        return (GlideOptions) super.autoClone();
    }

    public GlideOptions fileLength(long j) {
        return (GlideOptions) GalleryLibraryGlideExtension.fileLength(this, j);
    }

    public GlideOptions secretKey(byte[] bArr) {
        return (GlideOptions) GalleryLibraryGlideExtension.secretKey(this, bArr);
    }

    public GlideOptions decodeRegion(RegionConfig regionConfig) {
        return (GlideOptions) GalleryLibraryGlideExtension.decodeRegion(this, regionConfig);
    }

    public GlideOptions thumb(long j) {
        return (GlideOptions) GalleryGlideExtension.thumb(this, j);
    }

    public GlideOptions tinyThumb() {
        return (GlideOptions) GalleryGlideExtension.tinyThumb(this);
    }

    public GlideOptions tinyThumb(long j) {
        return (GlideOptions) GalleryGlideExtension.tinyThumb(this, j);
    }

    public GlideOptions miniThumb() {
        return (GlideOptions) GalleryGlideExtension.miniThumb(this);
    }

    public GlideOptions microThumb() {
        return (GlideOptions) GalleryGlideExtension.microThumb(this);
    }

    public GlideOptions microThumb(long j) {
        return (GlideOptions) GalleryGlideExtension.microThumb(this, j);
    }

    public GlideOptions largeThumb() {
        return (GlideOptions) GalleryGlideExtension.largeThumb(this);
    }

    public GlideOptions bigPhoto() {
        return (GlideOptions) GalleryGlideExtension.bigPhoto(this);
    }

    public GlideOptions bigPhoto(long j) {
        return (GlideOptions) GalleryGlideExtension.bigPhoto(this, j);
    }

    public GlideOptions pixelsThumb() {
        return (GlideOptions) GalleryGlideExtension.pixelsThumb(this);
    }

    public GlideOptions pixelsThumb(long j) {
        return (GlideOptions) GalleryGlideExtension.pixelsThumb(this, j);
    }

    public GlideOptions peopleFace() {
        return (GlideOptions) GalleryGlideExtension.peopleFace(this);
    }

    public GlideOptions peopleFace(FaceRegionRectF faceRegionRectF) {
        return (GlideOptions) GalleryGlideExtension.peopleFace(this, faceRegionRectF);
    }

    public GlideOptions peopleFace(FaceRegionRectF faceRegionRectF, long j) {
        return (GlideOptions) GalleryGlideExtension.peopleFace(this, faceRegionRectF, j);
    }

    public GlideOptions skipCache() {
        return (GlideOptions) GalleryGlideExtension.skipCache(this);
    }

    public GlideOptions markTemp() {
        return (GlideOptions) GalleryGlideExtension.markTemp(this);
    }

    public static GlideOptions secretKeyOf(byte[] bArr) {
        return new GlideOptions().secretKey(bArr);
    }

    public static GlideOptions thumbOf(long j) {
        return new GlideOptions().thumb(j);
    }

    public static GlideOptions tinyThumbOf() {
        return new GlideOptions().tinyThumb();
    }

    public static GlideOptions tinyThumbOf(long j) {
        return new GlideOptions().tinyThumb(j);
    }

    public static GlideOptions miniThumbOf() {
        return new GlideOptions().miniThumb();
    }

    public static GlideOptions microThumbOf() {
        return new GlideOptions().microThumb();
    }

    public static GlideOptions microThumbOf(long j) {
        return new GlideOptions().microThumb(j);
    }

    public static GlideOptions largeThumbOf() {
        return new GlideOptions().largeThumb();
    }

    public static GlideOptions bigPhotoOf() {
        return new GlideOptions().bigPhoto();
    }

    public static GlideOptions bigPhotoOf(long j) {
        return new GlideOptions().bigPhoto(j);
    }

    public static GlideOptions pixelsThumbOf() {
        return new GlideOptions().pixelsThumb();
    }

    public static GlideOptions pixelsThumbOf(long j) {
        return new GlideOptions().pixelsThumb(j);
    }

    public static GlideOptions peopleFaceOf() {
        return new GlideOptions().peopleFace();
    }

    public static GlideOptions peopleFaceOf(FaceRegionRectF faceRegionRectF) {
        return new GlideOptions().peopleFace(faceRegionRectF);
    }

    public static GlideOptions peopleFaceOf(FaceRegionRectF faceRegionRectF, long j) {
        return new GlideOptions().peopleFace(faceRegionRectF, j);
    }

    public static GlideOptions skipCacheOf() {
        return new GlideOptions().skipCache();
    }

    public static GlideOptions markTempOf() {
        return new GlideOptions().markTemp();
    }
}
