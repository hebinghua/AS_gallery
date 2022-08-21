package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.Util;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class GalleryByteBufferBitmapDecoder implements ResourceDecoder<ByteBuffer, Bitmap> {
    public final BitmapPool mBitmapPool;

    public GalleryByteBufferBitmapDecoder(BitmapPool bitmapPool) {
        this.mBitmapPool = bitmapPool;
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(ByteBuffer byteBuffer, Options options) throws IOException {
        Boolean bool = (Boolean) options.get(GalleryOptions.CACHE_AS_PIXELS);
        return bool != null && bool.booleanValue();
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(ByteBuffer byteBuffer, int i, int i2, Options options) throws IOException {
        DecodeFormat decodeFormat = (DecodeFormat) options.get(Downsampler.DECODE_FORMAT);
        Bitmap.Config config = Bitmap.Config.RGB_565;
        if (decodeFormat == DecodeFormat.PREFER_ARGB_8888) {
            config = Bitmap.Config.ARGB_8888;
        }
        if (byteBuffer.capacity() != Util.getBitmapByteSize(i, i2, config)) {
            Bitmap.Config matchConfig = BaseBitmapUtils.matchConfig(i, i2, byteBuffer.capacity());
            DefaultLogger.i("GalleryByteBufferBitmapDecoder", "the requested config[%s] doesn't match the source, try to find a matched config[%s]", config, matchConfig);
            if (matchConfig == null) {
                return null;
            }
            config = matchConfig;
        }
        Bitmap bitmap = this.mBitmapPool.get(i, i2, config);
        bitmap.copyPixelsFromBuffer(byteBuffer);
        return BitmapResource.obtain(bitmap, this.mBitmapPool);
    }
}
