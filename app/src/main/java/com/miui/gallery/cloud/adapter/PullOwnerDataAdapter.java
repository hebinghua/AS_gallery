package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.SyncOwnerAlbumFromServer;
import com.miui.gallery.cloud.SyncOwnerAll;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.util.SyncLogger;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class PullOwnerDataAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 1L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return false;
    }

    public PullOwnerDataAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception {
        GallerySyncResult<JSONObject> sync = new SyncOwnerAlbumFromServer(getContext(), account, galleryExtendedAuthToken).sync();
        GallerySyncCode gallerySyncCode = sync.code;
        GallerySyncCode gallerySyncCode2 = GallerySyncCode.OK;
        if (gallerySyncCode != gallerySyncCode2) {
            SyncLogger.e(this.TAG, "pull owner album error, return %s", sync);
            return sync;
        }
        GallerySyncResult<JSONObject> sync2 = new SyncOwnerAll(getContext(), account, galleryExtendedAuthToken).sync();
        if (sync2.code != gallerySyncCode2) {
            SyncLogger.e(this.TAG, "pull owner cloud error, return %s", sync2);
        }
        return sync2;
    }
}
