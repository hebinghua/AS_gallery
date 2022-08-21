package com.google.common.collect;

import java.util.Queue;

/* loaded from: classes.dex */
public abstract class ForwardingQueue<E> extends ForwardingCollection<E> implements Queue<E> {
    @Override // com.google.common.collect.ForwardingCollection, com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    public abstract Queue<E> mo254delegate();

    @Override // java.util.Queue
    public E poll() {
        return mo254delegate().poll();
    }

    @Override // java.util.Queue
    public E remove() {
        return mo254delegate().remove();
    }

    @Override // java.util.Queue
    public E peek() {
        return mo254delegate().peek();
    }

    @Override // java.util.Queue
    public E element() {
        return mo254delegate().element();
    }
}
