package com.miui.gallery.cloud.operation.delete;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.RequestOperationBase;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DeleteAlbumItem extends RequestOperationBase {
    public DeleteAlbumItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        DefaultLogger.d("galleryAction_SyncDeleteAlbum", "onPreRequest =>");
        if (!(requestItemBase instanceof RequestAlbumItem)) {
            SyncLogger.e(getTag(), "item is not instanceof RequestCloudItem.");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        if (tryDeleteDirtyItem(requestAlbumItem.dbAlbum)) {
            SyncLogger.e(getTag(), "serverId is null means item is deleted by user, delete this dirty record: %s", requestAlbumItem.dbAlbum.getName());
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public RequestOperationBase.Request buildRequest(Account account, RequestItemBase requestItemBase) throws Exception {
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        RequestOperationBase.Request.Builder builder = new RequestOperationBase.Request.Builder();
        String serverId = requestAlbumItem.dbAlbum.getServerId();
        if (TextUtils.isEmpty(serverId)) {
            return null;
        }
        String deleteAlbumUrl = HostManager.OwnerAlbum.getDeleteAlbumUrl(serverId);
        String valueOf = String.valueOf(requestAlbumItem.dbAlbum.getServerTag());
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair(nexExportFormat.TAG_FORMAT_TAG, valueOf));
        builder.setUrl(deleteAlbumUrl).setMethod(2).setParams(arrayList).setRetryTimes(requestItemBase.otherRetryTimes).setNeedReRequest(false);
        return builder.build();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public void onRequestSuccess(RequestItemBase requestItemBase, JSONObject jSONObject) throws Exception {
        DefaultLogger.d("galleryAction_SyncDeleteAlbum", "onRequestSuccess =>");
        RequestAlbumItem requestAlbumItem = (RequestAlbumItem) requestItemBase;
        if (Preference.sGetCloudGalleryRecyclebinFull()) {
            Preference.sSetCloudGalleryRecyclebinFull(false);
        }
        updateForDeleteOrPurgedOnLocal(requestAlbumItem.dbAlbum.getBaseUri(), this.mContext, requestAlbumItem.dbAlbum, jSONObject);
        SyncLogger.d(getTag(), "Delete album success: %s, type: %s", requestAlbumItem.dbAlbum.getName());
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

    public static boolean tryDeleteDirtyItem(DBAlbum dBAlbum) {
        int safeDelete = GalleryUtils.safeDelete(dBAlbum.getBaseUri(), "_id = ? AND serverId IS NULL ", new String[]{dBAlbum.getId()});
        DefaultLogger.d("galleryAction_SyncDeleteAlbum", "delete db : album id [%s] count [%d]", dBAlbum.getId(), Integer.valueOf(safeDelete));
        return safeDelete > 0;
    }

    public static void updateForDeleteOrPurgedOnLocal(Uri uri, Context context, DBAlbum dBAlbum, JSONObject jSONObject) throws JSONException {
        if (CloudUtils.hasCreateCopyMoveImageByGroupId(context, dBAlbum.getId())) {
            CreateGroupItem.recreateGroup(dBAlbum.getId());
        } else {
            updateForDeleteOrPurgedGroupOnLocal(context, dBAlbum, jSONObject);
        }
    }

    public static void updateForDeleteOrPurgedGroupOnLocal(Context context, DBAlbum dBAlbum, JSONObject jSONObject) throws JSONException {
        ContentValues contentValuesForUploadDeletePurged = AlbumDataHelper.getContentValuesForUploadDeletePurged(jSONObject);
        if (!contentValuesForUploadDeletePurged.containsKey("serverStatus")) {
            contentValuesForUploadDeletePurged.put("serverStatus", "deleted");
        }
        Uri uri = GalleryCloudUtils.ALBUM_URI;
        DefaultLogger.d("galleryAction_SyncDeleteAlbum", "update db : album id [%s] count [%d] values [%s]", dBAlbum.getId(), Integer.valueOf(GalleryUtils.safeUpdate(uri, contentValuesForUploadDeletePurged, "_id = '" + dBAlbum.getId() + "'", null)), Util.desensitization(contentValuesForUploadDeletePurged));
    }
}
