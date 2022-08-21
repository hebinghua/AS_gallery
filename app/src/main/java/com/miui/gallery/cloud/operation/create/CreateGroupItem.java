package com.miui.gallery.cloud.operation.create;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.classic.net.SyslogAppender;
import ch.qos.logback.classic.spi.CallerData;
import com.miui.gallery.R;
import com.miui.gallery.cloud.AlbumShareOperations;
import com.miui.gallery.cloud.AlbumSyncHelper;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class CreateGroupItem extends RequestOperationBase {
    public static String[] invalidCharacters = {h.g, "\\", ":", "@", Marker.ANY_MARKER, CallerData.NA, "<", ">", "\r", "\n", SyslogAppender.DEFAULT_STACKTRACE_PATTERN, "-"};
    public static String[] invalidStartWiths = {".", "_"};

    public CreateGroupItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestAlbumItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        SyncLogger.d(getTag(), "create group start: %s", requestAlbumItem.dbAlbum.getName());
        if (requestAlbumItem.dbAlbum.isShareAlbum()) {
            SyncLogger.e(getTag(), "can't create share group.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        synchronized (GalleryCloudUtils.ALBUM_URI) {
            DBAlbum albumByColumnnameAndValue = AlbumDataHelper.getAlbumByColumnnameAndValue(this.mContext, j.c, requestAlbumItem.dbAlbum.getId());
            if (albumByColumnnameAndValue.getLocalFlag() == 0 && !TextUtils.isEmpty(albumByColumnnameAndValue.getServerId())) {
                SyncLogger.d(getTag(), "this group already exist in server, severId: %s", albumByColumnnameAndValue.getServerId());
                return GallerySyncCode.NOT_RETRY_ERROR;
            } else if (albumByColumnnameAndValue.getLocalFlag() == -1 && !TextUtils.isEmpty(albumByColumnnameAndValue.getServerId())) {
                SyncLogger.d(getTag(), "this group already exist in server, severId: %s", albumByColumnnameAndValue.getServerId());
                return GallerySyncCode.NOT_RETRY_ERROR;
            } else {
                return super.onPreRequest(requestItemBase);
            }
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        String createAlbumUrl = HostManager.OwnerAlbum.getCreateAlbumUrl();
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(createAlbumUrl).setPostData(new JSONObject().put(MiStat.Param.CONTENT, requestAlbumItem.dbAlbum.toJSONObject())).setRetryTimes(requestAlbumItem.createRetryTimes).setNeedReRequest(requestAlbumItem.needReRequest).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.createRetryTimes = requestItemBase.createRetryTimes + 1;
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        JSONObject optJSONObject = jSONObject.optJSONObject(MiStat.Param.CONTENT);
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response content null");
            return;
        }
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        String string = optJSONObject.getString("id");
        ContentValues contentValuesForResponse = AlbumSyncHelper.getContentValuesForResponse(optJSONObject);
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        synchronized (uri) {
            DBAlbum albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, string);
            if (albumByServerID != null && !albumByServerID.getId().equals(requestAlbumItem.dbAlbum.getId())) {
                String tag = getTag();
                SyncLogger.d(tag, "cloud:" + requestAlbumItem.dbAlbum.getId() + " has the same one :" + albumByServerID.getId() + " delete oldCloud, fileName:" + albumByServerID.getName());
                AlbumDataHelper.deleteDirty(albumByServerID);
            }
            AlbumSyncHelper.reviseAttributes(contentValuesForResponse, requestAlbumItem.dbAlbum, jSONObject);
            AlbumDataHelper.updateAlbumAndSetLocalFlagToSynced(uri, contentValuesForResponse, requestAlbumItem.dbAlbum.getId());
        }
        SyncLogger.d(getTag(), AlbumShareOperations.requestUrlForBarcode(string).toString());
        String tag2 = getTag();
        SyncLogger.d(tag2, "create group succeed and end:" + requestAlbumItem.dbAlbum.getName());
    }

    public static String localCreateBabyGroup(Context context, String str) {
        SyncLogger.d("LOCAL_CREATE_GROUP", "local create group start:" + str);
        synchronized (GalleryCloudUtils.ALBUM_URI) {
            DBAlbum albumByFileName = AlbumDataHelper.getAlbumByFileName(context, str, InternalContract$Album.SELECTION_SYNCED_OR_CREATE);
            if (albumByFileName != null) {
                return albumByFileName.getId();
            }
            return localCreateBabyGroup(context, null, null, str);
        }
    }

    public static String localCreateBabyGroup(Context context, String str, String str2, String str3) {
        String lastPathSegment;
        SyncLogger.d("LOCAL_CREATE_GROUP", "localCreateGroupBySpecialAppKey:" + str3);
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        synchronized (uri) {
            ContentValues contentValues = new ContentValues();
            if (!TextUtils.isEmpty(str)) {
                contentValues.put("appKey", str);
            }
            if (!TextUtils.isEmpty(str2)) {
                contentValues.put("serverId", str2);
            }
            contentValues.put("attributes", (Long) 1L);
            contentValues.put("name", str3);
            long currentTimeMillis = System.currentTimeMillis();
            contentValues.put("dateTaken", Long.valueOf(currentTimeMillis));
            contentValues.put("dateModified", Long.valueOf(currentTimeMillis));
            contentValues.put("localFlag", (Integer) 8);
            contentValues.put("localPath", DownloadPathHelper.getFolderRelativePathInCloud(str3));
            contentValues.put("sortInfo", AlbumSortHelper.calculateSortPositionByBabyAlbum());
            Uri safeInsert = GalleryUtils.safeInsert(uri, contentValues);
            SyncLogger.d("LOCAL_CREATE_GROUP", "insert a group in local DB:" + str3);
            SyncLogger.d("LOCAL_CREATE_GROUP", "local create group end:" + str3);
            lastPathSegment = safeInsert.getLastPathSegment();
        }
        return lastPathSegment;
    }

    public static void recreateGroup(String str) {
        recreateGroupBySpecialAppKey(null, str);
    }

    public static void recreateGroupBySpecialAppKey(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("serverId");
        contentValues.putNull("serverTag");
        contentValues.putNull("serverStatus");
        if (!TextUtils.isEmpty(str)) {
            contentValues.put("appKey", str);
        }
        contentValues.put("localFlag", (Integer) 8);
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        GalleryUtils.safeUpdate(uri, contentValues, "_id = '" + str2 + "'", null);
    }

    public static String checkFileNameValid(Context context, String str) {
        String[] strArr;
        String[] strArr2;
        if (TextUtils.isEmpty(str)) {
            return context.getText(R.string.cloudfolder_name_cannot_empty).toString();
        }
        int[] iArr = {R.string.cloud_camera_display_name, R.string.cloud_screenshots_display_name, R.string.secret_album_display_name, R.string.all_video_album_display_name, R.string.pet_album_display_name};
        for (int i = 0; i < 5; i++) {
            if (str.equalsIgnoreCase(context.getString(iArr[i]))) {
                return context.getText(R.string.cloudfolder_name_reserved).toString();
            }
        }
        if (CloudControlStrategyHelper.getServerReservedAlbumNamesStrategy().containsName(str)) {
            return context.getText(R.string.cloudfolder_name_reserved).toString();
        }
        for (String str2 : invalidCharacters) {
            if (str.contains(str2)) {
                return context.getResources().getString(R.string.cloudfolder_contain_invalidate_char, str2);
            }
        }
        for (String str3 : invalidStartWiths) {
            if (str.startsWith(str3)) {
                return context.getResources().getString(R.string.cloudfolder_cannot_start_with, str3);
            }
        }
        return null;
    }
}
