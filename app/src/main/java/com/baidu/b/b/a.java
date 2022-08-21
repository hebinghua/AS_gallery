package com.baidu.b.b;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import com.baidu.b.e.a;
import com.baidu.b.h;
import java.util.Comparator;
import org.json.JSONObject;

/* loaded from: classes.dex */
public abstract class a {
    public static Comparator<a> c = new com.baidu.b.b.b();
    public C0002a a;
    public a.C0004a b;
    private final String d;
    private long e;

    /* renamed from: com.baidu.b.b.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0002a {
        public Context a;
        public com.baidu.b.e.a b;
    }

    /* loaded from: classes.dex */
    public static abstract class b {
        private a.C0004a a;
        private String b;
        private String c;
        private boolean d = true;

        public b(a.C0004a c0004a, String str) {
            this.a = c0004a;
            this.b = str;
            this.c = "target-pkg-" + Base64.encodeToString(str.getBytes(), 3);
        }

        public abstract void a(JSONObject jSONObject);

        public void a(boolean z) {
            this.d = z;
        }

        public boolean a() {
            String a = this.a.a(this.c, true);
            if (!TextUtils.isEmpty(a)) {
                try {
                    a(new JSONObject(a));
                    a(false);
                    return true;
                } catch (Exception unused) {
                }
            }
            return false;
        }

        public abstract void b(JSONObject jSONObject);

        public boolean b() {
            if (this.d) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    b(jSONObject);
                    this.a.a(this.c, jSONObject.toString(), true);
                    a(false);
                    return true;
                } catch (Exception unused) {
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class c {
    }

    /* loaded from: classes.dex */
    public static class d {
        public boolean a;
    }

    /* loaded from: classes.dex */
    public static class e {
        public h.a a;
        public int b;
        public Exception c;

        public e(int i, h.a aVar, Exception exc) {
            this.b = i;
            this.a = aVar;
            this.c = exc;
        }

        public static e a(int i) {
            return new e(i, null, null);
        }

        public static e a(h.a aVar) {
            return new e(0, aVar, null);
        }

        public static e b() {
            return new e(-1, null, null);
        }

        public boolean a() {
            return this.b == 0;
        }
    }

    public a(String str, long j) {
        this.d = str;
        this.e = j;
    }

    public abstract e a(String str, d dVar);

    public String a() {
        return this.d;
    }

    public final void a(C0002a c0002a) {
        this.a = c0002a;
        this.b = c0002a.b.b().a(com.xiaomi.stat.d.v);
    }

    public abstract void a(c cVar);

    public long b() {
        return this.e;
    }
}
