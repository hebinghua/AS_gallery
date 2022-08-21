package com.miui.gallery.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class StreamUtils {
    public static String toString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = toByteArrayOutputStream(inputStream);
        if (byteArrayOutputStream != null) {
            return byteArrayOutputStream.toString();
        }
        return null;
    }

    public static ByteArrayOutputStream toByteArrayOutputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[512];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                return byteArrayOutputStream;
            }
        }
    }
}
