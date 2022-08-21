package com.baidu.mapsdkplatform.comapi.commonutils;

import com.baidu.mapsdkplatform.comapi.commonutils.b;
import com.baidu.platform.comjni.engine.NAEngine;

/* loaded from: classes.dex */
class c implements Runnable {
    public final /* synthetic */ b.EnumC0017b a;
    public final /* synthetic */ String b;
    public final /* synthetic */ String c;
    public final /* synthetic */ b d;

    public c(b bVar, b.EnumC0017b enumC0017b, String str, String str2) {
        this.d = bVar;
        this.a = enumC0017b;
        this.b = str;
        this.c = str2;
    }

    @Override // java.lang.Runnable
    public void run() {
        NAEngine.a(this.a.ordinal(), this.b, this.c);
    }
}
