package com.miui.gallery.widget.recyclerview;

import android.content.Context;

/* loaded from: classes3.dex */
public abstract class ProportionTagView<T> implements DrawView {
    public int mAlpha;
    public T mContent;
    public Context mContext;
    public int mPositionY;
    public int mTagMarginStart;
    public float mScaleX = 1.0f;
    public float mScaleY = 1.0f;
    public boolean mIsLayoutRTL = false;

    public ProportionTagView(Context context) {
        this.mContext = context;
    }

    public void setContent(T t) {
        this.mContent = t;
    }

    public int getPositionY() {
        return this.mPositionY;
    }

    public void setPositionY(int i) {
        this.mPositionY = i;
    }

    public int getTagMarginStart() {
        return this.mTagMarginStart;
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setScaleX(float f) {
        this.mScaleX = f;
    }

    public float getScaleX() {
        return this.mScaleX;
    }

    public void setScaleY(float f) {
        this.mScaleY = f;
    }

    public void setIsLayoutRTL(boolean z) {
        this.mIsLayoutRTL = z;
    }
}
