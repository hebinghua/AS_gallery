package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.xiaomi.push.al;
import com.xiaomi.push.cj;

/* loaded from: classes3.dex */
public class bx {
    public static volatile bx a;

    /* renamed from: a  reason: collision with other field name */
    public Context f139a;

    /* renamed from: a  reason: collision with other field name */
    public cm f141a;

    /* renamed from: a  reason: collision with other field name */
    public cn f142a;
    public String e;
    public String f;

    /* renamed from: a  reason: collision with other field name */
    public final String f143a = "push_stat_sp";

    /* renamed from: b  reason: collision with other field name */
    public final String f144b = "upload_time";

    /* renamed from: c  reason: collision with other field name */
    public final String f145c = "delete_time";
    public final String d = "check_time";

    /* renamed from: a  reason: collision with other field name */
    public al.a f140a = new by(this);
    public al.a b = new bz(this);
    public al.a c = new ca(this);

    public bx(Context context) {
        this.f139a = context;
    }

    public static bx a(Context context) {
        if (a == null) {
            synchronized (bx.class) {
                if (a == null) {
                    a = new bx(context);
                }
            }
        }
        return a;
    }

    public String a() {
        return this.e;
    }

    public void a(cj.a aVar) {
        cj.a(this.f139a).a(aVar);
    }

    public void a(hn hnVar) {
        if (m1989a() && com.xiaomi.push.service.bz.a(hnVar.e())) {
            a(cg.a(this.f139a, c(), hnVar));
        }
    }

    public void a(String str) {
        if (m1989a() && !TextUtils.isEmpty(str)) {
            a(co.a(this.f139a, str));
        }
    }

    public void a(String str, String str2, Boolean bool) {
        if (this.f141a != null) {
            if (bool.booleanValue()) {
                this.f141a.a(this.f139a, str2, str);
            } else {
                this.f141a.b(this.f139a, str2, str);
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m1989a() {
        return com.xiaomi.push.service.ba.a(this.f139a).a(ho.StatDataSwitch.a(), true);
    }

    public String b() {
        return this.f;
    }

    public final void b(String str) {
        SharedPreferences.Editor edit = this.f139a.getSharedPreferences("push_stat_sp", 0).edit();
        edit.putLong(str, System.currentTimeMillis());
        t.a(edit);
    }

    public final String c() {
        return this.f139a.getDatabasePath(cb.f148a).getAbsolutePath();
    }
}
