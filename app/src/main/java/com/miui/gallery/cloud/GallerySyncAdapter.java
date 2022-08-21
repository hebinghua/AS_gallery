package com.miui.gallery.cloud;

import android.content.Context;
import android.os.Bundle;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.util.SyncLogger;
import com.xiaomi.micloudsdk.exception.CloudServerException;
import com.xiaomi.micloudsdk.exception.SyncLocalException;
import com.xiaomi.micloudsdk.sync.SyncAdapterBase;

/* loaded from: classes.dex */
public class GallerySyncAdapter extends SyncAdapterBase {
    public GallerySyncAdapterImpl mImpl;

    public GallerySyncAdapter(Context context, boolean z) {
        super(context, z, "micgallery");
        this.mImpl = null;
        this.mImpl = new GallerySyncAdapterImpl(this.mContext);
    }

    @Override // com.xiaomi.micloudsdk.sync.SyncAdapterBase
    public void onPerformMiCloudSync(Bundle bundle) throws CloudServerException, SyncLocalException {
        if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            SyncLogger.d("GallerySyncAdapter", "gallery cta can't connect network");
            throw new SyncLocalException(1000);
        }
        try {
            this.mImpl.onPerformMiCloudSync(bundle, this.mAccount, this.mSyncResult, new GalleryExtendedAuthToken(this.mExtToken));
        } catch (GalleryMiCloudServerException e) {
            throw ((CloudServerException) e.getCloudServerException());
        }
    }

    @Override // android.content.AbstractThreadedSyncAdapter
    public void onSyncCanceled() {
        SyncLogger.fd("GallerySyncAdapter", "on sync canceled");
        this.mImpl.onSyncCanceled();
        super.onSyncCanceled();
    }
}
