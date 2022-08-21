package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.RetryRequestHelper;
import com.miui.gallery.cloud.base.SyncTask;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deprecated.Preference;
import com.xiaomi.stat.MiStat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class FetchSyncExtraInfoTask {
    public Account mAccount;
    public Context mContext;
    public GalleryExtendedAuthToken mExtendedAuthToken;

    public FetchSyncExtraInfoTask(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        this.mContext = context;
        this.mAccount = account;
        this.mExtendedAuthToken = galleryExtendedAuthToken;
    }

    public final ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getCurrentSyncTag() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(1));
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(4));
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(3));
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(5));
        arrayList.add(new GalleryCloudSyncTagUtils.SyncTagItem(2));
        return SyncFromServer.getCurrentSyncTag(arrayList, GalleryCloudSyncTagUtils.getAccountSelections(this.mAccount));
    }

    public final String getUrl() {
        return HostManager.Upgrade.getUpgradeUrl();
    }

    public final JSONObject callAPI(ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        ArrayList arrayList2 = new ArrayList();
        Iterator<GalleryCloudSyncTagUtils.SyncTagItem> it = arrayList.iterator();
        while (it.hasNext()) {
            GalleryCloudSyncTagUtils.SyncTagItem next = it.next();
            if (TextUtils.isEmpty(GalleryCloudSyncTagUtils.getJsonTagInput(next.syncTagType))) {
                SyncLogger.e("FetchSyncExtraInfoTask", "get input tag is null, syncType:" + next.syncTagType);
                return null;
            }
            arrayList2.add(new BasicNameValuePair(GalleryCloudSyncTagUtils.getJsonTagInput(next.syncTagType), Long.toString(next.currentValue)));
        }
        JSONObject fromXiaomi = CloudUtils.getFromXiaomi(getUrl(), arrayList2, this.mAccount, this.mExtendedAuthToken, 0, false);
        SyncLogger.d("FetchSyncExtraInfoTask", "allJson=" + fromXiaomi);
        return fromXiaomi;
    }

    public final void handleResult(JSONObject jSONObject) throws JSONException {
        JSONObject jSONObject2 = jSONObject.getJSONObject("data");
        String string = jSONObject2.getString("full");
        String string2 = jSONObject2.getString(MiStat.Event.SHARE);
        String string3 = jSONObject2.getString("oneshare");
        ContentValues contentValues = new ContentValues();
        contentValues.put("syncInfo", string);
        contentValues.put("shareSyncInfo", string2);
        GalleryUtils.safeUpdate(GalleryCloudUtils.CLOUD_SETTING_URI, contentValues, GalleryCloudSyncTagUtils.getAccountSelections(this.mAccount), null);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("syncInfo", string3);
        GalleryUtils.safeUpdate(GalleryCloudUtils.SHARE_ALBUM_URI, contentValues2, null, null);
        Preference.setSyncFetchSyncExtraInfoFromV2ToV3(false);
    }

    public final void run() throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        SyncLogger.d("FetchSyncExtraInfoTask", "start");
        long currentTimeMillis = System.currentTimeMillis();
        final ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> currentSyncTag = getCurrentSyncTag();
        GallerySyncResult retryTask = RetryRequestHelper.retryTask(new SyncTask<JSONObject>() { // from class: com.miui.gallery.cloud.FetchSyncExtraInfoTask.1
            @Override // com.miui.gallery.cloud.base.SyncTask
            public String getIdentifier() {
                return FetchSyncExtraInfoTask.this.getUrl();
            }

            @Override // com.miui.gallery.cloud.base.SyncTask
            public GallerySyncResult<JSONObject> run() throws Exception {
                return CheckResult.checkXMResultCode(FetchSyncExtraInfoTask.this.callAPI(currentSyncTag), null, null);
            }
        });
        JSONObject jSONObject = (JSONObject) retryTask.data;
        if (retryTask.code != GallerySyncCode.OK) {
            SyncLogger.e("FetchSyncExtraInfoTask", "sync from server error:" + jSONObject);
        } else {
            handleResult(jSONObject);
        }
        SyncLogger.d("FetchSyncExtraInfoTask", "finish: " + (System.currentTimeMillis() - currentTimeMillis));
    }
}
