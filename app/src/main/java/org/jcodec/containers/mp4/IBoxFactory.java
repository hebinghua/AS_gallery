package org.jcodec.containers.mp4;

import org.jcodec.containers.mp4.boxes.Box;
import org.jcodec.containers.mp4.boxes.Header;

/* loaded from: classes3.dex */
public interface IBoxFactory {
    Box newBox(Header header);
}
