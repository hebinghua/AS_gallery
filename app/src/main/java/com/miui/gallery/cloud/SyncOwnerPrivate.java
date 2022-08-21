package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/* loaded from: classes.dex */
public final class SyncOwnerPrivate extends SyncOwnerCloud {
    public String mSyncIgnoreTag;
    public long mSyncTag;

    public SyncOwnerPrivate(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
        this.mSyncTag = 0L;
        this.mSyncIgnoreTag = String.valueOf(0);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncUrl() {
        return HostManager.SyncPull.getPullOwnerPrivateUrl();
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void appendSyncInfo(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        arrayList.add(new BasicNameValuePair("syncIgnoreTag", this.mSyncIgnoreTag));
    }

    @Override // com.miui.gallery.cloud.SyncOwnerCloud, com.miui.gallery.cloud.SyncCloudBase, com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        arrayList.add(new BasicNameValuePair("returnHiddenType", "all"));
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> getCurrentSyncTag() {
        ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> syncTagList = getSyncTagList();
        if (syncTagList != null && syncTagList.size() != 0) {
            syncTagList.get(0).currentValue = this.mSyncTag;
        }
        return syncTagList;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void updateSyncInfo(JSONObject jSONObject, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        String optString = jSONObject.optString("syncIgnoreTag");
        if (TextUtils.isEmpty(optString)) {
            return;
        }
        this.mSyncIgnoreTag = optString;
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public void updateSyncTag(ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return;
        }
        this.mSyncTag = arrayList.get(0).serverValue;
    }
}
