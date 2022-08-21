package ch.qos.logback.core.recovery;

import ch.qos.logback.core.net.SyslogOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class ResilientSyslogOutputStream extends ResilientOutputStreamBase {
    public int port;
    public String syslogHost;

    public ResilientSyslogOutputStream(String str, int i) throws UnknownHostException, SocketException {
        this.syslogHost = str;
        this.port = i;
        this.os = new SyslogOutputStream(str, i);
        this.presumedClean = true;
    }

    @Override // ch.qos.logback.core.recovery.ResilientOutputStreamBase
    public String getDescription() {
        return "syslog [" + this.syslogHost + ":" + this.port + "]";
    }

    @Override // ch.qos.logback.core.recovery.ResilientOutputStreamBase
    public OutputStream openNewOutputStream() throws IOException {
        return new SyslogOutputStream(this.syslogHost, this.port);
    }

    public String toString() {
        return "c.q.l.c.recovery.ResilientSyslogOutputStream@" + System.identityHashCode(this);
    }
}
