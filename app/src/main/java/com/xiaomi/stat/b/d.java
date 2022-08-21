package com.xiaomi.stat.b;

import android.content.Context;
import android.text.TextUtils;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.r;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class d {
    private static final Object a = new Object();
    private static String b = " https://data.mistat.xiaomi.com/idservice/deviceid_get";
    private static final String c = "DeviceIdManager";
    private static final String d = "ia";
    private static final String e = "ib";
    private static final String f = "md";
    private static final String g = "mm";
    private static final String h = "bm";
    private static final String i = "aa";
    private static final String j = "ai";
    private static final String k = "oa";
    private static final int l = 0;
    private static final int m = 1;
    private static final int n = 2;
    private static final int o = 3;
    private static final int p = 4;
    private static final int q = 5;
    private static final int r = 6;
    private static final int s = 7;
    private static final int t = 1;
    private static final String u = "pref_key_device_id";
    private static final String v = "pref_key_restore_ts";
    private static d w;
    private Context y = ak.a();
    private String x = ab.a().a(u, "");

    private d() {
    }

    public static d a() {
        if (w == null) {
            synchronized (a) {
                if (w == null) {
                    w = new d();
                }
            }
        }
        return w;
    }

    public synchronized String b() {
        if (!com.xiaomi.stat.b.e()) {
            if (!d()) {
                e();
            }
        } else {
            this.x = com.xiaomi.stat.d.e.d();
        }
        return this.x;
    }

    public String c() {
        if (TextUtils.isEmpty(this.x)) {
            return f();
        }
        return this.x;
    }

    public String a(boolean z) {
        if (z) {
            return com.xiaomi.stat.d.e.d();
        }
        String r2 = com.xiaomi.stat.d.e.r(ak.a());
        return !TextUtils.isEmpty(r2) ? r2 : com.xiaomi.stat.d.e.d();
    }

    private void e() {
        if (!com.xiaomi.stat.b.a() || !com.xiaomi.stat.b.b()) {
            k.c(c, "request abort: statistic or network is not enabled");
        } else if (l.a()) {
            for (int i2 = 1; i2 <= 3 && TextUtils.isEmpty(f()) && i2 != 3; i2++) {
                try {
                    Thread.sleep(AbstractComponentTracker.LINGERING_TIMEOUT);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        } else {
            k.b(c, "network is not connected!");
        }
    }

    private String f() {
        try {
            if (k.b()) {
                b = k.b;
            }
            String a2 = com.xiaomi.stat.c.c.a(b, (Map<String, String>) h(), true);
            k.b(c, a2);
            if (!TextUtils.isEmpty(a2)) {
                JSONObject jSONObject = new JSONObject(a2);
                long optLong = jSONObject.optLong("timestamp");
                int optInt = jSONObject.optInt("code");
                String optString = jSONObject.optString("device_id");
                if (optInt == 1) {
                    this.x = optString;
                    ab a3 = ab.a();
                    if (!TextUtils.isEmpty(this.x)) {
                        a3.b(u, optString);
                        a3.b(v, optLong);
                    }
                    r.a(optLong);
                    return this.x;
                }
            }
        } catch (IOException e2) {
            k.b(c, "[getDeviceIdLocal IOException]:", e2);
        } catch (JSONException e3) {
            k.b(c, "[getDeviceIdLocal JSONException]:", e3);
        }
        return this.x;
    }

    public boolean d() {
        String a2 = ab.a().a(u, (String) null);
        return !TextUtils.isEmpty(a2) && !TextUtils.isEmpty(this.x) && this.x.equals(a2);
    }

    private String[] g() {
        return new String[]{com.xiaomi.stat.d.e.b(this.y), com.xiaomi.stat.d.e.e(this.y), com.xiaomi.stat.d.e.h(this.y), com.xiaomi.stat.d.e.k(this.y), com.xiaomi.stat.d.e.n(this.y), com.xiaomi.stat.d.e.q(this.y), com.xiaomi.stat.d.e.p(this.y), f.b(this.y)};
    }

    private HashMap<String, String> h() {
        HashMap<String, String> hashMap = new HashMap<>();
        String[] g2 = g();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("ia", g2[0]);
            jSONObject.put("ib", g2[1]);
            jSONObject.put("md", g2[2]);
            jSONObject.put(g, g2[3]);
            jSONObject.put("bm", g2[4]);
            jSONObject.put("aa", g2[5]);
            jSONObject.put("ai", g2[6]);
            jSONObject.put(k, g2[7]);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        k.b(c, "[pay-load]:" + jSONObject.toString());
        byte[] bArr = new byte[0];
        try {
            bArr = i.a().a(jSONObject.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e3) {
            e3.printStackTrace();
        }
        String str = null;
        if (bArr != null) {
            str = com.xiaomi.stat.d.d.a(com.xiaomi.stat.d.g.a(bArr, true).getBytes());
        }
        hashMap.put(com.xiaomi.stat.d.b, com.xiaomi.stat.a.g);
        if (str == null) {
            str = "";
        }
        hashMap.put(com.xiaomi.stat.d.e, str);
        hashMap.put("ai", ak.b());
        hashMap.put(com.xiaomi.stat.d.aj, "0");
        hashMap.put(com.xiaomi.stat.d.ak, i.a().c());
        hashMap.put(com.xiaomi.stat.d.g, i.a().b());
        return hashMap;
    }
}
