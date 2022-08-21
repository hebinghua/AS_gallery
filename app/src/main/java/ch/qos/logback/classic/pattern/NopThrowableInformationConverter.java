package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* loaded from: classes.dex */
public class NopThrowableInformationConverter extends ThrowableHandlingConverter {
    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent iLoggingEvent) {
        return "";
    }
}
