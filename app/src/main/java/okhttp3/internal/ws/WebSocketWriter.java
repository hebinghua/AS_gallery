package okhttp3.internal.ws;

import ch.qos.logback.core.FileAppender;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Timeout;

/* loaded from: classes3.dex */
public final class WebSocketWriter {
    public boolean activeWriter;
    public final Buffer buffer = new Buffer();
    public final FrameSink frameSink = new FrameSink();
    public final boolean isClient;
    public final byte[] maskBuffer;
    public final byte[] maskKey;
    public final Random random;
    public final BufferedSink sink;
    public boolean writerClosed;

    public WebSocketWriter(boolean z, BufferedSink bufferedSink, Random random) {
        Objects.requireNonNull(bufferedSink, "sink == null");
        Objects.requireNonNull(random, "random == null");
        this.isClient = z;
        this.sink = bufferedSink;
        this.random = random;
        byte[] bArr = null;
        this.maskKey = z ? new byte[4] : null;
        this.maskBuffer = z ? new byte[8192] : bArr;
    }

    public void writePing(ByteString byteString) throws IOException {
        writeControlFrame(9, byteString);
    }

    public void writePong(ByteString byteString) throws IOException {
        writeControlFrame(10, byteString);
    }

    public void writeClose(int i, ByteString byteString) throws IOException {
        ByteString byteString2 = ByteString.EMPTY;
        if (i != 0 || byteString != null) {
            if (i != 0) {
                WebSocketProtocol.validateCloseCode(i);
            }
            Buffer buffer = new Buffer();
            buffer.mo2635writeShort(i);
            if (byteString != null) {
                buffer.mo2628write(byteString);
            }
            byteString2 = buffer.readByteString();
        }
        try {
            writeControlFrame(8, byteString2);
        } finally {
            this.writerClosed = true;
        }
    }

    public final void writeControlFrame(int i, ByteString byteString) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        int size = byteString.size();
        if (size > 125) {
            throw new IllegalArgumentException("Payload size must be less than or equal to 125");
        }
        this.sink.mo2631writeByte(i | 128);
        if (this.isClient) {
            this.sink.mo2631writeByte(size | 128);
            this.random.nextBytes(this.maskKey);
            this.sink.mo2629write(this.maskKey);
            byte[] byteArray = byteString.toByteArray();
            WebSocketProtocol.toggleMask(byteArray, byteArray.length, this.maskKey, 0L);
            this.sink.mo2629write(byteArray);
        } else {
            this.sink.mo2631writeByte(size);
            this.sink.mo2628write(byteString);
        }
        this.sink.flush();
    }

    public Sink newMessageSink(int i, long j) {
        if (this.activeWriter) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.activeWriter = true;
        FrameSink frameSink = this.frameSink;
        frameSink.formatOpcode = i;
        frameSink.contentLength = j;
        frameSink.isFirstFrame = true;
        frameSink.closed = false;
        return frameSink;
    }

    public void writeMessageFrame(int i, long j, boolean z, boolean z2) throws IOException {
        if (this.writerClosed) {
            throw new IOException("closed");
        }
        if (!z) {
            i = 0;
        }
        if (z2) {
            i |= 128;
        }
        this.sink.mo2631writeByte(i);
        int i2 = this.isClient ? 128 : 0;
        if (j <= 125) {
            this.sink.mo2631writeByte(i2 | ((int) j));
        } else if (j <= 65535) {
            this.sink.mo2631writeByte(i2 | 126);
            this.sink.mo2635writeShort((int) j);
        } else {
            this.sink.mo2631writeByte(i2 | BaiduSceneResult.BANK_CARD);
            this.sink.mo2634writeLong(j);
        }
        if (this.isClient) {
            this.random.nextBytes(this.maskKey);
            this.sink.mo2629write(this.maskKey);
            long j2 = 0;
            while (j2 < j) {
                int read = this.buffer.read(this.maskBuffer, 0, (int) Math.min(j, this.maskBuffer.length));
                if (read == -1) {
                    throw new AssertionError();
                }
                long j3 = read;
                WebSocketProtocol.toggleMask(this.maskBuffer, j3, this.maskKey, j2);
                this.sink.mo2630write(this.maskBuffer, 0, read);
                j2 += j3;
            }
        } else {
            this.sink.write(this.buffer, j);
        }
        this.sink.emit();
    }

    /* loaded from: classes3.dex */
    public final class FrameSink implements Sink {
        public boolean closed;
        public long contentLength;
        public int formatOpcode;
        public boolean isFirstFrame;

        public FrameSink() {
        }

        @Override // okio.Sink
        public void write(Buffer buffer, long j) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter.this.buffer.write(buffer, j);
            boolean z = this.isFirstFrame && this.contentLength != -1 && WebSocketWriter.this.buffer.size() > this.contentLength - FileAppender.DEFAULT_BUFFER_SIZE;
            long completeSegmentByteCount = WebSocketWriter.this.buffer.completeSegmentByteCount();
            if (completeSegmentByteCount <= 0 || z) {
                return;
            }
            WebSocketWriter.this.writeMessageFrame(this.formatOpcode, completeSegmentByteCount, this.isFirstFrame, false);
            this.isFirstFrame = false;
        }

        @Override // okio.Sink, java.io.Flushable
        public void flush() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter webSocketWriter = WebSocketWriter.this;
            webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.buffer.size(), this.isFirstFrame, false);
            this.isFirstFrame = false;
        }

        @Override // okio.Sink
        public Timeout timeout() {
            return WebSocketWriter.this.sink.timeout();
        }

        @Override // okio.Sink, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter webSocketWriter = WebSocketWriter.this;
            webSocketWriter.writeMessageFrame(this.formatOpcode, webSocketWriter.buffer.size(), this.isFirstFrame, true);
            this.closed = true;
            WebSocketWriter.this.activeWriter = false;
        }
    }
}
