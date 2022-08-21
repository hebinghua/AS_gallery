package com.xiaomi.push;

import android.os.Bundle;
import java.util.Objects;

/* loaded from: classes3.dex */
public class gp extends gn {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public a f425a;

    /* renamed from: a  reason: collision with other field name */
    public b f426a;
    public String b;

    /* loaded from: classes3.dex */
    public enum a {
        chat,
        available,
        away,
        xa,
        dnd
    }

    /* loaded from: classes3.dex */
    public enum b {
        available,
        unavailable,
        subscribe,
        subscribed,
        unsubscribe,
        unsubscribed,
        error,
        probe
    }

    public gp(Bundle bundle) {
        super(bundle);
        this.f426a = b.available;
        this.b = null;
        this.a = Integer.MIN_VALUE;
        this.f425a = null;
        if (bundle.containsKey("ext_pres_type")) {
            this.f426a = b.valueOf(bundle.getString("ext_pres_type"));
        }
        if (bundle.containsKey("ext_pres_status")) {
            this.b = bundle.getString("ext_pres_status");
        }
        if (bundle.containsKey("ext_pres_prio")) {
            this.a = bundle.getInt("ext_pres_prio");
        }
        if (bundle.containsKey("ext_pres_mode")) {
            this.f425a = a.valueOf(bundle.getString("ext_pres_mode"));
        }
    }

    public gp(b bVar) {
        this.f426a = b.available;
        this.b = null;
        this.a = Integer.MIN_VALUE;
        this.f425a = null;
        a(bVar);
    }

    @Override // com.xiaomi.push.gn
    /* renamed from: a */
    public Bundle mo2201a() {
        Bundle mo2201a = super.mo2201a();
        b bVar = this.f426a;
        if (bVar != null) {
            mo2201a.putString("ext_pres_type", bVar.toString());
        }
        String str = this.b;
        if (str != null) {
            mo2201a.putString("ext_pres_status", str);
        }
        int i = this.a;
        if (i != Integer.MIN_VALUE) {
            mo2201a.putInt("ext_pres_prio", i);
        }
        a aVar = this.f425a;
        if (aVar != null && aVar != a.available) {
            mo2201a.putString("ext_pres_mode", aVar.toString());
        }
        return mo2201a;
    }

    @Override // com.xiaomi.push.gn
    /* renamed from: a  reason: collision with other method in class */
    public String mo2201a() {
        StringBuilder sb = new StringBuilder();
        sb.append("<presence");
        if (p() != null) {
            sb.append(" xmlns=\"");
            sb.append(p());
            sb.append("\"");
        }
        if (j() != null) {
            sb.append(" id=\"");
            sb.append(j());
            sb.append("\"");
        }
        if (l() != null) {
            sb.append(" to=\"");
            sb.append(gy.a(l()));
            sb.append("\"");
        }
        if (m() != null) {
            sb.append(" from=\"");
            sb.append(gy.a(m()));
            sb.append("\"");
        }
        if (k() != null) {
            sb.append(" chid=\"");
            sb.append(gy.a(k()));
            sb.append("\"");
        }
        if (this.f426a != null) {
            sb.append(" type=\"");
            sb.append(this.f426a);
            sb.append("\"");
        }
        sb.append(">");
        if (this.b != null) {
            sb.append("<status>");
            sb.append(gy.a(this.b));
            sb.append("</status>");
        }
        if (this.a != Integer.MIN_VALUE) {
            sb.append("<priority>");
            sb.append(this.a);
            sb.append("</priority>");
        }
        a aVar = this.f425a;
        if (aVar != null && aVar != a.available) {
            sb.append("<show>");
            sb.append(this.f425a);
            sb.append("</show>");
        }
        sb.append(o());
        gr a2 = a();
        if (a2 != null) {
            sb.append(a2.m2202a());
        }
        sb.append("</presence>");
        return sb.toString();
    }

    public void a(int i) {
        if (i >= -128 && i <= 128) {
            this.a = i;
            return;
        }
        throw new IllegalArgumentException("Priority value " + i + " is not valid. Valid range is -128 through 128.");
    }

    public void a(a aVar) {
        this.f425a = aVar;
    }

    public void a(b bVar) {
        Objects.requireNonNull(bVar, "Type cannot be null");
        this.f426a = bVar;
    }

    @Override // com.xiaomi.push.gn
    public void a(String str) {
        this.b = str;
    }
}
