package com.baidu.mapsdkplatform.comapi.commonutils;

import android.text.TextUtils;
import com.baidu.mapapi.OpenLogUtil;
import com.baidu.platform.comapi.util.SysOSUtil;
import com.baidu.platform.comapi.util.i;
import com.baidu.platform.comjni.engine.NAEngine;

/* loaded from: classes.dex */
public class b {
    private static boolean a = true;
    private static boolean b = false;

    /* loaded from: classes.dex */
    public enum a {
        eMonitorConsole(1),
        eMonitorNative(2),
        eMonitorNet(4);
        
        private int d;

        a(int i) {
            this.d = 0;
            this.d = i;
        }

        public int a() {
            return this.d;
        }
    }

    /* renamed from: com.baidu.mapsdkplatform.comapi.commonutils.b$b  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public enum EnumC0017b {
        eNone,
        eMonitorVerbose,
        eMonitorDebug,
        eMonitorInfo,
        eMonitorWarn,
        eMonitorError,
        eMonitorRealTime
    }

    /* loaded from: classes.dex */
    public static class c {
        private static final b a = new b(null);
    }

    /* loaded from: classes.dex */
    public enum d {
        SDK_MAP,
        Net,
        Engine
    }

    private b() {
    }

    public /* synthetic */ b(com.baidu.mapsdkplatform.comapi.commonutils.c cVar) {
        this();
    }

    public static b a() {
        return c.a;
    }

    private void a(EnumC0017b enumC0017b, String str, String str2) {
        if (!a) {
            return;
        }
        i.a().submit(new com.baidu.mapsdkplatform.comapi.commonutils.c(this, enumC0017b, str, str2));
    }

    private void d() {
        NAEngine.a(new String[]{d.SDK_MAP.name(), d.Engine.name()});
    }

    public void a(String str) {
        a(EnumC0017b.eMonitorRealTime, d.SDK_MAP.name(), str);
    }

    public void b() {
        boolean isMapLogEnable = OpenLogUtil.isMapLogEnable();
        a = isMapLogEnable;
        if (!isMapLogEnable || b) {
            return;
        }
        String mapLogFilePath = OpenLogUtil.getMapLogFilePath();
        if (TextUtils.isEmpty(mapLogFilePath)) {
            mapLogFilePath = SysOSUtil.getInstance().getExternalFilesDir();
        }
        NAEngine.a(false);
        NAEngine.a(mapLogFilePath);
        NAEngine.a(a.eMonitorNative.a());
        NAEngine.b(EnumC0017b.eMonitorError.ordinal());
        d();
        NAEngine.a(true);
        b = true;
    }

    public void c() {
        if (!a || !b) {
            return;
        }
        b = false;
        a = false;
        NAEngine.a(false);
    }
}
