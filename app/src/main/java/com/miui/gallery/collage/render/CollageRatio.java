package com.miui.gallery.collage.render;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;

/* loaded from: classes.dex */
public enum CollageRatio {
    RATIO_3_4(R.drawable.collage_ratio_3_4, 0.75f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.collage_talkback_ratio_3_4)),
    RATIO_1_1(R.drawable.collage_ratio_1_1, 1.0f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.collage_talkback_ratio_1_1));
    
    public final int iconRes;
    public final float ratio;
    public final String talkbackName;

    CollageRatio(int i, float f, String str) {
        this.iconRes = i;
        this.ratio = f;
        this.talkbackName = str;
    }
}
