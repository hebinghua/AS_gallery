package com.xiaomi.push.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.push.gc;
import com.xiaomi.push.hg;
import com.xiaomi.push.hn;
import com.xiaomi.push.ho;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class o {
    @SuppressLint({"StaticFieldLeak"})
    public static volatile o a;

    /* renamed from: a  reason: collision with other field name */
    public long f961a;

    /* renamed from: a  reason: collision with other field name */
    public final Context f962a;

    /* renamed from: a  reason: collision with other field name */
    public final SharedPreferences f963a;

    /* renamed from: b  reason: collision with other field name */
    public final boolean f968b;

    /* renamed from: c  reason: collision with other field name */
    public final boolean f969c;

    /* renamed from: a  reason: collision with other field name */
    public final AtomicInteger f965a = new AtomicInteger(0);

    /* renamed from: a  reason: collision with other field name */
    public String f964a = null;

    /* renamed from: a  reason: collision with other field name */
    public volatile boolean f966a = false;
    public String b = null;

    /* renamed from: b  reason: collision with other field name */
    public final AtomicInteger f967b = new AtomicInteger(0);
    public final AtomicInteger c = new AtomicInteger(0);

    /* renamed from: a  reason: collision with other field name */
    public int f960a = -1;

    /* loaded from: classes3.dex */
    public static class a {
        public static String a() {
            return "support_wifi_digest";
        }

        public static String a(String str) {
            return String.format("HB_%s", str);
        }

        public static String b() {
            return "record_support_wifi_digest_reported";
        }

        public static String b(String str) {
            return String.format("HB_dead_time_%s", str);
        }

        public static String c() {
            return "record_hb_count_start";
        }

        public static String d() {
            return "record_short_hb_count";
        }

        public static String e() {
            return "record_long_hb_count";
        }

        public static String f() {
            return "record_hb_change";
        }

        public static String g() {
            return "record_mobile_ptc";
        }

        public static String h() {
            return "record_wifi_ptc";
        }

        public static String i() {
            return "record_ptc_start";
        }

        public static String j() {
            return "keep_short_hb_effective_time";
        }
    }

    public o(Context context) {
        this.f962a = context;
        this.f969c = com.xiaomi.push.m.m2400a(context);
        this.f968b = ba.a(context).a(ho.IntelligentHeartbeatSwitchBoolean.a(), true);
        SharedPreferences sharedPreferences = context.getSharedPreferences("hb_record", 0);
        this.f963a = sharedPreferences;
        long currentTimeMillis = System.currentTimeMillis();
        if (sharedPreferences.getLong(a.c(), -1L) == -1) {
            sharedPreferences.edit().putLong(a.c(), currentTimeMillis).apply();
        }
        long j = sharedPreferences.getLong(a.i(), -1L);
        this.f961a = j;
        if (j == -1) {
            this.f961a = currentTimeMillis;
            sharedPreferences.edit().putLong(a.i(), currentTimeMillis).apply();
        }
    }

    public static o a(Context context) {
        if (a == null) {
            synchronized (o.class) {
                if (a == null) {
                    a = new o(context);
                }
            }
        }
        return a;
    }

    public final int a() {
        if (!TextUtils.isEmpty(this.f964a)) {
            try {
                return this.f963a.getInt(a.a(this.f964a), -1);
            } catch (Throwable unused) {
                return -1;
            }
        }
        return -1;
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m2520a() {
        int a2;
        long b = gc.b();
        boolean z = true;
        if (this.f969c && !m2525b() && ((ba.a(this.f962a).a(ho.IntelligentHeartbeatSwitchBoolean.a(), true) || b() >= System.currentTimeMillis()) && (a2 = a()) != -1)) {
            b = a2;
        }
        if (!TextUtils.isEmpty(this.f964a) && !"WIFI-ID-UNKNOWN".equals(this.f964a) && this.f960a == 1) {
            if (b >= 300000) {
                z = false;
            }
            a(z);
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] ping interval:" + b);
        return b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m2521a() {
    }

    public void a(int i) {
        this.f963a.edit().putLong(a.j(), System.currentTimeMillis() + (i * 1000)).apply();
    }

    public synchronized void a(NetworkInfo networkInfo) {
        if (m2528d()) {
            String str = null;
            if (networkInfo == null) {
                b(null);
            } else if (networkInfo.getType() == 0) {
                String subtypeName = networkInfo.getSubtypeName();
                if (!TextUtils.isEmpty(subtypeName) && !"UNKNOWN".equalsIgnoreCase(subtypeName)) {
                    str = "M-" + subtypeName;
                }
                b(str);
                this.f960a = 0;
            } else {
                if (networkInfo.getType() != 1 && networkInfo.getType() != 6) {
                    b(null);
                }
                b("WIFI-ID-UNKNOWN");
                this.f960a = 1;
            }
            this.f960a = -1;
        }
    }

    public synchronized void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            e();
        }
        if (m2528d() && !TextUtils.isEmpty(str)) {
            b("W-" + str);
        }
    }

    public final void a(String str, String str2, Map<String, String> map) {
        hn hnVar = new hn();
        hnVar.d(str);
        hnVar.c("hb_name");
        hnVar.a("hb_channel");
        hnVar.a(1L);
        hnVar.b(str2);
        hnVar.a(false);
        hnVar.b(System.currentTimeMillis());
        hnVar.g(this.f962a.getPackageName());
        hnVar.e(com.xiaomi.stat.c.c.a);
        if (map == null) {
            map = new HashMap<>();
        }
        String str3 = null;
        t m2542a = u.m2542a(this.f962a);
        if (m2542a != null && !TextUtils.isEmpty(m2542a.f983a)) {
            String[] split = m2542a.f983a.split("@");
            if (split.length > 0) {
                str3 = split[0];
            }
        }
        map.put(nexExportFormat.TAG_FORMAT_UUID, str3);
        map.put("model", Build.MODEL);
        Context context = this.f962a;
        map.put("avc", String.valueOf(com.xiaomi.push.h.a(context, context.getPackageName())));
        map.put("pvc", String.valueOf(40091));
        map.put("cvc", String.valueOf(48));
        hnVar.a(map);
        hg a2 = hg.a(this.f962a);
        if (a2 != null) {
            a2.a(hnVar, this.f962a.getPackageName());
        }
    }

    public final void a(boolean z) {
        if (!m2527c()) {
            return;
        }
        int incrementAndGet = (z ? this.f967b : this.c).incrementAndGet();
        Object[] objArr = new Object[2];
        String str = "short";
        objArr[0] = z ? str : "long";
        objArr[1] = Integer.valueOf(incrementAndGet);
        com.xiaomi.channel.commonutils.logger.b.b(String.format("[HB] %s ping interval count: %s", objArr));
        if (incrementAndGet < 5) {
            return;
        }
        String d = z ? a.d() : a.e();
        int i = this.f963a.getInt(d, 0) + incrementAndGet;
        this.f963a.edit().putInt(d, i).apply();
        Object[] objArr2 = new Object[2];
        if (!z) {
            str = "long";
        }
        objArr2[0] = str;
        objArr2[1] = Integer.valueOf(i);
        com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("[HB] accumulate %s hb count(%s) and write to file. ", objArr2));
        (z ? this.f967b : this.c).set(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m2522a() {
        return this.f965a.get() >= Math.max(ba.a(this.f962a).a(ho.IntelligentHeartbeatNATCountInt.a(), 5), 3);
    }

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m2523a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith("W-") || str.startsWith("M-");
    }

    public final long b() {
        return this.f963a.getLong(a.j(), -1L);
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m2524b() {
        if (m2528d()) {
            f();
            if (!this.f966a || TextUtils.isEmpty(this.f964a) || !this.f964a.equals(this.b)) {
                return;
            }
            this.f965a.getAndIncrement();
            com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] ping timeout count:" + this.f965a);
            if (!m2522a()) {
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] change hb interval for net:" + this.f964a);
            c(this.f964a);
            this.f966a = false;
            this.f965a.getAndSet(0);
            d(this.f964a);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void b(java.lang.String r10) {
        /*
            r9 = this;
            java.lang.String r0 = "WIFI-ID-UNKNOWN"
            boolean r0 = r0.equals(r10)
            if (r0 == 0) goto L16
            java.lang.String r10 = r9.f964a
            if (r10 == 0) goto L15
            java.lang.String r0 = "W-"
            boolean r10 = r10.startsWith(r0)
            if (r10 == 0) goto L15
            goto L18
        L15:
            r10 = 0
        L16:
            r9.f964a = r10
        L18:
            android.content.SharedPreferences r10 = r9.f963a
            java.lang.String r0 = r9.f964a
            java.lang.String r0 = com.xiaomi.push.service.o.a.a(r0)
            r1 = -1
            int r10 = r10.getInt(r0, r1)
            android.content.SharedPreferences r0 = r9.f963a
            java.lang.String r2 = r9.f964a
            java.lang.String r2 = com.xiaomi.push.service.o.a.b(r2)
            r3 = -1
            long r5 = r0.getLong(r2, r3)
            long r7 = java.lang.System.currentTimeMillis()
            if (r10 == r1) goto L75
            int r10 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r10 != 0) goto L56
            android.content.SharedPreferences r10 = r9.f963a
            android.content.SharedPreferences$Editor r10 = r10.edit()
            java.lang.String r0 = r9.f964a
            java.lang.String r0 = com.xiaomi.push.service.o.a.b(r0)
            long r2 = r9.c()
            long r7 = r7 + r2
            android.content.SharedPreferences$Editor r10 = r10.putLong(r0, r7)
        L52:
            r10.apply()
            goto L75
        L56:
            int r10 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1))
            if (r10 <= 0) goto L75
            android.content.SharedPreferences r10 = r9.f963a
            android.content.SharedPreferences$Editor r10 = r10.edit()
            java.lang.String r0 = r9.f964a
            java.lang.String r0 = com.xiaomi.push.service.o.a.a(r0)
            android.content.SharedPreferences$Editor r10 = r10.remove(r0)
            java.lang.String r0 = r9.f964a
            java.lang.String r0 = com.xiaomi.push.service.o.a.b(r0)
            android.content.SharedPreferences$Editor r10 = r10.remove(r0)
            goto L52
        L75:
            java.util.concurrent.atomic.AtomicInteger r10 = r9.f965a
            r0 = 0
            r10.getAndSet(r0)
            java.lang.String r10 = r9.f964a
            boolean r10 = android.text.TextUtils.isEmpty(r10)
            r2 = 1
            if (r10 != 0) goto L8e
            int r10 = r9.a()
            if (r10 == r1) goto L8b
            goto L8e
        L8b:
            r9.f966a = r2
            goto L90
        L8e:
            r9.f966a = r0
        L90:
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.String r1 = r9.f964a
            r10[r0] = r1
            boolean r0 = r9.f966a
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r10[r2] = r0
            java.lang.String r0 = "[HB] network changed, netid:%s, %s"
            java.lang.String r10 = java.lang.String.format(r0, r10)
            com.xiaomi.channel.commonutils.logger.b.m1859a(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.o.b(java.lang.String):void");
    }

    /* renamed from: b  reason: collision with other method in class */
    public final boolean m2525b() {
        return !TextUtils.isEmpty(this.f964a) && this.f964a.startsWith("M-") && !ba.a(this.f962a).a(ho.IntelligentHeartbeatUseInMobileNetworkBoolean.a(), false);
    }

    public final long c() {
        return ba.a(this.f962a).a(ho.ShortHeartbeatEffectivePeriodMsLong.a(), 777600000L);
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m2526c() {
        if (m2528d()) {
            this.b = this.f964a;
        }
    }

    public final void c(String str) {
        if (!m2523a(str)) {
            return;
        }
        this.f963a.edit().putInt(a.a(str), 235000).apply();
        this.f963a.edit().putLong(a.b(this.f964a), System.currentTimeMillis() + c()).apply();
    }

    /* renamed from: c  reason: collision with other method in class */
    public final boolean m2527c() {
        return m2528d() && ba.a(this.f962a).a(ho.IntelligentHeartbeatDataCollectSwitchBoolean.a(), true) && com.xiaomi.push.q.China.name().equals(com.xiaomi.push.service.a.a(this.f962a).a());
    }

    public void d() {
        if (m2528d()) {
            g();
            if (!this.f966a) {
                return;
            }
            this.f965a.getAndSet(0);
        }
    }

    public final void d(String str) {
        String str2;
        String str3;
        if (m2527c() && !TextUtils.isEmpty(str)) {
            if (str.startsWith("W-")) {
                str2 = "W";
            } else if (!str.startsWith("M-")) {
                return;
            } else {
                str2 = "M";
            }
            String valueOf = String.valueOf(235000);
            String valueOf2 = String.valueOf(System.currentTimeMillis() / 1000);
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(":::");
            sb.append(str2);
            sb.append(":::");
            sb.append(valueOf);
            sb.append(":::");
            sb.append(valueOf2);
            String string = this.f963a.getString(a.f(), null);
            if (TextUtils.isEmpty(string)) {
                str3 = sb.toString();
            } else {
                str3 = string + "###" + sb.toString();
            }
            this.f963a.edit().putString(a.f(), str3).apply();
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public final boolean m2528d() {
        return this.f969c && (this.f968b || ((b() > System.currentTimeMillis() ? 1 : (b() == System.currentTimeMillis() ? 0 : -1)) >= 0));
    }

    public final void e() {
        if (!this.f963a.getBoolean(a.a(), false)) {
            this.f963a.edit().putBoolean(a.a(), true).apply();
        }
    }

    /* renamed from: e  reason: collision with other method in class */
    public final boolean m2529e() {
        long j = this.f963a.getLong(a.c(), -1L);
        if (j == -1) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        return j > currentTimeMillis || currentTimeMillis - j >= 259200000;
    }

    public final void f() {
        int i = this.f960a;
        String h = i != 0 ? i != 1 ? null : a.h() : a.g();
        if (!TextUtils.isEmpty(h)) {
            if (this.f963a.getLong(a.i(), -1L) == -1) {
                this.f961a = System.currentTimeMillis();
                this.f963a.edit().putLong(a.i(), this.f961a).apply();
            }
            this.f963a.edit().putInt(h, this.f963a.getInt(h, 0) + 1).apply();
        }
    }

    /* renamed from: f  reason: collision with other method in class */
    public final boolean m2530f() {
        if (this.f961a == -1) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        long j = this.f961a;
        return j > currentTimeMillis || currentTimeMillis - j >= 259200000;
    }

    public final void g() {
        int i;
        String[] split;
        String[] split2;
        if (m2527c()) {
            String string = this.f963a.getString(a.f(), null);
            char c = 1;
            char c2 = 0;
            if (!TextUtils.isEmpty(string) && (split = string.split("###")) != null) {
                int i2 = 0;
                while (i2 < split.length) {
                    if (!TextUtils.isEmpty(split[i2]) && (split2 = split[i2].split(":::")) != null && split2.length >= 4) {
                        String str = split2[c2];
                        String str2 = split2[c];
                        String str3 = split2[2];
                        String str4 = split2[3];
                        HashMap hashMap = new HashMap();
                        hashMap.put("event", "change");
                        hashMap.put("model", Build.MODEL);
                        hashMap.put("net_type", str2);
                        hashMap.put("net_name", str);
                        hashMap.put("interval", str3);
                        hashMap.put("timestamp", str4);
                        a("category_hb_change", null, hashMap);
                        com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] report hb changed events.");
                    }
                    i2++;
                    c = 1;
                    c2 = 0;
                }
                this.f963a.edit().remove(a.f()).apply();
            }
            if (this.f963a.getBoolean(a.a(), false) && !this.f963a.getBoolean(a.b(), false)) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("event", "support");
                hashMap2.put("model", Build.MODEL);
                hashMap2.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
                a("category_hb_change", null, hashMap2);
                com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] report support wifi digest events.");
                this.f963a.edit().putBoolean(a.b(), true).apply();
            }
            if (m2529e()) {
                int i3 = this.f963a.getInt(a.d(), 0);
                int i4 = this.f963a.getInt(a.e(), 0);
                if (i3 > 0 || i4 > 0) {
                    long j = this.f963a.getLong(a.c(), -1L);
                    String valueOf = String.valueOf(235000);
                    String valueOf2 = String.valueOf(j);
                    String valueOf3 = String.valueOf(System.currentTimeMillis());
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("interval", valueOf);
                        jSONObject.put("c_short", String.valueOf(i3));
                        jSONObject.put("c_long", String.valueOf(i4));
                        jSONObject.put(MiStat.Param.COUNT, String.valueOf(i3 + i4));
                        jSONObject.put("start_time", valueOf2);
                        jSONObject.put("end_time", valueOf3);
                        String jSONObject2 = jSONObject.toString();
                        HashMap hashMap3 = new HashMap();
                        hashMap3.put("event", "long_and_short_hb_count");
                        a("category_hb_count", jSONObject2, hashMap3);
                        com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] report short/long hb count events.");
                    } catch (Throwable unused) {
                    }
                }
                this.f963a.edit().putInt(a.d(), 0).putInt(a.e(), 0).putLong(a.c(), System.currentTimeMillis()).apply();
            }
            if (!m2530f()) {
                return;
            }
            String valueOf4 = String.valueOf(this.f961a);
            String valueOf5 = String.valueOf(System.currentTimeMillis());
            int i5 = this.f963a.getInt(a.g(), 0);
            if (i5 > 0) {
                try {
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("net_type", "M");
                    jSONObject3.put("ptc", i5);
                    jSONObject3.put("start_time", valueOf4);
                    jSONObject3.put("end_time", valueOf5);
                    String jSONObject4 = jSONObject3.toString();
                    HashMap hashMap4 = new HashMap();
                    hashMap4.put("event", "ptc_event");
                    a("category_lc_ptc", jSONObject4, hashMap4);
                    com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] report ping timeout count events of mobile network.");
                    this.f963a.edit().putInt(a.g(), 0).apply();
                } catch (Throwable unused2) {
                    i = 0;
                    this.f963a.edit().putInt(a.g(), 0).apply();
                }
            }
            i = 0;
            int i6 = this.f963a.getInt(a.h(), i);
            if (i6 > 0) {
                try {
                    JSONObject jSONObject5 = new JSONObject();
                    jSONObject5.put("net_type", "W");
                    jSONObject5.put("ptc", i6);
                    jSONObject5.put("start_time", valueOf4);
                    jSONObject5.put("end_time", valueOf5);
                    String jSONObject6 = jSONObject5.toString();
                    HashMap hashMap5 = new HashMap();
                    hashMap5.put("event", "ptc_event");
                    a("category_lc_ptc", jSONObject6, hashMap5);
                    com.xiaomi.channel.commonutils.logger.b.m1859a("[HB] report ping timeout count events of wifi network.");
                } catch (Throwable unused3) {
                }
                this.f963a.edit().putInt(a.h(), 0).apply();
            }
            this.f961a = System.currentTimeMillis();
            this.f963a.edit().putLong(a.i(), this.f961a).apply();
        }
    }
}
