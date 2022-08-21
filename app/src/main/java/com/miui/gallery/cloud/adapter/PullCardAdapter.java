package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.card.SyncCardFromServer;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;

/* loaded from: classes.dex */
public class PullCardAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 4L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return false;
    }

    public PullCardAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        if (!ApplicationHelper.isStoryAlbumFeatureOpen()) {
            SyncLogger.w(this.TAG, "the feature of story album isn't enabled");
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        }
        GallerySyncResult sync = new SyncCardFromServer(account).sync();
        SyncLogger.d(this.TAG, "pull result %s", sync);
        return sync;
    }
}
