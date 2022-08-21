package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* loaded from: classes.dex */
public class RelativeTimeConverter extends ClassicConverter {
    public long lastTimestamp = -1;
    public String timesmapCache = null;

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent iLoggingEvent) {
        String str;
        long timeStamp = iLoggingEvent.getTimeStamp();
        synchronized (this) {
            if (timeStamp != this.lastTimestamp) {
                this.lastTimestamp = timeStamp;
                this.timesmapCache = Long.toString(timeStamp - iLoggingEvent.getLoggerContextVO().getBirthTime());
            }
            str = this.timesmapCache;
        }
        return str;
    }
}
