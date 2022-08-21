package com.miui.gallery.inject;

import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import dagger.internal.Preconditions;
import javax.inject.Provider;

/* loaded from: classes2.dex */
public final class AppModule_ProvideCoroutineDispatchersFactory implements Provider {
    public static CoroutineDispatcherProvider provideCoroutineDispatchers() {
        return (CoroutineDispatcherProvider) Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideCoroutineDispatchers());
    }
}
