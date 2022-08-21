package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.baby.BabyAlbumUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.operation.delete.DeleteCloudItem;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.data.DBItem;
import com.miui.gallery.data.ServerItem;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.micloudsdk.request.utils.Request;
import com.xiaomi.stat.MiStat;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class SyncCloudBase extends SyncFromServer {
    public abstract List<DBImage> getCandidateItemsInAGroup(String str, String str2, String str3);

    /* renamed from: getItem */
    public abstract DBItem mo690getItem(JSONObject jSONObject);

    public abstract String getLocalGroupId(ContentValues contentValues);

    public void handleFavoriteInfo(JSONObject jSONObject, Long l) throws JSONException {
    }

    public abstract void handleUbiSubImage(JSONObject jSONObject, String str) throws JSONException;

    public abstract void onNewImageSynced(ContentValues contentValues, String str);

    public SyncCloudBase(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
    }

    public final boolean handleItem(JSONObject jSONObject) throws Exception {
        if (!SyncUtil.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext())) {
            return false;
        }
        String string = jSONObject.getString(nexExportFormat.TAG_FORMAT_TYPE);
        String string2 = jSONObject.getString("status");
        String string3 = jSONObject.getString("id");
        synchronized (getBaseUri()) {
            if (string.equals("group")) {
                return SyncOwnerAlbumFromServer.handleResponse(this.mContext, jSONObject);
            }
            DBItem mo690getItem = mo690getItem(jSONObject);
            if (mo690getItem == null) {
                ContentValues contentValuesForAll = CloudUtils.getContentValuesForAll(jSONObject);
                contentValuesForAll.put("localGroupId", getLocalGroupId(contentValuesForAll));
                String handleNewImage = handleNewImage(contentValuesForAll, string3, string2, jSONObject);
                handleUbiSubImage(jSONObject, handleNewImage);
                if (handleNewImage != null && jSONObject.has("isFavorite")) {
                    try {
                        handleFavoriteInfo(jSONObject, Long.valueOf(Long.parseLong(handleNewImage)));
                    } catch (NumberFormatException e) {
                        DefaultLogger.e("SyncCloudBase", e);
                    }
                }
                DeleteCloudItem.handleDeleteOrPurgeStatusForNewImage(this.mContext, string2, string3, jSONObject, "SyncCloudBase");
                return true;
            } else if (((ServerItem) mo690getItem).getServerTag() >= CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG)) {
                SyncLogger.d("SyncCloudBase", "cloud serverId:" + ((ServerItem) mo690getItem).getServerId() + " local tag:" + ((ServerItem) mo690getItem).getServerTag() + " >= server tag:" + CloudUtils.getLongAttributeFromJson(jSONObject, nexExportFormat.TAG_FORMAT_TAG) + ", don't update local.");
                return false;
            } else {
                if (string2.equals("custom")) {
                    handleCustom(mo690getItem, jSONObject);
                    handleUbiSubImage(jSONObject, null);
                } else if (string2.equals("deleted")) {
                    handleDelete(mo690getItem, jSONObject);
                } else if (string2.equals("purged")) {
                    handlePurged(mo690getItem, jSONObject);
                } else {
                    SyncLogger.e("SyncCloudBase", "status unrecognized, schema:" + jSONObject);
                }
                return false;
            }
        }
    }

    public static void statSuspiciousAlbum(ContentValues contentValues) {
        HashMap hashMap = new HashMap();
        hashMap.put("album", contentValues.toString());
        SamplingStatHelper.recordCountEvent("Sync", "sync_suspicious_album", hashMap);
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00b0, code lost:
        if (android.text.TextUtils.isEmpty(com.miui.gallery.util.ExifUtil.getUserCommentSha1(r6.getLocalFile())) != false) goto L74;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x00d7, code lost:
        if (r6.getSize() < r3) goto L77;
     */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00f1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String handleNewImage(android.content.ContentValues r23, java.lang.String r24, java.lang.String r25, org.json.JSONObject r26) {
        /*
            Method dump skipped, instructions count: 557
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.SyncCloudBase.handleNewImage(android.content.ContentValues, java.lang.String, java.lang.String, org.json.JSONObject):java.lang.String");
    }

    public final void logForNullLocalGroupId(ContentValues contentValues, JSONObject jSONObject) {
        SyncLogger.i("SyncCloudBase", "localGroupId is null, schemaJson= ", jSONObject == null ? "" : jSONObject.toString());
        HashMap hashMap = new HashMap();
        hashMap.put("album", contentValues.getAsString("groupId"));
        hashMap.put("state", contentValues.getAsString("serverStatus"));
        SamplingStatHelper.recordCountEvent("Sync", "group_record_not_found", hashMap);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x00c1  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x013a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void handleCustom(com.miui.gallery.data.DBItem r13, org.json.JSONObject r14) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 388
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloud.SyncCloudBase.handleCustom(com.miui.gallery.data.DBItem, org.json.JSONObject):void");
    }

    public final void handleDelete(DBItem dBItem, JSONObject jSONObject) throws Exception {
        try {
            DeleteCloudItem.updateForDeleteOrPurgedOnLocal(getBaseUri(), this.mContext, (DBImage) dBItem, jSONObject);
        } catch (StoragePermissionMissingException e) {
            throw new InterruptedExceptionWrapper(e);
        }
    }

    public final void handlePurged(DBItem dBItem, JSONObject jSONObject) throws JSONException {
        try {
            DeleteCloudItem.updateForDeleteOrPurgedOnLocal(getBaseUri(), this.mContext, (DBImage) dBItem, jSONObject);
        } catch (StoragePermissionMissingException unused) {
        }
    }

    public final Set<String> getLargerTags(long j) {
        HashSet newHashSet = Sets.newHashSet();
        Cursor cursor = null;
        try {
            Uri appendLimit = UriUtil.appendLimit(getBaseUri(), 100);
            cursor = this.mContext.getContentResolver().query(appendLimit, new String[]{"serverTag"}, "serverTag > " + j, null, "serverTag ASC ");
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    newHashSet.add(cursor.getString(0));
                }
            }
            return newHashSet;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        if (arrayList2 == null) {
            return;
        }
        super.appendParams(arrayList, arrayList2);
        Set<String> largerTags = getLargerTags(arrayList2.get(0).currentValue);
        HashMap newHashMap = Maps.newHashMap();
        Iterator<NameValuePair> it = arrayList.iterator();
        while (it.hasNext()) {
            NameValuePair next = it.next();
            newHashMap.put(next.getName(), next.getValue());
        }
        Request.addFilterTagsToParams(getSyncUrl(), newHashMap, this.mExtendedAuthToken.getSecurity(), largerTags, arrayList2.get(0).currentValue);
        String str = (String) newHashMap.get("filterTag");
        if (TextUtils.isEmpty(str)) {
            return;
        }
        arrayList.add(new BasicNameValuePair("filterTag", str));
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public final boolean handleDataJson(JSONObject jSONObject) throws Exception {
        JSONArray optJSONArray = jSONObject.optJSONArray(MiStat.Param.CONTENT);
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            SyncLogger.w("SyncCloudBase", "contentArray is empty, return.");
            return false;
        }
        boolean z = false;
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
            if (handleItem(jSONObject2)) {
                z = true;
            }
            String string = jSONObject2.getString(nexExportFormat.TAG_FORMAT_TYPE);
            String string2 = jSONObject2.getString("status");
            if (string.equals("group") && string2.equals("custom") && jSONObject2.has("isPublic") && jSONObject2.getBoolean("isPublic")) {
                AlbumShareOperations.requestPublicUrl(jSONObject2.getString("id"), false);
            }
        }
        return z;
    }

    public boolean belong2BabyAlbum(ContentValues contentValues, boolean z) {
        return BabyAlbumUtils.isBabyAlbumForThisServerId(this.mContext, contentValues.getAsString("groupId"), z);
    }
}
