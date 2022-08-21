package kotlin.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.RandomAccess;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: SlidingWindow.kt */
/* loaded from: classes3.dex */
public final class RingBuffer<T> extends AbstractList<T> implements RandomAccess {
    public final Object[] buffer;
    public final int capacity;
    public int size;
    public int startIndex;

    public RingBuffer(Object[] buffer, int i) {
        Intrinsics.checkNotNullParameter(buffer, "buffer");
        this.buffer = buffer;
        boolean z = true;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("ring buffer filled size should not be negative but it is ", Integer.valueOf(i)).toString());
        }
        if (!(i > buffer.length ? false : z)) {
            throw new IllegalArgumentException(("ring buffer filled size: " + i + " cannot be larger than the buffer size: " + buffer.length).toString());
        }
        this.capacity = buffer.length;
        this.size = i;
    }

    public static final /* synthetic */ Object[] access$getBuffer$p(RingBuffer ringBuffer) {
        return ringBuffer.buffer;
    }

    public static final /* synthetic */ int access$getCapacity$p(RingBuffer ringBuffer) {
        return ringBuffer.capacity;
    }

    public static final /* synthetic */ int access$getStartIndex$p(RingBuffer ringBuffer) {
        return ringBuffer.startIndex;
    }

    public RingBuffer(int i) {
        this(new Object[i], 0);
    }

    @Override // kotlin.collections.AbstractCollection
    public int getSize() {
        return this.size;
    }

    @Override // kotlin.collections.AbstractList, java.util.List
    public T get(int i) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(i, size());
        return (T) this.buffer[(this.startIndex + i) % this.capacity];
    }

    public final boolean isFull() {
        return size() == this.capacity;
    }

    @Override // kotlin.collections.AbstractList, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<T> iterator() {
        return new AbstractIterator<T>(this) { // from class: kotlin.collections.RingBuffer$iterator$1
            public int count;
            public int index;
            public final /* synthetic */ RingBuffer<T> this$0;

            {
                this.this$0 = this;
                this.count = this.size();
                this.index = RingBuffer.access$getStartIndex$p(this);
            }

            @Override // kotlin.collections.AbstractIterator
            public void computeNext() {
                if (this.count == 0) {
                    done();
                    return;
                }
                setNext(RingBuffer.access$getBuffer$p(this.this$0)[this.index]);
                this.index = (this.index + 1) % RingBuffer.access$getCapacity$p(this.this$0);
                this.count--;
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public <T> T[] toArray(T[] array) {
        Intrinsics.checkNotNullParameter(array, "array");
        if (array.length < size()) {
            array = (T[]) Arrays.copyOf(array, size());
            Intrinsics.checkNotNullExpressionValue(array, "copyOf(this, newSize)");
        }
        int size = size();
        int i = 0;
        int i2 = 0;
        for (int i3 = this.startIndex; i2 < size && i3 < this.capacity; i3++) {
            array[i2] = this.buffer[i3];
            i2++;
        }
        while (i2 < size) {
            array[i2] = this.buffer[i];
            i2++;
            i++;
        }
        if (array.length > size()) {
            array[size()] = null;
        }
        return array;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final RingBuffer<T> expanded(int i) {
        Object[] array;
        int i2 = this.capacity;
        int coerceAtMost = RangesKt___RangesKt.coerceAtMost(i2 + (i2 >> 1) + 1, i);
        if (this.startIndex == 0) {
            array = Arrays.copyOf(this.buffer, coerceAtMost);
            Intrinsics.checkNotNullExpressionValue(array, "copyOf(this, newSize)");
        } else {
            array = toArray(new Object[coerceAtMost]);
        }
        return new RingBuffer<>(array, size());
    }

    @Override // kotlin.collections.AbstractCollection, java.util.Collection
    public final void add(T t) {
        if (isFull()) {
            throw new IllegalStateException("ring buffer is full");
        }
        this.buffer[(this.startIndex + size()) % this.capacity] = t;
        this.size = size() + 1;
    }

    public final void removeFirst(int i) {
        boolean z = true;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("n shouldn't be negative but it is ", Integer.valueOf(i)).toString());
        }
        if (i > size()) {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(("n shouldn't be greater than the buffer size: n = " + i + ", size = " + size()).toString());
        } else if (i <= 0) {
        } else {
            int i2 = this.startIndex;
            int i3 = (i2 + i) % this.capacity;
            if (i2 > i3) {
                ArraysKt___ArraysJvmKt.fill(this.buffer, null, i2, this.capacity);
                ArraysKt___ArraysJvmKt.fill(this.buffer, null, 0, i3);
            } else {
                ArraysKt___ArraysJvmKt.fill(this.buffer, null, i2, i3);
            }
            this.startIndex = i3;
            this.size = size() - i;
        }
    }
}
