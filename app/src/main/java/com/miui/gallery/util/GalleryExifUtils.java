package com.miui.gallery.util;

import android.content.Context;
import android.text.TextUtils;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class GalleryExifUtils {
    /* JADX WARN: Removed duplicated region for block: B:34:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00a4 A[Catch: Exception -> 0x006f, TRY_ENTER, TryCatch #0 {Exception -> 0x006f, blocks: (B:19:0x0055, B:21:0x0063, B:36:0x00a4, B:40:0x00cb, B:41:0x00d8, B:42:0x00e0, B:47:0x00ed, B:49:0x0100, B:51:0x0112, B:54:0x0119, B:57:0x0122, B:59:0x012a, B:62:0x0131, B:37:0x00b2), top: B:71:0x0055 }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00b2 A[Catch: Exception -> 0x006f, TryCatch #0 {Exception -> 0x006f, blocks: (B:19:0x0055, B:21:0x0063, B:36:0x00a4, B:40:0x00cb, B:41:0x00d8, B:42:0x00e0, B:47:0x00ed, B:49:0x0100, B:51:0x0112, B:54:0x0119, B:57:0x0122, B:59:0x012a, B:62:0x0131, B:37:0x00b2), top: B:71:0x0055 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e8 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00e9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.util.Pair<java.lang.Boolean, java.io.File> setImageFileDataTime(android.content.Context r17, java.lang.String r18, long r19, boolean r21, java.lang.String r22, java.lang.String r23) {
        /*
            Method dump skipped, instructions count: 336
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.GalleryExifUtils.setImageFileDataTime(android.content.Context, java.lang.String, long, boolean, java.lang.String, java.lang.String):android.util.Pair");
    }

    public static boolean isPhotoContainsCustomExifTags(String str) {
        return !TextUtils.isEmpty(str) && (ExifUtil.supportRefocus(str) || ExifUtil.isMotionPhoto(str) || ExifUtil.isDocPhoto(str) || ExifUtil.getMTSpecialAITypeValue(str) > 0 || ExifUtil.getXiaomiProduct(str) != null);
    }

    public static String generateUniqueFileDateTimeName(File file, long j) {
        return generateFileDateTimeName(file, j, BaseFileUtils.getParentFolderPath(file.getAbsolutePath()), true);
    }

    public static String generateUniqueFileDateTimeName(File file, long j, String str) {
        return generateFileDateTimeName(file, j, BaseFileUtils.getParentFolderPath(file.getAbsolutePath()), str, true);
    }

    public static String generateFileDateTimeName(File file, long j, String str, boolean z) {
        return generateFileDateTimeName(file, j, str, (String) null, z);
    }

    public static String generateFileDateTimeName(File file, long j, String str, boolean z, String str2) {
        return generateFileDateTimeName(file, j, str, str2, z);
    }

    public static String generateFileDateTimeName(File file, long j, String str, String str2, boolean z) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        Date date = new Date(j);
        String absolutePath = file.getAbsolutePath();
        String str3 = "." + BaseFileUtils.getExtension(absolutePath);
        StringBuilder sb = new StringBuilder(str + File.separator);
        String format = simpleDateFormat.format(date);
        String name = file.getName();
        int indexOf = name.indexOf(95);
        if (indexOf > 0) {
            sb.append(name.substring(0, indexOf + 1));
        } else {
            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf(46));
            }
            sb.append(name);
            sb.append("_");
        }
        sb.append(format);
        int length = sb.length();
        sb.append(str3);
        if (str2 != null) {
            sb.insert(length, "_" + str2);
        }
        if (z && new File(sb.toString()).exists()) {
            return sb.insert(length, BaseFileUtils.getFileSize(sb.toString())).toString();
        }
        return sb.toString();
    }

    public static String generateCacheFilePath(Context context, File file, long j, String str) {
        return generateFileDateTimeName(file, j, context.getCacheDir().getAbsolutePath(), false, str);
    }
}
