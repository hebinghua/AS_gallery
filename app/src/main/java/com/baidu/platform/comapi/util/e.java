package com.baidu.platform.comapi.util;

import android.os.Build;
import com.baidu.mapsdkplatform.comapi.util.SyncSysInfo;
import com.baidu.platform.comjni.map.commonmemcache.NACommonMemCache;
import com.baidu.vi.VIContext;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes.dex */
public class e {
    private static NACommonMemCache a = new NACommonMemCache();

    public static void a() {
        c();
    }

    public static NACommonMemCache b() {
        return a;
    }

    private static void c() {
        JsonBuilder jsonBuilder = new JsonBuilder();
        String str = Build.MODEL;
        a.a(SyncSysInfo.initPhoneInfo());
        jsonBuilder.object();
        jsonBuilder.putStringValue("pd", "map");
        jsonBuilder.putStringValue(com.xiaomi.stat.d.R, "Android" + Build.VERSION.SDK_INT);
        jsonBuilder.putStringValue("ver", "2");
        jsonBuilder.key("sw").value(SysOSUtil.getInstance().getScreenWidth());
        jsonBuilder.key("sh").value(SysOSUtil.getInstance().getScreenHeight());
        jsonBuilder.putStringValue("channel", "oem");
        jsonBuilder.putStringValue("mb", str);
        jsonBuilder.putStringValue(com.xiaomi.stat.d.b, SyncSysInfo.getSoftWareVer());
        jsonBuilder.putStringValue(com.xiaomi.stat.d.l, "android");
        jsonBuilder.putStringValue("cuid", SyncSysInfo.getCid());
        jsonBuilder.putStringValue(nexExportFormat.TAG_FORMAT_PATH, SysOSUtil.getInstance().getOutputDirPath() + "/udc/");
        jsonBuilder.endObject();
        a.a("logstatistics", jsonBuilder.getJson());
        jsonBuilder.reset();
        jsonBuilder.object();
        jsonBuilder.putStringValue("cuid", SyncSysInfo.getCid());
        jsonBuilder.putStringValue("app", "1");
        jsonBuilder.putStringValue(nexExportFormat.TAG_FORMAT_PATH, VIContext.getContext().getCacheDir().getAbsolutePath() + com.xiaomi.stat.b.h.g);
        jsonBuilder.putStringValue("domain", "");
        jsonBuilder.endObject();
        a.a("longlink", jsonBuilder.getJson());
    }
}
