package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;

/* loaded from: classes3.dex */
public class bq extends al.a {
    public Context a;

    public bq(Context context) {
        this.a = context;
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a */
    public String mo2050a() {
        return "100886";
    }

    @Override // com.xiaomi.push.al.a
    /* renamed from: a  reason: collision with other method in class */
    public final boolean mo2050a() {
        return com.xiaomi.clientreport.manager.a.a(this.a).m1864a().isEventUploadSwitchOpen();
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            if (!mo2050a()) {
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.c(this.a.getPackageName() + " begin upload event");
            com.xiaomi.clientreport.manager.a.a(this.a).m1866b();
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }
}
