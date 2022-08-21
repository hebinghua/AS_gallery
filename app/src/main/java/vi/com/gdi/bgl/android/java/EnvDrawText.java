package vi.com.gdi.bgl.android.java;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Layout;
import android.text.TextPaint;
import android.util.SparseArray;

/* loaded from: classes3.dex */
public class EnvDrawText {
    private static final String DEVICE_VIVOX3L = "vivo X3L";
    private static final int FONT_STYLE_BOLD = 1;
    private static final int FONT_STYLE_ITALIC = 2;
    private static final int FONT_STYLE_NORMAL = 0;
    public static int[] buffer;
    private static Bitmap defaultFontBmp;
    public static SparseArray<a> fontCache;

    /* JADX WARN: Removed duplicated region for block: B:102:0x0286 A[Catch: all -> 0x02e2, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:28:0x00d3, B:30:0x00d7, B:33:0x00e0, B:35:0x00e8, B:38:0x00ec, B:40:0x00f4, B:42:0x00f8, B:45:0x0104, B:46:0x0124, B:107:0x02c9, B:109:0x02ce, B:111:0x02d7, B:113:0x02dd, B:43:0x00ff, B:48:0x013a, B:49:0x014e, B:51:0x0156, B:54:0x0167, B:55:0x016d, B:57:0x0173, B:60:0x0186, B:62:0x01a0, B:69:0x01df, B:71:0x01e3, B:74:0x01ec, B:76:0x01f4, B:79:0x01f9, B:81:0x0201, B:83:0x0205, B:85:0x020f, B:93:0x0227, B:95:0x022f, B:97:0x023a, B:99:0x025f, B:100:0x027a, B:102:0x0286, B:104:0x0293, B:105:0x02b3, B:90:0x0220, B:91:0x0223, B:84:0x020c, B:15:0x004b, B:16:0x0052), top: B:119:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x027a A[EDGE_INSN: B:123:0x027a->B:100:0x027a ?: BREAK  , SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:71:0x01e3 A[Catch: all -> 0x02e2, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:28:0x00d3, B:30:0x00d7, B:33:0x00e0, B:35:0x00e8, B:38:0x00ec, B:40:0x00f4, B:42:0x00f8, B:45:0x0104, B:46:0x0124, B:107:0x02c9, B:109:0x02ce, B:111:0x02d7, B:113:0x02dd, B:43:0x00ff, B:48:0x013a, B:49:0x014e, B:51:0x0156, B:54:0x0167, B:55:0x016d, B:57:0x0173, B:60:0x0186, B:62:0x01a0, B:69:0x01df, B:71:0x01e3, B:74:0x01ec, B:76:0x01f4, B:79:0x01f9, B:81:0x0201, B:83:0x0205, B:85:0x020f, B:93:0x0227, B:95:0x022f, B:97:0x023a, B:99:0x025f, B:100:0x027a, B:102:0x0286, B:104:0x0293, B:105:0x02b3, B:90:0x0220, B:91:0x0223, B:84:0x020c, B:15:0x004b, B:16:0x0052), top: B:119:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0205 A[Catch: all -> 0x02e2, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:28:0x00d3, B:30:0x00d7, B:33:0x00e0, B:35:0x00e8, B:38:0x00ec, B:40:0x00f4, B:42:0x00f8, B:45:0x0104, B:46:0x0124, B:107:0x02c9, B:109:0x02ce, B:111:0x02d7, B:113:0x02dd, B:43:0x00ff, B:48:0x013a, B:49:0x014e, B:51:0x0156, B:54:0x0167, B:55:0x016d, B:57:0x0173, B:60:0x0186, B:62:0x01a0, B:69:0x01df, B:71:0x01e3, B:74:0x01ec, B:76:0x01f4, B:79:0x01f9, B:81:0x0201, B:83:0x0205, B:85:0x020f, B:93:0x0227, B:95:0x022f, B:97:0x023a, B:99:0x025f, B:100:0x027a, B:102:0x0286, B:104:0x0293, B:105:0x02b3, B:90:0x0220, B:91:0x0223, B:84:0x020c, B:15:0x004b, B:16:0x0052), top: B:119:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:84:0x020c A[Catch: all -> 0x02e2, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:28:0x00d3, B:30:0x00d7, B:33:0x00e0, B:35:0x00e8, B:38:0x00ec, B:40:0x00f4, B:42:0x00f8, B:45:0x0104, B:46:0x0124, B:107:0x02c9, B:109:0x02ce, B:111:0x02d7, B:113:0x02dd, B:43:0x00ff, B:48:0x013a, B:49:0x014e, B:51:0x0156, B:54:0x0167, B:55:0x016d, B:57:0x0173, B:60:0x0186, B:62:0x01a0, B:69:0x01df, B:71:0x01e3, B:74:0x01ec, B:76:0x01f4, B:79:0x01f9, B:81:0x0201, B:83:0x0205, B:85:0x020f, B:93:0x0227, B:95:0x022f, B:97:0x023a, B:99:0x025f, B:100:0x027a, B:102:0x0286, B:104:0x0293, B:105:0x02b3, B:90:0x0220, B:91:0x0223, B:84:0x020c, B:15:0x004b, B:16:0x0052), top: B:119:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0219  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x021c  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x022f A[Catch: all -> 0x02e2, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:28:0x00d3, B:30:0x00d7, B:33:0x00e0, B:35:0x00e8, B:38:0x00ec, B:40:0x00f4, B:42:0x00f8, B:45:0x0104, B:46:0x0124, B:107:0x02c9, B:109:0x02ce, B:111:0x02d7, B:113:0x02dd, B:43:0x00ff, B:48:0x013a, B:49:0x014e, B:51:0x0156, B:54:0x0167, B:55:0x016d, B:57:0x0173, B:60:0x0186, B:62:0x01a0, B:69:0x01df, B:71:0x01e3, B:74:0x01ec, B:76:0x01f4, B:79:0x01f9, B:81:0x0201, B:83:0x0205, B:85:0x020f, B:93:0x0227, B:95:0x022f, B:97:0x023a, B:99:0x025f, B:100:0x027a, B:102:0x0286, B:104:0x0293, B:105:0x02b3, B:90:0x0220, B:91:0x0223, B:84:0x020c, B:15:0x004b, B:16:0x0052), top: B:119:0x0011 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized int[] drawText(java.lang.String r26, int r27, int r28, int[] r29, int r30, int r31, int r32, int r33, int r34) {
        /*
            Method dump skipped, instructions count: 741
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: vi.com.gdi.bgl.android.java.EnvDrawText.drawText(java.lang.String, int, int, int[], int, int, int, int, int):int[]");
    }

    private static Bitmap drawTextAlpha(String str, int i, int i2, int i3) {
        int ceil;
        int desiredWidth;
        Canvas canvas = new Canvas();
        TextPaint textPaint = new TextPaint();
        String str2 = Build.MODEL;
        int i4 = 0;
        int i5 = (str2 == null || !str2.equals(DEVICE_VIVOX3L)) ? i2 : 0;
        textPaint.reset();
        textPaint.setSubpixelText(false);
        textPaint.setAntiAlias(false);
        textPaint.setTextSize(i);
        int i6 = 2;
        textPaint.setTypeface(i5 != 1 ? i5 != 2 ? Typeface.DEFAULT : Typeface.create(Typeface.DEFAULT, 2) : Typeface.DEFAULT_BOLD);
        float f = (i3 * 1.3f) + 0.5f;
        int i7 = 92;
        int indexOf = str.indexOf(92, 0);
        Bitmap bitmap = null;
        if (indexOf == -1) {
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            int desiredWidth2 = (int) (Layout.getDesiredWidth(str, 0, str.length(), textPaint) + f);
            int ceil2 = (int) Math.ceil(fontMetrics.descent - fontMetrics.ascent);
            if (desiredWidth2 > 0 && ceil2 > 0) {
                bitmap = Bitmap.createBitmap(desiredWidth2, ceil2, Bitmap.Config.ALPHA_8);
                if (bitmap == null) {
                    return bitmap;
                }
                bitmap.eraseColor(0);
                canvas.setBitmap(bitmap);
            }
            textPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(str, f * 0.5f, 0.0f - fontMetrics.ascent, textPaint);
        } else {
            int i8 = indexOf + 1;
            int desiredWidth3 = (int) (Layout.getDesiredWidth(str.substring(0, indexOf), textPaint) + 0.5d);
            while (true) {
                int indexOf2 = str.indexOf(i7, i8);
                if (indexOf2 <= 0) {
                    break;
                }
                int desiredWidth4 = (int) (Layout.getDesiredWidth(str.substring(i8, indexOf2), textPaint) + 0.5d);
                if (desiredWidth4 > desiredWidth3) {
                    desiredWidth3 = desiredWidth4;
                }
                i8 = indexOf2 + 1;
                i6++;
                i7 = 92;
            }
            if (i8 != str.length() && (desiredWidth = (int) (Layout.getDesiredWidth(str.substring(i8, str.length()), textPaint) + 0.5d)) > desiredWidth3) {
                desiredWidth3 = desiredWidth;
            }
            Paint.FontMetrics fontMetrics2 = textPaint.getFontMetrics();
            int i9 = desiredWidth3 + i3;
            int ceil3 = i6 * ((int) Math.ceil(fontMetrics2.descent - fontMetrics2.ascent));
            if (i9 > 0 && ceil3 > 0) {
                bitmap = Bitmap.createBitmap(i9, ceil3, Bitmap.Config.ALPHA_8);
                if (bitmap == null) {
                    return bitmap;
                }
                bitmap.eraseColor(0);
                canvas.setBitmap(bitmap);
            }
            textPaint.setTextAlign(getTextAlignedType(3));
            float f2 = i9 - (f * 0.5f);
            int i10 = 0;
            while (true) {
                int indexOf3 = str.indexOf(92, i4);
                if (indexOf3 <= 0) {
                    break;
                }
                String substring = str.substring(i4, indexOf3);
                Layout.getDesiredWidth(substring, textPaint);
                textPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(substring, f2, (i10 * ceil) - fontMetrics2.ascent, textPaint);
                i10++;
                i4 = indexOf3 + 1;
            }
            if (i4 != str.length()) {
                String substring2 = str.substring(i4, str.length());
                Layout.getDesiredWidth(substring2, textPaint);
                textPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(substring2, f2, (i10 * ceil) - fontMetrics2.ascent, textPaint);
            }
        }
        return bitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00d8 A[Catch: all -> 0x02b9, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:29:0x00d4, B:31:0x00d8, B:34:0x00e1, B:38:0x00eb, B:40:0x00f0, B:42:0x00f4, B:45:0x0100, B:46:0x0120, B:43:0x00fb, B:47:0x0134, B:48:0x0148, B:50:0x0150, B:53:0x0161, B:54:0x0167, B:56:0x016d, B:59:0x0180, B:61:0x019a, B:67:0x01d6, B:69:0x01da, B:72:0x01e3, B:76:0x01ed, B:78:0x01f2, B:80:0x01f6, B:82:0x0200, B:90:0x0218, B:92:0x0220, B:94:0x022b, B:96:0x0250, B:97:0x026b, B:99:0x0277, B:101:0x0284, B:102:0x02a4, B:87:0x0211, B:88:0x0214, B:81:0x01fd, B:15:0x004b, B:16:0x0052), top: B:109:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00f4 A[Catch: all -> 0x02b9, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:29:0x00d4, B:31:0x00d8, B:34:0x00e1, B:38:0x00eb, B:40:0x00f0, B:42:0x00f4, B:45:0x0100, B:46:0x0120, B:43:0x00fb, B:47:0x0134, B:48:0x0148, B:50:0x0150, B:53:0x0161, B:54:0x0167, B:56:0x016d, B:59:0x0180, B:61:0x019a, B:67:0x01d6, B:69:0x01da, B:72:0x01e3, B:76:0x01ed, B:78:0x01f2, B:80:0x01f6, B:82:0x0200, B:90:0x0218, B:92:0x0220, B:94:0x022b, B:96:0x0250, B:97:0x026b, B:99:0x0277, B:101:0x0284, B:102:0x02a4, B:87:0x0211, B:88:0x0214, B:81:0x01fd, B:15:0x004b, B:16:0x0052), top: B:109:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00fb A[Catch: all -> 0x02b9, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:29:0x00d4, B:31:0x00d8, B:34:0x00e1, B:38:0x00eb, B:40:0x00f0, B:42:0x00f4, B:45:0x0100, B:46:0x0120, B:43:0x00fb, B:47:0x0134, B:48:0x0148, B:50:0x0150, B:53:0x0161, B:54:0x0167, B:56:0x016d, B:59:0x0180, B:61:0x019a, B:67:0x01d6, B:69:0x01da, B:72:0x01e3, B:76:0x01ed, B:78:0x01f2, B:80:0x01f6, B:82:0x0200, B:90:0x0218, B:92:0x0220, B:94:0x022b, B:96:0x0250, B:97:0x026b, B:99:0x0277, B:101:0x0284, B:102:0x02a4, B:87:0x0211, B:88:0x0214, B:81:0x01fd, B:15:0x004b, B:16:0x0052), top: B:109:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0100 A[Catch: all -> 0x02b9, TryCatch #0 {, blocks: (B:4:0x0011, B:6:0x0020, B:10:0x002c, B:13:0x0045, B:14:0x0047, B:18:0x0057, B:19:0x006a, B:21:0x0079, B:23:0x009c, B:29:0x00d4, B:31:0x00d8, B:34:0x00e1, B:38:0x00eb, B:40:0x00f0, B:42:0x00f4, B:45:0x0100, B:46:0x0120, B:43:0x00fb, B:47:0x0134, B:48:0x0148, B:50:0x0150, B:53:0x0161, B:54:0x0167, B:56:0x016d, B:59:0x0180, B:61:0x019a, B:67:0x01d6, B:69:0x01da, B:72:0x01e3, B:76:0x01ed, B:78:0x01f2, B:80:0x01f6, B:82:0x0200, B:90:0x0218, B:92:0x0220, B:94:0x022b, B:96:0x0250, B:97:0x026b, B:99:0x0277, B:101:0x0284, B:102:0x02a4, B:87:0x0211, B:88:0x0214, B:81:0x01fd, B:15:0x004b, B:16:0x0052), top: B:109:0x0011 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized android.graphics.Bitmap drawTextExt(java.lang.String r25, int r26, int r27, int[] r28, int r29, int r30, int r31, int r32, int r33) {
        /*
            Method dump skipped, instructions count: 700
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: vi.com.gdi.bgl.android.java.EnvDrawText.drawTextExt(java.lang.String, int, int, int[], int, int, int, int, int):android.graphics.Bitmap");
    }

    private static Paint.Align getTextAlignedType(int i) {
        return 1 == i ? Paint.Align.LEFT : 2 == i ? Paint.Align.RIGHT : Paint.Align.CENTER;
    }

    private static Bitmap getTextBitmap() {
        Paint paint = new Paint();
        paint.setSubpixelText(true);
        paint.setAntiAlias(false);
        paint.setTextSize(12.0f);
        paint.setTypeface(Typeface.DEFAULT);
        Bitmap createBitmap = Bitmap.createBitmap((int) Math.ceil(paint.measureText("!")), (int) Math.ceil(paint.descent() - paint.ascent()), Bitmap.Config.ALPHA_8);
        createBitmap.eraseColor(0);
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        canvas.drawText("!", 0.0f, 0.0f - paint.ascent(), paint);
        return createBitmap;
    }

    private static short[] getTextSize(String str, int i, int i2) {
        int i3;
        int length = str.length();
        if (length == 0) {
            return null;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setSubpixelText(true);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(i);
        textPaint.setTypeface(i2 != 1 ? i2 != 2 ? Typeface.DEFAULT : Typeface.create(Typeface.DEFAULT, 2) : Typeface.DEFAULT_BOLD);
        short[] sArr = new short[length];
        for (int i4 = 0; i4 < length; i4++) {
            sArr[i4] = (short) (Layout.getDesiredWidth(str, 0, i3, textPaint) + 0.5d);
        }
        return sArr;
    }

    private static float[] getTextSizeExt(String str, int i, int i2) {
        if (str.length() == 0) {
            return null;
        }
        Paint paint = new Paint();
        paint.setSubpixelText(true);
        paint.setAntiAlias(true);
        paint.setTextSize(i);
        paint.setTypeface(i2 != 1 ? i2 != 2 ? Typeface.DEFAULT : Typeface.create(Typeface.DEFAULT, 2) : Typeface.DEFAULT_BOLD);
        return new float[]{paint.measureText(str), paint.descent() - paint.ascent()};
    }

    private static synchronized boolean isSystemFontChanged() {
        synchronized (EnvDrawText.class) {
            if (defaultFontBmp == null) {
                defaultFontBmp = getTextBitmap();
                return false;
            }
            Bitmap textBitmap = getTextBitmap();
            if (!(!nativeIsBitmapSame(textBitmap, defaultFontBmp))) {
                textBitmap.recycle();
                return false;
            }
            defaultFontBmp.recycle();
            defaultFontBmp = Bitmap.createBitmap(textBitmap);
            textBitmap.recycle();
            return true;
        }
    }

    private static native boolean nativeIsBitmapSame(Bitmap bitmap, Bitmap bitmap2);

    public static synchronized void registFontCache(int i, Typeface typeface) {
        synchronized (EnvDrawText.class) {
            if (i == 0 || typeface == null) {
                return;
            }
            if (fontCache == null) {
                fontCache = new SparseArray<>();
            }
            a aVar = fontCache.get(i);
            if (aVar == null) {
                a aVar2 = new a();
                aVar2.a = typeface;
                aVar2.b++;
                fontCache.put(i, aVar2);
            } else {
                aVar.b++;
            }
        }
    }

    public static synchronized void removeFontCache(int i) {
        synchronized (EnvDrawText.class) {
            a aVar = fontCache.get(i);
            if (aVar == null) {
                return;
            }
            int i2 = aVar.b - 1;
            aVar.b = i2;
            if (i2 == 0) {
                fontCache.remove(i);
            }
        }
    }
}
