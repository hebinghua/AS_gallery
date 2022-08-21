package com.miui.gallery.sdk.download.executor.queue;

import com.miui.gallery.sdk.download.executor.queue.Command;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/* loaded from: classes2.dex */
public class Queue<T extends Command> {
    public final int mMaxSize;
    public final HashMap<String, T> mMap = new HashMap<>();
    public final LinkedList<T> mList = new LinkedList<>();

    public Queue(int i) {
        this.mMaxSize = i;
    }

    public T get(String str) {
        return this.mMap.get(str);
    }

    public int size() {
        return this.mMap.size();
    }

    public T remove(String str) {
        T remove = this.mMap.remove(str);
        if (remove != null) {
            this.mList.remove(remove);
        }
        return remove;
    }

    public void clear() {
        this.mMap.clear();
        this.mList.clear();
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(this.mMap.values());
    }

    public List<T> poll(int i) {
        LinkedList linkedList = new LinkedList();
        ListIterator<T> listIterator = this.mList.listIterator();
        while (listIterator.hasNext() && linkedList.size() < i) {
            T next = listIterator.next();
            linkedList.add(next);
            listIterator.remove();
            this.mMap.remove(next.getKey());
        }
        return linkedList;
    }

    public int putAtFirst(List<T> list) {
        int i = 0;
        ListIterator<T> listIterator = list.listIterator(0);
        while (listIterator.hasNext()) {
            i += putInternal(listIterator.next(), true) ? 1 : 0;
        }
        return i;
    }

    public int putAtLast(List<T> list) {
        ListIterator<T> listIterator = list.listIterator(0);
        int i = 0;
        while (listIterator.hasNext()) {
            i += putInternal(listIterator.next(), false) ? 1 : 0;
        }
        return i;
    }

    public final boolean putInternal(T t, boolean z) {
        if (!needAdd(t)) {
            return false;
        }
        this.mMap.put(t.getKey(), t);
        if (z) {
            this.mList.addFirst(t);
            if (this.mMaxSize <= 0 || size() <= this.mMaxSize) {
                return true;
            }
            this.mMap.remove(this.mList.removeLast().getKey());
            return true;
        }
        this.mList.addLast(t);
        return true;
    }

    public final boolean needAdd(T t) {
        T t2 = this.mMap.get(t.getKey());
        if (t2 == null) {
            return true;
        }
        if (t2.getPriority() >= t.getPriority()) {
            return false;
        }
        remove((Queue<T>) t2);
        return true;
    }

    public final void remove(T t) {
        this.mMap.remove(t.getKey());
        this.mList.remove(t);
    }
}
