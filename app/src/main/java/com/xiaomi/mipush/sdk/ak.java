package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.mipush.sdk.l;
import com.xiaomi.push.bk;

/* loaded from: classes3.dex */
public class ak {
    public static AbstractPushManager a(Context context, e eVar) {
        return b(context, eVar);
    }

    public static AbstractPushManager b(Context context, e eVar) {
        l.a m1925a = l.m1925a(eVar);
        if (m1925a == null || TextUtils.isEmpty(m1925a.a) || TextUtils.isEmpty(m1925a.b)) {
            return null;
        }
        return (AbstractPushManager) bk.a(m1925a.a, m1925a.b, context);
    }
}
