package com.miui.gallery.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.load.resource.bitmap.BitmapEncoder;
import com.bumptech.glide.module.AppGlideModule;
import com.miui.gallery.glide.load.data.BoundCover;
import com.miui.gallery.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.miui.gallery.glide.load.model.BoundCoverFactory;
import com.miui.gallery.glide.load.model.CameraPreviewFactory;
import com.miui.gallery.glide.load.model.EditExportedPreviewFactory;
import com.miui.gallery.glide.load.model.FileDescriptorFactory;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.load.model.PixelsBlobFactory;
import com.miui.gallery.glide.load.model.StreamFactory;
import com.miui.gallery.glide.load.resource.bitmap.BoundCoverDecoder;
import com.miui.gallery.glide.load.resource.bitmap.FFmpegVideoDecoder;
import com.miui.gallery.glide.load.resource.bitmap.GalleryBitmapEncoder;
import com.miui.gallery.glide.load.resource.bitmap.GalleryByteBufferBitmapDecoder;
import com.miui.gallery.glide.manager.GalleryConnectivityMonitorFactory;
import com.miui.gallery.util.BaseBuildUtil;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class GalleryGlideModule extends AppGlideModule {
    @Override // com.bumptech.glide.module.AppGlideModule
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override // com.bumptech.glide.module.AppGlideModule, com.bumptech.glide.module.AppliesOptions
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        GlideBuilder logLevel = glideBuilder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, "gallery_disk_cache", 268435456L, 2684354560L, 838860800L)).setDiskCacheExecutor(GlideExecutor.newDiskCacheBuilder().setThreadCount(GlideExecutor.calculateBestThreadCount()).build()).setLogLevel(4);
        float f = 1.0f;
        MemorySizeCalculator.Builder memoryCacheScreens = new MemorySizeCalculator.Builder(context).setMemoryCacheScreens(BaseBuildUtil.isLowRamDevice() ? 1.0f : 2.0f);
        if (BaseBuildUtil.isLowRamDevice()) {
            f = 0.0f;
        }
        logLevel.setMemorySizeCalculator(memoryCacheScreens.setBitmapPoolScreens(f).setMaxSizeMultiplier(BaseBuildUtil.isLowRamDevice() ? 0.2f : 0.3f).setLowMemoryMaxSizeMultiplier(0.2f)).setConnectivityMonitorFactory(new GalleryConnectivityMonitorFactory());
    }

    @Override // com.bumptech.glide.module.LibraryGlideModule, com.bumptech.glide.module.RegistersComponents
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.prepend(GalleryModel.class, InputStream.class, new StreamFactory(context));
        registry.prepend(GalleryModel.class, ParcelFileDescriptor.class, new FileDescriptorFactory(context));
        registry.prepend(GalleryModel.class, BoundCover.class, new BoundCoverFactory(context));
        registry.prepend(GalleryModel.class, Bitmap.class, new PixelsBlobFactory(context));
        registry.prepend(GalleryModel.class, Bitmap.class, new CameraPreviewFactory(context));
        registry.prepend(GalleryModel.class, Bitmap.class, new EditExportedPreviewFactory(context));
        ArrayPool arrayPool = glide.getArrayPool();
        registry.prepend(Bitmap.class, new GalleryBitmapEncoder(arrayPool, new BitmapEncoder(arrayPool)));
        registry.prepend(BoundCover.class, Bitmap.class, new BoundCoverDecoder(context, glide, registry));
        BitmapPool bitmapPool = glide.getBitmapPool();
        registry.prepend(ByteBuffer.class, Bitmap.class, new GalleryByteBufferBitmapDecoder(bitmapPool));
        registry.append(InputStream.class, Bitmap.class, new FFmpegVideoDecoder(bitmapPool));
        registry.append(ParcelFileDescriptor.class, Bitmap.class, new FFmpegVideoDecoder(bitmapPool));
    }
}
