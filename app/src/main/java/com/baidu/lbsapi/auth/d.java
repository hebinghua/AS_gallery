package com.baidu.lbsapi.auth;

import android.content.Context;
import java.util.HashMap;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class d implements Runnable {
    public final /* synthetic */ c a;

    public d(c cVar) {
        this.a = cVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        Context context;
        HashMap<String, String> hashMap;
        a.a("postWithHttps start Thread id = " + String.valueOf(Thread.currentThread().getId()));
        context = this.a.a;
        g gVar = new g(context);
        hashMap = this.a.b;
        this.a.a(gVar.a(hashMap));
    }
}
