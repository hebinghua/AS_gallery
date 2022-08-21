package com.miui.gallery.stat;

import android.app.Activity;
import com.miui.gallery.preference.BaseGalleryPreferences;
import java.util.Locale;
import java.util.Map;
import miuix.os.Build;

/* loaded from: classes2.dex */
public class SamplingStatHelper {
    public static final boolean FILTERED;

    static {
        FILTERED = BaseGalleryPreferences.SampleStatistic.getUniqId() % 9 != 0;
    }

    public static boolean isEnabled() {
        return !FILTERED && !Build.IS_INTERNATIONAL_BUILD;
    }

    public static String formatSampledCategoryKey(String str) {
        return String.format(Locale.US, "sample_%s", str);
    }

    public static void recordCountEvent(String str, String str2) {
        if (isEnabled()) {
            StatHelper.recordCountEvent(formatSampledCategoryKey(str), str2);
        }
    }

    public static void recordCountEvent(String str, String str2, Map<String, String> map) {
        if (isEnabled()) {
            StatHelper.recordCountEvent(formatSampledCategoryKey(str), str2, map);
        }
    }

    public static void recordStringPropertyEvent(String str, String str2) {
        if (isEnabled()) {
            StatHelper.recordStringPropertyEvent(formatSampledCategoryKey(str), str2);
        }
    }

    public static void recordPageEnd(Activity activity, String str) {
        if (isEnabled()) {
            StatHelper.recordPageEnd(activity, formatSampledCategoryKey(str));
        }
    }

    public static void recordPageStart(Activity activity, String str) {
        if (isEnabled()) {
            StatHelper.recordPageStart(activity, formatSampledCategoryKey(str));
        }
    }

    public static void trackHttpEvent(String str, int i) {
        if (isEnabled()) {
            StatHelper.trackHttpEvent(str, i);
        }
    }

    public static void trackHttpEvent(String str, long j, String str2) {
        if (isEnabled()) {
            StatHelper.trackHttpEvent(str, j, str2);
        }
    }

    public static void trackHttpEvent(String str, long j, long j2) {
        if (isEnabled()) {
            StatHelper.trackHttpEvent(str, j, j2);
        }
    }

    public static void trackHttpEvent(String str, long j, long j2, int i) {
        if (isEnabled()) {
            StatHelper.trackHttpEvent(str, j, j2, i);
        }
    }

    public static void trackHttpEvent(String str, long j, long j2, int i, String str2) {
        if (isEnabled()) {
            StatHelper.trackHttpEvent(str, j, j2, i, str2);
        }
    }

    public static int generatorValueStage(float f, int[] iArr) {
        for (int i : iArr) {
            if (f <= i) {
                return i;
            }
        }
        return -1;
    }

    public static String formatValueStage(float f, int[] iArr) {
        int generatorValueStage = generatorValueStage(f, iArr);
        return generatorValueStage != -1 ? String.format(Locale.US, "<=%d", Integer.valueOf(generatorValueStage)) : String.format(Locale.US, ">%d", Integer.valueOf(iArr[iArr.length - 1]));
    }
}
