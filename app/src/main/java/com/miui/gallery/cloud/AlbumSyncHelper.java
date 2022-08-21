package com.miui.gallery.cloud;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.operation.delete.DeleteAlbumItem;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.ExtraTextUtils;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.NoMediaUtil;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AlbumSyncHelper {
    public static long mergeAttributes(long j, long j2, long j3, boolean z, boolean z2) {
        if (z2) {
            return (z ? j | j2 : j & (~j2)) | j3;
        }
        return (j3 & j) == 0 ? z ? j | j2 : j & (~j2) : j;
    }

    public static String handleNewGroup(Context context, Uri uri, String str, JSONObject jSONObject) throws JSONException {
        return handleNewGroup(context, uri, null, str, jSONObject);
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0180  */
    /* JADX WARN: Removed duplicated region for block: B:62:0x018f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String handleNewGroup(android.content.Context r18, android.net.Uri r19, android.content.ContentValues r20, java.lang.String r21, org.json.JSONObject r22) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 424
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.AlbumSyncHelper.handleNewGroup(android.content.Context, android.net.Uri, android.content.ContentValues, java.lang.String, org.json.JSONObject):java.lang.String");
    }

    public static DBAlbum getAlbumByColumnnameAndValue(Context context, String str, String str2, boolean z) {
        return AlbumDataHelper.getAlbumByColumnnameAndValue(context, str, str2, InternalContract$Album.SELECTION_SYNCED_OR_CREATE, z);
    }

    public static DBAlbum getAlbumByFileName(Context context, String str) {
        return AlbumDataHelper.getAlbumByFileName(context, str, InternalContract$Album.SELECTION_SYNCED_OR_CREATE);
    }

    public static DBAlbum getAlbumByAppKey(Context context, String str) {
        return AlbumDataHelper.getAlbumByAppKey(context, str, InternalContract$Album.SELECTION_SYNCED_OR_CREATE);
    }

    public static Boolean getShareToTvAttribute(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("shareDeviceType")) {
            JSONArray jSONArray = jSONObject.getJSONArray("shareDeviceType");
            for (int i = 0; i < jSONArray.length(); i++) {
                String string = jSONArray.getString(i);
                if (!TextUtils.isEmpty(string) && string.equals("TV")) {
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        }
        return null;
    }

    public static void reviseAttributes(ContentValues contentValues, DBAlbum dBAlbum, JSONObject jSONObject) throws JSONException {
        Album.ExtraInfo.DescriptionBean newInstance = Album.ExtraInfo.DescriptionBean.newInstance(contentValues.getAsString("description"));
        Boolean isRubbish = newInstance == null ? null : newInstance.isRubbish();
        Boolean isAutoUpload = newInstance == null ? null : newInstance.isAutoUpload();
        Boolean isHidden = newInstance == null ? null : newInstance.isHidden();
        Boolean isManualSetUpload = newInstance == null ? null : newInstance.isManualSetUpload();
        Boolean isShowInOtherAlbums = newInstance == null ? null : newInstance.isShowInOtherAlbums();
        Boolean isManualShowInOtherAlbums = newInstance == null ? null : newInstance.isManualShowInOtherAlbums();
        Boolean isManualHidden = newInstance == null ? null : newInstance.isManualHidden();
        String asString = contentValues.getAsString("localPath");
        long attributes = dBAlbum != null ? dBAlbum.getAttributes() : 0L;
        boolean isRubbishAlbum = Album.isRubbishAlbum(attributes);
        boolean z = (isAutoUpload != null && isAutoUpload.booleanValue()) || (isHidden != null && !isHidden.booleanValue());
        Boolean shareToTvAttribute = getShareToTvAttribute(jSONObject);
        if (isRubbish == null || !isRubbish.booleanValue() || !z) {
            if (isRubbish != null && isRubbish.booleanValue()) {
                mergeShareToTvAttributes(6274L, shareToTvAttribute);
                contentValues.put("attributes", (Long) 6274L);
                return;
            } else if (isRubbishAlbum && z) {
                isShowInOtherAlbums = Boolean.TRUE;
                if (isAutoUpload != null && isAutoUpload.booleanValue()) {
                    isAutoUpload = isShowInOtherAlbums;
                    isManualSetUpload = isAutoUpload;
                }
                attributes = attributes & (-2049) & (-4097);
                removeNoMediaIfNeed(dBAlbum);
                isManualShowInOtherAlbums = isShowInOtherAlbums;
            } else if (!TextUtils.isEmpty(asString) && MediaScannerHelper.isInRubbishList(asString) && isManualShowInOtherAlbums != null && !isManualShowInOtherAlbums.booleanValue() && isShowInOtherAlbums != null && isShowInOtherAlbums.booleanValue() && isManualHidden != null && !isManualHidden.booleanValue() && isHidden != null && isHidden.booleanValue()) {
                attributes = 2048;
            }
        } else if (isRubbishAlbum) {
            attributes = (attributes | 4096) & (-2049);
            removeNoMediaIfNeed(dBAlbum);
        }
        Boolean isShowInPhotosTab = newInstance == null ? null : newInstance.isShowInPhotosTab();
        Boolean isManualShowInPhotosTab = newInstance == null ? null : newInstance.isManualShowInPhotosTab();
        if (dBAlbum == null && ExtraTextUtils.startsWithIgnoreCase(asString, StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM) && isAutoUpload == null) {
            attributes |= 1;
        }
        long j = attributes;
        if (!TextUtils.isEmpty(contentValues.getAsString("babyInfoJson"))) {
            if (isAutoUpload == null || !isAutoUpload.booleanValue() || (j & 1) == 0) {
                DefaultLogger.w("AlbumSyncHelper", "correct attribute autoUpload to true for baby album");
            }
            j |= 1;
        } else if (isAutoUpload != null) {
            j = mergeAttributes(j, 1L, 2L, isAutoUpload.booleanValue(), isManualSetUpload != null ? isManualSetUpload.booleanValue() : false);
        }
        long j2 = j;
        if (isShowInOtherAlbums != null) {
            j2 = mergeAttributes(j2, 64L, 128L, isShowInOtherAlbums.booleanValue(), isManualShowInOtherAlbums != null ? isManualShowInOtherAlbums.booleanValue() : false);
        }
        long j3 = j2;
        if (isShowInPhotosTab != null) {
            j3 = mergeAttributes(j3, 4L, 8L, isShowInPhotosTab.booleanValue(), isManualShowInPhotosTab != null ? isManualShowInPhotosTab.booleanValue() : false);
        }
        long j4 = j3;
        if (isHidden != null) {
            j4 = mergeAttributes(j4, 16L, 32L, isHidden.booleanValue(), isManualHidden != null ? isManualHidden.booleanValue() : false);
        }
        long mergeShareToTvAttributes = mergeShareToTvAttributes(j4, shareToTvAttribute);
        if (CloudUtils.isNeedMoveToOtherAlbum(asString, mergeShareToTvAttributes)) {
            mergeShareToTvAttributes = mergeAttributes(mergeShareToTvAttributes, 64L, 128L, true, false);
            DefaultLogger.d("AlbumSyncHelper", "handleNewGroup album is non white and non rubbish,hidden,other,now set album attributes is OtherAlbum");
        }
        contentValues.put("attributes", Long.valueOf(mergeShareToTvAttributes));
        DefaultLogger.d("AlbumSyncHelper", "reviseAttributes album attributes:[%d],localFile:[%s]", Long.valueOf(mergeShareToTvAttributes), asString);
        if (((mergeShareToTvAttributes & 1) == 0) == (isAutoUpload == null || !isAutoUpload.booleanValue())) {
            if (((64 & mergeShareToTvAttributes) == 0) == (isShowInOtherAlbums == null || !isShowInOtherAlbums.booleanValue())) {
                if (((4 & mergeShareToTvAttributes) == 0) == (isShowInPhotosTab == null || !isShowInPhotosTab.booleanValue())) {
                    if (((mergeShareToTvAttributes & 16) == 0) == (isHidden == null || !isHidden.booleanValue())) {
                        return;
                    }
                }
            }
        }
        contentValues.put("editedColumns", GalleryCloudUtils.mergeEditedColumns(GalleryCloudUtils.mergeEditedColumns((dBAlbum == null || TextUtils.isEmpty(dBAlbum.getEditedColumns())) ? "" : dBAlbum.getEditedColumns(), contentValues.getAsString("editedColumns")), GalleryCloudUtils.transformToEditedColumnsElement(22)));
    }

    public static void fillSortPosition(ContentValues contentValues) {
        String calculateSortPositionByNormalAlbum;
        long longValue = contentValues.getAsLong("dateTaken").longValue();
        Object obj = contentValues.get("serverId");
        boolean z = (obj instanceof Long) && CloudUtils.isSystemAlbum(((Long) obj).longValue());
        String asString = contentValues.getAsString("localPath");
        if (!z && longValue < 2147483647L) {
            DefaultLogger.e("AlbumSyncHelper", "fillSortPosition error,dateTaken is invalide,values is %s", contentValues);
            Long asLong = contentValues.getAsLong("dateModified");
            longValue = (asLong == null || asLong.longValue() <= 2147483647L) ? System.currentTimeMillis() : asLong.longValue();
        }
        if (z) {
            calculateSortPositionByNormalAlbum = String.valueOf(longValue);
        } else if (contentValues.containsKey("babyInfoJson")) {
            calculateSortPositionByNormalAlbum = CloudUtils.calculateSortPositionByBabyAlbum();
        } else if (CloudUtils.isUserCreative(asString)) {
            calculateSortPositionByNormalAlbum = CloudUtils.calculateSortPositionByUserCreativeAlbum();
        } else {
            calculateSortPositionByNormalAlbum = CloudUtils.calculateSortPositionByNormalAlbum(Long.valueOf(longValue));
        }
        DefaultLogger.d("AlbumSyncHelper", "相册:[%s],更改sort_position:[%s]", asString, calculateSortPositionByNormalAlbum);
        contentValues.put("sortInfo", calculateSortPositionByNormalAlbum);
    }

    public static void removeNoMediaIfNeed(DBAlbum dBAlbum) {
        if (dBAlbum == null || !NoMediaUtil.isManualHideAlbum(dBAlbum.getLocalPath())) {
            return;
        }
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(dBAlbum.getLocalPath());
        MediaAndAlbumOperations.doRemoveNoMediaForRubbishAlbum(arrayList);
    }

    public static long mergeShareToTvAttributes(long j, Boolean bool) {
        if (bool != null) {
            if (bool.booleanValue()) {
                DefaultLogger.d("AlbumSyncHelper", "before update the attribute is= %s", Long.valueOf(j));
                DefaultLogger.d("AlbumSyncHelper", "cloud is not null and isShareToTv= true");
                j |= 1280;
            } else {
                j &= -1281;
            }
            DefaultLogger.d("AlbumSyncHelper", "the final attribute is= %s", Long.valueOf(j));
        }
        return j;
    }

    public static ContentValues getContentValuesForResponse(JSONObject jSONObject) throws JSONException {
        ContentValues contentValues = new ContentValues();
        if (jSONObject != null) {
            if (jSONObject.has(MiStat.Param.CONTENT)) {
                try {
                    jSONObject = jSONObject.getJSONObject(MiStat.Param.CONTENT);
                } catch (Exception unused) {
                    DefaultLogger.e("AlbumSyncHelper", "getContentValuesForResponse error,json:[%s]", jSONObject);
                }
            }
            if (jSONObject.has("albumId")) {
                contentValues.put("serverId", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "albumId")));
            } else if (jSONObject.has("id")) {
                contentValues.put("serverId", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "id")));
            }
            Long serverTagFromResponse = getServerTagFromResponse(jSONObject);
            if (serverTagFromResponse != null) {
                contentValues.put("serverTag", serverTagFromResponse);
            }
            if (jSONObject.has("status")) {
                String string = jSONObject.getString("status");
                if (string.equals("1")) {
                    contentValues.put("serverStatus", "custom");
                } else {
                    contentValues.put("serverStatus", string);
                }
            }
            if (jSONObject.has("name")) {
                contentValues.put("name", jSONObject.getString("name"));
            } else if (jSONObject.has("fileName")) {
                contentValues.put("name", jSONObject.getString("fileName"));
            }
            if (jSONObject.has("lastUpdateTime")) {
                contentValues.put("dateModified", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "lastUpdateTime")));
            } else if (jSONObject.has("dateModified")) {
                contentValues.put("dateModified", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "dateModified")));
            }
            if (jSONObject.has("createTime")) {
                contentValues.put("dateTaken", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "createTime")));
            } else if (jSONObject.has("dateTaken")) {
                contentValues.put("dateTaken", Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, "dateTaken")));
            }
            if (contentValues.get("serverId") != null && AlbumDataHelper.isSystemAlbum(String.valueOf(contentValues.getAsLong("serverId")))) {
                AlbumDataHelper.replaceFieldsForSystemAlbum(contentValues);
            }
            if (jSONObject.has("coverImageId")) {
                long longAttributeFromJson = CloudUtils.getLongAttributeFromJson(jSONObject, "coverImageId");
                if (longAttributeFromJson == 0) {
                    contentValues.putNull("coverId");
                } else {
                    contentValues.put("coverId", Long.valueOf(longAttributeFromJson));
                }
            }
            fillExtraInfoFromResponse(jSONObject, contentValues);
        }
        return contentValues;
    }

    public static ContentValues fillExtraInfoFromResponse(JSONObject jSONObject, ContentValues contentValues) {
        try {
            if (jSONObject.has("description")) {
                String string = jSONObject.getString("description");
                if (!TextUtils.isEmpty(string)) {
                    JSONObject jSONObject2 = new JSONObject(string);
                    if (TextUtils.isEmpty(jSONObject2.optString("migration_path"))) {
                        String optString = jSONObject2.optString("localFile");
                        if (StringUtils.containsIgnoreCase(optString, "MIUI/Gallery/cloud")) {
                            String replaceIgnoreCase = StringUtils.replaceIgnoreCase(optString, "MIUI/Gallery/cloud", StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM);
                            jSONObject.put("migration_path", replaceIgnoreCase);
                            try {
                                jSONObject2.put("migration_path", replaceIgnoreCase);
                                string = jSONObject2.toString();
                                jSONObject.putOpt("description", string);
                            } catch (Exception e) {
                                SyncLogger.w("AlbumSyncHelper", "error when fill migration, %s", e);
                            }
                            contentValues.put("editedColumns", GalleryCloudUtils.mergeEditedColumns(null, GalleryCloudUtils.transformToEditedColumnsElement(22)));
                            GalleryPreferences.Album.applyAlbumMigrationState(false, 1L);
                        }
                    }
                }
                if (!TextUtils.isEmpty(string)) {
                    try {
                        String optString2 = new JSONObject(string).optString("migration_path");
                        if (!TextUtils.isEmpty(optString2)) {
                            contentValues.put("localPath", optString2);
                        } else {
                            CloudUtils.parseDescription(contentValues, "localPath", string);
                        }
                    } catch (Exception e2) {
                        DefaultLogger.w("AlbumSyncHelper", e2);
                    }
                }
                contentValues.put("description", string);
            }
            parseBabyInfo(jSONObject, contentValues);
            return contentValues;
        } catch (JSONException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static void parseBabyInfo(JSONObject jSONObject, ContentValues contentValues) throws JSONException {
        if (ApplicationHelper.isBabyAlbumFeatureOpen()) {
            JSONObject jSONObject2 = null;
            if (jSONObject.has("renderInfo")) {
                jSONObject2 = jSONObject.getJSONObject("renderInfo");
            } else if (jSONObject.has("renderInfos")) {
                jSONObject2 = jSONObject.getJSONArray("renderInfos").getJSONObject(0);
            }
            if (jSONObject2 == null || !jSONObject2.getString(nexExportFormat.TAG_FORMAT_TYPE).equalsIgnoreCase("baby")) {
                return;
            }
            contentValues.put("babyInfoJson", jSONObject2.toString());
            if (!jSONObject2.has("peopleId")) {
                return;
            }
            contentValues.put("peopleId", jSONObject2.getString("peopleId"));
        }
    }

    public static void statSuspiciousAlbumPathModify(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("oldLocalFile", str);
        hashMap.put("newLocalFile", str2);
        SamplingStatHelper.recordCountEvent("Sync", "sync_suspicious_album_path_modify", hashMap);
    }

    public static void handleItemForCustom(JSONObject jSONObject, DBItem dBItem) throws JSONException {
        ContentValues contentValuesForResponse = getContentValuesForResponse(jSONObject);
        contentValuesForResponse.putNull("editedColumns");
        DBAlbum dBAlbum = (DBAlbum) dBItem;
        if (!Album.isCameraAlbum(dBAlbum.getServerId())) {
            reviseAttributes(contentValuesForResponse, dBAlbum, jSONObject);
        }
        String localPath = dBAlbum.getLocalPath();
        String asString = contentValuesForResponse.getAsString("localFile");
        if (!TextUtils.isEmpty(localPath) && !TextUtils.isEmpty(asString) && !localPath.equalsIgnoreCase(asString)) {
            if (asString.startsWith(".cloud/owner")) {
                contentValuesForResponse.remove("localFile");
            }
            statSuspiciousAlbumPathModify(localPath, asString);
        }
        CloudUtils.updateLocalGroupIdInGroup(GalleryCloudUtils.CLOUD_URI, contentValuesForResponse.getAsString("serverId"), dBAlbum.getId(), "groupId");
        if (contentValuesForResponse.containsKey("dateTaken") && dBAlbum.getDateTaken() != contentValuesForResponse.getAsLong("dateTaken").longValue()) {
            fillSortPosition(contentValuesForResponse);
        }
        AlbumDataHelper.updateAlbumAndSetLocalFlagToSynced(contentValuesForResponse, dBAlbum.getId());
    }

    public static void handleItemForDeleted(Uri uri, Context context, JSONObject jSONObject, DBItem dBItem) throws JSONException {
        DeleteAlbumItem.updateForDeleteOrPurgedOnLocal(uri, context, (DBAlbum) dBItem, jSONObject);
    }

    public static void handleItemForPurged(Uri uri, Context context, JSONObject jSONObject, DBItem dBItem) throws JSONException {
        DeleteAlbumItem.updateForDeleteOrPurgedOnLocal(uri, context, (DBAlbum) dBItem, jSONObject);
    }

    public static Long getServerTagFromResponse(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has(nexExportFormat.TAG_FORMAT_TAG)) {
            return Long.valueOf(CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG));
        }
        return null;
    }
}
