package com.miui.gallery.scanner.core.task;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.permission.PermissionIntroductionUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.task.state.ITaskState;
import com.miui.gallery.scanner.core.task.state.TaskStateEnum;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.threadpool.PriorityTask;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class BaseScanTask<TASK, RESULT> implements PriorityTask<TASK, RESULT>, ITaskState {
    public ScanTaskConfig mConfig;
    public Context mContext;
    public long mDoneTime;
    public BaseScanTask mParentTask;
    public long mSelfDoneTime;
    public long mStartTime;
    public ScanContracts$StatusReason mStateReason;
    public long mCreateTime = System.currentTimeMillis();
    public final List<BaseScanTask> mChildren = Collections.synchronizedList(new LinkedList());
    public ITaskState mState = TaskStateEnum.WAITING;
    public List<BaseScanTaskStateListener<BaseScanTask>> mStateListeners = Collections.synchronizedList(new LinkedList());

    public void doOnAllChildrenTaskDone() {
    }

    /* renamed from: doRun */
    public abstract RESULT mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) throws Exception;

    /* renamed from: genDefaultScanResult */
    public abstract RESULT mo1305genDefaultScanResult();

    public BaseScanTask(Context context, ScanTaskConfig scanTaskConfig) {
        this.mContext = context;
        this.mConfig = scanTaskConfig;
        addStateListener(new BaseScanTaskStateListener() { // from class: com.miui.gallery.scanner.core.task.BaseScanTask.1
            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onRunning(BaseScanTask baseScanTask, ScanContracts$StatusReason scanContracts$StatusReason) {
                BaseScanTask.this.mStateReason = scanContracts$StatusReason;
                BaseScanTask.this.mStartTime = System.currentTimeMillis();
            }

            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onAbandoned(BaseScanTask baseScanTask, ScanContracts$StatusReason scanContracts$StatusReason) {
                BaseScanTask.this.mStateReason = scanContracts$StatusReason;
                onDone(baseScanTask, scanContracts$StatusReason);
            }

            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onSelfDone(BaseScanTask baseScanTask, ScanContracts$StatusReason scanContracts$StatusReason) {
                BaseScanTask.this.mStateReason = scanContracts$StatusReason;
                BaseScanTask.this.mSelfDoneTime = System.currentTimeMillis();
            }

            @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
            public void onDone(BaseScanTask baseScanTask, ScanContracts$StatusReason scanContracts$StatusReason) {
                BaseScanTask.this.mStateReason = scanContracts$StatusReason;
                BaseScanTask.this.mDoneTime = System.currentTimeMillis();
                BaseScanTask.this.printTaskLifeRecord();
                BaseScanTask.this.onAllChildrenTaskDone();
            }
        });
    }

    public void addStateListener(BaseScanTaskStateListener baseScanTaskStateListener) {
        this.mStateListeners.add(baseScanTaskStateListener);
    }

    public ScanContracts$StatusReason getStateReason() {
        return this.mStateReason;
    }

    public ScanTaskConfig getConfig() {
        return this.mConfig;
    }

    public long getPriority() {
        return this.mConfig.getPriority();
    }

    public ITaskState getState() {
        return this.mState;
    }

    public void setParentTask(BaseScanTask baseScanTask) {
        this.mParentTask = baseScanTask;
        baseScanTask.registerChild(this);
    }

    public BaseScanTask getParentTask() {
        return this.mParentTask;
    }

    public long getCreateTime() {
        return this.mCreateTime;
    }

    public final void registerChild(BaseScanTask baseScanTask) {
        this.mChildren.add(baseScanTask);
        DefaultLogger.d("BaseScanTask", "%s registerChild %s.", toString(), baseScanTask.toString());
    }

    public void onChildNotified(BaseScanTask baseScanTask) {
        DefaultLogger.d("BaseScanTask", "%s unregisterChild %s.", toString(), baseScanTask.toString());
        this.mChildren.remove(baseScanTask);
        if (checkAllChildrenTaskDone()) {
            gotoDone(ScanContracts$StatusReason.ALL_CHILDREN_DONE);
        }
    }

    public final void onAllChildrenTaskDone() {
        DefaultLogger.d("BaseScanTask", "All children task of %s has been done.", toString());
        doOnAllChildrenTaskDone();
        BaseScanTask baseScanTask = this.mParentTask;
        if (baseScanTask != null) {
            baseScanTask.onChildNotified(this);
        }
    }

    public void printTaskLifeRecord() {
        DefaultLogger.d("BaseScanTask", String.format(Locale.US, " \nTask Life Record Msg:\nState: [%s]\nName: [%s]\nCreate time: [%d]\nWaiting cost: [%d] ms\nSelf cost: [%d] ms\nExtra work/Waiting children cost: [%d] ms", getState().toString(), toString(), Long.valueOf(this.mCreateTime), Long.valueOf(this.mStartTime - this.mCreateTime), Long.valueOf(this.mSelfDoneTime - this.mStartTime), Long.valueOf(this.mDoneTime - this.mSelfDoneTime)));
    }

    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run */
    public final RESULT mo1807run(ThreadPool.JobContext jobContext) {
        RESULT mo1305genDefaultScanResult = mo1305genDefaultScanResult();
        RESULT result = mo1305genDefaultScanResult;
        gotoRunning(ScanContracts$StatusReason.DEFAULT);
        if (!checkBeforeRun()) {
            gotoAbandoned(ScanContracts$StatusReason.PARENT_ABANDONED);
            return null;
        }
        List<Throwable> linkedList = new LinkedList<>();
        try {
            result = mo1304doRun(jobContext, linkedList);
        } catch (Exception e) {
            linkedList.add(e);
        }
        if (BaseMiscUtil.isValid(linkedList)) {
            for (Throwable th : linkedList) {
                DefaultLogger.e("BaseScanTask", "doRun [%s] error [%s].", toString(), th.getMessage());
            }
        }
        dealWithResult(result);
        onProduceDone();
        return result;
    }

    public boolean checkBeforeRun() {
        if (PermissionIntroductionUtils.isAlreadyGetExternalStoragePermission(GalleryApp.sGetAndroidContext()) || BaseBuildUtil.isInternational() || BaseGalleryPreferences.CTA.isSystemCTAPermissionAllowed()) {
            if (getParentTask() != null && TaskStateEnum.ABANDONED == getParentTask().getState()) {
                return false;
            }
            if (getConfig().isCredible() || getConfig().getSceneCode() == 20 || getConfig().getSceneCode() == 6 || getConfig().getSceneCode() == 17 || getConfig().getSceneCode() == 16) {
                return true;
            }
            Boolean bool = (Boolean) ScanCache.getInstance().get("key_mi_mover_event_start");
            return bool == null || !bool.booleanValue();
        }
        return false;
    }

    public void onProduceDone() {
        if (checkAllChildrenTaskDone()) {
            gotoDone(ScanContracts$StatusReason.NO_CHILD_OR_ALL_DONE);
        }
    }

    public void dealWithResult(RESULT result) {
        gotoSelfDone(ScanContracts$StatusReason.DEFAULT);
    }

    public final boolean checkAllChildrenTaskDone() {
        return TaskStateEnum.SELF_DONE == getState() && this.mChildren.size() <= 0;
    }

    @Override // com.miui.gallery.scanner.core.task.state.ITaskState
    public synchronized ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
        DefaultLogger.d("BaseScanTask", "task [%s] try to goto [Running] since [%s].", toString(), scanContracts$StatusReason);
        this.mState = this.mState.gotoRunning(scanContracts$StatusReason);
        if (BaseMiscUtil.isValid(this.mStateListeners)) {
            for (BaseScanTaskStateListener<BaseScanTask> baseScanTaskStateListener : this.mStateListeners) {
                baseScanTaskStateListener.onRunning(this, scanContracts$StatusReason);
            }
        }
        return this.mState;
    }

    @Override // com.miui.gallery.scanner.core.task.state.ITaskState
    public synchronized ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
        DefaultLogger.d("BaseScanTask", "task [%s] try to goto [Abandoned] since [%s].", toString(), scanContracts$StatusReason);
        this.mState = this.mState.gotoAbandoned(scanContracts$StatusReason);
        if (BaseMiscUtil.isValid(this.mStateListeners)) {
            for (BaseScanTaskStateListener<BaseScanTask> baseScanTaskStateListener : this.mStateListeners) {
                baseScanTaskStateListener.onAbandoned(this, scanContracts$StatusReason);
            }
        }
        return this.mState;
    }

    @Override // com.miui.gallery.scanner.core.task.state.ITaskState
    public synchronized ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
        DefaultLogger.d("BaseScanTask", "task [%s] try to goto [Waiting] since [%s].", toString(), scanContracts$StatusReason);
        this.mState = this.mState.gotoWaiting(scanContracts$StatusReason);
        if (BaseMiscUtil.isValid(this.mStateListeners)) {
            for (BaseScanTaskStateListener<BaseScanTask> baseScanTaskStateListener : this.mStateListeners) {
                baseScanTaskStateListener.onWaiting(this, scanContracts$StatusReason);
            }
        }
        return this.mState;
    }

    @Override // com.miui.gallery.scanner.core.task.state.ITaskState
    public synchronized ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
        DefaultLogger.d("BaseScanTask", "task [%s] try to goto [SelfDone] since [%s].", toString(), scanContracts$StatusReason);
        this.mState = this.mState.gotoSelfDone(scanContracts$StatusReason);
        if (BaseMiscUtil.isValid(this.mStateListeners)) {
            for (BaseScanTaskStateListener<BaseScanTask> baseScanTaskStateListener : this.mStateListeners) {
                baseScanTaskStateListener.onSelfDone(this, scanContracts$StatusReason);
            }
        }
        return this.mState;
    }

    @Override // com.miui.gallery.scanner.core.task.state.ITaskState
    public synchronized ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
        DefaultLogger.d("BaseScanTask", "task [%s] try to goto [Done] since [%s].", toString(), scanContracts$StatusReason);
        this.mState = this.mState.gotoDone(scanContracts$StatusReason);
        if (BaseMiscUtil.isValid(this.mStateListeners)) {
            for (BaseScanTaskStateListener<BaseScanTask> baseScanTaskStateListener : this.mStateListeners) {
                baseScanTaskStateListener.onDone(this, scanContracts$StatusReason);
            }
        }
        return this.mState;
    }

    @Override // com.miui.gallery.scanner.core.task.state.ITaskState
    public synchronized ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
        DefaultLogger.d("BaseScanTask", "task [%s] try to goto [Retry] since [%s].", toString(), scanContracts$StatusReason);
        this.mState = this.mState.gotoRetry(scanContracts$StatusReason);
        if (BaseMiscUtil.isValid(this.mStateListeners)) {
            for (BaseScanTaskStateListener<BaseScanTask> baseScanTaskStateListener : this.mStateListeners) {
                baseScanTaskStateListener.onRetry(this, scanContracts$StatusReason);
            }
        }
        return this.mState;
    }

    @Override // java.lang.Comparable
    public int compareTo(TASK task) {
        if (!(task instanceof BaseScanTask)) {
            throw new IllegalArgumentException();
        }
        BaseScanTask<TASK, RESULT> baseScanTask = (BaseScanTask) task;
        if (baseScanTask != this) {
            return Long.compare(getPriority(), baseScanTask.getPriority());
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof BaseScanTask)) {
            return false;
        }
        return this.mConfig.equals(((BaseScanTask) obj).mConfig);
    }

    public int hashCode() {
        return this.mConfig.hashCode();
    }
}
