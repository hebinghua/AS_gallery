package com.xiaomi.push.service;

import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.hj;
import com.xiaomi.push.hw;
import com.xiaomi.push.ii;
import com.xiaomi.push.it;
import com.xiaomi.push.service.XMPushService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class s extends XMPushService.j {
    public final /* synthetic */ r a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ String f981a;

    /* renamed from: a  reason: collision with other field name */
    public final /* synthetic */ List f982a;
    public final /* synthetic */ String b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public s(r rVar, int i, String str, List list, String str2) {
        super(i);
        this.a = rVar;
        this.f981a = str;
        this.f982a = list;
        this.b = str2;
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a */
    public String mo2550a() {
        return "Send tiny data.";
    }

    @Override // com.xiaomi.push.service.XMPushService.j
    /* renamed from: a  reason: collision with other method in class */
    public void mo2550a() {
        String a;
        XMPushService xMPushService;
        a = this.a.a(this.f981a);
        ArrayList<ii> a2 = bz.a(this.f982a, this.f981a, a, 32768);
        if (a2 == null) {
            com.xiaomi.channel.commonutils.logger.b.d("TinyData LongConnUploader.upload Get a null XmPushActionNotification list when TinyDataHelper.pack() in XMPushService.");
            return;
        }
        Iterator<ii> it = a2.iterator();
        while (it.hasNext()) {
            ii next = it.next();
            next.a("uploadWay", "longXMPushService");
            Cif a3 = ah.a(this.f981a, a, next, hj.Notification);
            if (!TextUtils.isEmpty(this.b) && !TextUtils.equals(this.f981a, this.b)) {
                if (a3.m2293a() == null) {
                    hw hwVar = new hw();
                    hwVar.a("-1");
                    a3.a(hwVar);
                }
                a3.m2293a().b("ext_traffic_source_pkg", this.b);
            }
            byte[] a4 = it.a(a3);
            xMPushService = this.a.a;
            xMPushService.a(this.f981a, a4, true);
        }
    }
}
