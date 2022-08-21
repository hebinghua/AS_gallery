package com.miui.gallery.collage.render;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.collage.CollageUtils;
import com.miui.gallery.collage.core.poster.ElementPositionModel;
import com.miui.gallery.collage.core.poster.ImageElementModel;
import com.miui.gallery.collage.core.poster.PosterModel;
import com.miui.gallery.collage.core.poster.SpecifyDrawableModel;
import com.miui.gallery.collage.core.poster.TextElementModel;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;
import com.miui.gallery.util.ScreenUtils;
import java.io.File;

/* loaded from: classes.dex */
public class PosterElementRender {
    public int mBackground = -1;
    public Drawable[] mImageDrawables;
    public Drawable[] mSpecifyDrawables;
    public TextEditorHolder[] mTextEditorHolders;

    /* loaded from: classes.dex */
    public interface LoadDataListener {
        void onLoadFinish();
    }

    public void draw(Canvas canvas) {
        Drawable[] drawableArr = this.mImageDrawables;
        if (drawableArr != null && drawableArr.length > 0) {
            for (Drawable drawable : drawableArr) {
                drawable.draw(canvas);
            }
        }
        Drawable[] drawableArr2 = this.mSpecifyDrawables;
        if (drawableArr2 != null && drawableArr2.length > 0) {
            for (Drawable drawable2 : drawableArr2) {
                drawable2.draw(canvas);
            }
        }
        TextEditorHolder[] textEditorHolderArr = this.mTextEditorHolders;
        if (textEditorHolderArr == null || textEditorHolderArr.length <= 0) {
            return;
        }
        for (TextEditorHolder textEditorHolder : textEditorHolderArr) {
            textEditorHolder.draw(canvas);
        }
    }

    public int getBackground() {
        return this.mBackground;
    }

    public TextEditorHolder[] getTextEditorHolders() {
        return this.mTextEditorHolders;
    }

    public void initializeAsync(final PosterModel posterModel, final int i, final int i2, final float f, final Context context, final LoadDataListener loadDataListener) {
        new Thread() { // from class: com.miui.gallery.collage.render.PosterElementRender.1
            public int mBackground;
            public Drawable[] mImageDrawables;
            public Drawable[] mSpecifyDrawables;
            public TextEditorHolder[] mTextEditorHolders;

            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                PosterModel posterModel2 = posterModel;
                ImageElementModel[] imageElementModelArr = posterModel2.imageElementModels;
                SpecifyDrawableModel[] specifyDrawableModelArr = posterModel2.specifyDrawableModels;
                TextElementModel[] textElementModelArr = posterModel2.textElementModels;
                String str = posterModel2.background;
                if (imageElementModelArr != null && imageElementModelArr.length > 0) {
                    this.mImageDrawables = PosterElementRender.generateImageDrawables(context.getResources(), imageElementModelArr, i, i2);
                }
                if (specifyDrawableModelArr != null && specifyDrawableModelArr.length > 0) {
                    this.mSpecifyDrawables = PosterElementRender.generateSpecifyDrawables(context.getResources(), specifyDrawableModelArr, i, i2, f);
                }
                if (textElementModelArr != null && textElementModelArr.length > 0) {
                    this.mTextEditorHolders = PosterElementRender.generateTextHolders(context, textElementModelArr, i, i2, f);
                }
                if (!TextUtils.isEmpty(str)) {
                    this.mBackground = Color.parseColor(str);
                }
                if (context == null) {
                    return;
                }
                new Handler(context.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.collage.render.PosterElementRender.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                        PosterElementRender.this.mImageDrawables = anonymousClass1.mImageDrawables;
                        AnonymousClass1 anonymousClass12 = AnonymousClass1.this;
                        PosterElementRender.this.mSpecifyDrawables = anonymousClass12.mSpecifyDrawables;
                        AnonymousClass1 anonymousClass13 = AnonymousClass1.this;
                        PosterElementRender.this.mTextEditorHolders = anonymousClass13.mTextEditorHolders;
                        AnonymousClass1 anonymousClass14 = AnonymousClass1.this;
                        PosterElementRender.this.mBackground = anonymousClass14.mBackground;
                        LoadDataListener loadDataListener2 = loadDataListener;
                        if (loadDataListener2 != null) {
                            loadDataListener2.onLoadFinish();
                        }
                    }
                });
            }
        }.start();
    }

    public void initialize(PosterModel posterModel, int i, int i2, float f, Context context) {
        ImageElementModel[] imageElementModelArr = posterModel.imageElementModels;
        SpecifyDrawableModel[] specifyDrawableModelArr = posterModel.specifyDrawableModels;
        TextElementModel[] textElementModelArr = posterModel.textElementModels;
        String str = posterModel.background;
        if (imageElementModelArr != null && imageElementModelArr.length > 0) {
            this.mImageDrawables = generateImageDrawables(context.getResources(), imageElementModelArr, i, i2);
        }
        if (specifyDrawableModelArr != null && specifyDrawableModelArr.length > 0) {
            this.mSpecifyDrawables = generateSpecifyDrawables(context.getResources(), specifyDrawableModelArr, i, i2, f);
        }
        if (textElementModelArr != null && textElementModelArr.length > 0) {
            this.mTextEditorHolders = generateTextHolders(context, textElementModelArr, i, i2, f);
        }
        if (!TextUtils.isEmpty(str)) {
            this.mBackground = Color.parseColor(str);
        }
    }

    /* loaded from: classes.dex */
    public static class TextEditorHolder {
        public float mDensity;
        public boolean mHasModify;
        public int mHighLightColor;
        public int mHighLightLineColor;
        public String mOriginText;
        public int mParentHeight;
        public int mParentWidth;
        public boolean mRTL;
        public float mScaleOffset;
        public TextElementModel mTextElementModel;
        public int mTextTouchOffset;
        public RectF mTextRectF = new RectF();
        public RectF mHighLightRectF = new RectF();
        public AutoLineLayout mAutoLineLayout = new AutoLineLayout();
        public Paint mPaint = new Paint(1);
        public Paint mPathPaint = new Paint(1);
        public Path mHighLightPath = new Path();
        public int mHighLightBackground = 0;
        public RectF mTextRectForTouch = new RectF();
        public float mCurrentTextProgress = 0.0f;
        public int mAlpha = 0;

        public TextEditorHolder(Context context, TextElementModel textElementModel, int i, int i2, float f) {
            boolean z = false;
            this.mHasModify = false;
            this.mRTL = false;
            Resources resources = context.getResources();
            this.mRTL = resources.getConfiguration().getLayoutDirection() == 1 ? true : z;
            this.mHighLightLineColor = resources.getColor(R.color.collage_poster_high_light_line_color);
            this.mTextTouchOffset = resources.getDimensionPixelSize(R.dimen.collage_text_touch_offset);
            this.mHighLightColor = resources.getColor(R.color.collage_poster_high_light_color);
            this.mPathPaint.setPathEffect(new DashPathEffect(new float[]{12.0f, 6.0f}, 0.0f));
            this.mPathPaint.setColor(this.mHighLightLineColor);
            this.mPathPaint.setStyle(Paint.Style.STROKE);
            this.mPathPaint.setStrokeWidth(1.0f);
            this.mParentWidth = i;
            this.mParentHeight = i2;
            this.mDensity = resources.getDisplayMetrics().density;
            this.mScaleOffset = f;
            this.mTextElementModel = textElementModel;
            configPaint(resources);
            this.mOriginText = this.mTextElementModel.getText(context);
            if (TextUtils.isEmpty(this.mTextElementModel.currentText)) {
                this.mTextElementModel.currentText = this.mOriginText;
            } else {
                this.mHasModify = true;
            }
            this.mAutoLineLayout.setTextAlignment(AutoLineLayout.TextAlignment.CENTER);
            this.mAutoLineLayout.setText(textElementModel.currentText);
            countTextAndStroke();
        }

        public void draw(Canvas canvas) {
            if (this.mCurrentTextProgress > 0.0f) {
                this.mPaint.setColor(this.mHighLightBackground);
                this.mPathPaint.setAlpha(this.mAlpha);
                canvas.drawRect(this.mHighLightRectF, this.mPaint);
                canvas.drawPath(this.mHighLightPath, this.mPathPaint);
            }
            PosterElementRender.drawText(canvas, this.mAutoLineLayout, this.mTextRectF, this.mScaleOffset);
        }

        public final void configPaint(Resources resources) {
            TextElementModel textElementModel = this.mTextElementModel;
            if (textElementModel == null) {
                return;
            }
            PosterElementRender.configTextPaint(this.mAutoLineLayout, textElementModel, resources);
        }

        public void countTextAndStroke() {
            TextElementModel textElementModel = this.mTextElementModel;
            if (textElementModel == null) {
                return;
            }
            PosterElementRender.countText(this.mAutoLineLayout, textElementModel, this.mTextRectF, this.mParentWidth, this.mParentHeight, this.mScaleOffset, this.mRTL);
            this.mHighLightRectF.set(this.mTextRectF);
            RectF rectF = this.mHighLightRectF;
            float f = this.mDensity;
            rectF.inset((-6.0f) * f, f * (-2.0f));
            RectF rectF2 = new RectF();
            rectF2.set(this.mHighLightRectF);
            this.mHighLightPath.reset();
            this.mHighLightPath.addRect(rectF2, Path.Direction.CW);
            this.mTextRectForTouch.set(this.mTextRectF);
            RectF rectF3 = this.mTextRectForTouch;
            int i = this.mTextTouchOffset;
            rectF3.inset(-i, -i);
        }

        public void setTextAndCount(String str, Rect rect) {
            TextElementModel textElementModel = this.mTextElementModel;
            if (textElementModel == null) {
                return;
            }
            this.mHasModify = true;
            textElementModel.currentText = str;
            this.mAutoLineLayout.setText(str);
            countTextAndStroke();
            this.mTextRectF.roundOut(rect);
        }

        public boolean contains(float f, float f2) {
            return this.mTextRectForTouch.contains(f, f2);
        }

        public String getCurrentText() {
            return this.mAutoLineLayout.getText();
        }

        public void setCurrentTextProgress(float f) {
            this.mCurrentTextProgress = f;
            this.mHighLightBackground = PosterElementRender.getColorToColorProgress(0, this.mHighLightColor, f);
            this.mAlpha = (int) (this.mCurrentTextProgress * 255.0f);
        }

        public boolean hasModify() {
            return this.mHasModify;
        }

        public int getMaxSize() {
            return Math.max(this.mTextElementModel.maxLength, this.mOriginText.length());
        }
    }

    public static void configTextPaint(AutoLineLayout autoLineLayout, TextElementModel textElementModel, Resources resources) {
        if (textElementModel == null) {
            return;
        }
        Paint paint = autoLineLayout.getPaint();
        paint.setTextSize(textElementModel.textSize * resources.getDisplayMetrics().density);
        paint.setColor(Color.parseColor(textElementModel.textColor));
        autoLineLayout.setLetterSpace((textElementModel.letterSpace * ScreenUtils.getScreenWidth()) / 1080.0f);
    }

    public static void countText(AutoLineLayout autoLineLayout, TextElementModel textElementModel, RectF rectF, int i, int i2, float f, boolean z) {
        autoLineLayout.countText();
        autoLineLayout.getTextRect(rectF);
        rectF.left *= f;
        rectF.right *= f;
        rectF.top *= f;
        rectF.bottom *= f;
        ElementPositionModel.getRectByLocation(rectF, textElementModel, rectF.width(), rectF.height(), i, i2, z);
    }

    public static void drawText(Canvas canvas, AutoLineLayout autoLineLayout, RectF rectF, float f) {
        canvas.save();
        canvas.translate(rectF.centerX(), rectF.centerY());
        canvas.scale(f, f, 0.0f, 0.0f);
        autoLineLayout.draw(canvas);
        canvas.restore();
    }

    public static TextEditorHolder[] generateTextHolders(Context context, TextElementModel[] textElementModelArr, int i, int i2, float f) {
        TextEditorHolder[] textEditorHolderArr = new TextEditorHolder[textElementModelArr.length];
        for (int i3 = 0; i3 < textElementModelArr.length; i3++) {
            textEditorHolderArr[i3] = new TextEditorHolder(context, textElementModelArr[i3], i, i2, f);
        }
        return textEditorHolderArr;
    }

    public static Drawable[] generateSpecifyDrawables(Resources resources, SpecifyDrawableModel[] specifyDrawableModelArr, int i, int i2, float f) {
        float f2;
        float f3;
        if (specifyDrawableModelArr == null || specifyDrawableModelArr.length == 0) {
            return null;
        }
        RectF rectF = new RectF();
        Rect rect = new Rect();
        int length = specifyDrawableModelArr.length;
        Drawable[] drawableArr = new Drawable[length];
        for (int i3 = 0; i3 < length; i3++) {
            SpecifyDrawableModel specifyDrawableModel = specifyDrawableModelArr[i3];
            Drawable drawable = specifyDrawableModel.specifyDrawableType.getDrawable(resources, specifyDrawableModel);
            float intrinsicWidth = drawable.getIntrinsicWidth();
            float intrinsicHeight = drawable.getIntrinsicHeight();
            float f4 = specifyDrawableModel.width;
            if (f4 != 0.0f) {
                f2 = i * f4;
                float f5 = specifyDrawableModel.height;
                f3 = f5 != 0.0f ? i2 * f5 : (intrinsicHeight / f2) * f2;
            } else {
                f2 = intrinsicWidth * f;
                f3 = intrinsicHeight * f;
            }
            ElementPositionModel.getRectByLocation(rectF, specifyDrawableModel, f2, f3, i, i2, resources.getConfiguration().getLayoutDirection() == 1);
            rectF.round(rect);
            drawable.setBounds(rect);
            drawableArr[i3] = drawable;
        }
        return drawableArr;
    }

    public static Drawable[] generateImageDrawables(Resources resources, ImageElementModel[] imageElementModelArr, int i, int i2) {
        if (imageElementModelArr == null || imageElementModelArr.length == 0) {
            return null;
        }
        RectF rectF = new RectF();
        Rect rect = new Rect();
        int length = imageElementModelArr.length;
        Drawable[] drawableArr = new Drawable[length];
        for (int i3 = 0; i3 < length; i3++) {
            ImageElementModel imageElementModel = imageElementModelArr[i3];
            Drawable drawableByAssets = CollageUtils.getDrawableByAssets(resources, imageElementModel.relativePath + File.separator + imageElementModel.name);
            float intrinsicHeight = ((float) drawableByAssets.getIntrinsicHeight()) / ((float) drawableByAssets.getIntrinsicWidth());
            float f = imageElementModel.width * ((float) i);
            ElementPositionModel.getRectByLocation(rectF, imageElementModel, f, f * intrinsicHeight, i, i2, resources.getConfiguration().getLayoutDirection() == 1);
            rectF.round(rect);
            drawableByAssets.setBounds(rect);
            drawableArr[i3] = drawableByAssets;
        }
        return drawableArr;
    }

    public static int getColorToColorProgress(int i, int i2, float f) {
        int alpha = Color.alpha(i);
        Color.red(i);
        Color.green(i);
        Color.blue(i);
        return Color.argb(alpha + ((int) ((Color.alpha(i2) - alpha) * f)), Color.red(i2), Color.green(i2), Color.blue(i2));
    }
}
