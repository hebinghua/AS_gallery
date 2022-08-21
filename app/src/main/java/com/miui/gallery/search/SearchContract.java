package com.miui.gallery.search;

import android.net.Uri;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes2.dex */
public class SearchContract {
    public static final Uri AUTHORITY_URI = Uri.parse("content://com.miui.gallery.search");

    /* loaded from: classes2.dex */
    public interface History {
        public static final Uri URI = SearchContract.AUTHORITY_URI.buildUpon().appendPath("history").build();
    }

    /* loaded from: classes2.dex */
    public interface Icon {
        public static final Uri URI = SearchContract.AUTHORITY_URI.buildUpon().appendPath(CallMethod.RESULT_ICON).build();
        public static final Uri PEOPLE_COVER_URI = Uri.parse("image://people");
        public static final Uri ALBUM_COVER_URI = Uri.parse("image://album");
        public static final Uri LOCAL_IMAGE_URI = Uri.parse("image://image");
        public static final Uri WEB_IMAGE_URI = Uri.parse("image://web");
    }
}
