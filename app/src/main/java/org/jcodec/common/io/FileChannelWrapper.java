package org.jcodec.common.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/* loaded from: classes3.dex */
public class FileChannelWrapper implements SeekableByteChannel {

    /* renamed from: ch  reason: collision with root package name */
    public FileChannel f1002ch;

    public FileChannelWrapper(FileChannel fileChannel) throws FileNotFoundException {
        this.f1002ch = fileChannel;
    }

    @Override // java.nio.channels.ReadableByteChannel
    public int read(ByteBuffer byteBuffer) throws IOException {
        return this.f1002ch.read(byteBuffer);
    }

    @Override // java.nio.channels.Channel, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.f1002ch.close();
    }

    @Override // java.nio.channels.Channel
    public boolean isOpen() {
        return this.f1002ch.isOpen();
    }

    @Override // java.nio.channels.WritableByteChannel
    public int write(ByteBuffer byteBuffer) throws IOException {
        return this.f1002ch.write(byteBuffer);
    }

    @Override // org.jcodec.common.io.SeekableByteChannel
    public SeekableByteChannel setPosition(long j) throws IOException {
        this.f1002ch.position(j);
        return this;
    }

    @Override // org.jcodec.common.io.SeekableByteChannel
    public long size() throws IOException {
        return this.f1002ch.size();
    }
}
