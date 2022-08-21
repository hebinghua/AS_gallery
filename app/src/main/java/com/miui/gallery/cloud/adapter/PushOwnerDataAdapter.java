package com.miui.gallery.cloud.adapter;

import android.accounts.Account;
import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.cloud.SyncOwnerDataFromLocal;
import com.miui.gallery.cloud.SyncOwnerSubUbiFromLocal;
import com.miui.gallery.cloud.base.AbstractSyncAdapter;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;

/* loaded from: classes.dex */
public class PushOwnerDataAdapter extends AbstractSyncAdapter {
    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public long getBindingReason() {
        return 32L;
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public boolean isAsynchronous() {
        return false;
    }

    public PushOwnerDataAdapter(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.base.AbstractSyncAdapter
    public GallerySyncResult onPerformSync(Account account, Bundle bundle, GalleryExtendedAuthToken galleryExtendedAuthToken) throws Exception {
        boolean z = bundle.getBoolean("sync_no_delay", false);
        new SyncOwnerDataFromLocal(getContext(), account, galleryExtendedAuthToken, z).sync();
        new SyncOwnerSubUbiFromLocal(getContext(), account, galleryExtendedAuthToken, z).sync();
        return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
    }
}
