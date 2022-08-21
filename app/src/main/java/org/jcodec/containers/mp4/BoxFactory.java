package org.jcodec.containers.mp4;

import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Header;
import org.jcodec.containers.mp4.boxes.NodeBox;
import org.jcodec.platform.Platform;

/* loaded from: classes3.dex */
public class BoxFactory implements IBoxFactory {
    public static IBoxFactory instance = new BoxFactory(new DefaultBoxes());
    public Boxes boxes;

    public static IBoxFactory getDefault() {
        return instance;
    }

    public BoxFactory(Boxes boxes) {
        this.boxes = boxes;
    }

    @Override // org.jcodec.containers.mp4.IBoxFactory
    public Box newBox(Header header) {
        Class<? extends Box> cls = this.boxes.toClass(header.getFourcc());
        if (cls == null) {
            return new Box.LeafBox(header);
        }
        Box box = (Box) Platform.newInstance(cls, new Object[]{header});
        if (box instanceof NodeBox) {
            ((NodeBox) box).setFactory(this);
        }
        return box;
    }
}
