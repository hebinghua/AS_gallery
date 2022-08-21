package com.miui.gallery3d.exif;

import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public class ByteBufferInputStream extends InputStream {
    public ByteBuffer mBuf;

    public ByteBufferInputStream(ByteBuffer byteBuffer) {
        this.mBuf = byteBuffer;
    }

    @Override // java.io.InputStream
    public int read() {
        if (!this.mBuf.hasRemaining()) {
            return -1;
        }
        return this.mBuf.get() & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) {
        if (!this.mBuf.hasRemaining()) {
            return -1;
        }
        int min = Math.min(i2, this.mBuf.remaining());
        this.mBuf.get(bArr, i, min);
        return min;
    }
}
