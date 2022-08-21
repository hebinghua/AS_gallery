package com.baidu.platform.comapi;

import android.content.Context;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.platform.comjni.engine.MessageProxy;
import com.baidu.platform.comjni.engine.NAEngine;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class a {
    public static boolean a = false;
    private NAEngine b;

    public boolean a() {
        a = false;
        return true;
    }

    public boolean a(Context context) {
        a = false;
        this.b = new NAEngine();
        boolean a2 = NAEngine.a(context, null);
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b a3 = com.baidu.mapsdkplatform.comapi.commonutils.b.a();
            a3.a("initEngine isEngineSuccess = " + a2);
        }
        if (!a2) {
            com.baidu.platform.comapi.c.a.a().a("engine_init_failed");
            return false;
        }
        return a2;
    }

    public void b() {
        if (a) {
            a();
        }
        MessageProxy.destroy();
        NAEngine.b();
    }
}
