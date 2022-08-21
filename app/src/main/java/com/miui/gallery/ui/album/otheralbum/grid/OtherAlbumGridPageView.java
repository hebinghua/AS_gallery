package com.miui.gallery.ui.album.otheralbum.grid;

import android.content.res.Configuration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.album.common.BaseAlbumPageView;
import com.miui.gallery.ui.album.common.HoldInconsistencyGridLayoutManager;
import com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.base.config.GridAlbumPageStyle;

/* loaded from: classes2.dex */
public class OtherAlbumGridPageView extends BaseAlbumPageView {
    public GridAlbumPageStyle mGridStyle;
    public GridLayoutManager mLayoutManager;

    public OtherAlbumGridPageView(BaseAlbumPageContract$V baseAlbumPageContract$V) {
        super(baseAlbumPageContract$V);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public void onInitRecyclerView(RecyclerView recyclerView) {
        this.mLayoutManager = new HoldInconsistencyGridLayoutManager(recyclerView.getContext(), getSpanCount());
        recyclerView.setClipToPadding(false);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = getAdapter().getSpanSizeLookup(this.mLayoutManager.getSpanCount());
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        this.mLayoutManager.setSpanSizeLookup(spanSizeLookup);
        recyclerView.setLayoutManager(this.mLayoutManager);
        this.mGridStyle = AlbumPageConfig.AlbumTabPage.getGridAlbumConfig();
        getParent().setRecyclerviewPadding(this.mGridStyle.getRecyclerViewMarginStart(), this.mGridStyle.getRecyclerViewMarginTop(), this.mGridStyle.getRecyclerViewMarginStart(), this.mGridStyle.getRecyclerViewMarginBottom());
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return AlbumPageConfig.AlbumTabPage.getGridAlbumConfig().getItemDecorations();
    }

    @Override // com.miui.gallery.ui.album.main.base.AbsAlbumPageView
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        AlbumPageConfig.getAlbumTabConfig().updateConfigResource(configuration);
        onSpanCountUpdate();
        getParent().setRecyclerviewPadding(this.mGridStyle.getRecyclerViewMarginStart(), this.mGridStyle.getRecyclerViewMarginTop(), this.mGridStyle.getRecyclerViewMarginStart(), this.mGridStyle.getRecyclerViewMarginBottom());
    }

    public void onSpanCountUpdate() {
        int spanCount = getSpanCount();
        getAdapter().setSpanCount(spanCount);
        ((GridLayoutManager) getRecyclerView().getLayoutManager()).setSpanCount(spanCount);
    }

    @Override // com.miui.gallery.ui.album.common.BaseAlbumPageView
    public RecyclerView.LayoutManager getLayoutManager() {
        return this.mLayoutManager;
    }

    public final int getSpanCount() {
        return AlbumPageConfig.AlbumTabPage.getGridAlbumConfig().getSpanCount(getActivity());
    }
}
