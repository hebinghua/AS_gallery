package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.TypedValue;
import com.miui.gallery.R;
import com.miui.gallery.util.ScreenUtils;

/* loaded from: classes2.dex */
public class PhotoPageErrorDrawable extends Drawable {
    public Paint mBackgroundPaint;
    public Rect mBackgroundRect;
    public BitmapDrawable mIcon;
    public Rect mTextCompoundDrawablePaddingRect;
    public StaticLayout mTextLayout;
    public TextPaint mTextPaint;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public PhotoPageErrorDrawable(Context context, int i, int i2) {
        Resources resources = context.getResources();
        i = i == 0 ? ScreenUtils.getScreenWidth() : i;
        this.mBackgroundRect = new Rect(0, 0, i, i2 == 0 ? ScreenUtils.getFullScreenHeight(context) : i2);
        Paint paint = new Paint();
        this.mBackgroundPaint = paint;
        paint.setColor(resources.getColor(R.color.photo_page_empty_background));
        TextPaint textPaint = new TextPaint(1);
        this.mTextPaint = textPaint;
        textPaint.setColor(resources.getColor(R.color.photo_page_empty_text));
        this.mTextPaint.setTextSize((TypedValue.applyDimension(0, resources.getDimensionPixelSize(R.dimen.photo_empty_text), resources.getDisplayMetrics()) * i) / ScreenUtils.getScreenWidth());
        this.mTextCompoundDrawablePaddingRect = new Rect(0, 0, 0, resources.getDimensionPixelSize(R.dimen.photo_empty_drawable_padding));
    }

    public void setTip(CharSequence charSequence) {
        if (!TextUtils.isEmpty(charSequence)) {
            this.mTextLayout = new StaticLayout(charSequence.toString(), this.mTextPaint, getIntrinsicWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        } else {
            this.mTextLayout = null;
        }
    }

    public void setIcon(BitmapDrawable bitmapDrawable) {
        this.mIcon = (BitmapDrawable) bitmapDrawable.mutate();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.mBackgroundRect.width();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.mBackgroundRect.height();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.drawRect(this.mBackgroundRect, this.mBackgroundPaint);
        if (this.mTextLayout != null) {
            canvas.translate(0.0f, (getIntrinsicHeight() - this.mTextLayout.getHeight()) / 2.0f);
            this.mTextLayout.draw(canvas);
            canvas.translate(0.0f, (-(getIntrinsicHeight() - this.mTextLayout.getHeight())) / 2.0f);
        }
        BitmapDrawable bitmapDrawable = this.mIcon;
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            float intrinsicWidth = (getIntrinsicWidth() - this.mIcon.getIntrinsicWidth()) / 2.0f;
            int intrinsicHeight = getIntrinsicHeight();
            StaticLayout staticLayout = this.mTextLayout;
            canvas.drawBitmap(bitmap, intrinsicWidth, (((intrinsicHeight - (staticLayout == null ? 0 : staticLayout.getHeight())) / 2.0f) - this.mTextCompoundDrawablePaddingRect.height()) - this.mIcon.getIntrinsicHeight(), this.mIcon.getPaint());
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mBackgroundPaint.setAlpha(i);
        TextPaint textPaint = this.mTextPaint;
        if (textPaint != null) {
            textPaint.setAlpha(i);
        }
        BitmapDrawable bitmapDrawable = this.mIcon;
        if (bitmapDrawable != null) {
            bitmapDrawable.setAlpha(i);
        }
    }
}
