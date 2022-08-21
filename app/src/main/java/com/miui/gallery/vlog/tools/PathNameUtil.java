package com.miui.gallery.vlog.tools;

import android.text.TextUtils;
import com.xiaomi.stat.b.h;

/* loaded from: classes2.dex */
public class PathNameUtil {
    public static String getPathSuffix(String str) {
        return !TextUtils.isEmpty(str) ? str.substring(str.lastIndexOf(".") + 1) : "";
    }

    public static String getOutPathNameNoSuffix(String str) {
        if (!TextUtils.isEmpty(str)) {
            int lastIndexOf = str.lastIndexOf(".");
            if (lastIndexOf < 0) {
                lastIndexOf = str.length();
            }
            return str.substring(str.lastIndexOf(h.g) + 1, lastIndexOf);
        }
        return "";
    }
}
