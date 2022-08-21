package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public class br extends al.a {
    public Context a;

    public br(Context context) {
        this.a = context;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "100887";
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a  reason: collision with other method in class */
    public final boolean mo2050a() {
        return com.xiaomi.clientreport.manager.a.a(this.a).m1864a().isPerfUploadSwitchOpen();
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (!mo2050a()) {
                return;
            }
            com.xiaomi.clientreport.manager.a.a(this.a).c();
            com.xiaomi.channel.commonutils.logger.b.c(this.a.getPackageName() + " perf begin upload");
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.d("fail to send perf data. " + e);
        }
    }
}
