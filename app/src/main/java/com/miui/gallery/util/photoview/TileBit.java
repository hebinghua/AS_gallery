package com.miui.gallery.util.photoview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class TileBit {
    public static final Rect sTileRect = new Rect();
    public Bitmap mBitmap;
    public int mValidateHeight;
    public int mValidateWidth;

    public TileBit(Bitmap bitmap, int i, int i2) {
        this.mBitmap = bitmap;
        this.mValidateWidth = i;
        this.mValidateHeight = i2;
    }

    public int getValidateWidth() {
        return this.mValidateWidth;
    }

    public int getValidateHeight() {
        return this.mValidateHeight;
    }

    public void recycle(BitmapRecycleCallback bitmapRecycleCallback) {
        if (isContentValidate()) {
            if (bitmapRecycleCallback != null) {
                bitmapRecycleCallback.recycle(this.mBitmap);
            } else {
                DefaultLogger.d("TileBit", "recycle bitmap: %s", this.mBitmap);
                this.mBitmap.recycle();
            }
        }
        this.mBitmap = null;
    }

    public boolean isContentValidate() {
        return BaseBitmapUtils.isValid(this.mBitmap);
    }

    public void draw(Canvas canvas, RectF rectF, Paint paint) {
        Rect rect = sTileRect;
        rect.set(0, 0, getValidateWidth(), getValidateHeight());
        canvas.drawBitmap(this.mBitmap, rect, rectF, paint);
    }
}
