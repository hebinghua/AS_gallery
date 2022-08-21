package com.miui.gallery.util.concurrent;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineExceptionHandler;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.SupervisorKt;

/* compiled from: GalleryScopes.kt */
/* loaded from: classes2.dex */
public final class GlobalMainScope implements CoroutineScope {
    public static final GlobalMainScope INSTANCE = new GlobalMainScope();

    @Override // kotlinx.coroutines.CoroutineScope
    public CoroutineContext getCoroutineContext() {
        return SupervisorKt.SupervisorJob$default(null, 1, null).plus(Dispatchers.getMain()).plus(new GlobalMainScope$special$$inlined$CoroutineExceptionHandler$1(CoroutineExceptionHandler.Key));
    }
}
