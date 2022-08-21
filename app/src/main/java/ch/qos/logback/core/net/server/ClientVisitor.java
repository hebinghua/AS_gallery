package ch.qos.logback.core.net.server;

import ch.qos.logback.core.net.server.Client;

/* loaded from: classes.dex */
public interface ClientVisitor<T extends Client> {
    void visit(T t);
}
