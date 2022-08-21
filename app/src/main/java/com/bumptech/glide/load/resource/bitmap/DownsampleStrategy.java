package com.bumptech.glide.load.resource.bitmap;

import android.os.Build;
import com.bumptech.glide.load.Option;
import com.miui.gallery.glide.load.resource.bitmap.GalleryDownsampleStrategy;

/* loaded from: classes.dex */
public abstract class DownsampleStrategy {
    public static final DownsampleStrategy CENTER_OUTSIDE;
    public static final DownsampleStrategy DEFAULT;
    public static final boolean IS_BITMAP_FACTORY_SCALING_SUPPORTED;
    public static final DownsampleStrategy NONE;
    public static final Option<DownsampleStrategy> OPTION;
    public static final DownsampleStrategy AT_LEAST = GalleryDownsampleStrategy.AT_LEAST;
    public static final DownsampleStrategy AT_MOST = GalleryDownsampleStrategy.AT_MOST;
    public static final DownsampleStrategy FIT_CENTER = GalleryDownsampleStrategy.FIT_CENTER;
    public static final DownsampleStrategy CENTER_INSIDE = GalleryDownsampleStrategy.CENTER_INSIDE;

    /* loaded from: classes.dex */
    public enum SampleSizeRounding {
        MEMORY,
        QUALITY
    }

    public abstract SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4);

    public abstract float getScaleFactor(int i, int i2, int i3, int i4);

    static {
        DownsampleStrategy downsampleStrategy = GalleryDownsampleStrategy.CENTER_OUTSIDE;
        CENTER_OUTSIDE = downsampleStrategy;
        NONE = new None();
        DEFAULT = downsampleStrategy;
        OPTION = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", downsampleStrategy);
        IS_BITMAP_FACTORY_SCALING_SUPPORTED = Build.VERSION.SDK_INT >= 19;
    }

    /* loaded from: classes.dex */
    public static class None extends DownsampleStrategy {
        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return 1.0f;
        }

        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }
    }
}
