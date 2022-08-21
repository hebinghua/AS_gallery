package ch.qos.logback.core.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* loaded from: classes.dex */
class RemoteReceiverServerListener extends ServerSocketListener<RemoteReceiverClient> {
    public RemoteReceiverServerListener(ServerSocket serverSocket) {
        super(serverSocket);
    }

    @Override // ch.qos.logback.core.net.server.ServerSocketListener
    /* renamed from: createClient  reason: collision with other method in class */
    public RemoteReceiverClient mo168createClient(String str, Socket socket) throws IOException {
        return new RemoteReceiverStreamClient(str, socket);
    }
}
