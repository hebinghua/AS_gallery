package com.xiaomi.clientreport.manager;

import android.content.Context;
import com.xiaomi.clientreport.data.Config;
import com.xiaomi.clientreport.data.EventClientReport;
import com.xiaomi.clientreport.data.PerfClientReport;
import com.xiaomi.clientreport.processor.IEventProcessor;
import com.xiaomi.clientreport.processor.IPerfProcessor;
import com.xiaomi.push.al;
import com.xiaomi.push.bp;
import com.xiaomi.push.bq;
import com.xiaomi.push.br;
import com.xiaomi.push.bs;
import com.xiaomi.push.bt;
import com.xiaomi.push.bw;
import com.xiaomi.push.m;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public class a {
    public static final int a;

    /* renamed from: a  reason: collision with other field name */
    public static volatile a f8a;

    /* renamed from: a  reason: collision with other field name */
    public Context f9a;

    /* renamed from: a  reason: collision with other field name */
    public Config f10a;

    /* renamed from: a  reason: collision with other field name */
    public IEventProcessor f11a;

    /* renamed from: a  reason: collision with other field name */
    public IPerfProcessor f12a;

    /* renamed from: a  reason: collision with other field name */
    public String f13a;

    /* renamed from: a  reason: collision with other field name */
    public ExecutorService f15a = Executors.newSingleThreadExecutor();

    /* renamed from: a  reason: collision with other field name */
    public HashMap<String, HashMap<String, com.xiaomi.clientreport.data.a>> f14a = new HashMap<>();
    public HashMap<String, ArrayList<com.xiaomi.clientreport.data.a>> b = new HashMap<>();

    static {
        a = m.m2399a() ? 30 : 10;
    }

    public a(Context context) {
        this.f9a = context;
    }

    public static a a(Context context) {
        if (f8a == null) {
            synchronized (a.class) {
                if (f8a == null) {
                    f8a = new a(context);
                }
            }
        }
        return f8a;
    }

    public final int a() {
        HashMap<String, ArrayList<com.xiaomi.clientreport.data.a>> hashMap = this.b;
        if (hashMap != null) {
            int i = 0;
            for (String str : hashMap.keySet()) {
                ArrayList<com.xiaomi.clientreport.data.a> arrayList = this.b.get(str);
                i += arrayList != null ? arrayList.size() : 0;
            }
            return i;
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Config m1864a() {
        if (this.f10a == null) {
            this.f10a = Config.defaultConfig(this.f9a);
        }
        return this.f10a;
    }

    public EventClientReport a(int i, String str) {
        EventClientReport eventClientReport = new EventClientReport();
        eventClientReport.eventContent = str;
        eventClientReport.eventTime = System.currentTimeMillis();
        eventClientReport.eventType = i;
        eventClientReport.eventId = bp.a(6);
        eventClientReport.production = 1000;
        eventClientReport.reportType = 1001;
        eventClientReport.clientInterfaceId = "E100004";
        eventClientReport.setAppPackageName(this.f9a.getPackageName());
        eventClientReport.setSdkVersion(this.f13a);
        return eventClientReport;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m1865a() {
        a(this.f9a).f();
        a(this.f9a).g();
    }

    public void a(Config config, IEventProcessor iEventProcessor, IPerfProcessor iPerfProcessor) {
        this.f10a = config;
        this.f11a = iEventProcessor;
        this.f12a = iPerfProcessor;
        iEventProcessor.setEventMap(this.b);
        this.f12a.setPerfMap(this.f14a);
    }

    public void a(EventClientReport eventClientReport) {
        if (m1864a().isEventUploadSwitchOpen()) {
            this.f15a.execute(new b(this, eventClientReport));
        }
    }

    public void a(PerfClientReport perfClientReport) {
        if (m1864a().isPerfUploadSwitchOpen()) {
            this.f15a.execute(new c(this, perfClientReport));
        }
    }

    public final void a(al.a aVar, int i) {
        al.a(this.f9a).b(aVar, i);
    }

    public void a(String str) {
        this.f13a = str;
    }

    public void a(boolean z, boolean z2, long j, long j2) {
        Config config = this.f10a;
        if (config != null) {
            if (z == config.isEventUploadSwitchOpen() && z2 == this.f10a.isPerfUploadSwitchOpen() && j == this.f10a.getEventUploadFrequency() && j2 == this.f10a.getPerfUploadFrequency()) {
                return;
            }
            long eventUploadFrequency = this.f10a.getEventUploadFrequency();
            long perfUploadFrequency = this.f10a.getPerfUploadFrequency();
            Config build = Config.getBuilder().setAESKey(bt.a(this.f9a)).setEventEncrypted(this.f10a.isEventEncrypted()).setEventUploadSwitchOpen(z).setEventUploadFrequency(j).setPerfUploadSwitchOpen(z2).setPerfUploadFrequency(j2).build(this.f9a);
            this.f10a = build;
            if (!build.isEventUploadSwitchOpen()) {
                al.a(this.f9a).m1936a("100886");
            } else if (eventUploadFrequency != build.getEventUploadFrequency()) {
                com.xiaomi.channel.commonutils.logger.b.c(this.f9a.getPackageName() + "reset event job " + build.getEventUploadFrequency());
                f();
            }
            if (!this.f10a.isPerfUploadSwitchOpen()) {
                al.a(this.f9a).m1936a("100887");
            } else if (perfUploadFrequency == build.getPerfUploadFrequency()) {
            } else {
                com.xiaomi.channel.commonutils.logger.b.c(this.f9a.getPackageName() + " reset perf job " + build.getPerfUploadFrequency());
                g();
            }
        }
    }

    public final int b() {
        HashMap<String, HashMap<String, com.xiaomi.clientreport.data.a>> hashMap = this.f14a;
        int i = 0;
        if (hashMap != null) {
            for (String str : hashMap.keySet()) {
                HashMap<String, com.xiaomi.clientreport.data.a> hashMap2 = this.f14a.get(str);
                if (hashMap2 != null) {
                    for (String str2 : hashMap2.keySet()) {
                        com.xiaomi.clientreport.data.a aVar = hashMap2.get(str2);
                        if (aVar instanceof PerfClientReport) {
                            i = (int) (i + ((PerfClientReport) aVar).perfCounts);
                        }
                    }
                }
            }
        }
        return i;
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m1866b() {
        if (m1864a().isEventUploadSwitchOpen()) {
            bs bsVar = new bs();
            bsVar.a(this.f9a);
            bsVar.a(this.f11a);
            this.f15a.execute(bsVar);
        }
    }

    public final void b(EventClientReport eventClientReport) {
        IEventProcessor iEventProcessor = this.f11a;
        if (iEventProcessor != null) {
            iEventProcessor.mo1867a(eventClientReport);
            if (a() < 10) {
                a(new d(this), a);
                return;
            }
            d();
            al.a(this.f9a).m1936a("100888");
        }
    }

    public final void b(PerfClientReport perfClientReport) {
        IPerfProcessor iPerfProcessor = this.f12a;
        if (iPerfProcessor != null) {
            iPerfProcessor.mo1867a(perfClientReport);
            if (b() < 10) {
                a(new f(this), a);
                return;
            }
            e();
            al.a(this.f9a).m1936a("100889");
        }
    }

    public void c() {
        if (m1864a().isPerfUploadSwitchOpen()) {
            bs bsVar = new bs();
            bsVar.a(this.f12a);
            bsVar.a(this.f9a);
            this.f15a.execute(bsVar);
        }
    }

    public final void d() {
        try {
            this.f11a.b();
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.d("we: " + e.getMessage());
        }
    }

    public final void e() {
        try {
            this.f12a.b();
        } catch (Exception e) {
            com.xiaomi.channel.commonutils.logger.b.d("wp: " + e.getMessage());
        }
    }

    public final void f() {
        if (!a(this.f9a).m1864a().isEventUploadSwitchOpen()) {
            return;
        }
        bq bqVar = new bq(this.f9a);
        int eventUploadFrequency = (int) a(this.f9a).m1864a().getEventUploadFrequency();
        if (eventUploadFrequency < 1800) {
            eventUploadFrequency = 1800;
        }
        if (System.currentTimeMillis() - bw.a(this.f9a).a("sp_client_report_status", "event_last_upload_time", 0L) > eventUploadFrequency * 1000) {
            al.a(this.f9a).a(new h(this, bqVar), 10);
        }
        synchronized (a.class) {
            if (!al.a(this.f9a).a((al.a) bqVar, eventUploadFrequency)) {
                al.a(this.f9a).m1936a("100886");
                al.a(this.f9a).a((al.a) bqVar, eventUploadFrequency);
            }
        }
    }

    public final void g() {
        if (!a(this.f9a).m1864a().isPerfUploadSwitchOpen()) {
            return;
        }
        br brVar = new br(this.f9a);
        int perfUploadFrequency = (int) a(this.f9a).m1864a().getPerfUploadFrequency();
        if (perfUploadFrequency < 1800) {
            perfUploadFrequency = 1800;
        }
        if (System.currentTimeMillis() - bw.a(this.f9a).a("sp_client_report_status", "perf_last_upload_time", 0L) > perfUploadFrequency * 1000) {
            al.a(this.f9a).a(new i(this, brVar), 15);
        }
        synchronized (a.class) {
            if (!al.a(this.f9a).a((al.a) brVar, perfUploadFrequency)) {
                al.a(this.f9a).m1936a("100887");
                al.a(this.f9a).a((al.a) brVar, perfUploadFrequency);
            }
        }
    }
}
