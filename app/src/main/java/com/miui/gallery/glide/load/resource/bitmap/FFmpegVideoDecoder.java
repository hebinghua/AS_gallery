package com.miui.gallery.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryConstantsHelper;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.glide.load.ExtraInfoManager;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;

/* loaded from: classes2.dex */
public class FFmpegVideoDecoder<T> implements ResourceDecoder<T, Bitmap> {
    public static LazyValue<Void, Boolean> sFFmpegLoaded;
    public static boolean sFFmpegSupport;
    public final BitmapPool bitmapPool;
    public long lastRealCheckTime = -1;

    static {
        sFFmpegSupport = !"x86_64".equalsIgnoreCase(LibraryUtils.getCurrentAbi()) && !"x86".equalsIgnoreCase(LibraryUtils.getCurrentAbi());
        sFFmpegLoaded = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.glide.load.resource.bitmap.FFmpegVideoDecoder.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public Boolean mo1272onInit(Void r4) {
                LibraryManager libraryManager = LibraryManager.getInstance();
                Library librarySync = libraryManager.getLibrarySync(1038L);
                if (librarySync == null) {
                    return Boolean.FALSE;
                }
                if (Library.LibraryStatus.STATE_AVAILABLE == librarySync.getLibraryStatus()) {
                    return Boolean.valueOf(libraryManager.loadLibrary(LibraryConstantsHelper.sFFmpegLibraries));
                }
                if (Library.LibraryStatus.STATE_LOADED == librarySync.getLibraryStatus()) {
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
        };
    }

    public FFmpegVideoDecoder(BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(T t, Options options) {
        String str;
        if (!sFFmpegSupport) {
            return false;
        }
        if (!sFFmpegLoaded.get(null).booleanValue()) {
            long currentTimeMillis = System.currentTimeMillis();
            long j = this.lastRealCheckTime;
            if (j == -1) {
                this.lastRealCheckTime = currentTimeMillis;
            } else if (currentTimeMillis > j + 3000) {
                sFFmpegLoaded.reset();
                this.lastRealCheckTime = currentTimeMillis;
            }
            return false;
        }
        if (t instanceof InputStreamWrapper) {
            str = (String) ExtraInfoManager.getInstance().get(((InputStreamWrapper) t).getWrapped(), GalleryOptions.EXTRA_MIME_TYPE);
        } else {
            str = (String) ExtraInfoManager.getInstance().get(t, GalleryOptions.EXTRA_MIME_TYPE);
        }
        return TextUtils.isEmpty(str) || !BaseFileMimeUtil.isImageFromMimeType(str);
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(T t, int i, int i2, Options options) throws IOException {
        String str;
        if (t instanceof InputStreamWrapper) {
            str = (String) ExtraInfoManager.getInstance().get(((InputStreamWrapper) t).getWrapped(), GalleryOptions.EXTRA_PATH);
        } else {
            str = (String) ExtraInfoManager.getInstance().get(t, GalleryOptions.EXTRA_PATH);
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
        if (downsampleStrategy == null) {
            downsampleStrategy = DownsampleStrategy.DEFAULT;
        }
        try {
            return BitmapResource.obtain(decode(str, i, i2, downsampleStrategy), this.bitmapPool);
        } catch (Exception e) {
            throw new IOException(String.format("decode [%s] error : %s.", str, e.toString()));
        }
    }

    public final Bitmap decode(String str, int i, int i2, DownsampleStrategy downsampleStrategy) throws Exception {
        DefaultLogger.v("FFmpegDecoder", "try decode with GalleryFrameRetriever, path:[%s], size:[%dx%d] ", str, Integer.valueOf(i), Integer.valueOf(i2));
        GalleryFrameRetriever galleryFrameRetriever = new GalleryFrameRetriever();
        try {
            galleryFrameRetriever.setSource(str);
            Bitmap frameAtIndex = galleryFrameRetriever.getFrameAtIndex(0, Bitmap.Config.ARGB_8888);
            galleryFrameRetriever.close();
            return BitmapUtils.downsample(frameAtIndex, i, i2, downsampleStrategy, this.bitmapPool);
        } catch (Throwable th) {
            try {
                galleryFrameRetriever.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }
}
