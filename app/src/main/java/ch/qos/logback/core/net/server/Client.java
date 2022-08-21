package ch.qos.logback.core.net.server;

import java.io.Closeable;

/* loaded from: classes.dex */
public interface Client extends Runnable, Closeable {
    void close();
}
