package com.xiaomi.mediatranscode;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.view.Surface;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.xiaomi.mediatranscode.MoviePlayer;
import com.xiaomi.stat.a.j;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes3.dex */
public class VideoEncoder {
    private static final int IFRAME_INTERVAL = 1;
    private static String TAG = "videoencoder";
    private String VIDEO_MIME_TYPE;
    private MediaCodec.Callback encoderCallback;
    private int mBitrate;
    private MediaCodec.BufferInfo mBufferInfo;
    private VideoEncoderCallBack mCallBack;
    private MediaCodec mEncoder;
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
    private Queue<MoviePlayer.AudioFrame> audioFramequeue = new LinkedList();

    /* loaded from: classes3.dex */
    public interface VideoEncoderCallBack {
        void onVideoEncoderEOF();

        void onVideoEncoderError(int i);
    }

    public static /* synthetic */ long access$208(VideoEncoder videoEncoder) {
        long j = videoEncoder.mEncoderFrames;
        videoEncoder.mEncoderFrames = 1 + j;
        return j;
    }

    public VideoEncoder(int i, int i2, int i3, int i4, String str, String str2, VideoEncoderCallBack videoEncoderCallBack) {
        this.mBitrate = 0;
        this.VIDEO_MIME_TYPE = "video/avc";
        if (str2.equals("hevc")) {
            this.VIDEO_MIME_TYPE = "video/hevc";
        } else {
            this.VIDEO_MIME_TYPE = "video/avc";
        }
        Logg.LogI(" video encoder setup with width " + i + " height " + i2 + " bitrate " + i4 + " fps " + i3 + " codecName " + str2);
        this.mCallBack = videoEncoderCallBack;
        this.mWidth = i;
        this.mHeight = i2;
        this.mFps = i3;
        this.mPath = str;
        this.mBitrate = i4;
        if (this.mDump) {
            try {
                this.mOutputStream = new FileOutputStream(this.mDumpPath);
            } catch (FileNotFoundException e) {
                Logg.LogI(" found exception at dump " + e);
            }
        }
        this.encoderCallback = new MediaCodec.Callback() { // from class: com.xiaomi.mediatranscode.VideoEncoder.1
            @Override // android.media.MediaCodec.Callback
            public void onInputBufferAvailable(MediaCodec mediaCodec, int i5) {
                Logg.LogI(" Input Buffer Avail");
            }

            @Override // android.media.MediaCodec.Callback
            public void onOutputBufferAvailable(MediaCodec mediaCodec, int i5, MediaCodec.BufferInfo bufferInfo) {
                ByteBuffer outputBuffer = VideoEncoder.this.mEncoder.getOutputBuffer(i5);
                if (outputBuffer == null) {
                    throw new RuntimeException("couldn't fetch buffer at index " + i5);
                }
                if ((bufferInfo.flags & 2) != 0) {
                    bufferInfo.size = 0;
                }
                Logg.LogI("video info.size " + bufferInfo.size);
                if (bufferInfo.size != 0 && VideoEncoder.this.mMuxerStarted) {
                    VideoEncoder.access$208(VideoEncoder.this);
                    outputBuffer.position(bufferInfo.offset);
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
                    VideoEncoder.this.mMuxer.writeSampleData(VideoEncoder.this.mTrackIndex, outputBuffer, bufferInfo);
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
                Logg.LogI(" Output Format changed " + mediaFormat);
                if (VideoEncoder.this.mTrackIndex >= 0) {
                    throw new RuntimeException("format changed twice");
                }
                VideoEncoder videoEncoder = VideoEncoder.this;
                videoEncoder.mTrackIndex = videoEncoder.mMuxer.addTrack(VideoEncoder.this.mEncoder.getOutputFormat());
                if (!VideoEncoder.this.mMuxerStarted && VideoEncoder.this.mTrackIndex >= 0) {
                    VideoEncoder.this.mMuxer.start();
                    VideoEncoder.this.mMuxerStarted = true;
                }
                while (VideoEncoder.this.audioFramequeue.size() > 0) {
                    MoviePlayer.AudioFrame audioFrame = (MoviePlayer.AudioFrame) VideoEncoder.this.audioFramequeue.peek();
                    Logg.LogI("audio info.size " + audioFrame.info.size);
                    VideoEncoder.this.mMuxer.writeSampleData(VideoEncoder.this.mAudioTrackIndex, audioFrame.buffer, audioFrame.info);
                    VideoEncoder.this.audioFramequeue.remove();
                }
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
        createVideoFormat.setInteger(j.k, 1);
        createVideoFormat.setInteger("bitrate", this.mBitrate);
        createVideoFormat.setInteger("frame-rate", i);
        createVideoFormat.setInteger("capture-rate", i);
        createVideoFormat.setInteger("repeat-previous-frame-after", 1000000 / i);
        createVideoFormat.setInteger("i-frame-interval", 1);
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
            Logg.LogI("create encoder and start");
            this.mEncoder.start();
            MediaMuxer mediaMuxer = new MediaMuxer(this.mPath, 0);
            this.mMuxer = mediaMuxer;
            mediaMuxer.setOrientationHint(GlUtil.mPictureRotation);
            float[] fArr = GlUtil.location;
            if (fArr == null) {
                return;
            }
            this.mMuxer.setLocation(fArr[0], fArr[1]);
        } catch (Exception e) {
            Logg.LogI("find exception at set up encoder:" + e);
            release();
            this.mCallBack.onVideoEncoderError(1);
        }
    }

    public void addAudioTrack(MediaFormat mediaFormat) {
        if (mediaFormat.getString("mime").startsWith("audio/")) {
            this.mAudioTrackIndex = this.mMuxer.addTrack(mediaFormat);
        } else {
            this.mTrackIndex = this.mMuxer.addTrack(mediaFormat);
        }
        if (this.mTrackIndex == -1 || this.mAudioTrackIndex == -1) {
            return;
        }
        this.mMuxer.start();
        this.mMuxerStarted = true;
    }

    public void writeAudioSample(MoviePlayer.AudioFrame audioFrame) {
        if (!this.mMuxerStarted) {
            this.audioFramequeue.add(audioFrame);
        } else {
            this.mMuxer.writeSampleData(this.mAudioTrackIndex, audioFrame.buffer, audioFrame.info);
        }
    }

    public void stopEncoder() {
        MediaCodec mediaCodec;
        Logg.LogI("stopEncoder");
        try {
            if (!this.mAsync || (mediaCodec = this.mEncoder) == null) {
                return;
            }
            mediaCodec.signalEndOfInputStream();
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
