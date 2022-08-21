package com.miui.gallery.editor.photo.app.filter;

import android.graphics.Bitmap;
import android.util.Log;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.assistant.library.LibraryUtils;
import java.io.File;

/* loaded from: classes2.dex */
public class Filter {
    public static final String TAG = "Filter";

    public static native int filterBmpData(Bitmap bitmap, int i, int i2, int i3, String str);

    public static native int getScene();

    public static native boolean initialize();

    public static native void release();

    static {
        try {
            String libraryDirPath = LibraryUtils.getLibraryDirPath(GalleryApp.sGetAndroidContext());
            System.loadLibrary("c++_shared");
            StringBuilder sb = new StringBuilder();
            sb.append(libraryDirPath);
            String str = File.separator;
            sb.append(str);
            sb.append("libFaceDetLmd.so");
            System.load(sb.toString());
            System.load(libraryDirPath + str + "libcamera_scene.so");
            System.load(libraryDirPath + str + "libmiai_image_SDL.so");
            System.load(libraryDirPath + str + "libfilter_jni.so");
        } catch (Error e) {
            Log.w(TAG, "library load failed.\n", e);
        }
    }
}
