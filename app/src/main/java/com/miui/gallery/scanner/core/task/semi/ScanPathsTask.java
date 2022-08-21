package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import android.net.Uri;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.ContentProviderBatchOperator;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.bulkoperator.BulkInserter;
import com.miui.gallery.scanner.core.bulkoperator.CloudMediaBulkDeleter;
import com.miui.gallery.scanner.core.bulkoperator.InsertToRecentBehavior;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.scanpaths.ScanPathsTaskConverter;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ExtraTextUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class ScanPathsTask extends SemiScanTask {
    public final List<String> mPaths;

    public static ScanPathsTask create(Context context, List<String> list, ScanTaskConfig scanTaskConfig) {
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        return new ScanPathsTask(context, list, scanTaskConfig);
    }

    public ScanPathsTask(Context context, List<String> list, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mPaths = Collections.unmodifiableList(list);
        ScanTaskConfig build = new ScanTaskConfig.Builder().cloneFrom(this.mConfig).setDeleteRecords(Collections.synchronizedList(new LinkedList())).build();
        this.mConfig = build;
        if (build.isBulkNotify()) {
            ScanTaskConfig.Builder cloneFrom = new ScanTaskConfig.Builder().cloneFrom(this.mConfig);
            Uri uri = GalleryContract.Cloud.CLOUD_URI;
            this.mConfig = cloneFrom.setInserter(new BulkInserter(uri.buildUpon().appendQueryParameter("URI_PARAM_REQUEST_SYNC", String.valueOf(this.mConfig.needTriggerSync())).appendQueryParameter("bulk_notify_media", String.valueOf(true)).build(), 20, new InsertToRecentBehavior(20))).setDeleter(new CloudMediaBulkDeleter(uri, j.c)).setBatchOperator(new ContentProviderBatchOperator("com.miui.gallery.cloud.provider")).build();
        }
        this.mSemiScanTaskConverter = new ScanPathsTaskConverter(this.mContext, this);
    }

    public List<String> getPaths() {
        return this.mPaths;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void doOnAllChildrenTaskDone() {
        if (this.mConfig.getSceneCode() == 18) {
            GalleryPreferences.MediaScanner.setEverForceScanAllAlbumsForFormatExpansion();
        }
        if (this.mConfig.getDeleter() != null) {
            this.mConfig.getDeleter().flush(this.mContext);
        }
        if (this.mConfig.getBatchOperator() != null) {
            this.mConfig.getBatchOperator().apply(this.mContext);
        }
        if (BaseMiscUtil.isValid(this.mConfig.getDeleteRecords())) {
            DeleteRecorder.getInstance().record((DeleteRecord[]) this.mConfig.getDeleteRecords().toArray(new DeleteRecord[0]));
        }
        if (this.mConfig.isBulkNotify()) {
            if (this.mConfig.getInserter() != null) {
                this.mConfig.getInserter().flush(this.mContext);
            }
            if (this.mConfig.needTriggerSync()) {
                SyncUtil.requestSync(this.mContext);
            }
        }
        trackScanTimeCost();
    }

    public final void trackScanTimeCost() {
        if (getPaths() != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.14.0.1.13759");
            hashMap.put(MiStat.Param.COUNT, Integer.valueOf(getPaths().size()));
            TrackController.trackTimeMonitor(hashMap);
        }
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof ScanPathsTask) && this.mPaths.equals(((ScanPathsTask) obj).mPaths) && super.equals(obj);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        List<String> list = this.mPaths;
        return ((list != null ? 527 + list.hashCode() : 17) * 31) + super.hashCode();
    }

    public String toString() {
        return String.format("--%s %s", getClass().getSimpleName(), ExtraTextUtils.joinForLogPrint(", ", this.mPaths));
    }
}
