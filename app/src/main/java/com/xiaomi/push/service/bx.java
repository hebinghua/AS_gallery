package com.xiaomi.push.service;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes3.dex */
public final class bx implements ar {
    public static volatile bx a;

    /* renamed from: a  reason: collision with other field name */
    public long f934a;

    /* renamed from: a  reason: collision with other field name */
    public Context f935a;

    /* renamed from: a  reason: collision with other field name */
    public SharedPreferences f936a;

    /* renamed from: a  reason: collision with other field name */
    public volatile boolean f938a = false;

    /* renamed from: a  reason: collision with other field name */
    public ConcurrentHashMap<String, a> f937a = new ConcurrentHashMap<>();

    /* loaded from: classes3.dex */
    public static abstract class a implements Runnable {
        public long a;

        /* renamed from: a  reason: collision with other field name */
        public String f939a;

        public a(String str, long j) {
            this.f939a = str;
            this.a = j;
        }

        public abstract void a(bx bxVar);

        @Override // java.lang.Runnable
        public void run() {
            if (bx.a != null) {
                Context context = bx.a.f935a;
                if (!com.xiaomi.push.bj.d(context)) {
                    return;
                }
                long currentTimeMillis = System.currentTimeMillis();
                SharedPreferences sharedPreferences = bx.a.f936a;
                if (currentTimeMillis - sharedPreferences.getLong(":ts-" + this.f939a, 0L) <= this.a && !com.xiaomi.push.ai.a(context)) {
                    return;
                }
                SharedPreferences.Editor edit = bx.a.f936a.edit();
                com.xiaomi.push.t.a(edit.putLong(":ts-" + this.f939a, System.currentTimeMillis()));
                a(bx.a);
            }
        }
    }

    public bx(Context context) {
        this.f935a = context.getApplicationContext();
        this.f936a = context.getSharedPreferences("sync", 0);
    }

    public static bx a(Context context) {
        if (a == null) {
            synchronized (bx.class) {
                if (a == null) {
                    a = new bx(context);
                }
            }
        }
        return a;
    }

    public String a(String str, String str2) {
        SharedPreferences sharedPreferences = this.f936a;
        return sharedPreferences.getString(str + ":" + str2, "");
    }

    @Override // com.xiaomi.push.service.ar
    /* renamed from: a  reason: collision with other method in class */
    public void mo2512a() {
        if (this.f938a) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.f934a < 3600000) {
            return;
        }
        this.f934a = currentTimeMillis;
        this.f938a = true;
        com.xiaomi.push.al.a(this.f935a).a(new by(this), (int) (Math.random() * 10.0d));
    }

    public void a(a aVar) {
        if (this.f937a.putIfAbsent(aVar.f939a, aVar) == null) {
            com.xiaomi.push.al.a(this.f935a).a(aVar, ((int) (Math.random() * 30.0d)) + 10);
        }
    }

    public void a(String str, String str2, String str3) {
        SharedPreferences.Editor edit = a.f936a.edit();
        com.xiaomi.push.t.a(edit.putString(str + ":" + str2, str3));
    }
}
