package com.miui.arcsoftbeauty;

import android.graphics.Bitmap;
import android.os.Build;
import com.miui.filtersdk.beauty.BeautyProcessorManager;
import com.miui.filtersdk.beauty.IntelligentBeautyProcessor;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyEngine;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class ArcsoftBeautyJni {
    public static boolean sLoaded;
    public static final String[] sWhiteList;

    public static native void beautifyProcessBitmap(Bitmap bitmap, int i, int i2, int[] iArr, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6, int[] iArr7, int[] iArr8, int[] iArr9, int[] iArr10, int[] iArr11, int[] iArr12);

    public static native int detectFaceNum(Bitmap bitmap, int i, int i2);

    public static native void encodeYUV420SP(Bitmap bitmap, int i, int i2, byte[] bArr);

    public static native void updateBmpWithYuvBuffer(Bitmap bitmap, int i, int i2, byte[] bArr);

    static {
        String[] strArr = {"jason"};
        sWhiteList = strArr;
        sLoaded = false;
        for (String str : strArr) {
            if (str.equals(Build.DEVICE) && !BaseBuildUtil.isInternational()) {
                sLoaded = true;
            }
        }
        if (sLoaded) {
            try {
                System.loadLibrary("image_arcsoft_4plus");
            } catch (Error unused) {
                sLoaded = false;
                DefaultLogger.w("ArcsoftBeautyJni", "Failed to load library(image_arcsoft_4plus)");
            }
        }
    }

    public static void smartBeauty(Bitmap bitmap) {
        smartBeauty(MiuiBeautyEngine.preProcessBitmap(bitmap), 1);
    }

    public static void smartBeauty(Bitmap bitmap, int i) {
        IntelligentBeautyProcessor beautyProcessor = BeautyProcessorManager.INSTANCE.getBeautyProcessor();
        beautyProcessor.setBeautyParamsDegree(beautyProcessor.getIntelligentLevelParams(i));
        beautyProcessor.beautify(bitmap, bitmap.getWidth(), bitmap.getHeight());
    }

    public static boolean idBeautyAvailable() {
        return sLoaded;
    }
}
