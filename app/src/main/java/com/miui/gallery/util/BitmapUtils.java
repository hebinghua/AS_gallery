package com.miui.gallery.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;

/* loaded from: classes2.dex */
public class BitmapUtils extends BaseBitmapUtils {
    public static final HashSet<String> REGION_DECODER_SUPPORTED_MIMETYPES;

    /* loaded from: classes2.dex */
    public static class BitmapDimension {
    }

    static {
        HashSet<String> hashSet = new HashSet<>();
        REGION_DECODER_SUPPORTED_MIMETYPES = hashSet;
        hashSet.add("image/png");
        hashSet.add("image/jpeg");
        hashSet.add("image/jpg");
    }

    public static float computeThumbNailScaleSize(int i, int i2, int i3, int i4) {
        int min = Math.min(i, i2);
        int max = Math.max(i, i2);
        return Math.min((Math.min(i3, i4) * 1.0f) / min, (Math.max(i3, i4) * 1.0f) / max);
    }

    public static int computeThumbNailSampleSize(int i, int i2, int i3, int i4) {
        return reviseSampleSize((int) Math.floor(1.0f / computeThumbNailScaleSize(i, i2, i3, i4)));
    }

    public static int reviseSampleSize(int i) {
        if (i <= 1) {
            i = 1;
        }
        if (i <= 8) {
            return Utils.prevPowerOf2(i);
        }
        return (i / 8) * 8;
    }

    public static int computeSampleSizeSmaller(int i, int i2, int i3, int i4) {
        int i5;
        if (i <= 0 || i2 <= 0 || i3 <= 0 || i4 <= 0) {
            return 1;
        }
        if (i2 / i > i4 / i3) {
            i5 = i / i3;
        } else {
            i5 = i2 / i4;
        }
        if (i5 <= 1) {
            return 1;
        }
        return i5 <= 8 ? Utils.prevPowerOf2(i5) : (i5 / 8) * 8;
    }

    public static Bitmap resizeBitmapByScale(Bitmap bitmap, float f, boolean z) {
        int round = Math.round(bitmap.getWidth() * f);
        int round2 = Math.round(bitmap.getHeight() * f);
        if (round == bitmap.getWidth() && round2 == bitmap.getHeight()) {
            return bitmap;
        }
        Bitmap safeCreateBitmap = safeCreateBitmap(round, round2, getConfig(bitmap));
        if (safeCreateBitmap != null) {
            Canvas canvas = new Canvas(safeCreateBitmap);
            canvas.scale(f, f);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, new Paint(2));
            if (z) {
                bitmap.recycle();
            }
        }
        return safeCreateBitmap;
    }

    public static Bitmap.Config getConfig(Bitmap bitmap) {
        Bitmap.Config config = bitmap.getConfig();
        return config == null ? Bitmap.Config.ARGB_8888 : config;
    }

    public static Bitmap fitBitmapToScreen(Bitmap bitmap, int i, int i2, boolean z) {
        return resizeBitmapByScale(bitmap, computeThumbNailScaleSize(bitmap.getWidth(), bitmap.getHeight(), i, i2), z);
    }

    public static boolean isBitmapInScreen(int i, int i2, int i3, int i4) {
        return Math.min(i, i2) <= Math.min(i3, i4) && Math.max(i, i2) <= Math.max(i3, i4);
    }

    public static Bitmap cloneBitmap(Bitmap bitmap) {
        Bitmap safeCreateBitmap;
        if (bitmap == null || (safeCreateBitmap = safeCreateBitmap(bitmap.getWidth(), bitmap.getHeight(), getConfig(bitmap))) == null) {
            return null;
        }
        new Canvas(safeCreateBitmap).drawBitmap(bitmap, 0.0f, 0.0f, new Paint(6));
        return safeCreateBitmap;
    }

    /* JADX WARN: Not initialized variable reg: 5, insn: 0x0033: MOVE  (r4 I:??[OBJECT, ARRAY]) = (r5 I:??[OBJECT, ARRAY]), block:B:13:0x0033 */
    public static Bitmap createVideoThumbnail(String str) {
        FileInputStream fileInputStream;
        Closeable closeable;
        Bitmap frameAtTime;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        Closeable closeable2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(str);
                try {
                    mediaMetadataRetriever.setDataSource(fileInputStream.getFD());
                    byte[] embeddedPicture = mediaMetadataRetriever.getEmbeddedPicture();
                    if (embeddedPicture == null || (frameAtTime = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length)) == null) {
                        frameAtTime = mediaMetadataRetriever.getFrameAtTime();
                    }
                    mediaMetadataRetriever.release();
                    Utils.closeSilently(fileInputStream);
                    return frameAtTime;
                } catch (IllegalArgumentException e) {
                    e = e;
                    Log.e("BitmapUtils", "createVideoThumbnail() failed %s", e);
                    mediaMetadataRetriever.release();
                    Utils.closeSilently(fileInputStream);
                    return null;
                } catch (RuntimeException e2) {
                    e = e2;
                    Log.e("BitmapUtils", "createVideoThumbnail() failed %s", e);
                    mediaMetadataRetriever.release();
                    Utils.closeSilently(fileInputStream);
                    return null;
                } catch (Exception e3) {
                    e = e3;
                    Log.e("BitmapUtils", "createVideoThumbnail %s", e);
                    mediaMetadataRetriever.release();
                    Utils.closeSilently(fileInputStream);
                    return null;
                } catch (OutOfMemoryError e4) {
                    e = e4;
                    Log.e("BitmapUtils", "createVideoThumbnail %s", e);
                    mediaMetadataRetriever.release();
                    Utils.closeSilently(fileInputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                mediaMetadataRetriever.release();
                Utils.closeSilently(closeable2);
                throw th;
            }
        } catch (IllegalArgumentException e5) {
            e = e5;
            fileInputStream = null;
        } catch (OutOfMemoryError e6) {
            e = e6;
            fileInputStream = null;
        } catch (RuntimeException e7) {
            e = e7;
            fileInputStream = null;
        } catch (Exception e8) {
            e = e8;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            mediaMetadataRetriever.release();
            Utils.closeSilently(closeable2);
            throw th;
        }
    }

    public static boolean isSupportedByRegionDecoder(String str) {
        return REGION_DECODER_SUPPORTED_MIMETYPES.contains(str);
    }

    public static boolean isRotationSupported(String str) {
        if (str == null) {
            return false;
        }
        String lowerCase = str.toLowerCase();
        return lowerCase.equals("image/jpeg") || lowerCase.equals("image/png") || lowerCase.equals("image/bmp");
    }

    public static Bitmap safeCreateBitmap(int i, int i2, Bitmap.Config config) {
        try {
            return Bitmap.createBitmap(i, i2, config);
        } catch (Exception e) {
            Log.e("BitmapUtils", "safeCreateBitmap() failed: %s", e);
            return null;
        } catch (OutOfMemoryError e2) {
            Log.e("BitmapUtils", "safeCreateBitmap() failed OOM: %s", e2);
            return null;
        }
    }

    public static Bitmap safeCreateBitmap(Bitmap bitmap, int i, int i2, int i3, int i4) {
        try {
            return Bitmap.createBitmap(bitmap, i, i2, i3, i4);
        } catch (OutOfMemoryError e) {
            Log.e("BitmapUtils", "safeCreateBitmap() failed OOM %s", e);
            return null;
        }
    }

    public static Bitmap safeDecodeRegion(BitmapRegionDecoder bitmapRegionDecoder, Rect rect, BitmapFactory.Options options) {
        try {
            return bitmapRegionDecoder.decodeRegion(rect, options);
        } catch (Exception e) {
            Log.e("BitmapUtils", "safeDecodeRegion() failed %s", e);
            return null;
        } catch (OutOfMemoryError e2) {
            Log.e("BitmapUtils", "safeDecodeRegion() failed OOM %s", e2);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.content.ContentResolver] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v10, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.io.IOException, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    public static BitmapRegionDecoder safeCreateBitmapRegionDecoder(ContentResolver e, Uri uri, boolean z, byte[] bArr) {
        BitmapRegionDecoder bitmapRegionDecoder = null;
        bitmapRegionDecoder = null;
        bitmapRegionDecoder = null;
        bitmapRegionDecoder = null;
        bitmapRegionDecoder = null;
        InputStream inputStream = null;
        try {
        } catch (Throwable th) {
            th = th;
            inputStream = e;
        }
        try {
            try {
                e = BaseBitmapUtils.createInputStream(e, uri, bArr);
            } catch (IOException e2) {
                e = e2;
                DefaultLogger.e("BitmapUtils", "close inputStream failed %s", (Object) e);
            }
            try {
                bitmapRegionDecoder = BitmapRegionDecoder.newInstance((InputStream) e, z);
            } catch (Exception e3) {
                e = e3;
                DefaultLogger.e("BitmapUtils", "safeCreateBitmapRegionDecoder() failed %s", e);
                if (e != 0) {
                    e.close();
                    e = e;
                }
                return bitmapRegionDecoder;
            } catch (OutOfMemoryError e4) {
                e = e4;
                DefaultLogger.e("BitmapUtils", "safeCreateBitmapRegionDecoder() failed OOM %s", e);
                if (e != 0) {
                    e.close();
                    e = e;
                }
                return bitmapRegionDecoder;
            }
        } catch (Exception e5) {
            e = e5;
            e = 0;
        } catch (OutOfMemoryError e6) {
            e = e6;
            e = 0;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e7) {
                    DefaultLogger.e("BitmapUtils", "close inputStream failed %s", e7);
                }
            }
            throw th;
        }
        if (e != 0) {
            e.close();
            e = e;
        }
        return bitmapRegionDecoder;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v10, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.lang.Throwable, java.io.IOException] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    public static Bitmap safeDecodeBitmap(String e, BitmapFactory.Options options, byte[] bArr) {
        Bitmap bitmap = null;
        bitmap = null;
        bitmap = null;
        bitmap = null;
        bitmap = null;
        InputStream inputStream = null;
        try {
        } catch (Throwable th) {
            th = th;
            inputStream = e;
        }
        try {
            try {
                e = BaseBitmapUtils.createInputStream(e, bArr);
            } catch (IOException e2) {
                e = e2;
                Log.e("BitmapUtils", "close inputStream failed %s", e);
            }
            try {
                bitmap = BitmapFactory.decodeStream(e, null, options);
            } catch (Exception e3) {
                e = e3;
                Log.e("BitmapUtils", "safeDecodeBitmap() failed %s", e);
                if (e != 0) {
                    e.close();
                    e = e;
                }
                return bitmap;
            } catch (OutOfMemoryError e4) {
                e = e4;
                Log.e("BitmapUtils", "safeDecodeBitmap() failed OOM %s", e);
                if (e != 0) {
                    e.close();
                    e = e;
                }
                return bitmap;
            }
        } catch (Exception e5) {
            e = e5;
            e = 0;
        } catch (OutOfMemoryError e6) {
            e = e6;
            e = 0;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e7) {
                    Log.e("BitmapUtils", "close inputStream failed %s", e7);
                }
            }
            throw th;
        }
        if (e != 0) {
            e.close();
            e = e;
        }
        return bitmap;
    }

    public static Bitmap safeDecodeBitmap(ContentResolver contentResolver, Uri uri) {
        return safeDecodeBitmap(contentResolver, uri, null, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [android.content.ContentResolver] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v10, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v11 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference failed for: r3v13 */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v4 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.lang.Throwable, java.io.IOException] */
    /* JADX WARN: Type inference failed for: r3v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r3v8 */
    /* JADX WARN: Type inference failed for: r3v9 */
    public static Bitmap safeDecodeBitmap(ContentResolver e, Uri uri, BitmapFactory.Options options, byte[] bArr) {
        Bitmap bitmap = null;
        bitmap = null;
        bitmap = null;
        bitmap = null;
        bitmap = null;
        InputStream inputStream = null;
        try {
        } catch (Throwable th) {
            th = th;
            inputStream = e;
        }
        try {
            try {
                e = BaseBitmapUtils.createInputStream(e, uri, bArr);
            } catch (IOException e2) {
                e = e2;
                Log.e("BitmapUtils", "close inputStream failed %s", e);
            }
            try {
                bitmap = BitmapFactory.decodeStream(e, null, options);
            } catch (Exception e3) {
                e = e3;
                Log.e("BitmapUtils", "safeDecodeBitmap() failed %s", e);
                if (e != 0) {
                    e.close();
                    e = e;
                }
                return bitmap;
            } catch (OutOfMemoryError e4) {
                e = e4;
                Log.e("BitmapUtils", "safeDecodeBitmap() failed OOM %s", e);
                if (e != 0) {
                    e.close();
                    e = e;
                }
                return bitmap;
            }
        } catch (Exception e5) {
            e = e5;
            e = 0;
        } catch (OutOfMemoryError e6) {
            e = e6;
            e = 0;
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e7) {
                    Log.e("BitmapUtils", "close inputStream failed %s", e7);
                }
            }
            throw th;
        }
        if (e != 0) {
            e.close();
            e = e;
        }
        return bitmap;
    }

    public static Bitmap safeCreateBitmap(Bitmap bitmap, int i, int i2, int i3, int i4, Matrix matrix, boolean z, Bitmap.Config config) {
        Bitmap createBitmap;
        RectF rectF;
        Paint paint;
        if (!BaseBitmapUtils.isValid(bitmap)) {
            return null;
        }
        if (!bitmap.isMutable() && bitmap.getConfig() == config && i == 0 && i2 == 0 && i3 == bitmap.getWidth() && i4 == bitmap.getHeight() && (matrix == null || matrix.isIdentity())) {
            return bitmap;
        }
        Rect rect = new Rect(i, i2, i + i3, i2 + i4);
        RectF rectF2 = new RectF(0.0f, 0.0f, i3, i4);
        boolean z2 = true;
        if (matrix == null || matrix.isIdentity()) {
            createBitmap = Bitmap.createBitmap(i3, i4, config);
            rectF = null;
            paint = null;
        } else {
            boolean z3 = !matrix.rectStaysRect();
            rectF = new RectF();
            matrix.mapRect(rectF, rectF2);
            createBitmap = Bitmap.createBitmap(Math.round(rectF.width()), Math.round(rectF.height()), config);
            paint = new Paint();
            paint.setFilterBitmap(z);
            if (z3) {
                paint.setAntiAlias(true);
            }
        }
        createBitmap.setDensity(bitmap.getDensity());
        if ((createBitmap.getConfig() != Bitmap.Config.ARGB_8888 || !createBitmap.hasAlpha()) && !bitmap.isPremultiplied()) {
            z2 = false;
        }
        createBitmap.setPremultiplied(z2);
        Canvas canvas = new Canvas(createBitmap);
        if (rectF != null) {
            canvas.translate(-rectF.left, -rectF.top);
            canvas.concat(matrix);
        }
        canvas.drawBitmap(bitmap, rect, rectF2, paint);
        canvas.setBitmap(null);
        return createBitmap;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float f, int i, int i2) {
        Bitmap bitmap2 = null;
        try {
            Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            RectF rectF = new RectF(0.0f, 0.0f, i, i2);
            Paint paint = new Paint();
            bitmap2 = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawRoundRect(rectF, f, f, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rectF, paint);
            return bitmap2;
        } catch (Exception e) {
            Log.e("BitmapUtils", "getRoundedCornerBitmap() failed %s", e);
            return bitmap2;
        } catch (OutOfMemoryError e2) {
            Log.e("BitmapUtils", "getRoundedCornerBitmap() failed OOM %s", e2);
            return bitmap2;
        }
    }

    public static Bitmap createScaledBitmap(Bitmap bitmap, int i, int i2) {
        Bitmap copy;
        if (!BaseBitmapUtils.isValid(bitmap) || i <= 0 || i2 <= 0) {
            return null;
        }
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            if (width > i && height > i2) {
                float f = width;
                float f2 = height;
                float f3 = i;
                float f4 = i2;
                float f5 = f / f2 > f3 / f4 ? f4 / f2 : f3 / f;
                Matrix matrix = new Matrix();
                matrix.setScale(f5, f5);
                copy = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                return copy;
            }
            copy = bitmap.copy(bitmap.getConfig(), true);
            return copy;
        } catch (Exception e) {
            Log.e("BitmapUtils", "createScaledBitmap occur error.\n", e);
            return null;
        }
    }

    public static byte[] bitmap2RGB(Bitmap bitmap, boolean z) {
        if (bitmap == null) {
            return null;
        }
        ByteBuffer allocate = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(allocate);
        byte[] array = allocate.array();
        byte[] array2 = ByteBuffer.allocate((array.length / 4) * 3).array();
        int length = array.length / 4;
        for (int i = 0; i < length; i++) {
            if (z) {
                int i2 = i * 3;
                int i3 = i * 4;
                array2[i2] = array[i3];
                array2[i2 + 1] = array[i3 + 1];
                array2[i2 + 2] = array[i3 + 2];
            } else {
                int i4 = i * 3;
                int i5 = i * 4;
                array2[i4] = array[i5 + 2];
                array2[i4 + 1] = array[i5 + 1];
                array2[i4 + 2] = array[i5];
            }
        }
        return array2;
    }

    public static byte[] bitmap2RGBA(Bitmap bitmap) {
        if (bitmap == null) {
            return new byte[0];
        }
        ByteBuffer allocate = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(allocate);
        return allocate.array();
    }
}
