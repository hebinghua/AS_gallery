package com.nexstreaming.app.common.util;

import java.util.Collection;
import java.util.Iterator;

/* compiled from: Ints.java */
/* loaded from: classes3.dex */
public final class f {
    public static final int[] a(Collection<Integer> collection) {
        if (collection == null) {
            return new int[0];
        }
        int[] iArr = new int[collection.size()];
        Iterator<Integer> it = collection.iterator();
        int i = 0;
        while (it.hasNext()) {
            Integer next = it.next();
            int i2 = i + 1;
            iArr[i] = next == null ? 0 : next.intValue();
            i = i2;
        }
        return iArr;
    }
}
