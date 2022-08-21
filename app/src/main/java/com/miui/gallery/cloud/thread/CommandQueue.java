package com.miui.gallery.cloud.thread;

import android.os.SystemClock;
import com.google.common.collect.Maps;
import com.miui.gallery.cloud.thread.Command;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/* loaded from: classes.dex */
public class CommandQueue<T extends Command> {
    public final LinkedList<T>[] mLists;
    public final HashMap<String, T> mMap = Maps.newHashMap();
    public final int mMaxSize;

    /* loaded from: classes.dex */
    public interface OnQueueChangedListener<T extends Command> {
        void onAdd(T t);

        void onRemove(T t);
    }

    public CommandQueue(int i, int i2) {
        this.mLists = new LinkedList[i];
        this.mMaxSize = i2;
    }

    public int size() {
        return this.mMap.size();
    }

    public T remove(String str) {
        T remove = this.mMap.remove(str);
        if (remove != null) {
            this.mLists[remove.getPriority()].remove(remove);
        }
        return remove;
    }

    public Collection<T> values() {
        return Collections.unmodifiableCollection(this.mMap.values());
    }

    public void poll(List<T> list, int i, long j) {
        for (int length = this.mLists.length - 1; length >= 0; length--) {
            LinkedList<T> linkedList = this.mLists[length];
            if (linkedList != null && !linkedList.isEmpty()) {
                T t = null;
                ListIterator<T> listIterator = linkedList.listIterator();
                while (listIterator.hasNext() && list.size() < i) {
                    T next = listIterator.next();
                    boolean z = false;
                    if (t == null) {
                        if (next.getDelay(j) <= 0) {
                            z = true;
                            t = next;
                        }
                    } else if (!t.canMergeWith(next)) {
                        break;
                    } else if (next.getDelay(j) <= 0) {
                        z = true;
                    }
                    if (z) {
                        list.add(next);
                        listIterator.remove();
                        this.mMap.remove(next.getKey());
                    } else if (t != null) {
                        break;
                    }
                }
                if (!list.isEmpty()) {
                    return;
                }
            }
        }
    }

    public long getMinDelay(long j) {
        long j2 = Long.MAX_VALUE;
        for (T t : this.mMap.values()) {
            long delay = t.getDelay(j);
            if (j2 > delay) {
                j2 = delay;
            }
        }
        return j2;
    }

    public boolean hasDelayedItem() {
        long uptimeMillis = SystemClock.uptimeMillis();
        Iterator<T> it = this.mMap.values().iterator();
        while (it.hasNext()) {
            if (it.next().getDelay(uptimeMillis) > 0) {
                return true;
            }
        }
        return false;
    }

    public int putAtFrist(List<T> list, long j, OnQueueChangedListener<T> onQueueChangedListener) {
        ListIterator<T> listIterator = list.listIterator(list.size());
        int i = 0;
        while (listIterator.hasPrevious()) {
            i += putInternal(listIterator.previous(), true, j, onQueueChangedListener) ? 1 : 0;
        }
        return i;
    }

    public int putAtLast(List<T> list, long j, OnQueueChangedListener<T> onQueueChangedListener) {
        int i = 0;
        ListIterator<T> listIterator = list.listIterator(0);
        while (listIterator.hasNext()) {
            i += putInternal(listIterator.next(), false, j, onQueueChangedListener) ? 1 : 0;
        }
        return i;
    }

    public final boolean putInternal(T t, boolean z, long j, OnQueueChangedListener<T> onQueueChangedListener) {
        if (!needAdd(t, j, onQueueChangedListener)) {
            return false;
        }
        if (onQueueChangedListener != null) {
            onQueueChangedListener.onAdd(t);
        }
        this.mMap.put(t.getKey(), t);
        int priority = t.getPriority();
        LinkedList<T>[] linkedListArr = this.mLists;
        if (linkedListArr[priority] == null) {
            linkedListArr[priority] = new LinkedList<>();
        }
        if (z) {
            this.mLists[priority].addFirst(t);
            if (this.mMaxSize <= 0 || size() <= this.mMaxSize) {
                return true;
            }
            this.mMap.remove(this.mLists[priority].removeLast().getKey());
            return true;
        }
        this.mLists[priority].addLast(t);
        return true;
    }

    public final boolean needAdd(T t, long j, OnQueueChangedListener<T> onQueueChangedListener) {
        T t2 = this.mMap.get(t.getKey());
        if (t2 == null) {
            return true;
        }
        if (t2.getDelay(j) <= t.getDelay(j) && t2.getPriority() >= t.getPriority() && !t2.needProcess()) {
            return false;
        }
        if (onQueueChangedListener != null) {
            onQueueChangedListener.onRemove(t2);
        }
        remove((CommandQueue<T>) t2);
        return true;
    }

    public final void remove(T t) {
        this.mMap.remove(t.getKey());
        LinkedList<T> linkedList = this.mLists[t.getPriority()];
        if (linkedList != null) {
            linkedList.remove(t);
        }
    }
}
