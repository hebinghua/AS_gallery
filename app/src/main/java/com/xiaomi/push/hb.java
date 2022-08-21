package com.xiaomi.push;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import ch.qos.logback.core.util.FileSize;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class hb {

    /* renamed from: a  reason: collision with other field name */
    public static ao f437a = new ao(true);
    public static volatile int a = -1;

    /* renamed from: a  reason: collision with other field name */
    public static long f436a = System.currentTimeMillis();

    /* renamed from: a  reason: collision with other field name */
    public static final Object f439a = new Object();

    /* renamed from: a  reason: collision with other field name */
    public static List<a> f441a = Collections.synchronizedList(new ArrayList());

    /* renamed from: a  reason: collision with other field name */
    public static String f440a = "";

    /* renamed from: a  reason: collision with other field name */
    public static com.xiaomi.push.providers.a f438a = null;

    /* loaded from: classes3.dex */
    public static class a {
        public int a;

        /* renamed from: a  reason: collision with other field name */
        public long f442a;

        /* renamed from: a  reason: collision with other field name */
        public String f443a;
        public int b;

        /* renamed from: b  reason: collision with other field name */
        public long f444b;

        /* renamed from: b  reason: collision with other field name */
        public String f445b;

        public a(String str, long j, int i, int i2, String str2, long j2) {
            this.f443a = "";
            this.f442a = 0L;
            this.a = -1;
            this.b = -1;
            this.f445b = "";
            this.f444b = 0L;
            this.f443a = str;
            this.f442a = j;
            this.a = i;
            this.b = i2;
            this.f445b = str2;
            this.f444b = j2;
        }

        public boolean a(a aVar) {
            return TextUtils.equals(aVar.f443a, this.f443a) && TextUtils.equals(aVar.f445b, this.f445b) && aVar.a == this.a && aVar.b == this.b && Math.abs(aVar.f442a - this.f442a) <= 5000;
        }
    }

    public static int a(Context context) {
        if (a == -1) {
            a = b(context);
        }
        return a;
    }

    public static int a(String str) {
        try {
            return str.getBytes(Keyczar.DEFAULT_ENCODING).length;
        } catch (UnsupportedEncodingException unused) {
            return str.getBytes().length;
        }
    }

    public static long a(int i, long j, boolean z, long j2, boolean z2) {
        if (z && z2) {
            long j3 = f436a;
            f436a = j2;
            if (j2 - j3 > 30000 && j > FileSize.KB_COEFFICIENT) {
                return j * 2;
            }
        }
        return (j * (i == 0 ? 13 : 11)) / 10;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static com.xiaomi.push.providers.a m2218a(Context context) {
        com.xiaomi.push.providers.a aVar = f438a;
        if (aVar != null) {
            return aVar;
        }
        com.xiaomi.push.providers.a aVar2 = new com.xiaomi.push.providers.a(context);
        f438a = aVar2;
        return aVar2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized String m2219a(Context context) {
        synchronized (hb.class) {
            if (!TextUtils.isEmpty(f440a)) {
                return f440a;
            }
            return "";
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m2221a(Context context) {
        a = b(context);
    }

    public static void a(Context context, String str, long j, boolean z, long j2) {
        int a2;
        boolean isEmpty;
        if (context == null || TextUtils.isEmpty(str) || !com.xiaomi.stat.c.c.a.equals(context.getPackageName()) || com.xiaomi.stat.c.c.a.equals(str) || -1 == (a2 = a(context))) {
            return;
        }
        synchronized (f439a) {
            isEmpty = f441a.isEmpty();
            a(new a(str, j2, a2, z ? 1 : 0, a2 == 0 ? m2219a(context) : "", j));
        }
        if (!isEmpty) {
            return;
        }
        f437a.a(new hc(context), 5000L);
    }

    public static void a(Context context, String str, long j, boolean z, boolean z2, long j2) {
        a(context, str, a(a(context), j, z, j2, z2), z, j2);
    }

    public static void a(a aVar) {
        for (a aVar2 : f441a) {
            if (aVar2.a(aVar)) {
                aVar2.f444b += aVar.f444b;
                return;
            }
        }
        f441a.add(aVar);
    }

    public static int b(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return -1;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.getType();
            }
            return -1;
        } catch (Exception unused) {
            return -1;
        }
    }

    public static void b(Context context, List<a> list) {
        try {
            synchronized (com.xiaomi.push.providers.a.f805a) {
                SQLiteDatabase writableDatabase = m2218a(context).getWritableDatabase();
                writableDatabase.beginTransaction();
                for (a aVar : list) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(com.xiaomi.stat.d.am, aVar.f443a);
                    contentValues.put("message_ts", Long.valueOf(aVar.f442a));
                    contentValues.put("network_type", Integer.valueOf(aVar.a));
                    contentValues.put("bytes", Long.valueOf(aVar.f444b));
                    contentValues.put("rcv", Integer.valueOf(aVar.b));
                    contentValues.put("imsi", aVar.f445b);
                    writableDatabase.insert("traffic", null, contentValues);
                }
                writableDatabase.setTransactionSuccessful();
                writableDatabase.endTransaction();
            }
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.a(th);
        }
    }
}
