package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;

@Keep
/* loaded from: classes3.dex */
public abstract class FullBox extends Box {
    public int flags;
    public byte version;

    public FullBox(Header header) {
        super(header);
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        int i = byteBuffer.getInt();
        this.version = (byte) ((i >> 24) & 255);
        this.flags = i & 16777215;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.putInt((this.version << 24) | (this.flags & 16777215));
    }

    public byte getVersion() {
        return this.version;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setVersion(byte b) {
        this.version = b;
    }

    public void setFlags(int i) {
        this.flags = i;
    }
}
