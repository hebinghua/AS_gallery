package com.miui.gallery.scanner.core.task.manager;

import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.threadpool.PriorityTaskManager;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.functions.Consumer;
import java.io.PrintWriter;

/* loaded from: classes2.dex */
public class SemiScanTaskManager extends BaseScanTaskManager<SemiScanTask> implements PriorityTaskManager.OnAllTasksExecutedListener {
    public final EventualScanTaskManager mInnerManager;

    public static /* synthetic */ void $r8$lambda$wHQjKAAVqjZJoec4Hkx5vGEdptU(SemiScanTaskManager semiScanTaskManager, EventualScanTask eventualScanTask) {
        semiScanTaskManager.submitInternal(eventualScanTask);
    }

    public String toString() {
        return "SemiScanTaskManager";
    }

    public SemiScanTaskManager(int i, PriorityTaskManager.OnAllTasksExecutedListener onAllTasksExecutedListener) {
        super(i, "semi-scan", onAllTasksExecutedListener);
        this.mInnerManager = new EventualScanTaskManager(4, this);
    }

    @Override // com.miui.gallery.threadpool.NonBlockingPriorityTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public void submit(SemiScanTask semiScanTask) {
        if (semiScanTask != null && !semiScanTask.beforeRun(this)) {
            semiScanTask.setConsumer(new Consumer() { // from class: com.miui.gallery.scanner.core.task.manager.SemiScanTaskManager$$ExternalSyntheticLambda0
                @Override // io.reactivex.functions.Consumer
                public final void accept(Object obj) {
                    SemiScanTaskManager.$r8$lambda$wHQjKAAVqjZJoec4Hkx5vGEdptU(SemiScanTaskManager.this, (EventualScanTask) obj);
                }
            });
            super.submit((SemiScanTaskManager) semiScanTask);
        }
    }

    public final void submitInternal(EventualScanTask eventualScanTask) {
        if (this.mInnerManager.isShutDown()) {
            DefaultLogger.e("SemiScanTaskManager", "trying to submit task while inner manager is already down.");
        } else {
            this.mInnerManager.submit(eventualScanTask);
        }
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager.OnAllTasksExecutedListener
    public void onAllTasksExecuted() {
        if (this.mOnAllTasksExecutedListener == null || !isEmpty()) {
            return;
        }
        this.mOnAllTasksExecutedListener.onAllTasksExecuted();
    }

    @Override // com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager, com.miui.gallery.threadpool.NonBlockingPriorityTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public boolean isEmpty() {
        return super.isEmpty() && this.mInnerManager.isEmpty();
    }

    @Override // com.miui.gallery.threadpool.NonBlockingPriorityTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public void shutdown() {
        super.shutdown();
        this.mInnerManager.shutdown();
    }

    @Override // com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public void cancelAll() {
        super.cancelAll();
        this.mInnerManager.cancelAll();
    }

    @Override // com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager
    public void dump(PrintWriter printWriter) {
        super.dump(printWriter);
        this.mInnerManager.dump(printWriter);
    }
}
