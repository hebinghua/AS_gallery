package com.xiaomi.milab.videosdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/* loaded from: classes3.dex */
public final class Bitmaps {
    private static final String TAG = "Graphics";

    /* loaded from: classes3.dex */
    public enum SampleArea {
        START,
        CENTER,
        END
    }

    private Bitmaps() {
    }

    public static Bitmap createSample(Bitmap bitmap, int i, int i2, SampleArea sampleArea) {
        Objects.requireNonNull(bitmap, "source bitmap can't be null");
        if (i * i2 == 0) {
            throw new IllegalArgumentException(String.format(Locale.US, "%dx%d is not valid size of bitmap", Integer.valueOf(i), Integer.valueOf(i2)));
        }
        if (sampleArea == null) {
            sampleArea = SampleArea.START;
        }
        System.currentTimeMillis();
        Matrix matrix = new Matrix();
        float width = bitmap.getWidth() / i;
        float height = bitmap.getHeight() / i2;
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        if (Float.compare(width, height) >= 0) {
            width = height;
        }
        int round = Math.round(bitmap.getWidth() / width);
        int round2 = Math.round(bitmap.getHeight() / width);
        float f = 1.0f / width;
        matrix.postScale(f, f);
        if (sampleArea == SampleArea.CENTER) {
            matrix.postTranslate((i - round) / 2, (i2 - round2) / 2);
        } else if (sampleArea == SampleArea.END) {
            matrix.postTranslate(i - round, i2 - round2);
        }
        new Canvas(createBitmap).drawBitmap(bitmap, matrix, new Paint(3));
        return createBitmap;
    }

    public static Bitmap decodeUri(Context context, Uri uri, BitmapFactory.Options options) throws FileNotFoundException {
        InputStream openInputStream;
        InputStream inputStream = null;
        try {
            openInputStream = IOUtils.openInputStream(context, uri);
        } catch (Throwable th) {
            th = th;
        }
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(openInputStream, null, options);
            IOUtils.close(openInputStream);
            return decodeStream;
        } catch (Throwable th2) {
            th = th2;
            inputStream = openInputStream;
            IOUtils.close(inputStream);
            throw th;
        }
    }

    public static Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options) {
        try {
            return BitmapFactory.decodeStream(inputStream, null, options);
        } finally {
            IOUtils.close(inputStream);
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

    public static Bitmap copyBitmap(Bitmap bitmap) {
        Bitmap bitmap2 = null;
        if (bitmap == null) {
            return null;
        }
        try {
            try {
                bitmap2 = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            } catch (OutOfMemoryError e) {
                Log.e(TAG, "copyBitmap error:" + e.toString());
            }
            return bitmap2;
        } finally {
            bitmap.recycle();
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

    public static Bitmap textAsBitmap(String str, TextPaint textPaint, float f) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new StyleSpan(0), 0, str.length(), 33);
        StaticLayout staticLayout = new StaticLayout(spannableString, textPaint, (int) f, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        Bitmap createBitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);
        staticLayout.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap textEvaporateAsBitmap(String str, TextPaint textPaint, float f, float f2, boolean z) {
        return textSubtitleAsBitmap(str, textPaint, f, f2, z, null);
    }

    public static Bitmap textSubtitleAsBitmap(String str, TextPaint textPaint, float f, float f2, boolean z, CueDiffInfo cueDiffInfo) {
        Bitmap bitmap;
        int i;
        int i2;
        Canvas canvas;
        TextPaint textPaint2;
        List<Float> list;
        int i3;
        float f3;
        int i4;
        List<Float> list2;
        int i5;
        float f4;
        TextPaint textPaint3;
        float f5 = f2 > 1.0f ? 1.0f : f2;
        CueDiffInfo cueDiffInfo2 = cueDiffInfo == null ? new CueDiffInfo(str, "", textPaint) : cueDiffInfo;
        String str2 = cueDiffInfo2.oldText;
        List<CharacterDiffResult> list3 = cueDiffInfo2.differentList;
        int max = Math.max(str.length(), str2.length());
        float f6 = 4;
        float length = f6 / ((str.length() + 4) - 1);
        List<Float> list4 = cueDiffInfo2.oldGapList;
        List<Float> list5 = cueDiffInfo2.gapList;
        TextPaint textPaint4 = new TextPaint();
        textPaint4.setStyle(textPaint.getStyle());
        textPaint4.setAntiAlias(textPaint.isAntiAlias());
        textPaint4.setFilterBitmap(textPaint.isFilterBitmap());
        textPaint4.setLetterSpacing(textPaint.getLetterSpacing());
        textPaint4.setColor(textPaint.getColor());
        textPaint4.setTextSize(textPaint.getTextSize());
        textPaint4.setUnderlineText(textPaint.isUnderlineText());
        textPaint4.setFakeBoldText(textPaint.isFakeBoldText());
        textPaint4.setStrikeThruText(textPaint.isStrikeThruText());
        textPaint4.setTextSkewX(0.0f);
        SpannableString spannableString = new SpannableString(str);
        List<Float> list6 = list4;
        spannableString.setSpan(new StyleSpan(0), 0, str.length(), 33);
        StaticLayout staticLayout = new StaticLayout(spannableString, textPaint4, (int) f, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        float lineBaseline = staticLayout.getLineBaseline(0);
        Bitmap createBitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(createBitmap);
        int i6 = cueDiffInfo2.mTextHeight;
        int i7 = 0;
        int i8 = 0;
        float f7 = 0.0f;
        while (i7 < max) {
            List<Float> list7 = list5;
            if (i7 < str2.length()) {
                textPaint4.setTextSize(textPaint.getTextSize());
                float length2 = f5 / (((length / f6) * (str.length() - 1)) + length);
                int needMove = CharacterUtils.needMove(i7, list3);
                i = max;
                if (needMove != -1) {
                    textPaint4.setAlpha(255);
                    float f8 = length2 * 2.0f;
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                    int i9 = i7;
                    canvas = canvas2;
                    f3 = 0.0f;
                    bitmap = createBitmap;
                    i3 = 255;
                    list = list7;
                    textPaint3 = textPaint4;
                    i4 = 0;
                    float offset = CharacterUtils.getOffset(i7, needMove, f8, 0.0f, 0.0f, list, list6);
                    StringBuilder sb = new StringBuilder();
                    i2 = i9;
                    sb.append(str2.charAt(i2));
                    sb.append("");
                    canvas.drawText(sb.toString(), 0, 1, offset, lineBaseline, (Paint) textPaint3);
                } else {
                    bitmap = createBitmap;
                    i2 = i7;
                    canvas = canvas2;
                    textPaint3 = textPaint4;
                    list = list7;
                    i3 = 255;
                    f3 = 0.0f;
                    i4 = 0;
                    if (f5 < 1.0f) {
                        textPaint2 = textPaint3;
                        textPaint2.setAlpha((int) ((1.0f - length2) * 255.0f));
                        String str3 = str2.charAt(i2) + "";
                        list2 = list6;
                        canvas.drawText(str3, 0, 1, f7 + ((list2.get(i2).floatValue() - textPaint2.measureText(str2.charAt(i2) + "")) / 2.0f), lineBaseline - (length2 * i6), (Paint) textPaint2);
                        f7 += list2.get(i2).floatValue();
                    }
                }
                list2 = list6;
                textPaint2 = textPaint3;
                f7 += list2.get(i2).floatValue();
            } else {
                bitmap = createBitmap;
                i = max;
                i2 = i7;
                canvas = canvas2;
                textPaint2 = textPaint4;
                list = list7;
                i3 = 255;
                f3 = 0.0f;
                i4 = 0;
                list2 = list6;
            }
            if (i2 < str.length()) {
                if (!CharacterUtils.stayHere(i2, list3)) {
                    float f9 = f5 - ((i2 * length) / f6);
                    int i10 = (int) ((255.0f / length) * f9);
                    if (i10 > i3) {
                        i10 = i3;
                    }
                    if (i10 < 0) {
                        i10 = i4;
                    }
                    textPaint2.setAlpha(i10);
                    if (z) {
                        float textSize = ((textPaint.getTextSize() * 1.0f) / length) * f9;
                        if (textSize > textPaint.getTextSize()) {
                            textSize = textPaint.getTextSize();
                        }
                        if (textSize < f3) {
                            textSize = f3;
                        }
                        textPaint2.setTextSize(textSize);
                    }
                    float length3 = f5 / (((length / f6) * (str.length() - 1)) + length);
                    if (StringUtils.isEmpty(str2)) {
                        f4 = lineBaseline;
                    } else {
                        float f10 = i6;
                        f4 = (f10 + lineBaseline) - (length3 * f10);
                    }
                    String str4 = str.charAt(i2) + "";
                    i5 = i8;
                    canvas.drawText(str4, 0, 1, i5 + ((list.get(i2).floatValue() - textPaint2.measureText(str.charAt(i2) + "")) / 2.0f), f4, (Paint) textPaint2);
                } else {
                    i5 = i8;
                }
                i8 = (int) (i5 + list.get(i2).floatValue());
            }
            i7 = i2 + 1;
            createBitmap = bitmap;
            list5 = list;
            list6 = list2;
            max = i;
            textPaint4 = textPaint2;
            canvas2 = canvas;
        }
        return createBitmap;
    }

    public static void saveBitmap(String str, Bitmap bitmap) {
        File file = new File(str);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (str.endsWith(".png")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, fileOutputStream);
        } else if (str.endsWith(".jpeg") || str.endsWith(".jpg")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream);
        }
        try {
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromFile(String str) {
        if (!new File(str).exists()) {
            return null;
        }
        try {
            return BitmapFactory.decodeFile(str);
        } catch (Exception e) {
            Log.e(TAG, "getBitmapFromFile: ", e);
            return null;
        }
    }
}
