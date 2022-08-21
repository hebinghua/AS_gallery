package com.miui.gallery.inject;

import com.miui.gallery.util.logger.LoggerConfigurator;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* loaded from: classes2.dex */
public final class LoggerModule_ProvideConfiguratorFactory implements Provider {
    public static LoggerConfigurator provideConfigurator() {
        return (LoggerConfigurator) Preconditions.checkNotNullFromProvides(LoggerModule.INSTANCE.provideConfigurator());
    }
}
