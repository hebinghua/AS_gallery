package com.xiaomi.video;

import android.media.Image;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import ch.qos.logback.classic.net.SyslogAppender;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.utils.CodecUtils;
import com.xiaomi.utils.ImageUtils;
import com.xiaomi.utils.TimeUtils;
import com.xiaomi.video.FrameInfo;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes3.dex */
public class VideoDecoder<Input extends FrameInfo> implements Runnable {
    private static final long DEFAULT_TIMEOUT_US = 10000;
    private static final String TAG = "VideoProcessService";
    private int decodeColorFormat;
    public boolean is_only_decode_key_frame;
    private DecodeCallback mCallback;
    private long[] mClipRange;
    private Thread mDeocdeThread;
    private MediaExtractor mExtractor;
    private long mFilterTime;
    private VideoDecoder<Input>.FrameFormat mFrameFormat;
    private int mFrameInterval;
    private Class<Input> mInputClass;
    private boolean mIsRealT;
    private MediaFormat mMediaFormat;
    private MediaInfo mMediaInfo;
    private LinkedBlockingQueue<Input> mQueue;
    private boolean mStopDecode;
    private boolean mUseBuffer;
    private long mVideoDuration;
    private File mVideoFile;

    /* loaded from: classes3.dex */
    public interface DecodeCallback {
        public static final int ERROR_DEQUEUE_INPUT_BUFFER = -1001;
        public static final int ERROR_DEQUEUE_OUTPUT_BUFFER = -1003;
        public static final int ERROR_EXTRACTOR_SAMPLE_FLAG = -1008;
        public static final int ERROR_GET_INPUT_BUFFER = -1005;
        public static final int ERROR_GET_OUTPUT_BUFFER = -1006;
        public static final int ERROR_INVALID_CLIP_RANGE = -1007;
        public static final int ERROR_MEDIACODEC_ERROR = -1009;
        public static final int ERROR_QUEUE_INPUT_BUFFER = -1002;
        public static final int ERROR_RELEASE_OUTPUT_BUFFER = -1004;
        public static final int ERROR_START = -1000;
        public static final int SUCCESS = 0;

        int onDecodeFrame(FrameInfo frameInfo);

        void onDecoderError(int i);

        void onFinishDecode();
    }

    public MediaExtractor getExtractor() {
        return this.mExtractor;
    }

    public void setDecodeCallback(DecodeCallback decodeCallback) {
        this.mCallback = decodeCallback;
    }

    public VideoDecoder(String str, Class cls, boolean z, boolean z2, long j) {
        this(new File(str), cls, z, z2, j);
    }

    public VideoDecoder(File file, Class cls, boolean z, boolean z2, long j) {
        this.is_only_decode_key_frame = false;
        this.mFrameInterval = 1;
        this.decodeColorFormat = 2135033992;
        this.mExtractor = null;
        this.mMediaFormat = null;
        this.mUseBuffer = true;
        this.mStopDecode = false;
        this.mIsRealT = false;
        this.mFilterTime = 5000000L;
        this.mVideoFile = file;
        this.mInputClass = cls;
        this.mUseBuffer = z;
        this.mIsRealT = z2;
        this.mFilterTime = j;
    }

    public File getVideoFile() {
        return this.mVideoFile;
    }

    public LinkedBlockingQueue<Input> getFrameQueue() {
        return this.mQueue;
    }

    public MediaInfo getMediaInfo() {
        return this.mMediaInfo;
    }

    public void setFrameInterval(int i) {
        if (i > 0) {
            this.mFrameInterval = i;
        } else {
            Log.e(TAG, "frame interval must be postive number");
        }
    }

    public void setProcessQueue(LinkedBlockingQueue<Input> linkedBlockingQueue) {
        this.mQueue = linkedBlockingQueue;
    }

    public void stopDecode() {
        this.mStopDecode = true;
    }

    public boolean clip(double d, double d2) {
        this.mClipRange = new long[]{TimeUtils.convertSec2TimeStamp(d), TimeUtils.convertSec2TimeStamp(d2)};
        return decode();
    }

    private boolean checkConfig(DecoderConfig decoderConfig) {
        if (decoderConfig != null) {
            VideoDecoder<Input>.FrameFormat frameFormat = this.mFrameFormat;
            int i = frameFormat.width;
            int i2 = frameFormat.height;
            if (decoderConfig.hasMaxSize()) {
                int max = Math.max(i, i2);
                int min = Math.min(i, i2);
                int max2 = Math.max(decoderConfig.maxWidth, decoderConfig.maxHeight);
                int min2 = Math.min(decoderConfig.maxWidth, decoderConfig.maxHeight);
                if (max >= max2 || min >= min2) {
                    return false;
                }
            }
            if (decoderConfig.hasMediaName() && !CodecUtils.isSupport(decoderConfig.mediaName, i, i2)) {
                return false;
            }
        }
        return true;
    }

    public boolean decode() {
        return decode(null);
    }

    public boolean decode(DecoderConfig decoderConfig) {
        int selectTrack;
        MediaExtractor mediaExtractor = new MediaExtractor();
        this.mExtractor = mediaExtractor;
        try {
            mediaExtractor.setDataSource(this.mVideoFile.getAbsolutePath());
            selectTrack = selectTrack(this.mExtractor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (selectTrack < 0) {
            Log.e(TAG, "No video track found in " + this.mVideoFile.getAbsolutePath());
            return false;
        }
        this.mExtractor.selectTrack(selectTrack);
        MediaFormat trackFormat = this.mExtractor.getTrackFormat(selectTrack);
        this.mMediaFormat = trackFormat;
        trackFormat.setInteger("video-analysis-mode", 1);
        this.mFrameFormat = new FrameFormat(this.mMediaFormat);
        if (checkConfig(decoderConfig)) {
            int i = decoderConfig.colorFormat;
            if (i >= 0) {
                this.decodeColorFormat = i;
            }
            if (this.mMediaFormat.containsKey("durationUs")) {
                long j = this.mMediaFormat.getLong("durationUs");
                this.mVideoDuration = j;
                if (j > this.mFilterTime && this.mIsRealT) {
                    this.is_only_decode_key_frame = true;
                }
            }
            Log.i(TAG, "video path " + this.mVideoFile.getAbsolutePath() + "rotation " + this.mFrameFormat.rotation + " mVideoDuration " + this.mVideoDuration + " mFrameRate " + this.mFrameFormat.frameRate + " is_only_decode_key_frame " + this.is_only_decode_key_frame);
            if (this.mDeocdeThread == null) {
                Thread thread = new Thread(this, "decode");
                this.mDeocdeThread = thread;
                thread.start();
            }
            MediaFormat mediaFormat = this.mMediaFormat;
            if (mediaFormat == null) {
                return false;
            }
            this.mMediaInfo = new MediaInfo(mediaFormat);
            return true;
        }
        Log.i(TAG, "Decode error,colorfomat < 0");
        return false;
    }

    public void forceStop() {
        stopDecode();
        Thread thread = this.mDeocdeThread;
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            videoDecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void videoDecode() throws Exception {
        Log.d(TAG, "videoDecode() start");
        MediaCodec mediaCodec = null;
        int i = 0;
        try {
            try {
            } catch (Exception e) {
                e.printStackTrace();
                if (0 != 0) {
                    if (0 == 0) {
                        try {
                            mediaCodec.stop();
                        } catch (IllegalStateException e2) {
                            e = e2;
                            e.printStackTrace();
                            mediaCodec.release();
                            Log.d(TAG, "videoDecode() end");
                        }
                    }
                }
            }
            if (!this.mVideoFile.exists()) {
                Log.i(TAG, "videoFilePath is not exist");
                return;
            }
            String string = this.mMediaFormat.getString("mime");
            try {
                mediaCodec = MediaCodec.createDecoderByType(string);
            } catch (IllegalArgumentException e3) {
                Log.e(TAG, "videoDecode createDecoderByType error: " + e3.getMessage());
                callbackError(DecodeCallback.ERROR_MEDIACODEC_ERROR);
            }
            showSupportedColorFormat(mediaCodec.getCodecInfo().getCapabilitiesForType(string));
            if (isColorFormatSupported(this.decodeColorFormat, mediaCodec.getCodecInfo().getCapabilitiesForType(string))) {
                this.mMediaFormat.setInteger("color-format", this.decodeColorFormat);
                Log.i(TAG, "set decode color format to type " + this.decodeColorFormat);
                i = decodeFramesToImage(mediaCodec, this.mExtractor);
                if (i == 0) {
                    callbackFinish();
                } else {
                    callbackError(i);
                }
            } else {
                Log.e(TAG, "unable to set decode color format, color format type " + this.decodeColorFormat + " not supported");
            }
            if (i == 0) {
                try {
                    mediaCodec.stop();
                } catch (IllegalStateException e4) {
                    e = e4;
                    e.printStackTrace();
                    mediaCodec.release();
                    Log.d(TAG, "videoDecode() end");
                }
            }
            mediaCodec.release();
            Log.d(TAG, "videoDecode() end");
        } catch (Throwable th) {
            if (0 != 0) {
                if (0 == 0) {
                    try {
                        mediaCodec.stop();
                    } catch (IllegalStateException e5) {
                        e5.printStackTrace();
                    }
                }
                mediaCodec.release();
            }
            throw th;
        }
    }

    public void release() {
        Log.d(TAG, "release()");
        MediaExtractor mediaExtractor = this.mExtractor;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.mExtractor = null;
        }
    }

    private static int selectTrack(MediaExtractor mediaExtractor) {
        int trackCount = mediaExtractor.getTrackCount();
        for (int i = 0; i < trackCount; i++) {
            MediaFormat trackFormat = mediaExtractor.getTrackFormat(i);
            String string = trackFormat.getString("mime");
            Log.d(TAG, "Extractor selected track " + i + " (" + string + "): " + trackFormat);
            if (string.startsWith("video/")) {
                return i;
            }
        }
        return -1;
    }

    private void showSupportedColorFormat(MediaCodecInfo.CodecCapabilities codecCapabilities) {
        StringBuffer stringBuffer = new StringBuffer("supported color format: ");
        for (int i : codecCapabilities.colorFormats) {
            stringBuffer.append(i);
            stringBuffer.append(SyslogAppender.DEFAULT_STACKTRACE_PATTERN);
        }
        Log.d(TAG, stringBuffer.toString());
    }

    private boolean isColorFormatSupported(int i, MediaCodecInfo.CodecCapabilities codecCapabilities) {
        for (int i2 : codecCapabilities.colorFormats) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private int start(MediaCodec mediaCodec) {
        try {
            mediaCodec.configure(this.mMediaFormat, (Surface) null, (MediaCrypto) null, 0);
            mediaCodec.start();
            return 0;
        } catch (Exception e) {
            Log.e(TAG, "start " + e.getMessage());
            return DecodeCallback.ERROR_START;
        }
    }

    private int dequeueInputBuffer(MediaCodec mediaCodec) {
        try {
            return mediaCodec.dequeueInputBuffer(10000L);
        } catch (IllegalStateException e) {
            Log.e(TAG, "dequeueInputBuffer " + e.getMessage());
            return DecodeCallback.ERROR_DEQUEUE_INPUT_BUFFER;
        }
    }

    private int queueInputBuffer(MediaCodec mediaCodec, int i, int i2, int i3, long j, int i4) {
        try {
            mediaCodec.queueInputBuffer(i, i2, i3, j, i4);
            return 0;
        } catch (IllegalStateException e) {
            Log.e(TAG, "queueInputBuffer " + e.getMessage());
            return DecodeCallback.ERROR_QUEUE_INPUT_BUFFER;
        }
    }

    private int dequeueOutputBuffer(MediaCodec mediaCodec, MediaCodec.BufferInfo bufferInfo) {
        try {
            return mediaCodec.dequeueOutputBuffer(bufferInfo, 10000L);
        } catch (IllegalStateException e) {
            Log.e(TAG, "dequeueOutputBuffer " + e.getMessage());
            return DecodeCallback.ERROR_DEQUEUE_OUTPUT_BUFFER;
        }
    }

    private int releaseOutputBuffer(MediaCodec mediaCodec, int i, boolean z) {
        try {
            mediaCodec.releaseOutputBuffer(i, z);
            return 0;
        } catch (IllegalStateException e) {
            Log.e(TAG, "releaseOutputBuffer " + e.getMessage());
            return DecodeCallback.ERROR_RELEASE_OUTPUT_BUFFER;
        }
    }

    private void callbackError(int i) {
        DecodeCallback decodeCallback = this.mCallback;
        if (decodeCallback != null) {
            decodeCallback.onDecoderError(i);
        }
    }

    private void callbackFinish() {
        DecodeCallback decodeCallback = this.mCallback;
        if (decodeCallback != null) {
            decodeCallback.onFinishDecode();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:127:0x026d A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x01f0 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x026e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int decodeFramesToImage(android.media.MediaCodec r34, android.media.MediaExtractor r35) {
        /*
            Method dump skipped, instructions count: 1040
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.video.VideoDecoder.decodeFramesToImage(android.media.MediaCodec, android.media.MediaExtractor):int");
    }

    private Input getFrameFromImage(Image image, int i, long j, boolean z) {
        byte[] convertYUV420888To420I;
        if (z) {
            convertYUV420888To420I = ImageUtils.convertYUV420888ToNV12(image);
        } else {
            convertYUV420888To420I = ImageUtils.convertYUV420888To420I(image);
        }
        Input create = create(this.mInputClass);
        this.mFrameFormat.cacheFrameInfo(create);
        create.data = convertYUV420888To420I;
        create.index = i;
        create.timeStamp = j;
        return create;
    }

    private Input getFrameFromBuffer(ByteBuffer byteBuffer, int i, int i2, long j) {
        Input create = create(this.mInputClass);
        this.mFrameFormat.cacheFrameInfo(create);
        create.index = i2;
        create.buffer = byteBuffer;
        create.bufferSize = i;
        create.timeStamp = j;
        return create;
    }

    private ByteBuffer copyBuffer(ByteBuffer byteBuffer) {
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(byteBuffer.capacity());
        byteBuffer.rewind();
        allocateDirect.put(byteBuffer);
        byteBuffer.rewind();
        allocateDirect.flip();
        return allocateDirect;
    }

    public Input create(Class<Input> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: classes3.dex */
    public class FrameFormat {
        public int colorFormat;
        public int cropBottom;
        public int cropLeft;
        public int cropRight;
        public int cropTop;
        public int frameRate;
        public int height;
        public int rotation;
        public int stride;
        public int strideHeight;
        public int width;

        public FrameFormat(MediaFormat mediaFormat) {
            update(mediaFormat);
        }

        public void update(MediaFormat mediaFormat) {
            if (mediaFormat.containsKey("crop-top")) {
                this.cropTop = mediaFormat.getInteger("crop-top");
                Log.d(VideoDecoder.TAG, "Crop-top:" + this.cropTop);
            }
            if (mediaFormat.containsKey("crop-bottom")) {
                this.cropBottom = mediaFormat.getInteger("crop-bottom");
                Log.d(VideoDecoder.TAG, "Crop-bottom:" + this.cropBottom);
            }
            if (mediaFormat.containsKey("crop-left")) {
                this.cropLeft = mediaFormat.getInteger("crop-left");
                Log.d(VideoDecoder.TAG, "Crop-left:" + this.cropLeft);
            }
            if (mediaFormat.containsKey("crop-right")) {
                this.cropRight = mediaFormat.getInteger("crop-right");
                Log.d(VideoDecoder.TAG, "Crop-right:" + this.cropRight);
            }
            if (mediaFormat.containsKey("rotation-degrees")) {
                this.rotation = mediaFormat.getInteger("rotation-degrees");
            }
            this.width = mediaFormat.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
            this.height = mediaFormat.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
            if (mediaFormat.containsKey("color-format")) {
                this.colorFormat = mediaFormat.getInteger("color-format");
                Log.d(VideoDecoder.TAG, "Color format:" + this.colorFormat);
            }
            if (mediaFormat.containsKey("frame-rate")) {
                this.frameRate = mediaFormat.getInteger("frame-rate");
            }
            if (mediaFormat.containsKey("stride")) {
                this.stride = mediaFormat.getInteger("stride");
            } else {
                this.stride = this.width;
            }
            if (mediaFormat.containsKey("slice-height")) {
                this.strideHeight = mediaFormat.getInteger("slice-height");
            } else {
                this.strideHeight = this.height;
            }
            Log.d(VideoDecoder.TAG, "update width:" + this.width + " height:" + this.height + ", rotation:" + this.rotation + ", stride:" + this.stride + ", strideHeight:" + this.strideHeight);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cacheFrameInfo(FrameInfo frameInfo) {
            if (frameInfo != null) {
                frameInfo.width = this.width;
                frameInfo.height = this.height;
                frameInfo.rotation = this.rotation;
                frameInfo.colorFormat = this.colorFormat;
                frameInfo.frameRate = this.frameRate;
                frameInfo.stride = this.stride;
                frameInfo.strideHeight = this.strideHeight;
                frameInfo.cropTop = this.cropTop;
                frameInfo.cropBottom = this.cropBottom;
                frameInfo.cropLeft = this.cropLeft;
                frameInfo.cropRight = this.cropRight;
            }
        }
    }
}
