package com.xiaomi.push;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes3.dex */
public class cx extends cr {
    public cr a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ cv f185a;
    public final /* synthetic */ cr b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public cx(cv cvVar, String str, cr crVar) {
        super(str);
        this.f185a = cvVar;
        this.b = crVar;
        this.a = crVar;
        ((cr) this).f171b = ((cr) this).f171b;
        if (crVar != null) {
            this.f = crVar.f;
        }
    }

    @Override // com.xiaomi.push.cr
    public synchronized ArrayList<String> a(boolean z) {
        ArrayList<String> arrayList;
        arrayList = new ArrayList<>();
        cr crVar = this.a;
        if (crVar != null) {
            arrayList.addAll(crVar.a(true));
        }
        Map<String, cr> map = cv.b;
        synchronized (map) {
            cr crVar2 = map.get(((cr) this).f171b);
            if (crVar2 != null) {
                Iterator<String> it = crVar2.a(true).iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    if (arrayList.indexOf(next) == -1) {
                        arrayList.add(next);
                    }
                }
                arrayList.remove(((cr) this).f171b);
                arrayList.add(((cr) this).f171b);
            }
        }
        return arrayList;
    }

    @Override // com.xiaomi.push.cr
    public synchronized void a(String str, cq cqVar) {
        cr crVar = this.a;
        if (crVar != null) {
            crVar.a(str, cqVar);
        }
    }

    @Override // com.xiaomi.push.cr
    public boolean b() {
        return false;
    }
}
