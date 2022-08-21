package com.miui.gallery.scanner.core.task.manager;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.state.TaskStateEnum;
import com.miui.gallery.threadpool.PriorityTaskManager;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class EventualScanTaskManager extends BaseScanTaskManager<EventualScanTask> {
    public String toString() {
        return "EventualScanTaskManager";
    }

    public EventualScanTaskManager(int i, PriorityTaskManager.OnAllTasksExecutedListener onAllTasksExecutedListener) {
        super(i, "eventual-scan", onAllTasksExecutedListener);
    }

    @Override // com.miui.gallery.threadpool.PriorityTaskManager
    public void doWithSameTask(EventualScanTask eventualScanTask, EventualScanTask eventualScanTask2) {
        super.doWithSameTask(eventualScanTask, eventualScanTask2);
        eventualScanTask.mergePriority(eventualScanTask2.getPriority());
        eventualScanTask2.gotoAbandoned(ScanContracts$StatusReason.SAME_TASK_EXISTS);
    }

    @Override // com.miui.gallery.threadpool.NonBlockingPriorityTaskManager, com.miui.gallery.threadpool.PriorityTaskManager
    public void shutdown() {
        super.shutdown();
        ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.scanner.core.task.manager.EventualScanTaskManager.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run  reason: collision with other method in class */
            public Void mo1807run(ThreadPool.JobContext jobContext) {
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                MediaScannerHelper.clearScannableDirectoryCache();
                MediaScannerHelper.checkMiMoverStopped(sGetAndroidContext);
                return null;
            }
        });
    }

    @Override // com.miui.gallery.scanner.core.task.manager.BaseScanTaskManager, com.miui.gallery.threadpool.PriorityTaskManager, com.miui.gallery.concurrent.FutureListener
    public void onFutureDone(Future future) {
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mLock) {
            DefaultLogger.d("EventualScanTaskManager", "onFutureDone wait lock cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            if (future != null) {
                EventualScanTask eventualScanTask = (EventualScanTask) future.getJob();
                if (eventualScanTask.getState() == TaskStateEnum.RETRY) {
                    eventualScanTask.demote();
                    eventualScanTask.gotoWaiting(ScanContracts$StatusReason.DEFAULT);
                    this.mWaitQueue.offer(eventualScanTask);
                }
            }
        }
        super.onFutureDone(future);
        DefaultLogger.d("EventualScanTaskManager", "onFutureDone cost [%d] ms, waiting queue size [%d].", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Integer.valueOf(this.mWaitQueue.size()));
    }
}
