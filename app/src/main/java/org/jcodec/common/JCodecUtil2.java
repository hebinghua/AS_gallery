package org.jcodec.common;

import org.jcodec.platform.Platform;

/* loaded from: classes3.dex */
public class JCodecUtil2 {
    public static byte[] asciiString(String str) {
        return Platform.getBytes(str);
    }
}
