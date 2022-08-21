package com.baidu.mapsdkplatform.comapi.map;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.mapapi.common.BaiduMapSDKException;
import com.baidu.mapsdkplatform.comapi.NativeLoader;
import com.baidu.mapsdkplatform.comapi.commonutils.SysUpdateUtil;
import com.baidu.mapsdkplatform.comapi.util.SysUpdateObservable;
import com.baidu.platform.comapi.longlink.BNLongLink;
import com.baidu.platform.comjni.engine.NAEngine;

/* loaded from: classes.dex */
public class i {
    private static int a;
    private static Context b = BMapManager.getContext();

    static {
        if (!com.baidu.mapsdkplatform.comapi.c.a()) {
            NativeLoader.getInstance().loadLibrary(com.baidu.mapapi.VersionInfo.getKitName());
        }
        if (com.baidu.mapapi.VersionInfo.getApiVersion().equals(VersionInfo.getApiVersion())) {
            NativeLoader.getInstance().loadLibrary(VersionInfo.getKitName());
            return;
        }
        throw new BaiduMapSDKException("the version of map is not match with base");
    }

    public static void a() {
        if (a == 0) {
            if (b == null) {
                Log.e("BDMapSDK", "you have not supplyed the global app context info from SDKInitializer.initialize(Context) function.");
                return;
            }
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().b();
            com.baidu.platform.comapi.b.a((Application) b, true, false, false, true);
            com.baidu.platform.comapi.b.b();
            com.baidu.platform.comapi.b.c();
            com.baidu.platform.comapi.util.e.a();
            BNLongLink.initLongLink();
            NAEngine.c();
            NAEngine.startRunningRequest();
            SysUpdateObservable.getInstance().addObserver(new SysUpdateUtil());
            SysUpdateObservable.getInstance().init("");
        }
        a++;
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap init mRef = " + a);
        }
    }

    public static void b() {
        a--;
        if (OpenLogUtil.isMapLogEnable()) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().a("BasicMap destroy mRef = " + a);
        }
        if (a == 0) {
            com.baidu.mapsdkplatform.comapi.commonutils.b.a().c();
            com.baidu.platform.comapi.b.d();
        }
    }
}
