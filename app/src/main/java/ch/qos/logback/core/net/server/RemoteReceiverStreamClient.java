package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.CloseUtil;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes.dex */
class RemoteReceiverStreamClient extends ContextAwareBase implements RemoteReceiverClient {
    private final String clientId;
    private final OutputStream outputStream;
    private BlockingQueue<Serializable> queue;
    private final Socket socket;

    public RemoteReceiverStreamClient(String str, Socket socket) {
        this.clientId = "client " + str + ": ";
        this.socket = socket;
        this.outputStream = null;
    }

    public RemoteReceiverStreamClient(String str, OutputStream outputStream) {
        this.clientId = "client " + str + ": ";
        this.socket = null;
        this.outputStream = outputStream;
    }

    @Override // ch.qos.logback.core.net.server.RemoteReceiverClient
    public void setQueue(BlockingQueue<Serializable> blockingQueue) {
        this.queue = blockingQueue;
    }

    @Override // ch.qos.logback.core.net.server.RemoteReceiverClient
    public boolean offer(Serializable serializable) {
        BlockingQueue<Serializable> blockingQueue = this.queue;
        if (blockingQueue == null) {
            throw new IllegalStateException("client has no event queue");
        }
        return blockingQueue.offer(serializable);
    }

    @Override // ch.qos.logback.core.net.server.Client, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        Socket socket = this.socket;
        if (socket == null) {
            return;
        }
        CloseUtil.closeQuietly(socket);
    }

    @Override // java.lang.Runnable
    public void run() {
        StringBuilder sb;
        addInfo(this.clientId + "connected");
        ObjectOutputStream objectOutputStream = null;
        try {
            try {
                try {
                    try {
                        objectOutputStream = createObjectOutputStream();
                        loop0: while (true) {
                            int i = 0;
                            while (!Thread.currentThread().isInterrupted()) {
                                try {
                                    objectOutputStream.writeObject(this.queue.take());
                                    objectOutputStream.flush();
                                    i++;
                                } catch (InterruptedException unused) {
                                }
                                if (i >= 70) {
                                    try {
                                        objectOutputStream.reset();
                                        break;
                                    } catch (InterruptedException unused2) {
                                        i = 0;
                                        Thread.currentThread().interrupt();
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (objectOutputStream != null) {
                            CloseUtil.closeQuietly(objectOutputStream);
                        }
                        close();
                        sb = new StringBuilder();
                    } catch (SocketException e) {
                        addInfo(this.clientId + e);
                        if (objectOutputStream != null) {
                            CloseUtil.closeQuietly(objectOutputStream);
                        }
                        close();
                        sb = new StringBuilder();
                    }
                } catch (RuntimeException e2) {
                    addError(this.clientId + e2);
                    if (objectOutputStream != null) {
                        CloseUtil.closeQuietly(objectOutputStream);
                    }
                    close();
                    sb = new StringBuilder();
                }
            } catch (IOException e3) {
                addError(this.clientId + e3);
                if (objectOutputStream != null) {
                    CloseUtil.closeQuietly(objectOutputStream);
                }
                close();
                sb = new StringBuilder();
            }
            sb.append(this.clientId);
            sb.append("connection closed");
            addInfo(sb.toString());
        } catch (Throwable th) {
            if (objectOutputStream != null) {
                CloseUtil.closeQuietly(objectOutputStream);
            }
            close();
            addInfo(this.clientId + "connection closed");
            throw th;
        }
    }

    private ObjectOutputStream createObjectOutputStream() throws IOException {
        if (this.socket == null) {
            return new ObjectOutputStream(this.outputStream);
        }
        return new ObjectOutputStream(this.socket.getOutputStream());
    }
}
