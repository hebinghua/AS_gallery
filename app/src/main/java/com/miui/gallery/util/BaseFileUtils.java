package com.miui.gallery.util;

import android.content.Context;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.util.Locale;

/* loaded from: classes2.dex */
public class BaseFileUtils extends miuix.core.util.FileUtils {
    public static String getFileName(String str) {
        String trim = str == null ? null : str.trim();
        if (!TextUtils.isEmpty(trim)) {
            String str2 = File.separator;
            if (trim.endsWith(str2)) {
                trim = trim.substring(0, trim.length() - 1);
            }
            return trim.substring(trim.lastIndexOf(str2) + 1);
        }
        return "";
    }

    public static String getFileNameWithoutExtension(String str) {
        return getFileTitle(getFileName(str));
    }

    public static String getExtension(String str) {
        return getExtensionWithFileName(getFileName(str));
    }

    public static String getExtensionWithFileName(String str) {
        int lastIndexOf;
        return (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf(46)) > -1) ? str.substring(lastIndexOf + 1) : "";
    }

    public static String getFileTitle(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf > -1 ? str.substring(0, lastIndexOf) : str;
    }

    public static String appendFileTitleWithExtention(String str, String str2) {
        int lastIndexOf;
        if (str2 == null || str2.isEmpty() || (lastIndexOf = str2.lastIndexOf(46)) < 0) {
            return str;
        }
        return str + str2.substring(lastIndexOf);
    }

    public static long getFileSize(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0L;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("BaseFileUtils", "getFileSize"));
        if (documentFile != null && documentFile.exists() && documentFile.isFile()) {
            return documentFile.length();
        }
        return 0L;
    }

    public static String getParentFolderPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(File.separator);
        return lastIndexOf > -1 ? str.substring(0, lastIndexOf) : str;
    }

    public static String getRelativeRootParentFolderPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String str2 = File.separator;
        if (str.startsWith(str2)) {
            str = str.substring(1);
        }
        if (!str.endsWith(str2)) {
            str = str.substring(0, str.lastIndexOf(str2) + 1);
        }
        return str.substring(0, str.indexOf(str2) + 1);
    }

    public static String getAbsoluteRootParentFolderPath(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            return null;
        }
        if (TextUtils.isEmpty(str) || !str.startsWith(File.separator)) {
            return str;
        }
        for (String str2 : StorageUtils.getMountedVolumePaths(context)) {
            if (contains(str2, str) && str2.length() < str.length()) {
                String relativeRootParentFolderPath = getRelativeRootParentFolderPath(str.substring(str2.length()));
                String str3 = File.separator;
                if (str2.endsWith(str3)) {
                    return str2 + relativeRootParentFolderPath;
                }
                return str2 + str3 + relativeRootParentFolderPath;
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x0012, code lost:
        if (r3.startsWith(r0) == false) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String concat(java.lang.String r2, java.lang.String r3) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r3)
            if (r0 != 0) goto L15
            java.lang.String r0 = java.io.File.separator
            boolean r1 = r2.endsWith(r0)
            if (r1 != 0) goto L15
            boolean r1 = r3.startsWith(r0)
            if (r1 != 0) goto L15
            goto L17
        L15:
            java.lang.String r0 = ""
        L17:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r2)
            r1.append(r0)
            r1.append(r3)
            java.lang.String r2 = r1.toString()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.BaseFileUtils.concat(java.lang.String, java.lang.String):java.lang.String");
    }

    public static boolean contains(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        if (str.equals(str2)) {
            return true;
        }
        if (!str.endsWith(h.g)) {
            str = str + h.g;
        }
        return str2.toLowerCase().startsWith(str.toLowerCase());
    }

    public static int getBucketID(String str) {
        if (str.endsWith(File.separator)) {
            str = str.substring(0, str.length() - 1);
        }
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        return str.toLowerCase(Locale.ENGLISH).hashCode();
    }
}
