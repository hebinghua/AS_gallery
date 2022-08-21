package com.xiaomi.push;

import com.xiaomi.push.al;
import java.util.Map;

/* loaded from: classes3.dex */
public class an extends al.b {
    public final /* synthetic */ al a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public an(al alVar, al.a aVar) {
        super(aVar);
        this.a = alVar;
    }

    @Override // com.xiaomi.push.al.b
    public void b() {
        Object obj;
        Map map;
        obj = this.a.f87a;
        synchronized (obj) {
            map = this.a.f88a;
            map.remove(super.a.mo2050a());
        }
    }
}
