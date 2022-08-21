package com.miui.gallery.inject;

import dagger.internal.Preconditions;
import javax.inject.Provider;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes2.dex */
public final class AppModule_ProvideGlobalMainScopeFactory implements Provider {
    public static CoroutineScope provideGlobalMainScope() {
        return (CoroutineScope) Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideGlobalMainScope());
    }
}
