package ch.qos.logback.classic;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AsyncAppenderBase;

/* loaded from: classes.dex */
public class AsyncAppender extends AsyncAppenderBase<ILoggingEvent> {
    public boolean includeCallerData = false;

    @Override // ch.qos.logback.core.AsyncAppenderBase
    public boolean isDiscardable(ILoggingEvent iLoggingEvent) {
        return iLoggingEvent.getLevel().toInt() <= 20000;
    }

    @Override // ch.qos.logback.core.AsyncAppenderBase
    public void preprocess(ILoggingEvent iLoggingEvent) {
        iLoggingEvent.prepareForDeferredProcessing();
        if (this.includeCallerData) {
            iLoggingEvent.getCallerData();
        }
    }

    public boolean isIncludeCallerData() {
        return this.includeCallerData;
    }

    public void setIncludeCallerData(boolean z) {
        this.includeCallerData = z;
    }
}
