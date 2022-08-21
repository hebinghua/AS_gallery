package com.baidu.b.b;

import com.baidu.b.b.e;
import java.util.Comparator;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f implements Comparator<e.c.a> {
    public final /* synthetic */ e.c a;

    public f(e.c cVar) {
        this.a = cVar;
    }

    @Override // java.util.Comparator
    /* renamed from: a */
    public int compare(e.c.a aVar, e.c.a aVar2) {
        int i;
        int i2;
        i = aVar.a;
        i2 = aVar2.a;
        return i - i2;
    }
}
