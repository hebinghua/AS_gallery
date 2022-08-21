package com.xiaomi.push;

import android.os.SystemClock;
import android.text.TextUtils;
import com.xiaomi.push.fw;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.bg;

/* loaded from: classes3.dex */
public class fs extends gd {
    public fn a;

    /* renamed from: a  reason: collision with other field name */
    public fo f378a;

    /* renamed from: a  reason: collision with other field name */
    public Thread f379a;

    /* renamed from: a  reason: collision with other field name */
    public byte[] f380a;

    public fs(XMPushService xMPushService, fx fxVar) {
        super(xMPushService, fxVar);
    }

    @Override // com.xiaomi.push.gd
    /* renamed from: a */
    public final fl mo2171a(boolean z) {
        fr frVar = new fr();
        if (z) {
            frVar.a("1");
        }
        byte[] m2157a = fj.m2157a();
        if (m2157a != null) {
            dx$j dx_j = new dx$j();
            dx_j.a(a.a(m2157a));
            frVar.a(dx_j.a(), (String) null);
        }
        return frVar;
    }

    @Override // com.xiaomi.push.gd, com.xiaomi.push.fw
    /* renamed from: a */
    public synchronized void mo2190a() {
        h();
        this.f378a.a();
    }

    @Override // com.xiaomi.push.gd
    public synchronized void a(int i, Exception exc) {
        fn fnVar = this.a;
        if (fnVar != null) {
            fnVar.b();
            this.a = null;
        }
        fo foVar = this.f378a;
        if (foVar != null) {
            try {
                foVar.b();
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a(e);
            }
            this.f378a = null;
        }
        this.f380a = null;
        super.a(i, exc);
    }

    public void a(fl flVar) {
        if (flVar == null) {
            return;
        }
        if (flVar.m2161a()) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("[Slim] RCV blob chid=" + flVar.a() + "; id=" + flVar.e() + "; errCode=" + flVar.b() + "; err=" + flVar.m2165c());
        }
        if (flVar.a() == 0) {
            if ("PING".equals(flVar.m2158a())) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("[Slim] RCV ping id=" + flVar.e());
                g();
            } else if ("CLOSE".equals(flVar.m2158a())) {
                c(13, null);
            }
        }
        for (fw.a aVar : ((fw) this).f397a.values()) {
            aVar.a(flVar);
        }
    }

    @Override // com.xiaomi.push.fw
    @Deprecated
    public void a(gn gnVar) {
        b(fl.a(gnVar, (String) null));
    }

    @Override // com.xiaomi.push.fw
    public synchronized void a(bg.b bVar) {
        fk.a(bVar, c(), this);
    }

    @Override // com.xiaomi.push.fw
    public synchronized void a(String str, String str2) {
        fk.a(str, str2, this);
    }

    @Override // com.xiaomi.push.gd
    /* renamed from: a  reason: collision with other method in class */
    public void mo2171a(boolean z) {
        if (this.f378a != null) {
            fl mo2171a = mo2171a(z);
            com.xiaomi.channel.commonutils.logger.b.m1859a("[Slim] SND ping id=" + mo2171a.e());
            b(mo2171a);
            f();
            return;
        }
        throw new gh("The BlobWriter is null.");
    }

    @Override // com.xiaomi.push.fw
    public void a(fl[] flVarArr) {
        for (fl flVar : flVarArr) {
            b(flVar);
        }
    }

    @Override // com.xiaomi.push.gd, com.xiaomi.push.fw
    /* renamed from: a  reason: collision with other method in class */
    public boolean mo2190a() {
        return true;
    }

    @Override // com.xiaomi.push.gd, com.xiaomi.push.fw
    /* renamed from: a */
    public synchronized byte[] mo2190a() {
        if (this.f380a == null && !TextUtils.isEmpty(((fw) this).f394a)) {
            String m2505a = com.xiaomi.push.service.bv.m2505a();
            StringBuilder sb = new StringBuilder();
            String str = ((fw) this).f394a;
            sb.append(str.substring(str.length() / 2));
            sb.append(m2505a.substring(m2505a.length() / 2));
            this.f380a = com.xiaomi.push.service.bp.a(((fw) this).f394a.getBytes(), sb.toString().getBytes());
        }
        return this.f380a;
    }

    @Override // com.xiaomi.push.fw
    public void b(fl flVar) {
        fo foVar = this.f378a;
        if (foVar != null) {
            try {
                int a = foVar.a(flVar);
                ((fw) this).d = SystemClock.elapsedRealtime();
                String f = flVar.f();
                if (!TextUtils.isEmpty(f)) {
                    hb.a(((fw) this).f393a, f, a, false, true, System.currentTimeMillis());
                }
                for (fw.a aVar : ((fw) this).f400b.values()) {
                    aVar.a(flVar);
                }
                return;
            } catch (Exception e) {
                throw new gh(e);
            }
        }
        throw new gh("the writer is null.");
    }

    public void b(gn gnVar) {
        if (gnVar == null) {
            return;
        }
        for (fw.a aVar : ((fw) this).f397a.values()) {
            aVar.a(gnVar);
        }
    }

    public final void h() {
        try {
            this.a = new fn(((gd) this).f407a.getInputStream(), this, ((fw) this).f393a);
            this.f378a = new fo(((gd) this).f407a.getOutputStream(), this);
            ft ftVar = new ft(this, "Blob Reader (" + ((fw) this).b + ")");
            this.f379a = ftVar;
            ftVar.start();
        } catch (Exception e) {
            throw new gh("Error to init reader and writer", e);
        }
    }
}
