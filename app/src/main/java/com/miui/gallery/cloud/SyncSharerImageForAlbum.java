package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.UbiFocusUtils;
import com.miui.gallery.util.baby.SendNotificationUtilForSharedBabyAlbum;
import com.miui.gallery.util.deprecated.Preference;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class SyncSharerImageForAlbum extends SyncCloudBase {
    public final String mShareAlbumId;

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getTagColumnName() {
        return "serverTag";
    }

    public SyncSharerImageForAlbum(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, String str) {
        super(context, account, galleryExtendedAuthToken);
        this.mShareAlbumId = str;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getSyncTagList() {
        ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList = new ArrayList<>();
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(8));
        return arrayList;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_IMAGE_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncUrl() {
        return HostManager.SyncPull.getPullShareAlbumImage();
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase, com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        super.appendParams(arrayList, arrayList2);
        arrayList.add(new BasicNameValuePair("sharedAlbumId", this.mShareAlbumId));
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncTagSelection() {
        return "albumId = '" + this.mShareAlbumId + "'";
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    /* renamed from: getItem */
    public DBImage mo690getItem(JSONObject jSONObject) {
        String optString = jSONObject.optString("id");
        String optString2 = jSONObject.optString("shareId");
        String format = String.format("%s = '%s'", "serverId", optString);
        if (!TextUtils.isEmpty(optString2)) {
            format = String.format("%s OR %s = '%s'", format, "shareId", optString2);
        }
        return CloudUtils.getItem(GalleryCloudUtils.SHARE_IMAGE_URI, this.mContext, format);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public List<DBImage> getCandidateItemsInAGroup(String str, String str2, String str3) {
        return CloudUtils.getCandidateItemsInAGroup(this.mContext, str, str2, str3, true);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public String handleNewImage(ContentValues contentValues, String str, String str2, JSONObject jSONObject) {
        if (str2.equals("custom")) {
            contentValues.put("albumId", this.mShareAlbumId);
            String handleNewImage = super.handleNewImage(contentValues, str, str2, jSONObject);
            if (contentValues.getAsLong("serverTag") != null) {
                return handleNewImage;
            }
            SyncLogger.e("SyncSharerImageForAlbum", "server tag should not be null");
            return handleNewImage;
        }
        return null;
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public void onNewImageSynced(ContentValues contentValues, String str) {
        DBShareAlbum dBShareAlbumBySharedId;
        Long asLong = contentValues.getAsLong("serverTag");
        if (asLong == null) {
            SyncLogger.e("SyncSharerImageForAlbum", "server tag should not be null");
        } else if (!Preference.sIsFirstSynced()) {
        } else {
            if (!isCreatedByMySelf(contentValues) && belong2BabyAlbum(contentValues, true) && (dBShareAlbumBySharedId = CloudUtils.getDBShareAlbumBySharedId(this.mShareAlbumId)) != null) {
                SendNotificationUtilForSharedBabyAlbum.getInstance().sendNotification(this.mShareAlbumId, Long.parseLong(dBShareAlbumBySharedId.getId()), dBShareAlbumBySharedId.getFileName(), true, asLong.longValue());
            }
            if (!GalleryPreferences.Sync.isAutoDownload()) {
                return;
            }
            BatchDownloadManager.getInstance().download(ContentUris.withAppendedId(GalleryContract.ShareImage.SHARE_URI, Long.parseLong(str)), contentValues.getAsString("mimeType"));
        }
    }

    public boolean isCreatedByMySelf(ContentValues contentValues) {
        String asString = contentValues.getAsString("creatorId");
        return !TextUtils.isEmpty(asString) && this.mAccount.name.equalsIgnoreCase(asString);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public void handleUbiSubImage(JSONObject jSONObject, String str) throws JSONException {
        UbiFocusUtils.handleSubUbiImage(jSONObject, true, str);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public String getLocalGroupId(ContentValues contentValues) {
        return CloudUtils.getLocalGroupIdForSharerAlbum(this.mShareAlbumId);
    }
}
