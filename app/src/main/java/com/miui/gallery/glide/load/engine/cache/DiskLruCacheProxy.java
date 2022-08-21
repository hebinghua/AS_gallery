package com.miui.gallery.glide.load.engine.cache;

import android.util.Log;
import androidx.tracing.Trace;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.CacheKeyUtils;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;

/* loaded from: classes2.dex */
public class DiskLruCacheProxy implements DiskCache {
    public DiskLruCache dataDiskCache;
    public DiskLruCache fullSizeCache;
    public final File fullSizeDirectory;
    public final long fullSizeThumbCacheSize;
    public final long remoteDataCacheSize;
    public final File remoteDataDirectory;
    public DiskLruCache smallSizeCache;
    public final File smallSizeDirectory;
    public final long smallSizeThumbCacheSize;
    public final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();
    public final SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();

    public static DiskCache create(File file, File file2, File file3, long j, long j2, long j3) {
        return new DiskLruCacheProxy(file, file2, file3, j, j2, j3);
    }

    public DiskLruCacheProxy(File file, File file2, File file3, long j, long j2, long j3) {
        this.remoteDataDirectory = file;
        this.smallSizeDirectory = file2;
        this.fullSizeDirectory = file3;
        this.remoteDataCacheSize = j;
        this.smallSizeThumbCacheSize = j2;
        this.fullSizeThumbCacheSize = j3;
    }

    public final synchronized DiskLruCache getDiskCache(Key key) throws IOException {
        DiskLruCache diskLruCache;
        if (CacheKeyUtils.isResourceCacheKey(key)) {
            if (CacheKeyUtils.isFullSizeFromKey(key)) {
                if (this.fullSizeCache == null) {
                    Trace.beginSection("openFullSizeDiskCache");
                    long currentTimeMillis = System.currentTimeMillis();
                    this.fullSizeCache = DiskLruCache.open(this.fullSizeDirectory, 1, 1, this.fullSizeThumbCacheSize);
                    DefaultLogger.d("DiskLruCacheProxy", "open full size cache costs: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    Trace.endSection();
                }
                diskLruCache = this.fullSizeCache;
            } else {
                if (this.smallSizeCache == null) {
                    Trace.beginSection("openSmallSizeDiskCache");
                    long currentTimeMillis2 = System.currentTimeMillis();
                    this.smallSizeCache = DiskLruCache.open(this.smallSizeDirectory, 1, 1, this.smallSizeThumbCacheSize);
                    DefaultLogger.d("DiskLruCacheProxy", "open small size cache costs: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2));
                    Trace.endSection();
                }
                diskLruCache = this.smallSizeCache;
            }
        } else {
            if (this.dataDiskCache == null) {
                long currentTimeMillis3 = System.currentTimeMillis();
                this.dataDiskCache = DiskLruCache.open(this.remoteDataDirectory, 1, 1, this.remoteDataCacheSize);
                DefaultLogger.d("DiskLruCacheProxy", "open remote data cache costs: %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis3));
            }
            diskLruCache = this.dataDiskCache;
        }
        return diskLruCache;
    }

    @Override // com.bumptech.glide.load.engine.cache.DiskCache
    public File get(Key key) {
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        if (Log.isLoggable("DiskLruCacheProxy", 2)) {
            Log.v("DiskLruCacheProxy", "Get: Obtained: " + safeKey + " for for Key: " + key);
        }
        try {
            DiskLruCache.Value value = getDiskCache(key).get(safeKey);
            if (value == null) {
                return null;
            }
            return value.getFile(0);
        } catch (IOException e) {
            if (!Log.isLoggable("DiskLruCacheProxy", 5)) {
                return null;
            }
            Log.w("DiskLruCacheProxy", "Unable to get from disk cache", e);
            return null;
        }
    }

    @Override // com.bumptech.glide.load.engine.cache.DiskCache
    public void put(Key key, DiskCache.Writer writer) {
        DiskLruCache diskCache;
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        this.writeLocker.acquire(safeKey);
        try {
            if (Log.isLoggable("DiskLruCacheProxy", 2)) {
                Log.v("DiskLruCacheProxy", "Put: Obtained: " + safeKey + " for for Key: " + key);
            }
            try {
                diskCache = getDiskCache(key);
            } catch (IOException e) {
                if (Log.isLoggable("DiskLruCacheProxy", 5)) {
                    Log.w("DiskLruCacheProxy", "Unable to put to disk cache", e);
                }
            }
            if (diskCache.get(safeKey) != null) {
                return;
            }
            DiskLruCache.Editor edit = diskCache.edit(safeKey);
            if (edit == null) {
                throw new IllegalStateException("Had two simultaneous puts for: " + safeKey);
            }
            try {
                if (writer.write(edit.getFile(0))) {
                    edit.commit();
                }
                edit.abortUnlessCommitted();
            } catch (Throwable th) {
                edit.abortUnlessCommitted();
                throw th;
            }
        } finally {
            this.writeLocker.release(safeKey);
        }
    }
}
