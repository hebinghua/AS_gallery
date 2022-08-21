package com.miui.gallery.scanner.core.task.eventual;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.model.ShareAlbumEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import java.nio.file.Path;
import java.util.List;

/* loaded from: classes2.dex */
public class ScanShareImageTask extends EventualScanTask {
    public ShareAlbumEntry mShareAlbumEntry;

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return -35121058;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public ScanShareImageTask(Context context, ScanTaskConfig scanTaskConfig, Path path, ShareAlbumEntry shareAlbumEntry) {
        super(context, scanTaskConfig, path);
        this.mShareAlbumEntry = shareAlbumEntry;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        return getConfig().getScanner().scanFile(this.mContext, this.mPath, this.mShareAlbumEntry, this.mConfig);
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof ScanShareImageTask;
    }
}
