package com.miui.gallery.cloud.operation.copy;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class CopyImageBase extends RequestOperationBase {
    public String mAlbumId;

    public void appendValues(ContentValues contentValues) {
    }

    public abstract String getAlbumId(RequestCloudItem requestCloudItem);

    public abstract String getAlbumIdTagName();

    public abstract Uri getBaseUri();

    public abstract String getUrl(CloudUrlProvider cloudUrlProvider, String str, String str2, boolean z, RequestCloudItem requestCloudItem);

    public abstract void handleSubUbiSchema(JSONObject jSONObject, DBImage dBImage, ContentValues contentValues) throws Exception;

    public abstract boolean isToShare();

    public CopyImageBase(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestCloudItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        SyncLogger.d(getTag(), "copy image start: %s", requestCloudItem.dbCloud.getFileName());
        String albumId = getAlbumId(requestCloudItem);
        this.mAlbumId = albumId;
        if (TextUtils.isEmpty(albumId)) {
            DBImage dBImage = requestCloudItem.dbCloud;
            SyncLogger.e(getTag(), "copy image but albumId can't find id[%s], localGroupId[%s]", dBImage.getId(), dBImage.getLocalGroupId());
            DBAlbum albumById = AlbumDataHelper.getAlbumById(this.mContext, dBImage.getLocalGroupId(), null);
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            sb.append(GalleryCloudUtils.getAccountName());
            if (albumById != null) {
                sb.append("_");
                sb.append(albumById.getName());
                sb.append("_");
                sb.append(albumById.getLocalFlag());
            }
            hashMap.put("album", sb.toString());
            hashMap.put(Action.FILE_ATTRIBUTE, dBImage.getLocalFile());
            hashMap.put("local_flag", String.valueOf(dBImage.getLocalFlag()));
            SamplingStatHelper.recordCountEvent("Sync", "album_not_find_when_sync_media", hashMap);
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        requestCloudItem.dbCloud.setRequestAlbumId(this.mAlbumId);
        if (getSpaceFullListener().isSpaceFull(requestCloudItem)) {
            SyncLogger.e(getTag(), "is space full");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        boolean z;
        String str;
        String shareIdByLocalId;
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        boolean z2 = false;
        if (requestCloudItem.dbCloud.getLocalFlag() == 6) {
            shareIdByLocalId = CloudUtils.getServerIdByLocalId(this.mContext, requestCloudItem.dbCloud.getLocalImageId(), requestCloudItem);
        } else if (requestCloudItem.dbCloud.getLocalFlag() == 9) {
            z2 = true;
            shareIdByLocalId = CloudUtils.getShareIdByLocalId(this.mContext, requestCloudItem.dbCloud.getLocalImageId());
        } else {
            z = false;
            str = null;
            String url = getUrl(CloudUrlProvider.getUrlProvider(z, requestCloudItem.dbCloud.isVideoType()), account.name, str, z, requestCloudItem);
            String valueOf = String.valueOf(requestCloudItem.dbCloud.getServerTag());
            if (!TextUtils.isEmpty(str) || TextUtils.isEmpty(valueOf)) {
                return null;
            }
            JSONObject put = new JSONObject().put(MiStat.Param.CONTENT, requestCloudItem.dbCloud.toJSONObject());
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(nexExportFormat.TAG_FORMAT_TAG, String.valueOf(requestCloudItem.dbCloud.getServerTag())));
            arrayList.add(new BasicNameValuePair(getAlbumIdTagName(), this.mAlbumId));
            if (z) {
                arrayList.add(new BasicNameValuePair("sharedGalleryId", str));
            }
            return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(url).setParams(arrayList).setPostData(put).setRetryTimes(requestCloudItem.createRetryTimes).setNeedReRequest(requestCloudItem.needReRequest).build();
        }
        str = shareIdByLocalId;
        z = z2;
        String url2 = getUrl(CloudUrlProvider.getUrlProvider(z, requestCloudItem.dbCloud.isVideoType()), account.name, str, z, requestCloudItem);
        String valueOf2 = String.valueOf(requestCloudItem.dbCloud.getServerTag());
        if (!TextUtils.isEmpty(str)) {
        }
        return null;
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
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        JSONObject optJSONObject = jSONObject.optJSONObject(MiStat.Param.CONTENT);
        if (optJSONObject == null) {
            SyncLogger.e(getTag(), "response content null");
            return;
        }
        ContentValues contentValuesForAll = CloudUtils.getContentValuesForAll(optJSONObject, isToShare());
        appendValues(contentValuesForAll);
        if (CloudUtils.getItem(getBaseUri(), this.mContext, "serverId", contentValuesForAll.getAsString("serverId")) != null) {
            GalleryUtils.safeDelete(getBaseUri(), "_id = ?", new String[]{requestCloudItem.dbCloud.getId()});
            SyncLogger.e(getTag(), "item has already exist.");
            return;
        }
        try {
            try {
                CloudUtils.renameItemIfNeeded(requestCloudItem.dbCloud, contentValuesForAll);
            } catch (StoragePermissionMissingException unused) {
                SyncLogger.w("CopyImageBase", "skip file name conflict for [%s] since storage permission missing.", requestCloudItem.dbCloud.getFileName());
            }
        } catch (StoragePermissionMissingException unused2) {
            SyncLogger.w("CopyImageBase", "delete file and mask db for [%s] since storage permission missing.", requestCloudItem.dbCloud.getFileName());
            CloudUtils.markAndDeleteFile(requestCloudItem.dbCloud, contentValuesForAll);
        }
        CloudUtils.updateToLocalDBForSync(getBaseUri(), contentValuesForAll, requestCloudItem.dbCloud);
        handleSubUbiSchema(optJSONObject, requestCloudItem.dbCloud, contentValuesForAll);
        String tag = getTag();
        SyncLogger.d(tag, "Copy image succeed and end:" + requestCloudItem.dbCloud.getFileName());
    }
}
