package ch.qos.logback.classic.net.server;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.net.HardenedObjectInputStream;
import ch.qos.logback.core.util.CloseUtil;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/* loaded from: classes.dex */
class RemoteAppenderStreamClient implements RemoteAppenderClient {
    private final String id;
    private final InputStream inputStream;
    private LoggerContext lc;
    private Logger logger;
    private final Socket socket;

    public RemoteAppenderStreamClient(String str, Socket socket) {
        this.id = str;
        this.socket = socket;
        this.inputStream = null;
    }

    public RemoteAppenderStreamClient(String str, InputStream inputStream) {
        this.id = str;
        this.socket = null;
        this.inputStream = inputStream;
    }

    @Override // ch.qos.logback.classic.net.server.RemoteAppenderClient
    public void setLoggerContext(LoggerContext loggerContext) {
        this.lc = loggerContext;
        this.logger = loggerContext.mo166getLogger(getClass().getPackage().getName());
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
        Logger logger;
        StringBuilder sb;
        Logger logger2 = this.logger;
        logger2.info(this + ": connected");
        HardenedObjectInputStream hardenedObjectInputStream = null;
        try {
            try {
                try {
                    hardenedObjectInputStream = createObjectInputStream();
                    while (true) {
                        ILoggingEvent iLoggingEvent = (ILoggingEvent) hardenedObjectInputStream.readObject();
                        Logger mo166getLogger = this.lc.mo166getLogger(iLoggingEvent.getLoggerName());
                        if (mo166getLogger.isEnabledFor(iLoggingEvent.getLevel())) {
                            mo166getLogger.callAppenders(iLoggingEvent);
                        }
                    }
                } catch (EOFException unused) {
                    if (hardenedObjectInputStream != null) {
                        CloseUtil.closeQuietly(hardenedObjectInputStream);
                    }
                    close();
                    logger = this.logger;
                    sb = new StringBuilder();
                    sb.append(this);
                    sb.append(": connection closed");
                    logger.info(sb.toString());
                } catch (ClassNotFoundException unused2) {
                    Logger logger3 = this.logger;
                    logger3.error(this + ": unknown event class");
                    if (hardenedObjectInputStream != null) {
                        CloseUtil.closeQuietly(hardenedObjectInputStream);
                    }
                    close();
                    logger = this.logger;
                    sb = new StringBuilder();
                    sb.append(this);
                    sb.append(": connection closed");
                    logger.info(sb.toString());
                }
            } catch (IOException e) {
                Logger logger4 = this.logger;
                logger4.info(this + ": " + e);
                if (hardenedObjectInputStream != null) {
                    CloseUtil.closeQuietly(hardenedObjectInputStream);
                }
                close();
                logger = this.logger;
                sb = new StringBuilder();
                sb.append(this);
                sb.append(": connection closed");
                logger.info(sb.toString());
            } catch (RuntimeException e2) {
                Logger logger5 = this.logger;
                logger5.error(this + ": " + e2);
                if (hardenedObjectInputStream != null) {
                    CloseUtil.closeQuietly(hardenedObjectInputStream);
                }
                close();
                logger = this.logger;
                sb = new StringBuilder();
                sb.append(this);
                sb.append(": connection closed");
                logger.info(sb.toString());
            }
        } catch (Throwable th) {
            if (hardenedObjectInputStream != null) {
                CloseUtil.closeQuietly(hardenedObjectInputStream);
            }
            close();
            Logger logger6 = this.logger;
            logger6.info(this + ": connection closed");
            throw th;
        }
    }

    private HardenedObjectInputStream createObjectInputStream() throws IOException {
        if (this.inputStream != null) {
            return new HardenedLoggingEventInputStream(this.inputStream);
        }
        return new HardenedLoggingEventInputStream(this.socket.getInputStream());
    }

    public String toString() {
        return "client " + this.id;
    }
}
