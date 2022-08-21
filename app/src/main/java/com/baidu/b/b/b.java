package com.baidu.b.b;

import java.util.Comparator;

/* loaded from: classes.dex */
final class b implements Comparator<a> {
    @Override // java.util.Comparator
    /* renamed from: a */
    public int compare(a aVar, a aVar2) {
        int i = ((aVar.b() - aVar2.b()) > 0L ? 1 : ((aVar.b() - aVar2.b()) == 0L ? 0 : -1));
        return i != 0 ? i > 0 ? -1 : 1 : aVar.a().compareTo(aVar2.a());
    }
}
