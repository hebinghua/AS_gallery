package com.miui.gallery.cloud.operation;

import android.content.Context;
import com.miui.gallery.cloud.HostManager;

/* loaded from: classes.dex */
public class PurgeCloudItem extends PurgeRecoveryCloudItemBase {
    public PurgeCloudItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.PurgeRecoveryCloudItemBase
    public String getRequestUrl() {
        return HostManager.TrashBin.getPurgeUrl();
    }
}
