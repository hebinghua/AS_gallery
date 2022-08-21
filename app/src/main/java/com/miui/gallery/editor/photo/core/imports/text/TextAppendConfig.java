package com.miui.gallery.editor.photo.core.imports.text;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextPaint;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.dialog.DialogManager;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexClip;

/* loaded from: classes2.dex */
public class TextAppendConfig implements Parcelable, ITextDialogConfig {
    public static final Parcelable.Creator<TextAppendConfig> CREATOR = new Parcelable.Creator<TextAppendConfig>() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextAppendConfig.2
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public TextAppendConfig mo877createFromParcel(Parcel parcel) {
            return new TextAppendConfig(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public TextAppendConfig[] mo878newArray(int i) {
            return new TextAppendConfig[i];
        }
    };
    public AutoLineLayout mAutoLineLayout;
    public AutoLineLayout.Callbacks mCallbacks;
    public int mColor;
    public Context mContext;
    public final int mCornerDialogWidth;
    public final int mCorrectionDegrees;
    public final float mCountSizeSpacing;
    public RectF mCurrentImageRectF;
    public float mDefaultLocationX;
    public float mDefaultLocationY;
    public final int mDefaultStrokeWidth;
    public final float mDefaultTextSize;
    public Drawable mDialogDrawable;
    public BaseDialogModel mDialogModel;
    public final int mDialogOutLineOffsetX;
    public final int mDialogOutLineOffsetY;
    public final int mDialogPaddingBottom;
    public RectF mDialogRect;
    public final int mDialogWidth;
    public int mGradientsColor;
    public RectF mInitImageRectF;
    public boolean mIsActivation;
    public boolean mIsBoldText;
    public boolean mIsCorrection;
    public boolean mIsMirror;
    public boolean mIsRTL;
    public boolean mIsShadow;
    public boolean mIsSubstrate;
    public Matrix mMatrix;
    public final float mMaxTextSize;
    public float mMaxWidth;
    public final int mMinTextSize;
    public String mName;
    public final int mOutLineOffsetX;
    public final int mOutLineOffsetY;
    public RectF mOutLineRect;
    public boolean mReverseColor;
    public float mRotateDegrees;
    public float mScaleMultipleOrigin;
    public RectF mSubstrateActualRectF;
    public int[] mSubstrateColors;
    public Paint mSubstratePaint;
    public RectF mTemRect;
    public RectF mTempRectF;
    public String mText;
    public final float mTextAppendPadding;
    public TextDrawable mTextDrawable;
    public RectF mTextInDialogRect;
    public RectF mTextRect;
    public TextStyle mTextStyle;
    public float mTextTransparent;
    public float mUserLocationX;
    public float mUserLocationY;
    public float mUserScaleMultiple;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isSignature() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isWatermark() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void onDetachedFromWindow() {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDrawOutline(boolean z) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setSignatureDrawable(BaseDialogModel baseDialogModel, Drawable drawable) {
    }

    public TextAppendConfig(Context context) {
        this.mIsRTL = false;
        this.mName = DialogManager.LocalDialog.NONE.name();
        this.mCallbacks = new AutoLineLayout.Callbacks() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextAppendConfig.1
            @Override // com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout.Callbacks
            public Shader getShader(float f) {
                return new LinearGradient(0.0f, TextAppendConfig.this.mAutoLineLayout.getLineHeight(), f, 0.0f, TextAppendConfig.this.mColor, TextAppendConfig.this.mGradientsColor, Shader.TileMode.REPEAT);
            }
        };
        this.mContext = context;
        Resources resources = context.getResources();
        if (resources.getConfiguration().getLayoutDirection() == 1) {
            this.mIsRTL = true;
        }
        this.mOutLineOffsetX = resources.getDimensionPixelSize(R.dimen.text_append_out_line_offset_x);
        this.mOutLineOffsetY = resources.getDimensionPixelSize(R.dimen.text_append_out_line_offset_y);
        this.mDialogOutLineOffsetX = resources.getDimensionPixelSize(R.dimen.text_append_dialog_out_line_offset_x);
        this.mDialogOutLineOffsetY = resources.getDimensionPixelSize(R.dimen.text_append_dialog_out_line_offset_y);
        this.mDialogWidth = resources.getDimensionPixelSize(R.dimen.text_append_dialog_bg_default_width);
        this.mCornerDialogWidth = resources.getDimensionPixelSize(R.dimen.text_append_corner_dialog_default_width);
        this.mMinTextSize = resources.getDimensionPixelSize(R.dimen.text_append_min_text_size);
        this.mDefaultStrokeWidth = resources.getDimensionPixelSize(R.dimen.text_append_default_stroke_width);
        this.mCorrectionDegrees = 5;
        float dimension = resources.getDimension(R.dimen.text_append_default_text_size);
        this.mDefaultTextSize = dimension;
        this.mMaxTextSize = resources.getDimension(R.dimen.text_append_max_text_size);
        this.mCountSizeSpacing = resources.getDisplayMetrics().density;
        this.mTextAppendPadding = resources.getDimension(R.dimen.text_append_padding);
        this.mDialogPaddingBottom = resources.getDimensionPixelSize(R.dimen.text_append_dialog_padding_bottom);
        this.mSubstratePaint = new Paint();
        this.mColor = -1;
        this.mTextStyle = null;
        if (this.mText == null) {
            this.mText = resources.getString(R.string.text_append_hint);
        }
        this.mIsActivation = false;
        this.mTextTransparent = 0.0f;
        this.mIsBoldText = false;
        this.mIsShadow = false;
        this.mDefaultLocationX = -1.0f;
        this.mDefaultLocationY = -1.0f;
        this.mUserLocationX = 0.0f;
        this.mUserLocationY = 0.0f;
        this.mUserScaleMultiple = 1.0f;
        this.mScaleMultipleOrigin = 1.0f;
        this.mRotateDegrees = 0.0f;
        this.mIsMirror = false;
        this.mIsCorrection = false;
        AutoLineLayout autoLineLayout = new AutoLineLayout();
        this.mAutoLineLayout = autoLineLayout;
        autoLineLayout.getPaint().setTextSize(dimension);
        this.mAutoLineLayout.setLineHeightOffset(resources.getDimension(R.dimen.text_append_line_height_offset));
        this.mOutLineRect = new RectF();
        this.mTextRect = new RectF();
        this.mTextInDialogRect = new RectF();
        this.mDialogRect = new RectF();
        this.mInitImageRectF = new RectF();
        this.mCurrentImageRectF = new RectF();
        this.mTempRectF = new RectF();
        this.mSubstrateActualRectF = new RectF();
        this.mTemRect = new RectF();
        this.mMatrix = new Matrix();
        this.mTextDrawable = new TextDrawable();
        this.mAutoLineLayout.setTextAlignment(this.mIsRTL ? AutoLineLayout.TextAlignment.RIGHT : AutoLineLayout.TextAlignment.LEFT);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDialogModel(BaseDialogModel baseDialogModel, Resources resources) {
        this.mDialogModel = baseDialogModel;
        Drawable readDialogDrawable = baseDialogModel.readDialogDrawable(resources);
        this.mDialogDrawable = readDialogDrawable;
        if (readDialogDrawable == null) {
            this.mDialogModel = null;
        }
        updateDefaultLocation();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public BaseDialogModel getDialogModel() {
        return this.mDialogModel;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public String getSampleName() {
        if (this.mDialogModel != null) {
            return "dialog_" + this.mDialogModel.name;
        }
        return "dialog_none";
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void toggleMirror() {
        this.mIsMirror = !this.mIsMirror;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public int getGradientsColor() {
        return this.mGradientsColor;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setGradientsColor(int i) {
        this.mGradientsColor = i;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setColor(int i) {
        this.mColor = i;
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
    public void setTextStyle(TextStyle textStyle) {
        this.mTextStyle = textStyle;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public String getText() {
        return this.mText;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setText(String str) {
        this.mText = str;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextTransparent(float f) {
        this.mTextTransparent = f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public float getTextTransparent() {
        return this.mTextTransparent;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isActivation() {
        return this.mIsActivation;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setActivation(boolean z) {
        this.mIsActivation = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isBoldText() {
        return this.mIsBoldText;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setBoldText(boolean z) {
        this.mIsBoldText = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isShadow() {
        return this.mIsShadow;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setShadow(boolean z) {
        this.mIsShadow = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getUserLocationX() {
        return this.mUserLocationX;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getUserLocationY() {
        return this.mUserLocationY;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public AutoLineLayout.TextAlignment getTextAlignment() {
        return this.mAutoLineLayout.getTextAlignment();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextAlignment(AutoLineLayout.TextAlignment textAlignment) {
        this.mAutoLineLayout.setTextAlignment(textAlignment);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void appendUserLocationX(float f) {
        this.mUserLocationX += Math.signum(f) * getImageRectMatrix(true).mapRadius(f);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void appendUserLocationY(float f) {
        this.mUserLocationY += Math.signum(f) * getImageRectMatrix(true).mapRadius(f);
    }

    public final void setUserLocationX(float f) {
        this.mUserLocationX = f;
    }

    public final void setUserLocationY(float f) {
        this.mUserLocationY = f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void getOutLineRect(RectF rectF) {
        rectF.set(this.mOutLineRect);
        offsetRect(rectF);
        getImageRectMatrix(false).mapRect(rectF);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setUserScaleMultiple(float f) {
        this.mUserScaleMultiple = f;
        if (f < 0.3f) {
            this.mUserScaleMultiple = 0.3f;
        }
        if (this.mUserScaleMultiple > 5.0f) {
            this.mUserScaleMultiple = 5.0f;
        }
    }

    public float getScaleMultiple() {
        return this.mUserScaleMultiple * this.mScaleMultipleOrigin;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getUserScaleMultiple() {
        return this.mUserScaleMultiple;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getRotateDegrees() {
        float f = this.mRotateDegrees;
        float f2 = 0.0f;
        if (f < 0.0f) {
            f += 360.0f;
        }
        float f3 = f % 360.0f;
        this.mIsCorrection = false;
        if (f3 > 0.0f && f3 < this.mCorrectionDegrees) {
            this.mIsCorrection = true;
            f3 = 0.0f;
        }
        int i = this.mCorrectionDegrees;
        if (f3 > 360 - i) {
            this.mIsCorrection = true;
        } else {
            f2 = f3;
        }
        if (f2 != 90.0f && f2 > 90 - i && f2 < i + 90) {
            this.mIsCorrection = true;
            f2 = 90.0f;
        }
        if (f2 != 180.0f && f2 > 180 - i && f2 < i + nexClip.kClip_Rotate_180) {
            this.mIsCorrection = true;
            f2 = 180.0f;
        }
        if (f2 == 270.0f || f2 <= 270 - i || f2 >= i + nexClip.kClip_Rotate_270) {
            return f2;
        }
        this.mIsCorrection = true;
        return 270.0f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setRotateDegrees(float f) {
        this.mRotateDegrees = f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void refreshRotateDegree() {
        this.mRotateDegrees = getRotateDegrees();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isDialogEnable() {
        BaseDialogModel baseDialogModel = this.mDialogModel;
        return baseDialogModel != null && baseDialogModel.hasDialog();
    }

    public final boolean isCornerDialog() {
        BaseDialogModel baseDialogModel = this.mDialogModel;
        return baseDialogModel != null && baseDialogModel.isCorner;
    }

    public final void offsetRect(RectF rectF) {
        rectF.offset(this.mDefaultLocationX + this.mUserLocationX, this.mDefaultLocationY + this.mUserLocationY);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean contains(float f, float f2) {
        getOutLineRect(this.mTemRect);
        this.mMatrix.reset();
        this.mMatrix.postRotate(-getRotateDegrees(), this.mTemRect.centerX(), this.mTemRect.centerY());
        float[] fArr = {f, f2};
        this.mMatrix.mapPoints(fArr);
        return this.mTemRect.contains(fArr[0], fArr[1]);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void countLocation(boolean z, float f) {
        this.mMaxWidth = f;
        float f2 = (f / this.mScaleMultipleOrigin) - this.mTextAppendPadding;
        configTextPaint(this.mAutoLineLayout.getPaint());
        if (this.mAutoLineLayout.isStroke()) {
            configStrokePaint(this.mAutoLineLayout.getStrokePaint());
        }
        if (!isDialogEnable()) {
            if (z) {
                countTextNormal(f2);
            }
            countOutLine();
        } else if (isCornerDialog()) {
            if (z) {
                countTextNormal(f2);
            }
            countDialogCorner();
            countOutLine();
        } else {
            countDialog();
            if (z) {
                countTextInDialog();
            }
            countOutLine();
        }
    }

    public final void configStrokePaint(TextPaint textPaint) {
        textPaint.set(this.mAutoLineLayout.getPaint());
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setStrokeJoin(Paint.Join.ROUND);
        textPaint.setStrokeWidth(this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_stroke_width));
        int i = -1;
        if (this.mColor == -1) {
            i = -16777216;
        }
        textPaint.setColor(i);
        textPaint.setTextSize(this.mAutoLineLayout.getPaint().getTextSize());
        textPaint.setShader(null);
    }

    public final void countOutLine() {
        this.mAutoLineLayout.getTextRect(this.mTextRect);
        if (!isDialogEnable()) {
            if (TextUtils.isEmpty(this.mText)) {
                this.mTextRect.setEmpty();
                RectF rectF = this.mTextRect;
                int i = this.mDefaultStrokeWidth;
                rectF.inset(-i, -i);
            }
            this.mOutLineRect.set(this.mTextRect);
        } else if (isCornerDialog()) {
            this.mOutLineRect.set(this.mDialogRect);
            this.mOutLineRect.union(this.mTextInDialogRect);
        } else {
            this.mOutLineRect.set(this.mDialogRect);
        }
        if (isDialogEnable()) {
            if (this.mDialogModel.isBubbleModel()) {
                this.mOutLineRect.inset(-this.mDialogOutLineOffsetX, -this.mDialogOutLineOffsetY);
            }
        } else {
            this.mOutLineRect.inset(-this.mOutLineOffsetX, -this.mOutLineOffsetY);
        }
        this.mOutLineRect.inset(-(((this.mOutLineRect.width() * getScaleMultiple()) - this.mOutLineRect.width()) / 2.0f), -(((this.mOutLineRect.height() * getScaleMultiple()) - this.mOutLineRect.height()) / 2.0f));
    }

    public final void countDialog() {
        float intrinsicWidth = this.mDialogDrawable.getIntrinsicWidth();
        float intrinsicHeight = this.mDialogDrawable.getIntrinsicHeight();
        BaseDialogModel baseDialogModel = this.mDialogModel;
        if (baseDialogModel != null && baseDialogModel.isBubbleModel()) {
            int i = this.mDialogWidth;
            intrinsicHeight *= i / intrinsicWidth;
            intrinsicWidth = i;
        }
        float f = intrinsicHeight / 2.0f;
        float f2 = intrinsicWidth / 2.0f;
        this.mDialogRect.set(-f2, -f, f2, f);
    }

    public final void countTextNormal(float f) {
        this.mAutoLineLayout.setText(this.mText);
        long currentTimeMillis = System.currentTimeMillis();
        this.mAutoLineLayout.countText(f, -1.0f);
        DefaultLogger.d("TextAppendConfig", "文字计算耗时： %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public final void countTextInDialog() {
        this.mTextInDialogRect.setEmpty();
        if (TextUtils.isEmpty(this.mText)) {
            return;
        }
        this.mAutoLineLayout.setText(this.mText);
        this.mTextInDialogRect.set(this.mDialogRect);
        this.mDialogModel.configRect(this.mTextInDialogRect, false);
        long currentTimeMillis = System.currentTimeMillis();
        this.mAutoLineLayout.countTextArea(this.mTextInDialogRect.width(), this.mTextInDialogRect.height(), this.mMinTextSize, this.mMaxTextSize, this.mCountSizeSpacing);
        DefaultLogger.d("TextAppendConfig", "测量耗时： %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public final void countDialogCorner() {
        this.mAutoLineLayout.getTextRect(this.mTextRect);
        int i = this.mCornerDialogWidth;
        float intrinsicHeight = (this.mDialogDrawable.getIntrinsicHeight() * (i / this.mDialogDrawable.getIntrinsicWidth())) / 2.0f;
        float f = i / 2.0f;
        float f2 = -f;
        float f3 = -intrinsicHeight;
        this.mDialogRect.set(f2, f3, f, intrinsicHeight);
        float width = this.mTextRect.width() / 2.0f;
        float height = this.mTextRect.height() / 2.0f;
        this.mTextInDialogRect.set(this.mTextRect);
        int i2 = this.mDialogModel.cornerPosition;
        if (i2 == 0) {
            this.mDialogRect.offset(-width, -height);
            this.mTextInDialogRect.offset(f, intrinsicHeight);
        } else if (i2 == 1) {
            this.mDialogRect.offset(width, -height);
            this.mTextInDialogRect.offset(f2, intrinsicHeight);
        } else if (i2 == 2) {
            this.mDialogRect.offset(-width, height);
            this.mTextInDialogRect.offset(f, f3);
        } else if (i2 != 3) {
        } else {
            this.mDialogRect.offset(width, height);
            this.mTextInDialogRect.offset(f2, f3);
        }
    }

    public final void configTextPaint(Paint paint) {
        if (this.mGradientsColor != 0) {
            this.mAutoLineLayout.setCallbacks(this.mCallbacks);
        } else {
            this.mAutoLineLayout.setCallbacks(null);
            paint.setShader(null);
            paint.setColor(this.mColor);
        }
        TextStyle textStyle = this.mTextStyle;
        paint.setTypeface(textStyle == null ? Typeface.DEFAULT : textStyle.getTypeFace());
        if (!this.mIsSubstrate) {
            paint.setAlpha(getAlphaByConfig());
        }
        paint.setFakeBoldText(this.mIsBoldText);
        if (this.mIsShadow) {
            paint.setShadowLayer(1.0f, 1.0f, 1.0f, 1711276032);
        } else {
            paint.clearShadowLayer();
        }
        if (!isDialogEnable() || isCornerDialog()) {
            paint.setTextSize(this.mDefaultTextSize);
        }
    }

    public final int getAlphaByConfig() {
        return (int) (255.0f - (this.mTextTransparent * 255.0f));
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void draw(Canvas canvas) {
        TextDrawable textDrawable = this.mTextDrawable;
        if (textDrawable != null) {
            textDrawable.draw(canvas);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setImageInitDisplayRect(RectF rectF) {
        if (rectF == null || rectF.isEmpty()) {
            return;
        }
        if (this.mInitImageRectF.isEmpty()) {
            this.mInitImageRectF.set(rectF);
            this.mScaleMultipleOrigin = this.mInitImageRectF.width() / 1080.0f;
            updateDefaultLocation();
            return;
        }
        this.mCurrentImageRectF.set(rectF);
    }

    public final void updateDefaultLocation() {
        this.mDefaultLocationX = this.mInitImageRectF.centerX();
        this.mDefaultLocationY = this.mInitImageRectF.centerY();
        BaseDialogModel baseDialogModel = this.mDialogModel;
        if (baseDialogModel == null || baseDialogModel.isBubbleModel() || this.mDefaultLocationY <= 0.0f) {
            return;
        }
        float f = this.mInitImageRectF.bottom;
        float f2 = this.mScaleMultipleOrigin;
        this.mDefaultLocationY = (f - ((this.mDialogDrawable.getIntrinsicHeight() / 2.0f) * f2)) - (this.mDialogPaddingBottom * f2);
    }

    public final Matrix getImageRectMatrix(boolean z) {
        RectF rectF;
        this.mMatrix.reset();
        RectF rectF2 = this.mInitImageRectF;
        if (rectF2 == null || rectF2.isEmpty() || (rectF = this.mCurrentImageRectF) == null || rectF.isEmpty()) {
            return this.mMatrix;
        }
        if (z) {
            this.mMatrix.setRectToRect(this.mCurrentImageRectF, this.mInitImageRectF, Matrix.ScaleToFit.CENTER);
        } else {
            this.mMatrix.setRectToRect(this.mInitImageRectF, this.mCurrentImageRectF, Matrix.ScaleToFit.CENTER);
        }
        return this.mMatrix;
    }

    /* loaded from: classes2.dex */
    public class TextDrawable extends Drawable {
        public Rect rectTemp;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
        }

        public TextDrawable() {
            this.rectTemp = new Rect();
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            if (TextAppendConfig.this.isSubstrate() && TextAppendConfig.this.getDialogDrawable() == null && !TextAppendConfig.this.isWatermark() && !TextAppendConfig.this.isSignature()) {
                drawSubstrate(canvas);
            }
            initCanvas(canvas);
            if (!TextAppendConfig.this.isDialogEnable()) {
                drawText(canvas);
                return;
            }
            drawDialog(canvas);
            if (TextAppendConfig.this.mDialogModel == null || !TextAppendConfig.this.mDialogModel.isBubbleModel()) {
                return;
            }
            drawText(canvas);
        }

        public final void initCanvas(Canvas canvas) {
            if (TextAppendConfig.this.mDefaultLocationX < 0.0f) {
                TextAppendConfig.this.mDefaultLocationX = canvas.getWidth() / 2.0f;
            }
            if (TextAppendConfig.this.mDefaultLocationY < 0.0f) {
                TextAppendConfig.this.mDefaultLocationY = canvas.getHeight() / 2.0f;
            }
        }

        public final void drawDialog(Canvas canvas) {
            if (TextAppendConfig.this.mDialogDrawable == null) {
                return;
            }
            TextAppendConfig.this.mDialogRect.round(this.rectTemp);
            TextAppendConfig.this.mDialogDrawable.setBounds(this.rectTemp);
            canvas.save();
            canvasTranslate(canvas, true, true);
            if (TextAppendConfig.this.mIsMirror) {
                canvas.scale(-1.0f, 1.0f);
            }
            if (TextAppendConfig.this.isCornerDialog()) {
                TextAppendConfig.this.mDialogDrawable.setColorFilter(TextAppendConfig.this.mColor, PorterDuff.Mode.SRC_IN);
            }
            if (TextAppendConfig.this.mDialogModel != null && !TextAppendConfig.this.mDialogModel.isBubbleModel() && !TextAppendConfig.this.mDialogModel.hasBlackDialog()) {
                if (!TextAppendConfig.this.mReverseColor) {
                    TextAppendConfig.this.mDialogDrawable.setColorFilter(-1, PorterDuff.Mode.SRC_IN);
                } else {
                    TextAppendConfig.this.mDialogDrawable.setColorFilter(-16777216, PorterDuff.Mode.SRC_IN);
                }
            }
            TextAppendConfig.this.mDialogDrawable.draw(canvas);
            canvas.restore();
        }

        public final void drawText(Canvas canvas) {
            if (TextUtils.isEmpty(TextAppendConfig.this.mText)) {
                return;
            }
            canvas.save();
            canvasTranslate(canvas, true, true);
            if (TextAppendConfig.this.isDialogEnable()) {
                if (TextAppendConfig.this.mIsMirror) {
                    canvas.translate(-TextAppendConfig.this.mTextInDialogRect.centerX(), TextAppendConfig.this.mTextInDialogRect.centerY());
                } else {
                    canvas.translate(TextAppendConfig.this.mTextInDialogRect.centerX(), TextAppendConfig.this.mTextInDialogRect.centerY());
                }
            }
            TextAppendConfig.this.mAutoLineLayout.draw(canvas);
            canvas.restore();
        }

        public final void drawSubstrate(Canvas canvas) {
            TextAppendConfig textAppendConfig = TextAppendConfig.this;
            textAppendConfig.getOutLineRect(textAppendConfig.mTempRectF);
            canvas.save();
            canvas.rotate(TextAppendConfig.this.getRotateDegrees(), ((TextAppendConfig.this.mTempRectF.right - TextAppendConfig.this.mTempRectF.left) / 2.0f) + TextAppendConfig.this.mTempRectF.left, ((TextAppendConfig.this.mTempRectF.bottom - TextAppendConfig.this.mTempRectF.top) / 2.0f) + TextAppendConfig.this.mTempRectF.top);
            float dimensionPixelSize = TextAppendConfig.this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_text_dialog_substrate_padding_left) * TextAppendConfig.this.getScaleMultiple();
            float dimensionPixelSize2 = TextAppendConfig.this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_text_dialog_substrate_padding_top) * TextAppendConfig.this.getScaleMultiple();
            float dimensionPixelSize3 = TextAppendConfig.this.mContext.getResources().getDimensionPixelSize(R.dimen.text_edit_text_dialog_substrate_radius) * TextAppendConfig.this.getScaleMultiple();
            TextAppendConfig.this.mSubstratePaint.setAlpha(TextAppendConfig.this.getAlphaByConfig());
            if (TextAppendConfig.this.getSubstrateColors().length <= 1) {
                TextAppendConfig.this.mSubstratePaint.setColor(TextAppendConfig.this.getSubstrateColors()[0]);
                TextAppendConfig.this.mSubstratePaint.setAlpha(TextAppendConfig.this.getAlphaByConfig());
                TextAppendConfig.this.mSubstratePaint.setShader(null);
                canvas.drawRoundRect(new RectF(TextAppendConfig.this.mTempRectF.left + dimensionPixelSize, TextAppendConfig.this.mTempRectF.top + dimensionPixelSize2, TextAppendConfig.this.mTempRectF.right - dimensionPixelSize, TextAppendConfig.this.mTempRectF.bottom - dimensionPixelSize2), dimensionPixelSize3, dimensionPixelSize3, TextAppendConfig.this.mSubstratePaint);
                canvas.restore();
                return;
            }
            TextAppendConfig.this.mSubstratePaint.setShader(new LinearGradient(TextAppendConfig.this.mTempRectF.left, TextAppendConfig.this.mTempRectF.bottom, TextAppendConfig.this.mTempRectF.right, TextAppendConfig.this.mTempRectF.top, TextAppendConfig.this.getSubstrateColors()[0], TextAppendConfig.this.getSubstrateColors()[1], Shader.TileMode.MIRROR));
            TextAppendConfig.this.mSubstrateActualRectF.left = TextAppendConfig.this.mTempRectF.left + dimensionPixelSize;
            TextAppendConfig.this.mSubstrateActualRectF.top = TextAppendConfig.this.mTempRectF.top + dimensionPixelSize2;
            TextAppendConfig.this.mSubstrateActualRectF.right = TextAppendConfig.this.mTempRectF.right - dimensionPixelSize;
            TextAppendConfig.this.mSubstrateActualRectF.bottom = TextAppendConfig.this.mTempRectF.bottom - dimensionPixelSize2;
            canvas.drawRoundRect(TextAppendConfig.this.mSubstrateActualRectF, dimensionPixelSize3, dimensionPixelSize3, TextAppendConfig.this.mSubstratePaint);
            canvas.restore();
        }

        public final void canvasTranslate(Canvas canvas, boolean z, boolean z2) {
            canvas.concat(TextAppendConfig.this.getImageRectMatrix(false));
            canvas.translate(TextAppendConfig.this.mDefaultLocationX + TextAppendConfig.this.mUserLocationX, TextAppendConfig.this.mDefaultLocationY + TextAppendConfig.this.mUserLocationY);
            if (z) {
                canvas.scale(TextAppendConfig.this.getScaleMultiple(), TextAppendConfig.this.getScaleMultiple(), 0.0f, 0.0f);
            }
            if (z2) {
                canvas.rotate(TextAppendConfig.this.getRotateDegrees(), 0.0f, 0.0f);
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void reverseColor(int i) {
        this.mReverseColor = !this.mReverseColor;
        if (i == -16777216) {
            if (!this.mDialogModel.hasBlackDialog()) {
                return;
            }
            this.mDialogDrawable = this.mDialogModel.readBlackDialogDrawable(this.mContext.getResources());
        } else if (!this.mDialogModel.hasBlackDialog()) {
        } else {
            this.mDialogDrawable = this.mDialogModel.readDialogDrawable(this.mContext.getResources());
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isReverseColor() {
        return this.mReverseColor;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void getDialogStatusData(DialogStatusData dialogStatusData) {
        dialogStatusData.itemPositionX = getUserLocationX();
        dialogStatusData.itemPositionY = getUserLocationY();
        dialogStatusData.itemScale = getUserScaleMultiple();
        dialogStatusData.itemDegree = getRotateDegrees();
        dialogStatusData.color = getColor();
        dialogStatusData.transparentProgress = getTextTransparent();
        dialogStatusData.textStyle = getTextStyle();
        dialogStatusData.textBold = isBoldText();
        dialogStatusData.textShadow = isShadow();
        dialogStatusData.textAlignment = getTextAlignment();
        dialogStatusData.text = getText();
        dialogStatusData.isReverseColor = this.mReverseColor;
        dialogStatusData.mSubstrateColors = getSubstrateColors();
        dialogStatusData.isStroke = isStroke();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDialogWithStatusData(DialogStatusData dialogStatusData) {
        setUserLocationX(dialogStatusData.itemPositionX);
        setUserLocationY(dialogStatusData.itemPositionY);
        setUserScaleMultiple(dialogStatusData.itemScale);
        setRotateDegrees(dialogStatusData.itemDegree);
        setColor(dialogStatusData.color);
        setTextTransparent(dialogStatusData.transparentProgress);
        setTextStyle(dialogStatusData.textStyle);
        setBoldText(dialogStatusData.textBold);
        setShadow(dialogStatusData.textShadow);
        setSubstrate(dialogStatusData.isSubstrate);
        setIsStroke(dialogStatusData.isStroke);
        setTextAlignment(dialogStatusData.textAlignment);
        String str = dialogStatusData.text;
        if (str != null) {
            setText(str);
        }
        this.mReverseColor = dialogStatusData.isReverseColor;
        setSubstrateColors(dialogStatusData.mSubstrateColors);
        setGradientsColor(dialogStatusData.gradientsColor);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        ParcelableGenericUtils.writeObject(parcel, i, this.mDialogModel);
        parcel.writeInt(this.mColor);
        ParcelableGenericUtils.writeObject(parcel, i, this.mTextStyle);
        parcel.writeString(this.mText);
        parcel.writeByte(this.mIsActivation ? (byte) 1 : (byte) 0);
        parcel.writeFloat(this.mTextTransparent);
        parcel.writeByte(this.mIsBoldText ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsShadow ? (byte) 1 : (byte) 0);
        parcel.writeFloat(this.mDefaultLocationX);
        parcel.writeFloat(this.mDefaultLocationY);
        parcel.writeFloat(this.mUserLocationX);
        parcel.writeFloat(this.mUserLocationY);
        parcel.writeFloat(this.mUserScaleMultiple);
        parcel.writeFloat(this.mScaleMultipleOrigin);
        parcel.writeFloat(this.mRotateDegrees);
        parcel.writeByte(this.mIsMirror ? (byte) 1 : (byte) 0);
        parcel.writeByte(this.mIsCorrection ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.mAutoLineLayout.getTextAlignment().ordinal());
        parcel.writeFloat(this.mMaxWidth);
        parcel.writeInt(this.mGradientsColor);
        parcel.writeByte(this.mIsSubstrate ? (byte) 1 : (byte) 0);
        parcel.writeIntArray(this.mSubstrateColors);
        parcel.writeByte(this.mAutoLineLayout.isStroke() ? (byte) 1 : (byte) 0);
    }

    public TextAppendConfig(Parcel parcel) {
        this(GalleryApp.sGetAndroidContext());
        this.mDialogModel = (BaseDialogModel) ParcelableGenericUtils.readObject(parcel);
        this.mColor = parcel.readInt();
        this.mTextStyle = (TextStyle) ParcelableGenericUtils.readObject(parcel);
        this.mText = parcel.readString();
        boolean z = true;
        this.mIsActivation = parcel.readByte() != 0;
        this.mTextTransparent = parcel.readFloat();
        this.mIsBoldText = parcel.readByte() != 0;
        this.mIsShadow = parcel.readByte() != 0;
        this.mDefaultLocationX = parcel.readFloat();
        this.mDefaultLocationY = parcel.readFloat();
        this.mUserLocationX = parcel.readFloat();
        this.mUserLocationY = parcel.readFloat();
        this.mUserScaleMultiple = parcel.readFloat();
        this.mScaleMultipleOrigin = parcel.readFloat();
        this.mRotateDegrees = parcel.readFloat();
        this.mIsMirror = parcel.readByte() != 0;
        this.mIsCorrection = parcel.readByte() != 0;
        this.mAutoLineLayout.setTextAlignment(AutoLineLayout.TextAlignment.values()[parcel.readInt()]);
        this.mMaxWidth = parcel.readFloat();
        this.mGradientsColor = parcel.readInt();
        this.mIsSubstrate = parcel.readByte() != 0;
        this.mSubstrateColors = parcel.createIntArray();
        this.mAutoLineLayout.setIsStroke(parcel.readByte() == 0 ? false : z);
        initForParcelable();
    }

    public final void initForParcelable() {
        BaseDialogModel baseDialogModel = this.mDialogModel;
        if (baseDialogModel != null) {
            this.mDialogDrawable = baseDialogModel.readDialogDrawable(this.mContext.getResources());
        }
        countLocation(true, this.mMaxWidth);
    }

    public Drawable getDialogDrawable() {
        return this.mDialogDrawable;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isSubstrate() {
        return this.mIsSubstrate;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setSubstrate(boolean z) {
        this.mIsSubstrate = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public int[] getSubstrateColors() {
        return this.mSubstrateColors;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setSubstrateColors(int... iArr) {
        this.mSubstrateColors = iArr;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setIsStroke(boolean z) {
        this.mAutoLineLayout.setIsStroke(z);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isStroke() {
        return this.mAutoLineLayout.isStroke();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public String getName() {
        return this.mName;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setName(String str) {
        this.mName = str;
    }
}
