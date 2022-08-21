package com.xiaomi.stat.b;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.xiaomi.stat.ab;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.o;
import com.xiaomi.stat.d.r;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class i {
    private static final String a = "SecretKeyManager";
    private static final String b = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCA1ynlvPE46RxIPx6qrb8f20DU\n\rkAJgwHtD3zCEkgOjcvFY2mLl0UGnK1F0Vsh4LvImSCa8o8qYYfBguROgIXRdJGZ+\n\rk9stSV7vWmcsxphMfHEE9R4q+QWqgPBSzwyWmwmAQ7PZmHifOrEYl9t/l0YtmjnW\n\r8Zs3aL7Ap9CGse2kWwIDAQAB\r";
    private static final String c = "sid";
    private static final String d = "sk";
    private static final String e = "rt";
    private static final String f = "rc";
    private static final String g = "request_history";
    private static final String h = "last_aes_content";
    private static final String i = "last_success_time";
    private static final String j = "4ef8b4ac42dbc3f95320b73ae0edbd43";
    private static final String k = "050f03d86eeafeb29cf38986462d957c";
    private static final int l = 1;
    private static final int m = 2;
    private static final String n = "1";
    private static final String o = "0";
    private static final int p = 7;
    private static final int q = 15;
    private static volatile i r;
    private Context s = ak.a();
    private byte[] t;
    private byte[] u;
    private String v;

    private i() {
        d();
    }

    public static i a() {
        if (r == null) {
            synchronized (i.class) {
                if (r == null) {
                    r = new i();
                }
            }
        }
        return r;
    }

    private void d() {
        byte[] a2 = com.xiaomi.stat.d.a.a();
        this.u = a2;
        if (a2 == null || a2.length <= 0) {
            this.u = com.xiaomi.stat.d.a.a(k);
        }
        String concat = com.xiaomi.stat.d.g.a(this.u, true).concat("_").concat(String.valueOf(r.b()));
        try {
            concat = com.xiaomi.stat.d.g.a(concat.getBytes("utf-8"), true);
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        this.t = com.xiaomi.stat.d.a.a(com.xiaomi.stat.d.a.a(concat), j);
    }

    public synchronized byte[] a(byte[] bArr) {
        if (bArr == null) {
            k.b(a, "encrypt content is empty");
            return null;
        }
        return com.xiaomi.stat.d.a.a(bArr, e());
    }

    private String e() {
        String str;
        JSONObject g2;
        String str2 = null;
        if (Build.VERSION.SDK_INT < 18 || (g2 = g()) == null) {
            str = null;
        } else {
            str2 = g2.optString("sk");
            str = g2.optString("sid");
        }
        return (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str)) ? com.xiaomi.stat.d.g.a(this.u, true) : str2;
    }

    public synchronized String b() {
        String str;
        String str2;
        JSONObject g2;
        str = null;
        if (Build.VERSION.SDK_INT < 18 || (g2 = g()) == null) {
            str2 = null;
        } else {
            str = g2.optString("sid");
            str2 = g2.optString("sk");
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            str = com.xiaomi.stat.d.g.a(this.t, true);
        }
        return str;
    }

    public synchronized void a(boolean z) {
        if (com.xiaomi.stat.b.a() && com.xiaomi.stat.b.b()) {
            if (l.a()) {
                for (int i2 = 1; i2 <= 3 && b(z) && i2 != 3; i2++) {
                    try {
                        Thread.sleep(AbstractComponentTracker.LINGERING_TIMEOUT);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            } else {
                k.b(a, "network not connected!");
            }
            return;
        }
        k.c(a, "update abort: statistic or network is not enabled");
    }

    private boolean b(boolean z) {
        if (Build.VERSION.SDK_INT < 18) {
            k.b(a, "under 4.3,use randomly generated key");
            return false;
        }
        if (j()) {
            k();
        }
        JSONObject g2 = g();
        if (g2 != null) {
            String optString = g2.optString("sid");
            if (!TextUtils.isEmpty(g2.optString("sk")) && !TextUtils.isEmpty(optString) && !z) {
                k.b(a, "key and sid already requested successfully in recent 7 days!");
                return false;
            }
        }
        JSONObject h2 = h();
        long optLong = h2.optLong(e);
        int optInt = h2.optInt("rc");
        if (r.b(optLong) && optInt >= 15 && !z) {
            k.b(a, "request count > max count today, skip...");
            return false;
        }
        return f();
    }

    private boolean f() {
        boolean z = false;
        try {
            byte[] a2 = com.xiaomi.stat.d.a.a();
            String a3 = com.xiaomi.stat.d.d.a(o.a(com.xiaomi.stat.d.d.a(b), a2));
            i();
            HashMap hashMap = new HashMap();
            hashMap.put("skey_rsa", a3);
            String a4 = com.xiaomi.stat.c.c.a(g.a().d(), (Map<String, String>) hashMap, false);
            if (!TextUtils.isEmpty(a4)) {
                k.b(a, "result:" + a4);
                JSONObject jSONObject = new JSONObject(a4);
                String optString = jSONObject.optString("msg");
                int optInt = jSONObject.optInt("code");
                long optLong = jSONObject.optLong("curTime");
                JSONObject optJSONObject = jSONObject.optJSONObject("result");
                if (optInt == 1 && optJSONObject != null) {
                    try {
                        String optString2 = optJSONObject.optString("sid");
                        String a5 = com.xiaomi.stat.d.a.a(optJSONObject.optString(Action.KEY_ATTRIBUTE), a2);
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("sk", a5);
                        jSONObject2.put("sid", optString2);
                        this.v = jSONObject2.toString();
                        ab.a().b("last_aes_content", com.xiaomi.stat.d.b.a(this.s, jSONObject2.toString()));
                        ab.a().b("last_success_time", optLong);
                        r.a(optLong);
                        return false;
                    } catch (Exception e2) {
                        e = e2;
                        k.d(a, "updateSecretKey e", e);
                        return z;
                    }
                } else if (optInt == 2) {
                    k.b(a, "update secret-key failed: " + optString);
                }
            }
            return true;
        } catch (Exception e3) {
            e = e3;
            z = true;
        }
    }

    private JSONObject g() {
        String b2;
        String a2 = ab.a().a("last_aes_content", "");
        try {
            if (TextUtils.isEmpty(a2)) {
                return null;
            }
            if (!TextUtils.isEmpty(this.v)) {
                b2 = this.v;
            } else {
                b2 = com.xiaomi.stat.d.b.b(this.s, a2);
                this.v = b2;
            }
            return new JSONObject(b2);
        } catch (Exception e2) {
            k.d(a, "decodeFromAndroidKeyStore e", e2);
            return null;
        }
    }

    private JSONObject h() {
        try {
            String a2 = ab.a().a("request_history", "");
            if (!TextUtils.isEmpty(a2)) {
                return new JSONObject(a2);
            }
        } catch (Exception e2) {
            k.d(a, "getRequestHistory e", e2);
        }
        return new JSONObject();
    }

    private void i() {
        try {
            JSONObject h2 = h();
            long optLong = h2.optLong(e);
            int optInt = h2.optInt("rc");
            if (r.b(optLong)) {
                h2.put("rc", optInt + 1);
            } else {
                h2.put("rc", 1);
            }
            h2.put(e, r.b());
            ab.a().b("request_history", h2.toString());
        } catch (JSONException e2) {
            k.b(a, "updateSecretKey e", e2);
        }
    }

    private boolean j() {
        long a2 = ab.a().a("last_success_time", 0L);
        return a2 != 0 && r.a(a2, CoreConstants.MILLIS_IN_ONE_WEEK);
    }

    private void k() {
        ab a2 = ab.a();
        this.v = null;
        a2.b("last_aes_content");
        a2.b("last_success_time");
    }

    private synchronized boolean l() {
        boolean z;
        JSONObject g2 = g();
        z = true;
        if (g2 != null) {
            String optString = g2.optString("sk");
            String optString2 = g2.optString("sid");
            if (!TextUtils.isEmpty(optString)) {
                if (!TextUtils.isEmpty(optString2)) {
                    z = false;
                }
            }
        }
        return z;
    }

    public String c() {
        return (Build.VERSION.SDK_INT < 18 || l()) ? n : o;
    }
}
