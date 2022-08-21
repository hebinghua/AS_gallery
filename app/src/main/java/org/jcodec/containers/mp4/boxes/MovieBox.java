package org.jcodec.containers.mp4.boxes;

import androidx.annotation.Keep;

@Keep
/* loaded from: classes3.dex */
public class MovieBox extends NodeBox {
    public static String fourcc() {
        return "moov";
    }

    public MovieBox(Header header) {
        super(header);
    }
}
