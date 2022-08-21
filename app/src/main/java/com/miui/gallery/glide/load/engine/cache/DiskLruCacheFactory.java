package com.miui.gallery.glide.load.engine.cache;

import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;

/* loaded from: classes2.dex */
public class DiskLruCacheFactory implements DiskCache.Factory {
    public final CacheDirectoryGetter cacheDirectoryGetter;
    public final long fullSizeThumbCacheSize;
    public final long remoteDataCacheSize;
    public final long smallSizeThumbCacheSize;

    /* loaded from: classes2.dex */
    public interface CacheDirectoryGetter {
        File getCacheDirectory();
    }

    public DiskLruCacheFactory(CacheDirectoryGetter cacheDirectoryGetter, long j, long j2, long j3) {
        this.remoteDataCacheSize = j;
        this.smallSizeThumbCacheSize = j2;
        this.fullSizeThumbCacheSize = j3;
        this.cacheDirectoryGetter = cacheDirectoryGetter;
    }

    @Override // com.bumptech.glide.load.engine.cache.DiskCache.Factory
    public DiskCache build() {
        File cacheDirectory = this.cacheDirectoryGetter.getCacheDirectory();
        if (cacheDirectory == null) {
            return null;
        }
        if (!cacheDirectory.mkdirs() && (!cacheDirectory.exists() || !cacheDirectory.isDirectory())) {
            return null;
        }
        File file = new File(cacheDirectory, "remote_data");
        if (!file.mkdirs() && (!file.exists() || !file.isDirectory())) {
            return null;
        }
        File file2 = new File(cacheDirectory, "small_size");
        if (!file2.mkdirs() && (!file2.exists() || !file2.isDirectory())) {
            return null;
        }
        File file3 = new File(cacheDirectory, "full_size");
        if (!file3.mkdirs() && (!file3.exists() || !file3.isDirectory())) {
            return null;
        }
        return DiskLruCacheProxy.create(file, file2, file3, this.remoteDataCacheSize, this.smallSizeThumbCacheSize, this.fullSizeThumbCacheSize);
    }
}
