package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;
import ch.qos.logback.core.joran.action.Action;
import com.xiaomi.push.ao;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class df {
    public static volatile df a;

    /* renamed from: a  reason: collision with other field name */
    public Context f194a;

    /* renamed from: a  reason: collision with other field name */
    public final ConcurrentLinkedQueue<b> f195a;

    /* loaded from: classes3.dex */
    public class a extends b {
        public a() {
            super();
        }

        @Override // com.xiaomi.push.df.b, com.xiaomi.push.ao.b
        /* renamed from: b */
        public void mo2039b() {
            df.this.b();
        }
    }

    /* loaded from: classes3.dex */
    public class b extends ao.b {
        public long a = System.currentTimeMillis();

        public b() {
        }

        @Override // com.xiaomi.push.ao.b
        public boolean a() {
            return true;
        }

        @Override // com.xiaomi.push.ao.b
        /* renamed from: b */
        public void mo2039b() {
        }

        @Override // com.xiaomi.push.ao.b
        /* renamed from: b  reason: collision with other method in class */
        public final boolean mo2039b() {
            return System.currentTimeMillis() - this.a > 172800000;
        }
    }

    /* loaded from: classes3.dex */
    public class c extends b {
        public int a;

        /* renamed from: a  reason: collision with other field name */
        public File f197a;

        /* renamed from: a  reason: collision with other field name */
        public String f198a;

        /* renamed from: a  reason: collision with other field name */
        public boolean f199a;
        public String b;

        /* renamed from: b  reason: collision with other field name */
        public boolean f200b;

        public c(String str, String str2, File file, boolean z) {
            super();
            this.f198a = str;
            this.b = str2;
            this.f197a = file;
            this.f200b = z;
        }

        @Override // com.xiaomi.push.df.b, com.xiaomi.push.ao.b
        public boolean a() {
            return bj.e(df.this.f194a) || (this.f200b && bj.b(df.this.f194a));
        }

        @Override // com.xiaomi.push.df.b, com.xiaomi.push.ao.b
        /* renamed from: b */
        public void mo2039b() {
            try {
                if (mo2040c()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("uid", com.xiaomi.push.service.bv.m2505a());
                    hashMap.put("token", this.b);
                    hashMap.put("net", bj.m1969a(df.this.f194a));
                    bj.a(this.f198a, hashMap, this.f197a, Action.FILE_ATTRIBUTE);
                }
                this.f199a = true;
            } catch (IOException unused) {
            }
        }

        @Override // com.xiaomi.push.ao.b
        /* renamed from: c */
        public void mo2040c() {
            if (!this.f199a) {
                int i = this.a + 1;
                this.a = i;
                if (i < 3) {
                    df.this.f195a.add(this);
                }
            }
            if (this.f199a || this.a >= 3) {
                this.f197a.delete();
            }
            df.this.a((1 << this.a) * 1000);
        }

        @Override // com.xiaomi.push.ao.b
        /* renamed from: c  reason: collision with other method in class */
        public final boolean mo2040c() {
            int i;
            int i2 = 0;
            SharedPreferences sharedPreferences = df.this.f194a.getSharedPreferences("log.timestamp", 0);
            String string = sharedPreferences.getString("log.requst", "");
            long currentTimeMillis = System.currentTimeMillis();
            try {
                JSONObject jSONObject = new JSONObject(string);
                currentTimeMillis = jSONObject.getLong(com.xiaomi.stat.b.j);
                i = jSONObject.getInt("times");
            } catch (JSONException unused) {
                i = 0;
            }
            if (System.currentTimeMillis() - currentTimeMillis >= 86400000) {
                currentTimeMillis = System.currentTimeMillis();
            } else if (i > 10) {
                return false;
            } else {
                i2 = i;
            }
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put(com.xiaomi.stat.b.j, currentTimeMillis);
                jSONObject2.put("times", i2 + 1);
                sharedPreferences.edit().putString("log.requst", jSONObject2.toString()).commit();
            } catch (JSONException e) {
                com.xiaomi.channel.commonutils.logger.b.c("JSONException on put " + e.getMessage());
            }
            return true;
        }
    }

    public df(Context context) {
        ConcurrentLinkedQueue<b> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
        this.f195a = concurrentLinkedQueue;
        this.f194a = context;
        concurrentLinkedQueue.add(new a());
        b(0L);
    }

    public static df a(Context context) {
        if (a == null) {
            synchronized (df.class) {
                if (a == null) {
                    a = new df(context);
                }
            }
        }
        a.f194a = context;
        return a;
    }

    public void a() {
        c();
        a(0L);
    }

    public final void a(long j) {
        b peek = this.f195a.peek();
        if (peek == null || !peek.a()) {
            return;
        }
        b(j);
    }

    public void a(String str, String str2, Date date, Date date2, int i, boolean z) {
        this.f195a.add(new dg(this, i, date, date2, str, str2, z));
        b(0L);
    }

    public final void b() {
        if (ad.b() || ad.m1931a()) {
            return;
        }
        try {
            File file = new File(this.f194a.getExternalFilesDir(null) + "/.logcache");
            if (!file.exists() || !file.isDirectory()) {
                return;
            }
            for (File file2 : file.listFiles()) {
                file2.delete();
            }
        } catch (NullPointerException unused) {
        }
    }

    public final void b(long j) {
        if (!this.f195a.isEmpty()) {
            gz.a(new dh(this), j);
        }
    }

    public final void c() {
        while (!this.f195a.isEmpty()) {
            b peek = this.f195a.peek();
            if (peek != null) {
                if (!peek.mo2039b() && this.f195a.size() <= 6) {
                    return;
                }
                com.xiaomi.channel.commonutils.logger.b.c("remove Expired task");
                this.f195a.remove(peek);
            }
        }
    }
}
