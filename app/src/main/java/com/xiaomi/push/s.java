package com.xiaomi.push;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes3.dex */
public class s implements Runnable {
    public final /* synthetic */ r a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f811a;
    public final /* synthetic */ String b;
    public final /* synthetic */ String c;

    public s(r rVar, String str, String str2, String str3) {
        this.a = rVar;
        this.f811a = str;
        this.b = str2;
        this.c = str3;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        context = this.a.f808a;
        SharedPreferences.Editor edit = context.getSharedPreferences(this.f811a, 4).edit();
        edit.putString(this.b, this.c);
        edit.commit();
    }
}
