package com.nexstreaming.kminternal.kinemaster.codeccolorformat;

import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.IOException;
import java.nio.ByteBuffer;

/* compiled from: WrapMediaDecoder.java */
/* loaded from: classes3.dex */
public class b extends a {
    private d a;
    private int b;
    private MediaCodec c;
    private Surface d;
    private MediaFormat e;
    private int f;
    private int g;
    private ByteBuffer[] h;
    private ByteBuffer[] i;

    public b(MediaFormat mediaFormat, Surface surface) throws IOException {
        this.b = 1;
        String string = mediaFormat.getString("mime");
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType(string);
        this.c = createDecoderByType;
        if (createDecoderByType == null) {
            Log.e("WrapMediaDecoder", "Can't create media codec!");
            return;
        }
        createDecoderByType.configure(mediaFormat, surface, (MediaCrypto) null, 0);
        if (string.startsWith("video/")) {
            this.d = surface;
            this.b = 0;
        }
        this.c.start();
        this.h = this.c.getInputBuffers();
        this.i = this.c.getOutputBuffers();
        this.f = mediaFormat.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
        this.g = mediaFormat.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
    }

    public void a() throws IOException {
        this.c.stop();
        this.c.release();
    }

    public boolean a(byte[] bArr, long j) {
        MediaCodec mediaCodec = this.c;
        if (mediaCodec == null || this.h == null || this.i == null || this.a == null) {
            Log.e("WrapMediaDecoder", "Media codec did not initailize");
            return false;
        }
        int dequeueInputBuffer = mediaCodec.dequeueInputBuffer(-1L);
        if (dequeueInputBuffer >= 0) {
            if (bArr == null || bArr.length <= 0) {
                this.c.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                Log.d("WrapMediaDecoder", String.format("WrapMediaDecoder EOS pass", new Object[0]));
            } else {
                ByteBuffer byteBuffer = this.h[dequeueInputBuffer];
                byteBuffer.clear();
                byteBuffer.put(bArr);
                this.c.queueInputBuffer(dequeueInputBuffer, 0, bArr.length, j, 0);
                Log.d("WrapMediaDecoder", String.format("WrapMediaDecoder one frame pass time(%d)", Long.valueOf(j)));
            }
        }
        b();
        return true;
    }

    public boolean a(ByteBuffer byteBuffer, long j) {
        MediaCodec mediaCodec = this.c;
        if (mediaCodec == null || this.h == null || this.i == null || this.a == null) {
            Log.e("WrapMediaDecoder", "Media codec did not initailize");
            return false;
        }
        int dequeueInputBuffer = mediaCodec.dequeueInputBuffer(AbstractComponentTracker.LINGERING_TIMEOUT);
        if (dequeueInputBuffer >= 0) {
            if (byteBuffer == null) {
                this.c.queueInputBuffer(dequeueInputBuffer, 0, 0, j, 4);
                Log.d("WrapMediaDecoder", String.format("WrapMediaDecoder EOS pass", new Object[0]));
            } else {
                ByteBuffer byteBuffer2 = this.h[dequeueInputBuffer];
                byteBuffer2.clear();
                byteBuffer2.put(byteBuffer);
                this.c.queueInputBuffer(dequeueInputBuffer, 0, byteBuffer.limit(), j, 0);
                Log.d("WrapMediaDecoder", String.format("WrapMediaDecoder one frame pass", new Object[0]));
            }
        }
        b();
        return true;
    }

    public boolean b() {
        boolean a;
        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int dequeueOutputBuffer = this.c.dequeueOutputBuffer(bufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
        Log.d("WrapMediaDecoder", String.format("OutputBuffer outIndex(%d)", Integer.valueOf(dequeueOutputBuffer)));
        if (dequeueOutputBuffer == -3) {
            Log.d("WrapMediaDecoder", "INFO_OUTPUT_BUFFERS_CHANGED");
            this.i = this.c.getOutputBuffers();
            this.a.a(1);
        } else if (dequeueOutputBuffer == -2) {
            MediaFormat outputFormat = this.c.getOutputFormat();
            this.e = outputFormat;
            if (outputFormat != null) {
                this.f = outputFormat.getInteger(nexExportFormat.TAG_FORMAT_WIDTH);
                this.g = this.e.getInteger(nexExportFormat.TAG_FORMAT_HEIGHT);
            }
            Log.d("WrapMediaDecoder", "New format " + this.e);
            this.a.a(2);
        } else if (dequeueOutputBuffer != -1) {
            while (true) {
                if (dequeueOutputBuffer < 0) {
                    break;
                }
                if ((bufferInfo.flags & 4) != 0) {
                    Log.d("WrapMediaDecoder", "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                    this.a.a(4);
                }
                ByteBuffer byteBuffer = this.i[dequeueOutputBuffer];
                if (byteBuffer == null) {
                    this.c.releaseOutputBuffer(dequeueOutputBuffer, true);
                    Log.w("WrapMediaDecoder", "OutputBuffer was not available");
                    break;
                } else if (bufferInfo.size <= 0) {
                    this.c.releaseOutputBuffer(dequeueOutputBuffer, true);
                    Log.w("WrapMediaDecoder", "Output was not available!");
                    break;
                } else {
                    Log.d("WrapMediaDecoder", "Can't use; render anyway, " + byteBuffer);
                    if (this.d != null) {
                        a = this.a.a(this.b, bufferInfo.presentationTimeUs);
                    } else {
                        int i = bufferInfo.size;
                        byte[] bArr = new byte[i];
                        byteBuffer.get(bArr);
                        byteBuffer.position(0);
                        a = this.a.a(bArr, this.b, i, bufferInfo.presentationTimeUs);
                    }
                    this.c.releaseOutputBuffer(dequeueOutputBuffer, a);
                    dequeueOutputBuffer = this.c.dequeueOutputBuffer(bufferInfo, AbstractComponentTracker.LINGERING_TIMEOUT);
                }
            }
        } else {
            Log.d("WrapMediaDecoder", "dequeueOutputBuffer timed out!");
            this.a.a(3);
        }
        return true;
    }

    public void a(d dVar) {
        this.a = dVar;
    }

    public int c() {
        return this.f;
    }

    public int d() {
        return this.g;
    }
}
