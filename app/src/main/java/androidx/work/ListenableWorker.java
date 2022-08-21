package androidx.work;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Keep;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.UUID;
import java.util.concurrent.Executor;

/* loaded from: classes.dex */
public abstract class ListenableWorker {
    public Context mAppContext;
    public boolean mRunInForeground;
    public volatile boolean mStopped;
    public boolean mUsed;
    public WorkerParameters mWorkerParams;

    public void onStopped() {
    }

    public abstract ListenableFuture<Result> startWork();

    @Keep
    @SuppressLint({"BanKeepAnnotation"})
    public ListenableWorker(Context appContext, WorkerParameters workerParams) {
        if (appContext != null) {
            if (workerParams == null) {
                throw new IllegalArgumentException("WorkerParameters is null");
            }
            this.mAppContext = appContext;
            this.mWorkerParams = workerParams;
            return;
        }
        throw new IllegalArgumentException("Application Context is null");
    }

    public final Context getApplicationContext() {
        return this.mAppContext;
    }

    public final UUID getId() {
        return this.mWorkerParams.getId();
    }

    public final Data getInputData() {
        return this.mWorkerParams.getInputData();
    }

    public ListenableFuture<ForegroundInfo> getForegroundInfoAsync() {
        SettableFuture create = SettableFuture.create();
        create.setException(new IllegalStateException("Expedited WorkRequests require a ListenableWorker to provide an implementation for `getForegroundInfoAsync()`"));
        return create;
    }

    public final boolean isStopped() {
        return this.mStopped;
    }

    public final void stop() {
        this.mStopped = true;
        onStopped();
    }

    public final boolean isUsed() {
        return this.mUsed;
    }

    public final void setUsed() {
        this.mUsed = true;
    }

    public boolean isRunInForeground() {
        return this.mRunInForeground;
    }

    public void setRunInForeground(boolean runInForeground) {
        this.mRunInForeground = runInForeground;
    }

    public Executor getBackgroundExecutor() {
        return this.mWorkerParams.getBackgroundExecutor();
    }

    public TaskExecutor getTaskExecutor() {
        return this.mWorkerParams.getTaskExecutor();
    }

    public WorkerFactory getWorkerFactory() {
        return this.mWorkerParams.getWorkerFactory();
    }

    /* loaded from: classes.dex */
    public static abstract class Result {
        public static Result success() {
            return new Success();
        }

        public static Result success(Data outputData) {
            return new Success(outputData);
        }

        public static Result retry() {
            return new Retry();
        }

        public static Result failure() {
            return new Failure();
        }

        /* loaded from: classes.dex */
        public static final class Success extends Result {
            public final Data mOutputData;

            public Success() {
                this(Data.EMPTY);
            }

            public Success(Data outputData) {
                this.mOutputData = outputData;
            }

            public Data getOutputData() {
                return this.mOutputData;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o != null && Success.class == o.getClass()) {
                    return this.mOutputData.equals(((Success) o).mOutputData);
                }
                return false;
            }

            public int hashCode() {
                return (Success.class.getName().hashCode() * 31) + this.mOutputData.hashCode();
            }

            public String toString() {
                return "Success {mOutputData=" + this.mOutputData + '}';
            }
        }

        /* loaded from: classes.dex */
        public static final class Failure extends Result {
            public final Data mOutputData;

            public Failure() {
                this(Data.EMPTY);
            }

            public Failure(Data outputData) {
                this.mOutputData = outputData;
            }

            public Data getOutputData() {
                return this.mOutputData;
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o != null && Failure.class == o.getClass()) {
                    return this.mOutputData.equals(((Failure) o).mOutputData);
                }
                return false;
            }

            public int hashCode() {
                return (Failure.class.getName().hashCode() * 31) + this.mOutputData.hashCode();
            }

            public String toString() {
                return "Failure {mOutputData=" + this.mOutputData + '}';
            }
        }

        /* loaded from: classes.dex */
        public static final class Retry extends Result {
            public String toString() {
                return "Retry";
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                return o != null && Retry.class == o.getClass();
            }

            public int hashCode() {
                return Retry.class.getName().hashCode();
            }
        }
    }
}
