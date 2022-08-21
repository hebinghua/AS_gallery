package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.platform.Platform;

@Keep
/* loaded from: classes3.dex */
public class MdtaBox extends Box {
    private static final String FOURCC = "mdta";
    private String key;

    public static String fourcc() {
        return FOURCC;
    }

    public MdtaBox(Header header) {
        super(header);
    }

    public static MdtaBox createMdtaBox(String str) {
        MdtaBox mdtaBox = new MdtaBox(Header.createHeader(FOURCC, 0L));
        mdtaBox.key = str;
        return mdtaBox;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        this.key = Platform.stringFromBytes(NIOUtils.toArray(NIOUtils.readBuf(byteBuffer)));
    }

    public String getKey() {
        return this.key;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        byteBuffer.put(this.key.getBytes());
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public int estimateSize() {
        return this.key.getBytes().length;
    }
}
