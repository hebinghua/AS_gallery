package com.miui.gallery.cloud.thread;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.cloud.GalleryMiCloudServerException;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RetryOperation;
import com.miui.gallery.cloud.ServerErrorCode;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.control.BatteryMonitor;
import com.miui.gallery.cloud.operation.create.CreateOwnerImage;
import com.miui.gallery.cloud.operation.create.CreateShareImage;
import com.miui.gallery.cloud.syncstate.SyncMonitor;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.thread.RequestCommandQueue;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.UploadStatusController;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class BackUploadTask extends BaseSyncLockTask<DBImage> {
    public long mStartTime;
    public int mUploadCount;

    public BackUploadTask(int i, int i2, int i3, int i4, RequestCommandQueue.OnItemChangedListener onItemChangedListener) {
        super(i, i2, i3, i4, onItemChangedListener);
    }

    public final SyncType getSyncType() {
        SyncType syncType = SyncStateManager.getInstance().getSyncType();
        if (syncType == SyncType.UNKNOW) {
            SyncLogger.e(this.TAG, "sync type shouldn't be UNKNOW");
            return SyncType.NORMAL;
        }
        return syncType;
    }

    public final long getSyncReason() {
        return SyncStateManager.getInstance().getSyncReason();
    }

    @Override // com.miui.gallery.cloud.thread.BaseTask
    public void onPreExecute() {
        this.mStartTime = System.currentTimeMillis();
        this.mUploadCount = 0;
        UploadStatusController.getInstance().startUpload();
        SyncMonitor.getInstance().onSyncStart(getSyncType(), getSyncReason());
        BatteryMonitor.getInstance().start();
        acquireLock();
    }

    @Override // com.miui.gallery.cloud.thread.BaseTask
    public void onPostExecute() {
        UploadStatusController.getInstance().endUpload();
        SyncMonitor.getInstance().onSyncEnd();
        BatteryMonitor.getInstance().end();
        releaseLock();
        statUpload();
    }

    public final void statUpload() {
        HashMap hashMap = new HashMap();
        hashMap.put("cost_time", String.valueOf(Math.round((((float) (System.currentTimeMillis() - this.mStartTime)) * 1.0f) / 1000.0f)));
        hashMap.put(MiStat.Param.COUNT, String.valueOf(this.mUploadCount));
        SamplingStatHelper.recordCountEvent("Sync", "sync_upload_time_total", hashMap);
    }

    @Override // com.miui.gallery.cloud.thread.BaseTask
    public GallerySyncResult<DBImage> onPerformSync() throws Throwable {
        try {
            return super.onPerformSync();
        } catch (GalleryMiCloudServerException e) {
            SyncLogger.e(this.TAG, e);
            if (ServerErrorCode.MiCloudServerExceptionHandler.handleMiCloudException(e.getCloudServerException())) {
                SyncLogger.e(this.TAG, "throw Cloud server exception status code %d", Integer.valueOf(e.getStatusCode()));
            } else {
                SyncLogger.d(this.TAG, "no retry");
            }
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        }
    }

    @Override // com.miui.gallery.cloud.thread.BaseTask
    public GallerySyncResult<DBImage> handle(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List<RequestCloudItem> list) {
        if (list.isEmpty()) {
            return new GallerySyncResult.Builder().setCode(GallerySyncCode.OK).build();
        }
        this.mUploadCount += list.size();
        return RetryOperation.doOperation(context, account, galleryExtendedAuthToken, list, list.get(0).dbCloud.isShareItem() ? new CreateShareImage(context) : new CreateOwnerImage(context));
    }
}
