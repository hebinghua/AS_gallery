package com.miui.gallery.storage.utils;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes2.dex */
public class StrategyOrder implements Iterable<Integer> {
    public static final Map<String, StrategyOrder> sStrategyOrders = new Hashtable();
    public final int[] mOrder;

    static {
        register("camera_first", new StrategyOrder(new int[]{3}));
    }

    public StrategyOrder(int[] iArr) {
        this.mOrder = iArr;
    }

    public static void register(String str, StrategyOrder strategyOrder) {
        sStrategyOrders.put(str, strategyOrder);
    }

    public static StrategyOrder get(String str) {
        return sStrategyOrders.get(str);
    }

    @Override // java.lang.Iterable
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() { // from class: com.miui.gallery.storage.utils.StrategyOrder.1
            public int index = -1;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.index + 1 < StrategyOrder.this.mOrder.length;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            /* renamed from: next */
            public Integer mo1413next() {
                int[] iArr = StrategyOrder.this.mOrder;
                int i = this.index + 1;
                this.index = i;
                return Integer.valueOf(iArr[i]);
            }
        };
    }
}
