package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager;

/* loaded from: classes2.dex */
public class CancelRunningTask extends SemiScanTask {
    public CancelRunningTask(Context context) {
        super(context, null);
    }

    @Override // com.miui.gallery.scanner.core.task.semi.SemiScanTask
    public boolean beforeRun(BaseScanTaskManager baseScanTaskManager) {
        baseScanTaskManager.cancelAll();
        return true;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof CancelRunningTask;
    }
}
