package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.model.dto.utils.Insertable;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SyncOwnerAlbumFromServer extends SyncFromServer {
    public int mLocalGroupIdErrorCount;
    public long mSyncId;

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getCurrentSyncTag() {
        return null;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getSyncTagList() {
        return null;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncTagSelection() {
        return null;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getTagColumnName() {
        return null;
    }

    public SyncOwnerAlbumFromServer(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
        initSyncId();
        DefaultLogger.d("SyncSystemAlbum", "init syncId %d", Long.valueOf(this.mSyncId));
    }

    public final void initSyncId() {
        if (GalleryPreferences.Sync.getEverSyncedSystemAlbum()) {
            int localGroupIDErrorCount = AlbumDataHelper.getLocalGroupIDErrorCount(this.mContext);
            this.mLocalGroupIdErrorCount = localGroupIDErrorCount;
            if (localGroupIDErrorCount > 0) {
                DefaultLogger.d("SyncSystemAlbum", "error localGroupId，reset mSyncId = 0");
                this.mSyncId = 0L;
                return;
            }
            this.mSyncId = ((Long) SafeDBUtil.safeQuery(this.mContext, getBaseUri(), new String[]{"max(serverId)"}, String.format(Locale.US, "(%s=%d AND %s='%s')", "localFlag", 0, "serverStatus", "custom"), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Long>() { // from class: com.miui.gallery.cloud.SyncOwnerAlbumFromServer.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public Long mo1808handle(Cursor cursor) {
                    if (cursor != null && cursor.moveToFirst()) {
                        return Long.valueOf(cursor.getLong(0));
                    }
                    return 0L;
                }
            })).longValue();
            return;
        }
        this.mSyncId = 0L;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public Uri getBaseUri() {
        return GalleryCloudUtils.ALBUM_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncUrl() {
        return HostManager.SyncPull.getPullOwnerAlbumUrl();
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) {
        arrayList.add(new BasicNameValuePair("syncId", String.valueOf(this.mSyncId)));
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public boolean handleResultAndShouldContinue(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) throws JSONException {
        String handleNewGroup;
        JSONArray optJSONArray = jSONObject.optJSONArray("albums");
        if (optJSONArray == null) {
            SyncLogger.e("SyncSystemAlbum", "response content null");
            return false;
        }
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
            long longAttributeFromJson = CloudUtils.getLongAttributeFromJson(jSONObject2, "albumId");
            DBAlbum albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, String.valueOf(longAttributeFromJson));
            ContentValues contentValuesForResponse = AlbumSyncHelper.getContentValuesForResponse(jSONObject2);
            boolean isSystemAlbum = AlbumDataHelper.isSystemAlbum(String.valueOf(longAttributeFromJson));
            if (albumByServerID == null && isSystemAlbum) {
                AlbumDataHelper.addRecordsForCameraAndScreenshots(new Insertable() { // from class: com.miui.gallery.cloud.SyncOwnerAlbumFromServer.2
                    @Override // com.miui.gallery.model.dto.utils.Insertable
                    public long insert(Uri uri, String str, String str2, ContentValues contentValues) {
                        return GalleryUtils.safeInsert(uri, contentValues) == null ? 0L : 1L;
                    }
                });
                albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, String.valueOf(longAttributeFromJson));
                HashMap hashMap = new HashMap();
                hashMap.put("album", String.valueOf(longAttributeFromJson));
                SamplingStatHelper.recordCountEvent("Sync", "system_album_record_not_found", hashMap);
            }
            if (isSystemAlbum) {
                if (albumByServerID != null && albumByServerID.getLocalFlag() == 0 && !GalleryPreferences.Sync.getEverSyncedSystemAlbum()) {
                    AlbumDataHelper.updateToLocalDBByServerId(contentValuesForResponse, String.valueOf(longAttributeFromJson));
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("localGroupId", albumByServerID.getId());
                    SafeDBUtil.safeUpdate(this.mContext, GalleryCloudUtils.CLOUD_URI, contentValues, String.format(Locale.US, "%s=%s AND (%s!=%s) AND (%s=%d OR %s=%d)", "groupId", Long.valueOf(longAttributeFromJson), "localGroupId", albumByServerID.getId(), "serverType", 1, "serverType", 2), (String[]) null);
                }
            } else if (!CloudTableUtils.isGroupWithoutRecord(longAttributeFromJson) && albumByServerID == null && (handleNewGroup = AlbumSyncHelper.handleNewGroup(this.mContext, getBaseUri(), contentValuesForResponse, String.valueOf(longAttributeFromJson), jSONObject2)) != null && this.mLocalGroupIdErrorCount > 0) {
                DefaultLogger.w("SyncSystemAlbum", "getLocalGroupIDErrorCount: " + this.mLocalGroupIdErrorCount + " currentServerID:" + longAttributeFromJson);
                CloudUtils.updateLocalGroupIdInGroup(GalleryCloudUtils.CLOUD_URI, String.valueOf(longAttributeFromJson), handleNewGroup, "groupId");
                int localGroupIDErrorCount = AlbumDataHelper.getLocalGroupIDErrorCount(this.mContext);
                int i2 = localGroupIDErrorCount - this.mLocalGroupIdErrorCount;
                this.mLocalGroupIdErrorCount = localGroupIDErrorCount;
                DefaultLogger.w("SyncSystemAlbum", "after update, currentErrorCount = " + this.mLocalGroupIdErrorCount + " fixed count =" + i2);
                if (i2 > 0) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(MiStat.Param.COUNT, String.valueOf(i2));
                    SamplingStatHelper.recordCountEvent("Sync", "sync_fix_localgroupid", hashMap2);
                }
            }
        }
        if (!GalleryPreferences.Sync.getEverSyncedSystemAlbum()) {
            SafeDBUtil.safeDelete(this.mContext, GalleryCloudUtils.CLOUD_URI, String.format(Locale.US, "%s=-1 AND (%s=%d OR %s=%d) AND (%s=%d OR %s=%d)", "localGroupId", "localFlag", 7, "localFlag", 8, "serverType", 1, "serverType", 2), null);
        }
        GalleryPreferences.Sync.setEverSyncedSystemAlbum();
        this.mSyncId = CloudUtils.getLongAttributeFromJson(jSONObject, "syncAlbumId");
        return !jSONObject.getBoolean("lastPage");
    }

    public static boolean handleResponse(Context context, JSONObject jSONObject) throws JSONException {
        String string;
        String string2;
        DBAlbum albumByServerID;
        if (!jSONObject.getString(nexExportFormat.TAG_FORMAT_TYPE).equals("group")) {
            DefaultLogger.e("SyncSystemAlbum", "handleResponse,but item type not album:[%s]", TextUtils.join("\n", Thread.currentThread().getStackTrace()));
            return false;
        }
        try {
            string = jSONObject.getString("id");
            string2 = jSONObject.getString("status");
            albumByServerID = AlbumDataHelper.getAlbumByServerID(context, string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (albumByServerID == null) {
            CloudUtils.updateLocalGroupIdInGroup(GalleryCloudUtils.CLOUD_URI, string, AlbumSyncHelper.handleNewGroup(context, GalleryCloudUtils.ALBUM_URI, string, jSONObject), "groupId");
            return false;
        } else if (albumByServerID.getServerTag() >= CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG)) {
            SyncLogger.d("SyncSystemAlbum", "cloud serverId:" + albumByServerID.getServerId() + " local tag:" + albumByServerID.getServerTag() + " >= server tag:" + CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG) + ", don't update local.");
            return false;
        } else {
            if (string2.equals("custom")) {
                AlbumSyncHelper.handleItemForCustom(jSONObject, albumByServerID);
            } else if (string2.equals("deleted")) {
                AlbumSyncHelper.handleItemForDeleted(GalleryCloudUtils.ALBUM_URI, context, jSONObject, albumByServerID);
            } else if (string2.equals("purged")) {
                AlbumSyncHelper.handleItemForPurged(GalleryCloudUtils.ALBUM_URI, context, jSONObject, albumByServerID);
            } else {
                SyncLogger.e("SyncSystemAlbum", "status unrecognized, schema:" + jSONObject);
            }
            return false;
        }
    }
}
