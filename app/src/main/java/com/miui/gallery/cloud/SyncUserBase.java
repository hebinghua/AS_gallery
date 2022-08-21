package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBShareUser;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class SyncUserBase extends SyncFromServer {
    public final String mAlbumId;

    @Override // com.miui.gallery.cloud.SyncFromServer
    public final int getSyncItemLimit() {
        return 0;
    }

    public SyncUserBase(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, String str) {
        super(context, account, galleryExtendedAuthToken);
        this.mAlbumId = str;
    }

    public boolean handleUser(JSONObject jSONObject) throws JSONException {
        String str;
        String string = jSONObject.getString("status");
        String string2 = jSONObject.getString("userId");
        long longAttributeFromJson = CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG);
        synchronized (getBaseUri()) {
            DBShareUser shareUserById = getShareUserById(this.mContext, string2);
            if (shareUserById == null) {
                String str2 = null;
                if (jSONObject.has("relation")) {
                    str2 = jSONObject.getString("relation");
                    str = jSONObject.getString("relationText");
                } else {
                    str = null;
                }
                if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
                    setPhoneNumberNullIfNeeded(str2, str);
                    DBShareUser invitedShareUserByRelation = getInvitedShareUserByRelation(string2, str2, str);
                    if (invitedShareUserByRelation != null) {
                        return handleUserInvited(jSONObject, invitedShareUserByRelation, string);
                    }
                }
                if (!string.equals("normal")) {
                    return false;
                }
                ContentValues contentValues = DBShareUser.getContentValues(jSONObject);
                contentValues.put("albumId", this.mAlbumId);
                GalleryUtils.safeInsert(getBaseUri(), contentValues);
                return true;
            }
            SyncLogger.d("SyncUserBase", "find this user in local, update");
            if (shareUserById.getServerTag() >= longAttributeFromJson) {
                SyncLogger.d("SyncUserBase", "shareUser:" + shareUserById.getId() + " local tag:" + shareUserById.getServerTag() + " >= server tag:" + longAttributeFromJson + ", don't update local.");
                return false;
            } else if (!string.equals("normal")) {
                GalleryUtils.safeDelete(getBaseUri(), "_id = ? ", new String[]{shareUserById.getId()});
                SyncLogger.d("SyncUserBase", "delete share user:" + shareUserById.getId());
                return false;
            } else {
                GalleryUtils.safeUpdate(getBaseUri(), DBShareUser.getContentValues(jSONObject), "_id = ? ", new String[]{shareUserById.getId()});
                return false;
            }
        }
    }

    public final boolean handleUserInvited(JSONObject jSONObject, DBShareUser dBShareUser, String str) throws JSONException {
        if (dBShareUser != null) {
            if (str.equals("normal") || str.equals("invited")) {
                GalleryUtils.safeUpdate(getBaseUri(), DBShareUser.getContentValues(jSONObject), "_id = ? ", new String[]{dBShareUser.getId()});
                return true;
            }
            GalleryUtils.safeDelete(getBaseUri(), "_id = ? ", new String[]{dBShareUser.getId()});
        }
        return false;
    }

    public final void setPhoneNumberNullIfNeeded(String str, String str2) {
        Uri baseUri = getBaseUri();
        Uri uri = GalleryCloudUtils.CLOUD_USER_URI;
        if (baseUri == uri) {
            if (!TextUtils.equals(str, "family") && !TextUtils.equals(str, "friend")) {
                return;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.putNull("phone");
            GalleryUtils.safeUpdate(uri, contentValues, String.format(Locale.US, "%s='%s' and %s='%s' and %s='%s' and %s='%s'", "albumId", this.mAlbumId, "relation", str, "relationText", str2, "serverStatus", "invited"), null);
        }
    }

    public final DBShareUser getInvitedShareUserByRelation(String str, String str2, String str3) {
        return CloudUtils.getInvitedDBShareUserInLocal(getBaseUri(), str, this.mAlbumId, str2, str3);
    }

    public final DBShareUser getShareUserById(Context context, String str) {
        return CloudUtils.getDBShareUserInLocal(getBaseUri(), str, this.mAlbumId);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public final boolean updateSyncTagAndShouldContinue(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) throws JSONException {
        long longAttributeFromJson = CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG);
        if (longAttributeFromJson > arrayList.get(0).currentValue) {
            SyncLogger.d("SyncUserBase", "update the syncTag:" + longAttributeFromJson);
            arrayList.get(0).serverValue = longAttributeFromJson;
            updateSyncTag(arrayList);
        }
        return false;
    }

    public final boolean addUsers(JSONObject jSONObject) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray("list");
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            SyncLogger.d("SyncUserBase", "changedUserList is empty, return.");
            return false;
        }
        boolean z = false;
        for (int i = 0; i < optJSONArray.length(); i++) {
            if (handleUser(optJSONArray.getJSONObject(i))) {
                z = true;
            }
        }
        return z;
    }
}
