package com.xiaomi.video;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;
import android.view.Surface;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public class VideoEncoder implements Runnable {
    private static final int FRAME_RATE = 30;
    private static final int IFRAME_INTERVAL = 1;
    private static final String TAG = VideoEncoder.class.getSimpleName();
    private EncodeCallback mCallback;
    private LinkedBlockingQueue<FrameInfo> mInputQueue;
    private MediaInfo mMediaInfo;
    private File mOutputFile;
    private VideoDecoder mVideoDecoder;
    private MediaCodec mMediaCodec = null;
    private MediaMuxer mMediaMuxer = null;
    private Thread mEnocdeThread = null;
    private volatile boolean mIsFinishing = false;
    private boolean mMuxerStarted = false;
    private int mTrackIndex = 0;
    private boolean mIsStart = true;

    /* loaded from: classes3.dex */
    public interface EncodeCallback {
        void onEncodeFinish();

        void onEncodeFrame(long j);

        void onEncodeStart();
    }

    public VideoEncoder(File file, VideoDecoder videoDecoder) {
        this.mOutputFile = file;
        this.mVideoDecoder = videoDecoder;
    }

    public VideoEncoder(LinkedBlockingQueue<FrameInfo> linkedBlockingQueue, File file, MediaInfo mediaInfo) {
        this.mInputQueue = linkedBlockingQueue;
        this.mOutputFile = file;
        this.mMediaInfo = mediaInfo;
    }

    public void setEncodeCallback(EncodeCallback encodeCallback) {
        this.mCallback = encodeCallback;
    }

    public boolean encode() {
        if (this.mMediaInfo == null || this.mInputQueue == null) {
            VideoDecoder videoDecoder = this.mVideoDecoder;
            if (videoDecoder != null) {
                this.mInputQueue = videoDecoder.getFrameQueue();
                this.mMediaInfo = this.mVideoDecoder.getMediaInfo();
            }
            if (this.mMediaInfo == null || this.mInputQueue == null) {
                return false;
            }
        }
        if (this.mEnocdeThread == null) {
            Thread thread = new Thread(this, "encode");
            this.mEnocdeThread = thread;
            thread.start();
            return true;
        }
        return true;
    }

    public void forceStop() {
        finish();
        Thread thread = this.mEnocdeThread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    private MediaFormat initMediaFormat() {
        MediaInfo mediaInfo = this.mMediaInfo;
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", mediaInfo.width, mediaInfo.height);
        createVideoFormat.setInteger("color-format", 2135033992);
        createVideoFormat.setInteger("bitrate", 2500000);
        createVideoFormat.setInteger("frame-rate", this.mMediaInfo.frameRate);
        createVideoFormat.setInteger("i-frame-interval", 1);
        return createVideoFormat;
    }

    @Override // java.lang.Runnable
    public void run() {
        MediaFormat initMediaFormat = initMediaFormat();
        try {
            this.mMediaCodec = MediaCodec.createEncoderByType("video/avc");
            MediaMuxer mediaMuxer = new MediaMuxer(this.mOutputFile.getAbsolutePath(), 0);
            this.mMediaMuxer = mediaMuxer;
            mediaMuxer.setOrientationHint(this.mMediaInfo.rotation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.mMediaCodec == null || this.mMediaMuxer == null) {
            return;
        }
        EncodeCallback encodeCallback = this.mCallback;
        if (encodeCallback != null) {
            encodeCallback.onEncodeStart();
        }
        this.mMediaCodec.configure(initMediaFormat, (Surface) null, (MediaCrypto) null, 1);
        this.mMediaCodec.start();
        videoEncode();
        release();
        Log.d(TAG, "encode is finished.");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x009d, code lost:
        r17 = computePresentationTime(r5);
        r8 = r20.mMediaCodec.getInputBuffer(r14);
        r8.clear();
        r9 = r7.data;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00ac, code lost:
        if (r9 != null) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00ae, code lost:
        r7.buffer.rewind();
        r8.put(r7.buffer);
        r8.rewind();
        r8.flip();
        r20.mMediaCodec.queueInputBuffer(r14, 0, r7.bufferSize, r17, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00cb, code lost:
        r8.put(r9);
        r20.mMediaCodec.queueInputBuffer(r14, 0, r7.data.length, r17, 0);
     */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0108 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0095 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void videoEncode() {
        /*
            Method dump skipped, instructions count: 329
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.video.VideoEncoder.videoEncode():void");
    }

    private long computePresentationTime(long j) {
        return ((j * 1000000) / this.mMediaInfo.frameRate) + 132;
    }

    private void drainEncoder(boolean z, MediaCodec.BufferInfo bufferInfo, long j) {
        if (z) {
            try {
                this.mMediaCodec.signalEndOfInputStream();
            } catch (Exception unused) {
            }
        }
        EncodeCallback encodeCallback = this.mCallback;
        if (encodeCallback != null) {
            encodeCallback.onEncodeFrame(j);
        }
        while (true) {
            int dequeueOutputBuffer = this.mMediaCodec.dequeueOutputBuffer(bufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
            if (dequeueOutputBuffer == -1) {
                if (!z) {
                    return;
                }
                Log.i(TAG, "no output available, spinning to await EOS");
            } else if (dequeueOutputBuffer == -2) {
                if (this.mMuxerStarted) {
                    throw new RuntimeException("format changed twice");
                }
                this.mTrackIndex = this.mMediaMuxer.addTrack(this.mMediaCodec.getOutputFormat());
                this.mMediaMuxer.start();
                this.mMuxerStarted = true;
            } else if (dequeueOutputBuffer < 0) {
                String str = TAG;
                Log.i(str, "unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
            } else {
                ByteBuffer outputBuffer = this.mMediaCodec.getOutputBuffer(dequeueOutputBuffer);
                if (outputBuffer == null) {
                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                }
                if ((bufferInfo.flags & 2) != 0) {
                    Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    bufferInfo.size = 0;
                }
                if (bufferInfo.size != 0) {
                    if (!this.mMuxerStarted) {
                        throw new RuntimeException("muxer hasn't started");
                    }
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    if (this.mIsStart) {
                        bufferInfo.flags = 1;
                    }
                    this.mIsStart = false;
                    String str2 = TAG;
                    Log.d(str2, "BufferInfo: " + bufferInfo.offset + "," + bufferInfo.size + "," + bufferInfo.presentationTimeUs);
                    try {
                        Log.i(str2, "writeSampleData index = " + j);
                        this.mMediaMuxer.writeSampleData(this.mTrackIndex, outputBuffer, bufferInfo);
                    } catch (Exception unused2) {
                        Log.i(TAG, "Too many frames");
                    }
                }
                this.mMediaCodec.releaseOutputBuffer(dequeueOutputBuffer, false);
                if ((bufferInfo.flags & 4) != 0) {
                    if (!z) {
                        Log.i(TAG, "reached end of stream unexpectedly");
                        return;
                    } else {
                        Log.i(TAG, "end of stream reached");
                        return;
                    }
                }
            }
        }
    }

    public void finish() {
        Log.d(TAG, "VideoEncoder finish()");
        this.mIsFinishing = true;
    }

    private void release() {
        MediaMuxer mediaMuxer = this.mMediaMuxer;
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            this.mMediaMuxer.release();
        }
        MediaCodec mediaCodec = this.mMediaCodec;
        if (mediaCodec != null) {
            mediaCodec.flush();
            this.mMediaCodec.stop();
            this.mMediaCodec.release();
        }
        EncodeCallback encodeCallback = this.mCallback;
        if (encodeCallback != null) {
            encodeCallback.onEncodeFinish();
        }
    }
}
