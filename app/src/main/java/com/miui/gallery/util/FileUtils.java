package com.miui.gallery.util;

import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import miuix.security.DigestUtils;

/* loaded from: classes2.dex */
public class FileUtils extends BaseFileUtils {
    public static byte[] getFileSha1(String str) {
        long uptimeMillis = SystemClock.uptimeMillis();
        DocumentFile fromFile = DocumentFile.fromFile(new File(str));
        if (!fromFile.exists() || !fromFile.isFile()) {
            DefaultLogger.d("FileUtils", "File [%s] doesn't exist or is not a file", str);
            return null;
        }
        fromFile.length();
        try {
            try {
                InputStream openInputStream = StorageSolutionProvider.get().openInputStream(fromFile);
                try {
                    byte[] bArr = DigestUtils.get(openInputStream, "SHA-1");
                    if (openInputStream != null) {
                        openInputStream.close();
                    }
                    long uptimeMillis2 = SystemClock.uptimeMillis() - uptimeMillis;
                    if (uptimeMillis2 > 100) {
                        DefaultLogger.w("FileUtils", "getSha1 for [%s] too long, costs [%d]", str, Long.valueOf(uptimeMillis2));
                    } else {
                        DefaultLogger.d("FileUtils", "getSha1 for [%s] costs [%d]", str, Long.valueOf(uptimeMillis2));
                    }
                    return bArr;
                } catch (Throwable th) {
                    if (openInputStream != null) {
                        try {
                            openInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (Exception e) {
                DefaultLogger.e("FileUtils", e);
                long uptimeMillis3 = SystemClock.uptimeMillis() - uptimeMillis;
                if (uptimeMillis3 > 100) {
                    DefaultLogger.w("FileUtils", "getSha1 for [%s] too long, costs [%d]", str, Long.valueOf(uptimeMillis3));
                } else {
                    DefaultLogger.d("FileUtils", "getSha1 for [%s] costs [%d]", str, Long.valueOf(uptimeMillis3));
                }
                return null;
            }
        } catch (Throwable th3) {
            long uptimeMillis4 = SystemClock.uptimeMillis() - uptimeMillis;
            if (uptimeMillis4 > 100) {
                DefaultLogger.w("FileUtils", "getSha1 for [%s] too long, costs [%d]", str, Long.valueOf(uptimeMillis4));
            } else {
                DefaultLogger.d("FileUtils", "getSha1 for [%s] costs [%d]", str, Long.valueOf(uptimeMillis4));
            }
            throw th3;
        }
    }

    public static boolean isBase64Url(String str) {
        int indexOf;
        return !TextUtils.isEmpty(str) && (indexOf = str.indexOf(44)) != -1 && str.substring(0, indexOf).endsWith(";base64");
    }

    public static String getSha1(String str) {
        return miuix.text.utilities.ExtraTextUtils.toHexReadable(getFileSha1(str));
    }

    public static File forceCreate(String str, String str2, int i) {
        DefaultLogger.d("FileUtils", "forceCreate file(%s, %s).", str, str2);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("FileUtils", "forceCreate");
        File file = new File(str, str2);
        if (!file.exists()) {
            DefaultLogger.d("FileUtils", "no conflict, create finish.");
            return file;
        } else if (i == 1) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile == null) {
                return file;
            }
            if (documentFile.delete()) {
                DeleteRecorder.getInstance().record(new DeleteRecord(51, file.getAbsolutePath(), "FileUtils"));
                return file;
            }
            DefaultLogger.e("FileUtils", "forceCreate: Delete exist file failed");
            return file;
        } else {
            DefaultLogger.d("FileUtils", "duplicated file fount, rename: ");
            int indexOf = str2.indexOf(".");
            String substring = str2.substring(0, indexOf);
            String substring2 = str2.substring(indexOf);
            int i2 = 1;
            while (file.exists()) {
                file = new File(str, substring + "_" + i2 + substring2);
                i2++;
            }
            DefaultLogger.d("FileUtils", "rename to %s, create finish.", file);
            return file;
        }
    }

    public static boolean isScreenShot(Uri uri) {
        if (uri != null) {
            String parentFolder = getParentFolder(uri);
            if (TextUtils.isEmpty(parentFolder)) {
                return false;
            }
            return "Screenshots".equals(parentFolder);
        }
        return false;
    }

    public static String getParentFolder(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments == null || pathSegments.size() <= 1) {
            return null;
        }
        return pathSegments.get(pathSegments.size() - 2);
    }

    public static boolean isFromCamera(String str) {
        if (!TextUtils.isEmpty(str)) {
            return str.toLowerCase().contains(MIUIStorageConstants.DIRECTORY_CAMERA_PATH.toLowerCase());
        }
        return false;
    }

    public static File getImageRelativeDngFile(String str) {
        File file;
        if (str == null || str.length() < 4) {
            return null;
        }
        if (str.substring(str.length() - 4).equalsIgnoreCase(".jpg")) {
            file = new File(str.substring(0, str.length() - 4) + ".dng");
        } else {
            file = null;
        }
        if (file != null && (!file.exists() || !file.isFile())) {
            return null;
        }
        return file;
    }
}
