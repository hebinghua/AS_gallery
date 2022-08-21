package com.baidu.lbsapi.auth;

import com.baidu.lbsapi.auth.e;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class l implements e.a<String> {
    public final /* synthetic */ String a;
    public final /* synthetic */ LBSAuthManager b;

    public l(LBSAuthManager lBSAuthManager, String str) {
        this.b = lBSAuthManager;
        this.a = str;
    }

    @Override // com.baidu.lbsapi.auth.e.a
    public void a(String str) {
        this.b.a(str, this.a);
    }
}
