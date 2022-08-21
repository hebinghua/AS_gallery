package com.google.common.collect;

import com.google.common.base.Optional;

/* loaded from: classes.dex */
public abstract class FluentIterable<E> implements Iterable<E> {
    public final Optional<Iterable<E>> iterableDelegate = Optional.absent();

    public final Iterable<E> getDelegate() {
        return this.iterableDelegate.or(this);
    }

    public String toString() {
        return Iterables.toString(getDelegate());
    }
}
