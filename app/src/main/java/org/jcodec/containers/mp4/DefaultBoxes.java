package org.jcodec.containers.mp4;

import org.jcodec.containers.mp4.boxes.FileTypeBox;
import org.jcodec.containers.mp4.boxes.HandlerBox;
import org.jcodec.containers.mp4.boxes.IListBox;
import org.jcodec.containers.mp4.boxes.KeysBox;
import org.jcodec.containers.mp4.boxes.MetaBox;
import org.jcodec.containers.mp4.boxes.MovieBox;
import org.jcodec.containers.mp4.boxes.MtagBox;
import org.jcodec.containers.mp4.boxes.UdtaBox;

/* loaded from: classes3.dex */
public class DefaultBoxes extends Boxes {
    public DefaultBoxes() {
        this.mappings.put(MetaBox.fourcc(), MetaBox.class);
        this.mappings.put(FileTypeBox.fourcc(), FileTypeBox.class);
        this.mappings.put(MovieBox.fourcc(), MovieBox.class);
        this.mappings.put(IListBox.fourcc(), IListBox.class);
        this.mappings.put("keys", KeysBox.class);
        this.mappings.put(HandlerBox.fourcc(), HandlerBox.class);
        this.mappings.put(UdtaBox.fourcc(), UdtaBox.class);
        this.mappings.put(MtagBox.fourcc(), MtagBox.class);
    }
}
