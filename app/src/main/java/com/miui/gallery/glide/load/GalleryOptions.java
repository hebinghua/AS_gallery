package com.miui.gallery.glide.load;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

/* loaded from: classes2.dex */
public class GalleryOptions {
    public static final Option<Boolean> CACHE_AS_PIXELS;
    public static final Option<RegionConfig> DECODE_REGION;
    public static final Option<Boolean> EXTRA_HDR10;
    public static final Option<Boolean> EXTRA_HDR10_NEED_CONVERT_COLOR;
    public static final Option<String> EXTRA_MIME_TYPE;
    public static final Option<String> EXTRA_PATH;
    public static final Option<Boolean> FULL_SIZE;
    public static final Option<Boolean> MARK_TEMP;
    public static final Option<Boolean> SKIP_ACCOMPANY_FILE;
    public static final Option<Boolean> SMALL_SIZE;
    public static final Option<Integer> VERSION;
    public static final Option<byte[]> SECRET_KEY = Option.memory("com.miui.gallery.load.GalleryOptions.SecretKey");
    public static final Option<String> INFERRED_MIME_TYPE = Option.memory("com.miui.gallery.load.GalleryOptions.InferredMimeType");

    static {
        Boolean bool = Boolean.FALSE;
        SMALL_SIZE = Option.memory("com.miui.gallery.load.GalleryOptions.SmallSize", bool);
        FULL_SIZE = Option.memory("com.miui.gallery.load.GalleryOptions.FullSize", bool);
        SKIP_ACCOMPANY_FILE = Option.memory("com.miui.gallery.load.GalleryOptions.SkipAccompanyFile", bool);
        DECODE_REGION = Option.disk("com.miui.gallery.load.GalleryOptions.DecodeRegion", new Option.CacheKeyUpdater<RegionConfig>() { // from class: com.miui.gallery.glide.load.GalleryOptions.1
            public final ByteBuffer mShareBuffer = ByteBuffer.allocate(4);

            @Override // com.bumptech.glide.load.Option.CacheKeyUpdater
            public void update(byte[] bArr, RegionConfig regionConfig, MessageDigest messageDigest) {
                messageDigest.update(bArr);
                messageDigest.update(regionConfig.getRegion().toShortString().getBytes(Key.CHARSET));
                synchronized (this.mShareBuffer) {
                    this.mShareBuffer.position(0);
                    messageDigest.update(this.mShareBuffer.putFloat(regionConfig.getEnlargeFactor()).array());
                }
            }
        });
        CACHE_AS_PIXELS = Option.disk("com.miui.gallery.load.GalleryOptions.CacheAsPixels", GalleryOptions$$ExternalSyntheticLambda0.INSTANCE);
        VERSION = Option.disk("com.miui.gallery.load.GalleryOptions.Version", 0, new Option.CacheKeyUpdater<Integer>() { // from class: com.miui.gallery.glide.load.GalleryOptions.2
            public final ByteBuffer mShareBuffer = ByteBuffer.allocate(4);

            @Override // com.bumptech.glide.load.Option.CacheKeyUpdater
            public void update(byte[] bArr, Integer num, MessageDigest messageDigest) {
                messageDigest.update(bArr);
                synchronized (this.mShareBuffer) {
                    this.mShareBuffer.position(0);
                    messageDigest.update(this.mShareBuffer.putInt(num.intValue()).array());
                }
            }
        });
        MARK_TEMP = Option.disk("com.miui.gallery.load.GalleryOptions.MarkTemp", bool, new Option.CacheKeyUpdater<Boolean>() { // from class: com.miui.gallery.glide.load.GalleryOptions.3
            @Override // com.bumptech.glide.load.Option.CacheKeyUpdater
            public void update(byte[] bArr, Boolean bool2, MessageDigest messageDigest) {
                if (bool2.booleanValue()) {
                    messageDigest.update(bArr);
                }
            }
        });
        EXTRA_MIME_TYPE = Option.memory("com.miui.gallery.load.ExtraInfo.GalleryOptions.MimeType");
        EXTRA_HDR10 = Option.memory("com.miui.gallery.load.ExtraInfo.GalleryOptions.HDR10", bool);
        EXTRA_HDR10_NEED_CONVERT_COLOR = Option.memory("com.miui.gallery.load.ExtraInfo.GalleryOptions.HDR10NeedConvertColor", Boolean.TRUE);
        EXTRA_PATH = Option.memory("com.miui.gallery.load.ExtraInfo.GalleryOptions.Path");
    }

    public static /* synthetic */ void lambda$static$0(byte[] bArr, Boolean bool, MessageDigest messageDigest) {
        if (bool.booleanValue()) {
            messageDigest.update(bArr);
        }
    }
}
