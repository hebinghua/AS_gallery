package com.miui.gallery.ui.album.otheralbum.linear;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.album.common.BaseAlbumPageView;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;

/* loaded from: classes2.dex */
public class OtherAlbumLinearPageView extends BaseAlbumPageView {
    public LinearLayoutManager mLayoutManager;

    public OtherAlbumLinearPageView(BaseAlbumPageContract$V baseAlbumPageContract$V) {
        super(baseAlbumPageContract$V);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public void onInitRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext(), 1, false);
        this.mLayoutManager = linearLayoutManager;
        recyclerView.setLayoutManager(linearLayoutManager);
        getParent().setRecyclerviewPadding(0, 0, 0, AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getRecyclerViewMarginBottom());
        recyclerView.setClipToPadding(false);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getLinearAlbumConfig().getItemDecorations();
    }

    @Override // com.miui.gallery.ui.album.common.BaseAlbumPageView
    public RecyclerView.LayoutManager getLayoutManager() {
        return this.mLayoutManager;
    }
}
