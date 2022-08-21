package com.xiaomi.onetrack.c;

import android.content.Context;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.Action;
import com.xiaomi.onetrack.util.aa;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.q;
import com.xiaomi.onetrack.util.x;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class f {
    public static final JSONObject a = new JSONObject();
    public Context f;
    public JSONObject g;
    public String[] h;

    public f() {
        this.g = null;
        this.h = new String[2];
        this.f = com.xiaomi.onetrack.e.a.a();
    }

    /* loaded from: classes3.dex */
    public static final class a {
        public static final f a = new f();
    }

    public static f a() {
        return a.a;
    }

    public synchronized String[] b() {
        JSONObject e = e();
        this.h[0] = e != null ? e.optString(Action.KEY_ATTRIBUTE) : "";
        this.h[1] = e != null ? e.optString(com.xiaomi.stat.d.g) : "";
        d();
        return this.h;
    }

    public final void d() {
        if (p.a) {
            if (!TextUtils.isEmpty(this.h[0]) && !TextUtils.isEmpty(this.h[1])) {
                p.a("SecretKeyManager", "key  and sid is valid! ");
            } else {
                p.a("SecretKeyManager", "key or sid is invalid!");
            }
        }
    }

    public JSONObject c() {
        try {
        } catch (Exception e) {
            p.b("SecretKeyManager", "requestSecretData: " + e.toString());
        }
        if (q.a("SecretKeyManager")) {
            return a;
        }
        byte[] a2 = com.xiaomi.onetrack.c.a.a();
        String a3 = c.a(e.a(a2));
        HashMap hashMap = new HashMap();
        hashMap.put("secretKey", a3);
        String b = com.xiaomi.onetrack.f.b.b(x.a().e(), hashMap, true);
        if (!TextUtils.isEmpty(b)) {
            JSONObject jSONObject = new JSONObject(b);
            int optInt = jSONObject.optInt("code");
            JSONObject optJSONObject = jSONObject.optJSONObject("data");
            if (optInt == 0 && optJSONObject != null) {
                String optString = optJSONObject.optString(Action.KEY_ATTRIBUTE);
                String optString2 = optJSONObject.optString(com.xiaomi.stat.d.g);
                if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(optString2)) {
                    String a4 = c.a(com.xiaomi.onetrack.c.a.b(c.a(optString), a2));
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put(Action.KEY_ATTRIBUTE, a4);
                    jSONObject2.put(com.xiaomi.stat.d.g, optString2);
                    this.g = jSONObject2;
                    aa.a(b.a(this.f, jSONObject2.toString()));
                    aa.i(System.currentTimeMillis());
                }
            }
        }
        return this.g;
    }

    public final JSONObject e() {
        JSONObject jSONObject = this.g;
        if (jSONObject == null && (jSONObject = f()) != null) {
            this.g = jSONObject;
        }
        return jSONObject == null ? c() : jSONObject;
    }

    public final JSONObject f() {
        try {
            String g = aa.g();
            if (!TextUtils.isEmpty(g)) {
                return new JSONObject(b.b(this.f, g));
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
