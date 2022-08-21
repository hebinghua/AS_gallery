package com.miui.gallery.provider.cache;

import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ParcelableUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.uil.BlobCache;
import java.io.File;

/* loaded from: classes2.dex */
public class SearchIconDiskCache {
    public BlobCache mBlobCache;
    public final Object mLock;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final SearchIconDiskCache INSTANCE = new SearchIconDiskCache(new File(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/searchIconCache")), 350, 52428800);
    }

    public static SearchIconDiskCache getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public SearchIconDiskCache(File file, int i, int i2) {
        this.mLock = new Object();
        if (file != null) {
            try {
                if (!file.exists()) {
                    StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SearchIconDiskCache", "SearchIconDiskCache"));
                }
            } catch (Exception e) {
                DefaultLogger.e("SearchIconDiskCache", e);
                return;
            }
        }
        this.mBlobCache = new BlobCache(file + File.separator + "search_icon_blob", i, i2, false, 1);
    }

    public boolean putIcon(String str, SearchIconItem searchIconItem) {
        if (str == null || searchIconItem == null) {
            return false;
        }
        synchronized (this.mLock) {
            if (this.mBlobCache != null) {
                try {
                    System.currentTimeMillis();
                    this.mBlobCache.insert(BaseMiscUtil.crc64Long(str.getBytes()), ParcelableUtil.marshall(searchIconItem));
                } catch (Exception e) {
                    DefaultLogger.e("SearchIconDiskCache", e);
                    return false;
                }
            }
        }
        return true;
    }

    public SearchIconItem getIcon(String str) {
        SearchIconItem searchIconItem = null;
        if (str == null) {
            return null;
        }
        synchronized (this.mLock) {
            try {
                if (this.mBlobCache != null) {
                    System.currentTimeMillis();
                    byte[] lookup = this.mBlobCache.lookup(BaseMiscUtil.crc64Long(str.getBytes()));
                    if (lookup != null) {
                        searchIconItem = (SearchIconItem) ParcelableUtil.unmarshall(lookup, SearchIconItem.CREATOR);
                    }
                }
            } catch (Exception e) {
                DefaultLogger.e("SearchIconDiskCache", e);
            }
        }
        return searchIconItem;
    }
}
