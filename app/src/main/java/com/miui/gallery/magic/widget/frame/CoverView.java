package com.miui.gallery.magic.widget.frame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.magic.R$color;
import com.miui.gallery.util.BaseMiscUtil;

/* loaded from: classes2.dex */
public class CoverView extends View {
    public Bitmap mBitmap;
    public int mCoverLayerColor;
    public int mPaddingLeft;
    public int mPaddingRight;
    public Paint mPaint;
    public RectF mRectF;

    public CoverView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CoverView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public CoverView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public final void init() {
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mCoverLayerColor = getResources().getColor(R$color.magic_cut_video_block_percent_60);
    }

    @Override // android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mRectF = new RectF(i, i2, i3, i4);
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null && bitmap.getWidth() == this.mRectF.width() && this.mBitmap.getHeight() == this.mRectF.height()) {
            return;
        }
        Bitmap bitmap2 = this.mBitmap;
        if (bitmap2 != null && !bitmap2.isRecycled()) {
            this.mBitmap.recycle();
        }
        this.mBitmap = Bitmap.createBitmap((int) this.mRectF.width(), (int) this.mRectF.height(), Bitmap.Config.ARGB_8888);
    }

    public void updateRect(int i, int i2) {
        Rect rect = new Rect(0, 0, (int) this.mRectF.width(), (int) this.mRectF.height());
        Canvas canvas = new Canvas(this.mBitmap);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mPaint.setColor(this.mCoverLayerColor);
        canvas.drawRect(rect, this.mPaint);
        this.mPaint.setXfermode(null);
        this.mPaint.setColor(this.mCoverLayerColor);
        canvas.drawRect(new Rect(this.mPaddingLeft, 0, (int) (this.mRectF.width() - this.mPaddingRight), (int) this.mRectF.height()), this.mPaint);
        if (BaseMiscUtil.isRTLDirection()) {
            this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            this.mPaint.setColor(0);
            canvas.drawRect(new Rect((int) (this.mRectF.width() - i2), 0, (int) (this.mRectF.width() - i), (int) this.mRectF.height()), this.mPaint);
            return;
        }
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.mPaint.setColor(0);
        canvas.drawRect(new Rect(i, 0, i2, (int) this.mRectF.height()), this.mPaint);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, (Paint) null);
    }
}
