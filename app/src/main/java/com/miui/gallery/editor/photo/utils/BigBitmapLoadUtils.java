package com.miui.gallery.editor.photo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import ch.qos.logback.core.util.FileSize;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class BigBitmapLoadUtils {
    public static final String[] sSpecialWhiteList = {"davinci", "davinciin"};

    public static boolean isHR108(int i, int i2) {
        return i * i2 >= 105910000;
    }

    public static int getMaxMemory(Context context) {
        int largeMemoryClass = ((ActivityManager) context.getSystemService("activity")).getLargeMemoryClass();
        DefaultLogger.d("BigBitmapLoadUtils", "max memory:%dM", Integer.valueOf(largeMemoryClass));
        return largeMemoryClass;
    }

    public static long getPhoneTotalMem(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        DefaultLogger.d("BigBitmapLoadUtils", "phone total memory:%d", Long.valueOf(memoryInfo.totalMem));
        return memoryInfo.totalMem;
    }

    public static boolean isMemoryBeyond3G(Context context) {
        return getPhoneTotalMem(context) > 3435973836L;
    }

    public static boolean isMemoryReach6G(Context context) {
        return getPhoneTotalMem(context) > 5690831667L;
    }

    public static boolean isSpecialBigMemoryDevices() {
        for (String str : sSpecialWhiteList) {
            if (str.equals(Build.DEVICE)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBigMemoryApp(Context context) {
        return isSpecialBigMemoryDevices() || getMaxMemory(context) >= 512;
    }

    public static int calculateInSampleSize(Context context, int i, int i2) {
        int i3;
        long j = i * i2 * 4;
        if (Build.VERSION.SDK_INT < 26) {
            long maxMemory = ((getMaxMemory(context) * FileSize.KB_COEFFICIENT) * FileSize.KB_COEFFICIENT) / 3;
            i3 = 1;
            while (true) {
                long j2 = i3;
                if (maxMemory >= (j / j2) / j2) {
                    break;
                }
                i3 *= 2;
            }
        } else {
            long j3 = 96000000;
            if (j > 96000000) {
                if (isMemoryReach6G(context)) {
                    j3 = 436000000;
                } else if (isMemoryBeyond3G(context) && isBigMemoryApp(context)) {
                    j3 = 260000000;
                }
                i3 = 1;
                while (true) {
                    long j4 = i3;
                    if (j3 >= (j / j4) / j4) {
                        break;
                    }
                    i3 *= 2;
                }
            } else {
                i3 = 1;
            }
        }
        if (i3 != 1) {
            DefaultLogger.d("BigBitmapLoadUtils", "decoding original bitmap,inSampleSize:%d", Integer.valueOf(i3));
            HashMap hashMap = new HashMap();
            hashMap.put("in_sample_size", String.valueOf(i3));
            hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, String.format("%dk*%dk", Integer.valueOf(i / 1000), Integer.valueOf(i2 / 1000)));
            hashMap.put("model", Build.MODEL);
            SamplingStatHelper.recordCountEvent("photo_editor", "decode_resize", hashMap);
        }
        return i3;
    }
}
