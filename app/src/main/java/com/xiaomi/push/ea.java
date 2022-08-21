package com.xiaomi.push;

import android.content.Context;

/* loaded from: classes3.dex */
public final class ea implements Runnable {
    public final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Context f287a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f288a;
    public final /* synthetic */ String b;

    public ea(Context context, String str, int i, String str2) {
        this.f287a = context;
        this.f288a = str;
        this.a = i;
        this.b = str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        dz.c(this.f287a, this.f288a, this.a, this.b);
    }
}
