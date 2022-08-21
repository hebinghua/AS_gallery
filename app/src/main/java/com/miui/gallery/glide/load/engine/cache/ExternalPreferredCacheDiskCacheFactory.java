package com.miui.gallery.glide.load.engine.cache;

import android.content.Context;
import com.miui.gallery.glide.load.engine.cache.DiskLruCacheFactory;
import java.io.File;

/* loaded from: classes2.dex */
public final class ExternalPreferredCacheDiskCacheFactory extends DiskLruCacheFactory {
    public ExternalPreferredCacheDiskCacheFactory(final Context context, final String str, long j, long j2, long j3) {
        super(new DiskLruCacheFactory.CacheDirectoryGetter() { // from class: com.miui.gallery.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory.1
            public final File getInternalCacheDirectory() {
                File filesDir = context.getFilesDir();
                if (filesDir == null) {
                    return null;
                }
                return str != null ? new File(filesDir, str) : filesDir;
            }

            @Override // com.miui.gallery.glide.load.engine.cache.DiskLruCacheFactory.CacheDirectoryGetter
            public File getCacheDirectory() {
                File externalFilesDir;
                File internalCacheDirectory = getInternalCacheDirectory();
                return ((internalCacheDirectory == null || !internalCacheDirectory.exists()) && (externalFilesDir = context.getExternalFilesDir(null)) != null && externalFilesDir.canWrite()) ? str != null ? new File(externalFilesDir, str) : externalFilesDir : internalCacheDirectory;
            }
        }, j, j2, j3);
    }
}
