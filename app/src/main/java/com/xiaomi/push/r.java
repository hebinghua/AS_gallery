package com.xiaomi.push;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class r {
    public static volatile r a;

    /* renamed from: a  reason: collision with other field name */
    public Context f808a;

    /* renamed from: a  reason: collision with other field name */
    public Handler f809a = new Handler(Looper.getMainLooper());

    /* renamed from: a  reason: collision with other field name */
    public Map<String, Map<String, String>> f810a = new HashMap();

    public r(Context context) {
        this.f808a = context;
    }

    public static r a(Context context) {
        if (a == null) {
            synchronized (r.class) {
                if (a == null) {
                    a = new r(context);
                }
            }
        }
        return a;
    }

    public final synchronized String a(String str, String str2) {
        if (this.f810a != null && !TextUtils.isEmpty(str)) {
            if (!TextUtils.isEmpty(str2)) {
                try {
                    Map<String, String> map = this.f810a.get(str);
                    if (map == null) {
                        return "";
                    }
                    return map.get(str2);
                } catch (Throwable unused) {
                    return "";
                }
            }
        }
        return "";
    }

    public synchronized String a(String str, String str2, String str3) {
        String a2 = a(str, str2);
        if (!TextUtils.isEmpty(a2)) {
            return a2;
        }
        return this.f808a.getSharedPreferences(str, 4).getString(str2, str3);
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m2408a(String str, String str2, String str3) {
        b(str, str2, str3);
        this.f809a.post(new s(this, str, str2, str3));
    }

    public final synchronized void b(String str, String str2, String str3) {
        if (this.f810a == null) {
            this.f810a = new HashMap();
        }
        Map<String, String> map = this.f810a.get(str);
        if (map == null) {
            map = new HashMap<>();
        }
        map.put(str2, str3);
        this.f810a.put(str, map);
    }
}
