package com.xiaomi.push;

import java.util.Date;

/* loaded from: classes3.dex */
public class fv implements fz {
    public final /* synthetic */ fu a;

    public fv(fu fuVar) {
        this.a = fuVar;
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar) {
        com.xiaomi.channel.commonutils.logger.b.c("[Slim] " + this.a.f385a.format(new Date()) + " Connection started (" + this.a.f382a.hashCode() + ")");
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar, int i, Exception exc) {
        com.xiaomi.channel.commonutils.logger.b.c("[Slim] " + this.a.f385a.format(new Date()) + " Connection closed (" + this.a.f382a.hashCode() + ")");
    }

    @Override // com.xiaomi.push.fz
    public void a(fw fwVar, Exception exc) {
        com.xiaomi.channel.commonutils.logger.b.c("[Slim] " + this.a.f385a.format(new Date()) + " Reconnection failed due to an exception (" + this.a.f382a.hashCode() + ")");
        exc.printStackTrace();
    }

    @Override // com.xiaomi.push.fz
    public void b(fw fwVar) {
        com.xiaomi.channel.commonutils.logger.b.c("[Slim] " + this.a.f385a.format(new Date()) + " Connection reconnected (" + this.a.f382a.hashCode() + ")");
    }
}
