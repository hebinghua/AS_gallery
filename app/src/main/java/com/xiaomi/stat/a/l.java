package com.xiaomi.stat.a;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.stat.HttpEvent;
import com.xiaomi.stat.MiStatParams;
import com.xiaomi.stat.NetAvailableEvent;
import com.xiaomi.stat.aj;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.p;
import com.xiaomi.stat.d.r;
import java.io.PrintWriter;
import java.io.StringWriter;

/* loaded from: classes3.dex */
public class l {
    public String a;
    public String b;
    public String c;
    public aj d;
    public long e;
    public String f;
    public boolean g;

    /* loaded from: classes3.dex */
    public static class a {
        public static final String A = "pc";
        public static final String B = "sts";
        public static final String C = "ets";
        private static final String D = "mistat_net_available";
        private static final String E = "mistat_pa";
        private static final String F = "mistat_dau";
        private static final String G = "mistat_app_exception";
        private static final String H = "mistat_app_update";
        private static final String I = "mistat_signin";
        private static final String J = "mistat_signout";
        private static final String K = "mistat_net_monitor";
        private static final String L = "mistat_page_monitor";
        private static final String M = "mistat_net_event";
        private static final String N = "mi_sai";
        private static final String O = "track";
        private static final String P = "track_signin";
        private static final String Q = "profile_set";
        private static final String R = "fo";
        private static final String S = "ia";
        private static final String T = "i1";
        private static final String U = "ib";
        private static final String V = "i2";
        private static final String W = "md";
        private static final String X = "ms";
        private static final String Y = "ii";
        private static final String Z = "mcm";
        public static final int a = 30;
        private static final String aA = "ec";
        private static final String aB = "rt";
        private static final String aC = "rst";
        private static final String aD = "n";
        private static final String aE = "ns";
        private static final String aF = "rc";
        private static final String aG = "ext";
        private static final String aa = "mcs";
        private static final String ab = "bm";
        private static final String ac = "bs";
        private static final String ad = "aa";
        private static final String ae = "ai";
        private static final String af = "od";
        private static final String ag = "oa";
        private static final String ah = "va";
        private static final String ai = "pg";
        private static final String aj = "bt";
        private static final String ak = "et";
        private static final String al = "sk";
        private static final String am = "ek";
        private static final String an = "et";
        private static final String ao = "em";
        private static final String ap = "pvr";
        private static final String aq = "ud";
        private static final String ar = "ur";
        private static final String as = "dt";
        private static final String at = "rc";
        private static final String au = "nf";
        private static final String av = "ecn";
        private static final String aw = "ve";
        private static final String ax = "fg";
        private static final String ay = "rsc";
        private static final String az = "sc";
        public static final String b = "e";
        public static final String c = "eg";
        public static final String d = "tp";
        public static final String e = "ts";
        public static final String f = "ps";
        public static final String g = "eid";
        public static final String h = "mistat_basic";
        public static final String i = "mistat_user_page";
        public static final String j = "mistat_crash";
        public static final String k = "mistat_network";
        public static final String l = "mistat_plain_text";
        public static final String m = "mistat_delete_event";
        public static final String n = "mi_av";
        public static final String o = "mi_sv";
        public static final String p = "mi_ov";
        public static final String q = "mi_ob";
        public static final String r = "mi_n";
        public static final String s = "mi_rd";
        public static final String t = "mi_mf";
        public static final String u = "mi_m";
        public static final String v = "mi_os";
        public static final String w = "profile_";
        public static final String x = "ca";
        public static final String y = "c_";
        public static final String z = "rc";
    }

    private l() {
    }

    public static l a() {
        l lVar = new l();
        lVar.a = "mistat_dau";
        lVar.b = a.h;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        boolean q = com.xiaomi.stat.b.q();
        if (q) {
            com.xiaomi.stat.b.e(false);
        }
        ajVar.putInt(com.xiaomi.stat.d.B, q ? 1 : 0);
        Context a2 = ak.a();
        ajVar.putString(com.xiaomi.stat.d.C, com.xiaomi.stat.d.e.b(a2));
        ajVar.putString(com.xiaomi.stat.d.D, com.xiaomi.stat.d.e.c(a2));
        ajVar.putString(com.xiaomi.stat.d.E, com.xiaomi.stat.d.e.e(a2));
        ajVar.putString(com.xiaomi.stat.d.F, com.xiaomi.stat.d.e.f(a2));
        ajVar.putString(com.xiaomi.stat.d.G, com.xiaomi.stat.d.e.h(a2));
        ajVar.putString(com.xiaomi.stat.d.H, com.xiaomi.stat.d.e.i(a2));
        ajVar.putString("ii", com.xiaomi.stat.d.e.d());
        ajVar.putString(com.xiaomi.stat.d.J, com.xiaomi.stat.d.e.k(a2));
        ajVar.putString(com.xiaomi.stat.d.K, com.xiaomi.stat.d.e.l(a2));
        ajVar.putString(com.xiaomi.stat.d.L, com.xiaomi.stat.d.e.n(a2));
        ajVar.putString("bs", com.xiaomi.stat.d.e.o(a2));
        ajVar.putString(com.xiaomi.stat.d.O, com.xiaomi.stat.d.e.q(a2));
        ajVar.putString("ai", com.xiaomi.stat.d.e.p(a2));
        ajVar.putString(com.xiaomi.stat.d.P, com.xiaomi.stat.d.e.x(a2));
        ajVar.putString("oa", com.xiaomi.stat.b.f.b(a2));
        ajVar.putString("va", com.xiaomi.stat.b.f.c(a2));
        lVar.d = ajVar;
        a(lVar);
        return lVar;
    }

    private static void a(l lVar) {
        if (com.xiaomi.stat.b.e()) {
            lVar.g = true;
        } else {
            lVar.g = false;
        }
    }

    private static void a(l lVar, String str) {
        if (!TextUtils.isEmpty(str)) {
            lVar.f = str;
            lVar.d.putString("mi_sai", str);
        }
    }

    public static l a(String str, long j, long j2) {
        return a(str, j, j2, true, null, null);
    }

    public static l a(String str, long j, long j2, MiStatParams miStatParams, String str2) {
        return a(str, j, j2, false, miStatParams, str2);
    }

    private static l a(String str, long j, long j2, boolean z, MiStatParams miStatParams, String str2) {
        l lVar = new l();
        lVar.a = "mistat_pa";
        lVar.b = z ? a.h : a.i;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj(miStatParams);
        ajVar.putString(com.xiaomi.stat.d.aa, str);
        ajVar.putLong("bt", j);
        ajVar.putLong("et", j2);
        lVar.d = ajVar;
        a(lVar);
        if (!z) {
            a(lVar, str2);
        }
        return lVar;
    }

    public static l a(String str, String str2, MiStatParams miStatParams, String str3, boolean z) {
        l lVar = new l();
        lVar.a = str;
        lVar.b = str2;
        lVar.c = "track";
        lVar.e = r.b();
        lVar.d = new aj(miStatParams);
        if (com.xiaomi.stat.b.e()) {
            lVar.g = !z;
        } else {
            lVar.g = false;
        }
        a(lVar, str3);
        return lVar;
    }

    public static l a(Throwable th, String str, boolean z, String str2) {
        l lVar = new l();
        lVar.a = "mistat_app_exception";
        lVar.b = a.j;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        lVar.d = ajVar;
        ajVar.putString(com.xiaomi.stat.d.ah, str);
        ajVar.putInt("et", z ? 1 : 0);
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        ajVar.putString(com.xiaomi.stat.d.af, stringWriter.toString());
        ajVar.putString(com.xiaomi.stat.d.ag, th.getMessage());
        a(lVar);
        a(lVar, str2);
        return lVar;
    }

    public static l a(MiStatParams miStatParams, boolean z, String str) {
        l lVar = new l();
        lVar.c = "profile_set";
        lVar.e = r.b();
        lVar.d = new aj(miStatParams);
        if (com.xiaomi.stat.b.e()) {
            lVar.g = !z;
        } else {
            lVar.g = false;
        }
        a(lVar, str);
        return lVar;
    }

    public static l a(int i) {
        l lVar = new l();
        lVar.a = "mistat_app_update";
        lVar.b = a.h;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        ajVar.putInt(com.xiaomi.stat.d.S, i);
        lVar.d = ajVar;
        a(lVar);
        return lVar;
    }

    public static l a(String str) {
        l lVar = new l();
        boolean isEmpty = TextUtils.isEmpty(str);
        lVar.a = isEmpty ? "mistat_signout" : "mistat_signin";
        lVar.b = a.h;
        lVar.c = "track_signin";
        lVar.e = r.b();
        aj ajVar = new aj();
        if (!isEmpty) {
            ajVar.putString("ud", str);
        }
        lVar.d = ajVar;
        a(lVar);
        return lVar;
    }

    public static l a(HttpEvent httpEvent, String str) {
        l lVar = new l();
        lVar.a = "mistat_net_monitor";
        lVar.b = a.k;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        ajVar.putString("ur", httpEvent.getUrl());
        ajVar.putLong(com.xiaomi.stat.d.Q, httpEvent.getTimeCost());
        ajVar.putInt("rc", httpEvent.getResponseCode());
        ajVar.putLong("nf", httpEvent.getNetFlow());
        ajVar.putString("ecn", httpEvent.getExceptionName());
        lVar.d = ajVar;
        a(lVar);
        a(lVar, str);
        return lVar;
    }

    public static l a(NetAvailableEvent netAvailableEvent, String str) {
        l lVar = new l();
        lVar.a = "mistat_net_event";
        lVar.b = "mistat_net_available";
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        ajVar.putString("fg", netAvailableEvent.getFlag());
        ajVar.putInt("rsc", netAvailableEvent.getResponseCode());
        ajVar.putInt("sc", netAvailableEvent.getStatusCode());
        ajVar.putString("ec", netAvailableEvent.getException());
        ajVar.putInt("rt", netAvailableEvent.getResultType());
        ajVar.putLong("rst", netAvailableEvent.getRequestStartTime());
        Context a2 = ak.a();
        ajVar.putString("n", a2 != null ? com.xiaomi.stat.d.l.b(a2) : "UNKNOWN");
        ajVar.putString("ns", String.valueOf(a2 != null ? p.l(a2) : 0.0f));
        ajVar.putInt("rc", netAvailableEvent.getRetryCount());
        ajVar.putString("ext", netAvailableEvent.getExt());
        lVar.d = ajVar;
        a(lVar);
        a(lVar, str);
        return lVar;
    }

    public static l a(aj ajVar) {
        l lVar = new l();
        lVar.a = a.m;
        lVar.b = a.h;
        lVar.c = "track";
        lVar.e = r.b();
        lVar.d = ajVar;
        a(lVar);
        return lVar;
    }

    public static l a(int i, int i2, long j, long j2) {
        l lVar = new l();
        lVar.a = "mistat_page_monitor";
        lVar.b = a.h;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        ajVar.putInt("rc", i);
        ajVar.putInt(a.A, i2);
        ajVar.putLong(a.B, j);
        ajVar.putLong(a.C, j2);
        lVar.d = ajVar;
        a(lVar);
        return lVar;
    }

    public static l a(String str, String str2, String str3) {
        l lVar = new l();
        lVar.a = str;
        lVar.b = a.l;
        lVar.c = "track";
        lVar.e = r.b();
        aj ajVar = new aj();
        ajVar.putString("ve", str2);
        lVar.d = ajVar;
        a(lVar);
        a(lVar, str3);
        return lVar;
    }
}
