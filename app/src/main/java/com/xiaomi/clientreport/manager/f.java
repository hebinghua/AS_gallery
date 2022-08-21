package com.xiaomi.clientreport.manager;

import com.xiaomi.push.al;
import java.util.concurrent.ExecutorService;

/* loaded from: classes3.dex */
public class f extends al.a {
    public final /* synthetic */ a a;

    public f(a aVar) {
        this.a = aVar;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "100889";
    }

    @Override // java.lang.Runnable
    public void run() {
        int b;
        ExecutorService executorService;
        b = this.a.b();
        if (b > 0) {
            executorService = this.a.f15a;
            executorService.execute(new g(this));
        }
    }
}
