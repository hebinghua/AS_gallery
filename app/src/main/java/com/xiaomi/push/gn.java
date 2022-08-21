package com.xiaomi.push;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes3.dex */
public abstract class gn {
    public static long a;

    /* renamed from: a  reason: collision with other field name */
    public static final DateFormat f421a;
    public static String c;

    /* renamed from: a  reason: collision with other field name */
    public gr f422a;

    /* renamed from: a  reason: collision with other field name */
    public List<gk> f423a;

    /* renamed from: a  reason: collision with other field name */
    public final Map<String, Object> f424a;
    public String d;
    public String e;
    public String f;
    public String g;
    public String h;
    public String i;

    /* renamed from: a  reason: collision with other field name */
    public static final String f420a = Locale.getDefault().getLanguage().toLowerCase();
    public static String b = null;

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        f421a = simpleDateFormat;
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        c = gy.a(5) + "-";
        a = 0L;
    }

    public gn() {
        this.d = b;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.f423a = new CopyOnWriteArrayList();
        this.f424a = new HashMap();
        this.f422a = null;
    }

    public gn(Bundle bundle) {
        this.d = b;
        this.e = null;
        this.f = null;
        this.g = null;
        this.h = null;
        this.i = null;
        this.f423a = new CopyOnWriteArrayList();
        this.f424a = new HashMap();
        this.f422a = null;
        this.f = bundle.getString("ext_to");
        this.g = bundle.getString("ext_from");
        this.h = bundle.getString("ext_chid");
        this.e = bundle.getString("ext_pkt_id");
        Parcelable[] parcelableArray = bundle.getParcelableArray("ext_exts");
        if (parcelableArray != null) {
            this.f423a = new ArrayList(parcelableArray.length);
            for (Parcelable parcelable : parcelableArray) {
                gk a2 = gk.a((Bundle) parcelable);
                if (a2 != null) {
                    this.f423a.add(a2);
                }
            }
        }
        Bundle bundle2 = bundle.getBundle("ext_ERROR");
        if (bundle2 != null) {
            this.f422a = new gr(bundle2);
        }
    }

    public static synchronized String i() {
        String sb;
        synchronized (gn.class) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(c);
            long j = a;
            a = 1 + j;
            sb2.append(Long.toString(j));
            sb = sb2.toString();
        }
        return sb;
    }

    public static String q() {
        return f420a;
    }

    /* renamed from: a */
    public Bundle mo2201a() {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(this.d)) {
            bundle.putString("ext_ns", this.d);
        }
        if (!TextUtils.isEmpty(this.g)) {
            bundle.putString("ext_from", this.g);
        }
        if (!TextUtils.isEmpty(this.f)) {
            bundle.putString("ext_to", this.f);
        }
        if (!TextUtils.isEmpty(this.e)) {
            bundle.putString("ext_pkt_id", this.e);
        }
        if (!TextUtils.isEmpty(this.h)) {
            bundle.putString("ext_chid", this.h);
        }
        gr grVar = this.f422a;
        if (grVar != null) {
            bundle.putBundle("ext_ERROR", grVar.a());
        }
        List<gk> list = this.f423a;
        if (list != null) {
            Bundle[] bundleArr = new Bundle[list.size()];
            int i = 0;
            for (gk gkVar : this.f423a) {
                Bundle a2 = gkVar.a();
                if (a2 != null) {
                    bundleArr[i] = a2;
                    i++;
                }
            }
            bundle.putParcelableArray("ext_exts", bundleArr);
        }
        return bundle;
    }

    public gk a(String str) {
        return a(str, null);
    }

    public gk a(String str, String str2) {
        for (gk gkVar : this.f423a) {
            if (str2 == null || str2.equals(gkVar.b())) {
                if (str.equals(gkVar.m2194a())) {
                    return gkVar;
                }
            }
        }
        return null;
    }

    public gr a() {
        return this.f422a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Object m2198a(String str) {
        Map<String, Object> map = this.f424a;
        if (map == null) {
            return null;
        }
        return map.get(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public abstract String m2199a();

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Collection<gk> m2200a() {
        if (this.f423a == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList(this.f423a));
    }

    public void a(gk gkVar) {
        this.f423a.add(gkVar);
    }

    public void a(gr grVar) {
        this.f422a = grVar;
    }

    public synchronized Collection<String> b() {
        if (this.f424a == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet(this.f424a.keySet()));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        gn gnVar = (gn) obj;
        gr grVar = this.f422a;
        if (grVar == null ? gnVar.f422a != null : !grVar.equals(gnVar.f422a)) {
            return false;
        }
        String str = this.g;
        if (str == null ? gnVar.g != null : !str.equals(gnVar.g)) {
            return false;
        }
        if (!this.f423a.equals(gnVar.f423a)) {
            return false;
        }
        String str2 = this.e;
        if (str2 == null ? gnVar.e != null : !str2.equals(gnVar.e)) {
            return false;
        }
        String str3 = this.h;
        if (str3 == null ? gnVar.h != null : !str3.equals(gnVar.h)) {
            return false;
        }
        Map<String, Object> map = this.f424a;
        if (map == null ? gnVar.f424a != null : !map.equals(gnVar.f424a)) {
            return false;
        }
        String str4 = this.f;
        if (str4 == null ? gnVar.f != null : !str4.equals(gnVar.f)) {
            return false;
        }
        String str5 = this.d;
        String str6 = gnVar.d;
        if (str5 != null) {
            if (str5.equals(str6)) {
                return true;
            }
        } else if (str6 == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        String str = this.d;
        int i = 0;
        int hashCode = (str != null ? str.hashCode() : 0) * 31;
        String str2 = this.e;
        int hashCode2 = (hashCode + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.f;
        int hashCode3 = (hashCode2 + (str3 != null ? str3.hashCode() : 0)) * 31;
        String str4 = this.g;
        int hashCode4 = (hashCode3 + (str4 != null ? str4.hashCode() : 0)) * 31;
        String str5 = this.h;
        int hashCode5 = (((((hashCode4 + (str5 != null ? str5.hashCode() : 0)) * 31) + this.f423a.hashCode()) * 31) + this.f424a.hashCode()) * 31;
        gr grVar = this.f422a;
        if (grVar != null) {
            i = grVar.hashCode();
        }
        return hashCode5 + i;
    }

    public String j() {
        if ("ID_NOT_AVAILABLE".equals(this.e)) {
            return null;
        }
        if (this.e == null) {
            this.e = i();
        }
        return this.e;
    }

    public String k() {
        return this.h;
    }

    public void k(String str) {
        this.e = str;
    }

    public String l() {
        return this.f;
    }

    public void l(String str) {
        this.h = str;
    }

    public String m() {
        return this.g;
    }

    public void m(String str) {
        this.f = str;
    }

    public String n() {
        return this.i;
    }

    public void n(String str) {
        this.g = str;
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x011b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x0116 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0105 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x010b A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized java.lang.String o() {
        /*
            Method dump skipped, instructions count: 301
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gn.o():java.lang.String");
    }

    public void o(String str) {
        this.i = str;
    }

    public String p() {
        return this.d;
    }
}
