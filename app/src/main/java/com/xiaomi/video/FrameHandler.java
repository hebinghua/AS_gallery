package com.xiaomi.video;

import android.util.Log;
import com.xiaomi.video.FrameInfo;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public class FrameHandler<Input extends FrameInfo, Output extends FrameInfo, R> implements Runnable {
    private static final String TAG = FrameHandler.class.getSimpleName();
    private FrameHandlerCallback mCallback;
    private Thread mHandlerThread;
    private LinkedBlockingQueue<Input> mInputQueue;
    private boolean mIsFinishing = false;
    private LinkedBlockingQueue<Output> mOutputQueue;
    private VideoProcessor mProcessor;

    /* loaded from: classes3.dex */
    public interface FrameHandlerCallback {
        void onAllFrameHanded();

        void onFrameHandled(int i);
    }

    /* loaded from: classes3.dex */
    public interface VideoProcessor<Input, Output, R> {
        Output handleFrame(Input input);

        void putResult(R r);
    }

    public FrameHandler(VideoProcessor videoProcessor, LinkedBlockingQueue<Input> linkedBlockingQueue, LinkedBlockingQueue<Output> linkedBlockingQueue2) {
        this.mProcessor = videoProcessor;
        this.mInputQueue = linkedBlockingQueue;
        this.mOutputQueue = linkedBlockingQueue2;
    }

    public void setFrameHandlerCallback(FrameHandlerCallback frameHandlerCallback) {
        this.mCallback = frameHandlerCallback;
    }

    public void handle() {
        Thread thread = new Thread(this, "handle frame");
        this.mHandlerThread = thread;
        thread.start();
    }

    public void finish() {
        Log.d(TAG, "FrameHandler finish()");
        this.mIsFinishing = true;
    }

    public void forceStop() {
        finish();
        Thread thread = this.mHandlerThread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    private void handleLoop() {
        Input poll;
        while (true) {
            boolean z = false;
            if (this.mIsFinishing) {
                poll = this.mInputQueue.poll();
                String str = TAG;
                Log.d(str, "mInputQueue.poll() = " + poll);
                if (poll == null) {
                    z = true;
                }
            } else {
                try {
                    poll = this.mInputQueue.take();
                    String str2 = TAG;
                    Log.d(str2, "mInputQueue.take() = " + poll);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (poll != null) {
                try {
                    Output handle = handle(poll);
                    FrameHandlerCallback frameHandlerCallback = this.mCallback;
                    if (frameHandlerCallback != null) {
                        frameHandlerCallback.onFrameHandled(handle.index);
                    }
                    this.mOutputQueue.put(handle);
                    String str3 = TAG;
                    Log.d(str3, "handle " + poll.index + " is over");
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
            if (z) {
                return;
            }
        }
    }

    private Output handle(Input input) {
        long currentTimeMillis = System.currentTimeMillis();
        VideoProcessor videoProcessor = this.mProcessor;
        Output output = videoProcessor != null ? (Output) videoProcessor.handleFrame(input) : null;
        String str = TAG;
        Log.d(str, "handle time : " + (System.currentTimeMillis() - currentTimeMillis));
        return output;
    }

    @Override // java.lang.Runnable
    public void run() {
        handleLoop();
        FrameHandlerCallback frameHandlerCallback = this.mCallback;
        if (frameHandlerCallback != null) {
            frameHandlerCallback.onAllFrameHanded();
        }
        Log.d(TAG, "FrameHandler loop is over");
    }
}
