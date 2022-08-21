package com.miui.gallery.scanner.core.task.eventual;

import android.content.Context;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public class EventualParallelProcessingImageScanTask extends ScanSingleFileTask {
    @Override // com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask
    public OwnerAlbumEntry getAlbumEntry() {
        return null;
    }

    public EventualParallelProcessingImageScanTask(Context context, Path path, ScanTaskConfig scanTaskConfig) {
        super(context, path, scanTaskConfig, 8L);
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask
    public long getLastModified() {
        return System.currentTimeMillis();
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask, com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof EventualParallelProcessingImageScanTask) && super.equals(obj);
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask, com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask, java.lang.Comparable
    public int compareTo(EventualScanTask eventualScanTask) {
        if (eventualScanTask instanceof EventualParallelProcessingImageScanTask) {
            return Long.compare(this.mLastModified, ((EventualParallelProcessingImageScanTask) eventualScanTask).mLastModified);
        }
        return 1;
    }
}
