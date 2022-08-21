package com.miui.gallery.magic.matting.adapter;

import android.graphics.Color;

/* loaded from: classes2.dex */
public class StrokeIconItem extends IconItem {
    public int color;
    public int distance;
    public boolean empty;
    public float mMaxW;
    public String[] rainbow;
    public float screenScale;
    public float screenW;
    public int strokeWidth;
    public int style;
    public int type;

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(int i) {
        this.strokeWidth = i;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int i) {
        this.distance = i;
    }

    public int[] getRainbow() {
        String[] strArr = this.rainbow;
        if (strArr == null) {
            return null;
        }
        int[] iArr = new int[strArr.length];
        for (int length = strArr.length - 1; length >= 0; length--) {
            iArr[length] = Color.parseColor(this.rainbow[length]);
        }
        return iArr;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int i) {
        this.color = i;
    }

    public int getStyle() {
        return this.style;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void setEmpty(boolean z) {
        this.empty = z;
    }

    public float getmMaxW() {
        return this.mMaxW;
    }

    public float getScreenScale() {
        return this.screenScale;
    }

    public int getProgress() {
        float abs;
        if (this.type == 5) {
            abs = Math.abs((this.distance / this.mMaxW) * 100.0f);
        } else {
            abs = Math.abs((this.strokeWidth / this.mMaxW) * 100.0f);
        }
        return (int) abs;
    }

    public float getScreenW() {
        return this.screenW;
    }
}
