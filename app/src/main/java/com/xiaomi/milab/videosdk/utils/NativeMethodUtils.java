package com.xiaomi.milab.videosdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.milab.videosdk.XmsContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class NativeMethodUtils {
    private static final String TAG = "NativeMethodUtils";

    public static int exifOrientationToDegrees(int i) {
        if (i != 3) {
            if (i == 6) {
                return 90;
            }
            if (i == 8) {
                return nexClip.kClip_Rotate_270;
            }
            return 0;
        }
        return nexClip.kClip_Rotate_180;
    }

    private static void closeSilently(InputStream inputStream) {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void recyleBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v13 */
    /* JADX WARN: Type inference failed for: r0v14 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v6 */
    /* JADX WARN: Type inference failed for: r0v8, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r10v0, types: [java.lang.String] */
    public static Bitmap decodeBitmap(String str) {
        FileInputStream fileInputStream;
        BitmapFactory.Options options;
        ?? r0 = "assets:/";
        InputStream inputStream = null;
        try {
            try {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                if (str.startsWith(r0)) {
                    fileInputStream = XmsContext.getInstance().getContext().getAssets().open(str.substring(8));
                } else {
                    fileInputStream = new FileInputStream((String) str);
                }
            } catch (Throwable th) {
                th = th;
                inputStream = r0;
            }
            try {
                decodeStream(fileInputStream, options);
                int i = options.outWidth;
                int i2 = options.outHeight;
                options.inSampleSize = Integer.highestOneBit(Math.max(Math.max(i2 / ScreenUtils.getScreenHeight(), i / ScreenUtils.getScreenWidth()), 1));
                options.inJustDecodeBounds = false;
                r0 = str.startsWith(r0);
                try {
                    if (r0 != 0) {
                        String substring = str.substring(8);
                        InputStream open = XmsContext.getInstance().getContext().getAssets().open(substring);
                        fileInputStream = XmsContext.getInstance().getContext().getAssets().open(substring);
                        r0 = open;
                    } else {
                        FileInputStream fileInputStream2 = new FileInputStream((String) str);
                        fileInputStream = new FileInputStream((String) str);
                        r0 = fileInputStream2;
                    }
                    Bitmap decodeBitmap = decodeBitmap(fileInputStream, options, exifOrientationToDegrees(new ExifInterface((InputStream) r0).getAttributeInt("Orientation", 1)));
                    if (decodeBitmap != null && !decodeBitmap.isRecycled()) {
                        Bitmap.Config config = decodeBitmap.getConfig();
                        Bitmap.Config config2 = Bitmap.Config.ARGB_8888;
                        if (config != config2) {
                            decodeBitmap = decodeBitmap.copy(config2, true);
                        }
                    }
                    Log.d(TAG, "decodeBitmap " + i + "," + i2 + "inSampleSize" + options.inSampleSize);
                    closeSilently(r0);
                    closeSilently(fileInputStream);
                    return decodeBitmap;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    closeSilently(r0);
                    closeSilently(fileInputStream);
                    return null;
                }
            } catch (Exception e2) {
                e = e2;
                r0 = 0;
            } catch (Throwable th2) {
                th = th2;
                closeSilently(inputStream);
                closeSilently(fileInputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            r0 = 0;
            fileInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
        }
    }

    private static Bitmap decodeBitmap(InputStream inputStream, BitmapFactory.Options options, int i) {
        return Bitmaps.setConfig(Bitmaps.joinExif(decodeStream(inputStream, options), i, options));
    }

    private static Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) {
        try {
            return BitmapFactory.decodeStream(inputStream, null, options);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Object getAssets() {
        return XmsContext.getInstance().getContext().getAssets();
    }
}
