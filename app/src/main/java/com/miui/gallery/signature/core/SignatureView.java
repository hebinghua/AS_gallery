package com.miui.gallery.signature.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.signature.SignatureColor;
import com.miui.gallery.signature.core.pen.BasePen;
import com.miui.gallery.signature.core.pen.SteelPen;
import com.miui.gallery.signature.core.utils.ConvertUtils;
import com.miui.gallery.util.MiscUtil;

/* loaded from: classes2.dex */
public class SignatureView extends View {
    public static int DEFAULT_TEXT_COLOR = Color.parseColor("#66000000");
    public static int DEFAULT_TEXT_SIZE = 42;
    public Bitmap mBitmap;
    public Canvas mCanvas;
    public int mHeight;
    public String mHintText;
    public boolean mIsBlank;
    public boolean mIsClearBitmap;
    public boolean mIsDrawing;
    public Matrix mMatrix;
    public int mMaxHeight;
    public String mOriginPath;
    public Paint mPaint;
    public BasePen mPen;
    public SignatureStateListener mSignatureStateListener;
    public int mTextColor;
    public Paint mTextPaint;
    public float mTextSize;
    public int mWidth;

    public SignatureView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignatureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsBlank = true;
        this.mTextColor = DEFAULT_TEXT_COLOR;
        this.mTextSize = DEFAULT_TEXT_SIZE;
        init(context, attributeSet, i);
    }

    public void init(Context context, AttributeSet attributeSet, int i) {
        obtainAttrs(context, attributeSet);
        initPaint();
        this.mMatrix = new Matrix();
        SteelPen steelPen = new SteelPen(this.mPaint);
        this.mPen = steelPen;
        steelPen.setContext(getContext());
        this.mMaxHeight = getResources().getDimensionPixelSize(R.dimen.signature_dialog_signature_view_height);
    }

    public final void obtainAttrs(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SignatureView);
        this.mTextSize = obtainStyledAttributes.getDimension(2, DEFAULT_TEXT_SIZE);
        this.mTextColor = obtainStyledAttributes.getColor(1, DEFAULT_TEXT_COLOR);
        this.mHintText = obtainStyledAttributes.getString(0);
        obtainStyledAttributes.recycle();
    }

    public final void initCanvas() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            Bitmap copy = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            this.mBitmap = createBlankBitmap();
            this.mCanvas = new Canvas(this.mBitmap);
            this.mMatrix.reset();
            this.mMatrix.setRectToRect(new RectF(0.0f, 0.0f, copy.getWidth(), copy.getHeight()), new RectF(0.0f, 0.0f, this.mBitmap.getWidth(), this.mBitmap.getHeight()), Matrix.ScaleToFit.CENTER);
            this.mCanvas.drawColor(0);
            this.mCanvas.drawBitmap(copy, this.mMatrix, this.mPaint);
            copy.recycle();
            return;
        }
        this.mBitmap = createBlankBitmap();
        Canvas canvas = new Canvas(this.mBitmap);
        this.mCanvas = canvas;
        canvas.drawColor(0);
    }

    public final void initPaint() {
        Paint paint = new Paint(1);
        this.mTextPaint = paint;
        paint.setTextSize(this.mTextSize);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mPaint = new Paint(1);
        if (MiscUtil.isNightMode(getContext())) {
            this.mPaint.setColor(-1);
        } else {
            this.mPaint.setColor(-16777216);
        }
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeMiter(1.0f);
        this.mPaint.setStrokeWidth(ConvertUtils.dp2px(getContext(), 7));
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(Math.min(View.MeasureSpec.getSize(i2), this.mMaxHeight), 1073741824));
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
        initCanvas();
    }

    public void restoreFromBitmap(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mOriginPath = str;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options);
        initCanvas();
        this.mMatrix.reset();
        int width = decodeFile.getWidth();
        int height = decodeFile.getHeight();
        float f = width;
        float f2 = height;
        new RectF(0.0f, 0.0f, f, f2);
        RectF rectF = new RectF(0.0f, 0.0f, f, f2);
        int i = this.mWidth;
        if (width > i || height > this.mHeight) {
            rectF.right = i;
            rectF.bottom = this.mHeight * 1.0f * ((i * 1.0f) / f);
        }
        this.mMatrix.setRectToRect(new RectF(0.0f, 0.0f, decodeFile.getWidth(), decodeFile.getHeight()), new RectF(0.0f, 0.0f, this.mBitmap.getWidth(), this.mBitmap.getHeight()), Matrix.ScaleToFit.CENTER);
        this.mCanvas.drawBitmap(decodeFile, this.mMatrix, this.mPaint);
        this.mIsBlank = false;
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mIsBlank) {
            if (TextUtils.isEmpty(this.mHintText)) {
                return;
            }
            canvas.drawText(this.mHintText, (this.mWidth / 2.0f) - (getTextWidth(this.mTextPaint, this.mHintText) / 2.0f), (this.mHeight / 2.0f) - ((this.mTextPaint.descent() + this.mTextPaint.ascent()) / 2.0f), this.mTextPaint);
            return;
        }
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
        if (this.mIsClearBitmap) {
            clearBitmap();
        } else {
            this.mPen.onDraw(canvas);
        }
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

    @Override // android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mPen.onTouchEvent(motionEvent, this.mCanvas);
        int action = motionEvent.getAction();
        if (action == 1) {
            this.mIsDrawing = false;
        } else if (action == 2) {
            this.mIsDrawing = true;
            this.mIsBlank = false;
        }
        invalidate();
        return true;
    }

    public final Bitmap createBlankBitmap() {
        return Bitmap.createBitmap(this.mWidth, this.mHeight, Bitmap.Config.ARGB_8888);
    }

    public final void clearBitmap() {
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mCanvas.drawPaint(this.mPaint);
        this.mPaint.setXfermode(null);
        this.mPen.clear();
        this.mIsBlank = true;
        this.mIsClearBitmap = false;
        SignatureStateListener signatureStateListener = this.mSignatureStateListener;
        if (signatureStateListener != null) {
            signatureStateListener.onCleared();
        }
        invalidate();
    }

    public void changeBitmapColor(SignatureColor signatureColor) {
        changeBitmapColor(signatureColor, true);
    }

    public void changeBitmapColor(SignatureColor signatureColor, boolean z) {
        int color = getResources().getColor(signatureColor.mColorId);
        setPenColor(color);
        this.mBitmap = getChangeColorBitmap(color);
        this.mCanvas = new Canvas(this.mBitmap);
        if (z) {
            invalidate();
        }
    }

    public final Bitmap getChangeColorBitmap(int i) {
        Bitmap createBitmap = Bitmap.createBitmap(this.mBitmap.getWidth(), this.mBitmap.getHeight(), this.mBitmap.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        this.mPaint.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
        this.mPaint.setColorFilter(null);
        return createBitmap;
    }

    public void setPenColor(int i) {
        Paint paint = this.mPaint;
        if (paint == null) {
            return;
        }
        paint.setColor(i);
    }

    public int getPenColor() {
        Paint paint = this.mPaint;
        if (paint == null) {
            return getResources().getColor(SignatureColor.SIGNATURE_COLOR_DEFAULT.mColorId);
        }
        return paint.getColor();
    }

    public void setPenSize(float f) {
        Paint paint = this.mPaint;
        if (paint == null) {
            return;
        }
        paint.setStrokeWidth(f);
        this.mPen.setPaint(this.mPaint);
    }

    public float getPenSize() {
        Paint paint = this.mPaint;
        if (paint == null) {
            return 0.0f;
        }
        return paint.getStrokeWidth();
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public void clear() {
        this.mIsClearBitmap = true;
        invalidate();
    }

    public boolean isBlank() {
        return this.mIsBlank;
    }

    public void release() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        this.mBitmap.recycle();
        this.mBitmap = null;
    }

    public Bitmap buildAreaBitmap(boolean z, int i) {
        Bitmap bitmap;
        if (this.mIsBlank) {
            return null;
        }
        if (z) {
            bitmap = BitmapUtil.clearBlank(this.mBitmap, i, 0);
        } else {
            bitmap = this.mBitmap;
        }
        destroyDrawingCache();
        return bitmap;
    }

    public void setSignatureStateListener(SignatureStateListener signatureStateListener) {
        this.mSignatureStateListener = signatureStateListener;
    }

    public String getOriginPath() {
        return this.mOriginPath;
    }
}
