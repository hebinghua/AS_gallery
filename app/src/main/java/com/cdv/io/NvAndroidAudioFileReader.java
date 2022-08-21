package com.cdv.io;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import com.cdv.utils.NvAndroidUtils;
import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class NvAndroidAudioFileReader {
    private static final int ERROR_EOF = 1;
    private static final int ERROR_FAIL = 2;
    private static final int ERROR_OK = 0;
    private static final String TAG = "NvAndroidAudioFileReader";
    private static final boolean m_verbose = false;
    private MediaCodec.BufferInfo m_bufferInfo;
    private MediaExtractor m_extractor = null;
    private int m_audioTrackIndex = -1;
    private MediaFormat m_format = null;
    private long m_duration = 0;
    private int m_channelCount = 1;
    private int m_sampleRate = 44100;
    private int m_pcmEncoding = 2;
    private MediaCodec m_decoder = null;
    private boolean m_decoderSetupFailed = false;
    private boolean m_decoderStarted = false;
    public ByteBuffer[] m_decoderInputBuffers = null;
    public ByteBuffer[] m_decoderOutputBuffers = null;
    private boolean m_inputBufferQueued = false;
    private int m_pendingInputFrameCount = 0;
    private boolean m_sawInputEOS = false;
    private boolean m_sawOutputEOS = false;

    /* loaded from: classes.dex */
    public static class AudioFrame {
        public ByteBuffer audioFrame;
        public int channelCount;
        public long pts;
        public int retCode;
        public int sampleCount;
        public int sampleRate;
        public int sampleSize;
    }

    public NvAndroidAudioFileReader() {
        this.m_bufferInfo = null;
        this.m_bufferInfo = new MediaCodec.BufferInfo();
    }

    public boolean openFile(String str, Context context) {
        if (isValid()) {
            Log.e(TAG, "You can't call OpenFile() twice!");
            return false;
        }
        MediaExtractor createMediaExtractorFromMediaFilePath = NvAndroidUtils.createMediaExtractorFromMediaFilePath(context, str);
        this.m_extractor = createMediaExtractorFromMediaFilePath;
        if (createMediaExtractorFromMediaFilePath == null) {
            return false;
        }
        int trackCount = createMediaExtractorFromMediaFilePath.getTrackCount();
        int i = 0;
        while (true) {
            if (i >= trackCount) {
                break;
            } else if (this.m_extractor.getTrackFormat(i).getString("mime").startsWith("audio/")) {
                this.m_audioTrackIndex = i;
                break;
            } else {
                i++;
            }
        }
        int i2 = this.m_audioTrackIndex;
        if (i2 < 0) {
            Log.e(TAG, "Failed to find a audio track from " + str);
            closeFile();
            return false;
        }
        this.m_extractor.selectTrack(i2);
        MediaFormat trackFormat = this.m_extractor.getTrackFormat(this.m_audioTrackIndex);
        this.m_format = trackFormat;
        this.m_duration = trackFormat.getLong("durationUs");
        String string = this.m_format.getString("mime");
        this.m_decoderSetupFailed = false;
        if (setupDecoder(string)) {
            return true;
        }
        this.m_decoderSetupFailed = true;
        closeFile();
        return false;
    }

    public boolean hasDecoderSetupFailed() {
        return this.m_decoderSetupFailed;
    }

    public void closeFile() {
        cleanupDecoder();
        MediaExtractor mediaExtractor = this.m_extractor;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.m_extractor = null;
            this.m_audioTrackIndex = -1;
            this.m_format = null;
            this.m_duration = 0L;
        }
    }

    public int startPlayback(long j) {
        if (!isValid()) {
            return 1;
        }
        long max = Math.max(j, 0L);
        if (max >= this.m_duration) {
            return 1;
        }
        try {
            this.m_extractor.seekTo(max, 0);
            if (!this.m_sawInputEOS && !this.m_sawOutputEOS) {
                if (this.m_inputBufferQueued) {
                    try {
                        this.m_decoder.flush();
                    } catch (Exception unused) {
                    }
                    this.m_inputBufferQueued = false;
                    this.m_pendingInputFrameCount = 0;
                }
            }
            cleanupDecoder();
            return !setupDecoder(this.m_format.getString("mime")) ? 2 : 0;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            return 2;
        }
    }

    public AudioFrame getNextAudioFrameForPlayback() {
        if (!isValid()) {
            AudioFrame audioFrame = new AudioFrame();
            audioFrame.retCode = 1;
            return audioFrame;
        }
        try {
            return decodeNextFrame();
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            cleanupDecoder();
            return null;
        }
    }

    private boolean isValid() {
        return this.m_decoder != null;
    }

    private boolean setupDecoder(String str) {
        try {
            MediaCodec createDecoderByType = MediaCodec.createDecoderByType(str);
            this.m_decoder = createDecoderByType;
            createDecoderByType.configure(this.m_format, (Surface) null, (MediaCrypto) null, 0);
            this.m_decoder.start();
            this.m_decoderStarted = true;
            this.m_decoderInputBuffers = this.m_decoder.getInputBuffers();
            this.m_decoderOutputBuffers = this.m_decoder.getOutputBuffers();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            cleanupDecoder();
            return false;
        }
    }

    private void cleanupDecoder() {
        MediaCodec mediaCodec = this.m_decoder;
        if (mediaCodec != null) {
            if (this.m_decoderStarted) {
                try {
                    if (this.m_inputBufferQueued) {
                        try {
                            mediaCodec.flush();
                        } catch (Exception unused) {
                        }
                        this.m_inputBufferQueued = false;
                    }
                    this.m_decoder.stop();
                } catch (Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                    e.printStackTrace();
                }
                this.m_decoderStarted = false;
                this.m_decoderInputBuffers = null;
            }
            this.m_decoder.release();
            this.m_decoder = null;
        }
        this.m_pendingInputFrameCount = 0;
        this.m_sawInputEOS = false;
        this.m_sawOutputEOS = false;
    }

    private AudioFrame decodeNextFrame() {
        int dequeueInputBuffer;
        int max = Math.max(this.m_decoderInputBuffers.length / 3, 2);
        AudioFrame audioFrame = new AudioFrame();
        int i = 0;
        while (true) {
            boolean z = true;
            if (!this.m_sawOutputEOS) {
                if (!this.m_sawInputEOS && (dequeueInputBuffer = this.m_decoder.dequeueInputBuffer(500L)) >= 0) {
                    int readSampleData = this.m_extractor.readSampleData(this.m_decoderInputBuffers[dequeueInputBuffer], 0);
                    if (readSampleData < 0) {
                        this.m_decoder.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                        this.m_sawInputEOS = true;
                    } else {
                        if (this.m_extractor.getSampleTrackIndex() != this.m_audioTrackIndex) {
                            Log.w(TAG, "WEIRD: got sample from track " + this.m_extractor.getSampleTrackIndex() + ", expected " + this.m_audioTrackIndex);
                        }
                        this.m_decoder.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, this.m_extractor.getSampleTime(), 0);
                        this.m_inputBufferQueued = true;
                        this.m_pendingInputFrameCount++;
                        this.m_extractor.advance();
                    }
                }
                int dequeueOutputBuffer = this.m_decoder.dequeueOutputBuffer(this.m_bufferInfo, (this.m_pendingInputFrameCount > max || this.m_sawInputEOS) ? 500 : 0);
                int i2 = i + 1;
                if (dequeueOutputBuffer != -1) {
                    if (dequeueOutputBuffer == -3) {
                        this.m_decoderOutputBuffers = this.m_decoder.getOutputBuffers();
                    } else if (dequeueOutputBuffer == -2) {
                        parseMediaFormat(this.m_decoder.getOutputFormat());
                    } else if (dequeueOutputBuffer < 0) {
                        Log.e(TAG, "Unexpected result from decoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                        return null;
                    } else {
                        MediaCodec.BufferInfo bufferInfo = this.m_bufferInfo;
                        if ((bufferInfo.flags & 4) != 0) {
                            this.m_sawOutputEOS = true;
                        } else {
                            this.m_pendingInputFrameCount--;
                        }
                        int i3 = bufferInfo.size;
                        if (i3 == 0) {
                            z = false;
                        }
                        if (z) {
                            int i4 = this.m_channelCount;
                            audioFrame.channelCount = i4;
                            audioFrame.sampleRate = this.m_sampleRate;
                            audioFrame.sampleSize = 16;
                            int i5 = this.m_pcmEncoding;
                            if (i5 == 3) {
                                audioFrame.sampleSize = 8;
                            } else if (i5 == 4) {
                                audioFrame.sampleSize = 32;
                            }
                            audioFrame.sampleCount = i3 / ((audioFrame.sampleSize / 8) * i4);
                            try {
                                ByteBuffer byteBuffer = this.m_decoderOutputBuffers[dequeueOutputBuffer];
                                byteBuffer.position(0);
                                byteBuffer.limit(this.m_bufferInfo.size);
                                ByteBuffer allocateDirect = ByteBuffer.allocateDirect(this.m_bufferInfo.size);
                                allocateDirect.put(byteBuffer);
                                audioFrame.audioFrame = allocateDirect;
                                audioFrame.pts = this.m_bufferInfo.presentationTimeUs;
                                audioFrame.retCode = 0;
                            } catch (Exception e) {
                                e.printStackTrace();
                                audioFrame.retCode = 2;
                                z = false;
                            }
                        }
                        this.m_decoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                        if (z) {
                            return audioFrame;
                        }
                    }
                }
                if (i2 > 100) {
                    Log.e(TAG, "We have tried too many times and can't decode a frame!");
                    return null;
                }
                i = i2;
            } else {
                audioFrame.retCode = 1;
                return audioFrame;
            }
        }
    }

    private void parseMediaFormat(MediaFormat mediaFormat) {
        if (mediaFormat.containsKey("channel-count")) {
            this.m_channelCount = mediaFormat.getInteger("channel-count");
        }
        if (mediaFormat.containsKey("sample-rate")) {
            this.m_sampleRate = mediaFormat.getInteger("sample-rate");
        }
        if (mediaFormat.containsKey("pcm-encoding")) {
            this.m_pcmEncoding = mediaFormat.getInteger("pcm-encoding");
        }
    }
}
