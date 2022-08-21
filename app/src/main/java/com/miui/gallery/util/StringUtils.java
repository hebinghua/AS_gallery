package com.miui.gallery.util;

import android.text.TextUtils;
import java.util.Objects;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class StringUtils {
    public static String nullToEmpty(String str) {
        return str == null ? "" : str;
    }

    public static String[] mergeStringArray(String[] strArr, String[] strArr2) {
        if (strArr == null) {
            return strArr2;
        }
        if (strArr2 == null) {
            return strArr;
        }
        String[] strArr3 = new String[strArr.length + strArr2.length];
        System.arraycopy(strArr, 0, strArr3, 0, strArr.length);
        System.arraycopy(strArr2, 0, strArr3, strArr.length, strArr2.length);
        return strArr3;
    }

    public static String join(String str, long[] jArr) {
        if (jArr == null || jArr.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        boolean z = true;
        for (long j : jArr) {
            if (z) {
                z = false;
            } else {
                sb.append(str);
            }
            sb.append(j);
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str != null && str2 != null) {
            return TextUtils.equals(str.toLowerCase(), str2.toLowerCase());
        }
        return false;
    }

    public static boolean isValid(String[] strArr) {
        return strArr != null && strArr.length > 0;
    }

    public static boolean containsIgnoreCase(String str, String str2) {
        return indexOfIgnoreCase(str, str2, 0) > -1;
    }

    public static int indexOfIgnoreCase(String str, String str2, int i) {
        if (str != null && str2 != null) {
            if (i < 0) {
                i = 0;
            }
            int length = (str.length() - str2.length()) + 1;
            if (i > length) {
                return -1;
            }
            if (str2.length() == 0) {
                return i;
            }
            while (i < length) {
                if (str.regionMatches(true, i, str2, 0, str2.length())) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    public static String replaceIgnoreCase(String str, String str2, String str3) {
        Objects.requireNonNull(str2, "target == null");
        Objects.requireNonNull(str3, "replacement == null");
        int length = str.length();
        int i = 0;
        if (str2.isEmpty()) {
            StringBuilder sb = new StringBuilder((str3.length() * (length + 2)) + length);
            sb.append(str3);
            while (i < length) {
                sb.append(str.charAt(i));
                sb.append(str3);
                i++;
            }
            return sb.toString();
        }
        StringBuilder sb2 = null;
        while (true) {
            int indexOfIgnoreCase = indexOfIgnoreCase(str, str2, i);
            if (indexOfIgnoreCase == -1) {
                break;
            }
            if (sb2 == null) {
                sb2 = new StringBuilder(length);
            }
            sb2.append((CharSequence) str, i, indexOfIgnoreCase);
            sb2.append(str3);
            i = str2.length() + indexOfIgnoreCase;
        }
        if (sb2 == null) {
            return str;
        }
        sb2.append((CharSequence) str, i, length);
        return sb2.toString();
    }

    public static String getNumberStringInRange(long j, long j2, long j3) {
        if (j < j2) {
            return "";
        }
        if (j > j3) {
            return j3 + Marker.ANY_NON_NULL_MARKER;
        }
        return String.valueOf(j);
    }

    public static String getNumberStringInRange(long j) {
        return getNumberStringInRange(j, 1L, 999L);
    }
}
