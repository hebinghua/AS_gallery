package com.miui.gallery.editor.photo.core.imports.longcrop;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class LongBitmapDrawable extends Drawable {
    public Bitmap[] mBitmaps;
    public Paint mPaint = new Paint(3);

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public LongBitmapDrawable(Bitmap bitmap) {
        this.mBitmaps = new Bitmap[0];
        if (bitmap == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Paint paint = new Paint(4);
        while (height > 0) {
            int min = Math.min(height, 1024);
            Bitmap createBitmap = Bitmap.createBitmap(width, min, bitmap.getConfig());
            new Canvas(createBitmap).drawBitmap(bitmap, 0.0f, -(bitmap.getHeight() - height), paint);
            arrayList.add(createBitmap);
            height -= min;
        }
        this.mBitmaps = (Bitmap[]) arrayList.toArray(new Bitmap[arrayList.size()]);
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        canvas.save();
        Rect bounds = getBounds();
        if (bounds != null) {
            canvas.translate(-bounds.left, -bounds.top);
        }
        int i = 0;
        while (true) {
            Bitmap[] bitmapArr = this.mBitmaps;
            if (i < bitmapArr.length) {
                Bitmap bitmap = bitmapArr[i];
                if (!canvas.quickReject(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight(), Canvas.EdgeType.BW)) {
                    canvas.drawBitmap(bitmap, 0.0f, 0.0f, this.mPaint);
                }
                canvas.translate(0.0f, bitmap.getHeight());
                i++;
            } else {
                canvas.restore();
                return;
            }
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        Bitmap[] bitmapArr = this.mBitmaps;
        if (bitmapArr == null || bitmapArr.length == 0) {
            return 0;
        }
        return bitmapArr[0].getWidth();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        Bitmap[] bitmapArr = this.mBitmaps;
        int i = 0;
        if (bitmapArr == null || bitmapArr.length == 0) {
            return 0;
        }
        int i2 = 0;
        while (true) {
            Bitmap[] bitmapArr2 = this.mBitmaps;
            if (i >= bitmapArr2.length) {
                return i2;
            }
            i2 += bitmapArr2[i].getHeight();
            i++;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }
}
