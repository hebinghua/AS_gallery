package com.miui.gallery.util;

import com.miui.gallery.GalleryApp;

/* loaded from: classes2.dex */
public class DimensionUtils {
    public static float getDimensionPixelSize(int i) {
        return GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(i);
    }

    public static float getDimension(int i) {
        return GalleryApp.sGetAndroidContext().getResources().getDimension(i);
    }
}
