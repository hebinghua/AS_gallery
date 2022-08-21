package com.meicam.sdk;

/* loaded from: classes.dex */
public class NvsAVFileInfo {
    public static final int AUDIO_MAX_STREAM_COUNT = 4;
    public static final int AV_FILE_TYPE_AUDIO = 1;
    public static final int AV_FILE_TYPE_AUDIOVIDEO = 0;
    public static final int AV_FILE_TYPE_IMAGE = 2;
    public static final int AV_FILE_TYPE_UNKNOWN = -1;
    public NvsAudioStreamInfo[] m_audioStreamInfo = new NvsAudioStreamInfo[4];
    public long m_dataRate;
    public long m_duration;
    public int m_numAudioStreams;
    public int m_numVideoStreams;
    public int m_type;
    public NvsVideoStreamInfo m_videoStreamInfo;

    public int getAVFileType() {
        return this.m_type;
    }

    public long getDuration() {
        return this.m_duration;
    }

    public long getDataRate() {
        return this.m_dataRate;
    }

    public int getVideoStreamCount() {
        return this.m_numVideoStreams;
    }

    public int getAudioStreamCount() {
        return this.m_numAudioStreams;
    }

    public long getVideoStreamDuration(int i) {
        if (i < this.m_numVideoStreams) {
            return this.m_videoStreamInfo.duration;
        }
        return 0L;
    }

    public NvsSize getVideoStreamDimension(int i) {
        NvsSize nvsSize = new NvsSize(0, 0);
        if (i < this.m_numVideoStreams) {
            NvsVideoStreamInfo nvsVideoStreamInfo = this.m_videoStreamInfo;
            nvsSize.width = nvsVideoStreamInfo.imageWidth;
            nvsSize.height = nvsVideoStreamInfo.imageHeight;
        }
        return nvsSize;
    }

    public NvsRational getVideoStreamPixelAspectRatio(int i) {
        NvsRational nvsRational = new NvsRational(1, 1);
        if (i < this.m_numVideoStreams) {
            NvsRational nvsRational2 = this.m_videoStreamInfo.pixelAspectRatio;
            nvsRational.num = nvsRational2.num;
            nvsRational.den = nvsRational2.den;
        }
        return nvsRational;
    }

    public NvsRational getVideoStreamFrameRate(int i) {
        NvsRational nvsRational = new NvsRational(1, 1);
        if (i < this.m_numVideoStreams) {
            NvsRational nvsRational2 = this.m_videoStreamInfo.frameRate;
            nvsRational.num = nvsRational2.num;
            nvsRational.den = nvsRational2.den;
        }
        return nvsRational;
    }

    public int getVideoStreamRotation(int i) {
        if (i < this.m_numVideoStreams) {
            return this.m_videoStreamInfo.displayRotation;
        }
        return 0;
    }

    public int getVideoStreamComponentBitCount(int i) {
        if (i < this.m_numVideoStreams) {
            return this.m_videoStreamInfo.componentBitCount;
        }
        return 8;
    }

    public int getVideoStreamCodecType(int i) {
        if (i < this.m_numVideoStreams) {
            return this.m_videoStreamInfo.videoCodecType;
        }
        return 0;
    }

    public long getAudioStreamDuration(int i) {
        if (i < this.m_numAudioStreams) {
            return this.m_audioStreamInfo[i].duration;
        }
        return 0L;
    }

    public int getAudioStreamSampleRate(int i) {
        if (i < this.m_numAudioStreams) {
            return this.m_audioStreamInfo[i].sampleRate;
        }
        return 0;
    }

    public int getAudioStreamChannelCount(int i) {
        if (i < this.m_numAudioStreams) {
            return this.m_audioStreamInfo[i].channelCount;
        }
        return 0;
    }

    public void setAudioStreamInfo(int i, NvsAudioStreamInfo nvsAudioStreamInfo) {
        if (i < this.m_numAudioStreams) {
            this.m_audioStreamInfo[i] = nvsAudioStreamInfo;
        }
    }
}
