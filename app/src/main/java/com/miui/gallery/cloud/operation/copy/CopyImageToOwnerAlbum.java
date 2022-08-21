package com.miui.gallery.cloud.operation.copy;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.CloudTableUtils;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.UbiFocusUtils;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CopyImageToOwnerAlbum extends CopyImageBase {
    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public String getAlbumIdTagName() {
        return "toAlbumId";
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public boolean isToShare() {
        return false;
    }

    public CopyImageToOwnerAlbum(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public String getAlbumId(RequestCloudItem requestCloudItem) {
        return AlbumDataHelper.getAlbumServerIdByLocalId(this.mContext, requestCloudItem.dbCloud.getLocalGroupId());
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public Uri getBaseUri() {
        return GalleryCloudUtils.CLOUD_URI;
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return SpaceFullHandler.getOwnerSpaceFullListener();
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public void handleSubUbiSchema(JSONObject jSONObject, DBImage dBImage, ContentValues contentValues) throws Exception {
        UbiFocusUtils.handleSubUbiImage(jSONObject, false, dBImage.getId());
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public String getUrl(CloudUrlProvider cloudUrlProvider, String str, String str2, boolean z, RequestCloudItem requestCloudItem) {
        boolean z2;
        boolean isSecretAlbum = CloudTableUtils.isSecretAlbum(this.mAlbumId, null);
        if (!z) {
            String groupIdByPhotoLocalId = CloudUtils.getGroupIdByPhotoLocalId(requestCloudItem.dbCloud.getLocalImageId());
            if (TextUtils.isEmpty(groupIdByPhotoLocalId)) {
                return null;
            }
            z2 = CloudTableUtils.isSecretAlbum(groupIdByPhotoLocalId, null);
        } else {
            z2 = false;
        }
        if (z) {
            if (!isSecretAlbum && !z2) {
                return cloudUrlProvider.getCopyUrl(str, str2);
            }
            SyncLogger.d(getTag(), "getUrl  cannot support copy share image to hide album");
            return null;
        } else if (!isSecretAlbum && !z2) {
            return cloudUrlProvider.getCopyUrl(str, str2);
        } else {
            if (isSecretAlbum && !z2) {
                return cloudUrlProvider.getHideCopyUrl(str, str2);
            }
            if (!isSecretAlbum && z2) {
                return cloudUrlProvider.getUnhideCopyUrl(str, str2);
            }
            SyncLogger.d(getTag(), "getUrl  only support one hide album now");
            return null;
        }
    }
}
