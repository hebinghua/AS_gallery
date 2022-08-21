package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.ho;
import com.xiaomi.push.service.ba;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class f implements AbstractPushManager {
    public static volatile f a;

    /* renamed from: a  reason: collision with other field name */
    public Context f69a;

    /* renamed from: a  reason: collision with other field name */
    public PushConfiguration f70a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f72a = false;

    /* renamed from: a  reason: collision with other field name */
    public Map<e, AbstractPushManager> f71a = new HashMap();

    public f(Context context) {
        this.f69a = context.getApplicationContext();
    }

    public static f a(Context context) {
        if (a == null) {
            synchronized (f.class) {
                if (a == null) {
                    a = new f(context);
                }
            }
        }
        return a;
    }

    public AbstractPushManager a(e eVar) {
        return this.f71a.get(eVar);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x018b  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x01af  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01c3  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:70:? A[ADDED_TO_REGION, RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void a() {
        /*
            Method dump skipped, instructions count: 500
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.f.a():void");
    }

    public void a(PushConfiguration pushConfiguration) {
        this.f70a = pushConfiguration;
        this.f72a = ba.a(this.f69a).a(ho.AggregatePushSwitch.a(), true);
        if (this.f70a.getOpenHmsPush() || this.f70a.getOpenFCMPush() || this.f70a.getOpenCOSPush() || this.f70a.getOpenFTOSPush()) {
            ba.a(this.f69a).a(new g(this, 101, "assemblePush"));
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1920a(e eVar) {
        this.f71a.remove(eVar);
    }

    public void a(e eVar, AbstractPushManager abstractPushManager) {
        if (abstractPushManager != null) {
            if (this.f71a.containsKey(eVar)) {
                this.f71a.remove(eVar);
            }
            this.f71a.put(eVar, abstractPushManager);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m1921a(e eVar) {
        return this.f71a.containsKey(eVar);
    }

    public boolean b(e eVar) {
        int i = h.a[eVar.ordinal()];
        boolean z = false;
        if (i == 1) {
            PushConfiguration pushConfiguration = this.f70a;
            if (pushConfiguration == null) {
                return false;
            }
            return pushConfiguration.getOpenHmsPush();
        } else if (i == 2) {
            PushConfiguration pushConfiguration2 = this.f70a;
            if (pushConfiguration2 == null) {
                return false;
            }
            return pushConfiguration2.getOpenFCMPush();
        } else {
            if (i == 3) {
                PushConfiguration pushConfiguration3 = this.f70a;
                if (pushConfiguration3 != null) {
                    z = pushConfiguration3.getOpenCOSPush();
                }
            } else if (i != 4) {
                return false;
            }
            PushConfiguration pushConfiguration4 = this.f70a;
            return pushConfiguration4 != null ? pushConfiguration4.getOpenFTOSPush() : z;
        }
    }

    @Override // com.xiaomi.mipush.sdk.AbstractPushManager
    public void register() {
        com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : assemble push register");
        if (this.f71a.size() <= 0) {
            a();
        }
        if (this.f71a.size() > 0) {
            for (AbstractPushManager abstractPushManager : this.f71a.values()) {
                if (abstractPushManager != null) {
                    abstractPushManager.register();
                }
            }
            i.a(this.f69a);
        }
    }

    @Override // com.xiaomi.mipush.sdk.AbstractPushManager
    public void unregister() {
        com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : assemble push unregister");
        for (AbstractPushManager abstractPushManager : this.f71a.values()) {
            if (abstractPushManager != null) {
                abstractPushManager.unregister();
            }
        }
        this.f71a.clear();
    }
}
