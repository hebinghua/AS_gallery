package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class dz {
    public static void a(Context context, String str, int i, String str2) {
        al.a(context).a(new ea(context, str, i, str2));
    }

    public static void a(Context context, HashMap<String, String> hashMap) {
        eh m2123a = ed.a(context).m2123a();
        if (m2123a != null) {
            m2123a.a(context, hashMap);
        }
    }

    public static void b(Context context, HashMap<String, String> hashMap) {
        eh m2123a = ed.a(context).m2123a();
        if (m2123a != null) {
            m2123a.c(context, hashMap);
        }
    }

    public static void c(Context context, String str, int i, String str2) {
        if (context == null || TextUtils.isEmpty(str)) {
            return;
        }
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("awake_info", str);
            hashMap.put("event_type", String.valueOf(i));
            hashMap.put("description", str2);
            int a = ed.a(context).a();
            if (a != 1) {
                if (a != 2) {
                    if (a == 3) {
                        a(context, hashMap);
                    }
                }
                c(context, hashMap);
            } else {
                a(context, hashMap);
            }
            b(context, hashMap);
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }

    public static void c(Context context, HashMap<String, String> hashMap) {
        eh m2123a = ed.a(context).m2123a();
        if (m2123a != null) {
            m2123a.b(context, hashMap);
        }
    }
}
