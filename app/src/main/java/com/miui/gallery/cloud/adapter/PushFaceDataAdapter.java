package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.peopleface.SyncFaceFromLocal;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.SyncLogger;

/* loaded from: classes.dex */
public class PushFaceDataAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 128L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return true;
    }

    public PushFaceDataAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception {
        if (!AIAlbumStatusHelper.isFaceAlbumEnabled()) {
            SyncLogger.w(this.TAG, "the feature of face isn't enabled");
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        }
        new SyncFaceFromLocal(getContext(), account, galleryExtendedAuthToken).sync();
        return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
    }
}
