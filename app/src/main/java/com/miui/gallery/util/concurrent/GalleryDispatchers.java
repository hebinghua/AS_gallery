package com.miui.gallery.util.concurrent;

import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: GalleryDispatchers.kt */
/* loaded from: classes2.dex */
public final class GalleryDispatchers {
    public static final GalleryDispatchers INSTANCE = new GalleryDispatchers();
    public static final Lazy ASYNC_TASK$delegate = LazyKt__LazyJVMKt.lazy(GalleryDispatchers$ASYNC_TASK$2.INSTANCE);
    public static final Lazy MISC$delegate = LazyKt__LazyJVMKt.lazy(GalleryDispatchers$MISC$2.INSTANCE);
    public static final Lazy ASYNC_DIFFER$delegate = LazyKt__LazyJVMKt.lazy(GalleryDispatchers$ASYNC_DIFFER$2.INSTANCE);

    public final CoroutineDispatcher getMISC() {
        return (CoroutineDispatcher) MISC$delegate.mo119getValue();
    }

    public final CoroutineDispatcher getASYNC_DIFFER() {
        return (CoroutineDispatcher) ASYNC_DIFFER$delegate.mo119getValue();
    }
}
