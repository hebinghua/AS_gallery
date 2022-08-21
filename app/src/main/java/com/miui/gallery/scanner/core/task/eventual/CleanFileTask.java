package com.miui.gallery.scanner.core.task.eventual;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.model.OwnerItemEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.util.BaseFileUtils;
import java.nio.file.Path;
import java.util.List;

/* loaded from: classes2.dex */
public class CleanFileTask extends EventualScanTask {
    public final OwnerItemEntry mEntry;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public CleanFileTask(Context context, ScanTaskConfig scanTaskConfig, Path path) {
        this(context, scanTaskConfig, path, null);
    }

    public CleanFileTask(Context context, ScanTaskConfig scanTaskConfig, Path path, OwnerItemEntry ownerItemEntry) {
        super(context, scanTaskConfig, path);
        this.mEntry = ownerItemEntry == null ? OwnerItemEntry.fromFilePath(context, path).get(path.toString().toLowerCase()) : ownerItemEntry;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean checkBeforeRun() {
        List<String> list = (List) ScanCache.getInstance().get("key_migrate_affected_paths");
        if (list == null) {
            return true;
        }
        for (String str : list) {
            if (BaseFileUtils.contains(str, this.mEntry.mPath)) {
                return false;
            }
        }
        return super.checkBeforeRun();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        return getConfig().getScanner().cleanFile(this.mContext, this.mPath, this.mEntry, getConfig());
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return this.mHashCode;
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof CleanFileTask) && super.equals(obj);
    }
}
