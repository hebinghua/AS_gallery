package com.miui.gallery.provider.album.config;

import android.net.Uri;
import com.miui.gallery.provider.GalleryContract;
import java.util.Set;

/* loaded from: classes2.dex */
public class QueryUriConfig$Album {
    public static final Uri URI;
    public static final Uri URI_ALL;
    public static final Uri URI_ALL_EXCEPT_DELETED;
    public static final Uri URI_ALL_EXCEPT_RUBBISH;

    static {
        Uri build = GalleryContract.AUTHORITY_URI.buildUpon().appendPath("album").build();
        URI = build;
        Uri build2 = build.buildUpon().appendQueryParameter("param_join_all_virtual_album", "true").appendQueryParameter("join_share", "true").build();
        URI_ALL = build2;
        URI_ALL_EXCEPT_DELETED = build2.buildUpon().appendQueryParameter("param_query_all", "true").build();
        URI_ALL_EXCEPT_RUBBISH = build2.buildUpon().appendQueryParameter("param_exclude_rubbish_album", "true").build();
    }

    public static boolean isHaveAlbumQueryParam(Uri uri) {
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        int size = queryParameterNames.size();
        if (size > 0) {
            if (queryParameterNames.contains("distinct")) {
                size--;
            }
            if (queryParameterNames.contains("groupBy")) {
                size--;
            }
            if (queryParameterNames.contains("having")) {
                size--;
            }
            if (queryParameterNames.contains("limit")) {
                size--;
            }
            return size > 0;
        }
        return false;
    }
}
