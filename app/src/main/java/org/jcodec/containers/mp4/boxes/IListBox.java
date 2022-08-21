package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.containers.mp4.Boxes;
import org.jcodec.containers.mp4.IBoxFactory;

@Keep
/* loaded from: classes3.dex */
public class IListBox extends Box {
    private static final String FOURCC = "ilst";
    private IBoxFactory factory;
    private Map<Integer, List<Box>> values;

    public static String fourcc() {
        return FOURCC;
    }

    /* loaded from: classes3.dex */
    public static class LocalBoxes extends Boxes {
        public LocalBoxes() {
            this.mappings.put(DataBox.fourcc(), DataBox.class);
        }
    }

    public IListBox(Header header) {
        super(header);
        this.factory = new SimpleBoxFactory(new LocalBoxes());
        this.values = new LinkedHashMap();
    }

    public static IListBox createIListBox(Map<Integer, List<Box>> map) {
        IListBox iListBox = new IListBox(Header.createHeader(FOURCC, 0L));
        iListBox.values = map;
        return iListBox;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void parse(ByteBuffer byteBuffer) {
        while (byteBuffer.remaining() >= 4) {
            ByteBuffer read = NIOUtils.read(byteBuffer, byteBuffer.getInt() - 4);
            int i = read.getInt();
            ArrayList arrayList = new ArrayList();
            this.values.put(Integer.valueOf(i), arrayList);
            while (read.hasRemaining()) {
                Header read2 = Header.read(read);
                if (read2 != null && read.remaining() >= read2.getBodySize()) {
                    arrayList.add(Box.parseBox(NIOUtils.read(read, (int) read2.getBodySize()), read2, this.factory));
                }
            }
        }
    }

    public Map<Integer, List<Box>> getValues() {
        return this.values;
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public void doWrite(ByteBuffer byteBuffer) {
        for (Map.Entry<Integer, List<Box>> entry : this.values.entrySet()) {
            ByteBuffer duplicate = byteBuffer.duplicate();
            byteBuffer.putInt(0);
            byteBuffer.putInt(entry.getKey().intValue());
            for (Box box : entry.getValue()) {
                box.write(byteBuffer);
            }
            duplicate.putInt(byteBuffer.position() - duplicate.position());
        }
    }

    @Override // org.jcodec.containers.mp4.boxes.Box
    public int estimateSize() {
        int i = 8;
        for (Map.Entry<Integer, List<Box>> entry : this.values.entrySet()) {
            for (Box box : entry.getValue()) {
                i += box.estimateSize() + 8;
            }
        }
        return i;
    }
}
