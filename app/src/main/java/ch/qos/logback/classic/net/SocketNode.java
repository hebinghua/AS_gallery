package ch.qos.logback.classic.net;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.server.HardenedLoggingEventInputStream;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/* loaded from: classes.dex */
public class SocketNode implements Runnable {
    public boolean closed = false;
    public LoggerContext context;
    public HardenedLoggingEventInputStream hardenedLoggingEventInputStream;
    public Logger logger;
    public SocketAddress remoteSocketAddress;
    public Socket socket;
    public SimpleSocketServer socketServer;

    public SocketNode(SimpleSocketServer simpleSocketServer, Socket socket, LoggerContext loggerContext) {
        this.socketServer = simpleSocketServer;
        this.socket = socket;
        this.remoteSocketAddress = socket.getRemoteSocketAddress();
        this.context = loggerContext;
        this.logger = loggerContext.getLogger(SocketNode.class);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.hardenedLoggingEventInputStream = new HardenedLoggingEventInputStream(new BufferedInputStream(this.socket.getInputStream()));
        } catch (Exception e) {
            Logger logger = this.logger;
            logger.error("Could not open ObjectInputStream to " + this.socket, (Throwable) e);
            this.closed = true;
        }
        while (!this.closed) {
            try {
                ILoggingEvent iLoggingEvent = (ILoggingEvent) this.hardenedLoggingEventInputStream.readObject();
                Logger mo166getLogger = this.context.mo166getLogger(iLoggingEvent.getLoggerName());
                if (mo166getLogger.isEnabledFor(iLoggingEvent.getLevel())) {
                    mo166getLogger.callAppenders(iLoggingEvent);
                }
            } catch (EOFException unused) {
                this.logger.info("Caught java.io.EOFException closing connection.");
            } catch (SocketException unused2) {
                this.logger.info("Caught java.net.SocketException closing connection.");
            } catch (IOException e2) {
                Logger logger2 = this.logger;
                logger2.info("Caught java.io.IOException: " + e2);
                this.logger.info("Closing connection.");
            } catch (Exception e3) {
                this.logger.error("Unexpected exception. Closing connection.", (Throwable) e3);
            }
        }
        this.socketServer.socketNodeClosing(this);
        close();
    }

    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        HardenedLoggingEventInputStream hardenedLoggingEventInputStream = this.hardenedLoggingEventInputStream;
        if (hardenedLoggingEventInputStream == null) {
            return;
        }
        try {
            try {
                hardenedLoggingEventInputStream.close();
            } catch (IOException e) {
                this.logger.warn("Could not close connection.", (Throwable) e);
            }
        } finally {
            this.hardenedLoggingEventInputStream = null;
        }
    }

    public String toString() {
        return getClass().getName() + this.remoteSocketAddress.toString();
    }
}
