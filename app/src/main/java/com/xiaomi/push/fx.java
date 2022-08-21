package com.xiaomi.push;

import java.util.Map;

/* loaded from: classes3.dex */
public class fx implements Cloneable {
    public static final String b = ah.a;
    public static String c;
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public ga f403a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f404a = fw.f388a;

    /* renamed from: b  reason: collision with other field name */
    public boolean f405b = true;
    public String d;
    public String e;
    public String f;

    public fx(Map<String, Integer> map, int i, String str, ga gaVar) {
        a(map, i, str, gaVar);
    }

    public static final String a() {
        String str = c;
        return str != null ? str : ae.m1932a() ? "sandbox.xmpush.xiaomi.com" : ae.b() ? b : "app.chat.xiaomi.net";
    }

    public static final void a(String str) {
        if (!m.m2400a(v.m2551a()) || !ae.b()) {
            c = str;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public int mo2185a() {
        return this.a;
    }

    public final void a(Map<String, Integer> map, int i, String str, ga gaVar) {
        this.a = i;
        this.d = str;
        this.f403a = gaVar;
    }

    public void a(boolean z) {
        this.f404a = z;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2186a() {
        return this.f404a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m2187a() {
        throw null;
    }

    public String b() {
        return this.f;
    }

    public void b(String str) {
        this.f = str;
    }

    public String c() {
        if (this.e == null) {
            this.e = a();
        }
        return this.e;
    }

    public void c(String str) {
        this.e = str;
    }
}
