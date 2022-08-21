package com.miui.gallery.util;

import android.content.Context;
import com.miui.gallery.base.R$string;

/* loaded from: classes2.dex */
public class FormatUtil {
    public static final int[] STORAGE_SPACE = {16, 32, 64, 128, 256, 512, 1024};

    public static int getDeviceSpaceRange(long j) {
        int i = 0;
        while (true) {
            int[] iArr = STORAGE_SPACE;
            if (i < iArr.length) {
                if (j < iArr[i]) {
                    return iArr[i];
                }
                i++;
            } else {
                return -1;
            }
        }
    }

    public static String formatFileSize(Context context, long j) {
        return miuix.text.utilities.ExtraTextUtils.formatFileSize(context, j);
    }

    public static String formatDuration(Context context, int i) {
        int i2 = i / 3600;
        int i3 = i2 * 3600;
        int i4 = (i - i3) / 60;
        int i5 = i - (i3 + (i4 * 60));
        return i2 == 0 ? String.format(context.getString(R$string.detail_ms), Integer.valueOf(i4), Integer.valueOf(i5)) : String.format(context.getString(R$string.detail_hms), Integer.valueOf(i2), Integer.valueOf(i4), Integer.valueOf(i5));
    }

    public static String formatVideoDuration(long j) {
        long j2;
        long j3;
        if (j < 0) {
            return null;
        }
        if (j >= 3600) {
            j2 = j / 3600;
            j -= 3600 * j2;
        } else {
            j2 = 0;
        }
        if (j >= 60) {
            j3 = j / 60;
            j -= 60 * j3;
        } else {
            j3 = 0;
        }
        StringBuilder sb = new StringBuilder();
        if (j2 > 0) {
            sb.append(String.format("%d", Long.valueOf(j2)));
            sb.append(":");
        }
        sb.append(String.format("%d", Long.valueOf(j3)));
        sb.append(":");
        if (j < 10) {
            sb.append(String.format("%d", 0));
        }
        sb.append(String.format("%d", Long.valueOf(j)));
        return sb.toString();
    }

    public static int formatMinutes(long j) {
        return (int) Math.ceil((((float) j) * 1.0f) / 60000.0f);
    }
}
