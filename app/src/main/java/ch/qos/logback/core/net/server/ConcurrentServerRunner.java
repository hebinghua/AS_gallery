package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;
import ch.qos.logback.core.spi.ContextAwareBase;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public abstract class ConcurrentServerRunner<T extends Client> extends ContextAwareBase implements Runnable, ServerRunner<T> {
    private final Executor executor;
    private final ServerListener<T> listener;
    private boolean running;
    private final Lock clientsLock = new ReentrantLock();
    private final Collection<T> clients = new ArrayList();

    public abstract boolean configureClient(T t);

    public ConcurrentServerRunner(ServerListener<T> serverListener, Executor executor) {
        this.listener = serverListener;
        this.executor = executor;
    }

    @Override // ch.qos.logback.core.net.server.ServerRunner
    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean z) {
        this.running = z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // ch.qos.logback.core.net.server.ServerRunner
    public void stop() throws IOException {
        this.listener.close();
        accept(new ClientVisitor<T>() { // from class: ch.qos.logback.core.net.server.ConcurrentServerRunner.1
            @Override // ch.qos.logback.core.net.server.ClientVisitor
            public void visit(T t) {
                t.close();
            }
        });
    }

    @Override // ch.qos.logback.core.net.server.ServerRunner
    public void accept(ClientVisitor<T> clientVisitor) {
        for (T t : copyClients()) {
            try {
                clientVisitor.visit(t);
            } catch (RuntimeException e) {
                addError(t + ": " + e);
            }
        }
    }

    private Collection<T> copyClients() {
        this.clientsLock.lock();
        try {
            return new ArrayList(this.clients);
        } finally {
            this.clientsLock.unlock();
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        setRunning(true);
        try {
            addInfo("listening on " + this.listener);
            while (!Thread.currentThread().isInterrupted()) {
                T acceptClient = this.listener.acceptClient();
                if (!configureClient(acceptClient)) {
                    addError(acceptClient + ": connection dropped");
                    acceptClient.close();
                } else {
                    try {
                        this.executor.execute(new ClientWrapper(acceptClient));
                    } catch (RejectedExecutionException unused) {
                        addError(acceptClient + ": connection dropped");
                        acceptClient.close();
                    }
                }
            }
        } catch (InterruptedException unused2) {
        } catch (Exception e) {
            addError("listener: " + e);
        }
        setRunning(false);
        addInfo("shutting down");
        this.listener.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addClient(T t) {
        this.clientsLock.lock();
        try {
            this.clients.add(t);
        } finally {
            this.clientsLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeClient(T t) {
        this.clientsLock.lock();
        try {
            this.clients.remove(t);
        } finally {
            this.clientsLock.unlock();
        }
    }

    /* loaded from: classes.dex */
    public class ClientWrapper implements Client {
        private final T delegate;

        public ClientWrapper(T t) {
            this.delegate = t;
        }

        @Override // java.lang.Runnable
        public void run() {
            ConcurrentServerRunner.this.addClient(this.delegate);
            try {
                this.delegate.run();
            } finally {
                ConcurrentServerRunner.this.removeClient(this.delegate);
            }
        }

        @Override // ch.qos.logback.core.net.server.Client, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.delegate.close();
        }
    }
}
