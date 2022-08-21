package com.xiaomi.push;

import android.content.Context;
import android.os.IBinder;
import com.xiaomi.push.bb;

/* loaded from: classes3.dex */
public class bd implements Runnable {
    public final /* synthetic */ IBinder a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ bb.b f127a;

    public bd(bb.b bVar, IBinder iBinder) {
        this.f127a = bVar;
        this.a = iBinder;
    }

    @Override // java.lang.Runnable
    public void run() {
        Object obj;
        Object obj2;
        Object obj3;
        Object obj4;
        Context context;
        String b;
        Object obj5;
        Object obj6;
        try {
            context = bb.this.f122a;
            String packageName = context.getPackageName();
            b = bb.this.b();
            bb.a aVar = new bb.a();
            aVar.f126a = bb.c.a(this.a, packageName, b, "GUID");
            aVar.b = bb.c.a(this.a, packageName, b, "OUID");
            aVar.c = bb.c.a(this.a, packageName, b, "DUID");
            aVar.d = bb.c.a(this.a, packageName, b, "AUID");
            bb.this.f124a = aVar;
            bb.this.m1965b();
            bb.this.f121a = 2;
            obj5 = bb.this.f125a;
            synchronized (obj5) {
                try {
                    obj6 = bb.this.f125a;
                    obj6.notifyAll();
                } catch (Exception unused) {
                }
            }
        } catch (Exception unused2) {
            bb.this.m1965b();
            bb.this.f121a = 2;
            obj3 = bb.this.f125a;
            synchronized (obj3) {
                try {
                    obj4 = bb.this.f125a;
                    obj4.notifyAll();
                } catch (Exception unused3) {
                }
            }
        } catch (Throwable th) {
            bb.this.m1965b();
            bb.this.f121a = 2;
            obj = bb.this.f125a;
            synchronized (obj) {
                try {
                    obj2 = bb.this.f125a;
                    obj2.notifyAll();
                } catch (Exception unused4) {
                }
                throw th;
            }
        }
    }
}
