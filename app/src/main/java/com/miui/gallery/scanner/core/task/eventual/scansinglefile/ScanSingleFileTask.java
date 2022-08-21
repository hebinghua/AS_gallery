package com.miui.gallery.scanner.core.task.eventual.scansinglefile;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.eventual.ScanDirectoryTask;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.os.Rom;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ScanSingleFileTask extends EventualScanTask {
    public OwnerAlbumEntry mAlbumEntry;
    public ExtraWorker mExtraWorker;
    public long mLastModified;
    public long mPriority;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    @Deprecated
    public static ScanSingleFileTask create(Context context, Path path, ScanTaskConfig scanTaskConfig, long j) {
        ScanTaskConfig.Builder cloneFrom = new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig);
        if (path.toFile().length() > 41943040) {
            cloneFrom.setBulkNotify(false).setInserter(null).setBatchOperator(null).setDeleter(null);
        }
        return new ScanSingleFileTask(context, path, cloneFrom.build(), j);
    }

    public ScanSingleFileTask(Context context, Path path, ScanTaskConfig scanTaskConfig, long j) {
        super(context, scanTaskConfig, path);
        this.mExtraWorker = new ExtraWorker(context, path, scanTaskConfig);
        this.mLastModified = getLastModified();
        this.mAlbumEntry = getAlbumEntry();
        this.mPriority = j;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean checkBeforeRun() {
        List<String> list = (List) ScanCache.getInstance().get("key_migrate_affected_paths");
        if (list == null) {
            return true;
        }
        for (String str : list) {
            if (BaseFileUtils.contains(str, this.mPath.toString())) {
                return false;
            }
        }
        return super.checkBeforeRun();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        return getConfig().getScanner().scanFile(this.mContext, this.mPath, this.mAlbumEntry, this.mConfig);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public void dealWithResult(ScanResult scanResult) {
        super.dealWithResult(scanResult);
        if (ScanResult.Result.SUCCESS == scanResult.getResult()) {
            if ((scanResult.getMediaId() == -1 || !(scanResult.getReasonCode() == ScanContracts$ScanResultReason.DEFAULT || scanResult.getReasonCode() == ScanContracts$ScanResultReason.UNSYNCED_MEDIA_UPDATED)) && scanResult.getReasonCode() != ScanContracts$ScanResultReason.BULK_INSERT) {
                return;
            }
            this.mExtraWorker.work(scanResult);
        }
    }

    public OwnerAlbumEntry getAlbumEntry() {
        BaseScanTask parentTask = getParentTask();
        if (!(parentTask instanceof ScanDirectoryTask)) {
            return null;
        }
        return ((ScanDirectoryTask) parentTask).getAlbumEntry();
    }

    public long getLastModified() {
        return this.mPath.toFile().lastModified();
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return this.mHashCode;
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof ScanSingleFileTask) && this.mLastModified == ((ScanSingleFileTask) obj).mLastModified && super.equals(obj);
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask, java.lang.Comparable
    public int compareTo(EventualScanTask eventualScanTask) {
        if (eventualScanTask instanceof ScanSingleFileTask) {
            ScanSingleFileTask scanSingleFileTask = (ScanSingleFileTask) eventualScanTask;
            int compare = Long.compare(this.mPriority, scanSingleFileTask.mPriority);
            return compare != 0 ? compare : Long.compare(this.mLastModified, scanSingleFileTask.mLastModified);
        }
        return super.compareTo(eventualScanTask);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void printTaskLifeRecord() {
        if (!Rom.IS_DEBUGGABLE) {
            super.printTaskLifeRecord();
            return;
        }
        StringBuilder sb = new StringBuilder();
        Locale locale = Locale.US;
        sb.append(String.format(locale, " \nTask Life Record Msg:\nState: [%s]\nName: [%s]\nCreate time: [%d]\nWaiting cost: [%d] ms\nSelf cost: [%d] ms\nExtra work/Waiting children cost: [%d] ms", getState().toString(), toString(), Long.valueOf(this.mCreateTime), Long.valueOf(this.mStartTime - this.mCreateTime), Long.valueOf(this.mSelfDoneTime - this.mStartTime), Long.valueOf(this.mDoneTime - this.mSelfDoneTime)));
        sb.append(String.format(locale, "\nConfig scene code: [%d]", Integer.valueOf(getConfig().getSceneCode())));
        BaseScanTask baseScanTask = this;
        while (baseScanTask.getParentTask() != null) {
            baseScanTask = baseScanTask.getParentTask();
        }
        Locale locale2 = Locale.US;
        sb.append(String.format(locale2, "\nRoot task :[%s]\nRoot task create time  : [%d]", baseScanTask.toString(), Long.valueOf(baseScanTask.getCreateTime())));
        sb.append(String.format(locale2, "\nFile last modified time: [%d]", Long.valueOf(this.mPath.toFile().lastModified())));
        DefaultLogger.fd("ScanSingleFileTask", sb.toString());
    }
}
