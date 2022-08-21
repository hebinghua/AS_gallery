package miuix.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.renderscript.RenderScript;
import java.io.IOException;
import java.util.regex.Pattern;
import miuix.io.ResettableInputStream;

/* loaded from: classes3.dex */
public class BitmapFactory extends android.graphics.BitmapFactory {
    public static final String[] APPELLATION_SUFFIX;
    public static final Pattern ASIALANGPATTERN;
    public static RenderScript sRsContext;
    public static final Paint sSrcInPaint;
    public static Object sLockForRsContext = new Object();
    public static byte[] PNG_HEAD_FORMAT = {-119, 80, 78, 71, 13, 10, 26, 10};
    public static final ThreadLocal<Canvas> sCanvasCache = new ThreadLocal<>();

    static {
        Paint paint = new Paint(1);
        sSrcInPaint = paint;
        paint.setFilterBitmap(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        ASIALANGPATTERN = Pattern.compile("[\u3100-ㄭㆠ-ㆺ一-鿌㐀-䶵豈-龎⼀-⿕⺀-⻳㇀-㇣ᄀ-ᇿꥠ-ꥼힰ-ퟻㄱ-ㆎ가-힣\u3040-ゟ゠-ヿㇰ-ㇿ㆐-㆟ꀀ-ꒌ꒐-꓆]");
        APPELLATION_SUFFIX = new String[]{"老师", "先生", "老板", "仔", "手机", "叔", "阿姨", "宅", "伯", "伯母", "伯父", "哥", "姐", "弟", "妹", "舅", "姑", "父", "主任", "经理", "工作", "同事", "律师", "司机", "师傅", "师父", "爷", "奶", "中介", "董", "总", "太太", "保姆", "某", "秘书", "处长", "局长", "班长", "兄", "助理"};
    }

    public static BitmapFactory.Options getBitmapSize(ResettableInputStream resettableInputStream) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeStream(resettableInputStream, null, options);
        return options;
    }

    public static BitmapFactory.Options getBitmapSize(String str) throws IOException {
        ResettableInputStream resettableInputStream;
        ResettableInputStream resettableInputStream2 = null;
        try {
            resettableInputStream = new ResettableInputStream(str);
        } catch (Throwable th) {
            th = th;
        }
        try {
            BitmapFactory.Options bitmapSize = getBitmapSize(resettableInputStream);
            resettableInputStream.close();
            return bitmapSize;
        } catch (Throwable th2) {
            th = th2;
            resettableInputStream2 = resettableInputStream;
            if (resettableInputStream2 != null) {
                resettableInputStream2.close();
            }
            throw th;
        }
    }

    public static BitmapFactory.Options getBitmapSize(Context context, Uri uri) throws IOException {
        ResettableInputStream resettableInputStream;
        ResettableInputStream resettableInputStream2 = null;
        try {
            resettableInputStream = new ResettableInputStream(context, uri);
        } catch (Throwable th) {
            th = th;
        }
        try {
            BitmapFactory.Options bitmapSize = getBitmapSize(resettableInputStream);
            resettableInputStream.close();
            return bitmapSize;
        } catch (Throwable th2) {
            th = th2;
            resettableInputStream2 = resettableInputStream;
            if (resettableInputStream2 != null) {
                resettableInputStream2.close();
            }
            throw th;
        }
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            return bitmap;
        }
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        if (bitmap.getConfig() != null) {
            config = bitmap.getConfig();
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, config);
        scaleBitmap(bitmap, createBitmap);
        return createBitmap;
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null || bitmap2 == null) {
            return null;
        }
        if (bitmap.getWidth() == bitmap2.getWidth() && bitmap.getHeight() == bitmap2.getHeight()) {
            return bitmap;
        }
        Canvas canvas = new Canvas(bitmap2);
        canvas.drawARGB(0, 0, 0, 0);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        paint.setAntiAlias(true);
        paint.setDither(true);
        canvas.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight()), paint);
        return bitmap2;
    }

    public static Bitmap copyToEmpty(Bitmap bitmap) {
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        if (bitmap.getConfig() != null) {
            config = bitmap.getConfig();
        }
        return Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
    }

    public static Bitmap fastBlur(Context context, Bitmap bitmap, Bitmap bitmap2, int i) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap2 == null || bitmap.getWidth() != bitmap2.getWidth() || bitmap.getHeight() != bitmap2.getHeight()) {
            bitmap2 = copyToEmpty(bitmap);
        }
        fastblur_v17(context, bitmap, bitmap2, i);
        return bitmap2;
    }

    /* JADX WARN: Code restructure failed: missing block: B:19:0x0059, code lost:
        r2 = transferF16ToARGB(r2);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Bitmap fastblur_v17(android.content.Context r9, android.graphics.Bitmap r10, android.graphics.Bitmap r11, int r12) {
        /*
            r0 = 1
            r1 = r0
        L2:
            r2 = 25
            if (r12 <= r2) goto Lb
            int r1 = r1 * 2
            int r12 = r12 / 2
            goto L2
        Lb:
            int r2 = r10.getWidth()
            int r3 = r10.getHeight()
            if (r1 != r0) goto L17
            r2 = r10
            goto L25
        L17:
            int r2 = r2 / r1
            int r2 = java.lang.Math.max(r2, r0)
            int r3 = r3 / r1
            int r3 = java.lang.Math.max(r3, r0)
            android.graphics.Bitmap r2 = scaleBitmap(r10, r2, r3)
        L25:
            android.content.Context r3 = r9.getApplicationContext()
            if (r3 != 0) goto L31
            miuix.graphics.BitmapFactory$1 r3 = new miuix.graphics.BitmapFactory$1
            r3.<init>(r9)
            r9 = r3
        L31:
            java.lang.ClassLoader r3 = r9.getClassLoader()     // Catch: java.lang.Exception -> L61
            java.lang.String r4 = "android.graphics.Bitmap$Config"
            java.lang.Class r3 = r3.loadClass(r4)     // Catch: java.lang.Exception -> L61
            java.lang.Object[] r3 = r3.getEnumConstants()     // Catch: java.lang.Exception -> L61
            int r4 = r3.length     // Catch: java.lang.Exception -> L61
            r5 = 0
        L41:
            if (r5 >= r4) goto L61
            r6 = r3[r5]     // Catch: java.lang.Exception -> L61
            java.lang.Enum r6 = (java.lang.Enum) r6     // Catch: java.lang.Exception -> L61
            java.lang.String r7 = r6.name()     // Catch: java.lang.Exception -> L61
            java.lang.String r8 = "RGBA_F16"
            boolean r7 = r8.equals(r7)     // Catch: java.lang.Exception -> L61
            if (r7 == 0) goto L5e
            android.graphics.Bitmap$Config r7 = r2.getConfig()     // Catch: java.lang.Exception -> L61
            if (r7 != r6) goto L5e
            android.graphics.Bitmap r2 = transferF16ToARGB(r2)     // Catch: java.lang.Exception -> L61
            goto L61
        L5e:
            int r5 = r5 + 1
            goto L41
        L61:
            java.lang.Object r3 = miuix.graphics.BitmapFactory.sLockForRsContext
            monitor-enter(r3)
            android.renderscript.RenderScript r4 = miuix.graphics.BitmapFactory.sRsContext     // Catch: java.lang.Throwable -> Lc4
            if (r4 != 0) goto L6e
            android.renderscript.RenderScript r9 = android.renderscript.RenderScript.create(r9)     // Catch: java.lang.Throwable -> Lc4
            miuix.graphics.BitmapFactory.sRsContext = r9     // Catch: java.lang.Throwable -> Lc4
        L6e:
            if (r1 != r0) goto L72
            r9 = r11
            goto L73
        L72:
            r9 = r2
        L73:
            int r1 = r2.getRowBytes()     // Catch: java.lang.Throwable -> Lc4
            int r4 = r9.getRowBytes()     // Catch: java.lang.Throwable -> Lc4
            if (r1 == r4) goto L83
            android.graphics.Bitmap$Config r1 = android.graphics.Bitmap.Config.ARGB_8888     // Catch: java.lang.Throwable -> Lc4
            android.graphics.Bitmap r2 = r2.copy(r1, r0)     // Catch: java.lang.Throwable -> Lc4
        L83:
            android.renderscript.RenderScript r0 = miuix.graphics.BitmapFactory.sRsContext     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.Allocation r0 = android.renderscript.Allocation.createFromBitmap(r0, r2)     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.RenderScript r1 = miuix.graphics.BitmapFactory.sRsContext     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.Type r4 = r0.getType()     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.Allocation r1 = android.renderscript.Allocation.createTyped(r1, r4)     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.RenderScript r4 = miuix.graphics.BitmapFactory.sRsContext     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.Element r5 = android.renderscript.Element.U8_4(r4)     // Catch: java.lang.Throwable -> Lc4
            android.renderscript.ScriptIntrinsicBlur r4 = android.renderscript.ScriptIntrinsicBlur.create(r4, r5)     // Catch: java.lang.Throwable -> Lc4
            float r12 = (float) r12     // Catch: java.lang.Throwable -> Lc4
            r4.setRadius(r12)     // Catch: java.lang.Throwable -> Lc4
            r4.setInput(r0)     // Catch: java.lang.Throwable -> Lc4
            r4.forEach(r1)     // Catch: java.lang.Throwable -> Lc4
            r1.copyTo(r9)     // Catch: java.lang.Throwable -> Lc4
            if (r9 == r11) goto Laf
            scaleBitmap(r9, r11)     // Catch: java.lang.Throwable -> Lc4
        Laf:
            if (r2 == r10) goto Lb4
            r2.recycle()     // Catch: java.lang.Throwable -> Lc4
        Lb4:
            if (r9 == r11) goto Lb9
            r9.recycle()     // Catch: java.lang.Throwable -> Lc4
        Lb9:
            r0.destroy()     // Catch: java.lang.Throwable -> Lc4
            r1.destroy()     // Catch: java.lang.Throwable -> Lc4
            r4.destroy()     // Catch: java.lang.Throwable -> Lc4
            monitor-exit(r3)     // Catch: java.lang.Throwable -> Lc4
            return r11
        Lc4:
            r9 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> Lc4
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.graphics.BitmapFactory.fastblur_v17(android.content.Context, android.graphics.Bitmap, android.graphics.Bitmap, int):android.graphics.Bitmap");
    }

    public static Bitmap transferF16ToARGB(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width == 0 || height == 0) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setFlags(3);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return createBitmap;
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap, float f) {
        return getRoundBitmap(bitmap, f, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap, float f, Bitmap.Config config) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), config);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, f, f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return createBitmap;
    }
}
