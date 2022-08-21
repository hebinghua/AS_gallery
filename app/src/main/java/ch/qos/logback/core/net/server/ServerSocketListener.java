package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
import ch.qos.logback.core.util.CloseUtil;
import com.xiaomi.stat.b.h;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/* loaded from: classes.dex */
public abstract class ServerSocketListener<T extends Client> implements ServerListener<T> {
    private final ServerSocket serverSocket;

    /* renamed from: createClient */
    public abstract T mo168createClient(String str, Socket socket) throws IOException;

    public ServerSocketListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override // ch.qos.logback.core.net.server.ServerListener
    public T acceptClient() throws IOException {
        Socket accept = this.serverSocket.accept();
        return mo168createClient(socketAddressToString(accept.getRemoteSocketAddress()), accept);
    }

    @Override // ch.qos.logback.core.net.server.ServerListener, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        CloseUtil.closeQuietly(this.serverSocket);
    }

    public String toString() {
        return socketAddressToString(this.serverSocket.getLocalSocketAddress());
    }

    private String socketAddressToString(SocketAddress socketAddress) {
        String obj = socketAddress.toString();
        int indexOf = obj.indexOf(h.g);
        return indexOf >= 0 ? obj.substring(indexOf + 1) : obj;
    }
}
