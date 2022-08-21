package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class b {
    public static volatile b a;

    /* renamed from: a  reason: collision with other field name */
    public Context f59a;

    /* renamed from: a  reason: collision with other field name */
    public a f60a;

    /* renamed from: a  reason: collision with other field name */
    public String f61a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, a> f62a;

    /* loaded from: classes3.dex */
    public static class a {

        /* renamed from: a  reason: collision with other field name */
        public Context f63a;

        /* renamed from: a  reason: collision with other field name */
        public String f64a;
        public String b;
        public String c;
        public String d;
        public String e;
        public String f;
        public String g;
        public String h;

        /* renamed from: a  reason: collision with other field name */
        public boolean f65a = true;

        /* renamed from: b  reason: collision with other field name */
        public boolean f66b = false;
        public int a = 1;

        public a(Context context) {
            this.f63a = context;
        }

        public static String a(a aVar) {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("appId", aVar.f64a);
                jSONObject.put("appToken", aVar.b);
                jSONObject.put("regId", aVar.c);
                jSONObject.put("regSec", aVar.d);
                jSONObject.put("devId", aVar.f);
                jSONObject.put("vName", aVar.e);
                jSONObject.put("valid", aVar.f65a);
                jSONObject.put("paused", aVar.f66b);
                jSONObject.put("envType", aVar.a);
                jSONObject.put("regResource", aVar.g);
                return jSONObject.toString();
            } catch (Throwable th) {
                com.xiaomi.channel.commonutils.logger.b.a(th);
                return null;
            }
        }

        public final String a() {
            Context context = this.f63a;
            return com.xiaomi.push.h.m2213a(context, context.getPackageName());
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m1916a() {
            b.a(this.f63a).edit().clear().commit();
            this.f64a = null;
            this.b = null;
            this.c = null;
            this.d = null;
            this.f = null;
            this.e = null;
            this.f65a = false;
            this.f66b = false;
            this.h = null;
            this.a = 1;
        }

        public void a(int i) {
            this.a = i;
        }

        public void a(String str, String str2) {
            this.c = str;
            this.d = str2;
            this.f = com.xiaomi.push.j.j(this.f63a);
            this.e = a();
            this.f65a = true;
        }

        public void a(String str, String str2, String str3) {
            this.f64a = str;
            this.b = str2;
            this.g = str3;
            SharedPreferences.Editor edit = b.a(this.f63a).edit();
            edit.putString("appId", this.f64a);
            edit.putString("appToken", str2);
            edit.putString("regResource", str3);
            edit.commit();
        }

        public void a(boolean z) {
            this.f66b = z;
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m1917a() {
            return m1918a(this.f64a, this.b);
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m1918a(String str, String str2) {
            boolean equals = TextUtils.equals(this.f64a, str);
            boolean equals2 = TextUtils.equals(this.b, str2);
            boolean z = !TextUtils.isEmpty(this.c);
            boolean z2 = !TextUtils.isEmpty(this.d);
            boolean z3 = TextUtils.equals(this.f, com.xiaomi.push.j.j(this.f63a)) || TextUtils.equals(this.f, com.xiaomi.push.j.i(this.f63a));
            boolean z4 = equals && equals2 && z && z2 && z3;
            if (!z4) {
                com.xiaomi.channel.commonutils.logger.b.e(String.format("register invalid, aid=%s;atn=%s;rid=%s;rse=%s;did=%s", Boolean.valueOf(equals), Boolean.valueOf(equals2), Boolean.valueOf(z), Boolean.valueOf(z2), Boolean.valueOf(z3)));
            }
            return z4;
        }

        public void b() {
            this.f65a = false;
            b.a(this.f63a).edit().putBoolean("valid", this.f65a).commit();
        }

        public void b(String str, String str2, String str3) {
            this.c = str;
            this.d = str2;
            this.f = com.xiaomi.push.j.j(this.f63a);
            this.e = a();
            this.f65a = true;
            this.h = str3;
            SharedPreferences.Editor edit = b.a(this.f63a).edit();
            edit.putString("regId", str);
            edit.putString("regSec", str2);
            edit.putString("devId", this.f);
            edit.putString("vName", a());
            edit.putBoolean("valid", true);
            edit.putString("appRegion", str3);
            edit.commit();
        }
    }

    public b(Context context) {
        this.f59a = context;
        m1912c();
    }

    public static SharedPreferences a(Context context) {
        return context.getSharedPreferences("mipush", 0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static b m1906a(Context context) {
        if (a == null) {
            synchronized (b.class) {
                if (a == null) {
                    a = new b(context);
                }
            }
        }
        return a;
    }

    public int a() {
        return this.f60a.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m1907a() {
        return this.f60a.f64a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1908a() {
        this.f60a.m1916a();
    }

    public void a(int i) {
        this.f60a.a(i);
        a(this.f59a).edit().putInt("envType", i).commit();
    }

    public void a(String str) {
        SharedPreferences.Editor edit = a(this.f59a).edit();
        edit.putString("vName", str);
        edit.commit();
        this.f60a.e = str;
    }

    public void a(String str, a aVar) {
        this.f62a.put(str, aVar);
        String a2 = a.a(aVar);
        a(this.f59a).edit().putString("hybrid_app_info_" + str, a2).commit();
    }

    public void a(String str, String str2, String str3) {
        this.f60a.a(str, str2, str3);
    }

    public void a(boolean z) {
        this.f60a.a(z);
        a(this.f59a).edit().putBoolean("paused", z).commit();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m1909a() {
        Context context = this.f59a;
        return !TextUtils.equals(com.xiaomi.push.h.m2213a(context, context.getPackageName()), this.f60a.e);
    }

    public boolean a(String str, String str2) {
        return this.f60a.m1918a(str, str2);
    }

    public String b() {
        return this.f60a.b;
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m1910b() {
        this.f60a.b();
    }

    public void b(String str, String str2, String str3) {
        this.f60a.b(str, str2, str3);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m1911b() {
        if (!this.f60a.m1917a()) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Don't send message before initialization succeeded!");
            return false;
        }
        return true;
    }

    public String c() {
        return this.f60a.c;
    }

    /* renamed from: c  reason: collision with other method in class */
    public final void m1912c() {
        this.f60a = new a(this.f59a);
        this.f62a = new HashMap();
        SharedPreferences a2 = a(this.f59a);
        this.f60a.f64a = a2.getString("appId", null);
        this.f60a.b = a2.getString("appToken", null);
        this.f60a.c = a2.getString("regId", null);
        this.f60a.d = a2.getString("regSec", null);
        this.f60a.f = a2.getString("devId", null);
        if (!TextUtils.isEmpty(this.f60a.f) && com.xiaomi.push.j.a(this.f60a.f)) {
            this.f60a.f = com.xiaomi.push.j.j(this.f59a);
            a2.edit().putString("devId", this.f60a.f).commit();
        }
        this.f60a.e = a2.getString("vName", null);
        this.f60a.f65a = a2.getBoolean("valid", true);
        this.f60a.f66b = a2.getBoolean("paused", false);
        this.f60a.a = a2.getInt("envType", 1);
        this.f60a.g = a2.getString("regResource", null);
        this.f60a.h = a2.getString("appRegion", null);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m1913c() {
        return this.f60a.m1917a();
    }

    public String d() {
        return this.f60a.d;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m1914d() {
        return !TextUtils.isEmpty(this.f60a.f64a) && !TextUtils.isEmpty(this.f60a.b) && !TextUtils.isEmpty(this.f60a.c) && !TextUtils.isEmpty(this.f60a.d);
    }

    public String e() {
        return this.f60a.g;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m1915e() {
        return this.f60a.f66b;
    }

    public boolean f() {
        return !this.f60a.f65a;
    }
}
