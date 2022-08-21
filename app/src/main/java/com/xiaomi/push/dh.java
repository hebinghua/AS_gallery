package com.xiaomi.push;

import com.xiaomi.push.ao;
import com.xiaomi.push.df;

/* loaded from: classes3.dex */
public class dh extends ao.b {
    public ao.b a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ df f207a;

    public dh(df dfVar) {
        this.f207a = dfVar;
    }

    @Override // com.xiaomi.push.ao.b
    /* renamed from: b */
    public void mo2039b() {
        df.b bVar = (df.b) this.f207a.f195a.peek();
        if (bVar == null || !bVar.a()) {
            return;
        }
        if (this.f207a.f195a.remove(bVar)) {
            this.a = bVar;
        }
        ao.b bVar2 = this.a;
        if (bVar2 == null) {
            return;
        }
        bVar2.mo2039b();
    }

    @Override // com.xiaomi.push.ao.b
    /* renamed from: c */
    public void mo2040c() {
        ao.b bVar = this.a;
        if (bVar != null) {
            bVar.mo2040c();
        }
    }
}
