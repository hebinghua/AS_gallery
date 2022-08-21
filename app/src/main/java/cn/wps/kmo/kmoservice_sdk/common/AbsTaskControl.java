package cn.wps.kmo.kmoservice_sdk.common;

import android.content.Context;
import android.text.TextUtils;
import cn.wps.kmo.kmoservice_sdk.service.CorServiceFac;
import cn.wps.kmo.kmoservice_sdk.service.CorServiceHelper;
import cn.wps.kmo.kmoservice_sdk.service.CorServiceManager;
import cn.wps.kmo.kmoservice_sdk.service.CorTaskManager;

/* loaded from: classes.dex */
public abstract class AbsTaskControl {
    public String appType;
    public CorServiceManager mCorServiceManager;
    public TaskData mTaskData;
    public String taskName;

    public abstract String getApptype();

    public abstract ICorTask getCorTask();

    public abstract String getTaskName();

    public void init(Context context) {
        if (TextUtils.isEmpty(getApptype()) || TextUtils.isEmpty(getTaskName())) {
            throw null;
        }
        this.appType = getApptype();
        this.taskName = getTaskName();
        CorServiceFac.getInstance().registerCorServiceManager(this.taskName, this.appType);
        CorServiceManager corServiceManager = CorServiceFac.getInstance().getCorServiceManager(this.taskName, this.appType);
        this.mCorServiceManager = corServiceManager;
        corServiceManager.init(context, this.appType, this.taskName);
        registerCorTask();
        CorTaskManager.getInstance().getCorTask(this.taskName).init(context);
    }

    public TaskResult checkAppInvalid() {
        return this.mCorServiceManager.checkAppInvalid();
    }

    public void tryToBindService() {
        this.mCorServiceManager.tryToBindService();
    }

    public void requestPermission(CorServiceHelper.OnPermissionListener onPermissionListener) {
        this.mCorServiceManager.requestPermission(onPermissionListener);
    }

    public void startTask(TaskData taskData) {
        if (taskData == null) {
            return;
        }
        taskData.setAppType(this.appType);
        taskData.setTaskName(this.taskName);
        this.mTaskData = taskData;
        if (CorTaskManager.getInstance().getCorTask(this.taskName).prepare(taskData)) {
            return;
        }
        this.mCorServiceManager.startTask(taskData);
    }

    public void dispose(boolean z) {
        String taskName = getTaskName();
        this.taskName = taskName;
        this.mCorServiceManager.dispose(taskName, z);
        TaskData taskData = this.mTaskData;
        if (taskData != null) {
            taskData.mCallback = null;
            this.mTaskData = null;
        }
    }

    public final void registerCorTask() {
        CorTaskManager.getInstance().registerTask(this.taskName, getCorTask());
    }
}
