package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;

/* loaded from: classes.dex */
public class ExtendedThrowableProxyConverter extends ThrowableProxyConverter {
    public void prepareLoggingEvent(ILoggingEvent iLoggingEvent) {
    }

    @Override // ch.qos.logback.classic.pattern.ThrowableProxyConverter
    public void extraData(StringBuilder sb, StackTraceElementProxy stackTraceElementProxy) {
        ThrowableProxyUtil.subjoinPackagingData(sb, stackTraceElementProxy);
    }
}
