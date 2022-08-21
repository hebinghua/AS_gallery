package com.miui.gallery.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.miui.gallery.glide.load.ExtraInfoManager;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.glide.load.data.BoundCover;
import com.miui.gallery.util.StaticContext;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class BoundCoverDecoder implements ResourceDecoder<BoundCover, Bitmap> {
    public final BitmapPool mPool;
    public final StreamBitmapDecoder mWrapped;

    public BoundCoverDecoder(Context context, Glide glide, Registry registry) {
        Downsampler downsampler = new Downsampler(registry.getImageHeaderParsers(), context.getResources().getDisplayMetrics(), glide.getBitmapPool(), glide.getArrayPool());
        this.mPool = glide.getBitmapPool();
        this.mWrapped = new StreamBitmapDecoder(downsampler, glide.getArrayPool());
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(BoundCover boundCover, Options options) throws IOException {
        return boundCover.getData() != null && boundCover.getData().length > 0;
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(BoundCover boundCover, int i, int i2, Options options) throws IOException {
        Resource<Bitmap> decode = this.mWrapped.decode((InputStream) new ByteArrayInputStream(boundCover.getData()), i, i2, options);
        Bitmap mo237get = decode != null ? decode.mo237get() : null;
        return (mo237get == null || !isNeedConvertColor(boundCover)) ? decode : BitmapResource.obtain(new ColorSpaceTransform().transform(StaticContext.sGetAndroidContext(), decode, mo237get.getWidth(), mo237get.getHeight()).mo237get(), this.mPool);
    }

    public static <T> boolean isNeedConvertColor(T t) {
        return ((Boolean) ExtraInfoManager.getInstance().get(t, GalleryOptions.EXTRA_HDR10)).booleanValue() && ((Boolean) ExtraInfoManager.getInstance().get(t, GalleryOptions.EXTRA_HDR10_NEED_CONVERT_COLOR)).booleanValue();
    }
}
