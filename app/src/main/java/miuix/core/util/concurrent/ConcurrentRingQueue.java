package miuix.core.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes3.dex */
public class ConcurrentRingQueue<T> {
    public volatile int mAdditional;
    public final boolean mAllowExtendCapacity;
    public final boolean mAutoReleaseCapacity;
    public int mCapacity;
    public final AtomicInteger mReadLock = new AtomicInteger(0);
    public final AtomicInteger mWriteLock = new AtomicInteger(0);
    public volatile Node<T> mReadCursor = new Node<>();
    public volatile Node<T> mWriteCursor = this.mReadCursor;

    /* loaded from: classes3.dex */
    public static class Node<T> {
        public T element;
        public Node<T> next;

        public Node() {
        }
    }

    public ConcurrentRingQueue(int i, boolean z, boolean z2) {
        this.mCapacity = i;
        this.mAllowExtendCapacity = z;
        this.mAutoReleaseCapacity = z2;
        int i2 = 0;
        Node<T> node = this.mReadCursor;
        while (i2 < i) {
            Node<T> node2 = new Node<>();
            node.next = node2;
            i2++;
            node = node2;
        }
        node.next = this.mReadCursor;
    }

    public boolean put(T t) {
        if (t == null) {
            return false;
        }
        while (true) {
            if (this.mWriteLock.get() == 0 && this.mWriteLock.compareAndSet(0, -1)) {
                break;
            }
            Thread.yield();
        }
        Node<T> node = this.mReadCursor;
        Node<T> node2 = this.mWriteCursor;
        int i = this.mAdditional;
        Node<T> node3 = node2.next;
        boolean z = true;
        if (node3 != node) {
            node2.element = t;
            Node<T> node4 = node3.next;
            if (node4 != node && this.mAutoReleaseCapacity && i > 0) {
                node2.next = node4;
                this.mAdditional = i - 1;
            }
            this.mWriteCursor = node2.next;
        } else if (this.mAllowExtendCapacity || i < 0) {
            Node<T> node5 = new Node<>();
            node2.next = node5;
            node5.next = node;
            node2.element = t;
            this.mAdditional = i + 1;
            this.mWriteCursor = node2.next;
        } else {
            z = false;
        }
        this.mWriteLock.set(0);
        return z;
    }

    public T get() {
        while (true) {
            if (this.mReadLock.get() == 0 && this.mReadLock.compareAndSet(0, -1)) {
                break;
            }
            Thread.yield();
        }
        Node<T> node = this.mReadCursor;
        Node<T> node2 = this.mWriteCursor;
        T t = null;
        while (t == null && node != node2) {
            t = node.element;
            node.element = null;
            node = node.next;
            node2 = this.mWriteCursor;
        }
        if (t != null) {
            this.mReadCursor = node;
        }
        this.mReadLock.set(0);
        return t;
    }

    public int getCapacity() {
        int i = this.mAdditional;
        int i2 = this.mCapacity;
        return i > 0 ? i2 + i : i2;
    }

    public void increaseCapacity(int i) {
        if (this.mAllowExtendCapacity || i <= 0) {
            return;
        }
        while (true) {
            if (this.mWriteLock.get() != 0 || !this.mWriteLock.compareAndSet(0, -1)) {
                Thread.yield();
            } else {
                this.mAdditional = -i;
                this.mCapacity += i;
                this.mWriteLock.set(0);
                return;
            }
        }
    }

    public void decreaseCapacity(int i) {
        if (!this.mAutoReleaseCapacity || i <= 0) {
            return;
        }
        while (true) {
            if (this.mWriteLock.get() != 0 || !this.mWriteLock.compareAndSet(0, -1)) {
                Thread.yield();
            } else {
                this.mCapacity -= i;
                this.mAdditional = i;
                this.mWriteLock.set(0);
                return;
            }
        }
    }
}
