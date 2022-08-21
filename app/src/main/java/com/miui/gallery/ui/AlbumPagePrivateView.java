package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes2.dex */
public class AlbumPagePrivateView extends View {
    public Bitmap mBitmap;
    public int mBlueColor;
    public OnPrivateColorChangeListener mColorChangeListener;
    public int mCurrPosition;
    public int mGreenColor;
    public boolean mIsBackgroundGradient;
    public int mRedColor;

    /* loaded from: classes2.dex */
    public interface OnPrivateColorChangeListener {
        void onColorChange(int i);
    }

    public AlbumPagePrivateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnColorChangeListener(OnPrivateColorChangeListener onPrivateColorChangeListener) {
        this.mColorChangeListener = onPrivateColorChangeListener;
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        this.mRedColor = Color.red(i);
        this.mGreenColor = Color.green(i);
        this.mBlueColor = Color.blue(i);
    }

    public void setBackgroundResId(int i) {
        this.mBitmap = BitmapFactory.decodeResource(getResources(), i);
    }

    public void setBackgroundGradient(boolean z) {
        this.mIsBackgroundGradient = z;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int i;
        super.onDraw(canvas);
        canvas.save();
        int height = (int) (getHeight() * 0.08f);
        int height2 = (!this.mIsBackgroundGradient || (i = this.mCurrPosition) <= height) ? 255 : (int) (((i - height) / (getHeight() - height)) * 90.0f);
        canvas.drawARGB(height2, this.mRedColor, this.mGreenColor, this.mBlueColor);
        OnPrivateColorChangeListener onPrivateColorChangeListener = this.mColorChangeListener;
        if (onPrivateColorChangeListener != null) {
            onPrivateColorChangeListener.onColorChange(Color.argb(height2, this.mRedColor, this.mGreenColor, this.mBlueColor));
        }
        if (this.mBitmap != null) {
            float height3 = (getHeight() - this.mBitmap.getHeight()) * 0.32f;
            if (this.mCurrPosition > height3) {
                canvas.drawBitmap(this.mBitmap, (getWidth() - this.mBitmap.getWidth()) / 2, getHeight() - (this.mCurrPosition - height3), (Paint) null);
            }
        }
        canvas.restore();
    }
}
