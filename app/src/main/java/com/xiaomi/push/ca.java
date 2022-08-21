package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public class ca extends al.a {
    public final /* synthetic */ bx a;

    public ca(bx bxVar) {
        this.a = bxVar;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "10053";
    }

    @Override // java.lang.Runnable
    public void run() {
        cn cnVar;
        cn cnVar2;
        Context context;
        cnVar = this.a.f142a;
        if (cnVar != null) {
            cnVar2 = this.a.f142a;
            context = this.a.f139a;
            cnVar2.b(context);
            this.a.b("delete_time");
        }
    }
}
