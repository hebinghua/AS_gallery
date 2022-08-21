package org.jcodec.containers.mp4;

import java.util.HashMap;
import java.util.Map;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes3.dex */
public abstract class Boxes {
    public final Map<String, Class<? extends Box>> mappings = new HashMap();

    public Class<? extends Box> toClass(String str) {
        return this.mappings.get(str);
    }
}
