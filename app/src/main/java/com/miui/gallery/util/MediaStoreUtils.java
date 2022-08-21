package com.miui.gallery.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaStoreUtils {
    public static final String[] PROJECTION = {j.c, "_data", "mime_type"};
    @Deprecated
    public static final Uri BASE_URI = MediaStore.Files.getContentUri("external");

    public static String getMineTypeFromUri(Uri uri) {
        if (uri != null && uri.getPath() != null) {
            String path = uri.getPath();
            if (path.contains("images")) {
                return "image";
            }
            if (path.contains("videos")) {
                return "video";
            }
        }
        return null;
    }

    public static String getMediaFilePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Uri parse = Uri.parse(str);
        if (!"media".equals(parse.getAuthority())) {
            return null;
        }
        return (String) SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), parse, new String[]{"_data"}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<String>() { // from class: com.miui.gallery.util.MediaStoreUtils.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(0);
            }
        });
    }

    public static long getMediaStoreId(String str) {
        Uri contentUri;
        if (TextUtils.isEmpty(str)) {
            return -1L;
        }
        String format = String.format("%s=%s", "_data", DatabaseUtils.sqlEscapeString(str));
        Context sGetAndroidContext = StaticContext.sGetAndroidContext();
        if (BaseFileMimeUtil.isImageFromMimeType(BaseFileMimeUtil.getMimeType(str))) {
            contentUri = MediaStore.Images.Media.getContentUri("external");
        } else {
            contentUri = MediaStore.Video.Media.getContentUri("external");
        }
        Long l = (Long) SafeDBUtil.safeQuery(sGetAndroidContext, contentUri, PROJECTION, format, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Long>() { // from class: com.miui.gallery.util.MediaStoreUtils.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Long mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.moveToFirst()) {
                    return Long.valueOf(cursor.getLong(0));
                }
                return -1L;
            }
        });
        if (l == null) {
            return -1L;
        }
        return l.longValue();
    }

    public static Uri getFileMediaUri(String str) {
        Uri contentUri;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        final String mediaStoreVolumeName = StorageUtils.getMediaStoreVolumeName(StaticContext.sGetAndroidContext(), str);
        String format = String.format("%s='%s'", "_data", str);
        Context sGetAndroidContext = StaticContext.sGetAndroidContext();
        if (BaseFileMimeUtil.isImageFromMimeType(BaseFileMimeUtil.getMimeType(str))) {
            contentUri = MediaStore.Images.Media.getContentUri(mediaStoreVolumeName);
        } else {
            contentUri = MediaStore.Video.Media.getContentUri(mediaStoreVolumeName);
        }
        return (Uri) SafeDBUtil.safeQuery(sGetAndroidContext, contentUri, PROJECTION, format, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Uri>() { // from class: com.miui.gallery.util.MediaStoreUtils.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Uri mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return MediaStoreUtils.getFileMediaUri(mediaStoreVolumeName, cursor.getLong(0), cursor.getString(2));
            }
        });
    }

    public static Uri getFileMediaUri(String str, long j, String str2) {
        if (BaseFileMimeUtil.isImageFromMimeType(str2)) {
            return ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri(str), j);
        }
        if (!BaseFileMimeUtil.isVideoFromMimeType(str2)) {
            return null;
        }
        return ContentUris.withAppendedId(MediaStore.Video.Media.getContentUri(str), j);
    }

    public static void makeInvalid(Context context, List<String> list) {
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        LinkedList linkedList2 = new LinkedList();
        for (String str : list) {
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            if (BaseFileMimeUtil.isImageFromMimeType(mimeType)) {
                linkedList.add(str);
            } else if (BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
                linkedList2.add(str);
            }
        }
        makeInvalid(context, linkedList, MediaStore.Images.Media.getContentUri("external"));
        makeInvalid(context, linkedList2, MediaStore.Video.Media.getContentUri("external"));
    }

    public static void makeInvalid(Context context, List<String> list, Uri uri) {
        ContentResolver contentResolver;
        Object[] objArr;
        int i;
        int i2;
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        if (Build.VERSION.SDK_INT >= 30) {
            contentValues.put("is_favorite", (Integer) (-1));
        } else {
            contentValues.put("media_type", (Integer) 0);
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = 0;
        int i3 = 0;
        while (true) {
            try {
                contentResolver = context.getContentResolver();
                i = i3 * 100;
                i3++;
                i2 = i3 * 100;
            } catch (Exception e) {
                e = e;
            } catch (Throwable th) {
                th = th;
            }
            try {
                new Object[1][0] = TextUtils.join("', '", list.subList(i, Math.min(list.size(), i2)));
                try {
                    try {
                        j += contentResolver.update(uri, contentValues, String.format("_data IN ('%s')", objArr), null);
                        if (list.size() == Math.min(list.size(), i2)) {
                            DefaultLogger.d("MediaStoreUtils", "cost [%d] ms to hide [%d]/[%d] items in [%s].", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Long.valueOf(j), Integer.valueOf(list.size()), uri.toString());
                            return;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        DefaultLogger.d("MediaStoreUtils", "cost [%d] ms to hide [%d]/[%d] items in [%s].", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Long.valueOf(j), Integer.valueOf(list.size()), uri.toString());
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    DefaultLogger.e("MediaStoreUtils", "makeInvisible error, [%s].", e.getMessage());
                    DefaultLogger.d("MediaStoreUtils", "cost [%d] ms to hide [%d]/[%d] items in [%s].", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Long.valueOf(j), Integer.valueOf(list.size()), uri.toString());
                    return;
                }
            } catch (Exception e3) {
                e = e3;
                DefaultLogger.e("MediaStoreUtils", "makeInvisible error, [%s].", e.getMessage());
                DefaultLogger.d("MediaStoreUtils", "cost [%d] ms to hide [%d]/[%d] items in [%s].", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Long.valueOf(j), Integer.valueOf(list.size()), uri.toString());
                return;
            } catch (Throwable th3) {
                th = th3;
                DefaultLogger.d("MediaStoreUtils", "cost [%d] ms to hide [%d]/[%d] items in [%s].", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Long.valueOf(j), Integer.valueOf(list.size()), uri.toString());
                throw th;
            }
        }
    }
}
