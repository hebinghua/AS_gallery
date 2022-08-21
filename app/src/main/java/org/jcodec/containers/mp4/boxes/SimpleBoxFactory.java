package org.jcodec.containers.mp4.boxes;

import org.jcodec.containers.mp4.Boxes;
import org.jcodec.containers.mp4.IBoxFactory;
import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.platform.Platform;

/* loaded from: classes3.dex */
public class SimpleBoxFactory implements IBoxFactory {
    public Boxes boxes;

    public SimpleBoxFactory(Boxes boxes) {
        this.boxes = boxes;
    }

    @Override // org.jcodec.containers.mp4.IBoxFactory
    public Box newBox(Header header) {
        Class<? extends Box> cls = this.boxes.toClass(header.getFourcc());
        if (cls == null) {
            return new Box.LeafBox(header);
        }
        return (Box) Platform.newInstance(cls, new Object[]{header});
    }
}
