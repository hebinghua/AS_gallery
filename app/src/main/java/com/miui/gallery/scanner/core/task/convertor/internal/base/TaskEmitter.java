package com.miui.gallery.scanner.core.task.convertor.internal.base;

import com.miui.gallery.scanner.core.task.raw.InternalScanTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import io.reactivex.ObservableEmitter;

/* loaded from: classes2.dex */
public class TaskEmitter {
    public final ObservableEmitter<SemiScanTask> mEmitter;
    public final InternalScanTask mInternalScanTask;

    public TaskEmitter(InternalScanTask internalScanTask, ObservableEmitter<SemiScanTask> observableEmitter) {
        this.mInternalScanTask = internalScanTask;
        this.mEmitter = observableEmitter;
    }

    public void emit(SemiScanTask... semiScanTaskArr) {
        if (semiScanTaskArr == null) {
            return;
        }
        for (SemiScanTask semiScanTask : semiScanTaskArr) {
            if (semiScanTask != null) {
                this.mEmitter.onNext(semiScanTask);
            }
        }
    }

    public void registerAndEmit(SemiScanTask... semiScanTaskArr) {
        if (semiScanTaskArr == null) {
            return;
        }
        for (SemiScanTask semiScanTask : semiScanTaskArr) {
            if (semiScanTask != null) {
                semiScanTask.setParentTask(this.mInternalScanTask);
                this.mEmitter.onNext(semiScanTask);
            }
        }
    }
}
