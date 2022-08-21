package com.bumptech.glide.load.resource;

import android.annotation.SuppressLint;
import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import java.io.IOException;

/* loaded from: classes.dex */
public abstract class ImageDecoderResourceDecoder<T> implements ResourceDecoder<ImageDecoder.Source, T> {
    public final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();

    public abstract Resource<T> decode(ImageDecoder.Source source, int i, int i2, ImageDecoder.OnHeaderDecodedListener onHeaderDecodedListener) throws IOException;

    @Override // com.bumptech.glide.load.ResourceDecoder
    public final boolean handles(ImageDecoder.Source source, Options options) {
        return true;
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public final Resource<T> decode(ImageDecoder.Source source, final int i, final int i2, Options options) throws IOException {
        final DecodeFormat decodeFormat = (DecodeFormat) options.get(Downsampler.DECODE_FORMAT);
        final DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
        Option<Boolean> option = Downsampler.ALLOW_HARDWARE_CONFIG;
        final boolean z = options.get(option) != null && ((Boolean) options.get(option)).booleanValue();
        final PreferredColorSpace preferredColorSpace = (PreferredColorSpace) options.get(Downsampler.PREFERRED_COLOR_SPACE);
        return decode(source, i, i2, new ImageDecoder.OnHeaderDecodedListener() { // from class: com.bumptech.glide.load.resource.ImageDecoderResourceDecoder.1
            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
            @SuppressLint({"Override"})
            public void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source2) {
                boolean z2 = false;
                if (ImageDecoderResourceDecoder.this.hardwareConfigState.isHardwareConfigAllowed(i, i2, z, false)) {
                    imageDecoder.setAllocator(3);
                } else {
                    imageDecoder.setAllocator(1);
                }
                if (decodeFormat == DecodeFormat.PREFER_RGB_565) {
                    imageDecoder.setMemorySizePolicy(0);
                }
                imageDecoder.setOnPartialImageListener(new ImageDecoder.OnPartialImageListener() { // from class: com.bumptech.glide.load.resource.ImageDecoderResourceDecoder.1.1
                    @Override // android.graphics.ImageDecoder.OnPartialImageListener
                    public boolean onPartialImage(ImageDecoder.DecodeException decodeException) {
                        return false;
                    }
                });
                Size size = imageInfo.getSize();
                int i3 = i;
                if (i3 == Integer.MIN_VALUE) {
                    i3 = size.getWidth();
                }
                int i4 = i2;
                if (i4 == Integer.MIN_VALUE) {
                    i4 = size.getHeight();
                }
                float scaleFactor = downsampleStrategy.getScaleFactor(size.getWidth(), size.getHeight(), i3, i4);
                int round = Math.round(size.getWidth() * scaleFactor);
                int round2 = Math.round(size.getHeight() * scaleFactor);
                if (Log.isLoggable("ImageDecoder", 2)) {
                    Log.v("ImageDecoder", "Resizing from [" + size.getWidth() + "x" + size.getHeight() + "] to [" + round + "x" + round2 + "] scaleFactor: " + scaleFactor);
                }
                imageDecoder.setTargetSize(round, round2);
                int i5 = Build.VERSION.SDK_INT;
                if (i5 < 28) {
                    if (i5 < 26) {
                        return;
                    }
                    imageDecoder.setTargetColorSpace(ColorSpace.get(ColorSpace.Named.SRGB));
                    return;
                }
                if (preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && imageInfo.getColorSpace() != null && imageInfo.getColorSpace().isWideGamut()) {
                    z2 = true;
                }
                imageDecoder.setTargetColorSpace(ColorSpace.get(z2 ? ColorSpace.Named.DISPLAY_P3 : ColorSpace.Named.SRGB));
            }
        });
    }
}
