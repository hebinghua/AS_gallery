package ch.qos.logback.core.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public class COWArrayList<E> implements List<E> {
    public final E[] modelArray;
    public E[] ourCopy;
    public AtomicBoolean fresh = new AtomicBoolean(false);
    public CopyOnWriteArrayList<E> underlyingList = new CopyOnWriteArrayList<>();

    public COWArrayList(E[] eArr) {
        this.modelArray = eArr;
    }

    @Override // java.util.List, java.util.Collection
    public int size() {
        return this.underlyingList.size();
    }

    @Override // java.util.List, java.util.Collection
    public boolean isEmpty() {
        return this.underlyingList.isEmpty();
    }

    @Override // java.util.List, java.util.Collection
    public boolean contains(Object obj) {
        return this.underlyingList.contains(obj);
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return this.underlyingList.iterator();
    }

    private void refreshCopyIfNecessary() {
        if (!isFresh()) {
            refreshCopy();
        }
    }

    private boolean isFresh() {
        return this.fresh.get();
    }

    private void refreshCopy() {
        this.ourCopy = (E[]) this.underlyingList.toArray(this.modelArray);
        this.fresh.set(true);
    }

    @Override // java.util.List, java.util.Collection
    public Object[] toArray() {
        refreshCopyIfNecessary();
        return this.ourCopy;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [T[], E[]] */
    @Override // java.util.List, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        refreshCopyIfNecessary();
        return this.ourCopy;
    }

    public E[] asTypedArray() {
        refreshCopyIfNecessary();
        return this.ourCopy;
    }

    private void markAsStale() {
        this.fresh.set(false);
    }

    public void addIfAbsent(E e) {
        this.underlyingList.addIfAbsent(e);
        markAsStale();
    }

    @Override // java.util.List, java.util.Collection
    public boolean add(E e) {
        boolean add = this.underlyingList.add(e);
        markAsStale();
        return add;
    }

    @Override // java.util.List, java.util.Collection
    public boolean remove(Object obj) {
        boolean remove = this.underlyingList.remove(obj);
        markAsStale();
        return remove;
    }

    @Override // java.util.List, java.util.Collection
    public boolean containsAll(Collection<?> collection) {
        return this.underlyingList.containsAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public boolean addAll(Collection<? extends E> collection) {
        boolean addAll = this.underlyingList.addAll(collection);
        markAsStale();
        return addAll;
    }

    @Override // java.util.List
    public boolean addAll(int i, Collection<? extends E> collection) {
        boolean addAll = this.underlyingList.addAll(i, collection);
        markAsStale();
        return addAll;
    }

    @Override // java.util.List, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        boolean removeAll = this.underlyingList.removeAll(collection);
        markAsStale();
        return removeAll;
    }

    @Override // java.util.List, java.util.Collection
    public boolean retainAll(Collection<?> collection) {
        boolean retainAll = this.underlyingList.retainAll(collection);
        markAsStale();
        return retainAll;
    }

    @Override // java.util.List, java.util.Collection
    public void clear() {
        this.underlyingList.clear();
        markAsStale();
    }

    @Override // java.util.List
    public E get(int i) {
        refreshCopyIfNecessary();
        return this.ourCopy[i];
    }

    @Override // java.util.List
    public E set(int i, E e) {
        E e2 = this.underlyingList.set(i, e);
        markAsStale();
        return e2;
    }

    @Override // java.util.List
    public void add(int i, E e) {
        this.underlyingList.add(i, e);
        markAsStale();
    }

    @Override // java.util.List
    public E remove(int i) {
        E remove = this.underlyingList.remove(i);
        markAsStale();
        return remove;
    }

    @Override // java.util.List
    public int indexOf(Object obj) {
        return this.underlyingList.indexOf(obj);
    }

    @Override // java.util.List
    public int lastIndexOf(Object obj) {
        return this.underlyingList.lastIndexOf(obj);
    }

    @Override // java.util.List
    public ListIterator<E> listIterator() {
        return this.underlyingList.listIterator();
    }

    @Override // java.util.List
    public ListIterator<E> listIterator(int i) {
        return this.underlyingList.listIterator(i);
    }

    @Override // java.util.List
    public List<E> subList(int i, int i2) {
        return this.underlyingList.subList(i, i2);
    }
}
