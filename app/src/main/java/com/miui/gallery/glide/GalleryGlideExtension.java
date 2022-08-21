package com.miui.gallery.glide;

import android.graphics.Bitmap;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.miui.gallery.Config$BigPhotoConfig;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.resource.bitmap.CenterCrop;
import com.miui.gallery.glide.load.resource.bitmap.GalleryDownsampleStrategy;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.face.FaceRegionRectF;

/* loaded from: classes2.dex */
public class GalleryGlideExtension {
    /* JADX WARN: Type inference failed for: r2v1, types: [com.bumptech.glide.request.BaseRequestOptions] */
    /* JADX WARN: Type inference failed for: r2v6, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> thumb(BaseRequestOptions<?> baseRequestOptions, long j) {
        BaseRequestOptions<?> mo975set = baseRequestOptions.mo951dontTransform().mo958format(Config$ThumbConfig.getThumbnailConfig() == Bitmap.Config.RGB_565 ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888).mo953encodeQuality(90).mo952downsample(GalleryDownsampleStrategy.CENTER_OUTSIDE).mo975set(GalleryOptions.SMALL_SIZE, Boolean.TRUE);
        return j > 0 ? mo975set.mo976signature(new ObjectKey(Long.valueOf(j))) : mo975set;
    }

    public static BaseRequestOptions<?> tinyThumb(BaseRequestOptions<?> baseRequestOptions) {
        return tinyThumb(baseRequestOptions, 0L);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> tinyThumb(BaseRequestOptions<?> baseRequestOptions, long j) {
        return thumb(baseRequestOptions, j).mo969optionalTransform(new CenterCrop()).mo975set(GalleryOptions.CACHE_AS_PIXELS, Boolean.TRUE).mo973placeholder(Config$ThumbConfig.getTinyThumbPlaceholder()).mo971override(Config$ThumbConfig.get().sTinyTargetSize.getWidth(), Config$ThumbConfig.get().sTinyTargetSize.getHeight()).mo975set(GalleryOptions.VERSION, 1);
    }

    public static BaseRequestOptions<?> miniThumb(BaseRequestOptions<?> baseRequestOptions) {
        return miniThumb(baseRequestOptions, 0L);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> miniThumb(BaseRequestOptions<?> baseRequestOptions, long j) {
        return thumb(baseRequestOptions, j).mo969optionalTransform(new CenterCrop()).mo973placeholder(Config$ThumbConfig.getMiniThumbPlaceholder()).mo971override(Config$ThumbConfig.get().sMiniTargetSize.getWidth(), Config$ThumbConfig.get().sMiniTargetSize.getHeight()).mo975set(GalleryOptions.VERSION, 1);
    }

    public static BaseRequestOptions<?> microThumb(BaseRequestOptions<?> baseRequestOptions) {
        return microThumb(baseRequestOptions, 0L);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> microThumb(BaseRequestOptions<?> baseRequestOptions, long j) {
        return thumb(baseRequestOptions, j).mo973placeholder(Config$ThumbConfig.getMicroThumbPlaceholder()).mo971override(Config$ThumbConfig.get().sMicroTargetSize.getWidth(), Config$ThumbConfig.get().sMicroTargetSize.getHeight()).mo958format(Config$ThumbConfig.getMicroThumbConfig() == Bitmap.Config.RGB_565 ? DecodeFormat.PREFER_RGB_565 : DecodeFormat.PREFER_ARGB_8888).mo975set(GalleryOptions.VERSION, 1);
    }

    public static BaseRequestOptions<?> largeThumb(BaseRequestOptions<?> baseRequestOptions) {
        return largeThumb(baseRequestOptions, 0L);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> largeThumb(BaseRequestOptions<?> baseRequestOptions, long j) {
        return thumb(baseRequestOptions, j).mo969optionalTransform(new CenterCrop()).mo973placeholder(Config$ThumbConfig.getLargeThumbPlaceholder()).mo971override(Config$ThumbConfig.get().sLargeTargetSize.getWidth(), Config$ThumbConfig.get().sLargeTargetSize.getHeight()).mo975set(GalleryOptions.VERSION, 1);
    }

    public static BaseRequestOptions<?> bigPhoto(BaseRequestOptions<?> baseRequestOptions) {
        return bigPhoto(baseRequestOptions, 0L);
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.bumptech.glide.request.BaseRequestOptions] */
    /* JADX WARN: Type inference failed for: r3v8, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> bigPhoto(BaseRequestOptions<?> baseRequestOptions, long j) {
        BaseRequestOptions<?> mo975set = baseRequestOptions.mo978skipMemoryCache(true).mo958format(Config$BigPhotoConfig.getDecodeFormat()).mo953encodeQuality(97).mo952downsample(GalleryDownsampleStrategy.CENTER_INSIDE).mo969optionalTransform(new CenterInside()).mo975set(GalleryOptions.FULL_SIZE, Boolean.TRUE).mo975set(GalleryOptions.VERSION, 1);
        return j >= 0 ? mo975set.mo976signature(new ObjectKey(Long.valueOf(j))) : mo975set;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> pixelsThumb(BaseRequestOptions<?> baseRequestOptions) {
        if (BaseBuildUtil.isLowRamDevice()) {
            return microThumb(baseRequestOptions).mo969optionalTransform(new CenterCrop());
        }
        return tinyThumb(baseRequestOptions);
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> pixelsThumb(BaseRequestOptions<?> baseRequestOptions, long j) {
        if (BaseBuildUtil.isLowRamDevice()) {
            return microThumb(baseRequestOptions, j).mo969optionalTransform(new CenterCrop());
        }
        return tinyThumb(baseRequestOptions, j);
    }

    public static BaseRequestOptions<?> peopleFace(BaseRequestOptions<?> baseRequestOptions) {
        return peopleFace(baseRequestOptions, null, 0L);
    }

    public static BaseRequestOptions<?> peopleFace(BaseRequestOptions<?> baseRequestOptions, FaceRegionRectF faceRegionRectF) {
        return peopleFace(baseRequestOptions, faceRegionRectF, 0L);
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> peopleFace(BaseRequestOptions<?> baseRequestOptions, FaceRegionRectF faceRegionRectF, long j) {
        BaseRequestOptions<?> mo956fallback = microThumb(baseRequestOptions, j).mo969optionalTransform(new CenterCrop()).mo954error(R.drawable.people_face_default).mo956fallback(R.drawable.people_face_default);
        BaseRequestOptions<?> baseRequestOptions2 = mo956fallback;
        if (j > 0) {
            baseRequestOptions2 = mo956fallback.mo976signature(new ObjectKey(Long.valueOf(j)));
        }
        return faceRegionRectF != null ? baseRequestOptions2.mo975set(GalleryOptions.DECODE_REGION, RegionConfig.ofFace(faceRegionRectF, 2.0f)) : baseRequestOptions2;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> skipCache(BaseRequestOptions<?> baseRequestOptions) {
        return baseRequestOptions.mo978skipMemoryCache(true).mo950diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.bumptech.glide.request.BaseRequestOptions<?>, com.bumptech.glide.request.BaseRequestOptions] */
    public static BaseRequestOptions<?> markTemp(BaseRequestOptions<?> baseRequestOptions) {
        return baseRequestOptions.mo975set(GalleryOptions.MARK_TEMP, Boolean.TRUE);
    }
}
