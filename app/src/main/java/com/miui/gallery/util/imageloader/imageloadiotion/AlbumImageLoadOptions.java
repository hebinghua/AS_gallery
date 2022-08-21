package com.miui.gallery.util.imageloader.imageloadiotion;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.util.face.FaceRegionRectF;

/* loaded from: classes2.dex */
public class AlbumImageLoadOptions extends BaseImageLoadOption {
    public RequestOptions mDefaultAlbumImageOptions;
    public RequestOptions mFaceLoaderFailedOptions;
    public RequestOptions mLoadResourceImageOptions;
    public RequestOptions mMapAlbumNoCoverOptions;
    public RequestOptions mOtherAlbumNoCoverOptions;

    public static AlbumImageLoadOptions getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumImageLoadOptions INSTANCE = new AlbumImageLoadOptions();
    }

    public AlbumImageLoadOptions() {
    }

    @Override // com.miui.gallery.util.imageloader.imageloadiotion.BaseImageLoadOption
    public void initDefaultOption() {
        super.initDefaultOption();
        if (this.mDefaultAlbumImageOptions == null) {
            RequestOptions mo956fallback = cloneDefaultImageOptions().mo973placeholder((Drawable) null).mo972placeholder(0).mo954error(R.drawable.default_album_empty_cover).mo956fallback(R.drawable.default_album_empty_cover);
            this.mDefaultAlbumImageOptions = mo956fallback;
            mo956fallback.autoClone();
        }
    }

    public RequestOptions getDefaultAlbumImageOptions() {
        return this.mDefaultAlbumImageOptions;
    }

    public RequestOptions getDefaultAlbumImageOptions(long j) {
        if (j > 0) {
            return this.mDefaultAlbumImageOptions.clone().mo976signature(new ObjectKey(Long.valueOf(j)));
        }
        return getDefaultAlbumImageOptions();
    }

    public RequestOptions getOtherAlbumNoCoverOptions() {
        if (this.mOtherAlbumNoCoverOptions == null) {
            this.mOtherAlbumNoCoverOptions = getDefaultAlbumImageOptions().mo956fallback(R.drawable.default_album_empty_cover).mo954error(R.drawable.default_album_empty_cover);
        }
        return this.mOtherAlbumNoCoverOptions;
    }

    public RequestOptions getMapAlbumNoCoverOptions() {
        if (this.mMapAlbumNoCoverOptions == null) {
            this.mMapAlbumNoCoverOptions = getDefaultAlbumImageOptions().mo956fallback(R.drawable.map_album_load_fail).mo954error(R.drawable.map_album_load_fail);
        }
        return this.mMapAlbumNoCoverOptions;
    }

    public RequestOptions getFaceLoaderFailedRequestOptions() {
        if (this.mFaceLoaderFailedOptions == null) {
            this.mFaceLoaderFailedOptions = getDefaultAlbumImageOptions().mo956fallback(R.drawable.people_face_default);
        }
        return this.mFaceLoaderFailedOptions;
    }

    public RequestOptions buildFaceRequestOptions(FaceRegionRectF faceRegionRectF, long j) {
        return GlideOptions.peopleFaceOf(faceRegionRectF, j).mo956fallback(R.drawable.album_image_default_cover).mo954error(R.drawable.album_image_default_cover).mo973placeholder((Drawable) null);
    }

    public RequestOptions getDefaultNoCacheModeOption() {
        if (this.mLoadResourceImageOptions == null) {
            this.mLoadResourceImageOptions = new RequestOptions().mo973placeholder((Drawable) null).mo972placeholder(0).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo954error(R.drawable.default_album_cover);
        }
        return this.mLoadResourceImageOptions;
    }
}
