package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.miui.gallery.glide.load.ExtraInfoManager;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ImageSizeUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;

/* loaded from: classes2.dex */
public final class TightParcelFileDescriptorBitmapDecoder implements ResourceDecoder<ParcelFileDescriptor, Bitmap> {
    public final Downsampler downsampler;

    public TightParcelFileDescriptorBitmapDecoder(Downsampler downsampler) {
        this.downsampler = downsampler;
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(ParcelFileDescriptor parcelFileDescriptor, Options options) {
        if (!this.downsampler.handles(parcelFileDescriptor)) {
            return false;
        }
        String str = (String) ExtraInfoManager.getInstance().get(parcelFileDescriptor, GalleryOptions.EXTRA_MIME_TYPE);
        return TextUtils.isEmpty(str) || !BaseFileMimeUtil.isVideoFromMimeType(str);
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(ParcelFileDescriptor parcelFileDescriptor, int i, int i2, Options options) throws IOException {
        Resource<Bitmap> decode = this.downsampler.decode(parcelFileDescriptor, i, i2, options);
        if (decode != null) {
            Bitmap mo237get = decode.mo237get();
            int maxTextureSize = ImageSizeUtils.getMaxTextureSize();
            if (mo237get.getWidth() <= maxTextureSize && mo237get.getHeight() <= maxTextureSize) {
                return decode;
            }
            DefaultLogger.w("TightParcelFileDescriptorBitmapDecoder", "Got too large bitmap: %dx%d with excepted size: %dx%d", Integer.valueOf(mo237get.getWidth()), Integer.valueOf(mo237get.getHeight()), Integer.valueOf(i), Integer.valueOf(i2));
            return null;
        }
        return decode;
    }
}
