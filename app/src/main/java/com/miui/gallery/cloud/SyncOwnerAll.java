package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/* loaded from: classes.dex */
public class SyncOwnerAll extends SyncOwnerCloud {
    public SyncOwnerAll(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        super(context, account, galleryExtendedAuthToken);
    }

    @Override // com.miui.gallery.cloud.SyncFromServer
    public String getSyncUrl() {
        return HostManager.SyncPull.getPullOwnerAllUrl();
    }

    @Override // com.miui.gallery.cloud.SyncOwnerCloud, com.miui.gallery.cloud.SyncCloudBase, com.miui.gallery.cloud.SyncFromServer
    public void appendParams(ArrayList<NameValuePair> arrayList, ArrayList<GalleryCloudSyncTagUtils.SyncTagItem> arrayList2) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        super.appendParams(arrayList, arrayList2);
        if (ApplicationHelper.isSecretAlbumFeatureOpen()) {
            arrayList.add(new BasicNameValuePair("returnHiddenData", String.valueOf(true)));
            arrayList.add(new BasicNameValuePair("returnHiddenType", "all"));
        }
        arrayList.add(new BasicNameValuePair("returnSystemAlbum", String.valueOf(true)));
    }
}
