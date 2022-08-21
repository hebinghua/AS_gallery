package com.miui.gallery.vlog.tools;

import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class DebugLogUtils {
    public static boolean HAS_LOADED_SELECT_HEADTAIL = false;
    public static boolean HAS_LOADED_SELECT_MUSIC = false;
    public static boolean HAS_LOADED_SELECT_TEMPLATE = false;
    public static boolean HAS_LOADED_SELECT_TRANS = false;
    public static boolean HAS_LOADED_TEMPLATE_DEFAULT = false;
    public static boolean IS_FIRST_FRAME_LOADED_INTO_VLOG = true;
    public static boolean IS_FIRST_FRAME_LOADED_SELECT_HEADTAIL = true;
    public static boolean IS_FIRST_FRAME_LOADED_SELECT_MUSIC = true;
    public static boolean IS_FIRST_FRAME_LOADED_SELECT_TEMPLATE = true;
    public static boolean IS_FIRST_FRAME_LOADED_SELECT_TRANS = true;
    public static long current_time;
    public static HashMap<String, Long> mTimeMap = new HashMap<>();

    public static void startDebugLog(String str, String str2) {
        current_time = System.currentTimeMillis();
        DefaultLogger.d(str, "DebugLogUtils start->%s", str2);
    }

    public static void endDebugLog(String str, String str2) {
        DefaultLogger.d(str, "DebugLogUtils end->%s costs %dms ", str2, Long.valueOf(System.currentTimeMillis() - current_time));
    }

    public static void startDebugLogSpecialTime(String str, String str2) {
        mTimeMap.put(str2, Long.valueOf(System.currentTimeMillis()));
        DefaultLogger.d(str, "DebugLogUtils start->%s", str2);
    }

    public static void endDebugLogSpecialTime(String str, String str2) {
        DefaultLogger.d(str, "DebugLogUtils end->%s costs %dms ", str2, Long.valueOf(System.currentTimeMillis() - (mTimeMap.get(str2) == null ? 0L : mTimeMap.get(str2).longValue())));
    }
}
