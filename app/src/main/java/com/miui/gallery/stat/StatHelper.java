package com.miui.gallery.stat;

import android.app.Activity;
import android.content.Context;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.xiaomi.micloudsdk.stat.IMiCloudStatCallback;
import com.xiaomi.micloudsdk.stat.MiCloudStatManager;
import com.xiaomi.stat.HttpEvent;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.MiStatParams;
import java.util.Map;
import miuix.os.Build;

/* loaded from: classes2.dex */
public class StatHelper {
    public static final boolean IS_ENABLED;
    public static boolean sInitialized;

    static {
        IS_ENABLED = !Build.IS_INTERNATIONAL_BUILD && !((StatsDependsModule) ModuleRegistry.getModule(StatsDependsModule.class)).isGlobalBuild();
    }

    public static void init(final Context context) {
        if (!IS_ENABLED || sInitialized || !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            return;
        }
        try {
            MiCloudStatManager.getInstance().init(new IMiCloudStatCallback() { // from class: com.miui.gallery.stat.StatHelper.1
                @Override // com.xiaomi.micloudsdk.stat.IMiCloudStatCallback
                public void onEnableAutoRecord() {
                }

                @Override // com.xiaomi.micloudsdk.stat.IMiCloudStatCallback
                public void onSetEventFilter() {
                }

                @Override // com.xiaomi.micloudsdk.stat.IMiCloudStatCallback
                public void onSetUploadPolicy() {
                }

                @Override // com.xiaomi.micloudsdk.stat.IMiCloudStatCallback
                public void onInitialize() {
                    try {
                        MiStat.initialize(context, "2882303761517492012", "5601749292012", false, "com.miui.gallery");
                        MiStat.setUseSystemUploadingService(true, true);
                        boolean unused = StatHelper.sInitialized = true;
                        MiStat.setExceptionCatcherEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override // com.xiaomi.micloudsdk.stat.IMiCloudStatCallback
                public void onAddHttpEvent(String str, long j, long j2, int i, String str2) {
                    MiStat.trackHttpEvent(new HttpEvent(str, j, j2, i, str2));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isUsable() {
        return IS_ENABLED && sInitialized;
    }

    public static MiStatParams constructMiStatParams(Map<String, String> map, String str) {
        MiStatParams miStatParams = new MiStatParams();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue() instanceof Integer) {
                    miStatParams.putInt(entry.getKey(), ((Integer) entry.getValue()).intValue());
                } else if (entry.getValue() instanceof Long) {
                    miStatParams.putLong(entry.getKey(), ((Long) entry.getValue()).longValue());
                } else if (entry.getValue() instanceof Double) {
                    miStatParams.putDouble(entry.getKey(), ((Double) entry.getValue()).doubleValue());
                } else if (entry.getValue() instanceof String) {
                    miStatParams.putString(entry.getKey(), entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    miStatParams.putBoolean(entry.getKey(), ((Boolean) entry.getValue()).booleanValue());
                }
            }
        }
        if (str != null) {
            miStatParams.putString("subevent", str);
        }
        return miStatParams;
    }

    public static void recordCountEvent(String str, String str2) {
        if (!isUsable()) {
            return;
        }
        try {
            MiStat.trackEvent(str, constructMiStatParams(null, str2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recordCountEvent(String str, String str2, Map<String, String> map) {
        if (!isUsable()) {
            return;
        }
        try {
            MiStat.trackEvent(str, constructMiStatParams(map, str2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recordPageEnd(Activity activity, String str) {
        if (!isUsable()) {
            return;
        }
        try {
            MiStat.trackPageEnd(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recordPageStart(Activity activity, String str) {
        if (!isUsable()) {
            return;
        }
        try {
            MiStat.trackPageStart(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackHttpEvent(String str, int i) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, 0L, -1L, i));
    }

    public static void trackHttpEvent(String str, long j, String str2) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, j, str2));
    }

    public static void trackHttpEvent(String str, long j, long j2) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, j, j2));
    }

    public static void trackHttpEvent(String str, long j, long j2, int i) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, j, j2, i));
    }

    public static void trackHttpEvent(String str, long j, long j2, int i, String str2) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, j, j2, i, str2));
    }

    public static void recordStringPropertyEvent(String str, String str2) {
        if (!isUsable()) {
            return;
        }
        try {
            MiStat.setUserProperty(str, str2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addHttpEvent(String str, String str2) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, -1L, str2));
    }

    public static void addHttpEvent(String str, long j, long j2, int i, String str2) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, j, j2, i, str2));
    }

    public static void addHttpEvent(String str, long j, long j2, int i) {
        if (!isUsable()) {
            return;
        }
        MiStat.trackHttpEvent(new HttpEvent(str, j, j2, i));
    }
}
