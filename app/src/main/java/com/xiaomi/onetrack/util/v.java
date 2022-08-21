package com.xiaomi.onetrack.util;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.e.a;

/* loaded from: classes3.dex */
public class v {
    public OneTrack.IEventHook f;
    public Configuration g;
    public boolean h;
    public boolean i;
    public long j = 0;

    public v(Configuration configuration) {
        this.g = configuration;
        this.h = aa.k(r.a(configuration));
    }

    public boolean a(String str) {
        boolean b;
        String str2 = "open";
        if (this.g.isUseCustomPrivacyPolicy()) {
            StringBuilder sb = new StringBuilder();
            sb.append("use custom privacy policy, the policy is ");
            if (!this.h) {
                str2 = "close";
            }
            sb.append(str2);
            p.a("PrivacyManager", sb.toString());
            b = this.h;
        } else {
            b = b();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("use system experience plan, the policy is ");
            if (!b) {
                str2 = "close";
            }
            sb2.append(str2);
            p.a("PrivacyManager", sb2.toString());
        }
        if (!b) {
            boolean b2 = b(str);
            boolean c = c(str);
            boolean d = d(str);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("This event ");
            sb3.append(str);
            sb3.append(b2 ? " is " : " is not ");
            sb3.append("basic event and ");
            String str3 = "is";
            sb3.append(c ? str3 : "is not");
            sb3.append(" recommend event and ");
            if (!d) {
                str3 = "is not";
            }
            sb3.append(str3);
            sb3.append(" custom dau event");
            p.a("PrivacyManager", sb3.toString());
            return b2 || c || d;
        }
        return b;
    }

    public final boolean b(String str) {
        return "onetrack_dau".equals(str) || "onetrack_pa".equals(str);
    }

    public final boolean c(String str) {
        OneTrack.IEventHook iEventHook = this.f;
        return iEventHook != null && iEventHook.isRecommendEvent(str);
    }

    public final boolean d(String str) {
        OneTrack.IEventHook iEventHook = this.f;
        return iEventHook != null && iEventHook.isCustomDauEvent(str);
    }

    public void a(OneTrack.IEventHook iEventHook) {
        this.f = iEventHook;
    }

    public String a() {
        return this.g.isUseCustomPrivacyPolicy() ? this.h ? "custom_open" : "custom_close" : b() ? "exprience_open" : "exprience_close";
    }

    public final boolean b() {
        if (Math.abs(System.currentTimeMillis() - this.j) > 900000) {
            this.j = System.currentTimeMillis();
            this.i = q.b(a.b());
        }
        return this.i;
    }
}
