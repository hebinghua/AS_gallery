package com.xiaomi.onetrack.util.oaid;

import android.content.Context;
import com.xiaomi.onetrack.util.n;
import com.xiaomi.onetrack.util.oaid.helpers.b;
import com.xiaomi.onetrack.util.oaid.helpers.g;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.q;
import com.xiaomi.onetrack.util.w;

/* loaded from: classes3.dex */
public class a {
    public static final String a = "a";
    public static volatile a b;
    public volatile boolean c = false;
    public volatile String d = "";

    public static a a() {
        if (b == null) {
            synchronized (a.class) {
                if (b == null) {
                    b = new a();
                }
            }
        }
        return b;
    }

    public String a(Context context) {
        synchronized (this.d) {
            if (w.a()) {
                if (p.a) {
                    throw new IllegalStateException("Don't use it on the main thread");
                }
                p.b(a, "getOaid() throw exception : Don't use it on the main thread");
                return "";
            } else if (this.d != null && !this.d.equals("")) {
                return this.d;
            } else if (this.c) {
                return this.d;
            } else if (q.a()) {
                this.d = n.b(context);
                return this.d;
            } else {
                String a2 = new g().a(context);
                if (a2 != null && !a2.equals("")) {
                    this.d = a2;
                    return a2;
                }
                String a3 = new b().a(context);
                if (a3 != null && !a3.equals("")) {
                    this.d = a3;
                    return a3;
                }
                this.c = true;
                return this.d;
            }
        }
    }
}
