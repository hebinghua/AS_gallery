package com.xiaomi.push.service;

import com.xiaomi.push.ii;

/* loaded from: classes3.dex */
public class i {
    public static a a;

    /* renamed from: a  reason: collision with other field name */
    public static b f959a;

    /* loaded from: classes3.dex */
    public interface a {
        boolean a(ii iiVar);
    }

    /* loaded from: classes3.dex */
    public interface b {
    }

    public static void a(b bVar) {
        f959a = bVar;
    }

    public static boolean a(ii iiVar) {
        String str;
        if (a == null || iiVar == null) {
            str = "rc params is null, not cpra";
        } else if (com.xiaomi.push.m.m2400a(com.xiaomi.push.v.m2551a())) {
            return a.a(iiVar);
        } else {
            str = "rc app not permission to cpra";
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a(str);
        return false;
    }
}
