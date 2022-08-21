package com.nexstreaming.app.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* compiled from: StreamUtil.java */
/* loaded from: classes3.dex */
public class m {
    public static long a(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        long j = 0;
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 != read) {
                outputStream.write(bArr, 0, read);
                j += read;
            } else {
                return j;
            }
        }
    }
}
