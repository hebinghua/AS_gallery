package com.miui.gallery.adapter.itemmodel.common;

import android.net.Uri;

/* loaded from: classes.dex */
public interface AlbumDetailInfoProvider {
    default String getContentDescription() {
        return null;
    }

    default String getCoverPath() {
        return null;
    }

    default long getCoverSize() {
        return 0L;
    }

    default Uri getCoverUri() {
        return null;
    }

    long getId();

    default Object getSource() {
        return null;
    }

    default CharSequence getSubTitle() {
        return null;
    }

    String getTitle();

    default boolean isMoreStyle() {
        return false;
    }

    default void set(int i, Object obj) {
    }
}
