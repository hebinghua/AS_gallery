package com.miui.gallery.cloud.operation;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class EditCloudBase extends RequestOperationBase {
    public abstract RequestOperationBase.Request buildRequest(Account account, RequestCloudItem requestCloudItem) throws Exception;

    public abstract int getColumnIndex();

    public EditCloudBase(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestCloudItem)) {
            SyncLogger.e("EditCloudBase", "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        DBImage dBImage = ((RequestCloudItem) requestItemBase).dbCloud;
        if (dBImage.getLocalFlag() != 0 || TextUtils.isEmpty(dBImage.getRequestId())) {
            SyncLogger.w("EditCloudBase", "item has not been synced yet");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else if (dBImage.isShareItem()) {
            SyncLogger.e("EditCloudBase", "share item can't be edit");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else {
            return super.onPreRequest(requestItemBase);
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public final RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        return buildRequest(account, (RequestCloudItem) requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        DBImage dBImage = ((RequestCloudItem) requestItemBase).dbCloud;
        updateDb(CloudUtils.getContentValuesForAll(jSONObject), CloudUtils.getItemByServerID(this.mContext, dBImage.getServerId()));
        SyncLogger.d("EditCloudBase", "edit success %s", dBImage.getFileName());
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
        }
    }

    public void updateDb(ContentValues contentValues, DBImage dBImage) throws JSONException {
        contentValues.put("editedColumns", Utils.ensureNotNull(dBImage.getEditedColumns()).replace(GalleryCloudUtils.transformToEditedColumnsElement(getColumnIndex()), ""));
        CloudUtils.updateToLocalDB(GalleryCloudUtils.CLOUD_URI, contentValues, dBImage);
    }
}
