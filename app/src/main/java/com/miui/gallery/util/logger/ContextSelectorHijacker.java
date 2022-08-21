package com.miui.gallery.util.logger;

import androidx.annotation.Keep;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.DefaultContextSelector;

@Keep
/* loaded from: classes2.dex */
public class ContextSelectorHijacker extends DefaultContextSelector {
    public ContextSelectorHijacker(LoggerContext loggerContext) {
        super(loggerContext);
        initLogger(loggerContext);
    }

    private void initLogger(LoggerContext loggerContext) {
        GalleryLoggerFactory.instance().doInit(loggerContext);
    }
}
