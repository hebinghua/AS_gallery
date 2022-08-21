package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class SemiScanTask extends BaseScanTask<SemiScanTask, Void> {
    public Consumer<EventualScanTask> mConsumer;
    public IScanTaskConverter<EventualScanTask> mSemiScanTaskConverter;

    public boolean beforeRun(BaseScanTaskManager baseScanTaskManager) {
        return false;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: genDefaultScanResult  reason: collision with other method in class */
    public Void mo1305genDefaultScanResult() {
        return null;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public /* bridge */ /* synthetic */ Void mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public SemiScanTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
    }

    public void setConsumer(Consumer<EventualScanTask> consumer) {
        this.mConsumer = consumer;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public Void mo1304doRun(ThreadPool.JobContext jobContext, final List<Throwable> list) {
        this.mSemiScanTaskConverter.convertFlow(list).subscribe(new Observer<EventualScanTask>() { // from class: com.miui.gallery.scanner.core.task.semi.SemiScanTask.1
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
            public void onNext(EventualScanTask eventualScanTask) {
                if (eventualScanTask.getParentTask() == null) {
                    eventualScanTask.setParentTask(SemiScanTask.this);
                }
                try {
                    SemiScanTask.this.mConsumer.accept(eventualScanTask);
                } catch (Exception e) {
                    list.add(e);
                }
            }
        });
        return null;
    }
}
