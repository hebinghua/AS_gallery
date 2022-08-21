package com.baidu.mapsdkplatform.comapi.util;

import android.content.Context;

/* loaded from: classes.dex */
public class c {
    private int a;
    private Context b;

    /* loaded from: classes.dex */
    public static class a {
        private static final c a = new c();
    }

    private c() {
    }

    private int a(String str) {
        Context context = this.b;
        if (context == null) {
            return -101;
        }
        return context.getSharedPreferences("ad_auth", 0).getInt(str, 0);
    }

    public static c a() {
        return a.a;
    }

    private void a(String str, int i) {
        Context context = this.b;
        if (context == null) {
            return;
        }
        context.getSharedPreferences("ad_auth", 0).edit().putInt(str, i).apply();
    }

    public void a(int i) {
        if (i == -1 && (i = a("ad_key")) == -101) {
            return;
        }
        this.a = i;
        a("ad_key", i);
    }

    public void a(Context context) {
        this.b = context;
    }

    public boolean b() {
        int i = this.a;
        return i >= 0 && (i & 1) == 1;
    }

    public boolean c() {
        int i = this.a;
        return i >= 0 && (i & 1024) == 1024;
    }
}
