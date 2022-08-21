package androidx.lifecycle;

import android.annotation.SuppressLint;
import java.util.ArrayDeque;
import java.util.Queue;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.MainCoroutineDispatcher;

/* compiled from: DispatchQueue.kt */
/* loaded from: classes.dex */
public final class DispatchQueue {
    public boolean finished;
    public boolean isDraining;
    public boolean paused = true;
    public final Queue<Runnable> queue = new ArrayDeque();

    public static final /* synthetic */ void access$enqueue(DispatchQueue dispatchQueue, Runnable runnable) {
        dispatchQueue.enqueue(runnable);
    }

    public final void pause() {
        this.paused = true;
    }

    public final void resume() {
        if (!this.paused) {
            return;
        }
        if (!(!this.finished)) {
            throw new IllegalStateException("Cannot resume a finished dispatcher".toString());
        }
        this.paused = false;
        drainQueue();
    }

    public final void finish() {
        this.finished = true;
        drainQueue();
    }

    public final void drainQueue() {
        if (this.isDraining) {
            return;
        }
        try {
            this.isDraining = true;
            while ((!this.queue.isEmpty()) && canRun()) {
                Runnable poll = this.queue.poll();
                if (poll != null) {
                    poll.run();
                }
            }
        } finally {
            this.isDraining = false;
        }
    }

    public final boolean canRun() {
        return this.finished || !this.paused;
    }

    @SuppressLint({"WrongThread"})
    public final void dispatchAndEnqueue(CoroutineContext context, final Runnable runnable) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        MainCoroutineDispatcher mo2585getImmediate = Dispatchers.getMain().mo2585getImmediate();
        if (mo2585getImmediate.isDispatchNeeded(context) || canRun()) {
            mo2585getImmediate.mo2586dispatch(context, new Runnable() { // from class: androidx.lifecycle.DispatchQueue$dispatchAndEnqueue$1$1
                @Override // java.lang.Runnable
                public final void run() {
                    DispatchQueue.access$enqueue(DispatchQueue.this, runnable);
                }
            });
        } else {
            enqueue(runnable);
        }
    }

    public final void enqueue(Runnable runnable) {
        if (!this.queue.offer(runnable)) {
            throw new IllegalStateException("cannot enqueue any more runnables".toString());
        }
        drainQueue();
    }
}
