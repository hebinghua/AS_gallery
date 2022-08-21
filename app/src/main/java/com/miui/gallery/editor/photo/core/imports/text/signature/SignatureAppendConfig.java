package com.miui.gallery.editor.photo.core.imports.text.signature;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
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
import com.miui.gallery.signature.SignatureColor;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexClip;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes2.dex */
public class SignatureAppendConfig implements Parcelable, ITextDialogConfig {
    public static final Parcelable.Creator<SignatureAppendConfig> CREATOR = new Parcelable.Creator<SignatureAppendConfig>() { // from class: com.miui.gallery.editor.photo.core.imports.text.signature.SignatureAppendConfig.1
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SignatureAppendConfig mo898createFromParcel(Parcel parcel) {
            return new SignatureAppendConfig(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SignatureAppendConfig[] mo899newArray(int i) {
            return new SignatureAppendConfig[i];
        }
    };
    public AutoLineLayout mAutoLineLayout;
    public int mColor;
    public Context mContext;
    public final int mCornerDialogWidth;
    public final int mCorrectionDegrees;
    public final float mCountSizeSpacing;
    public final SimpleDateFormat mCurrentDateFormat;
    public int mDateColor;
    public Paint mDatePaint;
    public final float mDefaultDateAreaHeight;
    public final float mDefaultDateAreaMarginHeight;
    public final float mDefaultDateSize;
    public final float mDefaultDialogHeight;
    public final float mDefaultDialogWidth;
    public float mDefaultLocationX;
    public float mDefaultLocationY;
    public final float mDefaultSignatureAreaHeight;
    public final float mDefaultSignatureAreaWidth;
    public final int mDefaultStrokeWidth;
    public final float mDefaultTextSize;
    public Drawable mDialogDrawable;
    public BaseDialogModel mDialogModel;
    public final int mDialogOutLineOffsetX;
    public final int mDialogOutLineOffsetY;
    public RectF mDialogRect;
    public Disposable mDisposable;
    public boolean mIsActivation;
    public boolean mIsBoldText;
    public boolean mIsCorrection;
    public boolean mIsShadow;
    public boolean mIsShowTimeStamp;
    public boolean mIsSubstrate;
    public Matrix mMatrix;
    public final float mMaxTextSize;
    public float mMaxWidth;
    public final int mMinTextSize;
    public String mName;
    public final int mOutLineOffsetX;
    public final int mOutLineOffsetY;
    public RectF mOutLineRect;
    public float mRotateDegrees;
    public float mScaleMultipleOrigin;
    public String mSignaturePath;
    public RectF mTemRect;
    public String mText;
    public SignatureDrawable mTextDrawable;
    public RectF mTextInDialogRect;
    public RectF mTextRect;
    public TextStyle mTextStyle;
    public float mTextTransparent;
    public float mUserLocationX;
    public float mUserLocationY;
    public float mUserScaleMultiple;

    public static /* synthetic */ void $r8$lambda$7ArkRWaKbJpfZZcsK_USuJvqJbM(SignatureAppendConfig signatureAppendConfig, Drawable drawable) {
        signatureAppendConfig.lambda$loadDrawable$1(drawable);
    }

    /* renamed from: $r8$lambda$Lz-tgmmMsQr67lDX8f-rF1wUQ1g */
    public static /* synthetic */ void m897$r8$lambda$LztgmmMsQr67lDX8frF1wUQ1g(SignatureAppendConfig signatureAppendConfig, ObservableEmitter observableEmitter) {
        signatureAppendConfig.lambda$loadDrawable$0(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$N3jMSwYZV29f7XmGfowK5obhJ3o(SignatureAppendConfig signatureAppendConfig, Throwable th) {
        signatureAppendConfig.lambda$loadDrawable$2(th);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isReverseColor() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isSignature() {
        return true;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isSubstrate() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isWatermark() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void reverseColor(int i) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDialogModel(BaseDialogModel baseDialogModel, Resources resources) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDrawOutline(boolean z) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void toggleMirror() {
    }

    public SignatureAppendConfig(Context context) {
        this.mIsShowTimeStamp = false;
        this.mName = DialogManager.LocalDialog.SIGNATURE.name();
        this.mContext = context;
        Resources resources = context.getResources();
        this.mOutLineOffsetX = resources.getDimensionPixelSize(R.dimen.text_append_out_line_offset_x);
        this.mOutLineOffsetY = resources.getDimensionPixelSize(R.dimen.text_append_out_line_offset_y);
        this.mDialogOutLineOffsetX = 0;
        this.mDialogOutLineOffsetY = resources.getDimensionPixelSize(R.dimen.text_append_dialog_out_line_offset_y);
        this.mCornerDialogWidth = resources.getDimensionPixelSize(R.dimen.text_append_corner_dialog_default_width);
        this.mMinTextSize = resources.getDimensionPixelSize(R.dimen.text_append_min_text_size);
        this.mDefaultStrokeWidth = resources.getDimensionPixelSize(R.dimen.text_append_default_stroke_width);
        this.mCorrectionDegrees = 5;
        float dimension = resources.getDimension(R.dimen.signature_date_text_default_text_size);
        this.mDefaultTextSize = dimension;
        this.mMaxTextSize = resources.getDimension(R.dimen.text_append_max_text_size);
        this.mCountSizeSpacing = resources.getDisplayMetrics().density;
        this.mDefaultDialogWidth = resources.getDimension(R.dimen.signature_edit_dialog_width);
        this.mDefaultDialogHeight = resources.getDimension(R.dimen.signature_edit_dialog_height);
        this.mDefaultSignatureAreaWidth = resources.getDimension(R.dimen.signature_edit_signature_area_width);
        this.mDefaultSignatureAreaHeight = resources.getDimension(R.dimen.signature_edit_signature_area_height);
        this.mDefaultDateAreaHeight = resources.getDimension(R.dimen.signature_edit_date_string_height);
        this.mDefaultDateAreaMarginHeight = resources.getDimension(R.dimen.signature_edit_date_margin_height);
        this.mDefaultDateSize = resources.getDimension(R.dimen.signature_edit_date_string_text_size);
        this.mCurrentDateFormat = new SimpleDateFormat("yyyy.M.d");
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
        this.mIsCorrection = false;
        AutoLineLayout autoLineLayout = new AutoLineLayout();
        this.mAutoLineLayout = autoLineLayout;
        autoLineLayout.getPaint().setTextSize(dimension);
        this.mAutoLineLayout.setLineHeightOffset(resources.getDimension(R.dimen.text_append_line_height_offset));
        this.mOutLineRect = new RectF();
        this.mTextRect = new RectF();
        this.mTextInDialogRect = new RectF();
        this.mDialogRect = new RectF();
        this.mTemRect = new RectF();
        this.mMatrix = new Matrix();
        this.mTextDrawable = new SignatureDrawable();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void countLocation(boolean z, float f) {
        this.mMaxWidth = f;
        if (!isDialogEnable()) {
            countOutLine();
        } else if (isCornerDialog()) {
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

    public void setSignaturePath(String str) {
        this.mSignaturePath = str;
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
            this.mOutLineRect.inset(-this.mDialogOutLineOffsetX, -this.mDialogOutLineOffsetY);
        } else {
            this.mOutLineRect.inset(-this.mOutLineOffsetX, -this.mOutLineOffsetY);
        }
        this.mOutLineRect.inset(-(((this.mOutLineRect.width() * getScaleMultiple()) - this.mOutLineRect.width()) / 2.0f), -(((this.mOutLineRect.height() * getScaleMultiple()) - this.mOutLineRect.height()) / 2.0f));
    }

    public final void countDialog() {
        RectF rectF = this.mDialogRect;
        float f = this.mDefaultDialogWidth;
        float f2 = this.mDefaultDialogHeight;
        rectF.set((-f) / 2.0f, (-f2) / 2.0f, f / 2.0f, f2 / 2.0f);
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
        DefaultLogger.d("SignatureAppendConfig", "测量耗时： %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
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

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void draw(Canvas canvas) {
        SignatureDrawable signatureDrawable = this.mTextDrawable;
        if (signatureDrawable != null) {
            signatureDrawable.draw(canvas);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setImageInitDisplayRect(RectF rectF) {
        if (rectF == null) {
            return;
        }
        this.mDefaultLocationX = rectF.centerX();
        this.mDefaultLocationY = rectF.centerY();
        float f = this.mUserLocationX;
        float f2 = this.mScaleMultipleOrigin;
        float width = rectF.width() / 1080.0f;
        this.mScaleMultipleOrigin = width;
        this.mUserLocationX = (f / f2) * width;
        this.mUserLocationY = (this.mUserLocationY / f2) * width;
    }

    public final String getDateText() {
        return this.mCurrentDateFormat.format(new Date());
    }

    /* loaded from: classes2.dex */
    public class SignatureDrawable extends Drawable {
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

        public SignatureDrawable() {
            SignatureAppendConfig.this = r1;
            this.rectTemp = new Rect();
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            initCanvas(canvas);
            drawDialog(canvas);
            if (SignatureAppendConfig.this.mIsShowTimeStamp) {
                drawText(canvas);
            }
        }

        public final void initCanvas(Canvas canvas) {
            if (SignatureAppendConfig.this.mDefaultLocationX < 0.0f) {
                SignatureAppendConfig.this.mDefaultLocationX = canvas.getWidth() / 2.0f;
            }
            if (SignatureAppendConfig.this.mDefaultLocationY < 0.0f) {
                SignatureAppendConfig.this.mDefaultLocationY = canvas.getHeight() / 2.0f;
            }
        }

        public final void drawDialog(Canvas canvas) {
            if (SignatureAppendConfig.this.mDialogDrawable == null) {
                return;
            }
            SignatureAppendConfig.this.mDialogRect.round(this.rectTemp);
            float intrinsicWidth = SignatureAppendConfig.this.mDialogDrawable.getIntrinsicWidth();
            float intrinsicHeight = SignatureAppendConfig.this.mDialogDrawable.getIntrinsicHeight();
            float f = SignatureAppendConfig.this.mDefaultSignatureAreaWidth;
            float f2 = SignatureAppendConfig.this.mDefaultSignatureAreaHeight;
            if (intrinsicWidth > intrinsicHeight) {
                f2 = (intrinsicHeight * SignatureAppendConfig.this.mDefaultSignatureAreaWidth) / intrinsicWidth;
            } else if (intrinsicWidth < intrinsicHeight) {
                f = (intrinsicWidth * SignatureAppendConfig.this.mDefaultSignatureAreaHeight) / intrinsicHeight;
            }
            int i = (int) (f / 2.0f);
            int i2 = (int) (f2 / 2.0f);
            int i3 = (int) (SignatureAppendConfig.this.mDefaultDateAreaHeight / 2.0f);
            if (SignatureAppendConfig.this.mIsShowTimeStamp) {
                SignatureAppendConfig.this.mDialogDrawable.setBounds(this.rectTemp.centerX() - i, (this.rectTemp.centerY() - i2) - i3, this.rectTemp.centerX() + i, (this.rectTemp.centerY() + i2) - i3);
            } else {
                SignatureAppendConfig.this.mDialogDrawable.setBounds(this.rectTemp.centerX() - i, this.rectTemp.centerY() - i2, this.rectTemp.centerX() + i, this.rectTemp.centerY() + i2);
            }
            canvas.save();
            canvasTranslate(canvas);
            if (SignatureAppendConfig.this.isCornerDialog()) {
                SignatureAppendConfig.this.mDialogDrawable.setColorFilter(SignatureAppendConfig.this.mColor, PorterDuff.Mode.SRC_IN);
            }
            SignatureAppendConfig.this.mDialogDrawable.draw(canvas);
            canvas.restore();
        }

        public final void drawText(Canvas canvas) {
            if (SignatureAppendConfig.this.mDatePaint == null) {
                SignatureAppendConfig.this.mDatePaint = new Paint(1);
                SignatureAppendConfig.this.mDatePaint.setColor(-1);
                SignatureAppendConfig.this.mDatePaint.setTextSize(SignatureAppendConfig.this.mDefaultDateSize);
            }
            if (!TextUtils.isEmpty(SignatureAppendConfig.this.mSignaturePath)) {
                SignatureColor colorWithColorTag = SignatureColor.getColorWithColorTag(SignatureColor.getSignatureTag(SignatureAppendConfig.this.mSignaturePath));
                if (colorWithColorTag == SignatureColor.SIGNATURE_COLOR_DEFAULT) {
                    SignatureAppendConfig signatureAppendConfig = SignatureAppendConfig.this;
                    signatureAppendConfig.mDateColor = signatureAppendConfig.mContext.getResources().getColor(R.color.signature_color_default);
                } else {
                    SignatureAppendConfig signatureAppendConfig2 = SignatureAppendConfig.this;
                    signatureAppendConfig2.mDateColor = signatureAppendConfig2.mContext.getResources().getColor(colorWithColorTag.mColorId);
                }
                SignatureAppendConfig.this.mDatePaint.setColor(SignatureAppendConfig.this.mDateColor);
            }
            canvas.save();
            canvasTranslate(canvas);
            String dateText = SignatureAppendConfig.this.getDateText();
            canvas.drawText(dateText, (-getTextWidth(SignatureAppendConfig.this.mDatePaint, dateText)) / 2.0f, ((SignatureAppendConfig.this.mDefaultSignatureAreaHeight / 2.0f) + SignatureAppendConfig.this.mDefaultDateAreaMarginHeight) - ((SignatureAppendConfig.this.mDatePaint.descent() + SignatureAppendConfig.this.mDatePaint.ascent()) / 2.0f), SignatureAppendConfig.this.mDatePaint);
            canvas.restore();
        }

        public final int getTextWidth(Paint paint, String str) {
            float[] fArr;
            if (str == null || str.length() <= 0) {
                return 0;
            }
            int length = str.length();
            paint.getTextWidths(str, new float[length]);
            int i = 0;
            for (int i2 = 0; i2 < length; i2++) {
                i += (int) Math.ceil(fArr[i2]);
            }
            return i;
        }

        public final void canvasTranslate(Canvas canvas) {
            canvas.translate(SignatureAppendConfig.this.mDefaultLocationX + SignatureAppendConfig.this.mUserLocationX, SignatureAppendConfig.this.mDefaultLocationY + SignatureAppendConfig.this.mUserLocationY);
            canvas.scale(SignatureAppendConfig.this.getScaleMultiple(), SignatureAppendConfig.this.getScaleMultiple(), 0.0f, 0.0f);
            canvas.rotate(SignatureAppendConfig.this.getRotateDegrees(), 0.0f, 0.0f);
        }
    }

    public boolean isShowTimeStamp() {
        return this.mIsShowTimeStamp;
    }

    public void setShowTimeStamp(boolean z) {
        this.mIsShowTimeStamp = z;
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
        dialogStatusData.mSignaturePath = this.mSignaturePath;
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
        setTextAlignment(dialogStatusData.textAlignment);
        setSignaturePath(dialogStatusData.mSignaturePath);
        String str = dialogStatusData.text;
        if (str != null) {
            setText(str);
        }
    }

    public final void loadDrawable() {
        this.mDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.editor.photo.core.imports.text.signature.SignatureAppendConfig$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                SignatureAppendConfig.m897$r8$lambda$LztgmmMsQr67lDX8frF1wUQ1g(SignatureAppendConfig.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.editor.photo.core.imports.text.signature.SignatureAppendConfig$$ExternalSyntheticLambda1
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                SignatureAppendConfig.$r8$lambda$7ArkRWaKbJpfZZcsK_USuJvqJbM(SignatureAppendConfig.this, (Drawable) obj);
            }
        }, new Consumer() { // from class: com.miui.gallery.editor.photo.core.imports.text.signature.SignatureAppendConfig$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                SignatureAppendConfig.$r8$lambda$N3jMSwYZV29f7XmGfowK5obhJ3o(SignatureAppendConfig.this, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$loadDrawable$0(ObservableEmitter observableEmitter) throws Exception {
        if (TextUtils.isEmpty(this.mSignaturePath)) {
            observableEmitter.onNext(null);
            return;
        }
        observableEmitter.onNext(new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeFile(this.mSignaturePath)));
    }

    public /* synthetic */ void lambda$loadDrawable$1(Drawable drawable) throws Exception {
        if (drawable != null) {
            this.mDialogDrawable = drawable;
        } else {
            this.mDialogModel = null;
        }
        countLocation(true, this.mMaxWidth);
    }

    public /* synthetic */ void lambda$loadDrawable$2(Throwable th) throws Exception {
        this.mDialogModel = null;
        countLocation(true, this.mMaxWidth);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setSignatureDrawable(BaseDialogModel baseDialogModel, Drawable drawable) {
        this.mDialogModel = baseDialogModel;
        this.mDialogDrawable = drawable;
        if (drawable == null) {
            this.mDialogModel = null;
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void onDetachedFromWindow() {
        Disposable disposable = this.mDisposable;
        if (disposable != null) {
            if (!disposable.isDisposed()) {
                this.mDisposable.dispose();
            }
            this.mDisposable = null;
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setSubstrate(boolean z) {
        this.mIsSubstrate = z;
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
        this.mUserLocationX += f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void appendUserLocationY(float f) {
        this.mUserLocationY += f;
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
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setUserScaleMultiple(float f) {
        this.mUserScaleMultiple = f;
        if (f < 0.4f) {
            this.mUserScaleMultiple = 0.4f;
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
        return this.mDialogModel != null;
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
        parcel.writeByte(this.mIsCorrection ? (byte) 1 : (byte) 0);
        parcel.writeInt(this.mAutoLineLayout.getTextAlignment().ordinal());
        parcel.writeFloat(this.mMaxWidth);
    }

    public SignatureAppendConfig(Parcel parcel) {
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
        this.mIsCorrection = parcel.readByte() == 0 ? false : z;
        this.mAutoLineLayout.setTextAlignment(AutoLineLayout.TextAlignment.values()[parcel.readInt()]);
        this.mMaxWidth = parcel.readFloat();
        initForParcelable();
    }

    public final void initForParcelable() {
        if (this.mDialogModel != null) {
            loadDrawable();
        }
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
