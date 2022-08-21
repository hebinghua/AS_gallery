package com.miui.gallery.magic.util;

import android.content.Context;
import com.miui.gallery.magic.tools.MagicUtils;

/* loaded from: classes2.dex */
public class ResourceUtil {
    public static int getDrawableId(String str) {
        return MagicUtils.getGalleryApp().getResources().getIdentifier(str, "drawable", MagicUtils.getGalleryApp().getPackageName());
    }

    public static String[] getArrayById(int i) {
        return MagicUtils.getGalleryApp().getResources().getStringArray(i);
    }

    public static String getString(int i) {
        return MagicUtils.getGalleryApp().getString(i);
    }

    public static int getInt(int i) {
        return MagicUtils.getGalleryApp().getResources().getInteger(i);
    }

    public static String getLibraryDirPath(Context context) {
        return context.getDir("libs", 0).getAbsolutePath();
    }
}
