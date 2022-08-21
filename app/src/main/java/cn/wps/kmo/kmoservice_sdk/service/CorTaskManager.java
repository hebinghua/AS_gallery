package cn.wps.kmo.kmoservice_sdk.service;

import android.text.TextUtils;
import cn.wps.kmo.kmoservice_sdk.common.ICorTask;
import java.util.HashMap;
import java.util.Objects;

/* loaded from: classes.dex */
public class CorTaskManager {
    public static CorTaskManager mTaskFac;
    public HashMap<String, ICorTask> mRgtTasks = new HashMap<>();

    public static CorTaskManager getInstance() {
        if (mTaskFac == null) {
            synchronized (CorTaskManager.class) {
                if (mTaskFac == null) {
                    mTaskFac = new CorTaskManager();
                }
            }
        }
        return mTaskFac;
    }

    public ICorTask getCorTask(String str) {
        ICorTask newCorTask = newCorTask(str);
        Objects.requireNonNull(newCorTask, "CorTask is null, please go to TaskControl register");
        return newCorTask;
    }

    public final ICorTask newCorTask(String str) {
        if (TextUtils.isEmpty(str)) {
            throw null;
        }
        if (!this.mRgtTasks.containsKey(str)) {
            return null;
        }
        return this.mRgtTasks.get(str);
    }

    public void registerTask(String str, ICorTask iCorTask) {
        if (this.mRgtTasks == null || TextUtils.isEmpty(str) || iCorTask == null) {
            return;
        }
        this.mRgtTasks.put(str, iCorTask);
    }

    public void removeTask(String str) {
        HashMap<String, ICorTask> hashMap = this.mRgtTasks;
        if (hashMap == null || hashMap.size() <= 0 || !this.mRgtTasks.containsKey(str)) {
            return;
        }
        this.mRgtTasks.remove(str);
    }

    public void destoryTask(String str) {
        HashMap<String, ICorTask> hashMap = this.mRgtTasks;
        if (hashMap == null || hashMap.size() <= 0 || !this.mRgtTasks.containsKey(str)) {
            return;
        }
        this.mRgtTasks.get(str).destory();
    }
}
