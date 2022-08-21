package com.miui.gallery.inject;

import ch.qos.logback.classic.Level;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.logger.LoggerConfigurator;
import com.miui.os.Rom;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LoggerModule.kt */
/* loaded from: classes2.dex */
public final class LoggerModule {
    public static final LoggerModule INSTANCE = new LoggerModule();

    public final LoggerConfigurator provideConfigurator() {
        return new LoggerConfigurator() { // from class: com.miui.gallery.inject.LoggerModule$provideConfigurator$1
            @Override // com.miui.gallery.util.logger.LoggerConfigurator
            public Level determineLogLevel() {
                if (BaseGalleryPreferences.Debug.isPrintLog() || Rom.IS_DEBUGGABLE) {
                    Level ALL = Level.ALL;
                    Intrinsics.checkNotNullExpressionValue(ALL, "ALL");
                    return ALL;
                }
                Level level = Level.WARN;
                if (!Rom.IS_MIUI) {
                    level = Level.ERROR;
                } else if (!Rom.IS_STABLE) {
                    level = Level.DEBUG;
                }
                Intrinsics.checkNotNullExpressionValue(level, "level");
                return level;
            }

            @Override // com.miui.gallery.util.logger.LoggerConfigurator
            public String getFileLogPath() {
                String ABSOLUTE_DIRECTORY_DEBUG_LOG = StorageConstants.ABSOLUTE_DIRECTORY_DEBUG_LOG;
                Intrinsics.checkNotNullExpressionValue(ABSOLUTE_DIRECTORY_DEBUG_LOG, "ABSOLUTE_DIRECTORY_DEBUG_LOG");
                return ABSOLUTE_DIRECTORY_DEBUG_LOG;
            }

            @Override // com.miui.gallery.util.logger.LoggerConfigurator
            public boolean isPrintLog() {
                return BaseGalleryPreferences.Debug.isPrintLog();
            }
        };
    }
}
