package com.miui.gallery.vlog.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/* loaded from: classes2.dex */
public class BitmapUtils {
    public static Bitmap getTextVertical(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4.0f);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (bitmap2 == null) {
            int i = width + 32;
            Bitmap createBitmap = Bitmap.createBitmap(i, height, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(createBitmap);
            canvas.drawBitmap(bitmap, 16, 0.0f, paint2);
            paint.setStrokeWidth(6.0f);
            float f = height - 8;
            canvas.drawLine(0.0f, 12.0f, 0.0f, f, paint);
            paint.setStrokeWidth(6.0f);
            float f2 = i;
            canvas.drawLine(f2, 12.0f, f2, f, paint);
            return createBitmap;
        }
        float width2 = (width - bitmap2.getWidth()) / 2.0f;
        int i2 = width + 32;
        int height2 = bitmap2.getHeight() + height;
        Bitmap createBitmap2 = Bitmap.createBitmap(i2, height2, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(createBitmap2);
        float f3 = 16;
        canvas.drawBitmap(bitmap, f3, 0.0f, paint2);
        float f4 = height;
        canvas.drawBitmap(bitmap2, f3 + width2, f4, paint2);
        paint.setStrokeWidth(6.0f);
        float f5 = 14;
        float f6 = f5 / 2.0f;
        canvas.drawLine(0.0f, f5, 0.0f, f4 - f6, paint);
        paint.setStrokeWidth(3.0f);
        float f7 = i2 - width2;
        canvas.drawLine(f7, height + 14, f7, height2 - f6, paint);
        return createBitmap2;
    }

    public static Bitmap getTextUnderline(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2.5f);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (bitmap2 == null) {
            Bitmap createBitmap = Bitmap.createBitmap(width, height + 10, Bitmap.Config.ARGB_8888);
            canvas.setBitmap(createBitmap);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
            float f = height + 5;
            canvas.drawLine(0.0f, f, width, f, paint);
            return createBitmap;
        }
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        float f2 = width;
        float f3 = width2;
        float f4 = (f2 - f3) / 2.0f;
        Bitmap createBitmap2 = Bitmap.createBitmap(width, height + height2 + 20, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(createBitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
        float f5 = height + 5;
        canvas.drawLine(0.0f, f5, f2, f5, paint);
        int i = height + 10;
        canvas.drawBitmap(bitmap2, f4, i, paint2);
        float f6 = i + height2 + 5;
        canvas.drawLine(f4, f6, f4 + f3, f6, paint);
        return createBitmap2;
    }

    public static Bitmap getTextBracket(Bitmap bitmap, Bitmap bitmap2) {
        int i;
        Bitmap bitmap3;
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4.0f);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float[] fArr = new float[8];
        float[] fArr2 = new float[8];
        if (bitmap2 == null) {
            Bitmap createBitmap = Bitmap.createBitmap(width + 52, height, Bitmap.Config.ARGB_8888);
            int width2 = createBitmap.getWidth();
            int height2 = createBitmap.getHeight();
            float f = 4;
            fArr[0] = f;
            fArr[2] = f;
            float f2 = height2 - 10;
            fArr[1] = f2;
            fArr[7] = f2;
            float f3 = 14;
            fArr[3] = f3;
            fArr[5] = f3;
            fArr[4] = f3;
            fArr[6] = f3;
            int i2 = width2 - 4;
            float f4 = i2 - 10;
            fArr2[0] = f4;
            fArr2[2] = f4;
            fArr2[1] = f2;
            fArr2[7] = f2;
            fArr2[3] = f3;
            fArr2[5] = f3;
            float f5 = i2;
            fArr2[4] = f5;
            fArr2[6] = f5;
            canvas.setBitmap(createBitmap);
            canvas.drawBitmap(bitmap, 26, 0.0f, paint2);
            bitmap3 = createBitmap;
        } else {
            int width3 = bitmap2.getWidth();
            int height3 = bitmap2.getHeight();
            float f6 = (width - width3) / 2.0f;
            Bitmap createBitmap2 = Bitmap.createBitmap(width + 52, height + height3, Bitmap.Config.ARGB_8888);
            int width4 = createBitmap2.getWidth();
            int height4 = createBitmap2.getHeight();
            float f7 = 4;
            fArr[0] = f7;
            fArr[2] = f7;
            int i3 = height4 - 10;
            float f8 = i3 - height3;
            fArr[1] = f8;
            fArr[7] = f8;
            float f9 = 14;
            fArr[3] = f9;
            fArr[5] = f9;
            fArr[4] = f9;
            fArr[6] = f9;
            float f10 = (i - 10) - f6;
            fArr2[0] = f10;
            fArr2[2] = f10;
            float f11 = i3;
            fArr2[1] = f11;
            fArr2[7] = f11;
            float f12 = 14 + height;
            fArr2[3] = f12;
            fArr2[5] = f12;
            float f13 = (width4 - 4) - f6;
            fArr2[4] = f13;
            fArr2[6] = f13;
            canvas.setBitmap(createBitmap2);
            float f14 = 26;
            canvas.drawBitmap(bitmap, f14, 0.0f, paint2);
            canvas.drawBitmap(bitmap2, f6 + f14, height, paint2);
            bitmap3 = createBitmap2;
        }
        paint.setStrokeWidth(3.0f);
        canvas.drawLine(fArr[2], fArr[3], fArr[0], fArr[1], paint);
        paint.setStrokeWidth(3.0f);
        canvas.drawLine(fArr2[4], fArr2[5], fArr2[6], fArr2[7], paint);
        paint.setStrokeWidth(2.0f);
        canvas.drawLine(fArr[2], fArr[3], fArr[4], fArr[5], paint);
        canvas.drawLine(fArr[0], fArr[1], fArr[6], fArr[7], paint);
        canvas.drawLine(fArr2[2], fArr2[3], fArr2[4], fArr2[5], paint);
        canvas.drawLine(fArr2[0], fArr2[1], fArr2[6], fArr2[7], paint);
        return bitmap3;
    }

    public static Bitmap getTextTriangle(Bitmap bitmap, Bitmap bitmap2) {
        int i;
        int i2;
        Bitmap bitmap3;
        int i3;
        int i4;
        if (bitmap == null) {
            return null;
        }
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1.0f);
        paint.setStyle(Paint.Style.FILL);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float[] fArr = new float[6];
        float[] fArr2 = new float[6];
        if (bitmap2 == null) {
            bitmap3 = Bitmap.createBitmap(width + 68, height, Bitmap.Config.ARGB_8888);
            int width2 = bitmap3.getWidth();
            int height2 = bitmap3.getHeight();
            float f = 4;
            fArr[0] = f;
            fArr[2] = f;
            fArr[1] = 29;
            float f2 = 12;
            fArr[3] = f2;
            fArr[5] = f2;
            fArr[4] = 21;
            fArr2[0] = i3 - 17;
            float f3 = height2 - 10;
            fArr2[1] = f3;
            fArr2[5] = f3;
            float f4 = width2 - 4;
            fArr2[2] = f4;
            fArr2[4] = f4;
            fArr2[3] = i4 - 17;
            canvas.setBitmap(bitmap3);
            canvas.drawBitmap(bitmap, 34, 0.0f, paint2);
        } else {
            float width3 = (width - bitmap2.getWidth()) / 2.0f;
            Bitmap createBitmap = Bitmap.createBitmap(width + 68, height + bitmap2.getHeight(), Bitmap.Config.ARGB_8888);
            int width4 = createBitmap.getWidth();
            int height3 = createBitmap.getHeight();
            float f5 = 4;
            fArr[0] = f5;
            fArr[2] = f5;
            fArr[1] = 29;
            float f6 = 12;
            fArr[3] = f6;
            fArr[5] = f6;
            fArr[4] = 21;
            fArr2[0] = (i - 17) - width3;
            float f7 = height3 - 10;
            fArr2[1] = f7;
            fArr2[5] = f7;
            float f8 = (width4 - 4) - width3;
            fArr2[2] = f8;
            fArr2[4] = f8;
            fArr2[3] = i2 - 17;
            canvas.setBitmap(createBitmap);
            float f9 = 34;
            canvas.drawBitmap(bitmap, f9, 0.0f, paint2);
            canvas.drawBitmap(bitmap2, width3 + f9, height, paint2);
            bitmap3 = createBitmap;
        }
        Path path = new Path();
        path.moveTo(fArr[0], fArr[1]);
        path.lineTo(fArr[2], fArr[3]);
        path.lineTo(fArr[4], fArr[5]);
        path.close();
        canvas.drawPath(path, paint);
        path.moveTo(fArr2[0], fArr2[1]);
        path.lineTo(fArr2[2], fArr2[3]);
        path.lineTo(fArr2[4], fArr2[5]);
        path.close();
        canvas.drawPath(path, paint);
        return bitmap3;
    }

    public static Bitmap getTextYellowFrame(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            return null;
        }
        int parseColor = Color.parseColor("#FFFFE154");
        Canvas canvas = new Canvas();
        Path path = new Path();
        Paint paint = new Paint();
        paint.setColor(-1);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(4.0f);
        Paint paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setColorFilter(new PorterDuffColorFilter(-16777216, PorterDuff.Mode.SRC_IN));
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (bitmap2 == null) {
            Bitmap createBitmap = Bitmap.createBitmap(width + 40, height + 20, Bitmap.Config.ARGB_8888);
            float f = 8;
            path.addRoundRect(0.0f, 0.0f, createBitmap.getWidth(), createBitmap.getHeight(), f, f, Path.Direction.CW);
            canvas.setBitmap(createBitmap);
            canvas.clipPath(path);
            canvas.drawColor(parseColor);
            canvas.drawBitmap(bitmap, 20, 10, paint2);
            return createBitmap;
        }
        int width2 = bitmap2.getWidth();
        Bitmap createBitmap2 = Bitmap.createBitmap(width + 40, bitmap2.getHeight() + height + 20, Bitmap.Config.ARGB_8888);
        float f2 = 8;
        path.addRoundRect(0.0f, 0.0f, createBitmap2.getWidth(), createBitmap2.getHeight(), f2, f2, Path.Direction.CW);
        canvas.setBitmap(createBitmap2);
        canvas.clipPath(path);
        canvas.drawColor(parseColor);
        float f3 = 20;
        canvas.drawBitmap(bitmap, f3, 10, paint2);
        canvas.drawBitmap(bitmap2, f3 + ((width - width2) / 2.0f), height + 10, paint2);
        return createBitmap2;
    }

    public static Bitmap drawTextStyle(Bitmap bitmap, Bitmap bitmap2, String str) {
        if (TextUtils.isEmpty(str)) {
            return bitmap;
        }
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 654569:
                if (str.equals("三角")) {
                    c = 0;
                    break;
                }
                break;
            case 1007273:
                if (str.equals("竖线")) {
                    c = 1;
                    break;
                }
                break;
            case 1286658:
                if (str.equals("黄框")) {
                    c = 2;
                    break;
                }
                break;
            case 19883576:
                if (str.equals("下划线")) {
                    c = 3;
                    break;
                }
                break;
            case 25831940:
                if (str.equals("方括号")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return getTextTriangle(bitmap, bitmap2);
            case 1:
                return getTextVertical(bitmap, bitmap2);
            case 2:
                return getTextYellowFrame(bitmap, bitmap2);
            case 3:
                return getTextUnderline(bitmap, bitmap2);
            case 4:
                return getTextBracket(bitmap, bitmap2);
            default:
                return bitmap;
        }
    }

    public static Bitmap textAsBitmap(int i, String str, String str2, String str3, Context context) {
        Bitmap bitmap = null;
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        float f = (i / 1080.0f) * 45.0f;
        TextView textView = new TextView(context);
        textView.setTextAlignment(4);
        textView.setText(str2);
        textView.setTextSize(0, f);
        textView.setTextColor(-1);
        textView.setDrawingCacheEnabled(true);
        textView.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight());
        Bitmap createBitmap = Bitmap.createBitmap(textView.getDrawingCache());
        textView.destroyDrawingCache();
        if (!TextUtils.isEmpty(str3)) {
            TextView textView2 = new TextView(context);
            textView2.setTextAlignment(4);
            textView2.setText(str3);
            textView2.setTextSize(0, f);
            textView2.setTextColor(-1);
            textView2.setDrawingCacheEnabled(true);
            textView2.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            textView2.layout(0, 0, textView2.getMeasuredWidth(), textView2.getMeasuredHeight());
            bitmap = Bitmap.createBitmap(textView2.getDrawingCache());
            textView2.destroyDrawingCache();
        }
        return TextUtils.isEmpty(str) ? createBitmap : drawTextStyle(createBitmap, bitmap, str);
    }

    public static Bitmap textAsBitmap(Context context, int i, String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(str2)) {
            if (TextUtils.isEmpty(str3)) {
                if (!TextUtils.isEmpty(str2)) {
                    str = String.format("%s\n%s", str, str2);
                }
                str2 = null;
            }
            return textAsBitmap(i, str3, str, str2, context);
        }
        return null;
    }
}
