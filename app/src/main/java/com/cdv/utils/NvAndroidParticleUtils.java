package com.cdv.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

/* loaded from: classes.dex */
public class NvAndroidParticleUtils {

    /* loaded from: classes.dex */
    public static class SpriteGenerator {
        private Canvas m_canvas;
        private Paint m_paint;
        private int m_spriteImageSize;

        public SpriteGenerator(Bitmap bitmap, int i) {
            Canvas canvas = new Canvas(bitmap);
            this.m_canvas = canvas;
            canvas.drawColor(0, PorterDuff.Mode.SRC);
            Paint paint = new Paint();
            this.m_paint = paint;
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            this.m_paint.setAntiAlias(false);
            this.m_paint.setFilterBitmap(false);
            this.m_spriteImageSize = i;
        }

        public void drawSpriteImage(int i, int i2, Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            if (bitmap.getWidth() == this.m_spriteImageSize && bitmap.getHeight() == this.m_spriteImageSize) {
                this.m_canvas.drawBitmap(bitmap, i, i2, this.m_paint);
                return;
            }
            Canvas canvas = this.m_canvas;
            int i3 = this.m_spriteImageSize;
            canvas.drawBitmap(bitmap, (Rect) null, new Rect(i, i2, i + i3, i3 + i2), this.m_paint);
        }
    }
}
