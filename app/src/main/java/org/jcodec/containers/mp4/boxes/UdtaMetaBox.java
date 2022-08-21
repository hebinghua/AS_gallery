package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;

@Keep
/* loaded from: classes3.dex */
public class UdtaMetaBox extends MetaBox {
    public UdtaMetaBox(Header header) {
        super(header);
    }

    public static UdtaMetaBox createUdtaMetaBox() {
        return new UdtaMetaBox(Header.createHeader(MetaBox.fourcc(), 0L));
    }

    @Override // org.jcodec.containers.mp4.boxes.NodeBox, org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        byteBuffer.getInt();
        super.parse(byteBuffer);
    }

    @Override // org.jcodec.containers.mp4.boxes.NodeBox, org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.putInt(0);
        super.doWrite(byteBuffer);
    }
}
