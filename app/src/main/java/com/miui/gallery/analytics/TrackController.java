package com.miui.gallery.analytics;

import android.content.Context;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes.dex */
public class TrackController {
    public static final ITrackHelper IMPL;
    public static volatile boolean sInitialized;
    public static final ConcurrentHashMap<String, Map<String, Object>> sPendingMap = new ConcurrentHashMap<>();

    static {
        IMPL = ((AnalyticsDependsModule) ModuleRegistry.getModule(AnalyticsDependsModule.class)).isGlobalBuild() ? new FirebaseHelper() : new OneTrackHelper();
    }

    public static void init(Context context) {
        if (sInitialized) {
            return;
        }
        synchronized (TrackController.class) {
            IMPL.init(context);
            sInitialized = true;
            ConcurrentHashMap<String, Map<String, Object>> concurrentHashMap = sPendingMap;
            if (concurrentHashMap.size() > 0) {
                for (Map.Entry<String, Map<String, Object>> entry : concurrentHashMap.entrySet()) {
                    track(entry.getKey(), entry.getValue());
                }
                sPendingMap.clear();
            }
        }
    }

    public static void trackNav(String str, String str2) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        track("Jump", hashMap);
    }

    public static Map<String, Object> buildViewParams(String str, String str2) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        return hashMap;
    }

    public static Map<String, Object> buildViewParams(String str, String str2, int i) {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
        return hashMap;
    }

    public static Map<String, Object> buildViewParams(String str, String str2, String str3) {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.valueOf(str3));
        return hashMap;
    }

    public static void trackView(Map<String, Object> map) {
        track("view", map);
    }

    public static void trackExpose(String str) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        trackExpose(hashMap);
    }

    public static void trackExpose(String str, String str2) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        trackExpose(hashMap);
    }

    public static void trackExpose(Map<String, Object> map) {
        track("expose", map);
    }

    public static void trackDualFinger(Map<String, Object> map) {
        track("Dual_finger", map);
    }

    public static void trackClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        trackClick(hashMap);
    }

    public static void trackClick(String str, String str2) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        trackClick(hashMap);
    }

    public static void trackClick(String str, String str2, int i) {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
        trackClick(hashMap);
    }

    public static void trackClick(String str, String str2, String str3) {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str3);
        trackClick(hashMap);
    }

    public static void trackClick(Map<String, Object> map) {
        track(MiStat.Event.CLICK, map);
    }

    public static void trackDrag(String str) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        track("Drag", hashMap);
    }

    public static void trackFling(String str) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        track("flow", hashMap);
    }

    public static void trackFling(String str, String str2) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        track("flow", hashMap);
    }

    public static void trackStats(String str, long j) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put(MiStat.Param.COUNT, Long.valueOf(j));
        trackStats(hashMap);
    }

    public static void trackStats(String str, String str2, int i) {
        HashMap hashMap = new HashMap(4, 1.0f);
        hashMap.put("tip", str);
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str2);
        trackStats(hashMap);
    }

    public static void trackStats(Map<String, Object> map) {
        track("statistics", map);
    }

    public static void trackUserStatus(String str, String str2) {
        HashMap hashMap = new HashMap(2, 1.0f);
        hashMap.put("tip", str);
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str2);
        track("Status", hashMap);
    }

    public static void trackError(Map<String, Object> map) {
        track("error", map);
    }

    public static void trackTimeMonitor(String str, long j) {
        if (j <= 0) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put("duration", Long.valueOf(j));
        trackTimeMonitor(hashMap);
    }

    public static void trackTimeMonitor(String str, long j, long j2) {
        if (j2 <= 0) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put(MiStat.Param.COUNT, Long.valueOf(j));
        hashMap.put("duration", Long.valueOf(j2));
        trackTimeMonitor(hashMap);
    }

    public static void trackTimeMonitor(String str, String str2, long j, long j2) {
        if (j2 <= 0) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put("ref_tip", str2);
        hashMap.put(MiStat.Param.COUNT, Long.valueOf(j));
        hashMap.put("duration", Long.valueOf(j2));
        trackTimeMonitor(hashMap);
    }

    public static void trackTimeMonitor(String str, long j, long j2, long j3) {
        if (j3 <= 0) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put(MiStat.Param.COUNT, Long.valueOf(j));
        hashMap.put("count_extra", Long.valueOf(j2));
        hashMap.put("duration", Long.valueOf(j3));
        trackTimeMonitor(hashMap);
    }

    public static void trackTimeMonitor(Map<String, Object> map) {
        track("time_monitor", map);
    }

    public static void track(String str, Map<String, Object> map) {
        ITrackHelper iTrackHelper = IMPL;
        if (iTrackHelper == null || !sInitialized) {
            sPendingMap.put(str, map);
        } else {
            iTrackHelper.track(str, map);
        }
    }
}
