package androidx.work;

import android.os.Build;
import androidx.work.impl.DefaultRunnableScheduler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes.dex */
public final class Configuration {
    public final String mDefaultProcessName;
    public final InitializationExceptionHandler mExceptionHandler;
    public final Executor mExecutor;
    public final InputMergerFactory mInputMergerFactory;
    public final boolean mIsUsingDefaultTaskExecutor;
    public final int mLoggingLevel;
    public final int mMaxJobSchedulerId;
    public final int mMaxSchedulerLimit;
    public final int mMinJobSchedulerId;
    public final RunnableScheduler mRunnableScheduler;
    public final Executor mTaskExecutor;
    public final WorkerFactory mWorkerFactory;

    /* loaded from: classes.dex */
    public interface Provider {
        Configuration getWorkManagerConfiguration();
    }

    public Configuration(Builder builder) {
        Executor executor = builder.mExecutor;
        if (executor == null) {
            this.mExecutor = createDefaultExecutor(false);
        } else {
            this.mExecutor = executor;
        }
        Executor executor2 = builder.mTaskExecutor;
        if (executor2 == null) {
            this.mIsUsingDefaultTaskExecutor = true;
            this.mTaskExecutor = createDefaultExecutor(true);
        } else {
            this.mIsUsingDefaultTaskExecutor = false;
            this.mTaskExecutor = executor2;
        }
        WorkerFactory workerFactory = builder.mWorkerFactory;
        if (workerFactory == null) {
            this.mWorkerFactory = WorkerFactory.getDefaultWorkerFactory();
        } else {
            this.mWorkerFactory = workerFactory;
        }
        InputMergerFactory inputMergerFactory = builder.mInputMergerFactory;
        if (inputMergerFactory == null) {
            this.mInputMergerFactory = InputMergerFactory.getDefaultInputMergerFactory();
        } else {
            this.mInputMergerFactory = inputMergerFactory;
        }
        RunnableScheduler runnableScheduler = builder.mRunnableScheduler;
        if (runnableScheduler == null) {
            this.mRunnableScheduler = new DefaultRunnableScheduler();
        } else {
            this.mRunnableScheduler = runnableScheduler;
        }
        this.mLoggingLevel = builder.mLoggingLevel;
        this.mMinJobSchedulerId = builder.mMinJobSchedulerId;
        this.mMaxJobSchedulerId = builder.mMaxJobSchedulerId;
        this.mMaxSchedulerLimit = builder.mMaxSchedulerLimit;
        this.mExceptionHandler = builder.mExceptionHandler;
        this.mDefaultProcessName = builder.mDefaultProcessName;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    public Executor getTaskExecutor() {
        return this.mTaskExecutor;
    }

    public WorkerFactory getWorkerFactory() {
        return this.mWorkerFactory;
    }

    public InputMergerFactory getInputMergerFactory() {
        return this.mInputMergerFactory;
    }

    public RunnableScheduler getRunnableScheduler() {
        return this.mRunnableScheduler;
    }

    public int getMinimumLoggingLevel() {
        return this.mLoggingLevel;
    }

    public int getMinJobSchedulerId() {
        return this.mMinJobSchedulerId;
    }

    public int getMaxJobSchedulerId() {
        return this.mMaxJobSchedulerId;
    }

    public String getDefaultProcessName() {
        return this.mDefaultProcessName;
    }

    public int getMaxSchedulerLimit() {
        if (Build.VERSION.SDK_INT == 23) {
            return this.mMaxSchedulerLimit / 2;
        }
        return this.mMaxSchedulerLimit;
    }

    public InitializationExceptionHandler getExceptionHandler() {
        return this.mExceptionHandler;
    }

    public final Executor createDefaultExecutor(boolean isTaskExecutor) {
        return Executors.newFixedThreadPool(Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)), createDefaultThreadFactory(isTaskExecutor));
    }

    public final ThreadFactory createDefaultThreadFactory(final boolean isTaskExecutor) {
        return new ThreadFactory() { // from class: androidx.work.Configuration.1
            public final AtomicInteger mThreadCount = new AtomicInteger(0);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                String str = isTaskExecutor ? "WM.task-" : "androidx.work-";
                return new Thread(runnable, str + this.mThreadCount.incrementAndGet());
            }
        };
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public String mDefaultProcessName;
        public InitializationExceptionHandler mExceptionHandler;
        public Executor mExecutor;
        public InputMergerFactory mInputMergerFactory;
        public RunnableScheduler mRunnableScheduler;
        public Executor mTaskExecutor;
        public WorkerFactory mWorkerFactory;
        public int mLoggingLevel = 4;
        public int mMinJobSchedulerId = 0;
        public int mMaxJobSchedulerId = Integer.MAX_VALUE;
        public int mMaxSchedulerLimit = 20;

        public Builder setJobSchedulerJobIdRange(int minJobSchedulerId, int maxJobSchedulerId) {
            if (maxJobSchedulerId - minJobSchedulerId < 1000) {
                throw new IllegalArgumentException("WorkManager needs a range of at least 1000 job ids.");
            }
            this.mMinJobSchedulerId = minJobSchedulerId;
            this.mMaxJobSchedulerId = maxJobSchedulerId;
            return this;
        }

        public Builder setMaxSchedulerLimit(int maxSchedulerLimit) {
            if (maxSchedulerLimit < 20) {
                throw new IllegalArgumentException("WorkManager needs to be able to schedule at least 20 jobs in JobScheduler.");
            }
            this.mMaxSchedulerLimit = Math.min(maxSchedulerLimit, 50);
            return this;
        }

        public Builder setMinimumLoggingLevel(int loggingLevel) {
            this.mLoggingLevel = loggingLevel;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }
}
