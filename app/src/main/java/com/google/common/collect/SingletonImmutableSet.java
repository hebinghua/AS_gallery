package com.google.common.collect;

import com.google.common.base.Preconditions;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class SingletonImmutableSet<E> extends ImmutableSet<E> {
    public final transient E element;

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return 1;
    }

    public SingletonImmutableSet(E e) {
        this.element = (E) Preconditions.checkNotNull(e);
    }

    @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return this.element.equals(obj);
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    /* renamed from: iterator */
    public UnmodifiableIterator<E> mo273iterator() {
        return Iterators.singletonIterator(this.element);
    }

    @Override // com.google.common.collect.ImmutableCollection
    public int copyIntoArray(Object[] objArr, int i) {
        objArr[i] = this.element;
        return i + 1;
    }

    @Override // com.google.common.collect.ImmutableSet, java.util.Collection, java.util.Set
    public final int hashCode() {
        return this.element.hashCode();
    }

    @Override // java.util.AbstractCollection
    public String toString() {
        String obj = this.element.toString();
        StringBuilder sb = new StringBuilder(String.valueOf(obj).length() + 2);
        sb.append('[');
        sb.append(obj);
        sb.append(']');
        return sb.toString();
    }
}
