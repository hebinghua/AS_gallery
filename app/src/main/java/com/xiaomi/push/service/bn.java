package com.xiaomi.push.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;

/* loaded from: classes3.dex */
public class bn {
    public static bn a;

    /* renamed from: a  reason: collision with other field name */
    public int f923a = 0;

    /* renamed from: a  reason: collision with other field name */
    public Context f924a;

    public bn(Context context) {
        this.f924a = context.getApplicationContext();
    }

    public static bn a(Context context) {
        if (a == null) {
            a = new bn(context);
        }
        return a;
    }

    @SuppressLint({"NewApi"})
    public int a() {
        int i = this.f923a;
        if (i != 0) {
            return i;
        }
        try {
            this.f923a = Settings.Global.getInt(this.f924a.getContentResolver(), "device_provisioned", 0);
        } catch (Exception unused) {
        }
        return this.f923a;
    }

    @SuppressLint({"NewApi"})
    /* renamed from: a  reason: collision with other method in class */
    public Uri m2500a() {
        return Settings.Global.getUriFor("device_provisioned");
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m2501a() {
        String str = com.xiaomi.push.ae.f83a;
        return str.contains("xmsf") || str.contains("xiaomi") || str.contains("miui");
    }
}
