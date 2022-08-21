package com.miui.gallery.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import androidx.exifinterface.media.ExifInterface;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.io.FileNotFoundException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public final class Bitmaps {
    public static Bitmap decodeUri(Context context, Uri uri, BitmapFactory.Options options) throws FileNotFoundException {
        InputStream openInputStream;
        InputStream inputStream = null;
        try {
            openInputStream = IoUtils.openInputStream(context, uri);
        } catch (Throwable th) {
            th = th;
        }
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
            IoUtils.close(openInputStream);
            return decodeStream;
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            IoUtils.close(inputStream);
            throw th;
        }
    }

    public static Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) {
        try {
            return BitmapFactory.decodeStream(inputStream, null, options);
        } finally {
            IoUtils.close(inputStream);
        }
    }

    public static ExifInterface readExif(Context context, Uri uri) {
        InputStream openInputStream = IoUtils.openInputStream("Graphics", context, uri);
        if (openInputStream == null) {
            return null;
        }
        try {
            return ExifUtil.sSupportExifCreator.mo1691create(openInputStream);
        } finally {
            IoUtils.close("Graphics", openInputStream);
        }
    }

    public static Bitmap setConfig(Bitmap bitmap) {
        if (bitmap == null || bitmap.getConfig() != null) {
            return bitmap;
        }
        Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmap.recycle();
        return copy;
    }

    public static Bitmap joinExif(Bitmap bitmap, int i, BitmapFactory.Options options) {
        Bitmap bitmap2;
        if (i != 0) {
            if (bitmap != null) {
                Matrix matrix = new Matrix();
                matrix.preRotate(i);
                bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
            } else {
                bitmap2 = null;
            }
            if (options != null && i % nexClip.kClip_Rotate_180 != 0) {
                int i2 = options.outWidth;
                options.outWidth = options.outHeight;
                options.outHeight = i2;
            }
            return bitmap2;
        }
        return bitmap;
    }

    public static Bitmap copyBitmapAndRecycle(Bitmap bitmap) {
        Bitmap bitmap2 = null;
        if (bitmap == null) {
            return null;
        }
        try {
            try {
                bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            } catch (OutOfMemoryError e) {
                DefaultLogger.e("Graphics", "copyBitmap error:" + e.toString());
            }
            return bitmap2;
        } finally {
            bitmap.recycle();
        }
    }

    public static Bitmap copyBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        try {
            return bitmap.copy(bitmap.getConfig(), true);
        } catch (OutOfMemoryError e) {
            DefaultLogger.e("Graphics", "copyBitmap error:" + e.toString());
            return null;
        }
    }

    public static Bitmap copyBitmapInCaseOfRecycle(Bitmap bitmap) {
        try {
            return bitmap.copy(Bitmap.Config.ARGB_8888, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r0v2 */
    public static Bitmap decodeAsset(Context context, String str, BitmapFactory.Options options) {
        InputStream inputStream;
        ?? r0 = 0;
        try {
            try {
                inputStream = context.getAssets().open(str);
                try {
                    Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                    IoUtils.close(inputStream);
                    return decodeStream;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    IoUtils.close(inputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                r0 = context;
                IoUtils.close(r0);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.close(r0);
            throw th;
        }
    }
}
