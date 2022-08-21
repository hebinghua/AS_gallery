package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;
import org.jcodec.containers.mp4.IBoxFactory;

@Keep
/* loaded from: classes3.dex */
public class UdtaBox extends NodeBox {
    private static final String FOURCC = "udta";

    public static String fourcc() {
        return FOURCC;
    }

    public static UdtaBox createUdtaBox() {
        return new UdtaBox(Header.createHeader(fourcc(), 0L));
    }

    @Override // org.jcodec.containers.mp4.boxes.NodeBox
    public void setFactory(final IBoxFactory iBoxFactory) {
        this.factory = new IBoxFactory() { // from class: org.jcodec.containers.mp4.boxes.UdtaBox.1
            @Override // org.jcodec.containers.mp4.IBoxFactory
            public Box newBox(Header header) {
                if (header.getFourcc().equals(MetaBox.fourcc())) {
                    UdtaMetaBox udtaMetaBox = new UdtaMetaBox(header);
                    udtaMetaBox.setFactory(iBoxFactory);
                    return udtaMetaBox;
                }
                return iBoxFactory.newBox(header);
            }
        };
    }

    public UdtaBox(Header header) {
        super(header);
    }

    public MetaBox meta() {
        return (MetaBox) NodeBox.findFirst(this, MetaBox.class, MetaBox.fourcc());
    }
}
