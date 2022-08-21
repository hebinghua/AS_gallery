package cn.kuaipan.android.utils;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: classes.dex */
public class TwoKeyHashMap<E, K, V> extends AbstractMap<String, V> {
    public Entry<E, K, V>[] arr;
    public int arrSize;
    public Set<Map.Entry<String, V>> entrySet;
    public float loadFactor;
    public int modCount;
    public int size;
    public int threshold;
    public Collection<V> values;

    public static /* synthetic */ int access$010(TwoKeyHashMap twoKeyHashMap) {
        int i = twoKeyHashMap.size;
        twoKeyHashMap.size = i - 1;
        return i;
    }

    public static /* synthetic */ int access$308(TwoKeyHashMap twoKeyHashMap) {
        int i = twoKeyHashMap.modCount;
        twoKeyHashMap.modCount = i + 1;
        return i;
    }

    public TwoKeyHashMap() {
        this(16, 0.75f);
    }

    public TwoKeyHashMap(int i, float f) {
        this.threshold = 0;
        if (i >= 0) {
            if (f <= 0.0f) {
                throw new IllegalArgumentException("initialLoadFactor should be > 0");
            }
            this.loadFactor = f;
            i = i == Integer.MAX_VALUE ? i - 1 : i;
            i = i <= 0 ? 1 : i;
            this.arrSize = i;
            this.threshold = (int) (i * f);
            this.arr = new Entry[i + 1];
            return;
        }
        throw new IllegalArgumentException("initialCapacity should be >= 0");
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Collection<V> values() {
        if (this.values == null) {
            this.values = new ValuesCollectionImpl();
        }
        return this.values;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = new EntrySetImpl();
        }
        return this.entrySet;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        this.modCount++;
        this.size = 0;
        Entry<E, K, V>[] entryArr = this.arr;
        Arrays.fill(entryArr, 0, entryArr.length, (Object) null);
    }

    public V put(E e, K k, V v) {
        if (e == null && k == null) {
            int i = this.arrSize;
            Entry<E, K, V>[] entryArr = this.arr;
            if (entryArr[i] == null) {
                entryArr[i] = createEntry(0, null, null, v, null);
                this.size++;
                this.modCount++;
                return null;
            }
            V v2 = entryArr[i].value;
            entryArr[i].value = v;
            return v2;
        }
        int hashCode = e.hashCode() + k.hashCode();
        int i2 = (Integer.MAX_VALUE & hashCode) % this.arrSize;
        for (Entry<E, K, V> entry = this.arr[i2]; entry != null; entry = entry.next) {
            if (hashCode == entry.hash && e.equals(entry.getKey1()) && k.equals(entry.getKey2())) {
                V v3 = entry.value;
                entry.value = v;
                return v3;
            }
        }
        Entry<E, K, V>[] entryArr2 = this.arr;
        entryArr2[i2] = createEntry(hashCode, e, k, v, entryArr2[i2]);
        int i3 = this.size + 1;
        this.size = i3;
        this.modCount++;
        if (i3 > this.threshold) {
            rehash();
        }
        return null;
    }

    public void rehash() {
        Entry<E, K, V>[] entryArr;
        int i = ((this.arrSize + 1) * 2) + 1;
        if (i < 0) {
            i = 2147483646;
        }
        Entry<E, K, V>[] entryArr2 = new Entry[i + 1];
        int i2 = 0;
        while (true) {
            entryArr = this.arr;
            if (i2 >= entryArr.length - 1) {
                break;
            }
            Entry<E, K, V> entry = entryArr[i2];
            while (entry != null) {
                Entry<E, K, V> entry2 = entry.next;
                int i3 = (entry.hash & Integer.MAX_VALUE) % i;
                entry.next = entryArr2[i3];
                entryArr2[i3] = entry;
                entry = entry2;
            }
            i2++;
        }
        entryArr2[i] = entryArr[this.arrSize];
        this.arrSize = i;
        if (i == Integer.MAX_VALUE) {
            this.loadFactor *= 10.0f;
        }
        this.threshold = (int) (i * this.loadFactor);
        this.arr = entryArr2;
    }

    public V get(Object obj, Object obj2) {
        Entry<E, K, V> findEntry = findEntry(obj, obj2);
        if (findEntry != null) {
            return findEntry.value;
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.size;
    }

    public Entry<E, K, V> createEntry(int i, E e, K k, V v, Entry<E, K, V> entry) {
        return new Entry<>(i, e, k, v, entry);
    }

    public Iterator<Map.Entry<String, V>> createEntrySetIterator() {
        return new EntryIteratorImpl();
    }

    public Iterator<V> createValueCollectionIterator() {
        return new ValueIteratorImpl();
    }

    /* loaded from: classes.dex */
    public static class Entry<E, K, V> implements Map.Entry<String, V> {
        public int hash;
        public E key1;
        public K key2;
        public Entry<E, K, V> next;
        public V value;

        public Entry(int i, E e, K k, V v, Entry<E, K, V> entry) {
            this.hash = i;
            this.key1 = e;
            this.key2 = k;
            this.value = v;
            this.next = entry;
        }

        @Override // java.util.Map.Entry
        public String getKey() {
            return this.key1.toString() + this.key2.toString();
        }

        public E getKey1() {
            return this.key1;
        }

        public K getKey2() {
            return this.key2;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Object key1 = entry.getKey1();
            Object key2 = entry.getKey2();
            Object value = entry.getValue();
            E e = this.key1;
            return (e != null || key1 == null) && (this.key2 != null || key2 == null) && ((this.value != null || value == null) && e.equals(entry.getKey1()) && this.key2.equals(entry.getKey2()) && this.value.equals(value));
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            E e = this.key1;
            int i = 0;
            int hashCode = e == null ? 0 : e.hashCode();
            K k = this.key2;
            int hashCode2 = hashCode + (k == null ? 0 : k.hashCode());
            V v = this.value;
            if (v != null) {
                i = v.hashCode();
            }
            return hashCode2 ^ i;
        }
    }

    /* loaded from: classes.dex */
    public class EntrySetImpl extends AbstractSet<Map.Entry<String, V>> {
        public EntrySetImpl() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return TwoKeyHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            TwoKeyHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean isEmpty() {
            return TwoKeyHashMap.this.size == 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            Entry findEntry = TwoKeyHashMap.this.findEntry(entry.getKey1(), entry.getKey2());
            if (findEntry == null) {
                return false;
            }
            Object value = entry.getValue();
            Object value2 = findEntry.getValue();
            if (value != null) {
                return value.equals(value2);
            }
            return value2 == null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return TwoKeyHashMap.this.removeEntry(entry.getKey1(), entry.getKey2()) != null;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator<Map.Entry<String, V>> iterator() {
            return TwoKeyHashMap.this.createEntrySetIterator();
        }
    }

    /* loaded from: classes.dex */
    public class EntryIteratorImpl implements Iterator<Map.Entry<String, V>> {
        public Entry<E, K, V> curr_entry;
        public boolean found;
        public Entry<E, K, V> returned_entry;
        public int startModCount;
        public int curr = -1;
        public int returned_index = -1;

        public EntryIteratorImpl() {
            this.startModCount = TwoKeyHashMap.this.modCount;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            boolean z = true;
            if (this.found) {
                return true;
            }
            Entry<E, K, V> entry = this.curr_entry;
            if (entry != null) {
                this.curr_entry = entry.next;
            }
            if (this.curr_entry == null) {
                this.curr++;
                while (this.curr < TwoKeyHashMap.this.arr.length) {
                    Entry[] entryArr = TwoKeyHashMap.this.arr;
                    int i = this.curr;
                    if (entryArr[i] != null) {
                        break;
                    }
                    this.curr = i + 1;
                }
                if (this.curr < TwoKeyHashMap.this.arr.length) {
                    this.curr_entry = TwoKeyHashMap.this.arr[this.curr];
                }
            }
            if (this.curr_entry == null) {
                z = false;
            }
            this.found = z;
            return z;
        }

        @Override // java.util.Iterator
        public Map.Entry<String, V> next() {
            if (TwoKeyHashMap.this.modCount != this.startModCount) {
                throw new ConcurrentModificationException();
            }
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            this.found = false;
            this.returned_index = this.curr;
            Entry<E, K, V> entry = this.curr_entry;
            this.returned_entry = entry;
            return entry;
        }

        @Override // java.util.Iterator
        public void remove() {
            Entry<E, K, V> entry;
            Entry<E, K, V> entry2;
            if (this.returned_index != -1) {
                if (TwoKeyHashMap.this.modCount == this.startModCount) {
                    Entry<E, K, V> entry3 = null;
                    Entry<E, K, V> entry4 = TwoKeyHashMap.this.arr[this.returned_index];
                    while (true) {
                        Entry<E, K, V> entry5 = entry4;
                        entry = entry3;
                        entry3 = entry5;
                        entry2 = this.returned_entry;
                        if (entry3 == entry2) {
                            break;
                        }
                        entry4 = entry3.next;
                    }
                    if (entry == null) {
                        TwoKeyHashMap.this.arr[this.returned_index] = this.returned_entry.next;
                    } else {
                        entry.next = entry2.next;
                    }
                    TwoKeyHashMap.access$010(TwoKeyHashMap.this);
                    TwoKeyHashMap.access$308(TwoKeyHashMap.this);
                    this.startModCount++;
                    this.returned_index = -1;
                    return;
                }
                throw new ConcurrentModificationException();
            }
            throw new IllegalStateException();
        }
    }

    public final Entry<E, K, V> findEntry(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            return this.arr[this.arrSize];
        }
        int hashCode = obj.hashCode() + obj2.hashCode();
        for (Entry<E, K, V> entry = this.arr[(Integer.MAX_VALUE & hashCode) % this.arrSize]; entry != null; entry = entry.next) {
            if (hashCode == entry.hash && obj.equals(entry.getKey1()) && obj2.equals(entry.getKey2())) {
                return entry;
            }
        }
        return null;
    }

    public final Entry<E, K, V> removeEntry(Object obj, Object obj2) {
        if (obj == null && obj2 == null) {
            int i = this.arrSize;
            Entry<E, K, V>[] entryArr = this.arr;
            if (entryArr[i] == null) {
                return null;
            }
            Entry<E, K, V> entry = entryArr[i];
            entryArr[i] = null;
            this.size--;
            this.modCount++;
            return entry;
        }
        int hashCode = obj.hashCode() + obj2.hashCode();
        int i2 = (Integer.MAX_VALUE & hashCode) % this.arrSize;
        Entry<E, K, V> entry2 = this.arr[i2];
        Entry<E, K, V> entry3 = entry2;
        while (entry2 != null) {
            if (hashCode == entry2.hash && obj.equals(entry2.getKey1()) && obj2.equals(entry2.getKey2())) {
                if (entry3 == entry2) {
                    this.arr[i2] = entry2.next;
                } else {
                    entry3.next = entry2.next;
                }
                this.size--;
                this.modCount++;
                return entry2;
            }
            entry3 = entry2;
            entry2 = entry2.next;
        }
        return null;
    }

    /* loaded from: classes.dex */
    public class ValuesCollectionImpl extends AbstractCollection<V> {
        public ValuesCollectionImpl() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return TwoKeyHashMap.this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            TwoKeyHashMap.this.clear();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean isEmpty() {
            return TwoKeyHashMap.this.size == 0;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public Iterator<V> iterator() {
            return TwoKeyHashMap.this.createValueCollectionIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            return TwoKeyHashMap.this.containsValue(obj);
        }
    }

    /* loaded from: classes.dex */
    public class ValueIteratorImpl implements Iterator<V> {
        public TwoKeyHashMap<E, K, V>.EntryIteratorImpl itr;

        public ValueIteratorImpl() {
            this.itr = new EntryIteratorImpl();
        }

        @Override // java.util.Iterator
        public V next() {
            return this.itr.next().getValue();
        }

        @Override // java.util.Iterator
        public void remove() {
            this.itr.remove();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.itr.hasNext();
        }
    }
}
