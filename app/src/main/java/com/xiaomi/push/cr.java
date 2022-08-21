package com.xiaomi.push;

import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class cr {

    /* renamed from: a  reason: collision with other field name */
    public long f168a;

    /* renamed from: a  reason: collision with other field name */
    public String f169a;

    /* renamed from: b  reason: collision with other field name */
    public String f171b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;
    public String h;
    public String i;

    /* renamed from: a  reason: collision with other field name */
    public ArrayList<da> f170a = new ArrayList<>();
    public double a = 0.1d;
    public String j = "s.mi1.cc";
    public long b = 86400000;

    public cr(String str) {
        this.f169a = "";
        if (!TextUtils.isEmpty(str)) {
            this.f168a = System.currentTimeMillis();
            this.f170a.add(new da(str, -1));
            this.f169a = cv.m2025a();
            this.f171b = str;
            return;
        }
        throw new IllegalArgumentException("the host is empty");
    }

    public synchronized cr a(JSONObject jSONObject) {
        this.f169a = jSONObject.optString("net");
        this.b = jSONObject.getLong("ttl");
        this.a = jSONObject.getDouble("pct");
        this.f168a = jSONObject.getLong("ts");
        this.d = jSONObject.optString("city");
        this.c = jSONObject.optString("prv");
        this.g = jSONObject.optString(MapBundleKey.OfflineMapKey.OFFLINE_CITY_TYPE);
        this.e = jSONObject.optString("isp");
        this.f = jSONObject.optString("ip");
        this.f171b = jSONObject.optString("host");
        this.h = jSONObject.optString("xf");
        JSONArray jSONArray = jSONObject.getJSONArray("fbs");
        for (int i = 0; i < jSONArray.length(); i++) {
            a(new da().a(jSONArray.getJSONObject(i)));
        }
        return this;
    }

    public synchronized String a() {
        if (!TextUtils.isEmpty(this.i)) {
            return this.i;
        } else if (TextUtils.isEmpty(this.e)) {
            return "hardcode_isp";
        } else {
            String a = bp.a(new String[]{this.e, this.c, this.d, this.g, this.f}, "_");
            this.i = a;
            return a;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized ArrayList<String> m2016a() {
        return a(false);
    }

    public ArrayList<String> a(String str) {
        if (!TextUtils.isEmpty(str)) {
            URL url = new URL(str);
            if (!TextUtils.equals(url.getHost(), this.f171b)) {
                throw new IllegalArgumentException("the url is not supported by the fallback");
            }
            ArrayList<String> arrayList = new ArrayList<>();
            Iterator<String> it = a(true).iterator();
            while (it.hasNext()) {
                ct a = ct.a(it.next(), url.getPort());
                arrayList.add(new URL(url.getProtocol(), a.m2024a(), a.a(), url.getFile()).toString());
            }
            return arrayList;
        }
        throw new IllegalArgumentException("the url is empty.");
    }

    public synchronized ArrayList<String> a(boolean z) {
        ArrayList<String> arrayList;
        String substring;
        int size = this.f170a.size();
        da[] daVarArr = new da[size];
        this.f170a.toArray(daVarArr);
        Arrays.sort(daVarArr);
        arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            da daVar = daVarArr[i];
            if (z) {
                substring = daVar.f187a;
            } else {
                int indexOf = daVar.f187a.indexOf(":");
                substring = indexOf != -1 ? daVar.f187a.substring(0, indexOf) : daVar.f187a;
            }
            arrayList.add(substring);
        }
        return arrayList;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized JSONObject m2017a() {
        JSONObject jSONObject;
        jSONObject = new JSONObject();
        jSONObject.put("net", this.f169a);
        jSONObject.put("ttl", this.b);
        jSONObject.put("pct", this.a);
        jSONObject.put("ts", this.f168a);
        jSONObject.put("city", this.d);
        jSONObject.put("prv", this.c);
        jSONObject.put(MapBundleKey.OfflineMapKey.OFFLINE_CITY_TYPE, this.g);
        jSONObject.put("isp", this.e);
        jSONObject.put("ip", this.f);
        jSONObject.put("host", this.f171b);
        jSONObject.put("xf", this.h);
        JSONArray jSONArray = new JSONArray();
        Iterator<da> it = this.f170a.iterator();
        while (it.hasNext()) {
            jSONArray.put(it.next().a());
        }
        jSONObject.put("fbs", jSONArray);
        return jSONObject;
    }

    public void a(double d) {
        this.a = d;
    }

    public void a(long j) {
        if (j > 0) {
            this.b = j;
            return;
        }
        throw new IllegalArgumentException("the duration is invalid " + j);
    }

    public synchronized void a(da daVar) {
        c(daVar.f187a);
        this.f170a.add(daVar);
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2018a(String str) {
        a(new da(str));
    }

    public void a(String str, int i, long j, long j2, Exception exc) {
        a(str, new cq(i, j, j2, exc));
    }

    public void a(String str, long j, long j2) {
        try {
            b(new URL(str).getHost(), j, j2);
        } catch (MalformedURLException unused) {
        }
    }

    public void a(String str, long j, long j2, Exception exc) {
        try {
            b(new URL(str).getHost(), j, j2, exc);
        } catch (MalformedURLException unused) {
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001b, code lost:
        r1.a(r5);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized void a(java.lang.String r4, com.xiaomi.push.cq r5) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.util.ArrayList<com.xiaomi.push.da> r0 = r3.f170a     // Catch: java.lang.Throwable -> L20
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> L20
        L7:
            boolean r1 = r0.hasNext()     // Catch: java.lang.Throwable -> L20
            if (r1 == 0) goto L1e
            java.lang.Object r1 = r0.next()     // Catch: java.lang.Throwable -> L20
            com.xiaomi.push.da r1 = (com.xiaomi.push.da) r1     // Catch: java.lang.Throwable -> L20
            java.lang.String r2 = r1.f187a     // Catch: java.lang.Throwable -> L20
            boolean r2 = android.text.TextUtils.equals(r4, r2)     // Catch: java.lang.Throwable -> L20
            if (r2 == 0) goto L7
            r1.a(r5)     // Catch: java.lang.Throwable -> L20
        L1e:
            monitor-exit(r3)
            return
        L20:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.cr.a(java.lang.String, com.xiaomi.push.cq):void");
    }

    public synchronized void a(String[] strArr) {
        int i;
        int size = this.f170a.size() - 1;
        while (true) {
            i = 0;
            if (size < 0) {
                break;
            }
            int length = strArr.length;
            while (true) {
                if (i < length) {
                    if (TextUtils.equals(this.f170a.get(size).f187a, strArr[i])) {
                        this.f170a.remove(size);
                        break;
                    }
                    i++;
                }
            }
            size--;
        }
        Iterator<da> it = this.f170a.iterator();
        int i2 = 0;
        while (it.hasNext()) {
            int i3 = it.next().a;
            if (i3 > i2) {
                i2 = i3;
            }
        }
        while (i < strArr.length) {
            a(new da(strArr[i], (strArr.length + i2) - i));
            i++;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2019a() {
        return TextUtils.equals(this.f169a, cv.m2025a());
    }

    public boolean a(cr crVar) {
        return TextUtils.equals(this.f169a, crVar.f169a);
    }

    public void b(String str) {
        this.j = str;
    }

    public void b(String str, long j, long j2) {
        a(str, 0, j, j2, null);
    }

    public void b(String str, long j, long j2, Exception exc) {
        a(str, -1, j, j2, exc);
    }

    public boolean b() {
        return System.currentTimeMillis() - this.f168a < this.b;
    }

    public final synchronized void c(String str) {
        Iterator<da> it = this.f170a.iterator();
        while (it.hasNext()) {
            if (TextUtils.equals(it.next().f187a, str)) {
                it.remove();
            }
        }
    }

    public boolean c() {
        long j = this.b;
        if (864000000 >= j) {
            j = 864000000;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j2 = this.f168a;
        return currentTimeMillis - j2 > j || (currentTimeMillis - j2 > this.b && this.f169a.startsWith("WIFI-"));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.f169a);
        sb.append("\n");
        sb.append(a());
        Iterator<da> it = this.f170a.iterator();
        while (it.hasNext()) {
            sb.append("\n");
            sb.append(it.next().toString());
        }
        sb.append("\n");
        return sb.toString();
    }
}
