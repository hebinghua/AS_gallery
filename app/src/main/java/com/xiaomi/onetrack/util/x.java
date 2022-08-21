package com.xiaomi.onetrack.util;

import android.text.TextUtils;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class x {
    public static ConcurrentHashMap<String, String> w = new ConcurrentHashMap<>();

    public final String g() {
        return com.xiaomi.stat.b.h.f;
    }

    public x() {
        w.put("IN", "tracking.india.miui.com");
        w.put("RU", "tracking.rus.miui.com");
        f();
    }

    /* loaded from: classes3.dex */
    public static class a {
        public static final x a = new x();
    }

    public static x a() {
        return a.a;
    }

    public final void f() {
        try {
            String h = aa.h();
            if (TextUtils.isEmpty(h)) {
                return;
            }
            a(new JSONObject(h));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public synchronized void a(JSONObject jSONObject) {
        p.a("RegionDomainManager", "updateHostMap:" + jSONObject.toString());
        try {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next);
                if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(optString)) {
                    w.put(next, optString);
                }
            }
            aa.b(new JSONObject(w).toString());
        } catch (Exception e) {
            p.a("RegionDomainManager", "updateHostMap: " + e.toString());
        }
        p.a("RegionDomainManager", "merge config:" + new JSONObject(w).toString());
    }

    public String b() {
        try {
            if (TextUtils.isEmpty(aa.l())) {
                com.xiaomi.onetrack.a.d.b();
            }
        } catch (Exception e) {
            p.a("RegionDomainManager", "getTrackingUrl: " + e.toString());
        }
        return a(g(), h(), "/track/v4");
    }

    public String c() {
        return a(g(), i(), "/api/v4/detail/config");
    }

    public String d() {
        return a(g(), i(), "/api/v4/detail/config_common");
    }

    public String e() {
        return a(g(), h(), "/track/key_get");
    }

    public String a(String str, String str2, String str3) {
        return str + str2 + str3;
    }

    public final String h() {
        return a(q.x(), q.y());
    }

    public final String a(boolean z, String str) {
        if (!z) {
            return "tracking.miui.com";
        }
        String str2 = w.get(str);
        return TextUtils.isEmpty(str2) ? "tracking.intl.miui.com" : str2;
    }

    public final String i() {
        boolean x = q.x();
        String y = q.y();
        return !x ? "sdkconfig.ad.xiaomi.com" : TextUtils.equals(y, "IN") ? "sdkconfig.ad.india.xiaomi.com" : TextUtils.equals(y, "RU") ? "sdkconfig.ad.rus.xiaomi.com" : "sdkconfig.ad.intl.xiaomi.com";
    }
}
