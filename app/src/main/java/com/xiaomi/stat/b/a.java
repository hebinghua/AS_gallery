package com.xiaomi.stat.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.r;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class a {
    public static final int a = 1;
    public static final int b = 2;
    public static final int c = 4;
    private static final String d = "ConfigManager";
    private static final String e = "-";
    private static int f = 0;
    private static int g = 1;
    private static int h = 2;
    private static final String i = "config_request_count";
    private static final String j = "config_request_time";
    private static final String k = "config_success_requested";
    private static final int l = 1;
    private static final int m = 2;
    private static final int n = 0;
    private static final int o = -1;
    private static final int p = 0;
    private static final int q = 12;
    private static final String r = "t";
    private static final int s = 0;
    private static volatile a t;
    private String w;
    private int u = 0;
    private BroadcastReceiver x = new b(this);
    private Context v = ak.a();

    private a() {
    }

    public static a a() {
        if (t == null) {
            synchronized (a.class) {
                if (t == null) {
                    t = new a();
                }
            }
        }
        return t;
    }

    public synchronized void a(boolean z, boolean z2) {
        int parseInt;
        int parseInt2;
        if (com.xiaomi.stat.b.a() && com.xiaomi.stat.b.b()) {
            if (!l.a()) {
                k.b(d, "network is not connected!");
                try {
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    this.v.registerReceiver(this.x, intentFilter);
                    this.u = 1;
                } catch (Exception e2) {
                    k.e(d, "updateConfig registerReceiver error:" + e2);
                }
                return;
            }
            k.b(d, "updateConfig");
            if (!z2) {
                k.b("MI_STAT_TEST", "updateConfig-InToday");
                if (r.b(ab.a().a(k, 0L))) {
                    k.b(d, "Today has successfully requested key.");
                    return;
                } else if (c()) {
                    k.d(d, "config request to max count skip..");
                    return;
                }
            }
            int i2 = 0;
            if (!z || z2) {
                a(0);
            } else {
                String[] split = com.xiaomi.stat.b.k().split(e);
                if (split.length > 1 && (parseInt2 = Integer.parseInt(split[1])) > (parseInt = Integer.parseInt(split[0]))) {
                    i2 = new Random().nextInt(parseInt2 - parseInt) + parseInt;
                }
                a(i2);
            }
            return;
        }
        k.c(d, "update abort: statistic or network is not enabled");
    }

    private void a(int i2) {
        if (i2 > 0) {
            try {
                Thread.sleep(i2 * 1000);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        b();
    }

    private String b() {
        k.b(d, "requestConfigInner");
        this.w = g.a().b();
        if (k.b()) {
            this.w = k.a;
        }
        String str = null;
        try {
            TreeMap treeMap = new TreeMap();
            treeMap.put(r, String.valueOf(h));
            treeMap.put("ai", ak.b());
            treeMap.put("rc", m.h());
            treeMap.put(com.xiaomi.stat.d.d, m.g());
            treeMap.put(com.xiaomi.stat.d.V, Build.MODEL);
            treeMap.put(com.xiaomi.stat.d.b, com.xiaomi.stat.a.g);
            treeMap.put(com.xiaomi.stat.d.j, com.xiaomi.stat.d.c.b());
            str = com.xiaomi.stat.c.c.a(this.w, (Map<String, String>) treeMap, false);
            a(str);
            return str;
        } catch (Exception e2) {
            k.b(d, "requestConfigInner exception ", e2);
            return str;
        }
    }

    private void a(String str) {
        try {
            k.b(d, String.format("config result:%s", str));
            d();
            if (TextUtils.isEmpty(str)) {
                return;
            }
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.getInt("errorCode") != 0) {
                return;
            }
            long optLong = jSONObject.optLong(com.xiaomi.stat.b.j, 0L);
            ab.a().b(k, optLong);
            com.xiaomi.stat.b.c(jSONObject.optString(com.xiaomi.stat.b.i, com.xiaomi.stat.b.n));
            com.xiaomi.stat.b.c(jSONObject.optInt(com.xiaomi.stat.b.h, -1));
            com.xiaomi.stat.b.d(jSONObject.optInt(com.xiaomi.stat.b.g, 0) / 1000);
            com.xiaomi.stat.b.h(jSONObject.optBoolean(com.xiaomi.stat.b.k));
            a(jSONObject);
            r.a(optLong);
            g.a().a(jSONObject);
            if (this.u == 1) {
                this.v.unregisterReceiver(this.x);
            }
            this.u = 2;
        } catch (Exception e2) {
            k.d(d, "processResult exception", e2);
        }
    }

    private void a(JSONObject jSONObject) {
        try {
            int optInt = jSONObject.optInt(com.xiaomi.stat.b.l);
            if (optInt <= 0) {
                return;
            }
            boolean z = false;
            com.xiaomi.stat.b.k((optInt & 1) == 1);
            com.xiaomi.stat.b.i((optInt & 2) == 2);
            if ((optInt & 4) == 4) {
                z = true;
            }
            com.xiaomi.stat.b.j(z);
        } catch (Exception e2) {
            k.e(d, "updateConfig: " + e2);
        }
    }

    private boolean c() {
        long b2 = r.b();
        ab a2 = ab.a();
        boolean z = false;
        try {
            if (ab.a().a(j)) {
                if (!r.b(a2.a(j, 0L))) {
                    a2.b(j, b2);
                    a2.b(i, 0);
                } else if (a2.a(i, 0) >= 12) {
                    z = true;
                }
            } else {
                a2.b(j, b2);
                a2.b(i, 1);
            }
        } catch (Exception e2) {
            k.d(d, "isRequestCountReachMax exception", e2);
        }
        return z;
    }

    private void d() {
        try {
            ab a2 = ab.a();
            a2.b(i, a2.a(i, 0) + 1);
        } catch (Exception e2) {
            k.d(d, "addRequestCount exception", e2);
        }
    }
}
