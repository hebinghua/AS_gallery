package com.miui.gallery.scanner.core.task.eventual;

import android.content.ContentValues;
import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask;
import com.miui.gallery.scanner.core.task.state.ITaskState;
import com.miui.gallery.scanner.core.task.state.TaskStateEnum;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

/* loaded from: classes2.dex */
public class ScanDirectoryTask extends EventualScanTask {
    public OwnerAlbumEntry mAlbum;
    public boolean mIsProducing;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public ScanDirectoryTask(Context context, ScanTaskConfig scanTaskConfig, Path path, OwnerAlbumEntry ownerAlbumEntry) {
        super(context, scanTaskConfig, path);
        this.mIsProducing = true;
        ScanContracts$StatusReason scanContracts$StatusReason = ScanContracts$StatusReason.DEFAULT;
        gotoRunning(scanContracts$StatusReason);
        OwnerAlbumEntry queryAndUpdateAlbum = ownerAlbumEntry == null ? MediaScannerHelper.queryAndUpdateAlbum(context, path.toString(), new ContentValues()) : ownerAlbumEntry;
        this.mAlbum = queryAndUpdateAlbum;
        if (queryAndUpdateAlbum != null) {
            queryAndUpdateAlbum.updateDateModified(this.mContext, queryAndUpdateAlbum.mDateModified, 1L, StorageUtils.isInPrimaryStorage(this.mPath.toString()), false);
        }
        gotoSelfDone(scanContracts$StatusReason);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        return ScanResult.success(ScanContracts$ScanResultReason.DEFAULT).build();
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void onChildNotified(BaseScanTask baseScanTask) {
        ScanResult scanResult;
        super.onChildNotified(baseScanTask);
        if (this.mAlbum != null || !(baseScanTask instanceof ScanSingleFileTask) || baseScanTask.getState() != TaskStateEnum.DONE || (scanResult = ((ScanSingleFileTask) baseScanTask).getScanResult()) == null || scanResult.getAlbumEntry() == null) {
            return;
        }
        this.mAlbum = (OwnerAlbumEntry) scanResult.getAlbumEntry();
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void doOnAllChildrenTaskDone() {
        if (this.mAlbum == null) {
            OwnerAlbumEntry queryAndUpdateAlbum = MediaScannerHelper.queryAndUpdateAlbum(this.mContext, this.mPath.toString(), null);
            this.mAlbum = queryAndUpdateAlbum;
            if (queryAndUpdateAlbum == null) {
                return;
            }
        }
        String[] absolutePath = StorageUtils.getAbsolutePath(this.mContext, StorageUtils.ensureCommonRelativePath(StorageUtils.getRelativePath(this.mContext, this.mPath.toString())));
        if (absolutePath == null || absolutePath.length == 0) {
            return;
        }
        long j = 0;
        long j2 = 0;
        for (String str : absolutePath) {
            long lastModified = new File(str).lastModified();
            j = Math.max(j, lastModified);
            if (this.mPath.toString().equalsIgnoreCase(str)) {
                j2 = lastModified;
            }
        }
        this.mAlbum.updateDateModified(this.mContext, j, j2, StorageUtils.isInPrimaryStorage(this.mPath.toString()), !StorageUtils.hasExternalSDCard(this.mContext));
        this.mAlbum.updatePublicMediaStatus(this.mContext);
    }

    public OwnerAlbumEntry getAlbumEntry() {
        return this.mAlbum;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public ITaskState getState() {
        if (this.mIsProducing) {
            return TaskStateEnum.RUNNING;
        }
        return super.getState();
    }

    public void setIsProducing(boolean z) {
        this.mIsProducing = z;
        if (!z) {
            onProduceDone();
        }
    }

    public boolean isProducing() {
        return this.mIsProducing;
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return this.mHashCode;
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof ScanDirectoryTask) && super.equals(obj);
    }
}
