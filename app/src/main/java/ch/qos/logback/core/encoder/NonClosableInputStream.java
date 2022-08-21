package ch.qos.logback.core.encoder;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes.dex */
public class NonClosableInputStream extends FilterInputStream {
    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
    }

    public NonClosableInputStream(InputStream inputStream) {
        super(inputStream);
    }

    public void realClose() throws IOException {
        super.close();
    }
}
