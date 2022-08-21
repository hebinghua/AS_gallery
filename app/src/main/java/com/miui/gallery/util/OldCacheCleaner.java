package com.miui.gallery.util;

import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.deprecated.Storage;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.uil.BlobCache;
import java.io.File;

/* loaded from: classes2.dex */
public class OldCacheCleaner {
    public static void clean() {
        if (!PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.UPGRADE_OLD_CACHE_CLEANED, false)) {
            cleanThumbnailCache();
            cleanBlobCache();
            cleanTempFiles();
            PreferenceHelper.putBoolean(GalleryPreferences.PrefKeys.UPGRADE_OLD_CACHE_CLEANED, true);
        }
        if (!PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.UPGRADE_LEGACY_THUMB_CACHE_DELETED, false)) {
            deleteLegacyThumbCache();
            PreferenceHelper.putBoolean(GalleryPreferences.PrefKeys.UPGRADE_LEGACY_THUMB_CACHE_DELETED, true);
        }
        if (!PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.UPGRADE_DEPRECATED_GLIDE_CACHE_DELETED, false)) {
            deleteDeprecatedGlideCache();
            PreferenceHelper.putBoolean(GalleryPreferences.PrefKeys.UPGRADE_DEPRECATED_GLIDE_CACHE_DELETED, true);
        }
        if (!PreferenceHelper.getBoolean(GalleryPreferences.PrefKeys.UPGRADE_LOCAL_THUMBNAIL_CACHE_FILE_DELETED, false)) {
            deleteLocalThumbnailCache();
            PreferenceHelper.putBoolean(GalleryPreferences.PrefKeys.UPGRADE_LOCAL_THUMBNAIL_CACHE_FILE_DELETED, true);
        }
    }

    public static void deleteLocalThumbnailCache() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("MIUI/Gallery/cloud/.cache");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("OldCacheCleaner", "deleteLocalThumbnailCache");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(pathInPrimaryStorage + File.separator + ".localthumbnailFile", IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, appendInvokerTag);
        if (documentFile == null) {
            return;
        }
        documentFile.delete();
    }

    public static void deleteDeprecatedGlideCache() {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("OldCacheCleaner", "deleteDeprecatedGlideCache");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        StringBuilder sb = new StringBuilder();
        sb.append(GalleryApp.sGetAndroidContext().getCacheDir());
        String str = File.separator;
        sb.append(str);
        sb.append("gallery_disk_cache");
        String sb2 = sb.toString();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE_DIRECTORY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(sb2, permission, appendInvokerTag);
        if (documentFile != null) {
            documentFile.delete();
        }
        StorageStrategyManager storageStrategyManager2 = StorageSolutionProvider.get();
        DocumentFile documentFile2 = storageStrategyManager2.getDocumentFile(GalleryApp.sGetAndroidContext().getExternalCacheDir() + str + "gallery_disk_cache", permission, appendInvokerTag);
        if (documentFile2 != null) {
            documentFile2.delete();
        }
    }

    public static void deleteLegacyThumbCache() {
        String[] strArr = {"micro_thumbnail_blob", "thumbnail_blob", "photo_blob"};
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("MIUI/Gallery/cloud/.cache");
        for (int i = 0; i < 3; i++) {
            String str = strArr[i];
            DefaultLogger.i("OldCacheCleaner", "delete thumb BlobCache: %s", str);
            BlobCache.deleteFiles(pathInPrimaryStorage + File.separator + str);
        }
    }

    public static void cleanThumbnailCache() {
        String[] pathsInExternalStorage = StorageUtils.getPathsInExternalStorage(GalleryApp.sGetAndroidContext(), "MIUI");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("OldCacheCleaner", "cleanThumbnailCache");
        for (String str : pathsInExternalStorage) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str + File.separator + ".cache/Gallery", IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
        }
    }

    public static void cleanBlobCache() {
        File file;
        String[] strArr = {"imgcache", "rev_geocoding", "bookmark", "avatar", "internal_img_cache", "cover"};
        File[] fileArr = {GalleryApp.sGetAndroidContext().getCacheDir(), GalleryApp.sGetAndroidContext().getExternalCacheDir()};
        for (int i = 0; i < 2; i++) {
            if (fileArr[i] != null) {
                for (int i2 = 0; i2 < 6; i2++) {
                    BlobCache.deleteFiles(file.getAbsolutePath() + File.separator + strArr[i2]);
                }
            }
        }
    }

    public static void cleanTempFiles() {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("OldCacheCleaner", "cleanTempFiles");
        for (String str : Storage.getAllSdCardAvatarFilePath()) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
        }
    }
}
