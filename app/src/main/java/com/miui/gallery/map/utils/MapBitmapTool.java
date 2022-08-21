package com.miui.gallery.map.utils;

import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;

/* loaded from: classes2.dex */
public class MapBitmapTool {
    public static RequestOptions getMapItemOptions(int i) {
        return GlideOptions.microThumbOf().mo971override(i, i).centerCrop().mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo974priority(Priority.HIGH);
    }

    public static Bitmap getSourceBitmap(String str, int i) {
        return GlideLoadingUtils.blockingLoad(Glide.with(GalleryApp.sGetAndroidContext()), GalleryModel.of(str), getMapItemOptions(i));
    }
}
