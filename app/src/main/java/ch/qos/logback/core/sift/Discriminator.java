package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.LifeCycle;

/* loaded from: classes.dex */
public interface Discriminator<E> extends LifeCycle {
    String getDiscriminatingValue(E e);

    String getKey();
}
