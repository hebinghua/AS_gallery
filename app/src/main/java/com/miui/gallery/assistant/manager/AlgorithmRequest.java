package com.miui.gallery.assistant.manager;

import android.os.Build;
import android.os.Looper;
import com.miui.gallery.assistant.manager.request.param.RequestParams;
import com.miui.gallery.assistant.manager.result.AlgorithmResult;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/* loaded from: classes.dex */
public abstract class AlgorithmRequest<I, P extends RequestParams<I>, R extends AlgorithmResult> implements Runnable, Comparable<AlgorithmRequest> {
    public static final AtomicLong seq = new AtomicLong(0);
    public R mAlgorithmResult;
    public volatile Listener<R> mMainThreadListener;
    public final Priority mPriority;
    public P mRequestParams;
    public volatile Listener<R> mThreadListener;
    public long seqNum;
    public final String TAG = getClass().getSimpleName();
    public CountDownLatch mSyncExecuteLock = null;
    public volatile boolean mIsCanceled = false;
    public volatile State mState = State.STATE_INIT;

    /* loaded from: classes.dex */
    public interface Listener<R extends AlgorithmResult> {
        void onCancel();

        void onComputeComplete(R r);

        void onSaveComplete(R r);

        void onStart();
    }

    /* loaded from: classes.dex */
    public enum Priority {
        PRIORITY_IMMEDIATELY,
        PRIORITY_HIGH,
        PRIORITY_NORMAL,
        PRIORITY_LOW
    }

    /* loaded from: classes.dex */
    public enum State {
        STATE_INIT,
        STATE_QUEUING,
        STATE_START,
        STATE_FINISHED
    }

    public abstract void onSaveResult(R r);

    public abstract R process(I i);

    public AlgorithmRequest(Priority priority, P p) {
        this.mRequestParams = p;
        this.mPriority = priority;
    }

    public void refreshSequence() {
        this.seqNum = seq.getAndIncrement();
    }

    public void setMainThreadListener(Listener<R> listener) {
        this.mMainThreadListener = listener;
    }

    public void setThreadListener(Listener<R> listener) {
        this.mThreadListener = listener;
    }

    public R executeSync() {
        if (Looper.myLooper() == Looper.getMainLooper() || Looper.myLooper() == ThreadManager.getNetworkRequestLooper()) {
            throw new RuntimeException("executeSync could not call on main thread or request thread.");
        }
        if (this.mPriority == Priority.PRIORITY_IMMEDIATELY) {
            run();
            return this.mAlgorithmResult;
        }
        this.mSyncExecuteLock = new CountDownLatch(1);
        execute();
        try {
            DefaultLogger.d(this.TAG, "mSyncExecuteLock.lock");
            this.mSyncExecuteLock.await();
        } catch (InterruptedException unused) {
            DefaultLogger.e(this.TAG, "mSyncExecuteLock.lock failed");
        }
        return this.mAlgorithmResult;
    }

    public void execute() {
        refreshSequence();
        AlgorithmExecuteManager.getInstance().enqueue(this);
        this.mState = State.STATE_QUEUING;
    }

    public Priority getPriority() {
        return this.mPriority;
    }

    public final void onStart() {
        if (this.mThreadListener != null) {
            this.mThreadListener.onStart();
        }
        if (this.mMainThreadListener != null) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.manager.AlgorithmRequest.1
                @Override // java.lang.Runnable
                public void run() {
                    if (AlgorithmRequest.this.mMainThreadListener != null) {
                        AlgorithmRequest.this.mMainThreadListener.onStart();
                    }
                }
            });
        }
    }

    public final void releaseSyncExecuteLock() {
        if (this.mSyncExecuteLock != null) {
            DefaultLogger.d(this.TAG, "mSyncExecuteLock.unlock");
            this.mSyncExecuteLock.countDown();
        }
    }

    public void deliverResponse(final R r) {
        this.mAlgorithmResult = r;
        releaseSyncExecuteLock();
        if (this.mThreadListener != null) {
            this.mThreadListener.onComputeComplete(r);
        }
        if (this.mMainThreadListener != null) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.manager.AlgorithmRequest.2
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    if (AlgorithmRequest.this.mMainThreadListener != null) {
                        AlgorithmRequest.this.mMainThreadListener.onComputeComplete(r);
                    }
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.Runnable
    public void run() {
        if (this.mIsCanceled) {
            if (this.mThreadListener != null) {
                this.mThreadListener.onCancel();
            }
            if (this.mMainThreadListener == null) {
                return;
            }
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.manager.AlgorithmRequest.3
                @Override // java.lang.Runnable
                public void run() {
                    if (AlgorithmRequest.this.mMainThreadListener != null) {
                        AlgorithmRequest.this.mMainThreadListener.onCancel();
                    }
                }
            });
            return;
        }
        this.mState = State.STATE_START;
        onStart();
        try {
            long currentTimeMillis = System.currentTimeMillis();
            AlgorithmResult process = process(this.mRequestParams.mo564getAlgorithmRequestInputs());
            recordAlgorithmProcessTime(currentTimeMillis);
            if (process != null && process.getResultCode() == 6) {
                return;
            }
            deliverResponse(process);
            this.mState = State.STATE_FINISHED;
            String str = this.TAG;
            DefaultLogger.d(str, "%s AlgorithmRequest save result!", str);
            onSaveResult(process);
            notifySaveComplete(process);
        } catch (Exception e) {
            DefaultLogger.e(this.TAG, e);
        }
    }

    public final void recordAlgorithmProcessTime(long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        String str = this.TAG;
        DefaultLogger.d(str, "%s AlgorithmRequest process using time %d ms", str, Long.valueOf(currentTimeMillis));
        HashMap hashMap = new HashMap();
        hashMap.put("model", Build.MODEL);
        hashMap.put("elapse_time", String.valueOf((currentTimeMillis / 50) * 50));
        SamplingStatHelper.recordCountEvent("assistant", "assistant_algorithm_process_time_" + this.TAG, hashMap);
    }

    public final void notifySaveComplete(final R r) {
        if (this.mThreadListener != null) {
            this.mThreadListener.onSaveComplete(r);
        }
        if (this.mMainThreadListener != null) {
            ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.assistant.manager.AlgorithmRequest.4
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.lang.Runnable
                public void run() {
                    if (AlgorithmRequest.this.mMainThreadListener != null) {
                        AlgorithmRequest.this.mMainThreadListener.onSaveComplete(r);
                    }
                }
            });
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(AlgorithmRequest algorithmRequest) {
        int ordinal = this.mPriority.ordinal() - algorithmRequest.getPriority().ordinal();
        if (ordinal == 0) {
            return this.seqNum < algorithmRequest.seqNum ? -1 : 1;
        }
        return ordinal;
    }
}
