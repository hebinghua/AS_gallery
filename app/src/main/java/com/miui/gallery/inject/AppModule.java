package com.miui.gallery.inject;

import com.miui.gallery.util.concurrent.CoroutineDispatcherProvider;
import com.miui.gallery.util.concurrent.GlobalMainScope;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

/* compiled from: AppModule.kt */
/* loaded from: classes2.dex */
public final class AppModule {
    public static final AppModule INSTANCE = new AppModule();

    public final CoroutineDispatcherProvider provideCoroutineDispatchers() {
        return new CoroutineDispatcherProvider(Dispatchers.getIO(), Dispatchers.getDefault(), Dispatchers.getMain());
    }

    public final CoroutineScope provideGlobalMainScope() {
        return GlobalMainScope.INSTANCE;
    }
}
