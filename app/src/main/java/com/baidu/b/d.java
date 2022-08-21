package com.baidu.b;

import java.util.Comparator;

/* loaded from: classes.dex */
class d implements Comparator<b> {
    public final /* synthetic */ c a;

    public d(c cVar) {
        this.a = cVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a */
    public int compare(b bVar, b bVar2) {
        int i = bVar2.b - bVar.b;
        if (i == 0) {
            boolean z = bVar.d;
            if (z && bVar2.d) {
                return 0;
            }
            if (z) {
                return -1;
            }
            if (bVar2.d) {
                return 1;
            }
        }
        return i;
    }
}
