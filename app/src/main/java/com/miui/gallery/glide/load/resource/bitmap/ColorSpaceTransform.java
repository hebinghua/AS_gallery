package com.miui.gallery.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.milab.colorspace.Convert;
import java.security.MessageDigest;

/* loaded from: classes2.dex */
public class ColorSpaceTransform implements Transformation<Bitmap> {
    public static final byte[] ID_BYTES = "com.miui.gallery.glide.load.resource.bitmap.ColorSpaceTransform.1".getBytes(Key.CHARSET);

    @Override // com.bumptech.glide.load.Key
    public int hashCode() {
        return 950051990;
    }

    @Override // com.bumptech.glide.load.Transformation
    public Resource<Bitmap> transform(Context context, Resource<Bitmap> resource, int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis();
        BitmapPool bitmapPool = Glide.get(context).getBitmapPool();
        Bitmap mo237get = resource.mo237get();
        Bitmap convertColor = convertColor(mo237get);
        if (convertColor != mo237get) {
            DefaultLogger.d("ColorSpaceTransform", "convert cost %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            bitmapPool.put(mo237get);
            return BitmapResource.obtain(convertColor, bitmapPool);
        }
        return resource;
    }

    public static Bitmap convertColor(Bitmap bitmap) {
        try {
            return Convert.processLutByGPU(bitmap);
        } catch (Throwable th) {
            th.printStackTrace();
            return bitmap;
        }
    }

    @Override // com.bumptech.glide.load.Key
    public boolean equals(Object obj) {
        return obj instanceof ColorSpaceTransform;
    }

    @Override // com.bumptech.glide.load.Key
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
