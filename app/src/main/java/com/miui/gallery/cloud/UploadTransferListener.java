package com.miui.gallery.cloud;

import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.opensdk.file.model.MiCloudFileListener;

/* loaded from: classes.dex */
public class UploadTransferListener implements MiCloudFileListener {
    public RequestCloudItem mRequestItem;

    public UploadTransferListener(RequestCloudItem requestCloudItem) {
        this.mRequestItem = requestCloudItem;
    }

    @Override // com.xiaomi.opensdk.file.model.MiCloudFileListener
    public void onDataSended(long j, long j2) {
        if (SyncConditionManager.check(this.mRequestItem.priority) != 0) {
            DefaultLogger.i("UploadTransferListener", "net work is changed, interrupt this thread, priority=" + this.mRequestItem.priority + ", item:" + this.mRequestItem.getIdentity());
            Thread.currentThread().interrupt();
        }
    }

    @Override // com.xiaomi.opensdk.file.model.MiCloudFileListener
    public void onDataReceived(long j, long j2) {
        DefaultLogger.v("UploadTransferListener", "upload should not received, pos:" + j + ", total:" + j2 + ", item:" + this.mRequestItem.getIdentity());
    }
}
