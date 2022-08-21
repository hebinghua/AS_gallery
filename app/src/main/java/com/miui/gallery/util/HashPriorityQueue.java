package com.miui.gallery.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class HashPriorityQueue<E> extends AbstractQueue<E> implements Serializable {
    private static final long serialVersionUID = -7720805057305804111L;
    private final Comparator<? super E> comparator;
    public HashMap<Object, Object> elementMap;
    public transient int modCount;
    public transient Object[] queue;
    public int size;

    public HashPriorityQueue() {
        this(11, null);
    }

    public HashPriorityQueue(Comparator<? super E> comparator) {
        this(11, comparator);
    }

    public HashPriorityQueue(int i, Comparator<? super E> comparator) {
        if (i < 1) {
            throw new IllegalArgumentException();
        }
        this.queue = new Object[i];
        this.elementMap = new HashMap<>(i);
        this.comparator = comparator;
    }

    public final void grow(int i) {
        int length = this.queue.length;
        int i2 = length + (length < 64 ? length + 2 : length >> 1);
        if (i2 - 2147483639 > 0) {
            i2 = hugeCapacity(i);
        }
        this.queue = Arrays.copyOf(this.queue, i2);
    }

    public static int hugeCapacity(int i) {
        if (i >= 0) {
            return i > 2147483639 ? Integer.MAX_VALUE : 2147483639;
        }
        throw new OutOfMemoryError();
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection, java.util.Queue
    public boolean add(E e) {
        return offer(e);
    }

    @Override // java.util.Queue
    public boolean offer(E e) {
        Objects.requireNonNull(e);
        this.modCount++;
        int i = this.size;
        if (i >= this.queue.length) {
            grow(i + 1);
        }
        this.size = i + 1;
        if (i == 0) {
            this.queue[0] = e;
        } else {
            siftUp(i, e);
        }
        this.elementMap.put(e, e);
        return true;
    }

    @Override // java.util.Queue
    public E peek() {
        if (this.size == 0) {
            return null;
        }
        return (E) this.queue[0];
    }

    public E found(E e) {
        return (E) this.elementMap.get(e);
    }

    public final int indexOf(Object obj) {
        if (obj != null) {
            for (int i = 0; i < this.size; i++) {
                if (obj.equals(this.queue[i])) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean remove(Object obj) {
        int indexOf = indexOf(obj);
        if (indexOf == -1) {
            return false;
        }
        removeAt(indexOf);
        return true;
    }

    public boolean removeEq(Object obj) {
        for (int i = 0; i < this.size; i++) {
            if (obj == this.queue[i]) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return Arrays.copyOf(this.queue, this.size);
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] tArr) {
        int i = this.size;
        if (tArr.length < i) {
            return (T[]) Arrays.copyOf(this.queue, i, tArr.getClass());
        }
        System.arraycopy(this.queue, 0, tArr, 0, i);
        if (tArr.length > i) {
            tArr[i] = null;
        }
        return tArr;
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
    public Iterator<E> iterator() {
        return new Itr();
    }

    /* loaded from: classes2.dex */
    public final class Itr implements Iterator<E> {
        public int cursor;
        public int expectedModCount;
        public ArrayDeque<E> forgetMeNot;
        public int lastRet;
        public E lastRetElt;

        public Itr() {
            this.lastRet = -1;
            this.expectedModCount = HashPriorityQueue.this.modCount;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            ArrayDeque<E> arrayDeque;
            return this.cursor < HashPriorityQueue.this.size || ((arrayDeque = this.forgetMeNot) != null && !arrayDeque.isEmpty());
        }

        @Override // java.util.Iterator
        public E next() {
            int i = this.expectedModCount;
            HashPriorityQueue hashPriorityQueue = HashPriorityQueue.this;
            if (i != hashPriorityQueue.modCount) {
                throw new ConcurrentModificationException();
            }
            int i2 = this.cursor;
            if (i2 < hashPriorityQueue.size) {
                Object[] objArr = hashPriorityQueue.queue;
                this.cursor = i2 + 1;
                this.lastRet = i2;
                return (E) objArr[i2];
            }
            ArrayDeque<E> arrayDeque = this.forgetMeNot;
            if (arrayDeque != null) {
                this.lastRet = -1;
                E poll = arrayDeque.poll();
                this.lastRetElt = poll;
                if (poll != null) {
                    return poll;
                }
            }
            throw new NoSuchElementException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Iterator
        public void remove() {
            int i = this.expectedModCount;
            HashPriorityQueue hashPriorityQueue = HashPriorityQueue.this;
            if (i != hashPriorityQueue.modCount) {
                throw new ConcurrentModificationException();
            }
            int i2 = this.lastRet;
            if (i2 != -1) {
                Object removeAt = hashPriorityQueue.removeAt(i2);
                this.lastRet = -1;
                if (removeAt == null) {
                    this.cursor--;
                } else {
                    if (this.forgetMeNot == null) {
                        this.forgetMeNot = new ArrayDeque<>();
                    }
                    this.forgetMeNot.add(removeAt);
                }
            } else {
                E e = this.lastRetElt;
                if (e != null) {
                    hashPriorityQueue.removeEq(e);
                    this.lastRetElt = null;
                } else {
                    throw new IllegalStateException();
                }
            }
            this.expectedModCount = HashPriorityQueue.this.modCount;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public int size() {
        return this.size;
    }

    @Override // java.util.AbstractQueue, java.util.AbstractCollection, java.util.Collection
    public void clear() {
        this.modCount++;
        for (int i = 0; i < this.size; i++) {
            this.queue[i] = null;
        }
        this.elementMap.clear();
        this.size = 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Queue
    public E poll() {
        int i = this.size;
        if (i == 0) {
            return null;
        }
        int i2 = i - 1;
        this.size = i2;
        this.modCount++;
        Object[] objArr = this.queue;
        E e = (E) objArr[0];
        Object obj = objArr[i2];
        objArr[i2] = null;
        if (i2 != 0) {
            siftDown(0, obj);
        }
        this.elementMap.remove(e);
        return e;
    }

    public E removeAt(int i) {
        this.modCount++;
        int i2 = this.size - 1;
        this.size = i2;
        if (i2 == i) {
            this.queue[i] = null;
        } else {
            Object[] objArr = this.queue;
            E e = (E) objArr[i2];
            objArr[i2] = null;
            siftDown(i, e);
            if (this.queue[i] == e) {
                siftUp(i, e);
                if (this.queue[i] != e) {
                    this.elementMap.remove(e);
                    return e;
                }
            }
        }
        return null;
    }

    public final void siftUp(int i, E e) {
        if (this.comparator != null) {
            siftUpUsingComparator(i, e);
        } else {
            siftUpComparable(i, e);
        }
    }

    public final void siftUpComparable(int i, E e) {
        Comparable comparable = (Comparable) e;
        while (i > 0) {
            int i2 = (i - 1) >>> 1;
            Object obj = this.queue[i2];
            if (comparable.compareTo(obj) >= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i2;
        }
        this.queue[i] = comparable;
    }

    public final void siftUpUsingComparator(int i, E e) {
        while (i > 0) {
            int i2 = (i - 1) >>> 1;
            Object obj = this.queue[i2];
            if (this.comparator.compare(e, obj) >= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i2;
        }
        this.queue[i] = e;
    }

    public final void siftDown(int i, E e) {
        if (this.comparator != null) {
            siftDownUsingComparator(i, e);
        } else {
            siftDownComparable(i, e);
        }
    }

    public final void siftDownComparable(int i, E e) {
        Comparable comparable = (Comparable) e;
        int i2 = this.size >>> 1;
        while (i < i2) {
            int i3 = (i << 1) + 1;
            Object[] objArr = this.queue;
            Object obj = objArr[i3];
            int i4 = i3 + 1;
            if (i4 < this.size && ((Comparable) obj).compareTo(objArr[i4]) > 0) {
                obj = this.queue[i4];
                i3 = i4;
            }
            if (comparable.compareTo(obj) <= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i3;
        }
        this.queue[i] = comparable;
    }

    public final void siftDownUsingComparator(int i, E e) {
        int i2 = this.size >>> 1;
        while (i < i2) {
            int i3 = (i << 1) + 1;
            Object[] objArr = this.queue;
            Object obj = objArr[i3];
            int i4 = i3 + 1;
            if (i4 < this.size && this.comparator.compare(obj, objArr[i4]) > 0) {
                obj = this.queue[i4];
                i3 = i4;
            }
            if (this.comparator.compare(e, obj) <= 0) {
                break;
            }
            this.queue[i] = obj;
            i = i3;
        }
        this.queue[i] = e;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void heapify() {
        for (int i = (this.size >>> 1) - 1; i >= 0; i--) {
            siftDown(i, this.queue[i]);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(Math.max(2, this.size + 1));
        for (int i = 0; i < this.size; i++) {
            objectOutputStream.writeObject(this.queue[i]);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        objectInputStream.readInt();
        this.queue = new Object[this.size];
        for (int i = 0; i < this.size; i++) {
            this.queue[i] = objectInputStream.readObject();
        }
        heapify();
    }

    @Override // java.util.Collection, java.lang.Iterable
    public final Spliterator<E> spliterator() {
        return new PriorityQueueSpliterator(this, 0, -1, 0);
    }

    /* loaded from: classes2.dex */
    public static final class PriorityQueueSpliterator<E> implements Spliterator<E> {
        public int expectedModCount;
        public int fence;
        public int index;
        public final HashPriorityQueue<E> pq;

        @Override // java.util.Spliterator
        public int characteristics() {
            return 16704;
        }

        public PriorityQueueSpliterator(HashPriorityQueue<E> hashPriorityQueue, int i, int i2, int i3) {
            this.pq = hashPriorityQueue;
            this.index = i;
            this.fence = i2;
            this.expectedModCount = i3;
        }

        public final int getFence() {
            int i = this.fence;
            if (i < 0) {
                HashPriorityQueue<E> hashPriorityQueue = this.pq;
                this.expectedModCount = hashPriorityQueue.modCount;
                int i2 = hashPriorityQueue.size;
                this.fence = i2;
                return i2;
            }
            return i;
        }

        @Override // java.util.Spliterator
        /* renamed from: trySplit */
        public PriorityQueueSpliterator<E> mo1696trySplit() {
            int fence = getFence();
            int i = this.index;
            int i2 = (fence + i) >>> 1;
            if (i >= i2) {
                return null;
            }
            HashPriorityQueue<E> hashPriorityQueue = this.pq;
            this.index = i2;
            return new PriorityQueueSpliterator<>(hashPriorityQueue, i, i2, this.expectedModCount);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super E> consumer) {
            Object[] objArr;
            int i;
            Objects.requireNonNull(consumer);
            HashPriorityQueue<E> hashPriorityQueue = this.pq;
            if (hashPriorityQueue != null && (objArr = hashPriorityQueue.queue) != null) {
                int i2 = this.fence;
                if (i2 < 0) {
                    i = hashPriorityQueue.modCount;
                    i2 = hashPriorityQueue.size;
                } else {
                    i = this.expectedModCount;
                }
                int i3 = this.index;
                if (i3 >= 0) {
                    this.index = i2;
                    if (i2 <= objArr.length) {
                        while (true) {
                            if (i3 < i2) {
                                Object obj = objArr[i3];
                                if (obj == null) {
                                    break;
                                }
                                consumer.accept(obj);
                                i3++;
                            } else if (hashPriorityQueue.modCount == i) {
                                return;
                            }
                        }
                    }
                }
            }
            throw new ConcurrentModificationException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            int fence = getFence();
            int i = this.index;
            if (i < 0 || i >= fence) {
                return false;
            }
            this.index = i + 1;
            Object obj = this.pq.queue[i];
            if (obj == null) {
                throw new ConcurrentModificationException();
            }
            consumer.accept(obj);
            if (this.pq.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            return true;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return getFence() - this.index;
        }
    }
}
