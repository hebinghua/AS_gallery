package ch.qos.logback.core.net;

import java.net.Socket;
import java.util.concurrent.Callable;
import javax.net.SocketFactory;

/* loaded from: classes.dex */
public interface SocketConnector extends Callable<Socket> {

    /* loaded from: classes.dex */
    public interface ExceptionHandler {
        void connectionFailed(SocketConnector socketConnector, Exception exc);
    }

    @Override // java.util.concurrent.Callable
    Socket call() throws InterruptedException;

    void setExceptionHandler(ExceptionHandler exceptionHandler);

    void setSocketFactory(SocketFactory socketFactory);
}
