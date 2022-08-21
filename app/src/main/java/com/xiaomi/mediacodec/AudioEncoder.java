package com.xiaomi.mediacodec;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.xiaomi.mediacodec.MoviePlayer;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class AudioEncoder {
    private MiHWEncoder encodercallback;
    private FileOutputStream mAudioFile;
    private FileOutputStream outputStream;
    private MediaFormat encoderFormat = null;
    private MediaFormat outputencoderFormat = null;
    private MediaCodec encoder = null;
    private MediaCodec.BufferInfo info = null;
    private int perpcmsize = 0;
    private byte[] outByteBuffer = null;
    private int aacsamplerate = 4;
    private double recordTime = SearchStatUtils.POW;
    private int audioSamplerate = 0;
    private boolean initmediacodec = false;
    private boolean first_frame = false;
    private double last_pcm_timestamp = SearchStatUtils.POW;
    private long last_aac_timestamp = 0;

    private int getADTSsamplerate(int i) {
        switch (i) {
            case 7350:
                return 12;
            case 8000:
                return 11;
            case 11025:
                return 10;
            case 12000:
                return 9;
            case 16000:
                return 8;
            case 22050:
                return 7;
            case 24000:
                return 6;
            case 32000:
                return 5;
            case 44100:
            default:
                return 4;
            case 48000:
                return 3;
            case 64000:
                return 2;
            case 88200:
                return 1;
            case 96000:
                return 0;
        }
    }

    public void SetEncoderDataCallback(MiHWEncoder miHWEncoder) {
        this.encodercallback = miHWEncoder;
    }

    public void initMediacodec(int i) {
        try {
            if (this.initmediacodec) {
                Logg.LogI("AudioEncoder craete audio encoder initMediacodec has success");
                return;
            }
            this.aacsamplerate = getADTSsamplerate(i);
            MediaFormat createAudioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", i, 2);
            this.encoderFormat = createAudioFormat;
            createAudioFormat.setInteger("bitrate", 96000);
            this.encoderFormat.setInteger("aac-profile", 2);
            this.encoderFormat.setInteger("max-input-size", 4096);
            this.encoder = MediaCodec.createEncoderByType("audio/mp4a-latm");
            this.info = new MediaCodec.BufferInfo();
            MediaCodec mediaCodec = this.encoder;
            if (mediaCodec == null) {
                Logg.LogI("craete encoder wrong");
                return;
            }
            this.recordTime = SearchStatUtils.POW;
            mediaCodec.configure(this.encoderFormat, (Surface) null, (MediaCrypto) null, 1);
            this.encoder.start();
            this.initmediacodec = true;
            Logg.LogI("craete audio encoder initMediacodec success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean encodecPcmToAAc(int i, byte[] bArr, double d) {
        int i2;
        MediaCodec mediaCodec;
        if (bArr == null || (mediaCodec = this.encoder) == null) {
            i2 = 0;
        } else {
            i2 = mediaCodec.dequeueInputBuffer(0L);
            if (i2 >= 0) {
                ByteBuffer byteBuffer = this.encoder.getInputBuffers()[i2];
                byteBuffer.clear();
                byteBuffer.put(bArr, 0, i);
                this.encoder.queueInputBuffer(i2, 0, i, (long) (d * 1000.0d), 0);
            }
            int dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.info, 300L);
            if (dequeueOutputBuffer == -2) {
                if (this.encodercallback != null) {
                    MediaFormat outputFormat = this.encoder.getOutputFormat();
                    this.outputencoderFormat = outputFormat;
                    this.encodercallback.addAudioFormat(outputFormat);
                } else {
                    Logg.LogI("MediaCodec.INFO_OUTPUT_FORMAT_CHANGED");
                }
            }
            while (dequeueOutputBuffer >= 0) {
                try {
                    ByteBuffer byteBuffer2 = this.encoder.getOutputBuffers()[dequeueOutputBuffer];
                    byteBuffer2.position(this.info.offset);
                    MediaCodec.BufferInfo bufferInfo = this.info;
                    byteBuffer2.limit(bufferInfo.offset + bufferInfo.size);
                    ByteBuffer allocate = ByteBuffer.allocate(byteBuffer2.capacity());
                    allocate.clear();
                    allocate.position(this.info.offset);
                    int i3 = this.info.size;
                    byte[] bArr2 = new byte[i3];
                    byteBuffer2.get(bArr2, 0, i3);
                    allocate.put(bArr2, 0, this.info.size);
                    allocate.position(this.info.offset);
                    MediaCodec.BufferInfo bufferInfo2 = this.info;
                    allocate.limit(bufferInfo2.offset + bufferInfo2.size);
                    MoviePlayer.MediaFrame mediaFrame = new MoviePlayer.MediaFrame();
                    mediaFrame.buffer = allocate;
                    MediaCodec.BufferInfo bufferInfo3 = new MediaCodec.BufferInfo();
                    mediaFrame.info = bufferInfo3;
                    MediaCodec.BufferInfo bufferInfo4 = this.info;
                    bufferInfo3.set(bufferInfo4.offset, bufferInfo4.size, bufferInfo4.presentationTimeUs, bufferInfo4.flags);
                    MediaCodec.BufferInfo bufferInfo5 = this.info;
                    if ((bufferInfo5.flags & 2) != 0) {
                        bufferInfo5.size = 0;
                        Logg.LogI("ignoring BUFFER_FLAG_CODEC_CONFIG");
                    }
                    MiHWEncoder miHWEncoder = this.encodercallback;
                    if (miHWEncoder != null && this.info.size != 0) {
                        miHWEncoder.addAudioFrame(mediaFrame);
                    }
                    this.encoder.releaseOutputBuffer(dequeueOutputBuffer, false);
                    dequeueOutputBuffer = this.encoder.dequeueOutputBuffer(this.info, 0L);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return i2 >= 0;
    }

    private void addADtsHeader(byte[] bArr, int i, int i2) {
        bArr[0] = -1;
        bArr[1] = -7;
        bArr[2] = (byte) (64 + (i2 << 2) + 0);
        bArr[3] = (byte) (128 + (i >> 11));
        bArr[4] = (byte) ((i & 2047) >> 3);
        bArr[5] = (byte) (((i & 7) << 5) + 31);
        bArr[6] = -4;
    }

    public void releaseMedicacodec() {
        MediaCodec mediaCodec = this.encoder;
        if (mediaCodec == null) {
            return;
        }
        try {
            this.recordTime = SearchStatUtils.POW;
            mediaCodec.stop();
            this.encoder.release();
            this.encoder = null;
            this.encoderFormat = null;
            this.info = null;
            this.initmediacodec = false;
            Logg.LogI("AudioEncoder end!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
