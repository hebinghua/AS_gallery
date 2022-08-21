package com.miui.gallery.map.utils;

import com.baidu.mapapi.SDKInitializer;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class MapInitializerImpl {
    public static volatile boolean mHasInitialized;

    public static boolean checkMapAvailable() {
        return true;
    }

    public static boolean init() {
        DefaultLogger.d("MapInitializerImpl", "BD map SDK init start==>");
        long currentTimeMillis = System.currentTimeMillis();
        if (!mHasInitialized) {
            SDKInitializer.initialize(GalleryApp.sGetAndroidContext());
            mHasInitialized = true;
            DefaultLogger.d("MapInitializerImpl", "BD map SDK init end==>%s", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        }
        return mHasInitialized;
    }

    public static boolean checkInitialized() {
        return mHasInitialized;
    }
}
