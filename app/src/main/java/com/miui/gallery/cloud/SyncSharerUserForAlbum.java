package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBShareAlbum;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class SyncSharerUserForAlbum extends SyncUserBase {
    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getTagColumnName() {
        return "serverTag";
    }

    public SyncSharerUserForAlbum(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, String str) {
        super(context, account, galleryExtendedAuthToken, str);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public Uri getBaseUri() {
        return GalleryCloudUtils.SHARE_USER_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getSyncTagList() {
        ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList = new ArrayList<>();
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(9));
        return arrayList;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncUrl() {
        return HostManager.SyncPull.getPullShareUserUrl();
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        super.appendParams(arrayList, arrayList2);
        arrayList.add(new BasicNameValuePair("sharedAlbumId", this.mAlbumId));
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void updateSyncTag(ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GalleryCloudSyncTagUtils.getColumnName(arrayList.get(0).syncTagType), Long.valueOf(arrayList.get(0).serverValue));
        GalleryUtils.safeUpdate(GalleryCloudUtils.SHARE_ALBUM_URI, contentValues, "albumId = ? ", new String[]{this.mAlbumId});
    }

    public final boolean cleanAllUsersInLocalDB() {
        return GalleryUtils.safeDelete(getBaseUri(), "albumId = ? ", new String[]{this.mAlbumId}) > 0;
    }

    public final void resetUsersInLocalDB() {
        cleanAllUsersInLocalDB();
        insertCreatorIntoShareUser(CloudUtils.getCreatorIdByAlbumId(this.mAlbumId), this.mAlbumId);
    }

    public static void insertCreatorIntoShareUser(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            SyncLogger.d("SyncSharerUserForAlbum", "reset users, creator id or album id is null.");
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("albumId", str2);
        contentValues.put("userId", str);
        GalleryUtils.safeInsert(GalleryCloudUtils.SHARE_USER_URI, contentValues);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public final boolean handleDataJson(JSONObject jSONObject) throws JSONException {
        if (CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG) > getCurrentSyncTag().get(0).currentValue) {
            resetUsersInLocalDB();
        }
        return addUsers(jSONObject);
    }

    @Override // com.miui.gallery.cloud.SyncUserBase
    public final boolean handleUser(JSONObject jSONObject) throws JSONException {
        if ("normal".equals(jSONObject.getString("status"))) {
            String optString = jSONObject.optString("peopleId");
            String optString2 = jSONObject.optString("userId");
            if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2) && optString2.equals(GalleryCloudUtils.getAccountName())) {
                DBShareAlbum dBShareAlbumBySharedId = CloudUtils.getDBShareAlbumBySharedId(this.mAlbumId);
                BabyInfo fromJSON = dBShareAlbumBySharedId == null ? null : BabyInfo.fromJSON(dBShareAlbumBySharedId.getBabyInfoJson());
                if (fromJSON != null) {
                    fromJSON.mAutoupdate = jSONObject.optBoolean("autoUpdate");
                    fromJSON.mPeopleId = optString;
                    ContentValues contentValues = new ContentValues();
                    contentValues.putNull("editedColumns");
                    contentValues.put("babyInfoJson", fromJSON.toJSON());
                    contentValues.put("peopleId", optString);
                    GalleryUtils.safeUpdate(GalleryCloudUtils.SHARE_ALBUM_URI, contentValues, String.format(Locale.US, "%s=?", "albumId"), new String[]{this.mAlbumId});
                }
            }
        }
        return super.handleUser(jSONObject);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncTagSelection() {
        return "albumId = '" + this.mAlbumId + "'";
    }
}
