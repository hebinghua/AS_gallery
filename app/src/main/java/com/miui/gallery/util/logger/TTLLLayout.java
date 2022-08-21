package com.miui.gallery.util.logger;

import androidx.core.util.PatternsCompat;
import ch.qos.logback.classic.pattern.Abbreviator;
import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.util.CachingDateFormatter;

/* loaded from: classes2.dex */
public class TTLLLayout extends LayoutBase<ILoggingEvent> {
    public final CachingDateFormatter cachingDateFormatter = new CachingDateFormatter("yyyy-MM-dd HH:mm:ss.SSS");
    public final ThrowableProxyConverter tpc = new ThrowableProxyConverter();
    public final Abbreviator abbreviator = new ClassNameOnlyAbbreviator();

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.tpc.start();
        super.start();
    }

    @Override // ch.qos.logback.core.Layout
    public String doLayout(ILoggingEvent iLoggingEvent) {
        if (!isStarted()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.cachingDateFormatter.format(iLoggingEvent.getTimeStamp()));
        sb.append(" ");
        sb.append(iLoggingEvent.getLevel().toString());
        sb.append(" [");
        sb.append(iLoggingEvent.getThreadName());
        sb.append("] ");
        sb.append(this.abbreviator.abbreviate(iLoggingEvent.getLoggerName()));
        sb.append(" - ");
        sb.append(PatternsCompat.IP_ADDRESS.matcher(iLoggingEvent.getFormattedMessage()).replaceAll("*.*.*.*"));
        sb.append(CoreConstants.LINE_SEPARATOR);
        if (iLoggingEvent.getThrowableProxy() != null) {
            sb.append(this.tpc.convert(iLoggingEvent));
        }
        return sb.toString();
    }
}
