package com.miui.gallery.cloud;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.ScannerStrategy;
import com.miui.gallery.cloudcontrol.strategies.WhiteAlbumsStrategy;
import com.miui.gallery.data.DBCloud;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBOwnerSubUbiImage;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.data.DBShareImage;
import com.miui.gallery.data.DBShareSubUbiImage;
import com.miui.gallery.data.DBShareUser;
import com.miui.gallery.data.DecodeUtils;
import com.miui.gallery.data.LocalUbifocus;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.model.SecretInfo;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.GalleryProvider;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.CursorUtils;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.HttpUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.SecretAlbumCryptoUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.UbiFocusUtils;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import com.miui.settings.Settings;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class CloudUtils {
    @Deprecated
    public static final String HTTPHOST_GALLERY_V1;
    @Deprecated
    public static final String HTTPHOST_GALLERY_V1_1;
    @Deprecated
    public static final String HTTPHOST_GALLERY_V2;
    @Deprecated
    public static final String HTTPHOST_GALLERY_V2_1;
    @Deprecated
    public static final String HTTPHOST_GALLERY_V2_2;
    public static final String HTTPHOST_GALLERY_V3;
    public static final String MAIN_MICRO_FOLDER_CACHE;
    public static final String PHOTO_LOCAL_FLAG_SYNCED;
    public static final String SELECTION_ALBUM_CAN_SYNC;
    public static final String SELECTION_CAN_SYNC;
    public static final String SELECTION_EDITED;
    public static final String SELECTION_GROUP_AUTO_UPLOAD;
    public static final String SELECTION_ITEM_WITHOUT_VALID_FILE;
    public static final String SELECTION_NOT_SYNCED_OR_EDITED;
    public static final String SELECTION_OWNER_ALBUM_NEED_SYNC;
    public static final String SELECTION_OWNER_NEED_SYNC;
    public static final String SELECTION_PURGE_OR_RECOVERY;
    public static final String SELECTION_SERVER_STATUS_CAN_SYNC;
    public static final String SELECTION_SERVER_STATUS_CLEAR;
    public static final String itemIsNotGroup;
    public static final String itemIsVideo;
    public static final String photoLocalFlag_Create_ForceCreate_Move_Copy_Rename;
    public static ScannerStrategy.FileSizeLimitStrategy sFileSizeLimitStrategy;
    public static final Object sLock;
    public static LazyValue<String, Long> sMinFileSizeLimit;

    /* loaded from: classes.dex */
    public interface ThumbnailFactory {
        Bitmap create();

        String getDirectory();

        String getFileName();
    }

    static {
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s = '%s' OR %s = '%s')", "serverStatus", "toBePurged", "serverStatus", "recovery");
        SELECTION_PURGE_OR_RECOVERY = format;
        String format2 = String.format(locale, "(%s NOT NULL AND %s!='') AND (%s IS NULL OR %s!=%d) AND (%s IS NULL OR %s='' OR %s='%s')", "editedColumns", "editedColumns", "localFlag", "localFlag", 2, "serverStatus", "serverStatus", "serverStatus", "custom");
        SELECTION_EDITED = format2;
        String str = "localFlag != 0 OR " + format + " OR " + format2;
        SELECTION_NOT_SYNCED_OR_EDITED = str;
        String format3 = String.format(locale, "(%s = %d OR %s = %d)", "serverType", 1, "serverType", 2);
        itemIsNotGroup = format3;
        itemIsVideo = String.format(locale, "(%s = %d)", "serverType", 2);
        SELECTION_ITEM_WITHOUT_VALID_FILE = String.format(locale, "((%s IS NULL OR %s = '') AND (%s IS NULL OR %s = ''))", "thumbnailFile", "thumbnailFile", "localFile", "localFile");
        String format4 = String.format(locale, "(%s = '%s' OR %s = '%s' OR %s = '%s')", "serverStatus", "custom", "serverStatus", "toBePurged", "serverStatus", "recovery");
        SELECTION_SERVER_STATUS_CAN_SYNC = format4;
        String format5 = String.format(locale, "((%s & %d != 0 AND %s = '%s' ))", "attributes", 1L, "serverStatus", "custom");
        SELECTION_GROUP_AUTO_UPLOAD = format5;
        SELECTION_SERVER_STATUS_CLEAR = String.format(locale, "(%s = '%s' OR %s = '%s' OR %s = '%s')", "serverStatus", "deleted", "serverStatus", "purged", "serverStatus", "toBePurged");
        String format6 = String.format(locale, "(((%s = %d OR %s = %d OR %s = %d) AND %s) OR (%s AND (%s IN (%d, %d) OR (%s IN (SELECT %s FROM %s WHERE %s)))))", "localFlag", 0, "localFlag", 2, "localFlag", 10, format4, format3, "localGroupId", Long.valueOf(CloudTableUtils.getCloudIdForGroupWithoutRecord(1000L)), Long.valueOf(CloudTableUtils.getCloudIdForGroupWithoutRecord(1001L)), "localGroupId", j.c, "album", format5);
        SELECTION_CAN_SYNC = format6;
        SELECTION_OWNER_NEED_SYNC = "(" + str + ")  AND " + format6;
        String format7 = String.format(locale, "(((%s = %d OR %s = %d OR %s = %d) AND %s='%s') OR (%s & %d != 0))", "localFlag", 0, "localFlag", 2, "localFlag", 10, "serverStatus", "custom", "attributes", 1L);
        SELECTION_ALBUM_CAN_SYNC = format7;
        SELECTION_OWNER_ALBUM_NEED_SYNC = "(localFlag != 0 OR " + format2 + ")  AND " + format7;
        StringBuilder sb = new StringBuilder();
        sb.append(ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost());
        sb.append("/mic/v1/gallery");
        HTTPHOST_GALLERY_V1 = sb.toString();
        HTTPHOST_GALLERY_V1_1 = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost() + "/mic/v1.1/gallery";
        HTTPHOST_GALLERY_V2 = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost() + "/mic/gallery/v2";
        HTTPHOST_GALLERY_V2_1 = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost() + "/mic/gallery/v2.1";
        HTTPHOST_GALLERY_V2_2 = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost() + "/mic/gallery/v2.2";
        HTTPHOST_GALLERY_V3 = ApplicationHelper.getMiCloudProvider().getCloudManager().getGalleryHost() + "/mic/gallery/v3";
        sLock = new Object();
        sMinFileSizeLimit = new LazyValue<String, Long>() { // from class: com.miui.gallery.cloud.CloudUtils.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public Long mo1272onInit(String str2) {
                return Long.valueOf(Preference.sGetFilterMinSize());
            }
        };
        MAIN_MICRO_FOLDER_CACHE = StorageUtils.getSafePriorMicroThumbnailPath();
        PHOTO_LOCAL_FLAG_SYNCED = String.format(locale, "(%s = %d AND %s = '%s')", "localFlag", 0, "serverStatus", "custom");
        photoLocalFlag_Create_ForceCreate_Move_Copy_Rename = String.format(locale, "(%s = %d OR %s = %d OR %s = %d OR %s = %d OR %s = %d OR %s = %d )", "localFlag", 8, "localFlag", 7, "localFlag", 5, "localFlag", 6, "localFlag", 9, "localFlag", 10);
    }

    public static ScannerStrategy.FileSizeLimitStrategy getFileSizeLimitStrategy() {
        ScannerStrategy.FileSizeLimitStrategy fileSizeLimitStrategy;
        synchronized (sLock) {
            if (sFileSizeLimitStrategy == null) {
                ScannerStrategy.FileSizeLimitStrategy fileSizeLimitStrategy2 = CloudControlStrategyHelper.getFileSizeLimitStrategy();
                sFileSizeLimitStrategy = fileSizeLimitStrategy2;
                DefaultLogger.d("CloudUtils", fileSizeLimitStrategy2);
            }
            fileSizeLimitStrategy = sFileSizeLimitStrategy;
        }
        return fileSizeLimitStrategy;
    }

    public static void putServerColumnsNull(ContentValues contentValues) {
        contentValues.putNull("serverId");
        contentValues.putNull("serverTag");
        contentValues.putNull("serverStatus");
        contentValues.putNull("groupId");
    }

    public static void putLocalImageIdColumnsNull(ContentValues contentValues) {
        contentValues.putNull("localImageId");
    }

    public static ContentValues getContentValuesForAll(JSONObject jSONObject) throws JSONException {
        return getContentValuesForAll(jSONObject, true);
    }

    public static ContentValues getContentValuesForAll(JSONObject jSONObject, boolean z) throws JSONException {
        String str = null;
        if (jSONObject == null) {
            return null;
        }
        if (jSONObject.has(MiStat.Param.CONTENT)) {
            jSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT);
        }
        ContentValues contentValuesForDefault = getContentValuesForDefault(jSONObject);
        contentValuesForDefault.put("fileName", jSONObject.getString("fileName"));
        if (jSONObject.has("groupId")) {
            long longAttributeFromJson = getLongAttributeFromJson(jSONObject, "groupId");
            contentValuesForDefault.put("groupId", Long.valueOf(longAttributeFromJson));
            if (jSONObject.has("sourcePackage")) {
                contentValuesForDefault.put("source_pkg", jSONObject.getString("sourcePackage"));
            } else if (longAttributeFromJson == 2) {
                contentValuesForDefault.put("source_pkg", PackageUtils.gePackageNameForScreenshot(contentValuesForDefault.getAsString("fileName")));
                contentValuesForDefault.put("location", PackageUtils.getAppNameForScreenshot(contentValuesForDefault.getAsString("fileName")));
            }
        }
        if (ApplicationHelper.isAutoUploadFeatureOpen() && jSONObject.has("appKey")) {
            contentValuesForDefault.put("appKey", jSONObject.getString("appKey"));
        }
        if (jSONObject.has(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE)) {
            contentValuesForDefault.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(getLongAttributeFromJson(jSONObject, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE)));
        }
        if (jSONObject.has("dateModified")) {
            contentValuesForDefault.put("dateModified", Long.valueOf(getLongAttributeFromJson(jSONObject, "dateModified")));
        }
        if (jSONObject.has("mimeType")) {
            contentValuesForDefault.put("mimeType", jSONObject.getString("mimeType"));
        }
        if (jSONObject.has("title")) {
            contentValuesForDefault.put("title", jSONObject.getString("title"));
        }
        if (jSONObject.has("description")) {
            String string = jSONObject.getString("description");
            DefaultLogger.i("CloudUtils", "the description from cloud is %s", string);
            contentValuesForDefault.put("description", string);
            parseDescription(contentValuesForDefault, string);
        }
        if (jSONObject.has("dateTaken")) {
            contentValuesForDefault.put("dateTaken", Long.valueOf(getLongAttributeFromJson(jSONObject, "dateTaken")));
        }
        if (jSONObject.has("duration")) {
            contentValuesForDefault.put("duration", Integer.valueOf(jSONObject.getInt("duration")));
        }
        if (jSONObject.has(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE)) {
            contentValuesForDefault.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Long.valueOf(getLongAttributeFromJson(jSONObject, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE)));
        }
        if (z && jSONObject.has("shareId")) {
            contentValuesForDefault.put("shareId", jSONObject.getString("shareId"));
        }
        if (jSONObject.has("albumId")) {
            contentValuesForDefault.put("albumId", jSONObject.getString("albumId"));
        }
        if (jSONObject.has("creatorInfo")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("creatorInfo");
            if (jSONObject2.has("creatorId")) {
                contentValuesForDefault.put("creatorId", jSONObject2.getString("creatorId"));
            }
        }
        if (jSONObject.has("isPublic")) {
            if (jSONObject.getBoolean("isPublic")) {
                contentValuesForDefault.put("isPublic", (Integer) 1);
                if (jSONObject.has("publicUrl")) {
                    contentValuesForDefault.put("publicUrl", jSONObject.getString("publicUrl"));
                }
            } else {
                contentValuesForDefault.put("isPublic", (Integer) 0);
                contentValuesForDefault.putNull("publicUrl");
            }
        }
        if (jSONObject.has("ubiDefaultIndex")) {
            contentValuesForDefault.put("ubiSubIndex", Integer.valueOf(jSONObject.getInt("ubiDefaultIndex")));
        }
        if (jSONObject.has("ubiSubImageCount")) {
            contentValuesForDefault.put("ubiSubImageCount", Integer.valueOf(jSONObject.getInt("ubiSubImageCount")));
        }
        if (jSONObject.has("currentFocusIndex")) {
            contentValuesForDefault.put("ubiFocusIndex", Integer.valueOf(jSONObject.getInt("currentFocusIndex")));
        }
        if (jSONObject.has("exifInfo")) {
            JSONObject jSONObject3 = jSONObject.getJSONObject("exifInfo");
            Iterator<DBImage.ExifDataConst> it = DBImage.sExifDataConst.iterator();
            while (it.hasNext()) {
                DBImage.ExifDataConst next = it.next();
                int i = next.exifValueType;
                if (i != 0) {
                    if (i == 1) {
                        if (jSONObject3.has(next.cloudTagName)) {
                            contentValuesForDefault.put(GalleryDBHelper.getInstance().getCloudColumns().get(next.columnIndex).name, jSONObject3.getString(next.cloudTagName));
                        }
                    } else {
                        DefaultLogger.e("CloudUtils", "exifDataConst.exifValueType is wrong: " + next.exifValueType);
                    }
                } else if (jSONObject3.has(next.cloudTagName)) {
                    contentValuesForDefault.put(GalleryDBHelper.getInstance().getCloudColumns().get(next.columnIndex).name, Long.valueOf(getLongAttributeFromJson(jSONObject3, next.cloudTagName)));
                }
            }
        }
        if (jSONObject.has("geoInfo")) {
            JSONObject jSONObject4 = jSONObject.getJSONObject("geoInfo");
            String optString = jSONObject4.optString("addressList");
            String optString2 = jSONObject4.optString("address");
            boolean optBoolean = jSONObject4.optBoolean("isAccurate", true);
            String optString3 = jSONObject4.optString("gps");
            if (optString != null && optString.length() > 0) {
                JSONArray jSONArray = new JSONArray(optString);
                if (!optBoolean) {
                    str = optString3;
                }
                contentValuesForDefault.put("location", LocationManager.formatAddress(jSONArray, str));
                contentValuesForDefault.put("address", optString);
            } else if (optString2 != null) {
                if (!optBoolean) {
                    str = optString3;
                }
                contentValuesForDefault.put("location", LocationManager.formatAddress(optString2, str));
            }
            if (!optBoolean && !TextUtils.isEmpty(optString3)) {
                contentValuesForDefault.put("extraGPS", optString3);
            }
        }
        return contentValuesForDefault;
    }

    public static void parseDescription(ContentValues contentValues, String str, String str2) {
        if (!TextUtils.isEmpty(str2)) {
            try {
                String optString = new JSONObject(str2).optString("localFile");
                if (TextUtils.isEmpty(optString)) {
                    return;
                }
                contentValues.put(str, optString);
            } catch (Exception e) {
                DefaultLogger.w("CloudUtils", e);
            }
        }
    }

    public static void parseDescription(ContentValues contentValues, String str) {
        parseDescription(contentValues, "localFile", str);
    }

    public static Long getSpecialTypeFlagsFromDescription(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (!jSONObject.has("specialTypeFlags")) {
                    return null;
                }
                return Long.valueOf(jSONObject.getLong("specialTypeFlags"));
            } catch (Exception e) {
                DefaultLogger.w("CloudUtils", e);
                return null;
            }
        }
        return null;
    }

    public static void reviseSpecialTypeFlags(ContentValues contentValues, DBImage dBImage) {
        Long specialTypeFlagsFromDescription = getSpecialTypeFlagsFromDescription(contentValues.getAsString("description"));
        if (dBImage != null && specialTypeFlagsFromDescription != null) {
            specialTypeFlagsFromDescription = Long.valueOf(specialTypeFlagsFromDescription.longValue() | dBImage.getSpecialTypeFlags());
        }
        if (specialTypeFlagsFromDescription != null) {
            contentValues.put("specialTypeFlags", specialTypeFlagsFromDescription);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002e A[Catch: JSONException -> 0x004a, TryCatch #1 {JSONException -> 0x004a, blocks: (B:3:0x0001, B:11:0x002e, B:12:0x0033, B:8:0x0012, B:5:0x000b), top: B:19:0x0001, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String collectMediaDescription(com.miui.gallery.data.DBImage r6) {
        /*
            r0 = 0
            java.lang.String r1 = r6.getDescription()     // Catch: org.json.JSONException -> L4a
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch: org.json.JSONException -> L4a
            if (r2 != 0) goto L2b
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: org.json.JSONException -> L11
            r2.<init>(r1)     // Catch: org.json.JSONException -> L11
            goto L2c
        L11:
            r2 = move-exception
            java.lang.String r3 = "CloudUtils"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: org.json.JSONException -> L4a
            r4.<init>()     // Catch: org.json.JSONException -> L4a
            java.lang.String r5 = "failed to parse: "
            r4.append(r5)     // Catch: org.json.JSONException -> L4a
            r4.append(r1)     // Catch: org.json.JSONException -> L4a
            java.lang.String r1 = r4.toString()     // Catch: org.json.JSONException -> L4a
            com.miui.gallery.util.logger.DefaultLogger.w(r3, r1)     // Catch: org.json.JSONException -> L4a
            r2.printStackTrace()     // Catch: org.json.JSONException -> L4a
        L2b:
            r2 = r0
        L2c:
            if (r2 != 0) goto L33
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch: org.json.JSONException -> L4a
            r2.<init>()     // Catch: org.json.JSONException -> L4a
        L33:
            java.lang.String r1 = "isFavorite"
            boolean r3 = r6.isFavorite()     // Catch: org.json.JSONException -> L4a
            r2.put(r1, r3)     // Catch: org.json.JSONException -> L4a
            java.lang.String r1 = "specialTypeFlags"
            long r3 = r6.getSpecialTypeFlags()     // Catch: org.json.JSONException -> L4a
            r2.put(r1, r3)     // Catch: org.json.JSONException -> L4a
            java.lang.String r6 = r2.toString()     // Catch: org.json.JSONException -> L4a
            return r6
        L4a:
            r6 = move-exception
            r6.printStackTrace()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.CloudUtils.collectMediaDescription(com.miui.gallery.data.DBImage):java.lang.String");
    }

    public static ContentValues getContentValuesForAllAndThumbNull(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return null;
        }
        if (jSONObject.has(MiStat.Param.CONTENT)) {
            jSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT);
        }
        ContentValues contentValuesForAll = getContentValuesForAll(jSONObject);
        contentValuesForAll.putNull("thumbnailFile");
        contentValuesForAll.putNull("microthumbfile");
        return contentValuesForAll;
    }

    public static ContentValues getContentValuesForDefault(JSONObject jSONObject) throws JSONException {
        ContentValues contentValues = new ContentValues();
        if (jSONObject.has("id")) {
            contentValues.put("serverId", Long.valueOf(getLongAttributeFromJson(jSONObject, "id")));
        }
        if (jSONObject.has("status")) {
            contentValues.put("serverStatus", jSONObject.getString("status"));
        }
        if (jSONObject.has(nexExportFormat.TAG_FORMAT_TAG)) {
            contentValues.put("serverTag", Long.valueOf(getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG)));
        }
        int serverTypeFromResponse = getServerTypeFromResponse(jSONObject);
        if (-1 != serverTypeFromResponse) {
            contentValues.put("serverType", Integer.valueOf(serverTypeFromResponse));
        }
        if (jSONObject.has("sha1")) {
            contentValues.put("sha1", jSONObject.getString("sha1"));
        }
        if (jSONObject.has("labels")) {
            JSONArray jSONArray = jSONObject.getJSONArray("labels");
            if (jSONArray.length() > 0) {
                int i = 0;
                while (true) {
                    if (i >= jSONArray.length()) {
                        break;
                    }
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                    if (jSONObject2.has("label") && "pet".equalsIgnoreCase(jSONObject2.getString("label"))) {
                        contentValues.put("lables", (Integer) 1);
                        break;
                    }
                    i++;
                }
            }
        }
        return contentValues;
    }

    public static int getServerTypeFromResponse(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has(nexExportFormat.TAG_FORMAT_TYPE)) {
            String string = jSONObject.getString(nexExportFormat.TAG_FORMAT_TYPE);
            if (string.equals("image")) {
                return 1;
            }
            if (string.equals("video")) {
                return 2;
            }
            if (string.equals("group")) {
                return 0;
            }
            DefaultLogger.e("CloudUtils", "error server type:" + string);
            return 0;
        }
        return -1;
    }

    public static ContentValues getContentValuesForUploadDeletePurged(JSONObject jSONObject) throws JSONException {
        ContentValues contentValues = new ContentValues();
        if (jSONObject != null) {
            if (jSONObject.has(MiStat.Param.CONTENT)) {
                jSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT);
            }
            contentValues = getContentValuesForDefault(jSONObject);
        }
        contentValues.put("localFlag", (Integer) 0);
        return contentValues;
    }

    public static String moveImage(String str, String str2) throws StoragePermissionMissingException {
        String appendInvokerTag;
        DocumentFile documentFile;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || (documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, (appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "moveImage")))) == null || !documentFile.exists()) {
            return str;
        }
        LinkedList linkedList = new LinkedList();
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(str, IStoragePermissionStrategy.Permission.DELETE);
        if (!checkPermission.granted) {
            linkedList.add(checkPermission);
        }
        IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(str2, IStoragePermissionStrategy.Permission.INSERT);
        if (!checkPermission2.granted) {
            linkedList.add(checkPermission2);
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        if (!StorageSolutionProvider.get().moveFile(str, str2, appendInvokerTag)) {
            return str;
        }
        long lastModified = documentFile.lastModified();
        if (lastModified != 0) {
            StorageSolutionProvider.get().setLastModified(StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.UPDATE, appendInvokerTag), lastModified);
        }
        return str2;
    }

    public static void deleteDirty(DBImage dBImage) {
        Uri baseUri = dBImage.getBaseUri();
        GalleryUtils.safeDelete(baseUri, "_id = '" + dBImage.getId() + "'", null);
        UbiFocusUtils.safeDeleteSubUbi(dBImage);
    }

    public static File saveBitmapToFile(Bitmap bitmap, String str, String str2) {
        DocumentFile documentFile;
        String extensionWithFileName = BaseFileUtils.getExtensionWithFileName(str2);
        File file = new File(str + h.g + str2);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "saveBitmapToFile");
        OutputStream outputStream = null;
        if (StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) == null || (documentFile = StorageSolutionProvider.get().getDocumentFile(file.getAbsolutePath(), IStoragePermissionStrategy.Permission.INSERT, appendInvokerTag)) == null) {
            return null;
        }
        try {
            outputStream = StorageSolutionProvider.get().openOutputStream(documentFile);
            GalleryUtils.saveBitmapToOutputStream(bitmap, GalleryUtils.convertExtensionToCompressFormat(extensionWithFileName), outputStream);
            return file;
        } finally {
            BaseMiscUtil.closeSilently(outputStream);
        }
    }

    public static String getThumbnailNameBySha1(String str) {
        return str + ".jpg";
    }

    public static String getThumbnailNameByTitle(String str) {
        return str + ".jpg";
    }

    public static String getMicroPath(String str) {
        return BaseFileUtils.concat(MAIN_MICRO_FOLDER_CACHE, getThumbnailNameBySha1(str));
    }

    /* loaded from: classes.dex */
    public static class VideoThumbnailFactory implements ThumbnailFactory {
        public final String mFileName;
        public final String mThumbnailDir;
        public final String mVideoPath;

        public VideoThumbnailFactory(String str, String str2, String str3) {
            this.mVideoPath = str;
            this.mThumbnailDir = str2;
            this.mFileName = str3;
        }

        @Override // com.miui.gallery.cloud.CloudUtils.ThumbnailFactory
        public Bitmap create() {
            return BitmapUtils.createVideoThumbnail(this.mVideoPath);
        }

        @Override // com.miui.gallery.cloud.CloudUtils.ThumbnailFactory
        public String getDirectory() {
            return this.mThumbnailDir;
        }

        @Override // com.miui.gallery.cloud.CloudUtils.ThumbnailFactory
        public String getFileName() {
            return this.mFileName;
        }
    }

    public static File buildThumbnail(String str, ThumbnailFactory thumbnailFactory, ThumbnailMetaWriter thumbnailMetaWriter) {
        String fileName = thumbnailFactory.getFileName();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "buildThumbnail");
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(new File(thumbnailFactory.getDirectory(), fileName).getAbsolutePath(), IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
        if (documentFile == null || !documentFile.exists()) {
            Bitmap create = thumbnailFactory.create();
            if (create == null) {
                return null;
            }
            try {
                File saveBitmapToFile = saveBitmapToFile(create, thumbnailFactory.getDirectory(), fileName);
                if (saveBitmapToFile == null) {
                    return null;
                }
                if (!thumbnailMetaWriter.write(saveBitmapToFile.getAbsolutePath()) && !StorageUtils.getSafePriorMicroThumbnailPath().equals(thumbnailFactory.getDirectory())) {
                    DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(saveBitmapToFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile2 == null) {
                        if (!create.isRecycled()) {
                            create.recycle();
                        }
                        return null;
                    }
                    documentFile2.delete();
                    if (!create.isRecycled()) {
                        create.recycle();
                    }
                    return null;
                }
                if (!create.isRecycled()) {
                    create.recycle();
                }
                return saveBitmapToFile;
            } finally {
                if (!create.isRecycled()) {
                    create.recycle();
                }
            }
        }
        return new File(thumbnailFactory.getDirectory(), fileName);
    }

    public static String readFileNameFromExif(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                ExifUtil.UserCommentData userCommentData = ExifUtil.getUserCommentData(str);
                if (userCommentData == null) {
                    return null;
                }
                return userCommentData.getFileName(BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(str)));
            } catch (Exception e) {
                DefaultLogger.e("CloudUtils", "Failed to read exif!!", e);
            }
        }
        return null;
    }

    public static String buildBigThumbnailsForVideo(String str, String str2, String str3, String str4, ThumbnailMetaWriter thumbnailMetaWriter) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.w("CloudUtils", "failed to convert, sourceFile=%s", str);
            return null;
        } else if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
            DefaultLogger.w("CloudUtils", "failed to convert, sourceFile=%s, thumbnailDir=%s, thumbnailFileName=%s", str, str3, str4);
            return null;
        } else {
            File buildThumbnail = buildThumbnail(str, new VideoThumbnailFactory(str, str3, str4), thumbnailMetaWriter);
            if (buildThumbnail != null) {
                return buildThumbnail.getAbsolutePath();
            }
            return null;
        }
    }

    public static String buildBigThumbnailForImage(final String str, String str2, final String str3, final String str4, ThumbnailMetaWriter thumbnailMetaWriter) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.w("CloudUtils", "failed to convert, sourceFile=%s", str);
            return null;
        } else if (TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4)) {
            DefaultLogger.w("CloudUtils", "failed to convert, sourceFile=%s, thumbnailDir=%s, thumbnailFileName=%s", str, str3, str4);
            return null;
        } else {
            File buildThumbnail = buildThumbnail(str, new ThumbnailFactory() { // from class: com.miui.gallery.cloud.CloudUtils.3
                @Override // com.miui.gallery.cloud.CloudUtils.ThumbnailFactory
                public String getDirectory() {
                    return str3;
                }

                @Override // com.miui.gallery.cloud.CloudUtils.ThumbnailFactory
                public Bitmap create() {
                    DecodeUtils.GalleryOptions galleryOptions = new DecodeUtils.GalleryOptions();
                    ((BitmapFactory.Options) galleryOptions).inPreferredConfig = Bitmap.Config.ARGB_8888;
                    return DecodeUtils.requestDecodeThumbNail(str, galleryOptions);
                }

                @Override // com.miui.gallery.cloud.CloudUtils.ThumbnailFactory
                public String getFileName() {
                    return str4;
                }
            }, thumbnailMetaWriter);
            if (buildThumbnail == null) {
                return null;
            }
            return buildThumbnail.getAbsolutePath();
        }
    }

    public static void addExtraParams(List<NameValuePair> list) {
        Pair<String, String> appLifecycleParameter = HttpUtils.getAppLifecycleParameter();
        list.add(new BasicNameValuePair((String) appLifecycleParameter.first, (String) appLifecycleParameter.second));
        Pair<String, String> apkVersionParameter = HttpUtils.getApkVersionParameter();
        list.add(new BasicNameValuePair((String) apkVersionParameter.first, (String) apkVersionParameter.second));
    }

    public static JSONObject postToXiaomi(String str, List<NameValuePair> list, JSONObject jSONObject, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, int i, boolean z) throws IllegalBlockSizeException, BadPaddingException, JSONException, IOException, GalleryMiCloudServerException {
        String httpPostRequestForString;
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("CloudUtils", "CTA not confirmed when post request");
            return null;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        addRetryParameters(list, i, z, galleryExtendedAuthToken);
        addExtraParams(list);
        if (jSONObject != null) {
            list.add(new BasicNameValuePair("data", jSONObject.toString()));
        }
        DefaultLogger.d("CloudUtils", "POST URL:" + str);
        if (galleryExtendedAuthToken != null) {
            httpPostRequestForString = ApplicationHelper.getMiCloudProvider().securePost(str, getParamsMap(list));
        } else {
            httpPostRequestForString = NetworkUtils.httpPostRequestForString(str, new UrlEncodedFormEntity(list, Keyczar.DEFAULT_ENCODING), null);
        }
        return new JSONObject(httpPostRequestForString);
    }

    public static JSONObject getFromXiaomi(String str, List<NameValuePair> list, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, int i, boolean z) throws IllegalBlockSizeException, BadPaddingException, JSONException, IOException, URISyntaxException, GalleryMiCloudServerException {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.d("CloudUtils", "CTA not confirmed when get from server");
            return null;
        }
        if (list == null) {
            list = new ArrayList<>();
        }
        addRetryParameters(list, i, z, galleryExtendedAuthToken);
        addExtraParams(list);
        DefaultLogger.d("CloudUtils", "GET URL:" + str);
        return new JSONObject(ApplicationHelper.getMiCloudProvider().secureGet(str, getParamsMap(list)));
    }

    public static Map<String, String> getParamsMap(List<NameValuePair> list) {
        HashMap newHashMap = Maps.newHashMap();
        for (NameValuePair nameValuePair : list) {
            newHashMap.put(nameValuePair.getName(), nameValuePair.getValue());
        }
        return newHashMap;
    }

    public static void addRetryParameters(List<NameValuePair> list, int i, boolean z, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        if (i > 0) {
            list.add(new BasicNameValuePair("retry", Integer.toString(i)));
        }
        if (z) {
            list.add(new BasicNameValuePair("needReRequest", String.valueOf(z)));
        }
    }

    public static int getServerType(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        if (!BaseFileMimeUtil.isImageFromMimeType(mimeType)) {
            if (!BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
                String extension = BaseFileUtils.getExtension(str);
                if (!extension.equalsIgnoreCase("y") && !extension.equalsIgnoreCase("img")) {
                    if (!extension.equalsIgnoreCase("vid")) {
                        return -1;
                    }
                }
            }
            return 2;
        }
        return 1;
    }

    public static DBShareAlbum getDBShareAlbumByLocalId(String str) {
        return getDBShareAlbum(j.c, str);
    }

    public static DBShareAlbum getDBShareAlbumBySharedId(String str) {
        return getDBShareAlbum("albumId", str);
    }

    public static DBShareAlbum getDBShareAlbum(String str, String str2) {
        Cursor cursor = null;
        try {
            Cursor query = GalleryApp.sGetAndroidContext().getContentResolver().query(getLimitUri(GalleryCloudUtils.SHARE_ALBUM_URI, 1), getProjectionAll(), String.format(Locale.US, "%s=?", str), new String[]{str2}, null);
            try {
                if (query != null) {
                    if (query.moveToNext()) {
                        DBShareAlbum dBShareAlbum = new DBShareAlbum(query);
                        query.close();
                        return dBShareAlbum;
                    }
                } else {
                    DefaultLogger.d("CloudUtils", "there isn't have any share album in local DB for " + str + " = " + str2);
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x004c A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.ArrayList<com.miui.gallery.data.DBShareAlbum> getEmptyDBShareAlbums() {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1 = 0
            android.content.Context r2 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> L50
            android.content.ContentResolver r3 = r2.getContentResolver()     // Catch: java.lang.Throwable -> L50
            android.net.Uri r4 = com.miui.gallery.cloud.GalleryCloudUtils.SHARE_ALBUM_URI     // Catch: java.lang.Throwable -> L50
            java.lang.String[] r5 = getProjectionAll()     // Catch: java.lang.Throwable -> L50
            java.util.Locale r2 = java.util.Locale.US     // Catch: java.lang.Throwable -> L50
            java.lang.String r6 = "%s IS NULL AND %s IS '%s'"
            r7 = 3
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch: java.lang.Throwable -> L50
            r8 = 0
            java.lang.String r9 = "albumImageTag"
            r7[r8] = r9     // Catch: java.lang.Throwable -> L50
            r8 = 1
            java.lang.String r9 = "serverStatus"
            r7[r8] = r9     // Catch: java.lang.Throwable -> L50
            r8 = 2
            java.lang.String r9 = "custom"
            r7[r8] = r9     // Catch: java.lang.Throwable -> L50
            java.lang.String r6 = java.lang.String.format(r2, r6, r7)     // Catch: java.lang.Throwable -> L50
            r7 = 0
            r8 = 0
            android.database.Cursor r1 = r3.query(r4, r5, r6, r7, r8)     // Catch: java.lang.Throwable -> L50
            if (r1 == 0) goto L4a
            boolean r2 = r1.moveToFirst()     // Catch: java.lang.Throwable -> L50
            if (r2 == 0) goto L4a
        L3c:
            com.miui.gallery.data.DBShareAlbum r2 = new com.miui.gallery.data.DBShareAlbum     // Catch: java.lang.Throwable -> L50
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L50
            r0.add(r2)     // Catch: java.lang.Throwable -> L50
            boolean r2 = r1.moveToNext()     // Catch: java.lang.Throwable -> L50
            if (r2 != 0) goto L3c
        L4a:
            if (r1 == 0) goto L4f
            r1.close()
        L4f:
            return r0
        L50:
            r0 = move-exception
            if (r1 == 0) goto L56
            r1.close()
        L56:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.CloudUtils.getEmptyDBShareAlbums():java.util.ArrayList");
    }

    public static DBShareUser getDBShareUserInLocal(Uri uri, String str, String str2) {
        Cursor cursor = null;
        try {
            Cursor query = GalleryApp.sGetAndroidContext().getContentResolver().query(getLimitUri(uri, 1), getProjectionAll(), "userId = ? AND albumId = ? ", new String[]{str, str2}, null);
            try {
                if (query != null) {
                    if (query.moveToNext()) {
                        DBShareUser dBShareUser = new DBShareUser(query);
                        query.close();
                        return dBShareUser;
                    }
                } else {
                    DefaultLogger.d("CloudUtils", "there isn't have any user for userId = " + str + ", albumId = " + str2);
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003e  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0086 A[Catch: all -> 0x00b9, TRY_ENTER, TryCatch #0 {all -> 0x00b9, blocks: (B:16:0x0086, B:18:0x008c, B:21:0x0095), top: B:31:0x0084 }] */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0095 A[Catch: all -> 0x00b9, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x00b9, blocks: (B:16:0x0086, B:18:0x008c, B:21:0x0095), top: B:31:0x0084 }] */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00b5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.data.DBShareUser getInvitedDBShareUserInLocal(android.net.Uri r15, java.lang.String r16, java.lang.String r17, java.lang.String r18, java.lang.String r19) {
        /*
            r0 = r16
            r1 = r17
            r2 = r18
            java.lang.String r3 = "userId"
            r4 = 0
            java.lang.String r5 = "friend"
            boolean r5 = android.text.TextUtils.equals(r2, r5)     // Catch: java.lang.Throwable -> Lbc
            r6 = 0
            r7 = 1
            if (r5 != 0) goto L1e
            java.lang.String r5 = "family"
            boolean r5 = android.text.TextUtils.equals(r2, r5)     // Catch: java.lang.Throwable -> Lbc
            if (r5 == 0) goto L1c
            goto L1e
        L1c:
            r5 = r6
            goto L1f
        L1e:
            r5 = r7
        L1f:
            android.content.Context r8 = com.miui.gallery.GalleryApp.sGetAndroidContext()     // Catch: java.lang.Throwable -> Lbc
            android.content.ContentResolver r9 = r8.getContentResolver()     // Catch: java.lang.Throwable -> Lbc
            r8 = r15
            android.net.Uri r10 = getLimitUri(r15, r7)     // Catch: java.lang.Throwable -> Lbc
            java.lang.String[] r11 = getProjectionAll()     // Catch: java.lang.Throwable -> Lbc
            java.util.Locale r8 = java.util.Locale.US     // Catch: java.lang.Throwable -> Lbc
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lbc
            r12.<init>()     // Catch: java.lang.Throwable -> Lbc
            java.lang.String r13 = "%s = '%s' AND (%s IS NULL OR %s = '%s') AND %s = '%s' AND %s = '%s'"
            r12.append(r13)     // Catch: java.lang.Throwable -> Lbc
            if (r5 == 0) goto L41
            java.lang.String r5 = " AND %s = '%s'"
            goto L43
        L41:
            java.lang.String r5 = ""
        L43:
            r12.append(r5)     // Catch: java.lang.Throwable -> Lbc
            java.lang.String r5 = r12.toString()     // Catch: java.lang.Throwable -> Lbc
            r12 = 11
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch: java.lang.Throwable -> Lbc
            java.lang.String r13 = "serverStatus"
            r12[r6] = r13     // Catch: java.lang.Throwable -> Lbc
            java.lang.String r6 = "invited"
            r12[r7] = r6     // Catch: java.lang.Throwable -> Lbc
            r6 = 2
            r12[r6] = r3     // Catch: java.lang.Throwable -> Lbc
            r6 = 3
            r12[r6] = r3     // Catch: java.lang.Throwable -> Lbc
            r3 = 4
            r12[r3] = r0     // Catch: java.lang.Throwable -> Lbc
            r3 = 5
            java.lang.String r6 = "albumId"
            r12[r3] = r6     // Catch: java.lang.Throwable -> Lbc
            r3 = 6
            r12[r3] = r1     // Catch: java.lang.Throwable -> Lbc
            r3 = 7
            java.lang.String r6 = "relation"
            r12[r3] = r6     // Catch: java.lang.Throwable -> Lbc
            r3 = 8
            r12[r3] = r2     // Catch: java.lang.Throwable -> Lbc
            r2 = 9
            java.lang.String r3 = "relationText"
            r12[r2] = r3     // Catch: java.lang.Throwable -> Lbc
            r2 = 10
            r12[r2] = r19     // Catch: java.lang.Throwable -> Lbc
            java.lang.String r12 = java.lang.String.format(r8, r5, r12)     // Catch: java.lang.Throwable -> Lbc
            r13 = 0
            r14 = 0
            android.database.Cursor r2 = r9.query(r10, r11, r12, r13, r14)     // Catch: java.lang.Throwable -> Lbc
            if (r2 == 0) goto L95
            boolean r0 = r2.moveToNext()     // Catch: java.lang.Throwable -> Lb9
            if (r0 == 0) goto Lb3
            com.miui.gallery.data.DBShareUser r0 = new com.miui.gallery.data.DBShareUser     // Catch: java.lang.Throwable -> Lb9
            r0.<init>(r2)     // Catch: java.lang.Throwable -> Lb9
            r2.close()
            return r0
        L95:
            java.lang.String r3 = "CloudUtils"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb9
            r5.<init>()     // Catch: java.lang.Throwable -> Lb9
            java.lang.String r6 = "there isn't have any user for userId = "
            r5.append(r6)     // Catch: java.lang.Throwable -> Lb9
            r5.append(r0)     // Catch: java.lang.Throwable -> Lb9
            java.lang.String r0 = ", albumId = "
            r5.append(r0)     // Catch: java.lang.Throwable -> Lb9
            r5.append(r1)     // Catch: java.lang.Throwable -> Lb9
            java.lang.String r0 = r5.toString()     // Catch: java.lang.Throwable -> Lb9
            com.miui.gallery.util.logger.DefaultLogger.d(r3, r0)     // Catch: java.lang.Throwable -> Lb9
        Lb3:
            if (r2 == 0) goto Lb8
            r2.close()
        Lb8:
            return r4
        Lb9:
            r0 = move-exception
            r4 = r2
            goto Lbd
        Lbc:
            r0 = move-exception
        Lbd:
            if (r4 == 0) goto Lc2
            r4.close()
        Lc2:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.CloudUtils.getInvitedDBShareUserInLocal(android.net.Uri, java.lang.String, java.lang.String, java.lang.String, java.lang.String):com.miui.gallery.data.DBShareUser");
    }

    public static DBImage getItemByServerID(Context context, String str) {
        return getItem(GalleryCloudUtils.CLOUD_URI, context, "serverId", str);
    }

    public static DBImage getItem(Uri uri, Context context, String str, String str2) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri limitUri = getLimitUri(uri, 1);
            String[] projectionAll = getProjectionAll();
            Cursor query = contentResolver.query(limitUri, projectionAll, str + " = '" + str2 + "'", null, null);
            try {
                if (query != null) {
                    if (query.moveToNext()) {
                        DBImage createDBImageByUri = createDBImageByUri(uri, query);
                        query.close();
                        return createDBImageByUri;
                    }
                } else {
                    DefaultLogger.d("CloudUtils", "there isn't have any item in local DB for " + str + " = " + str2);
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static DBImage getItem(Uri uri, Context context, String str) {
        Cursor cursor = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Cursor query = context.getContentResolver().query(getLimitUri(uri, 1), getProjectionAll(), str, null, null);
            try {
                if (query != null) {
                    if (query.moveToNext()) {
                        DBImage createDBImageByUri = createDBImageByUri(uri, query);
                        query.close();
                        return createDBImageByUri;
                    }
                } else {
                    DefaultLogger.d("CloudUtils", "there isn't have any item in local DB for " + str);
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static DBImage createDBImageByUri(Uri uri, Cursor cursor) {
        if (uri.equals(GalleryCloudUtils.CLOUD_URI)) {
            return new DBCloud(cursor);
        }
        if (uri.equals(GalleryCloudUtils.SHARE_IMAGE_URI)) {
            return new DBShareImage(cursor);
        }
        if (uri.equals(GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI)) {
            return new DBOwnerSubUbiImage(cursor);
        }
        if (!uri.equals(GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI)) {
            return null;
        }
        return new DBShareSubUbiImage(cursor);
    }

    public static boolean isShareUri(Uri uri) {
        return uri.equals(GalleryCloudUtils.SHARE_ALBUM_URI) || uri.equals(GalleryCloudUtils.SHARE_IMAGE_URI) || uri.equals(GalleryCloudUtils.SHARE_USER_URI);
    }

    public static DBShareAlbum getGroupItemByColumnnameAndValueFromShare(Context context, String str, String str2) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri cloudShareAlbumLimit1Uri = getCloudShareAlbumLimit1Uri();
            String[] projectionAll = getProjectionAll();
            Cursor query = contentResolver.query(cloudShareAlbumLimit1Uri, projectionAll, str + " = ? ", new String[]{str2}, null);
            if (query != null) {
                try {
                    if (query.moveToNext()) {
                        DBShareAlbum dBShareAlbum = new DBShareAlbum(query);
                        query.close();
                        return dBShareAlbum;
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

    public static List<String> getImageServerIdsBySHA1(String str) {
        Cursor cursor = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Cursor query = GalleryApp.sGetAndroidContext().getContentResolver().query(GalleryCloudUtils.CLOUD_URI, new String[]{"serverId"}, String.format(Locale.US, "%s AND (%s = '%s')", "(localFlag = 0 AND serverStatus = 'custom')", "sha1", str), null, null);
            if (query == null) {
                if (query != null) {
                    query.close();
                }
                return null;
            }
            try {
                ArrayList arrayList = new ArrayList(query.getCount());
                while (query.moveToNext()) {
                    String string = query.getString(0);
                    if (!TextUtils.isEmpty(string)) {
                        arrayList.add(string);
                    }
                }
                query.close();
                return arrayList;
            } catch (Throwable th) {
                th = th;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00c9, code lost:
        if (r3 != null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00d2, code lost:
        if (0 == 0) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00d4, code lost:
        r3.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00d7, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.miui.gallery.data.DBImage> getCandidateItemsInAGroup(android.content.Context r18, java.lang.String r19, java.lang.String r20, java.lang.String r21, boolean r22) {
        /*
            r0 = r20
            r1 = r21
            java.util.ArrayList r2 = com.google.common.collect.Lists.newArrayList()
            r3 = 0
            java.lang.Long r4 = java.lang.Long.valueOf(r21)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            long r4 = r4.longValue()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            boolean r4 = com.miui.gallery.cloud.CloudTableUtils.isGroupWithoutRecordByCloudId(r4)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r5.<init>()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r6 = com.miui.gallery.util.BaseFileUtils.getFileTitle(r19)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r5.append(r6)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r6 = "%"
            r5.append(r6)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r6 = "localGroupId"
            r7 = 4
            r8 = 3
            r9 = 2
            r10 = 1
            r11 = 0
            if (r4 == 0) goto L5e
            java.lang.Long r4 = java.lang.Long.valueOf(r21)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            long r12 = r4.longValue()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            long r12 = com.miui.gallery.cloud.CloudTableUtils.getServerIdForGroupWithoutRecord(r12)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String[] r4 = new java.lang.String[r7]     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r11] = r5     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r10] = r0     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r9] = r1     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r5 = java.lang.String.valueOf(r12)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r8] = r5     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.util.Locale r5 = java.util.Locale.US     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r12 = "(%s = ? OR %s = ?)"
            java.lang.Object[] r13 = new java.lang.Object[r9]     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r13[r11] = r6     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r6 = "groupId"
            r13[r10] = r6     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r5 = java.lang.String.format(r5, r12, r13)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            goto L72
        L5e:
            java.lang.String[] r4 = new java.lang.String[r8]     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r11] = r5     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r10] = r0     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r4[r9] = r1     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.util.Locale r5 = java.util.Locale.US     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r12 = "%s = ?"
            java.lang.Object[] r13 = new java.lang.Object[r10]     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r13[r11] = r6     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r5 = java.lang.String.format(r5, r12, r13)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
        L72:
            r16 = r4
            android.content.ContentResolver r12 = r18.getContentResolver()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            if (r22 == 0) goto L7d
            android.net.Uri r4 = com.miui.gallery.cloud.GalleryCloudUtils.SHARE_IMAGE_URI     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            goto L7f
        L7d:
            android.net.Uri r4 = com.miui.gallery.cloud.GalleryCloudUtils.CLOUD_URI     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
        L7f:
            r13 = r4
            java.lang.String[] r14 = getProjectionAll()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.util.Locale r4 = java.util.Locale.US     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r6 = "(%s like ? OR %s = ?) AND %s AND %s AND %s"
            r15 = 5
            java.lang.Object[] r15 = new java.lang.Object[r15]     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r17 = "fileName"
            r15[r11] = r17     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r11 = "sha1"
            r15[r10] = r11     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r10 = com.miui.gallery.cloud.CloudUtils.photoLocalFlag_Create_ForceCreate_Move_Copy_Rename     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r15[r9] = r10     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r9 = com.miui.gallery.cloud.CloudUtils.itemIsNotGroup     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r15[r8] = r9     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r15[r7] = r5     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            java.lang.String r15 = java.lang.String.format(r4, r6, r15)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r17 = 0
            android.database.Cursor r3 = r12.query(r13, r14, r15, r16, r17)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            if (r3 == 0) goto Lc0
        La9:
            boolean r0 = r3.moveToNext()     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            if (r0 == 0) goto Lc9
            if (r22 == 0) goto Lb7
            com.miui.gallery.data.DBShareImage r0 = new com.miui.gallery.data.DBShareImage     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r0.<init>(r3)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            goto Lbc
        Lb7:
            com.miui.gallery.data.DBCloud r0 = new com.miui.gallery.data.DBCloud     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            r0.<init>(r3)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
        Lbc:
            r2.add(r0)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
            goto La9
        Lc0:
            java.lang.String r4 = "CloudUtils"
            java.lang.String r5 = "there isn't have any item in group: %s for name: %s, sha1: %s"
            r6 = r19
            com.miui.gallery.util.logger.DefaultLogger.d(r4, r5, r1, r6, r0)     // Catch: java.lang.Throwable -> Lcc android.database.sqlite.SQLiteCantOpenDatabaseException -> Lce
        Lc9:
            if (r3 == 0) goto Ld7
            goto Ld4
        Lcc:
            r0 = move-exception
            goto Ld8
        Lce:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> Lcc
            if (r3 == 0) goto Ld7
        Ld4:
            r3.close()
        Ld7:
            return r2
        Ld8:
            if (r3 == 0) goto Ldd
            r3.close()
        Ldd:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.CloudUtils.getCandidateItemsInAGroup(android.content.Context, java.lang.String, java.lang.String, java.lang.String, boolean):java.util.List");
    }

    public static Uri getCloudLimitUri(int i) {
        return UriUtil.appendLimit(GalleryCloudUtils.CLOUD_URI, i);
    }

    public static Uri getCloudShareAlbumLimit1Uri() {
        return getLimitUri(GalleryCloudUtils.SHARE_ALBUM_URI, 1);
    }

    public static Uri getCloudLimit1Uri() {
        return getCloudLimitUri(1);
    }

    public static Uri getLimitUri(Uri uri, int i) {
        return UriUtil.appendLimit(uri, i);
    }

    public static Uri getLimitUri(Uri uri, int i, int i2) {
        return UriUtil.appendLimit(uri, i, i2);
    }

    public static Uri getCloudDistinctUri(Uri uri) {
        return UriUtil.appendDistinct(uri, true);
    }

    public static String[] getServerIdAndSha1ByLocalId(Context context, String str, RequestCloudItem requestCloudItem) {
        return getStringColumnValues(context, GalleryCloudUtils.CLOUD_URI, new String[]{"serverId", "sha1"}, j.c, str);
    }

    public static String getServerIdByLocalId(Context context, String str, RequestCloudItem requestCloudItem) {
        long serverIdForGroupWithoutRecord = CloudTableUtils.getServerIdForGroupWithoutRecord(Long.parseLong(str));
        if (serverIdForGroupWithoutRecord > 0) {
            return String.valueOf(serverIdForGroupWithoutRecord);
        }
        return getStringColumnValue(context, GalleryCloudUtils.CLOUD_URI, "serverId", j.c, str);
    }

    public static String getShareAlbumIdByLocalId(Context context, RequestCloudItem requestCloudItem) {
        return getStringColumnValue(context, GalleryCloudUtils.SHARE_ALBUM_URI, "albumId", j.c, requestCloudItem.dbCloud.getLocalGroupId());
    }

    public static String getShareIdByLocalId(Context context, String str) {
        return getStringColumnValue(context, GalleryCloudUtils.SHARE_IMAGE_URI, "shareId", j.c, str);
    }

    public static String getCreatorIdByAlbumId(String str) {
        return getStringColumnValue(getLimitUri(GalleryCloudUtils.SHARE_ALBUM_URI, 1), "creatorId", "albumId", str);
    }

    public static String getStringColumnValue(Uri uri, String str, String str2, String str3) {
        return getStringColumnValue(GalleryApp.sGetAndroidContext(), uri, str, str2, str3);
    }

    public static String[] getStringColumnValues(Context context, Uri uri, String[] strArr, String str, String str2) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri limitUri = getLimitUri(uri, 1);
            Cursor query = contentResolver.query(limitUri, strArr, str + " = '" + str2 + "'", null, null);
            try {
                if (query != null) {
                    if (query.moveToNext()) {
                        int columnCount = query.getColumnCount();
                        String[] strArr2 = new String[columnCount];
                        for (int i = 0; i < columnCount; i++) {
                            strArr2[i] = query.getString(i);
                        }
                        query.close();
                        return strArr2;
                    }
                } else {
                    DefaultLogger.d("CloudUtils", String.format("No item in DB for:  %s = %s", str, str2));
                }
                if (query != null) {
                    query.close();
                }
                return null;
            } catch (Throwable th) {
                th = th;
                cursor = query;
                if (cursor != null) {
                    cursor.close();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String getStringColumnValue(Context context, Uri uri, String str, String str2, String str3) {
        Cursor cursor = null;
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Uri limitUri = getLimitUri(uri, 1);
            String[] strArr = {str};
            Cursor query = contentResolver.query(limitUri, strArr, str2 + " = '" + str3 + "'", null, null);
            if (query != null) {
                if (query.moveToNext()) {
                    String cursorString = CursorUtils.getCursorString(query, 0);
                    query.close();
                    return cursorString;
                }
            } else {
                DefaultLogger.d("CloudUtils", String.format("No item in DB for:  %s = %s", str2, str3));
            }
            if (query == null) {
                return "";
            }
            query.close();
            return "";
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public static boolean hasCreateCopyMoveImageByGroupId(Context context, String str) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(getCloudLimit1Uri(), new String[]{j.c}, "localGroupId = ? AND (localFlag = ? OR localFlag = ? OR localFlag = ? OR localFlag = ? OR localFlag = ? ) ", new String[]{str, String.valueOf(6), String.valueOf(9), String.valueOf(8), String.valueOf(7), String.valueOf(5)}, null);
            if (query != null) {
                if (query.moveToNext()) {
                    query.close();
                    return true;
                }
            } else {
                DefaultLogger.d("CloudUtils", "there isn't have any item in local DB for localId = " + str);
            }
            if (query != null) {
                query.close();
            }
            return false;
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public static Account getAccountFromDB(Context context) {
        Throwable th;
        Cursor cursor;
        Account account = null;
        try {
            cursor = context.getContentResolver().query(GalleryCloudUtils.CLOUD_SETTING_URI, new String[]{"accountName", "accountType"}, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToNext()) {
                        String string = cursor.getString(0);
                        String string2 = cursor.getString(1);
                        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
                            account = new Account(string, string2);
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return account;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
        }
    }

    public static boolean update(Uri uri, String[] strArr, String[] strArr2, String str, boolean z) {
        int i;
        if (strArr == null || strArr2 == null || strArr.length != strArr2.length) {
            throw new IllegalArgumentException("illegal column names or values");
        }
        String matchTableName = GalleryProvider.matchTableName(uri);
        if (TextUtils.isEmpty(matchTableName)) {
            DefaultLogger.e("CloudUtils", "No table matched with provided uri: %s", uri.toString());
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            sb.append(strArr[i2]);
            sb.append("=");
            DatabaseUtils.appendEscapedSQLString(sb, strArr2[i2]);
            if (i2 < strArr.length - 1) {
                sb.append(", ");
            }
        }
        String format = String.format(Locale.US, "update %s set %s where _id=%s", matchTableName, sb.toString(), str);
        Bundle bundle = new Bundle();
        bundle.putString("statement", format);
        if (z) {
            bundle.putParcelable("notify_uri", uri);
        }
        Bundle call = GalleryApp.sGetAndroidContext().getContentResolver().call(uri, "raw_update", (String) null, bundle);
        boolean z2 = call != null && call.getBoolean("bool_result", false);
        if (z2) {
            ContentValues contentValues = new ContentValues();
            for (int i3 = 0; i3 < strArr.length; i3++) {
                contentValues.put(strArr[i3], strArr2[i3]);
            }
            if ("cloud".equals(matchTableName)) {
                i = MediaManager.getInstance().update("_id=?", new String[]{str}, contentValues);
            } else if ("shareImage".equals(matchTableName)) {
                i = ShareMediaManager.getInstance().update("_id=?", new String[]{String.valueOf(ShareMediaManager.convertToMediaId(Long.parseLong(str)))}, contentValues);
            }
            return z2 && i > 0;
        }
        i = 0;
        if (z2) {
            return false;
        }
    }

    public static int updateToLocalDB(Uri uri, ContentValues contentValues, DBImage dBImage) throws JSONException {
        return updateToLocalDB(uri, contentValues, dBImage.getId());
    }

    public static int updateToLocalDB(Uri uri, ContentValues contentValues, String str) {
        return GalleryUtils.safeUpdate(uri, contentValues, "_id = '" + str + "'", null);
    }

    public static void updateToLocalDBByServerId(Uri uri, ContentValues contentValues, String str) throws JSONException {
        GalleryUtils.safeUpdate(uri, contentValues, "serverId = '" + str + "'", null);
    }

    public static void updateToLocalDBForSync(Uri uri, ContentValues contentValues, DBImage dBImage) throws JSONException {
        updateToLocalDBForSync(uri, contentValues, dBImage.getId());
    }

    public static void updateToLocalDBForSync(Uri uri, ContentValues contentValues, String str) throws JSONException {
        updateToLocalDBForSync(uri, contentValues, str, false);
    }

    public static void updateToLocalDBForSync(Uri uri, ContentValues contentValues, String str, boolean z) throws JSONException {
        String str2;
        ContentValues contentValues2;
        if (contentValues != null) {
            String asString = contentValues.getAsString("serverId");
            if (!TextUtils.isEmpty(asString)) {
                contentValues2 = new ContentValues();
                contentValues2.putNull("serverId");
                str2 = String.format(Locale.US, "%s = '%s'", "serverId", asString);
            } else {
                str2 = null;
                contentValues2 = null;
            }
            if (isShareUri(uri)) {
                String asString2 = contentValues.getAsString("shareId");
                if (!TextUtils.isEmpty(asString2)) {
                    if (contentValues2 == null) {
                        contentValues2 = new ContentValues();
                    }
                    contentValues2.putNull("shareId");
                    if (str2 != null) {
                        str2 = String.format(Locale.US, "(%s) OR (%s = '%s')", str2, "shareId", asString2);
                    } else {
                        str2 = String.format(Locale.US, "%s = '%s'", "shareId", asString2);
                    }
                }
            }
            if (contentValues2 != null) {
                GalleryUtils.safeUpdate(uri, contentValues2, str2, null);
            }
            updateToLocalDB(uri, contentValues, str);
        } else {
            contentValues = new ContentValues();
        }
        if (!z) {
            contentValues.put("localFlag", (Integer) 0);
        }
        GalleryUtils.safeUpdate(uri, contentValues, String.format("_id = ? AND localFlag NOT IN(%s,%s)", String.valueOf(2), String.valueOf(15)), new String[]{str});
    }

    public static void updateFilePathForSync(Uri uri, ContentValues contentValues, DBImage dBImage) throws StoragePermissionMissingException {
        String str;
        String fileName = dBImage.getFileName();
        String asString = contentValues.getAsString("fileName");
        if (TextUtils.equals(fileName, asString)) {
            return;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "updateFilePathForSync");
        String localFile = dBImage.getLocalFile();
        if (TextUtils.isEmpty(localFile)) {
            localFile = dBImage.getThumbnailFile();
            str = "thumbnailFile";
        } else {
            str = "localFile";
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(localFile, permission, appendInvokerTag);
        if (documentFile == null || !documentFile.exists()) {
            return;
        }
        String downloadFileNameNotSecret = DownloadPathHelper.getDownloadFileNameNotSecret(dBImage, asString);
        String concat = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(localFile), downloadFileNameNotSecret);
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(concat, permission, appendInvokerTag);
        if (documentFile2 != null && documentFile2.exists()) {
            concat = BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(localFile), String.format("%s_%s.%s", BaseFileUtils.getFileTitle(downloadFileNameNotSecret), Long.valueOf(System.currentTimeMillis()), BaseFileUtils.getExtension(downloadFileNameNotSecret)));
        }
        LinkedList linkedList = new LinkedList();
        IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(localFile, IStoragePermissionStrategy.Permission.DELETE);
        if (!checkPermission.granted) {
            linkedList.add(checkPermission);
        }
        IStoragePermissionStrategy.PermissionResult checkPermission2 = StorageSolutionProvider.get().checkPermission(concat, IStoragePermissionStrategy.Permission.INSERT);
        if (!checkPermission2.granted) {
            linkedList.add(checkPermission2);
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(linkedList);
        }
        if (!StorageSolutionProvider.get().moveFile(localFile, concat, appendInvokerTag)) {
            return;
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(str, concat);
        GalleryUtils.safeUpdate(uri, contentValues2, "_id=?", new String[]{String.valueOf(dBImage.getId())});
    }

    public static void updateToPeopleFaceDBForSync(Uri uri, ContentValues contentValues, String str) throws JSONException {
        updateToLocalDBByServerId(uri, contentValues, str);
        contentValues.put("localFlag", (Integer) 0);
        GalleryUtils.safeUpdate(uri, contentValues, "serverId = ? AND localFlag != ? ", new String[]{str, String.valueOf(2)});
    }

    public static void updateToPeopleFaceDBForDeleteItem(Uri uri, ContentValues contentValues, String str) throws JSONException {
        contentValues.put("localFlag", (Integer) 0);
        updateToLocalDBByServerId(uri, contentValues, str);
    }

    public static int canUpload(String str) {
        if (TextUtils.isEmpty(str)) {
            return 6;
        }
        return canUpload(str, true);
    }

    public static int canUpload(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return 6;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CloudUtils", "canUpload"));
        if (documentFile == null || !documentFile.exists() || documentFile.isDirectory()) {
            return 6;
        }
        int serverType = getServerType(str);
        if (serverType == -1 || serverType == 0) {
            return 5;
        }
        long sGetFilterMinSize = Preference.sGetFilterMinSize();
        long length = documentFile.length();
        if (length <= sGetFilterMinSize) {
            return 10;
        }
        if (z && serverType == 1 && length > getFileSizeLimitStrategy().getImageMaxSize()) {
            return 4;
        }
        return (!z || serverType != 2 || length <= getFileSizeLimitStrategy().getVideoMaxSize()) ? 0 : 4;
    }

    public static long getMinFileSizeLimit(String str) {
        return sMinFileSizeLimit.get(str).longValue();
    }

    public static long getMaxImageSizeLimit() {
        return getFileSizeLimitStrategy().getImageMaxSize();
    }

    public static long getMaxVideoSizeLimit() {
        return getFileSizeLimitStrategy().getVideoMaxSize();
    }

    public static GalleryExtendedAuthToken getExtToken(Context context, Account account) {
        if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
            DefaultLogger.e("CloudUtils", "get extToken failed : cta not allowed");
            return null;
        }
        if (account == null) {
            account = AccountHelper.getXiaomiAccount(context);
        }
        Account account2 = account;
        if (account2 == null) {
            DefaultLogger.e("CloudUtils", "get extToken failed : account is null");
            return null;
        }
        try {
            return GalleryExtendedAuthToken.parse(AccountManager.get(context).getAuthToken(account2, "micgallery", (Bundle) null, true, (AccountManagerCallback<Bundle>) null, (Handler) null).getResult().getString("authtoken"));
        } catch (Exception e) {
            DefaultLogger.e("CloudUtils", "get extToken error", e);
            return null;
        }
    }

    public static void deleteShareAlbumInLocal(String str, String str2) {
        Uri uri = GalleryCloudUtils.SHARE_ALBUM_URI;
        int safeDelete = GalleryUtils.safeDelete(uri, "albumId like '" + str + "' ", null);
        DefaultLogger.d("CloudUtils", "deleted " + safeDelete + " share albums");
        Uri uri2 = GalleryContract.ShareImage.SHARE_URI;
        GalleryUtils.safeQuery(uri2, new String[]{"localFile", "thumbnailFile", "microthumbfile"}, "(groupId = ? OR localGroupId = ?) AND serverStatus = ?", new String[]{str, str2, "custom"}, (String) null, new GalleryUtils.QueryHandler<Void>() { // from class: com.miui.gallery.cloud.CloudUtils.5
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: avoid collision after fix types in other method */
            public Void mo1712handle(Cursor cursor) {
                if (cursor == null) {
                    return null;
                }
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "deleteShareAlbumInLocal");
                while (cursor.moveToNext()) {
                    String string = cursor.getString(0);
                    String string2 = cursor.getString(1);
                    String string3 = cursor.getString(2);
                    StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                    IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.DELETE;
                    DocumentFile documentFile = storageStrategyManager.getDocumentFile(string, permission, appendInvokerTag);
                    if (documentFile != null) {
                        documentFile.delete();
                    }
                    DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(string2, permission, appendInvokerTag);
                    if (documentFile2 != null) {
                        documentFile2.delete();
                    }
                    DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(string3, permission, appendInvokerTag);
                    if (documentFile3 != null) {
                        documentFile3.delete();
                    }
                }
                DefaultLogger.d("CloudUtils", "Deleted 0 share image files of share album ");
                return null;
            }
        });
        int safeDelete2 = GalleryUtils.safeDelete(uri2, "albumId = ? OR localGroupId = ? ", new String[]{str, str2});
        DefaultLogger.d("CloudUtils", "deleted " + safeDelete2 + " share images ");
        int safeDelete3 = GalleryUtils.safeDelete(GalleryContract.ShareUser.SHARE_USER_URI, "albumId = ? ", new String[]{str});
        DefaultLogger.d("CloudUtils", "deleted " + safeDelete3 + " share users");
    }

    public static String sqlNotEmptyStr(String str) {
        return String.format("%s!='' AND %s is not NULL", str, str);
    }

    public static long getItemId(Uri uri, String str, String str2) {
        ContentResolver contentResolver = GalleryApp.sGetAndroidContext().getContentResolver();
        Uri limitUri = getLimitUri(uri, 1);
        String[] strArr = {j.c};
        Cursor query = contentResolver.query(limitUri, strArr, str + "=?", new String[]{str2}, null);
        if (query != null) {
            try {
                if (!query.moveToFirst()) {
                    return 0L;
                }
                return query.getLong(0);
            } finally {
                query.close();
            }
        }
        return 0L;
    }

    public static boolean isFileNeedUpload(String str) {
        return isFileNeedUpload(GalleryCloudUtils.CLOUD_URI, str) || isFileNeedUpload(GalleryCloudUtils.SHARE_IMAGE_URI, str) || isFileNeedUpload(GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI, str) || isFileNeedUpload(GalleryCloudUtils.SHARE_SUB_UBIFOCUS_URI, str);
    }

    public static boolean isFileNeedUpload(Uri uri, String str) {
        Cursor cursor = null;
        try {
            cursor = GalleryApp.sGetAndroidContext().getContentResolver().query(getLimitUri(uri, 1), new String[]{j.c}, "localFile = ?  COLLATE NOCASE  AND ( localFlag = ? OR localFlag = ? ) ", new String[]{str, String.valueOf(8), String.valueOf(7)}, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    cursor.close();
                    return true;
                }
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void sendShareAlbumNotification(Context context, String str, int i, Uri uri) {
        sendNotification(context, str, i, uri, R.string.share_album, R.string.share_album, R.drawable.album_share_invitation_stat, "com.miui.gallery.action.VIEW_ALBUM", -1L, null, true);
    }

    public static void sendBabyAlbumNewPhotoNotification(Context context, String str, int i, Uri uri, long j, String str2, boolean z) {
        sendNotification(context, str, i, uri, R.string.baby_album, R.string.baby_album, R.drawable.album_share_invitation_stat, "com.miui.gallery.action.VIEW_ALBUM_NEW_PHOTO", j, str2, z);
    }

    public static void sendNotification(Context context, String str, int i, Uri uri, int i2, int i3, int i4, String str2, long j, String str3, boolean z) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setTicker(context.getString(i2));
        builder.setContentTitle(context.getString(i3));
        builder.setContentText(str);
        builder.setSmallIcon(i4);
        if (uri != null) {
            builder.setSound(uri);
            NotificationHelper.setDefaultChannel(context, builder);
        } else {
            NotificationHelper.setLowChannel(context, builder);
        }
        Intent intent = new Intent(str2);
        if (j != -1) {
            intent.putExtra("album_id", j);
            intent.putExtra("album_name", str3);
            intent.putExtra("other_share_album", z);
            intent.addFlags(67108864);
            intent.addFlags(2);
        }
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 67108864));
        Notification build = builder.build();
        build.flags = 16;
        ((NotificationManager) context.getSystemService("notification")).notify(i, build);
    }

    public static boolean checkAccount(Activity activity, boolean z, Runnable runnable) {
        boolean z2;
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
        Account accountFromDB = getAccountFromDB(sGetAndroidContext);
        if (accountFromDB == null || (xiaomiAccount != null && accountFromDB.name.equals(xiaomiAccount.name))) {
            z2 = true;
        } else {
            int deleteAccountStrategy = xiaomiAccount == null ? Preference.getDeleteAccountStrategy() : 2;
            DefaultLogger.w("CloudUtils", "account from DB not equals account from system, delete DB account, wipe data strategy is %d", Integer.valueOf(deleteAccountStrategy));
            if (z) {
                z2 = DeleteAccount.executeDeleteAccount(deleteAccountStrategy);
            } else {
                DeleteAccount.deleteAccountInTask(activity, accountFromDB, deleteAccountStrategy, null);
                z2 = false;
            }
        }
        if (xiaomiAccount != null && BaseGalleryPreferences.CTA.canConnectNetwork()) {
            if (z) {
                new CheckInternalAccountTask(runnable).run();
            } else {
                new CheckInternalAccountTask(runnable).execute(new Void[0]);
            }
        }
        return z2;
    }

    /* loaded from: classes.dex */
    public static class CheckInternalAccountTask extends AsyncTask<Void, Integer, Void> {
        public Runnable mRunOnPostExecute;

        @Override // android.os.AsyncTask
        public void onPreExecute() {
        }

        public CheckInternalAccountTask(Runnable runnable) {
            this.mRunOnPostExecute = runnable;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            run();
            return null;
        }

        public void run() {
            if (Preference.sIsInternationalAccount() == 2) {
                Preference.sSetIsInternationalAccount(com.xiaomi.micloudsdk.request.utils.CloudUtils.isInternationalAccount(true));
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r1) {
            Runnable runnable = this.mRunOnPostExecute;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public static boolean supportShare() {
        int sIsInternationalAccount = Preference.sIsInternationalAccount();
        if (sIsInternationalAccount == 1) {
            return false;
        }
        if (sIsInternationalAccount == 0) {
            return true;
        }
        return !Rom.IS_INTERNATIONAL || Settings.checkRegion("HK") || Settings.checkRegion("TW");
    }

    public static boolean isValidAlbumId(String str) {
        return !TextUtils.isEmpty(str) && !str.trim().equals("0");
    }

    public static int getDownloadFileStatusFromDB(DBImage dBImage) {
        return ((Integer) GalleryUtils.safeQuery(dBImage.getBaseUri(), new String[]{"downloadFileStatus"}, "_id=?", new String[]{dBImage.getId()}, (String) null, new GalleryUtils.QueryHandler<Integer>() { // from class: com.miui.gallery.cloud.CloudUtils.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle */
            public Integer mo1712handle(Cursor cursor) {
                if (cursor != null && cursor.moveToNext()) {
                    return Integer.valueOf(UpDownloadManager.mapStatusToRequestItem(cursor.getInt(0)));
                }
                return -1;
            }
        })).intValue();
    }

    public static String getLocalGroupIdForSharerAlbum(String str) {
        return (String) GalleryUtils.safeQuery(getCloudShareAlbumLimit1Uri(), new String[]{j.c}, String.format(Locale.US, "%s=? AND %s='%s'", "albumId", "serverStatus", "custom"), new String[]{str}, (String) null, new GalleryUtils.QueryHandler<String>() { // from class: com.miui.gallery.cloud.CloudUtils.7
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1712handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToNext()) {
                    return null;
                }
                return String.valueOf(cursor.getInt(0));
            }
        });
    }

    public static String getGroupIdByPhotoLocalId(String str) {
        return getStringColumnValue(GalleryApp.sGetAndroidContext(), GalleryCloudUtils.CLOUD_URI, "groupId", j.c, str);
    }

    public static int updateLocalGroupIdInGroup(Uri uri, String str, String str2, String str3) {
        return updateLocalGroupIdInGroup(uri, str, str2, str3, false);
    }

    public static int updateLocalGroupIdInGroup(Uri uri, String str, String str2, String str3, boolean z) {
        String str4;
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str)) {
            return 0;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("localGroupId", str2);
        Locale locale = Locale.US;
        if (z) {
            str4 = "%s like '" + str + "'";
        } else {
            str4 = "%s=?";
        }
        return GalleryUtils.safeUpdate(uri, contentValues, String.format(locale, str4, str3), z ? null : new String[]{str});
    }

    public static boolean isActive(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /* loaded from: classes.dex */
    public static class SecretAlbumUtils {
        public static final Set<String> UNENCRYPTED_FILE_EXTENSIONS;

        static {
            HashSet hashSet = new HashSet();
            UNENCRYPTED_FILE_EXTENSIONS = hashSet;
            hashSet.add(".img");
            hashSet.add(".vid");
        }

        public static boolean isUnencryptedVideoByPath(String str) {
            return str != null && str.endsWith(".vid");
        }

        public static boolean isEncryptedVideoByPath(String str) {
            return str != null && str.endsWith(".sav");
        }

        public static boolean isUnencryptedImageByPath(String str) {
            return str != null && str.endsWith(".img");
        }

        public static boolean isEncryptedImageByPath(String str) {
            return str != null && str.endsWith(".sa");
        }

        public static String encodeFileName(String str, boolean z) {
            Account account;
            if (!TextUtils.isEmpty(str) && (account = AccountCache.getAccount()) != null && !TextUtils.isEmpty(account.name)) {
                if (str.startsWith("{-sec-}") && probeSecretFileName(str, account.name) != null) {
                    return str;
                }
                CharSequence[] charSequenceArr = new CharSequence[3];
                charSequenceArr[0] = "{-sec-}";
                charSequenceArr[1] = Encode.encodeBase64(TextUtils.concat(account.name, "#", str).toString());
                charSequenceArr[2] = z ? ".vid" : ".img";
                return TextUtils.concat(charSequenceArr).toString();
            }
            return str;
        }

        public static String probeSecretFileName(String str, String str2) {
            String substring;
            if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str) && str.startsWith("{-sec-}") && str.length() > 7) {
                String str3 = "." + BaseFileUtils.getExtension(str);
                if (!TextUtils.isEmpty(str3) && UNENCRYPTED_FILE_EXTENSIONS.contains(str3)) {
                    int length = str.length() - str3.length();
                    if (length < 7) {
                        return null;
                    }
                    substring = str.substring(7, length);
                } else {
                    substring = str.substring(7);
                }
                try {
                    String decodeBase64 = Encode.decodeBase64(substring);
                    String str4 = str2 + "#";
                    if (decodeBase64.startsWith(str4)) {
                        return decodeBase64.substring(str4.length());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        public static String getSha1ThumbnailSA(String str, String str2, boolean z) {
            return String.format(Locale.US, "%s.%s%s", str, str2, ".sa");
        }

        public static String getEncryptedFileName(String str, String str2, boolean z) {
            int lastIndexOf = str.lastIndexOf(".");
            Locale locale = Locale.US;
            Object[] objArr = new Object[3];
            if (lastIndexOf != -1 && lastIndexOf != 0) {
                str = str.substring(0, lastIndexOf);
            }
            objArr[0] = str;
            objArr[1] = str2;
            objArr[2] = z ? ".sav" : ".sa";
            return String.format(locale, "%s.%s%s", objArr);
        }

        public static String getMD5Key(byte[] bArr) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(bArr, 0, bArr.length);
                return Encode.byteArrayToHexString(messageDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                DefaultLogger.w("CloudUtils", e);
                return null;
            }
        }

        public static boolean isEmpty(byte[] bArr) {
            return bArr == null || bArr.length <= 0;
        }

        public static String decryptFile(String str, String str2, boolean z, byte[] bArr, boolean z2) {
            String appendInvokerTag;
            DocumentFile documentFile;
            DocumentFile documentFile2;
            DocumentFile documentFile3;
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && (documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, (appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "decryptFile")))) != null && documentFile.isFile()) {
                if (SecretAlbumCryptoUtils.decrypt(str, str2, z, bArr)) {
                    if (z2 && (documentFile3 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                        documentFile3.delete();
                    }
                    return str2;
                } else if (z2 && (documentFile2 = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                    documentFile2.delete();
                }
            }
            return null;
        }

        public static String decryptFile(String str, String str2, boolean z, long j, boolean z2) {
            if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
                return null;
            }
            SecretInfo secretInfo = new SecretInfo();
            secretInfo.mSecretId = j;
            secretInfo.mSecretPath = str;
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "decryptFile");
            SecretInfo secretInfo2 = com.miui.gallery.provider.CloudUtils.getSecretInfo(GalleryApp.sGetAndroidContext(), secretInfo.mSecretId, secretInfo);
            if (TextUtils.isEmpty(secretInfo2.mSecretPath)) {
                return null;
            }
            byte[] bArr = secretInfo2.mSecretKey;
            if (bArr != null) {
                return decryptFile(secretInfo2.mSecretPath, str2, z, bArr, z2);
            }
            if (z2) {
                StorageSolutionProvider.get().moveFile(secretInfo2.mSecretPath, str2, appendInvokerTag);
            } else {
                StorageSolutionProvider.get().copyFile(secretInfo2.mSecretPath, str2, appendInvokerTag);
            }
            return str2;
        }

        public static String encryptFile(String str, String str2, boolean z, byte[] bArr) {
            int lastIndexOf;
            if (!TextUtils.isEmpty(str) && (lastIndexOf = str.lastIndexOf(h.g)) != -1) {
                String str3 = str.substring(0, lastIndexOf + 1) + str2;
                String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "encryptFile");
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile != null && documentFile.isFile()) {
                    if (SecretAlbumCryptoUtils.encrypt(str, str3, z, bArr)) {
                        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                        if (documentFile2 != null) {
                            documentFile2.delete();
                        }
                        return str3;
                    }
                    DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(str3, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile3 != null) {
                        documentFile3.delete();
                    }
                }
                return null;
            }
            return null;
        }

        public static void encryptFiles(DBImage dBImage, ContentValues contentValues) {
            if (dBImage.getSecretKeyNoGenerate() != null) {
                return;
            }
            byte[] secretKey = dBImage.getSecretKey();
            contentValues.put("secretKey", secretKey);
            boolean isVideoType = dBImage.isVideoType();
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "encryptFiles");
            String microThumbnailFile = dBImage.getMicroThumbnailFile();
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
            DocumentFile documentFile = storageStrategyManager.getDocumentFile(microThumbnailFile, permission, appendInvokerTag);
            if (documentFile != null && documentFile.exists()) {
                contentValues.put("microthumbfile", encryptFile(microThumbnailFile, dBImage.getSha1ThumbnailSA(), false, secretKey));
            }
            String localFile = dBImage.getLocalFile();
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(localFile, permission, appendInvokerTag);
            if (documentFile2 != null && documentFile2.exists()) {
                contentValues.put("localFile", encryptFile(localFile, dBImage.getEncodedFileName(), isVideoType, secretKey));
            }
            String thumbnailFile = dBImage.getThumbnailFile();
            DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(thumbnailFile, permission, appendInvokerTag);
            if (documentFile3 == null || !documentFile3.exists()) {
                return;
            }
            contentValues.put("thumbnailFile", encryptFile(thumbnailFile, dBImage.getSha1ThumbnailSA(), false, secretKey));
        }

        public static boolean isEncryptedFile(String str, boolean z) {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            return str.endsWith(z ? ".sav" : ".sa");
        }
    }

    public static long getLongAttributeFromJson(JSONObject jSONObject, String str) throws JSONException {
        return Long.parseLong(jSONObject.getString(str));
    }

    public static Boolean getIsFavoriteFromDescription(String str) {
        return Album.ExtraInfo.DescriptionBean.newInstance(str).isFavorite();
    }

    public static boolean deleteItemInHiddenAlbum(long j) {
        return GalleryUtils.safeDelete(GalleryCloudUtils.CLOUD_URI, String.format(Locale.US, "%s=? AND %s", j.c, CloudTableUtils.sGetWhereClauseAll(String.valueOf(CloudTableUtils.getCloudIdForGroupWithoutRecord(1001L)), String.valueOf(1001L), 0)), null) == 1;
    }

    public static void renameItemIfNeeded(DBImage dBImage, ContentValues contentValues) throws StoragePermissionMissingException {
        if (!contentValues.containsKey("fileName")) {
            return;
        }
        String asString = contentValues.getAsString("fileName");
        String fileName = dBImage.getFileName();
        if (TextUtils.equals(asString, fileName)) {
            return;
        }
        renameAnItemInLocal(dBImage, asString, fileName);
    }

    public static void renameAnItemInLocal(DBImage dBImage) throws StoragePermissionMissingException {
        String fileName = dBImage.getFileName();
        renameAnItemInLocal(dBImage, DownloadPathHelper.addPostfixToFileName(fileName, String.valueOf(System.currentTimeMillis())), fileName);
    }

    public static void renameAnItemInLocal(DBImage dBImage, String str, String str2) throws StoragePermissionMissingException {
        if (CloudTableUtils.isSecretAlbum(String.valueOf(dBImage.getGroupId()), dBImage.getLocalGroupId())) {
            DefaultLogger.i("CloudUtils", "item in secret album needn't be renamed.");
            return;
        }
        String downloadFileNameNotSecret = dBImage.isShareItem() ? DownloadPathHelper.getDownloadFileNameNotSecret(dBImage, str) : str;
        String downloadFolderRelativePath = DownloadPathHelper.getDownloadFolderRelativePath(dBImage);
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage(downloadFolderRelativePath);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "renameAnItemInLocal");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String localFile = dBImage.getLocalFile();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(localFile, permission, appendInvokerTag);
        String str3 = null;
        String moveImage = (documentFile == null || !documentFile.exists()) ? null : moveImage(dBImage.getLocalFile(), new File(pathInPriorStorage, downloadFileNameNotSecret).getAbsolutePath());
        DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(dBImage.getThumbnailFile(), permission, appendInvokerTag);
        if (documentFile2 != null && documentFile2.exists()) {
            str3 = moveImage(dBImage.getThumbnailFile(), new File(pathInPriorStorage, downloadFileNameNotSecret).getAbsolutePath());
        } else if (!TextUtils.isEmpty(dBImage.getThumbnailFile())) {
            str3 = moveImage;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("fileName", str);
        contentValues.put("title", BaseFileUtils.getFileTitle(str));
        contentValues.put("localFile", moveImage);
        contentValues.put("thumbnailFile", str3);
        GalleryUtils.safeUpdate(dBImage.getBaseUri(), contentValues, String.format(Locale.US, "%s=?", j.c), new String[]{String.valueOf(dBImage.getId())});
        DefaultLogger.i("CloudUtils", "item renamed, folderRelativePath: %s, oldFileName: %s, newFileName: %s", downloadFolderRelativePath, str2, str);
    }

    public static String renameForPhotoConflict(String str) {
        String sb;
        if (!TextUtils.isEmpty(str)) {
            long currentTimeMillis = System.currentTimeMillis();
            StringBuilder sb2 = new StringBuilder();
            String fileName = BaseFileUtils.getFileName(str);
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "renameForPhotoConflict");
            if (LocalUbifocus.isUbifocusImage(str)) {
                int ubifocusPatternIndex = LocalUbifocus.getUbifocusPatternIndex(fileName);
                sb2.append(BaseFileUtils.getParentFolderPath(str));
                sb2.append(File.separator);
                sb2.append(fileName.substring(0, ubifocusPatternIndex));
                sb2.append("_");
                sb2.append(currentTimeMillis);
                sb2.append(fileName.substring(ubifocusPatternIndex));
                sb = sb2.toString();
                List<LocalUbifocus.SubFile> ubifocusSubFiles = LocalUbifocus.getUbifocusSubFiles(str);
                if (ubifocusSubFiles != null) {
                    int size = ubifocusSubFiles.size();
                    for (int i = 0; i < size; i++) {
                        sb2.setLength(0);
                        String filePath = ubifocusSubFiles.get(i).getFilePath();
                        String name = new File(filePath).getName();
                        int ubifocusPatternIndex2 = LocalUbifocus.getUbifocusPatternIndex(name);
                        if (ubifocusPatternIndex2 >= 0) {
                            sb2.append(BaseFileUtils.getParentFolderPath(filePath));
                            sb2.append(File.separator);
                            sb2.append(name.substring(0, ubifocusPatternIndex2));
                            sb2.append("_");
                            sb2.append(currentTimeMillis);
                            sb2.append(name.substring(ubifocusPatternIndex2));
                            StorageSolutionProvider.get().moveFile(filePath, sb2.toString(), appendInvokerTag);
                        }
                    }
                }
            } else {
                int indexOf = fileName.indexOf("_BURST");
                if (indexOf >= 0 || (indexOf = fileName.indexOf("_TIMEBURST")) >= 0) {
                    sb2.append(BaseFileUtils.getParentFolderPath(str));
                    sb2.append(File.separator);
                    sb2.append(fileName.substring(0, indexOf));
                    sb2.append("_");
                    sb2.append(currentTimeMillis);
                    sb2.append(fileName.substring(indexOf));
                    return sb2.toString();
                } else if (fileName.endsWith("_STEREO.jpg")) {
                    int indexOf2 = fileName.indexOf("_STEREO.jpg");
                    sb2.append(BaseFileUtils.getParentFolderPath(str));
                    sb2.append(File.separator);
                    sb2.append(fileName.substring(0, indexOf2));
                    sb2.append("_");
                    sb2.append(currentTimeMillis);
                    sb2.append(fileName.substring(indexOf2));
                    return sb2.toString();
                } else {
                    int indexOf3 = fileName.indexOf(".");
                    if (indexOf3 >= 0) {
                        sb2.append(BaseFileUtils.getParentFolderPath(str));
                        sb2.append(File.separator);
                        sb2.append(fileName.substring(0, indexOf3));
                        sb2.append("_");
                        sb2.append(currentTimeMillis);
                        sb2.append(fileName.substring(indexOf3));
                        sb = sb2.toString();
                        StorageSolutionProvider.get().moveFile(str, sb, appendInvokerTag);
                        sb2.setLength(0);
                        File imageRelativeDngFile = FileUtils.getImageRelativeDngFile(str);
                        if (imageRelativeDngFile != null) {
                            sb2.append(imageRelativeDngFile.getAbsolutePath().substring(0, indexOf3));
                            sb2.append("_");
                            sb2.append(currentTimeMillis);
                            sb2.append(".dng");
                            StorageSolutionProvider.get().moveFile(imageRelativeDngFile.getAbsolutePath(), sb2.toString(), appendInvokerTag);
                        }
                    } else {
                        sb2.append(str);
                        sb2.append("_");
                        sb2.append(currentTimeMillis);
                        String sb3 = sb2.toString();
                        StorageSolutionProvider.get().moveFile(str, sb3, appendInvokerTag);
                        return sb3;
                    }
                }
            }
            return sb;
        }
        return str;
    }

    public static String[] getProjectionAll() {
        return new String[]{" * "};
    }

    public static boolean isNeedMoveToOtherAlbum(String str, long j) {
        WhiteAlbumsStrategy whiteAlbumsStrategy;
        return !Album.isUserCreateAlbum(str) && (whiteAlbumsStrategy = CloudControlStrategyHelper.getWhiteAlbumsStrategy()) != null && !whiteAlbumsStrategy.isWhiteAlbum(str) && (16 & j) == 0 && (64 & j) == 0 && (j & 2048) == 0;
    }

    public static boolean isRubbishAlbum(long j) {
        return Album.isRubbishAlbum(j);
    }

    public static boolean isUserCreative(String str) {
        return Album.isUserCreative(str);
    }

    public static String calculateSortPositionByBabyAlbum() {
        return AlbumSortHelper.calculateSortPositionByBabyAlbum();
    }

    public static String calculateSortPositionByUserCreativeAlbum() {
        return AlbumSortHelper.calculateSortPositionByUserCreativeAlbum();
    }

    public static String calculateSortPositionByNormalAlbum(Long l) {
        return AlbumSortHelper.calculateSortPositionByNormalAlbum(l.longValue());
    }

    public static boolean isSystemAlbum(long j) {
        return Album.isSystemAlbum(String.valueOf(j));
    }

    public static void markAndDeleteFile(DBImage dBImage, ContentValues contentValues) throws StoragePermissionMissingException, JSONException {
        String[] strArr = new String[4];
        strArr[0] = dBImage.getLocalFile();
        strArr[1] = dBImage.getThumbnailFile();
        String str = null;
        strArr[2] = contentValues == null ? null : contentValues.getAsString("localFile");
        if (contentValues != null) {
            str = contentValues.getAsString("thumbnailFile");
        }
        strArr[3] = str;
        List<String> list = (List) Lists.newArrayList(strArr).stream().filter(CloudUtils$$ExternalSyntheticLambda0.INSTANCE).collect(Collectors.toList());
        List<IStoragePermissionStrategy.PermissionResult> checkPermission = StorageSolutionProvider.get().checkPermission(list, IStoragePermissionStrategy.Permission.DELETE);
        LinkedList linkedList = new LinkedList();
        for (IStoragePermissionStrategy.PermissionResult permissionResult : checkPermission) {
            if (!permissionResult.granted) {
                linkedList.add(permissionResult);
            }
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            throw new StoragePermissionMissingException(Lists.newArrayList(linkedList));
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("CloudUtils", "markAndDeleteFile");
        for (String str2 : list) {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
            if (documentFile != null && documentFile.exists()) {
                documentFile.delete();
            }
        }
        dBImage.setLocalFile("");
        dBImage.setThumbnailFile("");
        if (contentValues != null) {
            contentValues.put("localFile", "");
            contentValues.put("thumbnailFile", "");
        }
        updateToLocalDBForSync(dBImage.getBaseUri(), contentValues, dBImage);
    }

    public static /* synthetic */ boolean lambda$markAndDeleteFile$0(String str) {
        return !TextUtils.isEmpty(str);
    }
}
