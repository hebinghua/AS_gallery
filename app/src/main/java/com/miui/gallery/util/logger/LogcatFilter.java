package com.miui.gallery.util.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class LogcatFilter extends Filter<ILoggingEvent> {
    public static volatile Level sLevel = Level.OFF;

    public static void setLevel(Level level) {
        sLevel = level;
    }

    @Override // ch.qos.logback.core.filter.Filter
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        Marker marker = iLoggingEvent.getMarker();
        if (marker != null && marker.contains(Markers.getReplayMarker())) {
            return FilterReply.DENY;
        }
        if (marker != null && marker.contains(Markers.getFileOnlyMarker())) {
            return FilterReply.DENY;
        }
        if (iLoggingEvent.getLevel().isGreaterOrEqual(sLevel)) {
            return FilterReply.NEUTRAL;
        }
        return FilterReply.DENY;
    }
}
