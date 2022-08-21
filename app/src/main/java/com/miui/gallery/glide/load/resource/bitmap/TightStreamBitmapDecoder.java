package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder;
import com.miui.gallery.glide.load.ExtraInfoManager;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ImageSizeUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class TightStreamBitmapDecoder extends StreamBitmapDecoder {
    public TightStreamBitmapDecoder(Downsampler downsampler, ArrayPool arrayPool) {
        super(downsampler, arrayPool);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder, com.bumptech.glide.load.ResourceDecoder
    public boolean handles(InputStream inputStream, Options options) {
        String str;
        if (!super.handles(inputStream, options)) {
            return false;
        }
        if (inputStream instanceof InputStreamWrapper) {
            str = (String) ExtraInfoManager.getInstance().get(((InputStreamWrapper) inputStream).getWrapped(), GalleryOptions.EXTRA_MIME_TYPE);
        } else {
            str = (String) ExtraInfoManager.getInstance().get(inputStream, GalleryOptions.EXTRA_MIME_TYPE);
        }
        return TextUtils.isEmpty(str) || !BaseFileMimeUtil.isVideoFromMimeType(str);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder, com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(InputStream inputStream, int i, int i2, Options options) throws IOException {
        Resource<Bitmap> decode = super.decode(inputStream, i, i2, options);
        if (decode != null) {
            Bitmap mo237get = decode.mo237get();
            int maxTextureSize = ImageSizeUtils.getMaxTextureSize();
            if (mo237get.getWidth() <= maxTextureSize && mo237get.getHeight() <= maxTextureSize) {
                return decode;
            }
            DefaultLogger.w("TightStreamBitmapDecoder", "Got too large bitmap: %dx%d with excepted size: %dx%d", Integer.valueOf(mo237get.getWidth()), Integer.valueOf(mo237get.getHeight()), Integer.valueOf(i), Integer.valueOf(i2));
            return null;
        }
        return decode;
    }
}
