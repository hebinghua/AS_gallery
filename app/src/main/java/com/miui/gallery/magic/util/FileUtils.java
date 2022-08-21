package com.miui.gallery.magic.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import ch.qos.logback.core.joran.action.Action;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.b.h;

/* loaded from: classes2.dex */
public class FileUtils {
    /* JADX WARN: Removed duplicated region for block: B:70:0x010a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void copyAssetsDspToDirPhone(android.content.Context r9, java.lang.String r10) {
        /*
            Method dump skipped, instructions count: 284
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.magic.util.FileUtils.copyAssetsDspToDirPhone(android.content.Context, java.lang.String):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r5v8, types: [com.miui.gallery.magic.util.MagicLog] */
    /* JADX WARN: Type inference failed for: r6v1, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r6v3 */
    /* JADX WARN: Type inference failed for: r6v6, types: [java.io.FileOutputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void copyAssetsDspToDirPhoneForPng(android.content.Context r5, java.lang.String r6) {
        /*
            android.content.res.AssetManager r0 = r5.getAssets()     // Catch: java.io.IOException -> Lb9
            java.io.InputStream r0 = r0.open(r6)     // Catch: java.io.IOException -> Lb9
            java.io.File r1 = new java.io.File     // Catch: java.io.IOException -> Lb9
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.io.IOException -> Lb9
            r2.<init>()     // Catch: java.io.IOException -> Lb9
            java.io.File r5 = r5.getFilesDir()     // Catch: java.io.IOException -> Lb9
            java.lang.String r5 = r5.getAbsolutePath()     // Catch: java.io.IOException -> Lb9
            r2.append(r5)     // Catch: java.io.IOException -> Lb9
            java.lang.String r5 = java.io.File.separator     // Catch: java.io.IOException -> Lb9
            r2.append(r5)     // Catch: java.io.IOException -> Lb9
            r2.append(r6)     // Catch: java.io.IOException -> Lb9
            java.lang.String r5 = r2.toString()     // Catch: java.io.IOException -> Lb9
            r1.<init>(r5)     // Catch: java.io.IOException -> Lb9
            java.io.File r5 = r1.getParentFile()     // Catch: java.io.IOException -> Lb9
            boolean r5 = r5.exists()     // Catch: java.io.IOException -> Lb9
            if (r5 != 0) goto L3a
            java.io.File r5 = r1.getParentFile()     // Catch: java.io.IOException -> Lb9
            r5.mkdirs()     // Catch: java.io.IOException -> Lb9
        L3a:
            com.miui.gallery.magic.util.MagicLog r5 = com.miui.gallery.magic.util.MagicLog.INSTANCE     // Catch: java.io.IOException -> Lb9
            java.lang.String r6 = "FileUtilsCopyAssetsPhone"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.io.IOException -> Lb9
            r2.<init>()     // Catch: java.io.IOException -> Lb9
            java.lang.String r3 = "file:"
            r2.append(r3)     // Catch: java.io.IOException -> Lb9
            r2.append(r1)     // Catch: java.io.IOException -> Lb9
            java.lang.String r2 = r2.toString()     // Catch: java.io.IOException -> Lb9
            r5.showLog(r6, r2)     // Catch: java.io.IOException -> Lb9
            boolean r5 = r1.exists()     // Catch: java.io.IOException -> Lb9
            if (r5 == 0) goto L62
            long r5 = r1.length()     // Catch: java.io.IOException -> Lb9
            r2 = 0
            int r5 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r5 != 0) goto Lbd
        L62:
            r5 = 0
            java.io.FileOutputStream r6 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L8f java.io.IOException -> L94
            r6.<init>(r1)     // Catch: java.lang.Throwable -> L8f java.io.IOException -> L94
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r5 = new byte[r5]     // Catch: java.io.IOException -> L8d java.lang.Throwable -> La9
        L6c:
            int r1 = r0.read(r5)     // Catch: java.io.IOException -> L8d java.lang.Throwable -> La9
            r2 = -1
            if (r1 == r2) goto L78
            r2 = 0
            r6.write(r5, r2, r1)     // Catch: java.io.IOException -> L8d java.lang.Throwable -> La9
            goto L6c
        L78:
            r0.close()     // Catch: java.io.IOException -> L8d java.lang.Throwable -> La9
            r6.close()     // Catch: java.io.IOException -> L8d java.lang.Throwable -> La9
            r6.flush()     // Catch: java.io.IOException -> L8d java.lang.Throwable -> La9
            r6.close()     // Catch: java.lang.Throwable -> L85 java.io.IOException -> L87
            goto Lbd
        L85:
            r5 = move-exception
            goto L8c
        L87:
            r5 = move-exception
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L85
            goto Lbd
        L8c:
            throw r5     // Catch: java.io.IOException -> Lb9
        L8d:
            r5 = move-exception
            goto L98
        L8f:
            r6 = move-exception
            r4 = r6
            r6 = r5
            r5 = r4
            goto Laa
        L94:
            r6 = move-exception
            r4 = r6
            r6 = r5
            r5 = r4
        L98:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> La9
            if (r6 == 0) goto Lbd
            r6.close()     // Catch: java.lang.Throwable -> La1 java.io.IOException -> La3
            goto Lbd
        La1:
            r5 = move-exception
            goto La8
        La3:
            r5 = move-exception
            r5.printStackTrace()     // Catch: java.lang.Throwable -> La1
            goto Lbd
        La8:
            throw r5     // Catch: java.io.IOException -> Lb9
        La9:
            r5 = move-exception
        Laa:
            if (r6 == 0) goto Lb8
            r6.close()     // Catch: java.lang.Throwable -> Lb0 java.io.IOException -> Lb2
            goto Lb8
        Lb0:
            r5 = move-exception
            goto Lb7
        Lb2:
            r6 = move-exception
            r6.printStackTrace()     // Catch: java.lang.Throwable -> Lb0
            goto Lb8
        Lb7:
            throw r5     // Catch: java.io.IOException -> Lb9
        Lb8:
            throw r5     // Catch: java.io.IOException -> Lb9
        Lb9:
            r5 = move-exception
            r5.printStackTrace()
        Lbd:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.magic.util.FileUtils.copyAssetsDspToDirPhoneForPng(android.content.Context, java.lang.String):void");
    }

    public static String getLibraryDirPath(Context context) {
        return context.getDir("libs", 0).getAbsolutePath();
    }

    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if ((Build.VERSION.SDK_INT >= 19) && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                String[] split = DocumentsContract.getDocumentId(uri).split(":");
                if ("primary".equalsIgnoreCase(split[0])) {
                    return Environment.getExternalStorageDirectory() + h.g + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), null, null);
            } else {
                if (isMediaDocument(uri)) {
                    String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
                    String str = split2[0];
                    if ("image".equals(str)) {
                        uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(str)) {
                        uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(str)) {
                        uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
                }
            }
        } else if (MiStat.Param.CONTENT.equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        } else if (Action.FILE_ATTRIBUTE.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String str, String[] strArr) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"_data"}, str, strArr, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(query.getColumnIndexOrThrow("_data"));
                        query.close();
                        return string;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = query;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
