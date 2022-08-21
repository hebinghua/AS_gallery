package com.xiaomi.player;

import android.util.Log;
import com.xiaomi.player.AlgorithmTask;
import com.xiaomi.player.videoAnalytic;
import com.xiaomi.stat.d;
import com.xiaomi.video.DecoderConfig;
import com.xiaomi.video.FrameInfo;
import com.xiaomi.video.VideoDecoder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public class VideoAnalyticManager implements VideoDecoder.DecodeCallback {
    private static final int MAX_FRAME_CACHE = 2;
    private static final int MAX_VIDEO_HEIGHT = 4320;
    private static final int MAX_VIDEO_WIDTH = 7680;
    private static final String TAG = "VideoAnalyticManager";
    private long convert_totaltime;
    private long end_time;
    private int mCount;
    private VideoDecoder mDecoder;
    private String mFile;
    private long mFilterTime;
    private LinkedBlockingQueue<FrameInfo> mFrameQueue;
    private boolean mIsFaceCluster;
    private boolean mIsRealT;
    private boolean mIsTag;
    private String mMediaName;
    private List<AlgorithmTask<Integer>> mTaskList;
    private videoAnalytic mVideoAnalytic;
    private VideoAnalyticWorker mWorker;
    private long start_time;

    public VideoAnalyticManager(videoAnalytic videoanalytic, String str, boolean z, boolean z2, boolean z3, long j) {
        this(videoanalytic, new File(str), z, z2, z3, j);
    }

    public VideoAnalyticManager(videoAnalytic videoanalytic, File file, boolean z, boolean z2, boolean z3, long j) {
        this.mFrameQueue = new LinkedBlockingQueue<>(2);
        this.mTaskList = new ArrayList();
        this.mCount = 0;
        this.start_time = 0L;
        this.end_time = 0L;
        this.convert_totaltime = 0L;
        this.mVideoAnalytic = videoanalytic;
        this.mIsTag = z2;
        this.mIsFaceCluster = z;
        this.mIsRealT = z3;
        this.mFilterTime = j;
        this.mFile = file.getAbsolutePath();
        long currentTimeMillis = System.currentTimeMillis();
        this.mMediaName = this.mVideoAnalytic.getMediaName(file.getPath());
        String str = TAG;
        Log.d(str, "getMediaName time:" + (System.currentTimeMillis() - currentTimeMillis) + d.H);
        VideoDecoder videoDecoder = new VideoDecoder(file, FrameInfo.class, true, this.mIsRealT, j);
        this.mDecoder = videoDecoder;
        videoDecoder.setDecodeCallback(this);
        this.mDecoder.setProcessQueue(this.mFrameQueue);
        this.mWorker = new VideoAnalyticWorker(this.mFrameQueue);
        setupTasks(videoanalytic, file, z, z2);
    }

    private void setupTasks(videoAnalytic videoanalytic, File file, boolean z, boolean z2) {
        if (z) {
            this.mTaskList.add(new FaceClusterTask(videoanalytic, file.getPath()));
        }
        if (z2) {
            this.mTaskList.add(new VideoTagTask(videoanalytic, file.getPath()));
        }
    }

    public videoAnalytic.VideoTag.TagNode[] analytic() {
        String str = TAG;
        Log.d(str, "analytic mMediaName:" + this.mMediaName);
        if (this.mDecoder.decode(new DecoderConfig.Builder().setMax(MAX_VIDEO_WIDTH, MAX_VIDEO_HEIGHT).setColorFormat(21).setMediaName(this.mMediaName).build())) {
            this.mWorker.start();
            final CountDownLatch countDownLatch = new CountDownLatch(this.mTaskList.size());
            AlgorithmTask.TaskCallback taskCallback = new AlgorithmTask.TaskCallback() { // from class: com.xiaomi.player.VideoAnalyticManager.1
                @Override // com.xiaomi.player.AlgorithmTask.TaskCallback
                public void onComplete() {
                    countDownLatch.countDown();
                }
            };
            for (AlgorithmTask<Integer> algorithmTask : this.mTaskList) {
                algorithmTask.start(taskCallback);
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mDecoder.release();
            for (AlgorithmTask<Integer> algorithmTask2 : this.mTaskList) {
                if (algorithmTask2 instanceof VideoTagTask) {
                    return ((VideoTagTask) algorithmTask2).getTagNodes();
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.xiaomi.video.VideoDecoder.DecodeCallback
    public void onFinishDecode() {
        this.mWorker.forceStop();
    }

    @Override // com.xiaomi.video.VideoDecoder.DecodeCallback
    public void onDecoderError(int i) {
        this.mWorker.forceStop();
    }

    @Override // com.xiaomi.video.VideoDecoder.DecodeCallback
    public int onDecodeFrame(FrameInfo frameInfo) {
        return this.mVideoAnalytic.cacheFrame(frameInfo);
    }

    /* loaded from: classes3.dex */
    public class VideoAnalyticWorker implements Runnable {
        private LinkedBlockingQueue<FrameInfo> mInputQueue;
        private volatile boolean mIsFinishing;
        private volatile Boolean mIsTaking;
        private Thread mThread;

        private VideoAnalyticWorker(LinkedBlockingQueue<FrameInfo> linkedBlockingQueue) {
            this.mIsFinishing = false;
            this.mIsTaking = Boolean.FALSE;
            this.mInputQueue = linkedBlockingQueue;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Thread start() {
            Thread thread = new Thread(this, "work");
            this.mThread = thread;
            thread.start();
            return this.mThread;
        }

        private void finish() {
            Log.d(VideoAnalyticManager.TAG, "FrameHandler finish()");
            this.mIsFinishing = true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void forceStop() {
            finish();
            String str = VideoAnalyticManager.TAG;
            Log.d(str, "VideoAnalyticWorker forceStop mInputQueue size:" + this.mInputQueue.size());
            if (!this.mInputQueue.isEmpty() || this.mThread == null || !this.mIsTaking.booleanValue()) {
                return;
            }
            this.mThread.interrupt();
        }

        /* JADX WARN: Removed duplicated region for block: B:20:0x0067  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x00f8 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:41:0x0002 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void handleLoop() {
            /*
                Method dump skipped, instructions count: 279
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.player.VideoAnalyticManager.VideoAnalyticWorker.handleLoop():void");
        }

        @Override // java.lang.Runnable
        public void run() {
            handleLoop();
            for (AlgorithmTask algorithmTask : VideoAnalyticManager.this.mTaskList) {
                algorithmTask.forceStop();
            }
            Log.d(VideoAnalyticManager.TAG, "FrameHandler loop is over");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getAvergeTime() {
        if (this.mCount == 0) {
            this.start_time = System.currentTimeMillis();
        }
        if (this.mCount == 500) {
            this.end_time = System.currentTimeMillis();
            Log.d(TAG, "Avergetime is " + ((this.end_time - this.start_time) / 500));
        }
        this.mCount++;
    }
}
