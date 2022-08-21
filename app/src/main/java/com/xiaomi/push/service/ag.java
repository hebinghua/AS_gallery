package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.push.Cif;
import java.util.Map;

/* loaded from: classes3.dex */
public class ag {
    public static a a;

    /* renamed from: a  reason: collision with other field name */
    public static b f867a;

    /* loaded from: classes3.dex */
    public interface a {
        Map<String, String> a(Context context, Cif cif);

        /* renamed from: a  reason: collision with other method in class */
        void m2456a(Context context, Cif cif);

        boolean a(Context context, Cif cif, boolean z);
    }

    /* loaded from: classes3.dex */
    public interface b {
        void a(Cif cif);

        void a(String str);

        /* renamed from: a  reason: collision with other method in class */
        boolean m2457a(Cif cif);
    }

    public static Map<String, String> a(Context context, Cif cif) {
        a aVar = a;
        if (aVar == null || cif == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("pepa listener or container is null");
            return null;
        }
        return aVar.a(context, cif);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m2454a(Context context, Cif cif) {
        a aVar = a;
        if (aVar == null || cif == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("handle msg wrong");
        } else {
            aVar.m2456a(context, cif);
        }
    }

    public static void a(Cif cif) {
        b bVar = f867a;
        if (bVar == null || cif == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("pepa clearMessage is null");
        } else {
            bVar.a(cif);
        }
    }

    public static void a(String str) {
        b bVar = f867a;
        if (bVar == null || str == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("pepa clearMessage is null");
        } else {
            bVar.a(str);
        }
    }

    public static boolean a(Context context, Cif cif, boolean z) {
        a aVar = a;
        if (aVar == null || cif == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("pepa judement listener or container is null");
            return false;
        }
        return aVar.a(context, cif, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static boolean m2455a(Cif cif) {
        b bVar = f867a;
        if (bVar == null || cif == null) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("pepa handleReceiveMessage is null");
            return false;
        }
        return bVar.m2457a(cif);
    }
}
