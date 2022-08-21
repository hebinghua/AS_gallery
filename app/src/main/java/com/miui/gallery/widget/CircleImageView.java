package com.miui.gallery.widget;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class CircleImageView extends ImageView {
    public Bitmap mBitmap;
    public int mBitmapHeight;
    public final Paint mBitmapPaint;
    public BitmapShader mBitmapShader;
    public int mBitmapWidth;
    public int mBorderColor;
    public boolean mBorderOverlay;
    public final Paint mBorderPaint;
    public float mBorderRadius;
    public final RectF mBorderRect;
    public int mBorderWidth;
    public ColorFilter mColorFilter;
    public float mDrawableRadius;
    public final RectF mDrawableRect;
    public int mDrawableRectInset;
    public int mFillColor;
    public final Paint mFillPaint;
    public boolean mReady;
    public boolean mSetupPending;
    public final Matrix mShaderMatrix;
    public static final ImageView.ScaleType SCALE_TYPE = ImageView.ScaleType.CENTER_CROP;
    public static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    @Override // android.widget.ImageView
    public ImageView.ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == SCALE_TYPE) {
            return;
        }
        throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
    }

    @Override // android.widget.ImageView
    public void setAdjustViewBounds(boolean z) {
        if (!z) {
            return;
        }
        throw new IllegalArgumentException("adjustViewBounds not supported.");
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        if (this.mBitmap == null) {
            return;
        }
        if (this.mFillColor != 0) {
            canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, this.mDrawableRadius, this.mFillPaint);
        }
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, this.mDrawableRadius, this.mBitmapPaint);
        if (this.mBorderWidth == 0) {
            return;
        }
        canvas.drawCircle(getWidth() / 2.0f, getHeight() / 2.0f, this.mBorderRadius, this.mBorderPaint);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        setup();
    }

    public int getBorderColor() {
        return this.mBorderColor;
    }

    public void setBorderColor(int i) {
        if (i == this.mBorderColor) {
            return;
        }
        this.mBorderColor = i;
        this.mBorderPaint.setColor(i);
        invalidate();
    }

    public void setBorderColorResource(int i) {
        setBorderColor(getContext().getResources().getColor(i));
    }

    public int getFillColor() {
        return this.mFillColor;
    }

    public void setFillColor(int i) {
        if (i == this.mFillColor) {
            return;
        }
        this.mFillColor = i;
        this.mFillPaint.setColor(i);
        invalidate();
    }

    public void setFillColorResource(int i) {
        setFillColor(getContext().getResources().getColor(i));
    }

    public int getBorderWidth() {
        return this.mBorderWidth;
    }

    public void setBorderWidth(int i) {
        if (i == this.mBorderWidth) {
            return;
        }
        this.mBorderWidth = i;
        setup();
    }

    public void setBorderOverlay(boolean z) {
        if (z == this.mBorderOverlay) {
            return;
        }
        this.mBorderOverlay = z;
        setup();
    }

    public void setDrawableInset(int i, boolean z) {
        if (this.mDrawableRectInset == i) {
            return;
        }
        this.mDrawableRectInset = i;
        if (!z) {
            return;
        }
        updateShaderMatrix();
        invalidate();
    }

    @Override // android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        this.mBitmap = bitmap;
        setup();
    }

    @Override // android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        this.mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override // android.widget.ImageView
    public void setImageResource(int i) {
        super.setImageResource(i);
        this.mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override // android.widget.ImageView
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        this.mBitmap = uri != null ? getBitmapFromDrawable(getDrawable()) : null;
        setup();
    }

    @Override // android.widget.ImageView
    public void setColorFilter(ColorFilter colorFilter) {
        if (colorFilter == this.mColorFilter) {
            return;
        }
        this.mColorFilter = colorFilter;
        applyColorFilter();
        invalidate();
    }

    public final void applyColorFilter() {
        Paint paint = this.mBitmapPaint;
        if (paint != null) {
            paint.setColorFilter(this.mColorFilter);
        }
    }

    public final Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap createBitmap;
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        try {
            if (drawable instanceof ColorDrawable) {
                createBitmap = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
            } else {
                createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return createBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final void setup() {
        if (!this.mReady) {
            this.mSetupPending = true;
        } else if (getWidth() == 0 && getHeight() == 0) {
        } else {
            if (this.mBitmap == null) {
                invalidate();
                return;
            }
            Bitmap bitmap = this.mBitmap;
            Shader.TileMode tileMode = Shader.TileMode.CLAMP;
            this.mBitmapShader = new BitmapShader(bitmap, tileMode, tileMode);
            this.mBitmapPaint.setAntiAlias(true);
            this.mBitmapPaint.setShader(this.mBitmapShader);
            this.mBorderPaint.setStyle(Paint.Style.STROKE);
            this.mBorderPaint.setAntiAlias(true);
            this.mBorderPaint.setColor(this.mBorderColor);
            this.mBorderPaint.setStrokeWidth(this.mBorderWidth);
            this.mFillPaint.setStyle(Paint.Style.FILL);
            this.mFillPaint.setAntiAlias(true);
            this.mFillPaint.setColor(this.mFillColor);
            this.mBitmapHeight = this.mBitmap.getHeight();
            this.mBitmapWidth = this.mBitmap.getWidth();
            this.mBorderRect.set(0.0f, 0.0f, getWidth(), getHeight());
            this.mBorderRadius = Math.min((this.mBorderRect.height() - this.mBorderWidth) / 2.0f, (this.mBorderRect.width() - this.mBorderWidth) / 2.0f);
            this.mDrawableRect.set(this.mBorderRect);
            if (!this.mBorderOverlay) {
                RectF rectF = this.mDrawableRect;
                int i = this.mBorderWidth;
                rectF.inset(i, i);
            }
            this.mDrawableRadius = Math.min(this.mDrawableRect.height() / 2.0f, this.mDrawableRect.width() / 2.0f);
            RectF rectF2 = this.mDrawableRect;
            int i2 = this.mDrawableRectInset;
            rectF2.inset(i2, i2);
            if (this.mDrawableRect.width() < 0.0f || this.mDrawableRect.height() < 0.0f) {
                DefaultLogger.w("CircleImageView", "invalidate drawable inset %d", Integer.valueOf(this.mDrawableRectInset));
                RectF rectF3 = this.mDrawableRect;
                int i3 = this.mDrawableRectInset;
                rectF3.inset(-i3, -i3);
            }
            applyColorFilter();
            updateShaderMatrix();
            invalidate();
        }
    }

    public final void updateShaderMatrix() {
        this.mShaderMatrix.set(null);
        this.mShaderMatrix.setRectToRect(new RectF(0.0f, 0.0f, this.mBitmapWidth, this.mBitmapHeight), this.mDrawableRect, Matrix.ScaleToFit.FILL);
        this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
    }
}
