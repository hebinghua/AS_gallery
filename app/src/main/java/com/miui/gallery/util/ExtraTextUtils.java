package com.miui.gallery.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ExtraTextUtils {
    public static boolean startsWithIgnoreCase(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return str.toLowerCase().startsWith(str2.toLowerCase());
    }

    public static boolean endsWithIgnoreCase(String str, String str2) {
        if (str == null || str2 == null) {
            return false;
        }
        return str.toLowerCase().endsWith(str2.toLowerCase());
    }

    public static boolean equalsIgnoreCase(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str != null && str2 != null && str.length() == str2.length()) {
            return str.equalsIgnoreCase(str2);
        }
        return false;
    }

    public static String joinForLogPrint(CharSequence charSequence, Collection collection) {
        Iterator it = collection.iterator();
        if (!it.hasNext()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(it.next());
        int i = 1;
        while (it.hasNext()) {
            int i2 = i + 1;
            if (i >= 5 || sb.length() > 3072) {
                sb.append(charSequence);
                sb.append(String.format(Locale.US, "etc. [%d items in total]", Integer.valueOf(collection.size())));
                break;
            }
            sb.append(charSequence);
            sb.append(it.next());
            i = i2;
        }
        return sb.toString();
    }
}
