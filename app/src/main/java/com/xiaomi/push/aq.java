package com.xiaomi.push;

import com.xiaomi.push.ao;

/* loaded from: classes3.dex */
public class aq implements Runnable {
    public final /* synthetic */ ao.b a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ ao f97a;

    public aq(ao aoVar, ao.b bVar) {
        this.f97a = aoVar;
        this.a = bVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f97a.a(this.a);
    }
}
