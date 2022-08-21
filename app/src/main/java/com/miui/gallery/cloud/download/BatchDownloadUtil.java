package com.miui.gallery.cloud.download;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.preference.ThumbnailPreference;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes.dex */
public class BatchDownloadUtil {
    public static final String[] PROJECTION = {j.c, "sha1", "mimeType", "thumbnailFile"};
    public static final String NOT_DOWNLOAD_PATH_ROOT = GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH;

    public static String getTableName(boolean z) {
        return z ? "shareImage" : "cloud";
    }

    public static String getFileSelection(DownloadType downloadType, boolean z) {
        String str = "";
        if (!z) {
            str = str + "(localGroupId NOT IN (SELECT _id FROM album WHERE localPath LIKE 'android/data%' )) AND ";
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$sdk$download$DownloadType[downloadType.ordinal()];
        if (i == 1) {
            str = str + String.format("(%s is null) AND ", "thumbnailFile");
        } else if (i != 2) {
            return str;
        }
        return str + String.format("(%s is null) ", "localFile");
    }

    /* renamed from: com.miui.gallery.cloud.download.BatchDownloadUtil$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$sdk$download$DownloadType;

        static {
            int[] iArr = new int[DownloadType.values().length];
            $SwitchMap$com$miui$gallery$sdk$download$DownloadType = iArr;
            try {
                iArr[DownloadType.THUMBNAIL_BATCH.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$sdk$download$DownloadType[DownloadType.ORIGIN_BATCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static Uri getUri(boolean z) {
        return z ? GalleryContract.ShareImage.SHARE_URI : GalleryContract.Cloud.CLOUD_URI;
    }

    public static List<BatchDownloadManager.BatchItem> queryDownload(Context context, DownloadType downloadType, boolean z, int i) {
        LinkedList linkedList = new LinkedList();
        Uri uri = getUri(z);
        String format = i > 0 ? String.format(Locale.US, "SELECT_id FROM %s WHERE (localFlag = 0 AND serverStatus = 'custom') AND %s LIMIT %s", getTableName(z), getFileSelection(downloadType, z), Integer.valueOf(i)) : String.format(Locale.US, "(localFlag = 0 AND serverStatus = 'custom') AND %s", getFileSelection(downloadType, z));
        Cursor cursor = null;
        try {
            try {
                cursor = context.getContentResolver().query(uri, PROJECTION, format, null, "serverTag DESC");
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        DownloadType downloadType2 = DownloadType.THUMBNAIL_BATCH;
                        if (downloadType != downloadType2 || !ThumbnailPreference.containsThumbnailKey(cursor.getString(1))) {
                            if (downloadType != DownloadType.ORIGIN_BATCH || !IncompatibleMediaType.isUnsupportedMediaType(cursor.getString(2))) {
                                downloadType2 = downloadType;
                            } else if (!cursor.isNull(3)) {
                            }
                            linkedList.add(new BatchDownloadManager.BatchItem(ContentUris.withAppendedId(uri, cursor.getLong(0)), downloadType2));
                        }
                    }
                }
            } catch (Exception e) {
                DefaultLogger.e("BatchDownloadUtil", e);
            }
            return linkedList;
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    public static void cleanDownloadedMark(Context context) {
        DownloadType downloadType = DownloadType.THUMBNAIL_BATCH;
        cleanDownloadedMark(context, downloadType, false);
        DownloadType downloadType2 = DownloadType.ORIGIN_BATCH;
        cleanDownloadedMark(context, downloadType2, false);
        cleanDownloadedMark(context, downloadType, true);
        cleanDownloadedMark(context, downloadType2, true);
    }

    public static void cleanDownloadedMark(Context context, DownloadType downloadType, boolean z) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$sdk$download$DownloadType[downloadType.ordinal()];
        String str = i != 1 ? i != 2 ? null : "localFile" : "thumbnailFile";
        if (str != null) {
            ContentValues contentValues = new ContentValues();
            contentValues.putNull(str);
            DefaultLogger.d("BatchDownloadUtil", "clean %s, result %d", str, Integer.valueOf(context.getContentResolver().update(getUri(z), contentValues, String.format(Locale.US, "%s=''", str), null)));
        }
    }
}
