package com.miui.gallery.util.logger;

import ch.qos.logback.classic.pattern.Abbreviator;
import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

/* loaded from: classes2.dex */
public class LogcatTagLayout extends LayoutBase<ILoggingEvent> {
    public final Abbreviator abbreviator = new ClassNameOnlyAbbreviator();
    public final String prefix;

    public LogcatTagLayout(String str) {
        this.prefix = str;
    }

    @Override // ch.qos.logback.core.Layout
    public String doLayout(ILoggingEvent iLoggingEvent) {
        if (!isStarted()) {
            return "";
        }
        return this.prefix + '_' + this.abbreviator.abbreviate(iLoggingEvent.getLoggerName());
    }
}
