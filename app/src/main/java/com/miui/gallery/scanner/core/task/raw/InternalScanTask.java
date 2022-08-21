package com.miui.gallery.scanner.core.task.raw;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.ScanRequest;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.internal.InternalScanTaskConverter;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class InternalScanTask extends RawScanTask {
    public ScanRequest mScanRequest;

    @Override // com.miui.gallery.scanner.core.task.raw.RawScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public /* bridge */ /* synthetic */ List<SemiScanTask> mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public InternalScanTask(Context context, ScanRequest scanRequest, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mScanRequest = scanRequest;
        this.mRawScanTaskConverter = new InternalScanTaskConverter(this.mContext, this);
    }

    public ScanRequest getScanRequest() {
        return this.mScanRequest;
    }

    @Override // com.miui.gallery.scanner.core.task.raw.RawScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public List<SemiScanTask> mo1304doRun(ThreadPool.JobContext jobContext, final List<Throwable> list) {
        if (getConfig().getSceneCode() == 0) {
            this.mRawScanTaskConverter.convertFlow(list).subscribe(new Observer<SemiScanTask>() { // from class: com.miui.gallery.scanner.core.task.raw.InternalScanTask.1
                @Override // io.reactivex.Observer
                public void onComplete() {
                }

                @Override // io.reactivex.Observer
                public void onError(Throwable th) {
                }

                @Override // io.reactivex.Observer
                public void onSubscribe(Disposable disposable) {
                }

                @Override // io.reactivex.Observer
                public void onNext(SemiScanTask semiScanTask) {
                    try {
                        InternalScanTask.this.mConsumer.accept(semiScanTask);
                    } catch (Exception e) {
                        list.add(e);
                    }
                }
            });
            return Collections.emptyList();
        }
        return super.mo1304doRun(jobContext, list);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return (obj instanceof InternalScanTask) && this.mScanRequest.equals(((InternalScanTask) obj).mScanRequest) && super.equals(obj);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        ScanRequest scanRequest = this.mScanRequest;
        return ((scanRequest != null ? 527 + scanRequest.hashCode() : 17) * 31) + super.hashCode();
    }

    public String toString() {
        return String.format("-%s %s", getClass().getSimpleName(), Integer.valueOf(getConfig().getSceneCode()));
    }
}
