package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import java.text.SimpleDateFormat;

/* loaded from: classes3.dex */
public class co {
    public static String a;

    /* renamed from: a  reason: collision with other field name */
    public static SimpleDateFormat f165a;

    static {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        f165a = simpleDateFormat;
        a = simpleDateFormat.format(Long.valueOf(System.currentTimeMillis()));
    }

    public static hn a(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        hn hnVar = new hn();
        hnVar.d("category_push_stat");
        hnVar.a("push_sdk_stat_channel");
        hnVar.a(1L);
        hnVar.b(str);
        hnVar.a(true);
        hnVar.b(System.currentTimeMillis());
        hnVar.g(bx.a(context).a());
        hnVar.e(com.xiaomi.stat.c.c.a);
        hnVar.f("");
        hnVar.c("push_stat");
        return hnVar;
    }
}
