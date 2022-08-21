package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.semi.ScanShareAlbumsTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes2.dex */
public class ShareAlbumScanner implements IScanner {
    @Override // com.miui.gallery.scanner.core.task.convertor.internal.base.IScanner
    public SemiScanTask[] createTasks(Context context) {
        if (!Preference.sIsFirstSynced() || Preference.sIsShareImageScanned()) {
            return null;
        }
        return new SemiScanTask[]{new ScanShareAlbumsTask(context, ScanTaskConfigFactory.get(14))};
    }
}
