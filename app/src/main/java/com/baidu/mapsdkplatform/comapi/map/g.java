package com.baidu.mapsdkplatform.comapi.map;

import android.content.Context;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapsdkplatform.comapi.commonutils.a.c;
import com.baidu.mapsdkplatform.comapi.map.e;
import java.io.File;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class g implements c.InterfaceC0016c {
    public final /* synthetic */ Context a;
    public final /* synthetic */ String b;
    public final /* synthetic */ e.a c;
    public final /* synthetic */ String d;
    public final /* synthetic */ String e;
    public final /* synthetic */ e f;

    public g(e eVar, Context context, String str, e.a aVar, String str2, String str3) {
        this.f = eVar;
        this.a = context;
        this.b = str;
        this.c = aVar;
        this.d = str2;
        this.e = str3;
    }

    @Override // com.baidu.mapsdkplatform.comapi.commonutils.a.c.InterfaceC0016c
    public void a() {
        e.a aVar = this.c;
        if (aVar != null) {
            aVar.a(HttpClient.HttpStateError.INNER_ERROR.ordinal(), "loadStyleFile onFailed", null);
        }
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("CustomMap loadStyleFile failed");
        }
    }

    @Override // com.baidu.mapsdkplatform.comapi.commonutils.a.c.InterfaceC0016c
    public void a(File file) {
        boolean a;
        boolean a2;
        a = this.f.a(this.a, file, this.b);
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b a3 = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
            a3.a("CustomMap loadStyleFile success ret = " + a);
        }
        String str = null;
        if (!a) {
            e.a aVar = this.c;
            if (aVar == null) {
                return;
            }
            aVar.a(HttpClient.HttpStateError.INNER_ERROR.ordinal(), "UnZipStyleFile onFailed", null);
            return;
        }
        this.f.a(this.a, this.b, this.d);
        if (this.c == null) {
            return;
        }
        a2 = this.f.a(this.e);
        if (a2) {
            str = this.e;
        }
        this.c.a(true, str);
    }
}
