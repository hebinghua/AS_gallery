package com.miui.gallery.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.widget.SlideShowView;

/* loaded from: classes2.dex */
public class SlideShowViewAware extends ViewAware<SlideShowView> {
    public SlideShowViewAware(SlideShowView slideShowView) {
        super(slideShowView);
    }

    @Override // com.miui.gallery.glide.ViewAware
    public void setImageDrawableInto(Drawable drawable, SlideShowView slideShowView) {
        if (drawable == null) {
            return;
        }
        setImageBitmapInto(drawableToBitmap(drawable), slideShowView);
    }

    public final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        canvas.setBitmap(null);
        return createBitmap;
    }

    @Override // com.miui.gallery.glide.ViewAware
    public void setImageBitmapInto(Bitmap bitmap, SlideShowView slideShowView) {
        if (bitmap == null) {
            return;
        }
        slideShowView.next(bitmap, 0);
    }

    @Override // com.miui.gallery.glide.ViewAware, com.miui.gallery.glide.ImageAware
    public RequestManager getRequestManager() {
        return Glide.with(GalleryApp.sGetAndroidContext());
    }
}
