package com.miui.gallery.provider.deprecated;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContentResolver;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryCloudProvider extends ContentProvider {
    public static final String[] SAFE_INSERT_ALBUM_PROJECTION;
    public static final String[] SAFE_INSERT_PROJECTION;
    public static final Uri SYNC_INFO_URI;
    public static final Uri SYNC_SETTINGS_URI;
    public static final Uri UPLOAD_STATE_URI;
    public static GalleryDBHelper sDBHelper;
    public static final UriMatcher sUriMatcher;
    public ContentResolver mContentResolver;
    public int mIsUploading = 0;
    public int mHasPendingItem = 0;
    public MediaManager mMediaManager = null;

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    static {
        Uri uri = GalleryContract.AUTHORITY_URI;
        UPLOAD_STATE_URI = uri.buildUpon().appendPath("uploadState").build();
        SYNC_SETTINGS_URI = uri.buildUpon().appendPath("syncSettings").build();
        SYNC_INFO_URI = uri.buildUpon().appendPath("syncInfo").build();
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud", 1);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloud/#", 2);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudSetting", 3);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudSetting/#", 4);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareAlbum", 5);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareAlbum/#", 6);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareUser", 7);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareUser/#", 8);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareImage", 9);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareImage/#", 10);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudUser", 11);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudUser/#", 12);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudCache", 13);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "cloudCache/#", 14);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "userInfo", 17);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "userInfo/#", 18);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "ownerSubUbifocus", 19);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "ownerSubUbifocus/#", 20);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareSubUbifocus", 21);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "shareSubUbifocus/#", 22);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFace", 24);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFace/#", 25);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "faceToImages", 26);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "faceToImages/#", 27);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFaceJoinFaceToImagesJoinCloud", 28);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFaceJoinFaceToImagesJoinCloud/#", 29);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFaceJoinFaceToImages", 38);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleFaceJoinFaceToImages/#", 39);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "uploadState", 23);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleRecommend", 30);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "peopleRecommend/#", 31);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "syncInfo", 40);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "syncSettings", 41);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "discoveryMessage", 43);
        uriMatcher.addURI("com.miui.gallery.cloud.provider", "recentDiscoveredMedia", 42);
        SAFE_INSERT_PROJECTION = new String[]{j.c, "fileName", "serverId"};
        SAFE_INSERT_ALBUM_PROJECTION = new String[]{j.c, "name", "serverId"};
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        sDBHelper = GalleryDBHelper.getInstance(getContext());
        this.mContentResolver = new ContentResolver();
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Cursor queryNonDBData = queryNonDBData(uri, strArr, str, strArr2, str2);
        if (queryNonDBData != null) {
            return queryNonDBData;
        }
        Cursor querySyncInfo = querySyncInfo(uri, strArr, str, strArr2, str2);
        if (querySyncInfo != null) {
            return querySyncInfo;
        }
        long idFromString = getIdFromString(uri.getLastPathSegment());
        String limit = UriUtil.getLimit(uri);
        String groupBy = UriUtil.getGroupBy(uri);
        String having = UriUtil.getHaving(uri);
        String matchTableName = matchTableName(uri);
        if (!matchTableName.equalsIgnoreCase("peopleFace join FaceToImages join cloud")) {
            matchTableName = matchTableName + " as master";
        }
        SupportSQLiteQueryBuilder limit2 = SupportSQLiteQueryBuilder.builder(matchTableName).columns(strArr).selection(appendIdSelection(str, idFromString), strArr2).groupBy(groupBy).having(having).orderBy(str2).limit(limit);
        if (UriUtil.getDistinct(uri)) {
            limit2.distinct();
        }
        return sDBHelper.getReadableDatabase().query(limit2.create());
    }

    public final String appendIdSelection(String str, long j) {
        String str2;
        if (j > 0) {
            str2 = "_id = " + j;
        } else {
            str2 = null;
        }
        if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
            if (TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                return str2;
            }
            if (!TextUtils.isEmpty(str) && TextUtils.isEmpty(str2)) {
                return str;
            }
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                return null;
            }
            return "(" + str + " ) AND " + str2;
        }
        return null;
    }

    public static long getIdFromString(String str) {
        if (str != null) {
            try {
                return Long.valueOf(str).longValue();
            } catch (NumberFormatException unused) {
                return -1L;
            }
        }
        return -1L;
    }

    public long onPreInsert(SupportSQLiteDatabase supportSQLiteDatabase, String str, ContentValues contentValues) {
        long j;
        String str2;
        String[] strArr;
        String str3;
        String[] strArr2;
        Cursor cursor = null;
        try {
            if ("cloud".equals(str)) {
                Integer asInteger = contentValues.getAsInteger("serverType");
                if (asInteger != null && (asInteger.intValue() == 1 || asInteger.intValue() == 2)) {
                    String asString = contentValues.getAsString("sha1");
                    String asString2 = contentValues.getAsString("localGroupId");
                    String asString3 = contentValues.getAsString("thumbnailFile");
                    String asString4 = contentValues.getAsString("fileName");
                    String asString5 = contentValues.getAsString("serverId");
                    if (!TextUtils.isEmpty(asString) && !TextUtils.isEmpty(asString2)) {
                        if (TextUtils.isEmpty(asString3) && !TextUtils.isEmpty(asString4)) {
                            if (TextUtils.isEmpty(asString5)) {
                                str3 = "localGroupId=? AND fileName=? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
                                strArr2 = new String[]{asString2, asString4};
                            } else {
                                str3 = "localGroupId=? AND (serverId IS NULL OR serverId=?) AND fileName=? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
                                strArr2 = new String[]{asString2, asString5, asString4};
                            }
                            cursor = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(SAFE_INSERT_PROJECTION).selection(str3, strArr2).create());
                        }
                        if (TextUtils.isEmpty(asString5)) {
                            str2 = "sha1=? AND localGroupId=? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
                            strArr = new String[]{asString, asString2};
                        } else {
                            str2 = "sha1=? AND localGroupId=? AND (serverId IS NULL OR serverId=?) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
                            strArr = new String[]{asString, asString2, asString5};
                        }
                        str3 = str2;
                        strArr2 = strArr;
                        cursor = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(SAFE_INSERT_PROJECTION).selection(str3, strArr2).create());
                    }
                }
            } else if ("album".equals(str)) {
                String asString6 = contentValues.getAsString("localPath");
                if (!TextUtils.isEmpty(asString6)) {
                    cursor = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("album").columns(SAFE_INSERT_ALBUM_PROJECTION).selection("localPath like ? AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", new String[]{asString6}).create());
                }
            }
            if (cursor != null && cursor.moveToFirst()) {
                String string = cursor.getString(2);
                String asString7 = contentValues.getAsString("serverStatus");
                if (TextUtils.isEmpty(string) && TextUtils.equals(asString7, "deleted")) {
                    DefaultLogger.w("GalleryCloudProvider", "do not merge delete item with local");
                } else {
                    j = cursor.getLong(0);
                    String string2 = cursor.getString(1);
                    DefaultLogger.w("GalleryCloudProvider", "item conflict in onPreInsert %s", string2);
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", contentValues.getAsString("fileName") + "_" + string2);
                    SamplingStatHelper.recordCountEvent("Sync", "sync_insert_transaction", hashMap);
                    return j;
                }
            }
            j = -1;
            return j;
        } finally {
            BaseMiscUtil.closeSilently(null);
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (sDBHelper != null && !insertNonDBData(uri, contentValues) && !insertSyncInfo(uri, contentValues)) {
            String matchTableName = matchTableName(uri);
            SupportSQLiteDatabase writableDatabase = sDBHelper.getWritableDatabase();
            writableDatabase.beginTransactionNonExclusive();
            boolean isAlbumTable = isAlbumTable(matchTableName);
            try {
                long onPreInsert = onPreInsert(writableDatabase, matchTableName, contentValues);
                if (onPreInsert == -1) {
                    validateInsertValues(matchTableName, contentValues);
                    if ("cloud".equals(matchTableName) || "shareImage".equals(matchTableName)) {
                        appendValuesForCloud(contentValues, true);
                    }
                    long insert = writableDatabase.insert(matchTableName, 0, contentValues);
                    if (insert == -1) {
                        return null;
                    }
                    if ("cloud".equals(matchTableName)) {
                        this.mMediaManager.insert(insert, contentValues);
                    } else if ("shareImage".equals(matchTableName)) {
                        ShareMediaManager.getInstance().insert(insert, contentValues);
                    } else if (isAlbumTable) {
                        internalInsertAlbumToCache(insert, matchTableName, contentValues);
                    }
                    onPreInsert = insert;
                } else {
                    update(uri, contentValues, j.c + "=?", new String[]{String.valueOf(onPreInsert)});
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
                if (onPreInsert == -1) {
                    return null;
                }
                Uri build = uri.buildUpon().appendPath(String.valueOf(onPreInsert)).build();
                this.mContentResolver.notifyChangeDelay(build, null, 0L);
                return build;
            } finally {
                writableDatabase.endTransaction();
            }
        }
        return null;
    }

    public final void internalInsertAlbumToCache(long j, String str, ContentValues contentValues) {
        boolean equals = "shareAlbum".equals(str);
        if (equals) {
            j = ShareAlbumHelper.getUniformAlbumId(j);
        }
        if (equals && !contentValues.containsKey("attributes")) {
            contentValues.put("attributes", (Long) 0L);
        }
        AlbumCacheManager.getInstance().insert(j, contentValues);
    }

    public final boolean isAlbumTable(String str) {
        return "shareAlbum".equals(str) || "album".equals(str);
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        if (sDBHelper != null && !deleteNonDBData(uri, str, strArr) && !deleteSyncInfo(uri, str, strArr)) {
            String matchTableName = matchTableName(uri);
            if ("cloud".equals(matchTableName)) {
                this.mMediaManager.delete(genIDSelection(str, strArr, false), null);
            } else if (isAlbumTable(matchTableName)) {
                AlbumCacheManager.getInstance().delete(genAlbumIDSelection(str, strArr, "shareAlbum".equals(matchTableName)), null);
            }
            int delete = sDBHelper.getWritableDatabase().delete(matchTableName, str, strArr);
            if (delete > 0) {
                this.mContentResolver.notifyChange(uri, null, 0L);
            }
            return delete;
        }
        return 0;
    }

    public boolean isMediaItem(ContentValues contentValues) {
        Integer asInteger;
        if (contentValues == null || (asInteger = contentValues.getAsInteger("serverType")) == null) {
            return false;
        }
        return Numbers.equals(asInteger, 1) || Numbers.equals(asInteger, 2);
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        if (sDBHelper != null && !updateNonDBData(uri, contentValues, str, strArr) && !updateSyncInfo(uri, contentValues, str, strArr)) {
            String matchTableName = matchTableName(uri);
            String genIDSelection = "cloud".equals(matchTableName) ? genIDSelection(str, strArr, false) : null;
            validateUpdateValues(matchTableName, contentValues);
            if ("cloud".equals(matchTableName) || "shareImage".equals(matchTableName)) {
                appendValuesForCloud(contentValues, false);
            }
            int update = sDBHelper.getWritableDatabase().update(matchTableName, 0, contentValues, str, strArr);
            if (!TextUtils.isEmpty(genIDSelection) && this.mMediaManager.update(genIDSelection, null, contentValues) != update && isMediaItem(contentValues) && !this.mMediaManager.isItemDeleted(contentValues)) {
                this.mMediaManager.insert(sDBHelper.getWritableDatabase(), str, strArr);
            }
            if (update > 0 && needNotifyUpdate(uri, contentValues)) {
                this.mContentResolver.notifyChangeDelay(uri, null, 0L);
            }
            return update;
        }
        return 0;
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        Uri uri;
        if ("raw_update".equals(str)) {
            String string = bundle.getString("statement");
            if (TextUtils.isEmpty(string)) {
                return null;
            }
            boolean execSQL = sDBHelper.execSQL(string);
            Bundle bundle2 = new Bundle();
            bundle2.putBoolean("bool_result", execSQL);
            if (execSQL && (uri = (Uri) bundle.getParcelable("notify_uri")) != null) {
                this.mContentResolver.notifyChangeDelay(uri, null, 0L);
            }
            return bundle2;
        }
        return super.call(str, str2, bundle);
    }

    public boolean needNotifyUpdate(Uri uri, ContentValues contentValues) {
        if (contentValues == null || (!contentValues.containsKey("localFlag") && !contentValues.containsKey("serverStatus") && !contentValues.containsKey("specialTypeFlags"))) {
            return uri.getBooleanQueryParameter("requireNotifyUri", false);
        }
        return true;
    }

    public ContentValues appendValuesForCloud(ContentValues contentValues, boolean z) {
        String gePackageNameForScreenshot = PackageUtils.gePackageNameForScreenshot(contentValues.getAsString("fileName"));
        if (!TextUtils.isEmpty(gePackageNameForScreenshot)) {
            contentValues.put("source_pkg", gePackageNameForScreenshot);
        } else if (z && TextUtils.isEmpty(contentValues.getAsString("location"))) {
            LocationManager.getInstance().appendDefaultLocationValues(contentValues);
        }
        return contentValues;
    }

    public static void validateInsertValues(String str, ContentValues contentValues) {
        if (!TextUtils.equals("userInfo", str) || contentValues.containsKey("date_modified")) {
            return;
        }
        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
    }

    public static void validateUpdateValues(String str, ContentValues contentValues) {
        if (!TextUtils.equals("userInfo", str) || contentValues.containsKey("date_modified")) {
            return;
        }
        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis()));
    }

    public static Cursor rawQuery(String str, String[] strArr) {
        GalleryDBHelper galleryDBHelper = sDBHelper;
        if (galleryDBHelper == null) {
            return null;
        }
        return galleryDBHelper.getWritableDatabase().query(str);
    }

    public static boolean isCloudUri(Uri uri) {
        return uri.toString().startsWith(GalleryContract.AUTHORITY_URI.toString());
    }

    public static String matchTableName(Uri uri) {
        int match = sUriMatcher.match(uri);
        if (match == 38 || match == 39) {
            return "peopleFace join faceToImages";
        }
        if (match == 42) {
            return "recentDiscoveredMedia";
        }
        if (match == 43) {
            return "discoveryMessage";
        }
        switch (match) {
            case 1:
            case 2:
                return "cloud";
            case 3:
            case 4:
                return "cloudSetting";
            case 5:
            case 6:
                return "shareAlbum";
            case 7:
            case 8:
                return "shareUser";
            case 9:
            case 10:
                return "shareImage";
            case 11:
            case 12:
                return "cloudUser";
            case 13:
            case 14:
                return "cloudCache";
            default:
                switch (match) {
                    case 17:
                    case 18:
                        return "userInfo";
                    case 19:
                    case 20:
                        return "ownerSubUbifocus";
                    case 21:
                    case 22:
                        return "shareSubUbifocus";
                    case 23:
                        return "uploadState";
                    case 24:
                    case 25:
                        return "peopleFace";
                    case 26:
                    case 27:
                        return "faceToImages";
                    case 28:
                    case 29:
                        return "peopleFace join FaceToImages join cloud";
                    case 30:
                    case 31:
                        return "peopleRecommend";
                    default:
                        DefaultLogger.d("GalleryCloudProvider", "match table name, uri is not cloud");
                        return null;
                }
        }
    }

    public final Cursor queryNonDBData(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        int match = sUriMatcher.match(uri);
        if (match == 23) {
            MatrixCursor matrixCursor = new MatrixCursor(new String[]{"is_upload", "has_pending_item"}, 1);
            SyncLogger.d("GalleryCloudProvider", "query upload state isUploading[%d], hasPendingItem[%d]", Integer.valueOf(this.mIsUploading), Integer.valueOf(this.mHasPendingItem));
            matrixCursor.addRow(new Object[]{Integer.valueOf(this.mIsUploading), Integer.valueOf(this.mHasPendingItem)});
            return matrixCursor;
        } else if (match != 41) {
            return null;
        } else {
            MatrixCursor matrixCursor2 = new MatrixCursor(new String[]{"backupOnlyInWifi"}, 1);
            boolean backupOnlyInWifi = GalleryPreferences.Sync.getBackupOnlyInWifi();
            SyncLogger.d("GalleryCloudProvider", "query backup only wifi [%s]", Boolean.valueOf(backupOnlyInWifi));
            matrixCursor2.addRow(new Object[]{Integer.valueOf(backupOnlyInWifi ? 1 : 0)});
            return matrixCursor2;
        }
    }

    public final boolean updateNonDBData(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        if (sUriMatcher.match(uri) == 23) {
            Boolean asBoolean = contentValues.getAsBoolean("is_upload");
            Boolean asBoolean2 = contentValues.getAsBoolean("has_pending_item");
            if (asBoolean != null) {
                if (!asBoolean.booleanValue()) {
                    this.mIsUploading = 0;
                } else {
                    this.mIsUploading = 1;
                }
            }
            if (asBoolean2 != null) {
                if (asBoolean2.booleanValue()) {
                    this.mHasPendingItem = 1;
                } else {
                    this.mHasPendingItem = 0;
                }
            }
            this.mContentResolver.notifyChange(uri, null, 0L);
            return true;
        }
        return false;
    }

    public final boolean deleteNonDBData(Uri uri, String str, String[] strArr) {
        return sUriMatcher.match(uri) == 23;
    }

    public final Cursor querySyncInfo(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (sUriMatcher.match(uri) == 40) {
            Locale locale = Locale.US;
            String format = String.format(locale, "(%s = %d OR %s = %d) AND (%s = %s OR %s = %s) AND ((serverType = 1 AND size < %s) OR (serverType = 2 AND size < %s))", "localFlag", 7, "localFlag", 8, "serverType", 1, "serverType", 2, Long.valueOf(CloudUtils.getMaxImageSizeLimit()), Long.valueOf(CloudUtils.getMaxVideoSizeLimit()));
            String format2 = String.format(locale, "%s = '%s' AND %s = %d AND (%s = %s OR %s = %s) ", "serverStatus", "custom", "localFlag", 0, "serverType", 1, "serverType", 2);
            String[] strArr3 = strArr == null ? new String[]{"syncableCount"} : strArr;
            Integer[] numArr = new Integer[strArr3.length];
            int i = -1;
            int i2 = -1;
            for (int i3 = 0; i3 < strArr3.length; i3++) {
                if (TextUtils.equals(strArr3[i3], "syncableCount")) {
                    if (i == -1) {
                        i = queryDirtyMediaCount() + getMediaCount("shareImage", format);
                    }
                    numArr[i3] = Integer.valueOf(i);
                } else if (TextUtils.equals(strArr3[i3], "syncedCount")) {
                    if (i2 == -1) {
                        i2 = getMediaCount("cloud", format2) + getMediaCount("shareImage", format2);
                    }
                    numArr[i3] = Integer.valueOf(i2);
                } else {
                    throw new IllegalArgumentException("unsupported argument: " + strArr3[i3]);
                }
            }
            SyncLogger.d("GalleryCloudProvider", "query syncInfo syncableCount[%d], syncedCount[%d]", Integer.valueOf(i), Integer.valueOf(i2));
            MatrixCursor matrixCursor = new MatrixCursor(strArr3, 1);
            matrixCursor.addRow(numArr);
            return matrixCursor;
        }
        return null;
    }

    public final int queryDirtyMediaCount() {
        Locale locale = Locale.US;
        return getMediaCount("cloud", String.format(locale, "(%s) AND (%s = %s OR %s = %s) AND (%s = %s OR %s = %s)", CloudUtils.SELECTION_OWNER_NEED_SYNC, "serverType", 1, "serverType", 2, "localFlag", 7, "localFlag", 8)) - getMediaCount("cloud", String.format(locale, "((serverType = 1 AND size > %s) OR (serverType = 2 AND size > %s))", Long.valueOf(CloudUtils.getMaxImageSizeLimit()), Long.valueOf(CloudUtils.getMaxVideoSizeLimit())));
    }

    public final int getMediaCount(String str, String str2) {
        Cursor cursor = null;
        try {
            cursor = sDBHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder(str).columns(new String[]{"count(_id)"}).selection(str2, null).create());
            if (cursor == null || !cursor.moveToNext()) {
                return 0;
            }
            int i = cursor.getInt(0);
            cursor.close();
            return i;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public final boolean updateSyncInfo(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        if (sUriMatcher.match(uri) == 40) {
            DefaultLogger.w("GalleryCloudProvider", "operation is not supported!");
            return true;
        }
        return false;
    }

    public final boolean deleteSyncInfo(Uri uri, String str, String[] strArr) {
        if (sUriMatcher.match(uri) == 40) {
            DefaultLogger.w("GalleryCloudProvider", "operation is not supported!");
            return true;
        }
        return false;
    }

    public final boolean insertSyncInfo(Uri uri, ContentValues contentValues) {
        if (sUriMatcher.match(uri) == 40) {
            DefaultLogger.w("GalleryCloudProvider", "operation is not supported!");
            return true;
        }
        return false;
    }

    public final String genAlbumIDSelection(String str, String[] strArr, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(j.c);
        sb.append(" IN (");
        List<Long> idsSelection = getIdsSelection(z ? "shareAlbum" : "album", j.c, str, strArr);
        if (z) {
            for (int i = 0; i < idsSelection.size(); i++) {
                idsSelection.set(i, Long.valueOf(ShareAlbumHelper.getUniformAlbumId(idsSelection.get(i).longValue())));
            }
        }
        sb.append(TextUtils.join(",", idsSelection));
        sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        return sb.toString();
    }

    public final String genIDSelection(String str, String[] strArr, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(j.c);
        sb.append(" IN (");
        sb.append(TextUtils.join(",", getIdsSelection(z ? "shareImage" : "cloud", j.c, str, strArr)));
        sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
        return sb.toString();
    }

    public final List<Long> getIdsSelection(String str, String str2, String str3, String[] strArr) {
        Cursor query = sDBHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder(str).columns(new String[]{str2}).selection(str3, strArr).create());
        if (query != null) {
            try {
                ArrayList arrayList = new ArrayList();
                while (query.moveToNext()) {
                    arrayList.add(Long.valueOf(query.getLong(0)));
                }
                return arrayList;
            } finally {
                query.close();
            }
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public static class AlbumInfo {
        public static final String[] PROJECTION = {j.c, "attributes"};
        public final long mAlbumId;
        public final long mAttributes;

        public AlbumInfo(long j, long j2) {
            this.mAlbumId = j;
            this.mAttributes = j2;
        }
    }

    public final AlbumInfo[] genSelectedAlbums(String str) {
        Cursor query = sDBHelper.getReadableDatabase().query(SupportSQLiteQueryBuilder.builder("album").columns(AlbumInfo.PROJECTION).selection(str, null).create());
        AlbumInfo[] albumInfoArr = new AlbumInfo[query == null ? 0 : query.getCount()];
        if (query != null) {
            try {
                albumInfoArr = new AlbumInfo[query.getCount()];
                while (query.moveToNext()) {
                    albumInfoArr[query.getPosition()] = new AlbumInfo(query.getLong(0), query.getLong(1));
                }
            } finally {
                query.close();
            }
        }
        return albumInfoArr;
    }

    public final void updateAttributes(SupportSQLiteDatabase supportSQLiteDatabase, String str, String str2, ContentValues contentValues) {
        AlbumInfo[] genSelectedAlbums = genSelectedAlbums(str2);
        if (genSelectedAlbums == null || genSelectedAlbums.length <= 0) {
            return;
        }
        Long asLong = contentValues.getAsLong("attributes");
        ArrayList arrayList = new ArrayList();
        for (AlbumInfo albumInfo : genSelectedAlbums) {
            AlbumCacheManager.getInstance().updateOrInsertAttributes(albumInfo.mAlbumId, asLong == null ? 0L : asLong.longValue());
            if (!contentValues.containsKey("editedColumns")) {
                Iterator<Long> it = AlbumManager.getAlbumSyncAttributes().iterator();
                while (true) {
                    if (it.hasNext()) {
                        Long next = it.next();
                        if ((asLong.longValue() & next.longValue()) != (albumInfo.mAttributes & next.longValue())) {
                            arrayList.add(Long.valueOf(albumInfo.mAlbumId));
                            break;
                        }
                    }
                }
            }
        }
        if (arrayList.size() <= 0) {
            return;
        }
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(22);
        supportSQLiteDatabase.execSQL(String.format(Locale.US, "UPDATE %s SET %s=coalesce(replace(%s, '%s', '') || '%s', '%s') WHERE %s IN (%s)", str, "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, TextUtils.join(",", arrayList)));
    }

    public final boolean insertNonDBData(Uri uri, ContentValues contentValues) {
        return sUriMatcher.match(uri) == 23;
    }

    /* loaded from: classes2.dex */
    public class ContentResolver extends GalleryContentResolver {
        public ContentResolver() {
        }

        @Override // com.miui.gallery.provider.GalleryContentResolver
        public Object matchUri(Uri uri) {
            return GalleryCloudProvider.matchTableName(uri);
        }

        @Override // com.miui.gallery.provider.GalleryContentResolver
        public void notifyInternal(Uri uri, ContentObserver contentObserver, long j, Bundle bundle) {
            int match = GalleryCloudProvider.sUriMatcher.match(uri);
            if (match != 1 && match != 2) {
                if (match != 17 && match != 18) {
                    if (match != 30 && match != 31) {
                        switch (match) {
                            case 5:
                            case 6:
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, contentObserver, false);
                                break;
                            case 7:
                            case 8:
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_SHARE_ALL, contentObserver, false);
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.ShareUser.SHARE_USER_URI, contentObserver, false);
                                break;
                            case 9:
                            case 10:
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_OTHER_ALBUM_MEDIA, contentObserver, false);
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_ALL, contentObserver, false);
                                break;
                            case 11:
                            case 12:
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, contentObserver, false);
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_SHARE_ALL, contentObserver, false);
                                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.CloudUser.CLOUD_USER_URI, contentObserver, false);
                                break;
                            default:
                                switch (match) {
                                    case 24:
                                    case 25:
                                    case 26:
                                    case 27:
                                        GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.ONE_PERSON_URI, contentObserver, false);
                                        GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.PERSONS_URI, contentObserver, false);
                                        break;
                                    default:
                                        GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(uri, contentObserver, false);
                                        break;
                                }
                        }
                    } else {
                        GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.RECOMMEND_FACES_OF_ONE_PERSON_URI, contentObserver, false);
                    }
                } else {
                    GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI_SHARE_ALL, contentObserver, false);
                }
            } else {
                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI, contentObserver, false);
                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_OWNER_ALBUM_DETAIL_MEDIA, contentObserver, false);
                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Media.URI_ALL, contentObserver, false);
                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.Album.URI, contentObserver, false);
                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.ONE_PERSON_URI, contentObserver, false);
                GalleryCloudProvider.this.getContext().getContentResolver().notifyChange(GalleryContract.PeopleFace.PERSONS_URI, contentObserver, false);
            }
            if (j != 0) {
                SyncUtil.requestSync(GalleryCloudProvider.this.getContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(j).setDelayUpload(true).build());
            }
        }
    }
}
