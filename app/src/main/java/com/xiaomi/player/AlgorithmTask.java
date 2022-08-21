package com.xiaomi.player;

import android.util.Log;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public abstract class AlgorithmTask<Data> implements Runnable {
    public static final String TAG = AlgorithmTask.class.getSimpleName();
    private TaskCallback mCallback;
    public final String mFileId;
    private LinkedBlockingQueue<Data> mInputQueue;
    private Thread mThread;
    public videoAnalytic mVideoAnalytic;
    private volatile boolean mIsFinishing = false;
    private int mInputCount = 0;

    /* loaded from: classes3.dex */
    public interface TaskCallback {
        void onComplete();
    }

    public abstract void handleInput(Data data);

    public AlgorithmTask(videoAnalytic videoanalytic, String str, String str2, int i) {
        this.mVideoAnalytic = videoanalytic;
        this.mFileId = str;
        this.mThread = new Thread(this, str2);
        this.mInputQueue = new LinkedBlockingQueue<>(i);
    }

    public void post(Data data) throws InterruptedException {
        this.mInputQueue.put(data);
    }

    public void offer(Data data) {
        this.mInputQueue.offer(data);
    }

    public void start(TaskCallback taskCallback) {
        this.mCallback = taskCallback;
        this.mThread.start();
    }

    public void forceStop() {
        Thread thread;
        finish();
        String str = TAG;
        Log.d(str, "AlgorithmTask forceStop() mInputQueue size:" + this.mInputQueue.size());
        if (!this.mInputQueue.isEmpty() || (thread = this.mThread) == null) {
            return;
        }
        thread.interrupt();
    }

    private void finish() {
        Log.d(TAG, "AlgorithmTask finish()");
        this.mIsFinishing = true;
    }

    @Override // java.lang.Runnable
    public void run() {
        handleLoop();
        onComplete();
    }

    private void handleLoop() {
        Data data;
        while (true) {
            boolean z = false;
            if (this.mIsFinishing) {
                Data poll = this.mInputQueue.poll();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mInputQueue.poll() = ");
                sb.append((Object) (poll != null ? poll.toString() : poll));
                Log.d(str, sb.toString());
                data = poll;
                if (poll == null) {
                    z = true;
                    data = poll;
                }
            } else {
                try {
                    Data take = this.mInputQueue.take();
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mInputQueue.take() = ");
                    sb2.append((Object) (take != null ? take.toString() : take));
                    Log.d(str2, sb2.toString());
                    data = take;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (data != null) {
                this.mInputCount++;
                handleInput(data);
            }
            if (z) {
                return;
            }
        }
    }

    public int getInputCount() {
        return this.mInputCount;
    }

    public void onComplete() {
        TaskCallback taskCallback = this.mCallback;
        if (taskCallback != null) {
            taskCallback.onComplete();
        }
    }
}
