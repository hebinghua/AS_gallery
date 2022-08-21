package com.miui.gallery.scanner.core.task.raw;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.BaseMiscUtil;
import io.reactivex.functions.Consumer;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class RawScanTask extends BaseScanTask<RawScanTask, List<SemiScanTask>> {
    public Consumer<SemiScanTask> mConsumer;
    public IScanTaskConverter<SemiScanTask> mRawScanTaskConverter;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public /* bridge */ /* synthetic */ List<SemiScanTask> mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public RawScanTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
    }

    public void setConsumer(Consumer<SemiScanTask> consumer) {
        this.mConsumer = consumer;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public List<SemiScanTask> mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) {
        List<SemiScanTask> convert = this.mRawScanTaskConverter.convert(list);
        if (BaseMiscUtil.isValid(convert)) {
            for (SemiScanTask semiScanTask : convert) {
                if (semiScanTask != null) {
                    semiScanTask.setParentTask(this);
                }
            }
        }
        return convert;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: genDefaultScanResult  reason: collision with other method in class */
    public List<SemiScanTask> mo1305genDefaultScanResult() {
        return Collections.emptyList();
    }
}
