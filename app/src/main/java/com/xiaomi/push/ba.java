package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import java.util.Map;

/* loaded from: classes3.dex */
public class ba implements au {
    public static volatile ba a;

    /* renamed from: a  reason: collision with other field name */
    public int f119a = az.a;

    /* renamed from: a  reason: collision with other field name */
    public au f120a;

    public ba(Context context) {
        this.f120a = az.a(context);
        com.xiaomi.channel.commonutils.logger.b.m1859a("create id manager is: " + this.f119a);
    }

    public static ba a(Context context) {
        if (a == null) {
            synchronized (ba.class) {
                if (a == null) {
                    a = new ba(context.getApplicationContext());
                }
            }
        }
        return a;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public String mo1967a() {
        return a(this.f120a.mo1967a());
    }

    public final String a(String str) {
        return str == null ? "" : str;
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a  reason: collision with other method in class */
    public void mo1967a() {
    }

    public void a(Map<String, String> map) {
        if (map == null) {
            return;
        }
        String b = b();
        if (!TextUtils.isEmpty(b)) {
            map.put("udid", b);
        }
        String mo1967a = mo1967a();
        if (!TextUtils.isEmpty(mo1967a)) {
            map.put("oaid", mo1967a);
        }
        String c = c();
        if (!TextUtils.isEmpty(c)) {
            map.put("vaid", c);
        }
        String d = d();
        if (!TextUtils.isEmpty(d)) {
            map.put("aaid", d);
        }
        map.put("oaid_type", String.valueOf(this.f119a));
    }

    @Override // com.xiaomi.push.au
    /* renamed from: a */
    public boolean mo1967a() {
        return this.f120a.a();
    }

    public String b() {
        return null;
    }

    public String c() {
        return null;
    }

    public String d() {
        return null;
    }
}
