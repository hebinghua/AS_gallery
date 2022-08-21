package com.miui.gallery.cloud.operation;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class PurgeRecoveryCloudItemBase extends RequestOperationBase {
    public abstract String getRequestUrl();

    public PurgeRecoveryCloudItemBase(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestCloudItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else if (((RequestCloudItem) requestItemBase).dbCloud.isShareItem()) {
            SyncLogger.e(getTag(), "item is share item");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else {
            return super.onPreRequest(requestItemBase);
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        RequestOperationBase.Request.Builder builder = new RequestOperationBase.Request.Builder();
        if (TextUtils.isEmpty(requestCloudItem.dbCloud.getRequestId())) {
            return null;
        }
        String requestUrl = getRequestUrl();
        ArrayList arrayList = new ArrayList();
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(new TrashUtils.RequestItemInfo(requestCloudItem.dbCloud.getServerId(), requestCloudItem.dbCloud.getServerTag()).toJSON());
        arrayList.add(new BasicNameValuePair(MiStat.Param.CONTENT, jSONArray.toString()));
        builder.setUrl(requestUrl).setMethod(2).setParams(arrayList).setRetryTimes(requestItemBase.otherRetryTimes).setNeedReRequest(false);
        return builder.build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        JSONArray optJSONArray;
        RequestCloudItem requestCloudItem = (RequestCloudItem) requestItemBase;
        SyncLogger.d(getTag(), "purge or recovery item success: %s, status: %s", requestCloudItem.dbCloud.getFileName(), requestCloudItem.dbCloud.getServerStatus());
        if (jSONObject != null) {
            if (jSONObject.has("data")) {
                jSONObject = jSONObject.optJSONObject("data");
            }
            if (jSONObject == null || (optJSONArray = jSONObject.optJSONArray("succList")) == null || optJSONArray.length() <= 0) {
                return;
            }
            updateItem(requestCloudItem.dbCloud.getBaseUri(), this.mContext, requestCloudItem.dbCloud, optJSONArray.getJSONObject(0));
        }
    }

    public final void updateItem(Uri uri, Context context, DBImage dBImage, JSONObject jSONObject) throws Exception {
        ContentValues contentValuesForUploadDeletePurged = CloudUtils.getContentValuesForUploadDeletePurged(jSONObject);
        contentValuesForUploadDeletePurged.putNull("thumbnailFile");
        contentValuesForUploadDeletePurged.putNull("microthumbfile");
        GalleryUtils.safeUpdate(uri, contentValuesForUploadDeletePurged, "_id = '" + dBImage.getId() + "'", null);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode == GallerySyncCode.OK || gallerySyncCode == GallerySyncCode.ALBUM_NOT_EXIST) {
            return;
        }
        String tag = getTag();
        SyncLogger.e(tag, "request error: " + gallerySyncCode);
        requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
    }
}
