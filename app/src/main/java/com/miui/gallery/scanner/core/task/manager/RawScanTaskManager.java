package com.miui.gallery.scanner.core.task.manager;

import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.scanner.core.task.raw.RawScanTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.threadpool.PriorityTaskManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.functions.Consumer;
import java.io.PrintWriter;
import java.util.List;

/* loaded from: classes2.dex */
public class RawScanTaskManager extends BaseScanTaskManager<RawScanTask> implements PriorityTaskManager.OnAllTasksExecutedListener {
    public SemiScanTaskManager mInnerManager;
    public FutureListener<List<SemiScanTask>> mRawScanTaskOnDoneListener;

    /* renamed from: $r8$lambda$WpgK6halH-4XBozHOPZeWY-m8L4 */
    public static /* synthetic */ void m1298$r8$lambda$WpgK6halH4XBozHOPZeWYm8L4(RawScanTaskManager rawScanTaskManager, SemiScanTask semiScanTask) {
        rawScanTaskManager.submitInternal(semiScanTask);
    }

    public String toString() {
        return "RawScanTaskManager";
    }

    public RawScanTaskManager(int i, PriorityTaskManager.OnAllTasksExecutedListener onAllTasksExecutedListener) {
        super(i, "raw-scan", onAllTasksExecutedListener);
        this.mRawScanTaskOnDoneListener = new FutureListener<List<SemiScanTask>>() { // from class: com.miui.gallery.scanner.core.task.manager.RawScanTaskManager.1
            {
                RawScanTaskManager.this = this;
            }

            @Override // com.miui.gallery.concurrent.FutureListener
            public void onFutureDone(Future<List<SemiScanTask>> future) {
                List<SemiScanTask> list = future.get();
                if (!BaseMiscUtil.isValid(list)) {
                    return;
                }
                for (SemiScanTask semiScanTask : list) {
                    RawScanTaskManager.this.submitInternal(semiScanTask);
                }
            }
        };
        this.mInnerManager = new SemiScanTaskManager(4, this);
    }

    @Override // com.miui.gallery.threadpool.NonBlockingPriorityTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public void submit(RawScanTask rawScanTask) {
        if (rawScanTask == null) {
            return;
        }
        rawScanTask.setConsumer(new Consumer() { // from class: com.miui.gallery.scanner.core.task.manager.RawScanTaskManager$$ExternalSyntheticLambda0
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                RawScanTaskManager.m1298$r8$lambda$WpgK6halH4XBozHOPZeWYm8L4(RawScanTaskManager.this, (SemiScanTask) obj);
            }
        });
        super.submit((RawScanTaskManager) rawScanTask);
    }

    public final void submitInternal(SemiScanTask semiScanTask) {
        if (this.mInnerManager.isShutDown()) {
            DefaultLogger.e("RawScanTaskManager", "trying to submit task while inner manager is already down.");
        } else {
            this.mInnerManager.submit(semiScanTask);
        }
    }

    @Override // com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager, com.miui.gallery.threadpool.PriorityTaskManager, com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future future) {
        if (future != null && future.getCancelType() != 1) {
            this.mRawScanTaskOnDoneListener.onFutureDone(future);
        }
        super.onFutureDone(future);
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
