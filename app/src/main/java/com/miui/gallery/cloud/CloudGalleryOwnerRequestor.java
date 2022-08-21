package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.GalleryUtils;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CloudGalleryOwnerRequestor extends CloudGalleryRequestorBase {
    public CloudGalleryOwnerRequestor(Account account, CloudUrlProvider cloudUrlProvider) {
        super(account, cloudUrlProvider);
    }

    @Override // com.miui.gallery.cloud.CloudGalleryRequestorBase
    public String getAlbumId(RequestCloudItem requestCloudItem) {
        long groupId = requestCloudItem.dbCloud.getGroupId();
        if (CloudTableUtils.isSystemAlbum(groupId)) {
            return String.valueOf(groupId);
        }
        String albumServerIdByLocalId = AlbumDataHelper.getAlbumServerIdByLocalId(GalleryApp.sGetAndroidContext(), requestCloudItem.dbCloud.getLocalGroupId());
        if (TextUtils.isEmpty(albumServerIdByLocalId)) {
            return null;
        }
        requestCloudItem.dbCloud.setRequestAlbumId(albumServerIdByLocalId);
        return albumServerIdByLocalId;
    }

    @Override // com.miui.gallery.cloud.CloudGalleryRequestorBase
    public String parseRequestId(JSONObject jSONObject, RequestCloudItem requestCloudItem) throws JSONException {
        String string = jSONObject.getString("id");
        requestCloudItem.dbCloud.setServerId(string);
        return string;
    }

    @Override // com.miui.gallery.cloud.CloudGalleryRequestorBase
    public DBImage getItemByRequestId(String str, RequestCloudItem requestCloudItem) {
        return CloudUtils.getItem(requestCloudItem.dbCloud.getBaseUri(), GalleryApp.sGetAndroidContext(), "serverId", str);
    }

    @Override // com.miui.gallery.cloud.CloudGalleryRequestorBase
    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return SpaceFullHandler.getOwnerSpaceFullListener();
    }

    @Override // com.miui.gallery.cloud.CloudGalleryRequestorBase
    public void connectSubUbi(RequestCloudItem requestCloudItem) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("ubiServerId", requestCloudItem.dbCloud.getServerId());
        GalleryUtils.safeUpdate(GalleryCloudUtils.OWNER_SUB_UBIFOCUS_URI, contentValues, "ubiLocalId = ? ", new String[]{requestCloudItem.dbCloud.getId()});
    }
}
