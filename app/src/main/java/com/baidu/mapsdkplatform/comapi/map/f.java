package com.baidu.mapsdkplatform.comapi.map;

import android.content.Context;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapsdkplatform.comapi.map.e;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class f extends HttpClient.ProtoResultCallback {
    public final /* synthetic */ Context a;
    public final /* synthetic */ String b;
    public final /* synthetic */ e.a c;
    public final /* synthetic */ e d;

    public f(e eVar, Context context, String str, e.a aVar) {
        this.d = eVar;
        this.a = context;
        this.b = str;
        this.c = aVar;
    }

    @Override // com.baidu.mapapi.http.HttpClient.ProtoResultCallback
    public void onFailed(HttpClient.HttpStateError httpStateError) {
        String a;
        boolean a2;
        a = this.d.a(this.a, this.b);
        a2 = this.d.a(a);
        if (!a2) {
            a = null;
        }
        e.a aVar = this.c;
        if (aVar != null) {
            aVar.a(httpStateError.ordinal(), httpStateError.name(), a);
        }
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b a3 = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
            a3.a("CustomMap failed error = " + httpStateError.ordinal());
        }
    }

    @Override // com.baidu.mapapi.http.HttpClient.ProtoResultCallback
    public void onSuccess(String str) {
        this.d.b(this.a, str, this.b, this.c);
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b a = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
            a.a("CustomMap result = " + str);
        }
    }
}
