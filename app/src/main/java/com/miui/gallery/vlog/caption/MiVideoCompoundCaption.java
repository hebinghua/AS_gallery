package com.miui.gallery.vlog.caption;

import android.graphics.Bitmap;
import ch.qos.logback.core.CoreConstants;
import java.util.Map;

/* loaded from: classes2.dex */
public class MiVideoCompoundCaption {
    public int mBitmapHeight;
    public int mBitmapWidth;
    public Bitmap mCaptionBitmap;
    public long mDuration;
    public String mFirstLine;
    public long mInPoint;
    public String mJsonParamStr;
    public long mOutPoint;
    public Map<Integer, Float> mRatioToScaleMap;
    public Map<Integer, Float> mRatioToTransYMap;
    public double mRotation;
    public String mSecondLine;
    public float mTranslationX;

    public Bitmap getCaptionBitmap() {
        return this.mCaptionBitmap;
    }

    public void setCaptionBitmap(Bitmap bitmap) {
        this.mCaptionBitmap = bitmap;
    }

    public long getInPoint() {
        return this.mInPoint;
    }

    public void setInPoint(long j) {
        this.mInPoint = j;
    }

    public long getOutPoint() {
        return this.mOutPoint;
    }

    public void setOutPoint(long j) {
        this.mOutPoint = j;
    }

    public void setDuration(long j) {
        this.mDuration = j;
    }

    public String getFirstLine() {
        return this.mFirstLine;
    }

    public void setFirstLine(String str) {
        this.mFirstLine = str;
    }

    public String getSecondLine() {
        return this.mSecondLine;
    }

    public void setSecondLine(String str) {
        this.mSecondLine = str;
    }

    public void setRotation(double d) {
        this.mRotation = d;
    }

    public int getBitmapWidth() {
        return this.mBitmapWidth;
    }

    public void setBitmapWidth(int i) {
        this.mBitmapWidth = i;
    }

    public int getBitmapHeight() {
        return this.mBitmapHeight;
    }

    public void setBitmapHeight(int i) {
        this.mBitmapHeight = i;
    }

    public String getJsonParamStr() {
        return this.mJsonParamStr;
    }

    public void setJsonParamStr(String str) {
        this.mJsonParamStr = str;
    }

    public float getTranslationYByRatio(int i) {
        Map<Integer, Float> map = this.mRatioToTransYMap;
        if (map != null) {
            return map.get(Integer.valueOf(i)).floatValue();
        }
        return 0.0f;
    }

    public void setRatioToTransYMap(Map<Integer, Float> map) {
        this.mRatioToTransYMap = map;
    }

    public float getScaleByRatio(int i) {
        Map<Integer, Float> map = this.mRatioToScaleMap;
        if (map != null) {
            return map.get(Integer.valueOf(i)).floatValue();
        }
        return 1.0f;
    }

    public void setRatioToScaleMap(Map<Integer, Float> map) {
        this.mRatioToScaleMap = map;
    }

    public String toString() {
        return "MiVideoCompoundCaption{mInPoint=" + this.mInPoint + ", mOutPoint=" + this.mOutPoint + ", mDuration=" + this.mDuration + ", mFirstLine='" + this.mFirstLine + CoreConstants.SINGLE_QUOTE_CHAR + ", mSecondLine='" + this.mSecondLine + CoreConstants.SINGLE_QUOTE_CHAR + ", translationX=" + this.mTranslationX + ", rotation=" + this.mRotation + ", bitmapWidth=" + this.mBitmapWidth + ", bitmapHeight=" + this.mBitmapHeight + ", mJsonParamStr=" + this.mJsonParamStr + '}';
    }
}
