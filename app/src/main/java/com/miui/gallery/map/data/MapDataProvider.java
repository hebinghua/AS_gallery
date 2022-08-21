package com.miui.gallery.map.data;

import android.net.Uri;
import com.miui.gallery.provider.GalleryContract;

/* loaded from: classes2.dex */
public class MapDataProvider {
    public static Uri MAP_ALBUM_COVERS;
    public static Uri PHOTOS_SHOW_ON_MAP;

    static {
        Uri uri = GalleryContract.Media.URI;
        PHOTOS_SHOW_ON_MAP = uri.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(false)).appendQueryParameter("remove_rubbish_items", String.valueOf(true)).appendQueryParameter("extra_timeline_only_show_valid_location", String.valueOf(false)).build();
        MAP_ALBUM_COVERS = uri.buildUpon().appendQueryParameter("remove_duplicate_items", String.valueOf(false)).appendQueryParameter("remove_rubbish_items", String.valueOf(true)).appendQueryParameter("extra_timeline_only_show_valid_location", String.valueOf(false)).appendQueryParameter("limit", String.valueOf(8)).build();
    }
}
