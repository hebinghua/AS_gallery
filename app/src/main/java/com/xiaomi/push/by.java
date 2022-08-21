package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public class by extends al.a {
    public final /* synthetic */ bx a;

    public by(bx bxVar) {
        this.a = bxVar;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "10052";
    }

    @Override // java.lang.Runnable
    public void run() {
        cn cnVar;
        cn cnVar2;
        Context context;
        com.xiaomi.channel.commonutils.logger.b.c("exec== mUploadJob");
        cnVar = this.a.f142a;
        if (cnVar != null) {
            cnVar2 = this.a.f142a;
            context = this.a.f139a;
            cnVar2.a(context);
            this.a.b("upload_time");
        }
    }
}
