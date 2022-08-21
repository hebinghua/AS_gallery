package com.miui.gallery.pendingtask.base;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.pendingtask.base.PendingTask;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class PendingTaskService extends JobService {
    public Map<Integer, Future> mRunningQueue = null;
    public ThreadPool mThreadPool;

    public static void scheduleJob(Context context, JobInfo jobInfo) {
        DefaultLogger.i("PendingTaskService", "scheduleJob jobId: %s", Integer.valueOf(jobInfo.getId()));
        ((JobScheduler) context.getSystemService("jobscheduler")).schedule(jobInfo);
    }

    public static List<JobInfo> getAllPendingJob(Context context) {
        return ((JobScheduler) context.getSystemService("jobscheduler")).getAllPendingJobs();
    }

    public static void cancelJob(Context context, int i) {
        ((JobScheduler) context.getSystemService("jobscheduler")).cancel(i);
    }

    /* loaded from: classes2.dex */
    public class PendingTaskJob implements ThreadPool.Job<Void> {
        public JobParameters mParams;

        public PendingTaskJob(JobParameters jobParameters) {
            this.mParams = jobParameters;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run  reason: collision with other method in class */
        public Void mo1807run(final ThreadPool.JobContext jobContext) {
            PendingTaskManager.getInstance().executeTask(this.mParams, new PendingTask.Callback() { // from class: com.miui.gallery.pendingtask.base.PendingTaskService.PendingTaskJob.1
                @Override // com.miui.gallery.pendingtask.base.PendingTask.Callback
                public boolean isCancelled() {
                    return jobContext.isCancelled();
                }
            });
            PendingTaskService.this.jobFinished(this.mParams, false);
            PendingTaskService.this.mRunningQueue.remove(Integer.valueOf(this.mParams.getJobId()));
            return null;
        }
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        DefaultLogger.d("PendingTaskService", "onCreate");
        this.mThreadPool = new ThreadPool(2, 4, "PendingTask");
        this.mRunningQueue = new ConcurrentHashMap();
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(JobParameters jobParameters) {
        DefaultLogger.d("PendingTaskService", "onStartJob jobId: %s", Integer.valueOf(jobParameters.getJobId()));
        this.mRunningQueue.put(Integer.valueOf(jobParameters.getJobId()), this.mThreadPool.submit(new PendingTaskJob(jobParameters)));
        return true;
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        DefaultLogger.d("PendingTaskService", "onStopJob jobId: %s", Integer.valueOf(jobParameters.getJobId()));
        Future remove = this.mRunningQueue.remove(Integer.valueOf(jobParameters.getJobId()));
        if (remove != null) {
            remove.cancel();
            return false;
        }
        return false;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        DefaultLogger.d("PendingTaskService", "onDestroy");
        this.mThreadPool.shutdown();
    }
}
