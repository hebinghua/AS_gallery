package okhttp3.internal.ws;

import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;

/* loaded from: classes3.dex */
public final class WebSocketReader {
    public boolean closed;
    public long frameBytesRead;
    public final FrameCallback frameCallback;
    public long frameLength;
    public final boolean isClient;
    public boolean isControlFrame;
    public boolean isFinalFrame;
    public boolean isMasked;
    public int opcode;
    public final BufferedSource source;
    public final byte[] maskKey = new byte[4];
    public final byte[] maskBuffer = new byte[8192];

    /* loaded from: classes3.dex */
    public interface FrameCallback {
        void onReadClose(int i, String str);

        void onReadMessage(String str) throws IOException;

        void onReadMessage(ByteString byteString) throws IOException;

        void onReadPing(ByteString byteString);

        void onReadPong(ByteString byteString);
    }

    public WebSocketReader(boolean z, BufferedSource bufferedSource, FrameCallback frameCallback) {
        Objects.requireNonNull(bufferedSource, "source == null");
        Objects.requireNonNull(frameCallback, "frameCallback == null");
        this.isClient = z;
        this.source = bufferedSource;
        this.frameCallback = frameCallback;
    }

    public void processNextFrame() throws IOException {
        readHeader();
        if (this.isControlFrame) {
            readControlFrame();
        } else {
            readMessageFrame();
        }
    }

    /* JADX WARN: Finally extract failed */
    public final void readHeader() throws IOException {
        if (this.closed) {
            throw new IOException("closed");
        }
        long timeoutNanos = this.source.timeout().timeoutNanos();
        this.source.timeout().clearTimeout();
        try {
            int readByte = this.source.readByte() & 255;
            this.source.timeout().timeout(timeoutNanos, TimeUnit.NANOSECONDS);
            this.opcode = readByte & 15;
            boolean z = true;
            boolean z2 = (readByte & 128) != 0;
            this.isFinalFrame = z2;
            boolean z3 = (readByte & 8) != 0;
            this.isControlFrame = z3;
            if (z3 && !z2) {
                throw new ProtocolException("Control frames must be final.");
            }
            boolean z4 = (readByte & 64) != 0;
            boolean z5 = (readByte & 32) != 0;
            boolean z6 = (readByte & 16) != 0;
            if (z4 || z5 || z6) {
                throw new ProtocolException("Reserved flags are unsupported.");
            }
            int readByte2 = this.source.readByte() & 255;
            if ((readByte2 & 128) == 0) {
                z = false;
            }
            this.isMasked = z;
            if (z == this.isClient) {
                throw new ProtocolException(this.isClient ? "Server-sent frames must not be masked." : "Client-sent frames must be masked.");
            }
            long j = readByte2 & BaiduSceneResult.BANK_CARD;
            this.frameLength = j;
            if (j == 126) {
                this.frameLength = this.source.readShort() & 65535;
            } else if (j == 127) {
                long readLong = this.source.readLong();
                this.frameLength = readLong;
                if (readLong < 0) {
                    throw new ProtocolException("Frame length 0x" + Long.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF");
                }
            }
            this.frameBytesRead = 0L;
            if (this.isControlFrame && this.frameLength > 125) {
                throw new ProtocolException("Control frame must be less than 125B.");
            }
            if (!this.isMasked) {
                return;
            }
            this.source.readFully(this.maskKey);
        } catch (Throwable th) {
            this.source.timeout().timeout(timeoutNanos, TimeUnit.NANOSECONDS);
            throw th;
        }
    }

    public final void readControlFrame() throws IOException {
        String str;
        Buffer buffer = new Buffer();
        long j = this.frameBytesRead;
        long j2 = this.frameLength;
        if (j < j2) {
            if (!this.isClient) {
                while (true) {
                    long j3 = this.frameBytesRead;
                    long j4 = this.frameLength;
                    if (j3 >= j4) {
                        break;
                    }
                    int read = this.source.read(this.maskBuffer, 0, (int) Math.min(j4 - j3, this.maskBuffer.length));
                    if (read == -1) {
                        throw new EOFException();
                    }
                    long j5 = read;
                    WebSocketProtocol.toggleMask(this.maskBuffer, j5, this.maskKey, this.frameBytesRead);
                    buffer.mo2630write(this.maskBuffer, 0, read);
                    this.frameBytesRead += j5;
                }
            } else {
                this.source.readFully(buffer, j2);
            }
        }
        switch (this.opcode) {
            case 8:
                short s = 1005;
                long size = buffer.size();
                if (size == 1) {
                    throw new ProtocolException("Malformed close payload length of 1.");
                }
                if (size != 0) {
                    s = buffer.readShort();
                    str = buffer.readUtf8();
                    String closeCodeExceptionMessage = WebSocketProtocol.closeCodeExceptionMessage(s);
                    if (closeCodeExceptionMessage != null) {
                        throw new ProtocolException(closeCodeExceptionMessage);
                    }
                } else {
                    str = "";
                }
                this.frameCallback.onReadClose(s, str);
                this.closed = true;
                return;
            case 9:
                this.frameCallback.onReadPing(buffer.readByteString());
                return;
            case 10:
                this.frameCallback.onReadPong(buffer.readByteString());
                return;
            default:
                throw new ProtocolException("Unknown control opcode: " + Integer.toHexString(this.opcode));
        }
    }

    public final void readMessageFrame() throws IOException {
        int i = this.opcode;
        if (i != 1 && i != 2) {
            throw new ProtocolException("Unknown opcode: " + Integer.toHexString(i));
        }
        Buffer buffer = new Buffer();
        readMessage(buffer);
        if (i == 1) {
            this.frameCallback.onReadMessage(buffer.readUtf8());
        } else {
            this.frameCallback.onReadMessage(buffer.readByteString());
        }
    }

    public void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            readHeader();
            if (!this.isControlFrame) {
                return;
            }
            readControlFrame();
        }
    }

    public final void readMessage(Buffer buffer) throws IOException {
        long read;
        while (!this.closed) {
            if (this.frameBytesRead == this.frameLength) {
                if (this.isFinalFrame) {
                    return;
                }
                readUntilNonControlFrame();
                if (this.opcode != 0) {
                    throw new ProtocolException("Expected continuation opcode. Got: " + Integer.toHexString(this.opcode));
                } else if (this.isFinalFrame && this.frameLength == 0) {
                    return;
                }
            }
            long j = this.frameLength - this.frameBytesRead;
            if (this.isMasked) {
                read = this.source.read(this.maskBuffer, 0, (int) Math.min(j, this.maskBuffer.length));
                if (read == -1) {
                    throw new EOFException();
                }
                WebSocketProtocol.toggleMask(this.maskBuffer, read, this.maskKey, this.frameBytesRead);
                buffer.mo2630write(this.maskBuffer, 0, (int) read);
            } else {
                read = this.source.read(buffer, j);
                if (read == -1) {
                    throw new EOFException();
                }
            }
            this.frameBytesRead += read;
        }
        throw new IOException("closed");
    }
}
