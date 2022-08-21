package com.cdv.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import com.cdv.utils.NvAndroidBitmap;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public class NvAndroidThumbnail {
    private static final String TAG = "NvAndroidThumbnail";

    public static Bitmap createThumbnail(Context context, String str, boolean z, int i, int i2) {
        Cursor query;
        ContentResolver contentResolver;
        long j;
        Bitmap thumbnail;
        int columnIndex;
        Bitmap createRotatedBitmap;
        if (context == null || str == null || str.isEmpty()) {
            return null;
        }
        ContentResolver contentResolver2 = context.getContentResolver();
        if (contentResolver2 == null) {
            return null;
        }
        boolean startsWith = str.startsWith("content://");
        NvAndroidBitmap.Size size = new NvAndroidBitmap.Size(i, i2);
        if (!startsWith) {
            query = contentResolver2.query(z ? MediaStore.Video.Media.EXTERNAL_CONTENT_URI : MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{j.c}, "_data=?", new String[]{str}, null);
        } else {
            query = contentResolver2.query(Uri.parse(str), new String[]{j.c}, null, null, null);
        }
        if (query == null || !query.moveToFirst()) {
            if (query != null) {
                query.close();
            }
            return createThumbnailFromFile(context, str, z, size);
        }
        int columnIndex2 = query.getColumnIndex(j.c);
        if (columnIndex2 < 0) {
            query.close();
            return createThumbnailFromFile(context, str, z, size);
        }
        long j2 = query.getLong(columnIndex2);
        query.close();
        if (Build.VERSION.SDK_INT < 29) {
            Uri uri = z ? MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI : MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
            String[] strArr = {"_data"};
            StringBuilder sb = new StringBuilder();
            sb.append(z ? "video_id" : "image_id");
            sb.append("=?");
            contentResolver = contentResolver2;
            j = j2;
            Cursor query2 = contentResolver2.query(uri, strArr, sb.toString(), new String[]{String.valueOf(j2)}, null);
            if (query2 != null && query2.moveToFirst()) {
                if (z) {
                    columnIndex = query2.getColumnIndex("_data");
                } else {
                    columnIndex = query2.getColumnIndex("_data");
                }
                if (columnIndex >= 0) {
                    String string = query2.getString(columnIndex);
                    query2.close();
                    if (!string.isEmpty() && (createRotatedBitmap = NvAndroidBitmap.createRotatedBitmap(context, string, size, 2, false)) != null) {
                        return transformSystemGeneratedBitmap(context, createRotatedBitmap, str, z);
                    }
                    query2 = null;
                }
            }
            if (query2 != null) {
                query2.close();
            }
        } else {
            contentResolver = contentResolver2;
            j = j2;
        }
        if (z) {
            thumbnail = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, j, 1, null);
        } else {
            thumbnail = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, j, 1, null);
        }
        if (thumbnail != null) {
            return transformSystemGeneratedBitmap(context, thumbnail, str, z);
        }
        Log.w(TAG, String.format("Fail to get thumbnail file for media '%d'!", Long.valueOf(j)));
        return createThumbnailFromFile(context, str, z, size);
    }

    private static Bitmap transformSystemGeneratedBitmap(Context context, Bitmap bitmap, String str, boolean z) {
        NvAndroidBitmap.ImageInfo imageInfo;
        if (bitmap == null) {
            return null;
        }
        if (z || Build.VERSION.SDK_INT >= 29 || (imageInfo = NvAndroidBitmap.getImageInfo(context, str)) == null) {
            return bitmap;
        }
        try {
            return NvAndroidBitmap.transformBitmap(bitmap, imageInfo.orientation);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private static Bitmap createThumbnailFromFile(Context context, String str, boolean z, NvAndroidBitmap.Size size) {
        if (!z) {
            return NvAndroidBitmap.createRotatedBitmap(context, str, size, 2, false);
        }
        Bitmap createVideoThumbnail = ThumbnailUtils.createVideoThumbnail(str, 1);
        if (createVideoThumbnail == null) {
            Log.e(TAG, String.format("Failed to create video thumbnail bitmap for '%s'!", str));
        }
        return createVideoThumbnail;
    }
}
