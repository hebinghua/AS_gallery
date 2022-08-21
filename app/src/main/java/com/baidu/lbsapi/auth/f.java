package com.baidu.lbsapi.auth;

import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f implements Runnable {
    public final /* synthetic */ e a;

    public f(e eVar) {
        this.a = eVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        List list;
        e eVar = this.a;
        list = eVar.b;
        eVar.a(list);
    }
}
