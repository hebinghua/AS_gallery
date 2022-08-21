package com.miui.gallery.util;

import android.database.Cursor;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.Closeable;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class Utils {
    public static final boolean IS_DEBUG_BUILD;
    public static final LazyValue<Void, Pattern> SHARE_ALBUM_ID_PATTERN;
    public static long[] sCrcTable = new long[256];

    public static long clamp(long j, long j2, long j3) {
        return j > j3 ? j3 : j < j2 ? j2 : j;
    }

    public static String ensureNotNull(String str) {
        return str == null ? "" : str;
    }

    static {
        String str = Build.TYPE;
        IS_DEBUG_BUILD = str.equals("eng") || str.equals("userdebug");
        SHARE_ALBUM_ID_PATTERN = new LazyValue<Void, Pattern>() { // from class: com.miui.gallery.util.Utils.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public Pattern mo1272onInit(Void r1) {
                return Pattern.compile("(\\d+)(-\\d+)");
            }
        };
        for (int i = 0; i < 256; i++) {
            long j = i;
            for (int i2 = 0; i2 < 8; i2++) {
                j = (j >> 1) ^ ((((int) j) & 1) != 0 ? -7661587058870466123L : 0L);
            }
            sCrcTable[i] = j;
        }
    }

    public static void assertTrue(boolean z) {
        if (z) {
            return;
        }
        throw new AssertionError();
    }

    public static void assertTrue(boolean z, String str, Object... objArr) {
        if (!z) {
            if (objArr.length != 0) {
                str = String.format(str, objArr);
            }
            throw new AssertionError(str);
        }
    }

    public static boolean equals(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public static int prevPowerOf2(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException();
        }
        return Integer.highestOneBit(i);
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (Exception e) {
            DefaultLogger.w("Utils", "close fail", e);
        }
    }

    public static void closeSilently(ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor != null) {
            try {
                parcelFileDescriptor.close();
            } catch (Exception e) {
                DefaultLogger.w("Utils", "fail to close", e);
            }
        }
    }

    public static void closeSilently(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {
                DefaultLogger.w("Utils", "fail to close", e);
            }
        }
    }

    public static int parseIntSafely(String str, int i) {
        if (str == null) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    public static boolean doubleEquals(double d, double d2) {
        return doubleNear(d, d2, 1.0E-7d);
    }

    public static boolean floatNear(float f, float f2, float f3) {
        return Math.abs(f - f2) < Math.abs(f3);
    }

    public static boolean doubleNear(double d, double d2, double d3) {
        return Math.abs(d - d2) < Math.abs(d3);
    }

    public static String desensitizeShareAlbumId(String str) {
        return (TextUtils.isEmpty(str) || BaseBuildUtil.IS_DEBUG_BUILD) ? str : SHARE_ALBUM_ID_PATTERN.get(null).matcher(str).replaceAll("******$2");
    }
}
