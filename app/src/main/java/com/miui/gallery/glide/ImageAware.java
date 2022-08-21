package com.miui.gallery.glide;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.bumptech.glide.RequestManager;

/* loaded from: classes2.dex */
public interface ImageAware {
    int getHeight();

    RequestManager getRequestManager();

    Object getTag(int i);

    int getWidth();

    View getWrappedView();

    boolean isCollected();

    boolean setImageBitmap(Bitmap bitmap);

    boolean setImageDrawable(Drawable drawable);

    void setTag(int i, Object obj);
}
