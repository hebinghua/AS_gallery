package com.cdv.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/* loaded from: classes.dex */
public class NvAndroidBitmap {
    public static final int ASPECT_RATIO_MODE_IGNORE = 0;
    public static final int ASPECT_RATIO_MODE_KEEP = 1;
    public static final int ASPECT_RATIO_MODE_KEEP_EXPANDING = 2;
    private static final String TAG = "NvAndroidBitmap";

    /* loaded from: classes.dex */
    public static class ImageInfo {
        public int height;
        public String mimeType;
        public int orientation;
        public int width;
    }

    /* loaded from: classes.dex */
    public static class Size {
        private int m_height;
        private int m_width;

        public Size(int i, int i2) {
            this.m_width = i;
            this.m_height = i2;
        }

        public int getWidth() {
            return this.m_width;
        }

        public int getHeight() {
            return this.m_height;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Size)) {
                return false;
            }
            Size size = (Size) obj;
            return this.m_width == size.m_width && this.m_height == size.m_height;
        }
    }

    public static ImageInfo getImageInfo(Context context, String str) {
        ImageInfo imageInfo;
        if (str != null && !str.isEmpty()) {
            try {
                InputStream tryCreateInputStreamFromImageFilePath = tryCreateInputStreamFromImageFilePath(context, str);
                if (tryCreateInputStreamFromImageFilePath != null) {
                    imageInfo = getImageInfo(tryCreateInputStreamFromImageFilePath);
                    tryCreateInputStreamFromImageFilePath.close();
                    if (imageInfo == null) {
                        return null;
                    }
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(str, options);
                    if (options.outMimeType != null && options.outWidth >= 0 && options.outHeight >= 0) {
                        ImageInfo imageInfo2 = new ImageInfo();
                        imageInfo2.mimeType = options.outMimeType;
                        imageInfo2.width = options.outWidth;
                        imageInfo2.height = options.outHeight;
                        imageInfo2.orientation = 1;
                        imageInfo = imageInfo2;
                    }
                    Log.e(TAG, "Failed to get image information for " + str);
                    return null;
                }
                if (imageInfo.mimeType.equals("image/jpeg")) {
                    InputStream tryCreateInputStreamFromImageFilePath2 = tryCreateInputStreamFromImageFilePath(context, str);
                    if (tryCreateInputStreamFromImageFilePath2 != null) {
                        imageInfo.orientation = getImageRotation(tryCreateInputStreamFromImageFilePath2);
                        tryCreateInputStreamFromImageFilePath2.close();
                    } else {
                        imageInfo.orientation = getImageRotation(str);
                    }
                }
                return imageInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ImageInfo getImageInfo(byte[] bArr) {
        ImageInfo imageInfo;
        if (bArr == null || (imageInfo = getImageInfo(new ByteArrayInputStream(bArr))) == null) {
            return null;
        }
        if (imageInfo.mimeType.equals("image/jpeg")) {
            imageInfo.orientation = getImageRotation(new ByteArrayInputStream(bArr));
        }
        return imageInfo;
    }

    public static Bitmap convertBitmapToRGBA(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap.Config config = bitmap.getConfig();
        Bitmap.Config config2 = Bitmap.Config.ARGB_8888;
        if (config == config2) {
            return bitmap;
        }
        try {
            return bitmap.copy(config2, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createBitmap(Context context, String str, Size size, int i, boolean z) {
        ImageInfo imageInfo = getImageInfo(context, str);
        if (imageInfo == null) {
            return null;
        }
        try {
            Size size2 = new Size(imageInfo.width, imageInfo.height);
            InputStream tryCreateInputStreamFromImageFilePath = tryCreateInputStreamFromImageFilePath(context, str);
            if (tryCreateInputStreamFromImageFilePath != null) {
                Bitmap createBitmap = createBitmap(null, tryCreateInputStreamFromImageFilePath, size2, size, i, z);
                tryCreateInputStreamFromImageFilePath.close();
                return createBitmap;
            }
            return createBitmap(str, null, size2, size, i, z);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createBitmap(byte[] bArr, Size size, int i, boolean z) {
        ImageInfo imageInfo = getImageInfo(bArr);
        if (imageInfo == null) {
            return null;
        }
        try {
            return createBitmap(null, new ByteArrayInputStream(bArr), new Size(imageInfo.width, imageInfo.height), size, i, z);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createRotatedBitmap(Context context, String str, Size size, int i, boolean z) {
        Bitmap createBitmap;
        ImageInfo imageInfo = getImageInfo(context, str);
        if (imageInfo == null) {
            return null;
        }
        try {
            Size size2 = new Size(imageInfo.width, imageInfo.height);
            InputStream tryCreateInputStreamFromImageFilePath = tryCreateInputStreamFromImageFilePath(context, str);
            if (tryCreateInputStreamFromImageFilePath != null) {
                createBitmap = createBitmap(null, tryCreateInputStreamFromImageFilePath, size2, size, i, z);
                tryCreateInputStreamFromImageFilePath.close();
            } else {
                createBitmap = createBitmap(str, null, size2, size, i, z);
            }
            if (createBitmap == null) {
                return null;
            }
            int i2 = imageInfo.orientation;
            return i2 == 1 ? createBitmap : transformBitmap(createBitmap, i2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createRotatedBitmap(byte[] bArr, Size size, int i, boolean z) {
        ImageInfo imageInfo = getImageInfo(bArr);
        if (imageInfo == null) {
            return null;
        }
        try {
            Bitmap createBitmap = createBitmap(null, new ByteArrayInputStream(bArr), new Size(imageInfo.width, imageInfo.height), size, i, z);
            if (createBitmap == null) {
                return null;
            }
            int i2 = imageInfo.orientation;
            return i2 == 1 ? createBitmap : transformBitmap(createBitmap, i2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createBitmapRegion(Context context, String str, Rect rect) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream tryCreateInputStreamFromImageFilePath = tryCreateInputStreamFromImageFilePath(context, str);
            if (tryCreateInputStreamFromImageFilePath != null) {
                Bitmap decodeRegion = BitmapRegionDecoder.newInstance(tryCreateInputStreamFromImageFilePath, false).decodeRegion(rect, options);
                tryCreateInputStreamFromImageFilePath.close();
                return decodeRegion;
            }
            return BitmapRegionDecoder.newInstance(str, false).decodeRegion(rect, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createBitmapRegion(byte[] bArr, Rect rect) {
        try {
            return BitmapRegionDecoder.newInstance(bArr, 0, bArr.length, false).decodeRegion(rect, new BitmapFactory.Options());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap createRgbaBitmap(int i, int i2) {
        try {
            return Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(i);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveBitmapToFile(Bitmap bitmap, int i, String str) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
            if (str.endsWith(".png")) {
                compressFormat = Bitmap.CompressFormat.PNG;
            }
            return bitmap.compress(compressFormat, i, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d7, code lost:
        return android.graphics.BitmapFactory.decodeFile(r9, r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.graphics.Bitmap createBitmap(java.lang.String r9, java.io.InputStream r10, com.cdv.utils.NvAndroidBitmap.Size r11, com.cdv.utils.NvAndroidBitmap.Size r12, int r13, boolean r14) {
        /*
            r0 = 0
            android.graphics.BitmapFactory$Options r1 = new android.graphics.BitmapFactory$Options     // Catch: java.lang.Exception -> Ldd
            r1.<init>()     // Catch: java.lang.Exception -> Ldd
            if (r12 == 0) goto Ld1
            boolean r2 = r12.equals(r11)     // Catch: java.lang.Exception -> Ldd
            if (r2 == 0) goto L10
            goto Ld1
        L10:
            if (r13 == 0) goto L7d
            int r2 = r11.getWidth()     // Catch: java.lang.Exception -> Ldd
            double r2 = (double) r2     // Catch: java.lang.Exception -> Ldd
            int r4 = r11.getHeight()     // Catch: java.lang.Exception -> Ldd
            double r4 = (double) r4     // Catch: java.lang.Exception -> Ldd
            double r2 = r2 / r4
            int r4 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            double r4 = (double) r4     // Catch: java.lang.Exception -> Ldd
            int r6 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            double r6 = (double) r6     // Catch: java.lang.Exception -> Ldd
            double r4 = r4 / r6
            r6 = 1
            r7 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            if (r13 != r6) goto L55
            int r13 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r13 < 0) goto L43
            com.cdv.utils.NvAndroidBitmap$Size r13 = new com.cdv.utils.NvAndroidBitmap$Size     // Catch: java.lang.Exception -> Ldd
            int r4 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            int r12 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            double r5 = (double) r12     // Catch: java.lang.Exception -> Ldd
            double r5 = r5 / r2
            double r5 = r5 + r7
            int r12 = (int) r5     // Catch: java.lang.Exception -> Ldd
            r13.<init>(r4, r12)     // Catch: java.lang.Exception -> Ldd
            goto L7c
        L43:
            com.cdv.utils.NvAndroidBitmap$Size r13 = new com.cdv.utils.NvAndroidBitmap$Size     // Catch: java.lang.Exception -> Ldd
            int r4 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            double r4 = (double) r4     // Catch: java.lang.Exception -> Ldd
            double r4 = r4 * r2
            double r4 = r4 + r7
            int r2 = (int) r4     // Catch: java.lang.Exception -> Ldd
            int r12 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            r13.<init>(r2, r12)     // Catch: java.lang.Exception -> Ldd
            goto L7c
        L55:
            int r13 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r13 < 0) goto L6b
            com.cdv.utils.NvAndroidBitmap$Size r13 = new com.cdv.utils.NvAndroidBitmap$Size     // Catch: java.lang.Exception -> Ldd
            int r4 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            double r4 = (double) r4     // Catch: java.lang.Exception -> Ldd
            double r4 = r4 * r2
            double r4 = r4 + r7
            int r2 = (int) r4     // Catch: java.lang.Exception -> Ldd
            int r12 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            r13.<init>(r2, r12)     // Catch: java.lang.Exception -> Ldd
            goto L7c
        L6b:
            com.cdv.utils.NvAndroidBitmap$Size r13 = new com.cdv.utils.NvAndroidBitmap$Size     // Catch: java.lang.Exception -> Ldd
            int r4 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            int r12 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            double r5 = (double) r12     // Catch: java.lang.Exception -> Ldd
            double r5 = r5 / r2
            double r5 = r5 + r7
            int r12 = (int) r5     // Catch: java.lang.Exception -> Ldd
            r13.<init>(r4, r12)     // Catch: java.lang.Exception -> Ldd
        L7c:
            r12 = r13
        L7d:
            int r13 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            float r13 = (float) r13     // Catch: java.lang.Exception -> Ldd
            int r2 = r11.getWidth()     // Catch: java.lang.Exception -> Ldd
            float r2 = (float) r2     // Catch: java.lang.Exception -> Ldd
            float r13 = r13 / r2
            int r2 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            float r2 = (float) r2     // Catch: java.lang.Exception -> Ldd
            int r11 = r11.getHeight()     // Catch: java.lang.Exception -> Ldd
            float r11 = (float) r11     // Catch: java.lang.Exception -> Ldd
            float r2 = r2 / r11
            float r11 = java.lang.Math.max(r13, r2)     // Catch: java.lang.Exception -> Ldd
            r13 = 1065353216(0x3f800000, float:1.0)
            float r11 = java.lang.Math.min(r11, r13)     // Catch: java.lang.Exception -> Ldd
            float r13 = r13 / r11
            int r11 = (int) r13     // Catch: java.lang.Exception -> Ldd
            r1.inSampleSize = r11     // Catch: java.lang.Exception -> Ldd
            if (r9 == 0) goto La8
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeFile(r9, r1)     // Catch: java.lang.Exception -> Ldd
            goto Lac
        La8:
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeStream(r10, r0, r1)     // Catch: java.lang.Exception -> Ldd
        Lac:
            if (r9 != 0) goto Laf
            return r0
        Laf:
            int r10 = r9.getWidth()     // Catch: java.lang.Exception -> Ldd
            int r11 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            if (r10 > r11) goto Lc4
            int r10 = r9.getHeight()     // Catch: java.lang.Exception -> Ldd
            int r11 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            if (r10 > r11) goto Lc4
            return r9
        Lc4:
            int r10 = r12.getWidth()     // Catch: java.lang.Exception -> Ldd
            int r11 = r12.getHeight()     // Catch: java.lang.Exception -> Ldd
            android.graphics.Bitmap r9 = android.graphics.Bitmap.createScaledBitmap(r9, r10, r11, r14)     // Catch: java.lang.Exception -> Ldd
            return r9
        Ld1:
            if (r9 == 0) goto Ld8
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeFile(r9, r1)     // Catch: java.lang.Exception -> Ldd
            return r9
        Ld8:
            android.graphics.Bitmap r9 = android.graphics.BitmapFactory.decodeStream(r10, r0, r1)     // Catch: java.lang.Exception -> Ldd
            return r9
        Ldd:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cdv.utils.NvAndroidBitmap.createBitmap(java.lang.String, java.io.InputStream, com.cdv.utils.NvAndroidBitmap$Size, com.cdv.utils.NvAndroidBitmap$Size, int, boolean):android.graphics.Bitmap");
    }

    public static Bitmap transformBitmap(Bitmap bitmap, int i) throws Exception {
        Matrix matrix = new Matrix();
        switch (i) {
            case 2:
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 3:
                matrix.postRotate(180.0f);
                break;
            case 4:
                matrix.postScale(1.0f, -1.0f);
                break;
            case 5:
                matrix.postScale(-1.0f, 1.0f);
                matrix.postRotate(270.0f);
                break;
            case 6:
                matrix.postRotate(90.0f);
                break;
            case 7:
                matrix.postScale(-1.0f, 1.0f);
                matrix.postRotate(90.0f);
                break;
            case 8:
                matrix.postRotate(270.0f);
                break;
            default:
                return bitmap;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    private static ImageInfo getImageInfo(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            if (options.outMimeType != null && options.outWidth >= 0 && options.outHeight >= 0) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.mimeType = options.outMimeType;
                imageInfo.width = options.outWidth;
                imageInfo.height = options.outHeight;
                imageInfo.orientation = 1;
                return imageInfo;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static InputStream tryCreateInputStreamFromImageFilePath(Context context, String str) {
        try {
            if (str.startsWith("assets:/")) {
                if (context != null) {
                    return context.getAssets().open(str.substring(8));
                }
                return null;
            } else if (str.startsWith("content://") && context != null) {
                return context.getContentResolver().openInputStream(Uri.parse(str));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int getImageRotation(String str) {
        try {
            return new ExifInterface(str).getAttributeInt("Orientation", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private static int getImageRotation(InputStream inputStream) {
        if (Build.VERSION.SDK_INT < 24) {
            return 1;
        }
        try {
            return new ExifInterface(inputStream).getAttributeInt("Orientation", 1);
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }
}
