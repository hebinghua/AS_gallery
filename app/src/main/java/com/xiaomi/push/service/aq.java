package com.xiaomi.push.service;

import com.xiaomi.push.fj;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public final class aq implements Runnable {
    public final /* synthetic */ List a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ boolean f886a;

    public aq(List list, boolean z) {
        this.a = list;
        this.f886a = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean b;
        int i;
        boolean b2;
        b = ap.b("www.baidu.com:80");
        Iterator it = this.a.iterator();
        while (true) {
            i = 1;
            if (!it.hasNext()) {
                break;
            }
            String str = (String) it.next();
            if (!b) {
                b2 = ap.b(str);
                if (!b2) {
                    b = false;
                    if (!b && !this.f886a) {
                        break;
                    }
                }
            }
            b = true;
            if (!b) {
            }
        }
        if (!b) {
            i = 2;
        }
        fj.a(i);
    }
}
