package com.miui.gallery.cloud.operation.editextra;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.AlbumSyncHelper;
import com.miui.gallery.cloud.CloudGroupUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.ThumbnailInfo;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class EditAlbumThumbnailInfo extends EditAlbumBase {
    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase
    public int getColumnIndex() {
        return 23;
    }

    public EditAlbumThumbnailInfo(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase
    public RequestOperationBase.Request buildRequest(Account account, RequestAlbumItem requestAlbumItem) throws Exception {
        DBImage item;
        DBAlbum albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, requestAlbumItem.dbAlbum.getServerId());
        String editGroupUrl = CloudGroupUrlProvider.getUrlProvider(false).getEditGroupUrl(account.name, albumByServerID.getServerId());
        ThumbnailInfo thumbnailInfo = albumByServerID.getThumbnailInfo();
        JSONObject jSONObject = albumByServerID.toJSONObject();
        if (thumbnailInfo != null) {
            long bgImageLocalId = thumbnailInfo.getBgImageLocalId();
            String faceId = thumbnailInfo.getFaceId();
            if (bgImageLocalId != -1 && (item = CloudUtils.getItem(GalleryCloudUtils.CLOUD_URI, this.mContext, j.c, String.valueOf(bgImageLocalId))) != null) {
                if (item.getLocalFlag() == 0) {
                    String serverId = item.getServerId();
                    if (!TextUtils.isEmpty(serverId)) {
                        jSONObject.put("backgroundImageId", serverId);
                    }
                } else {
                    SyncLogger.w(getTag(), "bg image not synced!");
                    return null;
                }
            }
            if (!TextUtils.isEmpty(faceId)) {
                jSONObject.put("faceId", faceId);
            }
        }
        return new RequestOperationBase.Request.Builder().setUrl(editGroupUrl).setMethod(2).setPostData(new JSONObject().put(MiStat.Param.CONTENT, jSONObject)).setRetryTimes(requestAlbumItem.otherRetryTimes).setNeedReRequest(false).build();
    }

    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase, com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        DBAlbum dBAlbum = ((RequestAlbumItem) requestItemBase).dbAlbum;
        updateDb(AlbumSyncHelper.getContentValuesForResponse(jSONObject), AlbumDataHelper.getAlbumByServerID(this.mContext, dBAlbum.getServerId()));
        SyncLogger.d("EditCloudThumbnailInfo", "edit success %s", dBAlbum.getName());
    }

    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase, com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
        }
    }

    @Override // com.miui.gallery.cloud.operation.editextra.EditAlbumBase
    public void updateDb(ContentValues contentValues, DBAlbum dBAlbum) throws JSONException {
        ThumbnailInfo thumbnailInfo = dBAlbum.getThumbnailInfo();
        if (thumbnailInfo != null) {
            long bgImageLocalId = thumbnailInfo.getBgImageLocalId();
            if (bgImageLocalId != -1) {
                CloudUtils.deleteItemInHiddenAlbum(bgImageLocalId);
            }
        }
        contentValues.putNull("thumbnailInfo");
        super.updateDb(contentValues, dBAlbum);
    }
}
