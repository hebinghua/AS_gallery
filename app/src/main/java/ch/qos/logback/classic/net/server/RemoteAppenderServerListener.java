package ch.qos.logback.classic.net.server;

import ch.qos.logback.core.net.server.ServerSocketListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/* loaded from: classes.dex */
class RemoteAppenderServerListener extends ServerSocketListener<RemoteAppenderClient> {
    public RemoteAppenderServerListener(ServerSocket serverSocket) {
        super(serverSocket);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // ch.qos.logback.core.net.server.ServerSocketListener
    /* renamed from: createClient */
    public RemoteAppenderClient mo168createClient(String str, Socket socket) throws IOException {
        return new RemoteAppenderStreamClient(str, socket);
    }
}
