package com.xiaomi.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.view.Surface;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.xiaomi.mediacodec.MoviePlayer;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes3.dex */
public class VideoEncoder {
    private static final int IFRAME_INTERVAL = 2;
    private static String TAG = "videoencoder";
    private String VIDEO_MIME_TYPE;
    private MediaCodec.Callback encoderCallback;
    private int mBitrate;
    private MediaCodec.BufferInfo mBufferInfo;
    private VideoEncoderCallBack mCallBack;
    private MediaCodec mEncoder;
    private FileDescriptor mFd;
    private int mFps;
    private int mHeight;
    private Surface mInputSurface;
    private MediaMuxer mMuxer;
    private FileOutputStream mOutputStream;
    private String mPath;
    private int mWidth;
    private int mTrackIndex = -1;
    private int mAudioTrackIndex = -1;
    private boolean mMuxerStarted = false;
    private long mEncoderFrames = 0;
    private int mNum = 0;
    private boolean mDump = false;
    private String mDumpPath = "/sdcard/voip-data/dump.h264";
    private boolean mAsync = true;
    private Queue<MoviePlayer.MediaFrame> audioFrames = new LinkedList();
    private Queue<MoviePlayer.MediaFrame> videoFrames = new LinkedList();

    /* loaded from: classes3.dex */
    public interface VideoEncoderCallBack {
        void onVideoEncodedFrame(boolean z);

        void onVideoEncoderEOF();

        void onVideoEncoderError(int i);
    }

    public static /* synthetic */ long access$208(VideoEncoder videoEncoder) {
        long j = videoEncoder.mEncoderFrames;
        videoEncoder.mEncoderFrames = 1 + j;
        return j;
    }

    public VideoEncoder(int i, int i2, int i3, int i4, FileDescriptor fileDescriptor, String str, String str2, VideoEncoderCallBack videoEncoderCallBack) {
        this.mBitrate = 0;
        this.mFd = null;
        this.VIDEO_MIME_TYPE = "video/avc";
        if (str2.equals("hevc")) {
            this.VIDEO_MIME_TYPE = "video/hevc";
        } else {
            this.VIDEO_MIME_TYPE = "video/avc";
        }
        Logg.LogI(" video encoder setup with width " + i + " height " + i2 + " bitrate " + i4 + " fps " + i3 + " fd: " + fileDescriptor + " codecName " + str2);
        this.mCallBack = videoEncoderCallBack;
        this.mWidth = i;
        this.mHeight = i2;
        this.mFps = i3;
        this.mFd = fileDescriptor;
        this.mPath = str;
        this.mBitrate = i4;
        if (this.mDump) {
            try {
                this.mOutputStream = new FileOutputStream(this.mDumpPath);
            } catch (FileNotFoundException e) {
                Logg.LogI(" found exception at dump " + e);
            }
        }
        this.encoderCallback = new MediaCodec.Callback() { // from class: com.xiaomi.mediacodec.VideoEncoder.1
            @Override // android.media.MediaCodec.Callback
            public void onInputBufferAvailable(MediaCodec mediaCodec, int i5) {
                Logg.LogI(" Input Buffer Avail");
            }

            @Override // android.media.MediaCodec.Callback
            public void onOutputBufferAvailable(MediaCodec mediaCodec, int i5, MediaCodec.BufferInfo bufferInfo) {
                if (VideoEncoder.this.mCallBack != null) {
                    VideoEncoder.this.mCallBack.onVideoEncodedFrame(true);
                }
                ByteBuffer outputBuffer = VideoEncoder.this.mEncoder.getOutputBuffer(i5);
                if (outputBuffer == null) {
                    throw new RuntimeException("couldn't fetch buffer at index " + i5);
                }
                if ((bufferInfo.flags & 2) != 0) {
                    bufferInfo.size = 0;
                }
                Logg.LogI("add video info.size " + bufferInfo.size + " time:" + bufferInfo.presentationTimeUs + "index:" + i5);
                if (bufferInfo.size != 0) {
                    VideoEncoder.access$208(VideoEncoder.this);
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    MediaCodec.BufferInfo bufferInfo2 = new MediaCodec.BufferInfo();
                    ByteBuffer allocate = ByteBuffer.allocate(outputBuffer.capacity());
                    allocate.clear();
                    allocate.position(bufferInfo.offset);
                    int i6 = bufferInfo.size;
                    byte[] bArr = new byte[i6];
                    outputBuffer.get(bArr, 0, i6);
                    allocate.put(bArr, 0, bufferInfo.size);
                    allocate.position(bufferInfo.offset);
                    allocate.limit(bufferInfo.offset + bufferInfo.size);
                    bufferInfo2.set(bufferInfo.offset, bufferInfo.size, bufferInfo.presentationTimeUs, bufferInfo.flags);
                    if (VideoEncoder.this.mMuxerStarted) {
                        VideoEncoder.this.mMuxer.writeSampleData(VideoEncoder.this.mTrackIndex, allocate, bufferInfo2);
                    } else {
                        MoviePlayer.MediaFrame mediaFrame = new MoviePlayer.MediaFrame();
                        mediaFrame.buffer = allocate;
                        mediaFrame.info = bufferInfo2;
                        VideoEncoder.this.videoFrames.add(mediaFrame);
                    }
                }
                VideoEncoder.this.mEncoder.releaseOutputBuffer(i5, false);
                if ((bufferInfo.flags & 4) == 0 || VideoEncoder.this.mCallBack == null) {
                    return;
                }
                Logg.LogI("encode EOF mEncoderNums " + VideoEncoder.this.mEncoderFrames);
                VideoEncoder.this.mCallBack.onVideoEncoderEOF();
                VideoEncoder.this.mTrackIndex = -1;
                if (!VideoEncoder.this.mDump) {
                    return;
                }
                try {
                    VideoEncoder.this.mOutputStream.close();
                } catch (IOException e2) {
                    Logg.LogI(" found exception at dump " + e2);
                }
            }

            @Override // android.media.MediaCodec.Callback
            public void onError(MediaCodec mediaCodec, MediaCodec.CodecException codecException) {
                Logg.LogE(" MediaCodec " + mediaCodec.getName() + " onError:" + codecException.toString() + codecException);
            }

            @Override // android.media.MediaCodec.Callback
            public void onOutputFormatChanged(MediaCodec mediaCodec, MediaFormat mediaFormat) {
                Logg.LogI("encoder Output Format changed " + mediaFormat);
                if (VideoEncoder.this.mTrackIndex >= 0) {
                    throw new RuntimeException("format changed twice");
                }
                VideoEncoder videoEncoder = VideoEncoder.this;
                videoEncoder.addMediaTrack(videoEncoder.mEncoder.getOutputFormat());
            }
        };
        setupEncoder();
    }

    public void flush() {
        MediaCodec mediaCodec;
        MediaCodec mediaCodec2 = this.mEncoder;
        if (mediaCodec2 != null) {
            mediaCodec2.flush();
        }
        if (!this.mAsync || (mediaCodec = this.mEncoder) == null) {
            return;
        }
        mediaCodec.start();
    }

    private void setupEncoder() {
        if (!this.mAsync) {
            this.mBufferInfo = new MediaCodec.BufferInfo();
        }
        Logg.LogI("GlUtil.mPictureRotation " + GlUtil.mPictureRotation);
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat(this.VIDEO_MIME_TYPE, this.mWidth, this.mHeight);
        int i = this.mFps;
        createVideoFormat.setInteger("color-format", 2130708361);
        if (this.mBitrate <= 0) {
            this.mBitrate = this.mWidth * this.mHeight * 4 * 2;
        }
        createVideoFormat.setInteger("bitrate", this.mBitrate);
        createVideoFormat.setInteger("frame-rate", i);
        createVideoFormat.setInteger("capture-rate", i);
        createVideoFormat.setInteger("repeat-previous-frame-after", 1000000 / i);
        createVideoFormat.setInteger("i-frame-interval", 2);
        Logg.LogI(" video encoder setup with mwidth " + this.mWidth + " mheight " + this.mHeight + " mBitrate " + this.mBitrate + " frameRate " + i + " codecName " + this.VIDEO_MIME_TYPE);
        StringBuilder sb = new StringBuilder();
        sb.append(" set video encoder format ");
        sb.append(createVideoFormat);
        Logg.LogI(sb.toString());
        try {
            MediaCodec createEncoderByType = MediaCodec.createEncoderByType(this.VIDEO_MIME_TYPE);
            this.mEncoder = createEncoderByType;
            createEncoderByType.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
            this.mInputSurface = this.mEncoder.createInputSurface();
            if (this.mAsync) {
                this.mEncoder.setCallback(this.encoderCallback);
            }
            Logg.LogI("create encoder and start path:" + this.mPath);
            this.mEncoder.start();
            File file = new File(this.mPath);
            try {
                file.createNewFile();
                this.mFd = new FileOutputStream(file).getFD();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            FileDescriptor fileDescriptor = this.mFd;
            if (fileDescriptor != null && fileDescriptor.valid()) {
                Logg.LogI("MediaPlayer/android/src/com/xiaomi/mediacodec/VideoEncoder.java new mediamuxer" + this.mFd.valid() + "  " + this.mFd);
                this.mMuxer = new MediaMuxer(this.mFd, 0);
            } else {
                this.mMuxer = new MediaMuxer(this.mPath, 0);
            }
            this.mMuxer.setOrientationHint(GlUtil.mPictureRotation);
            float[] fArr = GlUtil.location;
            if (fArr == null) {
                return;
            }
            this.mMuxer.setLocation(fArr[0], fArr[1]);
        } catch (Exception e3) {
            Logg.LogI("find exception at set up encoder:" + e3);
            this.mCallBack.onVideoEncoderError(1);
            release();
        }
    }

    public void addMediaTrack(MediaFormat mediaFormat) {
        if (mediaFormat.getString("mime").startsWith("audio/")) {
            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
        } else {
            this.mTrackIndex = this.mMuxer.addTrack(mediaFormat);
        }
        if (this.mTrackIndex != -1 && this.mAudioTrackIndex != -1 && !this.mMuxerStarted) {
            this.mMuxer.start();
            this.mMuxerStarted = true;
        }
        if (!this.mMuxerStarted) {
            return;
        }
        while (this.audioFrames.size() > 0) {
            MoviePlayer.MediaFrame peek = this.audioFrames.peek();
            Logg.LogI("track audio info.size " + peek.info.size + " time:" + peek.info.presentationTimeUs);
            this.mMuxer.writeSampleData(this.mAudioTrackIndex, peek.buffer, peek.info);
            this.audioFrames.remove();
        }
        while (this.videoFrames.size() > 0) {
            MoviePlayer.MediaFrame peek2 = this.videoFrames.peek();
            Logg.LogI("track video info.size " + peek2.info.size + " time:" + peek2.info.presentationTimeUs);
            this.mMuxer.writeSampleData(this.mTrackIndex, peek2.buffer, peek2.info);
            this.videoFrames.remove();
        }
    }

    public void writeAudioSample(MoviePlayer.MediaFrame mediaFrame) {
        if (!this.mMuxerStarted) {
            this.audioFrames.add(mediaFrame);
            return;
        }
        Logg.LogI("add audio info.size " + mediaFrame.info.size + " time:" + mediaFrame.info.presentationTimeUs);
        this.mMuxer.writeSampleData(this.mAudioTrackIndex, mediaFrame.buffer, mediaFrame.info);
    }

    public void stopEncoder() {
        Logg.LogI("stopEncoder mAsync:" + this.mAsync);
        try {
            if (this.mAsync) {
                MediaCodec mediaCodec = this.mEncoder;
                if (mediaCodec != null) {
                    mediaCodec.signalEndOfInputStream();
                }
            } else {
                drainEncoder(true);
            }
        } catch (Exception unused) {
        }
    }

    public Surface getInputSurface() {
        return this.mInputSurface;
    }

    public void release() {
        Logg.LogI("releasing encoder objects");
        MediaCodec mediaCodec = this.mEncoder;
        if (mediaCodec != null) {
            try {
                mediaCodec.stop();
                this.mEncoder.release();
                this.mInputSurface.release();
                this.mBufferInfo = null;
            } catch (Exception unused) {
            }
            this.mEncoder = null;
        }
        if (this.mMuxer != null) {
            Logg.LogI("to stop muxter");
            try {
                this.mMuxer.stop();
                Logg.LogI("to release muxter");
                this.mMuxer.release();
                VideoEncoderCallBack videoEncoderCallBack = this.mCallBack;
                if (videoEncoderCallBack != null) {
                    videoEncoderCallBack.onVideoEncoderEOF();
                }
            } catch (Exception unused2) {
            }
            this.mMuxer = null;
        }
    }

    public void drainEncoder(boolean z) {
        if (this.mAsync) {
            return;
        }
        if (z) {
            Logg.LogI("sending EOS to encoder");
            this.mEncoder.signalEndOfInputStream();
        }
        while (true) {
            int dequeueOutputBuffer = this.mEncoder.dequeueOutputBuffer(this.mBufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
            if (dequeueOutputBuffer == -1) {
                Logg.LogI("MediaCodec.INFO_TRY_AGAIN_LATER");
                if (!z) {
                    return;
                }
                Logg.LogI("no output available, spinning to await EOS");
            } else if (dequeueOutputBuffer == -2) {
                Logg.LogI("MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
                if (this.mMuxerStarted) {
                    throw new RuntimeException("format changed twice");
                }
                MediaFormat outputFormat = this.mEncoder.getOutputFormat();
                Logg.LogI("encoder output format changed: " + outputFormat);
                this.mTrackIndex = this.mMuxer.addTrack(outputFormat);
                this.mMuxer.start();
                this.mMuxerStarted = true;
            } else if (dequeueOutputBuffer < 0) {
                Logg.LogI("unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
            } else {
                ByteBuffer outputBuffer = this.mEncoder.getOutputBuffer(dequeueOutputBuffer);
                if (outputBuffer == null) {
                    throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                }
                if ((this.mBufferInfo.flags & 2) != 0) {
                    Logg.LogI("ignoring BUFFER_FLAG_CODEC_CONFIG");
                    this.mBufferInfo.size = 0;
                }
                MediaCodec.BufferInfo bufferInfo = this.mBufferInfo;
                if (bufferInfo.size != 0) {
                    if (!this.mMuxerStarted) {
                        throw new RuntimeException("muxer hasn't started");
                    }
                    outputBuffer.position(bufferInfo.offset);
                    MediaCodec.BufferInfo bufferInfo2 = this.mBufferInfo;
                    outputBuffer.limit(bufferInfo2.offset + bufferInfo2.size);
                    this.mMuxer.writeSampleData(this.mTrackIndex, outputBuffer, this.mBufferInfo);
                    Logg.LogI("sent " + this.mBufferInfo.size + " bytes to muxer, ts=" + this.mBufferInfo.presentationTimeUs + " " + ((int) outputBuffer.get(0)) + " " + ((int) outputBuffer.get(1)) + " " + ((int) outputBuffer.get(2)) + " " + ((int) outputBuffer.get(3)) + " type " + (outputBuffer.get(4) & 31) + " mTrackIndex " + this.mTrackIndex + " mNum " + this.mNum);
                    this.mNum = this.mNum + 1;
                } else {
                    Logg.LogI("drainEncoder mBufferInfo: " + this.mBufferInfo.size);
                }
                this.mEncoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                if ((this.mBufferInfo.flags & 4) != 0) {
                    if (this.mCallBack != null) {
                        Logg.LogI("encode EOF mEncoderNums " + this.mEncoderFrames);
                        this.mCallBack.onVideoEncoderEOF();
                    }
                    if (!z) {
                        Logg.LogW("reached end of stream unexpectedly");
                        return;
                    } else {
                        Logg.LogI("end of stream reached");
                        return;
                    }
                }
            }
        }
    }
}
