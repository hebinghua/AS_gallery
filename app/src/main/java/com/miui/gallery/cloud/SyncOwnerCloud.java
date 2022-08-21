package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.cloud.download.MicroBatchDownloadManager;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.UbiFocusUtils;
import com.miui.gallery.util.baby.SendNotificationUtilForSharedBabyAlbum;
import com.miui.gallery.util.deprecated.Preference;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class SyncOwnerCloud extends SyncCloudBase {
    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getTagColumnName() {
        return "serverTag";
    }

    public SyncOwnerCloud(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    /* renamed from: getItem */
    public DBImage mo690getItem(JSONObject jSONObject) {
        return CloudUtils.getItemByServerID(this.mContext, jSONObject.optString("id"));
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public List<DBImage> getCandidateItemsInAGroup(String str, String str2, String str3) {
        return CloudUtils.getCandidateItemsInAGroup(this.mContext, str, str2, str3, false);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public Uri getBaseUri() {
        return GalleryCloudUtils.CLOUD_URI;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getSyncTagList() {
        ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList = new ArrayList<>();
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(1));
        return arrayList;
    }

    public boolean isCreatedByMySelf(ContentValues contentValues) {
        String asString = contentValues.getAsString("creatorId");
        return TextUtils.isEmpty(asString) || this.mAccount.name.equalsIgnoreCase(asString);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncTagSelection() {
        return GalleryCloudSyncTagUtils.getAccountSelections(this.mAccount);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public void handleUbiSubImage(JSONObject jSONObject, String str) throws JSONException {
        UbiFocusUtils.handleSubUbiImage(jSONObject, false, str);
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public void handleFavoriteInfo(JSONObject jSONObject, Long l) throws JSONException {
        if (jSONObject.has("isFavorite")) {
            boolean z = jSONObject.getBoolean("isFavorite");
            Boolean bool = (Boolean) GalleryUtils.safeQuery(GalleryContract.Favorites.URI, new String[]{j.c}, "cloud_id = ?", new String[]{String.valueOf(l)}, (String) null, new GalleryUtils.QueryHandler<Boolean>() { // from class: com.miui.gallery.cloud.SyncOwnerCloud.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
                /* renamed from: handle */
                public Boolean mo1712handle(Cursor cursor) {
                    return Boolean.valueOf(cursor != null && cursor.getCount() > 0);
                }
            });
            ContentValues contentValues = new ContentValues();
            contentValues.put("isFavorite", Integer.valueOf(z ? 1 : 0));
            if (bool != null && bool.booleanValue()) {
                GalleryUtils.safeUpdate(GalleryContract.Favorites.DELAY_NOTIFY_URI, contentValues, "cloud_id = ?", new String[]{String.valueOf(l)});
            } else if (!z) {
            } else {
                contentValues.put("cloud_id", String.valueOf(l));
                contentValues.put("dateFavorite", Long.valueOf(System.currentTimeMillis()));
                contentValues.put("source", (Integer) 1);
                GalleryUtils.safeInsert(GalleryContract.Favorites.DELAY_NOTIFY_URI, contentValues);
            }
        }
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public String getLocalGroupId(ContentValues contentValues) {
        Uri safeInsert;
        String asString = contentValues.getAsString("groupId");
        DBAlbum albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, asString);
        if (albumByServerID != null || !CloudTableUtils.isGroupWithoutRecord(Long.parseLong(asString))) {
            if (albumByServerID == null) {
                if (CloudTableUtils.isCameraGroup(asString)) {
                    safeInsert = GalleryUtils.safeInsert(GalleryCloudUtils.ALBUM_URI, AlbumDataHelper.getCameraRecordValues());
                } else {
                    safeInsert = CloudTableUtils.isScreenshotGroup(asString) ? GalleryUtils.safeInsert(GalleryCloudUtils.ALBUM_URI, AlbumDataHelper.getScreenshotsRecordValues()) : null;
                }
                if (safeInsert != null) {
                    long parseId = ContentUris.parseId(safeInsert);
                    if (parseId > 0) {
                        HashMap hashMap = new HashMap();
                        hashMap.put("server_id", asString);
                        SamplingStatHelper.recordCountEvent("Sync", "sync_try_insert_system_album", hashMap);
                        return String.valueOf(parseId);
                    }
                }
            }
            if (albumByServerID != null) {
                return albumByServerID.getId();
            }
            return null;
        }
        return String.valueOf(CloudTableUtils.getCloudIdForGroupWithoutRecord(Long.parseLong(asString)));
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase
    public void onNewImageSynced(ContentValues contentValues, String str) {
        DBAlbum albumByServerID;
        Long asLong = contentValues.getAsLong("serverTag");
        if (asLong == null) {
            SyncLogger.e("SyncOwnerCloud", "server tag should not be null");
        } else if (Preference.sIsFirstSynced()) {
            if (!isCreatedByMySelf(contentValues)) {
                String asString = contentValues.getAsString("groupId");
                if (belong2BabyAlbum(contentValues, false) && (albumByServerID = AlbumDataHelper.getAlbumByServerID(this.mContext, asString)) != null) {
                    SendNotificationUtilForSharedBabyAlbum.getInstance().sendNotification(asString, Long.parseLong(albumByServerID.getId()), albumByServerID.getName(), false, asLong.longValue());
                }
            }
            if (!GalleryPreferences.Sync.isAutoDownload() || isDeleteItem(contentValues.getAsString("serverStatus"))) {
                return;
            }
            BatchDownloadManager.getInstance().download(ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, Long.parseLong(str)), contentValues.getAsString("mimeType"));
        } else {
            MicroBatchDownloadManager.getInstance().download(ContentUris.withAppendedId(GalleryContract.Cloud.CLOUD_URI, Long.parseLong(str)));
        }
    }

    public final boolean isDeleteItem(String str) {
        return TextUtils.equals(str, "deleted") || TextUtils.equals(str, "purged");
    }

    @Override // com.miui.gallery.cloud.SyncCloudBase, com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        super.appendParams(arrayList, arrayList2);
        arrayList.add(new BasicNameValuePair("returnSystemAlbum", String.valueOf(true)));
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public boolean updateSyncTagAndShouldContinue(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) throws JSONException {
        if (jSONObject.getBoolean("lastPage")) {
            try {
                doCorrectLogic();
            } catch (Exception e) {
                SyncLogger.d("SyncOwnerCloud", "correct cloud setting logic error, [%s].", e.getMessage());
            }
        }
        return super.updateSyncTagAndShouldContinue(jSONObject, arrayList);
    }

    public final void doCorrectLogic() throws Exception {
        if (GalleryPreferences.Sync.hasResetCloudSetting()) {
            return;
        }
        long lastSyncTagOfResetCloudSetting = GalleryPreferences.Sync.getLastSyncTagOfResetCloudSetting();
        while (true) {
            Result minCustomSyncTag = getMinCustomSyncTag(lastSyncTagOfResetCloudSetting);
            long j = minCustomSyncTag.minSyncTag;
            if (j == 0 || j == Long.MAX_VALUE) {
                if (minCustomSyncTag.lastPage) {
                    GalleryPreferences.Sync.setHasResetCloudSetting();
                    return;
                } else {
                    lastSyncTagOfResetCloudSetting = minCustomSyncTag.syncTag;
                    GalleryPreferences.Sync.setLastSyncTagOfResetCloudSetting(lastSyncTagOfResetCloudSetting);
                }
            } else {
                if (!checkSyncTagExists(j).booleanValue()) {
                    SyncLogger.d("SyncOwnerCloud", "delete cloud_setting, count [%d].", Integer.valueOf(GalleryUtils.safeDelete(GalleryCloudUtils.BASE_URI.buildUpon().appendPath("cloudSetting").build(), null, null)));
                }
                GalleryPreferences.Sync.setHasResetCloudSetting();
                return;
            }
        }
    }

    public final Result getMinCustomSyncTag(long j) throws Exception {
        ArrayList arrayList = new ArrayList(4);
        arrayList.add(new BasicNameValuePair("syncTag", String.valueOf(j)));
        arrayList.add(new BasicNameValuePair("syncInfo", String.valueOf(Long.MAX_VALUE)));
        arrayList.add(new BasicNameValuePair("limit", String.valueOf(100)));
        JSONObject fromXiaomi = CloudUtils.getFromXiaomi(CloudUtils.HTTPHOST_GALLERY_V2_1 + "/user/full_v2", arrayList, this.mAccount, null, 0, false);
        if (fromXiaomi == null) {
            throw new Exception("get nothing from server.");
        }
        JSONObject jSONObject = fromXiaomi.getJSONObject("data");
        long longAttributeFromJson = CloudUtils.getLongAttributeFromJson(jSONObject, "syncTag");
        boolean z = jSONObject.getBoolean("lastPage");
        JSONArray optJSONArray = jSONObject.optJSONArray(MiStat.Param.CONTENT);
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            SyncLogger.d("SyncOwnerCloud", "current sync tag [%d]", Long.valueOf(longAttributeFromJson));
            return new Result(longAttributeFromJson, z, 0L);
        }
        long j2 = Long.MAX_VALUE;
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
            long longAttributeFromJson2 = CloudUtils.getLongAttributeFromJson(jSONObject2, nexExportFormat.TAG_FORMAT_TAG);
            if (CloudUtils.getLongAttributeFromJson(jSONObject2, "groupId") != 1000) {
                j2 = Math.min(j2, longAttributeFromJson2);
            }
        }
        return new Result(longAttributeFromJson, z, j2);
    }

    public final Boolean checkSyncTagExists(long j) {
        return (Boolean) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"COUNT(*)"}, "serverTag = " + j, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Boolean>() { // from class: com.miui.gallery.cloud.SyncOwnerCloud.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Boolean mo1808handle(Cursor cursor) {
                boolean z = false;
                if (cursor != null && cursor.moveToNext() && cursor.getInt(0) > 0) {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
        });
    }

    /* loaded from: classes.dex */
    public static class Result {
        public boolean lastPage;
        public long minSyncTag;
        public long syncTag;

        public Result(long j, boolean z, long j2) {
            this.syncTag = j;
            this.lastPage = z;
            this.minSyncTag = j2;
        }
    }
}
