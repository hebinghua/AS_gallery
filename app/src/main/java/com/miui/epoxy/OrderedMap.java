package com.miui.epoxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public class OrderedMap<K, V> implements Iterable<V> {
    public HashMap<K, V> map = new HashMap<>();
    public List<K> orderList = new ArrayList();

    public boolean checkExistAndConsistency(K k) {
        boolean containsKey = this.map.containsKey(k);
        boolean contains = this.orderList.contains(k);
        if (!(containsKey ^ contains)) {
            return containsKey & contains;
        }
        throw new IllegalStateException("inconsistent key=" + k);
    }

    public V get(K k) {
        if (checkExistAndConsistency(k)) {
            return this.map.get(k);
        }
        return null;
    }

    public V put(K k, V v) {
        if (!checkExistAndConsistency(k)) {
            this.map.put(k, v);
            this.orderList.add(k);
            return null;
        }
        return null;
    }

    public V remove(K k) {
        if (checkExistAndConsistency(k)) {
            this.map.remove(k);
            this.orderList.remove(k);
            return null;
        }
        return null;
    }

    public int size() {
        return this.orderList.size();
    }

    public Collection<V> values() {
        ArrayList arrayList = new ArrayList();
        for (K k : this.orderList) {
            arrayList.add(this.map.get(k));
        }
        return arrayList;
    }

    public V getFirstOrNull() {
        if (this.orderList.size() == 0) {
            return null;
        }
        return this.map.get(this.orderList.get(0));
    }

    @Override // java.lang.Iterable
    public Iterator<V> iterator() {
        return new Iterator<V>() { // from class: com.miui.epoxy.OrderedMap.1
            public int index = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index < OrderedMap.this.orderList.size();
            }

            @Override // java.util.Iterator
            public V next() {
                if (hasNext()) {
                    HashMap hashMap = OrderedMap.this.map;
                    List list = OrderedMap.this.orderList;
                    int i = this.index;
                    this.index = i + 1;
                    return (V) hashMap.get(list.get(i));
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
