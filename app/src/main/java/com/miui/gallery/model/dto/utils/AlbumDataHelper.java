package com.miui.gallery.model.dto.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.text.TextUtils;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudTableUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.FavoritesDelegate;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.ExtraTextUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.HashMap;
import java.util.Map;
import miuix.core.util.Pools;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class AlbumDataHelper {
    public static final String[] QUERY_COLUMN_SERVER_ID;
    public static final Map<String, String> sAppKeyToPathMap;
    public static LoadingCache<String, String> sLocalizedNamesCache;

    static {
        HashMap hashMap = new HashMap(12, 1.0f);
        sAppKeyToPathMap = hashMap;
        hashMap.put("com.sina.weibo", "sina/weibo/weibo");
        hashMap.put("com.tencent.mobileqq", "tencent/qq_images");
        hashMap.put("com.tencent.mm", "tencent/micromsg/weixin");
        hashMap.put("com.xiaomi.channel", "miliao/saved");
        hashMap.put("com.xiaomi.shop", "mishop/save");
        hashMap.put("com.UCMobile", "ucdownloads");
        hashMap.put("com.mt.mtxx.mtxx", "mtxx");
        hashMap.put("cn.wps.moffice_eng", "kingsoftoffice/file/summary/preview");
        hashMap.put("com.baidu.tieba", "tieba");
        hashMap.put("com.qzone", "tencent/qzonepic");
        hashMap.put("com.manboker.headportrait", "dcim/momentcam");
        sLocalizedNamesCache = CacheBuilder.newBuilder().maximumSize(10L).build(new CacheLoader<String, String>() { // from class: com.miui.gallery.model.dto.utils.AlbumDataHelper.1
            @Override // com.google.common.cache.CacheLoader
            public String load(String str) {
                try {
                    return GalleryApp.sGetAndroidContext().getString(Integer.parseInt(str.substring(0, str.indexOf(95))));
                } catch (Exception e) {
                    DefaultLogger.w("AlbumNameCache", "failed to load cache for key [%s], %s", str, e);
                    return "";
                }
            }
        });
        QUERY_COLUMN_SERVER_ID = new String[]{"serverId"};
    }

    public static String getCameraLocalPath() {
        return MIUIStorageConstants.DIRECTORY_CAMERA_PATH;
    }

    public static String getScreenshotsLocalPath() {
        return MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH;
    }

    public static String getScreenRecorderLocalPath() {
        return MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH;
    }

    public static String getUserCreateLocalPath() {
        return StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM;
    }

    public static String getCameraFileName() {
        return GalleryApp.sGetAndroidContext().getString(R.string.cloud_camera_display_name);
    }

    public static String getScreenshotsFileName() {
        return GalleryApp.sGetAndroidContext().getString(R.string.cloud_screenshots_display_name);
    }

    public static ContentValues getCameraRecordValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("dateModified", (Long) 999L);
        contentValues.put("realDateModified", (Long) 999L);
        contentValues.put("dateTaken", (Long) 999L);
        contentValues.put("name", getCameraFileName());
        contentValues.put("serverId", (Long) 1L);
        contentValues.put("serverStatus", "custom");
        contentValues.put("localFlag", (Integer) 0);
        contentValues.put("localPath", getCameraLocalPath());
        contentValues.put("attributes", (Long) 5L);
        contentValues.put("sortInfo", String.valueOf(999L));
        return contentValues;
    }

    public static ContentValues getScreenshotsRecordValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("dateModified", (Long) 995L);
        contentValues.put("realDateModified", (Long) 995L);
        contentValues.put("dateTaken", (Long) 995L);
        contentValues.put("name", getScreenshotsFileName());
        contentValues.put("serverId", (Long) 2L);
        contentValues.put("serverStatus", "custom");
        contentValues.put("localFlag", (Integer) 0);
        contentValues.put("localPath", getScreenshotsLocalPath());
        contentValues.put("attributes", (Long) 1L);
        contentValues.put("sortInfo", String.valueOf(995L));
        return contentValues;
    }

    public static long queryScreenshotAlbumId(Context context) {
        DBAlbum albumByServerID = getAlbumByServerID(context, "2");
        if (albumByServerID != null) {
            return Long.parseLong(albumByServerID.getId());
        }
        return -1L;
    }

    public static void addRecordsForCameraAndScreenshots(Insertable insertable) {
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        insertable.insert(uri, "album", null, getCameraRecordValues());
        insertable.insert(uri, "album", null, getScreenshotsRecordValues());
    }

    public static void replaceFieldsForSystemAlbum(ContentValues contentValues) {
        long longValue = contentValues.getAsLong("serverId").longValue();
        if (longValue == 1) {
            contentValues.put("name", getCameraFileName());
            contentValues.put("localPath", getCameraLocalPath());
            contentValues.put("dateTaken", (Long) 999L);
        } else if (longValue != 2) {
        } else {
            contentValues.put("name", getScreenshotsFileName());
            contentValues.put("localPath", getScreenshotsLocalPath());
            contentValues.put("dateTaken", (Long) 995L);
        }
    }

    public static boolean isSystemAlbum(String str) {
        for (Long l : GalleryContract.Album.ALL_SYSTEM_ALBUM_SERVER_IDS) {
            if (String.valueOf(l).equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static String getOwnerAlbumLocalFile(String str, String str2) {
        String str3 = !TextUtils.isEmpty(str2) ? sAppKeyToPathMap.get(str2) : null;
        return TextUtils.isEmpty(str3) ? DownloadPathHelper.getFolderRelativePathInCloud(str) : str3;
    }

    public static Boolean getAutoUploadAttributeFromDescription(String str) {
        Album.ExtraInfo.DescriptionBean newInstance = Album.ExtraInfo.DescriptionBean.newInstance(str);
        if (newInstance == null) {
            return null;
        }
        return newInstance.isAutoUpload();
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0090, code lost:
        if (android.text.TextUtils.isEmpty(r3) == false) goto L7;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getLocalizedAlbumNameIfExists(java.lang.String r3, long r4, java.lang.String r6) {
        /*
            r4 = 1
            java.lang.String r4 = java.lang.String.valueOf(r4)
            boolean r4 = r4.equals(r3)
            r5 = 0
            if (r4 == 0) goto L11
            r3 = 2131886196(0x7f120074, float:1.9406964E38)
            goto L66
        L11:
            r0 = 2
            java.lang.String r4 = java.lang.String.valueOf(r0)
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L21
            r3 = 2131886243(0x7f1200a3, float:1.940706E38)
            goto L66
        L21:
            r0 = -2147483647(0xffffffff80000001, double:NaN)
            java.lang.String r4 = java.lang.String.valueOf(r0)
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L32
            r3 = 2131886270(0x7f1200be, float:1.9407114E38)
            goto L66
        L32:
            r0 = -2147483644(0xffffffff80000004, double:NaN)
            java.lang.String r4 = java.lang.String.valueOf(r0)
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L43
            r3 = 2131886213(0x7f120085, float:1.9406998E38)
            goto L66
        L43:
            r0 = -2147483642(0xffffffff80000006, double:NaN)
            java.lang.String r4 = java.lang.String.valueOf(r0)
            boolean r4 = r4.equals(r3)
            if (r4 == 0) goto L54
            r3 = 2131886203(0x7f12007b, float:1.9406978E38)
            goto L66
        L54:
            r0 = -2147483645(0xffffffff80000003, double:NaN)
            java.lang.String r4 = java.lang.String.valueOf(r0)
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L65
            r3 = 2131886242(0x7f1200a2, float:1.9407057E38)
            goto L66
        L65:
            r3 = r5
        L66:
            if (r3 == 0) goto L93
            java.util.Locale r4 = java.util.Locale.getDefault()
            java.lang.String r4 = r4.toString()
            com.google.common.cache.LoadingCache<java.lang.String, java.lang.String> r0 = com.miui.gallery.model.dto.utils.AlbumDataHelper.sLocalizedNamesCache
            java.util.Locale r1 = java.util.Locale.US
            r2 = 2
            java.lang.Object[] r2 = new java.lang.Object[r2]
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            r2[r5] = r3
            r3 = 1
            r2[r3] = r4
            java.lang.String r3 = "%d_%s"
            java.lang.String r3 = java.lang.String.format(r1, r3, r2)
            java.lang.Object r3 = r0.getUnchecked(r3)
            java.lang.String r3 = (java.lang.String) r3
            boolean r4 = android.text.TextUtils.isEmpty(r3)
            if (r4 != 0) goto L93
            goto L94
        L93:
            r3 = 0
        L94:
            if (r3 == 0) goto L97
            r6 = r3
        L97:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.dto.utils.AlbumDataHelper.getLocalizedAlbumNameIfExists(java.lang.String, long, java.lang.String):java.lang.String");
    }

    public static String getSystemAlbumServerIdByLocalId(long j) {
        return String.valueOf(AlbumManager.getAlbumServerIds().get(Long.valueOf(j)));
    }

    public static String genQueryAlbumExtraInfoSql(String str, String str2) {
        String str3;
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            if (str.equals("babyInfoJson")) {
                acquire.append(" json_extract(extra,'$.babyInfoJson') ");
            } else if (str.equals("appKey")) {
                acquire.append(" json_extract(extra,'$.appKey') ");
            } else if (str.equals("description")) {
                acquire.append(" json_extract(extra,'$.description') ");
            } else if (str.equals("peopleId")) {
                acquire.append(" json_extract(extra,'$.peopleId') ");
            } else if (str.equals("sharerInfo")) {
                acquire.append(" json_extract(extra,'$.sharerInfo') ");
            } else if (!str.equals("thumbnailInfo")) {
                str3 = "";
                return str3;
            } else {
                acquire.append(" json_extract(extra,'$.thumbnailInfo') ");
            }
            if (str2 != null) {
                acquire.append(" = ");
                acquire.append(DatabaseUtils.sqlEscapeString(str2));
            }
            str3 = acquire.toString();
            return str3;
        } finally {
            Pools.getStringBuilderPool().release(acquire);
        }
    }

    public static String genUpdateAlbumExtraInfoSql(String str, String str2, boolean z) {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put(str, str2);
        return genUpdateAlbumExtraInfoSql(contentValues, z);
    }

    public static String genUpdateAlbumExtraInfoSql(ContentValues contentValues, boolean z) {
        if (contentValues.size() == 0) {
            return null;
        }
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        if (z) {
            try {
                acquire.append(CallMethod.ARG_EXTRA_STRING);
                acquire.append("=");
            } finally {
                Pools.getStringBuilderPool().release(acquire);
            }
        }
        acquire.append("json_set(");
        acquire.append(CallMethod.ARG_EXTRA_STRING);
        for (String str : contentValues.keySet()) {
            Object obj = contentValues.get(str);
            acquire.append(",");
            acquire.append("'$.");
            acquire.append(str);
            acquire.append("'");
            acquire.append(",");
            acquire.append(obj == null ? "''" : DatabaseUtils.sqlEscapeString(obj.toString()));
        }
        acquire.append(" ) ");
        return acquire.toString();
    }

    public static void updateToLocalDBByServerId(ContentValues contentValues, String str) {
        updateToLocalDBBy(contentValues, "serverId = '" + str + "'");
    }

    public static void updateToLocalDBById(ContentValues contentValues, String str) {
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        GalleryUtils.safeUpdate(uri, contentValues, "_id = '" + str + "'", null);
    }

    public static void updateToLocalDBBy(ContentValues contentValues, String str) {
        GalleryUtils.safeUpdate(GalleryCloudUtils.ALBUM_URI, contentValues, str, null);
    }

    public static void updateAlbumAndSetLocalFlagToSynced(ContentValues contentValues, String str) throws JSONException {
        updateAlbumAndSetLocalFlagToSynced(GalleryCloudUtils.ALBUM_URI, contentValues, str, false);
    }

    public static void updateAlbumAndSetLocalFlagToSynced(Uri uri, ContentValues contentValues, String str) throws JSONException {
        updateAlbumAndSetLocalFlagToSynced(uri, contentValues, str, false);
    }

    public static void updateAlbumAndSetLocalFlagToSynced(Uri uri, ContentValues contentValues, String str, boolean z) throws JSONException {
        if (contentValues != null) {
            String asString = contentValues.getAsString("serverId");
            if (!TextUtils.isEmpty(asString)) {
                ContentValues contentValues2 = new ContentValues();
                contentValues2.putNull("serverId");
                GalleryUtils.safeUpdate(uri, contentValues2, "serverId = ?", new String[]{asString});
            }
            updateToLocalDBById(contentValues, str);
        }
        ContentValues contentValues3 = new ContentValues();
        if (!z) {
            contentValues3.put("localFlag", (Integer) 0);
        }
        GalleryUtils.safeUpdate(uri, contentValues3, String.format("_id = ? AND localFlag NOT IN(%s)", String.valueOf(2)), new String[]{str});
    }

    public static Album.ExtraInfo parseExtraInfo(String str) {
        return Album.ExtraInfo.newInstance(str);
    }

    public static void deleteDirty(DBAlbum dBAlbum) {
        Uri baseUri = dBAlbum.getBaseUri();
        GalleryUtils.safeDelete(baseUri, "_id = '" + dBAlbum.getId() + "'", null);
    }

    public static void fixValueForContentValues(ContentValues contentValues) {
        if (contentValues == null) {
            return;
        }
        contentValues.remove("serverType");
        String genExtraInfoForContentValues = genExtraInfoForContentValues(contentValues);
        if (TextUtils.isEmpty(genExtraInfoForContentValues)) {
            return;
        }
        for (String str : GalleryContract.Album.EXTRA_INFO_CHILDS) {
            contentValues.remove(str);
        }
        contentValues.put(CallMethod.ARG_EXTRA_STRING, genExtraInfoForContentValues);
    }

    public static void genUpdateExtraValueForContentValuesIfNeed(ContentValues contentValues) {
        String[] strArr;
        if (contentValues == null) {
            return;
        }
        ContentValues contentValues2 = new ContentValues(contentValues.size());
        for (String str : GalleryContract.Album.EXTRA_INFO_CHILDS) {
            if (contentValues.containsKey(str)) {
                contentValues2.put(str, contentValues.getAsString(str));
                contentValues.remove(str);
            }
        }
        String genUpdateAlbumExtraInfoSql = genUpdateAlbumExtraInfoSql(contentValues2, false);
        if (TextUtils.isEmpty(genUpdateAlbumExtraInfoSql)) {
            return;
        }
        contentValues.put(CallMethod.ARG_EXTRA_STRING, genUpdateAlbumExtraInfoSql);
    }

    public static String genExtraInfoForContentValues(ContentValues contentValues) {
        String[] strArr;
        if (contentValues == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            for (String str : GalleryContract.Album.EXTRA_INFO_CHILDS) {
                if (contentValues.containsKey(str)) {
                    jSONObject.put(str, contentValues.get(str));
                }
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ContentValues getContentValuesForUploadDeletePurged(JSONObject jSONObject) throws JSONException {
        ContentValues contentValues = new ContentValues();
        if (jSONObject != null) {
            if (jSONObject.has(MiStat.Param.CONTENT)) {
                jSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT);
            }
            if (jSONObject.has("id")) {
                contentValues.put("serverId", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "id")));
            }
            if (jSONObject.has("status")) {
                contentValues.put("serverStatus", jSONObject.getString("status"));
            }
            if (jSONObject.has(nexExportFormat.TAG_FORMAT_TAG)) {
                contentValues.put("serverTag", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG)));
            }
        }
        contentValues.put("localFlag", (Integer) 0);
        return contentValues;
    }

    public static ContentValues getInsertAlbumContentValue(Context context, String str, long j, long j2, String str2, long j3, String str3) {
        ContentValues contentValues = new ContentValues();
        long j4 = 0;
        if (str2 != null) {
            if (getCameraLocalPath().equalsIgnoreCase(str2)) {
                return getCameraRecordValues();
            }
            if (getScreenshotsLocalPath().equalsIgnoreCase(str2)) {
                return getScreenshotsRecordValues();
            }
            String str4 = StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM;
            if (ExtraTextUtils.startsWithIgnoreCase(str2, str4)) {
                contentValues = new ContentValues();
                if (SyncUtil.existXiaomiAccount(context)) {
                    j4 = 1;
                }
                contentValues.put("attributes", Long.valueOf(j4));
            } else {
                contentValues.put("attributes", Long.valueOf(collectNewAlbumAttributesByLocalPath(context, str2)));
            }
            contentValues.put("localPath", StringUtils.replaceIgnoreCase(str2, "MIUI/Gallery/cloud", str4));
            GalleryPreferences.Album.applyAlbumMigrationState(false, 1L);
        } else {
            contentValues.put("attributes", (Long) 0L);
        }
        contentValues.put("name", str);
        contentValues.put("dateTaken", Long.valueOf(j));
        contentValues.put("dateModified", Long.valueOf(j2));
        contentValues.put("localFlag", Long.valueOf(j3));
        contentValues.put("sortInfo", str3);
        return contentValues;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x006c A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long collectNewAlbumAttributesByLocalPath(android.content.Context r6, java.lang.String r7) {
        /*
            java.lang.String r6 = getCameraLocalPath()
            boolean r6 = r6.equalsIgnoreCase(r7)
            if (r6 == 0) goto Ld
            r6 = 5
            return r6
        Ld:
            java.lang.String r6 = getScreenshotsLocalPath()
            boolean r6 = r6.equalsIgnoreCase(r7)
            r0 = 1
            if (r6 == 0) goto L1a
            return r0
        L1a:
            java.lang.String r6 = com.miui.gallery.storage.constants.StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM
            boolean r6 = com.miui.gallery.util.ExtraTextUtils.startsWithIgnoreCase(r7, r6)
            if (r6 == 0) goto L23
            return r0
        L23:
            r6 = 1
            r0 = 0
            com.miui.gallery.cloudcontrol.CloudControlManager r2 = com.miui.gallery.cloudcontrol.CloudControlManager.getInstance()
            boolean r2 = r2.isInitDone()
            if (r2 != 0) goto L31
            return r0
        L31:
            java.lang.String r2 = com.miui.gallery.util.StorageUtils.ensureCommonRelativePath(r7)
            com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Album r2 = com.miui.gallery.cloudcontrol.CloudControlStrategyHelper.getAlbumByPath(r2)
            r3 = 0
            if (r2 == 0) goto L4c
            com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Attributes r4 = r2.getAttributes()
            if (r4 == 0) goto L4c
            com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Attributes r6 = r2.getAttributes()
            long r4 = r6.checkAndFixAlbumAttributes(r7)
            long r0 = r0 | r4
            goto L57
        L4c:
            com.miui.gallery.cloudcontrol.strategies.AlbumsStrategy$Attributes r2 = com.miui.gallery.cloudcontrol.CloudControlStrategyHelper.getAlbumAttributesByPath(r7)
            if (r2 == 0) goto L59
            long r4 = r2.checkAndFixAlbumAttributes(r7)
            long r0 = r0 | r4
        L57:
            r6 = r3
            goto L5b
        L59:
            r0 = 64
        L5b:
            if (r6 == 0) goto L66
            boolean r6 = com.miui.gallery.cloudcontrol.CloudControlStrategyHelper.isInHideList(r7)
            if (r6 == 0) goto L66
            r2 = 2048(0x800, double:1.0118E-320)
            long r0 = r0 | r2
        L66:
            boolean r6 = com.miui.gallery.util.NoMediaUtil.isManualHideAlbum(r7)
            if (r6 == 0) goto L6e
            r0 = 6146(0x1802, double:3.0365E-320)
        L6e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.dto.utils.AlbumDataHelper.collectNewAlbumAttributesByLocalPath(android.content.Context, java.lang.String):long");
    }

    public static DBAlbum getAlbumByAppKey(Context context, String str, String str2) {
        return getAlbumByColumnnameAndValue(context, "appKey", str, str2, false);
    }

    public static DBAlbum getAlbumByFileName(Context context, String str) {
        return getAlbumByColumnnameAndValue(context, "name", str, null, true);
    }

    public static DBAlbum getAlbumByFileName(Context context, String str, String str2) {
        return getAlbumByColumnnameAndValue(context, "name", str, str2, true);
    }

    public static DBAlbum getAlbumByFilePath(Context context, String str, String str2) {
        return getAlbumByColumnnameAndValue(context, "localPath", str, str2, true);
    }

    public static DBAlbum getAlbumById(Context context, String str, String str2) {
        return getAlbumByColumnnameAndValue(context, j.c, str, str2, false);
    }

    public static DBAlbum getAlbumByServerID(Context context, String str) {
        return getAlbumByColumnnameAndValue(context, "serverId", str);
    }

    public static DBAlbum getAlbumByColumnnameAndValue(Context context, String str, String str2) {
        return getAlbumByColumnnameAndValue(context, str, str2, null, false);
    }

    public static DBAlbum getAlbumByColumnnameAndValue(Context context, String str, String str2, String str3, boolean z) {
        String str4;
        if (z) {
            str4 = str + " like ? ";
        } else {
            str4 = str + " = ? ";
        }
        if (str3 != null) {
            str4 = str4 + "  AND ( " + str3 + " ) ";
        }
        Cursor cursor = null;
        try {
            Cursor albumByColumnnameAndValue = getAlbumByColumnnameAndValue(context, CloudUtils.getProjectionAll(), str4, new String[]{str2});
            if (albumByColumnnameAndValue != null) {
                try {
                    if (albumByColumnnameAndValue.moveToFirst()) {
                        DBAlbum dBAlbum = new DBAlbum(albumByColumnnameAndValue);
                        albumByColumnnameAndValue.close();
                        return dBAlbum;
                    }
                } catch (Throwable th) {
                    th = th;
                    cursor = albumByColumnnameAndValue;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            DefaultLogger.d("AlbumDataHelper", "there isn't have any item in local DB for " + str4);
            if (albumByColumnnameAndValue != null) {
                albumByColumnnameAndValue.close();
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static int getLocalGroupIDErrorCount(Context context) {
        Cursor queryToCursor = SafeDBUtil.queryToCursor(context, GalleryContract.Cloud.CLOUD_URI, new MediaCacheItem.Generator(new FavoritesDelegate()).getProjection(), "localGroupId is NULL AND serverStatus = \"custom\" AND serverType != 0 AND groupId not NULL AND localFlag = 0", null, null);
        if (queryToCursor != null) {
            try {
                if (queryToCursor.getCount() > 0) {
                    int count = queryToCursor.getCount();
                    queryToCursor.close();
                    return count;
                }
            } catch (Throwable th) {
                try {
                    queryToCursor.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
        if (queryToCursor != null) {
            queryToCursor.close();
        }
        return 0;
    }

    public static Cursor getAlbumByColumnnameAndValue(Context context, String[] strArr, String str, String[] strArr2) {
        return context.getContentResolver().query(UriUtil.appendLimit(GalleryCloudUtils.ALBUM_URI, 1), strArr, str, strArr2, null);
    }

    public static String getAlbumServerIdByLocalId(Context context, String str) {
        long serverIdForGroupWithoutRecord = CloudTableUtils.getServerIdForGroupWithoutRecord(Long.parseLong(str));
        if (serverIdForGroupWithoutRecord > 0) {
            return String.valueOf(serverIdForGroupWithoutRecord);
        }
        Cursor albumByColumnnameAndValue = getAlbumByColumnnameAndValue(context, QUERY_COLUMN_SERVER_ID, "_id=?", new String[]{str});
        if (albumByColumnnameAndValue != null && albumByColumnnameAndValue.moveToFirst()) {
            return albumByColumnnameAndValue.getString(0);
        }
        return null;
    }

    public static String queryAlbumNameByAlbumId(Uri uri, Context context, long j) {
        Cursor cursor = null;
        if (j <= 0) {
            return null;
        }
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"name"}, "_id=?", new String[]{String.valueOf(j)}, null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        String string = query.getString(0);
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

    public static String queryAlbumPathByAlbumId(Context context, long j, boolean z) {
        if (context == null) {
            return null;
        }
        return getAlbumAbsolutePath(context.getContentResolver(), j, z);
    }

    public static String getAlbumAbsolutePath(ContentResolver contentResolver, long j, boolean z) {
        String queryAlbumInfo;
        if (contentResolver == null) {
            return null;
        }
        if (j == -1000) {
            queryAlbumInfo = "MIUI/Gallery/cloud/secretAlbum";
        } else if (z) {
            queryAlbumInfo = Util.genRelativePath(null, true);
        } else {
            queryAlbumInfo = queryAlbumInfo(contentResolver, j);
        }
        if (!TextUtils.isEmpty(queryAlbumInfo)) {
            return StorageUtils.getPathInPriorStorage(queryAlbumInfo);
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:32:0x0080  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String queryAlbumInfo(android.content.ContentResolver r13, long r14) {
        /*
            r0 = 0
            java.lang.String r1 = "localPath"
            java.lang.String r2 = "name"
            java.lang.String[] r1 = new java.lang.String[]{r1, r2}     // Catch: java.lang.Throwable -> L7d
            java.lang.String r2 = "_id=?"
            r11 = 1
            java.lang.String[] r12 = new java.lang.String[r11]     // Catch: java.lang.Throwable -> L7d
            java.lang.String r14 = java.lang.String.valueOf(r14)     // Catch: java.lang.Throwable -> L7d
            r15 = 0
            r12[r15] = r14     // Catch: java.lang.Throwable -> L7d
            com.miui.gallery.provider.cache.AlbumCacheManager r3 = com.miui.gallery.provider.cache.AlbumCacheManager.getInstance()     // Catch: java.lang.Throwable -> L7d
            r7 = 0
            r8 = 0
            r9 = 0
            r10 = 0
            r4 = r1
            r5 = r2
            r6 = r12
            android.database.Cursor r14 = r3.query(r4, r5, r6, r7, r8, r9, r10)     // Catch: java.lang.Throwable -> L7d
            if (r14 == 0) goto L45
            boolean r3 = r14.moveToFirst()     // Catch: java.lang.Throwable -> L42
            if (r3 == 0) goto L45
            java.lang.String r13 = r14.getString(r15)     // Catch: java.lang.Throwable -> L42
            boolean r0 = android.text.TextUtils.isEmpty(r13)     // Catch: java.lang.Throwable -> L42
            if (r0 == 0) goto L3e
            java.lang.String r13 = r14.getString(r11)     // Catch: java.lang.Throwable -> L42
            java.lang.String r13 = com.miui.gallery.provider.cloudmanager.Util.genRelativePath(r13, r15)     // Catch: java.lang.Throwable -> L42
        L3e:
            r14.close()
            return r13
        L42:
            r13 = move-exception
            r0 = r14
            goto L7e
        L45:
            if (r14 == 0) goto L4a
            r14.close()     // Catch: java.lang.Throwable -> L42
        L4a:
            android.net.Uri r4 = com.miui.gallery.provider.GalleryContract.Album.URI     // Catch: java.lang.Throwable -> L42
            r8 = 0
            r3 = r13
            r5 = r1
            r6 = r2
            r7 = r12
            android.database.Cursor r13 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L42
            if (r13 == 0) goto L77
            boolean r14 = r13.moveToFirst()     // Catch: java.lang.Throwable -> L73
            if (r14 == 0) goto L77
            java.lang.String r14 = r13.getString(r15)     // Catch: java.lang.Throwable -> L73
            boolean r0 = android.text.TextUtils.isEmpty(r14)     // Catch: java.lang.Throwable -> L73
            if (r0 == 0) goto L6f
            java.lang.String r14 = r13.getString(r11)     // Catch: java.lang.Throwable -> L73
            java.lang.String r14 = com.miui.gallery.provider.cloudmanager.Util.genRelativePath(r14, r15)     // Catch: java.lang.Throwable -> L73
        L6f:
            r13.close()
            return r14
        L73:
            r14 = move-exception
            r0 = r13
            r13 = r14
            goto L7e
        L77:
            if (r13 == 0) goto L7c
            r13.close()
        L7c:
            return r0
        L7d:
            r13 = move-exception
        L7e:
            if (r0 == 0) goto L83
            r0.close()
        L83:
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.dto.utils.AlbumDataHelper.queryAlbumInfo(android.content.ContentResolver, long):java.lang.String");
    }
}
