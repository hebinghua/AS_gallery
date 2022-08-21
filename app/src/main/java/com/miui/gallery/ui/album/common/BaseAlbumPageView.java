package com.miui.gallery.ui.album.common;

import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V;
import com.miui.gallery.ui.album.main.base.BaseAlbumListPageView;

/* loaded from: classes2.dex */
public abstract class BaseAlbumPageView<V extends BaseAlbumPageContract$V> extends BaseAlbumListPageView {
    public abstract RecyclerView.LayoutManager getLayoutManager();

    public BaseAlbumPageView(V v) {
        super(v);
    }
}
