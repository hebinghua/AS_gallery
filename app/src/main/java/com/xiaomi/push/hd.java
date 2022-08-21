package com.xiaomi.push;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.service.XMPushService;
import java.io.File;

/* loaded from: classes3.dex */
public class hd implements XMPushService.n {
    public static boolean a = false;

    /* renamed from: a  reason: collision with other field name */
    public int f446a;

    /* renamed from: a  reason: collision with other field name */
    public Context f447a;
    public boolean b;

    public hd(Context context) {
        this.f447a = context;
    }

    public static void a(boolean z) {
        a = z;
    }

    public final String a(String str) {
        return com.xiaomi.stat.c.c.a.equals(str) ? "1000271" : this.f447a.getSharedPreferences("pref_registered_pkg_names", 0).getString(str, null);
    }

    @Override // com.xiaomi.push.service.XMPushService.n
    /* renamed from: a */
    public void mo2222a() {
        hh a2 = hg.a(this.f447a).a();
        if (hi.a(this.f447a) && a2 != null) {
            hf.a(this.f447a, a2, com.xiaomi.push.service.ca.f943a);
            com.xiaomi.push.service.ca.a();
            com.xiaomi.channel.commonutils.logger.b.c("coord data upload");
        }
        a(this.f447a);
        if (!this.b || !mo2222a()) {
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("TinyData TinyDataCacheProcessor.pingFollowUpAction ts:" + System.currentTimeMillis());
        if (a(a2)) {
            a = true;
            he.a(this.f447a, a2);
            return;
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a("TinyData TinyDataCacheProcessor.pingFollowUpAction !canUpload(uploader) ts:" + System.currentTimeMillis());
    }

    public final void a(Context context) {
        this.b = com.xiaomi.push.service.ba.a(context).a(ho.TinyDataUploadSwitch.a(), true);
        int a2 = com.xiaomi.push.service.ba.a(context).a(ho.TinyDataUploadFrequency.a(), 7200);
        this.f446a = a2;
        this.f446a = Math.max(60, a2);
    }

    @Override // com.xiaomi.push.service.XMPushService.n
    /* renamed from: a  reason: collision with other method in class */
    public final boolean mo2222a() {
        return Math.abs((System.currentTimeMillis() / 1000) - this.f447a.getSharedPreferences("mipush_extra", 4).getLong("last_tiny_data_upload_timestamp", -1L)) > ((long) this.f446a);
    }

    public final boolean a(hh hhVar) {
        if (bj.b(this.f447a) && hhVar != null && !TextUtils.isEmpty(a(this.f447a.getPackageName())) && new File(this.f447a.getFilesDir(), "tiny_data.data").exists() && !a) {
            return !com.xiaomi.push.service.ba.a(this.f447a).a(ho.ScreenOnOrChargingTinyDataUploadSwitch.a(), false) || j.m2364a(this.f447a) || j.m2366b(this.f447a);
        }
        return false;
    }
}
