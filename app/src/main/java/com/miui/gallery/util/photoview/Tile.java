package com.miui.gallery.util.photoview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import java.lang.ref.WeakReference;
import java.util.Locale;

/* loaded from: classes2.dex */
public class Tile {
    public static Paint sPaint;
    public TileBit mBit;
    public int mColumn;
    public RectF mDisplayRect;
    public WeakReference<BitmapRecycleCallback> mRecycleCallbackRef;
    public int mRefreshId = -1;
    public int mRow;
    public int mSampleSize;
    public int mState;
    public Rect mTileRect;

    public Tile(Rect rect, RectF rectF, int i, BitmapRecycleCallback bitmapRecycleCallback) {
        this.mTileRect = rect;
        this.mDisplayRect = rectF;
        this.mSampleSize = i;
        this.mRecycleCallbackRef = new WeakReference<>(bitmapRecycleCallback);
        setActive(true);
    }

    public void setIndex(int i, int i2) {
        this.mRow = i;
        this.mColumn = i2;
    }

    public final void setActive(boolean z) {
        int i = this.mState & (-4);
        this.mState = i;
        if (z) {
            this.mState = i | 0;
        } else {
            this.mState = i | 2;
        }
    }

    public final boolean isContentValidate() {
        TileBit tileBit = this.mBit;
        return tileBit != null && tileBit.isContentValidate();
    }

    public final void setDecoded() {
        this.mState &= -13;
        if (isContentValidate()) {
            this.mState |= 4;
        } else {
            this.mState |= 8;
        }
    }

    public void updateDisplayParam(RectF rectF) {
        this.mDisplayRect.set(rectF);
        setActive(true);
    }

    public void updateTileParam(Rect rect, int i) {
        this.mTileRect.set(rect);
        this.mSampleSize = i;
    }

    public void setRefreshId(int i) {
        this.mRefreshId = i;
    }

    public int getRefreshId() {
        return this.mRefreshId;
    }

    public boolean decode(TileBitProvider tileBitProvider) {
        if (tileBitProvider != null) {
            this.mBit = tileBitProvider.getTileBit(this.mTileRect, this.mSampleSize);
        }
        setDecoded();
        return isContentValidate();
    }

    public final BitmapRecycleCallback getBitmapRecycleCallback() {
        WeakReference<BitmapRecycleCallback> weakReference = this.mRecycleCallbackRef;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public void recycle() {
        this.mBit.recycle(getBitmapRecycleCallback());
        this.mBit = null;
        setActive(false);
        setDecoded();
        this.mRefreshId = -1;
    }

    public static Paint getPaint() {
        if (sPaint == null) {
            sPaint = new Paint();
        }
        return sPaint;
    }

    public boolean draw(Canvas canvas, Paint paint) {
        if (isContentValidate()) {
            if (paint == null) {
                paint = getPaint();
            }
            float width = ((this.mDisplayRect.width() * this.mBit.getValidateWidth()) * this.mSampleSize) / this.mTileRect.width();
            RectF rectF = this.mDisplayRect;
            float f = rectF.left;
            float f2 = rectF.top;
            this.mBit.draw(canvas, new RectF(f, f2, width + f, (((this.mDisplayRect.height() * this.mBit.getValidateHeight()) * this.mSampleSize) / this.mTileRect.height()) + f2), paint);
            return true;
        }
        return false;
    }

    public Rect getTileRect() {
        return this.mTileRect;
    }

    public boolean isActive() {
        return (this.mState & 3) == 0;
    }

    public String toString() {
        return String.format(Locale.US, "tileRect %s, displayRect %s, sampleSize %d, state %d, refreshId %d", String.valueOf(this.mTileRect), String.valueOf(this.mDisplayRect), Integer.valueOf(this.mSampleSize), Integer.valueOf(this.mState), Integer.valueOf(this.mRefreshId));
    }
}
