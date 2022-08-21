package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.semi.ScanTrashAlbumTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;

/* loaded from: classes2.dex */
public class TrashAlbumScanner implements IScanner {
    @Override // com.miui.gallery.scanner.core.task.convertor.internal.base.IScanner
    public SemiScanTask[] createTasks(Context context) {
        return new SemiScanTask[]{new ScanTrashAlbumTask(context, ScanTaskConfigFactory.get(19))};
    }
}
