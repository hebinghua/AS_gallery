package okio;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/* loaded from: classes3.dex */
public interface BufferedSink extends Sink, WritableByteChannel {
    Buffer buffer();

    BufferedSink emit() throws IOException;

    /* renamed from: emitCompleteSegments */
    BufferedSink mo2627emitCompleteSegments() throws IOException;

    @Override // okio.Sink, java.io.Flushable
    void flush() throws IOException;

    /* renamed from: write */
    BufferedSink mo2628write(ByteString byteString) throws IOException;

    /* renamed from: write */
    BufferedSink mo2629write(byte[] bArr) throws IOException;

    /* renamed from: write */
    BufferedSink mo2630write(byte[] bArr, int i, int i2) throws IOException;

    /* renamed from: writeByte */
    BufferedSink mo2631writeByte(int i) throws IOException;

    /* renamed from: writeHexadecimalUnsignedLong */
    BufferedSink mo2632writeHexadecimalUnsignedLong(long j) throws IOException;

    /* renamed from: writeInt */
    BufferedSink mo2633writeInt(int i) throws IOException;

    /* renamed from: writeLong */
    BufferedSink mo2634writeLong(long j) throws IOException;

    /* renamed from: writeShort */
    BufferedSink mo2635writeShort(int i) throws IOException;

    /* renamed from: writeUtf8 */
    BufferedSink mo2636writeUtf8(String str) throws IOException;

    /* renamed from: writeUtf8 */
    BufferedSink mo2637writeUtf8(String str, int i, int i2) throws IOException;
}
