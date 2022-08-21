package com.miui.gallery.model;

import java.io.Serializable;

/* loaded from: classes2.dex */
public class FavoriteInfo implements Serializable {
    private boolean mIsFavorite;

    public boolean isFavorite() {
        return this.mIsFavorite;
    }

    public void setFavorite(boolean z) {
        this.mIsFavorite = z;
    }
}
