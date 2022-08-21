package com.miui.gallery.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.data.PackageGenerator;
import com.miui.gallery.db.sqlite3.GallerySQLiteOpenHelperFactory;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.model.dto.utils.Insertable;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.updater.GalleryDBUpdateManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBHelper implements SupportSQLiteOpenHelper {
    public static String[] CLOUD_CONTROL_TABLES;
    public static String[] CLOUD_TABLES;
    public static String[] PEOPLE_FACE_TABLES;
    public static List<Index> mAlbumIndexList;
    public static List<Index> mShareAlbumIndexList;
    public static GalleryDBHelper sDBHelper;
    public static HashMap<String, Integer> sViewNameVersionMap;
    public List<TableColumn> mAlbumColumns;
    public List<TableColumn> mCloudCacheColumns;
    public List<TableColumn> mCloudColumns;
    public List<TableColumn> mCloudControlColumns;
    public List<TableColumn> mCloudSettingColumns;
    public List<TableColumn> mCloudUserColumns;
    public Context mContext;
    public final SupportSQLiteOpenHelper mDelegate;
    public List<TableColumn> mDiscoveryMessageColumns;
    public List<TableColumn> mFace2ImagesColumns;
    public List<TableColumn> mFavoritesColumns;
    public int mOldVersion;
    public List<TableColumn> mOwnerSubUbiImageColumns;
    public List<TableColumn> mPeopleFaceColumns;
    public List<TableColumn> mPeopleRecommendColumns;
    public List<TableColumn> mRecentDiscoveredMediaColumns;
    public List<TableColumn> mShareAlbumColumns;
    public List<TableColumn> mShareImageColumns;
    public List<TableColumn> mShareSubUbiImageColumns;
    public List<TableColumn> mShareUserColumns;
    public List<TableColumn> mUserInfoColumns;

    static {
        HashMap<String, Integer> hashMap = new HashMap<>();
        sViewNameVersionMap = hashMap;
        hashMap.put("extended_cloud", 5);
        sViewNameVersionMap.put("extended_faceImage", 5);
        CLOUD_TABLES = new String[]{"cloud", "shareAlbum", "shareUser", "shareImage", "cloudUser", "cloudCache", "userInfo", "ownerSubUbifocus", "peopleFace", "faceToImages", "peopleRecommend", "shareSubUbifocus", "recentDiscoveredMedia", "discoveryMessage", "cloudSetting"};
        CLOUD_CONTROL_TABLES = new String[]{"cloudControl"};
        PEOPLE_FACE_TABLES = new String[]{"peopleFace", "faceToImages", "peopleRecommend"};
    }

    public GalleryDBHelper(Context context) {
        SupportSQLiteOpenHelper create = new GallerySQLiteOpenHelperFactory().create(SupportSQLiteOpenHelper.Configuration.builder(context).name(context.getDatabasePath("gallery.db").getPath()).callback(new SQLiteOpenHelperCallback(112)).build());
        this.mDelegate = create;
        create.setWriteAheadLoggingEnabled(true);
        this.mContext = context;
    }

    public static synchronized GalleryDBHelper getInstance(Context context) {
        GalleryDBHelper galleryDBHelper;
        synchronized (GalleryDBHelper.class) {
            if (sDBHelper == null) {
                sDBHelper = new GalleryDBHelper(context);
            }
            galleryDBHelper = sDBHelper;
        }
        return galleryDBHelper;
    }

    public static synchronized GalleryDBHelper getInstance() {
        GalleryDBHelper galleryDBHelper;
        synchronized (GalleryDBHelper.class) {
            galleryDBHelper = getInstance(GalleryApp.sGetAndroidContext());
        }
        return galleryDBHelper;
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public String getDatabaseName() {
        return this.mDelegate.getDatabaseName();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public void setWriteAheadLoggingEnabled(boolean z) {
        this.mDelegate.setWriteAheadLoggingEnabled(z);
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase getWritableDatabase() {
        return this.mDelegate.getWritableDatabase();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper
    public SupportSQLiteDatabase getReadableDatabase() {
        return this.mDelegate.getReadableDatabase();
    }

    @Override // androidx.sqlite.db.SupportSQLiteOpenHelper, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.mDelegate.close();
    }

    public static void createTable(SupportSQLiteDatabase supportSQLiteDatabase, String str, List<TableColumn> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            TableColumn tableColumn = list.get(i);
            if (TextUtils.isEmpty(tableColumn.defaultValue)) {
                sb.append(String.format(" %s %s", tableColumn.name, tableColumn.type));
            } else {
                sb.append(String.format(" %s %s DEFAULT %s", tableColumn.name, tableColumn.type, tableColumn.defaultValue));
            }
            if (tableColumn.isUnique) {
                sb.append(" UNIQUE");
            }
            if (i == 0) {
                sb.append(" PRIMARY KEY");
            }
            if (j.c.equals(tableColumn.name)) {
                sb.append(" AUTOINCREMENT");
            }
            if (!TextUtils.isEmpty(tableColumn.check)) {
                sb.append(String.format(" CHECK(%s)", tableColumn.check));
            }
            if (!TextUtils.isEmpty(tableColumn.collateType)) {
                sb.append(String.format(" COLLATE %s", tableColumn.collateType));
            }
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        supportSQLiteDatabase.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (%s)", str, sb.toString()));
        createIndexByTable(supportSQLiteDatabase, str);
    }

    public static void createIndexByTable(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1815916080:
                if (str.equals("shareAlbum")) {
                    c = 0;
                    break;
                }
                break;
            case -1808499524:
                if (str.equals("shareImage")) {
                    c = 1;
                    break;
                }
                break;
            case -1581995958:
                if (str.equals("shareUser")) {
                    c = 2;
                    break;
                }
                break;
            case -426108000:
                if (str.equals("cloudUser")) {
                    c = 3;
                    break;
                }
                break;
            case -341607859:
                if (str.equals("cloudCache")) {
                    c = 4;
                    break;
                }
                break;
            case -78904752:
                if (str.equals("faceToImages")) {
                    c = 5;
                    break;
                }
                break;
            case 92896879:
                if (str.equals("album")) {
                    c = 6;
                    break;
                }
                break;
            case 94756405:
                if (str.equals("cloud")) {
                    c = 7;
                    break;
                }
                break;
            case 417623789:
                if (str.equals("peopleRecommend")) {
                    c = '\b';
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                for (Index index : getShareAlbumTableIndexList()) {
                    createIndex(supportSQLiteDatabase, index.index, "shareAlbum", index.column, index.unique);
                }
                return;
            case 1:
                createIndex(supportSQLiteDatabase, "index_shareId", "shareImage", "shareId", true);
                createIndex(supportSQLiteDatabase, "index_shareImage_albumId", "shareImage", "albumId");
                createIndex(supportSQLiteDatabase, "index_shareImage_creatorId", "shareImage", "creatorId");
                createIndex(supportSQLiteDatabase, "index_shareimage_mixed_exif_datetime", "shareImage", "mixedDateTime, exifDateTime");
                createIndex(supportSQLiteDatabase, "index_shareimage_size", "shareImage", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
                createIndex(supportSQLiteDatabase, "index_shareimage_microthumbnail", "shareImage", "groupId DESC, dateModified DESC");
                return;
            case 2:
                createIndex(supportSQLiteDatabase, "index_shareUser_albumId", "shareUser", "albumId");
                createIndex(supportSQLiteDatabase, "index_shareUser_userId", "shareUser", "userId");
                createIndex(supportSQLiteDatabase, "index_shareUser_localAlbumId", "shareUser", "localAlbumId");
                return;
            case 3:
                createIndex(supportSQLiteDatabase, "index_cloudUser_albumId", "cloudUser", "albumId");
                createIndex(supportSQLiteDatabase, "index_cloudUser_userId", "cloudUser", "userId");
                createIndex(supportSQLiteDatabase, "index_cloudUser_localAlbumId", "cloudUser", "localAlbumId");
                return;
            case 4:
                createIndex(supportSQLiteDatabase, "index_cloudCache_serverId", "cloudCache", "serverId");
                return;
            case 5:
                createIndex(supportSQLiteDatabase, "index_serverId_peopleFace", "peopleFace", "serverId");
                createIndex(supportSQLiteDatabase, "index_groupId_peopleFace", "peopleFace", "groupId");
                createIndex(supportSQLiteDatabase, "index_faceId_faceToImages", "faceToImages", "faceId");
                createIndex(supportSQLiteDatabase, "index_imageServerId_faceToImages", "faceToImages", "imageServerId");
                return;
            case 6:
                for (Index index2 : getAlbumTableIndexList()) {
                    createIndex(supportSQLiteDatabase, index2.index, "album", index2.column, index2.unique);
                }
                return;
            case 7:
                createIndex(supportSQLiteDatabase, "index_serverId", "cloud", "serverId", true);
                createIndex(supportSQLiteDatabase, "index_fileName", "cloud", "fileName");
                createIndex(supportSQLiteDatabase, "index_sort", "cloud", "dateModified DESC, _id DESC");
                createIndex(supportSQLiteDatabase, "index_localFlag", "cloud", "localFlag");
                createIndex(supportSQLiteDatabase, "index_fileName_NOCASE", "cloud", "fileName COLLATE NOCASE");
                createIndex(supportSQLiteDatabase, "index_cloud_albumId", "cloud", "albumId");
                createIndex(supportSQLiteDatabase, "index_cloud_mixed_exif_datetime", "cloud", "mixedDateTime, exifDateTime");
                createIndex(supportSQLiteDatabase, "index_cloud_size", "cloud", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
                createIndex(supportSQLiteDatabase, "index_microthumbnail", "cloud", "groupId DESC, dateModified DESC");
                createIndex(supportSQLiteDatabase, "index_cloud_sha1", "cloud", "sha1");
                createIndex(supportSQLiteDatabase, "index_cloud_server_type", "cloud", "serverType");
                createIndex(supportSQLiteDatabase, "index_local_group_id", "cloud", "localGroupId");
                return;
            case '\b':
                createIndex(supportSQLiteDatabase, "index_peopleServerID_peopleRecommend", "peopleRecommend", "peopleServerId");
                return;
            default:
                return;
        }
    }

    public static List<Index> getAlbumTableIndexList() {
        List<Index> list = mAlbumIndexList;
        if (list != null && !list.isEmpty()) {
            return mAlbumIndexList;
        }
        if (mAlbumIndexList == null) {
            mAlbumIndexList = new ArrayList(6);
        }
        mAlbumIndexList.add(new Index("index_album_server_id", "serverId", true));
        mAlbumIndexList.add(new Index("index_album_cover_id", "coverId"));
        mAlbumIndexList.add(new Index("index_album_server_status", "serverStatus"));
        mAlbumIndexList.add(new Index("index_album_name", "name"));
        mAlbumIndexList.add(new Index("index_album_localFlag", "localFlag"));
        mAlbumIndexList.add(new Index("index_album_name_nocase", "name COLLATE NOCASE"));
        return mAlbumIndexList;
    }

    public static List<Index> getShareAlbumTableIndexList() {
        List<Index> list = mShareAlbumIndexList;
        if (list != null && !list.isEmpty()) {
            return mShareAlbumIndexList;
        }
        if (mShareAlbumIndexList == null) {
            mShareAlbumIndexList = new ArrayList(2);
        }
        mShareAlbumIndexList.add(new Index("index_albumId", "albumId", true));
        mShareAlbumIndexList.add(new Index("index_shareAlbum_creatorId", "creatorId"));
        return mShareAlbumIndexList;
    }

    /* loaded from: classes2.dex */
    public static class Index {
        public String column;
        public String index;
        public boolean unique;

        public Index(String str, String str2) {
            this.index = str;
            this.column = str2;
        }

        public Index(String str, String str2, boolean z) {
            this.index = str;
            this.column = str2;
            this.unique = z;
        }
    }

    public static void createIndex(SupportSQLiteDatabase supportSQLiteDatabase, String str, String str2, String str3) {
        createIndex(supportSQLiteDatabase, str, str2, str3, false);
    }

    public static void createIndex(SupportSQLiteDatabase supportSQLiteDatabase, String str, String str2, String str3, boolean z) {
        Object[] objArr = new Object[4];
        objArr[0] = z ? "create unique index if not exists" : "create index if not exists";
        objArr[1] = str;
        objArr[2] = str2;
        objArr[3] = str3;
        safeExecSQL(supportSQLiteDatabase, String.format("%s %s on %s (%s)", objArr));
    }

    public static boolean safeExecSQL(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        try {
            supportSQLiteDatabase.execSQL(str);
            return true;
        } catch (Exception e) {
            DefaultLogger.w("GalleryDBHelper", "fail to execSQL: %s , detail: %s", str, e.toString());
            return false;
        }
    }

    public static void addColumn(SupportSQLiteDatabase supportSQLiteDatabase, String str, TableColumn tableColumn) {
        String format = TextUtils.isEmpty(tableColumn.defaultValue) ? String.format("ALTER TABLE %s ADD COLUMN %s %s", str, tableColumn.name, tableColumn.type) : String.format("ALTER TABLE %s ADD COLUMN %s %s DEFAULT %s", str, tableColumn.name, tableColumn.type, tableColumn.defaultValue);
        try {
            supportSQLiteDatabase.execSQL(format);
        } catch (Exception e) {
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, format);
            hashMap.put("version", String.valueOf(supportSQLiteDatabase.getVersion()));
            hashMap.put("error", e.getMessage() + " : " + e.getCause());
            SamplingStatHelper.recordCountEvent("db_helper", "db_add_columns", hashMap);
            throw new SQLException(e.getMessage(), e);
        }
    }

    public boolean execSQL(String str) {
        try {
            getWritableDatabase().execSQL(str);
            return true;
        } catch (SQLiteException e) {
            DefaultLogger.w("GalleryDBHelper", "exec sql", e);
            return false;
        }
    }

    public void analyze() {
        execSQL("analyze;");
    }

    public boolean deleteDatabase() {
        return this.mContext.deleteDatabase("gallery.db");
    }

    public static void dropTable(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        safeExecSQL(supportSQLiteDatabase, "drop table if exists " + str);
    }

    public static String[] getPeopleFaceTables() {
        String[] strArr = PEOPLE_FACE_TABLES;
        return (String[]) Arrays.copyOf(strArr, strArr.length);
    }

    public static String[] getCloudTables() {
        String[] strArr = CLOUD_TABLES;
        return (String[]) Arrays.copyOf(strArr, strArr.length);
    }

    public static String[] getCloudControlTables() {
        String[] strArr = CLOUD_CONTROL_TABLES;
        return (String[]) Arrays.copyOf(strArr, strArr.length);
    }

    public static void refillLocalGroupId(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2) {
        if (z) {
            SafeDBUtil.safeQuery(supportSQLiteDatabase, "album", new String[]{j.c, "serverId"}, String.format(Locale.US, "%s='%s'", "serverStatus", "custom"), (String[]) null, (String) null, new UpdateLocalGroupIdQueryHandler(supportSQLiteDatabase, "cloud", "groupId"));
        }
        if (z2) {
            SafeDBUtil.safeQuery(supportSQLiteDatabase, "shareAlbum", new String[]{j.c, "albumId"}, String.format(Locale.US, "%s='%s'", "serverStatus", "custom"), (String[]) null, (String) null, new UpdateLocalGroupIdQueryHandler(supportSQLiteDatabase, "shareImage", "albumId"));
        }
    }

    public static void refillLocalGroupId(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2, boolean z3) {
        boolean z4 = true;
        boolean z5 = !z;
        if (z2 || z3) {
            z4 = false;
        }
        refillLocalGroupId(supportSQLiteDatabase, z5, z4);
    }

    public static void cleanCloudData(SupportSQLiteDatabase supportSQLiteDatabase) {
        for (String str : CLOUD_TABLES) {
            try {
                dropTable(supportSQLiteDatabase, str);
            } catch (Exception e) {
                DefaultLogger.w("GalleryDBHelper", e);
            }
        }
    }

    public final int fixCameraAlbumAttributes(SupportSQLiteDatabase supportSQLiteDatabase) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("attributes", (Long) 5L);
        return supportSQLiteDatabase.update("cloud", 0, contentValues, String.format(Locale.US, "%s=%d and (%s & %d)=%d", "serverId", 1L, "attributes", 16L, 16L), null);
    }

    public final void upgradeAlbumEditedColumns(SupportSQLiteDatabase supportSQLiteDatabase) {
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(6);
        supportSQLiteDatabase.execSQL(String.format("update %s set %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s", "album", "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, DatabaseUtils.concatenateWhere("(localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", String.format(Locale.US, "(%s & %d) = %d or (%s & %d) = %d", "attributes", 12L, 12L, "attributes", 48L, 48L))));
    }

    public List<TableColumn> getCloudColumns() {
        initCloudColumns();
        return Collections.unmodifiableList(this.mCloudColumns);
    }

    public List<TableColumn> getCloudSettingColumns() {
        initCloudSettingColumns();
        return Collections.unmodifiableList(this.mCloudSettingColumns);
    }

    public List<TableColumn> getShareAlbumColumns() {
        initShareAlbumColumns();
        return Collections.unmodifiableList(this.mShareAlbumColumns);
    }

    public List<TableColumn> getShareUserColumns() {
        initShareUserColumns();
        return Collections.unmodifiableList(this.mShareUserColumns);
    }

    public List<TableColumn> getShareImageColumns() {
        initShareImageColumns();
        return Collections.unmodifiableList(this.mShareImageColumns);
    }

    public List<TableColumn> getCloudUserColumns() {
        initCloudUserColumns();
        return Collections.unmodifiableList(this.mCloudUserColumns);
    }

    public List<TableColumn> getCloudCacheColumns() {
        initCloudCacheColumns();
        return Collections.unmodifiableList(this.mCloudCacheColumns);
    }

    public List<TableColumn> getUserInfoColumns() {
        initUserInfoColumns();
        return Collections.unmodifiableList(this.mUserInfoColumns);
    }

    public List<TableColumn> getOwnerSubUbiFocusColumns() {
        initOwnerSubUbiFocusColumns();
        return Collections.unmodifiableList(this.mOwnerSubUbiImageColumns);
    }

    public List<TableColumn> getShareSubUbiFocusColumns() {
        initShareSubUbiFocusColumns();
        return Collections.unmodifiableList(this.mShareSubUbiImageColumns);
    }

    public List<TableColumn> getPeopleFaceColumns() {
        initPeopleFaceColumns();
        return Collections.unmodifiableList(this.mPeopleFaceColumns);
    }

    public List<TableColumn> getFace2ImagesColumns() {
        initFace2ImagesColumns();
        return Collections.unmodifiableList(this.mFace2ImagesColumns);
    }

    public List<TableColumn> getPeopleRecommendColumns() {
        initPeopleRecommendColumns();
        return Collections.unmodifiableList(this.mPeopleRecommendColumns);
    }

    public List<TableColumn> getDiscoveryMessageColumns() {
        initDiscoveryMessageColumns();
        return Collections.unmodifiableList(this.mDiscoveryMessageColumns);
    }

    public List<TableColumn> getRecentDiscoveredMediaColumns() {
        initRecentDiscoveredMediaColumns();
        return Collections.unmodifiableList(this.mRecentDiscoveredMediaColumns);
    }

    public List<TableColumn> getFavoritesColumns() {
        initFavoritesColumns();
        return Collections.unmodifiableList(this.mFavoritesColumns);
    }

    public List<TableColumn> getCloudControlColumns() {
        initCloudControlColumns();
        return Collections.unmodifiableList(this.mCloudControlColumns);
    }

    public List<TableColumn> getAlbumColumns() {
        initAlbumColumns();
        return Collections.unmodifiableList(this.mAlbumColumns);
    }

    public final void initCloudSettingColumns() {
        if (this.mCloudSettingColumns == null) {
            this.mCloudSettingColumns = new ArrayList(16);
        }
        if (!this.mCloudSettingColumns.isEmpty()) {
            return;
        }
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("accountName").setType("TEXT").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("accountType").setType("TEXT").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("syncTag").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("isSync").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("isSyncOnlyOnWifi").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("shareSyncTagAlbumList").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("shareSyncTagAlbumInfo").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("shareSyncTagImageList").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("shareSyncTagUserList").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("ownerSyncTagUserList").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("syncInfo").setType("TEXT").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("shareSyncInfo").setType("TEXT").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("cloudTabNewFlag").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("faceWatermark").setType("INTEGER").build());
        this.mCloudSettingColumns.add(new TableColumn.Builder().setName("faceSyncToken").setType("TEXT").build());
    }

    public final void initShareAlbumColumns() {
        if (this.mShareAlbumColumns == null) {
            this.mShareAlbumColumns = new ArrayList(34);
        }
        if (!this.mShareAlbumColumns.isEmpty()) {
            return;
        }
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("groupId").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("dateModified").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("description").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("fileName").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("dateTaken").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("serverType").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("sortBy").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("canModified").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("creatorId").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("shareUrl").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("albumStatus").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("albumTag").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("albumImageTag").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("albumUserTag").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("shareUrlLong").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("newImageFlag").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("isPublic").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("publicUrl").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("syncInfo").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("babyInfoJson").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("peopleId").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("sharerInfo").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("editedColumns").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("thumbnailInfo").setType("TEXT").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("attributes").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("sort_position").setType("REAL").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("coverId").setType("INTEGER").build());
        this.mShareAlbumColumns.add(new TableColumn.Builder().setName("sortInfo").setType("TEXT").build());
    }

    public final void initShareUserColumns() {
        if (this.mShareUserColumns == null) {
            this.mShareUserColumns = new ArrayList(15);
        }
        if (!this.mShareUserColumns.isEmpty()) {
            return;
        }
        this.mShareUserColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("userId").setType("INTEGER").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("userName").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("createTime").setType("INTEGER").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("requestType").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("requestValue").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("shareUrl").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("shareText").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("localAlbumId").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("relation").setType("TEXT").build());
        this.mShareUserColumns.add(new TableColumn.Builder().setName("relationText").setType("TEXT").build());
    }

    public final void initShareImageColumns() {
        if (this.mShareImageColumns == null) {
            this.mShareImageColumns = new ArrayList(61);
        }
        if (!this.mShareImageColumns.isEmpty()) {
            return;
        }
        this.mShareImageColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("groupId").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE).setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("dateModified").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("mimeType").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("title").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("description").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("fileName").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("dateTaken").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("duration").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("serverType").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifImageWidth").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifImageLength").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifOrientation").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSLatitude").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSLongitude").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifMake").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifModel").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifFlash").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSLatitudeRef").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSLongitudeRef").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifExposureTime").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifFNumber").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifISOSpeedRatings").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSAltitude").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSAltitudeRef").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSTimeStamp").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSDateStamp").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifWhiteBalance").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifFocalLength").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifGPSProcessingMethod").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("exifDateTime").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("thumbnailFile").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("downloadFile").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("localFile").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("sha1").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("sortBy").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("microthumbfile").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("localGroupId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("localImageId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("creatorId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("shareId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("downloadFileStatus").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("downloadFileTime").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("mixedDateTime").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("ubiSubImageCount").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("ubiFocusIndex").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("ubiSubIndex").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("editedColumns").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("fromLocalGroupId").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("secretKey").setType("BLOB").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("lables").setType("INTEGER").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("location").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("extraGPS").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("address").setType("TEXT").build());
        this.mShareImageColumns.add(new TableColumn.Builder().setName("specialTypeFlags").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
    }

    public final void initCloudUserColumns() {
        if (this.mCloudUserColumns == null) {
            this.mCloudUserColumns = new ArrayList(16);
        }
        if (!this.mCloudUserColumns.isEmpty()) {
            return;
        }
        this.mCloudUserColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("userId").setType("INTEGER").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("userName").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("createTime").setType("INTEGER").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("requestType").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("requestValue").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("shareUrl").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("shareText").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("localAlbumId").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("relation").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("relationText").setType("TEXT").build());
        this.mCloudUserColumns.add(new TableColumn.Builder().setName("phone").setType("TEXT").build());
    }

    public final void initCloudCacheColumns() {
        if (this.mCloudCacheColumns == null) {
            this.mCloudCacheColumns = new ArrayList(6);
        }
        if (!this.mCloudCacheColumns.isEmpty()) {
            return;
        }
        this.mCloudCacheColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mCloudCacheColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mCloudCacheColumns.add(new TableColumn.Builder().setName("barcodeData").setType("TEXT").build());
        this.mCloudCacheColumns.add(new TableColumn.Builder().setName("barcodeDataDeadline").setType("INTEGER").build());
        this.mCloudCacheColumns.add(new TableColumn.Builder().setName("smsShareData").setType("TEXT").build());
        this.mCloudCacheColumns.add(new TableColumn.Builder().setName("smsShareDataDeadline").setType("INTEGER").build());
    }

    public final void initUserInfoColumns() {
        if (this.mUserInfoColumns == null) {
            this.mUserInfoColumns = new ArrayList(6);
        }
        if (!this.mUserInfoColumns.isEmpty()) {
            return;
        }
        this.mUserInfoColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mUserInfoColumns.add(new TableColumn.Builder().setName("date_modified").setType("INTEGER").build());
        this.mUserInfoColumns.add(new TableColumn.Builder().setName("user_id").setType("TEXT").build());
        this.mUserInfoColumns.add(new TableColumn.Builder().setName("alias_nick").setType("TEXT").build());
        this.mUserInfoColumns.add(new TableColumn.Builder().setName("miliao_nick").setType("TEXT").build());
        this.mUserInfoColumns.add(new TableColumn.Builder().setName("miliao_icon_url").setType("TEXT").build());
    }

    public final void initOwnerSubUbiFocusColumns() {
        if (this.mOwnerSubUbiImageColumns == null) {
            this.mOwnerSubUbiImageColumns = new ArrayList(52);
        }
        if (!this.mOwnerSubUbiImageColumns.isEmpty()) {
            return;
        }
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("groupId").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE).setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("dateModified").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("mimeType").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("title").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("description").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("fileName").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("dateTaken").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("duration").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("serverType").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifImageWidth").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifImageLength").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifOrientation").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLatitude").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLongitude").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifMake").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifModel").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifFlash").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLatitudeRef").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLongitudeRef").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifExposureTime").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifFNumber").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifISOSpeedRatings").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSAltitude").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSAltitudeRef").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSTimeStamp").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSDateStamp").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifWhiteBalance").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifFocalLength").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSProcessingMethod").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("exifDateTime").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("thumbnailFile").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("downloadFile").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("localFile").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("sha1").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("sortBy").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("microthumbfile").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("localGroupId").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("localImageId").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("downloadFileStatus").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("downloadFileTime").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("mixedDateTime").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("ubiServerId").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("ubiLocalId").setType("TEXT").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("ubiSubIndex").setType("INTEGER").build());
        this.mOwnerSubUbiImageColumns.add(new TableColumn.Builder().setName("secretKey").setType("BLOB").build());
    }

    public final void initShareSubUbiFocusColumns() {
        if (this.mShareSubUbiImageColumns == null) {
            this.mShareSubUbiImageColumns = new ArrayList(54);
        }
        if (!this.mShareSubUbiImageColumns.isEmpty()) {
            return;
        }
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("groupId").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE).setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("dateModified").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("mimeType").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("title").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("description").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("fileName").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("dateTaken").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("duration").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("serverType").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifImageWidth").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifImageLength").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifOrientation").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLatitude").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLongitude").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifMake").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifModel").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifFlash").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLatitudeRef").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSLongitudeRef").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifExposureTime").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifFNumber").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifISOSpeedRatings").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSAltitude").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSAltitudeRef").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSTimeStamp").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSDateStamp").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifWhiteBalance").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifFocalLength").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifGPSProcessingMethod").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("exifDateTime").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("thumbnailFile").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("downloadFile").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("localFile").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("sha1").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("sortBy").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("microthumbfile").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("localGroupId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("localImageId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("creatorId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("shareId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("downloadFileStatus").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("downloadFileTime").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("mixedDateTime").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("ubiServerId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("ubiLocalId").setType("TEXT").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("ubiSubIndex").setType("INTEGER").build());
        this.mShareSubUbiImageColumns.add(new TableColumn.Builder().setName("secretKey").setType("BLOB").build());
    }

    public final void initPeopleFaceColumns() {
        if (this.mPeopleFaceColumns == null) {
            this.mPeopleFaceColumns = new ArrayList(24);
        }
        if (!this.mPeopleFaceColumns.isEmpty()) {
            return;
        }
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName(nexExportFormat.TAG_FORMAT_TYPE).setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("groupId").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("localGroupId").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("faceXScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("faceYScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("faceWScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("faceHScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("leftEyeXScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("leftEyeYScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("RightEyeXScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("RightEyeYScale").setType("REAL").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("peopleName").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("visibilityType").setType("INTEGER").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("peopleType").setType("INTEGER").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("peopleContactJsonInfo").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("relationType").setType("INTEGER").setDefaultValue("0").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("eTag").setType("INTEGER").setDefaultValue("0").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("relationText").setType("TEXT").build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("faceCoverScore").setType("REAL").setDefaultValue(String.valueOf(-1.0d)).build());
        this.mPeopleFaceColumns.add(new TableColumn.Builder().setName("selectCoverId").setType("TEXT").build());
    }

    public final void initFace2ImagesColumns() {
        if (this.mFace2ImagesColumns == null) {
            this.mFace2ImagesColumns = new ArrayList(3);
        }
        if (!this.mFace2ImagesColumns.isEmpty()) {
            return;
        }
        this.mFace2ImagesColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mFace2ImagesColumns.add(new TableColumn.Builder().setName("faceId").setType("TEXT").build());
        this.mFace2ImagesColumns.add(new TableColumn.Builder().setName("imageServerId").setType("TEXT").build());
    }

    public final void initPeopleRecommendColumns() {
        if (this.mPeopleRecommendColumns == null) {
            this.mPeopleRecommendColumns = new ArrayList(5);
        }
        if (!this.mPeopleRecommendColumns.isEmpty()) {
            return;
        }
        this.mPeopleRecommendColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mPeopleRecommendColumns.add(new TableColumn.Builder().setName("peopleServerId").setType("TEXT").build());
        this.mPeopleRecommendColumns.add(new TableColumn.Builder().setName("recommendPeoplesJson").setType("TEXT").build());
        this.mPeopleRecommendColumns.add(new TableColumn.Builder().setName("recommendHistoryJson").setType("TEXT").build());
        this.mPeopleRecommendColumns.add(new TableColumn.Builder().setName("lastUpdateFromServerTime").setType("INTEGER").build());
    }

    public final void initCloudColumns() {
        if (this.mCloudColumns == null) {
            this.mCloudColumns = new ArrayList(76);
        }
        if (!this.mCloudColumns.isEmpty()) {
            return;
        }
        this.mCloudColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("groupId").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE).setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("dateModified").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("mimeType").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("title").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("description").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("fileName").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("dateTaken").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("duration").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("serverType").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("serverTag").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifImageWidth").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifImageLength").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifOrientation").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSLatitude").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSLongitude").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifMake").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifModel").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifFlash").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSLatitudeRef").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSLongitudeRef").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifExposureTime").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifFNumber").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifISOSpeedRatings").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSAltitude").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSAltitudeRef").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSTimeStamp").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSDateStamp").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifWhiteBalance").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifFocalLength").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifGPSProcessingMethod").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("exifDateTime").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("thumbnailFile").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("downloadFile").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("localFile").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("sha1").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("sortBy").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("microthumbfile").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("localGroupId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("localImageId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("albumId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("canModified").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("shareUrl").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("albumUserTag").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("newImageFlag").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("newUserFlag").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("creatorId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("isPublic").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("publicUrl").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("downloadFileStatus").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("downloadFileTime").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("mixedDateTime").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("ubiSubImageCount").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("ubiFocusIndex").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("ubiSubIndex").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("editedColumns").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("fromLocalGroupId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("secretKey").setType("BLOB").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("appKey").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("babyInfoJson").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("peopleId").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("lables").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("thumbnailInfo").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("location").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("attributes").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("extraGPS").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("address").setType("TEXT").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("specialTypeFlags").setType("INTEGER").setDefaultValue(String.valueOf(0L)).build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("sort_position").setType("REAL").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("realSize").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("realDateModified").setType("INTEGER").build());
        this.mCloudColumns.add(new TableColumn.Builder().setName("source_pkg").setType("TEXT").build());
    }

    public final void initFavoritesColumns() {
        if (this.mFavoritesColumns == null) {
            this.mFavoritesColumns = new ArrayList(5);
        }
        if (!this.mFavoritesColumns.isEmpty()) {
            return;
        }
        this.mFavoritesColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mFavoritesColumns.add(new TableColumn.Builder().setName("isFavorite").setType("INTEGER").build());
        this.mFavoritesColumns.add(new TableColumn.Builder().setName("dateFavorite").setType("INTEGER").build());
        this.mFavoritesColumns.add(new TableColumn.Builder().setName("source").setType("INTEGER").build());
        this.mFavoritesColumns.add(new TableColumn.Builder().setName("cloud_id").setType("INTEGER").setUnique(true).build());
    }

    public final void initRecentDiscoveredMediaColumns() {
        if (this.mRecentDiscoveredMediaColumns == null) {
            this.mRecentDiscoveredMediaColumns = new ArrayList(4);
        }
        if (!this.mRecentDiscoveredMediaColumns.isEmpty()) {
            return;
        }
        this.mRecentDiscoveredMediaColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mRecentDiscoveredMediaColumns.add(new TableColumn.Builder().setName("mediaId").setType("INTEGER").setUnique(true).build());
        this.mRecentDiscoveredMediaColumns.add(new TableColumn.Builder().setName("dateAdded").setType("INTEGER").build());
        this.mRecentDiscoveredMediaColumns.add(new TableColumn.Builder().setName("source").setType("INTEGER").build());
    }

    public final void initCloudControlColumns() {
        if (this.mCloudControlColumns == null) {
            this.mCloudControlColumns = new ArrayList(4);
        }
        if (!this.mCloudControlColumns.isEmpty()) {
            return;
        }
        this.mCloudControlColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mCloudControlColumns.add(new TableColumn.Builder().setName("featureName").setType("TEXT").setUnique(true).build());
        this.mCloudControlColumns.add(new TableColumn.Builder().setName("status").setType("TEXT").build());
        this.mCloudControlColumns.add(new TableColumn.Builder().setName("strategy").setType("TEXT").build());
    }

    public final void initDiscoveryMessageColumns() {
        if (this.mDiscoveryMessageColumns == null) {
            this.mDiscoveryMessageColumns = new ArrayList(14);
        }
        if (!this.mDiscoveryMessageColumns.isEmpty()) {
            return;
        }
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("message").setType("TEXT").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("title").setType("TEXT").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("subTitle").setType("TEXT").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName(nexExportFormat.TAG_FORMAT_TYPE).setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName(j.k).setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("receiveTime").setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("updateTime").setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("triggerTime").setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("expireTime").setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("actionUri").setType("TEXT").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("messageSource").setType("TEXT").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("isConsumed").setType("INTEGER").build());
        this.mDiscoveryMessageColumns.add(new TableColumn.Builder().setName("extraData").setType("TEXT").build());
    }

    public final void initAlbumColumns() {
        if (this.mAlbumColumns == null) {
            this.mAlbumColumns = new ArrayList(15);
        }
        if (!this.mAlbumColumns.isEmpty()) {
            return;
        }
        this.mAlbumColumns.add(new TableColumn.Builder().setName(j.c).setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("name").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("attributes").setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("coverId").setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("dateTaken").setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("dateModified").setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("realDateModified").setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("sort_position").setType("REAL").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("localFlag").setType("INTEGER").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("editedColumns").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("serverId").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("serverTag").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("serverStatus").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("localPath").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName(CallMethod.ARG_EXTRA_STRING).setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("sortInfo").setType("TEXT").build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("scan_public_media_count").setType("INTEGER").setDefaultValue(String.valueOf(0)).build());
        this.mAlbumColumns.add(new TableColumn.Builder().setName("scan_public_media_generation_modified").setType("INTEGER").setDefaultValue(String.valueOf(0)).build());
    }

    public void clearColumnList() {
        clearColumn(this.mCloudColumns);
        clearColumn(this.mCloudSettingColumns);
        clearColumn(this.mShareAlbumColumns);
        clearColumn(this.mShareImageColumns);
        clearColumn(this.mShareUserColumns);
        clearColumn(this.mCloudUserColumns);
        clearColumn(this.mCloudCacheColumns);
        clearColumn(this.mUserInfoColumns);
        clearColumn(this.mOwnerSubUbiImageColumns);
        clearColumn(this.mShareSubUbiImageColumns);
        clearColumn(this.mPeopleFaceColumns);
        clearColumn(this.mFace2ImagesColumns);
        clearColumn(this.mPeopleRecommendColumns);
        clearColumn(this.mDiscoveryMessageColumns);
        clearColumn(this.mRecentDiscoveredMediaColumns);
        clearColumn(this.mCloudControlColumns);
        clearColumn(this.mFavoritesColumns);
    }

    public final void clearColumn(List<TableColumn> list) {
        if (list == null) {
            return;
        }
        list.clear();
    }

    /* loaded from: classes2.dex */
    public static class UpdateLocalGroupIdQueryHandler implements SafeDBUtil.QueryHandler<Void> {
        public final String mAlbumIdColumnName;
        public final SupportSQLiteDatabase mDB;
        public final String mTable;

        public UpdateLocalGroupIdQueryHandler(SupportSQLiteDatabase supportSQLiteDatabase, String str, String str2) {
            this.mDB = supportSQLiteDatabase;
            this.mTable = str;
            this.mAlbumIdColumnName = str2;
        }

        @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
        /* renamed from: handle */
        public Void mo1808handle(Cursor cursor) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int i = cursor.getInt(0);
                    String string = cursor.getString(1);
                    if (!TextUtils.isEmpty(string)) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("localGroupId", Integer.valueOf(i));
                        this.mDB.update(this.mTable, 0, contentValues, String.format(Locale.US, "%s=?", this.mAlbumIdColumnName), new String[]{string});
                    }
                }
                return null;
            }
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public class SQLiteOpenHelperCallback extends SupportSQLiteOpenHelper.Callback {
        public static /* synthetic */ long $r8$lambda$JWpoLpi1GU8UuM21NUk80ANhJpk(SupportSQLiteDatabase supportSQLiteDatabase, Uri uri, String str, String str2, ContentValues contentValues) {
            return supportSQLiteDatabase.insert(str, 0, contentValues);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SQLiteOpenHelperCallback(int i) {
            super(i);
            GalleryDBHelper.this = r1;
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onCreate(final SupportSQLiteDatabase supportSQLiteDatabase) {
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloud", GalleryDBHelper.this.getCloudColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudSetting", GalleryDBHelper.this.getCloudSettingColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "shareAlbum", GalleryDBHelper.this.getShareAlbumColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "shareImage", GalleryDBHelper.this.getShareImageColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "shareUser", GalleryDBHelper.this.getShareUserColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudUser", GalleryDBHelper.this.getCloudUserColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudCache", GalleryDBHelper.this.getCloudCacheColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "userInfo", GalleryDBHelper.this.getUserInfoColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "ownerSubUbifocus", GalleryDBHelper.this.getOwnerSubUbiFocusColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "shareSubUbifocus", GalleryDBHelper.this.getShareSubUbiFocusColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "peopleFace", GalleryDBHelper.this.getPeopleFaceColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "faceToImages", GalleryDBHelper.this.getFace2ImagesColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "peopleRecommend", GalleryDBHelper.this.getPeopleRecommendColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "discoveryMessage", GalleryDBHelper.this.getDiscoveryMessageColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "recentDiscoveredMedia", GalleryDBHelper.this.getRecentDiscoveredMediaColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "cloudControl", GalleryDBHelper.this.getCloudControlColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "favorites", GalleryDBHelper.this.getFavoritesColumns());
            GalleryDBHelper.createTable(supportSQLiteDatabase, "album", GalleryDBHelper.this.getAlbumColumns());
            createViews(supportSQLiteDatabase, true);
            GalleryDBHelper.this.clearColumnList();
            AlbumDataHelper.addRecordsForCameraAndScreenshots(new Insertable() { // from class: com.miui.gallery.provider.GalleryDBHelper$SQLiteOpenHelperCallback$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.model.dto.utils.Insertable
                public final long insert(Uri uri, String str, String str2, ContentValues contentValues) {
                    return GalleryDBHelper.SQLiteOpenHelperCallback.$r8$lambda$JWpoLpi1GU8UuM21NUk80ANhJpk(SupportSQLiteDatabase.this, uri, str, str2, contentValues);
                }
            });
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            GalleryDBHelper.this.mOldVersion = i;
            if (i >= i2) {
                throw new IllegalStateException("database cannot be downgraded");
            }
            GalleryDBUpdateManager.update(supportSQLiteDatabase, i, i2);
            createViews(supportSQLiteDatabase, false);
            GalleryDBHelper.this.clearColumnList();
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
            SamplingStatHelper.recordCountEvent("db_helper", "db_downgrade");
            DefaultLogger.w("GalleryDBHelper", "onDatabaseDowngrade from %s to %s", Integer.valueOf(i), Integer.valueOf(i2));
        }

        public final void createViews(SupportSQLiteDatabase supportSQLiteDatabase, boolean z) {
            SQLiteView.of("extended_cloud").createLatest(supportSQLiteDatabase, z);
            SQLiteView.of("extended_faceImage").createLatest(supportSQLiteDatabase, z);
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onOpen(SupportSQLiteDatabase supportSQLiteDatabase) {
            if (!GalleryPreferences.DataBase.getEverFixedCameraAlbumAttributes()) {
                try {
                    GalleryDBHelper.this.fixCameraAlbumAttributes(supportSQLiteDatabase);
                    GalleryPreferences.DataBase.setEverFixedCameraAlbumAttributes();
                } catch (Exception e) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("version", GalleryDBHelper.this.mOldVersion + "_" + supportSQLiteDatabase.getVersion());
                    hashMap.put("error", Log.getStackTraceString(e));
                    SamplingStatHelper.recordCountEvent("db_helper", "db_fix_camera_attributes", hashMap);
                    DefaultLogger.e("GalleryDBHelper", "version old[%d], new[%d], exception[%s]", Integer.valueOf(GalleryDBHelper.this.mOldVersion), Integer.valueOf(supportSQLiteDatabase.getVersion()), Log.getStackTraceString(e));
                }
            }
            if (!GalleryPreferences.DataBase.getEverUpgradeAlbumEditedColumns()) {
                try {
                    DefaultLogger.i("GalleryDBHelper", "upgradeAlbumEditedColumns");
                    GalleryDBHelper.this.upgradeAlbumEditedColumns(supportSQLiteDatabase);
                    GalleryPreferences.DataBase.setEverUpgradeAlbumEditedColumns();
                } catch (Exception e2) {
                    DefaultLogger.e("GalleryDBHelper", "version old[%d], new[%d], exception[%s]", Integer.valueOf(GalleryDBHelper.this.mOldVersion), Integer.valueOf(supportSQLiteDatabase.getVersion()), Log.getStackTraceString(e2));
                }
            }
            if (!GalleryPreferences.DataBase.getEverUpgradeDBForScreenshots()) {
                try {
                    DefaultLogger.i("GalleryDBHelper", "upgrade db for screenshots");
                    PackageGenerator.getInstance().generate();
                } catch (Exception e3) {
                    DefaultLogger.e("GalleryDBHelper", "upgrade db for screenshot failed, db version[%d], exception[%s]", Integer.valueOf(supportSQLiteDatabase.getVersion()), Log.getStackTraceString(e3));
                }
            }
            createViews(supportSQLiteDatabase, false);
        }

        @Override // androidx.sqlite.db.SupportSQLiteOpenHelper.Callback
        public void onCorruption(SupportSQLiteDatabase supportSQLiteDatabase) {
            DefaultLogger.e("GalleryDBHelper", "Corruption reported by sqlite on database: %s", supportSQLiteDatabase.getPath());
            super.onCorruption(supportSQLiteDatabase);
        }
    }
}
