package com.miui.gallery.editor.photo.app;

import android.graphics.RectF;

/* loaded from: classes2.dex */
public class WaterMaskData {
    public int mMaskType;
    public SubImage mSubImage = new SubImage();
    public CoverRecord mRecord = new CoverRecord();

    /* loaded from: classes2.dex */
    public static class CoverRecord {
        public float paddingX;
        public float paddingY;
        public boolean moved = false;
        public boolean framed = false;
        public RectF maskBitmapRect = new RectF();
        public RectF displayRect = new RectF();
        public RectF maskRect = new RectF();
    }

    public WaterMaskData(int i) {
        this.mMaskType = i;
    }

    public int getMaskType() {
        return this.mMaskType;
    }

    public CoverRecord getRecord() {
        return this.mRecord;
    }

    public SubImage getSubImage() {
        return this.mSubImage;
    }
}
