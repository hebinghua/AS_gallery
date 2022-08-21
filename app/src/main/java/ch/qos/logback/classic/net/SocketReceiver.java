package ch.qos.logback.classic.net;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.server.HardenedLoggingEventInputStream;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.DefaultSocketConnector;
import ch.qos.logback.core.net.SocketConnector;
import ch.qos.logback.core.util.CloseUtil;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import javax.net.SocketFactory;

/* loaded from: classes.dex */
public class SocketReceiver extends ReceiverBase implements Runnable, SocketConnector.ExceptionHandler {
    private static final int DEFAULT_ACCEPT_CONNECTION_DELAY = 5000;
    private int acceptConnectionTimeout = 5000;
    private InetAddress address;
    private Future<Socket> connectorTask;
    private int port;
    private String receiverId;
    private int reconnectionDelay;
    private String remoteHost;
    private volatile Socket socket;

    @Override // ch.qos.logback.classic.net.ReceiverBase
    public Runnable getRunnableTask() {
        return this;
    }

    @Override // ch.qos.logback.classic.net.ReceiverBase
    public boolean shouldStart() {
        int i;
        if (this.port == 0) {
            addError("No port was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_port");
            i = 1;
        } else {
            i = 0;
        }
        if (this.remoteHost == null) {
            i++;
            addError("No host name or address was configured for receiver. For more information, please visit http://logback.qos.ch/codes.html#receiver_no_host");
        }
        if (this.reconnectionDelay == 0) {
            this.reconnectionDelay = 30000;
        }
        if (i == 0) {
            try {
                this.address = InetAddress.getByName(this.remoteHost);
            } catch (UnknownHostException unused) {
                addError("unknown host: " + this.remoteHost);
                i++;
            }
        }
        if (i == 0) {
            this.receiverId = "receiver " + this.remoteHost + ":" + this.port + ": ";
        }
        return i == 0;
    }

    @Override // ch.qos.logback.classic.net.ReceiverBase
    public void onStop() {
        if (this.socket != null) {
            CloseUtil.closeQuietly(this.socket);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            LoggerContext loggerContext = (LoggerContext) getContext();
            while (!Thread.currentThread().isInterrupted()) {
                Future<Socket> activateConnector = activateConnector(createConnector(this.address, this.port, 0, this.reconnectionDelay));
                this.connectorTask = activateConnector;
                if (activateConnector == null) {
                    break;
                }
                this.socket = waitForConnectorToReturnASocket();
                if (this.socket == null) {
                    break;
                }
                dispatchEvents(loggerContext);
            }
        } catch (InterruptedException unused) {
        }
        addInfo("shutting down");
    }

    private SocketConnector createConnector(InetAddress inetAddress, int i, int i2, int i3) {
        SocketConnector newConnector = newConnector(inetAddress, i, i2, i3);
        newConnector.setExceptionHandler(this);
        newConnector.setSocketFactory(getSocketFactory());
        return newConnector;
    }

    private Future<Socket> activateConnector(SocketConnector socketConnector) {
        try {
            return getContext().getScheduledExecutorService().submit(socketConnector);
        } catch (RejectedExecutionException unused) {
            return null;
        }
    }

    private Socket waitForConnectorToReturnASocket() throws InterruptedException {
        try {
            Socket socket = this.connectorTask.get();
            this.connectorTask = null;
            return socket;
        } catch (ExecutionException unused) {
            return null;
        }
    }

    private void dispatchEvents(LoggerContext loggerContext) {
        HardenedLoggingEventInputStream hardenedLoggingEventInputStream;
        StringBuilder sb;
        try {
            try {
                this.socket.setSoTimeout(this.acceptConnectionTimeout);
                hardenedLoggingEventInputStream = new HardenedLoggingEventInputStream(this.socket.getInputStream());
                try {
                    this.socket.setSoTimeout(0);
                    addInfo(this.receiverId + "connection established");
                    while (true) {
                        ILoggingEvent iLoggingEvent = (ILoggingEvent) hardenedLoggingEventInputStream.readObject();
                        Logger mo166getLogger = loggerContext.mo166getLogger(iLoggingEvent.getLoggerName());
                        if (mo166getLogger.isEnabledFor(iLoggingEvent.getLevel())) {
                            mo166getLogger.callAppenders(iLoggingEvent);
                        }
                    }
                } catch (EOFException unused) {
                    addInfo(this.receiverId + "end-of-stream detected");
                    CloseUtil.closeQuietly(hardenedLoggingEventInputStream);
                    CloseUtil.closeQuietly(this.socket);
                    this.socket = null;
                    sb = new StringBuilder();
                    sb.append(this.receiverId);
                    sb.append("connection closed");
                    addInfo(sb.toString());
                } catch (IOException e) {
                    e = e;
                    addInfo(this.receiverId + "connection failed: " + e);
                    CloseUtil.closeQuietly(hardenedLoggingEventInputStream);
                    CloseUtil.closeQuietly(this.socket);
                    this.socket = null;
                    sb = new StringBuilder();
                    sb.append(this.receiverId);
                    sb.append("connection closed");
                    addInfo(sb.toString());
                } catch (ClassNotFoundException e2) {
                    e = e2;
                    addInfo(this.receiverId + "unknown event class: " + e);
                    CloseUtil.closeQuietly(hardenedLoggingEventInputStream);
                    CloseUtil.closeQuietly(this.socket);
                    this.socket = null;
                    sb = new StringBuilder();
                    sb.append(this.receiverId);
                    sb.append("connection closed");
                    addInfo(sb.toString());
                }
            } catch (Throwable th) {
                th = th;
                CloseUtil.closeQuietly((Closeable) null);
                CloseUtil.closeQuietly(this.socket);
                this.socket = null;
                addInfo(this.receiverId + "connection closed");
                throw th;
            }
        } catch (EOFException unused2) {
            hardenedLoggingEventInputStream = null;
        } catch (IOException e3) {
            e = e3;
            hardenedLoggingEventInputStream = null;
        } catch (ClassNotFoundException e4) {
            e = e4;
            hardenedLoggingEventInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            CloseUtil.closeQuietly((Closeable) null);
            CloseUtil.closeQuietly(this.socket);
            this.socket = null;
            addInfo(this.receiverId + "connection closed");
            throw th;
        }
    }

    @Override // ch.qos.logback.core.net.SocketConnector.ExceptionHandler
    public void connectionFailed(SocketConnector socketConnector, Exception exc) {
        if (exc instanceof InterruptedException) {
            addWarn("connector interrupted", exc);
        } else if (exc instanceof ConnectException) {
            addWarn(this.receiverId + "connection refused", exc);
        } else {
            addWarn(this.receiverId + "unspecified error", exc);
        }
    }

    public SocketConnector newConnector(InetAddress inetAddress, int i, int i2, int i3) {
        return new DefaultSocketConnector(inetAddress, i, i2, i3);
    }

    public SocketFactory getSocketFactory() {
        return SocketFactory.getDefault();
    }

    public void setRemoteHost(String str) {
        this.remoteHost = str;
    }

    public void setPort(int i) {
        this.port = i;
    }

    public void setReconnectionDelay(int i) {
        this.reconnectionDelay = i;
    }

    public void setAcceptConnectionTimeout(int i) {
        this.acceptConnectionTimeout = i;
    }
}
