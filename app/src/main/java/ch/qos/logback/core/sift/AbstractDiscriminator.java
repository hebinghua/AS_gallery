package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.ContextAwareBase;

/* loaded from: classes.dex */
public abstract class AbstractDiscriminator<E> extends ContextAwareBase implements Discriminator<E> {
    public boolean started;

    public void start() {
        this.started = true;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.started = false;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.started;
    }
}
