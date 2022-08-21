package com.miui.gallery.analytics;

import android.text.TextUtils;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class TimeMonitor {
    public static final ThreadLocal<Map<Integer, TaskInfo>> mTaskInfoList = ThreadLocal.withInitial(TimeMonitor$$ExternalSyntheticLambda0.INSTANCE);
    public static final ConcurrentHashMap<Integer, TaskInfo> mTaskMap = new ConcurrentHashMap<>();

    public static void createNewTimeMonitor(String str) {
        createNewTimeMonitor(str, true);
    }

    public static void createNewTimeMonitor(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
        if (threadLocal.get() != null && threadLocal.get().containsKey(Integer.valueOf(str.hashCode())) && !z) {
            return;
        }
        TaskInfo taskInfo = new TaskInfo(str);
        threadLocal.get().put(Integer.valueOf(taskInfo.getCookie()), taskInfo);
    }

    public static void cancelTimeMonitor(String str) {
        if (!TextUtils.isEmpty(str)) {
            ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
            if (threadLocal.get() == null || threadLocal.get() == null) {
                return;
            }
            threadLocal.get().remove(Integer.valueOf(str.hashCode()));
        }
    }

    public static void trackTimeMonitor(String str) {
        TaskInfo taskInfo;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
        if (threadLocal.get() == null || (taskInfo = threadLocal.get().get(Integer.valueOf(str.hashCode()))) == null) {
            return;
        }
        taskInfo.track();
        threadLocal.get().remove(Integer.valueOf(taskInfo.getCookie()));
    }

    public static void trackTimeMonitor(String str, long j) {
        TaskInfo taskInfo;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
        if (threadLocal.get() == null || (taskInfo = threadLocal.get().get(Integer.valueOf(str.hashCode()))) == null) {
            return;
        }
        taskInfo.track(j);
        threadLocal.get().remove(Integer.valueOf(taskInfo.getCookie()));
    }

    public static void trackTimeMonitor(String str, long j, long j2) {
        TaskInfo taskInfo;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
        if (threadLocal.get() == null || (taskInfo = threadLocal.get().get(Integer.valueOf(str.hashCode()))) == null) {
            return;
        }
        taskInfo.track(j, j2);
        threadLocal.get().remove(Integer.valueOf(taskInfo.getCookie()));
    }

    public static void trackTimeMonitor(String str, String str2, long j) {
        TaskInfo taskInfo;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
        if (threadLocal.get() == null || (taskInfo = threadLocal.get().get(Integer.valueOf(str.hashCode()))) == null) {
            return;
        }
        taskInfo.track(str2, j);
        threadLocal.get().remove(Integer.valueOf(taskInfo.getCookie()));
    }

    public static void trackTimeMonitor(String str, Map<String, Object> map) {
        TaskInfo taskInfo;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ThreadLocal<Map<Integer, TaskInfo>> threadLocal = mTaskInfoList;
        if (threadLocal.get() == null || (taskInfo = threadLocal.get().get(Integer.valueOf(str.hashCode()))) == null) {
            return;
        }
        taskInfo.track(map);
        threadLocal.get().remove(Integer.valueOf(taskInfo.getCookie()));
    }

    /* loaded from: classes.dex */
    public static class TaskInfo {
        public final int mCookie;
        public final long mStartTime = System.currentTimeMillis();
        public final String mTip;

        public TaskInfo(String str) {
            this.mTip = str;
            this.mCookie = str.hashCode();
        }

        public long calDuration() {
            return System.currentTimeMillis() - this.mStartTime;
        }

        public void track() {
            TrackController.trackTimeMonitor(this.mTip, calDuration());
        }

        public void track(long j) {
            TrackController.trackTimeMonitor(this.mTip, j, calDuration());
        }

        public void track(long j, long j2) {
            TrackController.trackTimeMonitor(this.mTip, j, j2, calDuration());
        }

        public void track(String str, long j) {
            TrackController.trackTimeMonitor(this.mTip, str, j, calDuration());
        }

        public void track(Map<String, Object> map) {
            map.put("duration", Long.valueOf(calDuration()));
            TrackController.trackTimeMonitor(map);
        }

        public String getTip() {
            return this.mTip;
        }

        public int getCookie() {
            return this.mCookie;
        }

        public long getStartTime() {
            return this.mStartTime;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TaskInfo)) {
                return false;
            }
            TaskInfo taskInfo = (TaskInfo) obj;
            return Objects.equals(taskInfo.getTip(), this.mTip) && Objects.equals(Integer.valueOf(taskInfo.getCookie()), Integer.valueOf(this.mCookie)) && Objects.equals(Long.valueOf(taskInfo.getStartTime()), Long.valueOf(this.mStartTime));
        }

        public int hashCode() {
            return Objects.hash(this.mTip, Integer.valueOf(this.mCookie), Long.valueOf(this.mStartTime));
        }
    }
}
