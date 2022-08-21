package com.miui.gallery.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* loaded from: classes2.dex */
public class BaseBitmapUtils {
    public static final Bitmap.Config[] SUPPORTED_CONFIGS = {Bitmap.Config.ALPHA_8, Bitmap.Config.RGB_565, Bitmap.Config.ARGB_8888, Bitmap.Config.RGBA_F16};

    public static int getMaxCanvasBitmapSize() {
        return 104857600;
    }

    public static boolean isValid(Bitmap bitmap) {
        return bitmap != null && !bitmap.isRecycled();
    }

    public static boolean isValid(BitmapRegionDecoder bitmapRegionDecoder) {
        return bitmapRegionDecoder != null && !bitmapRegionDecoder.isRecycled();
    }

    public static InputStream createInputStream(String str, byte[] bArr) throws IOException {
        if (bArr != null) {
            return CryptoUtil.getDecryptCipherInputStream(str, bArr);
        }
        return new FileInputStream(str);
    }

    public static InputStream createInputStream(ContentResolver contentResolver, Uri uri, byte[] bArr) throws IOException {
        if (bArr != null) {
            return CryptoUtil.getDecryptCipherInputStream(contentResolver, uri, bArr);
        }
        return contentResolver.openInputStream(uri);
    }

    public static byte[] compressBitmapAsPng(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (!bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream)) {
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static void recycleSilently(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        try {
            bitmap.recycle();
        } catch (Exception e) {
            DefaultLogger.w("BaseBitmapUtils", "unable recycle bitmap %s", e);
        }
    }

    public static Bitmap.Config getAlphaSafeConfig(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 26 && Bitmap.Config.RGBA_F16.equals(bitmap.getConfig())) {
            return Bitmap.Config.RGBA_F16;
        }
        return Bitmap.Config.ARGB_8888;
    }

    public static Bitmap.Config matchConfig(int i, int i2, int i3) {
        Bitmap.Config[] configArr;
        for (Bitmap.Config config : SUPPORTED_CONFIGS) {
            if (i3 == getBitmapByteSize(i, i2, config)) {
                return config;
            }
        }
        return null;
    }

    public static int getBitmapByteSize(int i, int i2, Bitmap.Config config) {
        return i * i2 * getBytesPerPixel(config);
    }

    public static int getBytesPerPixel(Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()];
        if (i != 1) {
            if (i == 2 || i == 3) {
                return 2;
            }
            return i != 4 ? 4 : 8;
        }
        return 1;
    }

    /* renamed from: com.miui.gallery.util.BaseBitmapUtils$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$Config;

        static {
            int[] iArr = new int[Bitmap.Config.values().length];
            $SwitchMap$android$graphics$Bitmap$Config = iArr;
            try {
                iArr[Bitmap.Config.ALPHA_8.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGB_565.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_4444.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.RGBA_F16.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$Config[Bitmap.Config.ARGB_8888.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public static Bitmap createBitmapFromPixels(ParcelFileDescriptor parcelFileDescriptor, int i, int i2, int i3) {
        FileInputStream fileInputStream;
        byte[] bArr;
        Bitmap.Config matchConfig = matchConfig(i, i2, i3);
        if (matchConfig == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, matchConfig);
        try {
            fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
            fileInputStream.getChannel().position(0L);
            bArr = new byte[i3];
        } catch (IOException e) {
            DefaultLogger.e("BaseBitmapUtils", "read pixels data error, %s.", e);
        }
        if (fileInputStream.read(bArr) == i3) {
            createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bArr));
            fileInputStream.close();
            return createBitmap;
        }
        fileInputStream.close();
        return createBitmap;
    }

    public static Bitmap transformSafeDrawBitmap(Bitmap bitmap) {
        int maxCanvasBitmapSize = getMaxCanvasBitmapSize();
        int bitmapByteSize = getBitmapByteSize(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        if (bitmapByteSize < maxCanvasBitmapSize) {
            return bitmap;
        }
        float sqrt = (float) Math.sqrt(maxCanvasBitmapSize / bitmapByteSize);
        Matrix matrix = new Matrix();
        matrix.postScale(sqrt, sqrt);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }
}
