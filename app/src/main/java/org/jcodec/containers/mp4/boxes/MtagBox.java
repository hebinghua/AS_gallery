package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import org.jcodec.common.io.NIOUtils;

@Keep
/* loaded from: classes3.dex */
public class MtagBox extends Box {
    private static final String FOURCC = "mtag";
    private byte[] data;

    public static String fourcc() {
        return FOURCC;
    }

    public MtagBox(Header header) {
        super(header);
    }

    public static MtagBox createMtagBox(byte[] bArr) {
        MtagBox mtagBox = new MtagBox(Header.createHeader(FOURCC, 0L));
        mtagBox.data = bArr;
        return mtagBox;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        this.data = NIOUtils.toArray(NIOUtils.readBuf(byteBuffer));
    }

    public byte[] getData() {
        return this.data;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.put(this.data);
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public int estimateSize() {
        return this.data.length + 8;
    }
}
