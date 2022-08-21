package com.miui.gallery.editor.photo.app.crop;

import android.graphics.Bitmap;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class AutoCropJni {
    public static boolean sLoaded = false;

    public static native long nativeCreate();

    public static native void nativeDestroy(long j);

    public static native int nativeGetCropParams(long j, Bitmap bitmap, float[] fArr, Bbox bbox);

    public static native String nativeGetVersion(long j);

    public static native int nativeInit(long j);

    public static native int nativeRelease(long j);

    public static boolean isAvailable() {
        return sLoaded;
    }

    static {
        try {
            String libraryDirPath = LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext());
            System.load(libraryDirPath + "/libauto_crop.so");
            System.load(libraryDirPath + "/libauto_crop_jni.so");
            sLoaded = true;
        } catch (Error e) {
            DefaultLogger.w("AutoCropJni", "library load failed.\n", e);
        }
    }
}
