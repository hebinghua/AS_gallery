package com.miui.gallery.vlog.base.stat;

import com.miui.gallery.stat.SamplingStatHelper;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class VlogStatUtils {
    public static void statLibraryDownloadResult(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("result", z ? "0" : "-1");
        SamplingStatHelper.recordCountEvent("vlog", "library", hashMap);
    }

    public static void statEvent(String str) {
        statEvent(str, "");
    }

    public static void statEvent(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("effect", str2);
        SamplingStatHelper.recordCountEvent("vlog", str, hashMap);
    }
}
