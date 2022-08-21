package com.miui.gallery3d.exif;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* loaded from: classes3.dex */
public class OrderedDataOutputStream extends FilterOutputStream {
    public final ByteBuffer mByteBuffer;

    public OrderedDataOutputStream(OutputStream outputStream) {
        super(outputStream);
        this.mByteBuffer = ByteBuffer.allocate(4);
    }

    public OrderedDataOutputStream setByteOrder(ByteOrder byteOrder) {
        this.mByteBuffer.order(byteOrder);
        return this;
    }

    public OrderedDataOutputStream writeShort(short s) throws IOException {
        this.mByteBuffer.rewind();
        this.mByteBuffer.putShort(s);
        ((FilterOutputStream) this).out.write(this.mByteBuffer.array(), 0, 2);
        return this;
    }

    public OrderedDataOutputStream writeInt(int i) throws IOException {
        this.mByteBuffer.rewind();
        this.mByteBuffer.putInt(i);
        ((FilterOutputStream) this).out.write(this.mByteBuffer.array());
        return this;
    }

    public OrderedDataOutputStream writeRational(Rational rational) throws IOException {
        writeInt((int) rational.getNumerator());
        writeInt((int) rational.getDenominator());
        return this;
    }
}
