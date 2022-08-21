package com.miui.gallery.cloud.operation.move;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class MoveItemBase extends RequestOperationBase {
    public String mAlbumId;

    public abstract void appendAlbumIdParameter(ArrayList<NameValuePair> arrayList) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;

    public abstract void appendValues(ContentValues contentValues);

    public abstract String getAlbumId(RequestCloudItem requestCloudItem);

    public abstract String getUrl(CloudUrlProvider cloudUrlProvider, String str, String str2, RequestCloudItem requestCloudItem);

    public abstract void handleSubUbiSchema(JSONObject jSONObject, String str) throws JSONException;

    public abstract boolean isToShare();

    public MoveItemBase(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestCloudItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return GallerySyncCode.OK;
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, JSONException {
        String serverId;
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        DBImage dBImage = requestCloudItem.dbCloud;
        if (!TextUtils.isEmpty(dBImage.getLocalImageId())) {
            serverId = CloudUtils.getServerIdByLocalId(this.mContext, dBImage.getLocalImageId(), requestCloudItem);
        } else {
            serverId = dBImage.getServerId();
        }
        String albumId = getAlbumId(requestCloudItem);
        this.mAlbumId = albumId;
        requestCloudItem.dbCloud.setRequestAlbumId(albumId);
        if (TextUtils.isEmpty(serverId) || TextUtils.isEmpty(this.mAlbumId)) {
            return null;
        }
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        appendAlbumIdParameter(arrayList);
        String url = getUrl(CloudUrlProvider.getUrlProvider(requestCloudItem.dbCloud.isShareItem(), requestCloudItem.dbCloud.isVideoType()), account.name, serverId, requestCloudItem);
        if (url == null) {
            return null;
        }
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(url).setParams(arrayList).setPostData(new JSONObject().put(MiStat.Param.CONTENT, dBImage.toJSONObject())).setRetryTimes(requestCloudItem.createRetryTimes).setNeedReRequest(requestCloudItem.needReRequest).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        ContentValues contentValues;
        DBImage dBImage = ((RequestCloudItem) requestItemBase).dbCloud;
        if (jSONObject.has(MiStat.Param.CONTENT)) {
            JSONObject jSONObject2 = jSONObject.getJSONObject(MiStat.Param.CONTENT);
            contentValues = CloudUtils.getContentValuesForAll(jSONObject2);
            appendValues(contentValues);
            CloudUtils.updateToLocalDBForSync(GalleryCloudUtils.CLOUD_URI, contentValues, dBImage);
            handleSubUbiSchema(jSONObject2, dBImage.getId());
        } else if (!jSONObject.has("purgeContent") || !jSONObject.has("copyContent")) {
            contentValues = null;
        } else {
            CloudUtils.updateToLocalDBForSync(GalleryCloudUtils.CLOUD_URI, CloudUtils.getContentValuesForAll(jSONObject.getJSONObject("purgeContent")), dBImage.getLocalImageId());
            JSONObject jSONObject3 = jSONObject.getJSONObject("copyContent");
            contentValues = CloudUtils.getContentValuesForAll(jSONObject3, isToShare());
            appendValues(contentValues);
            CloudUtils.updateToLocalDBForSync(dBImage.getBaseUri(), contentValues, dBImage);
            handleSubUbiSchema(jSONObject3, dBImage.getId());
        }
        try {
            try {
                CloudUtils.renameItemIfNeeded(dBImage, contentValues);
            } catch (StoragePermissionMissingException unused) {
                SyncLogger.w("MoveItemBase", "delete file and mask db for [%s] since storage permission missing.", dBImage.getFileName());
                CloudUtils.markAndDeleteFile(dBImage, contentValues);
            }
        } catch (StoragePermissionMissingException unused2) {
            SyncLogger.w("MoveItemBase", "skip file name conflict for [%s] since storage permission missing.", dBImage.getFileName());
        }
        if (dBImage.isSecretItem()) {
            ContentValues contentValues2 = new ContentValues();
            CloudUtils.SecretAlbumUtils.encryptFiles(dBImage, contentValues2);
            if (contentValues2.size() > 0) {
                CloudUtils.updateToLocalDB(dBImage.getBaseUri(), contentValues2, dBImage);
            }
        }
        SyncLogger.d(getTag(), "Move image %s success, group: %s", dBImage.getFileName(), Long.valueOf(dBImage.getGroupId()));
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.createRetryTimes = requestItemBase.createRetryTimes + 1;
        }
    }
}
