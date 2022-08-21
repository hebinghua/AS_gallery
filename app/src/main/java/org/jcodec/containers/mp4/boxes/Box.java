package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import ch.qos.logback.core.CoreConstants;
import java.nio.ByteBuffer;
import org.jcodec.common.Preconditions;
import org.jcodec.common.StringUtils;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.IBoxFactory;
import org.jcodec.platform.Platform;

@Keep
/* loaded from: classes3.dex */
public abstract class Box {
    public static final int MAX_BOX_SIZE = 134217728;
    public Header header;

    public abstract void doWrite(ByteBuffer byteBuffer);

    public abstract int estimateSize();

    public abstract void parse(ByteBuffer byteBuffer);

    public Box(Header header) {
        this.header = header;
    }

    public Header getHeader() {
        return this.header;
    }

    public void write(ByteBuffer byteBuffer) {
        ByteBuffer duplicate = byteBuffer.duplicate();
        NIOUtils.skip(byteBuffer, 8);
        doWrite(byteBuffer);
        this.header.setBodySize((byteBuffer.position() - duplicate.position()) - 8);
        Preconditions.checkState(this.header.headerSize() == 8);
        this.header.write(duplicate);
    }

    public String getFourcc() {
        return this.header.getFourcc();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        dump(sb);
        return sb.toString();
    }

    public void dump(StringBuilder sb) {
        sb.append("{\"tag\":\"" + this.header.getFourcc() + "\"}");
    }

    public static Box terminatorAtom() {
        return createLeafBox(new Header(Platform.stringFromBytes(new byte[4])), ByteBuffer.allocate(0));
    }

    public static String[] path(String str) {
        return StringUtils.splitC(str, CoreConstants.DOT);
    }

    public static LeafBox createLeafBox(Header header, ByteBuffer byteBuffer) {
        LeafBox leafBox = new LeafBox(header);
        leafBox.data = byteBuffer;
        return leafBox;
    }

    public static Box parseBox(ByteBuffer byteBuffer, Header header, IBoxFactory iBoxFactory) {
        Box newBox = iBoxFactory.newBox(header);
        if (header.getBodySize() < 134217728) {
            newBox.parse(byteBuffer);
            return newBox;
        }
        return new LeafBox(Header.createHeader("free", 8L));
    }

    public static <T extends Box> T asBox(Class<T> cls, Box box) {
        try {
            T t = (T) Platform.newInstance(cls, new Object[]{box.getHeader()});
            ByteBuffer allocate = ByteBuffer.allocate((int) box.getHeader().getBodySize());
            box.doWrite(allocate);
            allocate.flip();
            t.parse(allocate);
            return t;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* loaded from: classes3.dex */
    public static class LeafBox extends Box {
        public ByteBuffer data;

        public LeafBox(Header header) {
            super(header);
        }

        @Override // org.jcodec.containers.mp4.boxes.Box
        public void parse(ByteBuffer byteBuffer) {
            this.data = NIOUtils.read(byteBuffer, (int) this.header.getBodySize());
        }

        @Override // org.jcodec.containers.mp4.boxes.Box
        public void doWrite(ByteBuffer byteBuffer) {
            NIOUtils.write(byteBuffer, this.data);
        }

        @Override // org.jcodec.containers.mp4.boxes.Box
        public int estimateSize() {
            return this.data.remaining() + Header.estimateHeaderSize(this.data.remaining());
        }
    }
}
