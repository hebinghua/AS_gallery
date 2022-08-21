package com.xiaomi.clientreport.manager;

import com.xiaomi.push.al;
import java.util.concurrent.ExecutorService;

/* loaded from: classes3.dex */
public class d extends al.a {
    public final /* synthetic */ a a;

    public d(a aVar) {
        this.a = aVar;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "100888";
    }

    @Override // java.lang.Runnable
    public void run() {
        int a;
        ExecutorService executorService;
        a = this.a.a();
        if (a > 0) {
            executorService = this.a.f15a;
            executorService.execute(new e(this));
        }
    }
}
