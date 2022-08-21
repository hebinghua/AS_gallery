package com.miui.gallery.util;

import android.net.Uri;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class UriUtil {
    public static Uri appendLimit(Uri uri, int i) {
        return uri != null ? appendParameter(uri.buildUpon(), "limit", String.valueOf(i)).build() : uri;
    }

    public static Uri appendLimit(Uri uri, int i, int i2) {
        if (uri != null) {
            Uri.Builder buildUpon = uri.buildUpon();
            return appendParameter(buildUpon, "limit", i2 + " , " + i).build();
        }
        return uri;
    }

    public static Uri appendGroupBy(Uri uri, String str, String str2) {
        return uri != null ? appendParameter(appendParameter(uri.buildUpon(), "groupBy", str), "having", str2).build() : uri;
    }

    public static Uri appendDistinct(Uri uri, boolean z) {
        return (uri == null || !z) ? uri : appendParameter(uri.buildUpon(), "distinct", "distinct").build();
    }

    public static Uri.Builder appendParameter(Uri.Builder builder, String str, String str2) {
        if (builder != null && str != null && str2 != null) {
            builder.appendQueryParameter(str, str2);
        }
        return builder;
    }

    public static String getLimit(Uri uri) {
        if (uri != null) {
            return uri.getQueryParameter("limit");
        }
        return null;
    }

    public static String getGroupBy(Uri uri) {
        if (uri != null) {
            return uri.getQueryParameter("groupBy");
        }
        return null;
    }

    public static String getHaving(Uri uri) {
        if (uri != null) {
            return uri.getQueryParameter("having");
        }
        return null;
    }

    public static boolean getDistinct(Uri uri) {
        return uri != null && "1".equals(uri.getQueryParameter("distinct"));
    }

    public static boolean isNetUri(Uri uri) {
        if (uri != null) {
            String scheme = uri.getScheme();
            if (TextUtils.isEmpty(scheme)) {
                return false;
            }
            return scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https");
        }
        return false;
    }
}
