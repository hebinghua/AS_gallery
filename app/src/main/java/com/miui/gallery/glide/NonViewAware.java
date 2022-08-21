package com.miui.gallery.glide;

import android.util.Size;
import android.util.SparseArray;
import android.view.View;
import com.bumptech.glide.RequestManager;

/* loaded from: classes2.dex */
public class NonViewAware implements ImageAware {
    public Size mImageSize;
    public SparseArray<Object> mKeyedTags;

    @Override // com.miui.gallery.glide.ImageAware
    public RequestManager getRequestManager() {
        return null;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public View getWrappedView() {
        return null;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public boolean isCollected() {
        return false;
    }

    public NonViewAware(Size size) {
        this.mImageSize = size;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public int getWidth() {
        return this.mImageSize.getWidth();
    }

    @Override // com.miui.gallery.glide.ImageAware
    public int getHeight() {
        return this.mImageSize.getHeight();
    }

    public final void setKeyedTag(int i, Object obj) {
        if (this.mKeyedTags == null) {
            this.mKeyedTags = new SparseArray<>(2);
        }
        this.mKeyedTags.put(i, obj);
    }

    @Override // com.miui.gallery.glide.ImageAware
    public void setTag(int i, Object obj) {
        if ((i >>> 24) < 2) {
            throw new IllegalArgumentException("The key must be an application-specific resource id.");
        }
        setKeyedTag(i, obj);
    }

    @Override // com.miui.gallery.glide.ImageAware
    public Object getTag(int i) {
        SparseArray<Object> sparseArray = this.mKeyedTags;
        if (sparseArray != null) {
            return sparseArray.get(i);
        }
        return null;
    }
}
