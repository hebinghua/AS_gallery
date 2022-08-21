package com.miui.gallery.cloud.operation;

import android.content.Context;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.util.SyncLogger;

/* loaded from: classes.dex */
public class RecoveryCloudItem extends PurgeRecoveryCloudItemBase {
    public RecoveryCloudItem(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.cloud.operation.PurgeRecoveryCloudItemBase, com.miui.gallery.cloud.RequestOperationBase
    public GallerySyncCode onPreRequest(RequestItemBase requestItemBase) {
        if (SyncConditionManager.checkCloudSpace(this.mContext) == 2) {
            SyncLogger.e(getTag(), "check cloud space exit");
            return GallerySyncCode.NOT_RETRY_ERROR;
        }
        return super.onPreRequest(requestItemBase);
    }

    @Override // com.miui.gallery.cloud.operation.PurgeRecoveryCloudItemBase
    public String getRequestUrl() {
        return HostManager.TrashBin.getRecoveryUrl();
    }

    @Override // com.miui.gallery.cloud.RequestOperationBase
    public SpaceFullHandler.SpaceFullListener getSpaceFullListener() {
        return SpaceFullHandler.getOwnerSpaceFullListener();
    }
}
