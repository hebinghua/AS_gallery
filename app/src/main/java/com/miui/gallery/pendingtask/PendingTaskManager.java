package com.miui.gallery.pendingtask;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.content.ComponentName;
import android.os.PersistableBundle;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import com.google.gson.Gson;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.pendingtask.base.PendingTask;
import com.miui.gallery.pendingtask.base.PendingTaskInfo;
import com.miui.gallery.pendingtask.base.PendingTaskService;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class PendingTaskManager {
    public static final int[] TIME_STAGE = {1, 2, 3, 6, 12, 24, 48, 72, SyslogConstants.LOG_LOCAL5};
    public static PendingTaskManager instance;

    public static synchronized PendingTaskManager getInstance() {
        PendingTaskManager pendingTaskManager;
        synchronized (PendingTaskManager.class) {
            if (instance == null) {
                instance = new PendingTaskManager();
            }
            pendingTaskManager = instance;
        }
        return pendingTaskManager;
    }

    public <T> void postTask(int i, T t) {
        postTask(i, t, null);
    }

    public <T> void postTask(int i, T t, String str) {
        postTask(i, t, str, 0L);
    }

    public <T> void postTask(int i, T t, String str, long j) {
        postTask(i, t, str, j, false);
    }

    public final <T> void postTask(int i, T t, String str, long j, boolean z) {
        if (z || !TextUtils.isEmpty(str)) {
            cancelDuplicateTasks(i, str, false, new long[0]);
        }
        DefaultLogger.d("PendingTaskManager", "postTask type: %s tag: %s, unique: %b", Integer.valueOf(i), str, Boolean.valueOf(z));
        PendingTask create = PendingTaskFactory.create(i);
        if (create == null) {
            throw new RuntimeException("Can't find PendingTask of this type");
        }
        PendingTaskInfo pendingTaskInfo = new PendingTaskInfo();
        pendingTaskInfo.setTaskType(i);
        pendingTaskInfo.setTaskTag(str);
        pendingTaskInfo.setCreateTime(System.currentTimeMillis());
        pendingTaskInfo.setExpireTime(j);
        pendingTaskInfo.setNetType(create.getNetworkType());
        pendingTaskInfo.setRequireCharging(create.requireCharging());
        pendingTaskInfo.setRequireDeviceIdle(create.requireDeviceIdle());
        pendingTaskInfo.setMinLatencyMillis(create.getMinLatency());
        try {
            pendingTaskInfo.setData(create.wrapData(t));
            GalleryEntityManager.getInstance().insert(pendingTaskInfo);
            scheduleTask(pendingTaskInfo);
        } catch (Exception e) {
            DefaultLogger.e("PendingTaskManager", "postTask %s wrapData error.\n", Integer.valueOf(i), e);
        }
    }

    public final Map<String, String> recordAllPendingTaskInfos() {
        DefaultLogger.d("PendingTaskManager", "try to record all pending task infos");
        List<PendingTaskInfo> query = GalleryEntityManager.getInstance().query(PendingTaskInfo.class, null, null);
        HashMap hashMap = new HashMap();
        for (PendingTaskInfo pendingTaskInfo : query) {
            String str = (String) hashMap.get(String.valueOf(pendingTaskInfo.getTaskType()));
            hashMap.put(String.valueOf(pendingTaskInfo.getTaskType()), String.valueOf((str == null ? 0 : Integer.valueOf(str).intValue()) + 1));
        }
        return hashMap;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("PendingTaskInfos:");
        List<PendingTaskInfo> query = GalleryEntityManager.getInstance().query(PendingTaskInfo.class, null, null);
        if (query == null || query.size() == 0) {
            printWriter.println(" current pending task size is 0");
            printWriter.println("end of PendingTaskInfos");
            return;
        }
        printWriter.println(String.format(" current pending task size is %s", Integer.valueOf(query.size())));
        for (PendingTaskInfo pendingTaskInfo : query) {
            printWriter.println(pendingTaskInfo.toString());
        }
        printWriter.println("end of PendingTaskInfos");
    }

    public final void cancelDuplicateTasks(int i, String str, boolean z, long... jArr) {
        DefaultLogger.i("PendingTaskManager", "try cancel duplicated tasks, type: %s, tag: %s", Integer.valueOf(i), str);
        List query = GalleryEntityManager.getInstance().query(PendingTaskInfo.class, TextUtils.isEmpty(str) ? String.format("%s = %s", "taskType", Integer.valueOf(i)) : String.format("%s = '%s' AND %s = %s", "taskTag", str, "taskType", Integer.valueOf(i)), null, "createTime ASC", null);
        if (!BaseMiscUtil.isValid(query)) {
            return;
        }
        HashSet hashSet = new HashSet();
        if (jArr != null && jArr.length > 0) {
            for (long j : jArr) {
                hashSet.add(Long.valueOf(j));
            }
        }
        int size = query.size() - 1;
        LinkedList linkedList = new LinkedList();
        for (int i2 = 0; i2 <= size; i2++) {
            PendingTaskInfo pendingTaskInfo = (PendingTaskInfo) query.get(i2);
            if (z && i2 == size) {
                if (pendingTaskInfo.getExpireTime() > 0 && pendingTaskInfo.getExpireTime() < System.currentTimeMillis()) {
                    linkedList.add(pendingTaskInfo);
                }
            } else if (!hashSet.contains(Long.valueOf(pendingTaskInfo.getRowId()))) {
                linkedList.add(pendingTaskInfo);
            }
        }
        cancelAll(linkedList);
    }

    public void checkTaskList() {
        boolean z;
        recordExpireCount();
        List<PendingTaskInfo> query = GalleryEntityManager.getInstance().query(PendingTaskInfo.class, String.format("%s > %s", "createTime", Long.valueOf(System.currentTimeMillis() - CoreConstants.MILLIS_IN_ONE_WEEK)), null);
        if (query == null || query.isEmpty()) {
            return;
        }
        List<JobInfo> allPendingJob = PendingTaskService.getAllPendingJob(GalleryApp.sGetAndroidContext());
        int i = 0;
        for (PendingTaskInfo pendingTaskInfo : query) {
            int generateJobId = generateJobId(pendingTaskInfo.getRowId());
            if (allPendingJob == null) {
                scheduleTask(pendingTaskInfo);
            } else {
                Iterator<JobInfo> it = allPendingJob.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().getId() == generateJobId) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (!z) {
                    i++;
                    scheduleTask(pendingTaskInfo);
                }
            }
        }
        recordRescheduleCount(i);
    }

    public void cancelAll(int i) {
        DefaultLogger.d("PendingTaskManager", "cancel all. type: %s", Integer.valueOf(i));
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        String format = String.format("%s=%s", "taskType", Integer.valueOf(i));
        List<PendingTaskInfo> query = galleryEntityManager.query(PendingTaskInfo.class, format, null);
        if (query == null || query.isEmpty()) {
            return;
        }
        for (PendingTaskInfo pendingTaskInfo : query) {
            cancelJob(pendingTaskInfo.getRowId());
        }
        galleryEntityManager.delete(PendingTaskInfo.class, format, null);
    }

    public final void cancelAll(List<PendingTaskInfo> list) {
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        for (PendingTaskInfo pendingTaskInfo : list) {
            DefaultLogger.i("PendingTaskManager", "Cancel task: %s", pendingTaskInfo);
            cancelJob(pendingTaskInfo.getRowId());
            galleryEntityManager.delete(pendingTaskInfo);
        }
    }

    public final void cancelJob(long j) {
        PendingTaskService.cancelJob(GalleryApp.sGetAndroidContext(), generateJobId(j));
    }

    public void executeTask(JobParameters jobParameters, PendingTask.Callback callback) {
        DefaultLogger.d("PendingTaskManager", "executeTask jobId: %s", Integer.valueOf(jobParameters.getJobId()));
        PersistableBundle extras = jobParameters.getExtras();
        if (extras == null) {
            return;
        }
        long j = extras.getLong("taskId", 0L);
        if (j == 0) {
            return;
        }
        executeTask(j, callback);
    }

    public void executeTask(long j, PendingTask.Callback callback) {
        DefaultLogger.d("PendingTaskManager", "executeTask taskId: %s", Long.valueOf(j));
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        PendingTaskInfo pendingTaskInfo = (PendingTaskInfo) galleryEntityManager.find(PendingTaskInfo.class, j);
        if (pendingTaskInfo == null) {
            DefaultLogger.e("PendingTaskManager", "can't find taskInfo %s", Long.valueOf(j));
            return;
        }
        int taskType = pendingTaskInfo.getTaskType();
        long expireTime = pendingTaskInfo.getExpireTime();
        long currentTimeMillis = System.currentTimeMillis();
        if (expireTime > 0 && expireTime < currentTimeMillis) {
            DefaultLogger.w("PendingTaskManager", "task %s is out of date", Integer.valueOf(taskType));
            galleryEntityManager.delete(pendingTaskInfo);
            recordDropReason(taskType, "out_date");
            return;
        }
        PendingTask create = PendingTaskFactory.create(taskType);
        if (create == null) {
            DefaultLogger.e("PendingTaskManager", "can't find PendingTask of this type %s", Integer.valueOf(taskType));
            galleryEntityManager.delete(pendingTaskInfo);
            recordDropReason(taskType, "create_task");
            return;
        }
        create.setCallback(callback);
        Object obj = null;
        try {
            obj = create.mo1252parseData(pendingTaskInfo.getData());
        } catch (Exception e) {
            DefaultLogger.e("PendingTaskManager", "task %s parseData error.\n", Integer.valueOf(taskType), e);
        }
        if (obj == null) {
            galleryEntityManager.delete(pendingTaskInfo);
            DefaultLogger.d("PendingTaskManager", "data is null");
            recordDropReason(taskType, "data_parse");
            return;
        }
        boolean z = false;
        long currentTimeMillis2 = System.currentTimeMillis();
        try {
            DefaultLogger.d("PendingTaskManager", "task %s begin process.\n", Integer.valueOf(pendingTaskInfo.getTaskType()));
            z = create.process(obj);
        } catch (Exception e2) {
            DefaultLogger.e("PendingTaskManager", "task %s process error.\n", Integer.valueOf(pendingTaskInfo.getTaskType()), e2);
        }
        long currentTimeMillis3 = System.currentTimeMillis() - currentTimeMillis2;
        recordProcessDuration(taskType, currentTimeMillis3);
        if (z) {
            pendingTaskInfo.increaseRetryTime();
            if (pendingTaskInfo.getRetryTime() >= 3) {
                DefaultLogger.w("PendingTaskManager", "task %s retry %s times, drop it!", Integer.valueOf(taskType), Integer.valueOf(pendingTaskInfo.getRetryTime()));
                galleryEntityManager.delete(pendingTaskInfo);
                recordDropReason(taskType, "over_time");
            } else {
                galleryEntityManager.update(pendingTaskInfo);
                scheduleTask(pendingTaskInfo);
            }
        } else {
            recordFinishDuration(System.currentTimeMillis() - pendingTaskInfo.getCreateTime());
            galleryEntityManager.delete(pendingTaskInfo);
        }
        DefaultLogger.d("PendingTaskManager", "execute taskId %s done, cost %s ms, need retry %s", Long.valueOf(j), Long.valueOf(currentTimeMillis3), Boolean.valueOf(z));
    }

    public final void scheduleTask(PendingTaskInfo pendingTaskInfo) {
        DefaultLogger.d("PendingTaskManager", "scheduleTask type: %s taskId: %s", Integer.valueOf(pendingTaskInfo.getTaskType()), Long.valueOf(pendingTaskInfo.getRowId()));
        ComponentName componentName = new ComponentName(GalleryApp.sGetAndroidContext(), PendingTaskService.class);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putLong("taskId", pendingTaskInfo.getRowId());
        long minLatencyMillis = pendingTaskInfo.getMinLatencyMillis() - (System.currentTimeMillis() - pendingTaskInfo.getCreateTime());
        int netType = pendingTaskInfo.getNetType();
        int i = 1;
        if (netType == 0) {
            i = 0;
        } else if (netType == 1 || netType != 2) {
            i = 2;
        }
        JobInfo.Builder requiredNetworkType = new JobInfo.Builder(generateJobId(pendingTaskInfo.getRowId()), componentName).setExtras(persistableBundle).setRequiresCharging(pendingTaskInfo.isRequireCharging()).setRequiresDeviceIdle(pendingTaskInfo.isRequireDeviceIdle()).setRequiredNetworkType(i);
        if (minLatencyMillis <= 0) {
            minLatencyMillis = 0;
        }
        try {
            PendingTaskService.scheduleJob(GalleryApp.sGetAndroidContext(), requiredNetworkType.setMinimumLatency(minLatencyMillis).build());
        } catch (Exception e) {
            DefaultLogger.e("PendingTaskManager", e);
            Map<String, String> recordAllPendingTaskInfos = recordAllPendingTaskInfos();
            HashMap hashMap = new HashMap();
            hashMap.put("error_type", new Gson().toJson(recordAllPendingTaskInfos));
            StatHelper.recordCountEvent("error_full", "pending_task_error", hashMap);
            tryFixIncorrectSchedule(pendingTaskInfo.getRowId());
        }
    }

    public final void tryFixIncorrectSchedule(long j) {
        cancelDuplicateTasks(8, null, true, j);
        cancelDuplicateTasks(7, null, true, j);
    }

    public final int generateJobId(long j) {
        return (int) ((j % 1000) + 1000);
    }

    public static void recordExpireCount() {
        long currentTimeMillis = System.currentTimeMillis() - CoreConstants.MILLIS_IN_ONE_WEEK;
        List query = GalleryEntityManager.getInstance().query(PendingTaskInfo.class, String.format("%s > %s AND %s < %s", "createTime", Long.valueOf(currentTimeMillis - 86400000), "createTime", Long.valueOf(currentTimeMillis)), null);
        if (query == null || query.size() <= 0) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, String.valueOf(query.size()));
        SamplingStatHelper.recordCountEvent("pending_task", "pending_task_expire_count", hashMap);
        DefaultLogger.w("PendingTaskManager", "%s tasks expire.", Integer.valueOf(query.size()));
    }

    public static void recordRescheduleCount(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
        SamplingStatHelper.recordCountEvent("pending_task", "pending_task_reschedule", hashMap);
    }

    public static void recordProcessDuration(int i, long j) {
        if (j < 600000) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(i));
        hashMap.put("cost_time", String.valueOf(j));
        SamplingStatHelper.recordCountEvent("pending_task", "pending_task_process_cost", hashMap);
    }

    public static void recordFinishDuration(long j) {
        float f = (((float) j) * 1.0f) / 3600000.0f;
        int generatorValueStage = SamplingStatHelper.generatorValueStage(f, TIME_STAGE);
        HashMap hashMap = new HashMap();
        hashMap.put("stage", String.valueOf(generatorValueStage));
        SamplingStatHelper.recordCountEvent("pending_task", "pending_task_finish_duration", hashMap);
        DefaultLogger.d("PendingTaskManager", "task spend %s hour since post, record stage %s.", new DecimalFormat("#.00").format(f), Integer.valueOf(generatorValueStage));
    }

    public static void recordDropReason(int i, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(i));
        hashMap.put("reason", str);
        SamplingStatHelper.recordCountEvent("pending_task", "pending_task_drop_reason", hashMap);
    }
}
