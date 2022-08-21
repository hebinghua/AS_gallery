package com.miui.gallery.util.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class FileLoggingFilter extends TurboFilter {
    public static final Marker mMarker = Markers.getFileMarker();

    @Override // ch.qos.logback.classic.turbo.TurboFilter
    public FilterReply decide(Marker marker, Logger logger, Level level, String str, Object[] objArr, Throwable th) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (marker != null && marker.contains(mMarker)) {
            return FilterReply.ACCEPT;
        }
        return FilterReply.NEUTRAL;
    }
}
