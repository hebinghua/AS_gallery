package com.baidu.a.a.a.a;

import android.content.Context;
import com.baidu.b.c;
import com.baidu.b.f;
import com.baidu.b.g;
import com.baidu.b.h;

/* loaded from: classes.dex */
public final class a {
    public static boolean a = true;
    private static volatile a e;
    private final Context b;
    private h c;
    private g d;
    private c f;
    private h.a g;
    private h.a h;
    private long i;

    private a(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.b = applicationContext;
        this.f = new c();
        this.c = new h(applicationContext, new com.baidu.b.e.a(applicationContext), this.f);
        this.d = new g(applicationContext, this.f);
    }

    private h.a a() {
        h.a aVar = this.h;
        if (aVar != null) {
            return aVar;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - this.i) > 3600000) {
            this.h = b();
            this.i = currentTimeMillis;
        }
        h.a aVar2 = this.h;
        if (aVar2 != null) {
            return aVar2;
        }
        if (this.g == null) {
            this.h = c(null);
        }
        return this.h;
    }

    private h.a a(String str) {
        h.a a2 = this.c.a();
        return a2 == null ? b(str) : a2;
    }

    public static String a(Context context) {
        String b;
        synchronized (a.class) {
            b = b(context).a().b();
        }
        return b;
    }

    public static a b(Context context) {
        a aVar;
        synchronized (f.class) {
            if (e == null) {
                e = new a(context);
            }
            aVar = e;
        }
        return aVar;
    }

    private h.a b() {
        return a((String) null);
    }

    private h.a b(String str) {
        f a2 = this.d.a(str);
        if (a2 != null) {
            return this.c.a(a2);
        }
        return null;
    }

    private h.a c(String str) {
        return this.c.b(str);
    }
}
