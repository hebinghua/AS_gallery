package com.baidu.location.b;

import java.io.File;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class i extends Thread {
    public final /* synthetic */ g a;

    public i(g gVar) {
        this.a = gVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.a.a(new File(com.baidu.location.e.j.i() + "/baidu/tempdata", "intime.dat"), "https://itsdata.map.baidu.com/long-conn-gps/sdk.php");
    }
}
