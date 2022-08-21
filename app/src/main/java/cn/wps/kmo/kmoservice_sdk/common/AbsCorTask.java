package cn.wps.kmo.kmoservice_sdk.common;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.Objects;

/* loaded from: classes.dex */
public abstract class AbsCorTask implements ICorTask {
    public Context mContext;
    public TaskData mTaskData;

    @Override // cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public void init(Context context) {
        this.mContext = context;
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public boolean prepare(TaskData taskData) {
        if (taskData == null) {
            throw new IllegalArgumentException("TaskData must not be null");
        }
        this.mTaskData = taskData;
        return false;
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public boolean prepareTask(TaskData taskData) {
        Objects.requireNonNull(taskData);
        this.mTaskData = taskData;
        return false;
    }

    @Override // cn.wps.kmo.kmoservice_sdk.common.ICorTask
    public void startTask(IBinder iBinder, TaskData taskData) throws RemoteException {
        if (iBinder == null || taskData == null) {
            throw null;
        }
        this.mTaskData = taskData;
    }
}
