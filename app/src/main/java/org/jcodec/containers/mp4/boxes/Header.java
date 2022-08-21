package org.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;
import org.jcodec.common.JCodecUtil2;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.platform.Platform;

/* loaded from: classes3.dex */
public class Header {
    public static final byte[] FOURCC_FREE = {102, 114, 101, 101};
    public String fourcc;
    public boolean lng;
    public long size;

    public static int estimateHeaderSize(int i) {
        return ((long) (i + 8)) > 4294967296L ? 16 : 8;
    }

    public Header(String str) {
        this.fourcc = str;
    }

    public static Header createHeader(String str, long j) {
        Header header = new Header(str);
        header.size = j;
        return header;
    }

    public static Header newHeader(String str, long j, boolean z) {
        Header header = new Header(str);
        header.size = j;
        header.lng = z;
        return header;
    }

    public static Header read(ByteBuffer byteBuffer) {
        long j = 0;
        while (byteBuffer.remaining() >= 4) {
            j = Platform.unsignedInt(byteBuffer.getInt());
            if (j != 0) {
                break;
            }
        }
        if (byteBuffer.remaining() < 4 || (j < 8 && j != 1)) {
            return null;
        }
        String readString = NIOUtils.readString(byteBuffer, 4);
        boolean z = false;
        if (j == 1) {
            if (byteBuffer.remaining() < 8) {
                return null;
            }
            z = true;
            j = byteBuffer.getLong();
        }
        return newHeader(readString, j, z);
    }

    public long headerSize() {
        return (this.lng || this.size > 4294967296L) ? 16L : 8L;
    }

    public String getFourcc() {
        return this.fourcc;
    }

    public long getBodySize() {
        return this.size - headerSize();
    }

    public void setBodySize(int i) {
        this.size = i + headerSize();
    }

    public void write(ByteBuffer byteBuffer) {
        long j = this.size;
        if (j > 4294967296L) {
            byteBuffer.putInt(1);
        } else {
            byteBuffer.putInt((int) j);
        }
        byte[] asciiString = JCodecUtil2.asciiString(this.fourcc);
        if (asciiString != null && asciiString.length == 4) {
            byteBuffer.put(asciiString);
        } else {
            byteBuffer.put(FOURCC_FREE);
        }
        long j2 = this.size;
        if (j2 > 4294967296L) {
            byteBuffer.putLong(j2);
        }
    }

    public long getSize() {
        return this.size;
    }

    public int hashCode() {
        String str = this.fourcc;
        return 31 + (str == null ? 0 : str.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Header header = (Header) obj;
        String str = this.fourcc;
        if (str == null) {
            if (header.fourcc != null) {
                return false;
            }
        } else if (!str.equals(header.fourcc)) {
            return false;
        }
        return true;
    }
}
