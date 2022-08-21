package com.xiaomi.stat.a;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class d implements Runnable {
    public final /* synthetic */ l a;
    public final /* synthetic */ c b;

    public d(c cVar, l lVar) {
        this.b = cVar;
        this.a = lVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            this.b.b(this.a);
        } catch (Exception e) {
            com.xiaomi.stat.d.k.e("EventManager", "addEvent exception: " + e.toString());
        }
    }
}
