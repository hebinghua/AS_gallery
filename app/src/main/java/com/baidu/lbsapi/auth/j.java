package com.baidu.lbsapi.auth;

import android.content.Context;
import java.util.Hashtable;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class j implements Runnable {
    public final /* synthetic */ int a;
    public final /* synthetic */ boolean b;
    public final /* synthetic */ String c;
    public final /* synthetic */ String d;
    public final /* synthetic */ Hashtable e;
    public final /* synthetic */ LBSAuthManager f;

    public j(LBSAuthManager lBSAuthManager, int i, boolean z, String str, String str2, Hashtable hashtable) {
        this.f = lBSAuthManager;
        this.a = i;
        this.b = z;
        this.c = str;
        this.d = str2;
        this.e = hashtable;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean b;
        String[] strArr;
        Context context;
        String[] b2;
        boolean b3;
        m mVar;
        m mVar2;
        StringBuilder sb = new StringBuilder();
        sb.append("status = ");
        sb.append(this.a);
        sb.append("; forced = ");
        sb.append(this.b);
        sb.append("checkAK = ");
        b = this.f.b(this.c);
        sb.append(b);
        a.a(sb.toString());
        int i = this.a;
        if (i != 601 && !this.b && i != -1) {
            b3 = this.f.b(this.c);
            if (!b3) {
                if (602 == this.a) {
                    a.a("authenticate wait ");
                    mVar = LBSAuthManager.d;
                    if (mVar != null) {
                        mVar2 = LBSAuthManager.d;
                        mVar2.b();
                    }
                } else {
                    a.a("authenticate else");
                }
                this.f.a((String) null, this.c);
                return;
            }
        }
        a.a("authenticate sendAuthRequest");
        strArr = LBSAuthManager.l;
        if (strArr != null) {
            b2 = LBSAuthManager.l;
        } else {
            context = LBSAuthManager.a;
            b2 = b.b(context);
        }
        String[] strArr2 = b2;
        if (strArr2 == null || strArr2.length <= 1) {
            this.f.a(this.b, this.d, this.e, this.c);
            return;
        }
        a.a("authStrings.length:" + strArr2.length);
        a.a("more sha1 auth");
        this.f.a(this.b, this.d, this.e, strArr2, this.c);
    }
}
