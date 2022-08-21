package com.nexstreaming.kminternal.kinemaster.codeccolorformat;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import java.io.IOException;
import java.nio.ByteBuffer;

/* compiled from: WrapMediaEncoder.java */
/* loaded from: classes3.dex */
public class c extends a {
    private d a;
    private MediaCodec b = MediaCodec.createEncoderByType("video/avc");
    private byte[] c;
    private byte[] d;
    private ByteBuffer[] e;
    private ByteBuffer[] f;

    public c() throws IOException {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", 1280, 720);
        createVideoFormat.setInteger("bitrate", 125000);
        createVideoFormat.setInteger("frame-rate", 30);
        createVideoFormat.setInteger("color-format", 21);
        createVideoFormat.setInteger("i-frame-interval", 5);
        this.b.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.b.start();
        this.e = this.b.getInputBuffers();
        this.f = this.b.getOutputBuffers();
    }

    public c(int i, int i2) throws IOException {
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", i, i2);
        createVideoFormat.setInteger("bitrate", 125000);
        createVideoFormat.setInteger("frame-rate", 30);
        createVideoFormat.setInteger("color-format", 21);
        createVideoFormat.setInteger("i-frame-interval", 5);
        this.b.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
        this.b.start();
        this.e = this.b.getInputBuffers();
        this.f = this.b.getOutputBuffers();
    }

    public void a() throws IOException {
        this.b.stop();
        this.b.release();
    }

    public boolean a(byte[] bArr, long j) {
        MediaCodec mediaCodec = this.b;
        if (mediaCodec == null || this.e == null || this.f == null || this.a == null) {
            Log.w("WrapMediaEncoder", "Media codec did not initailize");
            return false;
        }
        ByteBuffer[] inputBuffers = mediaCodec.getInputBuffers();
        ByteBuffer[] outputBuffers = this.b.getOutputBuffers();
        int dequeueInputBuffer = this.b.dequeueInputBuffer(-1L);
        if (dequeueInputBuffer >= 0) {
            ByteBuffer byteBuffer = inputBuffers[dequeueInputBuffer];
            byteBuffer.clear();
            if (bArr != null) {
                byteBuffer.put(bArr);
                this.b.queueInputBuffer(dequeueInputBuffer, 0, bArr.length, j, 0);
            } else {
                this.b.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
            }
        }
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int dequeueOutputBuffer = this.b.dequeueOutputBuffer(bufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
        if (dequeueOutputBuffer == -3) {
            Log.d("WrapMediaEncoder", "INFO_OUTPUT_BUFFERS_CHANGED");
            this.b.getOutputBuffers();
            this.a.a(1);
        } else if (dequeueOutputBuffer == -2) {
            Log.d("WrapMediaEncoder", "New format " + this.b.getOutputFormat());
            this.a.a(2);
        } else if (dequeueOutputBuffer != -1) {
            while (true) {
                if (dequeueOutputBuffer < 0) {
                    break;
                }
                if ((bufferInfo.flags & 4) != 0) {
                    Log.d("WrapMediaEncoder", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                    this.a.a(4);
                }
                ByteBuffer byteBuffer2 = outputBuffers[dequeueOutputBuffer];
                if (byteBuffer2 == null) {
                    Log.w("WrapMediaEncoder", "Output buffer is null!");
                    break;
                } else if (bufferInfo.size <= 0) {
                    Log.w("WrapMediaEncoder", "Output was not available!");
                    break;
                } else {
                    Log.d("WrapMediaEncoder", String.format("Output was available Falg:%d Size:%d", Integer.valueOf(bufferInfo.flags), Integer.valueOf(bufferInfo.size)));
                    byteBuffer2.position(bufferInfo.offset);
                    byteBuffer2.limit(bufferInfo.offset + bufferInfo.size);
                    if ((bufferInfo.flags & 2) != 0) {
                        Log.d("WrapMediaEncoder", "OutputBuffer BUFFER_FLAG_CODEC_CONFIG");
                        int i = bufferInfo.size;
                        byte[] bArr2 = new byte[i];
                        byteBuffer2.get(bArr2);
                        ByteBuffer wrap = ByteBuffer.wrap(bArr2);
                        if (wrap.getInt() == 1) {
                            System.out.println("parsing sps/pps");
                        } else {
                            System.out.println("something is amiss?");
                        }
                        while (true) {
                            if (wrap.get() == 0 && wrap.get() == 0 && wrap.get() == 0 && wrap.get() == 1) {
                                break;
                            }
                        }
                        int position = wrap.position();
                        byte[] bArr3 = new byte[(position - 8) + 4];
                        this.c = bArr3;
                        bArr3[0] = 0;
                        bArr3[1] = 0;
                        bArr3[2] = 0;
                        bArr3[3] = 1;
                        System.arraycopy(bArr2, 4, bArr3, 4, bArr3.length - 4);
                        byte[] bArr4 = new byte[(i - position) + 4];
                        this.d = bArr4;
                        bArr4[0] = 0;
                        bArr4[1] = 0;
                        bArr4[2] = 0;
                        bArr4[3] = 1;
                        System.arraycopy(bArr2, position, bArr4, 4, bArr4.length - 4);
                        this.a.a(this.c, this.d);
                        this.b.releaseOutputBuffer(dequeueOutputBuffer, false);
                        dequeueOutputBuffer = this.b.dequeueOutputBuffer(bufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
                    } else {
                        int i2 = bufferInfo.size;
                        byte[] bArr5 = new byte[i2];
                        byteBuffer2.get(bArr5);
                        this.a.a(bArr5, 0, i2, bufferInfo.presentationTimeUs);
                        this.b.releaseOutputBuffer(dequeueOutputBuffer, false);
                        dequeueOutputBuffer = this.b.dequeueOutputBuffer(bufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
                    }
                }
            }
        } else {
            Log.d("WrapMediaEncoder", "dequeueOutputBuffer timed out!");
            this.a.a(3);
        }
        return true;
    }

    public void a(d dVar) {
        this.a = dVar;
    }
}
