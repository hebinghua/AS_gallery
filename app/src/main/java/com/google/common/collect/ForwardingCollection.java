package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;

/* loaded from: classes.dex */
public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E> {
    @Override // com.google.common.collect.ForwardingObject
    /* renamed from: delegate */
    public abstract Collection<E> mo254delegate();

    @Override // java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return mo254delegate().iterator();
    }

    @Override // java.util.Collection
    public int size() {
        return mo254delegate().size();
    }

    @Override // java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        return mo254delegate().removeAll(collection);
    }

    @Override // java.util.Collection
    public boolean isEmpty() {
        return mo254delegate().isEmpty();
    }

    @Override // java.util.Collection
    public boolean contains(Object obj) {
        return mo254delegate().contains(obj);
    }

    @Override // java.util.Collection
    public boolean remove(Object obj) {
        return mo254delegate().remove(obj);
    }

    @Override // java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return mo254delegate().containsAll(collection);
    }

    @Override // java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        return mo254delegate().retainAll(collection);
    }

    @Override // java.util.Collection
    public void clear() {
        mo254delegate().clear();
    }

    public Object[] toArray() {
        return mo254delegate().toArray();
    }

    @Override // java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        return (T[]) mo254delegate().toArray(tArr);
    }

    public boolean standardAddAll(Collection<? extends E> collection) {
        return Iterators.addAll(this, collection.iterator());
    }
}
