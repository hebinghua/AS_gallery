package ch.qos.logback.core.status;

import java.io.PrintStream;

/* loaded from: classes.dex */
public class OnErrorConsoleStatusListener extends OnPrintStreamStatusListenerBase {
    @Override // ch.qos.logback.core.status.OnPrintStreamStatusListenerBase
    public PrintStream getPrintStream() {
        return System.err;
    }
}
