package com.baidu.b;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import com.baidu.b.b.a;
import com.baidu.b.e.a;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class h {
    private static boolean b = false;
    public c a;
    private Context c;
    private a.C0004a d;
    private com.baidu.b.b.c e;

    /* loaded from: classes.dex */
    public static class a {
        public static final String[] a = {"V", "O", "0"};
        private String b;
        private String c;
        private String d;
        private long e;
        private String f;
        private int g = 1;

        public String a() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("dik", this.b);
                jSONObject.put("v270fk", this.c);
                jSONObject.put("cck", this.d);
                jSONObject.put("vsk", this.g);
                jSONObject.put("ctk", this.e);
                jSONObject.put(com.xiaomi.stat.d.ah, this.f);
                return jSONObject.toString();
            } catch (JSONException e) {
                com.baidu.b.f.c.a(e);
                return null;
            }
        }

        public String b() {
            String str = this.c;
            if (TextUtils.isEmpty(str)) {
                str = "0";
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.b);
            sb.append("|");
            sb.append(str);
            if ("V".equals(str)) {
                sb.append(this.d);
            }
            if (!TextUtils.isEmpty(this.f)) {
                sb.append(this.f);
            }
            return sb.toString().trim();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            if (this.g == aVar.g && this.b.equals(aVar.b) && this.c.equals(aVar.c) && this.d.equals(aVar.d)) {
                String str = this.f;
                String str2 = aVar.f;
                if (str == str2) {
                    return true;
                }
                if (str != null && str.equals(str2)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(new Object[]{this.b, this.c, this.d, this.f, Integer.valueOf(this.g)});
        }
    }

    public h(Context context, com.baidu.b.e.a aVar, c cVar) {
        Objects.requireNonNull(context, "context should not be null!!!");
        this.c = context.getApplicationContext();
        a.C0004a a2 = aVar.b().a("bohrium");
        this.d = a2;
        a2.a();
        this.a = cVar;
        a(aVar);
    }

    public static a a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("dik", "");
            String optString2 = jSONObject.optString("cck", "");
            long optLong = jSONObject.optLong("ctk", 0L);
            int optInt = jSONObject.optInt("vsk", 1);
            String optString3 = jSONObject.optString(com.xiaomi.stat.d.ah, "");
            String optString4 = jSONObject.optString("v270fk", "V");
            if (!TextUtils.isEmpty(optString)) {
                a aVar = new a();
                aVar.b = optString;
                aVar.d = optString2;
                aVar.e = optLong;
                aVar.g = optInt;
                aVar.f = optString3;
                aVar.c = optString4;
                return aVar;
            }
        } catch (Exception e) {
            com.baidu.b.f.c.a(e);
        }
        return null;
    }

    public static a a(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                String c = c(str);
                long currentTimeMillis = System.currentTimeMillis();
                a aVar = new a();
                aVar.b = str;
                aVar.d = c;
                aVar.e = currentTimeMillis;
                aVar.g = 1;
                aVar.f = str3;
                aVar.c = str2;
                return aVar;
            } catch (Exception e) {
                com.baidu.b.f.c.a(e);
            }
        }
        return null;
    }

    private String a(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "android_id");
        return TextUtils.isEmpty(string) ? "" : string;
    }

    private void a(com.baidu.b.e.a aVar) {
        com.baidu.b.b.c cVar = new com.baidu.b.b.c(new com.baidu.b.a());
        a.C0002a c0002a = new a.C0002a();
        c0002a.a = this.c;
        c0002a.b = aVar;
        a.c cVar2 = new a.c();
        for (com.baidu.b.b.a aVar2 : cVar.a()) {
            aVar2.a(c0002a);
            aVar2.a(cVar2);
        }
        this.e = cVar;
    }

    private static String c(String str) {
        try {
            return new com.baidu.b.f.a("ABCDEFGHIJKLMNOPQRSTUVWXYZ234567=", false, false).a(new com.baidu.b.a.a().a(str.getBytes(Keyczar.DEFAULT_ENCODING)));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public a a() {
        a aVar;
        a.d dVar = new a.d();
        dVar.a = true;
        List<com.baidu.b.b.a> a2 = this.e.a();
        Collections.sort(a2, com.baidu.b.b.a.c);
        List<b> b2 = this.a.b(this.c);
        if (b2 != null) {
            for (b bVar : b2) {
                if (!bVar.d && bVar.c) {
                    for (com.baidu.b.b.a aVar2 : a2) {
                        a.e a3 = aVar2.a(bVar.a.packageName, dVar);
                        if (a3 != null && a3.a() && (aVar = a3.a) != null) {
                            return aVar;
                        }
                    }
                    continue;
                }
            }
            return null;
        }
        return null;
    }

    public a a(f fVar) {
        String str;
        if (fVar != null) {
            a aVar = new a();
            aVar.e = System.currentTimeMillis();
            aVar.g = 1;
            try {
                boolean z = false;
                aVar.c = fVar.b.substring(0, 1);
                aVar.b = fVar.a;
                aVar.d = c(fVar.a);
                String[] strArr = a.a;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        z = true;
                        break;
                    } else if (strArr[i].equals(aVar.c)) {
                        break;
                    } else {
                        i++;
                    }
                }
                if (z && (str = fVar.b) != null && str.length() >= 2) {
                    aVar.f = fVar.b.substring(1);
                }
                return aVar;
            } catch (Exception unused) {
                return null;
            }
        }
        throw new IllegalArgumentException("arg non-nullable is expected");
    }

    public a b(String str) {
        String a2 = a(this.c);
        String a3 = com.baidu.b.d.b.a(("com.baidu" + a2).getBytes(), true);
        a aVar = new a();
        aVar.e = System.currentTimeMillis();
        aVar.g = 1;
        aVar.b = a3;
        aVar.c = "E";
        aVar.d = c(a3);
        aVar.f = "RO";
        return aVar;
    }
}
