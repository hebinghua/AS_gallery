package com.miui.gallery.magic.widget.portrait;

import android.graphics.RectF;
import android.util.SizeF;

/* loaded from: classes2.dex */
public class BackgroundAdaptationHelper {
    public final float getValueByScale(float f, float f2, float f3, float f4, float f5, float f6) {
        float f7 = f2 * f5;
        if (f3 == 0.0f) {
            return 0.0f;
        }
        if (f3 >= 0.0f) {
            return f3 == Float.MAX_VALUE ? f - f7 : (f - f7) / ((1.0f / f3) + 1.0f);
        } else if (f4 < 0.0f) {
            return f4 * f2;
        } else {
            return (f - f7) - (((f6 - f4) - f5) * f2);
        }
    }

    /* loaded from: classes2.dex */
    public static class ScaleResult {
        public SizeF hsBgInfo;
        public RectF hsImageInfo;
        public float hsWidth = 1.0f;
        public float hsLeft = 1.0f;
        public float hsHeight = 1.0f;
        public float hsTop = 1.0f;
        public float scale = 1.0f;
        public float left = 0.0f;
        public float top = 0.0f;

        /* renamed from: clone */
        public ScaleResult m1075clone() {
            ScaleResult scaleResult = new ScaleResult();
            scaleResult.hsWidth = this.hsWidth;
            scaleResult.hsLeft = this.hsLeft;
            scaleResult.hsHeight = this.hsHeight;
            scaleResult.hsTop = this.hsTop;
            scaleResult.hsImageInfo = this.hsImageInfo;
            scaleResult.hsBgInfo = this.hsBgInfo;
            scaleResult.scale = this.scale;
            scaleResult.left = this.left;
            scaleResult.top = this.top;
            return scaleResult;
        }

        public float getScale() {
            return this.scale;
        }

        public void setScale(float f) {
            this.scale = f;
        }

        public float getLeft() {
            return this.left;
        }

        public void setLeft(float f) {
            this.left = f;
        }

        public float getTop() {
            return this.top;
        }

        public void setTop(float f) {
            this.top = f;
        }

        public void setHistoryInfo(RectF rectF, SizeF sizeF) {
            this.hsBgInfo = sizeF;
            this.hsImageInfo = rectF;
            this.hsWidth = rectF.width() / sizeF.getWidth();
            this.hsLeft = rectF.left / sizeF.getWidth();
            this.hsHeight = rectF.height() / sizeF.getHeight();
            this.hsTop = rectF.top / sizeF.getHeight();
        }

        public float getTopScale() {
            float f = this.hsImageInfo.top;
            float height = this.hsBgInfo.getHeight() - this.hsImageInfo.bottom;
            if (f == 0.0f) {
                return 0.0f;
            }
            if (height != 0.0f) {
                return f / height;
            }
            return Float.MAX_VALUE;
        }

        public float getLeftScale() {
            float f = this.hsImageInfo.left;
            float width = this.hsBgInfo.getWidth() - this.hsImageInfo.right;
            if (f == 0.0f) {
                return 0.0f;
            }
            if (width != 0.0f) {
                return f / width;
            }
            return Float.MAX_VALUE;
        }
    }

    public ScaleResult resetImageWithBackground(RectF rectF, SizeF sizeF) {
        ScaleResult scaleResult = new ScaleResult();
        scaleResult.setScale(1.0f);
        scaleResult.setLeft(rectF.left);
        scaleResult.setTop(rectF.top);
        scaleResult.setHistoryInfo(rectF, sizeF);
        return scaleResult;
    }

    public ScaleResult resetImageWithBackground(RectF rectF, float f, SizeF sizeF) {
        if (f == 1.0f) {
            return resetImageWithBackground(rectF, sizeF);
        }
        float f2 = rectF.left;
        return resetImageWithBackground(new RectF(f2 / f, rectF.top / f, (f2 / f) + rectF.width(), (rectF.top / f) + rectF.height()), new SizeF(sizeF.getWidth() / f, sizeF.getHeight() / f));
    }

    public ScaleResult calcImagePosition(SizeF sizeF, ScaleResult scaleResult) {
        return calcImagePosition(sizeF, scaleResult, autoDirection(sizeF, scaleResult));
    }

    public final int autoDirection(SizeF sizeF, ScaleResult scaleResult) {
        return scaleResult.hsImageInfo.height() / (scaleResult.hsImageInfo.width() / (sizeF.getWidth() * scaleResult.hsWidth)) > sizeF.getHeight() ? 1 : 0;
    }

    public final ScaleResult calcImagePosition(SizeF sizeF, ScaleResult scaleResult, int i) {
        float height;
        float height2;
        float valueByScale;
        float f;
        if (i == 0) {
            height = sizeF.getWidth();
            height2 = scaleResult.hsBgInfo.getWidth();
        } else {
            height = sizeF.getHeight();
            height2 = scaleResult.hsBgInfo.getHeight();
        }
        float f2 = height / height2;
        scaleResult.setScale(f2);
        if (i == 0) {
            valueByScale = scaleResult.hsImageInfo.left * f2;
            f = getValueByScale(sizeF.getHeight(), f2, scaleResult.getTopScale(), scaleResult.hsImageInfo.top, scaleResult.hsImageInfo.height(), scaleResult.hsBgInfo.getHeight());
        } else {
            float f3 = scaleResult.hsImageInfo.top * f2;
            valueByScale = getValueByScale(sizeF.getWidth(), f2, scaleResult.getLeftScale(), scaleResult.hsImageInfo.left, scaleResult.hsImageInfo.width(), scaleResult.hsBgInfo.getWidth());
            f = f3;
        }
        scaleResult.setLeft(valueByScale);
        scaleResult.setTop(f);
        return scaleResult;
    }
}
