package com.xiaomi.onetrack.a;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.DeviceUtil;
import com.xiaomi.onetrack.util.aa;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.q;
import com.xiaomi.onetrack.util.x;
import com.xiaomi.stat.d.i;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class d {
    public static ConcurrentHashMap<Integer, Integer> f = new ConcurrentHashMap<>();

    public static void b() {
        if (e()) {
            f();
        } else {
            p.a("CommonConfigUpdater", "CommonConfigUpdater Does not meet prerequisites for request");
        }
    }

    public static boolean e() {
        if (!com.xiaomi.onetrack.f.c.a()) {
            p.b("CommonConfigUpdater", "net is not connected!");
            return false;
        } else if (TextUtils.isEmpty(aa.l())) {
            return true;
        } else {
            long j = aa.j();
            return j < System.currentTimeMillis() || j - System.currentTimeMillis() > 172800000;
        }
    }

    public static void f() {
        if (q.a("CommonConfigUpdater")) {
            return;
        }
        HashMap hashMap = new HashMap();
        try {
            hashMap.put("oa", com.xiaomi.onetrack.util.oaid.a.a().a(com.xiaomi.onetrack.e.a.b()));
            hashMap.put(com.xiaomi.stat.d.R, q.h());
            hashMap.put(com.xiaomi.stat.d.q, q.d());
            hashMap.put("ii", q.x() ? "1" : "0");
            hashMap.put(com.xiaomi.stat.d.b, "1.2.9");
            hashMap.put("appVer", com.xiaomi.onetrack.e.a.c());
            hashMap.put(com.xiaomi.stat.d.j, q.i());
            hashMap.put("ml", DeviceUtil.c());
            hashMap.put("re", q.y());
            hashMap.put("platform", "Android");
            String d = x.a().d();
            String b = com.xiaomi.onetrack.f.b.b(d, hashMap, true);
            p.a("CommonConfigUpdater", "url:" + d + " response:" + b);
            a(b);
        } catch (IOException e) {
            p.a("CommonConfigUpdater", "requestCloudData: " + e.toString());
        }
    }

    public static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.optInt("code") != 0) {
                return;
            }
            String optString = jSONObject.optString("hash");
            JSONObject optJSONObject = jSONObject.optJSONObject("data");
            if (optJSONObject != null) {
                JSONObject optJSONObject2 = optJSONObject.optJSONObject("regionUrl");
                if (optJSONObject2 != null) {
                    x.a().a(optJSONObject2);
                }
                aa.d(optJSONObject.toString());
                aa.c(optString);
            }
            aa.j(System.currentTimeMillis() + 86400000 + new Random().nextInt(86400000));
        } catch (JSONException e) {
            p.a("CommonConfigUpdater", "saveCommonCloudData: " + e.toString());
        }
    }

    public static Map<Integer, Integer> c() {
        try {
        } catch (Exception e) {
            p.a("CommonConfigUpdater", "getLevelIntervalConfig: " + e.toString());
        }
        if (!f.isEmpty()) {
            return f;
        }
        String l = aa.l();
        if (!TextUtils.isEmpty(l)) {
            JSONArray optJSONArray = new JSONObject(l).optJSONArray("levels");
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject = optJSONArray.getJSONObject(i);
                int optInt = jSONObject.optInt("l");
                int optInt2 = jSONObject.optInt("t");
                if (optInt > 0 && optInt2 > 0) {
                    f.put(Integer.valueOf(optInt), Integer.valueOf(optInt2));
                }
            }
        }
        return f.isEmpty() ? g() : f;
    }

    public static HashMap<Integer, Integer> g() {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        hashMap.put(1, 5000);
        hashMap.put(2, Integer.valueOf((int) i.b));
        hashMap.put(3, 900000);
        return hashMap;
    }
}
