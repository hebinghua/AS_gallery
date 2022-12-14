package kotlinx.coroutines;

/* compiled from: EventLoop.common.kt */
/* loaded from: classes3.dex */
public final class ThreadLocalEventLoop {
    public static final ThreadLocalEventLoop INSTANCE = new ThreadLocalEventLoop();
    public static final ThreadLocal<EventLoop> ref = new ThreadLocal<>();

    public final EventLoop getEventLoop$kotlinx_coroutines_core() {
        ThreadLocal<EventLoop> threadLocal = ref;
        EventLoop eventLoop = threadLocal.get();
        if (eventLoop == null) {
            EventLoop createEventLoop = EventLoopKt.createEventLoop();
            threadLocal.set(createEventLoop);
            return createEventLoop;
        }
        return eventLoop;
    }

    public final void resetEventLoop$kotlinx_coroutines_core() {
        ref.set(null);
    }

    public final void setEventLoop$kotlinx_coroutines_core(EventLoop eventLoop) {
        ref.set(eventLoop);
    }
}
