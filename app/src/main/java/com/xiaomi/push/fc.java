package com.xiaomi.push;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;

/* loaded from: classes3.dex */
public class fc implements bg.b.a {
    public int a;

    /* renamed from: a  reason: collision with other field name */
    public fw f337a;

    /* renamed from: a  reason: collision with other field name */
    public XMPushService f338a;

    /* renamed from: a  reason: collision with other field name */
    public bg.b f339a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f341a = false;

    /* renamed from: a  reason: collision with other field name */
    public bg.c f340a = bg.c.binding;

    public fc(XMPushService xMPushService, bg.b bVar) {
        this.f338a = xMPushService;
        this.f339a = bVar;
    }

    public void a() {
        this.f339a.a(this);
        this.f337a = this.f338a.a();
    }

    @Override // com.xiaomi.push.service.bg.b.a
    public void a(bg.c cVar, bg.c cVar2, int i) {
        if (!this.f341a && cVar == bg.c.binding) {
            this.f340a = cVar2;
            this.a = i;
            this.f341a = true;
        }
        this.f338a.a(new fd(this, 4));
    }

    public final void b() {
        this.f339a.b(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x005e  */
    /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void c() {
        /*
            r4 = this;
            r4.b()
            boolean r0 = r4.f341a
            if (r0 != 0) goto L8
            return
        L8:
            int r0 = r4.a
            r1 = 11
            if (r0 != r1) goto Lf
            return
        Lf:
            com.xiaomi.push.fh r0 = com.xiaomi.push.fh.m2151a()
            com.xiaomi.push.fa r0 = r0.m2152a()
            int[] r1 = com.xiaomi.push.fe.a
            com.xiaomi.push.service.bg$c r2 = r4.f340a
            int r2 = r2.ordinal()
            r1 = r1[r2]
            r2 = 1
            if (r1 == r2) goto L31
            r3 = 3
            if (r1 == r3) goto L28
            goto L5c
        L28:
            com.xiaomi.push.ez r1 = com.xiaomi.push.ez.BIND_SUCCESS
        L2a:
            int r1 = r1.a()
            r0.f323a = r1
            goto L5c
        L31:
            int r1 = r4.a
            r3 = 17
            if (r1 != r3) goto L3a
            com.xiaomi.push.ez r1 = com.xiaomi.push.ez.BIND_TCP_READ_TIMEOUT
            goto L2a
        L3a:
            r3 = 21
            if (r1 != r3) goto L41
            com.xiaomi.push.ez r1 = com.xiaomi.push.ez.BIND_TIMEOUT
            goto L2a
        L41:
            com.xiaomi.push.fg r1 = com.xiaomi.push.fh.a()     // Catch: java.lang.NullPointerException -> L5b
            java.lang.Exception r1 = r1.a()     // Catch: java.lang.NullPointerException -> L5b
            com.xiaomi.push.ff$a r1 = com.xiaomi.push.ff.c(r1)     // Catch: java.lang.NullPointerException -> L5b
            com.xiaomi.push.ez r3 = r1.a     // Catch: java.lang.NullPointerException -> L5b
            int r3 = r3.a()     // Catch: java.lang.NullPointerException -> L5b
            r0.f323a = r3     // Catch: java.lang.NullPointerException -> L5b
            java.lang.String r1 = r1.f342a     // Catch: java.lang.NullPointerException -> L5b
            r0.c(r1)     // Catch: java.lang.NullPointerException -> L5b
            goto L5c
        L5b:
            r0 = 0
        L5c:
            if (r0 == 0) goto L83
            com.xiaomi.push.fw r1 = r4.f337a
            java.lang.String r1 = r1.m2177a()
            r0.b(r1)
            com.xiaomi.push.service.bg$b r1 = r4.f339a
            java.lang.String r1 = r1.f916b
            r0.d(r1)
            r0.f326b = r2
            com.xiaomi.push.service.bg$b r1 = r4.f339a     // Catch: java.lang.NumberFormatException -> L7c
            java.lang.String r1 = r1.g     // Catch: java.lang.NumberFormatException -> L7c
            int r1 = java.lang.Integer.parseInt(r1)     // Catch: java.lang.NumberFormatException -> L7c
            byte r1 = (byte) r1     // Catch: java.lang.NumberFormatException -> L7c
            r0.a(r1)     // Catch: java.lang.NumberFormatException -> L7c
        L7c:
            com.xiaomi.push.fh r1 = com.xiaomi.push.fh.m2151a()
            r1.a(r0)
        L83:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fc.c():void");
    }
}
