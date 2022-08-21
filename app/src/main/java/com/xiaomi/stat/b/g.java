package com.xiaomi.stat.b;

import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.text.TextUtils;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.r;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class g {
    private static final String a = "RDM";
    private static final Object b = new Object();
    private static final String c = "CN";
    private static final String d = "INTL";
    private static final String e = "IN";
    private static final String f = "data.mistat.xiaomi.com";
    private static final String g = "data.mistat.intl.xiaomi.com";
    private static final String h = "data.mistat.india.xiaomi.com";
    private static final String i = "region-url";
    private static final String j = "/map_domain";
    private static final String k = "region";
    private static HashMap<String, String> l;
    private static g r;
    private String m = c;
    private String n = f;
    private String o = null;
    private String p;
    private h q;

    static {
        HashMap<String, String> hashMap = new HashMap<>();
        l = hashMap;
        hashMap.put(c, f);
        l.put(d, g);
        l.put(e, h);
    }

    private g() {
        a(ak.a());
    }

    public static g a() {
        if (r == null) {
            synchronized (b) {
                if (r == null) {
                    r = new g();
                }
            }
        }
        return r;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone instance of this class");
    }

    private static void e() {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            k.d(a, "can not init in main thread!", null);
        }
    }

    public void a(Context context) {
        e();
        this.q = new h();
        this.p = context.getFilesDir().getPath();
        if (!com.xiaomi.stat.b.e()) {
            this.m = c;
            this.n = f;
        } else {
            String g2 = m.g();
            k.b(a, "[SystemRegion]:" + g2);
            String a2 = ab.a().a("region", (String) null);
            if (!TextUtils.isEmpty(g2)) {
                this.m = g2;
            }
            if (!TextUtils.isEmpty(a2)) {
                this.m = a2;
            }
            f();
        }
        k.b(a, "[file-dir]:" + this.p + "\n[CurrentRegion]:" + this.m + "\n[domain]:" + this.n);
    }

    private void f() {
        HashMap<String, String> a2 = this.q.a(l, (HashMap) com.xiaomi.stat.d.h.a(this.p.concat(j)));
        l = a2;
        String str = a2.get(this.m);
        if (!TextUtils.isEmpty(str)) {
            this.n = str;
        } else if (!com.xiaomi.stat.b.e()) {
        } else {
            this.m = d;
            this.n = l.get(d);
        }
    }

    public String b() {
        return b(h.b);
    }

    public String c() {
        return b(h.c);
    }

    public String d() {
        return b(h.d);
    }

    private String b(String str) {
        int i2 = ak.a().getApplicationInfo().targetSdkVersion;
        boolean e2 = com.xiaomi.stat.b.e();
        String str2 = h.f;
        if (!e2 && (Build.VERSION.SDK_INT < 28 || i2 < 28)) {
            str2 = h.e;
        }
        return str2.concat(this.n).concat(h.g).concat(str);
    }

    public void a(String str) {
        d(str);
        c(str);
    }

    public void a(boolean z) {
        if (z) {
            this.m = d;
            this.n = g;
            String str = TextUtils.isEmpty(this.o) ? this.m : this.o;
            if (TextUtils.isEmpty(str)) {
                return;
            }
            String str2 = l.get(str);
            if (TextUtils.isEmpty(str2)) {
                return;
            }
            this.m = str;
            this.n = str2;
            return;
        }
        this.m = c;
        this.n = f;
    }

    private boolean c(String str) {
        boolean z;
        if (l.keySet().contains(str)) {
            this.m = str;
            this.n = l.get(str);
            z = true;
        } else {
            this.m = d;
            this.n = l.get(d);
            k.d(a, "unknown region,set to unknown(singapore)'s domain");
            z = false;
        }
        ab.a().b("region", str);
        return z;
    }

    private void d(String str) {
        this.o = str;
    }

    public void a(JSONObject jSONObject) {
        HashMap<String, String> a2 = this.q.a(i, jSONObject);
        String str = TextUtils.isEmpty(this.o) ? this.m : this.o;
        if (a2 != null) {
            l = this.q.a(l, a2);
            if (!TextUtils.isEmpty(str)) {
                String str2 = l.get(str);
                if (!TextUtils.isEmpty(str2)) {
                    this.m = str;
                    this.n = str2;
                }
            } else if (com.xiaomi.stat.b.e()) {
                this.m = d;
                this.n = l.get(d);
            }
            com.xiaomi.stat.d.h.a(l, this.p.concat(j));
        }
    }

    public boolean b(Context context) {
        return r.b(ab.a().a(h.a, 0L)) || !com.xiaomi.stat.b.e();
    }
}
