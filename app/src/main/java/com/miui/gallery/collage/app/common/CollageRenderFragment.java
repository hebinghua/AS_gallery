package com.miui.gallery.collage.app.common;

import android.graphics.Bitmap;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.collage.CollageActivity;
import com.miui.gallery.collage.core.RenderData;
import java.util.HashMap;

/* loaded from: classes.dex */
public abstract class CollageRenderFragment extends AndroidFragment {
    public boolean mBitmapReady;
    public Bitmap[] mBitmaps;
    public CollageActivity.ReplaceImageListener mReplaceImageListener;

    public abstract void dismissControlWindow();

    public abstract RenderData export();

    public boolean isActivating() {
        return false;
    }

    public abstract void onBitmapReplace(Bitmap bitmap, Bitmap bitmap2);

    public abstract void onBitmapsReceive();

    public abstract HashMap<String, String> onSimple();

    public final void setBitmap(Bitmap[] bitmapArr) {
        if (bitmapArr == null || bitmapArr.length == 0) {
            return;
        }
        this.mBitmaps = bitmapArr;
        this.mBitmapReady = true;
        onBitmapsReceive();
    }

    public void setReplaceImageListener(CollageActivity.ReplaceImageListener replaceImageListener) {
        this.mReplaceImageListener = replaceImageListener;
    }
}
