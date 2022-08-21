package com.miui.gallery.cloud.operation.editextra;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.AlbumSyncHelper;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class EditAlbumBase extends RequestOperationBase {
    public abstract RequestOperationBase.Request buildRequest(Account account, RequestAlbumItem requestAlbumItem) throws Exception;

    public abstract int getColumnIndex();

    public EditAlbumBase(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (!(requestItemBase instanceof RequestAlbumItem)) {
            SyncLogger.e("EditCloudBase", "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        DBAlbum dBAlbum = ((RequestAlbumItem) requestItemBase).dbAlbum;
        if (dBAlbum.getLocalFlag() != 0 || TextUtils.isEmpty(dBAlbum.getRequestId())) {
            SyncLogger.w("EditCloudBase", "item has not been synced yet");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public final RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        return buildRequest(account, (RequestAlbumItem) requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        DBAlbum dBAlbum = ((RequestAlbumItem) requestItemBase).dbAlbum;
        SyncLogger.d("EditCloudBase", "edit success data=%s", jSONObject);
        updateDb(AlbumSyncHelper.getContentValuesForResponse(jSONObject), AlbumDataHelper.getAlbumByServerID(this.mContext, dBAlbum.getServerId()));
        SyncLogger.d("EditCloudBase", "edit success %s", dBAlbum.getName());
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestError(GallerySyncCode gallerySyncCode, RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        if (gallerySyncCode != GallerySyncCode.OK) {
            String tag = getTag();
            SyncLogger.e(tag, "request error: " + gallerySyncCode);
            requestItemBase.otherRetryTimes = requestItemBase.otherRetryTimes + 1;
        }
    }

    public void updateDb(ContentValues contentValues, DBAlbum dBAlbum) throws JSONException {
        contentValues.put("editedColumns", Utils.ensureNotNull(dBAlbum.getEditedColumns()).replace(GalleryCloudUtils.transformToEditedColumnsElement(getColumnIndex()), ""));
        AlbumDataHelper.updateToLocalDBById(contentValues, dBAlbum.getId());
    }
}
