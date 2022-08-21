package com.miui.gallery3d.exif;

import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/* loaded from: classes3.dex */
public class CountedDataInputStream extends FilterInputStream {
    public final byte[] mByteArray;
    public final ByteBuffer mByteBuffer;
    public int mCount;

    public CountedDataInputStream(InputStream inputStream) {
        super(inputStream);
        this.mCount = 0;
        byte[] bArr = new byte[8];
        this.mByteArray = bArr;
        this.mByteBuffer = ByteBuffer.wrap(bArr);
    }

    public int getReadByteCount() {
        return this.mCount;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        int read = ((FilterInputStream) this).in.read(bArr);
        this.mCount += read >= 0 ? read : 0;
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        int read = ((FilterInputStream) this).in.read(bArr, i, i2);
        this.mCount += read >= 0 ? read : 0;
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        int read = ((FilterInputStream) this).in.read();
        this.mCount += read >= 0 ? 1 : 0;
        return read;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long j) throws IOException {
        long skip = ((FilterInputStream) this).in.skip(j);
        this.mCount = (int) (this.mCount + skip);
        return skip;
    }

    public void skipOrThrow(long j) throws IOException {
        if (skip(j) == j) {
            return;
        }
        throw new EOFException();
    }

    public void skipTo(long j) throws IOException {
        skipOrThrow(j - this.mCount);
    }

    public void readOrThrow(byte[] bArr, int i, int i2) throws IOException {
        if (read(bArr, i, i2) == i2) {
            return;
        }
        throw new EOFException();
    }

    public void readOrThrow(byte[] bArr) throws IOException {
        readOrThrow(bArr, 0, bArr.length);
    }

    public void setByteOrder(ByteOrder byteOrder) {
        this.mByteBuffer.order(byteOrder);
    }

    public ByteOrder getByteOrder() {
        return this.mByteBuffer.order();
    }

    public short readShort() throws IOException {
        readOrThrow(this.mByteArray, 0, 2);
        this.mByteBuffer.rewind();
        return this.mByteBuffer.getShort();
    }

    public int readUnsignedShort() throws IOException {
        return readShort() & 65535;
    }

    public int readInt() throws IOException {
        readOrThrow(this.mByteArray, 0, 4);
        this.mByteBuffer.rewind();
        return this.mByteBuffer.getInt();
    }

    public long readUnsignedInt() throws IOException {
        return readInt() & 4294967295L;
    }

    public String readString(int i, Charset charset) throws IOException {
        byte[] bArr = new byte[i];
        readOrThrow(bArr);
        return new String(bArr, charset);
    }
}
