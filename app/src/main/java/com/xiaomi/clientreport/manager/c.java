package com.xiaomi.clientreport.manager;

import com.xiaomi.clientreport.data.PerfClientReport;

/* loaded from: classes3.dex */
public class c implements Runnable {
    public final /* synthetic */ PerfClientReport a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ a f17a;

    public c(a aVar, PerfClientReport perfClientReport) {
        this.f17a = aVar;
        this.a = perfClientReport;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f17a.b(this.a);
    }
}
