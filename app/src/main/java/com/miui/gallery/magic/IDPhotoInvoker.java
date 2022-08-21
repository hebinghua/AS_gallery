package com.miui.gallery.magic;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.ResourceUtil;
import java.io.IOException;

/* loaded from: classes2.dex */
public class IDPhotoInvoker {
    public static final String TAG;

    public native void idDestory();

    public native void idInit();

    public native int[] nativeCalculateWH(int i, int i2, int[] iArr);

    public native int nativeChangeBgColor(Bitmap bitmap, int i, int i2, int i3, int[] iArr, Bitmap bitmap2);

    public native int[][] nativeFaceDetect(Bitmap bitmap, int i, int i2);

    public native int nativeMerge(Bitmap bitmap, int i, int i2, int i3, int[] iArr, Bitmap bitmap2);

    static {
        String libraryDirPath = ResourceUtil.getLibraryDirPath(MagicUtils.getGalleryApp());
        System.load(libraryDirPath + "/libFaceDetLmd.so");
        System.load(libraryDirPath + "/libmicrednetials_matting.so");
        System.load(libraryDirPath + "/libidPhoto-lib.so");
        TAG = IDPhotoInvoker.class.getName();
    }

    public Rect[] detectFace(Bitmap bitmap) throws IOException {
        int[][] nativeFaceDetect = new IDPhotoInvoker().nativeFaceDetect(bitmap, bitmap.getWidth(), bitmap.getHeight());
        if (nativeFaceDetect.length < 1) {
            throw new IOException("0");
        }
        if (nativeFaceDetect[0][0] == -999) {
            throw new IOException("检测时发生异常，错误码 - " + nativeFaceDetect[1][0]);
        }
        Rect[] rectArr = new Rect[nativeFaceDetect.length];
        for (int i = 0; i < nativeFaceDetect.length; i++) {
            int[] iArr = nativeFaceDetect[i];
            rectArr[i] = new Rect(iArr[0], iArr[1], iArr[2], iArr[3]);
        }
        return rectArr;
    }

    public Bitmap idBlending(Bitmap bitmap, int i, Rect rect, Boolean bool) {
        int[] iArr = {rect.left, rect.top, rect.width(), rect.height()};
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        int width = copy.getWidth();
        int height = copy.getHeight();
        int[] nativeCalculateWH = nativeCalculateWH(width, height, iArr);
        Bitmap createBitmap = Bitmap.createBitmap(nativeCalculateWH[0], nativeCalculateWH[1], Bitmap.Config.ARGB_8888);
        if (bool.booleanValue()) {
            nativeMerge(copy, width, height, i, iArr, createBitmap);
        } else {
            nativeChangeBgColor(copy, width, height, i, iArr, createBitmap);
        }
        copy.recycle();
        return createBitmap;
    }
}
