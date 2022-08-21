package com.miui.gallery.cloud.operation.copy;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.miui.gallery.cloud.CloudUrlProvider;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.util.UbiFocusUtils;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class CopyImageToSharerAlbum extends CopyImageBase {
    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public String getAlbumIdTagName() {
        return "toSharedAlbumId";
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public boolean isToShare() {
        return true;
    }

    public CopyImageToSharerAlbum(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public String getAlbumId(RequestCloudItem requestCloudItem) {
        return CloudUtils.getShareAlbumIdByLocalId(this.mContext, requestCloudItem);
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_IMAGE_URI;
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public void appendValues(ContentValues contentValues) {
        contentValues.put("albumId", this.mAlbumId);
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return SpaceFullHandler.getSharerSpaceFullListener();
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public void handleSubUbiSchema(JSONObject jSONObject, DBImage dBImage, ContentValues contentValues) throws Exception {
        UbiFocusUtils.handleSubUbiImage(jSONObject, true, dBImage.getId());
    }

    @Override // com.miui.gallery.cloud.operation.copy.CopyImageBase
    public String getUrl(CloudUrlProvider cloudUrlProvider, String str, String str2, boolean z, RequestCloudItem requestCloudItem) {
        return cloudUrlProvider.getCopyUrl(str, str2);
    }
}
