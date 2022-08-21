package com.xiaomi.mediacodec;

import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public class MoviePlayer {
    private static final String TAG = "MoviePlayer";
    private static final boolean VERBOSE = false;
    private MediaFormat mAudioFromate;
    private MediaCodec.BufferInfo mBufferInfo;
    private long mDurationUs;
    private boolean mEndOfDecoder;
    public FrameCallback mFrameCallback;
    private volatile boolean mIsPause;
    private volatile boolean mIsStopRequested;
    private volatile boolean mLoop;
    private int mMaxAudioSize;
    private int mMaxSize;
    private long mOutputFrames;
    private Surface mOutputSurface;
    private volatile int mSeekMode;
    private volatile long mSeekPosMS;
    private volatile boolean mSeeking;
    private File mSourceFile;
    private long mStartTime;
    private MediaFormat mVideoFromate;
    private int mVideoHeight;
    private volatile boolean mVideoOnly;
    private int mVideoWidth;
    private final Object mWaitEvent;
    private long mWrittenPresentationTimeUs;
    public int maudioTrack;
    private final Semaphore semp;

    /* loaded from: classes3.dex */
    public interface FrameCallback {
        void loopReset();

        void onAudioFormat(MediaFormat mediaFormat);

        void onAudioFrame(MediaFrame mediaFrame);

        void onDecoderFinished();

        void onStreamDuration(long j);

        void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6);

        void onVideoFrame(int i);

        void postRender();

        void preRender(long j);
    }

    /* loaded from: classes3.dex */
    public interface PlayerFeedback {
        void playbackStopped(int i);
    }

    /* loaded from: classes3.dex */
    public class MediaFrame {
        public ByteBuffer buffer;
        public MediaCodec.BufferInfo info;

        public MediaFrame() {
        }
    }

    public MediaFormat getAudioFromate() {
        return this.mAudioFromate;
    }

    public MediaFormat getVideoFromate() {
        return this.mVideoFromate;
    }

    public float[] ISO6709LocationParser(String str) {
        Pattern compile = Pattern.compile("([+\\-][0-9.]+)([+\\-][0-9.]+)");
        if (str == null) {
            return null;
        }
        Matcher matcher = compile.matcher(str);
        if (matcher.find() && matcher.groupCount() == 2) {
            try {
                return new float[]{Float.parseFloat(matcher.group(1)), Float.parseFloat(matcher.group(2))};
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    private void getMetadata() throws IOException {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(this.mSourceFile.toString());
        if (Build.VERSION.SDK_INT >= 19) {
            GlUtil.locationString = mediaMetadataRetriever.extractMetadata(23);
            Logg.LogI("get location: " + GlUtil.locationString);
            String str = GlUtil.locationString;
            if (str != null) {
                float[] ISO6709LocationParser = ISO6709LocationParser(str);
                GlUtil.location = ISO6709LocationParser;
                if (ISO6709LocationParser == null) {
                    Logg.LogI("Failed to parse the location metadata: " + GlUtil.locationString);
                }
            }
        }
        try {
            this.mDurationUs = Long.parseLong(mediaMetadataRetriever.extractMetadata(9)) * 1000;
        } catch (NumberFormatException unused) {
            this.mDurationUs = -1L;
        }
        this.mFrameCallback.onStreamDuration(this.mDurationUs);
        mediaMetadataRetriever.release();
        Logg.LogI("Duration (us): " + this.mDurationUs);
    }

    public MoviePlayer() {
        this.mBufferInfo = new MediaCodec.BufferInfo();
        this.mOutputFrames = 0L;
        this.mEndOfDecoder = false;
        this.mStartTime = 0L;
        this.mLoop = false;
        this.mIsPause = false;
        this.mSeekPosMS = 0L;
        this.mSeeking = false;
        this.mVideoOnly = false;
        this.mSeekMode = 2;
        this.mWaitEvent = new Object();
        this.semp = new Semaphore(1);
        this.mAudioFromate = null;
        this.mVideoFromate = null;
        this.mMaxSize = 0;
        this.mMaxAudioSize = 0;
        this.mDurationUs = 0L;
        this.maudioTrack = -1;
    }

    public MoviePlayer(File file, Surface surface, FrameCallback frameCallback, long j, boolean z) throws IOException {
        this.mBufferInfo = new MediaCodec.BufferInfo();
        this.mOutputFrames = 0L;
        this.mEndOfDecoder = false;
        this.mStartTime = 0L;
        this.mLoop = false;
        this.mIsPause = false;
        this.mSeekPosMS = 0L;
        this.mSeeking = false;
        this.mVideoOnly = false;
        this.mSeekMode = 2;
        this.mWaitEvent = new Object();
        this.semp = new Semaphore(1);
        MediaExtractor mediaExtractor = null;
        this.mAudioFromate = null;
        this.mVideoFromate = null;
        this.mMaxSize = 0;
        this.mMaxAudioSize = 0;
        this.mDurationUs = 0L;
        this.maudioTrack = -1;
        this.mSourceFile = file;
        this.mSeekPosMS = j;
        this.mVideoOnly = z;
        frameCallback = frameCallback == null ? new SpeedControlCallback() : frameCallback;
        Logg.LogI("  == " + file.getAbsolutePath());
        this.mOutputSurface = surface;
        this.mFrameCallback = frameCallback;
        try {
            MediaExtractor mediaExtractor2 = new MediaExtractor();
            try {
                mediaExtractor2.setDataSource(file.toString());
                if (!this.mVideoOnly) {
                    Logg.LogI("Need audio format when mVideoOnly " + this.mVideoOnly);
                    int selectTrack = selectTrack(mediaExtractor2, "audio");
                    if (selectTrack != -1) {
                        MediaFormat trackFormat = mediaExtractor2.getTrackFormat(selectTrack);
                        this.mAudioFromate = trackFormat;
                        this.mFrameCallback.onAudioFormat(trackFormat);
                    }
                }
                int selectTrack2 = selectTrack(mediaExtractor2);
                if (selectTrack2 < 0) {
                    throw new RuntimeException("No video track found in " + this.mSourceFile);
                }
                mediaExtractor2.selectTrack(selectTrack2);
                MediaFormat trackFormat2 = mediaExtractor2.getTrackFormat(selectTrack2);
                this.mVideoFromate = trackFormat2;
                GlUtil.mPictureRotation = 0;
                if (trackFormat2.containsKey("rotation-degrees")) {
                    GlUtil.mPictureRotation = trackFormat2.getInteger("rotation-degrees");
                }
                this.mVideoWidth = trackFormat2.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
                int integer = trackFormat2.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
                this.mVideoHeight = integer;
                GlUtil.mWidht = this.mVideoWidth;
                GlUtil.mHeight = integer;
                getMetadata();
                Logg.LogI(" MoviePlayer play url " + file.getAbsolutePath() + " width " + this.mVideoWidth + " height " + this.mVideoHeight + " rotation " + GlUtil.mPictureRotation);
                mediaExtractor2.release();
            } catch (Throwable th) {
                th = th;
                mediaExtractor = mediaExtractor2;
                if (mediaExtractor != null) {
                    mediaExtractor.release();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public long getVideoDuration() {
        return this.mDurationUs;
    }

    public int getVideoWidth() {
        return this.mVideoWidth;
    }

    public int getVideoHeight() {
        return this.mVideoHeight;
    }

    public void setLoopMode(boolean z) {
        Logg.LogI("MoviePlayer setLoopMode: " + z);
        this.mLoop = z;
    }

    public void frameReceived() {
        Logg.LogI("MoviePlayer frameReceived: semp.release() ");
        this.semp.release();
    }

    public boolean seekTo(long j, int i) {
        Logg.LogI("MoviePlayer seekTo: msec: " + j + " seekMode: " + i);
        this.mSeekPosMS = j;
        this.mSeeking = true;
        if (i == 0) {
            this.mSeekMode = 2;
        } else if (i == 2) {
            this.mSeekMode = 0;
        }
        return true;
    }

    public void requestStop() {
        Logg.LogI("MoviePlayer requestStop! ");
        this.mIsStopRequested = true;
        this.semp.release();
        Logg.LogI("MoviePlayer requestStop: semp.release() ");
    }

    public void requestPause() {
        Logg.LogI("MoviePlayer requestPause! ");
        this.mIsPause = true;
    }

    public void requestResume() {
        Logg.LogI("MoviePlayer requestResume! ");
        this.mIsPause = false;
        synchronized (this.mWaitEvent) {
            this.mWaitEvent.notifyAll();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x00f3 A[Catch: Exception -> 0x00ef, TRY_LEAVE, TryCatch #0 {Exception -> 0x00ef, blocks: (B:35:0x00de, B:39:0x00f3), top: B:46:0x00de }] */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00de A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void play() throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 292
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mediacodec.MoviePlayer.play():void");
    }

    private static int selectTrack(MediaExtractor mediaExtractor) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            if (mediaExtractor.getTrackFormat(i).getString("mime").startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    private static int selectTrack(MediaExtractor mediaExtractor, String str) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            String string = mediaExtractor.getTrackFormat(i).getString("mime");
            if (string.startsWith(str + h.g)) {
                return i;
            }
        }
        return -1;
    }

    public void getOneFrame() {
        if (this.mEndOfDecoder) {
            return;
        }
        synchronized (this.mWaitEvent) {
            this.mWaitEvent.notifyAll();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:123:0x03d0, code lost:
        r27.mEndOfDecoder = true;
        com.xiaomi.mediacodec.Logg.LogI(" end of decoder ");
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x03d7, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void doExtract(android.media.MediaExtractor r28, int r29, android.media.MediaCodec r30, com.xiaomi.mediacodec.MoviePlayer.FrameCallback r31) {
        /*
            Method dump skipped, instructions count: 984
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mediacodec.MoviePlayer.doExtract(android.media.MediaExtractor, int, android.media.MediaCodec, com.xiaomi.mediacodec.MoviePlayer$FrameCallback):void");
    }

    /* loaded from: classes3.dex */
    public static class PlayTask implements Runnable {
        private static final int MSG_PLAY_STOPPED = 0;
        private static final int MSG_PLAY_STOPPED_WITH_ERROR = 1;
        private boolean mDoLoop;
        private boolean mError;
        private PlayerFeedback mFeedback;
        private MoviePlayer mPlayer;
        private Thread mThread;
        private final Object mStopLock = new Object();
        private boolean mStopped = false;
        private LocalHandler mLocalHandler = new LocalHandler();

        public PlayTask(MoviePlayer moviePlayer, PlayerFeedback playerFeedback) {
            this.mError = false;
            this.mPlayer = moviePlayer;
            this.mFeedback = playerFeedback;
            this.mError = false;
        }

        public void setLoopMode(boolean z) {
            this.mDoLoop = z;
            this.mPlayer.setLoopMode(z);
        }

        public void frameReceived() {
            this.mPlayer.frameReceived();
        }

        public boolean seekTo(long j, int i) {
            return this.mPlayer.seekTo(j, i);
        }

        public void execute() {
            Thread thread = new Thread(this, "Movie Player");
            this.mThread = thread;
            thread.start();
        }

        public void requestStop() {
            Logg.LogI("playtask requestStop! ");
            this.mPlayer.requestStop();
        }

        public void waitForStop() {
            synchronized (this.mStopLock) {
                while (!this.mStopped) {
                    try {
                        this.mStopLock.wait();
                    } catch (InterruptedException unused) {
                    }
                }
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:10:0x0016, code lost:
            r1 = r5.mLocalHandler;
            r1.sendMessage(r1.obtainMessage(0, r5.mFeedback));
         */
        /* JADX WARN: Code restructure failed: missing block: B:11:0x0022, code lost:
            r0 = r5.mLocalHandler;
            r0.sendMessage(r0.obtainMessage(1, r5.mFeedback));
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0057, code lost:
            if (r5.mError != false) goto L14;
         */
        /* JADX WARN: Code restructure failed: missing block: B:26:0x005a, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:?, code lost:
            return;
         */
        /* JADX WARN: Code restructure failed: missing block: B:9:0x0014, code lost:
            if (r5.mError == false) goto L11;
         */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() {
            /*
                r5 = this;
                r0 = 0
                r1 = 1
                com.xiaomi.mediacodec.MoviePlayer r2 = r5.mPlayer     // Catch: java.lang.Throwable -> L31 java.lang.Exception -> L33
                r2.play()     // Catch: java.lang.Throwable -> L31 java.lang.Exception -> L33
                java.lang.Object r2 = r5.mStopLock
                monitor-enter(r2)
                r5.mStopped = r1     // Catch: java.lang.Throwable -> L2e
                java.lang.Object r3 = r5.mStopLock     // Catch: java.lang.Throwable -> L2e
                r3.notifyAll()     // Catch: java.lang.Throwable -> L2e
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L2e
                boolean r2 = r5.mError
                if (r2 != 0) goto L22
            L16:
                com.xiaomi.mediacodec.MoviePlayer$PlayTask$LocalHandler r1 = r5.mLocalHandler
                com.xiaomi.mediacodec.MoviePlayer$PlayerFeedback r2 = r5.mFeedback
                android.os.Message r0 = r1.obtainMessage(r0, r2)
                r1.sendMessage(r0)
                goto L5a
            L22:
                com.xiaomi.mediacodec.MoviePlayer$PlayTask$LocalHandler r0 = r5.mLocalHandler
                com.xiaomi.mediacodec.MoviePlayer$PlayerFeedback r2 = r5.mFeedback
                android.os.Message r1 = r0.obtainMessage(r1, r2)
                r0.sendMessage(r1)
                goto L5a
            L2e:
                r0 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L2e
                throw r0
            L31:
                r2 = move-exception
                goto L5e
            L33:
                r2 = move-exception
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L31
                r3.<init>()     // Catch: java.lang.Throwable -> L31
                java.lang.String r4 = "find exception at mPlayer run:"
                r3.append(r4)     // Catch: java.lang.Throwable -> L31
                r3.append(r2)     // Catch: java.lang.Throwable -> L31
                java.lang.String r2 = r3.toString()     // Catch: java.lang.Throwable -> L31
                com.xiaomi.mediacodec.Logg.LogE(r2)     // Catch: java.lang.Throwable -> L31
                r5.mError = r1     // Catch: java.lang.Throwable -> L31
                java.lang.Object r2 = r5.mStopLock
                monitor-enter(r2)
                r5.mStopped = r1     // Catch: java.lang.Throwable -> L5b
                java.lang.Object r3 = r5.mStopLock     // Catch: java.lang.Throwable -> L5b
                r3.notifyAll()     // Catch: java.lang.Throwable -> L5b
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L5b
                boolean r2 = r5.mError
                if (r2 != 0) goto L22
                goto L16
            L5a:
                return
            L5b:
                r0 = move-exception
                monitor-exit(r2)     // Catch: java.lang.Throwable -> L5b
                throw r0
            L5e:
                java.lang.Object r3 = r5.mStopLock
                monitor-enter(r3)
                r5.mStopped = r1     // Catch: java.lang.Throwable -> L85
                java.lang.Object r4 = r5.mStopLock     // Catch: java.lang.Throwable -> L85
                r4.notifyAll()     // Catch: java.lang.Throwable -> L85
                monitor-exit(r3)     // Catch: java.lang.Throwable -> L85
                boolean r3 = r5.mError
                if (r3 != 0) goto L79
                com.xiaomi.mediacodec.MoviePlayer$PlayTask$LocalHandler r1 = r5.mLocalHandler
                com.xiaomi.mediacodec.MoviePlayer$PlayerFeedback r3 = r5.mFeedback
                android.os.Message r0 = r1.obtainMessage(r0, r3)
                r1.sendMessage(r0)
                goto L84
            L79:
                com.xiaomi.mediacodec.MoviePlayer$PlayTask$LocalHandler r0 = r5.mLocalHandler
                com.xiaomi.mediacodec.MoviePlayer$PlayerFeedback r3 = r5.mFeedback
                android.os.Message r1 = r0.obtainMessage(r1, r3)
                r0.sendMessage(r1)
            L84:
                throw r2
            L85:
                r0 = move-exception
                monitor-exit(r3)     // Catch: java.lang.Throwable -> L85
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mediacodec.MoviePlayer.PlayTask.run():void");
        }

        /* loaded from: classes3.dex */
        public static class LocalHandler extends Handler {
            private LocalHandler() {
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 0) {
                    ((PlayerFeedback) message.obj).playbackStopped(0);
                } else if (i == 1) {
                    ((PlayerFeedback) message.obj).playbackStopped(1);
                } else {
                    throw new RuntimeException("Unknown msg " + i);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class SpeedControlCallback implements FrameCallback {
        private static final boolean CHECK_SLEEP_TIME = false;
        private static final long ONE_MILLION = 1000000;
        private static final String TAG = "SpeedControlCallback";
        private long mFixedFrameDurationUsec;
        private boolean mLoopReset;
        private long mPrevMonoUsec;
        private long mPrevPresentUsec;

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void onAudioFormat(MediaFormat mediaFormat) {
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void onAudioFrame(MediaFrame mediaFrame) {
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void onDecoderFinished() {
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void onStreamDuration(long j) {
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void onVideoCrop(int i, int i2, int i3, int i4, int i5, int i6) {
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void onVideoFrame(int i) {
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void postRender() {
        }

        public SpeedControlCallback() {
        }

        public void setFixedPlaybackRate(int i) {
            this.mFixedFrameDurationUsec = ONE_MILLION / i;
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void preRender(long j) {
            long j2 = 0;
            if (this.mPrevMonoUsec == 0) {
                this.mPrevMonoUsec = System.nanoTime() / 1000;
                this.mPrevPresentUsec = j;
                return;
            }
            if (this.mLoopReset) {
                this.mPrevPresentUsec = j - 33333;
                this.mLoopReset = false;
            }
            long j3 = this.mFixedFrameDurationUsec;
            if (j3 == 0) {
                j3 = j - this.mPrevPresentUsec;
            }
            int i = (j3 > 0L ? 1 : (j3 == 0L ? 0 : -1));
            if (i < 0) {
                Log.w(TAG, "Weird, video times went backward");
            } else {
                if (i == 0) {
                    Logg.LogI("Warning: current frame and previous frame had same timestamp");
                } else if (j3 > 10000000) {
                    Logg.LogI("Inter-frame pause was " + (j3 / ONE_MILLION) + "sec, capping at 5 sec");
                    j2 = 5000000;
                }
                j2 = j3;
            }
            long j4 = this.mPrevMonoUsec + j2;
            long nanoTime = System.nanoTime();
            while (true) {
                long j5 = nanoTime / 1000;
                if (j5 < j4 - 100) {
                    long j6 = j4 - j5;
                    if (j6 > 500000) {
                        j6 = 500000;
                    }
                    try {
                        Thread.sleep(j6 / 1000, ((int) (j6 % 1000)) * 1000);
                    } catch (InterruptedException unused) {
                    }
                    nanoTime = System.nanoTime();
                } else {
                    this.mPrevMonoUsec += j2;
                    this.mPrevPresentUsec += j2;
                    return;
                }
            }
        }

        @Override // com.xiaomi.mediacodec.MoviePlayer.FrameCallback
        public void loopReset() {
            this.mLoopReset = true;
        }
    }
}
