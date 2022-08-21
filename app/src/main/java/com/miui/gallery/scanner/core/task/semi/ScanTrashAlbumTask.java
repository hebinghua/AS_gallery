package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.ScanTrashAlbumTaskConverter;

/* loaded from: classes2.dex */
public class ScanTrashAlbumTask extends SemiScanTask {
    public ScanTrashAlbumTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mSemiScanTaskConverter = new ScanTrashAlbumTaskConverter(context, this);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof ScanTrashAlbumTask;
    }

    public String toString() {
        return String.format("--%s", getClass().getSimpleName());
    }
}
