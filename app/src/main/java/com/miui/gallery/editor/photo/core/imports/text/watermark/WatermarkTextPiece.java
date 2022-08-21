package com.miui.gallery.editor.photo.core.imports.text.watermark;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class WatermarkTextPiece implements ITextSetting {
    public AutoLineLayout mAutoLineLayout;
    public int mColor;
    public final float mCountSizeSpacing;
    public final float mDefaultTextSize;
    public int mGradientsColor;
    public boolean mIsBoldText;
    public boolean mIsRTL;
    public boolean mIsShadow;
    public final float mMaxTextSize;
    public final float mMinTextSize;
    public RectF mOutlineDisplayRect;
    public RectF mOutlineRect;
    public Paint mPaint;
    public Resources mResources;
    public String mText;
    public RectF mTextInDialogRect;
    public final float mTextPaddingInsert;
    public WatermarkInfo.TextPieceInfo mTextPieceInfo;
    public TextStyle mTextStyle;
    public float mTextTransparent;
    public boolean mDrawOutline = true;
    public RectF mTextRect = new RectF();

    public WatermarkTextPiece(Resources resources, WatermarkInfo.TextPieceInfo textPieceInfo, float f, float f2, float f3) {
        this.mIsRTL = false;
        f3 = f3 > 1.0f ? 1.0f : f3;
        f3 = f3 < 0.3f ? 0.3f : f3;
        this.mResources = resources;
        this.mTextPieceInfo = textPieceInfo;
        if (resources.getConfiguration().getLayoutDirection() == 1) {
            this.mIsRTL = true;
        }
        float f4 = textPieceInfo.size;
        if (f4 == 0.0f) {
            this.mDefaultTextSize = resources.getDimension(R.dimen.text_append_default_text_size) * f3;
        } else {
            this.mDefaultTextSize = ScreenUtils.dpToPixel(f4) * f3;
        }
        float f5 = textPieceInfo.minSize;
        if (f5 > 0.0f) {
            this.mMinTextSize = ScreenUtils.dpToPixel(f5) * f3;
        } else {
            this.mMinTextSize = resources.getDimensionPixelSize(R.dimen.px_24);
        }
        this.mMaxTextSize = this.mDefaultTextSize;
        this.mCountSizeSpacing = resources.getDisplayMetrics().density;
        this.mTextPaddingInsert = resources.getDimension(R.dimen.text_watermark_padding_insert);
        this.mColor = -1;
        this.mTextStyle = null;
        this.mTextTransparent = 0.0f;
        this.mIsBoldText = false;
        this.mIsShadow = false;
        AutoLineLayout autoLineLayout = new AutoLineLayout();
        this.mAutoLineLayout = autoLineLayout;
        autoLineLayout.getPaint().setTextSize(this.mDefaultTextSize);
        this.mAutoLineLayout.setLineHeightOffset(resources.getDimension(R.dimen.text_append_line_height_offset));
        this.mAutoLineLayout.setTextAlignment(this.mIsRTL ? AutoLineLayout.TextAlignment.RIGHT : AutoLineLayout.TextAlignment.LEFT);
        this.mAutoLineLayout.setSingleVerticalText(this.mTextPieceInfo.isVerticalText == 1);
        this.mAutoLineLayout.setText(this.mTextPieceInfo.text);
        this.mAutoLineLayout.setLetterSpace(this.mTextPieceInfo.letterSpacing);
        this.mAutoLineLayout.setMaxLines(this.mTextPieceInfo.lines);
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setColor(-1);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeWidth(3.0f);
        this.mPaint.setPathEffect(new DashPathEffect(new float[]{8.0f, 4.0f}, 0.0f));
        this.mPaint.setShadowLayer(1.0f, 0.0f, 0.0f, resources.getColor(R.color.text_watermark_outline_shadow));
        RectF rectF = new RectF(ScreenUtils.dpToPixel(this.mTextPieceInfo.frameRect[0]), ScreenUtils.dpToPixel(this.mTextPieceInfo.frameRect[1]), ScreenUtils.dpToPixel(this.mTextPieceInfo.frameRect[2]), ScreenUtils.dpToPixel(this.mTextPieceInfo.frameRect[3]));
        this.mOutlineRect = rectF;
        rectF.offset((-f) / 2.0f, (-f2) / 2.0f);
        this.mText = this.mTextPieceInfo.text;
        this.mTextInDialogRect = new RectF();
        RectF rectF2 = new RectF(this.mOutlineRect);
        this.mOutlineDisplayRect = rectF2;
        rectF2.bottom -= 3.0f;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        if (this.mDrawOutline) {
            canvas.drawRect(this.mOutlineDisplayRect, this.mPaint);
        }
        canvas.translate(this.mOutlineRect.centerX(), this.mOutlineRect.centerY());
        if (!TextUtils.isEmpty(this.mText)) {
            canvas.translate(this.mTextRect.width() / 2.0f, this.mTextRect.height() / 2.0f);
            int i = this.mTextPieceInfo.align;
            if (i == 0) {
                canvas.translate((-this.mTextInDialogRect.width()) / 2.0f, (-this.mTextRect.height()) / 2.0f);
            } else if (i == 1) {
                canvas.translate((-this.mTextRect.width()) / 2.0f, (-this.mTextRect.height()) / 2.0f);
            } else if (i == 2) {
                canvas.translate((this.mTextInDialogRect.width() / 2.0f) - this.mTextRect.width(), (-this.mTextRect.height()) / 2.0f);
            }
            if (this.mAutoLineLayout.isStroke()) {
                configStrokePaint(this.mAutoLineLayout.getStrokePaint());
            }
            this.mAutoLineLayout.draw(canvas);
        }
        canvas.restore();
    }

    public void configTextPaint(Paint paint) {
        if (paint == null) {
            paint = this.mAutoLineLayout.getPaint();
        }
        refreshPaintColor();
        TextStyle textStyle = this.mTextStyle;
        paint.setTypeface(textStyle == null ? Typeface.DEFAULT : textStyle.getTypeFace());
        paint.setAlpha(getAlphaByConfig());
        paint.setFakeBoldText(this.mIsBoldText);
        if (this.mIsShadow) {
            paint.setShadowLayer(1.0f, 1.0f, 1.0f, 1711276032);
        } else {
            paint.clearShadowLayer();
        }
    }

    public final void setGradientsPaint(Paint paint) {
        paint.setShader(new LinearGradient(0.0f, this.mTextRect.height(), this.mTextRect.width(), 0.0f, this.mColor, this.mGradientsColor, Shader.TileMode.REPEAT));
    }

    public final void configStrokePaint(TextPaint textPaint) {
        textPaint.set(this.mAutoLineLayout.getPaint());
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint.setStrokeWidth(this.mResources.getDimensionPixelSize(R.dimen.text_edit_stroke_width));
        int i = -1;
        if (this.mColor == -1) {
            i = -16777216;
        }
        textPaint.setColor(i);
        textPaint.setTextSize(this.mAutoLineLayout.getPaint().getTextSize());
        textPaint.setShader(null);
    }

    public void countTextInDialog() {
        if (TextUtils.isEmpty(this.mText)) {
            return;
        }
        this.mAutoLineLayout.setText(this.mText);
        this.mTextInDialogRect.set(this.mOutlineRect);
        this.mTextInDialogRect.inset(this.mTextPaddingInsert, 0.0f);
        long currentTimeMillis = System.currentTimeMillis();
        this.mAutoLineLayout.countTextArea(this.mTextInDialogRect.width(), this.mTextInDialogRect.height() * 1.5f, this.mMinTextSize, this.mMaxTextSize, this.mCountSizeSpacing);
        long currentTimeMillis2 = System.currentTimeMillis();
        this.mAutoLineLayout.getTextRect(this.mTextRect);
        DefaultLogger.d("WatermarkTextPiece", "count text time： %d", Long.valueOf(currentTimeMillis2 - currentTimeMillis));
    }

    public final int getAlphaByConfig() {
        return (int) (255.0f - (this.mTextTransparent * 230.0f));
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setColor(int i) {
        this.mColor = i;
        refreshPaintColor();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setGradientsColor(int i) {
        this.mGradientsColor = i;
        refreshPaintColor();
    }

    public void refreshPaintColor() {
        Paint paint = this.mAutoLineLayout.getPaint();
        if (this.mGradientsColor != 0) {
            setGradientsPaint(paint);
            return;
        }
        paint.setShader(null);
        paint.setColor(this.mColor);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setText(String str) {
        this.mText = str;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setShadow(boolean z) {
        this.mIsShadow = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextTransparent(float f) {
        this.mTextTransparent = f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextStyle(TextStyle textStyle) {
        this.mTextStyle = textStyle;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setBoldText(boolean z) {
        this.mIsBoldText = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public String getText() {
        return this.mText;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public float getTextTransparent() {
        return this.mTextTransparent;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public int getColor() {
        return this.mColor;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public TextStyle getTextStyle() {
        return this.mTextStyle;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isBoldText() {
        return this.mIsBoldText;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isShadow() {
        return this.mIsShadow;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public AutoLineLayout.TextAlignment getTextAlignment() {
        return this.mAutoLineLayout.getTextAlignment();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextAlignment(AutoLineLayout.TextAlignment textAlignment) {
        this.mAutoLineLayout.setTextAlignment(textAlignment);
    }

    public void setDrawOutline(boolean z) {
        this.mDrawOutline = z;
    }

    public RectF getOutlineRect() {
        return this.mOutlineRect;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setIsStroke(boolean z) {
        this.mAutoLineLayout.setIsStroke(z);
    }
}
