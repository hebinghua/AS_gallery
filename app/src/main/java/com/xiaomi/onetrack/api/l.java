package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.v;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class l implements Runnable {
    public final /* synthetic */ g a;

    public l(g gVar) {
        this.a = gVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        JSONObject d;
        Configuration configuration;
        OneTrack.IEventHook iEventHook;
        v vVar;
        d dVar;
        try {
            if (com.xiaomi.onetrack.util.ac.d(com.xiaomi.onetrack.util.aa.t())) {
                return;
            }
            com.xiaomi.onetrack.util.aa.m(System.currentTimeMillis());
            d = this.a.d("onetrack_dau");
            configuration = this.a.f;
            iEventHook = this.a.h;
            vVar = this.a.i;
            String a = c.a(configuration, iEventHook, d, vVar);
            dVar = this.a.b;
            dVar.a("onetrack_dau", a);
        } catch (Exception e) {
            p.b("OneTrackImp", "trackDau error  e:" + e.toString());
        }
    }
}
