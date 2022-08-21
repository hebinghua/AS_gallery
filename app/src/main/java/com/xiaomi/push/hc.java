package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.ao;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public final class hc extends ao.b {
    public final /* synthetic */ Context a;

    public hc(Context context) {
        this.a = context;
    }

    @Override // com.xiaomi.push.ao.b
    /* renamed from: b */
    public void mo2039b() {
        Object obj;
        ArrayList arrayList;
        List list;
        List list2;
        obj = hb.f439a;
        synchronized (obj) {
            list = hb.f441a;
            arrayList = new ArrayList(list);
            list2 = hb.f441a;
            list2.clear();
        }
        hb.b(this.a, arrayList);
    }
}
