package com.xiaomi.push;

import android.os.Bundle;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class gl extends gn {
    public a a;

    /* renamed from: a  reason: collision with other field name */
    public final Map<String, String> f417a;

    /* loaded from: classes3.dex */
    public static class a {
        public static final a a = new a(CallMethod.METHOD_GET);
        public static final a b = new a("set");
        public static final a c = new a("result");
        public static final a d = new a("error");
        public static final a e = new a("command");

        /* renamed from: a  reason: collision with other field name */
        public String f418a;

        public a(String str) {
            this.f418a = str;
        }

        public static a a(String str) {
            if (str == null) {
                return null;
            }
            String lowerCase = str.toLowerCase();
            a aVar = a;
            if (aVar.toString().equals(lowerCase)) {
                return aVar;
            }
            a aVar2 = b;
            if (aVar2.toString().equals(lowerCase)) {
                return aVar2;
            }
            a aVar3 = d;
            if (aVar3.toString().equals(lowerCase)) {
                return aVar3;
            }
            a aVar4 = c;
            if (aVar4.toString().equals(lowerCase)) {
                return aVar4;
            }
            a aVar5 = e;
            if (!aVar5.toString().equals(lowerCase)) {
                return null;
            }
            return aVar5;
        }

        public String toString() {
            return this.f418a;
        }
    }

    public gl() {
        this.a = a.a;
        this.f417a = new HashMap();
    }

    public gl(Bundle bundle) {
        super(bundle);
        this.a = a.a;
        this.f417a = new HashMap();
        if (bundle.containsKey("ext_iq_type")) {
            this.a = a.a(bundle.getString("ext_iq_type"));
        }
    }

    @Override // com.xiaomi.push.gn
    /* renamed from: a */
    public Bundle mo2201a() {
        Bundle mo2201a = super.mo2201a();
        a aVar = this.a;
        if (aVar != null) {
            mo2201a.putString("ext_iq_type", aVar.toString());
        }
        return mo2201a;
    }

    @Override // com.xiaomi.push.gn
    /* renamed from: a  reason: collision with other method in class */
    public a mo2201a() {
        return this.a;
    }

    @Override // com.xiaomi.push.gn
    /* renamed from: a */
    public String mo2201a() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("<iq ");
        if (j() != null) {
            sb.append("id=\"" + j() + "\" ");
        }
        if (l() != null) {
            sb.append("to=\"");
            sb.append(gy.a(l()));
            sb.append("\" ");
        }
        if (m() != null) {
            sb.append("from=\"");
            sb.append(gy.a(m()));
            sb.append("\" ");
        }
        if (k() != null) {
            sb.append("chid=\"");
            sb.append(gy.a(k()));
            sb.append("\" ");
        }
        for (Map.Entry<String, String> entry : this.f417a.entrySet()) {
            sb.append(gy.a(entry.getKey()));
            sb.append("=\"");
            sb.append(gy.a(entry.getValue()));
            sb.append("\" ");
        }
        if (this.a == null) {
            str = "type=\"get\">";
        } else {
            sb.append("type=\"");
            sb.append(mo2201a());
            str = "\">";
        }
        sb.append(str);
        String b = b();
        if (b != null) {
            sb.append(b);
        }
        sb.append(o());
        gr a2 = a();
        if (a2 != null) {
            sb.append(a2.m2202a());
        }
        sb.append("</iq>");
        return sb.toString();
    }

    public void a(a aVar) {
        if (aVar == null) {
            aVar = a.a;
        }
        this.a = aVar;
    }

    public synchronized void a(Map<String, String> map) {
        this.f417a.putAll(map);
    }

    @Override // com.xiaomi.push.gn
    public String b() {
        return null;
    }
}
