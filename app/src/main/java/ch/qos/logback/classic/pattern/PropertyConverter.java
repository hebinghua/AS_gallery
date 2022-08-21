package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* loaded from: classes.dex */
public final class PropertyConverter extends ClassicConverter {
    public String key;

    @Override // ch.qos.logback.core.pattern.DynamicConverter, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        String firstOption = getFirstOption();
        if (firstOption != null) {
            this.key = firstOption;
            super.start();
        }
    }

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent iLoggingEvent) {
        if (this.key == null) {
            return "Property_HAS_NO_KEY";
        }
        String str = iLoggingEvent.getLoggerContextVO().getPropertyMap().get(this.key);
        return str != null ? str : System.getProperty(this.key);
    }
}
