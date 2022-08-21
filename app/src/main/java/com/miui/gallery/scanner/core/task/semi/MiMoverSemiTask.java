package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.provider.ContentProviderBatchOperator;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.bulkoperator.BulkInserter;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.MiMoverSemiTaskConverter;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class MiMoverSemiTask extends SemiScanTask {
    public List<String> mPaths;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return this == obj;
    }

    public MiMoverSemiTask(Context context, List<String> list, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mPaths = Collections.unmodifiableList(list);
        this.mConfig = new ScanTaskConfig.Builder().cloneFrom(this.mConfig).setInserter(new BulkInserter(GalleryContract.Cloud.CLOUD_URI.buildUpon().appendQueryParameter("insert_without_dedup", String.valueOf(true)).appendQueryParameter("bulk_notify_media", String.valueOf(true)).build(), this.mPaths.size())).setBatchOperator(new ContentProviderBatchOperator("com.miui.gallery.cloud.provider", this.mPaths.size())).build();
        this.mSemiScanTaskConverter = new MiMoverSemiTaskConverter(this.mContext, this);
    }

    public List<String> getPaths() {
        return this.mPaths;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void doOnAllChildrenTaskDone() {
        DefaultLogger.d("MiMoverSemiTask", "try flush, expected size: [%d]", Integer.valueOf(this.mPaths.size()));
        if (this.mConfig.getInserter() != null) {
            this.mConfig.getInserter().flush(this.mContext);
        }
        if (this.mConfig.getBatchOperator() != null) {
            this.mConfig.getBatchOperator().apply(this.mContext);
        }
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        List<String> list = this.mPaths;
        return ((list != null ? 527 + Objects.hash(list) : 17) * 31) + super.hashCode();
    }
}
