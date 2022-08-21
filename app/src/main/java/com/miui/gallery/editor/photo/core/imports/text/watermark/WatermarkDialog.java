package com.miui.gallery.editor.photo.core.imports.text.watermark;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.dialog.DialogManager;
import com.miui.gallery.editor.photo.core.imports.text.model.DialogStatusData;
import com.miui.gallery.editor.photo.core.imports.text.model.TextStatusData;
import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo;
import com.miui.gallery.editor.photo.utils.parcelable.ParcelableGenericUtils;
import com.miui.gallery.glide.GlideApp;
import com.miui.gallery.util.Bitmaps;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class WatermarkDialog implements ITextDialogConfig, Parcelable {
    public static final Parcelable.Creator<WatermarkDialog> CREATOR = new Parcelable.Creator<WatermarkDialog>() { // from class: com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog.2
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public WatermarkDialog mo908createFromParcel(Parcel parcel) {
            return new WatermarkDialog(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public WatermarkDialog[] mo909newArray(int i) {
            return new WatermarkDialog[i];
        }
    };
    public boolean mActivation;
    public Bitmap mBgBitmap;
    public Paint mBgPaint;
    public RectF mBgPostRect;
    public BitmapLoadingListener mBitmapLoadingListener;
    public int mCurrentPieceIndex;
    public float mDefaultLocationX;
    public float mDefaultLocationY;
    public DialogStatusData mDialogStatusData;
    public boolean mIsCorrection;
    public boolean mIsFirstCount;
    public boolean mIsFromParcel;
    public Matrix mMatrix;
    public String mName;
    public RectF mOutLineRect;
    public int mPaddingTop;
    public Resources mResource;
    public boolean mReverseColor;
    public float mRotateDegrees;
    public float mScaleMultipleOrigin;
    public RectF mTemRect;
    public float mUserLocationX;
    public float mUserLocationY;
    public float mUserScaleMultiple;
    public WatermarkInfo mWatermarkInfo;
    public List<WatermarkTextPiece> mWatermarkTextPieces;
    public RequestListener<Bitmap> mbgPostLoadingListener;

    /* loaded from: classes2.dex */
    public interface BitmapLoadingListener {
        void onCompleted();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public BaseDialogModel getDialogModel() {
        return null;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isDialogEnable() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isSignature() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isSubstrate() {
        return false;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isWatermark() {
        return true;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void onDetachedFromWindow() {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDialogModel(BaseDialogModel baseDialogModel, Resources resources) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setSignatureDrawable(BaseDialogModel baseDialogModel, Drawable drawable) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setSubstrate(boolean z) {
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void toggleMirror() {
    }

    public WatermarkDialog(Resources resources, WatermarkInfo watermarkInfo) {
        this.mName = DialogManager.LocalDialog.CIRCULAR.name();
        this.mIsFirstCount = true;
        this.mDefaultLocationX = -1.0f;
        this.mDefaultLocationY = -1.0f;
        this.mbgPostLoadingListener = new AnonymousClass1();
        this.mResource = resources;
        this.mWatermarkInfo = watermarkInfo;
    }

    public WatermarkDialog() {
        this.mName = DialogManager.LocalDialog.CIRCULAR.name();
        this.mIsFirstCount = true;
        this.mDefaultLocationX = -1.0f;
        this.mDefaultLocationY = -1.0f;
        this.mbgPostLoadingListener = new AnonymousClass1();
    }

    public void init() {
        Resources resources = this.mResource;
        this.mWatermarkTextPieces = new ArrayList();
        float dpToPixel = ScreenUtils.dpToPixel(this.mWatermarkInfo.width);
        float dpToPixel2 = ScreenUtils.dpToPixel(this.mWatermarkInfo.height);
        for (WatermarkInfo.TextPieceInfo textPieceInfo : this.mWatermarkInfo.textPieceList) {
            this.mWatermarkTextPieces.add(new WatermarkTextPiece(resources, textPieceInfo, dpToPixel, dpToPixel2, this.mScaleMultipleOrigin));
        }
        if (!TextUtils.isEmpty(this.mWatermarkInfo.bgPost)) {
            if (this.mIsFromParcel) {
                this.mBgBitmap = Bitmaps.decodeAsset(StaticContext.sGetAndroidContext(), Scheme.ASSETS.crop(this.mWatermarkInfo.bgPost), null);
                this.mBgPaint = new Paint(1);
            } else {
                GlideApp.with(StaticContext.sGetAndroidContext()).mo985asBitmap().mo963load(this.mWatermarkInfo.bgPost).skipCache().mo959listener(this.mbgPostLoadingListener).submit();
            }
        }
        this.mUserLocationX = 0.0f;
        this.mUserLocationY = 0.0f;
        this.mUserScaleMultiple = 1.0f;
        this.mRotateDegrees = 0.0f;
        this.mIsCorrection = false;
        float f = (-dpToPixel) / 2.0f;
        float f2 = (-dpToPixel2) / 2.0f;
        this.mOutLineRect = new RectF(f, f2, dpToPixel / 2.0f, dpToPixel2 / 2.0f);
        this.mTemRect = new RectF();
        this.mMatrix = new Matrix();
        if (this.mWatermarkInfo.bgPostRect != null) {
            RectF rectF = new RectF(ScreenUtils.dpToPixel(this.mWatermarkInfo.bgPostRect[0]), ScreenUtils.dpToPixel(this.mWatermarkInfo.bgPostRect[1]), ScreenUtils.dpToPixel(this.mWatermarkInfo.bgPostRect[2]), ScreenUtils.dpToPixel(this.mWatermarkInfo.bgPostRect[3]));
            this.mBgPostRect = rectF;
            rectF.offset(f, f2);
        }
        if (this.mIsFromParcel) {
            setDialogWithStatusData(this.mDialogStatusData);
            this.mIsFromParcel = false;
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements RequestListener<Bitmap> {
        public static /* synthetic */ void $r8$lambda$C5FWUvdetH8g3ujbyAY8Wc3RWrM(AnonymousClass1 anonymousClass1, Bitmap bitmap) {
            anonymousClass1.lambda$onResourceReady$0(bitmap);
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            return false;
        }

        public AnonymousClass1() {
            WatermarkDialog.this = r1;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(final Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkDialog$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    WatermarkDialog.AnonymousClass1.$r8$lambda$C5FWUvdetH8g3ujbyAY8Wc3RWrM(WatermarkDialog.AnonymousClass1.this, bitmap);
                }
            });
            return true;
        }

        public /* synthetic */ void lambda$onResourceReady$0(Bitmap bitmap) {
            WatermarkDialog.this.mBgBitmap = bitmap;
            WatermarkDialog.this.mBgPaint = new Paint(1);
            WatermarkDialog.this.mBgPaint.setAntiAlias(true);
            if (WatermarkDialog.this.mBitmapLoadingListener != null) {
                WatermarkDialog.this.mBitmapLoadingListener.onCompleted();
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void draw(Canvas canvas) {
        canvas.save();
        canvasTranslate(canvas, true, true);
        if (this.mBgBitmap != null && this.mWatermarkInfo.bgPostRect != null) {
            canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
            canvas.drawBitmap(this.mBgBitmap, (Rect) null, this.mBgPostRect, this.mBgPaint);
        }
        for (WatermarkTextPiece watermarkTextPiece : this.mWatermarkTextPieces) {
            watermarkTextPiece.draw(canvas);
        }
        canvas.restore();
    }

    public final void offsetRect(RectF rectF) {
        rectF.offset(this.mDefaultLocationX + this.mUserLocationX, this.mDefaultLocationY + this.mUserLocationY);
    }

    public final void canvasTranslate(Canvas canvas, boolean z, boolean z2) {
        canvas.translate(this.mDefaultLocationX + this.mUserLocationX, this.mDefaultLocationY + this.mUserLocationY);
        if (z) {
            canvas.scale(getScaleMultiple(), getScaleMultiple(), 0.0f, 0.0f);
        }
        if (z2) {
            canvas.rotate(getRotateDegrees(), 0.0f, 0.0f);
        }
    }

    public final float getScaleMultiple() {
        return this.mUserScaleMultiple * this.mScaleMultipleOrigin;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void getOutLineRect(RectF rectF) {
        getTransRect(this.mOutLineRect, rectF);
    }

    public final void getTransRect(RectF rectF, RectF rectF2) {
        rectF2.set(rectF);
        rectF2.inset(-(((rectF.width() * getScaleMultiple()) - rectF.width()) / 2.0f), -(((rectF.height() * getScaleMultiple()) - rectF.height()) / 2.0f));
        offsetRect(rectF2);
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
        if (f3 > 0.0f && f3 < 5.0f) {
            this.mIsCorrection = true;
            f3 = 0.0f;
        }
        if (f3 > 355.0f) {
            this.mIsCorrection = true;
        } else {
            f2 = f3;
        }
        if (f2 != 90.0f && f2 > 85.0f && f2 < 95.0f) {
            this.mIsCorrection = true;
            f2 = 90.0f;
        }
        if (f2 != 180.0f && f2 > 175.0f && f2 < 185.0f) {
            this.mIsCorrection = true;
            f2 = 180.0f;
        }
        if (f2 == 270.0f || f2 <= 265.0f || f2 >= 275.0f) {
            return f2;
        }
        this.mIsCorrection = true;
        return 270.0f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean contains(float f, float f2) {
        getOutLineRect(this.mTemRect);
        this.mMatrix.reset();
        this.mMatrix.postRotate(-getRotateDegrees(), this.mTemRect.centerX(), this.mTemRect.centerY());
        float[] fArr = {f, f2};
        this.mMatrix.mapPoints(fArr);
        boolean contains = this.mTemRect.contains(fArr[0], fArr[1]);
        this.mCurrentPieceIndex = 0;
        if (this.mWatermarkTextPieces.size() > 1) {
            this.mCurrentPieceIndex = -1;
            float f3 = Float.MAX_VALUE;
            int i = 0;
            while (true) {
                if (i >= this.mWatermarkTextPieces.size()) {
                    break;
                }
                getTransRect(this.mWatermarkTextPieces.get(i).getOutlineRect(), this.mTemRect);
                if (this.mTemRect.contains(fArr[0], fArr[1])) {
                    this.mCurrentPieceIndex = i;
                    break;
                }
                float pow = (float) (Math.pow(fArr[0] - this.mTemRect.centerX(), 2.0d) + Math.pow(fArr[1] - this.mTemRect.centerY(), 2.0d));
                if (pow < f3) {
                    this.mCurrentPieceIndex = i;
                    f3 = pow;
                }
                i++;
            }
        }
        return contains;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getUserScaleMultiple() {
        return this.mUserScaleMultiple;
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

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setRotateDegrees(float f) {
        this.mRotateDegrees = f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void refreshRotateDegree() {
        this.mRotateDegrees = getRotateDegrees();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void appendUserLocationX(float f) {
        this.mUserLocationX += f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void appendUserLocationY(float f) {
        this.mUserLocationY += f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void countLocation(boolean z, float f) {
        if (this.mIsFirstCount) {
            for (WatermarkTextPiece watermarkTextPiece : this.mWatermarkTextPieces) {
                watermarkTextPiece.configTextPaint(null);
                watermarkTextPiece.countTextInDialog();
            }
            this.mIsFirstCount = false;
            return;
        }
        for (WatermarkTextPiece watermarkTextPiece2 : this.mWatermarkTextPieces) {
            watermarkTextPiece2.configTextPaint(null);
            if (z) {
                watermarkTextPiece2.countTextInDialog();
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public String getText() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).getText();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public float getTextTransparent() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).getTextTransparent();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public int getColor() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).getColor();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public TextStyle getTextStyle() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).getTextStyle();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isBoldText() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).isBoldText();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public boolean isShadow() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).isShadow();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public AutoLineLayout.TextAlignment getTextAlignment() {
        return this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).getTextAlignment();
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getUserLocationX() {
        return this.mUserLocationX;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public float getUserLocationY() {
        return this.mUserLocationY;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setActivation(boolean z) {
        this.mActivation = z;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public boolean isActivation() {
        return this.mActivation;
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
        float f3 = f / f2;
        float f4 = this.mUserLocationY / f2;
        float width = (rectF.width() / 1080.0f) / 0.95f;
        this.mScaleMultipleOrigin = width;
        this.mUserLocationX = f3 * width;
        this.mUserLocationY = f4 * width;
        float f5 = this.mWatermarkInfo.offsetX;
        if (f5 > 0.0f) {
            this.mDefaultLocationX = rectF.left + (ScreenUtils.dpToPixel(f5) * this.mScaleMultipleOrigin);
        } else if (f5 < 0.0f) {
            this.mDefaultLocationX = rectF.right + (ScreenUtils.dpToPixel(f5) * this.mScaleMultipleOrigin);
        }
        float f6 = this.mWatermarkInfo.offsetY;
        if (f6 > 0.0f) {
            float dpToPixel = rectF.top + (ScreenUtils.dpToPixel(f6) * this.mScaleMultipleOrigin);
            this.mDefaultLocationY = dpToPixel;
            this.mDefaultLocationY = dpToPixel + (this.mPaddingTop / 2.0f);
        } else if (f6 >= 0.0f) {
        } else {
            float dpToPixel2 = rectF.bottom + (ScreenUtils.dpToPixel(f6) * this.mScaleMultipleOrigin);
            this.mDefaultLocationY = dpToPixel2;
            this.mDefaultLocationY = dpToPixel2 + (this.mPaddingTop / 2.0f);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setColor(int i) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setColor(i);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setGradientsColor(int i) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setGradientsColor(i);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setText(String str) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setText(str);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setShadow(boolean z) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setShadow(z);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextTransparent(float f) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setTextTransparent(f);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextStyle(TextStyle textStyle) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setTextStyle(textStyle);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setBoldText(boolean z) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setBoldText(z);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setTextAlignment(AutoLineLayout.TextAlignment textAlignment) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setTextAlignment(textAlignment);
    }

    public final void setUserLocationX(float f) {
        this.mUserLocationX = f;
    }

    public final void setUserLocationY(float f) {
        this.mUserLocationY = f;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDrawOutline(boolean z) {
        for (WatermarkTextPiece watermarkTextPiece : this.mWatermarkTextPieces) {
            watermarkTextPiece.setDrawOutline(z);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void reverseColor(int i) {
        this.mReverseColor = !this.mReverseColor;
        for (WatermarkTextPiece watermarkTextPiece : this.mWatermarkTextPieces) {
            watermarkTextPiece.setColor(i);
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
        dialogStatusData.textStatusDatas = new TextStatusData[this.mWatermarkTextPieces.size()];
        dialogStatusData.isReverseColor = this.mReverseColor;
        for (int i = 0; i < dialogStatusData.textStatusDatas.length; i++) {
            WatermarkTextPiece watermarkTextPiece = this.mWatermarkTextPieces.get(i);
            TextStatusData textStatusData = new TextStatusData();
            textStatusData.color = watermarkTextPiece.getColor();
            textStatusData.transparentProgress = watermarkTextPiece.getTextTransparent();
            textStatusData.textStyle = watermarkTextPiece.getTextStyle();
            textStatusData.textBold = watermarkTextPiece.isBoldText();
            textStatusData.textShadow = watermarkTextPiece.isShadow();
            textStatusData.textAlignment = watermarkTextPiece.getTextAlignment();
            textStatusData.text = watermarkTextPiece.getText();
            dialogStatusData.textStatusDatas[i] = textStatusData;
        }
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public void setDialogWithStatusData(DialogStatusData dialogStatusData) {
        if (dialogStatusData.textStatusDatas != null) {
            for (int i = 0; i < dialogStatusData.textStatusDatas.length && i < this.mWatermarkTextPieces.size(); i++) {
                WatermarkTextPiece watermarkTextPiece = this.mWatermarkTextPieces.get(i);
                TextStatusData textStatusData = dialogStatusData.textStatusDatas[i];
                watermarkTextPiece.setColor(textStatusData.color);
                watermarkTextPiece.setTextTransparent(textStatusData.transparentProgress);
                watermarkTextPiece.setTextStyle(textStatusData.textStyle);
                watermarkTextPiece.setBoldText(textStatusData.textBold);
                watermarkTextPiece.setShadow(textStatusData.textShadow);
                if (!TextUtils.isEmpty(textStatusData.text)) {
                    watermarkTextPiece.setText(textStatusData.text);
                }
                watermarkTextPiece.setTextAlignment(textStatusData.textAlignment);
            }
        } else {
            setColor(dialogStatusData.color);
            setTextTransparent(dialogStatusData.transparentProgress);
            setTextStyle(dialogStatusData.textStyle);
            setBoldText(dialogStatusData.textBold);
            setShadow(dialogStatusData.textShadow);
            setTextAlignment(dialogStatusData.textAlignment);
        }
        setUserLocationX(dialogStatusData.itemPositionX);
        setUserLocationY(dialogStatusData.itemPositionY);
        setUserScaleMultiple(dialogStatusData.itemScale);
        setRotateDegrees(dialogStatusData.itemDegree);
        this.mReverseColor = dialogStatusData.isReverseColor;
    }

    public void setPaddingTop(int i) {
        this.mPaddingTop = i;
    }

    public void setBitmapLoadingListener(BitmapLoadingListener bitmapLoadingListener) {
        this.mBitmapLoadingListener = bitmapLoadingListener;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextDialogConfig
    public String getSampleName() {
        return "watermark_" + this.mWatermarkInfo.name;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        DialogStatusData dialogStatusData = new DialogStatusData();
        getDialogStatusData(dialogStatusData);
        ParcelableGenericUtils.writeObject(parcel, i, dialogStatusData);
        ParcelableGenericUtils.writeObject(parcel, i, this.mWatermarkInfo);
        parcel.writeFloat(this.mScaleMultipleOrigin);
        parcel.writeFloat(this.mDefaultLocationX);
        parcel.writeFloat(this.mDefaultLocationY);
    }

    public WatermarkDialog(Parcel parcel) {
        this();
        this.mDialogStatusData = (DialogStatusData) ParcelableGenericUtils.readObject(parcel);
        this.mWatermarkInfo = (WatermarkInfo) ParcelableGenericUtils.readObject(parcel);
        this.mScaleMultipleOrigin = parcel.readFloat();
        this.mDefaultLocationX = parcel.readFloat();
        this.mDefaultLocationY = parcel.readFloat();
        this.mIsFromParcel = true;
        this.mResource = GalleryApp.sGetAndroidContext().getResources();
        init();
        countLocation(true, 0.0f);
    }

    @Override // com.miui.gallery.editor.photo.core.imports.text.base.ITextSetting
    public void setIsStroke(boolean z) {
        this.mWatermarkTextPieces.get(this.mCurrentPieceIndex).setIsStroke(z);
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
