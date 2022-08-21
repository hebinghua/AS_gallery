package com.miui.gallery.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ReplacementSpan;
import androidx.core.content.ContextCompat;
import com.miui.gallery.base.R$color;

/* loaded from: classes2.dex */
public class CenterTextSpan extends ReplacementSpan {
    public Context mContext;
    public boolean mIsFrontText;
    public int mTextSize;

    public CenterTextSpan(Context context, int i, boolean z) {
        this.mContext = context.getApplicationContext();
        this.mTextSize = i;
        this.mIsFrontText = z;
    }

    @Override // android.text.style.ReplacementSpan
    public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
        return (int) getCustomTextPaint(paint).measureText(charSequence, i, i2);
    }

    @Override // android.text.style.ReplacementSpan
    public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
        TextPaint customTextPaint = getCustomTextPaint(paint);
        Paint.FontMetricsInt fontMetricsInt = customTextPaint.getFontMetricsInt();
        if (this.mIsFrontText) {
            customTextPaint.setColor(ContextCompat.getColor(this.mContext, R$color.text_non_transparent));
            customTextPaint.setTypeface(Typeface.create("mipro-medium", 0));
        }
        canvas.drawText(charSequence, i, i2, f, i4 - (((((fontMetricsInt.ascent + i4) + i4) + fontMetricsInt.descent) / 2) - ((i3 + i5) / 2)), customTextPaint);
    }

    public final TextPaint getCustomTextPaint(Paint paint) {
        int i = this.mTextSize;
        if (i != 0) {
            paint.setTextSize(i * 1.0f);
        }
        return (TextPaint) paint;
    }
}
