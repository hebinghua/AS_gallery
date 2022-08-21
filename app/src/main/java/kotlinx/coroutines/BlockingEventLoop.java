package kotlinx.coroutines;

/* compiled from: EventLoop.kt */
/* loaded from: classes3.dex */
public final class BlockingEventLoop extends EventLoopImplBase {
    public final Thread thread;

    @Override // kotlinx.coroutines.EventLoopImplPlatform
    public Thread getThread() {
        return this.thread;
    }

    public BlockingEventLoop(Thread thread) {
        this.thread = thread;
    }
}
