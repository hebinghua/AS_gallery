package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.RetryRequestHelper;
import com.miui.gallery.cloud.base.SyncTask;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class SyncFromServer {
    public Account mAccount;
    public Context mContext;
    public GalleryExtendedAuthToken mExtendedAuthToken;

    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
    }

    public abstract Uri getBaseUri();

    public int getSyncItemLimit() {
        return 100;
    }

    public abstract ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getSyncTagList();

    public abstract String getSyncTagSelection();

    public abstract String getSyncUrl();

    public abstract String getTagColumnName();

    public boolean handleDataJson(JSONObject jSONObject) throws Exception {
        return false;
    }

    public void onSyncSuccess() {
    }

    public SyncFromServer(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        this.mContext = context;
        this.mAccount = account;
        this.mExtendedAuthToken = galleryExtendedAuthToken;
    }

    public final GallerySyncResult<JSONObject> sync() throws Exception {
        GallerySyncResult<JSONObject> retryTask;
        SyncLogger.d("SyncFromServer", "sync from server start");
        long currentTimeMillis = System.currentTimeMillis();
        while (true) {
            final ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> currentSyncTag = getCurrentSyncTag();
            if (SyncConditionManager.check(0) == 2) {
                SyncLogger.e("SyncFromServer", "SyncConditionManager check false");
                return new GallerySyncResult.Builder().setCode(GallerySyncCode.CONDITION_INTERRUPTED).build();
            }
            retryTask = RetryRequestHelper.retryTask(new SyncTask<JSONObject>() { // from class: com.miui.gallery.cloud.SyncFromServer.1
                @Override // com.miui.gallery.cloud.base.SyncTask
                public String getIdentifier() {
                    return SyncFromServer.this.getSyncUrl();
                }

                @Override // com.miui.gallery.cloud.base.SyncTask
                public GallerySyncResult<JSONObject> run() throws Exception {
                    SyncFromServer syncFromServer = SyncFromServer.this;
                    return CheckResult.checkXMResultCode(syncFromServer.getItemsList(currentSyncTag, syncFromServer.getSyncItemLimit()), null, null);
                }
            });
            JSONObject jSONObject = retryTask.data;
            GallerySyncCode gallerySyncCode = retryTask.code;
            GallerySyncCode gallerySyncCode2 = GallerySyncCode.RESET_SYNC_TAG;
            if (gallerySyncCode == gallerySyncCode2) {
                SyncLogger.e("SyncFromServer", "sync from server error " + gallerySyncCode2 + ", need clear data");
                Preference.setSyncShouldClearDataBase(true);
                break;
            } else if (gallerySyncCode == GallerySyncCode.OK) {
                if (jSONObject == null) {
                    break;
                }
                JSONObject optJSONObject = jSONObject.optJSONObject("data");
                if (optJSONObject == null) {
                    break;
                } else if (optJSONObject.length() == 0) {
                    break;
                } else if (!handleResultAndShouldContinue(optJSONObject, currentSyncTag)) {
                    break;
                }
            } else {
                SyncLogger.e("SyncFromServer", "sync from server error:" + jSONObject);
                break;
            }
        }
        SyncLogger.d("SyncFromServer", "sync from server finish: " + (System.currentTimeMillis() - currentTimeMillis));
        if (retryTask.code == GallerySyncCode.OK) {
            onSyncSuccess();
        } else {
            HashMap hashMap = new HashMap();
            hashMap.put("name", getClass().getSimpleName());
            hashMap.put("result", retryTask.toString());
            SamplingStatHelper.recordCountEvent("Sync", "sync_error_class", hashMap);
        }
        return retryTask;
    }

    public boolean handleResultAndShouldContinue(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) throws Exception {
        if (!SyncUtil.isGalleryCloudSyncable(GalleryApp.sGetAndroidContext())) {
            return false;
        }
        handleDataJson(jSONObject);
        updateSyncInfo(jSONObject, arrayList);
        return updateSyncTagAndShouldContinue(jSONObject, arrayList);
    }

    public boolean updateSyncTagAndShouldContinue(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) throws JSONException {
        boolean z = jSONObject.getBoolean("lastPage");
        long longAttributeFromJson = CloudUtils.getLongAttributeFromJson(jSONObject, "syncTag");
        if (z) {
            long largestTagInLocalDB = getLargestTagInLocalDB();
            longAttributeFromJson = (longAttributeFromJson > ((long) GalleryCloudSyncTagUtils.getInitSyncTagValue(arrayList.get(0).syncTagType)) || largestTagInLocalDB > ((long) GalleryCloudSyncTagUtils.getInitSyncTagValue(arrayList.get(0).syncTagType))) ? Math.max(longAttributeFromJson, largestTagInLocalDB) : 0L;
        }
        if (longAttributeFromJson > arrayList.get(0).currentValue) {
            SyncLogger.d("SyncFromServer", "update the syncTag:" + longAttributeFromJson);
            arrayList.get(0).serverValue = longAttributeFromJson;
            updateSyncTag(arrayList);
        } else {
            SyncLogger.w("SyncFromServer", "toUpdateSyncTag is not valid, current is %s toUpdateSyncTag is %s", Long.valueOf(arrayList.get(0).currentValue), Long.valueOf(longAttributeFromJson));
        }
        if (z) {
            SyncLogger.w("SyncFromServer", "last page, break sync from server");
            return false;
        }
        return true;
    }

    public static ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getCurrentSyncTag(final ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList, String str) {
        GalleryUtils.safeQuery(CloudUtils.getLimitUri(GalleryCloudSyncTagUtils.getUri(arrayList.get(0).syncTagType), 1), GalleryCloudSyncTagUtils.getSyncTagSelection(arrayList), str, (String[]) null, (String) null, new GalleryUtils.QueryHandler<Void>() { // from class: com.miui.gallery.cloud.SyncFromServer.2
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1712handle(Cursor cursor) {
                if (cursor != null && cursor.moveToNext()) {
                    for (int i = 0; i < arrayList.size(); i++) {
                        ((GalleryCloudSyncTagUtils.SyncTagItem) arrayList.get(i)).currentValue = Math.max(cursor.getLong(i), GalleryCloudSyncTagUtils.getInitSyncTagValue(((GalleryCloudSyncTagUtils.SyncTagItem) arrayList.get(i)).syncTagType));
                    }
                    return null;
                }
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    GalleryCloudSyncTagUtils.SyncTagItem syncTagItem = (GalleryCloudSyncTagUtils.SyncTagItem) it.next();
                    syncTagItem.currentValue = GalleryCloudSyncTagUtils.getInitSyncTagValue(syncTagItem.syncTagType);
                }
                return null;
            }
        });
        return arrayList;
    }

    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getCurrentSyncTag() {
        return getCurrentSyncTag(getSyncTagList(), getSyncTagSelection());
    }

    public void updateSyncTag(ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        Uri uri = GalleryCloudSyncTagUtils.getUri(arrayList.get(0).syncTagType);
        ContentValues contentValues = new ContentValues();
        Iterator<GalleryCloudSyncTagUtils.SyncTagItem> it = arrayList.iterator();
        while (it.hasNext()) {
            GalleryCloudSyncTagUtils.SyncTagItem next = it.next();
            contentValues.put(GalleryCloudSyncTagUtils.getColumnName(next.syncTagType), Long.valueOf(next.serverValue));
        }
        if (GalleryUtils.safeUpdate(uri, contentValues, getSyncTagSelection(), null) <= 0) {
            SyncLogger.e("SyncFromServer", "error update sync tag, %s : %d", Integer.valueOf(arrayList.get(0).syncTagType), Long.valueOf(arrayList.get(0).serverValue));
        }
        GalleryCloudSyncTagUtils.postUpdateSyncTag(this.mContext, contentValues);
    }

    public void updateSyncTag(GalleryCloudSyncTagUtils.SyncTagItem syncTagItem) {
        ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList = new ArrayList<>();
        arrayList.add(syncTagItem);
        updateSyncTag(arrayList);
    }

    public boolean supportSyncInfo(ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        return GalleryCloudSyncTagUtils.hasSyncInfo(arrayList.get(0).syncTagType);
    }

    public void appendSyncInfo(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        if (!supportSyncInfo(arrayList2)) {
            return;
        }
        GalleryCloudSyncTagUtils.SyncTagItem syncTagItem = arrayList2.get(0);
        String str = (String) GalleryUtils.safeQuery(CloudUtils.getLimitUri(GalleryCloudSyncTagUtils.getUri(syncTagItem.syncTagType), 1), new String[]{GalleryCloudSyncTagUtils.getSyncInfoColumnName(syncTagItem.syncTagType)}, getSyncTagSelection(), (String[]) null, (String) null, new GalleryUtils.QueryHandler<String>() { // from class: com.miui.gallery.cloud.SyncFromServer.3
            @Override // com.miui.gallery.util.GalleryUtils.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1712handle(Cursor cursor) {
                if (cursor != null && cursor.moveToNext()) {
                    return cursor.getString(0);
                }
                return null;
            }
        });
        if (str == null) {
            str = "";
        }
        arrayList.add(new BasicNameValuePair("syncExtraInfo", str));
    }

    public void updateSyncInfo(String str, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        GalleryCloudSyncTagUtils.SyncTagItem syncTagItem = arrayList.get(0);
        Uri uri = GalleryCloudSyncTagUtils.getUri(syncTagItem.syncTagType);
        String syncInfoColumnName = GalleryCloudSyncTagUtils.getSyncInfoColumnName(syncTagItem.syncTagType);
        ContentValues contentValues = new ContentValues();
        contentValues.put(syncInfoColumnName, str);
        GalleryUtils.safeUpdate(uri, contentValues, getSyncTagSelection(), null);
        GalleryCloudSyncTagUtils.postUpdateSyncTag(this.mContext, contentValues);
    }

    public void updateSyncInfo(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        String optString = jSONObject.optString("syncExtraInfo");
        if (TextUtils.isEmpty(optString)) {
            SyncLogger.w("SyncFromServer", "no syncinfo");
        } else if (!supportSyncInfo(arrayList)) {
            SyncLogger.w("SyncFromServer", "not support syncinfo");
        } else {
            updateSyncInfo(optString, arrayList);
        }
    }

    public final long getLargestTagInLocalDB() {
        Cursor cursor = null;
        try {
            Cursor query = this.mContext.getContentResolver().query(getBaseUri(), new String[]{" Max( " + getTagColumnName() + " ) "}, null, null, null);
            if (query != null && query.moveToNext()) {
                long j = query.getLong(0);
                query.close();
                return j;
            }
            long initSyncTagValue = GalleryCloudSyncTagUtils.getInitSyncTagValue(getSyncTagList().get(0).syncTagType);
            if (query != null) {
                query.close();
            }
            return initSyncTagValue;
        } catch (Throwable th) {
            if (0 != 0) {
                cursor.close();
            }
            throw th;
        }
    }

    public final JSONObject getItemsList(ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList, int i) throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        ArrayList<NameValuePair> arrayList2 = new ArrayList<>();
        if (arrayList != null) {
            Iterator<GalleryCloudSyncTagUtils.SyncTagItem> it = arrayList.iterator();
            while (it.hasNext()) {
                GalleryCloudSyncTagUtils.SyncTagItem next = it.next();
                if (next.shouldSync) {
                    if (TextUtils.isEmpty(GalleryCloudSyncTagUtils.getJsonTagInput(next.syncTagType))) {
                        SyncLogger.e("SyncFromServer", "get input tag is null, syncType:" + next.syncTagType);
                        return null;
                    }
                    arrayList2.add(new BasicNameValuePair(GalleryCloudSyncTagUtils.getJsonTagInput(next.syncTagType), Long.toString(next.currentValue)));
                }
            }
            appendSyncInfo(arrayList2, arrayList);
        }
        appendParams(arrayList2, arrayList);
        if (i > 0) {
            arrayList2.add(new BasicNameValuePair("limit", Long.toString(i)));
        }
        return CloudUtils.getFromXiaomi(getSyncUrl(), arrayList2, this.mAccount, this.mExtendedAuthToken, 0, false);
    }
}
