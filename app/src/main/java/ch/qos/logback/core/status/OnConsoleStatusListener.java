package ch.qos.logback.core.status;

import java.io.PrintStream;

/* loaded from: classes.dex */
public class OnConsoleStatusListener extends OnPrintStreamStatusListenerBase {
    @Override // ch.qos.logback.core.status.OnPrintStreamStatusListenerBase
    public PrintStream getPrintStream() {
        return System.out;
    }
}
