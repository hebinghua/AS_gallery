package com.miui.gallery.projection;

import com.milink.api.v1.MilinkClientManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ReflectUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ConnectControllerSingleton {
    public static IConnectController sInstance;

    public static synchronized IConnectController getInstance() {
        IConnectController iConnectController;
        synchronized (ConnectControllerSingleton.class) {
            if (sInstance == null) {
                if (isMilinkV2()) {
                    DefaultLogger.i("ConnectControllerHolder", "Use MiLink Service v2");
                    sInstance = new ConnectControllerImplV2();
                } else if (isMilinkV1()) {
                    DefaultLogger.i("ConnectControllerHolder", "Use MiLink Service v1");
                    sInstance = new ConnectControllerImplV1();
                } else {
                    DefaultLogger.e("ConnectControllerHolder", "Didn't find MiLink Service, fallback to empty implementation");
                    sInstance = new NullConnectControllerImpl();
                }
            }
            iConnectController = sInstance;
        }
        return iConnectController;
    }

    public static boolean isMilinkV1() {
        try {
            String str = MilinkClientManager.TAG;
            return true;
        } catch (Throwable th) {
            DefaultLogger.e("ConnectControllerHolder", th);
            return false;
        }
    }

    public static boolean isMilinkV2() {
        return ReflectUtils.getMethod("com.milink.api.v1.MilinkClientManager", "showScanList") != null && BaseMiscUtil.isPackageInstalled("com.milink.service");
    }
}
