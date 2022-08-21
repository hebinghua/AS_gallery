package com.miui.gallery.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.BabyLockWallpaperDataManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.peoplecover.PeopleCoverManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.GalleryTimeUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.os.Rom;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class FaceManager {
    public static final String[] BASIC_PEOPLE_INFO_PROJECTION;
    public static final String[] CLOUD_IMAGE_OF_FACE_PROJECTION;
    public static final String FACE_COVER_ALIAS_DISPLAYABLE;
    public static final String GROUP_LOCAL_FLAG_SYNCED_CREATED_RENAME;
    public static final String[] ID_COUNT_PROJECTION;
    public static final int LEAST_FACE_COUNT_OF_DISPLAY_PEOPLE;
    public static final String LOCAL_FILE_ALIAS;
    public static final String LOCAL_FLAG_SYNCED;
    public static final String MICRO_THUMBNAIL_ALIAS;
    public static final String Other_SHARED_GROUP_LOCAL_FLAG_SYNCED;
    public static final String[] PEOPLE_ALBUM_SERVER_ID_PROJECTION;
    public static final String[] PEOPLE_RELATION_PROJECTION;
    public static final String PEOPLE_USER_DEFINE_RELATION_SELECTION;
    public static final String PERSON_VISIBLE_CONDITION;
    public static final String PHOTO_LOCAL_FLAG_CREATE_MOVED_SYNCED;
    public static final String SQL_FACE_ALBUM_COVER;
    public static final String[] SQL_FACE_ALBUM_COVER_PROJECTION;
    public static final String[] SQL_OLDEST_FACE_OF_ALBUM_PROJECTION;
    public static final String SQL_ONE_PERSON_ALBUM_ITEM;
    public static final String SQL_ONE_PERSON_ALBUM_NORMAL;
    public static final String SQL_ONE_PERSON_ALBUM_RECOMMEND;
    public static final String SQL_PERSONS_ALBUM;
    public static final String SQL_PERSONS_ALBUM_ALL_BASIC;
    public static final String THUMBNAIL_ALIAS;

    public static String buildPeopleTagQuery() {
        return "SELECT serverId, eTag FROM (SELECT serverId FROM peopleface WHERE type = 'PEOPLE' AND ( visibilityType = 1 OR visibilityType =4 OR (visibilityType =2 AND groupId is null)) AND localFlag NOT IN ( 2)) serverTable JOIN (SELECT groupId AS peopleId, MAX(eTag) as eTag FROM peopleface GROUP BY groupId) tagTable  ON serverTable.serverId = tagTable.peopleId";
    }

    static {
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s NOT NULL and %s != '')", "thumbnailFile", "thumbnailFile");
        THUMBNAIL_ALIAS = format;
        MICRO_THUMBNAIL_ALIAS = String.format(locale, "(%s NOT NULL and %s != '')", "microthumbfile", "microthumbfile");
        String format2 = String.format(locale, "(%s NOT NULL and %s != '')", "localFile", "localFile");
        LOCAL_FILE_ALIAS = format2;
        String str = " CASE WHEN " + format + " THEN thumbnailFile WHEN " + format2 + " THEN localFile ELSE microthumbfile END ";
        FACE_COVER_ALIAS_DISPLAYABLE = str;
        SQL_FACE_ALBUM_COVER = "photo_id AS coverId, " + str + " AS coverPath, sha1 AS coverSha1, faceXScale||' '||faceYScale||' '||faceWScale||' '||faceHScale||' '||exifOrientation AS faceRect, 0 AS coverSyncState, " + MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE + " AS coverSize";
        int i = Rom.IS_ALPHA ? 1 : 2;
        LEAST_FACE_COUNT_OF_DISPLAY_PEOPLE = i;
        PERSON_VISIBLE_CONDITION = String.format(locale, "(visibilityType = 1 OR visibilityType = 4 OR faceCount >= %d AND (visibilityType IS NULL OR visibilityType = 0))", Integer.valueOf(i));
        SQL_PERSONS_ALBUM_ALL_BASIC = " SELECT _id, peopleName, se AS serverId," + buildCoverSelection("coverCloudId", "cId") + " AS photo_id, " + buildCoverSelection("coverSha1", "sha1") + " AS sha1, " + buildCoverSelection("coverMicroThumb", "cm") + " AS microthumbfile, " + buildCoverSelection("coverThumb", "ch") + " AS thumbnailFile, " + buildCoverSelection("coveOrigin", "cl") + " AS localFile, " + buildCoverSelection("cover_orientation", "co") + " AS exifOrientation, " + buildCoverSelection("coverFaceXScale", "fx") + " AS faceXScale, " + buildCoverSelection("coverFaceYScale", "fy") + " AS faceYScale, " + buildCoverSelection("coverFaceWScale", "fw") + " AS faceWScale, " + buildCoverSelection("coverFaceHScale", "fh") + " AS faceHScale, relationType, relationText, visibilityType, sum(faceCount) as faceCount, " + buildCoverSelection("coverSize", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE) + " AS size,  COUNT(*) FROM (   SELECT _id, peopleName, serverId as se, cId, sha1, cm, ch, cl, co, fx, fy, fw, fh, size, peopleFace.relationType, peopleFace.relationText, peopleFace.visibilityType, maxFaceScore, peopleFace.selectCoverId, faceCount,(CASE WHEN NOT(peopleName IS NULL) THEN 1 ELSE 2 END) AS order_by_notnull_null FROM peopleFace  JOIN  (SELECT pg, COUNT(*) as faceCount, cId, sha1, cd, cm, ch, cl, co, fx, fy, fw, fh, size, MAX(faceCoverScore) as maxFaceScore FROM           (SELECT serverId, faceXScale AS fx, faceYScale AS fy, faceWScale AS fw, faceHScale AS fh, size, groupId AS pg, photo_id AS cId, sha1, mixedDateTime AS cd, microthumbfile AS cm, thumbnailFile AS ch, localFile AS cl, exifOrientation as co, faceCoverScore            FROM extended_faceImage           WHERE localFlag =0 AND serverStatus='normal'            GROUP BY serverId            ORDER BY mixedDateTime DESC  ) temp1  GROUP BY pg  ) temp2  ON type = 'PEOPLE' AND %s AND se = pg  UNION ALL SELECT _id, peopleName, serverId as se, cId asphoto_id, sha1, cm, ch, cl, co, fx, fy, fw, fh, size, peopleFace.relationType, peopleFace.relationText, peopleFace.visibilityType, maxFaceScore, peopleFace.selectCoverId,faceCount,(CASE WHEN NOT(peopleName IS NULL) THEN 1 ELSE 2 END) AS order_by_notnull_null FROM  peopleFace  JOIN  (SELECT lpg, COUNT(*) as faceCount, cId, sha1, cd, cm, ch, cl, co, fx, fy, fw, fh, size, MAX(faceCoverScore) as maxFaceScore FROM           (SELECT serverId, faceXScale AS fx, faceYScale AS fy, faceWScale AS fw, faceHScale AS fh, localGroupId AS lpg, photo_id AS cId, sha1, mixedDateTime AS cd, microthumbfile AS cm, thumbnailFile AS ch, size, localFile AS cl, exifOrientation as co, faceCoverScore            FROM extended_faceImage           WHERE localFlag =5            GROUP BY serverId            ORDER BY mixedDateTime DESC )  temp1  GROUP BY lpg  ) temp2  ON type = 'PEOPLE' AND %s AND _id = lpg )  LEFT JOIN " + PeopleCoverManager.getLocalCoverTable() + " ON (se = localSelect.coverGroupId OR _id = localSelect.coverLocalGroupId) LEFT JOIN " + PeopleCoverManager.getUserCoverTable() + " ON selectCoverId = userSelect.coverServerId AND (se = userSelect.coverGroupId OR _id = userSelect.coverLocalGroupId) GROUP BY _id  ORDER BY " + PeopleContactInfo.getRelationOrderSql() + ", " + PeopleContactInfo.getUserDefineRelationOrderSql() + ", order_by_notnull_null ASC, faceCount DESC";
        SQL_ONE_PERSON_ALBUM_NORMAL = String.format(locale, "SELECT * from (extended_faceImage LEFT JOIN favorites ON extended_faceImage.cloud_id = favorites.cloud_id) WHERE (%s) GROUP BY serverId", "(localFlag =0 AND serverStatus='normal' AND groupId = '%s') OR (localFlag =5 AND localGroupId = %s)");
        SQL_ONE_PERSON_ALBUM_RECOMMEND = String.format(locale, "SELECT * from (extended_faceImage LEFT JOIN favorites ON extended_faceImage.cloud_id = favorites.cloud_id) WHERE (%s) GROUP BY serverId", "(localFlag =0 AND serverStatus='normal' AND serverId IN (%s))");
        SQL_ONE_PERSON_ALBUM_ITEM = String.format(locale, "SELECT * from (extended_faceImage LEFT JOIN favorites ON extended_faceImage.cloud_id = favorites.cloud_id) WHERE (%s) GROUP BY serverId", "serverId = '%s'");
        SQL_PERSONS_ALBUM = String.format(locale, "SELECT * from (extended_faceImage LEFT JOIN favorites ON extended_faceImage.cloud_id = favorites.cloud_id) WHERE (%s) GROUP BY serverId", "(localFlag =0 AND serverStatus='normal' AND groupId in (%s)) OR (localFlag =5 AND localGroupId in (%s))");
        CLOUD_IMAGE_OF_FACE_PROJECTION = new String[]{Marker.ANY_MARKER};
        ID_COUNT_PROJECTION = new String[]{j.c};
        PEOPLE_ALBUM_SERVER_ID_PROJECTION = new String[]{"serverId"};
        StringBuilder sb = new StringBuilder();
        sb.append(" CASE WHEN ");
        sb.append(InternalContract$Cloud.ALIAS_THUMBNAIL_VALID);
        sb.append(" THEN ");
        sb.append("thumbnailFile");
        sb.append(" WHEN ");
        sb.append(InternalContract$Cloud.ALIAS_ORIGIN_FILE_VALID);
        sb.append(" THEN ");
        sb.append("localFile");
        sb.append(" ELSE ");
        sb.append("microthumbfile");
        sb.append(" END AS ");
        sb.append("coverPath");
        sb.append(", ");
        sb.append("exifOrientation");
        sb.append(", ");
        sb.append("faceXScale");
        sb.append(", ");
        sb.append("faceYScale");
        sb.append(", ");
        sb.append("faceWScale");
        sb.append(", ");
        sb.append("faceHScale");
        SQL_FACE_ALBUM_COVER_PROJECTION = new String[]{sb.toString()};
        SQL_OLDEST_FACE_OF_ALBUM_PROJECTION = new String[]{" min( CASE WHEN dateTaken NOT NULL THEN dateTaken ELSE mixedDateTime END) AS oldest_image_time"};
        PEOPLE_RELATION_PROJECTION = new String[]{"relationType", "relationText"};
        StringBuilder sb2 = new StringBuilder();
        sb2.append("relationType = ");
        sb2.append(PeopleContactInfo.getUserDefineRelationIndex());
        sb2.append(" AND ");
        sb2.append("type = 'PEOPLE' AND ( visibilityType = 1 OR visibilityType =4) AND localFlag NOT IN ( 13, 2)");
        sb2.append(" AND ");
        sb2.append("(groupid IS NULL OR groupid = '' OR groupid = serverId)");
        sb2.append(" GROUP BY relationtext");
        PEOPLE_USER_DEFINE_RELATION_SELECTION = sb2.toString();
        String format3 = String.format(locale, "(%s = %d AND %s = '%s')", "localFlag", 0, "serverStatus", "custom");
        LOCAL_FLAG_SYNCED = format3;
        GROUP_LOCAL_FLAG_SYNCED_CREATED_RENAME = String.format(locale, "(%s OR %s = %d OR %s = %d OR %s = %d)", format3, "localFlag", 8, "localFlag", 10, "localFlag", 16);
        Other_SHARED_GROUP_LOCAL_FLAG_SYNCED = String.format(locale, "(%s = '%s' AND NOT (%s IS NULL))", "serverStatus", "custom", "serverId");
        PHOTO_LOCAL_FLAG_CREATE_MOVED_SYNCED = String.format(locale, "(%s OR %s = %d OR %s = %d OR %s = %d OR %s = %d)", format3, "localFlag", 8, "localFlag", 5, "localFlag", 6, "localFlag", 9);
        BASIC_PEOPLE_INFO_PROJECTION = new String[]{j.c, "serverId", "peopleName", "relationType"};
    }

    public static final String buildCoverSelection(String str, String str2) {
        return String.format(Locale.US, " CASE  WHEN userSelect.coverServerId IS NOT NULL THEN userSelect.%s  WHEN (localSelect.coverServerId IS NULL OR maxFaceScore >0) THEN %s  ELSE localSelect.%s END ", str, str2, str);
    }

    public static String buildAlbumQuery(String[] strArr, String str, String[] strArr2, String str2, String str3) {
        StringBuilder sb = new StringBuilder();
        String join = (strArr == null || strArr.length == 0) ? Marker.ANY_MARKER : TextUtils.join(",", strArr);
        sb.append("SELECT ");
        sb.append(join);
        sb.append(" FROM (");
        sb.append(str3);
        sb.append(")");
        if (!TextUtils.isEmpty(str)) {
            if (strArr2 != null && strArr2.length > 0) {
                str = String.format(Locale.US, str, strArr2);
            }
            sb.append(" WHERE ");
            sb.append(str);
        }
        if (!TextUtils.isEmpty(str2)) {
            sb.append(" order by ");
            sb.append(" ");
            sb.append(str2);
        }
        sb.append(";");
        return sb.toString();
    }

    public static String buildPeopleCoverQuery(String[] strArr, String str, String str2) {
        String format;
        if (str != null) {
            format = String.format(Locale.US, "serverId='%s'", str);
        } else {
            format = String.format(Locale.US, "_id=%s", str2);
        }
        Locale locale = Locale.US;
        String format2 = String.format(locale, "(visibilityType = 1 OR visibilityType = 4 OR faceCount >= %d AND (visibilityType IS NULL OR visibilityType = 0))", 1);
        return buildAlbumQuery(strArr, format, null, null, String.format(locale, SQL_PERSONS_ALBUM_ALL_BASIC, format2, format2));
    }

    public static String buildImageFaceQuery(String[] strArr, String str, String str2) {
        return buildAlbumQuery(strArr, null, null, null, String.format(Locale.US, "SELECT p.serverId, p.faceXScale, p.faceYScale, p.faceWScale, p.faceHScale,    c._id as photo_id, c.serverId as photo_server_id, c.microthumbfile, c.thumbnailFile, c.localFile, c.sha1   FROM peopleface p JOIN facetoimages f JOIN cloud c   WHERE p.serverId='%s' AND photo_server_id='%s'   AND p.type = 'FACE' AND (p.localFlag =0 AND p.serverStatus='normal') AND p.serverId = faceId  AND imageServerId = c.serverId    AND (c.localFlag <>2 AND c.serverStatus='custom') ", str, str2));
    }

    public static String buildPersonsQuery() {
        Locale locale = Locale.US;
        String str = SQL_PERSONS_ALBUM_ALL_BASIC;
        String str2 = PERSON_VISIBLE_CONDITION;
        return String.format(locale, str, str2, str2);
    }

    public static String buildTopFaceCoverQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(SQL_FACE_ALBUM_COVER);
        sb.append(" FROM ( ");
        Locale locale = Locale.US;
        String str = SQL_PERSONS_ALBUM_ALL_BASIC;
        String str2 = PERSON_VISIBLE_CONDITION;
        sb.append(String.format(locale, str, str2, str2));
        sb.append(")");
        return sb.toString();
    }

    public static String buildIgnorePersonsQuery() {
        return String.format(Locale.US, SQL_PERSONS_ALBUM_ALL_BASIC, "visibilityType = 2 AND (groupId IS NULL OR groupId = '' OR groupId = serverId)", "visibilityType = 2 AND (groupId IS NULL OR groupId = '' OR groupId = serverId)");
    }

    public static String buildOnePersonQuery(String[] strArr, String str, String str2, String str3, String str4) {
        return buildAlbumQuery(strArr, str, null, str2, String.format(Locale.US, SQL_ONE_PERSON_ALBUM_NORMAL, str3, str4));
    }

    public static String buildRecommendFacesOfOnePersonQuery(String[] strArr, String str, String[] strArr2) {
        if (strArr2 == null || strArr2.length < 1) {
            strArr2 = new String[1];
        }
        return buildAlbumQuery(strArr, null, null, str, String.format(Locale.US, SQL_ONE_PERSON_ALBUM_RECOMMEND, strArr2[0]));
    }

    public static String buildOnePersonItemQuery(String[] strArr, String[] strArr2) {
        if (strArr2 == null || strArr2.length < 1) {
            strArr2 = new String[1];
        }
        return buildAlbumQuery(strArr, null, null, null, String.format(Locale.US, SQL_ONE_PERSON_ALBUM_ITEM, strArr2[0]));
    }

    public static String buildPersonsItemQuery(String[] strArr, String str, String str2, String[] strArr2) {
        if (strArr2 == null || strArr2.length < 2) {
            strArr2 = new String[2];
        }
        return buildAlbumQuery(strArr, str, null, str2, String.format(SQL_PERSONS_ALBUM, strArr2[0], strArr2[1]));
    }

    public static Cursor queryOnePersonAlbum(SupportSQLiteDatabase supportSQLiteDatabase, String[] strArr, String str, String str2, String[] strArr2, boolean z) {
        if (strArr2 == null || strArr2.length < 2) {
            strArr2 = new String[2];
        }
        try {
            Cursor query = supportSQLiteDatabase.query(buildOnePersonQuery(strArr, str, str2, strArr2[0], strArr2[1]));
            return (!z || query == null || query.getCount() <= 0) ? query : generateGroupHeaders(query, strArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Cursor generateGroupHeaders(Cursor cursor, String[] strArr) {
        if (cursor.getColumnIndex("mixedDateTime") >= 0 && cursor.getColumnIndex("location") >= 0) {
            Integer num = 1;
            LinkedList linkedList = new LinkedList();
            TimelineHeadersGroup timelineHeadersGroup = null;
            int i = 0;
            int i2 = 0;
            while (cursor.moveToNext()) {
                int format = GalleryDateUtils.format(cursor.getLong(cursor.getColumnIndex("mixedDateTime")));
                String string = cursor.getString(cursor.getColumnIndex("location"));
                if (i == 0) {
                    timelineHeadersGroup = new TimelineHeadersGroup();
                    timelineHeadersGroup.start = i2;
                    timelineHeadersGroup.itemLocations.add(string);
                    if (!TextUtils.isEmpty(string)) {
                        timelineHeadersGroup.validLocation = string;
                    }
                    i = format;
                } else if (format == i) {
                    num = Integer.valueOf(num.intValue() + 1);
                    timelineHeadersGroup.itemLocations.add(string);
                    if (TextUtils.isEmpty(timelineHeadersGroup.validLocation) && !TextUtils.isEmpty(string)) {
                        timelineHeadersGroup.validLocation = string;
                    }
                } else {
                    timelineHeadersGroup.count = num.intValue();
                    linkedList.add(timelineHeadersGroup);
                    TimelineHeadersGroup timelineHeadersGroup2 = new TimelineHeadersGroup();
                    timelineHeadersGroup2.start = i2;
                    timelineHeadersGroup2.itemLocations.add(string);
                    if (!TextUtils.isEmpty(string)) {
                        timelineHeadersGroup2.validLocation = string;
                    }
                    i = format;
                    timelineHeadersGroup = timelineHeadersGroup2;
                    num = 1;
                }
                i2++;
                if (i2 == cursor.getCount()) {
                    timelineHeadersGroup.count = num.intValue();
                    linkedList.add(timelineHeadersGroup);
                }
            }
            TimelineHeadersGroup.bindGroup(1, linkedList, cursor);
        }
        return cursor;
    }

    public static PeopleContactInfo queryContactInfoOfOnePerson(long j) {
        String queryAStringColumn = queryAStringColumn(j.c, String.valueOf(j), "peopleContactJsonInfo");
        if (TextUtils.isEmpty(queryAStringColumn)) {
            return null;
        }
        return PeopleContactInfo.fromJson(queryAStringColumn);
    }

    public static String queryPersonName(String str) {
        return queryAStringColumn("serverId", str, "peopleName");
    }

    public static String queryPersonName(long j) {
        return queryAStringColumn(j.c, String.valueOf(j), "peopleName");
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x004d, code lost:
        if (r5 != null) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0058, code lost:
        if (r5 == null) goto L6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
        r5.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005d, code lost:
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String queryAStringColumn(java.lang.String r5, java.lang.String r6, java.lang.String r7) {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            r0.append(r5)
            java.lang.String r5 = " = ? "
            r0.append(r5)
            java.lang.String r5 = r0.toString()
            r0 = 1
            java.lang.String[] r1 = new java.lang.String[r0]
            r2 = 0
            r1[r2] = r7
            r7 = 0
            com.miui.gallery.provider.GalleryDBHelper r3 = com.miui.gallery.provider.GalleryDBHelper.getInstance()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            androidx.sqlite.db.SupportSQLiteDatabase r3 = r3.getReadableDatabase()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            java.lang.String r4 = "peopleFace"
            androidx.sqlite.db.SupportSQLiteQueryBuilder r4 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r4)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            androidx.sqlite.db.SupportSQLiteQueryBuilder r1 = r4.columns(r1)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            java.lang.String[] r0 = new java.lang.String[r0]     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            r0[r2] = r6     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            androidx.sqlite.db.SupportSQLiteQueryBuilder r5 = r1.selection(r5, r0)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            androidx.sqlite.db.SupportSQLiteQuery r5 = r5.create()     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            android.database.Cursor r5 = r3.query(r5)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L57
            if (r5 == 0) goto L4d
            boolean r6 = r5.moveToNext()     // Catch: java.lang.Throwable -> L4a java.lang.Exception -> L58
            if (r6 == 0) goto L4d
            java.lang.String r6 = r5.getString(r2)     // Catch: java.lang.Throwable -> L4a java.lang.Exception -> L58
            r5.close()
            return r6
        L4a:
            r6 = move-exception
            r7 = r5
            goto L51
        L4d:
            if (r5 == 0) goto L5d
            goto L5a
        L50:
            r6 = move-exception
        L51:
            if (r7 == 0) goto L56
            r7.close()
        L56:
            throw r6
        L57:
            r5 = r7
        L58:
            if (r5 == 0) goto L5d
        L5a:
            r5.close()
        L5d:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryAStringColumn(java.lang.String, java.lang.String, java.lang.String):java.lang.String");
    }

    public static Cursor queryAllImagesOfOnePerson(String str) {
        return GalleryApp.sGetAndroidContext().getContentResolver().query(GalleryContract.PeopleFace.ONE_PERSON_URI, CLOUD_IMAGE_OF_FACE_PROJECTION, null, new String[]{str, "-1"}, null);
    }

    public static String queryGroupId(String str) {
        Cursor cursor = null;
        try {
            cursor = GalleryApp.sGetAndroidContext().getContentResolver().query(GalleryContract.PeopleFace.PEOPLE_FACE_URI, new String[]{"groupId"}, "serverId = ?", new String[]{str}, null);
        } catch (Exception unused) {
            if (cursor == null) {
                return "";
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        if (cursor != null && cursor.moveToFirst()) {
            String string = cursor.getString(0);
            cursor.close();
            return string;
        }
        if (cursor == null) {
            return "";
        }
        cursor.close();
        return "";
    }

    public static long queryLocalGroupId(String str) {
        Cursor cursor = null;
        try {
            cursor = GalleryApp.sGetAndroidContext().getContentResolver().query(GalleryContract.PeopleFace.PEOPLE_FACE_URI, new String[]{"localGroupId"}, "serverId = ?", new String[]{str}, null);
        } catch (Exception unused) {
            if (cursor == null) {
                return -1L;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        if (cursor != null && cursor.moveToFirst()) {
            long j = cursor.getLong(0);
            cursor.close();
            return j;
        }
        if (cursor == null) {
            return -1L;
        }
        cursor.close();
        return -1L;
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0057, code lost:
        if (r1 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0059, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0065, code lost:
        if (r1 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0068, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String queryCoverImageOfOnePerson(java.lang.String r9, long r10, com.miui.gallery.util.face.FaceRegionRectF[] r12) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            android.net.Uri r2 = com.miui.gallery.provider.GalleryContract.PeopleFace.PEOPLE_COVER_URI     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            android.net.Uri$Builder r2 = r2.buildUpon()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            boolean r3 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            if (r3 != 0) goto L15
            java.lang.String r10 = "serverId"
            r2.appendQueryParameter(r10, r9)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            goto L28
        L15:
            r3 = 0
            int r9 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1))
            if (r9 <= 0) goto L5d
            java.lang.String r9 = "_id"
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            android.net.Uri$Builder r9 = r2.appendQueryParameter(r9, r10)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            r9.build()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
        L28:
            android.content.Context r9 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            android.content.ContentResolver r3 = r9.getContentResolver()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            android.net.Uri r4 = r2.build()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            java.lang.String[] r5 = com.miui.gallery.provider.FaceManager.SQL_FACE_ALBUM_COVER_PROJECTION     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            r6 = 0
            r7 = 0
            r8 = 0
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            if (r1 == 0) goto L57
            boolean r9 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            if (r9 == 0) goto L57
            r9 = 0
            com.miui.gallery.util.face.FaceRegionRectF r10 = getFacePositionRectOfCoverImage(r1)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            r12[r9] = r10     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            java.lang.String r9 = "coverPath"
            int r9 = r1.getColumnIndex(r9)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            java.lang.String r9 = r1.getString(r9)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L65
            r0 = r9
        L57:
            if (r1 == 0) goto L68
        L59:
            r1.close()
            goto L68
        L5d:
            return r1
        L5e:
            r9 = move-exception
            if (r1 == 0) goto L64
            r1.close()
        L64:
            throw r9
        L65:
            if (r1 == 0) goto L68
            goto L59
        L68:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryCoverImageOfOnePerson(java.lang.String, long, com.miui.gallery.util.face.FaceRegionRectF[]):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0037, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0042, code lost:
        if (r1 != null) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0045, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0035, code lost:
        if (r1 != null) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String queryCoverImageOfOneFace(java.lang.String r9, com.miui.gallery.util.face.FaceRegionRectF[] r10) {
        /*
            java.lang.String r0 = ""
            r1 = 0
            android.content.Context r2 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            android.net.Uri r4 = com.miui.gallery.provider.GalleryContract.PeopleFace.ONE_PERSON_ITEM_URI     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            java.lang.String[] r5 = com.miui.gallery.provider.FaceManager.SQL_FACE_ALBUM_COVER_PROJECTION     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            r6 = 0
            r2 = 1
            java.lang.String[] r7 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            r2 = 0
            r7[r2] = r9     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            java.lang.String r8 = "dateTaken DESC "
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            if (r1 == 0) goto L35
            boolean r9 = r1.moveToNext()     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            if (r9 == 0) goto L35
            com.miui.gallery.util.face.FaceRegionRectF r9 = getFacePositionRectOfCoverImage(r1)     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            r10[r2] = r9     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            java.lang.String r9 = "coverPath"
            int r9 = r1.getColumnIndex(r9)     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            java.lang.String r9 = r1.getString(r9)     // Catch: java.lang.Throwable -> L3b java.lang.Exception -> L42
            r0 = r9
        L35:
            if (r1 == 0) goto L45
        L37:
            r1.close()
            goto L45
        L3b:
            r9 = move-exception
            if (r1 == 0) goto L41
            r1.close()
        L41:
            throw r9
        L42:
            if (r1 == 0) goto L45
            goto L37
        L45:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryCoverImageOfOneFace(java.lang.String, com.miui.gallery.util.face.FaceRegionRectF[]):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x003f, code lost:
        if (r0 != null) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0042, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0032, code lost:
        if (r0 != null) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0034, code lost:
        r0.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long queryTimeOfOldestImagesOfOnePerson(java.lang.String r10) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r3 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            android.content.ContentResolver r4 = r3.getContentResolver()     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            android.net.Uri r5 = com.miui.gallery.provider.GalleryContract.PeopleFace.ONE_PERSON_URI     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            java.lang.String[] r6 = com.miui.gallery.provider.FaceManager.SQL_OLDEST_FACE_OF_ALBUM_PROJECTION     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            r7 = 0
            r3 = 2
            java.lang.String[] r8 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            r3 = 0
            r8[r3] = r10     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            r10 = 1
            java.lang.String r3 = "-1"
            r8[r10] = r3     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            r9 = 0
            android.database.Cursor r0 = r4.query(r5, r6, r7, r8, r9)     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            if (r0 == 0) goto L32
            boolean r10 = r0.moveToNext()     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            if (r10 == 0) goto L32
            java.lang.String r10 = "oldest_image_time"
            int r10 = r0.getColumnIndex(r10)     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
            long r1 = r0.getLong(r10)     // Catch: java.lang.Throwable -> L38 java.lang.Exception -> L3f
        L32:
            if (r0 == 0) goto L42
        L34:
            r0.close()
            goto L42
        L38:
            r10 = move-exception
            if (r0 == 0) goto L3e
            r0.close()
        L3e:
            throw r10
        L3f:
            if (r0 == 0) goto L42
            goto L34
        L42:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryTimeOfOldestImagesOfOnePerson(java.lang.String):long");
    }

    public static FaceRegionRectF getFacePositionRectOfCoverImage(Cursor cursor) {
        if (cursor == null) {
            return new FaceRegionRectF(0.0f, 0.0f, 0.0f, 0.0f, 0);
        }
        return new FaceRegionRectF(cursor.getFloat(cursor.getColumnIndex("faceXScale")), cursor.getFloat(cursor.getColumnIndex("faceYScale")), cursor.getFloat(cursor.getColumnIndex("faceXScale")) + cursor.getFloat(cursor.getColumnIndex("faceWScale")), cursor.getFloat(cursor.getColumnIndex("faceYScale")) + cursor.getFloat(cursor.getColumnIndex("faceHScale")), cursor.getInt(cursor.getColumnIndex("exifOrientation")));
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002d, code lost:
        if (r1 != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002f, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0032, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0023, code lost:
        if (r1 != null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int queryCountOfPhotosOfOneRecommendAlbum(java.lang.String r9) {
        /*
            r0 = 0
            r1 = 0
            android.content.Context r2 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            android.net.Uri r4 = com.miui.gallery.provider.GalleryContract.PeopleFace.RECOMMEND_FACES_OF_ONE_PERSON_URI     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            java.lang.String[] r5 = com.miui.gallery.provider.FaceManager.ID_COUNT_PROJECTION     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            r6 = 0
            r2 = 1
            java.lang.String[] r7 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            r7[r0] = r9     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            r8 = 0
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            if (r1 == 0) goto L23
            int r9 = r1.getCount()     // Catch: java.lang.Throwable -> L26 java.lang.Exception -> L2d
            r1.close()
            return r9
        L23:
            if (r1 == 0) goto L32
            goto L2f
        L26:
            r9 = move-exception
            if (r1 == 0) goto L2c
            r1.close()
        L2c:
            throw r9
        L2d:
            if (r1 == 0) goto L32
        L2f:
            r1.close()
        L32:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryCountOfPhotosOfOneRecommendAlbum(java.lang.String):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0035, code lost:
        if (r7 != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0037, code lost:
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x003a, code lost:
        return r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x002b, code lost:
        if (r7 != null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<java.lang.String> queryAllPeopleAlbumServerIds() {
        /*
            android.net.Uri r1 = com.miui.gallery.provider.GalleryContract.PeopleFace.PEOPLE_FACE_URI
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r7 = 0
            android.content.Context r0 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            java.lang.String[] r2 = com.miui.gallery.provider.FaceManager.PEOPLE_ALBUM_SERVER_ID_PROJECTION     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            java.lang.String r3 = "type = 'PEOPLE' AND ( visibilityType = 1 OR visibilityType =4) AND localFlag NOT IN ( 13, 2)"
            r4 = 0
            r5 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
        L1a:
            if (r7 == 0) goto L2b
            boolean r0 = r7.moveToNext()     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            if (r0 == 0) goto L2b
            r0 = 0
            java.lang.String r0 = r7.getString(r0)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            r6.add(r0)     // Catch: java.lang.Throwable -> L2e java.lang.Exception -> L35
            goto L1a
        L2b:
            if (r7 == 0) goto L3a
            goto L37
        L2e:
            r0 = move-exception
            if (r7 == 0) goto L34
            r7.close()
        L34:
            throw r0
        L35:
            if (r7 == 0) goto L3a
        L37:
            r7.close()
        L3a:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryAllPeopleAlbumServerIds():java.util.ArrayList");
    }

    public static ArrayList<String> queryAllUserDefineRelationsOfPeople(Context context) {
        Uri uri = GalleryContract.PeopleFace.PEOPLE_FACE_URI;
        return (ArrayList) SafeDBUtil.safeQuery(context, uri, PEOPLE_RELATION_PROJECTION, PEOPLE_USER_DEFINE_RELATION_SELECTION, (String[]) null, PeopleContactInfo.getRelationOrderSql() + ", " + PeopleContactInfo.getUserDefineRelationOrderSql(), new SafeDBUtil.QueryHandler<ArrayList<String>>() { // from class: com.miui.gallery.provider.FaceManager.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public ArrayList<String> mo1808handle(Cursor cursor) {
                ArrayList<String> arrayList = new ArrayList<>();
                while (cursor != null && cursor.moveToNext()) {
                    String string = cursor.getString(cursor.getColumnIndex("relationText"));
                    if (!TextUtils.isEmpty(string)) {
                        arrayList.add(string);
                    }
                }
                return arrayList;
            }
        });
    }

    public static List<Long> queryPeopleIdOfRelation(Context context, final int i) {
        return (List) SafeDBUtil.safeQuery(context, GalleryContract.PeopleFace.PERSONS_URI, (String[]) null, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<ArrayList<Long>>() { // from class: com.miui.gallery.provider.FaceManager.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public ArrayList<Long> mo1808handle(Cursor cursor) {
                if (cursor != null) {
                    ArrayList<Long> arrayList = new ArrayList<>();
                    int columnIndex = cursor.getColumnIndex(j.c);
                    int columnIndex2 = cursor.getColumnIndex("relationType");
                    while (cursor.moveToNext()) {
                        if (cursor.getInt(columnIndex2) == i) {
                            arrayList.add(Long.valueOf(cursor.getLong(columnIndex)));
                        }
                    }
                    return arrayList;
                }
                return null;
            }
        });
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0048, code lost:
        r10.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0053, code lost:
        if (r10 != null) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0056, code lost:
        return r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0046, code lost:
        if (r10 != null) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long queryBabyAlbumByPeopleId(java.lang.String r10) {
        /*
            java.lang.String r0 = "_id"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "peopleId"
            java.lang.String r10 = com.miui.gallery.model.dto.utils.AlbumDataHelper.genQueryAlbumExtraInfoSql(r2, r10)
            r1.append(r10)
            java.lang.String r10 = " AND"
            r1.append(r10)
            java.lang.String r10 = com.miui.gallery.provider.FaceManager.GROUP_LOCAL_FLAG_SYNCED_CREATED_RENAME
            r1.append(r10)
            java.lang.String r5 = r1.toString()
            r10 = 0
            r8 = -1
            android.content.Context r1 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            android.content.ContentResolver r2 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            android.net.Uri r3 = com.miui.gallery.provider.GalleryContract.Album.URI     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            java.lang.String[] r4 = new java.lang.String[]{r0}     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            r6 = 0
            r7 = 0
            android.database.Cursor r10 = r2.query(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            if (r10 == 0) goto L46
            boolean r1 = r10.moveToNext()     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            if (r1 == 0) goto L46
            int r0 = r10.getColumnIndex(r0)     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            long r0 = r10.getLong(r0)     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L53
            r8 = r0
        L46:
            if (r10 == 0) goto L56
        L48:
            r10.close()
            goto L56
        L4c:
            r0 = move-exception
            if (r10 == 0) goto L52
            r10.close()
        L52:
            throw r0
        L53:
            if (r10 == 0) goto L56
            goto L48
        L56:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryBabyAlbumByPeopleId(java.lang.String):long");
    }

    public static ArrayList<BabyLockWallpaperDataManager.BabyAlbumInfo> queryAllBabyAlbums() {
        ArrayList<BabyLockWallpaperDataManager.BabyAlbumInfo> arrayList = new ArrayList<>();
        arrayList.addAll(doQueryAllBabyAlbums(false));
        arrayList.addAll(doQueryAllBabyAlbums(true));
        return arrayList;
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0075, code lost:
        if (r8 != null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007f, code lost:
        if (r8 != null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0081, code lost:
        r8.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0084, code lost:
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<com.miui.gallery.provider.BabyLockWallpaperDataManager.BabyAlbumInfo> doQueryAllBabyAlbums(boolean r9) {
        /*
            android.net.Uri r0 = com.miui.gallery.provider.GalleryContract.Album.URI
            java.lang.String r1 = com.miui.gallery.provider.FaceManager.GROUP_LOCAL_FLAG_SYNCED_CREATED_RENAME
            if (r9 == 0) goto La
            android.net.Uri r0 = com.miui.gallery.cloud.GalleryCloudUtils.SHARE_ALBUM_URI
            java.lang.String r1 = com.miui.gallery.provider.FaceManager.Other_SHARED_GROUP_LOCAL_FLAG_SYNCED
        La:
            r3 = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r4 = "not ("
            r2.append(r4)
            java.lang.String r4 = "babyInfoJson"
            r8 = 0
            if (r9 == 0) goto L20
            goto L24
        L20:
            java.lang.String r4 = com.miui.gallery.model.dto.utils.AlbumDataHelper.genQueryAlbumExtraInfoSql(r4, r8)
        L24:
            r2.append(r4)
            java.lang.String r4 = " is null ) AND "
            r2.append(r4)
            r2.append(r1)
            java.lang.String r5 = r2.toString()
            android.content.Context r1 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            android.content.ContentResolver r2 = r1.getContentResolver()     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            if (r9 == 0) goto L46
            java.lang.String r1 = "_id"
            java.lang.String r4 = "fileName"
            java.lang.String[] r1 = new java.lang.String[]{r1, r4}     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            goto L48
        L46:
            java.lang.String[] r1 = com.miui.gallery.provider.BabyLockWallpaperDataManager.BABY_ALBUM_INFO_PROJECTION     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
        L48:
            r4 = r1
            r6 = 0
            r7 = 0
            android.database.Cursor r8 = r2.query(r3, r4, r5, r6, r7)     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
        L4f:
            if (r8 == 0) goto L75
            boolean r1 = r8.moveToNext()     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            if (r1 == 0) goto L75
            com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyAlbumInfo r1 = new com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyAlbumInfo     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            r1.<init>()     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            r2 = 0
            long r3 = r8.getLong(r2)     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            r1.localId = r3     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            r3 = 1
            java.lang.String r4 = r8.getString(r3)     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            r1.name = r4     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            if (r9 == 0) goto L6f
            r1.isOtherShared = r3     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            goto L71
        L6f:
            r1.isOtherShared = r2     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
        L71:
            r0.add(r1)     // Catch: java.lang.Throwable -> L78 java.lang.Exception -> L7f
            goto L4f
        L75:
            if (r8 == 0) goto L84
            goto L81
        L78:
            r9 = move-exception
            if (r8 == 0) goto L7e
            r8.close()
        L7e:
            throw r9
        L7f:
            if (r8 == 0) goto L84
        L81:
            r8.close()
        L84:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.doQueryAllBabyAlbums(boolean):java.util.ArrayList");
    }

    public static ArrayList<BabyLockWallpaperDataManager.BabyPhotoInfo> queryAllBabyAlbumPhotos(ArrayList<BabyLockWallpaperDataManager.BabyAlbumInfo> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }
        ArrayList<BabyLockWallpaperDataManager.BabyPhotoInfo> arrayList2 = new ArrayList<>();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            if (arrayList.get(i).isOtherShared) {
                arrayList4.add(arrayList.get(i));
            } else {
                arrayList3.add(arrayList.get(i));
            }
        }
        if (arrayList3.size() > 0) {
            arrayList2.addAll(doQueryAllBabyAlbumPhotos(arrayList3));
        }
        if (arrayList4.size() > 0) {
            arrayList2.addAll(doQueryAllBabyAlbumPhotos(arrayList4));
        }
        return arrayList2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x00ab, code lost:
        if (r0 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00b5, code lost:
        if (r0 != null) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00b7, code lost:
        r0.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00ba, code lost:
        return r9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<com.miui.gallery.provider.BabyLockWallpaperDataManager.BabyPhotoInfo> doQueryAllBabyAlbumPhotos(java.util.ArrayList<com.miui.gallery.provider.BabyLockWallpaperDataManager.BabyAlbumInfo> r10) {
        /*
            r0 = 0
            if (r10 == 0) goto Lbb
            int r1 = r10.size()
            if (r1 != 0) goto Lb
            goto Lbb
        Lb:
            android.net.Uri r1 = com.miui.gallery.provider.GalleryContract.Cloud.CLOUD_URI
            r2 = 0
            java.lang.Object r3 = r10.get(r2)
            com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyAlbumInfo r3 = (com.miui.gallery.provider.BabyLockWallpaperDataManager.BabyAlbumInfo) r3
            boolean r3 = r3.isOtherShared
            if (r3 == 0) goto L1a
            android.net.Uri r1 = com.miui.gallery.cloud.GalleryCloudUtils.SHARE_IMAGE_URI
        L1a:
            r4 = r1
            java.lang.StringBuffer r1 = new java.lang.StringBuffer
            r1.<init>()
            java.lang.String r3 = "("
            r1.append(r3)
            int r3 = r10.size()
            r5 = r2
        L2a:
            if (r5 >= r3) goto L43
            java.lang.Object r6 = r10.get(r5)
            com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyAlbumInfo r6 = (com.miui.gallery.provider.BabyLockWallpaperDataManager.BabyAlbumInfo) r6
            long r6 = r6.localId
            r1.append(r6)
            int r6 = r3 + (-1)
            if (r5 >= r6) goto L40
            java.lang.String r6 = ","
            r1.append(r6)
        L40:
            int r5 = r5 + 1
            goto L2a
        L43:
            java.lang.String r3 = ")"
            r1.append(r3)
            java.util.ArrayList r9 = new java.util.ArrayList
            r9.<init>()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r5 = "localGroupId in "
            r3.append(r5)
            java.lang.String r1 = r1.toString()
            r3.append(r1)
            java.lang.String r1 = " AND "
            r3.append(r1)
            java.lang.String r5 = com.miui.gallery.provider.FaceManager.PHOTO_LOCAL_FLAG_CREATE_MOVED_SYNCED
            r3.append(r5)
            r3.append(r1)
            java.lang.String r1 = com.miui.gallery.provider.InternalContract$Cloud.ALIAS_LOCAL_MEDIA
            r3.append(r1)
            java.lang.String r6 = r3.toString()
            android.content.Context r1 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            android.content.ContentResolver r3 = r1.getContentResolver()     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            java.lang.String[] r5 = com.miui.gallery.provider.BabyLockWallpaperDataManager.BABY_PHOTO_INFO_PROJECTION     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            r7 = 0
            r8 = 0
            android.database.Cursor r0 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
        L84:
            if (r0 == 0) goto Lab
            boolean r1 = r0.moveToNext()     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            if (r1 == 0) goto Lab
            com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyPhotoInfo r1 = new com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyPhotoInfo     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            r1.<init>()     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            java.lang.String r3 = "_id"
            int r3 = r0.getColumnIndex(r3)     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            long r3 = r0.getLong(r3)     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            r1.id = r3     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            java.lang.Object r3 = r10.get(r2)     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            com.miui.gallery.provider.BabyLockWallpaperDataManager$BabyAlbumInfo r3 = (com.miui.gallery.provider.BabyLockWallpaperDataManager.BabyAlbumInfo) r3     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            boolean r3 = r3.isOtherShared     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            r1.isOtherShared = r3     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            r9.add(r1)     // Catch: java.lang.Throwable -> Lae java.lang.Exception -> Lb5
            goto L84
        Lab:
            if (r0 == 0) goto Lba
            goto Lb7
        Lae:
            r10 = move-exception
            if (r0 == 0) goto Lb4
            r0.close()
        Lb4:
            throw r10
        Lb5:
            if (r0 == 0) goto Lba
        Lb7:
            r0.close()
        Lba:
            return r9
        Lbb:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.doQueryAllBabyAlbumPhotos(java.util.ArrayList):java.util.ArrayList");
    }

    public static final boolean localCopyFaceImages2BabyAlbum(ContentValues contentValues, boolean z) {
        String asString = contentValues.getAsString("exifGPSDateStamp");
        String asString2 = contentValues.getAsString("exifGPSTimeStamp");
        String asString3 = contentValues.getAsString("exifDateTime");
        Long asLong = contentValues.getAsLong("dateTaken");
        Long asLong2 = contentValues.getAsLong("dateModified");
        contentValues.put("mixedDateTime", Long.valueOf(GalleryTimeUtils.getTakenDateTime(asString, asString2, asString3, asLong != null ? asLong.longValue() : 0L, asLong2 != null ? asLong2.longValue() : 0L)));
        return GalleryApp.sGetAndroidContext().getContentResolver().insert(z ? GalleryCloudUtils.SHARE_IMAGE_URI : GalleryContract.Cloud.CLOUD_URI, contentValues) != null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0048, code lost:
        if (r7 != null) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x004a, code lost:
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0059, code lost:
        if (r7 == null) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005c, code lost:
        return r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String queryBabyAlbumCover(java.lang.String r6, boolean r7) {
        /*
            java.util.Locale r0 = java.util.Locale.US
            java.lang.String r1 = com.miui.gallery.provider.album.AlbumManager.SQL_BABY_ALBUM_COVER
            r2 = 1
            java.lang.Object[] r3 = new java.lang.Object[r2]
            r4 = 0
            r3[r4] = r6
            java.lang.String r1 = java.lang.String.format(r0, r1, r3)
            if (r7 == 0) goto L1a
            java.lang.String r7 = com.miui.gallery.provider.album.AlbumManager.SQL_BABY_OTHER_SHARED_ALBUM_COVER
            java.lang.Object[] r1 = new java.lang.Object[r2]
            r1[r4] = r6
            java.lang.String r1 = java.lang.String.format(r0, r7, r1)
        L1a:
            r6 = 0
            com.miui.gallery.provider.GalleryDBHelper r7 = com.miui.gallery.provider.GalleryDBHelper.getInstance()     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            androidx.sqlite.db.SupportSQLiteDatabase r7 = r7.getReadableDatabase()     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r1)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            java.lang.String r1 = "coverPath"
            java.lang.String[] r1 = new java.lang.String[]{r1}     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = r0.columns(r1)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            androidx.sqlite.db.SupportSQLiteQuery r0 = r0.create()     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            android.database.Cursor r7 = r7.query(r0)     // Catch: java.lang.Throwable -> L4e java.lang.Exception -> L58
            if (r7 == 0) goto L48
            boolean r0 = r7.moveToNext()     // Catch: java.lang.Throwable -> L46 java.lang.Exception -> L59
            if (r0 == 0) goto L48
            java.lang.String r6 = r7.getString(r4)     // Catch: java.lang.Throwable -> L46 java.lang.Exception -> L59
            goto L48
        L46:
            r6 = move-exception
            goto L52
        L48:
            if (r7 == 0) goto L5c
        L4a:
            r7.close()
            goto L5c
        L4e:
            r7 = move-exception
            r5 = r7
            r7 = r6
            r6 = r5
        L52:
            if (r7 == 0) goto L57
            r7.close()
        L57:
            throw r6
        L58:
            r7 = r6
        L59:
            if (r7 == 0) goto L5c
            goto L4a
        L5c:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.queryBabyAlbumCover(java.lang.String, boolean):java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004a, code lost:
        if (r7 == null) goto L20;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String querySharerInfoOfBabyAlbum(java.lang.String r7) {
        /*
            java.lang.String r3 = "_id = ? "
            java.lang.String[] r2 = com.miui.gallery.cloud.CloudUtils.getProjectionAll()
            r6 = 0
            android.content.Context r0 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L42 java.lang.Exception -> L49
            android.content.ContentResolver r0 = r0.getContentResolver()     // Catch: java.lang.Throwable -> L42 java.lang.Exception -> L49
            android.net.Uri r1 = com.miui.gallery.cloud.GalleryCloudUtils.SHARE_ALBUM_URI     // Catch: java.lang.Throwable -> L42 java.lang.Exception -> L49
            r4 = 1
            java.lang.String[] r4 = new java.lang.String[r4]     // Catch: java.lang.Throwable -> L42 java.lang.Exception -> L49
            r5 = 0
            r4[r5] = r7     // Catch: java.lang.Throwable -> L42 java.lang.Exception -> L49
            r5 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5)     // Catch: java.lang.Throwable -> L42 java.lang.Exception -> L49
            if (r7 == 0) goto L3c
            int r0 = r7.getCount()     // Catch: java.lang.Throwable -> L39 java.lang.Exception -> L4a
            if (r0 != 0) goto L25
            goto L3c
        L25:
            boolean r0 = r7.moveToNext()     // Catch: java.lang.Throwable -> L39 java.lang.Exception -> L4a
            if (r0 == 0) goto L4c
            java.lang.String r0 = "sharerInfo"
            int r0 = r7.getColumnIndex(r0)     // Catch: java.lang.Throwable -> L39 java.lang.Exception -> L4a
            java.lang.String r0 = r7.getString(r0)     // Catch: java.lang.Throwable -> L39 java.lang.Exception -> L4a
            r7.close()
            return r0
        L39:
            r0 = move-exception
            r6 = r7
            goto L43
        L3c:
            if (r7 == 0) goto L41
            r7.close()
        L41:
            return r6
        L42:
            r0 = move-exception
        L43:
            if (r6 == 0) goto L48
            r6.close()
        L48:
            throw r0
        L49:
            r7 = r6
        L4a:
            if (r7 == 0) goto L4f
        L4c:
            r7.close()
        L4f:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.querySharerInfoOfBabyAlbum(java.lang.String):java.lang.String");
    }

    public static long getPeopleLocalIdByServerId(String str) {
        Cursor cursor = null;
        try {
            cursor = GalleryApp.sGetAndroidContext().getContentResolver().query(GalleryContract.PeopleFace.PEOPLE_FACE_URI, new String[]{j.c}, "serverId = ?", new String[]{str}, null);
        } catch (Exception unused) {
            if (cursor == null) {
                return -1L;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        if (cursor != null && cursor.moveToNext()) {
            long j = cursor.getLong(0);
            cursor.close();
            return j;
        }
        if (cursor == null) {
            return -1L;
        }
        cursor.close();
        return -1L;
    }

    public static int getPeopleLocalFlagByLocalID(String str) {
        return ((Integer) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.PeopleFace.PEOPLE_FACE_URI, new String[]{"localFlag"}, "_id = ?", new String[]{str}, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.provider.FaceManager.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Integer mo1808handle(Cursor cursor) {
                if (cursor != null && cursor.moveToNext()) {
                    return Integer.valueOf(cursor.getInt(0));
                }
                return -1;
            }
        })).intValue();
    }

    public static void safeUpdatePeopleFaceByIds(ContentValues contentValues, ArrayList<String> arrayList) {
        try {
            GalleryApp.sGetAndroidContext().getContentResolver().update(GalleryContract.PeopleFace.PEOPLE_FACE_URI, contentValues, "_id in (" + TextUtils.join(",", arrayList) + ")", null);
        } catch (Exception unused) {
        }
    }

    /* loaded from: classes2.dex */
    public static class BasicPeopleInfo {
        public int id;
        public String name;
        public int relationType;
        public String serverId;

        public BasicPeopleInfo(int i, String str, String str2, int i2) {
            this.id = i;
            this.serverId = str;
            this.name = str2;
            this.relationType = i2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x006f, code lost:
        if (r2 != null) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0071, code lost:
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0074, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0065, code lost:
        if (r2 != null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<com.miui.gallery.provider.FaceManager.BasicPeopleInfo> getPeopleBasicInfoByIds(long[] r6) {
        /*
            java.lang.String[] r0 = com.miui.gallery.provider.FaceManager.BASIC_PEOPLE_INFO_PROJECTION
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "_id in ("
            r1.append(r2)
            java.lang.String r6 = formatSelectionIn(r6)
            r1.append(r6)
            java.lang.String r6 = ")"
            r1.append(r6)
            java.lang.String r6 = r1.toString()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r2 = 0
            com.miui.gallery.provider.GalleryDBHelper r3 = com.miui.gallery.provider.GalleryDBHelper.getInstance()     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            androidx.sqlite.db.SupportSQLiteDatabase r3 = r3.getReadableDatabase()     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            java.lang.String r4 = "peopleFace"
            androidx.sqlite.db.SupportSQLiteQueryBuilder r4 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r4)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            androidx.sqlite.db.SupportSQLiteQueryBuilder r0 = r4.columns(r0)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            androidx.sqlite.db.SupportSQLiteQueryBuilder r6 = r0.selection(r6, r2)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            androidx.sqlite.db.SupportSQLiteQuery r6 = r6.create()     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            android.database.Cursor r2 = r3.query(r6)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
        L40:
            if (r2 == 0) goto L65
            boolean r6 = r2.moveToNext()     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            if (r6 == 0) goto L65
            com.miui.gallery.provider.FaceManager$BasicPeopleInfo r6 = new com.miui.gallery.provider.FaceManager$BasicPeopleInfo     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            r0 = 0
            int r0 = r2.getInt(r0)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            r3 = 1
            java.lang.String r3 = r2.getString(r3)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            r4 = 2
            java.lang.String r4 = r2.getString(r4)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            r5 = 3
            int r5 = r2.getInt(r5)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            r6.<init>(r0, r3, r4, r5)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            r1.add(r6)     // Catch: java.lang.Throwable -> L68 java.lang.Exception -> L6f
            goto L40
        L65:
            if (r2 == 0) goto L74
            goto L71
        L68:
            r6 = move-exception
            if (r2 == 0) goto L6e
            r2.close()
        L6e:
            throw r6
        L6f:
            if (r2 == 0) goto L74
        L71:
            r2.close()
        L74:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.FaceManager.getPeopleBasicInfoByIds(long[]):java.util.ArrayList");
    }

    public static String formatSelectionIn(long[] jArr) {
        StringBuffer stringBuffer = new StringBuffer();
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            stringBuffer.append(jArr[i]);
            if (i < length - 1) {
                stringBuffer.append(CoreConstants.COMMA_CHAR);
            }
        }
        return stringBuffer.toString();
    }

    public static String formatSelectionIn(List list) {
        return formatSelectionIn(list, "-1");
    }

    public static String formatSelectionIn(List list, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (BaseMiscUtil.isValid(list)) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                stringBuffer.append("'");
                stringBuffer.append(list.get(i));
                stringBuffer.append("'");
                if (i < size - 1) {
                    stringBuffer.append(CoreConstants.COMMA_CHAR);
                }
            }
        } else {
            stringBuffer.append(str);
        }
        return stringBuffer.toString();
    }

    public static ContentValues changeCursorData2ContentValuesOfCloudTable(Cursor cursor, String str) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("fileName", cursor.getString(cursor.getColumnIndex("fileName")));
        contentValues.put("dateTaken", Long.valueOf(cursor.getLong(cursor.getColumnIndex("dateTaken"))));
        contentValues.put("dateModified", Long.valueOf(cursor.getLong(cursor.getColumnIndex("dateModified"))));
        contentValues.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(cursor.getLong(cursor.getColumnIndex(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE))));
        contentValues.put("mimeType", cursor.getString(cursor.getColumnIndex("mimeType")));
        contentValues.put("title", cursor.getString(cursor.getColumnIndex("title")));
        contentValues.put("sha1", cursor.getString(cursor.getColumnIndex("sha1")));
        contentValues.put("ubiSubImageCount", Integer.valueOf(cursor.getInt(cursor.getColumnIndex("ubiSubImageCount"))));
        contentValues.put("ubiSubIndex", Integer.valueOf(cursor.getInt(cursor.getColumnIndex("ubiSubIndex"))));
        contentValues.put("ubiFocusIndex", Integer.valueOf(cursor.getInt(cursor.getColumnIndex("ubiFocusIndex"))));
        contentValues.put("localFlag", (Integer) 6);
        contentValues.put("serverType", (Integer) 1);
        contentValues.put("localGroupId", str);
        if (cursor.getColumnIndex("photo_id") >= 0) {
            contentValues.put("localImageId", cursor.getString(cursor.getColumnIndex("photo_id")));
        } else {
            contentValues.put("localImageId", cursor.getString(cursor.getColumnIndex(j.c)));
        }
        if (cursor.getString(cursor.getColumnIndex("thumbnailFile")) != null) {
            contentValues.put("thumbnailFile", cursor.getString(cursor.getColumnIndex("thumbnailFile")));
        }
        if (cursor.getString(cursor.getColumnIndex("microthumbfile")) != null) {
            contentValues.put("microthumbfile", cursor.getString(cursor.getColumnIndex("microthumbfile")));
        }
        return contentValues;
    }
}
