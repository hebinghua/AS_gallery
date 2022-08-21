package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAware;
import java.io.Serializable;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes.dex */
interface RemoteReceiverClient extends Client, ContextAware {
    boolean offer(Serializable serializable);

    void setQueue(BlockingQueue<Serializable> blockingQueue);
}
