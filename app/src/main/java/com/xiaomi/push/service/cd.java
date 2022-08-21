package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public final class cd extends al.a {
    public final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Notification f945a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ Context f946a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f947a;
    public final /* synthetic */ String b;

    public cd(int i, String str, Context context, String str2, Notification notification) {
        this.a = i;
        this.f947a = str;
        this.f946a = context;
        this.b = str2;
        this.f945a = notification;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        String b;
        b = cc.b(this.a, this.f947a);
        return b;
    }

    @Override // java.lang.Runnable
    @TargetApi(19)
    public void run() {
        cc.c(this.f946a, this.b, this.a, this.f947a, this.f945a);
    }
}
