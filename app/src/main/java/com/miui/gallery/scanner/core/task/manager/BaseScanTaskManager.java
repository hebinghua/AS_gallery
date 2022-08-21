package com.miui.gallery.scanner.core.task.manager;

import com.miui.gallery.concurrent.Future;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.BaseScanTaskStateListener;
import com.miui.gallery.threadpool.NonBlockingPriorityTaskManager;
import com.miui.gallery.threadpool.PriorityTask;
import com.miui.gallery.threadpool.PriorityTaskManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public abstract class BaseScanTaskManager<TASK extends BaseScanTask> extends NonBlockingPriorityTaskManager<TASK> {
    public final AtomicInteger mAbandonTask;
    public final long mCreateTime;
    public final AtomicInteger mDoneTask;
    public final Map<TASK, TASK> mNotYetCompletedTasks;
    public final AtomicInteger mTotalTask;

    public boolean cancelRunningTaskWithNewSubmission(TASK task) {
        return false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public /* bridge */ /* synthetic */ void addToWaitingQueue(PriorityTask priorityTask) {
        addToWaitingQueue((BaseScanTaskManager<TASK>) ((BaseScanTask) priorityTask));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public /* bridge */ /* synthetic */ boolean cancelRunningTaskWithNewSubmission(PriorityTask priorityTask) {
        return cancelRunningTaskWithNewSubmission((BaseScanTaskManager<TASK>) ((BaseScanTask) priorityTask));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.threadpool.NonBlockingPriorityTaskManager
    public /* bridge */ /* synthetic */ void doSubmit(PriorityTask priorityTask) {
        doSubmit((BaseScanTaskManager<TASK>) ((BaseScanTask) priorityTask));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public /* bridge */ /* synthetic */ PriorityTask findSameTask(PriorityTask priorityTask) {
        return findSameTask((BaseScanTaskManager<TASK>) ((BaseScanTask) priorityTask));
    }

    public BaseScanTaskManager(int i, String str, PriorityTaskManager.OnAllTasksExecutedListener onAllTasksExecutedListener) {
        super(i, str, onAllTasksExecutedListener);
        this.mTotalTask = new AtomicInteger();
        this.mDoneTask = new AtomicInteger();
        this.mAbandonTask = new AtomicInteger();
        this.mNotYetCompletedTasks = Collections.synchronizedMap(new HashMap());
        this.mCreateTime = System.currentTimeMillis();
    }

    @Override // com.miui.gallery.threadpool.NonBlockingPriorityTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public boolean isEmpty() {
        return super.isEmpty() && this.mNotYetCompletedTasks.isEmpty();
    }

    public void doSubmit(TASK task) {
        this.mTotalTask.incrementAndGet();
        super.doSubmit((BaseScanTaskManager<TASK>) task);
    }

    public TASK findSameTask(TASK task) {
        TASK task2 = this.mNotYetCompletedTasks.get(task);
        if (task.equals(task2)) {
            return task2;
        }
        return null;
    }

    public void addToWaitingQueue(TASK task) {
        super.addToWaitingQueue((BaseScanTaskManager<TASK>) task);
        this.mNotYetCompletedTasks.put(task, task);
        task.addStateListener(new BaseScanTaskStateListener<TASK>() { // from class: com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager.1
            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onAbandoned(TASK task2, ScanContracts$StatusReason scanContracts$StatusReason) {
                BaseScanTaskManager.this.mAbandonTask.incrementAndGet();
                BaseScanTaskManager.this.mNotYetCompletedTasks.remove(task2);
            }

            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onDone(TASK task2, ScanContracts$StatusReason scanContracts$StatusReason) {
                BaseScanTaskManager.this.mDoneTask.incrementAndGet();
                BaseScanTaskManager.this.mNotYetCompletedTasks.remove(task2);
            }
        });
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager, com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future future) {
        synchronized (this.mLock) {
            StringBuilder sb = new StringBuilder();
            sb.append("onFutureDone: ");
            sb.append(future != null ? future.getJob() : null);
            DefaultLogger.fd("BaseScanTaskManager", sb.toString());
            this.mRunningQueue.remove(future);
            this.mLimit++;
            submitIfAllowed();
        }
        if (this.mOnAllTasksExecutedListener == null || !isEmpty()) {
            return;
        }
        this.mOnAllTasksExecutedListener.onAllTasksExecuted();
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public void cancelAll() {
        super.cancelAll();
        this.mNotYetCompletedTasks.clear();
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("--------------------------------------------");
        Locale locale = Locale.US;
        printWriter.println(String.format(locale, "[%s] CreateTime = [%d]", this, Long.valueOf(this.mCreateTime)));
        printWriter.println(String.format(locale, "[%s] TotalTasks size = [%d]", this, Integer.valueOf(this.mTotalTask.get())));
        printWriter.println(String.format(locale, "[%s] DoneTasks size = [%d]", this, Integer.valueOf(this.mDoneTask.get())));
        printWriter.println(String.format(locale, "[%s] AbandonTasks size = [%d]", this, Integer.valueOf(this.mAbandonTask.get())));
        printWriter.println(String.format(locale, "[%s] mNotYetCompletedTasks size = [%d]", this, Integer.valueOf(this.mNotYetCompletedTasks.size())));
        synchronized (this.mNotYetCompletedTasks) {
            for (TASK task : this.mNotYetCompletedTasks.values()) {
                Locale locale2 = Locale.US;
                printWriter.println(String.format(locale2, "[%s]", task.toString()));
                printWriter.println(String.format(locale2, "createTime: [%d], sceneCode: [%d]", Long.valueOf(task.getCreateTime()), Integer.valueOf(task.getConfig().getSceneCode())));
            }
        }
        printWriter.println("--------------------------------------------");
    }
}
