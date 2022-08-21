package com.miui.gallery.magic;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.miui.gallery.magic.MattingInvoker;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.ResourceUtil;
import java.io.IOException;
import java.lang.reflect.Array;

/* loaded from: classes2.dex */
public class MagicFilterInvoker {
    private native int nativeMagicFilter(Bitmap bitmap, Bitmap bitmap2, Bitmap bitmap3, int i, int i2, int i3, int i4, int[][] iArr, Bitmap[] bitmapArr, int[][] iArr2, int i5);

    public native int[][] nativeFaceDetect(Bitmap bitmap, int i, int i2);

    static {
        String libraryDirPath = ResourceUtil.getLibraryDirPath(MagicUtils.getGalleryApp());
        System.load(libraryDirPath + "/libFaceDetLmd.so");
        System.load(libraryDirPath + "/libmace.so");
        System.load(libraryDirPath + "/libremove.so");
        System.load(libraryDirPath + "/libmagic_filter.so");
        System.load(libraryDirPath + "/libmibokeh_mask.so");
        System.load(libraryDirPath + "/libmiportrait_seg.so");
        System.load(libraryDirPath + "/libmatting.so");
        System.load(libraryDirPath + "/libvis.so");
        System.load(libraryDirPath + "/libmatting-lib.so");
        System.load(libraryDirPath + "/libmagicFilter-lib.so");
    }

    public Bitmap getFilter(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult) {
        return MattingInvoker.SegmentResult.getMaskBitmapForFilter(segmentResult.getOrginalMask(), bitmap.getWidth(), bitmap.getHeight());
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x007c  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0092 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0093  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public android.graphics.Bitmap magic(android.graphics.Bitmap r15, com.miui.gallery.magic.MagicFilterType r16, android.graphics.Bitmap r17, android.graphics.Bitmap... r18) throws java.io.IOException {
        /*
            r14 = this;
            r0 = r16
            int r6 = r15.getWidth()
            int r7 = r15.getHeight()
            android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ARGB_8888
            android.graphics.Bitmap r12 = android.graphics.Bitmap.createBitmap(r6, r7, r1)
            r1 = 0
            int[][] r2 = new int[r1]
            com.miui.gallery.magic.MagicFilterType r3 = com.miui.gallery.magic.MagicFilterType.SHUTTER
            if (r0 != r3) goto L5f
            com.miui.gallery.magic.MagicFilterInvoker r3 = new com.miui.gallery.magic.MagicFilterInvoker     // Catch: java.io.IOException -> L5f
            r3.<init>()     // Catch: java.io.IOException -> L5f
            r4 = r15
            android.graphics.Rect[] r3 = r3.detectFace(r15)     // Catch: java.io.IOException -> L60
            int r5 = r3.length     // Catch: java.io.IOException -> L60
            r8 = 4
            r9 = 2
            int[] r10 = new int[r9]     // Catch: java.io.IOException -> L60
            r11 = 1
            r10[r11] = r8     // Catch: java.io.IOException -> L60
            r10[r1] = r5     // Catch: java.io.IOException -> L60
            java.lang.Class<int> r5 = int.class
            java.lang.Object r5 = java.lang.reflect.Array.newInstance(r5, r10)     // Catch: java.io.IOException -> L60
            int[][] r5 = (int[][]) r5     // Catch: java.io.IOException -> L60
            r2 = r1
        L34:
            int r8 = r3.length     // Catch: java.io.IOException -> L5d
            if (r2 >= r8) goto L5b
            r8 = r5[r2]     // Catch: java.io.IOException -> L5d
            r10 = r3[r2]     // Catch: java.io.IOException -> L5d
            int r10 = r10.left     // Catch: java.io.IOException -> L5d
            r8[r1] = r10     // Catch: java.io.IOException -> L5d
            r8 = r5[r2]     // Catch: java.io.IOException -> L5d
            r10 = r3[r2]     // Catch: java.io.IOException -> L5d
            int r10 = r10.top     // Catch: java.io.IOException -> L5d
            r8[r11] = r10     // Catch: java.io.IOException -> L5d
            r8 = r5[r2]     // Catch: java.io.IOException -> L5d
            r10 = r3[r2]     // Catch: java.io.IOException -> L5d
            int r10 = r10.right     // Catch: java.io.IOException -> L5d
            r8[r9] = r10     // Catch: java.io.IOException -> L5d
            r8 = r5[r2]     // Catch: java.io.IOException -> L5d
            r10 = 3
            r13 = r3[r2]     // Catch: java.io.IOException -> L5d
            int r13 = r13.bottom     // Catch: java.io.IOException -> L5d
            r8[r10] = r13     // Catch: java.io.IOException -> L5d
            int r2 = r2 + 1
            goto L34
        L5b:
            r8 = r5
            goto L61
        L5d:
            r2 = r5
            goto L60
        L5f:
            r4 = r15
        L60:
            r8 = r2
        L61:
            com.miui.gallery.magic.MagicFilterType r2 = com.miui.gallery.magic.MagicFilterType.GLASS_WINDOW
            if (r0 != r2) goto L7c
            r13 = r14
            r9 = r18
            int[][] r10 = r14.getExtraMaterialsWH(r9)
            int r11 = r16.getType()
            r0 = r14
            r1 = r15
            r2 = r12
            r3 = r17
            r4 = r6
            r5 = r7
            int r0 = r0.nativeMagicFilter(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
            goto L90
        L7c:
            r13 = r14
            android.graphics.Bitmap[] r9 = new android.graphics.Bitmap[r1]
            int[][] r10 = new int[r1]
            int r11 = r16.getType()
            r0 = r14
            r1 = r15
            r2 = r12
            r3 = r17
            r4 = r6
            r5 = r7
            int r0 = r0.nativeMagicFilter(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11)
        L90:
            if (r0 != 0) goto L93
            return r12
        L93:
            java.io.IOException r1 = new java.io.IOException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "调用失败，错误码是 - "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r0 = r2.toString()
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.magic.MagicFilterInvoker.magic(android.graphics.Bitmap, com.miui.gallery.magic.MagicFilterType, android.graphics.Bitmap, android.graphics.Bitmap[]):android.graphics.Bitmap");
    }

    public final int[][] getExtraMaterialsWH(Bitmap[] bitmapArr) {
        if (bitmapArr.length > 0) {
            int length = bitmapArr.length;
            int[][] iArr = (int[][]) Array.newInstance(int.class, length, 2);
            for (int i = 0; i < length; i++) {
                Bitmap bitmap = bitmapArr[i];
                iArr[i][0] = bitmap.getWidth();
                iArr[i][1] = bitmap.getHeight();
            }
            return iArr;
        }
        return new int[0];
    }

    public Rect[] detectFace(Bitmap bitmap) throws IOException {
        int[][] nativeFaceDetect = nativeFaceDetect(bitmap, bitmap.getWidth(), bitmap.getHeight());
        if (nativeFaceDetect.length < 1) {
            throw new IOException("未检测到人脸");
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
}
