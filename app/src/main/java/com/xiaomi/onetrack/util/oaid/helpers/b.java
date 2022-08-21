package com.xiaomi.onetrack.util.oaid.helpers;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.onetrack.util.ab;
import com.xiaomi.onetrack.util.n;
import com.xiaomi.onetrack.util.p;

/* loaded from: classes3.dex */
public class b {
    public static String a = "b";

    /* loaded from: classes3.dex */
    public enum a {
        asus("ASUS"),
        huawei("HUAWEI"),
        lenovo("LENOVO"),
        motolora("MOTOLORA"),
        meizu("MEIZU"),
        oppo("OPPO"),
        samsung("SAMSUNG"),
        numbia("NUBIA"),
        vivo("VIVO"),
        xiaomi("XIAOMI"),
        redmi("REDMI"),
        blackshark("BLACKSHARK"),
        oneplus("ONEPLUS"),
        zte("ZTE"),
        freemeos("FERRMEOS"),
        ssui("SSUI");
        
        public final String q;

        a(String str) {
            this.q = str;
        }

        public static a b(String str) {
            a[] values;
            for (a aVar : values()) {
                if (aVar.q.equals(str)) {
                    return aVar;
                }
            }
            return null;
        }
    }

    public static String c() {
        return Build.MANUFACTURER.toUpperCase();
    }

    public String a(Context context) {
        try {
            return a(context, c());
        } catch (Exception e) {
            p.a(a, e.getMessage());
            return "";
        }
    }

    public String a(Context context, String str) throws Exception {
        a b = a.b(str);
        if (a()) {
            b = a.freemeos;
        }
        if (b()) {
            b = a.ssui;
        }
        if (b == null) {
            throw new Exception(String.format("undefined oaid method of manufacturer %s", str));
        }
        switch (c.a[b.ordinal()]) {
            case 1:
                return new com.xiaomi.onetrack.util.oaid.helpers.a().a(context);
            case 2:
                return new d().a(context);
            case 3:
            case 4:
                return new e().a(context);
            case 5:
                return new f().a(context);
            case 6:
                return new j().a(context);
            case 7:
                return new k().a(context);
            case 8:
                return new h().a(context);
            case 9:
                return new l().a(context);
            case 10:
            case 11:
            case 12:
                return n.b(context);
            case 13:
                return new i().a(context);
            case 14:
            case 15:
            case 16:
                return new m().a(context);
            default:
                return "";
        }
    }

    public boolean a() {
        String a2 = ab.a("ro.build.freeme.label");
        return !TextUtils.isEmpty(a2) && a2.equalsIgnoreCase("FREEMEOS");
    }

    public boolean b() {
        String a2 = ab.a("ro.ssui.product");
        return !TextUtils.isEmpty(a2) && !a2.equalsIgnoreCase("unknown");
    }
}
