package cn.wps.kmo.kmoservice_sdk.common;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

/* loaded from: classes.dex */
public interface ICorTask {
    void destory();

    void init(Context context);

    boolean prepare(TaskData taskData);

    boolean prepareTask(TaskData taskData);

    void startTask(IBinder iBinder, TaskData taskData) throws RemoteException;

    void taskFinish(TaskData taskData);
}
