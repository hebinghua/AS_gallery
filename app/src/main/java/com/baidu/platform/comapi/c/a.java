package com.baidu.platform.comapi.c;

import com.baidu.platform.comapi.a.c;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class a {
    public static a a;
    private static JSONObject b;
    private c c = null;

    public static a a() {
        if (a == null) {
            a = new a();
        }
        if (b == null) {
            b = new JSONObject();
        }
        return a;
    }

    private void b() {
        b = null;
        b = new JSONObject();
    }

    public synchronized boolean a(String str) {
        boolean z;
        if (this.c == null) {
            this.c = c.a();
        }
        z = false;
        if (this.c != null) {
            JSONObject jSONObject = b;
            z = (jSONObject == null || jSONObject.length() <= 0) ? this.c.a(1100, 1, str, null) : this.c.a(1100, 1, str, b.toString());
            b();
        }
        return z;
    }
}
