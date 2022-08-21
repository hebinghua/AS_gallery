package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.ScanShareAlbumsTaskConverter;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes2.dex */
public class ScanShareAlbumsTask extends SemiScanTask {
    public ScanShareAlbumsTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mSemiScanTaskConverter = new ScanShareAlbumsTaskConverter(context, this);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void doOnAllChildrenTaskDone() {
        Preference.sSetShareImageScanned();
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof ScanShareAlbumsTask;
    }

    public String toString() {
        return String.format("--%s", getClass().getSimpleName());
    }
}
