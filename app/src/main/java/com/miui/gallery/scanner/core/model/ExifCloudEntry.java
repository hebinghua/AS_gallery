package com.miui.gallery.scanner.core.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.scanner.core.ScanContracts$SQL;
import com.miui.gallery.util.SafeDBUtil;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ExifCloudEntry {
    public static final SafeDBUtil.QueryHandler<ArrayList<ExifCloudEntry>> sFindEntryHandler = ExifCloudEntry$$ExternalSyntheticLambda0.INSTANCE;
    public long mDateTaken;
    public long mID;
    public int mLocalFlag;
    public long mLocalGroupId;
    public String mLocalPath;
    public String mName;
    public String mServerId;
    public String mServerStatus;
    public String mSha1;
    public long mSize;
    public String mThumbnailPath;

    public static /* synthetic */ ArrayList lambda$static$0(Cursor cursor) {
        if (cursor == null) {
            throw new IllegalStateException("query cursor is null");
        }
        ArrayList arrayList = new ArrayList(cursor.getCount());
        while (cursor.moveToNext()) {
            arrayList.add(fromCursor(cursor));
        }
        return arrayList;
    }

    public static ExifCloudEntry fromCursor(Cursor cursor) {
        ExifCloudEntry exifCloudEntry = new ExifCloudEntry();
        exifCloudEntry.mID = cursor.getLong(0);
        exifCloudEntry.mSize = cursor.getLong(1);
        exifCloudEntry.mName = cursor.getString(2);
        exifCloudEntry.mServerStatus = cursor.getString(3);
        exifCloudEntry.mLocalFlag = cursor.getInt(4);
        exifCloudEntry.mSha1 = cursor.getString(5);
        exifCloudEntry.mLocalPath = cursor.getString(6);
        exifCloudEntry.mThumbnailPath = cursor.getString(7);
        exifCloudEntry.mServerId = cursor.getString(8);
        exifCloudEntry.mDateTaken = cursor.getLong(9);
        exifCloudEntry.mLocalGroupId = cursor.getLong(10);
        return exifCloudEntry;
    }

    public static ArrayList<ExifCloudEntry> findEntry(Context context, String str, String str2, long j) {
        SupportSQLiteDatabase readableDatabase = GalleryDBHelper.getInstance().getReadableDatabase();
        if (readableDatabase != null) {
            return (ArrayList) SafeDBUtil.safeQuery(readableDatabase, getReadTableName(j), ScanContracts$SQL.CLOUD_PROJECTION, "(localGroupId=? OR localGroupId IS NULL  OR localGroupId = -1 )  AND (title = ? COLLATE NOCASE OR localFile = ? COLLATE NOCASE OR thumbnailFile = ? COLLATE NOCASE) AND (serverType=1 OR serverType=2) AND (serverStatus is null OR serverStatus='custom' OR serverStatus='recovery' OR serverStatus='cleanLocal' OR serverStatus='temp') AND localFlag <> 15", new String[]{String.valueOf(j), str, str2, str2}, (String) null, sFindEntryHandler);
        }
        return (ArrayList) SafeDBUtil.safeQuery(context, getReadUri(j), ScanContracts$SQL.CLOUD_PROJECTION, "(localGroupId=? OR localGroupId IS NULL  OR localGroupId = -1 )  AND (title = ? COLLATE NOCASE OR localFile = ? COLLATE NOCASE OR thumbnailFile = ? COLLATE NOCASE) AND (serverType=1 OR serverType=2) AND (serverStatus is null OR serverStatus='custom' OR serverStatus='recovery' OR serverStatus='cleanLocal' OR serverStatus='temp') AND localFlag <> 15", new String[]{String.valueOf(j), str, str2, str2}, (String) null, sFindEntryHandler);
    }

    public static ArrayList<ExifCloudEntry> findEntry(Context context, String str, long j, String str2, long j2) {
        SupportSQLiteDatabase readableDatabase = GalleryDBHelper.getInstance().getReadableDatabase();
        if (readableDatabase != null) {
            return (ArrayList) SafeDBUtil.safeQuery(readableDatabase, getReadTableName(j2), ScanContracts$SQL.CLOUD_PROJECTION, "(localGroupId=? OR localGroupId IS NULL  OR localGroupId = -1 )  AND (title = ? COLLATE NOCASE OR size = ? OR sha1 = ?) AND (serverType=1 OR serverType=2) AND (serverStatus is null OR serverStatus='custom' OR serverStatus='recovery' OR serverStatus='cleanLocal' OR serverStatus='temp') AND localFlag <> 15", new String[]{String.valueOf(j2), str, String.valueOf(j), str2}, (String) null, sFindEntryHandler);
        }
        return (ArrayList) SafeDBUtil.safeQuery(context, getReadUri(j2), ScanContracts$SQL.CLOUD_PROJECTION, "(localGroupId=? OR localGroupId IS NULL  OR localGroupId = -1 )  AND (title = ? COLLATE NOCASE OR size = ? OR sha1 = ?) AND (serverType=1 OR serverType=2) AND (serverStatus is null OR serverStatus='custom' OR serverStatus='recovery' OR serverStatus='cleanLocal' OR serverStatus='temp') AND localFlag <> 15", new String[]{String.valueOf(j2), str, String.valueOf(j), str2}, (String) null, sFindEntryHandler);
    }

    public static String getReadTableName(long j) {
        return ShareAlbumHelper.isOtherShareAlbumId(j) ? "shareImage" : "cloud";
    }

    public static Uri getReadUri(long j) {
        if (ShareAlbumHelper.isOtherShareAlbumId(j)) {
            return GalleryContract.ShareImage.SHARE_URI;
        }
        return GalleryContract.Cloud.CLOUD_URI;
    }

    public boolean hasSynced() {
        return this.mLocalFlag == 0 || !TextUtils.isEmpty(this.mServerStatus);
    }
}
