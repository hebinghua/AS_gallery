package com.baidu.platform.comapi.pano;

import com.baidu.mapapi.http.HttpClient;
import com.baidu.platform.comapi.pano.a;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class b extends HttpClient.ProtoResultCallback {
    public final /* synthetic */ a.InterfaceC0024a a;
    public final /* synthetic */ a b;

    public b(a aVar, a.InterfaceC0024a interfaceC0024a) {
        this.b = aVar;
        this.a = interfaceC0024a;
    }

    @Override // com.baidu.mapapi.http.HttpClient.ProtoResultCallback
    public void onFailed(HttpClient.HttpStateError httpStateError) {
        this.a.a(httpStateError);
    }

    @Override // com.baidu.mapapi.http.HttpClient.ProtoResultCallback
    public void onSuccess(String str) {
        c a;
        a.InterfaceC0024a interfaceC0024a = this.a;
        a = this.b.a(str);
        interfaceC0024a.a((a.InterfaceC0024a) a);
    }
}
