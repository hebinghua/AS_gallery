package com.miui.gallery.collage.render;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;

/* loaded from: classes.dex */
public enum CollageMargin {
    NONE(R.drawable.collage_layout_l_to_none, 0.0f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.collage_talkback_margin_non)),
    SMALL(R.drawable.collage_layout_none_to_s, 4.0f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.collage_talkback_margin_small)),
    MEDIUM(R.drawable.collage_layout_s_to_m, 6.0f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.collage_talkback_margin_medium)),
    LARGE(R.drawable.collage_layout_m_to_l, 11.333f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.collage_talkback_margin_large));
    
    public final int iconRes;
    public final float marginSize;
    public final String talkbackName;

    CollageMargin(int i, float f, String str) {
        this.iconRes = i;
        this.marginSize = f;
        this.talkbackName = str;
    }
}
