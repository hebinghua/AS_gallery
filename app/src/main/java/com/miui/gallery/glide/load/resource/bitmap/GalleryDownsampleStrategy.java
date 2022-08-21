package com.miui.gallery.glide.load.resource.bitmap;

import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.miui.gallery.util.ImageSizeUtils;

/* loaded from: classes2.dex */
public class GalleryDownsampleStrategy {
    public static final DownsampleStrategy CENTER_OUTSIDE = new CenterOutside();
    public static final DownsampleStrategy FIT_CENTER = new FitCenter();
    public static final DownsampleStrategy CENTER_INSIDE = new CenterInside();
    public static final DownsampleStrategy AT_LEAST = new AtLeast();
    public static final DownsampleStrategy AT_MOST = new AtMost();

    /* loaded from: classes2.dex */
    public static class AtLeast extends DownsampleStrategy {
        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int min = Math.min(i2 / i4, i / i3);
            if (min == 0) {
                return 1.0f;
            }
            return 1.0f / GalleryDownsampleStrategy.adjustSampleSize(i, i2, Math.max(1, Integer.highestOneBit(min)));
        }

        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return DownsampleStrategy.SampleSizeRounding.QUALITY;
        }
    }

    /* loaded from: classes2.dex */
    public static class AtMost extends DownsampleStrategy {
        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int ceil = (int) Math.ceil(Math.max(i2 / i4, i / i3));
            int i5 = 1;
            int max = Math.max(1, Integer.highestOneBit(ceil));
            if (max >= ceil) {
                i5 = 0;
            }
            return 1.0f / GalleryDownsampleStrategy.adjustSampleSize(i, i2, max << i5);
        }

        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return DownsampleStrategy.SampleSizeRounding.MEMORY;
        }
    }

    /* loaded from: classes2.dex */
    public static class CenterOutside extends DownsampleStrategy {
        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int adjustSampleSize = GalleryDownsampleStrategy.adjustSampleSize(i, i2, Math.max(1, Integer.highestOneBit(Math.min(i / i3, i2 / i4))));
            return adjustSampleSize > 1 ? 1.0f / adjustSampleSize : GalleryDownsampleStrategy.adjustUpscale(i, i2, Math.max(i3 / i, i4 / i2));
        }

        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return DownsampleStrategy.SampleSizeRounding.QUALITY;
        }
    }

    /* loaded from: classes2.dex */
    public static class FitCenter extends DownsampleStrategy {
        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int max = Math.max(i / i3, i2 / i4);
            int max2 = Math.max(1, Integer.highestOneBit(max));
            int adjustSampleSize = GalleryDownsampleStrategy.adjustSampleSize(i, i2, max2 << (max2 < max ? 1 : 0));
            return adjustSampleSize > 1 ? 1.0f / adjustSampleSize : GalleryDownsampleStrategy.adjustUpscale(i, i2, Math.min(i3 / i, i4 / i2));
        }

        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return DownsampleStrategy.SampleSizeRounding.QUALITY;
        }
    }

    /* loaded from: classes2.dex */
    public static class CenterInside extends DownsampleStrategy {
        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return Math.min(1.0f, GalleryDownsampleStrategy.FIT_CENTER.getScaleFactor(i, i2, i3, i4));
        }

        @Override // com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
        public DownsampleStrategy.SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return DownsampleStrategy.SampleSizeRounding.QUALITY;
        }
    }

    public static int adjustSampleSize(int i, int i2, int i3) {
        int maxTextureSize = ImageSizeUtils.getMaxTextureSize();
        while (true) {
            if (i / i3 > maxTextureSize || i2 / i3 > maxTextureSize) {
                i3 *= 2;
            } else {
                return i3;
            }
        }
    }

    public static float adjustUpscale(int i, int i2, float f) {
        if (f <= 1.0f) {
            return f;
        }
        float f2 = i;
        float maxTextureSize = ImageSizeUtils.getMaxTextureSize();
        float f3 = maxTextureSize / f;
        if (f2 > f3 || i2 > f3) {
            f = Math.min(maxTextureSize / f2, maxTextureSize / i2);
        }
        return ((float) (i * i2)) > (2.62144E7f / f) / f ? (float) Math.sqrt((2.62144E7f / f2) / i2) : f;
    }
}
