package com.miui.gallery.provider.album;

import android.accounts.Account;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import ch.qos.logback.core.util.FileSize;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.account.AccountHelper;
import com.miui.gallery.R;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.FaceAlbumCover;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.provider.InternalContract$ShareImage;
import com.miui.gallery.provider.MediaSortDateHelper;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miuix.core.util.Pools;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class AlbumManager {
    public static final Map<Long, Long> ALBUM_ATTRIBUTES;
    public static final Map<Long, Long> ALBUM_SERVER_ID;
    public static final List<Long> ALBUM_SYNC_ATTRIBUTES;
    public static final String[] QUERY_ALBUM_PROJECTION = AlbumConstants.QUERY_ALBUM_PROJECTION;
    public static final String[] SHARED_ALBUM_PROJECTION;
    public static final String SQL_ALBUM_ALL_PHOTOS;
    public static final String SQL_ALBUM_COVER_BY_CREATE_TIME;
    public static final String SQL_ALBUM_COVER_BY_MODIFIED_TIME;
    public static final String SQL_ALBUM_COVER_BY_SORT;
    public static final String SQL_ALBUM_COVER_DEFAULT_FORMAT;
    public static final String SQL_ALBUM_SCREENSHOTS_AND_RECORDERS;
    public static final String SQL_ALBUM_VIDEO;
    public static final String SQL_BABY_ALBUM_COVER;
    public static final String SQL_BABY_OTHER_SHARED_ALBUM_COVER;
    public static final String SQL_QUERY_COVER_BY_SHARE_IMAGE_ID;
    public static final String SQL_QUERY_DEFAULT_COVER_BY_ID;
    public static final String SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_LOCAL_COVER_ID;
    public static final String SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_SERVER_COVER_ID;
    public static final String SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_SHARE_MEDIA_LOCAL_ID;
    public static final String SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_SHARE_MEDIA_SERVER_ID;
    public static final String SQL_QUERY_NORMAL_ALBUM_DEFAULT_COVER_MODE_FORMAT;
    public static final String SQL_QUERY_NORMAL_ALBUM_FORMAT_BY_NEED_COVER;
    public static final String SQL_QUERY_SHARE_ALBUM_COVER_INFO_BY_SERVER_ID;
    public static final String SQL_QUERY_SHARE_ALBUM_FORMAT_BY_NEED_COVER;
    public static final String SQL_SHARE_ALBUM_COVER;
    public static final String SQL_UNION_ALL_PHOTOS_ALBUM;
    public static final String SQL_UNION_FAVORITES_ALBUM;
    public static final String SQL_UNION_SCREENSHOTS_RECORDERS;
    public static final String SQL_UNION_VIDEO_ALBUM;
    public static final String TABLE_COVER_DETAIL_INFO;
    public static final String TABLE_COVER_DETAIL_INFO_BY_SHARE_ALBUM;
    public static final String VIRTUAL_ALBUM_COVER_TABLE_BY_NORMAL_COVER_ID;
    public static final String VIRTUAL_ALBUM_COVER_TABLE_BY_SHARE_COVER_ID;
    public static long sScreenshotsAlbumId = -1;

    static {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String sb;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("_id AS coverId, (");
        String str7 = InternalContract$Cloud.ALIAS_SIZE_FIRST;
        sb2.append(str7);
        sb2.append(") AS ");
        sb2.append("coverPath");
        sb2.append(", ");
        sb2.append("sha1");
        sb2.append(" AS ");
        sb2.append("coverSha1");
        sb2.append(", ");
        sb2.append(" CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END ");
        sb2.append(" AS ");
        sb2.append("coverSyncState");
        sb2.append(", ");
        sb2.append(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        sb2.append(" AS ");
        sb2.append("coverSize");
        sb2.append(", %s");
        String sb3 = sb2.toString();
        SQL_ALBUM_COVER_DEFAULT_FORMAT = sb3;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("(SELECT * FROM (SELECT cloud.serverId as imageId,");
        Locale locale = Locale.US;
        sb4.append(String.format(locale, sb3, "max(dateModified) AS latest_photo "));
        sb4.append("FROM ");
        sb4.append("cloud");
        sb4.append("  WHERE ");
        sb4.append("localGroupId");
        sb4.append(" = %s ))");
        SQL_BABY_ALBUM_COVER = sb4.toString();
        SQL_BABY_OTHER_SHARED_ALBUM_COVER = "(SELECT * FROM (SELECT serverId as imageId," + String.format(locale, sb3, "max(dateModified) AS latest_photo ") + "FROM shareImage  WHERE localGroupId = %s ))";
        HashMap hashMap = new HashMap(6);
        hashMap.put(1L, 2L);
        hashMap.put(4L, 8L);
        hashMap.put(16L, 32L);
        hashMap.put(64L, 128L);
        hashMap.put(2048L, 4096L);
        ALBUM_ATTRIBUTES = Collections.unmodifiableMap(hashMap);
        ArrayList arrayList = new ArrayList(5);
        arrayList.add(1L);
        arrayList.add(4L);
        arrayList.add(16L);
        arrayList.add(64L);
        arrayList.add(2048L);
        ALBUM_SYNC_ATTRIBUTES = Collections.unmodifiableList(arrayList);
        HashMap hashMap2 = new HashMap(7, 1.0f);
        ALBUM_SERVER_ID = hashMap2;
        hashMap2.put(1L, 1L);
        hashMap2.put(2L, 2L);
        hashMap2.put(3L, -2147483647L);
        hashMap2.put(2147483644L, -2147483644L);
        hashMap2.put(2147483642L, -2147483642L);
        hashMap2.put(2147483645L, -2147483645L);
        hashMap2.put(1000L, 1000L);
        SQL_QUERY_DEFAULT_COVER_BY_ID = "SELECT " + sb3 + " FROM cloud WHERE localGroupId = %s AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
        SQL_QUERY_COVER_BY_SHARE_IMAGE_ID = "SELECT " + sb3 + " FROM shareImage WHERE serverId = %s AND (serverStatus='custom' OR serverStatus = 'recovery')";
        SQL_SHARE_ALBUM_COVER = "16777215+_id AS coverId,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, size AS coverSize, (" + InternalContract$ShareImage.ALIAS_SIZE_FIRST + ") AS coverPath, max(dateModified) AS latest_photo ,";
        String str8 = "_id AS coverId,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, size AS coverSize, (" + str7 + ") AS coverPath, 0 AS is_manual_set_cover, max( %s ) AS latest_photo ,";
        SQL_ALBUM_COVER_BY_SORT = str8;
        SQL_ALBUM_COVER_BY_MODIFIED_TIME = String.format(locale, str8, "dateModified");
        SQL_ALBUM_COVER_BY_CREATE_TIME = String.format(locale, str8, "mixedDateTime");
        SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_LOCAL_COVER_ID = " SELECT * FROM ( SELECT coverId, 1 AS is_manual_set_cover, _id FROM ( SELECT coverId, _id FROM album WHERE coverId NOT NULL  AND coverId<2147483647)) LEFT JOIN ( SELECT size AS coverSize,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, (" + str7 + ") AS coverPath, " + j.c + " AS coverId  FROM cloud WHERE coverId IN ( SELECT coverId FROM album WHERE coverId NOT NULL  AND coverId<2147483647) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))))USING(coverId) WHERE coverPath NOT NULL";
        SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_SERVER_COVER_ID = " SELECT * FROM ( SELECT coverId, 1 AS is_manual_set_cover, _id FROM ( SELECT coverId, _id FROM album WHERE coverId NOT NULL  AND coverId>2147483647)) LEFT JOIN ( SELECT size AS coverSize,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, (" + str7 + ") AS coverPath, serverId AS coverId  FROM cloud WHERE serverId IN (SELECT coverId FROM album WHERE coverId NOT NULL  AND coverId NOT NULL  AND coverId>2147483647) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY coverId)USING(coverId) WHERE coverPath NOT NULL";
        SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_SHARE_MEDIA_SERVER_ID = " SELECT * FROM ( SELECT coverId, 1 AS is_manual_set_cover, _id FROM ( SELECT coverId, _id FROM album WHERE coverId NOT NULL  GROUP BY coverId)) LEFT JOIN ( SELECT size AS coverSize,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, (" + str7 + ") AS coverPath, serverId AS coverId  FROM shareImage WHERE serverId IN (SELECT coverId FROM album WHERE coverId NOT NULL  AND coverId NOT NULL  AND coverId>2147483647) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY coverId)USING(coverId) WHERE coverPath NOT NULL";
        SQL_QUERY_NORMAL_ALBUM_COVER_INFO_BY_SHARE_MEDIA_LOCAL_ID = " SELECT * FROM ( SELECT coverId, 1 AS is_manual_set_cover, _id FROM ( SELECT coverId, _id FROM album WHERE coverId NOT NULL  AND (coverId >= 16777215 AND coverId<67108863) GROUP BY coverId)) LEFT JOIN ( SELECT size AS coverSize,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, (" + str7 + ") AS coverPath, 16777215+_id AS coverId  FROM shareImage WHERE coverId IN (SELECT coverId FROM album WHERE coverId NOT NULL  AND (coverId >= 16777215 AND coverId<67108863)) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))))USING(coverId) WHERE coverPath NOT NULL";
        String str9 = " SELECT _id, coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover  FROM  ( SELECT *  FROM  ( " + String.format(locale, " SELECT coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover FROM (%s)", String.format(locale, " SELECT coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover FROM (%s)", str) + " UNION " + String.format(locale, " SELECT coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover FROM (%s)", str2) + " UNION " + String.format(locale, " SELECT coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover FROM (%s)", str3) + " UNION " + String.format(locale, " SELECT coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover FROM (%s)", str4)) + ") LEFT JOIN ( SELECT " + j.c + ",coverId FROM album WHERE coverId NOT NULL)using(coverId))";
        TABLE_COVER_DETAIL_INFO = str9;
        String str10 = " SELECT * FROM ( SELECT coverId, 1 AS is_manual_set_cover, 2147383647+_id AS _id FROM shareAlbum WHERE coverId NOT NULL  AND coverId>2147483647) LEFT JOIN ( SELECT size AS coverSize,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, sha1 AS coverSha1, (" + str7 + ") AS coverPath, serverId AS coverId  FROM shareImage WHERE serverId IN (SELECT coverId FROM shareAlbum WHERE coverId NOT NULL  AND coverId>2147483647) AND (serverStatus = 'custom' OR serverStatus = 'recovery'))USING(coverId) GROUP BY coverId)";
        SQL_QUERY_SHARE_ALBUM_COVER_INFO_BY_SERVER_ID = str10;
        String str11 = " SELECT _id, coverId, coverSize, coverPath, coverSyncState, coverSha1, is_manual_set_cover  FROM  ( SELECT *  FROM  ( " + str10 + " WHERE coverPath NOT NULL)";
        TABLE_COVER_DETAIL_INFO_BY_SHARE_ALBUM = str11;
        String str12 = "SELECT * FROM (SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, realDateModified, serverTag, editedColumns, localPath, 0 AS is_manual_set_cover, %sserverStatus FROM (SELECT * FROM album%s)) LEFT JOIN ( SELECT _id AS coverId,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState,sha1 AS coverSha1,size AS coverSize,%s%s%slocalGroupId AS _id,(" + str7 + ") AS coverPath FROM cloud WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))%s%s GROUP BY localGroupId) USING(" + j.c + ")";
        SQL_QUERY_NORMAL_ALBUM_DEFAULT_COVER_MODE_FORMAT = str12;
        SQL_QUERY_NORMAL_ALBUM_FORMAT_BY_NEED_COVER = String.format(locale, "SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,( CASE WHEN secondTable.coverId NOT NULL THEN secondTable.coverId ELSE firstTable.coverId END ) AS coverId,( CASE WHEN secondTable.coverSyncState NOT NULL THEN secondTable.coverSyncState ELSE firstTable.coverSyncState END ) AS coverSyncState,( CASE WHEN secondTable.coverSize NOT NULL THEN secondTable.coverSize ELSE firstTable.coverSize END ) AS coverSize,( CASE WHEN secondTable.coverPath NOT NULL THEN secondTable.coverPath ELSE firstTable.coverPath END ) AS coverPath,( CASE WHEN secondTable.coverSha1 NOT NULL THEN secondTable.coverSha1 ELSE firstTable.coverSha1 END ) AS coverSha1,( CASE WHEN secondTable.is_manual_set_cover NOT NULL THEN secondTable.is_manual_set_cover ELSE firstTable.is_manual_set_cover END ) AS is_manual_set_cover FROM ( %s ) AS firstTable LEFT JOIN (%s) AS secondTable %s ", str12, str9, "USING(_id)");
        SQL_QUERY_SHARE_ALBUM_FORMAT_BY_NEED_COVER = String.format(locale, "SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,( CASE WHEN secondTable.coverId NOT NULL THEN secondTable.coverId ELSE firstTable.coverId END ) AS coverId,( CASE WHEN secondTable.coverSyncState NOT NULL THEN secondTable.coverSyncState ELSE firstTable.coverSyncState END ) AS coverSyncState,( CASE WHEN secondTable.coverSize NOT NULL THEN secondTable.coverSize ELSE firstTable.coverSize END ) AS coverSize,( CASE WHEN secondTable.coverPath NOT NULL THEN secondTable.coverPath ELSE firstTable.coverPath END ) AS coverPath,( CASE WHEN secondTable.coverSha1 NOT NULL THEN secondTable.coverSha1 ELSE firstTable.coverSha1 END ) AS coverSha1,( CASE WHEN secondTable.is_manual_set_cover NOT NULL THEN secondTable.is_manual_set_cover ELSE firstTable.is_manual_set_cover END ) AS is_manual_set_cover FROM ( %s ) AS firstTable LEFT JOIN (%s) AS secondTable %s ", "SELECT * FROM (SELECT 2147383647+_id AS _id, sharealbum.fileName AS name, attributes AS attributes, dateTaken AS dateTaken, dateModified AS dateModified, sortInfo AS sortInfo, json_object('babyInfoJson',babyInfoJson,'peopleId',peopleId,'sharerInfo',sharerInfo) AS extra,localFlag AS localFlag, serverId AS serverId, 0 AS realDateModified, serverTag AS serverTag, serverStatus AS serverStatus, NULL AS editedColumns, NULL AS localPath, 0 AS is_manual_set_cover, sortBy, serverStatus FROM shareAlbum  WHERE serverId IS NOT NULL ) LEFT JOIN (SELECT %s%s%s2147383647+localGroupId AS _id FROM shareImage WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) %s%s GROUP BY localGroupId) USING (_id)", str11, "USING(_id)");
        SQL_ALBUM_VIDEO = "SELECT 2147483647 AS _id, 'VIDEO' AS name, 0 AS attributes, 998 AS dateTaken, 998 AS dateModified, %s AS sortBy, '%s' AS sortInfo , NULL AS extra, 0 AS localFlag, 'custom' AS serverStatus, -2147483647 AS serverId, 0 AS realDateModified, NULL AS serverTag, NULL AS editedColumns, NULL AS localPath, %s%s0 AS size FROM " + String.format(locale, "( SELECT _id,localFlag,localFile,thumbnailFile,microthumbfile,localGroupId,size,mixedDateTime,dateTaken,dateModified,serverType,sha1,serverStatus FROM cloud%s)", " WHERE serverType= 2 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))  AND (localGroupId!=-1000) AND localGroupId IN ( SELECT _id FROM album WHERE (attributes & 16 = 0) AND (attributes & 2048 = 0))%s");
        SQL_ALBUM_ALL_PHOTOS = "SELECT 2147483644 AS _id, 'ALL_PHOTOS' AS name, 0 AS attributes, 1001 AS dateTaken, 1001 AS dateModified, %s AS sortBy, '%s' AS sortInfo, NULL AS extra, 0 AS localFlag, 'custom' AS serverStatus, -2147483644 AS serverId, 0 AS realDateModified, NULL AS serverTag, NULL AS editedColumns, NULL AS localPath, %s%s0 AS size FROM " + String.format(locale, "( SELECT _id,localFlag,localFile,thumbnailFile,microthumbfile,localGroupId,size,mixedDateTime,dateTaken,dateModified,serverType,sha1,serverStatus FROM cloud%s)", " WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))  AND (localGroupId!=-1000) AND localGroupId IN ( SELECT _id FROM album WHERE (attributes & 16 = 0) AND (attributes & 2048 = 0))%s%s");
        StringBuilder sb5 = new StringBuilder();
        sb5.append("SELECT 2147483645 AS _id, 'SCREENSHOTS OR RECORDERS' AS name, %s AS attributes, 996 AS dateTaken, 996 AS dateModified, %s AS sortBy, '%s' AS sortInfo, NULL AS extra, 0 AS localFlag, 'custom' AS serverStatus, -2147483645 AS serverId, 0 AS realDateModified, NULL AS serverTag, NULL AS editedColumns, NULL AS localPath, %s%s0 AS size FROM ");
        sb5.append(String.format(locale, "( SELECT _id,localFlag,localFile,thumbnailFile,microthumbfile,localGroupId,size,mixedDateTime,dateTaken,dateModified,serverType,sha1,serverStatus FROM cloud%s)", " WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND " + InternalContract$Cloud.ALIAS_IS_REAL_SCREENSHOT_OR_RECORDER + "%s%s"));
        SQL_ALBUM_SCREENSHOTS_AND_RECORDERS = sb5.toString();
        SQL_UNION_VIDEO_ALBUM = " UNION " + String.format(locale, "SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,coverId, coverSyncState, coverSize, coverPath, coverSha1,is_manual_set_cover FROM ( %s )", str5);
        SQL_UNION_ALL_PHOTOS_ALBUM = " UNION " + String.format(locale, "SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,coverId, coverSyncState, coverSize, coverPath, coverSha1,is_manual_set_cover FROM ( %s )", str6);
        SQL_UNION_FAVORITES_ALBUM = " UNION " + String.format(locale, "SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,coverId, coverSyncState, coverSize, coverPath, coverSha1,is_manual_set_cover FROM ( %s )", "SELECT 2147483642 AS _id, 'FAVORITES' AS name, 0 AS attributes, 1000 AS dateTaken, 1000 AS dateModified, %s AS sortBy, '%s' AS sortInfo, NULL AS extra, 0 AS localFlag, 'custom' AS serverStatus, -2147483642 AS serverId, 0 AS realDateModified, NULL AS serverTag, NULL AS editedColumns, NULL AS localPath, %scount(_id) AS photoCount, 0 AS size FROM cloud LEFT JOIN ( SELECT cloud_id AS _id,isFavorite FROM favorites)using(_id)WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000) AND (serverType IN (1,2) AND isFavorite NOT NULL AND isFavorite > 0) AND (localGroupId in (SELECT _id FROM album WHERE attributes&16=0 AND attributes&2048=0))%s%s");
        SQL_UNION_SCREENSHOTS_RECORDERS = " UNION " + String.format(locale, "SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,coverId, coverSyncState, coverSize, coverPath, coverSha1,is_manual_set_cover FROM ( %s )", sb);
        VIRTUAL_ALBUM_COVER_TABLE_BY_NORMAL_COVER_ID = "SELECT _id AS coverId, %s AS albumId, (" + str7 + ") AS coverPath,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, " + MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE + " AS coverSize, sha1 AS coverSha1, (CASE WHEN COUNT(1)>0 THEN 1 ELSE 0 END ) AS is_manual_set_cover FROM cloud WHERE (" + j.c + " = %s OR serverId = %s) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
        VIRTUAL_ALBUM_COVER_TABLE_BY_SHARE_COVER_ID = "SELECT 16777215+_id AS coverId, %s AS albumId, (" + str7 + ") AS coverPath,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState, " + MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE + " AS coverSize, sha1 AS coverSha1, (CASE WHEN COUNT(1)>0 THEN 1 ELSE 0 END ) AS is_manual_set_cover FROM shareImage WHERE (16777215" + Marker.ANY_NON_NULL_MARKER + j.c + " = %s) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
        SHARED_ALBUM_PROJECTION = new String[]{j.c, "creatorId", MiStat.Param.COUNT, "nickname"};
    }

    public static Map<String, Boolean> isQueryColumns(String[] strArr, String... strArr2) {
        if (strArr == null || strArr.length <= 0) {
            return null;
        }
        HashMap hashMap = new HashMap((int) ((strArr.length * 0.75f) + 1.0f));
        if (Marker.ANY_MARKER.equals(strArr[0].trim())) {
            for (String str : strArr2) {
                hashMap.put(str, Boolean.TRUE);
            }
            return hashMap;
        }
        for (String str2 : strArr) {
            if (hashMap.size() == strArr2.length) {
                return hashMap;
            }
            int length = strArr2.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    String str3 = strArr2[i];
                    if (str3.equals(str2)) {
                        hashMap.put(str3, Boolean.TRUE);
                        break;
                    }
                    i++;
                }
            }
        }
        return hashMap;
    }

    public static Cursor queryFaceAlbumCover(SupportSQLiteDatabase supportSQLiteDatabase) {
        return queryFaceAlbumCover(supportSQLiteDatabase, 0);
    }

    public static Cursor queryFaceAlbumCover(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        AbstractWindowedCursor abstractWindowedCursor = new AbstractWindowedCursor() { // from class: com.miui.gallery.provider.album.AlbumManager.1
            @Override // android.database.AbstractCursor, android.database.Cursor
            public String[] getColumnNames() {
                return new String[0];
            }

            @Override // android.database.AbstractCursor, android.database.Cursor
            public int getCount() {
                return 0;
            }
        };
        if (isIncludeFaceAlbum(false)) {
            abstractWindowedCursor.setExtras(generateFaceAlbumCover(supportSQLiteDatabase, i));
        }
        return abstractWindowedCursor;
    }

    public static Bundle generateFaceAlbumCover(SupportSQLiteDatabase supportSQLiteDatabase, int i) {
        if (i > 5) {
            i = 5;
        } else if (i <= 0) {
            i = 4;
        }
        Cursor query = supportSQLiteDatabase.query(FaceManager.buildTopFaceCoverQuery(), (Object[]) null);
        if (query != null) {
            try {
                ArrayList<? extends Parcelable> arrayList = new ArrayList<>();
                int columnIndex = query.getColumnIndex("coverId");
                int columnIndex2 = query.getColumnIndex("coverPath");
                int columnIndex3 = query.getColumnIndex("coverSha1");
                int columnIndex4 = query.getColumnIndex("coverSyncState");
                int columnIndex5 = query.getColumnIndex("coverSize");
                int columnIndex6 = query.getColumnIndex("faceRect");
                while (query.moveToNext()) {
                    FaceAlbumCover faceAlbumCover = new FaceAlbumCover();
                    faceAlbumCover.coverId = query.getLong(columnIndex);
                    faceAlbumCover.coverPath = query.getString(columnIndex2);
                    faceAlbumCover.coverSha1 = query.getString(columnIndex3);
                    faceAlbumCover.coverSyncState = query.getInt(columnIndex4);
                    faceAlbumCover.coverSize = query.getLong(columnIndex5);
                    FaceRegionRectF resolveFrom = FaceRegionRectF.resolveFrom(query.getString(columnIndex6));
                    faceAlbumCover.faceRectF = resolveFrom;
                    if (resolveFrom != null) {
                        arrayList.add(faceAlbumCover);
                    }
                    if (arrayList.size() == i) {
                        break;
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("face_album_cover", arrayList);
                bundle.putInt("face_album_count", query.getCount());
                return bundle;
            } finally {
                query.close();
            }
        }
        return null;
    }

    public static boolean isIncludeFaceAlbum(boolean z) {
        return AIAlbumStatusHelper.isFaceAlbumEnabled() && !z;
    }

    public static boolean isInLocalMode() {
        return GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x006c A[Catch: all -> 0x0088, TryCatch #0 {all -> 0x0088, blocks: (B:8:0x0012, B:16:0x0030, B:21:0x003f, B:22:0x0048, B:26:0x0055, B:27:0x005e, B:31:0x006c, B:32:0x006f, B:35:0x007e), top: B:40:0x0012 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getExcludeEmptyAlbumSelection(boolean r3, boolean r4, boolean r5, boolean r6) {
        /*
            if (r4 != 0) goto L8
            if (r6 != 0) goto L8
            if (r5 != 0) goto L8
            r3 = 0
            return r3
        L8:
            miuix.core.util.Pools$Pool r0 = miuix.core.util.Pools.getStringBuilderPool()
            java.lang.Object r0 = r0.acquire()
            java.lang.StringBuilder r0 = (java.lang.StringBuilder) r0
            java.lang.String r1 = "("
            r0.append(r1)     // Catch: java.lang.Throwable -> L88
            java.lang.String r1 = "photoCount"
            r0.append(r1)     // Catch: java.lang.Throwable -> L88
            java.lang.String r1 = " >0"
            r0.append(r1)     // Catch: java.lang.Throwable -> L88
            if (r3 != 0) goto L7e
            if (r4 == 0) goto L2a
            if (r6 == 0) goto L2a
            if (r5 == 0) goto L2a
            goto L7e
        L2a:
            r3 = 0
            r1 = 1
            java.lang.String r2 = " OR ("
            if (r5 == 0) goto L39
            r0.append(r2)     // Catch: java.lang.Throwable -> L88
            java.lang.String r3 = com.miui.gallery.provider.InternalContract$Album.SELECTION_EXCLUDE_EMPTY_USER_CREATE_ALBUMS     // Catch: java.lang.Throwable -> L88
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
            r3 = r1
        L39:
            java.lang.String r5 = " AND "
            if (r4 == 0) goto L51
            if (r3 == 0) goto L48
            r0.append(r5)     // Catch: java.lang.Throwable -> L88
            java.lang.String r3 = com.miui.gallery.provider.InternalContract$Album.SELECTION_EXCLUDE_EMPTY_THIRD_PARTY_ALBUMS     // Catch: java.lang.Throwable -> L88
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
            goto L50
        L48:
            r0.append(r2)     // Catch: java.lang.Throwable -> L88
            java.lang.String r3 = com.miui.gallery.provider.InternalContract$Album.SELECTION_EXCLUDE_EMPTY_THIRD_PARTY_ALBUMS     // Catch: java.lang.Throwable -> L88
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
        L50:
            r3 = r1
        L51:
            if (r6 == 0) goto L67
            if (r3 == 0) goto L5e
            r0.append(r5)     // Catch: java.lang.Throwable -> L88
            java.lang.String r4 = com.miui.gallery.provider.InternalContract$Album.SELECTION_EXCLUDE_EMPTY_SYSTEM_ALBUMS     // Catch: java.lang.Throwable -> L88
            r0.append(r4)     // Catch: java.lang.Throwable -> L88
            goto L67
        L5e:
            r0.append(r2)     // Catch: java.lang.Throwable -> L88
            java.lang.String r3 = com.miui.gallery.provider.InternalContract$Album.SELECTION_EXCLUDE_EMPTY_SYSTEM_ALBUMS     // Catch: java.lang.Throwable -> L88
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
            goto L68
        L67:
            r1 = r3
        L68:
            r3 = 41
            if (r1 == 0) goto L6f
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
        L6f:
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
            java.lang.String r3 = r0.toString()     // Catch: java.lang.Throwable -> L88
        L76:
            miuix.core.util.Pools$Pool r4 = miuix.core.util.Pools.getStringBuilderPool()
            r4.release(r0)
            return r3
        L7e:
            java.lang.String r3 = ")"
            r0.append(r3)     // Catch: java.lang.Throwable -> L88
            java.lang.String r3 = r0.toString()     // Catch: java.lang.Throwable -> L88
            goto L76
        L88:
            r3 = move-exception
            miuix.core.util.Pools$Pool r4 = miuix.core.util.Pools.getStringBuilderPool()
            r4.release(r0)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.album.AlbumManager.getExcludeEmptyAlbumSelection(boolean, boolean, boolean, boolean):java.lang.String");
    }

    public static String getExcludeScreenshotRecorderSelection(boolean z) {
        if (z) {
            return " WHERE " + InternalContract$Album.SELECTION_NON_REAL_SCREENSHOT_RECORDER_LOCAL_PATH;
        }
        return "";
    }

    public static String getMediaTypeSelection(Integer num) {
        return num != null ? (num.intValue() == 2 || num.intValue() == 1) ? String.format(Locale.US, " AND %s = %s ", "serverType", num.toString()) : "" : "";
    }

    public static Map<Long, Long> getAlbumAttributes() {
        return ALBUM_ATTRIBUTES;
    }

    public static Map<Long, Long> getAlbumServerIds() {
        return ALBUM_SERVER_ID;
    }

    public static long packageAttributeBit(long j, boolean z, boolean z2) {
        long longValue = ALBUM_ATTRIBUTES.get(Long.valueOf(j)).longValue();
        if (!z) {
            j = 0;
        }
        if (!z2) {
            longValue = 0;
        }
        return j | longValue;
    }

    public static List<Long> getAlbumSyncAttributes() {
        return ALBUM_SYNC_ATTRIBUTES;
    }

    public static long doQueryScreenshotsAlbumId(Context context) {
        Long l = (Long) SafeDBUtil.safeQuery(context, GalleryContract.Album.URI, new String[]{j.c}, "serverId = ?", new String[]{String.valueOf(2L)}, (String) null, AlbumManager$$ExternalSyntheticLambda0.INSTANCE);
        if (l != null) {
            return l.longValue();
        }
        return -1L;
    }

    public static /* synthetic */ Long lambda$doQueryScreenshotsAlbumId$0(Cursor cursor) {
        long j = -1;
        long j2 = (cursor == null || !cursor.moveToFirst()) ? -1L : cursor.getLong(0);
        if (j2 > 0) {
            j = j2;
        }
        return Long.valueOf(j);
    }

    public static synchronized long queryScreenshotsAlbumId(Context context) {
        long j;
        synchronized (AlbumManager.class) {
            if (sScreenshotsAlbumId == -1) {
                sScreenshotsAlbumId = doQueryScreenshotsAlbumId(context);
            }
            j = sScreenshotsAlbumId;
        }
        return j;
    }

    public static Cursor query(SupportSQLiteDatabase supportSQLiteDatabase, String[] strArr, String str, String[] strArr2, long j) {
        return query(supportSQLiteDatabase, strArr, str, strArr2, j, null);
    }

    public static Cursor query(SupportSQLiteDatabase supportSQLiteDatabase, String[] strArr, String str, String[] strArr2, long j, CancellationSignal cancellationSignal) {
        return query(supportSQLiteDatabase, strArr, str, strArr2, null, null, null, null, false, j, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:107:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0235  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x0239 A[Catch: all -> 0x02bf, TryCatch #2 {all -> 0x02bf, blocks: (B:97:0x01a8, B:112:0x01fa, B:114:0x0206, B:116:0x020b, B:120:0x022c, B:121:0x0231, B:124:0x0239, B:126:0x024d, B:128:0x0253, B:131:0x025b, B:133:0x0261, B:137:0x0271, B:139:0x02a8, B:140:0x02ab, B:134:0x0267, B:135:0x026e, B:104:0x01b8, B:106:0x01c0), top: B:153:0x0179 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0253 A[Catch: all -> 0x02bf, TryCatch #2 {all -> 0x02bf, blocks: (B:97:0x01a8, B:112:0x01fa, B:114:0x0206, B:116:0x020b, B:120:0x022c, B:121:0x0231, B:124:0x0239, B:126:0x024d, B:128:0x0253, B:131:0x025b, B:133:0x0261, B:137:0x0271, B:139:0x02a8, B:140:0x02ab, B:134:0x0267, B:135:0x026e, B:104:0x01b8, B:106:0x01c0), top: B:153:0x0179 }] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x02a8 A[Catch: all -> 0x02bf, TryCatch #2 {all -> 0x02bf, blocks: (B:97:0x01a8, B:112:0x01fa, B:114:0x0206, B:116:0x020b, B:120:0x022c, B:121:0x0231, B:124:0x0239, B:126:0x024d, B:128:0x0253, B:131:0x025b, B:133:0x0261, B:137:0x0271, B:139:0x02a8, B:140:0x02ab, B:134:0x0267, B:135:0x026e, B:104:0x01b8, B:106:0x01c0), top: B:153:0x0179 }] */
    /* JADX WARN: Removed duplicated region for block: B:94:0x017b  */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r9v12, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r9v2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.database.Cursor query(androidx.sqlite.db.SupportSQLiteDatabase r27, java.lang.String[] r28, java.lang.String r29, java.lang.String[] r30, java.lang.String r31, java.lang.String r32, java.lang.String r33, java.lang.String r34, boolean r35, long r36, android.os.CancellationSignal r38) {
        /*
            Method dump skipped, instructions count: 715
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.album.AlbumManager.query(androidx.sqlite.db.SupportSQLiteDatabase, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean, long, android.os.CancellationSignal):android.database.Cursor");
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x0148  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x015e A[Catch: all -> 0x0196, TryCatch #0 {all -> 0x0196, blocks: (B:59:0x00af, B:61:0x00b5, B:69:0x00c8, B:74:0x00da, B:79:0x00ec, B:84:0x00fe, B:89:0x0110, B:101:0x013d, B:106:0x014f, B:107:0x0158, B:109:0x015e, B:114:0x016d, B:116:0x0187, B:117:0x018a, B:91:0x011c, B:96:0x012d), top: B:123:0x00af }] */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getExcludeAlbumSelection(java.lang.String r19, boolean r20, long r21) {
        /*
            Method dump skipped, instructions count: 415
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.album.AlbumManager.getExcludeAlbumSelection(java.lang.String, boolean, long):java.lang.String");
    }

    public static String getQueryVirtualTable(long j, String str, String str2, boolean z, boolean z2, String str3) {
        String queryUnionVideoAlbumSql;
        boolean z3 = true;
        boolean z4 = (2147483648L & j) != 0;
        boolean z5 = (FileSize.GB_COEFFICIENT & j) != 0;
        boolean z6 = z4 || (j & 983040) == 983040;
        boolean z7 = z6 || (262144 & j) != 0;
        boolean z8 = z6 || (131072 & j) != 0;
        boolean z9 = z6 || (65536 & j) != 0 || z5;
        if (!z6 && (j & 524288) == 0) {
            z3 = false;
        }
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        String str4 = "";
        if (z9) {
            try {
                queryUnionVideoAlbumSql = getQueryUnionVideoAlbumSql(z, z2, str);
            } catch (Throwable th) {
                Pools.getStringBuilderPool().release(acquire);
                throw th;
            }
        } else {
            queryUnionVideoAlbumSql = str4;
        }
        acquire.append(queryUnionVideoAlbumSql);
        acquire.append(z8 ? getQueryUnionAllPhotosAlbumSql(z, z2, str2, str) : str4);
        acquire.append(z7 ? getQueryUnionFavoritesAlbumSql(z, z2, str2) : str4);
        if (z3) {
            str4 = getQueryUnionScreenshotsRecordsAlbumSql(z, z2, str2, str);
        }
        acquire.append(str4);
        String sb = acquire.toString();
        Pools.getStringBuilderPool().release(acquire);
        return sb;
    }

    public static Album.CoverBean queryDefaultAlbumCover(SupportSQLiteDatabase supportSQLiteDatabase, long j) {
        List<String> albumPathsByCoverSortDate;
        String str;
        if (BaseMiscUtil.isValid(MediaSortDateHelper.getSortDateProvider().getAlbumPathsByCoverSortDate(MediaSortDateHelper.SortDate.CREATE_TIME))) {
            str = "'" + TextUtils.join("', '", albumPathsByCoverSortDate) + "'";
        } else {
            str = null;
        }
        Locale locale = Locale.US;
        String format = String.format(locale, "(" + SQL_QUERY_DEFAULT_COVER_BY_ID + ")", String.format(locale, "max(CASE WHEN localGroupId IN (SELECT _id FROM album WHERE localPath COLLATE NOCASE IN (%s)) THEN  CASE WHEN mixedDateTime NOT NULL THEN mixedDateTime ELSE dateTaken END  ELSE (dateModified) END) AS latest_photo ", str), Long.valueOf(j));
        if (GalleryPreferences.LocalMode.isOnlyShowLocalPhoto()) {
            StringBuilder acquire = Pools.getStringBuilderPool().acquire();
            acquire.append(format.substring(0, format.length() - 1));
            acquire.append(" AND ");
            acquire.append(InternalContract$Cloud.ALIAS_LOCAL_MEDIA);
            acquire.append(")");
            format = acquire.toString();
            Pools.getStringBuilderPool().release(acquire);
        }
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder(format).create());
        if (query == null || !query.moveToFirst()) {
            return null;
        }
        return new Album.CoverBean(query.getLong(query.getColumnIndex("coverId")), query.getString(query.getColumnIndex("coverPath")), query.getString(query.getColumnIndex("coverSha1")), query.getInt(query.getColumnIndex("coverSyncState")), query.getLong(query.getColumnIndex("coverSize")), false);
    }

    public static String getExcludeNonLocalSelection(int i, boolean z) {
        String str;
        if (i == 1) {
            str = InternalContract$Cloud.ALIAS_LOCAL_IMAGE;
        } else if (i == 2) {
            str = InternalContract$Cloud.ALIAS_LOCAL_VIDEO;
        } else {
            str = z ? InternalContract$Cloud.ALIAS_LOCAL_MEDIA : "";
        }
        return z ? String.format(" AND %s ", str) : "";
    }

    public static String getQueryUnionVideoAlbumSql(boolean z, boolean z2, String str) {
        String virtualAlbumSortPosition = GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483647L);
        long virtualAlbumCoverId = GalleryPreferences.Album.getVirtualAlbumCoverId(2147483647L);
        long virtualAlbumSortBy = GalleryPreferences.Album.getVirtualAlbumSortBy(2147483647L, 0L);
        if (-1 != virtualAlbumCoverId && z2) {
            boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(virtualAlbumCoverId);
            Locale locale = Locale.US;
            Object[] objArr = new Object[3];
            objArr[0] = String.format(locale, SQL_ALBUM_VIDEO, Long.valueOf(virtualAlbumSortBy), virtualAlbumSortPosition, SQL_ALBUM_COVER_BY_MODIFIED_TIME, str, getExcludeNonLocalSelection(2, z));
            objArr[1] = isOtherShareMediaId ? String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_SHARE_COVER_ID, Integer.MAX_VALUE, Long.valueOf(virtualAlbumCoverId)) : String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_NORMAL_COVER_ID, Integer.MAX_VALUE, Long.valueOf(virtualAlbumCoverId), Long.valueOf(virtualAlbumCoverId));
            objArr[2] = " ON firstTable._id = secondTable.albumId";
            return String.format(locale, " UNION SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,( CASE WHEN secondTable.coverId NOT NULL THEN secondTable.coverId ELSE firstTable.coverId END ) AS coverId,( CASE WHEN secondTable.coverSyncState NOT NULL THEN secondTable.coverSyncState ELSE firstTable.coverSyncState END ) AS coverSyncState,( CASE WHEN secondTable.coverSize NOT NULL THEN secondTable.coverSize ELSE firstTable.coverSize END ) AS coverSize,( CASE WHEN secondTable.coverPath NOT NULL THEN secondTable.coverPath ELSE firstTable.coverPath END ) AS coverPath,( CASE WHEN secondTable.coverSha1 NOT NULL THEN secondTable.coverSha1 ELSE firstTable.coverSha1 END ) AS coverSha1,( CASE WHEN secondTable.is_manual_set_cover NOT NULL THEN secondTable.is_manual_set_cover ELSE firstTable.is_manual_set_cover END ) AS is_manual_set_cover FROM ( %s ) AS firstTable LEFT JOIN (%s) AS secondTable %s ", objArr);
        }
        Locale locale2 = Locale.US;
        String str2 = SQL_UNION_VIDEO_ALBUM;
        Object[] objArr2 = new Object[5];
        objArr2[0] = Long.valueOf(virtualAlbumSortBy);
        objArr2[1] = virtualAlbumSortPosition;
        objArr2[2] = z2 ? SQL_ALBUM_COVER_BY_MODIFIED_TIME : "-1 AS coverId, 0 AS coverSyncState, NULL AS coverSha1, NULL AS coverSyncState, 0 AS coverSize, NULL AS coverPath, 0 AS is_manual_set_cover, NULL AS latest_photo ,";
        objArr2[3] = str;
        objArr2[4] = getExcludeNonLocalSelection(2, z);
        return String.format(locale2, str2, objArr2);
    }

    public static String getQueryUnionAllPhotosAlbumSql(boolean z, boolean z2, String str, String str2) {
        String virtualAlbumSortPosition = GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483644L);
        long virtualAlbumCoverId = GalleryPreferences.Album.getVirtualAlbumCoverId(2147483644L);
        long virtualAlbumSortBy = GalleryPreferences.Album.getVirtualAlbumSortBy(2147483644L, 0L);
        if (-1 != virtualAlbumCoverId && z2) {
            boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(virtualAlbumCoverId);
            Locale locale = Locale.US;
            Object[] objArr = new Object[3];
            objArr[0] = String.format(locale, SQL_ALBUM_ALL_PHOTOS, Long.valueOf(virtualAlbumSortBy), virtualAlbumSortPosition, SQL_ALBUM_COVER_BY_MODIFIED_TIME, str2, str, getExcludeNonLocalSelection(-1, z));
            objArr[1] = isOtherShareMediaId ? String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_SHARE_COVER_ID, 2147483644, Long.valueOf(virtualAlbumCoverId)) : String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_NORMAL_COVER_ID, 2147483644, Long.valueOf(virtualAlbumCoverId), Long.valueOf(virtualAlbumCoverId));
            objArr[2] = " ON firstTable._id = secondTable.albumId";
            return String.format(locale, " UNION SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,( CASE WHEN secondTable.coverId NOT NULL THEN secondTable.coverId ELSE firstTable.coverId END ) AS coverId,( CASE WHEN secondTable.coverSyncState NOT NULL THEN secondTable.coverSyncState ELSE firstTable.coverSyncState END ) AS coverSyncState,( CASE WHEN secondTable.coverSize NOT NULL THEN secondTable.coverSize ELSE firstTable.coverSize END ) AS coverSize,( CASE WHEN secondTable.coverPath NOT NULL THEN secondTable.coverPath ELSE firstTable.coverPath END ) AS coverPath,( CASE WHEN secondTable.coverSha1 NOT NULL THEN secondTable.coverSha1 ELSE firstTable.coverSha1 END ) AS coverSha1,( CASE WHEN secondTable.is_manual_set_cover NOT NULL THEN secondTable.is_manual_set_cover ELSE firstTable.is_manual_set_cover END ) AS is_manual_set_cover FROM ( %s ) AS firstTable LEFT JOIN (%s) AS secondTable %s ", objArr);
        }
        Locale locale2 = Locale.US;
        String str3 = SQL_UNION_ALL_PHOTOS_ALBUM;
        Object[] objArr2 = new Object[6];
        objArr2[0] = Long.valueOf(virtualAlbumSortBy);
        objArr2[1] = virtualAlbumSortPosition;
        objArr2[2] = z2 ? SQL_ALBUM_COVER_BY_MODIFIED_TIME : "-1 AS coverId, 0 AS coverSyncState, NULL AS coverSha1, NULL AS coverSyncState, 0 AS coverSize, NULL AS coverPath, 0 AS is_manual_set_cover, NULL AS latest_photo ,";
        objArr2[3] = str2;
        objArr2[4] = str;
        objArr2[5] = getExcludeNonLocalSelection(-1, z);
        return String.format(locale2, str3, objArr2);
    }

    public static String getQueryUnionScreenshotsRecordsAlbumSql(boolean z, boolean z2, String str, String str2) {
        String virtualAlbumSortPosition = GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483645L);
        long virtualAlbumCoverId = GalleryPreferences.Album.getVirtualAlbumCoverId(2147483645L);
        long virtualAlbumSortBy = GalleryPreferences.Album.getVirtualAlbumSortBy(2147483645L, 0L);
        Locale locale = Locale.US;
        String format = String.format(locale, "(SELECT attributes FROM album WHERE localPath COLLATE NOCASE IN (%s))", "'" + MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH + "'");
        if (-1 != virtualAlbumCoverId && z2) {
            boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(virtualAlbumCoverId);
            Object[] objArr = new Object[3];
            objArr[0] = String.format(locale, SQL_ALBUM_SCREENSHOTS_AND_RECORDERS, format, Long.valueOf(virtualAlbumSortBy), virtualAlbumSortPosition, SQL_ALBUM_COVER_BY_CREATE_TIME, str2, str, getExcludeNonLocalSelection(-1, z));
            objArr[1] = isOtherShareMediaId ? String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_SHARE_COVER_ID, 2147483645, Long.valueOf(virtualAlbumCoverId)) : String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_NORMAL_COVER_ID, 2147483645, Long.valueOf(virtualAlbumCoverId), Long.valueOf(virtualAlbumCoverId));
            objArr[2] = " ON firstTable._id = secondTable.albumId";
            return String.format(locale, " UNION SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,( CASE WHEN secondTable.coverId NOT NULL THEN secondTable.coverId ELSE firstTable.coverId END ) AS coverId,( CASE WHEN secondTable.coverSyncState NOT NULL THEN secondTable.coverSyncState ELSE firstTable.coverSyncState END ) AS coverSyncState,( CASE WHEN secondTable.coverSize NOT NULL THEN secondTable.coverSize ELSE firstTable.coverSize END ) AS coverSize,( CASE WHEN secondTable.coverPath NOT NULL THEN secondTable.coverPath ELSE firstTable.coverPath END ) AS coverPath,( CASE WHEN secondTable.coverSha1 NOT NULL THEN secondTable.coverSha1 ELSE firstTable.coverSha1 END ) AS coverSha1,( CASE WHEN secondTable.is_manual_set_cover NOT NULL THEN secondTable.is_manual_set_cover ELSE firstTable.is_manual_set_cover END ) AS is_manual_set_cover FROM ( %s ) AS firstTable LEFT JOIN (%s) AS secondTable %s ", objArr);
        }
        String str3 = SQL_UNION_SCREENSHOTS_RECORDERS;
        Object[] objArr2 = new Object[7];
        objArr2[0] = format;
        objArr2[1] = Long.valueOf(virtualAlbumSortBy);
        objArr2[2] = virtualAlbumSortPosition;
        objArr2[3] = z2 ? SQL_ALBUM_COVER_BY_CREATE_TIME : "-1 AS coverId, 0 AS coverSyncState, NULL AS coverSha1, NULL AS coverSyncState, 0 AS coverSize, NULL AS coverPath, 0 AS is_manual_set_cover, NULL AS latest_photo ,";
        objArr2[4] = str2;
        objArr2[5] = str;
        objArr2[6] = getExcludeNonLocalSelection(-1, z);
        return String.format(locale, str3, objArr2);
    }

    public static String getQueryUnionFavoritesAlbumSql(boolean z, boolean z2, String str) {
        String virtualAlbumSortPosition = GalleryPreferences.Album.getVirtualAlbumSortPosition(2147483642L);
        long virtualAlbumCoverId = GalleryPreferences.Album.getVirtualAlbumCoverId(2147483642L);
        long virtualAlbumSortBy = GalleryPreferences.Album.getVirtualAlbumSortBy(2147483642L, 0L);
        if (-1 != virtualAlbumCoverId && z2) {
            boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(virtualAlbumCoverId);
            Locale locale = Locale.US;
            Object[] objArr = new Object[3];
            objArr[0] = String.format(locale, "SELECT 2147483642 AS _id, 'FAVORITES' AS name, 0 AS attributes, 1000 AS dateTaken, 1000 AS dateModified, %s AS sortBy, '%s' AS sortInfo, NULL AS extra, 0 AS localFlag, 'custom' AS serverStatus, -2147483642 AS serverId, 0 AS realDateModified, NULL AS serverTag, NULL AS editedColumns, NULL AS localPath, %scount(_id) AS photoCount, 0 AS size FROM cloud LEFT JOIN ( SELECT cloud_id AS _id,isFavorite FROM favorites)using(_id)WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000) AND (serverType IN (1,2) AND isFavorite NOT NULL AND isFavorite > 0) AND (localGroupId in (SELECT _id FROM album WHERE attributes&16=0 AND attributes&2048=0))%s%s", Long.valueOf(virtualAlbumSortBy), virtualAlbumSortPosition, SQL_ALBUM_COVER_BY_MODIFIED_TIME, str, getExcludeNonLocalSelection(-1, z));
            objArr[1] = isOtherShareMediaId ? String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_SHARE_COVER_ID, 2147483642, Long.valueOf(virtualAlbumCoverId)) : String.format(locale, VIRTUAL_ALBUM_COVER_TABLE_BY_NORMAL_COVER_ID, 2147483642, Long.valueOf(virtualAlbumCoverId), Long.valueOf(virtualAlbumCoverId));
            objArr[2] = " ON firstTable._id = secondTable.albumId";
            return String.format(locale, " UNION SELECT _id, name, attributes, dateTaken, dateModified, sortInfo, extra, localFlag, serverId, localPath, realDateModified, serverTag, serverStatus, editedColumns, serverStatus,photoCount, size, sortBy,( CASE WHEN secondTable.coverId NOT NULL THEN secondTable.coverId ELSE firstTable.coverId END ) AS coverId,( CASE WHEN secondTable.coverSyncState NOT NULL THEN secondTable.coverSyncState ELSE firstTable.coverSyncState END ) AS coverSyncState,( CASE WHEN secondTable.coverSize NOT NULL THEN secondTable.coverSize ELSE firstTable.coverSize END ) AS coverSize,( CASE WHEN secondTable.coverPath NOT NULL THEN secondTable.coverPath ELSE firstTable.coverPath END ) AS coverPath,( CASE WHEN secondTable.coverSha1 NOT NULL THEN secondTable.coverSha1 ELSE firstTable.coverSha1 END ) AS coverSha1,( CASE WHEN secondTable.is_manual_set_cover NOT NULL THEN secondTable.is_manual_set_cover ELSE firstTable.is_manual_set_cover END ) AS is_manual_set_cover FROM ( %s ) AS firstTable LEFT JOIN (%s) AS secondTable %s ", objArr);
        }
        Locale locale2 = Locale.US;
        String str2 = SQL_UNION_FAVORITES_ALBUM;
        Object[] objArr2 = new Object[5];
        objArr2[0] = Long.valueOf(virtualAlbumSortBy);
        objArr2[1] = virtualAlbumSortPosition;
        objArr2[2] = z2 ? SQL_ALBUM_COVER_BY_MODIFIED_TIME : "-1 AS coverId, 0 AS coverSyncState, NULL AS coverSha1, NULL AS coverSyncState, 0 AS coverSize, NULL AS coverPath, 0 AS is_manual_set_cover, NULL AS latest_photo ,";
        objArr2[3] = str;
        objArr2[4] = getExcludeNonLocalSelection(-1, z);
        return String.format(locale2, str2, objArr2);
    }

    public static Cursor queryShareAll(Context context, SupportSQLiteDatabase supportSQLiteDatabase, String[] strArr, String str, String[] strArr2, String str2) {
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(context);
        if (xiaomiAccount == null) {
            return null;
        }
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder(String.format("(SELECT * FROM (SELECT 2147383647+_id AS _id, creatorId, albumId, serverId, nickname FROM shareAlbum LEFT JOIN (SELECT user_id AS creatorId, miliao_nick AS nickname FROM userInfo) USING (creatorId)) LEFT JOIN (SELECT COUNT(*) AS count, albumId FROM shareUser GROUP BY albumId) USING (albumId) UNION SELECT * FROM (SELECT * FROM (SELECT _id, '%s' AS creatorId, serverId AS albumId, serverId FROM album ) LEFT JOIN (SELECT user_id AS creatorId, %s AS nickname FROM userInfo) USING (creatorId)) LEFT JOIN (SELECT COUNT(*) AS count, albumId FROM cloudUser GROUP BY albumId) USING (albumId))", xiaomiAccount.name, DatabaseUtils.sqlEscapeString(context.getResources().getString(R.string.album_owner_share)))).columns(strArr).selection(str, strArr2).orderBy(str2).create());
    }
}
