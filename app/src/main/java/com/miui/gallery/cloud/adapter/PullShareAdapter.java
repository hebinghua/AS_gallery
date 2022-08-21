package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.AlbumShareOperations;
import com.miui.gallery.cloud.SyncSharerAll;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PullShareAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 16L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return true;
    }

    public PullShareAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception {
        if (!ApplicationHelper.supportShare()) {
            SyncLogger.w(this.TAG, "the feature of share album isn't enabled");
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        }
        GallerySyncResult<JSONObject> sync = new SyncSharerAll(getContext(), account, galleryExtendedAuthToken, bundle).sync();
        SyncLogger.d(this.TAG, "pull result %s", sync);
        AlbumShareOperations.syncAllUserInfoFromNetwork();
        return sync;
    }
}
