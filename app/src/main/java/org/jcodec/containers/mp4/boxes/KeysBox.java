package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import org.jcodec.containers.mp4.Boxes;

@Keep
/* loaded from: classes3.dex */
public class KeysBox extends NodeBox {
    private static final String FOURCC = "keys";

    public static String fourcc() {
        return FOURCC;
    }

    /* loaded from: classes3.dex */
    public static class LocalBoxes extends Boxes {
        public LocalBoxes() {
            this.mappings.put(MdtaBox.fourcc(), MdtaBox.class);
        }
    }

    public KeysBox(Header header) {
        super(header);
        this.factory = new SimpleBoxFactory(new LocalBoxes());
    }

    public static KeysBox createKeysBox() {
        return new KeysBox(Header.createHeader(FOURCC, 0L));
    }

    @Override // org.jcodec.containers.mp4.boxes.NodeBox, org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        byteBuffer.getInt();
        byteBuffer.getInt();
        super.parse(byteBuffer);
    }

    @Override // org.jcodec.containers.mp4.boxes.NodeBox, org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.putInt(0);
        byteBuffer.putInt(this.boxes.size());
        super.doWrite(byteBuffer);
    }

    @Override // org.jcodec.containers.mp4.boxes.NodeBox, org.jcodec.containers.mp4.boxes.Box
    public int estimateSize() {
        return super.estimateSize() + 8;
    }
}
