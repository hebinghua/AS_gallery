package com.xiaomi.onetrack.api;

import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import com.xiaomi.onetrack.util.p;
import com.xiaomi.onetrack.util.v;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class ac implements Runnable {
    public final /* synthetic */ String a;
    public final /* synthetic */ String b;
    public final /* synthetic */ String c;
    public final /* synthetic */ String d;
    public final /* synthetic */ String e;
    public final /* synthetic */ long f;
    public final /* synthetic */ g g;

    public ac(g gVar, String str, String str2, String str3, String str4, String str5, long j) {
        this.g = gVar;
        this.a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = j;
    }

    @Override // java.lang.Runnable
    public void run() {
        JSONObject d;
        d dVar;
        Configuration configuration;
        OneTrack.IEventHook iEventHook;
        v vVar;
        try {
            d = this.g.d("onetrack_bug_report");
            dVar = this.g.b;
            String str = this.a;
            String str2 = this.b;
            String str3 = this.c;
            String str4 = this.d;
            String str5 = this.e;
            long j = this.f;
            configuration = this.g.f;
            iEventHook = this.g.h;
            vVar = this.g.i;
            dVar.a("onetrack_bug_report", c.a(str, str2, str3, str4, str5, j, configuration, iEventHook, d, vVar));
        } catch (Exception e) {
            p.b("OneTrackImp", "trackException error: " + e.toString());
        }
    }
}
