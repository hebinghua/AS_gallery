package com.baidu.b;

import android.text.TextUtils;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class f {
    public String a;
    public String b;
    public int c = 2;
    private int d = 0;

    public static f a(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            f fVar = new f();
            fVar.a = str;
            int length = TextUtils.isEmpty(str2) ? 0 : str2.length();
            fVar.d = length;
            if (length < 14) {
                if (TextUtils.isEmpty(str2)) {
                    str2 = "0";
                }
                fVar.b = str2;
            }
            return fVar;
        }
        return null;
    }

    public static boolean a(int i) {
        return i >= 14;
    }

    public static boolean a(String str) {
        return TextUtils.isEmpty(str);
    }

    public static f b(String str) {
        return c(e(str));
    }

    private static f c(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            String str2 = "0";
            String str3 = str2;
            while (keys.hasNext()) {
                String next = keys.next();
                if (!d("ZGV2aWNlaWQ=").equals(next) && !d("dmVy").equals(next)) {
                    str3 = jSONObject.optString(next, str2);
                }
            }
            String string = jSONObject.getString(d("ZGV2aWNlaWQ="));
            int i = jSONObject.getInt(d("dmVy"));
            int length = TextUtils.isEmpty(str3) ? 0 : str3.length();
            if (!TextUtils.isEmpty(string)) {
                f fVar = new f();
                fVar.a = string;
                fVar.c = i;
                fVar.d = length;
                if (length < 14) {
                    if (!TextUtils.isEmpty(str3)) {
                        str2 = str3;
                    }
                    fVar.b = str2;
                }
                fVar.c();
                return fVar;
            }
        } catch (JSONException e) {
            com.baidu.b.f.c.a(e);
        }
        return null;
    }

    private static String d(String str) {
        return new String(com.baidu.b.d.a.a(str.getBytes()));
    }

    private static String e(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            byte[] a = com.baidu.b.c.a.g.a();
            return new String(com.baidu.b.c.a.c.a(a, a, com.baidu.b.d.a.a(str.getBytes())));
        } catch (Exception e) {
            com.baidu.b.f.c.a(e);
            return "";
        }
    }

    public boolean a() {
        return a(this.b);
    }

    public boolean b() {
        return a(this.d);
    }

    public boolean c() {
        String str;
        if (b()) {
            str = "O";
        } else if (!a()) {
            return false;
        } else {
            str = "0";
        }
        this.b = str;
        return true;
    }
}
