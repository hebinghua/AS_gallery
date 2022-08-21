package com.miui.gallery.util;

import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.editor.photo.sdk.CleanScheduler;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public class SecretAlbumCryptoUtils {
    public static final Map<String, ReentrantLock> sFileLocks = new WeakHashMap();

    public static ReentrantLock getLockForFile(String str) {
        ReentrantLock reentrantLock;
        Map<String, ReentrantLock> map = sFileLocks;
        synchronized (map) {
            reentrantLock = map.get(str);
            if (reentrantLock == null) {
                reentrantLock = new ReentrantLock();
                map.put(str, reentrantLock);
            }
        }
        return reentrantLock;
    }

    public static String generateDecryptFileName4Video(File file, byte[] bArr) {
        String absolutePath = file.getAbsolutePath();
        long length = file.length();
        String fileName = BaseFileUtils.getFileName(BaseFileUtils.getFileTitle(absolutePath));
        if (bArr != null) {
            String str = "." + CloudUtils.SecretAlbumUtils.getMD5Key(bArr);
            if (fileName.endsWith(str)) {
                fileName = fileName.substring(0, fileName.lastIndexOf(str));
            }
        }
        return String.format(Locale.US, "%s%s%d.%s", fileName, "L", Long.valueOf(length), (String) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"fileName"}, "localGroupId = -1000 AND localFile = '" + absolutePath + "'", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<String>() { // from class: com.miui.gallery.util.SecretAlbumCryptoUtils.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1808handle(Cursor cursor) {
                return (cursor == null || !cursor.moveToNext()) ? "mp4" : BaseFileUtils.getExtensionWithFileName(cursor.getString(0));
            }
        }));
    }

    public static String generateTempFileName(String str) {
        return String.format(Locale.US, "temp_%s_%d", str, Long.valueOf(SystemClock.elapsedRealtimeNanos()));
    }

    public static File getSecretFolder() {
        return new File(GalleryApp.sGetAndroidContext().getCacheDir(), ".temp_video_files");
    }

    public static Uri decryptVideo2CacheFolder(Uri uri, byte[] bArr, long j) {
        File file;
        String decryptFile;
        Uri uri2;
        if (uri == null) {
            return null;
        }
        File secretFolder = getSecretFolder();
        if (!secretFolder.exists() && !secretFolder.mkdirs()) {
            DefaultLogger.e("SecretAlbumCryptoUtils", "Failed to create secret temp folder!!!");
            return null;
        }
        String path = uri.getPath();
        File file2 = new File(path);
        if (!CloudUtils.SecretAlbumUtils.isEncryptedVideoByPath(path)) {
            return Uri.fromFile(file2);
        }
        if (!file2.exists() || !file2.isFile()) {
            return null;
        }
        File file3 = new File(secretFolder, generateDecryptFileName4Video(file2, bArr));
        ReentrantLock lockForFile = getLockForFile(file3.getPath());
        if (lockForFile.isLocked()) {
            DefaultLogger.d("SecretAlbumCryptoUtils", "file is already decrypting, just waiting... %s", file3.getName());
        }
        lockForFile.lock();
        try {
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("SecretAlbumCryptoUtils", "decryptVideo2CacheFolder");
            if (file3.exists()) {
                DefaultLogger.d("SecretAlbumCryptoUtils", "decrypted file [%s] already exists, reuse it", file3.getName());
                uri2 = Uri.fromFile(file3);
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                File file4 = new File(secretFolder, generateTempFileName(file3.getName()));
                String path2 = file4.getPath();
                if (bArr != null) {
                    decryptFile = CloudUtils.SecretAlbumUtils.decryptFile(path, path2, true, bArr, false);
                    file = file4;
                } else {
                    file = file4;
                    decryptFile = CloudUtils.SecretAlbumUtils.decryptFile(path, path2, true, j, false);
                    DefaultLogger.d("SecretAlbumCryptoUtils", "decode %s and secret key is null", path);
                }
                DefaultLogger.d("SecretAlbumCryptoUtils", "decrypt video costs %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                if (file3.exists()) {
                    DefaultLogger.d("SecretAlbumCryptoUtils", "decrypted file [%s] already exists", file3.getName());
                    uri2 = Uri.fromFile(file3);
                    DefaultLogger.d("SecretAlbumCryptoUtils", "delete temp file");
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(decryptFile, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile != null) {
                        documentFile.delete();
                    }
                } else {
                    if (decryptFile != null) {
                        if (file.renameTo(file3)) {
                            uri2 = Uri.fromFile(file3);
                        } else {
                            DefaultLogger.e("SecretAlbumCryptoUtils", "rename temp file failed");
                            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
                            DocumentFile documentFile2 = storageStrategyManager.getDocumentFile(decryptFile, permission, appendInvokerTag);
                            if (documentFile2 != null) {
                                documentFile2.delete();
                            }
                            DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(file3.getAbsolutePath(), permission, appendInvokerTag);
                            if (documentFile3 != null) {
                                documentFile3.delete();
                            }
                        }
                    }
                    uri2 = null;
                }
            }
            if (uri2 != null) {
                CleanScheduler.schedule(GalleryApp.sGetAndroidContext(), "SecretAlbumCrypto#clean", secretFolder.getPath());
            }
            return uri2;
        } finally {
            lockForFile.unlock();
        }
    }

    public static boolean encrypt(String str, String str2, boolean z, byte[] bArr) {
        if (z) {
            return CryptoUtil.encryptFileHeader(str, str2, bArr);
        }
        return CryptoUtil.encryptFile(str, str2, bArr);
    }

    public static boolean decrypt(String str, String str2, boolean z, byte[] bArr) {
        if (z) {
            return CryptoUtil.decryptFileHeader(str, str2, bArr);
        }
        return CryptoUtil.decryptFile(str, str2, bArr);
    }
}
