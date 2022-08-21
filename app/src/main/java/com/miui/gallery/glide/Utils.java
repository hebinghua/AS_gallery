package com.miui.gallery.glide;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import ch.qos.logback.core.joran.action.Action;
import com.bumptech.glide.load.Options;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.glide.load.GalleryOptions;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StaticContext;
import com.xiaomi.stat.MiStat;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class Utils {
    public static final LoadingCache<Uri, String> MIME_TYPE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(2, TimeUnit.MINUTES).maximumSize(150).build(new CacheLoader<Uri, String>() { // from class: com.miui.gallery.glide.Utils.1
        @Override // com.google.common.cache.CacheLoader
        public String load(Uri uri) {
            return Utils.parseMimeTypeInner(uri);
        }
    });

    public static String parseMimeType(Uri uri) {
        return uri == null ? "*/*" : MIME_TYPE_CACHE.getUnchecked(uri);
    }

    public static String parseMimeType(Uri uri, Options options) {
        String str = (String) options.get(GalleryOptions.INFERRED_MIME_TYPE);
        return TextUtils.isEmpty(str) ? parseMimeType(uri) : str;
    }

    public static String parseMimeTypeInner(Uri uri) {
        String str = null;
        if (Action.FILE_ATTRIBUTE.equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getPath();
            if (CloudUtils.SecretAlbumUtils.isEncryptedImageByPath(path) || CloudUtils.SecretAlbumUtils.isUnencryptedImageByPath(path)) {
                str = "image/*";
            } else if (CloudUtils.SecretAlbumUtils.isEncryptedVideoByPath(path) || CloudUtils.SecretAlbumUtils.isUnencryptedVideoByPath(path)) {
                str = "video/*";
            } else if (TrashManager.isTrashFileByPath(path)) {
                str = BaseFileMimeUtil.getMimeType(TrashManager.probeFileName(path));
            }
            if (TextUtils.isEmpty(str) || "*/*".equals(str)) {
                str = BaseFileMimeUtil.getMimeType(path);
            }
        } else if (MiStat.Param.CONTENT.equals(uri.getScheme())) {
            try {
                str = StaticContext.sGetAndroidContext().getContentResolver().getType(uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TextUtils.isEmpty(str) ? "*/*" : str;
    }

    public static Uri parseUri(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.charAt(0) == '/') {
            return toFileUri(str);
        }
        Uri parse = Uri.parse(str);
        return parse.getScheme() == null ? toFileUri(str) : parse;
    }

    public static Uri toFileUri(String str) {
        return Uri.fromFile(new File(str));
    }

    public static DocumentFile bytes2TempFile(byte[] bArr, String str) {
        DocumentFile documentFile;
        OutputStream openOutputStream;
        if (bArr == null) {
            return null;
        }
        String str2 = StaticContext.sGetAndroidContext().getCacheDir() + File.separator + String.format(Locale.US, "tmp_%d_%d.%s", Long.valueOf(System.currentTimeMillis()), Long.valueOf(SystemClock.elapsedRealtimeNanos()), str);
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
        if (!storageStrategyManager.checkPermission(str2, permission).granted || (documentFile = StorageSolutionProvider.get().getDocumentFile(str2, permission, FileHandleRecordHelper.appendInvokerTag("Utils", "bytes2TempFile"))) == null || !documentFile.exists() || (openOutputStream = StorageSolutionProvider.get().openOutputStream(documentFile)) == null) {
            return null;
        }
        try {
            openOutputStream.write(bArr);
            openOutputStream.flush();
            return documentFile;
        } catch (Exception e) {
            e.printStackTrace();
            if (documentFile.exists()) {
                documentFile.delete();
            }
            return null;
        }
    }

    public static InputStream bytesToStream(byte[] bArr) {
        return new ByteArrayInputStream(bArr);
    }
}
