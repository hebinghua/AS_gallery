package ch.qos.logback.classic.pattern;

import ch.qos.logback.classic.spi.ILoggingEvent;

/* loaded from: classes.dex */
public abstract class NamedConverter extends ClassicConverter {
    public Abbreviator abbreviator = null;

    public abstract String getFullyQualifiedName(ILoggingEvent iLoggingEvent);

    @Override // ch.qos.logback.core.pattern.DynamicConverter, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        String firstOption = getFirstOption();
        if (firstOption != null) {
            try {
                int parseInt = Integer.parseInt(firstOption);
                if (parseInt == 0) {
                    this.abbreviator = new ClassNameOnlyAbbreviator();
                } else if (parseInt > 0) {
                    this.abbreviator = new TargetLengthBasedClassNameAbbreviator(parseInt);
                }
            } catch (NumberFormatException unused) {
            }
        }
    }

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(ILoggingEvent iLoggingEvent) {
        String fullyQualifiedName = getFullyQualifiedName(iLoggingEvent);
        Abbreviator abbreviator = this.abbreviator;
        return abbreviator == null ? fullyQualifiedName : abbreviator.abbreviate(fullyQualifiedName);
    }
}
