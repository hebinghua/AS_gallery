package kotlinx.coroutines.internal;

import kotlin.jvm.functions.Function1;

/* compiled from: ExceptionsConstructor.kt */
/* loaded from: classes3.dex */
public final class ClassValueCtorCache extends CtorCache {
    public static final ClassValueCtorCache INSTANCE = new ClassValueCtorCache();
    public static final ClassValueCtorCache$cache$1 cache = new ClassValue<Function1<? super Throwable, ? extends Throwable>>() { // from class: kotlinx.coroutines.internal.ClassValueCtorCache$cache$1
    };

    @Override // kotlinx.coroutines.internal.CtorCache
    public Function1<Throwable, Throwable> get(Class<? extends Throwable> cls) {
        return (Function1) cache.get(cls);
    }
}
