package com.miui.gallery.loader;

import android.net.Uri;
import com.miui.gallery.content.ExtendedCursorLoader;
import com.miui.gallery.model.HomeMedia;
import com.miui.gallery.provider.GalleryContract;

/* loaded from: classes2.dex */
public class HomeMediaLoader extends ExtendedCursorLoader {
    public static final Uri URI = GalleryContract.Media.URI.buildUpon().appendQueryParameter("generate_headers", String.valueOf(true)).appendQueryParameter("media_group_by", String.valueOf(7)).appendQueryParameter("extra_timeline_only_show_valid_location", String.valueOf(false)).build();
    public static final String[] PROJECTION = HomeMedia.Constants.PROJECTION;

    public static String getHomePageSelection(boolean z) {
        return z ? "sha1 NOT NULL AND alias_hidden = 0 AND alias_rubbish = 0 AND localGroupId != -1000" : "alias_show_in_homepage=1 AND alias_hidden = 0 AND alias_rubbish = 0";
    }
}
