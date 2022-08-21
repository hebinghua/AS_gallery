package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import org.jcodec.common.io.NIOUtils;

@Keep
/* loaded from: classes3.dex */
public class MCoverBox extends Box {
    private static final String FOURCC = "mcvr";
    private byte[] data;

    public static String fourcc() {
        return FOURCC;
    }

    public MCoverBox(Header header) {
        super(header);
    }

    public static MCoverBox createCoverBox(byte[] bArr) {
        MCoverBox mCoverBox = new MCoverBox(Header.createHeader(FOURCC, 0L));
        mCoverBox.data = bArr;
        return mCoverBox;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        this.data = NIOUtils.toArray(NIOUtils.readBuf(byteBuffer));
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.put(this.data);
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public int estimateSize() {
        return this.data.length + 8;
    }

    public byte[] getData() {
        return this.data;
    }
}
