package ch.qos.logback.core.net.server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RemoteReceiverServerRunner extends ConcurrentServerRunner<RemoteReceiverClient> {
    private final int clientQueueSize;

    public RemoteReceiverServerRunner(ServerListener<RemoteReceiverClient> serverListener, Executor executor, int i) {
        super(serverListener, executor);
        this.clientQueueSize = i;
    }

    @Override // ch.qos.logback.core.net.server.ConcurrentServerRunner
    public boolean configureClient(RemoteReceiverClient remoteReceiverClient) {
        remoteReceiverClient.setContext(getContext());
        remoteReceiverClient.setQueue(new ArrayBlockingQueue(this.clientQueueSize));
        return true;
    }
}
