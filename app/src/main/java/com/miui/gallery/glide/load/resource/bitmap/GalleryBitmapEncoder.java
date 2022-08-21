package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.ByteBufferUtil;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.GlideTrace;
import com.miui.gallery.glide.load.GalleryOptions;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class GalleryBitmapEncoder implements ResourceEncoder<Bitmap> {
    public final ArrayPool mArrayPool;
    public final ResourceEncoder<Bitmap> mUnderlyingEncoder;

    public GalleryBitmapEncoder(ArrayPool arrayPool, ResourceEncoder<Bitmap> resourceEncoder) {
        this.mArrayPool = arrayPool;
        this.mUnderlyingEncoder = resourceEncoder;
    }

    @Override // com.bumptech.glide.load.ResourceEncoder
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }

    @Override // com.bumptech.glide.load.Encoder
    public boolean encode(Resource<Bitmap> resource, File file, Options options) {
        Boolean bool = (Boolean) options.get(GalleryOptions.CACHE_AS_PIXELS);
        if (bool == null || !bool.booleanValue()) {
            return this.mUnderlyingEncoder.encode(resource, file, options);
        }
        Bitmap mo237get = resource.mo237get();
        boolean z = true;
        if (!isPixelsCacheable(mo237get)) {
            if (Log.isLoggable("GalleryBitmapEncoder", 5)) {
                Log.w("GalleryBitmapEncoder", String.format("Can't encode Bitmap as pixels for config: %s", mo237get.getConfig()));
            }
            return false;
        }
        GlideTrace.beginSectionFormat("encode: [%dx%d] as pixels", Integer.valueOf(mo237get.getWidth()), Integer.valueOf(mo237get.getHeight()));
        try {
            long logTime = LogTime.getLogTime();
            int byteCount = mo237get.getByteCount();
            ByteBuffer wrap = ByteBuffer.wrap((byte[]) this.mArrayPool.get(byteCount, byte[].class), 0, byteCount);
            mo237get.copyPixelsToBuffer(wrap);
            try {
                ByteBufferUtil.toFile(wrap, file);
            } catch (IOException e) {
                if (Log.isLoggable("GalleryBitmapEncoder", 3)) {
                    Log.d("GalleryBitmapEncoder", "Failed to encode Bitmap", e);
                }
                z = false;
            }
            if (Log.isLoggable("GalleryBitmapEncoder", 2)) {
                Log.v("GalleryBitmapEncoder", "Cache as pixels of size " + Util.getBitmapByteSize(mo237get) + " in " + LogTime.getElapsedMillis(logTime));
            }
            return z;
        } finally {
            GlideTrace.endSection();
        }
    }

    public final boolean isPixelsCacheable(Bitmap bitmap) {
        return Build.VERSION.SDK_INT < 26 || bitmap.getConfig() != Bitmap.Config.HARDWARE;
    }
}
