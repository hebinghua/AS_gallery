package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.net.Uri;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class SyncOwnerUserForAlbum extends SyncUserBase {
    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getTagColumnName() {
        return "serverTag";
    }

    public SyncOwnerUserForAlbum(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, String str) {
        super(context, account, galleryExtendedAuthToken, str);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public Uri getBaseUri() {
        return GalleryCloudUtils.CLOUD_USER_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getSyncTagList() {
        ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList = new ArrayList<>();
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(10));
        return arrayList;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncUrl() {
        return HostManager.SyncPull.getPullOwnerShareUserUrl(this.mAlbumId);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public final boolean handleDataJson(JSONObject jSONObject) throws JSONException {
        return addUsers(jSONObject);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncTagSelection() {
        return "albumId = '" + this.mAlbumId + "'";
    }
}
