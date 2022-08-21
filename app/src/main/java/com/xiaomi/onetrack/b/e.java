package com.xiaomi.onetrack.b;

/* loaded from: classes3.dex */
public class e implements Runnable {
    public final /* synthetic */ com.xiaomi.onetrack.e.b a;
    public final /* synthetic */ b b;

    public e(b bVar, com.xiaomi.onetrack.e.b bVar2) {
        this.b = bVar;
        this.a = bVar2;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.b.b(this.a);
            com.xiaomi.onetrack.util.p.a("EventManager", "addEvent: " + this.a.d() + "data:" + this.a.f().toString());
            p.a().a(this.a.e(), false);
        } catch (Exception e) {
            com.xiaomi.onetrack.util.p.b("EventManager", "EventManager.addEvent exception: ", e);
        }
    }
}
