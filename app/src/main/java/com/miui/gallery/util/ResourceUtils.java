package com.miui.gallery.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.xiaomi.stat.b.h;

/* loaded from: classes2.dex */
public class ResourceUtils {
    public static boolean isPortMode(Configuration configuration) {
        if (configuration == null) {
            return true;
        }
        int i = configuration.screenWidthDp;
        int i2 = configuration.screenHeightDp;
        return configuration.orientation == 1 || i <= i2 || i - i2 <= 100;
    }

    public static String getString(int i) {
        return GalleryApp.sGetAndroidContext().getString(i);
    }

    public static String getString(int i, Object... objArr) {
        return GalleryApp.sGetAndroidContext().getString(i, objArr);
    }

    public static int getInt(int i) {
        return GalleryApp.sGetAndroidContext().getResources().getInteger(i);
    }

    public static String getQuantityString(int i, int i2, Object... objArr) {
        return GalleryApp.sGetAndroidContext().getResources().getQuantityString(i, i2, objArr);
    }

    public static int getColor(int i) {
        return getColor(GalleryApp.sGetAndroidContext(), i);
    }

    public static int getColor(Context context, int i) {
        return context.getResources().getColor(i);
    }

    public static int getDimentionPixelsSize(int i) {
        return GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(i);
    }

    public static int getStringResourceIdentifier(String str, Context context) {
        return getResourceIdentifier(str, "string", context);
    }

    public static int getResourceIdentifier(String str, String str2, Context context) {
        if (TextUtils.isEmpty(str) || context == null) {
            return 0;
        }
        return GalleryApp.sGetAndroidContext().getResources().getIdentifier(str, str2, context.getPackageName());
    }

    public static Uri getResourceUri(int i) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        return Uri.parse("android.resource://" + resources.getResourcePackageName(i) + h.g + resources.getResourceTypeName(i) + h.g + resources.getResourceEntryName(i));
    }

    public static String getResourceUriPath(int i) {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        return "android.resource://" + resources.getResourcePackageName(i) + h.g + resources.getResourceTypeName(i) + h.g + resources.getResourceEntryName(i);
    }

    public static boolean isGalleryDrawableResourcePath(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.startsWith("android.resource://com.miui.gallery/drawable");
    }
}
