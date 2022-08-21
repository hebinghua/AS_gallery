package com.miui.gallery.cloud.operation;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.AlbumSyncHelper;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.Utils;
import com.xiaomi.stat.MiStat;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ReplaceAlbumCoverRequest extends RequestOperationBase {
    public ReplaceAlbumCoverRequest(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestAlbumItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        DBAlbum dBAlbum = requestAlbumItem.dbAlbum;
        if (dBAlbum == null) {
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        long coverId = dBAlbum.getCoverId();
        String valueOf = String.valueOf(dBAlbum.getCoverId());
        if (coverId != 0 && coverId < 2147483647L) {
            valueOf = CloudUtils.getServerIdByLocalId(this.mContext, String.valueOf(coverId), null);
            if (TextUtils.isEmpty(valueOf)) {
                return GallerySyncCode.NOT_RETRY_ERROR;
            }
        }
        requestAlbumItem.dbAlbum.setCoverServerId(valueOf);
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        String editAlbumUrl = HostManager.OwnerAlbum.getEditAlbumUrl(requestAlbumItem.dbAlbum.getServerId());
        JSONObject jSONObject = requestAlbumItem.dbAlbum.toJSONObject();
        jSONObject.put("coverImageId", requestAlbumItem.dbAlbum.getCoverId());
        return new RequestOperationBase.Request.Builder().setMethod(2).setUrl(editAlbumUrl).setPostData(new JSONObject().put(MiStat.Param.CONTENT, jSONObject)).setRetryTimes(requestAlbumItem.otherRetryTimes).setNeedReRequest(false).build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        DBAlbum albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, requestAlbumItem.dbAlbum.getServerId());
        int findEditedColumnsElementHitCount = GalleryCloudUtils.findEditedColumnsElementHitCount(3, requestAlbumItem.dbAlbum.getEditedColumns());
        int findEditedColumnsElementHitCount2 = GalleryCloudUtils.findEditedColumnsElementHitCount(3, albumByServerID.getEditedColumns());
        ContentValues contentValuesForResponse = AlbumSyncHelper.getContentValuesForResponse(jSONObject.getJSONObject(MiStat.Param.CONTENT));
        if (findEditedColumnsElementHitCount == findEditedColumnsElementHitCount2) {
            contentValuesForResponse.put("editedColumns", Utils.ensureNotNull(albumByServerID.getEditedColumns()).replaceAll(GalleryCloudUtils.transformToEditedColumnsElement(3), ""));
        } else {
            contentValuesForResponse.remove("coverId");
            SyncLogger.d(getTag(), "replace album cover success,This update is not the latest data, reqeust param editColumns is [%s]; newData param editColumns is [%s]", requestAlbumItem.dbAlbum.getEditedColumns(), albumByServerID.getEditedColumns());
        }
        AlbumDataHelper.updateAlbumAndSetLocalFlagToSynced(contentValuesForResponse, requestAlbumItem.dbAlbum.getId());
        SyncLogger.d(getTag(), "replace album cover success: %s", requestAlbumItem.dbAlbum.getName());
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
