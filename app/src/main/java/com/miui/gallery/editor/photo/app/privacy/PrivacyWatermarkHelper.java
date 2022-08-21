package com.miui.gallery.editor.photo.app.privacy;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import com.android.internal.SystemPropertiesCompat;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes2.dex */
public final class PrivacyWatermarkHelper {
    public static final String FONT_MIPRO_PATH;
    public static final boolean IS_MIPRO_EXISTS;
    public static final String TAG;
    public static final int TEXT_COLOR = Color.parseColor("#33000000");

    static {
        String str = SystemPropertiesCompat.get("ro.miui.ui.font.mi_font_path", "system/fonts/MiLanProVF.ttf");
        FONT_MIPRO_PATH = str;
        IS_MIPRO_EXISTS = new File(str).exists();
        TAG = PrivacyWatermarkHelper.class.getSimpleName();
    }

    public static Typeface getTextFont() {
        if (IS_MIPRO_EXISTS) {
            return new Typeface.Builder(FONT_MIPRO_PATH).setFontVariationSettings("'wght' 400").build();
        }
        return Typeface.create("sans-serif", 0);
    }

    public static Paint getInitialPaint() {
        Paint paint = new Paint(1);
        paint.setColor(TEXT_COLOR);
        paint.setTypeface(getTextFont());
        return paint;
    }

    public static void drawWatermark(Canvas canvas, String str, int i, int i2, int i3) {
        int i4;
        int i5;
        int i6 = i;
        if (canvas == null || TextUtils.isEmpty(str)) {
            return;
        }
        float min = Math.min(i, i2) * 0.02037037f;
        float f = 7.0f * min;
        Paint initialPaint = getInitialPaint();
        Rect rect = new Rect();
        initialPaint.setTextSize(min);
        initialPaint.getTextBounds(str, 0, str.length(), rect);
        double abs = (float) ((Math.abs(-30.0f) / 180.0f) * 3.141592653589793d);
        int width = (int) ((rect.width() + f) * 2.0f * Math.sin(abs) * Math.cos(abs));
        int height = (int) ((rect.height() + width) * Math.tan(abs));
        float height2 = (float) (rect.height() * Math.sin(abs));
        float height3 = (float) ((rect.height() * Math.cos(abs)) + (rect.width() * Math.sin(abs)));
        float max = (float) ((Math.max(i6 / i2, i2 / i6) * Math.sin(abs)) + 1.0d);
        if (i3 != 90) {
            if (i3 == 180) {
                float f2 = i2;
                i5 = (int) (max * f2);
                canvas.translate(i6 - height2, f2 - height3);
            } else if (i3 == 270) {
                float f3 = i6;
                i5 = (int) (max * f3);
                canvas.translate(f3 - height3, height2);
                i6 = i2;
            } else {
                if (i3 != 0) {
                    DefaultLogger.w(TAG, "Not standard orientation degree: " + i3);
                }
                i4 = (int) (i2 * max);
                canvas.translate(height2, height3);
            }
            i4 = i5;
        } else {
            canvas.translate(height3, i2 - height2);
            i4 = (int) (i6 * max);
            i6 = i2;
        }
        canvas.rotate((-30.0f) - i3);
        int i7 = 0;
        int i8 = 0;
        while (i7 <= i4) {
            int i9 = i8;
            while (i9 <= i6) {
                float f4 = i9;
                canvas.drawText(str, f4, i7, initialPaint);
                i9 = (int) (f4 + rect.width() + f);
            }
            i7 += rect.height() + width;
            i8 -= height;
        }
    }
}
