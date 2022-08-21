package com.miui.gallery.map.utils;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.net.library.LibraryLoaderHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class MapStatHelper {
    public static void trackViewMapError(LibraryLoaderHelper.Error error) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.61.0.1.15327");
        hashMap.put("ref_tip", AutoTracking.getRef());
        hashMap.put("error", String.valueOf(error));
        AutoTracking.trackView(hashMap);
    }

    public static void trackViewPrivacyPolicy(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.61.0.1.15329");
        hashMap.put("from", str);
        TrackController.trackClick(hashMap);
    }

    public static void trackPosition(boolean z, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.61.0.1.15328");
        hashMap.put("success", Boolean.valueOf(z));
        hashMap.put("error", String.valueOf(i));
        TrackController.trackClick(hashMap);
    }

    public static void trackShowOrHideNearby(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.61.0.1.16822");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, z ? "open" : "close");
        TrackController.trackClick(hashMap);
    }

    public static void trackClickMarker(int i) {
        HashMap hashMap = new HashMap();
        if (i == 1) {
            hashMap.put("tip", "403.61.0.1.15377");
        } else {
            hashMap.put("tip", "403.61.0.1.15330");
            hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        }
        TrackController.trackClick(hashMap);
    }
}
