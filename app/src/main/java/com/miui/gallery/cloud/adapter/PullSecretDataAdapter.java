package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.SyncOwnerPrivate;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PullSecretDataAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 2L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return false;
    }

    public PullSecretDataAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception {
        if (!ApplicationHelper.isSecretAlbumFeatureOpen()) {
            SyncLogger.w(this.TAG, "the feature of secret album isn't enabled");
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        } else if (Preference.getSyncFetchAllPrivateData() && Preference.getSyncFetchPrivateVideo()) {
            SyncLogger.w(this.TAG, "has fetched secret data, ignore");
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        } else {
            GallerySyncResult<JSONObject> sync = new SyncOwnerPrivate(getContext(), account, galleryExtendedAuthToken).sync();
            if (sync.code == GallerySyncCode.OK) {
                Preference.setSyncFetchAllPrivateData();
                Preference.setSyncFetchPrivateVideo();
            } else {
                String str = this.TAG;
                SyncLogger.e(str, "result code error" + sync.code);
            }
            SyncLogger.d(this.TAG, "pull result %s", sync);
            return sync;
        }
    }
}
