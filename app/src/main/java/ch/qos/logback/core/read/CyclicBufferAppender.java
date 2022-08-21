package ch.qos.logback.core.read;

import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.helpers.CyclicBuffer;

/* loaded from: classes.dex */
public class CyclicBufferAppender<E> extends AppenderBase<E> {
    public CyclicBuffer<E> cb;
    public int maxSize = 512;

    @Override // ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.cb = new CyclicBuffer<>(this.maxSize);
        super.start();
    }

    @Override // ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.cb = null;
        super.stop();
    }

    @Override // ch.qos.logback.core.AppenderBase
    public void append(E e) {
        if (!isStarted()) {
            return;
        }
        this.cb.add(e);
    }

    public int getLength() {
        if (isStarted()) {
            return this.cb.length();
        }
        return 0;
    }

    public E get(int i) {
        if (isStarted()) {
            return this.cb.get(i);
        }
        return null;
    }

    public void reset() {
        this.cb.clear();
    }

    public int getMaxSize() {
        return this.maxSize;
    }

    public void setMaxSize(int i) {
        this.maxSize = i;
    }
}
