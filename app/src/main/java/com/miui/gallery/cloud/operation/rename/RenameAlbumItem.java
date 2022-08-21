package com.miui.gallery.cloud.operation.rename;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import com.miui.gallery.cloud.AlbumSyncHelper;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.xiaomi.stat.MiStat;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class RenameAlbumItem extends RequestOperationBase {
    public RenameAlbumItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestAlbumItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else if (((RequestAlbumItem) requestItemBase).dbAlbum.isShareAlbum()) {
            SyncLogger.e(getTag(), "can't rename share group.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        } else {
            return super.onPreRequest(requestItemBase);
        }
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        String renameAlbumUrl = HostManager.OwnerAlbum.getRenameAlbumUrl(requestAlbumItem.dbAlbum.getServerId());
        if (ApplicationHelper.isAutoUploadFeatureOpen()) {
            renameAlbumUrl = renameAlbumUrl + "/rename";
        }
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(renameAlbumUrl).setPostData(new JSONObject().put(MiStat.Param.CONTENT, requestAlbumItem.dbAlbum.toJSONObject())).setRetryTimes(requestAlbumItem.otherRetryTimes).setNeedReRequest(false).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        ContentValues contentValuesForResponse = AlbumSyncHelper.getContentValuesForResponse(jSONObject);
        AlbumSyncHelper.reviseAttributes(contentValuesForResponse, requestAlbumItem.dbAlbum, jSONObject);
        AlbumDataHelper.updateAlbumAndSetLocalFlagToSynced(contentValuesForResponse, requestAlbumItem.dbAlbum.getId());
        SyncLogger.d(getTag(), "rename group success: %s", requestAlbumItem.dbAlbum.getName());
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
        }
    }
}
