package com.xiaomi.stat.c;

import android.content.Context;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class a {
    private static final String a = "ClientConfigMoniter";
    private static final int b = 1;
    private static final int c = 2;
    private static final int d = 3;
    private static final int e = 4;
    private static final int f = 5;
    private static Context g = ak.a();
    private static HashMap<Integer, Integer> h;

    static {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        h = hashMap;
        hashMap.put(1, 1);
        h.put(2, 2);
        h.put(3, 4);
        h.put(4, 8);
        h.put(5, 16);
    }

    public static int a(String str) {
        int i = 0;
        try {
            for (Map.Entry<Integer, Integer> entry : h.entrySet()) {
                int intValue = entry.getKey().intValue();
                int intValue2 = entry.getValue().intValue();
                if (a(intValue, str)) {
                    i |= intValue2;
                }
            }
        } catch (Exception e2) {
            k.d(a, "getClientConfiguration exception", e2);
        }
        return i;
    }

    private static boolean a(int i, String str) {
        boolean z = false;
        try {
        } catch (Exception e2) {
            k.d(a, "checkSetting exception", e2);
        }
        if (i == 1) {
            z = com.xiaomi.stat.b.u();
        } else if (i == 2) {
            z = com.xiaomi.stat.b.d(str);
        } else if (i != 3) {
            if (i == 5) {
                z = com.xiaomi.stat.b.g();
            }
            return z;
        } else {
            z = k.a();
        }
        return z;
    }
}
