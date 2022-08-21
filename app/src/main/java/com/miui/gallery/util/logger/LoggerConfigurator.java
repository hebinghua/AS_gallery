package com.miui.gallery.util.logger;

import ch.qos.logback.classic.Level;

/* compiled from: LoggerConfigurator.kt */
/* loaded from: classes2.dex */
public interface LoggerConfigurator {
    Level determineLogLevel();

    String getFileLogPath();

    boolean isPrintLog();
}
