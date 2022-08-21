package com.xiaomi.push.service;

import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public final class am extends al.a {
    public final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ ax f877a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f878a;

    public am(String str, ax axVar, int i) {
        this.f878a = str;
        this.f877a = axVar;
        this.a = i;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return this.f878a;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f877a.a(this.a);
    }
}
