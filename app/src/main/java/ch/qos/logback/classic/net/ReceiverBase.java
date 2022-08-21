package ch.qos.logback.classic.net;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

/* loaded from: classes.dex */
public abstract class ReceiverBase extends ContextAwareBase implements LifeCycle {
    private boolean started;

    public abstract Runnable getRunnableTask();

    public abstract void onStop();

    public abstract boolean shouldStart();

    @Override // ch.qos.logback.core.spi.LifeCycle
    public final void start() {
        if (isStarted()) {
            return;
        }
        if (getContext() == null) {
            throw new IllegalStateException("context not set");
        }
        if (!shouldStart()) {
            return;
        }
        getContext().getScheduledExecutorService().execute(getRunnableTask());
        this.started = true;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public final void stop() {
        if (!isStarted()) {
            return;
        }
        try {
            onStop();
        } catch (RuntimeException e) {
            addError("on stop: " + e, e);
        }
        this.started = false;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public final boolean isStarted() {
        return this.started;
    }
}
