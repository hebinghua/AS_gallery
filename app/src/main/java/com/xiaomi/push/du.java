package com.xiaomi.push;

import android.content.Context;

/* loaded from: classes3.dex */
public class du extends dt {
    public du(Context context, int i) {
        super(context, i);
    }

    @Override // com.xiaomi.push.dt, com.xiaomi.push.al.a
    /* renamed from: a */
    public hl mo2050a() {
        return hl.Storage;
    }

    @Override // com.xiaomi.push.dt, com.xiaomi.push.al.a
    /* renamed from: a  reason: collision with other method in class */
    public String mo2050a() {
        return "23";
    }

    @Override // com.xiaomi.push.dt
    public String b() {
        return "ram:" + j.m2362a() + ",rom:" + j.m2365b() + "|ramOriginal:" + j.m2367c() + ",romOriginal:" + j.d();
    }
}
