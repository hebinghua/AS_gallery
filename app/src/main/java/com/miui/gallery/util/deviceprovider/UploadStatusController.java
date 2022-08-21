package com.miui.gallery.util.deviceprovider;

import android.content.ContentValues;
import android.os.AsyncTask;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.RequestItemBase;
import com.miui.gallery.cloud.UpDownloadManager;
import com.miui.gallery.cloud.thread.RequestCommandQueue;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.sdk.uploadstatus.SyncProxy;
import com.miui.gallery.sdk.uploadstatus.UploadStatusItem;
import com.miui.gallery.util.SyncLogger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public class UploadStatusController implements RequestCommandQueue.OnItemChangedListener {
    public static UploadStatusController sThis;
    public volatile boolean mHasPendingItem;
    public final List<UploadStatusItem> mUploadingItems = new CopyOnWriteArrayList();
    public final Object mLock = new Object();
    public int mCount = 0;

    public final void refreshUploadStatus() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("is_upload", Boolean.valueOf(this.mCount > 0));
        contentValues.put("has_pending_item", Boolean.valueOf(this.mHasPendingItem));
        GalleryApp.sGetAndroidContext().getContentResolver().update(GalleryCloudProvider.UPLOAD_STATE_URI, contentValues, null, null);
    }

    public static synchronized UploadStatusController getInstance() {
        UploadStatusController uploadStatusController;
        synchronized (UploadStatusController.class) {
            if (sThis == null) {
                sThis = new UploadStatusController();
                UpDownloadManager.instance(4).addOnItemChangedListener(sThis);
                UpDownloadManager.instance(2).addOnItemChangedListener(sThis);
            }
            uploadStatusController = sThis;
        }
        return uploadStatusController;
    }

    public boolean isUploading() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mCount > 0;
        }
        return z;
    }

    public boolean isPending() {
        return this.mHasPendingItem;
    }

    public void startUpload() {
        synchronized (this.mLock) {
            int i = this.mCount + 1;
            this.mCount = i;
            if (i == 1) {
                refreshUploadStatus();
                SyncLogger.d("UploadStatusController", "start upload");
            }
        }
    }

    public void endUpload() {
        synchronized (this.mLock) {
            int i = this.mCount - 1;
            this.mCount = i;
            if (i == 0) {
                refreshUploadStatus();
                SyncLogger.d("UploadStatusController", "end upload");
            }
        }
    }

    public synchronized void start(RequestItemBase requestItemBase) {
        if (!accept(requestItemBase)) {
            return;
        }
        UploadStatusItem uploadStatusItem = new UploadStatusItem((RequestCloudItem) requestItemBase);
        this.mUploadingItems.remove(uploadStatusItem);
        this.mUploadingItems.add(uploadStatusItem);
        SyncProxy.getInstance().getUploadStatusProxy().onUploadStatusChanged(uploadStatusItem);
    }

    public synchronized void end(RequestItemBase requestItemBase) {
        if (!accept(requestItemBase)) {
            return;
        }
        UploadStatusItem uploadStatusItem = new UploadStatusItem((RequestCloudItem) requestItemBase);
        this.mUploadingItems.remove(uploadStatusItem);
        SyncProxy.getInstance().getUploadStatusProxy().onUploadStatusChanged(uploadStatusItem);
    }

    public final boolean accept(RequestItemBase requestItemBase) {
        int i = requestItemBase.priority;
        return i == 4 || i == 2 || i == 5 || i == 3;
    }

    public UploadStatusItem getUploadStatus(UploadStatusItem uploadStatusItem) {
        for (UploadStatusItem uploadStatusItem2 : this.mUploadingItems) {
            if (uploadStatusItem2.equals(uploadStatusItem)) {
                return uploadStatusItem2;
            }
        }
        return null;
    }

    @Override // com.miui.gallery.cloud.thread.RequestCommandQueue.OnItemChangedListener
    public void onAddItem(RequestCloudItem requestCloudItem) {
        checkPendingStatus();
    }

    @Override // com.miui.gallery.cloud.thread.RequestCommandQueue.OnItemChangedListener
    public void onRemoveItem(RequestCloudItem requestCloudItem) {
        checkPendingStatus();
    }

    public final void checkPendingStatus() {
        new AsyncTask<Void, Void, Void>() { // from class: com.miui.gallery.util.deviceprovider.UploadStatusController.1
            @Override // android.os.AsyncTask
            public Void doInBackground(Void... voidArr) {
                boolean z = UploadStatusController.this.mHasPendingItem;
                UploadStatusController.this.mHasPendingItem = UpDownloadManager.instance(4).hasDelayedItem() || UpDownloadManager.instance(2).hasDelayedItem();
                if (z != UploadStatusController.this.mHasPendingItem) {
                    UploadStatusController.this.refreshUploadStatus();
                    return null;
                }
                return null;
            }
        }.execute(new Void[0]);
    }
}
