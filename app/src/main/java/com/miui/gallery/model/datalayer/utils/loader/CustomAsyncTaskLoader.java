package com.miui.gallery.model.datalayer.utils.loader;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import androidx.core.os.OperationCanceledException;
import androidx.core.util.TimeUtils;
import androidx.loader.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public abstract class CustomAsyncTaskLoader<D> extends Loader<D> {
    public volatile CustomAsyncTaskLoader<D>.LoadTask mCancellingTask;
    public final Executor mExecutor;
    public Handler mHandler;
    public boolean mIsDispatchResultToWorkThread;
    public long mLastLoadCompleteTime;
    public volatile CustomAsyncTaskLoader<D>.LoadTask mTask;
    public long mUpdateThrottle;

    public void cancelLoadInBackground() {
    }

    /* renamed from: loadInBackground */
    public abstract D mo1130loadInBackground();

    public abstract void onCanceled(D d);

    /* loaded from: classes2.dex */
    public final class LoadTask extends ModernAsyncTask<Void, D> implements Runnable {
        public final CountDownLatch mDone;
        public boolean waiting;

        public LoadTask(boolean z) {
            super(z);
            this.mDone = new CountDownLatch(1);
        }

        @Override // com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask
        public D doInBackground(Void... voidArr) {
            try {
                return (D) CustomAsyncTaskLoader.this.onLoadInBackground();
            } catch (OperationCanceledException e) {
                if (!isCancelled()) {
                    throw e;
                }
                return null;
            }
        }

        @Override // com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask
        public void onPostExecute(D d) {
            try {
                CustomAsyncTaskLoader.this.dispatchOnLoadComplete(this, d);
            } finally {
                this.mDone.countDown();
            }
        }

        @Override // com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask
        public void onCancelled(D d) {
            try {
                CustomAsyncTaskLoader.this.dispatchOnCancelled(this, d);
            } finally {
                this.mDone.countDown();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            this.waiting = false;
            CustomAsyncTaskLoader.this.executePendingTask();
        }

        @Override // com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask
        public boolean onPrePostResult(D d) {
            if (!CustomAsyncTaskLoader.this.mIsDispatchResultToWorkThread) {
                return false;
            }
            CustomAsyncTaskLoader.this.onPrePostResult(d);
            return true;
        }
    }

    public void onPrePostResult(D d) {
        super.deliverResult(d);
    }

    public CustomAsyncTaskLoader(Context context, boolean z) {
        super(context);
        this.mLastLoadCompleteTime = -10000L;
        this.mExecutor = ModernAsyncTask.THREAD_POOL_EXECUTOR;
        this.mIsDispatchResultToWorkThread = z;
    }

    @Override // androidx.loader.content.Loader
    public void onForceLoad() {
        super.onForceLoad();
        cancelLoad();
        this.mTask = new LoadTask(false);
        executePendingTask();
    }

    @Override // androidx.loader.content.Loader
    public boolean onCancelLoad() {
        if (this.mTask != null) {
            if (!isStarted()) {
                try {
                    Field declaredField = getClass().getDeclaredField("mContentChanged");
                    declaredField.setAccessible(true);
                    declaredField.set(this, Boolean.TRUE);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                }
            }
            if (this.mCancellingTask != null) {
                if (this.mTask.waiting) {
                    this.mTask.waiting = false;
                    this.mHandler.removeCallbacks(this.mTask);
                }
                this.mTask = null;
                return false;
            } else if (this.mTask.waiting) {
                this.mTask.waiting = false;
                this.mHandler.removeCallbacks(this.mTask);
                this.mTask = null;
                return false;
            } else {
                boolean cancel = this.mTask.cancel(false);
                if (cancel) {
                    this.mCancellingTask = this.mTask;
                    cancelLoadInBackground();
                }
                this.mTask = null;
                return cancel;
            }
        }
        return false;
    }

    public void executePendingTask() {
        if (this.mCancellingTask != null || this.mTask == null) {
            return;
        }
        if (this.mTask.waiting) {
            this.mTask.waiting = false;
            this.mHandler.removeCallbacks(this.mTask);
        }
        if (this.mUpdateThrottle > 0 && SystemClock.uptimeMillis() < this.mLastLoadCompleteTime + this.mUpdateThrottle) {
            this.mTask.waiting = true;
            this.mHandler.postAtTime(this.mTask, this.mLastLoadCompleteTime + this.mUpdateThrottle);
            return;
        }
        this.mTask.executeOnExecutor(this.mExecutor, null);
    }

    public void dispatchOnCancelled(CustomAsyncTaskLoader<D>.LoadTask loadTask, D d) {
        onCanceled(d);
        if (this.mCancellingTask == loadTask) {
            rollbackContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mCancellingTask = null;
            deliverCancellation();
            executePendingTask();
        }
    }

    public void dispatchOnLoadComplete(CustomAsyncTaskLoader<D>.LoadTask loadTask, D d) {
        if (this.mTask != loadTask) {
            dispatchOnCancelled(loadTask, d);
        } else if (isAbandoned()) {
            onCanceled(d);
        } else {
            commitContentChanged();
            this.mLastLoadCompleteTime = SystemClock.uptimeMillis();
            this.mTask = null;
            Boolean bool = loadTask.mPreResultIsProcessed;
            if (bool != null && bool.booleanValue()) {
                return;
            }
            deliverResult(d);
        }
    }

    public D onLoadInBackground() {
        return mo1130loadInBackground();
    }

    public boolean isLoadInBackgroundCanceled() {
        return this.mCancellingTask != null;
    }

    @Override // androidx.loader.content.Loader
    @Deprecated
    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        if (this.mTask != null) {
            printWriter.print(str);
            printWriter.print("mTask=");
            printWriter.print(this.mTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mTask.waiting);
        }
        if (this.mCancellingTask != null) {
            printWriter.print(str);
            printWriter.print("mCancellingTask=");
            printWriter.print(this.mCancellingTask);
            printWriter.print(" waiting=");
            printWriter.println(this.mCancellingTask.waiting);
        }
        if (this.mUpdateThrottle != 0) {
            printWriter.print(str);
            printWriter.print("mUpdateThrottle=");
            TimeUtils.formatDuration(this.mUpdateThrottle, printWriter);
            printWriter.print(" mLastLoadCompleteTime=");
            TimeUtils.formatDuration(this.mLastLoadCompleteTime, SystemClock.uptimeMillis(), printWriter);
            printWriter.println();
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class ModernAsyncTask<Params, Result> {
        public static final Executor THREAD_POOL_EXECUTOR;
        public static volatile Executor sDefaultExecutor;
        public static InternalHandler sHandler;
        public static final BlockingQueue<Runnable> sPoolWorkQueue;
        public static final ThreadFactory sThreadFactory;
        public final FutureTask<Result> mFuture;
        public Boolean mPreResultIsProcessed;
        public final WorkerRunnable<Params, Result> mWorker;
        public volatile Status mStatus = Status.PENDING;
        public final AtomicBoolean mCancelled = new AtomicBoolean();
        public final AtomicBoolean mTaskInvoked = new AtomicBoolean();

        /* loaded from: classes2.dex */
        public enum Status {
            PENDING,
            RUNNING,
            FINISHED
        }

        /* loaded from: classes2.dex */
        public static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
            public Params[] mParams;
        }

        public abstract Result doInBackground(Params... paramsArr);

        public void onCancelled() {
        }

        public void onPostExecute(Result result) {
        }

        public void onPreExecute() {
        }

        public abstract boolean onPrePostResult(Result result);

        static {
            ThreadFactory threadFactory = new ThreadFactory() { // from class: com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask.1
                public final AtomicInteger mCount = new AtomicInteger(1);

                @Override // java.util.concurrent.ThreadFactory
                public Thread newThread(Runnable runnable) {
                    return new Thread(runnable, "ModernAsyncTask #" + this.mCount.getAndIncrement());
                }
            };
            sThreadFactory = threadFactory;
            LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(10);
            sPoolWorkQueue = linkedBlockingQueue;
            ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 128, 1L, TimeUnit.SECONDS, linkedBlockingQueue, threadFactory);
            THREAD_POOL_EXECUTOR = threadPoolExecutor;
            sDefaultExecutor = threadPoolExecutor;
        }

        public static Handler getHandler() {
            InternalHandler internalHandler;
            synchronized (ModernAsyncTask.class) {
                if (sHandler == null) {
                    sHandler = new InternalHandler();
                }
                internalHandler = sHandler;
            }
            return internalHandler;
        }

        public ModernAsyncTask(boolean z) {
            WorkerRunnable<Params, Result> workerRunnable = new WorkerRunnable<Params, Result>() { // from class: com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask.2
                @Override // java.util.concurrent.Callable
                public Result call() throws Exception {
                    ModernAsyncTask.this.mTaskInvoked.set(true);
                    Result result = null;
                    try {
                        Process.setThreadPriority(10);
                        result = (Result) ModernAsyncTask.this.doInBackground(this.mParams);
                        Binder.flushPendingCommands();
                        return result;
                    } finally {
                    }
                }
            };
            this.mWorker = workerRunnable;
            this.mFuture = new FutureTask<Result>(workerRunnable) { // from class: com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader.ModernAsyncTask.3
                @Override // java.util.concurrent.FutureTask
                public void done() {
                    try {
                        ModernAsyncTask.this.postResultIfNotInvoked(get());
                    } catch (InterruptedException e) {
                        Log.w("AsyncTask", e);
                    } catch (CancellationException unused) {
                        ModernAsyncTask.this.postResultIfNotInvoked(null);
                    } catch (ExecutionException e2) {
                        throw new RuntimeException("An error occurred while executing doInBackground()", e2.getCause());
                    } catch (Throwable th) {
                        throw new RuntimeException("An error occurred while executing doInBackground()", th);
                    }
                }
            };
        }

        public void postResultIfNotInvoked(Result result) {
            if (!this.mTaskInvoked.get()) {
                postResult(result);
            }
        }

        public Result postResult(Result result) {
            if (isCancelled()) {
                getHandler().obtainMessage(2, new AsyncTaskResult(this, result)).sendToTarget();
            } else {
                getHandler().obtainMessage(1, new AsyncTaskResult(this, result, Boolean.valueOf(onPrePostResult(result)))).sendToTarget();
            }
            return result;
        }

        public void onCancelled(Result result) {
            onCancelled();
        }

        public final boolean isCancelled() {
            return this.mCancelled.get();
        }

        public final boolean cancel(boolean z) {
            this.mCancelled.set(true);
            return this.mFuture.cancel(z);
        }

        public final ModernAsyncTask<Params, Result> executeOnExecutor(Executor executor, Params... paramsArr) {
            if (this.mStatus != Status.PENDING) {
                int i = AnonymousClass1.$SwitchMap$com$miui$gallery$model$datalayer$utils$loader$CustomAsyncTaskLoader$ModernAsyncTask$Status[this.mStatus.ordinal()];
                if (i == 1) {
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                }
                if (i == 2) {
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
                }
                throw new IllegalStateException("We should never reach this state");
            }
            this.mStatus = Status.RUNNING;
            onPreExecute();
            this.mWorker.mParams = paramsArr;
            executor.execute(this.mFuture);
            return this;
        }

        public void finish(Result result, boolean z) {
            if (z) {
                onCancelled(result);
            } else {
                onPostExecute(result);
            }
            this.mStatus = Status.FINISHED;
        }

        public void finish(Result result) {
            finish(result, isCancelled());
        }

        /* loaded from: classes2.dex */
        public static class InternalHandler extends Handler {
            public InternalHandler() {
                super(Looper.getMainLooper());
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                AsyncTaskResult asyncTaskResult = (AsyncTaskResult) message.obj;
                int i = message.what;
                if (i != 1) {
                    if (i != 2) {
                        return;
                    }
                    asyncTaskResult.mTask.finish(asyncTaskResult.mData[0], true);
                    return;
                }
                Object[] objArr = asyncTaskResult.mData;
                if (objArr.length == 2) {
                    asyncTaskResult.mTask.mPreResultIsProcessed = (Boolean) objArr[1];
                }
                asyncTaskResult.mTask.finish(objArr[0]);
            }
        }

        /* loaded from: classes2.dex */
        public static class AsyncTaskResult<Data> {
            public final Data[] mData;
            public final ModernAsyncTask mTask;

            public AsyncTaskResult(ModernAsyncTask modernAsyncTask, Data... dataArr) {
                this.mTask = modernAsyncTask;
                this.mData = dataArr;
            }
        }
    }

    /* renamed from: com.miui.gallery.model.datalayer.utils.loader.CustomAsyncTaskLoader$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$model$datalayer$utils$loader$CustomAsyncTaskLoader$ModernAsyncTask$Status;

        static {
            int[] iArr = new int[ModernAsyncTask.Status.values().length];
            $SwitchMap$com$miui$gallery$model$datalayer$utils$loader$CustomAsyncTaskLoader$ModernAsyncTask$Status = iArr;
            try {
                iArr[ModernAsyncTask.Status.RUNNING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$model$datalayer$utils$loader$CustomAsyncTaskLoader$ModernAsyncTask$Status[ModernAsyncTask.Status.FINISHED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }
}
