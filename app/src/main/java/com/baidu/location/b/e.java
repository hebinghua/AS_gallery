package com.baidu.location.b;

import com.baidu.location.b.d;
import java.util.Comparator;

/* loaded from: classes.dex */
class e implements Comparator<d.c> {
    public final /* synthetic */ d a;

    public e(d dVar) {
        this.a = dVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a */
    public int compare(d.c cVar, d.c cVar2) {
        int i = cVar.b;
        int i2 = cVar2.b;
        if (i > i2) {
            return -1;
        }
        return i == i2 ? 0 : 1;
    }
}
