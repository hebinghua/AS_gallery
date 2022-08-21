package com.miui.gallery.scanner.core.task.raw;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.scanner.core.model.UnhandledScanTaskRecord;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.ExternalScanTaskConverter;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.deprecated.Preference;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ExternalScanTask extends RawScanTask {
    public final String mPath;
    public final long mRowId;

    @Override // com.miui.gallery.scanner.core.task.raw.RawScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public /* bridge */ /* synthetic */ List<SemiScanTask> mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public ExternalScanTask(Context context, long j, long j2, String str, boolean z, String str2, String str3, int i, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mRowId = j;
        this.mPath = str;
        this.mRawScanTaskConverter = new ExternalScanTaskConverter(context, j2, str, z, str2, str3, i, scanTaskConfig);
    }

    @Override // com.miui.gallery.scanner.core.task.raw.RawScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public List<SemiScanTask> mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        if (!Preference.sIsMediaStoreSupportGalleryScan()) {
            Preference.sSetMediaStoreSupportGalleryScan();
        }
        return super.mo1304doRun(jobContext, list);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void doOnAllChildrenTaskDone() {
        GalleryEntityManager.getInstance().delete(UnhandledScanTaskRecord.class, String.format(Locale.US, "_id = %d", Long.valueOf(this.mRowId)), null);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof ExternalScanTask) && ((ExternalScanTask) obj).mPath.equalsIgnoreCase(this.mPath) && super.equals(obj);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        String str = this.mPath;
        return ((str != null ? 527 + str.hashCode() : 17) * 31) + super.hashCode();
    }

    public String toString() {
        return String.format("-%s %s", getClass().getSimpleName(), this.mPath);
    }
}
