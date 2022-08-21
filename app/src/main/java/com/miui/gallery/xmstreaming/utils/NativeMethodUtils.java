package com.miui.gallery.xmstreaming.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.xmstreaming.R;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class NativeMethodUtils {
    private static final String ASSETS_START = "assets:/";
    private static final String TAG = "NativeMethodUtils";

    public static Object getAssets() {
        return StaticContext.sGetAndroidContext().getAssets();
    }

    /* JADX WARN: Not initialized variable reg: 6, insn: 0x00a4: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r6 I:??[OBJECT, ARRAY]), block:B:24:0x00a4 */
    public static Bitmap decodeBitmap(String str) {
        FileInputStream fileInputStream;
        Closeable closeable;
        Log.d(TAG, "decodeBitmap path:" + str);
        Closeable closeable2 = null;
        try {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                decodeStream(new FileInputStream(str), options);
                int i = options.outWidth;
                int i2 = options.outHeight;
                options.inSampleSize = Integer.highestOneBit(Math.max(Math.max(i2 / ScreenUtils.getScreenHeight(), i / ScreenUtils.getScreenWidth()), 1));
                options.inJustDecodeBounds = false;
                fileInputStream = new FileInputStream(str);
                try {
                    Bitmap decodeBitmap = decodeBitmap(new FileInputStream(str), options, ExifUtil.getRotationDegrees(ExifUtil.sGallery3DExifCreator.mo1691create(fileInputStream)));
                    if (decodeBitmap != null && !decodeBitmap.isRecycled()) {
                        Bitmap.Config config = decodeBitmap.getConfig();
                        Bitmap.Config config2 = Bitmap.Config.ARGB_8888;
                        if (config != config2) {
                            decodeBitmap = decodeBitmap.copy(config2, true);
                        }
                    }
                    Log.d(TAG, "decodeBitmap" + i + i2 + options.inSampleSize);
                    BaseMiscUtil.closeSilently(fileInputStream);
                    return decodeBitmap;
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                    BaseMiscUtil.closeSilently(fileInputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                BaseMiscUtil.closeSilently(closeable2);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            BaseMiscUtil.closeSilently(closeable2);
            throw th;
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

    public static int loadTextTexture(String str, String str2, int i, int[] iArr) {
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(Typeface.DEFAULT);
        float dimension = StaticContext.sGetAndroidContext().getResources().getDimension(R.dimen.xm_movie_title);
        float dimension2 = StaticContext.sGetAndroidContext().getResources().getDimension(R.dimen.xm_movie_subtitle);
        textPaint.setTextSize(dimension);
        textPaint.setColor(-1);
        if (Build.VERSION.SDK_INT >= 21) {
            textPaint.setLetterSpacing(0.03f);
        }
        String manageStringLength = manageStringLength(str, (int) dimension);
        String manageString2Length = manageString2Length(str2, (int) dimension2);
        StaticLayout staticLayout = new StaticLayout(manageStringLength, textPaint, ScreenUtils.getScreenWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        Bitmap createBitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight() * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        staticLayout.draw(canvas);
        canvas.translate(0.0f, staticLayout.getHeight());
        textPaint.setTextSize(dimension2);
        new StaticLayout(manageString2Length, textPaint, ScreenUtils.getScreenWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true).draw(canvas);
        iArr[0] = createBitmap.getWidth();
        iArr[1] = createBitmap.getHeight();
        Log.d(TAG, "loadTextTexture" + iArr[0] + iArr[1]);
        return loadBitmapTexture(i, createBitmap);
    }

    private static String manageStringLength(String str, int i) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String trim = str.trim();
        Paint paint = new Paint();
        paint.setTextSize(i);
        float measureText = paint.measureText("一二三四五六七八九十");
        float measureText2 = paint.measureText("😀") - paint.measureText("\ud83d");
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= trim.length()) {
                break;
            }
            i3 = i2 + 1;
            float measureText3 = paint.measureText(trim.substring(0, i3));
            if (measureText3 < measureText) {
                i2 = i3;
            } else if (i2 < trim.length() - 1) {
                int i4 = i2 + 2;
                if (paint.measureText(trim.substring(0, i4)) - measureText3 <= measureText2) {
                    i3 = i4;
                }
            } else {
                i3 = trim.length();
            }
        }
        if (i3 == trim.length()) {
            return trim.substring(0, i3);
        }
        return trim.substring(0, i3) + "...";
    }

    private static String manageString2Length(String str, int i) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        String trim = str.trim();
        Paint paint = new Paint();
        paint.setTextSize(i);
        float screenWidth = ScreenUtils.getScreenWidth() - 100.0f;
        float measureText = paint.measureText("😀") - paint.measureText("\ud83d");
        int i2 = 0;
        int i3 = 0;
        while (true) {
            if (i2 >= trim.length()) {
                i2 = i3;
                break;
            }
            i3 = i2 + 1;
            float measureText2 = paint.measureText(trim.substring(0, i3));
            if (measureText2 < screenWidth) {
                i2 = i3;
            } else if (i2 <= trim.length() - 1 && measureText2 - paint.measureText(trim.substring(0, i2)) <= measureText) {
                i2--;
            }
        }
        return trim.substring(0, i2);
    }

    public static int loadAssetTexture(String str, int i) {
        Log.d(TAG, "loadAssetTexture:" + str);
        return loadTexture(StaticContext.sGetAndroidContext(), str, i);
    }

    private static int loadTexture(Context context, String str, int i) {
        return loadBitmapTexture(i, getImageWithPathOrAssets(context, str));
    }

    private static int loadBitmapTexture(int i, Bitmap bitmap) {
        if (i != 0) {
            GLES20.glBindTexture(3553, i);
            GLES20.glTexParameteri(3553, 10240, 9729);
            GLES20.glTexParameteri(3553, 10241, 9729);
            GLES20.glTexParameteri(3553, 10242, 33071);
            GLES20.glTexParameteri(3553, 10243, 33071);
            GLUtils.texImage2D(3553, 0, bitmap, 0);
            bitmap.recycle();
        }
        if (i == 0) {
            Log.e("OpenGlUtils", String.format("loadTexture failed", new Object[0]));
            return -1;
        }
        return i;
    }

    private static Bitmap getImageWithPathOrAssets(Context context, String str) {
        InputStream inputStream;
        InputStream fileInputStream;
        InputStream inputStream2 = null;
        Bitmap bitmap = null;
        try {
            if (str.startsWith(ASSETS_START)) {
                fileInputStream = context.getResources().getAssets().open(str.replace(ASSETS_START, ""));
            } else {
                fileInputStream = new FileInputStream(str);
            }
        } catch (IOException e) {
            e = e;
            inputStream = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            BaseMiscUtil.closeSilently(fileInputStream);
        } catch (IOException e2) {
            inputStream = fileInputStream;
            e = e2;
            try {
                e.printStackTrace();
                BaseMiscUtil.closeSilently(inputStream);
                return bitmap;
            } catch (Throwable th2) {
                th = th2;
                inputStream2 = inputStream;
                BaseMiscUtil.closeSilently(inputStream2);
                throw th;
            }
        } catch (Throwable th3) {
            inputStream2 = fileInputStream;
            th = th3;
            BaseMiscUtil.closeSilently(inputStream2);
            throw th;
        }
        return bitmap;
    }
}
