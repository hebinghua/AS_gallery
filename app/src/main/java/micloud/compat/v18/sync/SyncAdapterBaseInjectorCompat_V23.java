package micloud.compat.v18.sync;

import android.content.SyncResult;

/* loaded from: classes3.dex */
public class SyncAdapterBaseInjectorCompat_V23 extends SyncAdapterBaseInjectorCompat_Base {
    @Override // micloud.compat.v18.sync.SyncAdapterBaseInjectorCompat_Base, micloud.compat.v18.sync.ISyncAdapterBaseInjectorCompat
    public void setResultByGDPRStatus(boolean z, SyncResult syncResult) {
        if (!z) {
            syncResult.stats.numAuthExceptions++;
        }
    }
}
