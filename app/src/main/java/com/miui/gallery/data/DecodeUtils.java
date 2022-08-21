package com.miui.gallery.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.opengl.GLUtils;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.BitmapUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.Utils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/* loaded from: classes.dex */
public class DecodeUtils {
    public static final Object LOCK = new Object();

    /* loaded from: classes.dex */
    public static class GalleryOptions extends BitmapFactory.Options {
        public Uri uri;

        public ParcelFileDescriptor getFD() {
            try {
                return GalleryApp.sGetAndroidContext().getContentResolver().openFileDescriptor(this.uri, "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Bitmap considerOrientation(Bitmap bitmap, ExifUtil.ExifInfo exifInfo) {
        if (bitmap == null || bitmap.isRecycled() || exifInfo == null) {
            return bitmap;
        }
        boolean z = false;
        Matrix matrix = new Matrix();
        boolean z2 = true;
        if (exifInfo.flip) {
            matrix.postScale(-1.0f, 1.0f);
            z = true;
        }
        int i = exifInfo.rotation;
        if (i != 0) {
            matrix.postRotate(i);
        } else {
            z2 = z;
        }
        if (z2) {
            Bitmap safeCreateBitmap = BitmapUtils.safeCreateBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true, bitmap.getConfig());
            if (safeCreateBitmap != bitmap) {
                bitmap.recycle();
            }
            return safeCreateBitmap;
        }
        return bitmap;
    }

    public static Bitmap requestDecodeThumbNail(String str, GalleryOptions galleryOptions) {
        return requestDecodeThumbNail(str, galleryOptions, null);
    }

    public static Bitmap requestDecodeThumbNail(String str, GalleryOptions galleryOptions, BitmapUtils.BitmapDimension bitmapDimension) {
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        r0 = null;
        Bitmap bitmap = null;
        try {
            try {
                fileInputStream = new FileInputStream(str);
                try {
                    bitmap = requestDecodeThumbNail(fileInputStream.getFD(), galleryOptions, bitmapDimension, str);
                } catch (FileNotFoundException e) {
                    e = e;
                    e.printStackTrace();
                    Utils.closeSilently(fileInputStream);
                    return bitmap;
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    Utils.closeSilently(fileInputStream);
                    return bitmap;
                }
            } catch (Throwable th) {
                th = th;
                fileInputStream2 = fileInputStream;
                Utils.closeSilently(fileInputStream2);
                throw th;
            }
        } catch (FileNotFoundException e3) {
            e = e3;
            fileInputStream = null;
        } catch (IOException e4) {
            e = e4;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            Utils.closeSilently(fileInputStream2);
            throw th;
        }
        Utils.closeSilently(fileInputStream);
        return bitmap;
    }

    public static Bitmap requestDecodeThumbNail(FileDescriptor fileDescriptor, GalleryOptions galleryOptions, BitmapUtils.BitmapDimension bitmapDimension, String str) {
        Bitmap tryDecodeAndFit;
        if (galleryOptions == null) {
            galleryOptions = new GalleryOptions();
        }
        ((BitmapFactory.Options) galleryOptions).inJustDecodeBounds = true;
        GalleryUtils.safeDecodeFileDescriptor(fileDescriptor, null, galleryOptions);
        int i = ((BitmapFactory.Options) galleryOptions).outWidth;
        int i2 = ((BitmapFactory.Options) galleryOptions).outHeight;
        DisplayMetrics displayMetrics = GalleryApp.sGetAndroidContext().getResources().getDisplayMetrics();
        int i3 = displayMetrics.widthPixels;
        int i4 = displayMetrics.heightPixels;
        ((BitmapFactory.Options) galleryOptions).inSampleSize = BitmapUtils.computeThumbNailSampleSize(i, i2, i3, i4);
        ((BitmapFactory.Options) galleryOptions).inJustDecodeBounds = false;
        ((BitmapFactory.Options) galleryOptions).inPurgeable = false;
        ((BitmapFactory.Options) galleryOptions).inInputShareable = false;
        if (ScreenUtils.needOptimizeForLowMemory()) {
            synchronized (LOCK) {
                tryDecodeAndFit = tryDecodeAndFit(galleryOptions, i3, i4, str);
            }
            return tryDecodeAndFit;
        }
        return tryDecodeAndFit(galleryOptions, i3, i4, str);
    }

    public static Bitmap tryDecodeAndFit(GalleryOptions galleryOptions, int i, int i2, String str) {
        FileInputStream fileInputStream;
        Throwable th;
        OutOfMemoryError e;
        FileDescriptor fd;
        Bitmap bitmap = null;
        FileInputStream fileInputStream2 = null;
        ParcelFileDescriptor parcelFileDescriptor = null;
        for (int i3 = 0; i3 < 3 && bitmap == null; i3++) {
            if (str != null) {
                try {
                    fileInputStream = new FileInputStream(str);
                } catch (OutOfMemoryError e2) {
                    fileInputStream = fileInputStream2;
                    e = e2;
                    DefaultLogger.e("DecodeService", "Decode and fit is out of memory: " + e.toString());
                    ((BitmapFactory.Options) galleryOptions).inSampleSize *= 2;
                    GalleryUtils.closeStream(fileInputStream);
                    Utils.closeSilently(parcelFileDescriptor);
                    fileInputStream2 = fileInputStream;
                } catch (Throwable th2) {
                    fileInputStream = fileInputStream2;
                    th = th2;
                    try {
                        DefaultLogger.e("DecodeService", "unkown exception in decode and fit:" + th.toString());
                        ((BitmapFactory.Options) galleryOptions).inSampleSize *= 2;
                        GalleryUtils.closeStream(fileInputStream);
                        Utils.closeSilently(parcelFileDescriptor);
                        fileInputStream2 = fileInputStream;
                    } catch (Throwable th3) {
                        ((BitmapFactory.Options) galleryOptions).inSampleSize *= 2;
                        GalleryUtils.closeStream(fileInputStream);
                        Utils.closeSilently(parcelFileDescriptor);
                        throw th3;
                    }
                }
                try {
                    fd = fileInputStream.getFD();
                } catch (OutOfMemoryError e3) {
                    e = e3;
                    DefaultLogger.e("DecodeService", "Decode and fit is out of memory: " + e.toString());
                    ((BitmapFactory.Options) galleryOptions).inSampleSize *= 2;
                    GalleryUtils.closeStream(fileInputStream);
                    Utils.closeSilently(parcelFileDescriptor);
                    fileInputStream2 = fileInputStream;
                } catch (Throwable th4) {
                    th = th4;
                    DefaultLogger.e("DecodeService", "unkown exception in decode and fit:" + th.toString());
                    ((BitmapFactory.Options) galleryOptions).inSampleSize *= 2;
                    GalleryUtils.closeStream(fileInputStream);
                    Utils.closeSilently(parcelFileDescriptor);
                    fileInputStream2 = fileInputStream;
                }
            } else {
                parcelFileDescriptor = galleryOptions.getFD();
                if (parcelFileDescriptor != null) {
                    fileInputStream = fileInputStream2;
                    fd = parcelFileDescriptor.getFileDescriptor();
                } else {
                    fileInputStream = fileInputStream2;
                    fd = null;
                }
            }
            bitmap = GalleryUtils.safeDecodeFileDescriptor(fd, null, galleryOptions);
            if (bitmap != null) {
                try {
                    GLUtils.getType(bitmap);
                } catch (IllegalArgumentException e4) {
                    e4.printStackTrace();
                    DefaultLogger.w("DecodeService", "decoded bitmap type error, IllegalArgumentException:" + e4.getMessage());
                    bitmap = ensureGLCompatibleBitmap(bitmap);
                    if (bitmap == null) {
                    }
                }
                if (!BitmapUtils.isBitmapInScreen(bitmap.getWidth(), bitmap.getHeight(), i, i2)) {
                    bitmap = BitmapUtils.fitBitmapToScreen(bitmap, i, i2, true);
                }
            }
            ((BitmapFactory.Options) galleryOptions).inSampleSize *= 2;
            GalleryUtils.closeStream(fileInputStream);
            Utils.closeSilently(parcelFileDescriptor);
            fileInputStream2 = fileInputStream;
        }
        return considerOrientation(bitmap, ExifUtil.parseRotationInfo(str, null));
    }

    public static Bitmap ensureGLCompatibleBitmap(Bitmap bitmap) {
        if (bitmap == null || bitmap.getConfig() != null) {
            return bitmap;
        }
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        bitmap.recycle();
        return copy;
    }
}
