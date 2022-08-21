package kotlinx.coroutines;

import kotlinx.coroutines.internal.MainDispatcherLoader;
import kotlinx.coroutines.scheduling.DefaultIoScheduler;
import kotlinx.coroutines.scheduling.DefaultScheduler;

/* compiled from: Dispatchers.kt */
/* loaded from: classes3.dex */
public final class Dispatchers {
    public static final Dispatchers INSTANCE = new Dispatchers();
    public static final CoroutineDispatcher Default = DefaultScheduler.INSTANCE;
    public static final CoroutineDispatcher Unconfined = Unconfined.INSTANCE;
    public static final CoroutineDispatcher IO = DefaultIoScheduler.INSTANCE;

    public static final CoroutineDispatcher getDefault() {
        return Default;
    }

    public static final MainCoroutineDispatcher getMain() {
        return MainDispatcherLoader.dispatcher;
    }

    public static final CoroutineDispatcher getIO() {
        return IO;
    }
}
