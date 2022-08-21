package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.v;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class n implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ long b;
    public final /* synthetic */ g c;

    public n(g gVar, String str, long j) {
        this.c = gVar;
        this.a = str;
        this.b = j;
    }

    @Override // java.lang.Runnable
    public void run() {
        Configuration configuration;
        JSONObject d;
        Configuration configuration2;
        OneTrack.IEventHook iEventHook;
        v vVar;
        try {
            configuration = this.c.f;
            if (configuration.isAutoTrackActivityAction()) {
                d = this.c.d("onetrack_pa");
                String str = this.a;
                long j = this.b;
                configuration2 = this.c.f;
                iEventHook = this.c.h;
                vVar = this.c.i;
                com.xiaomi.onetrack.util.aa.i(c.a(str, "onetrack_pa", j, configuration2, iEventHook, d, vVar));
                return;
            }
            p.a("OneTrackImp", "config.autoTrackActivityAction is false, ignore onetrack_pa pause event");
        } catch (Exception e) {
            p.b("OneTrackImp", "savePageEndData error:" + e.toString());
        }
    }
}
