package ch.qos.logback.core.spi;

import java.io.Serializable;

/* loaded from: classes.dex */
public interface PreSerializationTransformer<E> {
    Serializable transform(E e);
}
