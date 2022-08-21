package com.miui.gallery.scanner.core.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.text.TextUtils;
import androidx.sqlite.db.CursorSpec;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.scanner.core.ScanContracts$SQL;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class OwnerAlbumEntry extends OwnerEntry implements IAlbumEntry {
    public boolean isOnlyLinkFolder;
    public boolean isShareAlbum;
    public String mAlbumName;
    public int mAttributes;
    public Map<String, OwnerItemEntry> mContents;
    public String mEditedColumns = "";
    public String mLocalPath;
    public int mPublicMediaCount;
    public long mPublicMediaGenerationModifiedSum;
    public String mServerID;

    public static boolean isSyncable(int i) {
        return (((long) i) & 1) != 0;
    }

    public void updatePublicMediaStatus(Context context) {
    }

    @Override // com.miui.gallery.scanner.core.model.OwnerEntry
    public boolean isLatest(BasicFileAttributes basicFileAttributes) {
        String[] absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), StorageUtils.ensureCommonRelativePath(this.mLocalPath));
        if (absolutePath == null || absolutePath.length == 0 || absolutePath.length > 2) {
            return false;
        }
        long j = 0;
        for (String str : absolutePath) {
            boolean isInPrimaryStorage = StorageUtils.isInPrimaryStorage(str);
            long abs = Math.abs(Long.hashCode(new File(str).lastModified()));
            j = isInPrimaryStorage ? j + abs : j + (abs << 32);
        }
        return this.mDateModified == j;
    }

    public static OwnerAlbumEntry fromLocalPath(Context context, String str) {
        Cursor query;
        Cursor cursor = null;
        try {
            query = context.getContentResolver().query(GalleryContract.Album.URI, ScanContracts$SQL.ALBUM_PROJECTION, "localPath=? COLLATE NOCASE AND (localFlag IN (7,8) OR (localFlag IN (2,10,0) AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{str}, null);
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (query == null) {
                throw new IllegalStateException("query album cursor null");
            }
            if (!query.moveToFirst()) {
                BaseMiscUtil.closeSilently(query);
                return null;
            }
            OwnerAlbumEntry fromCursor = fromCursor(query);
            BaseMiscUtil.closeSilently(query);
            return fromCursor;
        } catch (Throwable th2) {
            th = th2;
            cursor = query;
            BaseMiscUtil.closeSilently(cursor);
            throw th;
        }
    }

    public static OwnerAlbumEntry fromServerId(Context context, long j) {
        Cursor query;
        Cursor cursor = null;
        try {
            query = context.getContentResolver().query(GalleryContract.Album.URI, ScanContracts$SQL.ALBUM_PROJECTION, "serverId=?", new String[]{String.valueOf(j)}, null);
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (query == null) {
                throw new IllegalStateException("query album cursor null");
            }
            if (!query.moveToFirst()) {
                BaseMiscUtil.closeSilently(query);
                return null;
            }
            OwnerAlbumEntry fromCursor = fromCursor(query);
            BaseMiscUtil.closeSilently(query);
            return fromCursor;
        } catch (Throwable th2) {
            th = th2;
            cursor = query;
            BaseMiscUtil.closeSilently(cursor);
            throw th;
        }
    }

    public static OwnerAlbumEntry fromName(Context context, String str) {
        Cursor query;
        Cursor cursor = null;
        try {
            query = context.getContentResolver().query(GalleryContract.Album.URI, ScanContracts$SQL.ALBUM_PROJECTION, "name = ? COLLATE NOCASE AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{str}, null);
        } catch (Throwable th) {
            th = th;
        }
        try {
            if (query == null) {
                throw new IllegalStateException("query album cursor null");
            }
            if (!query.moveToFirst()) {
                BaseMiscUtil.closeSilently(query);
                return null;
            }
            OwnerAlbumEntry fromCursor = fromCursor(query);
            BaseMiscUtil.closeSilently(query);
            return fromCursor;
        } catch (Throwable th2) {
            th = th2;
            cursor = query;
            BaseMiscUtil.closeSilently(cursor);
            throw th;
        }
    }

    public static Map<String, OwnerAlbumEntry> fromDirectoryPath(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        OwnerAlbumEntry ownerAlbumEntry;
        Map<String, OwnerAlbumEntry> queryAlbum = queryAlbum(context, path, scanTaskConfig.isRecursiveScan());
        if (queryAlbum.size() <= 0) {
            return Collections.emptyMap();
        }
        ArrayList arrayList = new ArrayList(queryAlbum.size());
        HashMap hashMap = new HashMap(queryAlbum.size());
        for (OwnerAlbumEntry ownerAlbumEntry2 : queryAlbum.values()) {
            hashMap.put(Long.valueOf(ownerAlbumEntry2.mId), ownerAlbumEntry2);
            arrayList.add(Long.valueOf(ownerAlbumEntry2.mId));
        }
        Cursor query = GalleryDBHelper.getInstance().getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("cloud").columns(OwnerItemEntry.FILE_ENTRY_PROJECTION).selection(String.format("localGroupId IN (%s) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", TextUtils.join(",", arrayList)), null).create(), CursorSpec.FORWARD_ONLY);
        while (query != null) {
            try {
                if (!query.moveToNext()) {
                    break;
                }
                OwnerItemEntry fromCursor = OwnerItemEntry.fromCursor(query);
                if (fromCursor != null && (ownerAlbumEntry = (OwnerAlbumEntry) hashMap.get(Long.valueOf(fromCursor.mLocalGroupId))) != null) {
                    ownerAlbumEntry.mContents.put(fromCursor.mPath.toLowerCase(), fromCursor);
                }
            } catch (Throwable th) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return queryAlbum;
    }

    public static Map<String, OwnerAlbumEntry> queryAlbum(Context context, Path path, boolean z) {
        HashMap hashMap = new HashMap();
        String relativePath = StorageUtils.getRelativePath(context, path.toString());
        if (z && GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH.equals(relativePath)) {
            relativePath = "%";
        }
        String volumePath = StorageUtils.getVolumePath(context, path.toString());
        Cursor query = context.getContentResolver().query(GalleryContract.Album.URI, ScanContracts$SQL.ALBUM_PROJECTION, z ? "(localPath LIKE ? COLLATE NOCASE) AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))" : "(localPath=? COLLATE NOCASE) AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{relativePath}, null);
        while (query != null) {
            try {
                if (!query.moveToNext()) {
                    break;
                }
                String string = query.getString(8);
                if (GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH.equals(string)) {
                    hashMap.put(volumePath.toLowerCase(), fromCursor(query));
                } else {
                    hashMap.put((volumePath + h.g + string).toLowerCase(), fromCursor(query));
                }
            } catch (Throwable th) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (query != null) {
            query.close();
        }
        return hashMap;
    }

    public static OwnerAlbumEntry fromCursor(Cursor cursor) {
        OwnerAlbumEntry rOwnerAlbumEntry = Build.VERSION.SDK_INT >= 30 ? new ROwnerAlbumEntry() : new OwnerAlbumEntry();
        rOwnerAlbumEntry.mId = cursor.getLong(0);
        rOwnerAlbumEntry.mServerID = cursor.getString(1);
        rOwnerAlbumEntry.mDateModified = cursor.getLong(2);
        rOwnerAlbumEntry.mLocalFlag = cursor.getInt(3);
        rOwnerAlbumEntry.mServerStatus = cursor.getString(4);
        rOwnerAlbumEntry.mAlbumName = cursor.getString(5);
        rOwnerAlbumEntry.mAttributes = cursor.getInt(6);
        rOwnerAlbumEntry.mEditedColumns = cursor.getString(7);
        rOwnerAlbumEntry.mLocalPath = cursor.getString(8);
        rOwnerAlbumEntry.mPublicMediaCount = cursor.getInt(9);
        rOwnerAlbumEntry.mPublicMediaGenerationModifiedSum = cursor.getInt(10);
        rOwnerAlbumEntry.mContents = new HashMap();
        return rOwnerAlbumEntry;
    }

    public boolean isSyncable() {
        return (((long) this.mAttributes) & 1) != 0;
    }

    public boolean isDeletedAlbum() {
        int i = this.mLocalFlag;
        return i == -1 || i == 2;
    }

    public Map<String, OwnerItemEntry> getContents() {
        return this.mContents;
    }

    public void updateDateModified(Context context, long j, long j2, boolean z, boolean z2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("dateModified", Long.valueOf(j));
        long calcRealDateModified = calcRealDateModified(this.mDateModified, j2, z, z2);
        contentValues.put("realDateModified", Long.valueOf(calcRealDateModified));
        SafeDBUtil.safeUpdate(context, GalleryContract.Album.URI, contentValues, "_id=?", new String[]{String.valueOf(this.mId)});
        DefaultLogger.d("OwnerAlbumEntry", "update album [%s] real date modified from [%d] to [%d].", this.mLocalPath, Long.valueOf(this.mDateModified), Long.valueOf(calcRealDateModified));
    }

    public final long calcRealDateModified(long j, long j2, boolean z, boolean z2) {
        long j3 = (int) j;
        long j4 = z2 ? 0L : j >>> 32;
        long abs = Math.abs(Long.hashCode(j2));
        return z ? (j4 << 32) + abs : (abs << 32) + j3;
    }
}
