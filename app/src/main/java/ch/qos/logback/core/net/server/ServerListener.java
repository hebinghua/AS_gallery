package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
import java.io.Closeable;
import java.io.IOException;

/* loaded from: classes.dex */
public interface ServerListener<T extends Client> extends Closeable {
    T acceptClient() throws IOException, InterruptedException;

    @Override // java.io.Closeable, java.lang.AutoCloseable
    void close();
}
